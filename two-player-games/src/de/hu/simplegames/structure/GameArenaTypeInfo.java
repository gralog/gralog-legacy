package de.hu.simplegames.structure;

import org.jgrapht.EdgeFactory;
import org.jgrapht.VertexFactory;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.jgrapht.graph.SimpleDirectedGraph;

import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;
import de.hu.gralog.structure.GraphFactory;
import de.hu.gralog.structure.StructureBeanFactory;
import de.hu.gralog.structure.StructureTypeInfo;

public class GameArenaTypeInfo<GB> 
	extends StructureTypeInfo<GameArenaVertex,DefaultEdge,GB,ListenableDirectedGraph<GameArenaVertex, DefaultEdge>> {

	public String getName() {
		return "GameArena";
	}
	
	public String getDescription() {
		return 
			"<html>" +
			"This structure represents an arena " +
			"for simple two-player-games." +
			"</html>";
	}

	public GraphFactory<ListenableDirectedGraph<GameArenaVertex, DefaultEdge>> getGraphFactory() {
		return new GraphFactory<ListenableDirectedGraph<GameArenaVertex,DefaultEdge>>() {
			public ListenableDirectedGraph<GameArenaVertex, DefaultEdge> createGraph() {
				return new ListenableDirectedGraph<GameArenaVertex, DefaultEdge>( new SimpleDirectedGraph<GameArenaVertex,DefaultEdge>( DefaultEdge.class ) );
			}
		};
	}

	public StructureBeanFactory<GB> getStructureBeanFactory() {
		return null;
	}

	public VertexFactory<GameArenaVertex> getVertexFactory() {
		return new VertexFactory<GameArenaVertex>() {
			public GameArenaVertex createVertex() {
				return new GameArenaVertex();
			}
		};
	}

	public EdgeFactory<GameArenaVertex,DefaultEdge> getEdgeFactory() {
		return null;
	}

	public DefaultVertexRenderer getVertexRenderer() {
		return new GameArenaVertexRenderer();
	}

	public DefaultEdgeRenderer getEdgeRenderer() {
		return new DefaultEdgeRenderer();
	}

}
