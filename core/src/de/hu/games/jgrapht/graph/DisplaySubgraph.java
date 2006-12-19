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

package de.hu.games.jgrapht.graph;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Subgraph;




public class DisplaySubgraph implements DisplaySubgraphModeListener, Serializable {

	protected transient Vector listeners = new Vector();
	
	public static class DisplayMode implements Serializable {
		
		public static final DisplayMode SHOW = new DisplayMode( "SHOW" );
		public static final DisplayMode HIDE = new DisplayMode( "HIDE" );
		public static final DisplayMode HIGH1 = new DisplayMode( "HIGH1", Color.GREEN );
		public static final DisplayMode HIGH2 = new DisplayMode( "HIGH2", Color.RED );
		private static final DisplayMode[] ORDER = { HIGH2, HIGH1, SHOW, HIDE };
		
		private String name;
		private Color color;
		
		protected DisplayMode() {
			
		}
		
		private DisplayMode( String name ) {
			this( name, null );
		}
		
		private DisplayMode( String name, Color color ) {
			this.name = name;
			this.color = color;
		}
		
		private int getIndex( ) {
			for ( int i = 0; i < ORDER.length; i++ ) {
				if ( ORDER[i] == this )
					return i;
			}
			return -1;	
		}
		
		public boolean overwrites( DisplayMode displayMode ) {
			if ( getIndex() < displayMode.getIndex() )
				return true;
			return false;
		}
		
		public static DisplayMode[] getDisplayModes() {
			return ORDER;
		}
		
		public String toString() {
			return name;
		}
		
		public static DisplayMode parseString( String name ) {
			for ( DisplayMode mode : ORDER ) {
				if ( mode.name.equalsIgnoreCase( name ) )
					return mode;
			}
			return null;
		}
		
		public Color getColor() {
			return color;
		}
		
		private Object readResolve() {
			if ( name.equals( "SHOW" ) )
				return SHOW;
			if ( name.equals( "HIDE" ) )
				return HIDE;
			if ( name.equals( "HIGH1" ) )
				return HIGH1;
			return HIGH2;
		}
	}
	
	public static class DisplaySubgraphMode implements Serializable {
		protected transient ArrayList<DisplaySubgraphModeListener> listeners = new ArrayList<DisplaySubgraphModeListener>();
		protected transient ArrayList<UndoableEditListener> undoListeners = new ArrayList<UndoableEditListener>();
		
		protected DisplayMode[] vertexDisplayMode = { DisplayMode.SHOW, DisplayMode.SHOW };
		protected DisplayMode[] edgeDisplayMode = { DisplayMode.SHOW, DisplayMode.SHOW };
		protected boolean visible = true;

		public DisplayMode getVertexDisplayMode( boolean complement ) {
			if ( complement )
				return vertexDisplayMode[1];
			return vertexDisplayMode[0];
		}

		public void setVertexDisplayMode( DisplayMode sub, DisplayMode comp ) {
			setVertexDisplayMode( sub, comp, false );
		}
		
		public void setVertexDisplayMode( DisplayMode sub, DisplayMode comp, boolean undoRedoEdit ) {
			if ( !( sub != getVertexDisplayMode( false ) || comp != getVertexDisplayMode( true ) ) )
				return;
			DisplaySubgraphMode beforeEdit = null;
			if ( ! undoRedoEdit )
				 beforeEdit = getDataClone();
			vertexDisplayMode[0] = sub;
			vertexDisplayMode[1] = comp;
			fireDataChanged( beforeEdit, undoRedoEdit );
		}
		
		public void setEdgeDisplayMode( DisplayMode sub, DisplayMode comp ) {
			setEdgeDisplayMode( sub, comp, false );
		}
		
		public void setEdgeDisplayMode( DisplayMode sub, DisplayMode comp, boolean undoRedoEdit ) {
			if ( !( sub != getEdgeDisplayMode( false ) || comp != getEdgeDisplayMode( true ) ) )
				return;
			DisplaySubgraphMode beforeEdit = null;
			if ( ! undoRedoEdit )
				 beforeEdit = getDataClone();
			edgeDisplayMode[0] = sub;
			edgeDisplayMode[1] = comp;
			fireDataChanged( beforeEdit, undoRedoEdit );
		}

		public DisplayMode getEdgeDisplayMode( boolean complement ) {
			if ( complement )
				return edgeDisplayMode[1];
			return edgeDisplayMode[0];
		}

		public void setVisible( boolean visible ) {
			setVisible( visible, false );
		}
		
		public void setVisible( boolean visible, boolean undoRedoEdit  ) {
			if ( this.visible == visible )
				return;
			DisplaySubgraphMode beforeEdit = null;
			if ( ! undoRedoEdit )
				 beforeEdit = getDataClone();
			this.visible = visible;
			fireDataChanged( beforeEdit, undoRedoEdit );
		}
		
		public boolean isVisible() {
			return visible;
		}
		
