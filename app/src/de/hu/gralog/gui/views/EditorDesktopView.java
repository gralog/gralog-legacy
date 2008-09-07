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

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowAdapter;
import net.infonode.docking.OperationAbortedException;
import net.infonode.docking.RootWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.ViewSerializer;
import net.infonode.docking.WindowPopupMenuFactory;
import net.infonode.docking.util.DockingUtil;
import net.infonode.util.Direction;
import de.hu.gralog.algorithm.result.AlgorithmResultInfo;
import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.GralogGraphTypeInfo;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.gui.document.Document;
import de.hu.gralog.gui.document.DocumentContentFactory;
import de.hu.gralog.gui.document.DocumentListener;
import de.hu.gralog.jgraph.GJGraph;
import de.hu.gralog.util.WeakListenerList;

public class EditorDesktopView extends View implements DocumentListener {

	private final  WeakListenerList<EditorDesktopViewListener> listeners = new WeakListenerList<EditorDesktopViewListener>();
	private RootWindow rootWindow;
	private ArrayList<DocumentView> documentViews = new ArrayList<DocumentView>();
	private DocumentView currentView = null;
	private ArrayList<View> removeDummyDeSerializedViews = new ArrayList();
	private static final JPopupMenu POPUP_MENU = new JPopupMenu();
		
	public EditorDesktopView() {
		super( "desktop", null, new RootWindow( new ForwardingViewSerializer() ) );
		rootWindow = (RootWindow)getComponent();
		
		rootWindow.getRootWindowProperties().addSuperObject( MainPad.THEME.getRootWindowProperties() );
//		RootWindowProperties titleBarStyleProperties = PropertiesUtil.createTitleBarStyleRootWindowProperties();
//		rootWindow.getRootWindowProperties().addSuperObject( titleBarStyleProperties );
		
		getViewProperties().setAlwaysShowTitle( false );
		getWindowProperties().setMaximizeEnabled( false );
		getWindowProperties().setMinimizeEnabled( false );
		getWindowProperties().setCloseEnabled( false );
		getWindowProperties().setUndockEnabled( false );
		
		rootWindow.getRootWindowProperties().setRecursiveTabsEnabled( false );
		rootWindow.getRootWindowProperties().getDockingWindowProperties().setUndockEnabled( false );
		rootWindow.getRootWindowProperties().getDockingWindowProperties().setMaximizeEnabled( false );
		rootWindow.getRootWindowProperties().getDockingWindowProperties().setMinimizeEnabled( false );
		rootWindow.getRootWindowProperties().getDockingWindowProperties().setCloseEnabled( true );
		rootWindow.getWindowBar( Direction.DOWN ).setEnabled( false );
		rootWindow.addListener( new DesktopDocumentViewListener() );
		
		POPUP_MENU.add( MainPad.FILE_CLOSE_ACTION );
		POPUP_MENU.add( MainPad.FILE_CLOSE_OTHERS_ACTION );
		POPUP_MENU.add( MainPad.FILE_CLOSE_ALL_ACTION );
		POPUP_MENU.add( MainPad.FILE_REVERT_ACTION );
		POPUP_MENU.add( MainPad.FILE_RENAME_ACTION );
		
		rootWindow.setPopupMenuFactory( new WindowPopupMenuFactory() {
			public JPopupMenu createPopupMenu( DockingWindow window ) {
				final DockingWindow w = window;
				if ( window instanceof DocumentView ) {
					DocumentView view = (DocumentView)w;
					view.makeVisible();
					currentView = view;
					
					fireCurrentDocumentSelectionChanged();
					return POPUP_MENU;
				}
				return null;
			}
		});
		((ForwardingViewSerializer)rootWindow.getViewSerializer()).setSerializer( new DocumentViewSerializer() );
		updateControls();
	}
	
	public void openDocument( GJGraph graph ) {
		openDocument( new Document( DocumentContentFactory.getInstance().createDocumentContent( graph ) ) );
	}
	
	public void openDocument( File file ) {
		DocumentView view = getView( file );
		
		if ( view == null ) {
			try {
				Document document = new Document( DocumentContentFactory.getInstance().createDocumentContent( file ), file );
				openDocument( document );
			} catch(UserException e) {
				MainPad.getInstance().handleUserException( e );
			}
		} else
			setCurrentView( view );
                System.out.println("");
	}
	
	public void newDocument(GralogGraphTypeInfo graphType) {
		try {
			openDocument(new Document( DocumentContentFactory.getInstance().createDocumentContent( graphType )));
		} catch(UserException e) {
			MainPad.getInstance().handleUserException( e );
			return;
		}
	}
	
	public void newDocument( AlgorithmResultInfo info ) {
		openDocument(new Document( DocumentContentFactory.getInstance().createDocumentContent( info )));
	}
	
