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

package de.hu.gralog.gui.views;

import java.awt.Component;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JScrollPane;

import net.infonode.docking.View;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.gui.components.beans.BeanEditorTableModel;
import de.hu.gralog.gui.components.beans.PropertyEditorTable;
import de.hu.gralog.gui.document.Document;
import de.hu.gralog.gui.document.DocumentListener;
import de.hu.gralog.gui.document.GJGraphDocumentContent;

public class PropertyEditorView extends View implements EditorDesktopViewListener, DocumentListener {

	private static final JLabel NO_PROPERTIES_AVAIBLE = new JLabel( "no propertyview avaible" );
	private HashMap<Document, HashMap<Component, JScrollPane>> tables = new HashMap<Document, HashMap<Component, JScrollPane>>();
	
	public PropertyEditorView() {
		super( "PropertyEditor", null, NO_PROPERTIES_AVAIBLE );
	}

	protected JScrollPane getTable( Document document ) {
		HashMap<Component, JScrollPane> components = tables.get( document );
		if ( components == null ) {
			document.addDocumentListener( this );
			components = new HashMap<Component, JScrollPane>();
			tables.put( document, components );
		}
		JScrollPane scrollPane = components.get( document.getContent().getComponent() );
		if ( scrollPane == null ) {
			PropertyEditorTable table = new PropertyEditorTable();
			table.setModel( new BeanEditorTableModel( document.getGraph() ) );
			scrollPane = new JScrollPane( table );
			components.put( document.getContent().getComponent(), scrollPane );
		}
		
		return scrollPane;

	}
	
	public void currentDocumentSelectionChanged() {
		Document document = MainPad.getInstance().getDesktop().getCurrentDocument();
		if ( document != null && document.getGraph() != null && document.getGraph().isEditable() ) {
			JScrollPane scrollPane = getTable( document );
			
			setComponent( scrollPane );
		} else
			setComponent( NO_PROPERTIES_AVAIBLE );
		repaint();
	}

	public void documentComponentReplaced(Document document) {
		if ( document.getContent() instanceof GJGraphDocumentContent )
			tables.remove( document );
		
		currentDocumentSelectionChanged();
	}

	public void documentModifiedStatusChanged(Document document) {
		// TODO Auto-generated method stub
		
	}

	public void documentReverted(Document document) {
		// 
		
	}

	public void documentClosed(Document document) {
		tables.remove( document );
	}

	
}
