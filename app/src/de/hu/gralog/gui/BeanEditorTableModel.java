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

package de.hu.gralog.gui;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

public class BeanEditorTableModel extends AbstractBeanEditorTableModel {

	protected Object bean = null;
	
	public BeanEditorTableModel( ) {
		
	}
	
	public BeanEditorTableModel( Object bean ) {
		setBean( bean );
	}
	
	public void setBean( Object bean ) {
		this.bean = bean;
		updatePropertyDescriptors( bean.getClass() );
		fireTableDataChanged();
	}
	
	public int getColumnCount() {
		return 2;
	}

	public int getRowCount() {
		return propertyDescriptors.size();
	}
	
	protected String getPropertyName( int row, int col ) {
		return propertyDescriptors.get( row ).getDisplayName();
	}

	protected Object getPropertyValue( int row, int col ) {
		Object value = null;
		try {
			value = propertyDescriptors.get( row ).getReadMethod().invoke( bean, new Object[] {} );
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return value;
	}
	
	public Object getValueAt(int row, int col) {
		if ( col == 0 )
			return getPropertyName( row, col );
		return getPropertyValue( row, col );
	}
	
	public String getColumnName(int col) {
		if ( col == 0 )
			return "Name";
		
		return "Value";
	}
	
	public boolean isCellEditable(int row, int col) {
		if ( col == 0 )
			return false;
		return true;
	}

	public void setValueAt(Object value, int row, int col) {
		try {
			propertyDescriptors.get( row ).getWriteMethod().invoke( bean, new Object[] { value } );
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	public PropertyDescriptor getPropertyDescriptor( int row, int col ) {
		if ( col != 1 )
			return null;
		return propertyDescriptors.get( row );
	}
}
