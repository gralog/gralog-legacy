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
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.Customizer;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.JToggleButton.ToggleButtonModel;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import net.infonode.docking.View;
import de.hu.gralog.algorithm.result.AlgorithmInfoListener;
import de.hu.gralog.algorithm.result.AlgorithmResultContent;
import de.hu.gralog.algorithm.result.AlgorithmResultContentTreeNode;
import de.hu.gralog.algorithm.result.AlgorithmResultInfo;
import de.hu.gralog.algorithm.result.AlgorithmResultInteractiveContent;
import de.hu.gralog.algorithm.result.DisplaySubgraphMode;
import de.hu.gralog.algorithm.result.DisplaySubgraphModeListener;
import de.hu.gralog.algorithm.result.ElementTipsDisplayMode;
import de.hu.gralog.algorithm.result.ElementTipsDisplayModeListener;
import de.hu.gralog.algorithm.result.DisplaySubgraph.DisplayMode;
import de.hu.gralog.app.InputOutputException;
import de.hu.gralog.app.UserException;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.gui.components.HTMLEditorPane;
import de.hu.gralog.gui.document.AlgorithmResultDocumentContent;
import de.hu.gralog.gui.document.Document;
import de.hu.gralog.gui.document.DocumentListener;
import de.hu.gralog.jgraph.GJGraph;
import de.hu.gralog.jgraph.GJGraphUtil;

public class AlgorithmResultView extends View implements EditorDesktopViewListener, DocumentListener {
	
	private static final JLabel NO_RESULT = new JLabel( "Document is not an Algorithm-Result." );
	private HashMap<Document, JPanel> panels = new HashMap<Document, JPanel>();
	
	public AlgorithmResultView() {
		super("Algorithm-Result", null, NO_RESULT );
	}

	public void currentDocumentSelectionChanged() {
		Document document = MainPad.getInstance().getDesktop().getCurrentDocument();
		
		if ( document != null && document.getContent() instanceof AlgorithmResultDocumentContent ) {
			JPanel panel = panels.get( document );
			if ( panel == null ) {
				document.addDocumentListener( this );
				panel = createPanel( (AlgorithmResultDocumentContent)document.getContent() );
				panels.put( document, panel );
			}
			setComponent( panel );
		} else
			setComponent( NO_RESULT );
		repaint();
	}
	
	protected static JPanel createPanel( AlgorithmResultDocumentContent content ) {
		JTabbedPane tabPanel = new JTabbedPane();
		
		JPanel resultPanel = null;
		
		resultPanel = createResultPanel( content.getAlgorithmResultInfo() );
		
		tabPanel.addTab( "Result", resultPanel );
		
		final Object[] algorithmSettings = content.getAlgorithmResultInfo().getAlgorithmSettings().entrySet().toArray();
		JTable settingsTable = new JTable( new AbstractTableModel() {

			public int getColumnCount() {
				return 2;
			}

			public int getRowCount() {
				return algorithmSettings.length;
			}
			
			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}

			
			@Override
			public String getColumnName(int column) {
				if ( column == 0 )
					return "Name";
				return "Value";
			}

			public Object getValueAt(int rowIndex, int columnIndex) {
				Map.Entry entry = (Map.Entry)algorithmSettings[rowIndex];
				if ( columnIndex == 0 )
					return entry.getKey();
				return entry.getValue();
			}
			
		});
		
		JPanel settingsPanel = new JPanel();
		settingsPanel.setLayout( new BoxLayout( settingsPanel, BoxLayout.Y_AXIS ) );
		
		JPanel algorithmName = new JPanel();
		algorithmName.setLayout( new BorderLayout() );
		algorithmName.setBorder( BorderFactory.createTitledBorder( "Name" ) );
		JTextArea nameText = new JTextArea();
		nameText.setText( content.getAlgorithmResultInfo().getAlgorithmName() );
		nameText.setEditable( false );
		nameText.setRows( 3 );
		algorithmName.add( new JScrollPane( nameText ) );
		settingsPanel.add( algorithmName, BorderLayout.CENTER );
		
