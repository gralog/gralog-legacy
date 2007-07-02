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
 * This class represents an UndirectedGraph with {@link LabeledGraphVertex} as vertexClass {@link DefaultEdge} as
 * edgeClass and a SIMPLE_GRAPH graph-flavour.
 *  
 * @author ordyniak
 *
 */
public class LabeledUndirectedGraphTypeInfo extends GraphTypeInfo {

	public String getName() {
		return "LabeledUndirectedGraph";
	}

	public GraphWithEditableElements newInstance() {
		UndirectedGraph<LabeledGraphVertex, DefaultEdge> graph = new UndirectedGraph<LabeledGraphVertex, DefaultEdge>( LabeledGraphVertex.class, DefaultEdge.class );
		graph.setTypeInfo( this );
		return graph;
	}
	
	@Override
	public GraphWithEditableElements copyGraph( GraphWithEditableElements grapht, HashMap vMap ) {
		UndirectedGraph<LabeledGraphVertex, DefaultEdge> labUndirGraph = (UndirectedGraph<LabeledGraphVertex, DefaultEdge>) grapht;			// original GraphT
		UndirectedGraph<LabeledGraphVertex, DefaultEdge> newLabUndirGraph = new UndirectedGraph(LabeledGraphVertex.class, DefaultEdge.class);			// cloned GraphT
		newLabUndirGraph.setTypeInfo( this );

		// Clone the vertices and add them to the cloned GraphT "newLabUndirGraph":
		for( LabeledGraphVertex v : labUndirGraph.vertexSet() ) {
			LabeledGraphVertex v2 = new LabeledGraphVertex( v.getLabel() );	
			vMap.put( v, v2 ); 	// "vMap" stores for each vertex "v" in labUndirGraph the corresponding vertex "v2" in newLabUndirGraph
			newLabUndirGraph.addVertex( v2 );
		}

		// Clone the edges and add them to the cloned GraphT:		
		for( DefaultEdge e : labUndirGraph.edgeSet() ) {
			DefaultEdge e2 = new DefaultEdge();
			
			newLabUndirGraph.addEdge(
				(LabeledGraphVertex) vMap.get(labUndirGraph.getEdgeSource(e)),
				(LabeledGraphVertex) vMap.get(labUndirGraph.getEdgeTarget(e)),
				e2
			);
		}

		return newLabUndirGraph; 
	}

}
