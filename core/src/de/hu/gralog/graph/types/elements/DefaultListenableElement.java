package de.hu.gralog.graph.types.elements;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import de.hu.gralog.beans.DisplayChangeListenable;
import de.hu.gralog.beans.DisplayChangeListener;
import de.hu.gralog.beans.DisplayChangeSupport;
import de.hu.gralog.beans.PropertyChangeListenable;

public class DefaultListenableElement<V,E> implements PropertyChangeListenable, DisplayChangeListenable<V,E>, Serializable {

	protected final transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport( this );
	protected final transient DisplayChangeSupport<V,E> displayChangeSupport = new DisplayChangeSupport<V,E>();
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.addPropertyChangeListener( l );
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.removePropertyChangeListener( l );
	}

	public void addDisplayChangeListener(DisplayChangeListener l) {
		displayChangeSupport.addDisplayChangeListener( l );
	}

	public void removeDisplayChangeListener(DisplayChangeListener l) {
		displayChangeSupport.removeDisplayChangeListener( l );
	}
}
