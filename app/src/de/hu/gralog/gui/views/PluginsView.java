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
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
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
import de.hu.gralog.gui.data.Plugin;
import de.hu.gralog.gui.data.Plugin.AlgorithmInfo;
import de.hu.gralog.gui.data.PluginsTree.PluginsTreeNode;
import de.hu.gralog.gui.data.PluginsTree.PluginsTreeNodeType;
import de.hu.gralog.gui.document.Document;
import de.hu.gralog.gui.document.GJGraphDocumentContent;
import de.hu.gralog.jgraph.GJGraph;
import de.hu.gralog.jgraph.GJGraphUtil;
import de.hu.gralog.structure.Structure;
import de.hu.gralog.structure.StructureTypeInfo;

public class PluginsView extends View {

	public PluginsView() {
		super( "Plugins", null, new JPanel() );

		JPanel panel = (JPanel)getComponent();
		panel.setLayout( new BorderLayout() );

		panel.add( new PluginsPanel(), BorderLayout.CENTER );
	}

	static class PluginsPanel extends JPanel implements TreeSelectionListener {
		private final ContentPanel contentPanel = new ContentPanel();
		
		PluginsPanel() {
			super( new BorderLayout() );
		
			JSplitPane treeAndContent = new JSplitPane( JSplitPane.VERTICAL_SPLIT, new PluginsTreePanel( this ), contentPanel );
			treeAndContent.setDividerLocation( 100 );
			treeAndContent.setOneTouchExpandable( true );
			
			add( treeAndContent, BorderLayout.CENTER );
		}

		public void valueChanged(TreeSelectionEvent e) {
			TreePath path = e.getPath();
			if ( path != null )
				contentPanel.setContent( path );
		}
	}
	
	static class PluginsTreePanel extends JPanel {
		PluginsTreePanel( TreeSelectionListener listener ) {
			super( new BorderLayout() );
//			setBorder( BorderFactory.createTitledBorder( "Plugins" ) );
			
			JTree pluginsTree =  new JTree( new PluginsTreeModel( MainPad.getInstance().getPluginsTree(  ) ) );
			pluginsTree.setBorder( BorderFactory.createEmptyBorder( 0, 5, 0, 5) );
			pluginsTree.addTreeSelectionListener( listener );
			pluginsTree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
			pluginsTree.setRootVisible( false );
			pluginsTree.setShowsRootHandles( true );
			
			add( new JScrollPane( pluginsTree ), BorderLayout.CENTER );
		}
	}
	
	static class ContentPanel extends JPanel {
		private final JPanel cards = new JPanel( new CardLayout() );
		private final GraphPanel graphPanel = new GraphPanel();
		private final PluginPanel pluginPanel = new PluginPanel();
		private final AlgorithmPanel algorithmPanel = new AlgorithmPanel();
		private final EmptyPanel emptyPanel = new EmptyPanel();
		
		private static final String EMPTY_PANEL = "Empty Panel";
		private static final String GRAPH_PANEL = "Graph Panel";
		private static final String PLUGIN_PANEL = "Plugin Panel";
		private static final String ALGORITHM_PANEL = "Algorithm Panel";
		
		ContentPanel() {
			super( new BorderLayout() );
//			setBorder( BorderFactory.createTitledBorder( "Content" ) );
			
			cards.add( emptyPanel, EMPTY_PANEL );
			cards.add( graphPanel, GRAPH_PANEL );
			cards.add( pluginPanel, PLUGIN_PANEL );
			cards.add( algorithmPanel, ALGORITHM_PANEL );
			
			add( cards, BorderLayout.CENTER );
			
		}
		
		public void setContent( TreePath path ) {
			PluginsTreeNode content = (PluginsTreeNode)path.getLastPathComponent();
			if ( content.getType() == PluginsTreeNodeType.STRUCTURES || content.getType() == PluginsTreeNodeType.ALGORITHMS )
				content = (PluginsTreeNode)path.getPathComponent( 1 );
			switch ( content.getType() ) {
			case PLUGIN:
				pluginPanel.setPlugin( (Plugin)content.getData() );
				((CardLayout)cards.getLayout()).show( cards, PLUGIN_PANEL );
				break;
			case STRUCTURE:
				graphPanel.setGraphType( (StructureTypeInfo)content.getData() );
				((CardLayout)cards.getLayout()).show( cards, GRAPH_PANEL );
				break;
			case ALGORITHM:
				algorithmPanel.setAlgorithmInfo( (AlgorithmInfo)content.getData() );
				((CardLayout)cards.getLayout()).show( cards, ALGORITHM_PANEL );
				break;
			}
		}
		
		class PluginPanel extends JPanel {
			Plugin plugin;
			JEditorPane description = new HTMLEditorPane();
			
			PluginPanel() {
				super();
				setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
				add( new JScrollPane( description ) );
			}
			
			public void setPlugin( Plugin plugin ) {
				this.plugin = plugin;
				description.setText( plugin.getDescription() );
			}
		}
		
		class GraphPanel extends JPanel implements ActionListener {
			StructureTypeInfo graphType;
			JEditorPane description = new HTMLEditorPane();
			JButton button = new JButton( "Create Structure" );
			
