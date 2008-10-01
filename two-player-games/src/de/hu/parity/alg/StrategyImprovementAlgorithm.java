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
import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedSubgraph;
import org.jgrapht.graph.EdgeReversedGraph;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.jgrapht.graph.Subgraph;
import org.jgrapht.traverse.ClosestFirstIterator;

import de.hu.gralog.algorithm.Algorithm;
import de.hu.gralog.algorithm.InvalidPropertyValuesException;
import de.hu.gralog.algorithm.result.AlgorithmResult;
import de.hu.gralog.algorithm.result.AlgorithmResultContent;
import de.hu.gralog.algorithm.result.AlgorithmResultContentTreeNode;
import de.hu.gralog.algorithm.result.DisplaySubgraphMode;
import de.hu.gralog.algorithm.result.ElementTipsDisplayMode;
import de.hu.gralog.algorithm.result.DisplaySubgraph.DisplayMode;
import de.hu.gralog.algorithms.jgrapht.Algorithms;
import de.hu.gralog.algorithms.jgrapht.Filter;
import de.hu.gralog.app.UserException;
import de.hu.gralog.jgrapht.traverse.LongestPathIterator;
import de.hu.gralog.structure.Structure;
import de.hu.parity.structure.ParityGameVertex;

public class StrategyImprovementAlgorithm<V extends ParityGameVertex,E extends DefaultEdge, G extends ListenableDirectedGraph<V,E>> implements Algorithm {

	private static final String DSM_STRATEGY_PLAYER0 = "strategy player0";
	private static final String DSM_STRATEGY_PLAYER1 = "strategy player1";
	private static final String DSM_WINNING_POSITIONS = "winning positions player0";
	private static final String DSM_OMEGA= "w - the circle-vertex";
	private static final String DSM_CAN_REACH_OMEGA= "L - can reach w";
	private static final String DSM_VALUATION_GRAPH = "H - the valuation graph";
	
	private static final String ETM_VALUATION = "valuation";
	private static int ODD_INFINITY_PRIORITY = 100001;
	private static int EVEN_INFINITY_PRIORITY = 10000;
	
	private Structure<V,E,?,G> parityGameArena;

	public Structure<V,E,?,G> getParityGameArena() {
		return parityGameArena;
	}
	
	public void setParityGameArena( Structure<V,E,?,G> graph ) {
		this.parityGameArena = graph;
	}
	
	protected void fillContentValuation( AlgorithmResultContent content, Valuation valuation ) {
		if ( valuation != null )
			content.addElementTips( ETM_VALUATION, valuation.function );
	}

	protected void fillContentStrategyP0( AlgorithmResultContent content, Strategy<V,E> strategyPlayer0 ) {
		if ( strategyPlayer0 != null )
			content.addDisplaySubgraph( DSM_STRATEGY_PLAYER0, strategyPlayer0.getSubgraph().vertexSet(), strategyPlayer0.getSubgraph().edgeSet() );
	}

	protected void fillContentStrategyP1( AlgorithmResultContent content, Strategy<V,E> strategyPlayer1 ) {
		if ( strategyPlayer1 != null )
			content.addDisplaySubgraph( DSM_STRATEGY_PLAYER1, strategyPlayer1.getSubgraph().vertexSet(), strategyPlayer1.getSubgraph().edgeSet() );
	}
	
	protected void fillContentStrategies( AlgorithmResultContent content, Strategy<V,E> strategyPlayer0, Strategy<V,E> strategyPlayer1 ) {
		fillContentStrategyP0( content, strategyPlayer0 );
		fillContentStrategyP1( content, strategyPlayer1 );
	}

	protected void fillContentWinningSets( AlgorithmResultContent content, Valuation valuation ) {
		if ( valuation != null ) {
			DirectedSubgraph<V,E> winningPlayer0 = new DirectedSubgraph<V,E>( parityGameArena.getGraph(), new HashSet<V>(), new HashSet<E>() );
			Iterator<V> it = parityGameArena.getGraph().vertexSet().iterator();
			while ( it.hasNext() ) {
				V v = it.next();
		
				if ( valuation.getPlayProfile( v ).getU().getPriority() % 2 == 0 )
					winningPlayer0.addVertex( v );
			}
			content.addDisplaySubgraph( DSM_WINNING_POSITIONS, winningPlayer0.vertexSet(), winningPlayer0.edgeSet() );
		}
	}
	
	protected void fillContent( AlgorithmResultContent content, Strategy<V,E> strategyPlayer0, Strategy<V,E> strategyPlayer1, Valuation valuation, boolean winningSets ) {
		fillContentStrategies( content, strategyPlayer0, strategyPlayer1 );
		fillContentValuation( content, valuation );
		if ( winningSets )
			fillContentWinningSets( content, valuation );
	}

