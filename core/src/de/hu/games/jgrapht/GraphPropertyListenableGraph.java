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

package de.hu.games.jgrapht;

import org.jgrapht.ListenableGraph;

import de.hu.games.jgrapht.event.GraphPropertyListener;

/**
 * 
 * This interface extends {@link ListenableGraph ListenableGraph} with functions to add or remove listeners for 
 * changes of elements (vertices/edges) of the graph.
 * 
 * @author Sebastian
 *
 */

public interface GraphPropertyListenableGraph<V,E> extends ListenableGraph<V,E> {
	
	/**
	 * 
	 * add l to the list of elementlisteners of this graph
	 * 
	 * @param l the listener to be added
	 */
	public void addGraphPropertyListener(GraphPropertyListener l);
	
	/**
	 * 
	 * remove l from the list of elementlisteners of this graph
	 * 
	 * @param l the listener to be removed
	 */
	public void removeGraphPropertyListener(GraphPropertyListener l);
	
}
