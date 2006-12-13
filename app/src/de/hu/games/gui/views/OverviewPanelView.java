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

package de.hu.games.gui.views;

import java.awt.Component;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.infonode.docking.View;
import de.hu.games.gui.MainPad;
import de.hu.games.gui.OverviewPanel;
import de.hu.games.gui.document.Document;
import de.hu.games.gui.document.DocumentListener;
import de.hu.games.gui.document.GJGraphDocumentContent;

public class OverviewPanelView extends View implements EditorDesktopViewListener, DocumentListener {

	private static final JLabel NO_OVERVIEW_AVAIBLE = new JLabel( "no overview avaible" );
	private Hashtable<Document, Hashtable<Component,JPanel>> panels = new Hashtable<Document, Hashtable<Component,JPanel>>();
	
	public OverviewPanelView() {
		super("Overview", null, NO_OVERVIEW_AVAIBLE );
	}

	public void scaleChanged() {
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				((OverviewPanel)((JPanel)getComponent()).getComponent( 0 )).graphLayoutCacheChanged( null );
			}
		});
	}

	protected JPanel getPanel( Document document ) {
		Hashtable<Component, JPanel> components = panels.get( document );
		if ( components == null ) {
			document.addDocumentListener( this );
			components = new Hashtable<Component, JPanel>();
			panels.put( document, components );
		}
		JPanel panel = components.get( document.getContent().getComponent() );
		if ( panel == null ) {
			panel = OverviewPanel.createOverviewPanel( document.getGraph() );
			components.put( document.getContent().getComponent(), panel );
		}
		
		return panel;
	}
	
	public void currentDocumentSelectionChanged() {
		Document document = MainPad.getInstance().getDesktop().getCurrentDocument();
		
		if ( document != null && document.getGraph() != null ) {
			JPanel panel = getPanel( document );

			setComponent( panel );
		} else
			setComponent( NO_OVERVIEW_AVAIBLE );
		repaint();
	}

	public void documentComponentReplaced( Document document ) {
		panels.remove( document );
				
		currentDocumentSelectionChanged();
	}

	public void documentModifiedStatusChanged(Document document) {
		//
	}

	public void documentReverted(Document document) {
		// 
		
	}
}
