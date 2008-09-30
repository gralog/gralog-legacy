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
package de.hu.gralog.beans.propertydescriptor;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;

import de.hu.gralog.beans.propertyeditor.ChooseStructureElementPropertyEditor;
import de.hu.gralog.structure.Structure;

/**
 * This class is a special PropertyDescriptor that can be used to define
 * properties for algorithms, structures, vertices and edges in GrALoG. This
 * PropertyDescriptor allows the user to choose an element ( an element is
 * either a vertex or an edge ) of the currently active structure. The elements that
 * the user is allowed to choose from can be specified by a
 * {@link StructureElementFilter}. Make sure that the property corresponding to
 * this descriptor has a type compatible to all elements you allow the user to
 * choose from.
 * 
 * @author ordyniak
 * 
 */
public class ChooseStructureElementPropertyDescriptor extends PropertyDescriptor {

	private final StructureElementFilter structureElementFilter;

	/**
	 * Contructs a ChooseStructureElementPropertyDescriptor
	 * 
	 * @param propertyName
	 *            the name of the property
	 * @param beanClass
	 *            the class this property belongs to
	 * @param structureElementFilter
	 *            a {@link StructureElementFilter} that specifies the elements to
	 *            choose from.
	 * @throws IntrospectionException
	 */
	public ChooseStructureElementPropertyDescriptor(String propertyName,
			Class<?> beanClass, StructureElementFilter structureElementFilter)
			throws IntrospectionException {
		super(propertyName, beanClass);
		this.structureElementFilter = structureElementFilter;
	}

	/**
	 * 
	 * @return the {@link PropertyEditor} used by GrALoG to edit this property
	 * 
	 */
	@Override
	public PropertyEditor createPropertyEditor(Object bean) {
		return new ChooseStructureElementPropertyEditor((Structure) bean,
				structureElementFilter);
	}

}