	protected void fillContent( AlgorithmResultContent content, V omega, Subgraph<V,E, DirectedGraph<V,E>> valuationGraph, Subgraph<V,E, DirectedGraph<V,E>> canReachOmega ) {
		Set<V> omegaSet = new HashSet<V>();
		omegaSet.add( omega );
		content.addDisplaySubgraph( DSM_OMEGA, omegaSet, new HashSet<E>() );
		content.addDisplaySubgraph( DSM_VALUATION_GRAPH, valuationGraph.vertexSet(), valuationGraph.edgeSet() );
		content.addDisplaySubgraph( DSM_CAN_REACH_OMEGA, canReachOmega.vertexSet(), canReachOmega.edgeSet() );
	}
	
	protected AlgorithmResultContentTreeNode createIterationContent( AlgorithmResultContentTreeNode contentTree, int iterationCount ) {
		AlgorithmResultContentTreeNode iterationContent = new AlgorithmResultContentTreeNode();
		iterationContent.setName( "Iteration: " + iterationCount );
		contentTree.addChild( iterationContent );
		return iterationContent;
	}
	
	protected void addCircleContent( AlgorithmResultContentTreeNode content, V omega, Subgraph<V,E, DirectedGraph<V,E>> valuationGraph, Subgraph<V,E, DirectedGraph<V,E>> canReachOmega ) {
		AlgorithmResultContentTreeNode circleContent = new AlgorithmResultContentTreeNode();
		circleContent.setName( "circle around: " + omega );
		fillContent( circleContent, omega, valuationGraph, canReachOmega );
		content.addChild( circleContent );
	}
	
	private AlgorithmResult result;
	
	private List<E> getInvalidEdges() {
		List<E> edges = new ArrayList<E>();
		for ( E edge : getParityGameArena().getGraph().edgeSet() ) {
			V source = getParityGameArena().getGraph().getEdgeSource( edge );
			V target = getParityGameArena().getGraph().getEdgeTarget( edge );

			if ( source.isPlayer0() == target.isPlayer0() )
				edges.add( edge );
		}
		
		return edges;
	}
	
