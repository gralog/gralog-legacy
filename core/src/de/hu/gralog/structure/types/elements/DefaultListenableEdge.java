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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.jgrapht.graph.DefaultEdge;

import de.hu.gralog.beans.event.DisplayChangeListenable;
import de.hu.gralog.beans.event.DisplayChangeListener;
import de.hu.gralog.beans.event.DisplayChangeSupport;
import de.hu.gralog.beans.event.PropertyChangeListenable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * This class should be subclassed by Plugin-Developers to define their own
 * edges. Since edges as vertices are JavaBeans in GrALoG the reader is also
 * refered to the documentation of {@link DefaultListenableVertex}.
 * <p>
 * The only difference between an edge and a vertex in GrALoG is that an Edge
 * has to subclass {org.jgrapht.DefaultEdge}, in order provide
 * source- and target-vertices.
 * 
 * @author Sebastian
 * 
 */
public class DefaultListenableEdge extends DefaultEdge implements
		PropertyChangeListenable, DisplayChangeListenable {

	protected final transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);

	protected final transient DisplayChangeSupport displayChangeSupport = new DisplayChangeSupport();

	public DefaultListenableEdge() {
		super();
		displayChangeSupport.addEdgeForDefaultChange(this);
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.removePropertyChangeListener(l);
	}

	public void addDisplayChangeListener(DisplayChangeListener l) {
		displayChangeSupport.addDisplayChangeListener(l);
	}

	public void removeDisplayChangeListener(DisplayChangeListener l) {
		displayChangeSupport.removeDisplayChangeListener(l);
	}
        
        public Element WriteToXml(Document doc, Element parent, String srcid, String dstid) {
            Element edgenode = doc.createElement("edge");
            edgenode.setAttribute("source", srcid);
            edgenode.setAttribute("target", dstid);
            edgenode.setAttribute("directed","false");
            parent.appendChild(edgenode);
            return edgenode;
        }
}