			GraphPanel( ) {
				super( );
				setLayout( new BorderLayout() );
				add( new JScrollPane( description ), BorderLayout.CENTER );
				button.addActionListener( this );
				
				JPanel buttonPanel = new JPanel( new BorderLayout() );
				buttonPanel.add( button, BorderLayout.EAST );
				add( buttonPanel, BorderLayout.SOUTH );
			}
			
			public void setGraphType( StructureTypeInfo graphType ) {
				this.graphType = graphType;
				description.setText( graphType.getDescription() );
				button.setToolTipText( "Creates a new " + graphType.getName() );
			}

			public void actionPerformed(ActionEvent e) {
				MainPad.getInstance().getDesktop().newDocument( graphType );
			}
		}
		
		class AlgorithmPanel extends JPanel implements ActionListener {
			AlgorithmInfo algorithmInfo;
			final BeanEditorTableModel tableModel = new BeanEditorTableModel();
			final JButton executeButton = new JButton( "Execute Algorithm" );
			final JEditorPane description = new HTMLEditorPane();
			
			AlgorithmPanel() {
				super();
				setLayout( new BorderLayout() );

				executeButton.addActionListener( this );
				executeButton.setToolTipText( "Executes the algorithm" );

				JPanel propertiesPanel = new JPanel( new BorderLayout() );
				propertiesPanel.add( new JScrollPane( new PropertyEditorTable( tableModel ) ), BorderLayout.CENTER );
				
				JPanel descriptionPanel = new JPanel( new BorderLayout() );
				descriptionPanel.add( new JScrollPane( description ) );
				
				JTabbedPane tabs = new JTabbedPane();
				tabs.add( propertiesPanel, "PROPERTIES" );
				tabs.add( descriptionPanel, "DESCRIPTION" );
				
				add( tabs, BorderLayout.CENTER );
				JPanel buttonPanel = new JPanel( new BorderLayout() );
				buttonPanel.add( executeButton, BorderLayout.EAST );
				add( buttonPanel, BorderLayout.SOUTH );
			}

			public void setAlgorithmInfo( AlgorithmInfo algorithmInfo ) {
				this.algorithmInfo = algorithmInfo;

				tableModel.setBean( algorithmInfo.getAlgorithm() );
				
				description.setText( algorithmInfo.getDescription() );
			}
			
			public void actionPerformed(ActionEvent e) {
				executeAlgorithm( algorithmInfo );
			}
		}
		
		class EmptyPanel extends JPanel {
			EmptyPanel() {
				super( new BorderLayout() );
				add( new JLabel( "Please select a Plugin!" ), BorderLayout.CENTER );
			}
		}
	}
	
	public static class PluginsTreeModel implements TreeModel {

		private final PluginsTreeNode pluginTree;
		
		public PluginsTreeModel( PluginsTreeNode pluginTree ) {
			this.pluginTree = pluginTree;
		}
		
		public Object getRoot() {
			return pluginTree;
		}

		public Object getChild(Object parent, int index) {
			return ((PluginsTreeNode)parent).getChildren().get( index );
		}

		public int getChildCount(Object parent) {
			return ((PluginsTreeNode)parent).getChildren().size();
		}

		public boolean isLeaf(Object node) {
			return ((PluginsTreeNode)node).getChildren().isEmpty();
		}

		public void valueForPathChanged(TreePath path, Object newValue) {
		}

		public int getIndexOfChild(Object parent, Object child) {
			PluginsTreeNode parentTN = (PluginsTreeNode)parent;
			int index = 0;
			for ( PluginsTreeNode node : parentTN.getChildren() ) {
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
	
	static void executeAlgorithm( AlgorithmInfo algorithmInfo ) {
		try {
			Algorithm algorithm = algorithmInfo.getAlgorithm();
			Hashtable<String, Object> algorithmSettings = new Hashtable<String, Object>();
			String algorithmName;
			// erzeuge zun�chst eine Kopie des Algorithmus und aller zugeh�rigen Graphen
			Hashtable<Structure, GJGraph> jgraphs = new Hashtable<Structure, GJGraph>();
			
			Algorithm preparedAlgorithm = algorithm.getClass().newInstance();
			
			BeanInfo beanInfo = Introspector.getBeanInfo( algorithm.getClass() );
			
			algorithmName = algorithmInfo.getName();
			
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
					for( Structure structure : AlgorithmResultInfo.getAllGraphs( result ) ) {
						GJGraph gjGraph = jgraphs.get( structure );
						if ( gjGraph == null )
							gjGraph = new GJGraph( structure );
						MainPad.getInstance().getDesktop().newDocument( gjGraph );
					}
				} else {
					AlgorithmResultInfo info = new AlgorithmResultInfo( algorithmName, algorithmSettings, result, jgraphs );
					if ( info.hasContents() )
						MainPad.getInstance().getDesktop().newDocument( info );
					else
						JOptionPane.showMessageDialog( MainPad.getInstance(), info.getMessageComponent(), "Algorithm-Result", JOptionPane.INFORMATION_MESSAGE );
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
			MainPad.getInstance().handleUserException( new UserException("Could not execute algorithm", e) );
		} catch (InstantiationException e) {
			MainPad.getInstance().handleUserException( new UserException("could not execute algorithm", e) );
		} catch( UserException e ) {
			MainPad.getInstance().handleUserException( e );
		} catch( Throwable e ) {
			MainPad.getInstance().handleUserException( new UserException("error executing algorithm", e) );
		}
		
	}

}
