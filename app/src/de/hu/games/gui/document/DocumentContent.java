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

package de.hu.games.gui.document;

import java.awt.Component;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.undo.UndoManager;

import de.hu.games.app.InputOutputException;
import de.hu.games.gui.FileFormat;
import de.hu.games.jgraph.GJGraph;

public abstract class DocumentContent {
	
	protected ArrayList<DocumentContentListener> listeners = new ArrayList<DocumentContentListener>();
	protected Component component = null;
		
	public DocumentContent() {
		
	}

	public abstract FileFormat[] getFileFormats();
	public abstract GJGraph getGraph();
	public abstract void initComponent();
	
	public Component getComponent() {
		if ( component == null )
			initComponent();
		return component;
	}
	
	public abstract DocumentContent read(FileFormat format, InputStream in) throws InputOutputException;
	public abstract void write(FileFormat format, OutputStream out) throws InputOutputException;
	
	public abstract void clear();
	public abstract void registerUndoManager( UndoManager undoManager );
	
	protected void fireComponentReplaced() {
		for ( DocumentContentListener l : listeners )
			l.componentReplaced();
	}
	
	public void addDocumentContentListener( DocumentContentListener l ) {
		if ( !listeners.contains( l ) )
			listeners.add( l );
	}
	
	public void removeDocumentContentListener( DocumentContentListener l ) {
		listeners.remove( l );
	}
}
