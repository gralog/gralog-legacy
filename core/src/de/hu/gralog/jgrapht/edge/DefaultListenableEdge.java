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

package de.hu.gralog.jgrapht.edge;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.jgrapht.graph.DefaultEdge;

import de.hu.gralog.app.UserException;
import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgrapht.ListenableElement;
import de.hu.gralog.jgrapht.event.GraphPropertyListener;

/**
 * 
 * This class should be overriden in order to implement an Edge that is listenable.
 * It contains the listenerlist functionality.
 * 
 * @author Sebastian
 *
 */

public class DefaultListenableEdge extends DefaultEdge implements ListenableElement {

	private transient static final DefaultEdgeRenderer RENDERER = new DefaultEdgeRenderer();
	private transient ArrayList<GraphPropertyListener> listeners = new ArrayList<GraphPropertyListener>();


	public DefaultListenableEdge() {
		super();
	}
	
	public DefaultEdgeRenderer getRenderer() {
		return RENDERER;
	}
	
	protected void firePropertyChange( PropertyChangeEvent e ) {
		for ( GraphPropertyListener l : listeners )
			l.propertyChanged( this, e );
	}
	
	protected void firePropertyChange( String propertyName, Object oldValue, Object newValue ) {
		firePropertyChange( new PropertyChangeEvent( this, propertyName, oldValue, newValue ) );
	}
	
	protected void fireIndexPropertyChange( String propertyName, Object oldValue, Object newValue, int index ) {
		firePropertyChange( new IndexedPropertyChangeEvent( this, propertyName, oldValue, newValue, index ) );
	}
	
	public void addGraphPropertyListener(GraphPropertyListener l) {
		if ( ! listeners.contains( l ) )
			listeners.add( l );
	}

	public void removeGraphPropertyListener(GraphPropertyListener l) {
		listeners.remove( l );
	}
	
	private void readObject( ObjectInputStream ois ) throws IOException {
		try {
			ois.defaultReadObject();
		} catch (ClassNotFoundException e) {
			throw new IOException( "DefaultListenableVertex: readObject - ClassNotFoundException" );
		}
		listeners = new ArrayList<GraphPropertyListener>();
	}
	
	public Object cloneMe() throws UserException {
		DefaultListenableEdge clone = null;
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			new ObjectOutputStream( bout ).writeObject( this );
			ByteArrayInputStream bin = new ByteArrayInputStream( bout.toByteArray() );
			clone = (DefaultListenableEdge)new ObjectInputStream(bin).readObject();
		} catch(Throwable e) {
			throw new UserException( "error cloning defaultlistenablevertex", e);
		}
		clone.listeners = new ArrayList<GraphPropertyListener>();
		return clone;
	}
}