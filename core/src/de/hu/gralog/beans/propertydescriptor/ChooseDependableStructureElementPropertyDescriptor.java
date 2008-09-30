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
import java.util.HashSet;
import java.util.Set;

import de.hu.gralog.beans.propertyeditor.ChooseDependableStructureElementPropertyEditor;

/**
 * This class is a special PropertyDescriptor to define properties of
 * algorithms, structures, vertices and edges in GrALoG. This descriptor allows the
 * user to choose an element ( elements are either vertices or edges ) of a
 * structure, which is specified as another property of the same Bean, i.e. the bean
 * for the structure, algorithm, vertex or edge. Always make sure that the property
 * this descriptor depends on is of type
 * {@link de.hu.gralog.structure.Structure} and that the property defined
 * by this descriptor has the right type according to whether it is a vertex or
 * an edge.
 * 
 * @author ordyniak
 * 
 */
public class ChooseDependableStructureElementPropertyDescriptor extends
		PropertyDescriptor implements DependsOnPropertyPropertyDescriptor {

	private final StructureElementFilter structureElementFilter;

	private final PropertyDescriptor dependsOnPropertyDescriptor;

	/**
	 * 
	 * 
	 * 
	 * @param propertyName
	 *            the name of the property this descriptor describes
	 * @param beanClass
	 *            the beanClass this property belongs to
	 * @param dependsonStructurePD
	 *            the propertyDescriptor for the structure property on bean that
	 *            this propertydescriptor depends on, i.e. the property that
	 *            specifies the structure whose elements can be selected
	 * 
	 * @param structureElementFilter
	 *            a {@link StructureElementFilter} that filters the elements that
	 *            the user is able to select from the structure
	 * 
	 * @throws IntrospectionException
	 */
	public ChooseDependableStructureElementPropertyDescriptor(String propertyName,
			Class<?> beanClass, ChooseStructurePropertyDescriptor dependsonStructurePD,
			StructureElementFilter structureElementFilter)
			throws IntrospectionException {
		super(propertyName, beanClass);
		this.dependsOnPropertyDescriptor = dependsonStructurePD;
		this.structureElementFilter = structureElementFilter;
	}

	/**
	 * 
	 * 
	 * @return the {@link StructureElementFilter} used to select the selectable
	 *         elements
	 */
	public StructureElementFilter getStructureElementFilter() {
		return structureElementFilter;
	}

	/**
	 * 
	 * @return the list of properties that this property depends on, i.e.
	 *         informs GrALoG about all properties whose changes should trigger
	 *         updates to the PropertyEditor corresponding to this
	 *         PropertyDescriptor
	 */
	public Set<PropertyDescriptor> getDependsOnPropertyDescriptors() {
		HashSet<PropertyDescriptor> set = new HashSet<PropertyDescriptor>();
		set.add(dependsOnPropertyDescriptor);
		return set;
	}

	/**
	 * 
	 * @return the propertyEditor that GrALoG should use to edit this property
	 */
	@Override
	public PropertyEditor createPropertyEditor(Object bean) {
		return new ChooseDependableStructureElementPropertyEditor(this, bean);
	}
}
