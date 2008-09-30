/*
 * Created on 2008 by Sebastian Ordyniak
 *
 * Copyright 2008 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of GrALoG.
 *
 * GrALoG is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * GrALoG is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GrALoG; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */
package de.hu.gralog.structure.types.elements;

import de.hu.gralog.beans.support.DefaultPropertyAndDisplayChangeListenableBean;

/**
 * This class provides the default functionality for vertices in GrALoG. Like
 * Structure-Beans, edges and algorithms, vertices in GrALoG are JavaBeans.
 * <p>
 * Please refer to {@link de.hu.gralog.beans} for an introduction to JavaBeans
 * and further references.
 * <p>
 * Since vertices, unlike algorithms, are displayed in GrALoG they have to
 * provide functionality to listen to changes to their properties. A
 * GrALoG-Vertex does this in two ways:
 * 
 * <ul>
 * <li>
 * by implementing PropertyChangeListenable</li>
 * <li>
 * by implementing DisplayChangeListenable</li>
 * </ul>
 * 
 * Fireing a PropertyChangeEvent informs GrALoG that a change to a property has
 * occured so that the GrALoG-GUI can update it's PropertyEditors and register
 * the change with the UndoManager.
 * <p>
 * Fireing a DisplayChangeEvent informs GrALoG that it should update the display
 * of the elements specified by this event.
 * <p>
 * Note that since GrALoG looks at vertices as JavaBeans all that was said in
 * the description of {de.hu.gralog.beans} also applies to vertices, i.e. you
 * can specify your own Properties, PropertyDescriptors and PropertyEditors for
 * the properties of your vertices, that are then used by the User to edit them.
 * <p>
 * An example-implementation for a vertex that has a label is given in
 * {@link LabeledStructureVertex}.
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
