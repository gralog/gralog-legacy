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

import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JScrollPane;
import javax.swing.undo.UndoManager;

import de.hu.games.app.InputOutputException;
import de.hu.games.graph.io.XMLDecoderIO;
import de.hu.games.gui.FileFormat;
import de.hu.games.gui.GraphEditor;
import de.hu.games.jgraph.GJGraph;

public class GJGraphDocumentContent extends DocumentContent {

	protected static final FileFormat XML_DECODER_FILE_FORMAT = new FileFormat( "XML-Graphbeschreibung", "xgr" );

	protected static final FileFormat[] FILE_FORMATS = { XML_DECODER_FILE_FORMAT };
	
	private GJGraph graph;
	private GJGraphDocumentContent initialState;
	private UndoManager undoManager;
	
	public GJGraphDocumentContent() {
		super();
	}
	
	public GJGraphDocumentContent( GJGraph graph ) {
		this( graph, true );
	}
	
	public GJGraphDocumentContent( GJGraph graph, boolean copy ) {
		this.graph = graph;
		init();
		if ( copy )
			initialState = new GJGraphDocumentContent( new XMLDecoderIO().getDataCopy( graph ), false );
	}

	protected void init() {
		new JScrollPane( graph );
	}
	
	@Override
	public GJGraph getGraph() {
		return graph;
	}
	
	@Override
	public DocumentContent read(FileFormat format, InputStream in) throws InputOutputException {
		if ( format == XML_DECODER_FILE_FORMAT )
			return new XMLDecoderIO().readGJGraphDocumentContent( in );
		return null;
	}

	@Override
	public void write(FileFormat format, OutputStream out) throws InputOutputException {
		if ( format == XML_DECODER_FILE_FORMAT )
			new XMLDecoderIO().writeGJGraphDocumentContent( this, out );
	}

	@Override
	public void registerUndoManager(UndoManager undoManager) {
		this.undoManager = undoManager;
		graph.getModel().addUndoableEditListener( undoManager );
	}
	
	@Override
	public void clear() {
		XMLDecoderIO xmlIO = new XMLDecoderIO();
		
		graph = xmlIO.getDataCopy( initialState.graph );
		component = null;
		init();
		registerUndoManager( undoManager );
		fireComponentReplaced();
	}

	@Override
	public void initComponent() {
		component = new GraphEditor( graph );
	}

	public String toString() {
		return "Graph";
	}

	@Override
	public FileFormat[] getFileFormats() {
		return FILE_FORMATS;
	}
	
}
