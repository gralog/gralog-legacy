/*
 * Created on 7 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.demo;

import java.util.HashMap;

import org.jgrapht.graph.DefaultEdge;

import de.hu.gralog.graph.GraphTypeInfo;
import de.hu.gralog.graph.GraphWithEditableElements;
import de.hu.gralog.graph.LabeledGraphVertex;

public class TestGraphTypeInfo extends GraphTypeInfo {

	public String getName() {
		return "TestGraph";
	}

	public GraphWithEditableElements newInstance() {
		TestGraph graph = new TestGraph();
		graph.setTypeInfo( this );
		return graph;
	}
	
	@Override
	public GraphWithEditableElements copyGraph( GraphWithEditableElements grapht, HashMap vMap ) {
		TestGraph labDirGraph = (TestGraph) grapht;			// original GraphT
		TestGraph newLabDirGraph = new TestGraph();			// cloned GraphT
		newLabDirGraph.setTypeInfo( this );

		// Clone the vertices and add them to the cloned GraphT "newLabDirGraph":
		for( LabeledGraphVertex v : labDirGraph.vertexSet() ) {
			LabeledGraphVertex v2 = new LabeledGraphVertex( v.getLabel() );	
			vMap.put( v, v2 ); 	// "vMap" stores for each vertex "v" in labDirGraph the corresponding vertex "v2" in newLabDirGraph
			newLabDirGraph.addVertex( v2 );
		}

		// Clone the edges and add them to the cloned GraphT:		
		for( DefaultEdge e : labDirGraph.edgeSet() ) {
			DefaultEdge e2 = new DefaultEdge();
			
			newLabDirGraph.addEdge(
				(LabeledGraphVertex) vMap.get(labDirGraph.getEdgeSource(e)),
				(LabeledGraphVertex) vMap.get(labDirGraph.getEdgeTarget(e)),
				e2
			);
		}

		return newLabDirGraph; 
	}

}
