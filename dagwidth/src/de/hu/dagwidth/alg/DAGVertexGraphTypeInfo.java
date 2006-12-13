/*
 * Created on 10 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.dagwidth.alg;

import org.jgrapht.graph.DefaultEdge;

import de.hu.dagwidth.alg.DAGConstruction.DAGVertex;
import de.hu.games.graph.DirectedGraph;
import de.hu.games.graph.GraphTypeInfo;
import de.hu.games.graph.GraphWithEditableElements;

public class DAGVertexGraphTypeInfo extends GraphTypeInfo {

	@Override
	public String getName() {
		return "DAGGraph";
	}

	@Override
	public GraphWithEditableElements newInstance() {
		DirectedGraph<DAGVertex, DefaultEdge> graph = new DirectedGraph<DAGVertex, DefaultEdge>( DAGVertex.class );
		graph.setTypeInfo( this );
		return graph;
	}
	
}