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

import java.beans.PropertyDescriptor;
import java.util.Set;

/**
 * This interface allows to define PropertyDescriptors whose value depends on
 * other properties in the bean. If this properties get updated by the user
 * Gralog will reddisplay the PropertyEditor corresponding to this
 * PropertyDescriptor.
 * 
 * @author Sebastian
 * 
 */
public interface DependsOnPropertyPropertyDescriptor {
	/**
	 * 
	 * 
	 * @return a set of PropertyDescriptors this Property depends on
	 */
	public Set<PropertyDescriptor> getDependsOnPropertyDescriptors();
}
