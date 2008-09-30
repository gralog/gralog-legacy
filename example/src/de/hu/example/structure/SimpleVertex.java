package de.hu.example.structure;

import de.hu.gralog.structure.types.elements.LabeledStructureVertex;

/**
 * This example shows you how to define your
 * own simple vertex that extends
 * {@link de.hu.gralog.structure.types.elements.LabeledStructureVertex}
 * to have a label and adds a property <b>number</b> to it.
 * 
 * It also shows how to define a property that is only
 * used internally by this vertex, i.e. that is not persistent
 * and should not be editable by the user. The property
 * is <b>isTheGoodVertex</b> that is used by this vertex
 * and it's renderer {@link SimpleVertexVertexRenderer} but
 * should not be editable to the user - since it will
 * be edited via the {@link SimpleStructureBean}. We archive this by:
 * 
 * <ul>
 * 		<li>Not fireing {@link java.beans.PropertyChangeEvent} 
 * 		for this property ( see {@link #setTheGoodVertex(boolean)} ).</li>
 * 		<li>Defining a BeanInfo-class for this vertex,
 * 		that does not mention this property ( see {@link SimpleVertexBeanInfo} ).</li>
 * <ul>
 *    
 * @author Sebastian
 *
 */
public class SimpleVertex extends LabeledStructureVertex {

	private int number;
	private boolean isTheGoodVertex;
	
	/**
	 * Always provide a default-constructor to your vertices, since
	 * they have to be {@link java.io.Serializable Serializable} 
	 *
	 */
	public SimpleVertex() {
		this( "", 0 );
	}

	public SimpleVertex(String label, int number) {
		super( label );
		this.number = number;
	}

	public int getNumber() {
		return number;
	}

	/**
	 * Sets the number. Make sure to fire a 
	 * {@link java.beans.PropertyChangeEvent} whenever one
	 * of the properties of your vertex changes - in order
	 * to allow GrALoG to provide Undo-Support for the
	 * properties of your vertex. Also make
	 * sure that you fire a DisplayChangeEvent, when
	 * you know that your vertex has to be repainted by GrALoG.
	 * In this case we use {@link de.hu.gralog.beans.event.DisplayChangeSupport#fireDisplayChangeDefault()} 
	 * to do this. This is only possible because the default-constructor 
	 * of {@link de.hu.gralog.structure.types.elements.DefaultListenableVertex}
	 * sets up <b>displayChangeSupport</b> to fire an event
	 * that informs GrALoG only to update this vertex, when
	 * you call {@link de.hu.gralog.beans.event.DisplayChangeSupport#fireDisplayChangeDefault()}.
	 * So make sure to call it's constructor in the
	 * constructor for your vertex.
	 *  
	 * @param number
	 */
	public void setNumber(int number) {
		int oldNumber = this.number;
		this.number = number;
		propertyChangeSupport.firePropertyChange( "number", oldNumber, number );
		displayChangeSupport.fireDisplayChangeDefault();
	}
	
	public boolean isTheGoodVertex() {
		return isTheGoodVertex;
	}

	/**
	 * The setter-function for <b>isTheGoodVertex</b>. 
	 * Since this property is entirely administered by
	 * {@link SimpleStructureBean} we do not
	 * need to inform GrALoG about changes to this property. What
	 * we need to do however is to update the display for
	 * this vertex.
	 * 
	 * @param isTheGoodVertex
	 */
	public void setTheGoodVertex(boolean isTheGoodVertex) {
		this.isTheGoodVertex = isTheGoodVertex;
		displayChangeSupport.fireDisplayChangeDefault();
	}

	/**
	 * We override {@link LabeledStructureVertex#toString()} because
	 * the {@link de.hu.gralog.jgraph.cellview.DefaultVertexRenderer} uses
	 * the value of toString to display a label for the vertex.
	 * 
	 */
	public String toString() {
		return super.toString() + " : " + number;
	}

}
