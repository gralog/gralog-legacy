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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import de.hu.gralog.beans.event.PropertyChangeListenable;

/**
 * This class is used by {@link de.hu.gralog.graph.GralogGraphSupport} as a
 * helper to provide support for listenening and fireing PropertyChangeEvents.
 * It is of no use for PluginDevelopers.
 * 
 * 
 * @author Sebastian
 * 
 * @param V
 *            the vertexType
 * @param E
 *            the edgeType
 */
public class GraphPropertyChangeSupport implements PropertyChangeListenable,
		PropertyChangeListener {

	private final List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();

	public void registerBean(PropertyChangeListenable bean) {
		bean.addPropertyChangeListener(this);
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		if (!(listeners.contains(l)))
			listeners.add(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		listeners.remove(l);
	}

	protected void firePropertyChangeEvent(PropertyChangeEvent evt) {
		for (PropertyChangeListener l : listeners)
			l.propertyChange(evt);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		firePropertyChangeEvent(evt);
	}

}
