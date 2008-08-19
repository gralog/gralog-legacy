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

package de.hu.logic.graph;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.Customizer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.hu.gralog.jgrapht.event.GraphPropertyListener;

public class TransitionSystemCustomizer extends JPanel implements Customizer, ActionListener, ListSelectionListener, ItemListener {

	private static final String ADD_PROPOSITION = "add";
	private static final String REMOVE_PROPOSITION = "remove";
	private static final String RENAME_PROPOSITION = "rename";
	private static final String TO_IN_LIST = "<";
	private static final String TO_OUT_LIST = ">";
	private static final String ALL_TO_IN_LIST = "<<";
	private static final String ALL_TO_OUT_LIST = ">>";
	
	private TransitionSystem transitionSystem;
	private ChoosePropositionComboBoxModel choosePropositionComboBoxModel;
	private JComboBox chooseProposition;
	
	private final JButton addProposition = new JButton( ADD_PROPOSITION );
	private final JButton removeProposition = new JButton( REMOVE_PROPOSITION );
	private final JButton renameProposition = new JButton( RENAME_PROPOSITION );
	
	private final JPanel editPanel = new JPanel();
	private final JButton toInList = new JButton( TO_IN_LIST );
	private final JButton toOutList = new JButton( TO_OUT_LIST );
	private final JButton alltoInList = new JButton( ALL_TO_IN_LIST );
	private final JButton alltoOutList = new JButton( ALL_TO_OUT_LIST );
	
	private JList inList;
	private JList outList;
	private Proposition currentProposition = null;
	
	public TransitionSystemCustomizer() {
		super();
	}

	protected void createPanel() {
		setLayout( new BorderLayout() );
		
		add( createChoosePropositionPanel(), BorderLayout.NORTH );
		add( createEditPropositionPanel(), BorderLayout.CENTER );
		
	}
	
