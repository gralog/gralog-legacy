/*
 * Created on 7 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.graph;

import java.util.HashMap;

import org.jgrapht.graph.DefaultEdge;

/**
 * This class represents a DirectedGraph with {@link LabeledGraphVertex} as vertexClass {@link DefaultEdge} as
 * edgeClass and a SIMPLE_GRAPH graph-flavour.
 *  
 * @author ordyniak
 *
 */
public class LabeledDirectedGraphTypeInfo extends GraphTypeInfo {

	public String getName() {
		return "LabeledDirectedGraph";
	}

	public GraphWithEditableElements newInstance() {
		DirectedGraph<LabeledGraphVertex, DefaultEdge> graph = new DirectedGraph<LabeledGraphVertex, DefaultEdge>( LabeledGraphVertex.class, DefaultEdge.class );
		graph.setTypeInfo( this );
		return graph;
	}
	
	@Override
	public GraphWithEditableElements copyGraph( GraphWithEditableElements grapht, HashMap vMap ) {
		DirectedGraph<LabeledGraphVertex, DefaultEdge> labDirGraph = (DirectedGraph<LabeledGraphVertex, DefaultEdge>) grapht;			// original GraphT
		DirectedGraph<LabeledGraphVertex, DefaultEdge> newLabDirGraph = new DirectedGraph(LabeledGraphVertex.class, DefaultEdge.class);			// cloned GraphT
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
