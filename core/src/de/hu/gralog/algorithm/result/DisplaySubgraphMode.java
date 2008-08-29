/*
 * Created on 8 Jan 2007
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.algorithm.result;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import de.hu.gralog.algorithm.result.DisplaySubgraph.DisplayMode;

/**
 * The class represents a displaymode for subgraphs. Here you can specifify
 * displaymodes for the vertices and edges in and out of subgraph.
 * 
 * @author ordyniak
 *
 */
public class DisplaySubgraphMode implements Serializable {
	protected transient ArrayList<DisplaySubgraphModeListener> listeners = new ArrayList<DisplaySubgraphModeListener>();
	protected transient ArrayList<UndoableEditListener> undoListeners = new ArrayList<UndoableEditListener>();
	
	protected DisplayMode[] vertexDisplayMode = { DisplayMode.SHOW, DisplayMode.SHOW };
	protected DisplayMode[] edgeDisplayMode = { DisplayMode.SHOW, DisplayMode.SHOW };
	protected boolean visible = true;

	/**
	 * 
	 * @param complement 
	 * @return the DisplayMode for vertices in subgraph ( if complement == false ) or not in 
	 * subgraph ( if complement == true )
	 */
	public DisplayMode getVertexDisplayMode( boolean complement ) {
		if ( complement )
			return vertexDisplayMode[1];
		return vertexDisplayMode[0];
	}

	/**
	 * Sets the displaymodi for vertices.
	 * 
	 * @param sub the displaymode for vertices in subgraph
	 * @param comp the displaymode for vertices out of subgraph
	 */
	public void setVertexDisplayMode( DisplayMode sub, DisplayMode comp ) {
		setVertexDisplayMode( sub, comp, false );
	}
	
	/**
	 * 
	 * 
	 * @param sub
	 * @param comp
	 * @param undoRedoEdit
	 */
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
	
	/**
	 * Sets the displaymode for edges.
	 * 
	 * @param sub the displaymode for edges in subgraph
	 * @param comp the displaymode for edges not in subgraph
	 */
	
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

	/**
	 * 
	 * @param complement
	 * @return the displaymode for edges in subgraph ( complement == false ) or not in subgraph ( complement == true )
	 */
	
	public DisplayMode getEdgeDisplayMode( boolean complement ) {
		if ( complement )
			return edgeDisplayMode[1];
		return edgeDisplayMode[0];
	}

	/**
	 * Sets the visibility status for this Displaymode
	 * 
	 * @param visible 
	 */
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
	
	/**
	 * 
	 * @return visibilitystatus for this displaymode
	 */
	
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