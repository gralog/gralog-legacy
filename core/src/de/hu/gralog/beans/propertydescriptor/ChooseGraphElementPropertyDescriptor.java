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
package de.hu.gralog.beans.propertydescriptor;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;

import de.hu.gralog.beans.propertyeditor.ChooseGraphElementPropertyEditor;
import de.hu.gralog.graph.GralogGraphSupport;

/**
 * This class is a special PropertyDescriptor that can be used to define
 * properties of algorithms, graphs, vertices and edges in GrALoG. This
 * PropertyDescriptor allows the user to choose an element ( an element is
 * either a vertex or an edge ) of the currently active graph. The elements that
 * the user is allowed to choose from can be specified by an
 * {@link GraphElementFilter}. Make sure that the property corresponding to
 * this descriptor has a type compatible to all elements you allow the user to
 * choose from.
 * 
 * @author ordyniak
 * 
 */
public class ChooseGraphElementPropertyDescriptor extends PropertyDescriptor {

	private final GraphElementFilter graphElementFilter;

	/**
	 * Contructs a ChooseGraphElementPropertyDescriptor
	 * 
	 * @param propertyName
	 *            the name of the property
	 * @param beanClass
	 *            the class this property belongs to
	 * @param graphElementFilter
	 *            a {@link GraphElementFilter} that specifies the elements to
	 *            choose from.
	 * @throws IntrospectionException
	 */
	public ChooseGraphElementPropertyDescriptor(String propertyName,
			Class<?> beanClass, GraphElementFilter graphElementFilter)
			throws IntrospectionException {
		super(propertyName, beanClass);
		this.graphElementFilter = graphElementFilter;
	}

	/**
	 * 
	 * @return the {@link PropertyEditor} used by Gralog to edit this property
	 * 
	 */
	@Override
	public PropertyEditor createPropertyEditor(Object bean) {
		return new ChooseGraphElementPropertyEditor((GralogGraphSupport) bean,
				graphElementFilter);
	}

}
