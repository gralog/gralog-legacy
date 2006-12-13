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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.BeanDescriptor;
import java.beans.Customizer;
import java.beans.PropertyChangeEvent;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import net.infonode.docking.View;
import de.hu.games.app.UserException;
import de.hu.games.graph.GraphWithEditableElements;
import de.hu.games.gui.BeanEditorTableModel;
import de.hu.games.gui.GraphElementEditorTableModel;
import de.hu.games.gui.MainPad;
import de.hu.games.gui.PropertyEditorTable;
import de.hu.games.gui.document.Document;
import de.hu.games.gui.document.DocumentListener;
import de.hu.games.gui.document.GJGraphDocumentContent;
import de.hu.games.jgraph.GJGraph;
import de.hu.games.jgrapht.event.GraphPropertyListener;

public class ElementPropertyEditorView extends View implements EditorDesktopViewListener, DocumentListener {
	
	private static final JLabel NO_PROPERTIES_AVAIBLE = new JLabel( "elements not editable" );
	private Hashtable<Document, Hashtable<Component,JPanel>> panels = new Hashtable<Document, Hashtable<Component,JPanel>>();
		
	public ElementPropertyEditorView(  ) {
		super( "Element Properties", null, NO_PROPERTIES_AVAIBLE );
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
			panel = createPanel( document.getContent().getGraph() );
			components.put( document.getContent().getComponent(), panel );
		}
		return panel;
	}
	
	protected JPanel createPanel( final GJGraph graph ) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout() );
		
		GraphElementEditorPanel elementPanel = new GraphElementEditorPanel( graph );
				
		panel.add( elementPanel, BorderLayout.CENTER );
		return panel;
	}
	
	public void currentDocumentSelectionChanged() {
		Document document = MainPad.getInstance().getDesktop().getCurrentDocument();
		if ( document != null && document.getGraph() != null && document.getGraph().isElementsAndStructureEditable() ) {
			if ( document.getGraph().getGraphT().isVertexEditable() || document.getGraph().getGraphT().isEdgeEditable() ) {
				JPanel panel = getPanel( document );
			
				setComponent( panel );
			}
		}
		else {
			setComponent( NO_PROPERTIES_AVAIBLE );
		}
		repaint();
	}

	public void documentModifiedStatusChanged(Document document) {
		// 
	}

	public void documentComponentReplaced(Document document) {
		panels.remove( document );
		
		currentDocumentSelectionChanged();
	}

	public void documentReverted(Document document) {
		
	}

	private static class GraphElementEditorPanel extends JPanel implements ItemListener {
		
		private final JPanel cards = new JPanel( new CardLayout() );
		private static final String CARD_VERTEXES = "VERTEXES";
		private static final String CARD_EDGES = "EDGES";
		
		public GraphElementEditorPanel( GJGraph graph ) {
			super( new BorderLayout() );
			//setBorder( BorderFactory.createTitledBorder( "Element Properties" ) );
			add( cards, BorderLayout.CENTER );
			
			if ( graph.getGraphT().isVertexEditable() && graph.getGraphT().isEdgeEditable() ) {
				JCheckBox showVertexes = new JCheckBox();
				showVertexes.setSelected( true );
				showVertexes.addItemListener( this );
				showVertexes.setBorder( BorderFactory.createEmptyBorder( 5, 0, 5,0 ) );
				
				JPanel showVertexesPanel = new JPanel();
				showVertexesPanel.add( showVertexes );
				showVertexesPanel.add( new JLabel( "Show Vertex Properties" ) );
				
				add( showVertexesPanel, BorderLayout.SOUTH );
			}
			
			if ( graph.getGraphT().isVertexEditable() ) {
				PropertyEditorTable table = new PropertyEditorTable();
				table.setModel( new GraphElementEditorTableModel( graph ) );
				cards.add( new JScrollPane( table ) , CARD_VERTEXES );
			}
				
			if ( graph.getGraphT().isEdgeEditable() ) {
				PropertyEditorTable table = new PropertyEditorTable();
				table.setModel( new GraphElementEditorTableModel( graph, false ) );
				cards.add( new JScrollPane( table ) , CARD_EDGES );
			}
		}

		public void itemStateChanged(ItemEvent e) {
			CardLayout cl = (CardLayout)(cards.getLayout());
			
			if ( ((JCheckBox)e.getSource()).isSelected() )
				cl.show(cards, CARD_VERTEXES );
			else	
				cl.show(cards, CARD_EDGES );
		}
	}
	
}
