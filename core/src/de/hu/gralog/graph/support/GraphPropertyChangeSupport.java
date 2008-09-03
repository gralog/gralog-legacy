package de.hu.gralog.graph.support;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import de.hu.gralog.beans.event.PropertyChangeListenable;

/**
 * This class is used by {@link GralogGraphSupport} as a helper to
 * provide support for listenening and fireing PropertyChangeEvents. 
 * It is of no use for PluginDevelopers.
 * 
 * 
 * @author Sebastian
 *
 * @param <V> the vertexType
 * @param <E> the edgeType
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
