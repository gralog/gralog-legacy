/*
 * Created on 2006 by Sebastian Ordyniak
 *
 * Copyright 2006 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of GrALoG.
 *
 * GrALoG is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * GrALoG is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GrALoG; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */
package de.hu.gralog.algorithm.result;

import java.util.ArrayList;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

/**
 * This class represents a display-mode for element-tips. You can specify whether
 * or not the element-tips should be visible.
 * 
 * @author ordyniak
 * 
 */
public class ElementTipsDisplayMode {
	protected transient ArrayList<UndoableEditListener> undoListeners = new ArrayList<UndoableEditListener>();

	protected transient ArrayList<ElementTipsDisplayModeListener> listeners = new ArrayList<ElementTipsDisplayModeListener>();

	protected boolean visible = true;

	/**
	 * Specifies whether or not the elementtips should be displayed by GrALoG.
	 * 
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		setVisible(visible, false);
	}

	/**
	 * This method allows GrALoG to change the visibility-state without
	 * informing UndoRedoListeners. It should not be called by
	 * plugin-developers.
	 * 
	 * @param visible
	 * @param undoRedoEdit
	 *            if false UndoRedoListeners are not informed about the change.
	 */
	public void setVisible(boolean visible, boolean undoRedoEdit) {
		if (this.visible == visible)
			return;
		ElementTipsDisplayMode beforeEdit = null;
		if (!undoRedoEdit)
			beforeEdit = getDataClone();

		this.visible = visible;

		fireDataChanged(beforeEdit, undoRedoEdit);
	}

	/**
	 * 
	 * @return the visibility-state of this ElementTips
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Informs all listeners about changes to this ElementTips-instance.
	 * 
	 * @param beforeEdit
	 *            the ElementTips-instance before the change
	 * @param undoRedoEdit
	 *            if false UndoRedoListeners are not informed about the change
	 */
	protected void fireDataChanged(ElementTipsDisplayMode beforeEdit,
			boolean undoRedoEdit) {
		for (ElementTipsDisplayModeListener l : listeners)
			l.elementTipsDisplayModeChanged();
		if (!undoRedoEdit) {
			ElementTipsDisplayMode afterEdit = getDataClone();
			for (UndoableEditListener l : undoListeners)
				l.undoableEditHappened(new UndoableEditEvent(this,
						new ElementTipsDisplayModeUndoableEdit(beforeEdit,
								afterEdit, this)));
		}
	}

	public ElementTipsDisplayMode getDataClone() {
		ElementTipsDisplayMode mode = new ElementTipsDisplayMode();
		mode.setVisible(isVisible(), true);
		return mode;
	}

	public void addElementTipsDisplayModeListener(
			ElementTipsDisplayModeListener l) {
		if (!listeners.contains(l))
			listeners.add(l);
	}

	public void addUndoableEditListener(UndoableEditListener l) {
		if (!undoListeners.contains(l))
			undoListeners.add(l);
	}

	private Object readResolve() {
		listeners = new ArrayList<ElementTipsDisplayModeListener>();
		undoListeners = new ArrayList<UndoableEditListener>();
		return this;
	}

	private static class ElementTipsDisplayModeUndoableEdit implements
			UndoableEdit {

		protected ElementTipsDisplayMode beforeEdit, afterEdit, edited;

		protected boolean isAlive = true;

		public ElementTipsDisplayModeUndoableEdit(
				ElementTipsDisplayMode beforeEdit,
				ElementTipsDisplayMode afterEdit, ElementTipsDisplayMode edited) {
			this.beforeEdit = beforeEdit;
			this.afterEdit = afterEdit;
			this.edited = edited;
		}

		protected void applyData(ElementTipsDisplayMode from,
				ElementTipsDisplayMode to) {
			to.setVisible(from.isVisible(), true);
		}

		public void undo() throws CannotUndoException {
			applyData(beforeEdit, edited);
		}

		public boolean canUndo() {
			if (isAlive)
				return true;
			return false;
		}

		public void redo() throws CannotRedoException {
			applyData(afterEdit, edited);
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