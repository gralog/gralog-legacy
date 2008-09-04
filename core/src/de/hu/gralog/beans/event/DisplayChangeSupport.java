/*
 * Created on 2008 by Sebastian Ordyniak
 *
 * Copyright 2008 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of Gralog.
 *
 * Gralog is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * Gralog is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Gralog; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */
package de.hu.gralog.beans.event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class adds as a helper to implement DisplayChangeListenable. It
 * administrates the listeners and provides method to call when these listeners
 * should be triggered.
 * 
 * @author Sebastian
 * 
 * @param <V>
 *            the vertextype
 * @param <E>
 *            the edgetype
 */
public class DisplayChangeSupport<V, E> implements
		DisplayChangeListenable<V, E> {

	private final List<DisplayChangeListener<V, E>> listeners = new ArrayList<DisplayChangeListener<V, E>>();

	private final Set<V> vertices = new HashSet<V>();

	private final Set<E> edges = new HashSet<E>();

	private boolean wholeGraph = false;

	public DisplayChangeSupport() {
	}

	public void addVertexForDefaultChange(V vertex) {
		vertices.add(vertex);
	}

	public void addEdgeForDefaultChange(E edge) {
		edges.add(edge);
	}

	public void setWholeGraphForDefaultChange(boolean wholeGraph) {
		this.wholeGraph = wholeGraph;
	}

	public void fireDisplayChange(Set<V> vertices, Set<E> edges,
			boolean wholeGraph) {
		for (DisplayChangeListener l : listeners)
			l.displayChange(vertices, edges, wholeGraph);
	}

	public void fireDisplayChangeDefault() {
		fireDisplayChange(vertices, edges, wholeGraph);
	}

	public void addDisplayChangeListener(DisplayChangeListener l) {
		if (!(listeners.contains(l)))
			listeners.add(l);
	}

	public void removeDisplayChangeListener(DisplayChangeListener l) {
		listeners.remove(l);
	}
}
