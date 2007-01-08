/*
 * Created on 2006 by Sebastian Ordyniak
 *
 * Copyright 2006 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of Games.
 *
 * Games is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * Games is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Games; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */

package de.hu.parity.alg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedSubgraph;
import org.jgrapht.graph.Subgraph;
import org.jgrapht.traverse.ClosestFirstIterator;

import de.hu.gralog.graph.DirectedGraph;
import de.hu.gralog.graph.alg.Algorithm;
import de.hu.gralog.graph.alg.AlgorithmResult;
import de.hu.gralog.graph.alg.AlgorithmResultContent;
import de.hu.gralog.graph.alg.Algorithms;
import de.hu.gralog.graph.alg.InvalidPropertyValuesException;
import de.hu.gralog.jgrapht.graph.DisplaySubgraphMode;
import de.hu.gralog.jgrapht.graph.ElementTipsDisplayMode;
import de.hu.gralog.jgrapht.graph.GraphUtils;
import de.hu.gralog.jgrapht.graph.ReversedDirectedGraph;
import de.hu.gralog.jgrapht.graph.SubgraphFactory;
import de.hu.gralog.jgrapht.graph.DisplaySubgraph.DisplayMode;
import de.hu.gralog.jgrapht.traverse.LongestPathIterator;
import de.hu.gralog.jgrapht.traverse.VertexFilter;
import de.hu.parity.graph.ParityGameVertex;

public class StrategyImprovementAlgorithm<V extends ParityGameVertex,E extends DefaultEdge> implements Algorithm {

	private static final String DSM_STRATEGY_PLAYER0 = "strategy player0";
	private static final String DSM_STRATEGY_PLAYER1 = "strategy player1";
	private static final String DSM_WINNING_POSITIONS = "winning positions player0";
	private static final String ETM_VALUATION = "valuation";
	
	private DirectedGraph<V,E> graph;

	public DirectedGraph<V,E> getGraph() {
		return graph;
	}
	
	public void setGraph( DirectedGraph<V,E> graph ) {
		this.graph = graph;
	}

	protected AlgorithmResultContent getContent( Strategy<V,E> strategyPlayer0, Strategy<V,E> strategyPlayer1, Valuation valuation ) {
		AlgorithmResultContent content = new AlgorithmResultContent(  );
		
		if ( strategyPlayer0 != null )
			content.addDisplaySubgraph( DSM_STRATEGY_PLAYER0, strategyPlayer0.getSubgraph() );
		
		if ( strategyPlayer1 != null )
			content.addDisplaySubgraph( DSM_STRATEGY_PLAYER1, strategyPlayer1.getSubgraph() );
		
		if ( valuation != null ) {
			content.addElementTips( ETM_VALUATION, valuation.function );
			
			DirectedSubgraph<V,E> winningPlayer0 = new DirectedSubgraph<V,E>( graph, new HashSet<V>(), new HashSet<E>() );
			Iterator<V> it = graph.vertexSet().iterator();
			while ( it.hasNext() ) {
				V v = it.next();
				if ( valuation.getPlayProfile( v ).getU().getPriority() % 2 == 0 )
					winningPlayer0.addVertex( v );
			}
			content.addDisplaySubgraph( DSM_WINNING_POSITIONS, winningPlayer0 );
		}
		return content;
	}
	
