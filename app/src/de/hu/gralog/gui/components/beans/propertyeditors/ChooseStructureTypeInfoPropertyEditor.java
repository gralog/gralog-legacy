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
import de.hu.gralog.structure.Structure;
import de.hu.gralog.structure.StructureTypeInfo;
import de.hu.gralog.structure.StructureTypeInfoFilter;

public class ChooseStructureTypeInfoPropertyEditor extends PropertyEditorSupport implements PropertyEditorRendererExtension {

	private final ArrayList<StructureTypeInfo> structureTypeInfos;
	private final StructureTypeInfoFilter structureTypeInfoFilter;
	private final ChooseGraphTypeInfoPropertyEditorComponent editor;
	
	public ChooseStructureTypeInfoPropertyEditor( StructureTypeInfoFilter structureTypeInfoFilter ) {
		super( );
		this.structureTypeInfoFilter = structureTypeInfoFilter;
		structureTypeInfos = getGraphTypeInfoList();
		
		editor = new ChooseGraphTypeInfoPropertyEditorComponent();
	}
	
	protected ArrayList<StructureTypeInfo> getGraphTypeInfoList() {
		ArrayList<StructureTypeInfo> graphTypeInfos = new ArrayList<StructureTypeInfo>();
		for ( StructureTypeInfo graphTypeInfo : MainPad.getInstance().getStructureTypeInfos()) {
			if ( structureTypeInfoFilter == null || structureTypeInfoFilter.filterTypeInfo( graphTypeInfo ) == false )
				graphTypeInfos.add( graphTypeInfo );
		}
		return graphTypeInfos;
	}
	
	public Component getCustomRenderer() {
		editor.configure();
		return editor;
	}
	
	@Override
	public boolean supportsCustomEditor() {
		return true;
	}

	@Override
	public Component getCustomEditor() {
		editor.configure();
		return editor;
	}

	String[] getNames() {
		String[] names = new String[structureTypeInfos.size()];
		for ( int i = 0; i < names.length; i++ )
			names[i] = structureTypeInfos.get( i ).getName();
		return names;
	}
	
	Object getValueForName( String name ) {
		if ( name == null )
			return null;
		for ( StructureTypeInfo structureTypeInfo : structureTypeInfos ) {
			if ( structureTypeInfo.getName().equals( name ) )
				return structureTypeInfo;
		}
		return null;
	}
	
	String getNameForValue( Object value ) {
		if ( value == null )
			return null;
		return ((StructureTypeInfo)value).getName();
	}
	
	protected class ChooseGraphTypeInfoPropertyEditorComponent extends JPanel implements ActionListener {
		private JComboBox chooseCombo;
		private JButton button;

		public ChooseGraphTypeInfoPropertyEditorComponent(  ) {
			super( new BorderLayout() );
			
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
			chooseCombo.setModel( new DefaultComboBoxModel( getNames() ) );
			chooseCombo.setSelectedItem( getNameForValue( getValue() ) );
		}
		
		public void configure() {
			chooseCombo.setSelectedItem( getNameForValue( getValue() ) );
		}
		
		private StructureTypeInfo getCurrentStructureTypeInfo() {
			if ( MainPad.getInstance().getDesktop().getCurrentDocument() == null )
				return null;
			Structure structure = MainPad.getInstance().getDesktop().getCurrentDocument().getGraph().getGraphT();
			return structure.getTypeInfo();
		}
		public void actionPerformed(ActionEvent e) {
			if ( e.getActionCommand().equals( "SELECTED" ) ) {
				String documentName = (String)chooseCombo.getSelectedItem();
				setValue( getValueForName( documentName ) );
			} else {
				if ( getCurrentStructureTypeInfo() != null ) {
					setValue( getCurrentStructureTypeInfo() );
					chooseCombo.setSelectedItem( getNameForValue( getValue() ) );
				}
			}
			
		}
	}

}
