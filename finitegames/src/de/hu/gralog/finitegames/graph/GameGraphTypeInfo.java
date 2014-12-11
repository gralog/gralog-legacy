package de.hu.gralog.finitegames.graph;

import org.jgrapht.EdgeFactory;
import org.jgrapht.VertexFactory;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.jgrapht.graph.SimpleDirectedGraph;

import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;
import de.hu.gralog.structure.*;


public class GameGraphTypeInfo<GB> 
	extends StructureTypeInfo<GameGraphVertex,DefaultEdge,GB,ListenableDirectedGraph<GameGraphVertex, DefaultEdge>> {

	public String getName() {
		return "GameGraph";
	}

	public GraphFactory<ListenableDirectedGraph<GameGraphVertex, DefaultEdge>> getGraphFactory() {
		return new GraphFactory<ListenableDirectedGraph<GameGraphVertex,DefaultEdge>>() {
			public ListenableDirectedGraph<GameGraphVertex, DefaultEdge> createGraph() {
				return new ListenableDirectedGraph<GameGraphVertex, DefaultEdge>( new SimpleDirectedGraph<GameGraphVertex,DefaultEdge>( DefaultEdge.class ) );
			}
		};
	}

	public String getDescription() {
		return "Game Graph";
    }
	
	public StructureBeanFactory<GB> getStructureBeanFactory() {
		return null;
	}

	public VertexFactory<GameGraphVertex> getVertexFactory() {
		return new VertexFactory<GameGraphVertex>() {
			public GameGraphVertex createVertex() {
				return new GameGraphVertex();
			}
		};
	}

	public EdgeFactory<GameGraphVertex,DefaultEdge> getEdgeFactory() {
		return null;
	}

	public DefaultVertexRenderer getVertexRenderer() {
		return new GameGraphVertexRenderer();
	}

	public DefaultEdgeRenderer getEdgeRenderer() {
		return new DefaultEdgeRenderer();
	}

}