	public AlgorithmResult execute(  ) throws InvalidPropertyValuesException {
		if ( getGraph() == null )
			throw new InvalidPropertyValuesException( "graph", InvalidPropertyValuesException.PROPERTY_REQUIRED );
		
		AlgorithmResult result = new AlgorithmResult( getGraph() );
		result.setDescription( "<html>Hallo Du!</html>" );
		
		DisplaySubgraphMode displaySubgraphMode = new DisplaySubgraphMode();
		displaySubgraphMode.setEdgeDisplayMode( DisplayMode.HIGH1, DisplayMode.SHOW );
		result.addDisplaySubgraphMode( DSM_STRATEGY_PLAYER0, displaySubgraphMode );
		
		displaySubgraphMode = new DisplaySubgraphMode();
		displaySubgraphMode.setEdgeDisplayMode( DisplayMode.HIGH2, DisplayMode.SHOW );
		result.addDisplaySubgraphMode( DSM_STRATEGY_PLAYER1, displaySubgraphMode );

		displaySubgraphMode = new DisplaySubgraphMode();
		displaySubgraphMode.setVertexDisplayMode( DisplayMode.HIGH1, DisplayMode.HIGH2 );
		result.addDisplaySubgraphMode( DSM_WINNING_POSITIONS, displaySubgraphMode );

		ElementTipsDisplayMode elementTipsDisplayMode = new ElementTipsDisplayMode();
		result.addElementTipsDisplayMode( ETM_VALUATION, elementTipsDisplayMode );
		
		Strategy<V,E>[] strategies = new Strategy[2]; 
		
		strategies[0] = new Strategy<V,E>( graph, GraphUtils.filterVertexes( graph.vertexSet(), new PlayerVertexFilter<V>( true ) ) );
		strategies[1] = new Strategy<V,E>( graph, GraphUtils.filterVertexes( graph.vertexSet(), new PlayerVertexFilter<V>( false ) ) );

		strategies[0].initRandom();

		Strategy<V,E>[] oldStrategies = new Strategy[2];
		oldStrategies[0] = new Strategy<V,E>( strategies[0] );
		Valuation valuation = null;
		
		int strategyIdx = -1;
		ArrayList<AlgorithmResultContent> contents = new ArrayList<AlgorithmResultContent>();
		do {
			if ( strategyIdx == -1 ) {
				contents.add( getContent( strategies[0], null, valuation ) );
				strategyIdx = 0;
			} else 
				contents.add( getContent( strategies[0], strategies[1], valuation ) );
			
			oldStrategies[(strategyIdx +1 ) % 2] = new Strategy<V,E>( strategies[(strategyIdx +1 ) % 2] );
			valuation = valuate( strategies[strategyIdx].getSubgraphWithConcurrentEdges() );
			strategyIdx = ( strategyIdx + 1 ) % 2;
			calculateStrategy( valuation, strategies[strategyIdx] );
		} while ( ! ( oldStrategies[0].equals( strategies[0] ) ) || ! ( oldStrategies[1].equals( strategies[1] ) ));
		result.setContentList( contents );
		return result;
	}
	
	private void calculateStrategy( Valuation valuation, Strategy<V,E> strategy ) {
		Iterator<V> domain = strategy.getDomain().iterator();
		while ( domain.hasNext() ) {
			V vertex = domain.next();
			strategy.setValue( vertex, getOptimalStrategy( valuation, vertex ) );
		}
	}
	
	public V getOptimalStrategy( Valuation valuation, V vertex ) {
		V value = null;
		
		Iterator<V> vertexes = Graphs.successorListOf( graph, vertex ).iterator();
		while ( vertexes.hasNext() ) {
			V target = vertexes.next();
			if ( vertex.isPlayer0() ) {
				if ( value == null || RewardOrder.lessThan( valuation.getPlayProfile( value ), valuation.getPlayProfile( target )))
					value = target;
			} else {
				if ( value == null || RewardOrder.lessThan( valuation.getPlayProfile( target ), valuation.getPlayProfile( value )))
					value = target;
			}
		}
		return value;
	}
	
	private static class RewardOrder {
		
		public static <V extends ParityGameVertex> boolean lessThan( V u, V v ) {
			if ( u.getPriority() < v.getPriority() && v.getPriority() % 2 == 0 ||
					v.getPriority() < u.getPriority() && u.getPriority() % 2 == 1 )
				return true;
			return false;
		}
		
		public static <V extends ParityGameVertex> boolean equal( V u, V v ) {
			return u.getPriority() == v.getPriority();
		}
		
		public static <V extends ParityGameVertex> boolean lessThan( Set<V> s1, Set<V> s2 ) {
			if ( s1.equals( s2 ) )
				return false;
			Set<V> intersection = new HashSet<V>( s1 );
			intersection.retainAll( s2 );
			
			Set<V> symetricdifference = new HashSet<V>( s1 );
			symetricdifference.addAll( s2 );
			symetricdifference.removeAll( intersection );

			V max = null;
			Iterator<V> it = symetricdifference.iterator();
			while ( it.hasNext() ) {
				V curr = it.next();
				if ( max == null || max.getPriority() < curr.getPriority() )
					max = curr;
			}
			if ( max.getPriority() % 2 == 0 ) {
				if ( s1.contains( max ) )
					return false;
				return true;
			}
			if ( s1.contains( max ) )
				return true;
			return false;
		}
		
		public static <V extends ParityGameVertex> boolean equal( Set<V> s1, Set<V> s2 ) {
			return s1.equals( s2 );
		}
		
		public static <V extends ParityGameVertex> boolean lessThanOmega( Set<V> s1, Set<V> s2, V omega ) {
			Set<V> s11 = new HashSet<V>( s1 );
			Set<V> s21 = new HashSet<V>( s2 );
			Iterator<V> it = s11.iterator();
			while ( it.hasNext() ) {
				if ( (it.next()).getPriority() < omega.getPriority() )
					it.remove();
			}
			it = s21.iterator();
			while ( it.hasNext() ) {
				if ( (it.next()).getPriority() < omega.getPriority() )
					it.remove();
			}
			return lessThan( s11, s21 );
		}
		
