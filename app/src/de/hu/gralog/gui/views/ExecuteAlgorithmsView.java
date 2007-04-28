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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.infonode.docking.View;

import org.jgrapht.Graph;

import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.alg.Algorithm;
import de.hu.gralog.graph.alg.AlgorithmResult;
import de.hu.gralog.graph.alg.AlgorithmResultInfo;
import de.hu.gralog.graph.alg.InvalidPropertyValuesException;
import de.hu.gralog.gui.BeanEditorTableModel;
import de.hu.gralog.gui.HTMLEditorPane;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.gui.PropertyEditorTable;
import de.hu.gralog.gui.data.Plugin.AlgorithmInfo;
import de.hu.gralog.gui.document.Document;
import de.hu.gralog.gui.document.GJGraphDocumentContent;
import de.hu.gralog.jgraph.GJGraph;

public class ExecuteAlgorithmsView extends View implements ActionListener, ListSelectionListener {

	protected ArrayList<AlgorithmInfo> algorithms;
	protected JComboBox algorithmChooser;
	protected JEditorPane description = new HTMLEditorPane();
	protected JEditorPane propertyDescription = new HTMLEditorPane();
	protected BeanEditorTableModel algorithmPropertiesTM = new BeanEditorTableModel();
	protected JButton executeButton = new JButton( "Execute" );
	protected PropertyEditorTable propertyTable = new PropertyEditorTable(  );
	
	public ExecuteAlgorithmsView() {
		super( "Algorithms", null, new JPanel() );

		algorithms = MainPad.getInstance().getAlgorithms();
		
		JPanel panel = (JPanel)getComponent();
		panel.setLayout( new BorderLayout(  ) );
		
		
		propertyTable.setModel( algorithmPropertiesTM );
		propertyTable.getSelectionModel().addListSelectionListener( this );
		
		JTabbedPane tabPanel = new JTabbedPane();
		tabPanel.setBorder( BorderFactory.createTitledBorder( "Settings" ) );
		
		JPanel algorithmChooserPanel = new JPanel();
		algorithmChooserPanel.setLayout( new BoxLayout( algorithmChooserPanel, BoxLayout.X_AXIS ) );
		algorithmChooserPanel.setBorder( BorderFactory.createTitledBorder( "Choose Algorithm" ) );
		
		algorithmChooser = new JComboBox( new AlgorithmsComboBoxModel( algorithms ) );
		algorithmChooser.setBorder( BorderFactory.createEmptyBorder( 0, 5, 0, 5) );
		algorithmChooser.addItemListener( new ComboBoxItemListener() );
		
		executeButton.setActionCommand( "execute" );
		executeButton.addActionListener( this );
		executeButton.setEnabled( false );
		
		algorithmChooserPanel.add( algorithmChooser );
		algorithmChooserPanel.add( executeButton );
		
		description = new HTMLEditorPane();
		
		JSplitPane properties = new JSplitPane( JSplitPane.VERTICAL_SPLIT, new JScrollPane( propertyTable ), new JScrollPane( propertyDescription ) );
		properties.setDividerLocation( 100 );
		properties.setOneTouchExpandable( true );
		tabPanel.addTab( "properties", properties );
		tabPanel.addTab( "description", new JScrollPane( description ) );
		
		panel.add( algorithmChooserPanel, BorderLayout.NORTH );
		panel.add( tabPanel, BorderLayout.CENTER );
	}

	public class AlgorithmsComboBoxModel extends AbstractListModel implements ComboBoxModel {

		protected Object selectedItem;
		protected ArrayList<AlgorithmInfo> algorithms;
		
		public AlgorithmsComboBoxModel( ArrayList<AlgorithmInfo> algorithms ) {
			this.algorithms = algorithms;
		}

		public Object getSelectedItem() {
			return selectedItem;
		}

		public void setSelectedItem(Object anItem) {
			this.selectedItem = anItem;
		}
		
		public Object getElementAt(int index) {
			return algorithms.get( index );
		}

		public int getSize() {
			return algorithms.size();
		}
		
	}
	
	private class ComboBoxItemListener implements ItemListener {

