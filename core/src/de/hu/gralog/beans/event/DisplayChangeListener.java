/*
 * Created on 2008 by Sebastian Ordyniak
 *
 * Copyright 2008 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of GrALoG.
 *
 * GrALoG is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * GrALoG is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GrALoG; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */
package de.hu.gralog.beans.event;

import java.util.Set;

/**
 * This interface defines a listener that listens to displayChange Events.
 * 
 * @author Sebastian
 * 
 * @param <V>
 *            the vertextype
 * @param <E>
 *            the edgetype
 */
public interface DisplayChangeListener<V, E> {
	/**
	 * This method is called when an DisplayChangeEvent has happened.
	 * 
	 * @param vertices
	 *            the vertices that have changed
	 * @param edges
	 *            the edges that have changed
	 * @param wholeGraph
	 *            if true the whole graph will be redisplayed
	 */
	public void displayChange(Set<V> vertices, Set<E> edges, boolean wholeGraph);
}
