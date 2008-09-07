package de.hu.example.graph;

import org.jgrapht.ListenableGraph;

import de.hu.gralog.beans.support.DefaultPropertyAndDisplayChangeListenableBean;
import de.hu.gralog.beans.support.GralogGraphBean;
import de.hu.gralog.graph.GralogGraphSupport;

/**
 * This is an example how to build your own GralogGraphBeans.
 * Please also refer to the documentation in the gralog-core
 * library {@link de.hu.gralog.graph.GraphBeanFactory}. 
 * 
 * In this example the Graphbean stores one vertex, the
 * <b>goodVertex</b>. The example shows how to
 * use {@link de.hu.gralog.beans.propertyeditor.ChooseGraphElementPropertyEditor}
 * to let the user select the <b>goodvertex</b>. It also
 * shows how the GraphBean interacts with the underlying
 * JGrapht-Graph and its elements - in this case its vertices -
 * so that the user can see the <b>goodVertex</b> that
 * is selected by the graphbean.
 * 
 * Since we use a special propertyEditor for the
 * <b>goodVertex</b>-property we have to implement a 
 * BeanInfo-class for this GraphBean. This is done by
 * {@link SimpleGraphBeanBeanInfo}.
 * 
 * @author Sebastian
 *
 */
public class SimpleGraphBean extends DefaultPropertyAndDisplayChangeListenableBean implements GralogGraphBean<SimpleVertex,SimpleEdge, SimpleGraphBean,ListenableGraph<SimpleVertex,SimpleEdge>> {

	/**
	 * This is a reference to the GralogGraph this Bean belongs to. It will be
	 * set by Gralog after initializing the GralogGraph via
	 * {@link #setGraphSupport(GralogGraphSupport)}
	 * 
	 */
	private GralogGraphSupport<SimpleVertex, SimpleEdge, SimpleGraphBean, ListenableGraph<SimpleVertex, SimpleEdge>> graphSupport;

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
	public SimpleGraphBean() {
		super();
	}

	/**
	 * Please refer to {@link GralogGraphBean#setGraphSupport(de.hu.gralog.graph.GralogGraphSupport)}
	 * for a description of this method.
	 * 
	 * @param graphSupport
	 */
	public void setGraphSupport(GralogGraphSupport<SimpleVertex, SimpleEdge, SimpleGraphBean, ListenableGraph<SimpleVertex, SimpleEdge>> graphSupport) {
		this.graphSupport = graphSupport;
	}

	
	/**
	 * The getter-function for our <b>goodVertex</b>-property 
	 */
	public SimpleVertex getGoodVertex() {
		return goodVertex;
	}

	/**
	 * The setter-function for <b>goodVertex</b>. Apart from informing
	 * gralog of the change to this property, this function also
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