	public AlgorithmResult execute(  ) throws InvalidPropertyValuesException {
		InvalidPropertyValuesException e = new InvalidPropertyValuesException();
		if ( getParityGameArena() == null )
			e.addPropertyError( "parityGameArena", InvalidPropertyValuesException.PROPERTY_REQUIRED );
		else {
			List<E> edges = getInvalidEdges();
			if ( edges.size() != 0 ) {
				StringBuffer message = new StringBuffer();
				message.append( "<html>" );
				message.append( "The following edges are not allowed: <br>" );
				message.append( "<ul>" );
				for ( E edge : edges )
					message.append( "<li>The edge from : " + getParityGameArena().getGraph().getEdgeSource( edge ) +
							" to: " + getParityGameArena().getGraph().getEdgeTarget( edge ) + "</li>");
				message.append( "</ul>" );
				message.append( "See the description of this algorithm for further information." );
				e.addPropertyError( "parityGameArena", message.toString() );
			}
		}
		if ( e.hasErrors() )
			throw e;
		result = new AlgorithmResult( getParityGameArena() );
		result.setDescription( 
				"<html> " +
				"The format of this result is leaned on the presentation " +
				"of the Discrete Strategy Improvement Algorithm (DSIA) " +
				"as it can be found in " +
				"<a href=\"http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.35.9141\">DSIA</a>. " +
				"The result is displayed as a tree, starting with the " +
				"final result of the algorithm (represent by the root of the tree)." +
				"The <i>i</i>-th iteration of the " +
				"DSIA is then represented by a child of this root " +
				"called <i>iteration: i</i>. " +
				"The children of an iteration-node represent the circles that " +
				"are found during the valuation-routine of the DSIA." +
				"</html>" );
		
		DisplaySubgraphMode displaySubgraphMode = new DisplaySubgraphMode();
		displaySubgraphMode.setEdgeDisplayMode( DisplayMode.HIGH1, DisplayMode.SHOW );
		result.addDisplaySubgraphMode( DSM_STRATEGY_PLAYER0, displaySubgraphMode );
		
		displaySubgraphMode = new DisplaySubgraphMode();
		displaySubgraphMode.setEdgeDisplayMode( DisplayMode.HIGH2, DisplayMode.SHOW );
		result.addDisplaySubgraphMode( DSM_STRATEGY_PLAYER1, displaySubgraphMode );

		displaySubgraphMode = new DisplaySubgraphMode();
		displaySubgraphMode.setVertexDisplayMode( DisplayMode.HIGH1, DisplayMode.HIGH2 );
		result.addDisplaySubgraphMode( DSM_WINNING_POSITIONS, displaySubgraphMode );

		displaySubgraphMode = new DisplaySubgraphMode();
		displaySubgraphMode.setVertexDisplayMode( DisplayMode.HIGH2, DisplayMode.SHOW );
		result.addDisplaySubgraphMode( DSM_OMEGA, displaySubgraphMode );
		
		displaySubgraphMode = new DisplaySubgraphMode();
		displaySubgraphMode.setVertexDisplayMode( DisplayMode.HIGH1, DisplayMode.SHOW );
		result.addDisplaySubgraphMode( DSM_CAN_REACH_OMEGA, displaySubgraphMode );
		
		displaySubgraphMode = new DisplaySubgraphMode();
		displaySubgraphMode.setEdgeDisplayMode( DisplayMode.HIGH1, DisplayMode.SHOW );
		result.addDisplaySubgraphMode( DSM_VALUATION_GRAPH, displaySubgraphMode );
		
		ElementTipsDisplayMode elementTipsDisplayMode = new ElementTipsDisplayMode();
		result.addElementTipsDisplayMode( ETM_VALUATION, elementTipsDisplayMode );
	
		for (V vertex : parityGameArena.getGraph().vertexSet() ) {
			if ( parityGameArena.getGraph().outDegreeOf( vertex ) == 0 ) {
				if ( vertex.isPlayer0() )
					vertex.setPriority( ODD_INFINITY_PRIORITY );
				else
					vertex.setPriority( EVEN_INFINITY_PRIORITY );
			}
		}
		
		Strategy<V,E>[] strategies = new Strategy[2]; 
		
		strategies[0] = new Strategy<V,E>( parityGameArena.getGraph(), true );
		strategies[1] = new Strategy<V,E>( parityGameArena.getGraph(), false );

		strategies[0].initRandom();
		
		Strategy<V,E> oldStrategy = null;
		AlgorithmResultContentTreeNode contentTree = new AlgorithmResultContentTreeNode(); 
		contentTree.setName( "RESULT" );
		
		int iterationCount = 0;
		Valuation valuation;
		do {
			AlgorithmResultContentTreeNode iterationContent = createIterationContent( contentTree, iterationCount );
			
			valuation = valuate( strategies[0].getSubgraphWithConcurrentEdges(), iterationContent );
			
			calculateStrategy( valuation, strategies[1] );
			
			try {
				for ( AlgorithmResultContent content : iterationContent.getChildren() )
					fillContentValuation( content, valuation );
			} catch ( UserException u ) {
				
			}
			
			fillContent( iterationContent, strategies[0], strategies[1], valuation, true );
			
			oldStrategy = new Strategy<V,E>( strategies[0] );
			calculateStrategy( valuation, strategies[0] );
			iterationCount++;
		} while ( ! ( oldStrategy.equals( strategies[0] ) ) && iterationCount <= 100 );
		
		fillContent( contentTree, strategies[0], strategies[1], valuation, true );
		
		result.setContentTree( contentTree );
		return result;
	}
	
	private void calculateStrategy( Valuation valuation, Strategy<V,E> strategy ) {
		Iterator<V> domain = strategy.getDomain().iterator();
		while ( domain.hasNext() ) {
			V vertex = domain.next();
			V toVertex = getOptimalStrategy( valuation, vertex );
			strategy.setValue( vertex, toVertex );
		}
	}
	
	public V getOptimalStrategy( Valuation valuation, V vertex ) {
		V value = null;
		
		Iterator<V> vertexes = Graphs.successorListOf( (DirectedGraph<V,E>)parityGameArena.getGraph(), vertex ).iterator();
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
			
			for ( V v : new HashSet<V>( s11 ) ) {
				if ( v.getPriority() < omega.getPriority() )
					s11.remove( v );
			}
			for ( V v : new HashSet<V>( s21 ) ) {
				if ( v.getPriority() < omega.getPriority() )
					s21.remove( v );
			}

			return lessThan( s11, s21 );
		}
		
		public static <V extends ParityGameVertex> boolean equalOmega( Set<V> s1, Set<V> s2, V omega ) {
			return ! lessThanOmega( s1, s2, omega ) && ! lessThanOmega( s2, s1, omega );
		}
		
