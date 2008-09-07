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

import javax.swing.undo.UndoManager;

import de.hu.gralog.algorithm.result.AlgorithmInfoListener;
import de.hu.gralog.algorithm.result.AlgorithmResultInfo;
import de.hu.gralog.app.InputOutputException;
import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.io.XMLDecoderIO;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.gui.components.GraphEditor;
import de.hu.gralog.jgraph.GJGraph;

public class AlgorithmResultDocumentContent extends DocumentContent implements AlgorithmInfoListener {

	protected static final FileFormat XML_FILE_FORMAT = new FileFormat( "XML-Algorithmen Ergebnisbeschreibung", "xae" );
	protected static final FileFormat[] FILE_FORMATS = { XML_FILE_FORMAT };
	
	private UndoManager undoManager;
	private AlgorithmResultDocumentContent initialState;
	
	private AlgorithmResultInfo info;
	
	public AlgorithmResultDocumentContent( ) {
		super();
	}
	
	public AlgorithmResultDocumentContent( AlgorithmResultInfo info ) {
		this( info, MainPad.getInstance().isRevertEnabled() );
	}
	
	private AlgorithmResultDocumentContent( AlgorithmResultInfo info, boolean copy ) {
		this.info = info;
		info.addListener( this );
		
		XMLDecoderIO xmlIO = new XMLDecoderIO();
		if ( copy )
			initialState = new AlgorithmResultDocumentContent( xmlIO.getDataCopy( info ), false );
	}
	
	@Override
	public GJGraph getGraph() {
		try {
			return info.getGraph( );
		} catch (UserException e) {
			MainPad.getInstance().handleUserException( e );
		}
		return null;
	}
	
	public AlgorithmResultInfo getAlgorithmResultInfo() {
		return info;
	}
	
	@Override
	public FileFormat[] getFileFormats() {
		return FILE_FORMATS;
	}
	
	@Override
	public void registerUndoManager(UndoManager undoManager) {
		this.undoManager = undoManager;
		info.registerUndoManager( undoManager );
	}
	
	@Override
	public void clear() throws InputOutputException {
		/*XMLDecoderIO xmlIO = new XMLDecoderIO();
		
		info = xmlIO.getDataCopy( initialState.info );
		info.addListener( this );
		component = null;
		
		registerUndoManager( undoManager );
		fireComponentReplaced();*/
	}

	@Override
	public void initComponent() {
		component = new GraphEditor( getGraph() );
	}

	public String toString() {
		return "Algorithm result";
	}

	@Override
	public DocumentContent read(FileFormat format, InputStream in) throws InputOutputException {
		if ( format == XML_FILE_FORMAT )
			return new XMLDecoderIO().readAlgorithmResultDocumentContent( in );
		return null;
	}

	@Override
	public void write(FileFormat format, OutputStream out) throws InputOutputException {
		if ( format == XML_FILE_FORMAT )
			new XMLDecoderIO().writeAlgorithmResultDocumentContent( this, out );
	}

	public void graphReplaced() {
		component = null;
		fireComponentReplaced();
	}

	public void contentChanged() {
		
	}
	
}
