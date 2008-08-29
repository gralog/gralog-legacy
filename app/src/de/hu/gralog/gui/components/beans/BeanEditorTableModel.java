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

package de.hu.gralog.gui.components.beans;

import java.beans.PropertyDescriptor;

import de.hu.gralog.app.UserException;
import de.hu.gralog.beans.BeanUtil;
import de.hu.gralog.gui.MainPad;

public class BeanEditorTableModel extends AbstractBeanEditorTableModel {

	private Object bean = null;
	
	public BeanEditorTableModel( ) {
		this( null );
	}
	
	public BeanEditorTableModel( Object bean ) {
		setBean( bean );
	}
	
	public void setBean( Object bean ) {
		if ( bean != null ) {
			this.bean = bean;
			updatePropertyDescriptors();
			fireTableDataChanged();
		}
	}
	
	protected void updatePropertyDescriptors() {
		try {
			propertyDescriptors = BeanUtil.getRWPropertyDescriptors( bean );
		} catch (UserException e) {
			MainPad.getInstance().handleUserException( e );
		}
	}
	
	public int getRowCount() {
		if ( propertyDescriptors == null )
			return 0;
		return propertyDescriptors.size();
	}

	public int getColumnCount() {
		return 2;
	}
	
	@Override
	public String getColumnName(int column) {
		if ( column == 0 )
			return "NAME";
		return "VALUE";
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if ( columnIndex == 0 )
			return false;
		return true;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		PropertyDescriptor propertyDescriptor = propertyDescriptors.get( rowIndex );
		if ( columnIndex == 0 )
			return propertyDescriptor.getName();
		return super.getValueAt( rowIndex, columnIndex );
	}
	
	public PropertyDescriptor getPropertyDescriptor( int row, int col ) {
		if ( col == 0 )
			return null;
		return propertyDescriptors.get( row );
	}
	
	public Object getBean( int row, int col ) {
		return bean;
	}
	
	public String getToolTipText( int row, int col ) {
		PropertyDescriptor pd = getPropertyDescriptor( row, 1 );
		if ( pd == null )
			return null;
		return pd.getShortDescription();
	}
	
}
