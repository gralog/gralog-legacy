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

package de.hu.gralog.gui.document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import net.infonode.docking.OperationAbortedException;
import de.hu.gralog.app.InputOutputException;
import de.hu.gralog.app.UserException;
import de.hu.gralog.gui.GUndoManager;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.gui.UndoManagerListener;
import de.hu.gralog.jgraph.GJGraph;
import de.hu.gralog.util.WeakListenerList;

public class Document implements UndoManagerListener, DocumentContentListener {
	
	private final  WeakListenerList<DocumentListener> documentListeners = new WeakListenerList<DocumentListener>();
	private static int newDocIdx = 1;
	
	private int docIdx;
	private File file;
	
	private DocumentContent content;
	
	private GUndoManager undoManager = new GUndoManager();
		
	public Document( DocumentContent content ) {
		this.content = content;
		registerListeners();
		docIdx = newDocIdx++;
	}
	
	public Document( DocumentContent content, File file ) {
		this.content = content;
		this.file = file;
		registerListeners();
	}
	
	public GJGraph getGraph() {
		return content.getGraph();
	}
	
	public void registerListeners() {
		undoManager.addUndoManagerListener( this );
		content.registerUndoManager( undoManager );
		content.addDocumentContentListener( this );
	}
	
	public DocumentContent getContent() {
		return content;
	}
	
	public File getFile() {
		return file;
	}
	
	public String getName() {
		if ( file != null )
			return file.getName();
		return	"Dokument" + Integer.toString( docIdx );
	}
	
	public String getTitle() {
		String title = getName();
		if ( isModified() )
			title = title + "*";
		return title;
	}
	
	public void save() throws UserException, OperationAbortedException {
		if (file == null) {
			file = MainPad.getInstance().showFileDialog( content.getClass(), "Save " + getName() );
			
			if ( file == null )
				throw new OperationAbortedException();
		}
		
		try {
			if ( ! file.exists( ) )
				file.createNewFile();
			content.write( DocumentContentFactory.getInstance().getFileFormat( file ), new FileOutputStream( file ) );
			undoManager.discardAllEdits();
			fireDocumentModifiedStatusChanged();
		} catch (FileNotFoundException e) {
			throw new UserException( "unable to save document to file: " + file, e );
		} catch (InputOutputException e) {
			throw new UserException( "unable to save document to file: " + file, e );
		} catch (IOException e) {
			throw new UserException( "unable to save document to file: " + file, e );
		}
	}
	
	public void saveAs() throws UserException {
		File newFile = MainPad.getInstance().showFileDialog( content.getClass(), "Save " +  getName() + " As" );

		if ( newFile != null ) {
			try {
				content.write( DocumentContentFactory.getInstance().getFileFormat( newFile ), new FileOutputStream( newFile ) );
				
				undoManager.discardAllEdits();
				file = newFile;
				fireDocumentModifiedStatusChanged();
				
			} catch (FileNotFoundException e) {
				throw new UserException( "Error writing to file: " + file, e );
			} catch (InputOutputException e) {
				throw new UserException( "Error writing to file: " + file, e );
			}
		}
	}
	
	public void rename() throws UserException {
		File newFile = MainPad.getInstance().showFileDialog( content.getClass(), "Rename " + getName() + " to ");
			
		if ( newFile != null ) {
			
			try {
				content.write( DocumentContentFactory.getInstance().getFileFormat( file ), new FileOutputStream( newFile ) );
				
				file.delete();
				file = newFile;
				fireDocumentModifiedStatusChanged();
			} catch (FileNotFoundException e) {
				throw new UserException( "Error writing to file: " + file, e );
			} catch (InputOutputException e) {
				throw new UserException( "Error writing to file: " + file, e );
			} 
		}
	}
	
	public void revert() throws UserException {
		if ( isModified() ) {
//			if ( file != null ) {
//				content = DocumentContentFactory.getInstance().createDocumentContent( content.getClass(), file );
//					
//				registerListeners();
//				undoManager.discardAllEdits();
//				fireDocumentReverted(  );
//				fireDocumentModifiedStatusChanged();
//			} else {
				try {
					content.clear();
					undoManager.discardAllEdits();
					fireDocumentReverted(  );
					fireDocumentModifiedStatusChanged();
				} catch( InputOutputException e ) {
					throw new UserException( "unable to revert document", e );
				}
			//}
		}
	}

	private void fireDocumentComponentReplaced( ) {
		for ( DocumentListener l : documentListeners.getListeners() ) {
			l.documentComponentReplaced( this );
		}
	}

	public boolean isModified() {
		return undoManager.canUndo();
	}
	
	public GUndoManager getUndoManager() {
		return undoManager;
	}
	
	private void fireDocumentModifiedStatusChanged(  ) {
		for ( DocumentListener l : documentListeners.getListeners() )
			l.documentModifiedStatusChanged( this );
	}
	
	private void fireDocumentReverted(  ) {
		for ( DocumentListener l : documentListeners.getListeners() )
			l.documentReverted( this );
	}
	
	private void fireDocumentClosed(  ) {
		for ( DocumentListener l : documentListeners.getListeners() )
			l.documentClosed( this );
	}
	
	public void addDocumentListener(DocumentListener l) {
		documentListeners.addListener( l );
	}

	public void removeDocumentListener(DocumentListener l) {
		documentListeners.removeListener( l );
	}

		
	public String toString() {
		return getName();
	}

	public void statusChanged(GUndoManager undoManager) {
		fireDocumentModifiedStatusChanged();
	}

	public void componentReplaced() {
		fireDocumentComponentReplaced();
	}
	
	public void close() {
		fireDocumentClosed();
	}
}
