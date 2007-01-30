/*
 * Created on 7 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.demo;

import org.jgrapht.graph.DefaultEdge;

import de.hu.gralog.graph.DirectedGraph;
import de.hu.gralog.graph.GraphTypeInfo;
import de.hu.gralog.graph.GraphWithEditableElements;

public class TestGraphTypeInfo extends GraphTypeInfo {

	public String getName() {
		return "TestGraph";
	}

	public GraphWithEditableElements newInstance() {
		TestGraph graph = new TestGraph();
		graph.setTypeInfo( this );
		return graph;
	}

}
