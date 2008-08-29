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

package de.hu.gralog.algorithm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;

import de.hu.gralog.graph.old.SubgraphFactory;
import de.hu.gralog.jgrapht.traverse.VertexFilter;

public class Algorithms {

	public static <V,E> Set<V> reach( Graph<V,E> graph, V startVertex, boolean reverseEdges, VertexFilter<V> vertexFilter ) {
		Iterator<V> it = new BreadthFirstIterator<V,E>( SubgraphFactory.createInducedSubgraph( graph, vertexFilter, reverseEdges ), startVertex );
		HashSet<V> reach = new HashSet<V>();
		while ( it.hasNext() )
			reach.add( it.next() );
		return reach;
	}
	
}
