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
package de.hu.gralog.beans.support;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import de.hu.gralog.beans.event.PropertyChangeListenable;

/**
 * This class is a helper class to implement the
 * {@link PropertyChangeListenable} interface. It administers the listeners and
 * provides methods to fire {@link java.beans.PropertyChangeEvent PropertyChangeEvents}.
 * You should subclass this class to define a Bean that allows others to listen
 * to changes of its properties.
 * 
 * @author Sebastian
 * 
 */
public class DefaultPropertyChangeListenableBean implements
		PropertyChangeListenable, Serializable {

	protected final transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);

	public void addPropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.removePropertyChangeListener(l);
	}

}
