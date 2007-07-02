/*
 * Created on 7 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.logic.graph;

import java.util.HashMap;

import de.hu.gralog.graph.GraphTypeInfo;
import de.hu.gralog.graph.GraphWithEditableElements;

public class TransitionSystemTypeInfo extends GraphTypeInfo {

	public String getName() {
		return "TransitionSystem";
	}

	public GraphWithEditableElements newInstance() {
		TransitionSystem system = new TransitionSystem();
		system.setTypeInfo( this );
		return system;
	}
	
	@Override
	public GraphWithEditableElements copyGraph( GraphWithEditableElements grapht, HashMap vMap ) {
		TransitionSystem transSys = (TransitionSystem) grapht;			// original GraphT
		TransitionSystem newTransSys = new TransitionSystem();			// cloned GraphT
		newTransSys.setTypeInfo( this );

		// Clone the vertices and add them to the cloned GraphT "newTransSys":
		for( TransitionSystemVertex v : transSys.vertexSet() ) {
			TransitionSystemVertex v2 = new TransitionSystemVertex( v.getLabel() );
			vMap.put( v, v2 ); 	// "vMap" stores for each vertex "v" in transSys the corresponding vertex "v2" in newTransSys
			newTransSys.addVertex( v2 );
		}
		
		// Set the propositions:
		Proposition[] props = transSys.getPropositions();
		for( int i = 0; i < props.length; i++ ) {
			Proposition newProp = new Proposition( props[i].getName() );
			for( TransitionSystemVertex v : props[i].getVertexes() ) {
				newProp.addVertex( (TransitionSystemVertex) vMap.get(v) );
			}
			newTransSys.addProposition(newProp);
		}
		
		// Clone the edges and add them to the cloned GraphT:		
		for( TransitionSystemEdge e : transSys.edgeSet() ) {
			TransitionSystemEdge e2 = new TransitionSystemEdge( e.getLabel() );
			
			newTransSys.addEdge(
				(TransitionSystemVertex) vMap.get(transSys.getEdgeSource(e)),
				(TransitionSystemVertex) vMap.get(transSys.getEdgeTarget(e)),
				e2
			);
		}

		return newTransSys; 
	}

}
