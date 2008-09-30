package de.hu.dfa.structure;

import de.hu.gralog.beans.support.DefaultPropertyAndDisplayChangeListenableBean;



public class AutomatonBean<V extends AutomatonVertex> extends DefaultPropertyAndDisplayChangeListenableBean {
	
	private V startVertex;

	public AutomatonBean() {
		
	}
	
	public V getStartVertex() {
		return startVertex;
	}

	public void setStartVertex( V startVertex ) {
		AutomatonVertex oldVertex = this.startVertex;
		if ( oldVertex == startVertex )
			return;
		this.startVertex = startVertex;

		if ( oldVertex != null )
			oldVertex.setStartVertex( false );
		if ( startVertex != null )
			startVertex.setStartVertex( true );
		propertyChangeSupport.firePropertyChange( "startVertex", oldVertex, startVertex );
	}
	
}
