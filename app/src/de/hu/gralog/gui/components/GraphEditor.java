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

package de.hu.gralog.gui.components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JViewport;

import de.hu.gralog.gui.MainPad;
import de.hu.gralog.gui.MainPad.EditorState;
import de.hu.gralog.gui.MainPad.EditorStateListener;
import de.hu.gralog.gui.action.ChangeEditorStateAction;
import de.hu.gralog.jgraph.GJGraph;

public class GraphEditor extends JPanel implements EditorStateListener {
	
	private GJGraph graph;
	private JPopupMenu popupMenu;
	private ButtonGroup buttonGroup = new ButtonGroup();
	
	public GraphEditor( GJGraph graph ) {
		super();
		this.graph = graph;

		this.add( createScrollPane( graph ) );
		this.setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
		
		popupMenu = createPopupMenu();
		graph.addMouseListener( new PopupListener() );
		
		MainPad.getInstance().addEditorStateListener( this );
	}
	
	private Component createScrollPane( GJGraph g ) {
		JScrollPane spane = (JScrollPane)g.getParent().getParent();
		JViewport port = spane.getViewport();
		port.setScrollMode(JViewport.BLIT_SCROLL_MODE);
		return spane;
	}
	
	private JPopupMenu createPopupMenu() {
		JPopupMenu popup = new JPopupMenu();
		buttonGroup = new ButtonGroup();
		buttonGroup.add( new JToggleButton( MainPad.getInstance().ES_SELECT_ACTION ) );
		buttonGroup.add( new JToggleButton( MainPad.getInstance().ES_PAN_ACTION ) );
		if ( graph.isElementsAndStructureEditable() ) {
			buttonGroup.add( new JToggleButton( MainPad.getInstance().ES_CREATE_VERTEX_ACTION ) );
			buttonGroup.add( new JToggleButton( MainPad.getInstance().ES_CREATE_EDGE_ACTION ) );
		} else 
			MainPad.getInstance().setEditorState( EditorState.SELECT );
		
		buttonGroup.add( new JToggleButton( MainPad.getInstance().ES_MARQUE_ZOOM_ACTION ) );
		buttonGroup.add( new JToggleButton( MainPad.getInstance().ES_INTERACTIVE_ZOOM_ACTION ) );
		editorStateChanged( MainPad.getInstance().getEditorState(), MainPad.getInstance().getEditorState() );

		JPanel pbuttons = new JPanel();
		pbuttons.setLayout( new BoxLayout( pbuttons, BoxLayout.X_AXIS) );

		Enumeration buttons = buttonGroup.getElements();
		while ( buttons.hasMoreElements() ) {	
			JToggleButton button = (JToggleButton)buttons.nextElement();
			button.setPreferredSize( new Dimension( 30, 30 ) );
			button.setText( null );
			pbuttons.add( button );
		}
			
		popup.add( pbuttons );
		popup.addSeparator();
		
		JMenu edit = new JMenu( "Edit");
		edit.add( MainPad.EDIT_COPY_ACTION );
		if ( graph.isElementsAndStructureEditable() ) {
			edit.add( MainPad.EDIT_PASTE_ACTION );
			edit.add( MainPad.EDIT_CUT_ACTION );
			edit.add( MainPad.EDIT_DELETE_ACTION );
		}
		popup.add( edit );

		
		JMenu zoom = new JMenu( "Zoom" );
		zoom.add( MainPad.ZOOM_FIT_IN_CANVAS_ACTION );
		zoom.add( MainPad.ZOOM_11_ACTION );
		zoom.add( MainPad.ZOOM_IN_ACTION );
		zoom.add( MainPad.ZOOM_OUT_ACTION );
		popup.add( zoom );
		return popup;
	}
	
	public GJGraph getGraph() {
		return graph;
	}

	private class PopupListener extends MouseAdapter {
	    public void mousePressed(MouseEvent e) {
	        maybeShowPopup(e);
	    }

	    public void mouseReleased(MouseEvent e) {
	        maybeShowPopup(e);
	    }

	    private void maybeShowPopup(MouseEvent e) {
	        if (e.isPopupTrigger()) {
	            popupMenu.show(e.getComponent(),
	                       e.getX(), e.getY());
	        }
	    }
	}

	public void editorStateChanged( EditorState from, EditorState to ) {
		if ( from == EditorState.SELECT && to != EditorState.SELECT )
			graph.clearSelection();
		graph.setCursor( to.getCursor() );
		Enumeration buttons = buttonGroup.getElements();
		while ( buttons.hasMoreElements() ) {
			JToggleButton button = (JToggleButton)buttons.nextElement();
			if ( ((ChangeEditorStateAction)button.getAction()).getEditorState() == MainPad.getInstance().getEditorState() )
				button.setSelected( true );
		}
		if ( popupMenu != null && popupMenu.isVisible() )
			popupMenu.setVisible( false );
	}
}
