/*
 * Created on 9 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.games.graph.alg.types;

import org.jgrapht.graph.DefaultEdge;

import de.hu.games.app.UserException;
import de.hu.games.graph.GraphWithEditableElements;
import de.hu.games.graph.LabeledDirectedGraphTypeInfo;
import de.hu.games.graph.LabeledGraphVertex;
import de.hu.games.graph.LabeledUndirectedGraphTypeInfo;
import de.hu.games.graph.alg.Algorithm;
import de.hu.games.graph.alg.AlgorithmResult;
import de.hu.games.graph.alg.AlgorithmResultContent;
import de.hu.games.graph.alg.InvalidPropertyValuesException;

public class LabeledGraphGenerator extends Algorithm {

	private boolean directed = true;
	private int vertexCount = 10;
	private double edgePropability = 0.5;
	
	public boolean isDirected() {
		return directed;
	}

	public void setDirected(boolean directed) {
		this.directed = directed;
	}

	public double getEdgePropability() {
		return edgePropability;
	}

	public void setEdgePropability(double edgePropability) {
		this.edgePropability = edgePropability;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void setVertexCount(int vertexCount) {
		this.vertexCount = vertexCount;
	}

	@Override
	public AlgorithmResult execute() throws InvalidPropertyValuesException,
			UserException {
		InvalidPropertyValuesException e = new InvalidPropertyValuesException();
		if ( getEdgePropability() < 0 || getEdgePropability() > 1 )
			e.addPropertyError( "edgePropability", "The edgepropabilty should be between 0 and 1" );
		if ( getVertexCount() <= 0 )
			e.addPropertyError( "vertexCount", InvalidPropertyValuesException.GREATER_ZERO );
		if ( e.hasErrors() )
			throw e;
		
		GraphWithEditableElements<LabeledGraphVertex, DefaultEdge> graph = null;
		if ( isDirected() )
			graph = new LabeledDirectedGraphTypeInfo().newInstance();
		else
			graph = new LabeledUndirectedGraphTypeInfo().newInstance();
		
		for ( int i = 0; i < getVertexCount(); i++ ) {
			LabeledGraphVertex v = (LabeledGraphVertex)graph.createVertex();
			v.setLabel( Integer.toString( i ) );
			graph.addVertex( v );
		}
		
		double edgePropability = getEdgePropability();
		if ( isDirected() )
			edgePropability = edgePropability / 2.0;
		for ( LabeledGraphVertex v1 : graph.vertexSet() ) {
			for ( LabeledGraphVertex v2 : graph.vertexSet() ) {
				if ( v1 != v2 && (!graph.containsEdge( v1, v2)) && Math.random() <= edgePropability )
					graph.addEdge( v1, v2 );
			}
		}
		
		AlgorithmResult result = new AlgorithmResult();
		result.setSingleContent( new AlgorithmResultContent( graph ) );
		
		return result;
	}

}
