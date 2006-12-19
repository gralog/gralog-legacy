/*
 * Created on 7 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.games.graph;

import org.jgrapht.graph.DefaultEdge;

public class LabeledUndirectedGraphTypeInfo extends GraphTypeInfo {

	public String getName() {
		return "LabeledUndirectedGraph";
	}

	public GraphWithEditableElements newInstance() {
		UndirectedGraph<LabeledGraphVertex, DefaultEdge> graph = new UndirectedGraph<LabeledGraphVertex, DefaultEdge>( LabeledGraphVertex.class, DefaultEdge.class );
		graph.setTypeInfo( this );
		return graph;
	}

}
