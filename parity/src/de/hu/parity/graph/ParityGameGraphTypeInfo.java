/*
 * Created on 7 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.parity.graph;

import java.util.HashMap;

import org.jgrapht.graph.DefaultEdge;

import de.hu.gralog.graph.DirectedGraph;
import de.hu.gralog.graph.GraphTypeInfo;
import de.hu.gralog.graph.GraphWithEditableElements;

public class ParityGameGraphTypeInfo extends GraphTypeInfo {

	public String getName() {
		return "ParityGameGraph";
	}

	public GraphWithEditableElements newInstance() {
		DirectedGraph<ParityGameVertex, DefaultEdge> graph = new DirectedGraph<ParityGameVertex, DefaultEdge>( ParityGameVertex.class, DefaultEdge.class );
		graph.setTypeInfo( this );
		return graph;
	}
	
	@Override
	public GraphWithEditableElements copyGraph( GraphWithEditableElements grapht, HashMap vMap ) {
		DirectedGraph<ParityGameVertex, DefaultEdge> parityGameGraph = (DirectedGraph<ParityGameVertex, DefaultEdge>) grapht;			// original GraphT
		DirectedGraph<ParityGameVertex, DefaultEdge> newParityGameGraph = new DirectedGraph(ParityGameVertex.class, DefaultEdge.class);			// cloned GraphT
		newParityGameGraph.setTypeInfo( this );

		// Clone the vertices and add them to the cloned GraphT "newAutGraph":
		for( ParityGameVertex v : parityGameGraph.vertexSet() ) {
			ParityGameVertex v2 = new ParityGameVertex( v.getLabel(), v.getPriority(), v.isPlayer0() );	
			vMap.put( v, v2 ); 	// "vMap" stores for each vertex "v" in autGraph the corresponding vertex "v2" in newAutGraph
			newParityGameGraph.addVertex( v2 );
		}

		// Clone the edges and add them to the cloned GraphT:		
		for( DefaultEdge e : parityGameGraph.edgeSet() ) {
			DefaultEdge e2 = new DefaultEdge();
			
			newParityGameGraph.addEdge(
				(ParityGameVertex) vMap.get(parityGameGraph.getEdgeSource(e)),
				(ParityGameVertex) vMap.get(parityGameGraph.getEdgeTarget(e)),
				e2
			);
		}

		return newParityGameGraph; 
	}

}
