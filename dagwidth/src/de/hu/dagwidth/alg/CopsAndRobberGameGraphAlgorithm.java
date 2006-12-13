/*
 * Created on 2006 by Sebastian Ordyniak
 *
 * Copyright 2006 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of Games.
 *
 * Games is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * Games is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Games; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */

package de.hu.dagwidth.alg;

import java.util.Hashtable;

import org.jgrapht.graph.DefaultEdge;

import de.hu.dagwidth.alg.CopsAndRobberAlgorithm.CopsAndRobberVertex;
import de.hu.games.graph.DirectedGraph;
import de.hu.games.graph.LabeledGraphVertex;
import de.hu.games.graph.alg.Algorithm;
import de.hu.games.graph.alg.AlgorithmResult;
import de.hu.games.graph.alg.AlgorithmResultContent;
import de.hu.games.graph.alg.InvalidPropertyValuesException;
import de.hu.graphgames.graph.GameGraphTypeInfo;
import de.hu.graphgames.graph.GameGraphVertex;

public class CopsAndRobberGameGraphAlgorithm extends Algorithm {

	private DirectedGraph<? extends LabeledGraphVertex, ? extends DefaultEdge> graph;
	private int dagWidth;
	private boolean robberMonotone;
	private boolean copMonotone;

	public DirectedGraph<? extends LabeledGraphVertex, ? extends DefaultEdge> getGraph() {
		return graph;
	}

	public void setGraph(DirectedGraph<? extends LabeledGraphVertex, ? extends DefaultEdge> graph) {
		this.graph = graph;
	} 
	
	public boolean isCopMonotone() {
		return copMonotone;
	}

	public void setCopMonotone(boolean copMonotone) {
		this.copMonotone = copMonotone;
	}

	public boolean isRobberMonotone() {
		return robberMonotone;
	}

	public void setRobberMonotone(boolean robberMonotone) {
		this.robberMonotone = robberMonotone;
	}

	public int getDagWidth() {
		return dagWidth;
	}

	public void setDagWidth(int dagWidth) {
		this.dagWidth = dagWidth;
	}

	public AlgorithmResult execute(  ) throws InvalidPropertyValuesException {
		InvalidPropertyValuesException e = new InvalidPropertyValuesException();
		if ( getGraph() == null )
			e.addPropertyError( "graph", InvalidPropertyValuesException.PROPERTY_REQUIRED );
		if ( getDagWidth() < 0 )
			e.addPropertyError( "dagWidth",  InvalidPropertyValuesException.GREATER_EQUAL_ZERO );
		if ( e.hasErrors() )
			throw e;
		
		AlgorithmResult result = new AlgorithmResult( getGameGraphFromCAR( (DirectedGraph<CopsAndRobberVertex, DefaultEdge>)CopsAndRobberAlgorithm.getCopsAndRobberGameGraph( getGraph(), getDagWidth( ), isCopMonotone(), isRobberMonotone() ) ) );
		result.setSingleContent( new AlgorithmResultContent() );
		return result;
	}
	
	public DirectedGraph<GameGraphVertex, DefaultEdge> getGameGraphFromCAR( DirectedGraph<CopsAndRobberVertex, DefaultEdge> cargraph ) {
		DirectedGraph<GameGraphVertex, DefaultEdge> graph = (DirectedGraph<GameGraphVertex, DefaultEdge>)new GameGraphTypeInfo().newInstance();
		Hashtable<CopsAndRobberVertex, GameGraphVertex> vertexes = new Hashtable<CopsAndRobberVertex, GameGraphVertex>();
		for ( CopsAndRobberVertex vertex : cargraph.vertexSet() ) {
			GameGraphVertex v = new GameGraphVertex( vertex.getLabel(), vertex.isPlayer0() );
			vertexes.put( vertex, v );
			graph.addVertex( v );
		}
		for ( DefaultEdge edge : cargraph.edgeSet() ) {
			GameGraphVertex from = vertexes.get( cargraph.getEdgeSource( edge ) );
			GameGraphVertex to = vertexes.get( cargraph.getEdgeTarget( edge ) );
			graph.addEdge( from, to );
		}
		return graph;
	}
}
