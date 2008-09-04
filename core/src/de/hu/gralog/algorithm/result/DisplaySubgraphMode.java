/*
 * Created on 2006 by Sebastian Ordyniak
 *
 * Copyright 2006 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of Gralog.
 *
 * Gralog is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * Gralog is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Gralog; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
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
 * This class represents a displaymode for subgraphs. Here you can specifify
 * displaymodes for the vertices and edges in- and outside of the subgraph.
 * 
 * The following code shows how to construct and initialize a
 * DisplaySubgraphMode for use in an {@link AlgorithmResult}:
 * 
 * <pre>
 * DisplaySubgraphMode displayMode = new DisplaySubgraphMode();
 * displayMode.setVertexDisplayMode(DisplayMode.HIGH1, DisplayMode.SHOW);
 * displayMode.setEdgeDisplayMode(DisplayMode.HIGH1, DisplayMode.HIDE);
 * </pre>
 * 
 * @author ordyniak
 * 
 */
public class DisplaySubgraphMode implements Serializable {
	protected transient ArrayList<DisplaySubgraphModeListener> listeners = new ArrayList<DisplaySubgraphModeListener>();

	protected transient ArrayList<UndoableEditListener> undoListeners = new ArrayList<UndoableEditListener>();

	protected DisplayMode[] vertexDisplayMode = { DisplayMode.SHOW,
			DisplayMode.SHOW };

	protected DisplayMode[] edgeDisplayMode = { DisplayMode.SHOW,
			DisplayMode.SHOW };

	protected boolean visible = true;

	/**
	 * 
	 * @param complement
	 * @return the DisplayMode for vertices inside subgraph ( if complement ==
	 *         false ) respectively outside the subgraph ( if complement == true )
	 */
	public DisplayMode getVertexDisplayMode(boolean complement) {
		if (complement)
			return vertexDisplayMode[1];
		return vertexDisplayMode[0];
	}

	/**
	 * Sets the displaymodi for vertices.
	 * 
	 * @param sub
	 *            the displaymode for vertices inside the subgraph
	 * @param comp
	 *            the displaymode for vertices outside the subgraph
	 */
	public void setVertexDisplayMode(DisplayMode sub, DisplayMode comp) {
		setVertexDisplayMode(sub, comp, false);
	}

	/**
	 * This method should not be called by plugin-developers. It allows gralog
	 * to change the vertexDisplayMode without informing UndoEditListeners.
	 * 
	 * @param sub
	 *            displaymode for vertices inside the subgraph
	 * @param comp
	 *            displaymode for vertices outside the subgraph
	 * @param undoRedoEdit
	 *            if false listeners are not informed about the change
	 */
	public void setVertexDisplayMode(DisplayMode sub, DisplayMode comp,
			boolean undoRedoEdit) {
		if (!(sub != getVertexDisplayMode(false) || comp != getVertexDisplayMode(true)))
			return;
		DisplaySubgraphMode beforeEdit = null;
		if (!undoRedoEdit)
			beforeEdit = getDataClone();
		vertexDisplayMode[0] = sub;
		vertexDisplayMode[1] = comp;
		fireDataChanged(beforeEdit, undoRedoEdit);
	}

	/**
	 * Sets the displaymode for edges.
	 * 
	 * @param sub
	 *            the displaymode for edges inside the subgraph
	 * @param comp
	 *            the displaymode for edges outside the subgraph
	 */

	public void setEdgeDisplayMode(DisplayMode sub, DisplayMode comp) {
		setEdgeDisplayMode(sub, comp, false);
	}

	/**
	 * This method should not be called by plugin-developers. It allows gralog
	 * to change the vertexDisplayMode without informing UndoEditListeners.
	 * 
	 * @param sub
	 *            the displayMode for edges inside the subgraph
	 * @param comp
	 *            the displayMode for edges outside the subgraph
	 * @param undoRedoEdit
	 *            if false listeners are not informed about the change
	 */
	public void setEdgeDisplayMode(DisplayMode sub, DisplayMode comp,
			boolean undoRedoEdit) {
		if (!(sub != getEdgeDisplayMode(false) || comp != getEdgeDisplayMode(true)))
			return;
		DisplaySubgraphMode beforeEdit = null;
		if (!undoRedoEdit)
			beforeEdit = getDataClone();
		edgeDisplayMode[0] = sub;
		edgeDisplayMode[1] = comp;
		fireDataChanged(beforeEdit, undoRedoEdit);
	}

