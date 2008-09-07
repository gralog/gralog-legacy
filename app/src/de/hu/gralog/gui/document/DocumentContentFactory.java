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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.filechooser.FileFilter;

import de.hu.gralog.algorithm.result.AlgorithmResultInfo;
import de.hu.gralog.app.InputOutputException;
import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.GralogGraphFactory;
import de.hu.gralog.graph.GralogGraphTypeInfo;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.jgraph.GJGraph;

public class DocumentContentFactory {
	
	private static final DocumentContentFactory INSTANCE = new DocumentContentFactory();
	private Hashtable<Class<? extends DocumentContent>, FileFormat[]> fileFormatsCache = new Hashtable<Class<? extends DocumentContent>, FileFormat[]>();
	private FileFormat[] fileFormats;
	
	private DocumentContentFactory() {
		
	}
	
	public static DocumentContentFactory getInstance() {
		return INSTANCE;
	}
	
	public FileFormat getFileFormat( File file ) {
		for ( FileFormat format : getFileFormats() ) {
			if ( format.acceptsFile( file ) )
				return format;
		}
		return null;
	}
	
	public Class<? extends DocumentContent> getContentType( File file ) {
		getFileFormats();
		for ( Map.Entry<Class<? extends DocumentContent>, FileFormat[]> entry : fileFormatsCache.entrySet() ) {
			for ( FileFormat format : entry.getValue() ) {
				if ( format.acceptsFile( file ) )
					return entry.getKey();
			}
		}
		return null;
	}
	
	public DocumentContent createDocumentContent( File file ) throws UserException {
		if ( ! file.exists() )
			throw new UserException( "Error opening file", 
					"The file: \n" + 
					file + "\n" +   
					" does not exist!" );
		if ( ! file.isFile() )
			throw new UserException( "Error opening file", 
					"The file: \n" + 
					file + "\n" +   
					" is not a regular file!" );
		if ( ! file.canRead() )
			throw new UserException( "Error opening file", 
					"The file: \n" + 
					file + "\n" +   
					" cannot be read!" );
		if ( ! file.canWrite() )
			throw new UserException( "Error opening file", 
					"The file: \n" + 
					file + "\n" +   
					" cannot be written!" );
		if ( getContentType( file ) == null )
			throw new UserException( "Unable to retrieve type from file " +  file );
		return createDocumentContent( getContentType( file ), file );
	}
	
	protected DocumentContent createDocumentContent( Class<? extends DocumentContent> contentType, File file ) throws UserException {
		try {
			return contentType.newInstance().read( getFileFormat( file ), new FileInputStream( file )  );
		} catch( InputOutputException e ) {
			throw new UserException( "unable to read content" + contentType.getName() + " from file " + file.toString(), e );
		} catch (FileNotFoundException e) {
			throw new UserException( "unable to read content" + contentType.getName() + " from file " + file.toString(), e );
		} catch (InstantiationException e) {
			throw new UserException( "unable to read content" + contentType.getName() + " from file " + file.toString(), e );
		} catch (IllegalAccessException e) {
			throw new UserException( "unable to read content" + contentType.getName() + " from file " + file.toString(), e );
		}
	}
	
	public DocumentContent createDocumentContent( GralogGraphTypeInfo graphType ) throws UserException {
		DocumentContent content = null;
		
		content = new GJGraphDocumentContent( new GJGraph( GralogGraphFactory.createGraphSupport( graphType ) ) );
		
		return content;
	}
	
	public DocumentContent createDocumentContent( GJGraph graph ) {
		return new GJGraphDocumentContent( graph );
	}
	
	public DocumentContent createDocumentContent( AlgorithmResultInfo info ) {
		return new AlgorithmResultDocumentContent( info );
	}

	public FileFormat[] getFileFormats(Class<? extends DocumentContent> contentType) {
		try {
			FileFormat[] back = fileFormatsCache.get( contentType );
			if ( back == null ) {
				back = contentType.newInstance().getFileFormats();
				fileFormatsCache.put( contentType, back );
			}
			return back;
		} catch (InstantiationException e) {
			MainPad.getInstance().handleUserException( new UserException( "could not initiate documentcontent of type: " +  contentType, e ) );
		} catch (IllegalAccessException e) {
			MainPad.getInstance().handleUserException( new UserException( "could not initiate documentcontent of type: " +  contentType, e ) );
		}
		return null;
	}
	
	public FileFormat getFileFormat( FileFilter filter ) {
		for ( FileFormat format : getFileFormats() ) {
			if ( format.getFileFilter() == filter )
				return format;
		}
		return null;
	}
	
	public FileFormat[] getFileFormats() {
		if ( fileFormats == null ) {
			getFileFormats( GJGraphDocumentContent.class );
			getFileFormats( AlgorithmResultDocumentContent.class );
			
			int count = 0;
			for ( FileFormat[] fs : fileFormatsCache.values() )
				count = count + fs.length;
			fileFormats = new FileFormat[ count ];
			
			int i = 0;
			for ( FileFormat[] fs : fileFormatsCache.values() ) {
				for ( FileFormat f : fs )
					fileFormats[i++] = f;
			}
		}
		return fileFormats;
	}

	
}
