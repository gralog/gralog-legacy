/*
 * Created on 2006 by Sebastian Ordyniak
 *
 * Copyright 2006 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
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

package de.hu.gralog.algorithm;

import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JTable;

/**
 * This Exception is used to return errors concerning the values of properties
 * for algorithms.
 * 
 * @author ordyniak
 * 
 */
public class InvalidPropertyValuesException extends Exception {

	public static final String PROPERTY_REQUIRED = "This property is required";

	public static final String GREATER_ZERO = "This property should be creater than zero";

	public static final String GREATER_EQUAL_ZERO = "This property should be creater or equal zero";

	protected Vector errors = new Vector();

	private static final Vector COLUMN_NAMES = new Vector();

	static {
		COLUMN_NAMES.add("Property");
		COLUMN_NAMES.add("Errormessage");
	}

	/**
	 * Contructs an InvalidPropertyValuesException without errors. Use
	 * {@link #addPropertyError(String, String)} to add an Error to this
	 * Exception.
	 * 
	 */
	public InvalidPropertyValuesException() {

	}

	/**
	 * Contructs an InvalidPropertyValuesException.
	 * 
	 * @param property
	 *            the name of the property whose value is invalid
	 * @param errormsg
	 *            the errormessage to display to the user
	 */
	public InvalidPropertyValuesException(String property, String errormsg) {
		addPropertyError(property, errormsg);
	}

	/**
	 * Adds an error.
	 * 
	 * @param property
	 *            the name of the property whose value is invalid
	 * @param errormsg
	 *            the errormessage to display to the user
	 */
	public void addPropertyError(String property, String errormsg) {
		Vector error = new Vector();
		error.add(property);
		error.add(errormsg);
		errors.add(error);
	}

	public JComponent getComponent() {
		return new JTable(errors, COLUMN_NAMES);
	}

	/**
	 * 
	 * @return true if this Exception contains a propertyerror
	 */
	public boolean hasErrors() {
		return errors.size() != 0;
	}
}
