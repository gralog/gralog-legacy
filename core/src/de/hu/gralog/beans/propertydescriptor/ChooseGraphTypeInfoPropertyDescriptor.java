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

import de.hu.gralog.graph.types.GralogGraphTypeInfoFilter;

/**
 * This class is a special PropertyDescriptor for properties of algorithms in GrALoG.
 * This Descriptor can be used for all properties of type graph. Use the Descriptor
 * if you want the user to choose among all currently opened graphs in GrALog to define
 * your property. Each Descriptor has to have a graphType. The graphType determines which
 * graphs are selectable for that property ( always make sure, that the getter and setter-methods
 * of your property have a compatible type ). 
 * 
 * @author ordyniak
 *
 */
public class ChooseGraphTypeInfoPropertyDescriptor extends PropertyDescriptor {

	private GralogGraphTypeInfoFilter graphTypeInfoFilter;
	
	/**
	 * Contructs a ChooseGraphTypeInfoPropertyDescriptor
	 * 
	 * @param propertyName the name of the property
	 * @param beanClass the class this property belongs to
	 * @param graphTypeInfoFilter 
	 * @throws IntrospectionException
	 */
	public ChooseGraphTypeInfoPropertyDescriptor(String propertyName, Class<?> beanClass, GralogGraphTypeInfoFilter graphTypeInfoFilter )
			throws IntrospectionException {
		super(propertyName, beanClass);
		this.graphTypeInfoFilter = graphTypeInfoFilter;
	}

	/**
	 *  
	 * @return the TypeInfo-Object
	 */
	public GralogGraphTypeInfoFilter getGraphTypeInfoFilter() {
		return graphTypeInfoFilter;
	}
	
}
