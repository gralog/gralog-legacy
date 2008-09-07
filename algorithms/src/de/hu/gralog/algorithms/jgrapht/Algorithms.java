package de.hu.gralog.algorithms.jgrapht;

import java.util.HashSet;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedSubgraph;
import org.jgrapht.graph.EdgeReversedGraph;
import org.jgrapht.graph.Subgraph;
import org.jgrapht.traverse.BreadthFirstIterator;

public class Algorithms {
	
	public static<O> Set<O> filter( Set<O> objects, Filter<O> filter ) {
		if ( filter == null )
			return null;

		Set<O> filtered = new HashSet<O>();
		for ( O o : objects) {
			if ( filter == null || !filter.filter( o ) )
				filtered.add( o );
		}
		return filtered;
	}
	
	public static<V,E,G extends Graph<V,E>> Set<V> reach( G graph, V startVertex, boolean reverseEdges, Filter<V> vertexFilter ) {
		if ( reverseEdges && !(graph instanceof DirectedGraph ) )
			throw new IllegalArgumentException( "graph must be directed in order to reverse edges" );
		
		Graph<V,E> workingGraph = graph;
		if ( vertexFilter != null ) {
			if ( graph instanceof DirectedGraph )
				workingGraph = new DirectedSubgraph<V,E>( (DirectedGraph<V,E>)graph, filter( graph.vertexSet(), vertexFilter ), null );
			else
				workingGraph = new Subgraph<V,E,G>( graph, filter( graph.vertexSet(), vertexFilter ), null );
		}
		if ( reverseEdges )
			workingGraph = new EdgeReversedGraph<V,E>( (DirectedGraph<V,E>)workingGraph );
		
		Set<V> canReach = new HashSet<V>();
		BreadthFirstIterator it = new BreadthFirstIterator<V,E>( workingGraph, startVertex );
		while ( it.hasNext() )
			canReach.add( (V)it.next() );
		return canReach;
	}
}