		public static <V extends ParityGameVertex> boolean equalOmega( Set<V> s1, Set<V> s2, V omega ) {
			return ! lessThanOmega( s1, s2, omega ) && ! lessThanOmega( s2, s1, omega );
		}
		
		public static <V extends ParityGameVertex> boolean lessThan( PlayProfile<V> p1, PlayProfile<V> p2 ) {
			if ( lessThan( p1.getU(), p2.getU() ) ||
				 equal( p1.getU(), p2.getU() ) && ( 
				 (lessThan( p1.getP(), p2.getP()) ||
				 (equal( p1.getP(), p2.getP()) && p2.getU().getPriority() % 2 == 1 && p1.getLen() < p2.getLen()) ||
				 (equal( p1.getP(), p2.getP()) && p2.getU().getPriority() % 2 == 0 && p1.getLen() > p2.getLen() ))))
				return true;
			return false;
		}
		
		public static <V extends ParityGameVertex> boolean lessThanOmega( PlayProfile<V> p1, PlayProfile<V> p2, V omega ) {
			if ( lessThan( p1.getU(), p2.getU() ) ||
				 equal( p1.getU(), p2.getU() ) && lessThanOmega( p1.getP(), p2.getP(), omega ) )
				return true;
			return false;
		}
		
		public static <V extends ParityGameVertex> boolean equalOmega( PlayProfile<V> p1, PlayProfile<V> p2, V omega ) {
			if ( lessThanOmega( p1, p2, omega ) )
				return false;
			if ( lessThanOmega( p2, p1, omega ) )
				return false;
			return true;
		}
	}
	
	public static class PlayProfile<V> {
		
		private V u;
		private Set<V> p;
		private int len;

		public PlayProfile( ) {
			
		}
		
		public PlayProfile( V u, Set<V> p, int len ) {
			this.u = u;
			this.p = p;
			this.len = len;
		}

		
		public void setP(Set<V> p) {
			this.p = p;
		}

		public void setU(V u) {
			this.u = u;
		}

		public int getLen() {
			return len;
		}
		
		public void setLen( int len ) {
			this.len = len;
		}

		public Set<V> getP() {
			return p;
		}

		public V getU() {
			return u;
		}
		
		public String toString() {
			return getU() + ":" + getP() + ":" + Integer.toString( getLen() );
		}
		
	}
	
	private class Valuation {
		
		private Hashtable<V, PlayProfile<V>> function = new Hashtable<V, PlayProfile<V>>();

		public void setPlayProfile( V x, PlayProfile<V> y )  {
			function.put( x, y );
		}
		
		public PlayProfile<V> getPlayProfile( V x ) {
			return function.get( x );
		}
		
		public void union( Valuation valuation ) {
			function.putAll( valuation.function );
		}
	}

	private Iterator<V> getVertexesInRewardOrder(Set<V> vertexes) {
		LinkedList<V> list = new LinkedList<V>( vertexes );
		Collections.sort( list, new Comparator<V>() {
			public int compare( V o1, V o2 ) {
				if ( RewardOrder.lessThan( o1, o2 ) )
					return -1;
				if ( RewardOrder.equal( o1, o2 ) ) {
					if ( o1.hashCode() < o2.hashCode() )
						return -1;
					return 0;
				}
				return 1;
			}
		});
		return list.iterator();
	}
	
	private Set<E> getEdges( Subgraph<V,E> graph, Set<V> from, Set<V> to ) {
		HashSet<E> remove = new HashSet<E>();
		Iterator<E> edges = graph.edgeSet().iterator();
		while ( edges.hasNext() ) {
			E edge = edges.next();
			V source = graph.getEdgeSource( edge );
			V target = graph.getEdgeTarget( edge );
			if ( from.contains( source ) ) {
				if ( to != null && to.contains( target ) )
					remove.add( edge );
				if ( to == null && ! from.contains( target ) )
					remove.add( edge );
			}
		}
		return remove;
	}
	
	private void removeEdges( Subgraph<V,E> graph, Set<V> from, Set<V> to ) {
		graph.removeAllEdges( getEdges( graph, from, to ) );
	}
	