	public void openDocument( Document document ) {
		document.addDocumentListener( this );
		openView( document );
	}
	
	public void saveDocument() {
		try {
			getCurrentDocument().save();
		} catch(UserException e) {
			MainPad.getInstance().handleUserException( e );
		} catch(OperationAbortedException e) {
			// do nothing
		}
	}
	
	public void saveDocumentAs() {
		try {
			getCurrentDocument().saveAs();
		} catch(UserException e) {
			MainPad.getInstance().handleUserException( e );
		}
	}
	
	public void saveAllDocuments() {
		try {
			for ( DocumentView view : documentViews ) {
				view.getDocument().save();
			}
		} catch (UserException e) {
			MainPad.getInstance().handleUserException( e );
		} catch (OperationAbortedException e) {
			// to nothing
		}
	}

	public void closeDocument(  ) {
		try {
			getCurrentView().closeWithAbort();
		} catch( OperationAbortedException e ) {
			// do nothing
		}
	}
	
	public void closeOtherDocuments() {
		try {
			for ( DocumentView view : new ArrayList<DocumentView>( documentViews ) ) {
				if ( view != getCurrentView() ) {
					view.closeWithAbort();
				}
			}
		} catch(OperationAbortedException e) {
			// to nothing
		}
	}
	
	public void closeAllDocuments() {
		try {
			for ( DocumentView view : new ArrayList<DocumentView>(documentViews) ) {
				view.closeWithAbort();
			}
		} catch( OperationAbortedException e ) {
			// do nothing
		}
	}
	
	public void revertDocument() {
		try {
			getCurrentDocument().revert();
		} catch( UserException e ) {
			MainPad.getInstance().handleUserException( e );
		}
	}
	
	public void renameDocument() {
		try {
			getCurrentDocument().rename();
		} catch( UserException e ) {
			MainPad.getInstance().handleUserException( e );
		}
	}
	
	public boolean isEmpty() {
		return documentViews.size() == 0;
	}
	
	public boolean moreThanOneFileOpen() {
		return documentViews.size() > 1;
	}
	
	public boolean isModified() {
		for ( DocumentView view : documentViews ) {
			if ( view.getDocument().isModified() )
				return true;
		}
		return false;
	}
	
	public DocumentView getView( File file ) {
		for(DocumentView editorView : documentViews) {
			if ( editorView.getDocument().getFile() != null && editorView.getDocument().getFile().equals( file ) )
				return editorView;
		}
		return null;	
	}
	
	public DocumentView getView( Document document ) {
		for(DocumentView editorView : documentViews) {
			if ( editorView.getDocument() == document )
				return editorView;
		}
		return null;	
	}
	
	protected void openView(Document document) {
		DocumentView view = new DocumentView( document );
		
		documentViews.add( view );

		DockingWindow window = rootWindow.getWindow();
		if ( window == null )
			rootWindow.setWindow( view );
		else {
			if ( window instanceof TabWindow )
				((TabWindow)window).addTab( view );
			else {
				DockingWindow parent = currentView.getWindowParent();
				if ( parent instanceof TabWindow )
					((TabWindow)parent).addTab( view );
				else {
					MainPad.getInstance().handleUserException( new UserException( "internal error: " + getClass().getName()+ ".openView(Document document)" ) );
				}
			}
		}
			
		DockingUtil.addWindow( view, rootWindow );
		setCurrentView( view );
	}
	
	protected void setCurrentView( DocumentView view ) {
		if ( view.getWindowParent() == null )
			view.restore();
		view.makeVisible();
		view.requestFocusInWindow();
	}
	
	protected DocumentView getCurrentView() {
		if ( documentViews.contains( currentView ) )
			return currentView;
		return null;
	}
	
	public Document getCurrentDocument() {
		if ( getCurrentView() == null )
			return null;
		return getCurrentView().getDocument();
	}

	public GJGraph getCurrentGraph() {
		Document document = getCurrentDocument();
		if ( document != null )
			return document.getGraph();
		return null;
	}

	public ArrayList<Document> getOpenDocuments() {
		ArrayList<Document> documents = new ArrayList<Document>();
		for ( DocumentView view : documentViews ) {
			documents.add( view.getDocument() );
		}
		return documents;
	}
	
	protected void fireCurrentDocumentSelectionChanged() {
		
		for ( EditorDesktopViewListener l : listeners.getListeners() )
			l.currentDocumentSelectionChanged(  );
		updateControls();
	}
	
	public void addEditorDesktopViewListener(EditorDesktopViewListener l) {
		listeners.addListener( l );
	}

	public void removeEditorDesktopViewListener(EditorDesktopViewListener l) {
		listeners.removeListener( l );
	}
		
