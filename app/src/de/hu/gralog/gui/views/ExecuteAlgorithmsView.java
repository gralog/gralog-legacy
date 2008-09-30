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
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import net.infonode.docking.View;
import de.hu.gralog.algorithm.Algorithm;
import de.hu.gralog.algorithm.InvalidPropertyValuesException;
import de.hu.gralog.algorithm.result.AlgorithmResult;
import de.hu.gralog.algorithm.result.AlgorithmResultInfo;
import de.hu.gralog.app.UserException;
import de.hu.gralog.beans.propertydescriptor.ChooseStructurePropertyDescriptor;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.gui.components.HTMLEditorPane;
import de.hu.gralog.gui.components.beans.BeanEditorTableModel;
import de.hu.gralog.gui.components.beans.PropertyEditorTable;
import de.hu.gralog.gui.data.AlgorithmsTree;
import de.hu.gralog.gui.data.AlgorithmsTree.AlgorithmTreeNode;
import de.hu.gralog.gui.data.Plugin.AlgorithmInfo;
import de.hu.gralog.gui.document.Document;
import de.hu.gralog.gui.document.GJGraphDocumentContent;
import de.hu.gralog.jgraph.GJGraph;
import de.hu.gralog.jgraph.GJGraphUtil;
import de.hu.gralog.structure.Structure;

public class ExecuteAlgorithmsView extends View implements ActionListener, ListSelectionListener {

	protected ArrayList<AlgorithmInfo> algorithms;
	protected JTree algorithmChooser;
	protected JEditorPane description = new HTMLEditorPane();
	protected JEditorPane propertyDescription = new HTMLEditorPane();
	protected BeanEditorTableModel algorithmPropertiesTM = new BeanEditorTableModel();
	protected JButton executeButton = new JButton( "Execute Algorithm" );
	protected PropertyEditorTable propertyTable = new PropertyEditorTable(  );
	