		public void itemStateChanged(ItemEvent e) {
			if ( e.getStateChange() == ItemEvent.SELECTED ) {
				AlgorithmInfo info = (AlgorithmInfo)e.getItem();
				Algorithm algorithm = info.getAlgorithm();
				
				algorithmPropertiesTM.setBean( algorithm );
				
				try {
					description.setText( Introspector.getBeanInfo( info.getType() ).getBeanDescriptor().getShortDescription() );
				} catch (IntrospectionException e1) {
					description.setText( null );
				}
				
				executeButton.setEnabled( true );
			}			
		}
		
	}
	
	protected void executeAlgorithm( Algorithm algorithm ) {
		try {
			Hashtable<String, Object> algorithmSettings = new Hashtable<String, Object>();
			String algorithmName;
			// erzeuge zun�chst eine Kopie des Algorithmus und aller zugeh�rigen Graphen
			Hashtable<Graph, GJGraph> jgraphs = new Hashtable<Graph, GJGraph>();
			
			Algorithm preparedAlgorithm = algorithm.getClass().newInstance();
			
			BeanInfo beanInfo = Introspector.getBeanInfo( algorithm.getClass() );
			
			algorithmName = beanInfo.getBeanDescriptor().getDisplayName();
			
			PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
			
			for ( PropertyDescriptor propertyDescriptor : properties ) {
				if ( propertyDescriptor.getReadMethod() != null && propertyDescriptor.getWriteMethod() != null ) {
					Object value = propertyDescriptor.getReadMethod().invoke( algorithm, null );
					
					if ( value instanceof Graph ) {
						Graph graph = (Graph)value;
						for ( Document document : MainPad.getInstance().getDesktop().getOpenDocuments() ) {
							if ( document.getContent() instanceof GJGraphDocumentContent && document.getGraph().getGraphT() == graph ) {
								GJGraph jgraph = document.getGraph().clone();
								jgraphs.put( jgraph.getGraphT(), jgraph );
								propertyDescriptor.getWriteMethod().invoke( preparedAlgorithm, new Object[] { jgraph.getGraphT() } );
								algorithmSettings.put( propertyDescriptor.getName(), document.toString() );
							}
						}
					} else {
						propertyDescriptor.getWriteMethod().invoke( preparedAlgorithm, new Object[] { value } );
						algorithmSettings.put( propertyDescriptor.getName(), value );
					}
				}
			}
			
			AlgorithmResult result = preparedAlgorithm.execute();
			AlgorithmResultInfo info = new AlgorithmResultInfo( algorithmName, algorithmSettings, result, jgraphs );
			MainPad.getInstance().getDesktop().newDocument( info );
		} catch (IntrospectionException e) {
			MainPad.getInstance().handleUserException( new UserException("could not execute algorithm", e) );
		} catch (IllegalArgumentException e) {
			MainPad.getInstance().handleUserException( new UserException("could not execute algorithm", e) );
		} catch (IllegalAccessException e) {
			MainPad.getInstance().handleUserException( new UserException("could not execute algorithm", e) );
		} catch (InvocationTargetException e) {
			MainPad.getInstance().handleUserException( new UserException("could not execute algorithm", e) );
		} catch (InvalidPropertyValuesException e) {
			MainPad.getInstance().handleUserException( new UserException("could not execute algorithm", e) );
		} catch (InstantiationException e) {
			MainPad.getInstance().handleUserException( new UserException("could not execute algorithm", e) );
		} catch( UserException e ) {
			MainPad.getInstance().handleUserException( e );
		} catch( Throwable e ) {
			MainPad.getInstance().handleUserException( new UserException("error executing algorithm", e) );
		}
		
	}

	public void actionPerformed(ActionEvent e) {
		if ( e.getActionCommand().equalsIgnoreCase( "execute" ) ) {
			Algorithm algorithm = ((AlgorithmInfo)algorithmChooser.getModel().getSelectedItem()).getAlgorithm();
			
			executeAlgorithm( algorithm );
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		int row = propertyTable.getSelectedRow();
		if ( row != -1 ) {
			BeanEditorTableModel model = (BeanEditorTableModel)propertyTable.getModel();
			propertyDescription.setText( model.getPropertyDescriptor( row, 1 ).getShortDescription() );
		}
	}
}
