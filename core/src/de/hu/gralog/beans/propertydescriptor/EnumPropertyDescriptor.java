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

import de.hu.gralog.beans.propertyeditor.EnumPropertyEditor;

/**
 * This class is a special PropertyDescriptor to define properties for
 * algorithms, graphs, vertices and edges in GrALoG. This descriptor is for
 * properties of value Enum and allows the user to choose one of the avaible
 * values from a comboBox. Please make sure that your property is of the type
 * Enum this descriptor refers to.
 * 
 * @author ordyniak
 * 
 */
public class EnumPropertyDescriptor extends PropertyDescriptor {

	private final Enum[] values;

	/**
	 * Contructs an EnumPropertyDescriptor
	 * 
	 * @param propertyName
	 *            the name of the property
	 * @param beanClass
	 *            the class this property belongs to
	 * @param values
	 *            the values of the Enum-Class you want to display
	 * @throws IntrospectionException
	 */
	public EnumPropertyDescriptor(String propertyName, Class<?> beanClass,
			Enum[] values) throws IntrospectionException {
		super(propertyName, beanClass);
		this.values = values;
	}

	public PropertyEditor createPropertyEditor(Object bean) {
		return new EnumPropertyEditor(values);
	}
}