		protected void fireDataChanged( DisplaySubgraphMode beforeEdit, boolean undoRedoEdit  ) {
			for ( DisplaySubgraphModeListener subgraph : listeners )
				subgraph.displaySubgraphModeChanged();
			if ( !undoRedoEdit ) {
				DisplaySubgraphMode afterEdit = getDataClone();
			
				for ( UndoableEditListener listener : undoListeners )
					listener.undoableEditHappened( new UndoableEditEvent( this, new DisplayModeUndoableEdit( beforeEdit, afterEdit, this ) ) );
			}
		}

		public void addListener( DisplaySubgraphModeListener subgraph ) {
			if ( !listeners.contains( subgraph ) )
				listeners.add( subgraph );
		}
		
		private Object readResolve() {
			listeners = new ArrayList<DisplaySubgraphModeListener>();
			undoListeners = new ArrayList<UndoableEditListener>();
			return this;
		}

		public void addUndoableEditListener( UndoableEditListener listener ) {
			if ( ! undoListeners.contains( listener ) )
				undoListeners.add( listener );
		}
		
		protected DisplaySubgraphMode getDataClone() {
			DisplaySubgraphMode clone = new DisplaySubgraphMode();
			clone.setEdgeDisplayMode( getEdgeDisplayMode( false ), getEdgeDisplayMode( true ), true );
			clone.setVertexDisplayMode( getVertexDisplayMode( false ), getVertexDisplayMode( true ), true );
			clone.setVisible( isVisible(), true );
			return clone;
		}
		
		public static class DisplayModeUndoableEdit implements UndoableEdit {

			protected DisplaySubgraphMode beforeEdit;
			protected DisplaySubgraphMode afterEdit;
			protected DisplaySubgraphMode edited;
			protected boolean isAlive = true;
			
			public DisplayModeUndoableEdit( DisplaySubgraphMode beforeEdit, DisplaySubgraphMode afterEdit, DisplaySubgraphMode edited ) {
				this.beforeEdit = beforeEdit;
				this.afterEdit = afterEdit;
				this.edited = edited;
			}
			
			protected void applySettings( DisplaySubgraphMode from, DisplaySubgraphMode to ) {
				to.setVertexDisplayMode( from.getVertexDisplayMode( false ), from.getVertexDisplayMode( true ), true );
				to.setEdgeDisplayMode( from.getEdgeDisplayMode( false ), from.getEdgeDisplayMode( true ), true );
				to.setVisible( from.isVisible(), true );
			}
			
			public void undo() throws CannotUndoException {
				applySettings( beforeEdit, edited );
			}

			public boolean canUndo() {
				if ( isAlive )
					return true;
				return false;
			}

			public void redo() throws CannotRedoException {
				applySettings( afterEdit, edited );
			}

			public boolean canRedo() {
				if ( isAlive )
					return true;
				return false;
			}

			public void die() {
				isAlive = false;
			}

			public boolean addEdit(UndoableEdit anEdit) {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean replaceEdit(UndoableEdit anEdit) {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean isSignificant() {
				return true;
			}

			public String getPresentationName() {
				// TODO Auto-generated method stub
				return null;
			}

			public String getUndoPresentationName() {
				// TODO Auto-generated method stub
				return null;
			}

			public String getRedoPresentationName() {
				// TODO Auto-generated method stub
				return null;
			}
			
		}
	}

	protected DisplaySubgraphMode mode;
	protected Subgraph subgraph; 
	
	protected DisplaySubgraph() {
		
	}
	
	public DisplaySubgraph( DisplaySubgraphMode mode, Subgraph subgraph ) {
		this.mode = mode;
		this.subgraph = subgraph;
		mode.addListener( this );
	}
	
	public Subgraph getSubgraph() {
		return subgraph;
	}
	
	public DisplaySubgraphMode getMode() {
		return mode;
	}
	
	public DisplayMode getDisplayMode( Object userObject ) {
		if ( userObject instanceof DefaultEdge ) {
			if ( getMode().getEdgeDisplayMode( false ) == getMode().getEdgeDisplayMode( true ) )
				return getMode().getEdgeDisplayMode( false );
			if ( subgraph.containsEdge( (DefaultEdge)userObject ) )
				return getMode().getEdgeDisplayMode( false );
			return getMode().getEdgeDisplayMode( true );
		}
		if ( getMode().getVertexDisplayMode( false ) == getMode().getVertexDisplayMode( true ) )
			return getMode().getVertexDisplayMode( false );
		if ( subgraph.containsVertex( userObject ) ) 
			return getMode().getVertexDisplayMode( false );
		return getMode().getVertexDisplayMode( true );
	}

	protected void fireSubGraphChanged() {
		for (int i = 0; i < listeners.size();  i++ )
			((DisplaySubgraphListener)listeners.get( i )).displayUpdated( this );
	}
	
	public void addDisplaySubgraphListener(DisplaySubgraphListener l) {
		if ( ! listeners.contains( l ) )
			listeners.add( l );
	}

	public void removeDisplaySubgraphListener(DisplaySubgraphListener l) {
		listeners.remove( l );
	}

	public void displaySubgraphModeChanged() {
		fireSubGraphChanged();
	}
	
}
