/*
 * Created on 7 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.graph;

import org.jgrapht.graph.DefaultEdge;

public class LabeledDirectedGraphTypeInfo extends GraphTypeInfo {

	public String getName() {
		return "LabeledDirectedGraph";
	}

	public GraphWithEditableElements newInstance() {
		DirectedGraph<LabeledGraphVertex, DefaultEdge> graph = new DirectedGraph<LabeledGraphVertex, DefaultEdge>( LabeledGraphVertex.class, DefaultEdge.class );
		graph.setTypeInfo( this );
		return graph;
	}

}
