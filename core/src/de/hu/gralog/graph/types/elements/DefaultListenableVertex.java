/*
 * Created on 2008 by Sebastian Ordyniak
 *
 * Copyright 2008 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of Gralog.
 *
 * Gralog is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * Gralog is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Gralog; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */
package de.hu.gralog.graph.types.elements;

import de.hu.gralog.beans.support.DefaultPropertyAndDisplayChangeListenableBean;

/**
 * This class provides the default functionality for vertices in Gralog. Like
 * GraphBeans, Edges and algorithms, vertices in Gralog are JavaBeans.
 * <p>
 * Please refer to {@link de.hu.gralog.beans} for an introduction to JavaBeans
 * and further references.
 * <p>
 * Since vertices, unlike algorithms, are displayed in Gralog they have to
 * provide functionality to listen to changes to their properties. A
 * Gralog-Vertex does this in two ways:
 * 
 * <ul>
 * <li>
 * by implementing PropertyChangeListenable</li>
 * <li>
 * by implementing DisplayChangeListenable</li>
 * </ul>
 * 
 * Fireing a PropertyChangeEvent informs Gralog that a change to a property has
 * occured so that the Gralog-GUI can update its PropertyEditors and register
 * the change with the UndoManager.
 * <p>
 * Fireing a DisplayChangeEvent informs Gralog that it should update the display
 * of the elements specified by this event.
 * <p>
 * Note that since Gralog looks at vertices as JavaBeans all that was said in
 * the description of {de.hu.gralog.beans} also applies to vertices, i.e. you
 * can specify your own Properties, PropertyDescriptors and PropertyEditors for
 * the properties of your vertices, that are then used by the User to edit them.
 * <p>
 * An example-implementation for a vertex that has a label is given by
 * {@link LabeledGraphVertex}.
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
