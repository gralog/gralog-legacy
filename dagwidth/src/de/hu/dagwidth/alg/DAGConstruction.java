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

package de.hu.dagwidth.alg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;

import de.hu.dagwidth.alg.CopsAndRobberAlgorithm.CopsAndRobberVertex;
import de.hu.dagwidth.alg.CopsAndRobberAlgorithm.DummyVertexFilter;
import de.hu.games.graph.DirectedGraph;
import de.hu.games.graph.LabeledDirectedGraphTypeInfo;
import de.hu.games.graph.LabeledGraphVertex;
import de.hu.games.graph.alg.Algorithm;
import de.hu.games.graph.alg.AlgorithmResult;
import de.hu.games.graph.alg.AlgorithmResultContent;
import de.hu.games.graph.alg.Algorithms;
import de.hu.games.graph.alg.InvalidPropertyValuesException;
import de.hu.games.jgrapht.graph.GraphUtils;
import de.hu.games.jgrapht.traverse.VertexFilter;
import de.hu.graphgames.alg.Simple2PlayerGameAlgorithm;

public class DAGConstruction<V extends LabeledGraphVertex,E extends DefaultEdge> extends Algorithm {

	private DirectedGraph<V, E> graph;
	private int dagWidth = 0;
	private int maxDAGsCount = 1;
	
	public void setMaxDAGsCount( int countDAGs ) {
		this.maxDAGsCount = countDAGs;
	}
	
	public int getMaxDAGsCount() {
		return maxDAGsCount;
	}
	
	public int getDagWidth() {
		return dagWidth;
	}

	public void setDagWidth(int dagWidth) {
		this.dagWidth = dagWidth;
	}

	public DirectedGraph<V, E> getGraph() {
		return graph;
	}

	public void setGraph(DirectedGraph<V, E> graph) {
		this.graph = graph;
	}

	
	public AlgorithmResult execute(  ) throws InvalidPropertyValuesException {
		if ( getGraph() == null )
			throw new InvalidPropertyValuesException( "graph", InvalidPropertyValuesException.PROPERTY_REQUIRED );
		
		if ( graph.createVertex() instanceof CopsAndRobberVertex ) {
			AlgorithmResult result = new AlgorithmResult();
			result.setSingleContent( new AlgorithmResultContent( getDirectedGraphFromDAG( getDAG( (DirectedGraph<CopsAndRobberVertex, E>) graph ) ) ) );
			return result;
		}
		
		InvalidPropertyValuesException e = new InvalidPropertyValuesException();
		
		if ( getGraph() == null )
			e.addPropertyError( "graph",  InvalidPropertyValuesException.PROPERTY_REQUIRED );
		if ( getDagWidth() < 0 )
			e.addPropertyError( "dagWidth",  InvalidPropertyValuesException.GREATER_EQUAL_ZERO );
		if ( getMaxDAGsCount() <= 0 )
			e.addPropertyError( "dagWidth",  InvalidPropertyValuesException.GREATER_ZERO );

		if ( e.hasErrors() )
			throw e;
		
		AlgorithmResult result = new AlgorithmResult( );
		
		int currWidth = getDagWidth();
		if ( currWidth == 0 )
			currWidth++;
		ArrayList<AlgorithmResultContent> contents = new ArrayList<AlgorithmResultContent>();
		do {
			Iterator<DirectedGraph<CopsAndRobberVertex<V>, DefaultEdge>> it = CopsAndRobberAlgorithm.getCopsAndRobberGameGraphs( graph, currWidth++, true, true );
			while ( it.hasNext() ) {
				DirectedGraph<DAGVertex, DefaultEdge> outGraph = getDAG( it.next() );
				
				if ( outGraph != null ) {
					contents.add( new AlgorithmResultContent( getDirectedGraphFromDAG( outGraph ) ) );
					if ( contents.size() == getMaxDAGsCount() )
						break;
				}
			}	
		} while ( contents.size() == 0 && getDagWidth() == 0 );
		result.setContentList( contents );
		return result;
	}
	
	public DirectedGraph<LabeledGraphVertex, DefaultEdge> getDirectedGraphFromDAG( DirectedGraph<DAGVertex, DefaultEdge> dag ) {
		DirectedGraph<LabeledGraphVertex, DefaultEdge> graph = (DirectedGraph<LabeledGraphVertex, DefaultEdge>)new LabeledDirectedGraphTypeInfo().newInstance();
		Hashtable<DAGVertex, LabeledGraphVertex> vertexes = new Hashtable<DAGVertex, LabeledGraphVertex>();
		for ( DAGVertex vertex : dag.vertexSet() ) {
			LabeledGraphVertex v = new LabeledGraphVertex( vertex.getLabel() );
			vertexes.put( vertex, v );
			graph.addVertex( v );
		}
		for ( DefaultEdge edge : dag.edgeSet() ) {
			LabeledGraphVertex from = vertexes.get( dag.getEdgeSource( edge ) );
			LabeledGraphVertex to = vertexes.get( dag.getEdgeTarget( edge ) );
			graph.addEdge( from, to );
		}
		return graph;
	}
	
