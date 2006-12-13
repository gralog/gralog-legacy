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

package de.hu.games.gui;

import java.util.ArrayList;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class GUndoManager extends UndoManager {

	private final  ArrayList listeners = new ArrayList();
	
	public GUndoManager() {
		MainPad.getInstance().UNDO_ACTION.setEnabled( false );
		MainPad.getInstance().REDO_ACTION.setEnabled( false );
	}

	public synchronized void redo() throws CannotRedoException {
		super.redo();
		if ( !canRedo() )
			MainPad.getInstance().REDO_ACTION.setEnabled( false );
		if ( !MainPad.getInstance().UNDO_ACTION.isEnabled() )
			MainPad.getInstance().UNDO_ACTION.setEnabled( true );
		fireStatusChanged();
	}

	public synchronized void undo() throws CannotUndoException {
		super.undo();
		if ( !canUndo() )
			MainPad.getInstance().UNDO_ACTION.setEnabled( false );
		if (!MainPad.getInstance().REDO_ACTION.isEnabled())
			MainPad.getInstance().REDO_ACTION.setEnabled( true );
		fireStatusChanged();
	}

	public void undoableEditHappened(UndoableEditEvent arg0) {
		super.undoableEditHappened(arg0);
		if (!MainPad.getInstance().UNDO_ACTION.isEnabled() && canUndo())
			MainPad.getInstance().UNDO_ACTION.setEnabled( true );
		if (!canRedo())
			MainPad.getInstance().REDO_ACTION.setEnabled( false );
		else
			MainPad.getInstance().REDO_ACTION.setEnabled( true );
		fireStatusChanged();
	}
	
	private void fireStatusChanged(  ) {
		for (int i = 0; i < listeners.size();i++) {
			UndoManagerListener l = (UndoManagerListener)listeners.get( i );
			l.statusChanged( this );
		}
	}
	
	public void addUndoManagerListener(UndoManagerListener l) {
		if (!listeners.contains( l ))
			listeners.add( l );
	}

	public void removeUndoManagerListener(UndoManagerListener l) {
		listeners.remove( l );
	}
}
