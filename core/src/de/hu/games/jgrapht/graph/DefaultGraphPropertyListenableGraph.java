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

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultListenableGraph;

import de.hu.games.jgrapht.GraphPropertyListenableGraph;
import de.hu.games.jgrapht.ListenableElement;
import de.hu.games.jgrapht.event.GraphPropertyListener;

/**
 * 
 * This class acts as the default implementation for ElementListenableGraphs.
 * 
 * @author Sebastian
 *
 */
public class DefaultGraphPropertyListenableGraph<V,E> extends DefaultListenableGraph<V,E> implements GraphPropertyListenableGraph<V,E> {

	transient final private ArrayList<GraphPropertyListener> graphPropertyListeners = new ArrayList<GraphPropertyListener>();
	transient final private GraphPropertyListener elementListener = new ElementListener();
	
	public DefaultGraphPropertyListenableGraph(Graph<V,E> g, boolean reuseEvents) {
		super(g, reuseEvents);
	}

	public DefaultGraphPropertyListenableGraph(Graph<V,E> g) {
		super(g);
	}
	
	@Override
	public boolean addEdge(V sourceVertex, V targetVertex, E e) {
		if (e instanceof ListenableElement)
			((ListenableElement)e).addGraphPropertyListener( elementListener );
		return super.addEdge( sourceVertex, targetVertex, e );
	}

	@Override
	public E addEdge(V sourceVertex, V targetVertex) {
		E e = super.addEdge( sourceVertex, targetVertex );
		if (e instanceof ListenableElement)
			((ListenableElement)e).addGraphPropertyListener( elementListener );
		return e;
	}

	@Override
	public boolean addVertex(V v) {
		if (v instanceof ListenableElement)
			((ListenableElement)v).addGraphPropertyListener( elementListener );
		return super.addVertex(v);
	}

	protected void fireGraphPropertyChange( Object graphSource, PropertyChangeEvent e ) {
		for ( GraphPropertyListener l : graphPropertyListeners )
			l.propertyChanged( graphSource, e );
	}
	
	private class ElementListener implements GraphPropertyListener {

		public void propertyChanged(Object graphSource, PropertyChangeEvent e) {
			fireGraphPropertyChange( graphSource, e );
		}
		
	}

	public void addGraphPropertyListener(GraphPropertyListener l) {
		if ( ! graphPropertyListeners.contains( l ) )
			graphPropertyListeners.add( l );
	}

	public void removeGraphPropertyListener(GraphPropertyListener l) {
		graphPropertyListeners.remove( l );
	}

}
