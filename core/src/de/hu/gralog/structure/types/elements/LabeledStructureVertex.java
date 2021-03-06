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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class defines a LabeledStructureVertex, which is a GrALoG-Vertex that has a
 * label. It can be seen as an example of how to implement vertexTypes in
 * GrALoG.
 * 
 * @author Sebastian
 * 
 */
public class LabeledStructureVertex extends DefaultListenableVertex {

	/**
	 * the only property of this vertex
	 */
	protected String label;

	/**
	 * Every GrALoG-Vertex and -Edge has to have a default-constructor that
	 * takes no arguments since it has to implement {@link java.io.Serializable}.
	 * 
	 * 
	 */
	public LabeledStructureVertex() {
		this("");
	}

	public LabeledStructureVertex(String label) {
		this.label = label;
	}

	/**
	 * The getter-function for the label
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * The setter-function for the label. Note this function has to inform the
	 * PropertyChange- and DisplayChangeListeners that have been registered with this
	 * vertex. This is done via the inherented fields <b>propertyChangeSupport</b>
	 * and <b>displayChangeSupport</b>.
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
	 * {@link #toString()} to display the label for the GrALoG-Vertex.
	 * 
	 */
	public String toString() {
		return label;
	}
        
        
        public Element WriteToXml(Document doc, Element parent, String id) {
            Element v = super.WriteToXml(doc, parent, id);
            v.setAttribute("label", label);
            return v;
        }
}