	private class DesktopDocumentViewListener extends DockingWindowAdapter {
		
		public void viewFocusChanged(View previouslyFocusedView, View focusedView) {
			if ( focusedView != null && focusedView instanceof DocumentView ) {
				currentView = (DocumentView)focusedView;
				fireCurrentDocumentSelectionChanged();
			}
		}

		public void windowClosing(DockingWindow window) throws OperationAbortedException {
			if ( window instanceof DocumentView )
				saveWithAbort( (DocumentView)window );
			else
				saveAllWithAbort();
		}

		@Override
		public void windowClosed(DockingWindow window) {
			if ( window instanceof DocumentView )
				removeView( (DocumentView)window );
			fireCurrentDocumentSelectionChanged();
			rootWindow.restoreFocus();
		}
		
		
	}

	protected void saveWithAbort( DocumentView editor ) throws OperationAbortedException {
		if ( editor.getDocument().isModified() ) {
			int retValue = JOptionPane.showOptionDialog( MainPad.getInstance(), "Save changes for document " + editor.getDocument().getName() + " ?", "Close Document", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, MainPad.YES_NO_CANCEL_BUTTON_TEXT, MainPad.YES_NO_CANCEL_BUTTON_TEXT[0] );
			if ( retValue == JOptionPane.YES_OPTION ) {
				try {
					editor.getDocument().save();
				} catch( UserException e ) {
					MainPad.getInstance().handleUserException( e );
				}
			}
			if ( retValue == JOptionPane.CANCEL_OPTION )
				throw new OperationAbortedException();
		}
	}
	
	public void saveAllWithAbort() throws OperationAbortedException {
		for ( DocumentView editor : documentViews ) {
			saveWithAbort( editor );
		}
	}

	protected void removeView( DocumentView view ) {
		if ( getCurrentView() == view )
			currentView = null;
		view.getDocument().close();
		rootWindow.removeView( view );
		documentViews.remove( view );
	}
	
	protected void closeDocumentsWithoutFiles() {
		for ( DocumentView editor : new ArrayList<DocumentView>( documentViews ) ) {
			if ( editor.getDocument().getFile() == null )
				editor.close();
		}
	}
	
	public void prepareSerializing() throws OperationAbortedException {
		saveAllWithAbort();
		closeDocumentsWithoutFiles();
	}
	
	private class DocumentViewSerializer implements ViewSerializer {
		public View readView(ObjectInputStream in) throws IOException {
			try {
				Class contentType = getClass().forName( in.readUTF() );
				File file = new File( in.readUTF() );

				openDocument( file );
				if ( getView( file ) == null ) {
					View editor = new View( "dummy", null, null );
					removeDummyDeSerializedViews.add( editor );
					return editor;
				}
				return getView( file );
			} catch (ClassNotFoundException e) {
				MainPad.getInstance().handleUserException( new UserException( "error loading contenttype", e) );
			}
			return null;
		}

		public void writeView(View view, ObjectOutputStream out) throws IOException {
			DocumentView editor = (DocumentView)view;
			out.writeUTF( editor.getDocument().getContent().getClass().getCanonicalName() );
			out.writeUTF( editor.getDocument().getFile().getAbsolutePath() );
		}
		
	}

	private static class ForwardingViewSerializer implements ViewSerializer {

		private ViewSerializer serializer;
		
		public ForwardingViewSerializer() {
			
		}
		
		public void setSerializer( ViewSerializer serializer ) {
			this.serializer = serializer;
		}
		
		public View readView(ObjectInputStream in) throws IOException {
			return serializer.readView( in );
		}

		public void writeView(View view, ObjectOutputStream out) throws IOException {
			serializer.writeView( view, out );
		}
		
	}
	public void readView(ObjectInputStream in) throws IOException {
		rootWindow.read( in );	
		for ( View view : removeDummyDeSerializedViews ) {
			view.close();
			rootWindow.removeView( view );
		}
		for ( DocumentView view : documentViews ) {
			view.updateTitle();
		}
		removeDummyDeSerializedViews.clear();
	}

	public void writeView(ObjectOutputStream out) throws IOException {
		rootWindow.write( out );
	}

	public void restoreFocusAfterStart() {
		for ( DocumentView view : documentViews ) {
			if ( view.isShowing() ) {
				view.requestFocusInWindow();
				break;
			}
		}
	}
	
	public void documentModifiedStatusChanged(Document document) {
		updateControls();
	}

