package de.hu.gralog.beans;


public interface DisplayChangeListenable<V,E> {
	public void addDisplayChangeListener( DisplayChangeListener<V,E> l );
	
	public void removeDisplayChangeListener( DisplayChangeListener<V,E> l );
}