		JPanel settingsTablePanel = new JPanel();
		settingsTablePanel.setLayout( new BorderLayout() );
		settingsTablePanel.setBorder( BorderFactory.createTitledBorder( "Properties" ) );
		settingsTablePanel.add( new JScrollPane( settingsTable ), BorderLayout.CENTER );
		settingsPanel.add( settingsTablePanel );
		
		tabPanel.addTab( "Algorithm", settingsPanel );
		if ( content.getAlgorithmResultInfo().getDescription() != null )
			tabPanel.addTab( "Description", new JScrollPane( new HTMLEditorPane( content.getAlgorithmResultInfo().getDescription() ) ) );
		
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout() );
		panel.add( tabPanel, BorderLayout.CENTER );
		
		return panel;
	}

	protected static JPanel createResultPanel( AlgorithmResultInfo info ) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout() );
		
		if ( info.getSingleContent() == null )
			panel.add( createChooseContentPanel( info ), BorderLayout.CENTER );
		
		panel.add( createEditContentPanel( info ), BorderLayout.SOUTH );
		return panel;
	}
	
	protected static JPanel createChooseContentPanel( AlgorithmResultInfo info ) {
		if ( info.getContentList() != null )
			return new StepsPanel( info );
		return new TreePanel( info );
	}
	
	private static class StepsPanel extends JPanel {

		private AlgorithmResultInfo info;
		
		public StepsPanel( AlgorithmResultInfo info ) {
			this.info = info;
			setLayout( new BorderLayout() );
			
			add( new StepsControlPanel( info.getContentList().size() ), BorderLayout.NORTH );
		}
		
		private class StepsControlPanel extends JPanel implements ActionListener, ItemListener {
			
			private int step, stepCount;
				
			private final JButton BEGIN, END, PREVIOUS, NEXT;
			private final JComboBox CHOOSE_STEP;
			private final Integer[] comboBoxModel;

			
			public StepsControlPanel( int stepCount ) {
				super();
				this.setBorder( BorderFactory.createTitledBorder( "Content List") );
				this.stepCount = stepCount;
				step = 0;
				BEGIN = new JButton( "<<" );
				BEGIN.setActionCommand( "begin" );
				BEGIN.addActionListener( this );
				
				END = new JButton( ">>" );
				END.setActionCommand( "end" );
				END.addActionListener( this );

				PREVIOUS = new JButton( "<" );
				PREVIOUS.setActionCommand( "previous" );
				PREVIOUS.addActionListener( this );
				
				NEXT = new JButton( ">" );
				NEXT.setActionCommand( "next" );
				NEXT.addActionListener( this );
				
				comboBoxModel = new Integer[stepCount];
				for ( int i = 0; i < stepCount;i++ )
					comboBoxModel[i] = new Integer( i );
				CHOOSE_STEP = new JComboBox( comboBoxModel );
				CHOOSE_STEP.addItemListener( this );
				
				JLabel iteration = new JLabel( "Iteration" );
				add( iteration );
				add( BEGIN );
				add( PREVIOUS );
				add( CHOOSE_STEP );
				add( NEXT );
				add( END );
				
				CHOOSE_STEP.setSelectedIndex( step );
				stepUpdated(  );
			}

			protected void stepUpdated() {
				if ( step == 0 ) {
					BEGIN.setEnabled( false );
					PREVIOUS.setEnabled( false );
				} else {
					BEGIN.setEnabled( true );
					PREVIOUS.setEnabled( true );
				}
				
				if ( step == stepCount - 1 ) {
					END.setEnabled( false );
					NEXT.setEnabled( false );
				} else {
					END.setEnabled( true );
					NEXT.setEnabled( true );
				}
				
				try {
					info.setCurrentContent( info.getContentList().get( step ) );
				} catch( UserException e ) {
					MainPad.getInstance().handleUserException( e );
				}
			}
			
			public void actionPerformed(ActionEvent e) {
				if ( e.getActionCommand().equals( "begin" ) )
					step = 0;
				if ( e.getActionCommand().equals( "end" ) )
					step = stepCount - 1;
				if ( e.getActionCommand().equals( "previous" ) )
					step--;
				if ( e.getActionCommand().equals( "next" ) )
					step++;
				
				CHOOSE_STEP.setSelectedItem( comboBoxModel[step] );
			}

			public void itemStateChanged(ItemEvent e) {
				if ( e.getStateChange() == ItemEvent.SELECTED ) {
					step = ((Integer)CHOOSE_STEP.getSelectedItem()).intValue();
					stepUpdated(  );
				}
			}
		}
	}
	
	private static class TreePanel extends JPanel implements TreeSelectionListener {
		
		protected AlgorithmResultInfo info;
		
		public TreePanel( AlgorithmResultInfo info ) {
			super();
			setLayout( new BorderLayout() );
			this.info = info;
			setBorder( BorderFactory.createTitledBorder( "Content Tree" ) );
			JTree tree = new JTree( new AlgorithmResultContentTreeModel() );
			ToolTipManager.sharedInstance().registerComponent( tree );
			tree.setCellRenderer( new ARCTreeCellRenderer() );
			tree.addTreeSelectionListener( this );
			tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
			add( new JScrollPane( tree ), BorderLayout.CENTER );
		}
		
		private static class ARCTreeCellRenderer extends DefaultTreeCellRenderer {

			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
				Component comp = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
						row, hasFocus);
				AlgorithmResultContent content = (AlgorithmResultContent)value;
				if ( content.getDescription() != null )
					setToolTipText( content.getDescription() );
				return comp;
			}
			
		}
		
		private class AlgorithmResultContentTreeModel implements TreeModel {
			
			public AlgorithmResultContentTreeModel() {
				
			}

			public Object getChild(Object parent, int index) {
				AlgorithmResultContentTreeNode parentNode = (AlgorithmResultContentTreeNode)parent;
				try {
					return parentNode.getChildren().get( index );
				} catch (UserException e) {
					MainPad.getInstance().handleUserException( e );
				}
				return null;
			}

			public int getChildCount(Object parent) {
				AlgorithmResultContentTreeNode parentNode = (AlgorithmResultContentTreeNode)parent;
				try {
					return parentNode.getChildren().size();
				} catch (UserException e) {
					MainPad.getInstance().handleUserException( e );
				}
				return 0;
			}

			public int getIndexOfChild(Object parent, Object child) {
				AlgorithmResultContentTreeNode parentNode = (AlgorithmResultContentTreeNode)parent;
				try {
					return parentNode.getChildren().indexOf( child );
				} catch (UserException e) {
					MainPad.getInstance().handleUserException( e );
				}
				return 0;
			}

			public Object getRoot() {
				return info.getContentTree();
			}

			public boolean isLeaf(Object node) {
				AlgorithmResultContentTreeNode nodeNode = (AlgorithmResultContentTreeNode)node;
				try {
					return nodeNode.getChildren().size() == 0;
				} catch (UserException e) {
					MainPad.getInstance().handleUserException( e );
				}
				return true;
			}

			public void addTreeModelListener(TreeModelListener l) {
			}

			public void removeTreeModelListener(TreeModelListener l) {
			}

			public void valueForPathChanged(TreePath path, Object newValue) {
			}
			
		}

		public void valueChanged(TreeSelectionEvent e) {
			TreePath path = e.getNewLeadSelectionPath();
			if ( path != null ) {
				try {
					info.setCurrentContent( (AlgorithmResultContent) path.getLastPathComponent() );
				} catch (UserException e1) {
					MainPad.getInstance().handleUserException( e1 );
				}
			}
		}
	}
	
	protected static JPanel createEditContentPanel( AlgorithmResultInfo info ) {
		return new GraphPanel( info );
	}
	
	private static class GraphPanel extends JPanel implements AlgorithmInfoListener {

		private final JPanel cards = new JPanel( new CardLayout() );
		private final JPanel cardsET = new JPanel( new CardLayout() );
		private JComboBox chooseSubgraph;
		private JComboBox chooseElementTip;
		private final AlgorithmResultInfo info;
		
		public GraphPanel( AlgorithmResultInfo info ) {
			this.info = info;
			setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
			
			if ( info.getSingleContent() != null  && info.getSingleContent() instanceof AlgorithmResultInteractiveContent) {
				
				try {
					Class<Customizer> customizerClass = (Class<Customizer>)Introspector.getBeanInfo( info.getSingleContent().getClass() ).getBeanDescriptor().getCustomizerClass();
					if ( customizerClass != null ) {
						Customizer customizer = customizerClass.newInstance();
						customizer.setObject( info.getSingleContent() );
						JPanel panel = new JPanel();
						panel.setBorder( BorderFactory.createTitledBorder( "Content Customizer" ) );
						panel.add( (Component)customizer );
						add( panel );
					}
				} catch (IntrospectionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			if ( info.getDisplaySubgraphModes( true ) != null )
				add( createSubgraphPanel( info ) );
			if ( info.getElementTipsDisplayModes( true ) != null )
				add( createElementTipsPanel( info ) );
			add ( createGraphPanel( info ) );

			info.addListener( this );
		}
		
		public JPanel createGraphPanel( final AlgorithmResultInfo info ) {
			JPanel panel = new JPanel();
			panel.setLayout( new BorderLayout() );
			
			JButton openGraphInNewDocument = new JButton( new AbstractAction( "Open As Structure", null ) {

				public void actionPerformed(ActionEvent e) {
					try {
						GJGraph graph = GJGraphUtil.getGJGraphCopy( info.getGraph() );
						MainPad.getInstance().getDesktop().openDocument(  graph );
					} catch( InputOutputException i ) {
						MainPad.getInstance().handleUserException( new UserException( "unable to open as graph", i ) );
					} catch (UserException u) {
						MainPad.getInstance().handleUserException( u );
					}
				}
				
			} );
			
			panel.add( openGraphInNewDocument, BorderLayout.EAST );
			return panel;
		}
		
		public JPanel createSubgraphPanel( AlgorithmResultInfo info ) {
			JPanel panel = new JPanel();
			panel.setBorder( BorderFactory.createTitledBorder( "Subgraph Options" ) );

//			panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
			panel.setLayout( new BorderLayout( ) );
			
			for ( String description : info.getDisplaySubgraphModes( true ).keySet() ) {
				final DisplaySubgraphMode displayMode = info.getDisplaySubgraphModes( true ).get( description );
				
				JPanel tablePanel = new JPanel(  );
				tablePanel.setLayout( new BoxLayout( tablePanel, BoxLayout.Y_AXIS ) );
				JTable table = new DisplaySubgraphTable( displayMode );
								
				tablePanel.add( table.getTableHeader(), BorderLayout.NORTH );
				tablePanel.add( table, BorderLayout.CENTER );
				final JCheckBox visible = new JCheckBox( "visible" );
				visible.setModel( new DisplaySubgraphModeVisibleButtonModel( displayMode ) );

				visible.setBorder( BorderFactory.createEmptyBorder( 5, 0,0,0 ) );
				JPanel visiblePanel = new JPanel( new BorderLayout() );
				visiblePanel.add( visible, BorderLayout.EAST );
				tablePanel.add( visiblePanel, BorderLayout.SOUTH );
				cards.add( tablePanel, description );
			}
			JPanel noSubgraph = new JPanel();
			noSubgraph.add( new JLabel( "no subgraph selected" ) );
			cards.add( noSubgraph, "NO_SUBGRAPH" );
			JPanel chooseSubgraphPanel = new JPanel();
			chooseSubgraphPanel.setBorder( BorderFactory.createEmptyBorder( 0, 0, 10, 0 ) );

			chooseSubgraphPanel.setLayout( new BoxLayout( chooseSubgraphPanel, BoxLayout.X_AXIS ) );
			JLabel chooseSubgraphLabel = new JLabel( "Choose Subgraph" );
			
			chooseSubgraph = new JComboBox( info.getDisplaySubgraphModes( false ).keySet().toArray() );
			chooseSubgraph.setBorder( BorderFactory.createEmptyBorder( 0, 5, 0, 0 ) );
			chooseSubgraph.addActionListener( new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					CardLayout cl = (CardLayout)(cards.getLayout());
					String item = (String)chooseSubgraph.getSelectedItem();
					if( item == null )
						item = "NO_SUBGRAPH";
					cl.show(cards, item);
				}
				
			});

			chooseSubgraph.setSelectedItem( chooseSubgraph.getSelectedItem() );
			
			chooseSubgraphPanel.add( chooseSubgraphLabel );
			chooseSubgraphPanel.add( chooseSubgraph );

			panel.add( chooseSubgraphPanel, BorderLayout.NORTH );
			panel.add( cards, BorderLayout.CENTER );
			return panel;
		}
		
		private JPanel createElementTipsPanel( final AlgorithmResultInfo info ) {
			JPanel panel = new JPanel( new BorderLayout() );
			panel.setBorder( BorderFactory.createTitledBorder( "Element Tips" ) );
			
			for ( Map.Entry<String, ElementTipsDisplayMode> entry : info.getElementTipsDisplayModes( true ).entrySet() ) {
				JCheckBox visible = new JCheckBox( "visible" );
				visible.setModel( new ElementTipsDisplayModeButtonModel( entry.getValue() ) );
				cardsET.add( visible, entry.getKey() );
			}
			
			cardsET.add( new JLabel( "" ), "NO_ELEMENTTIP" );
			JPanel chooseElementTipPanel = new JPanel( new BorderLayout() );
			chooseElementTipPanel.setBorder( BorderFactory.createEmptyBorder( 0, 0, 10, 0 ) );
			
			JLabel chooseElementTipLabel = new JLabel( "Choose Elementtip" );
			
			chooseElementTip = new JComboBox( info.getElementTipsDisplayModes( false ).keySet().toArray() );
			chooseElementTip.setBorder( BorderFactory.createEmptyBorder( 0, 5, 0, 0 ) );
			chooseElementTip.addActionListener( new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					CardLayout cl = (CardLayout)(cardsET.getLayout());
					String item = (String)chooseElementTip.getSelectedItem();
					if ( item == null )
						item = "NO_ELEMENTTIP";
				    cl.show(cardsET, item );
				}
				
			});
			
			chooseElementTip.setSelectedItem( chooseElementTip.getSelectedItem() );
			chooseElementTipPanel.add( chooseElementTipLabel, BorderLayout.WEST );
			chooseElementTipPanel.add( chooseElementTip, BorderLayout.CENTER );

			panel.add( chooseElementTipPanel, BorderLayout.CENTER );
			panel.add( cardsET, BorderLayout.EAST );
			
			return panel;
		}
		
		
		
		private static class DisplaySubgraphModeVisibleButtonModel extends ToggleButtonModel implements DisplaySubgraphModeListener {
			
			private DisplaySubgraphMode mode;
			
			public DisplaySubgraphModeVisibleButtonModel( DisplaySubgraphMode mode ) {
				this.mode = mode;
				mode.addListener( this );
			}
			
			@Override
			public boolean isSelected() {
				return mode.isVisible();
			}

			@Override
			public void setSelected(boolean b) {
				mode.setVisible( b );
			}

			public void displaySubgraphModeChanged() {
				this.fireStateChanged();
			}
			
		}
		
		private static class ElementTipsDisplayModeButtonModel extends ToggleButtonModel implements ElementTipsDisplayModeListener {
			
			private ElementTipsDisplayMode mode;
			
			public ElementTipsDisplayModeButtonModel( ElementTipsDisplayMode mode ) {
				this.mode = mode;
				mode.addElementTipsDisplayModeListener( this );
			}
			
			@Override
			public boolean isSelected() {
				return mode.isVisible();
			}

			@Override
			public void setSelected(boolean b) {
				mode.setVisible( b );
			}

			public void elementTipsDisplayModeChanged() {
				this.fireStateChanged();
			}
			
		}

		public void graphReplaced() {
			
		}

		public void contentChanged() {
			if ( chooseSubgraph != null ) {
				String selectedItem = (String)chooseSubgraph.getSelectedItem();
				chooseSubgraph.setModel( new DefaultComboBoxModel( info.getDisplaySubgraphModes( false ).keySet().toArray() ) );
				chooseSubgraph.setSelectedItem( selectedItem );
				if ( chooseSubgraph.getSelectedItem() != selectedItem )
					chooseSubgraph.setSelectedItem( chooseSubgraph.getSelectedItem() );
			}
			
			if ( chooseElementTip != null ) {
				String selectedItem = (String)chooseElementTip.getSelectedItem();
				chooseElementTip.setModel( new DefaultComboBoxModel( info.getElementTipsDisplayModes( false ).keySet().toArray() ) );
				chooseElementTip.setSelectedItem( selectedItem );
				if ( chooseElementTip.getSelectedItem() != selectedItem )
					chooseElementTip.setSelectedItem( chooseElementTip.getSelectedItem() );
			}
		}

	}
	
	private static class DisplaySubgraphTable extends JTable {
		
		private DisplaySubgraphMode mode;
		
		public DisplaySubgraphTable( DisplaySubgraphMode mode ) {
			super();

			this.mode = mode;
			
			super.setDefaultEditor( DisplayMode.class, new DisplayModeEditor() );
			super.setDefaultRenderer( DisplayMode.class, new DisplayModeCellRenderer() );
			
			setModel( new DisplaySubgraphTableModel() );
		}
		
		private class DisplaySubgraphTableModel extends AbstractTableModel implements DisplaySubgraphModeListener {
			
			public DisplaySubgraphTableModel() {
				mode.addListener( this );
			}
			
			public int getRowCount() {
				return 2; 
			}

			public int getColumnCount() {
				return 3;
			}

			public Object getValueAt(int rowIndex, int columnIndex) {
				if ( rowIndex == 0 ) {
					if ( columnIndex == 0 )
						return "vertices";
					if ( columnIndex == 1 )
						return mode.getVertexDisplayMode( false );
					return mode.getVertexDisplayMode( true );
				}
				if ( columnIndex == 0 )
					return "edges";
				if ( columnIndex == 1 )
					return mode.getEdgeDisplayMode( false );
				return mode.getEdgeDisplayMode( true );
			}

			@Override
			public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
				if ( rowIndex == 0 ) {
					if ( columnIndex == 1 )
						mode.setVertexDisplayMode( (DisplayMode)aValue, mode.getVertexDisplayMode( true ) );
					else
						mode.setVertexDisplayMode( mode.getVertexDisplayMode( false), (DisplayMode)aValue );
				} else {
					if ( columnIndex == 1 )
						mode.setEdgeDisplayMode( (DisplayMode)aValue, mode.getEdgeDisplayMode( true ) );
					else
						mode.setEdgeDisplayMode( mode.getEdgeDisplayMode( false), (DisplayMode)aValue );
				}
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				if ( columnIndex == 0 )
					return String.class;
				return DisplayMode.class;
			}

			@Override
			public String getColumnName(int column) {
				if ( column == 0 )
					return "TYPE";
				if ( column == 1 )
					return "IN";
				return "OUT";
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				if ( columnIndex == 0 )
					return false;
				return true;
			}

			public void displaySubgraphModeChanged() {
				this.fireTableDataChanged();
			}
		}
		
	}
	
	public static class DisplayModeCellRenderer extends JLabel implements TableCellRenderer {
		
		public DisplayModeCellRenderer() {
			setOpaque( true );
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if ( value != null ) {
				if ( value instanceof DisplayMode && ((DisplayMode)value).getColor() != null ) {
					DisplayMode mode = (DisplayMode)value;
					setBackground( mode.getColor() );
				}
				else
					setBackground( Color.white );
				
				setText( value.toString() );
			}
			else {
				setBackground( Color.white );
				setText( null );
			}
			return this;
		}
		
	}

	public static class DisplayModeEditor extends DefaultCellEditor {
		
		public DisplayModeEditor(  ) {
			super( new JComboBox( DisplayMode.getDisplayModes() ) );
		}
	}

	public void documentModifiedStatusChanged(Document document) {
		// 
	}

	public void documentComponentReplaced(Document document) {
		//
	}

	public void documentReverted(Document document) {
		if ( document.getContent() instanceof AlgorithmResultDocumentContent) {
			panels.remove( document );
			currentDocumentSelectionChanged();
		}
	}

	public void documentClosed(Document document) {
		panels.remove( document );
	}
}