	/**
	 * 
	 * @param complement
	 * @return the displaymode for edges inside subgraph ( complement == false )
	 *         respectively outside the subgraph ( complement == true )
	 */
	public DisplayMode getEdgeDisplayMode(boolean complement) {
		if (complement)
			return edgeDisplayMode[1];
		return edgeDisplayMode[0];
	}

	/**
	 * Sets the visibility status for this Displaymode. If <b>false</b> gralog
	 * does not display the subgraph associated to this mode.
	 * 
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		setVisible(visible, false);
	}

	/**
	 * This method should not be called by plugindevelopers. It allows gralog to
	 * change visibility without informing registered listeners.
	 * 
	 * @param visible
	 * @param undoRedoEdit
	 *            if false listeners will not be informed about the change
	 */
	public void setVisible(boolean visible, boolean undoRedoEdit) {
		if (this.visible == visible)
			return;
		DisplaySubgraphMode beforeEdit = null;
		if (!undoRedoEdit)
			beforeEdit = getDataClone();
		this.visible = visible;
		fireDataChanged(beforeEdit, undoRedoEdit);
	}

	/**
	 * 
	 * @return visibilitystatus for this displaymode
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Notifies all listeners of a change to this DisplaySubgraphMode-object.
	 * 
	 * @param beforeEdit -
	 *            The state of DisplaySubgraphMode before the change
	 * @param undoRedoEdit -
	 *            if false undolisteners are not informed about the change
	 */
	protected void fireDataChanged(DisplaySubgraphMode beforeEdit,
			boolean undoRedoEdit) {
		for (DisplaySubgraphModeListener subgraph : listeners)
			subgraph.displaySubgraphModeChanged();
		if (!undoRedoEdit) {
			DisplaySubgraphMode afterEdit = getDataClone();

			for (UndoableEditListener listener : undoListeners)
				listener
						.undoableEditHappened(new UndoableEditEvent(this,
								new DisplayModeUndoableEdit(beforeEdit,
										afterEdit, this)));
		}
	}

	/**
	 * Adds a {@link DisplaySubgraphModeListener} to this DisplaySubgraphMode.
	 * 
	 * @param l
	 */
	public void addListener(DisplaySubgraphModeListener l) {
		if (!listeners.contains(l))
			listeners.add(l);
	}

	private Object readResolve() {
		listeners = new ArrayList<DisplaySubgraphModeListener>();
		undoListeners = new ArrayList<UndoableEditListener>();
		return this;
	}

	public void addUndoableEditListener(UndoableEditListener listener) {
		if (!undoListeners.contains(listener))
			undoListeners.add(listener);
	}

	protected DisplaySubgraphMode getDataClone() {
		DisplaySubgraphMode clone = new DisplaySubgraphMode();
		clone.setEdgeDisplayMode(getEdgeDisplayMode(false),
				getEdgeDisplayMode(true), true);
		clone.setVertexDisplayMode(getVertexDisplayMode(false),
				getVertexDisplayMode(true), true);
		clone.setVisible(isVisible(), true);
		return clone;
	}

	public static class DisplayModeUndoableEdit implements UndoableEdit {

		protected DisplaySubgraphMode beforeEdit;

		protected DisplaySubgraphMode afterEdit;

		protected DisplaySubgraphMode edited;

		protected boolean isAlive = true;

		public DisplayModeUndoableEdit(DisplaySubgraphMode beforeEdit,
				DisplaySubgraphMode afterEdit, DisplaySubgraphMode edited) {
			this.beforeEdit = beforeEdit;
			this.afterEdit = afterEdit;
			this.edited = edited;
		}

		protected void applySettings(DisplaySubgraphMode from,
				DisplaySubgraphMode to) {
			to.setVertexDisplayMode(from.getVertexDisplayMode(false), from
					.getVertexDisplayMode(true), true);
			to.setEdgeDisplayMode(from.getEdgeDisplayMode(false), from
					.getEdgeDisplayMode(true), true);
			to.setVisible(from.isVisible(), true);
		}

		public void undo() throws CannotUndoException {
			applySettings(beforeEdit, edited);
		}

		public boolean canUndo() {
			if (isAlive)
				return true;
			return false;
		}

		public void redo() throws CannotRedoException {
			applySettings(afterEdit, edited);
		}

		public boolean canRedo() {
			if (isAlive)
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