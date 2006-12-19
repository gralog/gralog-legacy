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

package de.hu.games.jgrapht.graph;

import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.GraphDelegator;

public class ReversedDirectedGraph<V,E> extends GraphDelegator<V,E> implements DirectedGraph<V,E> {

	public ReversedDirectedGraph( DirectedGraph<V,E> graph ) {
		super( graph );
	}

	@Override
	public V getEdgeSource(E e) {
		return super.getEdgeTarget( e );
	}

	@Override
	public V getEdgeTarget(E e) {
		return super.getEdgeSource( e );
	}

	@Override
	public Set<E> incomingEdgesOf(V vertex) {
		return super.outgoingEdgesOf( vertex );
	}

	@Override
	public int inDegreeOf(V vertex) {
		// TODO Auto-generated method stub
		return super.inDegreeOf(vertex);
	}

	@Override
	public int outDegreeOf(V vertex) {
		return super.inDegreeOf( vertex );
	}

	@Override
	public Set<E> outgoingEdgesOf(V vertex) {
		return super.incomingEdgesOf( vertex );
	}
	
	
	
}