	public ExecuteAlgorithmsView() {
		super( "Algorithms", null, new JPanel() );

		algorithms = MainPad.getInstance().getAlgorithms();
		
		JPanel panel = (JPanel)getComponent();
		panel.setLayout( new BoxLayout(  panel, BoxLayout.Y_AXIS ) );
				
		propertyTable.setModel( algorithmPropertiesTM );
		propertyTable.getSelectionModel().addListSelectionListener( this );
		
		JTabbedPane tabPanel = new JTabbedPane();
		//tabPanel.setBorder( BorderFactory.createTitledBorder( "Settings" ) );
		
		JPanel algorithmChooserPanel = new JPanel();
		algorithmChooserPanel.setLayout( new BorderLayout( ) );
		//algorithmChooserPanel.setBorder( BorderFactory.createTitledBorder( "Algorithms" ) );
		
		algorithmChooser =  new JTree( new AlgorithmsTreeModel( MainPad.getAlgorithmTree( algorithms ) ) );
		algorithmChooser.setBorder( BorderFactory.createEmptyBorder( 0, 5, 0, 5) );
		algorithmChooser.addTreeSelectionListener( new AlgorithmChooserListener() );
		algorithmChooser.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
		algorithmChooser.setRootVisible( false );
		algorithmChooser.setShowsRootHandles( true );
		
		executeButton.setActionCommand( "execute" );
		executeButton.addActionListener( this );
		executeButton.setEnabled( false );
		
		algorithmChooserPanel.add( new JScrollPane( algorithmChooser ), BorderLayout.CENTER );
		
		description = new HTMLEditorPane();
		
/*		JSplitPane properties = new JSplitPane( JSplitPane.VERTICAL_SPLIT, new JScrollPane( propertyTable ), new JScrollPane( propertyDescription ) );
		properties.setDividerLocation( 100 );
		properties.setOneTouchExpandable( true );
*/
		tabPanel.addTab( "properties", new JScrollPane( propertyTable ) );
		tabPanel.addTab( "description", new JScrollPane( description ) );
		
		JPanel executePanel = new JPanel( new BorderLayout() );
		executePanel.add( executeButton, BorderLayout.EAST );

		JSplitPane chooseAndProperties = new JSplitPane( JSplitPane.VERTICAL_SPLIT, algorithmChooserPanel, tabPanel );
		chooseAndProperties.setDividerLocation( 100 );
		chooseAndProperties.setOneTouchExpandable( true );
		
		JPanel chooseAndPropertiesPanel = new JPanel( new BorderLayout() );
		chooseAndPropertiesPanel.add( chooseAndProperties, BorderLayout.CENTER );
		
		panel.add( chooseAndPropertiesPanel );
		panel.add( executePanel );
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
	
	public class AlgorithmsTreeModel implements TreeModel {

		private final AlgorithmsTree algorithmsTree;
		
		public AlgorithmsTreeModel( AlgorithmsTree algorithmsTree ) {
			this.algorithmsTree = algorithmsTree;
		}
		
		public Object getRoot() {
			return algorithmsTree.getRoot();
		}

		public Object getChild(Object parent, int index) {
			return ((AlgorithmTreeNode)parent).getChildren().get( index );
		}

		public int getChildCount(Object parent) {
			return ((AlgorithmTreeNode)parent).getChildren().size();
		}

		public boolean isLeaf(Object node) {
			return ((AlgorithmTreeNode)node).isLeave();
		}

		public void valueForPathChanged(TreePath path, Object newValue) {
		}

		public int getIndexOfChild(Object parent, Object child) {
			AlgorithmTreeNode parentTN = (AlgorithmTreeNode)parent;
			int index = 0;
			for ( AlgorithmTreeNode node : parentTN.getChildren() ) {
				if ( node == child )
					return index;
				index++;
			}
			return index;
		}

		public void addTreeModelListener(TreeModelListener l) {
		}

		public void removeTreeModelListener(TreeModelListener l) {
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
	
	private class AlgorithmChooserListener implements TreeSelectionListener {

		public void valueChanged(TreeSelectionEvent e) {
			executeButton.setEnabled( false );
			if ( e.getPath() != null ) {
				AlgorithmTreeNode node = (AlgorithmTreeNode)e.getPath().getLastPathComponent();
				if (  node != null && node.isLeave() ) {
					AlgorithmInfo info = (AlgorithmInfo)node.getData();
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
		
	}

	
	protected void executeAlgorithm( Algorithm algorithm ) {
		try {
			Hashtable<String, Object> algorithmSettings = new Hashtable<String, Object>();
			String algorithmName;
			// erzeuge zun�chst eine Kopie des Algorithmus und aller zugeh�rigen Graphen
			Hashtable<Structure, GJGraph> jgraphs = new Hashtable<Structure, GJGraph>();
			
			Algorithm preparedAlgorithm = algorithm.getClass().newInstance();
			
			BeanInfo beanInfo = Introspector.getBeanInfo( algorithm.getClass() );
			
			algorithmName = beanInfo.getBeanDescriptor().getDisplayName();
			
			PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
			
			for ( PropertyDescriptor propertyDescriptor : properties ) {
				if ( propertyDescriptor.getReadMethod() != null && propertyDescriptor.getWriteMethod() != null ) {
					Object value = propertyDescriptor.getReadMethod().invoke( algorithm, new Object[] {} );
					
					if ( value instanceof Structure ) {
						Structure structure = (Structure)value;
						for ( Document document : MainPad.getInstance().getDesktop().getOpenDocuments() ) {
							if ( document.getContent() instanceof GJGraphDocumentContent && document.getGraph().getGraphT() == structure ) {
								GJGraph jgraph = document.getGraph();
								if ( propertyDescriptor instanceof ChooseStructurePropertyDescriptor && ((ChooseStructurePropertyDescriptor)propertyDescriptor).isMakeCopy() )
									jgraph = GJGraphUtil.getGJGraphCopy( jgraph );
								jgraphs.put( jgraph.getGraphT(), jgraph );
								propertyDescriptor.getWriteMethod().invoke( preparedAlgorithm, new Object[] { jgraph.getGraphT() } );
								algorithmSettings.put( propertyDescriptor.getName(), document.toString() );
							}
						}
					} else {
						propertyDescriptor.getWriteMethod().invoke( preparedAlgorithm, new Object[] { value } );
						if ( value == null )
							value = "null";
						algorithmSettings.put( propertyDescriptor.getName(), value );
					}
				}
			}
			
			AlgorithmResult result = preparedAlgorithm.execute();
			if ( result != null ) {
				if ( result.isOpenContentsAsStructures() ) {
					for( Structure graphSupport : AlgorithmResultInfo.getAllGraphs( result ) ) {
						GJGraph gjGraph = jgraphs.get( graphSupport );
						if ( gjGraph == null )
							gjGraph = new GJGraph( graphSupport );
						MainPad.getInstance().getDesktop().openDocument( gjGraph );
					}
				} else {
					AlgorithmResultInfo info = new AlgorithmResultInfo( algorithmName, algorithmSettings, result, jgraphs );
					MainPad.getInstance().getDesktop().newDocument( info );
				}
			}
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
			Algorithm algorithm = ((AlgorithmInfo)((AlgorithmTreeNode)algorithmChooser.getLastSelectedPathComponent()).getData()).getAlgorithm();
			
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
