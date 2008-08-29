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
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.Statement;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.jgrapht.graph.DirectedSubgraph;
import org.jgrapht.graph.Subgraph;

import de.hu.gralog.algorithm.result.AlgorithmResult;
import de.hu.gralog.algorithm.result.AlgorithmResultContent;
import de.hu.gralog.algorithm.result.AlgorithmResultContentTreeNode;
import de.hu.gralog.algorithm.result.AlgorithmResultInfo;
import de.hu.gralog.algorithm.result.DisplaySubgraphMode;
import de.hu.gralog.algorithm.result.ElementTipsDisplayMode;
import de.hu.gralog.algorithm.result.DisplaySubgraph.DisplayMode;
import de.hu.gralog.app.InputOutputException;
import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.GralogGraph;
import de.hu.gralog.graph.SimpleDirectedGralogGraph;
import de.hu.gralog.graph.GralogGraphBeanInfo.GralogGraphPersistenceDelegate;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.gui.document.AlgorithmResultDocumentContent;
import de.hu.gralog.gui.document.GJGraphDocumentContent;
import de.hu.gralog.jgraph.GJGraph;

public class XMLDecoderIOFast implements GJGraphDocumentContentIO, AlgorithmResultDocumentContentIO {

	private static final GJGraphPersistenceDelegate GJGRAPH_PD =  new GJGraphPersistenceDelegate();
	private static final SubgraphPersistenceDelegate SUBGRAPH_PD =  new SubgraphPersistenceDelegate();

	public XMLDecoderIOFast() {
		
	}

	public void writeGraph( GJGraph graph, OutputStream out ) {
		ClassLoader saveCL = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader( MainPad.getInstance().getJarLoader() );
		XMLCoderEL el = new XMLCoderEL();
		XMLEncoder enc = new XMLEncoder( out );
		enc.setExceptionListener( el );
		
		enc.setPersistenceDelegate( graph.getClass(), GJGRAPH_PD );
		
		//enc.setPersistenceDelegate( graph.getGraphT().getClass(), new GralogGraphPersistenceDelegate() );
		enc.writeObject( graph );
		enc.close();
		
		if ( el.hasExceptions() )
			el.getExceptions().get( 0 ).printStackTrace();
		
		Thread.currentThread().setContextClassLoader( saveCL );
	}
	
	public GJGraph readGraph( InputStream in ) throws Exception {
		XMLCoderEL el = new XMLCoderEL();
		XMLDecoder dec = new XMLDecoder( in, null, el, MainPad.getInstance().getJarLoader() );
		
		GJGraph graph = (GJGraph)dec.readObject();
		if ( el.hasExceptions() )
			throw el.getExceptions().get( 0 );
		return graph;
	}
	
	public void writeGJGraphDocumentContent(GJGraphDocumentContent content,
			OutputStream out) throws InputOutputException {
		writeGraph( content.getGraph(), out );
	}

	public GJGraphDocumentContent readGJGraphDocumentContent(InputStream in)
			throws InputOutputException {
		GJGraph graph = null;
		try {
			graph = readGraph( in );
		} catch( Throwable t ) {
			throw new InputOutputException( "unable to read graph", t  );
		}
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
		XMLDecoder dec = new XMLDecoder( in, null, null, MainPad.getInstance().getJarLoader() );
		return (AlgorithmResultInfo)dec.readObject();
	}
	
