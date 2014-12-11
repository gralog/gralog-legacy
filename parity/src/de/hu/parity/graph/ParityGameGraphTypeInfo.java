/*
 * Created on 7 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.parity.graph;

import org.jgrapht.VertexFactory;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.jgrapht.graph.SimpleDirectedGraph;

import de.hu.gralog.finitegames.graph.GameGraphVertexRenderer;
import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;
import de.hu.gralog.structure.*;

public class ParityGameGraphTypeInfo<GB> extends StructureTypeInfo<ParityGameVertex, DefaultEdge, GB, ListenableDirectedGraph<ParityGameVertex,DefaultEdge>> {

	@Override
	public String getName() {
		return "ParityGameGraph";
	}

	@Override
	public GraphFactory<ListenableDirectedGraph<ParityGameVertex, DefaultEdge>> getGraphFactory() {
		return new GraphFactory<ListenableDirectedGraph<ParityGameVertex,DefaultEdge>>() {
			public ListenableDirectedGraph<ParityGameVertex, DefaultEdge> createGraph() {
				return new ListenableDirectedGraph<ParityGameVertex, DefaultEdge>( new SimpleDirectedGraph<ParityGameVertex, DefaultEdge>( DefaultEdge.class ) );
			}
		};
	}

	@Override
	public StructureBeanFactory<GB> getStructureBeanFactory() {
		return null;
	}

	@Override
	public VertexFactory<ParityGameVertex> getVertexFactory() {
		return new VertexFactory<ParityGameVertex>() {
			public ParityGameVertex createVertex() {
				return new ParityGameVertex();
			}
		};
	}

	@Override
	public DefaultVertexRenderer getVertexRenderer() {
		return new GameGraphVertexRenderer();
	}

	@Override
	public DefaultEdgeRenderer getEdgeRenderer() {
		return null;
	}


	@Override
	public String getDescription() {
		return "Parity Game";
	}
	
}
