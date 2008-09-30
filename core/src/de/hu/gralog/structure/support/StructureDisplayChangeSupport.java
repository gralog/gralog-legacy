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
package de.hu.gralog.structure.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.hu.gralog.beans.event.DisplayChangeListenable;
import de.hu.gralog.beans.event.DisplayChangeListener;

/**
 * This class is used by {@link de.hu.gralog.structure.Structure} as a
 * helper to provide support for listenening and fireing DisplayChanges. It is
 * of no use to Plugin-Developers.
 * 
 * 
 * @author Sebastian
 * 
 * @param <V>
 *            the vertexType
 * @param <E>
 *            the edgeType
 */
public class StructureDisplayChangeSupport<V, E> implements
		DisplayChangeListenable<V, E>, DisplayChangeListener<V, E> {

	private final List<DisplayChangeListener<V, E>> listeners = new ArrayList<DisplayChangeListener<V, E>>();

	public void registerBean(DisplayChangeListenable<V, E> bean) {
		bean.addDisplayChangeListener(this);
	}

	public void addDisplayChangeListener(DisplayChangeListener<V, E> l) {
		if (!(listeners.contains(l)))
			listeners.add(l);
	}

	public void removeDisplayChangeListener(DisplayChangeListener<V, E> l) {
		listeners.remove(l);
	}

	protected void fireDisplayChange(Set<V> vertices, Set<E> edges,
			boolean wholeGraph) {
		for (DisplayChangeListener<V, E> l : listeners)
			l.displayChange(vertices, edges, wholeGraph);
	}

	public void displayChange(Set<V> vertices, Set<E> edges, boolean wholeGraph) {
		fireDisplayChange(vertices, edges, wholeGraph);
	}

}
