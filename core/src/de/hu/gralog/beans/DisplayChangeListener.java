package de.hu.gralog.beans;

import java.util.Set;

public interface DisplayChangeListener<V,E> {
	public void displayChange( Set<V> vertices, Set<E> edges, boolean wholeGraph );
}
