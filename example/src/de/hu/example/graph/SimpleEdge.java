package de.hu.example.graph;

import de.hu.gralog.graph.types.elements.DefaultListenableEdge;
import de.hu.gralog.graph.types.elements.LabeledGraphVertex;

/**
 * This example shows you how to define your own
 * Edge in Gralog. It extends {@link DefaultListenableEdge}
 * to provide it with a label, which will be
 * displayed in Gralog. 
 * 
 * @author Sebastian
 *
 */
public class SimpleEdge extends DefaultListenableEdge {

	private String label;
	
	/**
	 * Always provide a default-constructor to your edges, since
	 * they have to be {@link java.io.Serializable Serializable} 
	 *
	 */
	public SimpleEdge() {
		this( "" );
	}
	
	public SimpleEdge( String label ) {
		super();
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	/**
	 * Please see {@link SimpleVertex#setNumber(int)} on
	 * how to define setter-functions to set your properties.
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		String oldLabel = this.label;
		this.label = label;
		propertyChangeSupport.firePropertyChange( "label", oldLabel, label );
		displayChangeSupport.fireDisplayChangeDefault();
	}

	/**
	 * We override {@link LabeledGraphVertex#toString()} because
	 * the {@link de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer} uses
	 * the value of toString to display a label for the edge.
	 * 
	 */
	public String toString() {
		return label;
	}
}
