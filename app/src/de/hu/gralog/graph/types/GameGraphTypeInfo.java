/*
 * Created on 7 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.graph.types;

import java.util.HashMap;

import org.jgrapht.graph.DefaultEdge;

import de.hu.gralog.graph.DirectedGraph;
import de.hu.gralog.graph.GraphTypeInfo;
import de.hu.gralog.graph.GraphWithEditableElements;

public class GameGraphTypeInfo extends GraphTypeInfo {

	public String getName() {
		return "GameGraph";
	}

	public GraphWithEditableElements newInstance() {
		DirectedGraph<GameGraphVertex, DefaultEdge> graph = new DirectedGraph<GameGraphVertex, DefaultEdge>( GameGraphVertex.class, DefaultEdge.class );
		graph.setTypeInfo( this );
		return graph;
	}
	
	@Override
	public GraphWithEditableElements copyGraph( GraphWithEditableElements grapht, HashMap vMap ) {
		DirectedGraph<GameGraphVertex, DefaultEdge> gameGraph = (DirectedGraph<GameGraphVertex, DefaultEdge>) grapht;			// original GraphT
		DirectedGraph<GameGraphVertex, DefaultEdge> newGameGraph = new DirectedGraph(GameGraphVertex.class, DefaultEdge.class);			// cloned GraphT
		newGameGraph.setTypeInfo( this );

		// Clone the vertices and add them to the cloned GraphT "newGameGraph":
		for( GameGraphVertex v : gameGraph.vertexSet() ) {
			GameGraphVertex v2 = new GameGraphVertex( v.getLabel(), v.isPlayer0() );	
			vMap.put( v, v2 ); 	// "vMap" stores for each vertex "v" in gameGraph the corresponding vertex "v2" in newGameGraph
			newGameGraph.addVertex( v2 );
		}

		// Clone the edges and add them to the cloned GraphT:		
		for( DefaultEdge e : gameGraph.edgeSet() ) {
			DefaultEdge e2 = new DefaultEdge();
			
			newGameGraph.addEdge(
				(GameGraphVertex) vMap.get(gameGraph.getEdgeSource(e)),
				(GameGraphVertex) vMap.get(gameGraph.getEdgeTarget(e)),
				e2
			);
		}

		return newGameGraph; 
	}

}