	private Hashtable<V, Set<V>> getMinDivision( Set<V> vertexes, Set<V> seperator, V previous ) {
		Hashtable<V, Set<V>> division = new Hashtable<V, Set<V>>();
		Set<V> notReachable = new HashSet<V>( vertexes );
		notReachable.removeAll( seperator );
		while ( notReachable.size() != 0 ) {
			V v = notReachable.iterator().next();
			Set<V> reachableV;
			Set<V> reachV;
			Set<V> reachVCopy;
			do {
			    reachableV = Algorithms.reach( graph, v, false, new VertexVertexFilter<V>( seperator ) );
				reachV = Algorithms.reach( graph, v, true, new VertexVertexFilter<V>( seperator ) );
				reachVCopy = new HashSet<V>( reachV );
				reachV.removeAll( reachableV );
				reachV.retainAll( vertexes );
				if ( ! reachV.isEmpty() )
					v = reachV.iterator().next();
			} while (  !reachV.isEmpty() );
			if ( previous != null ) {
				reachVCopy.retainAll( reachableV );
				reachVCopy.retainAll( vertexes );
				Iterator<E> edges = graph.outgoingEdgesOf( previous ).iterator();
				while ( edges.hasNext() ) {
					V o = graph.getEdgeTarget( edges.next() );
					if ( reachVCopy.contains( o ) ) {
						v = o;
						break;
					}
				}
			}
			division.put( v , reachableV );
			notReachable.removeAll( reachableV );
		}
		return division;
	}
	
	public <VI extends CopsAndRobberVertex<V>, EI extends DefaultEdge> DirectedGraph<DAGVertex, DefaultEdge> getDAG( DirectedGraph<VI, EI> gameGraph ) {
		Set<VI> winningPlayer1 = new HashSet<VI>( new Simple2PlayerGameAlgorithm<VI,EI>( gameGraph ).execute() );
		
		Set<VI> winningStartVertexes = new HashSet<VI>( GraphUtils.filterVertexes( gameGraph.vertexSet(), new DummyVertexFilter<VI>() ));
		winningStartVertexes.removeAll( winningPlayer1 );
		
		if ( winningStartVertexes.size()  == 0 )
			return null;
		
		VI start = winningStartVertexes.iterator().next();
		
		DirectedGraph<DAGVertex, DefaultEdge> outGraph = new DirectedGraph<DAGVertex, DefaultEdge>( DAGVertex.class );
		
		LinkedList<VI> queue = new LinkedList<VI>();
		queue.add( start );
		
		Hashtable<VI, Set<V>> reachableHash = new Hashtable<VI, Set<V>>();
		reachableHash.put( start, graph.vertexSet() );
		
		outGraph.addVertex( new DAGVertex( start ) );
		
		while ( queue.size() != 0 ) {
			VI v = queue.removeFirst();
			DAGVertex vd = new DAGVertex( v );
			
			Set<V> reachable = reachableHash.get( v );
			
			Hashtable<V, Set<V>> division;
			Set<V> seperator = new HashSet<V>( v.getX2() );

			division = getMinDivision( reachable, seperator, v.getRobber() );
			
			Iterator<EI> edges = gameGraph.outgoingEdgesOf( v ).iterator();
			while ( edges.hasNext() ) {
				VI v1 = gameGraph.getEdgeTarget( edges.next() );
				
				if ( division.containsKey( v1.getRobber() ) ) {
					Iterator<EI> edgesv1 = gameGraph.outgoingEdgesOf( v1 ).iterator();
					VI v2 = null;
					while ( edgesv1.hasNext() ) {
						VI v2h = gameGraph.getEdgeTarget( edgesv1.next() );
						if ( ! winningPlayer1.contains( v2h ) ) {
							v2 = v2h;
							if ( v2.getAdded() == v2.getRobber() )
								break;
						}
					}
					DAGVertex v2d = new DAGVertex( v2 );
					if ( ! outGraph.containsVertex( v2d ) )
						outGraph.addVertex( v2d );
					outGraph.addEdge( vd, v2d );
					queue.addLast( v2 );
					reachable = division.get( v2.getRobber() );
					reachable.remove( v2.getAdded() );
					reachableHash.put( v2, reachable );
				}
			}
		}
		
		return outGraph;
	}
	
	public static class VertexVertexFilter<V> implements VertexFilter<V> {

		private Set<V> seperator;
		
		public VertexVertexFilter( Set<V> seperator ) {
			this.seperator = seperator;
		}
		
		public boolean filterVertex(V vertex) {
			if ( seperator.contains( vertex ) )
				return true;
			return false;
		}
		
	}
	
	public static class Player0WinningVertexFilter implements VertexFilter {
		private Set win;
		
		public Player0WinningVertexFilter( Set win ) {
			this.win = win;
		}

		public boolean filterVertex(Object vertex) {
			if ( win.contains( vertex ) )
				return true;
			return false;
		}
		
	}
	

	public static class DAGVertex extends LabeledGraphVertex {
		
		Set X;
		CopsAndRobberVertex v;
		
		public DAGVertex() {
			
		}
		
		public DAGVertex( CopsAndRobberVertex v ) {
			this.v = v;
			if ( v.isPlayer0() )
				this.X = v.getX();
			else
				this.X = v.getX2();
			setLabel( X.toString() );
		}
		
		
		
		public CopsAndRobberVertex getV() {
			return v;
		}

		public void setV(CopsAndRobberVertex v) {
			this.v = v;
		}

		public Set getX() {
			return X;
		}

		public void setX(Set x) {
			X = x;
		}

		public boolean equals( Object o ) {
			if ( ! (o instanceof DAGVertex ) )
				return false;
			//return this.v.equals( ((DAGVertex)o).v );
			return this.X.equals( ((DAGVertex)o).X ) && this.v.getRobber() ==  ((DAGVertex)o).v.getRobber() ;
		}
		
		public int hashCode() {
			return X.hashCode();
		}
	}
}
