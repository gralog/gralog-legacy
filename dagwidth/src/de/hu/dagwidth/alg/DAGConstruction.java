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

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;

import de.hu.dagwidth.alg.CopsAndRobberAlgorithm.CopsAndRobberVertex;
import de.hu.dagwidth.alg.CopsAndRobberAlgorithm.DummyVertexFilter;
import de.hu.gralog.algorithm.Algorithm;
import de.hu.gralog.algorithm.InvalidPropertyValuesException;
import de.hu.gralog.algorithm.result.AlgorithmResult;
import de.hu.gralog.algorithm.result.AlgorithmResultContent;
import de.hu.gralog.algorithms.jgrapht.Algorithms;
import de.hu.gralog.algorithms.jgrapht.Filter;
import de.hu.gralog.app.UserException;
import de.hu.gralog.finitegames.alg.Simple2PlayerGameAlgorithm;
import de.hu.gralog.graph.GralogGraphFactory;
import de.hu.gralog.graph.GralogGraphSupport;
import de.hu.gralog.graph.types.LabeledSimpleDirectedGraphTypeInfo;
import de.hu.gralog.graph.types.elements.LabeledGraphVertex;

public class DAGConstruction<V extends LabeledGraphVertex,E extends DefaultEdge, G extends ListenableDirectedGraph<V,E>> implements Algorithm {

	private GralogGraphSupport<V, E,?,G> graph;
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

	public GralogGraphSupport<V, E,?,G> getGraph() {
		return graph;
	}

	public void setGraph(GralogGraphSupport<V, E,?,G> graph) {
		this.graph = graph;
	}

	
	public AlgorithmResult execute(  ) throws InvalidPropertyValuesException, UserException {
		if ( getGraph() == null )
			throw new InvalidPropertyValuesException( "graph", InvalidPropertyValuesException.PROPERTY_REQUIRED );
		
		if ( graph.createVertex() instanceof CopsAndRobberVertex ) {
			AlgorithmResult result = new AlgorithmResult();
			result.setSingleContent( new AlgorithmResultContent( getDirectedGraphFromDAG( getDAG( (ListenableDirectedGraph<CopsAndRobberVertex, E>)graph.getGraph() ) ) ) );
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
			Iterator<DirectedGraph<CopsAndRobberVertex<V>, DefaultEdge>> it = CopsAndRobberAlgorithm.getCopsAndRobberGameGraphs( graph.getGraph(), currWidth++, true, true );
			while ( it.hasNext() ) {
				ListenableDirectedGraph<DAGVertex, DefaultEdge> outGraph = getDAG( it.next() );
				
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
	
	public GralogGraphSupport<LabeledGraphVertex, DefaultEdge,?,ListenableDirectedGraph<LabeledGraphVertex,DefaultEdge>> getDirectedGraphFromDAG( DirectedGraph<DAGVertex, DefaultEdge> dag ) throws UserException {
		GralogGraphSupport<LabeledGraphVertex, DefaultEdge,?,ListenableDirectedGraph<LabeledGraphVertex, DefaultEdge>> graph = (GralogGraphSupport<LabeledGraphVertex, DefaultEdge,?,ListenableDirectedGraph<LabeledGraphVertex,DefaultEdge>>)GralogGraphFactory.createGraphSupport( new LabeledSimpleDirectedGraphTypeInfo() );
		Hashtable<DAGVertex, LabeledGraphVertex> vertexes = new Hashtable<DAGVertex, LabeledGraphVertex>();
		for ( DAGVertex vertex : dag.vertexSet() ) {
			LabeledGraphVertex v = new LabeledGraphVertex( vertex.getLabel() );
			vertexes.put( vertex, v );
			graph.getGraph().addVertex( v );
		}
		for ( DefaultEdge edge : dag.edgeSet() ) {
			LabeledGraphVertex from = vertexes.get( dag.getEdgeSource( edge ) );
			LabeledGraphVertex to = vertexes.get( dag.getEdgeTarget( edge ) );
			graph.getGraph().addEdge( from, to );
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
			    reachableV = Algorithms.reach( graph.getGraph(), v, false, new VertexVertexFilter<V>( seperator ) );
				reachV = Algorithms.reach( graph.getGraph(), v, true, new VertexVertexFilter<V>( seperator ) );
				reachVCopy = new HashSet<V>( reachV );
				reachV.removeAll( reachableV );
				reachV.retainAll( vertexes );
				if ( ! reachV.isEmpty() )
					v = reachV.iterator().next();
			} while (  !reachV.isEmpty() );
			if ( previous != null ) {
				reachVCopy.retainAll( reachableV );
				reachVCopy.retainAll( vertexes );
				Iterator<E> edges = graph.getGraph().outgoingEdgesOf( previous ).iterator();
				while ( edges.hasNext() ) {
					V o = graph.getGraph().getEdgeTarget( edges.next() );
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
	
	public <VI extends CopsAndRobberVertex<V>, EI extends DefaultEdge> ListenableDirectedGraph<DAGVertex, DefaultEdge> getDAG( DirectedGraph<VI, EI> gameGraph ) throws UserException {
		Set<VI> winningPlayer1 = new HashSet<VI>( new Simple2PlayerGameAlgorithm<VI,EI>( gameGraph ).execute() );
		
		Set<VI> winningStartVertexes = new HashSet<VI>( Algorithms.filter( gameGraph.vertexSet(), new DummyVertexFilter<VI>() ));
		winningStartVertexes.removeAll( winningPlayer1 );
		
		if ( winningStartVertexes.size()  == 0 )
			return null;
		
		VI start = winningStartVertexes.iterator().next();
		
		ListenableDirectedGraph<DAGVertex, DefaultEdge> outGraph = new ListenableDirectedGraph<DAGVertex, DefaultEdge>( DefaultEdge.class );
		
		LinkedList<VI> queue = new LinkedList<VI>();
		queue.add( start );
		
		Hashtable<VI, Set<V>> reachableHash = new Hashtable<VI, Set<V>>();
		reachableHash.put( start, graph.getGraph().vertexSet() );
		
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
	
	public static class VertexVertexFilter<V> implements Filter<V> {

		private Set<V> seperator;
		
		public VertexVertexFilter( Set<V> seperator ) {
			this.seperator = seperator;
		}
		
		public boolean filter(V vertex) {
			if ( seperator.contains( vertex ) )
				return true;
			return false;
		}
		
	}
	
	public static class Player0WinningVertexFilter<V> implements Filter<V> {
		private Set<V> win;
		
		public Player0WinningVertexFilter( Set<V> win ) {
			this.win = win;
		}

		public boolean filter(V vertex) {
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
