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

package de.hu.games.jgrapht.traverse;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;
import org.jgrapht.traverse.CrossComponentIterator;
import org.jgrapht.util.FibonacciHeap;
import org.jgrapht.util.FibonacciHeapNode;

public class LongestPathIterator<V,E> extends CrossComponentIterator<V,E,FibonacciHeapNode<LongestPathIterator.HeapEntry<V, E>>> {

	private FibonacciHeap<HeapEntry<V,E>> heap = new FibonacciHeap<HeapEntry<V,E>>();
	
	public LongestPathIterator( DirectedGraph<V,E> graph, V startVertex ) {
		super( graph, startVertex );
	}

	@Override
	protected void encounterVertex(V vertex, E edge) {
		FibonacciHeapNode<HeapEntry<V, E>> node = createSeenData(vertex, edge);
        putSeenData(vertex, node);
        heap.insert(node, node.getKey());
	}

	@Override
	protected void encounterVertexAgain(V vertex, E edge) {
		FibonacciHeapNode<HeapEntry<V, E>> node = getSeenData(vertex);

        if (node.getData().frozen) {
            // no improvement for this vertex possible
            return;
        }

        double candidatePathLength = calculatePathLength(vertex, edge);

        if (candidatePathLength < node.getKey())
            heap.decreaseKey(node, candidatePathLength);
	}

	@Override
	protected boolean isConnectedComponentExhausted() {
		if ( heap.size() == 0 )
			return true;
		return false;
	}

	@Override
	protected V provideNextVertex() {
		FibonacciHeapNode<HeapEntry<V, E>> node = heap.removeMin();
        node.getData().frozen = true;

        return node.getData().vertex;
	}
	
	/**
     * Get the length of the shortest path known to the given vertex.  If the
     * vertex has already been visited, then it is truly the shortest path
     * length; otherwise, it is the best known upper bound.
     *
     * @param vertex vertex being sought from start vertex
     *
     * @return length of shortest path known, or Double.POSITIVE_INFINITY if no
     *         path found yet
     */
    public double getLongestPathLength(V vertex)
    {
        FibonacciHeapNode<HeapEntry<V, E>> node = getSeenData(vertex);

        if (node == null) {
            return Double.POSITIVE_INFINITY;
        }

        return node.getKey();
    }
	
	private void assertNonNegativeEdge(E edge)
    {
        if (getGraph().getEdgeWeight(edge) < 0) {
            throw new IllegalArgumentException(
                "negative edge weights not allowed");
        }
    }
	
	/**
     * Determine path length to a vertex via an edge, using the path length for
     * the opposite vertex.
     *
     * @param vertex the vertex for which to calculate the path length.
     * @param edge the edge via which the path is being extended.
     *
     * @return calculated path length.
     */
    private double calculatePathLength(V vertex, E edge )
    {
        assertNonNegativeEdge(edge);

        double maxPathLen = 0;
        
        for ( E incomingEdge : ((DirectedGraph<V,E>)getGraph()).incomingEdgesOf( vertex ) ) {
        	FibonacciHeapNode<HeapEntry<V,E>> node = getSeenData( Graphs.getOppositeVertex( getGraph(), incomingEdge, vertex ) );
        	if ( node == null ) {
        		maxPathLen = Double.POSITIVE_INFINITY;
        		break;
        	} else {
        		if ( maxPathLen < node.getKey() )
        			maxPathLen = node.getKey();
        	}
        }
        
       
        
        V otherVertex = Graphs.getOppositeVertex(getGraph(), edge, vertex);
        FibonacciHeapNode<HeapEntry<V, E>> otherEntry =
            getSeenData(otherVertex);

        return maxPathLen
            + getGraph().getEdgeWeight(edge);
    }
	
	private FibonacciHeapNode<HeapEntry<V, E>> createSeenData(
	        V vertex,
	        E edge)
	    {
	        double longestPathLength;

	        if (edge == null) {
	            longestPathLength = 0;
	        } else {
	            longestPathLength = calculatePathLength(vertex, edge);
	        }

	        HeapEntry<V, E> entry = new HeapEntry<V, E>();
	        entry.vertex = vertex;
	        entry.spanningTreeEdge = edge;

	        return
	            new FibonacciHeapNode<HeapEntry<V, E>>(
	                entry,
	                longestPathLength);
	    }

	    //~ Inner Classes ---------------------------------------------------------

	    /**
	     * Private data to associate with each entry in the priority queue.
	     */
	    static class HeapEntry<V, E>
	    {
	        /**
	         * Best spanning tree edge to vertex seen so far.
	         */
	        E spanningTreeEdge;

	        /**
	         * The vertex reached.
	         */
	        V vertex;

	        /**
	         * True once spanningTreeEdge is guaranteed to be the true minimum.
	         */
	        boolean frozen;

	        HeapEntry()
	        {
	        }
	    }
}
