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
import java.util.ArrayList;

import javax.swing.event.TableModelEvent;

import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.DefaultGraphCell;

import de.hu.gralog.app.UserException;
import de.hu.gralog.beans.BeanUtil;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.jgraph.GJGraph;

public class GraphElementEditorTableModel extends AbstractBeanEditorTableModel implements GraphSelectionListener, GraphModelListener {

	private GJGraph graph;
	private boolean vertexes;
	private ArrayList<DefaultGraphCell> elements = new ArrayList<DefaultGraphCell>();
	
	public GraphElementEditorTableModel( GJGraph graph ) {
		this( graph, true );
	}
	
	public GraphElementEditorTableModel( GJGraph graph, boolean vertexes ) {
		this.graph = graph;
		this.vertexes = vertexes;
		
		graph.addGraphSelectionListener( this );
		graph.getModel().addGraphModelListener( this );
		
		updateCellsAndProperties();
	}
	
	protected void updateCellsAndProperties() {
		updateCells( false );
		updatePropertyDescriptors( true );
	}
	
	protected void updateCells( boolean fireTableDataChanged ) {
		Object [] cells = graph.getSelectionCells();
		elements.clear();
		
		for ( int i = 0; i < cells.length; i++) {
			DefaultGraphCell cell = (DefaultGraphCell)cells[i];
			if ( graph.getModel().isEdge( cell ) && ! vertexes )
				elements.add( cell );
			if ((! ( graph.getModel().isEdge( cell ) || graph.getModel().isPort( cell ))) && vertexes )
				elements.add( cell );
		}
		if ( fireTableDataChanged )
			fireTableDataChanged();
	}
	
	protected void updatePropertyDescriptors( boolean fireTableChanged ) {
		try {
			if ( vertexes )
				propertyDescriptors = BeanUtil.getRWPropertyDescriptors( graph.getGraphT().createVertex() );
			else
				propertyDescriptors = BeanUtil.getRWPropertyDescriptors( graph.getGraphT().getEdgeFactory().createEdge( graph.getGraphT().createVertex(), graph.getGraphT().createVertex() ) );
		} catch (UserException e) {
			MainPad.getInstance().handleUserException( e );
		}
		
		if ( fireTableChanged )
			fireTableChanged( new TableModelEvent( this, TableModelEvent.HEADER_ROW) );
	}

	public int getColumnCount() {
		return propertyDescriptors.size();
	}

	public int getRowCount() {
		return elements.size();
	}

	public String getColumnName(int col) {
		return propertyDescriptors.get( col ).getDisplayName();
	}
	
	public boolean isCellEditable(int row, int col) {
		return true;
	}

	public void valueChanged(GraphSelectionEvent e) {
		updateCells( true );
	}

	public void graphChanged(GraphModelEvent e) {
		fireTableDataChanged();
	}

	@Override
	public PropertyDescriptor getPropertyDescriptor(int row, int col) {
		return propertyDescriptors.get( col );
	}
	
	public Object getBean( int row, int col ) {
		return elements.get( row ).getUserObject();
	}
}
