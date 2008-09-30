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

package de.hu.gralog.gui.components.beans.propertyeditors;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyEditorSupport;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import de.hu.gralog.beans.propertyeditor.PropertyEditorRendererExtension;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.gui.document.Document;
import de.hu.gralog.gui.document.GJGraphDocumentContent;
import de.hu.gralog.structure.Structure;
import de.hu.gralog.structure.StructureTypeInfoFilter;

public class ChooseStructurePropertyEditor extends PropertyEditorSupport implements PropertyEditorRendererExtension {

	private ArrayList<Document> documents = new ArrayList<Document>();
	private StructureTypeInfoFilter structureTypeInfoFilter = null;
	private ChooseStructurePropertyEditorComponent editor;
	
	public ChooseStructurePropertyEditor( StructureTypeInfoFilter structureTypeInfoFilter ) {
		super( );
		this.structureTypeInfoFilter = structureTypeInfoFilter;
		editor = new ChooseStructurePropertyEditorComponent();
	}
	
	protected void updateDocuments() {
		documents.clear();
		for ( Document document : MainPad.getInstance().getDesktop().getOpenDocuments() ) {
			if ( document.getContent() instanceof GJGraphDocumentContent ) {
				GJGraphDocumentContent content = (GJGraphDocumentContent)document.getContent();
				if ( !structureTypeInfoFilter.filterTypeInfo( content.getGraph().getGraphT().getTypeInfo() ) )
					documents.add( document );
			}
		}
	}

	public Component getCustomRenderer() {
		editor.updateDocuments();
		return editor;
	}
	
	@Override
	public boolean supportsCustomEditor() {
		return true;
	}

	@Override
	public Component getCustomEditor() {
		editor.updateDocuments();
		return editor;
	}

	Object getValueForName( String name ) {
		if ( name == null )
			return null;
		for ( Document document : documents ) {
			if ( document.getName().equals( name ) )
				return document.getGraph().getGraphT();
		}
		return null;
	}
	
	String getNameForValue( Object value ) {
		if ( value == null )
			return null;
		for ( Document document : documents ) {
			if ( document.getGraph().getGraphT() == value )
				return document.getName();
		}
		return null;
	}
	
	protected class ChooseStructurePropertyEditorComponent extends JPanel implements ActionListener {
		private JComboBox chooseCombo;
		private JButton button;

		public ChooseStructurePropertyEditorComponent(  ) {
			super( new BorderLayout() );
			
			ChooseStructurePropertyEditor.this.updateDocuments();
			chooseCombo = new JComboBox(  );
			comboBoxUpdate();
			chooseCombo.addActionListener( this );
			chooseCombo.setActionCommand( "SELECTED" );
			add( chooseCombo, BorderLayout.CENTER );
			
			button = new JButton( "C" );
			button.setActionCommand( "CURRENT" );
			button.addActionListener( this );
			add( button, BorderLayout.EAST );
		}

		private void comboBoxUpdate() {
			String[] documentNames = new String[documents.size()];
			for ( int i = 0; i < documentNames.length; i++ )
				documentNames[i] = documents.get( i ).getName();
			chooseCombo.setModel( new DefaultComboBoxModel( documentNames ) );
			chooseCombo.setSelectedItem( getNameForValue( getValue() ) );
		}
		
		private void updateDocuments() {
			ChooseStructurePropertyEditor.this.updateDocuments();
			comboBoxUpdate();
		}
		
		public void setValueFromEditor() {
			chooseCombo.setSelectedItem( getNameForValue( getValue() ) );
		}
		
		private Structure getCurrentStructure() {
			if ( MainPad.getInstance().getDesktop().getCurrentDocument() == null )
				return null;
			Structure graph = MainPad.getInstance().getDesktop().getCurrentDocument().getGraph().getGraphT();
			if ( graph != null && !structureTypeInfoFilter.filterTypeInfo( graph.getTypeInfo() ) )
				return graph;
			return null;
		}
		public void actionPerformed(ActionEvent e) {
			if ( e.getActionCommand().equals( "SELECTED" ) ) {
				String documentName = (String)chooseCombo.getSelectedItem();
				setValue( getValueForName( documentName ) );
			} else {
				if ( getCurrentStructure() != null ) {
					setValue( getCurrentStructure() );
					chooseCombo.setSelectedItem( getNameForValue( getValue() ) );
				}
			}
			
		}

		public void currentDocumentSelectionChanged() {
			updateDocuments();
		}

	}

}
