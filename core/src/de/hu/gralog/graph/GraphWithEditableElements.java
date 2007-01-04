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

package de.hu.gralog.graph;


/**
 * 
 * This interface need to be implemented by all custom graphobjects. It provides methods, to edit the
 * properties of graphelements (vertices, edges) in a standard manner.
 * 
 * @author Sebastian
 *
 */

public interface GraphWithEditableElements<V,E> extends SelectionListenableGraph<V,E> {

	/**
	 * 
	 * Returns a reference to an instance of the vertexclass used by this graph. 
	 * This method should not return null, since vertices of all graphs should be editable.
	 *  
	 * @return an instance of the vertexclass used by this graph.
	 */
	public V createVertex();
	
	/**
	 * Sets the TypeInfo of this graph. The TypeInfo attribute is required in order to
	 * store and load the graph. So if you construct a graph you should always set the
	 * TypeInfo of that graph - at a future version this should be moved to the constructor. 
	 * 
	 * @param typeInfo
	 */
	public void setTypeInfo( GraphTypeInfo typeInfo );
	
	/**
	 * @return the TypeInfo of this graph
	 */
	public GraphTypeInfo getTypeInfo();
	public boolean isGraphEditable();
	public boolean isVertexEditable();
	public boolean isEdgeEditable();
}
