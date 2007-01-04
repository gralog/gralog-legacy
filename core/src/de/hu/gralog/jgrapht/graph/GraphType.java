/*
 * Created on 3 Jan 2007
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.jgrapht.graph;

import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.graph.Multigraph;
import org.jgrapht.graph.Pseudograph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleGraph;

public enum GraphType {
	SIMPLE_GRAPH, MULTI_GRAPH, PSEUDO_GRAPH;
	
	public Graph newInstance(  boolean directed, Class edgeClass ) {
		if ( directed ) {
			switch( this ) {
			case SIMPLE_GRAPH: 
				return new SimpleDirectedGraph( edgeClass );
			case MULTI_GRAPH: 
				return new DirectedMultigraph( edgeClass );
			case PSEUDO_GRAPH: 
				return new DirectedPseudograph( edgeClass );
			}
		} else {
			switch( this ) {
			case SIMPLE_GRAPH: 
				return new SimpleGraph( edgeClass );
			case MULTI_GRAPH: 
				return new Multigraph( edgeClass );
			case PSEUDO_GRAPH: 
				return new Pseudograph( edgeClass );
			}
		}
		return null;
	}
}
