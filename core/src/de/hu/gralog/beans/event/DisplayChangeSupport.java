package de.hu.gralog.beans.event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class adds as a helper to implement DisplayChangeListenable. It
 * administrates the listeners and provides method to call when these listeners
 * should be triggered.
 * 
 * @author Sebastian
 * 
 * @param <V>
 *            the vertextype
 * @param <E>
 *            the edgetype
 */
public class DisplayChangeSupport<V, E> implements
		DisplayChangeListenable<V, E> {

	private final List<DisplayChangeListener<V, E>> listeners = new ArrayList<DisplayChangeListener<V, E>>();

	private final Set<V> vertices = new HashSet<V>();

	private final Set<E> edges = new HashSet<E>();

	private boolean wholeGraph = false;

	public DisplayChangeSupport() {
	}

	public void addVertexForDefaultChange(V vertex) {
		vertices.add(vertex);
	}

	public void addEdgeForDefaultChange(E edge) {
		edges.add(edge);
	}

	public void setWholeGraphForDefaultChange(boolean wholeGraph) {
		this.wholeGraph = wholeGraph;
	}

	public void fireDisplayChange(Set<V> vertices, Set<E> edges,
			boolean wholeGraph) {
		for (DisplayChangeListener l : listeners)
			l.displayChange(vertices, edges, wholeGraph);
	}

	public void fireDisplayChangeDefault() {
		fireDisplayChange(vertices, edges, wholeGraph);
	}

	public void addDisplayChangeListener(DisplayChangeListener l) {
		if (!(listeners.contains(l)))
			listeners.add(l);
	}

	public void removeDisplayChangeListener(DisplayChangeListener l) {
		listeners.remove(l);
	}
}
