/*
 * Created on 7 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.games.graph.types;

import org.jgrapht.graph.DefaultEdge;

import de.hu.games.graph.DirectedGraph;
import de.hu.games.graph.GraphTypeInfo;
import de.hu.games.graph.GraphWithEditableElements;

public class ParityGameGraphTypeInfo extends GraphTypeInfo {

	public String getName() {
		return "ParityGameGraph";
	}

	public GraphWithEditableElements newInstance() {
		DirectedGraph<ParityGameVertex, DefaultEdge> graph = new DirectedGraph<ParityGameVertex, DefaultEdge>( ParityGameVertex.class, DefaultEdge.class );
		graph.setTypeInfo( this );
		return graph;
	}

}
