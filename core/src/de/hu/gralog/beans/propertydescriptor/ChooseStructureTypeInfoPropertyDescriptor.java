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

import de.hu.gralog.structure.StructureTypeInfoFilter;

/**
 * This class is a special PropertyDescriptor to define properties for
 * algorithms, structures, vertices and edges in GrALoG. This Descriptor can be used
 * for all properties of type {@link de.hu.gralog.structure.StructureTypeInfo}.
 * Use the Descriptor if you want the user to choose among all avaible types of
 * structures that are currently installed in GrALog. You can filter the types of structures that
 * should be available to the user by specifying a
 * {@link de.hu.gralog.structure.StructureTypeInfoFilter} with this descriptor.
 * Please make sure that your property excepts all StructureTypeInfo objects
 * the user can choose from.
 * 
 * @author ordyniak
 * 
 */
public class ChooseStructureTypeInfoPropertyDescriptor extends PropertyDescriptor {

	private StructureTypeInfoFilter structureTypeInfoFilter;

	/**
	 * Contructs a ChooseStructureTypeInfoPropertyDescriptor
	 * 
	 * @param propertyName
	 *            the name of the property
	 * @param beanClass
	 *            the class this property belongs to
	 * @param structureTypeInfoFilter
	 *            a filter for the {@link de.hu.gralog.structure.StructureTypeInfo GralogTypeInfo's}
	 *            that should be avaible to the user.
	 * @throws IntrospectionException
	 */
	public ChooseStructureTypeInfoPropertyDescriptor(String propertyName,
			Class<?> beanClass, StructureTypeInfoFilter structureTypeInfoFilter)
			throws IntrospectionException {
		super(propertyName, beanClass);
		this.structureTypeInfoFilter = structureTypeInfoFilter;
	}

	/**
	 * 
	 * @return the TypeInfo-Object
	 */
	public StructureTypeInfoFilter getStructureTypeInfoFilter() {
		return structureTypeInfoFilter;
	}

}
