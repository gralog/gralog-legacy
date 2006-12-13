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

package de.hu.games.gui;

import java.awt.GridLayout;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class BeansEditor extends JPanel  {
	
	private PropertyEditorTable table;
	private JScrollPane scrollPane;
	private Hashtable<Object, BeanEditorTableModel> objectToModel = new Hashtable<Object, BeanEditorTableModel>();
	
	public BeansEditor( Object object ) {
		table = new PropertyEditorTable(  );
		setObject( object );
		setLayout( new GridLayout(1,0) );
		scrollPane = new JScrollPane( table );
		add( scrollPane );
	}
	
	public void setObject( Object object ) {
		if ( object != null ) {
			BeanEditorTableModel model = objectToModel.get( object );
			if ( model == null ) {
				model = new BeanEditorTableModel( object );
				objectToModel.put( object, model );
			}
			table.setModel( model );
			scrollPane.setVisible( true );
		} else
			scrollPane.setVisible( false );
	}
	
}
