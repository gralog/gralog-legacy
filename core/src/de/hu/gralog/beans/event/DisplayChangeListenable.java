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

/**
 * This interface provides the methods used to add and remove
 * DisplayChangeListerner's.
 * 
 * 
 * @author Sebastian
 * 
 * @param <V>
 *            the type of the vertices
 * @param <E>
 *            the type of the edges
 */
public interface DisplayChangeListenable<V, E> {
	public void addDisplayChangeListener(DisplayChangeListener<V, E> l);

	public void removeDisplayChangeListener(DisplayChangeListener<V, E> l);
}
