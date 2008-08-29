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

import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JScrollPane;
import javax.swing.undo.UndoManager;

import de.hu.gralog.app.InputOutputException;
import de.hu.gralog.graph.io.XMLDecoderIOFast;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.gui.components.GraphEditor;
import de.hu.gralog.jgraph.GJGraph;
import de.hu.gralog.jgraph.GJGraphUtil;

public class GJGraphDocumentContent extends DocumentContent {

	protected static final FileFormat XML_DECODER_FILE_FORMAT = new FileFormat( "XML-Graphbeschreibung", "xgr" );

	protected static final FileFormat[] FILE_FORMATS = { XML_DECODER_FILE_FORMAT };
	
	private GJGraph graph;
	private InputStream initialStateGraph;
	private UndoManager undoManager;
	
	public GJGraphDocumentContent() {
		super();
	}
	
	public GJGraphDocumentContent( GJGraph graph ) {
		this( graph, MainPad.getInstance().isRevertEnabled() );
	}
	
	private GJGraphDocumentContent( GJGraph graph, boolean copy ) {
		this.graph = graph;
		init();
		if ( copy )
			initialStateGraph = GJGraphUtil.getGJGraphAsStream( graph );
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
			return new XMLDecoderIOFast().readGJGraphDocumentContent( in );
		return null;
	}

	@Override
	public void write(FileFormat format, OutputStream out) throws InputOutputException {
		if ( format == XML_DECODER_FILE_FORMAT )
			new XMLDecoderIOFast().writeGJGraphDocumentContent( this, out );
	}

	@Override
	public void registerUndoManager(UndoManager undoManager) {
		this.undoManager = undoManager;
		graph.getModel().addUndoableEditListener( undoManager );
	}
	
	@Override
	public void clear() throws InputOutputException {
		graph = GJGraphUtil.getGJGraphFromStream( initialStateGraph );
		
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
