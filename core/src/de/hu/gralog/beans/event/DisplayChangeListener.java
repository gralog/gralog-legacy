package de.hu.gralog.beans.event;

import java.util.Set;

/**
 * This interface defines a listener that listens to displayChange Events.
 * 
 * @author Sebastian
 * 
 * @param <V>
 *            the vertextype
 * @param <E>
 *            the edgetype
 */
public interface DisplayChangeListener<V, E> {
	/**
	 * This method is called when an DisplayChangeEvent has happened.
	 * 
	 * @param vertices
	 *            the vertices that have changed
	 * @param edges
	 *            the edges that have changed
	 * @param wholeGraph
	 *            if true the whole graph will be redisplayed
	 */
	public void displayChange(Set<V> vertices, Set<E> edges, boolean wholeGraph);
}