	public void writeAlgorithmResultInfo( AlgorithmResultInfo info, OutputStream out ) {
		ClassLoader saveCL = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader( MainPad.getInstance().getJarLoader() );

		XMLEncoder enc = new XMLEncoder( out );
		
		enc.setPersistenceDelegate( info.getClass(), new AlgorithmResultInfoPersistenceDelegate() );
		enc.setPersistenceDelegate( info.getAlgorithmResult().getClass(), new AlgorithmResultPersistenceDelegate() );
		
		enc.setPersistenceDelegate( DisplaySubgraphMode.class, new DisplaySubgraphModePersistenceDelegate() );
		enc.setPersistenceDelegate( DisplayMode.class, new DisplayModePersistenceDelegate() );
		enc.setPersistenceDelegate( ElementTipsDisplayMode.class, new ElementTipsDisplayModePersistenceDelegate() );
		enc.setPersistenceDelegate( AlgorithmResultContent.class, new AlgorithmResultContentPersistenceDelegate() );
		enc.setPersistenceDelegate( AlgorithmResultContentTreeNode.class, new AlgorithmResultContentTreeNodePersistenceDelegate() );
		enc.setPersistenceDelegate( Subgraph.class, SUBGRAPH_PD );
		enc.setPersistenceDelegate( DirectedSubgraph.class, SUBGRAPH_PD );
		enc.setPersistenceDelegate( GJGraph.class, GJGRAPH_PD );
		
		enc.writeObject( info );
		enc.close();
		
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
	
	public static class AlgorithmResultPersistenceDelegate extends DefaultPersistenceDelegate {

		@Override
		protected void initialize(Class<?> type, Object oldInstance, Object newInstance, Encoder out) {
			AlgorithmResult result = (AlgorithmResult)oldInstance;
			
			if ( result.getDescription() != null )
				out.writeStatement( new Statement( oldInstance, "setDescription", new Object[] { result.getDescription() } ) );
			if ( result.getDisplaySubgraphModes() != null ) {
				for ( Map.Entry<String, DisplaySubgraphMode> entry : result.getDisplaySubgraphModes().entrySet() )
					out.writeStatement( new Statement( oldInstance, "addDisplaySubgraphMode", new Object[] { entry.getKey(), entry.getValue() } ) );
			}
			
			if ( result.getElementTipsDisplayModes() != null ) {
				for ( Map.Entry<String, ElementTipsDisplayMode> entry : result.getElementTipsDisplayModes().entrySet() )
					out.writeStatement( new Statement( oldInstance, "addElementTipsDisplayMode", new Object[] { entry.getKey(), entry.getValue() } ) );
			}
			
			if ( result.getSingleContent() != null )
				out.writeStatement( new Statement( oldInstance, "setSingleContent", new Object[] { result.getSingleContent() } ) );
			if ( result.getContentList() != null )
				out.writeStatement( new Statement( oldInstance, "setContentList", new Object[] { result.getContentList() } ) );
			if ( result.getContentTree() != null )
				out.writeStatement( new Statement( oldInstance, "setContentTree", new Object[] { result.getContentTree() } ) ); 
		}

		@Override
		protected Expression instantiate(Object oldInstance, Encoder out) {
			AlgorithmResult result = (AlgorithmResult)oldInstance;
			
			if ( result.getGraph() != null )
				return new Expression( oldInstance, oldInstance.getClass(), "new" , new Object[] { result.getGraph() } );
			return new Expression( oldInstance, oldInstance.getClass(), "new", null );
		}
		
	}
	
	public static class DisplaySubgraphModePersistenceDelegate extends DefaultPersistenceDelegate {

		@Override
		protected void initialize(Class<?> type, Object oldInstance, Object newInstance, Encoder out) {
			DisplaySubgraphMode mode = (DisplaySubgraphMode)oldInstance;
			
			out.writeStatement( new Statement( oldInstance, "setVertexDisplayMode", new Object[] { mode.getVertexDisplayMode( false ), mode.getVertexDisplayMode( true ) } ) );
			out.writeStatement( new Statement( oldInstance, "setEdgeDisplayMode", new Object[] { mode.getEdgeDisplayMode( false ), mode.getEdgeDisplayMode( true ) } ) );
			out.writeStatement( new Statement( oldInstance, "setVisible", new Object[] { mode.isVisible() } ) );
		}

		@Override
		protected Expression instantiate(Object oldInstance, Encoder out) {
			return super.instantiate(oldInstance, out);
		}
		
	}
	
	public static class DisplayModePersistenceDelegate extends DefaultPersistenceDelegate {

		@Override
		protected Expression instantiate(Object oldInstance, Encoder out) {
			return new Expression( oldInstance, oldInstance.getClass(), "parseString", new Object[] { oldInstance.toString() } );
		}
		
	}
	
	public static class ElementTipsDisplayModePersistenceDelegate extends DefaultPersistenceDelegate {

		@Override
		protected void initialize(Class<?> type, Object oldInstance, Object newInstance, Encoder out) {
			ElementTipsDisplayMode mode = (ElementTipsDisplayMode)oldInstance;
			
			out.writeStatement( new Statement( oldInstance, "setVisible", new Object[] { mode.isVisible() } ) );
		}

		@Override
		protected Expression instantiate(Object oldInstance, Encoder out) {
			
			return super.instantiate(oldInstance, out);
		}
		
	}
	
	public static class AlgorithmResultContentPersistenceDelegate extends DefaultPersistenceDelegate {

		@Override
		protected void initialize(Class<?> type, Object oldInstance, Object newInstance, Encoder out) {
			try {
				AlgorithmResultContent content = (AlgorithmResultContent)oldInstance;
			
				if ( content.getName() != null )
					out.writeStatement( new Statement( oldInstance, "setName", new Object[] { content.getName() } ) );

				if ( content.getGraph() != null )
					out.writeStatement( new Statement( oldInstance, "setGraph", new Object[] { content.getGraph() } ) );
			
				if ( content.getSubgraphs() != null ) {
					for ( Map.Entry<String, Subgraph> entry : content.getSubgraphs().entrySet() )
						out.writeStatement( new Statement( oldInstance, "addDisplaySubgraph", new Object[] { entry.getKey(), entry.getValue() } ) );
				}
			
				if ( content.getTips() != null ) {
					for ( Map.Entry<String, Hashtable> entry : content.getTips().entrySet() )
						out.writeStatement( new Statement( oldInstance, "addElementTips", new Object[] { entry.getKey(), entry.getValue() } ) );
				}
			} catch( UserException e ) {
				MainPad.getInstance().handleUserException( e );
			}
		}

		@Override
		protected Expression instantiate(Object oldInstance, Encoder out) {
			return super.instantiate(oldInstance, out);
		}
		
	}
	
	public static class AlgorithmResultContentTreeNodePersistenceDelegate extends AlgorithmResultContentPersistenceDelegate {

		@Override
		protected void initialize(Class<?> type, Object oldInstance, Object newInstance, Encoder out) {
			super.initialize(type, oldInstance, newInstance, out);

			AlgorithmResultContentTreeNode node = (AlgorithmResultContentTreeNode)oldInstance;
			try {
				for ( AlgorithmResultContentTreeNode child : node.getChildren() )
					out.writeStatement( new Statement( oldInstance, "addChild", new Object[] { child } ) );
			} catch (UserException e) {
				MainPad.getInstance().handleUserException( e );
			}
		}
	}

	
	public static class SubgraphPersistenceDelegate extends DefaultPersistenceDelegate {
		@Override
		protected Expression instantiate(Object oldInstance, Encoder out) {
			//Subgraph subgraph = (Subgraph)oldInstance;
			//Graph base = GraphUtils.getSubgraphBase( subgraph );
			//return new Expression( oldInstance, SubgraphFactory.class, "createSubgraph", new Object[] { base, new HashSet( subgraph.vertexSet() ), new HashSet( subgraph.edgeSet() ) } );
			return null;
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
