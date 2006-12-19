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

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.ExceptionListener;
import java.beans.Expression;
import java.beans.Statement;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedSubgraph;
import org.jgrapht.graph.Subgraph;

import de.hu.gralog.app.InputOutputException;
import de.hu.gralog.graph.GraphTypeInfo;
import de.hu.gralog.graph.GraphWithEditableElements;
import de.hu.gralog.graph.alg.AlgorithmResult;
import de.hu.gralog.graph.alg.AlgorithmResultContent;
import de.hu.gralog.graph.alg.AlgorithmResultContentTreeNode;
import de.hu.gralog.graph.alg.AlgorithmResultInfo;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.gui.document.AlgorithmResultDocumentContent;
import de.hu.gralog.gui.document.GJGraphDocumentContent;
import de.hu.gralog.jgraph.GJGraph;
import de.hu.gralog.jgrapht.graph.GraphUtils;
import de.hu.gralog.jgrapht.graph.SubgraphFactory;
import de.hu.gralog.jgrapht.graph.DisplaySubgraph.DisplayMode;
import de.hu.gralog.jgrapht.graph.DisplaySubgraph.DisplaySubgraphMode;
import de.hu.gralog.jgrapht.graph.ElementTips.ElementTipsDisplayMode;

public class XMLDecoderIO implements GJGraphDocumentContentIO, AlgorithmResultDocumentContentIO, ExceptionListener {

	private static final GJGraphPersistenceDelegate GJGRAPH_PD =  new GJGraphPersistenceDelegate();
	private static final GraphWithEditableElementsPersistenceDelegate GRAPH_PD = new GraphWithEditableElementsPersistenceDelegate();
	private static final SubgraphPersistenceDelegate SUBGRAPH_PD =  new SubgraphPersistenceDelegate();

	public XMLDecoderIO() {
		
	}

	public GJGraph getDataCopy( GJGraph graph ) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		writeGraph( graph, out );
		return readGraph( new ByteArrayInputStream( out.toByteArray() ) );
	}
	
	protected void writeGraph( GJGraph graph, OutputStream out ) {
		XMLEncoder enc = new XMLEncoder( out );
		
		enc.setPersistenceDelegate( graph.getClass(), GJGRAPH_PD );
		enc.setPersistenceDelegate( graph.getGraphT().getClass(), GRAPH_PD );
		enc.writeObject( graph );
		enc.close();
	}
	
	protected GJGraph readGraph( InputStream in ) {
		XMLDecoder dec = new XMLDecoder( in, null, null, MainPad.getInstance().getClassLoader() );
		return (GJGraph)dec.readObject();
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
			GJGraph graph = (GJGraph)oldInstance;
			
			for ( Object cell : graph.getGModel().getVertexCells() ) {
				DefaultGraphCell vertexCell = (DefaultGraphCell)cell;
				Rectangle2D bounds = GraphConstants.getBounds( graph.getAttributes( vertexCell ) );
				out.writeStatement( new Statement( oldInstance, "positionVertexAt", new Object[] { vertexCell.getUserObject(), new Point( (int)bounds.getX(), (int)bounds.getY() ) } ) );
			}
		}

		@Override
		protected Expression instantiate(Object oldInstance, Encoder out) {
			return new Expression( oldInstance, oldInstance.getClass(), "new", new Object[] { ((GJGraph)oldInstance).getGraphT() } );
		}
		
	}
	
	public static class GraphWithEditableElementsPersistenceDelegate extends DefaultPersistenceDelegate {

		@Override
		protected void initialize(Class<?> type, Object oldInstance, Object newInstance, Encoder out) {
			
			GraphWithEditableElements graph = (GraphWithEditableElements)oldInstance;
			
			if ( graph.isGraphEditable() )
				super.initialize( type, oldInstance, newInstance, out );
			
			for ( Object vertex : graph.vertexSet() )
				out.writeStatement( new Statement( oldInstance, "addVertex", new Object[] { vertex } ) );
			for ( Object edge : graph.edgeSet() )
				out.writeStatement( new Statement( oldInstance, "addEdge", new Object[] { graph.getEdgeSource( edge ), graph.getEdgeTarget( edge ), edge } ) );
		}

		@Override
		protected Expression instantiate(Object oldInstance, Encoder out) {
			GraphTypeInfo typeInfo = ((GraphWithEditableElements) oldInstance).getTypeInfo();
			return new Expression( oldInstance, GraphFactory.class, "createGraph", new Object[] { typeInfo.getClass().getCanonicalName() } );
		}
		
	}
	
	public AlgorithmResultInfo getDataCopy( AlgorithmResultInfo info ) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		writeAlgorithmResultInfo( info, out );
		return readAlgorithmResultInfo( new ByteArrayInputStream( out.toByteArray() ) );
	}
	
	public AlgorithmResultInfo readAlgorithmResultInfo( InputStream in ) {
		XMLDecoder dec = new XMLDecoder( in, null, null, MainPad.getInstance().getClassLoader() );
		return (AlgorithmResultInfo)dec.readObject();
	}
	
	public void writeAlgorithmResultInfo( AlgorithmResultInfo info, OutputStream out ) {
		XMLEncoder enc = new XMLEncoder( out );
		enc.setExceptionListener( this );
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
		
		for ( Graph graph : info.getAllGraphs() )
			enc.setPersistenceDelegate( graph.getClass(), GRAPH_PD );
		enc.writeObject( info );
		enc.close();
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
			AlgorithmResultContent content = (AlgorithmResultContent)oldInstance;
			
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
			for ( AlgorithmResultContentTreeNode child : node.getChildren() )
				out.writeStatement( new Statement( oldInstance, "addChild", new Object[] { child } ) );
		}
	}

	
	public static class SubgraphPersistenceDelegate extends DefaultPersistenceDelegate {
		@Override
		protected Expression instantiate(Object oldInstance, Encoder out) {
			Subgraph subgraph = (Subgraph)oldInstance;
			Graph base = GraphUtils.getSubgraphBase( subgraph );
			return new Expression( oldInstance, SubgraphFactory.class, "createSubgraph", new Object[] { base, new HashSet( subgraph.vertexSet() ), new HashSet( subgraph.edgeSet() ) } );
		}
	}

	public void exceptionThrown(Exception e) {
		e.printStackTrace();
	}
}
