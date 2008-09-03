package de.hu.gralog.graph.types.elements;

/**
 * This class defines a LabeledGraphVertex, which is 
 * a Gralog-Vertex that has a label. This
 * can be seen of an example of how to implement vertexTypes in Gralog. 
 * 
 * @author Sebastian
 *
 */
public class LabeledGraphVertex extends DefaultListenableVertex {

	/**
	 * the only property of this vertex
	 */
	protected String label;

	/**
	 * Every Gralog-Vertex and -Edge has to have a default-constructor
	 * that takes no arguments since it has to implement {@link Serializable}.
	 * 
	 *
	 */
	public LabeledGraphVertex() {
		this("");
	}

	public LabeledGraphVertex(String label) {
		this.label = label;
	}

	/**
	 * The getter-function for the label
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * The setter-function for the label. Note this
	 * function has to inform the PropertyChange- and DisplayChangeListeners that
	 * have registered with this vertex. This is done via the inherented
	 * fields <b>propertyChangeSupport</b> and <b>displayChangeSupport</b>.
	 *  
	 * @param label
	 */
	public void setLabel(String label) {
		String oldValue = this.label;
		this.label = label;
		propertyChangeSupport.firePropertyChange("label", oldValue, label);
		displayChangeSupport.fireDisplayChangeDefault();
	}

	/**
	 * The {@link de.hu.gralog.jgraph.cellview.DefaultVertexRenderer} uses
	 * {@link #toString()} to display the label for the Gralog-Vertex.
	 *   
	 */
	public String toString() {
		return label;
	}
}
