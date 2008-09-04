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

import de.hu.gralog.beans.event.DisplayChangeListenable;
import de.hu.gralog.beans.event.DisplayChangeListener;
import de.hu.gralog.beans.event.DisplayChangeSupport;
import de.hu.gralog.beans.event.PropertyChangeListenable;

/**
 * This class is a helper class to implement the
 * {@link de.hu.gralog.beans.event.PropertyChangeListenable} and
 * {@link de.hu.gralog.beans.event.DisplayChangeListenable} interface. It
 * administers the listeners and provides methods to fire
 * {@link java.beans.PropertyChangeEvent PropertyChangeEvents} respectively
 * DisplayChangeEvents. You should subclass this class to define a Bean that
 * allows others to listen to changes of its properties respectively
 * displayChanges.
 * 
 * @author Sebastian
 * 
 */
public class DefaultPropertyAndDisplayChangeListenableBean<V, E> extends
		DefaultPropertyChangeListenableBean implements
		DisplayChangeListenable<V, E> {

	protected final transient DisplayChangeSupport<V, E> displayChangeSupport = new DisplayChangeSupport<V, E>();

	public void addDisplayChangeListener(DisplayChangeListener l) {
		displayChangeSupport.addDisplayChangeListener(l);
	}

	public void removeDisplayChangeListener(DisplayChangeListener l) {
		displayChangeSupport.removeDisplayChangeListener(l);
	}
}