		public static <V extends ParityGameVertex> boolean lessThan( PlayProfile<V> p1, PlayProfile<V> p2 ) {
			if ( lessThan( p1.getU(), p2.getU() ) ||
				 equal( p1.getU(), p2.getU() ) &&  
				 (lessThan( p1.getP(), p2.getP() ) ||
				 (equal( p1.getP(), p2.getP() ) && p2.getU().getPriority() % 2 == 1 && p1.getLen() < p2.getLen() ) ||
				 (equal( p1.getP(), p2.getP() ) && p2.getU().getPriority() % 2 == 0 && p1.getLen() > p2.getLen() )))
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
	
	public static class PlayProfile<V extends ParityGameVertex> {
		
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
			return getU().getLabel() + ", " + vertexSetToString( getP() ) + ", " + Integer.toString( getLen() );
		}
		
		private static <V extends ParityGameVertex> String vertexSetToString( Set<V> set ) {
			StringBuffer sb = new StringBuffer();
			sb.append( "[" );
			for ( V v : set )
				sb.append( v.getLabel() + ", " );
			if ( set.size() != 0 )
				sb.delete( sb.length() - 2, sb.length() );
			sb.append( "]" );
			return sb.toString();
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
	
	private Set<E> getEdges( Subgraph<V,E, DirectedGraph<V,E>> graph, Set<V> from, Set<V> to ) {
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
	
	private void removeEdges( DirectedSubgraph<V,E> graph, Set<V> from, Set<V> to ) {
		graph.removeAllEdges( getEdges( graph, from, to ) );
	}
	
	private Valuation valuate( DirectedSubgraph<V,E> graph, AlgorithmResultContentTreeNode iterationContent ) {
		Valuation valuation = new Valuation();
		Iterator<V> vertexes = getVertexesInRewardOrder( graph.vertexSet() );
		while ( vertexes.hasNext() ) {
			V vertex = vertexes.next();
			if ( valuation.getPlayProfile( vertex ) == null ) {
				boolean onLoopOrLeaf = graph.outDegreeOf( vertex ) == 0;
									
				if ( !onLoopOrLeaf ) {
					Set<V> canreachOmega = Algorithms.reach( graph, vertex, true, new PriorityVertexFilter<V>( vertex ) );
				
					HashSet<V> svertex = new HashSet<V>();
					svertex.add( vertex );
					onLoopOrLeaf = getEdges( graph, svertex, canreachOmega ).size() != 0; 
				}
				if ( onLoopOrLeaf ) {
					Set<V> canreach = Algorithms.reach( graph, vertex, true, null );

					addCircleContent( iterationContent, vertex, new DirectedSubgraph<V,E>( getParityGameArena().getGraph(), null, graph.edgeSet() ), new DirectedSubgraph<V,E>( graph, canreach, null ) );
					
					valuation.union( subValuate( new DirectedSubgraph<V,E>( graph, canreach, null), vertex ) );
					removeEdges( graph, canreach, null );
				} 
			}
		}
		return valuation;
	}
	
	private Valuation subValuate( DirectedSubgraph<V,E> k, V omega ) {
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
			DirectedSubgraph<V,E> subgraph = new DirectedSubgraph<V,E>( k, null, null );
			subgraph.removeAllEdges( subgraph.outgoingEdgesOf( omega ) );
			
			LongestPathIterator<V,E> it = new LongestPathIterator<V,E>( new EdgeReversedGraph<V,E>( subgraph ) );
			while ( it.hasNext()  ) {
				V vertex = it.next();
				int length = (int)it.getLongestPathLength( vertex );
				valuation.getPlayProfile( vertex ).setLen( length );
			}
		} else {
			ClosestFirstIterator<V,E> it = new ClosestFirstIterator<V,E>( new EdgeReversedGraph<V,E>( (DirectedGraph<V,E>)k ), omega );
			while ( it.hasNext() ) {
				V vertex = it.next();
				int length = (int)it.getShortestPathLength( vertex );
				valuation.getPlayProfile( vertex ).setLen( length );
			}
		}
		return valuation;
	}
	
	private Iterator<V> getSortedVertexesOmega( Set<V> vertexes, V omega ) {
		LinkedList<V> list = new LinkedList<V>( vertexes );
		for ( V v : new LinkedList<V>( list ) ) {
			if ( v.getPriority() <= omega.getPriority() )
				list.remove( v );
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

	private static class VertexVertexFilter<V> implements Filter<V> {
		V w;
		
		public VertexVertexFilter( V w ) {
			this.w = w;
		}
		
		public boolean filter( V w ) {
			if ( this.w == w )
				return true;
			return false;
		}
	}
	
	private static class PriorityVertexFilter<V extends ParityGameVertex> implements Filter<V> {

		V w;
		
		public PriorityVertexFilter( V w) {
			this.w = w;
		}
		
		public boolean filter(V v) {
			if ( v.getPriority() > w.getPriority() )
				return true;
			return false;
		}
		
	}

}
