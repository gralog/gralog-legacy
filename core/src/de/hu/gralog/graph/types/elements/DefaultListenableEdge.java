package de.hu.gralog.graph.types.elements;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.jgrapht.graph.DefaultEdge;

import de.hu.gralog.beans.event.DisplayChangeListenable;
import de.hu.gralog.beans.event.DisplayChangeListener;
import de.hu.gralog.beans.event.DisplayChangeSupport;
import de.hu.gralog.beans.event.PropertyChangeListenable;

/**
 * Please refer to {@link DefaultListenableVertex}, since edges are also
 * JavaBeans all that is said there about vertices in Gralog applies
 * to Gralog-Edges as well. The only difference between
 * an edge and a vertex in Gralog is that an Edge has to subclass
 * {org.jgrapht.DefaultEdge} that provides fields for the source- and
 * target-vertex of this edge.
 * 
 * @author Sebastian
 *
 */
public class DefaultListenableEdge extends DefaultEdge implements
		PropertyChangeListenable, DisplayChangeListenable {

	protected final transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);

	protected final transient DisplayChangeSupport displayChangeSupport = new DisplayChangeSupport();

	public DefaultListenableEdge() {
		super();
		displayChangeSupport.addEdgeForDefaultChange(this);
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.removePropertyChangeListener(l);
	}

	public void addDisplayChangeListener(DisplayChangeListener l) {
		displayChangeSupport.addDisplayChangeListener(l);
	}

	public void removeDisplayChangeListener(DisplayChangeListener l) {
		displayChangeSupport.removeDisplayChangeListener(l);
	}
}
