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
import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.hu.gralog.app.UserException;
import de.hu.gralog.beans.BeanUtil;
import de.hu.gralog.beans.propertydescriptor.DependsOnPropertyPropertyDescriptor;
import de.hu.gralog.gui.MainPad;


public abstract class AbstractBeanEditorTableModel extends AbstractTableModel {
	
	protected List<PropertyDescriptor> propertyDescriptors = null;
	
	public abstract PropertyDescriptor getPropertyDescriptor( int row, int col );
	
	public abstract Object getBean( int row, int col );

	protected void firePropertyChanged( PropertyDescriptor pd ) {
		if ( pd == null )
			return;
		for ( PropertyDescriptor listener : propertyDescriptors ) {
			if ( listener instanceof DependsOnPropertyPropertyDescriptor ) {
				if ( ((DependsOnPropertyPropertyDescriptor)listener).getDependsOnPropertyDescriptors().contains( pd ) )
					fireCellsForPDChanged( listener );
			}
		}
	}
	
	protected void fireCellsForPDChanged( PropertyDescriptor pd ) {
		fireTableCellUpdated( propertyDescriptors.indexOf( pd ), 1 );
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		try {
			BeanUtil.setValue( getBean( rowIndex, columnIndex ), getPropertyDescriptor( rowIndex, columnIndex ), aValue );
		} catch (UserException e) {
			MainPad.getInstance().handleUserException( e );
		}
		fireTableCellUpdated( rowIndex, columnIndex );
		firePropertyChanged( getPropertyDescriptor( rowIndex, columnIndex ) );
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			return BeanUtil.getValue( getBean( rowIndex, columnIndex ), getPropertyDescriptor( rowIndex, columnIndex ) );
		} catch (UserException e) {
			MainPad.getInstance().handleUserException( e );
			return null;
		}
	}
	
	public String getToolTipText( int row, int col ) {
		PropertyDescriptor pd = getPropertyDescriptor( row, col );
		if ( pd == null )
			return null;
		return pd.getShortDescription();
	}
	
}
