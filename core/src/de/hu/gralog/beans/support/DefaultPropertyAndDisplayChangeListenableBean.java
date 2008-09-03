package de.hu.gralog.beans.support;

import de.hu.gralog.beans.event.DisplayChangeListenable;
import de.hu.gralog.beans.event.DisplayChangeListener;
import de.hu.gralog.beans.event.DisplayChangeSupport;
import de.hu.gralog.beans.event.PropertyChangeListenable;

/**
 * This class is a helper class to implement the 
 * {@link PropertyChangeListenable} and {@link DisplayChangeListenable}
 * interface. It administers the listeners and provides methods
 * to fire {@link PropertyChangeEvent PropertyChangeEvents} respectively DisplayChangeEvents. You
 * should subclass this class to define a Bean that allows others
 * to listen to changes of its properties respectively displayChanges.
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
