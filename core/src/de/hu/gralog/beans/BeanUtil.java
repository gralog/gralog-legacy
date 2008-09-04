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
package de.hu.gralog.beans;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import de.hu.gralog.app.UserException;

/**
 * Provides Utility-functions for beans used by gralog. Normally not used by
 * Plugin-Developers.
 * 
 * @author Sebastian
 * 
 */
public class BeanUtil {

	/**
	 * Returns the class underlying the <b>getPropertyType()</b> property of a
	 * {@link java.beans.PropertyDescriptor}.
	 * 
	 * @param propertyDescriptor
	 * @return the Class for this property
	 */
	public static final Class getPropertyTypeClass(
			PropertyDescriptor propertyDescriptor) {
		Class propType = propertyDescriptor.getPropertyType();
		if (propType.isPrimitive()) {
			if (propType.getName().equals("boolean"))
				return Boolean.class;
			if (propType.getName().equals("byte"))
				return Byte.class;
			if (propType.getName().equals("double"))
				return Double.class;
			if (propType.getName().equals("float"))
				return Float.class;
			if (propType.getName().equals("int"))
				return Integer.class;
			if (propType.getName().equals("long"))
				return Long.class;
			if (propType.getName().equals("short"))
				return Short.class;
		}
		return propType;
	}

	/**
	 * Return the value of a property described by the given propertyDescriptor
	 * on bean.
	 * 
	 * @param bean
	 *            the bean to infer the value from
	 * @param propertyDescriptor
	 *            the propertyDescriptor that describes the property whose value
	 *            should be returned, note that bean has to have such a property
	 * @return the value of the property
	 * @throws UserException
	 */
	public static final Object getValue(Object bean,
			PropertyDescriptor propertyDescriptor) throws UserException {
		try {
			return propertyDescriptor.getReadMethod().invoke(bean,
					new Object[] {});
		} catch (IllegalArgumentException e) {
			throw new UserException("unable to get property: "
					+ propertyDescriptor.getName() + " from bean: " + bean, e);
		} catch (IllegalAccessException e) {
			throw new UserException("unable to get property: "
					+ propertyDescriptor.getName() + " from bean: " + bean, e);
		} catch (InvocationTargetException e) {
			throw new UserException("unable to get property: "
					+ propertyDescriptor.getName() + " from bean: " + bean, e);
		}
	}

	/**
	 * Sets the value of the property given by propertyDescriptor on bean.
	 * 
	 * @param bean
	 *            the bean on which the property should be set
	 * @param propertyDescriptor
	 *            the propertyDescriptor describing the property that should be
	 *            set on bean
	 * @param value
	 *            the value for the property - this has to be an object
	 *            corresponding to the property
	 * @throws UserException
	 */
	public static final void setValue(Object bean,
			PropertyDescriptor propertyDescriptor, Object value)
			throws UserException {
		try {
			propertyDescriptor.getWriteMethod().invoke(bean, value);
		} catch (IllegalArgumentException e) {
			throw new UserException("unable not set property: "
					+ propertyDescriptor.getName() + " on bean: " + bean
					+ " with value: " + value, e);
		} catch (IllegalAccessException e) {
			throw new UserException("unable not set property: "
					+ propertyDescriptor.getName() + " on bean: " + bean
					+ " with value: " + value, e);
		} catch (InvocationTargetException e) {
			throw new UserException("unable not set property: "
					+ propertyDescriptor.getName() + " on bean: " + bean
					+ " with value: " + value, e);
		}
	}

	/**
	 * Returns all properties that have Read- and Write-Access on bean.
	 * 
	 * @param bean
	 *            the bean to inspect
	 * @return a list of propertyDescriptors containing all properties that have
	 *         read- and write access on bean.
	 * @throws UserException
	 */
	public static final List<PropertyDescriptor> getRWPropertyDescriptors(
			Object bean) throws UserException {
		List<PropertyDescriptor> propertyDescriptors = new ArrayList<PropertyDescriptor>();
		try {
			PropertyDescriptor[] allPropertyDescriptors = Introspector
					.getBeanInfo(bean.getClass()).getPropertyDescriptors();

			for (int i = 0; i < allPropertyDescriptors.length; i++) {
				PropertyDescriptor propertyDescriptor = allPropertyDescriptors[i];
				if (propertyDescriptor.getReadMethod() != null
						&& propertyDescriptor.getWriteMethod() != null)
					propertyDescriptors.add(propertyDescriptor);
			}

			return propertyDescriptors;
		} catch (IntrospectionException e) {
			throw new UserException("could not introspect bean", e);
		}
	}

	/**
	 * Copies the values of all properties with Read- and Write-Access from
	 * source to target bean. The targetBean has to have all properties of the
	 * sourcebean otherwise an exception will occur.
	 * 
	 * @param sourceBean
	 *            the bean to take the values from
	 * @param targetBean
	 *            the bean to set the values on
	 * @throws UserException
	 */
	public static final void cloneBean(Object sourceBean, Object targetBean)
			throws UserException {
		List<PropertyDescriptor> propertyDescriptors = getRWPropertyDescriptors(sourceBean);
		for (PropertyDescriptor pd : propertyDescriptors)
			setValue(targetBean, pd, getValue(sourceBean, pd));
	}

}
