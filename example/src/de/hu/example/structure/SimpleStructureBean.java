package de.hu.example.structure;

import org.jgrapht.ListenableGraph;

import de.hu.gralog.beans.support.DefaultPropertyAndDisplayChangeListenableBean;
import de.hu.gralog.beans.support.StructureBean;
import de.hu.gralog.structure.Structure;
import de.hu.gralog.structure.types.elements.LabeledStructureEdge;

/**
 * This is an example how to build your own GrALoG-Structure-Beans.
 * Please also refer to the documentation in the GrALoG-Core-Library 
 * {@link de.hu.gralog.structure.StructureBeanFactory}. 
 * 
 * In this example the Structure-Bean stores one vertex, the
 * <b>goodVertex</b>. The example shows how to
 * use {@link de.hu.gralog.beans.propertyeditor.ChooseStructureElementPropertyEditor}
 * to let the user select the <b>goodvertex</b>. It also
 * shows how the Structure-Bean interacts with the underlying
 * JGrapht-Graph and it's elements - in this case it's vertices -
 * so that the user can see the currently selected <b>goodVertex</b>.
 * 
 * Since we use a special PropertyEditor for the
 * <b>goodVertex</b>-property we have to implement a 
 * BeanInfo-class for this Structure-Bean. This is done in
 * {@link SimpleStructureBeanBeanInfo}.
 * 
 * @author Sebastian
 *
 */
public class SimpleStructureBean extends DefaultPropertyAndDisplayChangeListenableBean implements StructureBean<SimpleVertex,LabeledStructureEdge, SimpleStructureBean,ListenableGraph<SimpleVertex,LabeledStructureEdge>> {

	/**
	 * This is a reference to the GrALog-Structure this bean belongs to. It will be
	 * set by GrALoG after initializing the structure via
	 * {@link #setStructure(Structure)}
	 * 
	 */
	private Structure<SimpleVertex, LabeledStructureEdge, SimpleStructureBean, ListenableGraph<SimpleVertex, LabeledStructureEdge>> structure;

	/**
	 * This is the only property of our Bean.
	 * 
	 */
	private SimpleVertex goodVertex;
	
	/**
	 * Always provide a default-constructor to your graphbeans, since
	 * they have to be {@link java.io.Serializable Serializable} 
	 *
	 */
	public SimpleStructureBean() {
		super();
	}

	/**
	 * Please refer to {@link StructureBean#setStructure(de.hu.gralog.structure.Structure)}
	 * for a description of this method.
	 * 
	 * @param structure
	 */
	public void setStructure(Structure<SimpleVertex, LabeledStructureEdge, SimpleStructureBean, ListenableGraph<SimpleVertex, LabeledStructureEdge>> structure) {
		this.structure = structure;
	}

	
	/**
	 * The getter-function for our <b>goodVertex</b>-property 
	 */
	public SimpleVertex getGoodVertex() {
		return goodVertex;
	}

	/**
	 * The setter-function for <b>goodVertex</b>. Apart from informing
	 * GrALoG of the change to this property, this function also
	 * sets the <b>goodVertex</b>-property on the vertices, so
	 * that they can update their display 
	 * ( see {@link SimpleVertex#setTheGoodVertex(boolean)} ). 
	 * 
	 * @param goodVertex
	 */
	public void setGoodVertex(SimpleVertex goodVertex) {
		SimpleVertex oldValue = this.goodVertex;
		this.goodVertex = goodVertex;
		if ( oldValue != null )
			oldValue.setTheGoodVertex( false );
		if ( goodVertex != null )
			goodVertex.setTheGoodVertex( true );
		propertyChangeSupport.firePropertyChange( "goodVertex", oldValue, goodVertex );
	}

}
