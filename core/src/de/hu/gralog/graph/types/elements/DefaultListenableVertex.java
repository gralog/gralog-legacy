package de.hu.gralog.graph.types.elements;

import de.hu.gralog.beans.support.DefaultPropertyAndDisplayChangeListenableBean;

/**
 * This class provides the default functionality for vertices in Gralog.
 * Like GraphBeans, Edges and algorithms, vertices in Gralog are JavaBeans.
 *  
 * Here we give only a short introduction to JavaBeans if you are not
 * familar with JavaBeans please refer to <a href="{@docRoot}/../pdf/beans.101.pdf">Java
 * Bean Specification</a>.
 * 
 * Since vertices, unlike algorithms, are displayed in Gralog they have
 * to provide functionality to listen to changes to their properties. A
 * Gralog-Vertex does this in two ways:
 * 
 * <li>
 * 		<ul>by implementing PropertyChangeListenable</li>
 * 		<ul>by implementing DisplayChangeListenable</li>
 * </li>
 * 
 * Firering a PropertyChangeEvent informs Gralog that a change to
 * a property has occured so that the Gralog-GUI can update its PropertyEditors
 * and register the change with the UndoManager.
 * 
 * Firering a DisplayChangeEvent informs Gralog that it should update
 * the display of the elements specified by this event.
 * 
 * Note that since Gralog looks at vertices as JavaBeans all that was said
 * in the description of {de.hu.gralog.algorithm.Algorithm} also
 * applies to vertices, i.e. you can specify your own Properties, PropertyDescriptors
 * and PropertyEditors for the properties of your vertices, that are then
 * used by the User to edit them.
 * 
 * An example-implementation for a vertex that has a label is given
 * by {@link LabeledGraphVertex}.
 * 
 * @author Sebastian
 *
 */
public class DefaultListenableVertex extends
		DefaultPropertyAndDisplayChangeListenableBean {

	public DefaultListenableVertex() {
		displayChangeSupport.addVertexForDefaultChange(this);
	}

}
