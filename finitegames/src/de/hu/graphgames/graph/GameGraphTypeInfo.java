/*
 * Created on 7 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.graphgames.graph;

import org.jgrapht.graph.DefaultEdge;

import de.hu.games.graph.DirectedGraph;
import de.hu.games.graph.GraphTypeInfo;
import de.hu.games.graph.GraphWithEditableElements;

public class GameGraphTypeInfo extends GraphTypeInfo {

	public String getName() {
		return "GameGraph";
	}

	public GraphWithEditableElements newInstance() {
		DirectedGraph<GameGraphVertex, DefaultEdge> graph = new DirectedGraph<GameGraphVertex, DefaultEdge>( GameGraphVertex.class, DefaultEdge.class );
		graph.setTypeInfo( this );
		return graph;
	}

}
