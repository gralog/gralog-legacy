/*
 * Created on 30 Jan 2007
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.demo;

import org.jgrapht.graph.DefaultEdge;

import de.hu.gralog.graph.DirectedGraph;
import de.hu.gralog.graph.LabeledGraphVertex;

public class TestGraph extends DirectedGraph<LabeledGraphVertex, DefaultEdge >{

	private LabeledGraphVertex startVertex = null;
	
	public TestGraph( ) {
		super( LabeledGraphVertex.class );
	}
	
	public LabeledGraphVertex getStartVertex() {
		return startVertex;
	}

	public void setStartVertex(LabeledGraphVertex startVertex) {
		this.startVertex = startVertex;
	}

	public boolean isGraphEditable() {
		return true;
	}
}