	private Valuation valuate( Subgraph<V,E> graph ) {
		Valuation valuation = new Valuation();
		Iterator<V> vertexes = getVertexesInRewardOrder( graph.vertexSet() );
		while ( vertexes.hasNext() ) {
			V vertex = vertexes.next();
			if ( valuation.getPlayProfile( vertex ) == null ) {
				Set<V> canreachOmega = Algorithms.reach( graph, vertex, true, new PriorityVertexFilter<V>( vertex ) );
				
				HashSet<V> svertex = new HashSet<V>();
				svertex.add( vertex );
				if ( getEdges( graph, svertex, canreachOmega ).size() != 0 ) {
					Set<V> canreach = Algorithms.reach( graph, vertex, true, null );
					valuation.union( subValuate( SubgraphFactory.createSubgraph( graph, canreach, null), vertex ) );
					removeEdges( graph, canreach, null );
				}
			}
		}
		return valuation;
	}
	
	private Valuation subValuate( Subgraph<V,E> k, V omega ) {
		Valuation valuation = new Valuation();
		Iterator<V> vertexes = k.vertexSet().iterator();
		while ( vertexes.hasNext() ) {
			V vertex = vertexes.next();
			valuation.setPlayProfile( vertex, new PlayProfile<V>( omega, new HashSet<V>(), 0) );
		}
		
		vertexes = getSortedVertexesOmega( k.vertexSet(), omega );
		while ( vertexes.hasNext() ) {
			V vertex = vertexes.next();
			if ( vertex.getPriority() > omega.getPriority() ) {
				if ( vertex.getPriority() % 2 == 0 ) {
					Set<V> canReach = Algorithms.reach( k, omega, true, new VertexVertexFilter<V>( vertex ) );
					Iterator<V> it = k.vertexSet().iterator();
					while ( it.hasNext() ) {
						V v = it.next();
						if ( ! canReach.contains( v ) ) 
							valuation.getPlayProfile( v ).getP().add( vertex );
					}
					HashSet<V> to = new HashSet<V>( k.vertexSet() );
					to.removeAll( canReach );
					canReach.add( vertex );
					removeEdges( k, canReach, to );
				}
				else {
					Set<V> canReach = Algorithms.reach( k, vertex, true, new VertexVertexFilter<V>( omega ) );
					Iterator<V> it = canReach.iterator();
					while ( it.hasNext() ) {
						V v = it.next();
						valuation.getPlayProfile( v ).getP().add( vertex );
					}
					HashSet<V> to = new HashSet<V>( k.vertexSet() );
					to.removeAll( canReach );
					canReach.remove( vertex );
					removeEdges( k, canReach, to );
				}
			}
		}
		if ( omega.getPriority() % 2 == 0 ) {
			LongestPathIterator<V,E> it = new LongestPathIterator<V,E>( (DirectedSubgraph<V,E>)k, omega );
			while ( it.hasNext() ) {
				V vertex = it.next();
				valuation.getPlayProfile( vertex ).setLen( (int)it.getLongestPathLength( vertex ) );
			}
		} else {
			ClosestFirstIterator<V,E> it = new ClosestFirstIterator<V,E>( new ReversedDirectedGraph<V,E>( (DirectedSubgraph<V,E>)k ), omega );
			while ( it.hasNext() ) {
				V vertex = it.next();
				valuation.getPlayProfile( vertex ).setLen( (int)it.getShortestPathLength( vertex ) );
			}
		}
		return valuation;
	}
	
	private Iterator<V> getSortedVertexesOmega( Set<V> vertexes, V omega ) {
		LinkedList<V> list = new LinkedList<V>( vertexes );
		Iterator<V> it = list.iterator();
		while ( it.hasNext() ) {
			V v = it.next();
			if ( v.getPriority() <= omega.getPriority() )
				it.remove();
		}
		Collections.sort( list, new Comparator<V>() {
			public int compare(V o1, V o2) {
				if ( o1.getPriority() < o2.getPriority() )
					return 1;
				if ( o1.getPriority() == o2.getPriority() )
					return 0;
				return -1;
			}
		});
		return list.iterator();
	}

	private static class VertexVertexFilter<V> implements VertexFilter<V> {
		V w;
		
		public VertexVertexFilter( V w ) {
			this.w = w;
		}
		
		public boolean filterVertex( V w ) {
			if ( this.w == w )
				return true;
			return false;
		}
	}
	
	private static class PriorityVertexFilter<V extends ParityGameVertex> implements VertexFilter<V> {

		V w;
		
		public PriorityVertexFilter( V w) {
			this.w = w;
		}
		
		public boolean filterVertex(V v) {
			if ( v.getPriority() > w.getPriority() )
				return true;
			return false;
		}
		
	}

}
