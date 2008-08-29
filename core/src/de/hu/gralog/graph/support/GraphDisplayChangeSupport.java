package de.hu.gralog.graph.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.hu.gralog.beans.DisplayChangeListenable;
import de.hu.gralog.beans.DisplayChangeListener;

public class GraphDisplayChangeSupport<V,E> implements DisplayChangeListenable<V,E>, DisplayChangeListener<V,E> {

	private final List<DisplayChangeListener<V,E>> listeners = new ArrayList<DisplayChangeListener<V,E>>();
	
	public void registerBean( DisplayChangeListenable<V,E> bean ) {
		bean.addDisplayChangeListener( this );
	}
	
	public void addDisplayChangeListener(DisplayChangeListener<V,E> l) {
		if ( !(listeners.contains( l ) ) )
			listeners.add( l );
	}

	public void removeDisplayChangeListener(DisplayChangeListener<V,E> l) {
		listeners.remove( l );
	}

	protected void fireDisplayChange( Set<V> vertices, Set<E> edges, boolean wholeGraph ) {
		for ( DisplayChangeListener<V,E> l : listeners )
			l.displayChange( vertices, edges, wholeGraph );
	}
	
	public void displayChange( Set<V> vertices, Set<E> edges, boolean wholeGraph ) {
		fireDisplayChange( vertices, edges, wholeGraph );
	}

}
