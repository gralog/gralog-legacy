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
package de.hu.gralog.graph.support;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphSelectionModel;
import org.jgrapht.graph.DefaultEdge;

import de.hu.gralog.graph.event.SelectionListener;

/**
 * This class is used by {@link de.hu.gralog.graph.GralogGraphSupport} as a
 * helper to provide support for listenening and fireing SelectionChangeEvents.
 * Plugin-developers might use this class when implementing
 * {@link de.hu.gralog.algorithm.result.AlgorithmResultInteractiveContent} and
 * want to listen to SelectionChangeEvents.
 * 
 * @see de.hu.gralog.graph.GralogGraphSupport#getGraphSelectionSupport()
 * 
 * @author Sebastian
 * 
 * @param V
 *            the vertexType
 * @param E
 *            the edgeType
 */
public class GraphSelectionSupport<V, E> implements GraphSelectionListener {

	private final List<SelectionListener<V, E>> listeners = new ArrayList<SelectionListener<V, E>>();

	private final Set<V> vertices = new HashSet<V>();

	private final Set<E> edges = new HashSet<E>();

	/**
	 * Adds a {@link SelectionListener} to this class that is informed whenever
	 * the user changes his selection of elements ( vertices / edges ) for the
	 * GralogGraph this instance belongs to.
	 * 
	 * @param l
	 */
	public void addSelectionListener(SelectionListener<V, E> l) {
		if (!(listeners.contains(l)))
			listeners.add(l);
	}

	/**
	 * Removes a {@link SelectionListener} from this class.
	 * 
	 * @see #addSelectionListener(SelectionListener)
	 * 
	 * @param l
	 */
	public void removeSelectionListener(SelectionListener<V, E> l) {
		listeners.remove(l);
	}

	/**
	 * 
	 * 
	 * @return the currently selected vertices
	 */
	public Set<V> getSelectedVertices() {
		return vertices;
	}

	/**
	 * 
	 * @return the currently selected egdes
	 */
	public Set<E> getSelectedEdges() {
		return edges;
	}

	/**
	 * 
	 * @param vertexes
	 * @param edges
	 * @param event
	 */
	private void fireSelectionChangedEvent(Set<V> vertexes, Set<E> edges,
			GraphSelectionEvent event) {
		for (SelectionListener<V, E> listener : listeners)
			listener.valueChanged(vertexes, edges, event);
	}

	/**
	 * This function should only be called by JGraph when the selection has
	 * changed.
	 * 
	 * @param event
	 */
	public void valueChanged(GraphSelectionEvent event) {
		vertices.clear();
		edges.clear();

		for (Object objectCell : ((GraphSelectionModel) event.getSource())
				.getSelectionCells()) {
			if (objectCell instanceof DefaultGraphCell) {
				DefaultGraphCell cell = (DefaultGraphCell) objectCell;
				Object userObject = cell.getUserObject();

				if (userObject instanceof DefaultEdge)
					edges.add((E) userObject);
				else
					vertices.add((V) userObject);
			}
		}

		fireSelectionChangedEvent(vertices, edges, event);
	}

}
