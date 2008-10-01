/*
 * Created on 7 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.parity.structure;

import org.jgrapht.VertexFactory;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.jgrapht.graph.SimpleDirectedGraph;

import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;
import de.hu.gralog.structure.GraphFactory;
import de.hu.gralog.structure.StructureBeanFactory;
import de.hu.gralog.structure.StructureTypeInfo;
import de.hu.simplegames.structure.GameArenaVertexRenderer;

public class ParityGameArenaTypeInfo<GB> extends StructureTypeInfo<ParityGameVertex, DefaultEdge, GB, ListenableDirectedGraph<ParityGameVertex,DefaultEdge>> {

	@Override
	public String getName() {
		return "ParityGameArena";
	}
	
	public String getDescription() {
		return 
			"<html>" +
			"This structure represents a " +
			"so called Parity-Game-Arena. " +
			"</html>";
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
		return new GameArenaVertexRenderer();
	}

	@Override
	public DefaultEdgeRenderer getEdgeRenderer() {
		return null;
	}


	
}
