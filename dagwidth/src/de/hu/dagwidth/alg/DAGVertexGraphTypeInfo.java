/*
 * Created on 10 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.dagwidth.alg;

import java.util.HashMap;

import org.jgrapht.graph.DefaultEdge;

import de.hu.dagwidth.alg.DAGConstruction.DAGVertex;
import de.hu.gralog.graph.DirectedGraph;
import de.hu.gralog.graph.GraphTypeInfo;
import de.hu.gralog.graph.GraphWithEditableElements;

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
	
	@Override
	public GraphWithEditableElements copyGraph( GraphWithEditableElements grapht, HashMap vMap ) {
		DirectedGraph<DAGVertex, DefaultEdge> dagVertexGraph = (DirectedGraph<DAGVertex, DefaultEdge>) grapht;			// original GraphT
		DirectedGraph<DAGVertex, DefaultEdge> newDagVertexGraph = new DirectedGraph(DAGVertex.class, DefaultEdge.class);			// cloned GraphT
		newDagVertexGraph.setTypeInfo( this );

		// Clone the vertices and add them to the cloned GraphT "newDagVertexGraph":
		for( DAGVertex v : dagVertexGraph.vertexSet() ) {
			DAGVertex v2 = new DAGVertex( v.getV() );
			v2.setLabel(v.getLabel());
			vMap.put( v, v2 ); 	// "vMap" stores for each vertex "v" in dagVertexGraph the corresponding vertex "v2" in newDagVertexGraph
			newDagVertexGraph.addVertex( v2 );
		}

		// Clone the edges and add them to the cloned GraphT:		
		for( DefaultEdge e : dagVertexGraph.edgeSet() ) {
			DefaultEdge e2 = new DefaultEdge();
			
			newDagVertexGraph.addEdge(
				(DAGVertex) vMap.get(dagVertexGraph.getEdgeSource(e)),
				(DAGVertex) vMap.get(dagVertexGraph.getEdgeTarget(e)),
				e2
			);
		}

		return newDagVertexGraph; 
	}
	
}