	protected JPanel createButtonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS ) );
		
		panel.add( Box.createHorizontalGlue() );
		
		addProposition.setActionCommand( ADD_PROPOSITION );
		addProposition.addActionListener( this );
		panel.add( addProposition );
		panel.add( Box.createRigidArea( new Dimension( 2, 0 ) ) );
		
		removeProposition.setActionCommand( REMOVE_PROPOSITION );
		removeProposition.addActionListener( this );
		panel.add( removeProposition );
		panel.add( Box.createRigidArea( new Dimension( 2, 0 ) ) );
		
		renameProposition.setActionCommand( RENAME_PROPOSITION );
		renameProposition.addActionListener( this );
		panel.add( renameProposition );
		return panel;
	}
	
	protected JPanel createEditPropositionPanel() {
		editPanel.setLayout( new BoxLayout( editPanel, BoxLayout.X_AXIS ) );
						
		inList = new JList( new InOutPropositionListModel( true ) );
		outList = new JList( new InOutPropositionListModel( false ) );
		
		inList.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
		outList.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );

		inList.getSelectionModel().addListSelectionListener( this );
		outList.getSelectionModel().addListSelectionListener( this );
		
		editPanel.add( new JScrollPane( inList ) );
		editPanel.add( Box.createRigidArea( new Dimension( 5, 0 ) ) );
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new BoxLayout( buttonPanel, BoxLayout.Y_AXIS ) );
		
		toInList.setActionCommand( TO_IN_LIST );
		toInList.addActionListener( this );
		buttonPanel.add( toInList );
		
		toOutList.setActionCommand( TO_OUT_LIST );
		toOutList.addActionListener( this );
		buttonPanel.add( toOutList );
		
		buttonPanel.add( Box.createVerticalGlue() );
		
		alltoInList.setActionCommand( ALL_TO_IN_LIST );
		alltoInList.addActionListener( this );
		buttonPanel.add( alltoInList );
		
		alltoOutList.setActionCommand( ALL_TO_OUT_LIST );
		alltoOutList.addActionListener( this );
		buttonPanel.add( alltoOutList );
		
		editPanel.add( buttonPanel );
		editPanel.add( Box.createRigidArea( new Dimension( 5, 0 ) ) );
		
		editPanel.add( new JScrollPane( outList ) );
		
		return editPanel;
	}
	
	protected JPanel createChoosePropositionPanel() {
		JPanel choosePropositionPanel = new JPanel();
		choosePropositionPanel.setLayout( new BoxLayout( choosePropositionPanel, BoxLayout.X_AXIS ) );
		
		JLabel choosePropositionLabel = new JLabel( "Choose Proposition:" );
		choosePropositionPanel.add( choosePropositionLabel );
		choosePropositionPanel.add( Box.createRigidArea( new Dimension( 5, 0 ) ) );
		
		choosePropositionComboBoxModel = new ChoosePropositionComboBoxModel();
		chooseProposition = new JComboBox( choosePropositionComboBoxModel );
		
		chooseProposition.setPreferredSize( new Dimension( 100, (int)chooseProposition.getPreferredSize().getHeight() ) );
		chooseProposition.addItemListener( this );
		choosePropositionPanel.add( chooseProposition );
		choosePropositionPanel.add( Box.createRigidArea( new Dimension( 5, 0 ) ) );
		
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
		panel.add( choosePropositionPanel );
		panel.add( Box.createRigidArea( new Dimension( 0, 5 ) ) );
		panel.add( createButtonPanel() );
		panel.setBorder( BorderFactory.createEmptyBorder( 5, 0, 5, 0 ) );
		
		return panel;
	}
	
	public void setObject(Object bean) {
		transitionSystem = (TransitionSystem)bean;
		createPanel();
		updateControls();
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}
	
	protected Proposition getProposition() {
		if ( currentProposition == null ) {
			String name = (String)chooseProposition.getSelectedItem();
			if ( name == null )
				return null;
			currentProposition = transitionSystem.getProposition( name );
		}
		return currentProposition;
	}
	
	private class InOutPropositionListModel extends AbstractListModel implements GraphPropertyListener {

		protected Proposition currentProposition = null;
		protected ArrayList<TransitionSystemVertex> vertexes = null;
		protected boolean in;
		
		public InOutPropositionListModel( boolean in ) {
			this.in = in;

			transitionSystem.addGraphPropertyListener( this );
		}
		
		protected ArrayList<TransitionSystemVertex> getVertexes() {
			if ( vertexes == null ) {
				if ( in && getProposition() == null )
					vertexes = new ArrayList<TransitionSystemVertex>();
				else {
					if ( in )
						vertexes = getProposition().getVertexes();
					else {
						Set<TransitionSystemVertex> vertexSet = new HashSet<TransitionSystemVertex>( transitionSystem.vertexSet() );
						if ( getProposition() != null )
							vertexSet.removeAll( getProposition().getVertexes() );
						vertexes = new ArrayList<TransitionSystemVertex>( vertexSet );
					}
				}
			}
			return vertexes;
						
		}

		public int getSize() {
			return getVertexes().size();
		}

		public Object getElementAt(int index) {
			return getVertexes(  ).get( index );
		}
		
		public void selectedPropositionChanged() {
			vertexes = null;
			fireContentsChanged( this, 0, getSize() );
		}
		
		public void propertyChanged(Object graphSource, PropertyChangeEvent e) {
			if ( e.getSource() == getProposition() ) {
				vertexes = null;
				fireContentsChanged( this, 0, getSize() );
			}
		}

		public void propertyChanged(Object arg0, PropertyChangeEvent arg1, Object[] arg2) {
			propertyChanged( arg0, arg1 );
		}

	}
	
	private class ChoosePropositionComboBoxModel extends AbstractListModel implements ComboBoxModel, GraphPropertyListener {

		private String selectedPropostionName;
		
		public ChoosePropositionComboBoxModel() {
			transitionSystem.addGraphPropertyListener( this );
		}
		
		public void setSelectedItem(Object anItem) {
			selectedPropostionName = (String)anItem;
		}

		public Object getSelectedItem() {
			return selectedPropostionName;
		}

		public int getSize() {
			return transitionSystem.getPropositions().length;
		}

		public Object getElementAt(int index) {
			return transitionSystem.getPropositions( index ).getName();
		}
		
		public void propertyChanged(Object graphSource, PropertyChangeEvent e) {
			if ( getSelectedItem() != null && transitionSystem.getProposition( (String)getSelectedItem() ) == null ) {
				chooseProposition.setSelectedItem( null );
				chooseProposition.repaint();
			}
			fireContentsChanged(this, 0, getSize() );
		}

		public void propertyChanged(Object arg0, PropertyChangeEvent arg1, Object[] arg2) {
			propertyChanged( arg0, arg1 );
		}
	}

	public void actionPerformed(ActionEvent e) {
		if ( e.getActionCommand().equals( ADD_PROPOSITION ) ) {
			String name = JOptionPane.showInputDialog( this, "name: " );
			if ( name != null ) {
				if ( transitionSystem.getProposition( name ) == null )
					transitionSystem.addProposition( new Proposition( name ) );
				chooseProposition.setSelectedItem( name );
				chooseProposition.repaint();
			}
		}
		if ( e.getActionCommand().equals( REMOVE_PROPOSITION ) ) {
			transitionSystem.removeProposition( choosePropositionComboBoxModel.selectedPropostionName );
			chooseProposition.setSelectedItem( null );
			chooseProposition.repaint();
		}
		if ( e.getActionCommand().equals( RENAME_PROPOSITION ) ) {
			String name = JOptionPane.showInputDialog( this, "name: " );
			if ( name != null ) {
				if ( transitionSystem.getProposition( name ) == null )
					transitionSystem.getProposition( choosePropositionComboBoxModel.selectedPropostionName ).setName( name );
				chooseProposition.setSelectedItem( name );
				chooseProposition.repaint();
			}
		}
		if ( e.getActionCommand().equals( TO_IN_LIST ) ) {
			for ( Object vertex : outList.getSelectedValues() )
				getProposition().addVertex( (TransitionSystemVertex)vertex );
			inList.clearSelection();
			outList.clearSelection();
		}
		if ( e.getActionCommand().equals( TO_OUT_LIST ) ) {
			for ( Object vertex : inList.getSelectedValues() )
				getProposition().removeVertex( (TransitionSystemVertex)vertex );
			inList.clearSelection();
			outList.clearSelection();
		}
		if ( e.getActionCommand().equals( ALL_TO_IN_LIST ) ) {
			for ( TransitionSystemVertex vertex : transitionSystem.vertexSet() ) {
				if ( ! getProposition().containsVertex( vertex ) )
					getProposition().addVertex( vertex );
			}
			inList.clearSelection();
			outList.clearSelection();
		}
		if ( e.getActionCommand().equals( ALL_TO_OUT_LIST ) ) {
			for ( TransitionSystemVertex vertex : new ArrayList<TransitionSystemVertex>( getProposition( ).getVertexes() ) )
				getProposition().removeVertex( vertex );
			inList.clearSelection();
			outList.clearSelection();
		}

		
	}
	
	protected void updateControls() {
		if ( getProposition() == null ) {
			removeProposition.setEnabled( false );
			renameProposition.setEnabled( false );
			inList.setEnabled( false );
			outList.setEnabled( false );
			toInList.setEnabled( false );
			toOutList.setEnabled( false );
			alltoInList.setEnabled( false );
			alltoOutList.setEnabled( false );
		} else {
			removeProposition.setEnabled( true );
			renameProposition.setEnabled( true );
			inList.setEnabled( true );
			outList.setEnabled( true );
						
			if ( inList.getSelectedValues().length == 0 )
				toOutList.setEnabled( false );
			else
				toOutList.setEnabled( true );
			
			if ( outList.getSelectedValues().length == 0 )
				toInList.setEnabled( false );
			else
				toInList.setEnabled( true );
			
			if ( inList.getModel().getSize() == 0 )
				alltoOutList.setEnabled( false );
			else
				alltoOutList.setEnabled( true );
			
			if ( outList.getModel().getSize() == 0 )
				alltoInList.setEnabled( false );
			else
				alltoInList.setEnabled( true );
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		updateControls();
	}

	public void itemStateChanged(ItemEvent e) {
		currentProposition = null;
		((InOutPropositionListModel)inList.getModel()).selectedPropositionChanged();
		((InOutPropositionListModel)outList.getModel()).selectedPropositionChanged();
		updateControls();
	}
}
