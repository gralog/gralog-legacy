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

package de.hu.gralog.beans.propertyeditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import de.hu.gralog.app.UserException;
import de.hu.gralog.beans.BeanUtil;
import de.hu.gralog.beans.propertydescriptor.ChooseDependableGraphElementPropertyDescriptor;
import de.hu.gralog.beans.propertydescriptor.GraphElementFilter;
import de.hu.gralog.graph.GralogGraph;

public class ChooseDependableGraphElementPropertyEditor extends PropertyEditorSupport implements PropertyEditorRendererExtension {

	private final GraphElementFilter graphElementFilter;
	private final PropertyDescriptor graphPD;
	private final Object bean;
	
	private ChooseDependableElementPropertyEditorComponent editor;
	
	public ChooseDependableGraphElementPropertyEditor( ChooseDependableGraphElementPropertyDescriptor pd, Object bean ) {
		super( );
		this.graphElementFilter = pd.getGraphElementFilter();
		this.graphPD = pd.getDependsOnPropertyDescriptors().iterator().next();
		this.bean = bean;
		editor = new ChooseDependableElementPropertyEditorComponent();
	}
	
	public Component getCustomRenderer() {
		editor.updateElements();
		return editor;
	}
	
	@Override
	public boolean supportsCustomEditor() {
		return true;
	}

	@Override
	public Component getCustomEditor() {
		editor.updateElements();
		return editor;
	}

	private GralogGraph getGraph() {
		try {
			return (GralogGraph)BeanUtil.getValue( bean, graphPD );
		} catch (UserException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	List<Object> getChoosableElements() {
		ArrayList<Object> chooseableElements = new ArrayList<Object>();
		GralogGraph graph = getGraph();
		if ( getGraph() == null )
			return chooseableElements;
		for ( Object vertex : graph.vertexSet() ) {
			if ( !graphElementFilter.filterElement( graph, vertex ) )
				chooseableElements.add( vertex );
		}
		for ( Object edge : graph.edgeSet() ) {
			if ( !graphElementFilter.filterElement( graph, edge ) )
				chooseableElements.add( edge );
		}
		return chooseableElements;
	}
	
	Object getSelectedElement() {
		GralogGraph graph = getGraph();
		if ( graph == null )
			return null;
		Set vertices = graph.getGralogSupport().getGraphSelectionSupport().getSelectedVertices();
		if ( vertices != null && vertices.size() == 1 && !graphElementFilter.filterElement( graph, vertices.iterator().next() ) )
			return vertices.iterator().next();
		Set edges = graph.getGralogSupport().getGraphSelectionSupport().getSelectedEdges();
		if ( edges != null && edges.size() == 1 && !graphElementFilter.filterElement( graph, edges.iterator().next() ) )
			return edges.iterator().next(  );
		return null;
	}
	
	protected class ChooseDependableElementPropertyEditorComponent extends JPanel implements ActionListener {
		private JComboBox chooseCombo;
		private JButton button;

		public ChooseDependableElementPropertyEditorComponent(  ) {
			super( new BorderLayout() );
			
			chooseCombo = new JComboBox(  );
			comboBoxUpdate();
			chooseCombo.addActionListener( this );
			chooseCombo.setActionCommand( "SELECTED" );
			add( chooseCombo, BorderLayout.CENTER );
			
			button = new JButton( "S" );
			button.setActionCommand( "CURRENT" );
			button.addActionListener( this );
			add( button, BorderLayout.EAST );
		}

		private void comboBoxUpdate() {
			chooseCombo.setModel( new DefaultComboBoxModel( getChoosableElements().toArray() ) );
			chooseCombo.setSelectedItem( getValue() );
		}
		
		private void updateElements() {
			comboBoxUpdate();
		}
		
		public void actionPerformed(ActionEvent e) {
			if ( e.getActionCommand().equals( "SELECTED" ) ) {
				setValue( chooseCombo.getSelectedItem() );
			} else {
				if ( getSelectedElement() != null ) {
					setValue( getSelectedElement() );
					chooseCombo.setSelectedItem( getValue() );
				}
			}
			
		}

	}

}
