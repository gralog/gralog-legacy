package de.hu.dfa.structure;

import de.hu.gralog.structure.types.elements.LabeledStructureVertex;

public class AutomatonVertex extends LabeledStructureVertex {

	private boolean acceptingState;
	private boolean startVertex = false;

	public AutomatonVertex() {
		super();
	}

	public AutomatonVertex(String label) {
		super( label );
	}

	public AutomatonVertex(String label, boolean finalState) {
		super( label );
		this.acceptingState = finalState;
	}

	public boolean isAcceptingState() {
		return acceptingState;
	}

	public void setAcceptingState(boolean finalState) {
		boolean oldValue = this.acceptingState;
		this.acceptingState = finalState;
		propertyChangeSupport.firePropertyChange( "acceptingState", oldValue, finalState );
		displayChangeSupport.fireDisplayChangeDefault();
	}
	
	public void setStartVertex( boolean startVertex ) {
		this.startVertex = startVertex;
		displayChangeSupport.fireDisplayChangeDefault();
	}
	
	public boolean isStartVertex() {
		return startVertex;
	}
	
	
}
