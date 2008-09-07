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
import de.hu.gralog.graph.GralogGraphSupport;
import de.hu.gralog.graph.GralogGraphTypeInfo;
import de.hu.gralog.graph.GralogGraphTypeInfoFilter;
import de.hu.gralog.gui.MainPad;

public class ChooseGraphTypeInfoPropertyEditor extends PropertyEditorSupport implements PropertyEditorRendererExtension {

	private final ArrayList<GralogGraphTypeInfo> graphTypeInfos;
	private final GralogGraphTypeInfoFilter graphTypeInfoFilter;
	private final ChooseGraphTypeInfoPropertyEditorComponent editor;
	
	public ChooseGraphTypeInfoPropertyEditor( GralogGraphTypeInfoFilter graphTypeInfoFilter ) {
		super( );
		this.graphTypeInfoFilter = graphTypeInfoFilter;
		graphTypeInfos = getGraphTypeInfoList();
		
		editor = new ChooseGraphTypeInfoPropertyEditorComponent();
	}
	
	protected ArrayList<GralogGraphTypeInfo> getGraphTypeInfoList() {
		ArrayList<GralogGraphTypeInfo> graphTypeInfos = new ArrayList<GralogGraphTypeInfo>();
		for ( GralogGraphTypeInfo graphTypeInfo : MainPad.getInstance().getGraphTypeInfos()) {
			if ( graphTypeInfoFilter == null || graphTypeInfoFilter.filterTypeInfo( graphTypeInfo ) == false )
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
		String[] names = new String[graphTypeInfos.size()];
		for ( int i = 0; i < names.length; i++ )
			names[i] = graphTypeInfos.get( i ).getName();
		return names;
	}
	
	Object getValueForName( String name ) {
		if ( name == null )
			return null;
		for ( GralogGraphTypeInfo graphTypeInfo : graphTypeInfos ) {
			if ( graphTypeInfo.getName().equals( name ) )
				return graphTypeInfo;
		}
		return null;
	}
	
	String getNameForValue( Object value ) {
		if ( value == null )
			return null;
		return ((GralogGraphTypeInfo)value).getName();
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
		
		private GralogGraphTypeInfo getCurrentGraphTypeInfo() {
			if ( MainPad.getInstance().getDesktop().getCurrentDocument() == null )
				return null;
			GralogGraphSupport graphSupport = MainPad.getInstance().getDesktop().getCurrentDocument().getGraph().getGraphT();
			return graphSupport.getTypeInfo();
		}
		public void actionPerformed(ActionEvent e) {
			if ( e.getActionCommand().equals( "SELECTED" ) ) {
				String documentName = (String)chooseCombo.getSelectedItem();
				setValue( getValueForName( documentName ) );
			} else {
				if ( getCurrentGraphTypeInfo() != null ) {
					setValue( getCurrentGraphTypeInfo() );
					chooseCombo.setSelectedItem( getNameForValue( getValue() ) );
				}
			}
			
		}
	}

}