	protected void updateControls() {
		if ( moreThanOneFileOpen() )
			MainPad.FILE_CLOSE_OTHERS_ACTION.setEnabled( true );
		else
			MainPad.FILE_CLOSE_OTHERS_ACTION.setEnabled( false );
		if ( isEmpty() )
			MainPad.FILE_CLOSE_ALL_ACTION.setEnabled( false );
		else
			MainPad.FILE_CLOSE_ALL_ACTION.setEnabled( true );
		if ( isModified() )
			MainPad.FILE_SAVE_ALL_ACTION.setEnabled( true );
		else
			MainPad.FILE_SAVE_ALL_ACTION.setEnabled( false );
		
		if ( getCurrentDocument() == null ) {
			MainPad.FILE_CLOSE_ACTION.setEnabled( false );
			
			MainPad.FILE_SAVE_ACTION.setEnabled( false );
			MainPad.FILE_SAVE_AS_ACTION.setEnabled( false );
			
			MainPad.FILE_REVERT_ACTION.setEnabled( false && MainPad.getInstance().isRevertEnabled() );
			
			MainPad.FILE_RENAME_ACTION.setEnabled( false );
			
			MainPad.UNDO_ACTION.setEnabled( false );
			MainPad.REDO_ACTION.setEnabled( false );
			
			MainPad.EDIT_SELECT_ALL_ACTION.setEnabled( false );
			MainPad.EDIT_SELECT_ALL_NODES_ACTION.setEnabled( false );
			MainPad.EDIT_SELECT_ALL_EDGES_ACTION.setEnabled( false );
			
			MainPad.ZOOM_11_ACTION.setEnabled( false );
			MainPad.ZOOM_FIT_IN_CANVAS_ACTION.setEnabled( false );
			MainPad.ZOOM_IN_ACTION.setEnabled( false );
			MainPad.ZOOM_OUT_ACTION.setEnabled( false );
		} else {
			MainPad.FILE_CLOSE_ACTION.setEnabled( true );
			MainPad.FILE_SAVE_AS_ACTION.setEnabled( true );
			
			MainPad.EDIT_SELECT_ALL_ACTION.setEnabled( true );
			MainPad.EDIT_SELECT_ALL_NODES_ACTION.setEnabled( true );
			MainPad.EDIT_SELECT_ALL_EDGES_ACTION.setEnabled( true );
			
			MainPad.ZOOM_11_ACTION.setEnabled( true );
			MainPad.ZOOM_FIT_IN_CANVAS_ACTION.setEnabled( true );
			MainPad.ZOOM_IN_ACTION.setEnabled( true );
			MainPad.ZOOM_OUT_ACTION.setEnabled( true );
			
			if ( getCurrentDocument().isModified() ) {
				MainPad.FILE_SAVE_ACTION.setEnabled( true );
				MainPad.FILE_REVERT_ACTION.setEnabled( true && MainPad.getInstance().isRevertEnabled() );
				MainPad.FILE_RENAME_ACTION.setEnabled( false );
			} else {
				MainPad.FILE_SAVE_ACTION.setEnabled( false );
				MainPad.FILE_REVERT_ACTION.setEnabled( false && MainPad.getInstance().isRevertEnabled() );
				MainPad.FILE_RENAME_ACTION.setEnabled( true );
			}
			
			if ( getCurrentDocument().getUndoManager().canUndo() )
				MainPad.UNDO_ACTION.setEnabled( true );
			else
				MainPad.UNDO_ACTION.setEnabled( false );
			
			if ( getCurrentDocument().getUndoManager().canRedo() )
				MainPad.REDO_ACTION.setEnabled( true );
			else
				MainPad.REDO_ACTION.setEnabled( false );
			if ( getCurrentDocument().getGraph() == null || getCurrentDocument().getGraph().isElementsAndStructureEditable() == false ) {
				MainPad.ES_CREATE_EDGE_ACTION.setEnabled( false );
				MainPad.ES_CREATE_VERTEX_ACTION.setEnabled( false );
				MainPad.EDIT_COPY_ACTION.setEnabled( true );
				MainPad.EDIT_CUT_ACTION.setEnabled( false );
				MainPad.EDIT_DELETE_ACTION.setEnabled( false );
				MainPad.EDIT_PASTE_ACTION.setEnabled( false );
			} else {
				MainPad.ES_CREATE_EDGE_ACTION.setEnabled( true );
				MainPad.ES_CREATE_VERTEX_ACTION.setEnabled( true );
				MainPad.EDIT_COPY_ACTION.setEnabled( true );
				MainPad.EDIT_CUT_ACTION.setEnabled( true );
				MainPad.EDIT_DELETE_ACTION.setEnabled( true );
				MainPad.EDIT_PASTE_ACTION.setEnabled( true );
			}
		}
	}

	public void documentComponentReplaced(Document document) {
		// 
	}

	public void documentReverted(Document document) {
		// 
		
	}

	public void documentClosed(Document document) {
		// TODO Auto-generated method stub
		
	}
}
