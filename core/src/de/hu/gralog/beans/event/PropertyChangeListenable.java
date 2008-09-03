package de.hu.gralog.beans.event;

import java.beans.PropertyChangeListener;

/**
 * This interface provides its implementing classes with the ability to install
 * PropertyChangeListeners.
 * 
 * @author Sebastian
 * 
 */
public interface PropertyChangeListenable {
	public void addPropertyChangeListener(PropertyChangeListener l);

	public void removePropertyChangeListener(PropertyChangeListener l);
}
