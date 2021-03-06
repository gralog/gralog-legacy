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
 * for all properties of type {@link de.hu.gralog.structure.Structure}.
 * Use the Descriptor if you want the user to choose among all currently opened
 * graphs in GrALog to define your property. Each Descriptor has an associated
 * {@link de.hu.gralog.structure.StructureTypeInfoFilter}. The
 * {@link de.hu.gralog.structure.StructureTypeInfoFilter} determines which graphs
 * are selectable for that property ( always make sure, that the getter and
 * setter-methods of your property have a compatible type ).
 * 
 * @author ordyniak
 * 
 */
public class ChooseStructurePropertyDescriptor extends PropertyDescriptor {

	private final StructureTypeInfoFilter structureTypeInfoFilter;

	private final boolean makeCopy;

	/**
	 * Contructs a ChooseStructurePropertyDescriptor
	 * 
	 * @param propertyName
	 *            the name of the property
	 * @param beanClass
	 *            the class this property belongs to
	 * @param structureTypeInfoFilter
	 *            the TypeInfoFilter-Object to define which graphs should be
	 *            selectable for this property
	 * @throws IntrospectionException
	 */
	public ChooseStructurePropertyDescriptor(String propertyName,
			Class<?> beanClass, StructureTypeInfoFilter structureTypeInfoFilter)
			throws IntrospectionException {
		this(propertyName, beanClass, structureTypeInfoFilter, true);
	}

	/**
	 * Contructs a ChooseStructurePropertyDescriptor
	 * 
	 * @param propertyName
	 *            the name of the property
	 * @param beanClass
	 *            the class this property belongs to
	 * @param structureTypeInfoFilter
	 *            the TypeInfoFilter-Object to define which graphs should be
	 *            selectable for this property
	 * @param makeCopy
	 *            if <b>false</b> GrALoG passes the choosen graph without
	 *            copying it beforehand. This allows algorithms to make changes
	 *            on graphs that are currently open in GrALoG.
	 * @throws IntrospectionException
	 */
	public ChooseStructurePropertyDescriptor(String propertyName,
			Class<?> beanClass, StructureTypeInfoFilter structureTypeInfoFilter,
			boolean makeCopy) throws IntrospectionException {
		super(propertyName, beanClass);
		this.structureTypeInfoFilter = structureTypeInfoFilter;
		this.makeCopy = makeCopy;
	}

	/**
	 * 
	 * @return the TypeInfo-Object
	 */
	public StructureTypeInfoFilter getStructureType() {
		return structureTypeInfoFilter;
	}

	public boolean isMakeCopy() {
		return makeCopy;
	}
}
