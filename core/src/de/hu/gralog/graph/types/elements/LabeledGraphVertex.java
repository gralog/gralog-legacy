package de.hu.gralog.graph.types.elements;


public class LabeledGraphVertex<E> extends DefaultListenableElement<LabeledGraphVertex, E> {

	protected String label;
	
	public LabeledGraphVertex() {
		label = "";
		displayChangeSupport.addVertexForDefaultChange( this );
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		String oldValue = this.label;
		this.label = label;
		propertyChangeSupport.firePropertyChange( "label", oldValue, label );
		displayChangeSupport.fireDisplayChangeDefault( );
	}

	public String toString() {
		return label;
	}
}
