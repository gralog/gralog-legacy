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

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.BeanDescriptor;
import java.beans.Customizer;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.infonode.docking.View;
import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.GralogGraph;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.gui.components.beans.BeanEditorTableModel;
import de.hu.gralog.gui.components.beans.PropertyEditorTable;
import de.hu.gralog.gui.document.Document;
import de.hu.gralog.gui.document.DocumentListener;
import de.hu.gralog.gui.document.GJGraphDocumentContent;
import de.hu.gralog.jgraph.GJGraph;

public class GraphPropertyEditorView extends View implements EditorDesktopViewListener, DocumentListener {
	
	private static final JLabel NO_PROPERTIES_AVAIBLE = new JLabel( "graph not editable" );
	private HashMap<Document, HashMap<Component,JPanel>> panels = new HashMap<Document, HashMap<Component,JPanel>>();
		
	public GraphPropertyEditorView(  ) {
		super( "Graph Properties", null, NO_PROPERTIES_AVAIBLE );
	}
	
	protected JPanel getPanel( Document document ) {
		HashMap<Component, JPanel> components = panels.get( document );
		if ( components == null ) {
			document.addDocumentListener( this );
			components = new HashMap<Component, JPanel>();
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
		try {
			JPanel panel = new JPanel();
			panel.setLayout( new BorderLayout() );

			JPanel graphPanel = new JPanel();
			graphPanel.setLayout( new BorderLayout() );


			BeanDescriptor beanDescriptor = Introspector.getBeanInfo( graph.getGraphT().getClass() ).getBeanDescriptor();

			if ( beanDescriptor != null && beanDescriptor.getCustomizerClass() != null ) {
				Customizer customizer = (Customizer)beanDescriptor.getCustomizerClass().newInstance();
				customizer.setObject( graph.getGraphT() );
				graphPanel.add( (Component)customizer, BorderLayout.CENTER );

			} else {
				PropertyEditorTable graphTable = new PropertyEditorTable();
				graphTable.setModel( new GraphPropertyEditorTableModel( graph.getGraphT() ) );

				graphPanel.add( new JScrollPane( graphTable ), BorderLayout.CENTER );
			}


			panel.add( graphPanel, BorderLayout.CENTER );

			return panel;
		} catch( InstantiationException e ) {
			MainPad.getInstance().handleUserException( new UserException( "error instatiating customizer for graph", e ) );
		} catch (IllegalAccessException e) {
			MainPad.getInstance().handleUserException( new UserException( "error instatiating customizer for graph", e ) );
		} catch (IntrospectionException e) {
			MainPad.getInstance().handleUserException( new UserException( "error getting properties for graph", e ) );
		}
		return null;
	}
	
	public void currentDocumentSelectionChanged() {
		Document document = MainPad.getInstance().getDesktop().getCurrentDocument();
		if ( document != null && document.getGraph() != null && document.getGraph().isElementsAndStructureEditable() && document.getGraph().getGraphT().getGralogSupport().isBeanEditable() ) {
			JPanel panel = getPanel( document );
			
			setComponent( panel );
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
		if ( document.getContent() instanceof GJGraphDocumentContent )
			panels.remove( document );
		
		currentDocumentSelectionChanged();
	}

	public void documentReverted(Document document) {
		
	}

	private static class GraphPropertyEditorTableModel extends BeanEditorTableModel implements PropertyChangeListener {
		
		public GraphPropertyEditorTableModel( GralogGraph graph ) {
			super( graph );
			graph.getGralogSupport().getPropertyChangeSupport().addPropertyChangeListener( this );
		}

		public void propertyChange( PropertyChangeEvent e ) {
			fireTableDataChanged();
		}

	}

	public void documentClosed(Document document) {
		panels.remove( document );
	}

}
