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

package de.hu.gralog.graph.io;

import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.ExceptionListener;
import java.beans.Expression;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import de.hu.gralog.algorithm.result.AlgorithmResultInfo;
import de.hu.gralog.app.InputOutputException;
import de.hu.gralog.app.UserException;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.gui.document.AlgorithmResultDocumentContent;
import de.hu.gralog.gui.document.GJGraphDocumentContent;
import de.hu.gralog.jgraph.GJGraph;

public class XMLDecoderIO implements GJGraphDocumentContentIO, AlgorithmResultDocumentContentIO {

	private static final GJGraphPersistenceDelegate GJGRAPH_PD =  new GJGraphPersistenceDelegate();
	
	public XMLDecoderIO() {
		
	}

	public void writeGraph( GJGraph graph, OutputStream out ) {
		ClassLoader saveCL = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader( MainPad.getInstance().getJarLoader() );
		XMLCoderEL el = new XMLCoderEL();
		XMLEncoder enc = new XMLEncoder( out );
		enc.setExceptionListener( el );
		
		enc.setPersistenceDelegate( graph.getClass(), GJGRAPH_PD );
		
		enc.writeObject( graph );
		enc.close();
		
		if ( el.hasExceptions() ) {
			for ( Exception e : el.getExceptions() ) {
				if ( !(e instanceof UserException) )
					e = new UserException( "unable to write graph: ", e );
				MainPad.getInstance().handleUserException( (UserException)e );
			}
		}
		
		Thread.currentThread().setContextClassLoader( saveCL );
	}
	
	public GJGraph readGraph( InputStream in ) {
		XMLCoderEL el = new XMLCoderEL();
		XMLDecoder dec = new XMLDecoder( in, null, el, MainPad.getInstance().getJarLoader() );
		
		GJGraph graph = (GJGraph)dec.readObject();
		if ( el.hasExceptions() ) {
			for ( Exception e : el.getExceptions() ) {
				if ( !(e instanceof UserException) )
					e = new UserException( "unable to read graph: ", e );
				MainPad.getInstance().handleUserException( (UserException)e );
			}
		}
		return graph;
	}
	
	public void writeGJGraphDocumentContent(GJGraphDocumentContent content,
			OutputStream out) throws InputOutputException {
		writeGraph( content.getGraph(), out );
	}

	public GJGraphDocumentContent readGJGraphDocumentContent(InputStream in)
			throws InputOutputException {
		GJGraph graph = null;
		graph = readGraph( in );
		return new GJGraphDocumentContent( graph );
	}

	public static class GJGraphPersistenceDelegate extends DefaultPersistenceDelegate {

		@Override
		protected void initialize(Class<?> type, Object oldInstance, Object newInstance, Encoder out) {
			
		}

		@Override
		protected Expression instantiate(Object oldInstance, Encoder out) {
			GJGraph graph = (GJGraph)oldInstance;
			return new Expression( graph, graph.getClass(), "new", new Object[] { graph.getGraphT(), graph.getGModel().getVertexPositions() } );
		}
		
	}
	
	public AlgorithmResultInfo getDataCopy( AlgorithmResultInfo info ) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		writeAlgorithmResultInfo( info, out );
		return readAlgorithmResultInfo( new ByteArrayInputStream( out.toByteArray() ) );
	}
	
	public AlgorithmResultInfo readAlgorithmResultInfo( InputStream in ) {
		XMLCoderEL el = new XMLCoderEL();
		XMLDecoder dec = new XMLDecoder( in, null, el, MainPad.getInstance().getJarLoader() );
		AlgorithmResultInfo resultInfo = (AlgorithmResultInfo)dec.readObject();
		
		if ( el.hasExceptions() ) {
			for ( Exception e : el.getExceptions() ) {
				if ( !(e instanceof UserException) )
					e = new UserException( "unable to read algorithmResultInfo: ", e );
				MainPad.getInstance().handleUserException( (UserException)e );
			}
		}

		return resultInfo;
	}
	
	public void writeAlgorithmResultInfo( AlgorithmResultInfo info, OutputStream out ) {
		ClassLoader saveCL = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader( MainPad.getInstance().getJarLoader() );

		XMLCoderEL el = new XMLCoderEL();
		XMLEncoder enc = new XMLEncoder( out );
		
		enc.setExceptionListener( el );
		enc.setPersistenceDelegate( info.getClass(), new AlgorithmResultInfoPersistenceDelegate() );
		enc.setPersistenceDelegate( GJGraph.class, GJGRAPH_PD );
		
		enc.writeObject( info );
		enc.close();
		
		if ( el.hasExceptions() ) {
			for ( Exception e : el.getExceptions() ) {
				if ( !(e instanceof UserException) )
					e = new UserException( "unable to write algorithmresultinfo: ", e );
				MainPad.getInstance().handleUserException( (UserException)e );
			}
		}

		Thread.currentThread().setContextClassLoader( saveCL );
	}
	
	
	public AlgorithmResultDocumentContent readAlgorithmResultDocumentContent(InputStream in) throws InputOutputException {
		return new AlgorithmResultDocumentContent( readAlgorithmResultInfo( in ) );
	}

	public void writeAlgorithmResultDocumentContent(AlgorithmResultDocumentContent content, OutputStream out) throws InputOutputException {
		writeAlgorithmResultInfo( content.getAlgorithmResultInfo(), out );
	}
	
	public static class AlgorithmResultInfoPersistenceDelegate extends DefaultPersistenceDelegate {
		@Override
		protected Expression instantiate(Object oldInstance, Encoder out) {
			AlgorithmResultInfo info = (AlgorithmResultInfo)oldInstance;
			return new Expression( oldInstance, AlgorithmResultInfo.class, "new", new Object[] { info.getAlgorithmName(), info.getAlgorithmSettings(), info.getAlgorithmResult(), info.getGJGraphs() } );
		}
	}
	
	private class XMLCoderEL implements ExceptionListener {

		private ArrayList<Exception> exceptions = new ArrayList<Exception>();
		
		public void exceptionThrown(Exception e) {
			exceptions.add( e );
		}
		
		public boolean hasExceptions() {
			return !exceptions.isEmpty();
		}
		
		public ArrayList<Exception> getExceptions() {
			return exceptions;
		}
	}
}
