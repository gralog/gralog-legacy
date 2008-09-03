/*
 * Created on 2006 by Sebastian Ordyniak
 *
 * Copyright 2006 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of Games.
 *
 * Games is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * Games is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Games; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */

package de.hu.gralog.beans.propertydescriptor;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.util.HashSet;
import java.util.Set;

import de.hu.gralog.beans.propertyeditor.ChooseDependableGraphElementPropertyEditor;

/**
 * This class is a special PropertyDescriptor to define properties of
 * algorithms, graphs, vertices and edges in Gralog. This descriptor allows the
 * user to choose an element ( elements either vertices or edges )of a graph,
 * that is specified as another property of the same Bean, i.e. the bean for the
 * graph, algorithm, vertex or edge. Always make sure that the property this
 * descriptor depends on is of type {@link GralogGraphSupport} and that the
 * property defined by this descriptor has the right type according to whether
 * it is a Vertex or an edge.
 * 
 * @author ordyniak
 * 
 */
public class ChooseDependableGraphElementPropertyDescriptor extends
		PropertyDescriptor implements DependsOnPropertyPropertyDescriptor {

	private final GraphElementFilter graphElementFilter;

	private final PropertyDescriptor dependsOnPropertyDescriptor;

	/**
	 * 
	 * 
	 * 
	 * @param propertyName
	 *            the name of the property this descriptor describes
	 * @param beanClass
	 *            the beanClass this property belongs to
	 * @param dependsonGraphPD
	 *            the propertyDescriptor for the graph property on bean that
	 *            this propertydescriptor depends on, i.e. the property that
	 *            specifies the graph whose elements can be selected
	 * 
	 * @param graphElementFilter
	 *            a {@link GraphElementFilter} that filters the elements that
	 *            the user is able to select from the graph
	 * 
	 * @throws IntrospectionException
	 */
	public ChooseDependableGraphElementPropertyDescriptor(String propertyName,
			Class<?> beanClass, ChooseGraphPropertyDescriptor dependsonGraphPD,
			GraphElementFilter graphElementFilter)
			throws IntrospectionException {
		super(propertyName, beanClass);
		this.dependsOnPropertyDescriptor = dependsonGraphPD;
		this.graphElementFilter = graphElementFilter;
	}

	/**
	 * 
	 * 
	 * @return the {@link GraphElementFilter} used to select the selectable
	 *         elements
	 */
	public GraphElementFilter getGraphElementFilter() {
		return graphElementFilter;
	}

	/**
	 * 
	 * @return the list of properties that this property depends on, i.e.
	 *         informs gralog about all properties whose changes should trigger
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
	 * @return the propertyEditor that gralog should use to edit this property
	 */
	@Override
	public PropertyEditor createPropertyEditor(Object bean) {
		return new ChooseDependableGraphElementPropertyEditor(this, bean);
	}
}
