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

package de.hu.games.graph.types;

import org.jgrapht.graph.DefaultEdge;

import de.hu.games.graph.DirectedGraph;
import de.hu.games.graph.GraphWithEditableElements;
import de.hu.games.graph.LabeledGraphVertex;
import de.hu.games.graph.UndirectedGraph;
import de.hu.games.graph.types.TransitionSystem.TransitionSystem;

public enum GraphType {

	UndirectedGraph, DirectedGraph, GameGraph, ParityGameGraph, TransitionSystem;
	
	public static GraphWithEditableElements createGraph( String graphType ) throws InstantiationException, IllegalAccessException {
		return GraphType.valueOf( graphType ).newInstance();
	}
	
	public GraphWithEditableElements newInstance() throws InstantiationException, IllegalAccessException {
		if ( this == UndirectedGraph )
			return new UndirectedGraph<LabeledGraphVertex, DefaultEdge>( LabeledGraphVertex.class, DefaultEdge.class );
		if ( this == DirectedGraph )
			return new DirectedGraph<LabeledGraphVertex, DefaultEdge>( LabeledGraphVertex.class, DefaultEdge.class );
		if ( this == GameGraph )
			return new DirectedGraph<GameGraphVertex, DefaultEdge>( GameGraphVertex.class, DefaultEdge.class );
		if ( this == ParityGameGraph )
			return new DirectedGraph<ParityGameVertex, DefaultEdge>( ParityGameVertex.class, DefaultEdge.class );
		if ( this == TransitionSystem )
			return new TransitionSystem();
		return null;
	}
	
	public static GraphType getGraphType( GraphWithEditableElements graph ) {
		for ( GraphType graphType : GraphType.values() ) {
			if ( graphType.isType( graph ) )
				return graphType;
		}
		if ( DirectedGraph.isInstance( graph ) )
			return DirectedGraph;
		return null;
	}
	
	public <V,E> boolean isType( GraphWithEditableElements<V,E> graph ) {
		try {
			GraphWithEditableElements thisGraph = newInstance();

			if ( ! graph.getClass().isInstance( thisGraph ) )
				return false;
			V v1 = graph.createVertex();
			Object v2 = thisGraph.createVertex();
			
			E e1 = graph.getEdgeFactory().createEdge( graph.createVertex(), graph.createVertex() );
			Object e2 = thisGraph.getEdgeFactory().createEdge( thisGraph.createVertex(), thisGraph.createVertex() );
			
			if ( v1.getClass().isInstance( v2 ) && e1.getClass().isInstance( e2 ) )
				return true;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
	public <V,E> boolean isInstance( GraphWithEditableElements<V,E> graph ) {
		try {
			GraphWithEditableElements thisGraph = newInstance();

			if ( ! thisGraph.getClass().isInstance( graph ) )
				return false;
			V v1 = graph.createVertex();
			Object v2 = thisGraph.createVertex();
			
			E e1 = graph.getEdgeFactory().createEdge( graph.createVertex(), graph.createVertex() );
			Object e2 = thisGraph.getEdgeFactory().createEdge( thisGraph.createVertex(), thisGraph.createVertex() );
			
			if ( v2.getClass().isInstance( v1 ) && e2.getClass().isInstance( e1 ) )
				return true;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
