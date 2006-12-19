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

package de.hu.gralog.jgrapht.graph;

import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedSubgraph;
import org.jgrapht.graph.Subgraph;

import de.hu.gralog.jgrapht.traverse.VertexFilter;

public class SubgraphFactory {

	public static <V,E> Subgraph<V,E> createInducedSubgraph( Graph<V,E> graph, VertexFilter<V> vertexFilter, boolean reverseEdges ) {
		if ( graph instanceof DirectedGraph && reverseEdges )
			graph = new ReversedDirectedGraph<V,E>( (DirectedGraph<V,E>)graph );
		return createInducedSubgraph( graph, vertexFilter );
	}
	
	public static <V,E> Subgraph<V,E> createInducedSubgraph( Graph<V,E> graph, VertexFilter<V> vertexFilter ) {
		Set<V> vertexes = GraphUtils.filterVertexes( graph.vertexSet(), vertexFilter );

		return createSubgraph( graph, vertexes, null );
	}
	
	public static <V,E> Subgraph<V,E> createSubgraph( Graph<V,E> graph, Set<V> vertices, Set<E> edges ) {
		if ( graph instanceof DirectedGraph )
			return new DirectedSubgraph<V,E>( (DirectedGraph<V,E>) graph, vertices, edges );
		return new Subgraph<V,E>( graph, vertices, edges );
	}
}
