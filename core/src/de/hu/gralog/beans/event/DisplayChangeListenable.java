package de.hu.gralog.beans.event;

/**
 * This interface provides the methods used to add and remove
 * DisplayChangeListerner's.
 * 
 * 
 * @author Sebastian
 * 
 * @param <V>
 *            the type of the vertices
 * @param <E>
 *            the type of the edges
 */
public interface DisplayChangeListenable<V, E> {
	public void addDisplayChangeListener(DisplayChangeListener<V, E> l);

	public void removeDisplayChangeListener(DisplayChangeListener<V, E> l);
}
