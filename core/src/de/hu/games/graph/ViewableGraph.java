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

package de.hu.games.graph;

import de.hu.games.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.games.jgraph.cellview.DefaultVertexRenderer;
import de.hu.games.jgrapht.GraphPropertyListenableGraph;


/**
 * 	
 * This interface extends ElementListenableGraph, with functions needed to
 * display the Graph with JGraph. The methods of this interface allow you to 
 * specify renderers for vertices and egdes of the graph.
 * 
 * This interface needs to be implemented for all customtypes of graphs, which should be displayed
 * and editable by the gamesapplication. See {@link de.hu.games.types GraphTypes} for exampleimplementations.
 * 
 * @author Sebastian
 *
 */
public interface ViewableGraph<V,E> extends GraphPropertyListenableGraph<V,E> {
	/**
	 *
	 * This method allows you to provide the Renderer which should be used by JGraph to render the vertices
	 * of the graph.
	 * 
	 * @return the vertexrenderer used to render the vertices of the graph. Return null, if you want JGraph to use
	 * the {@link de.hu.games.jgraph.cellview.DefaultVertexRenderer DefaultVertexRenderer}. 
	 */
	public DefaultVertexRenderer getVertexRenderer();
	
	/**
	 *
	 * This method allows you to provide the Renderer which should be used by JGraph to render the edges
	 * of the graph.
	 * 
	 * @return the vertexrenderer used to render the edges of the graph. Return null, if you want JGraph to use
	 * the {@link de.hu.games.jgraph.cellview.DefaultEdgeRenderer DefaultEdgeRenderer}. 
	 */
	public DefaultEdgeRenderer getEdgeRenderer();
}
