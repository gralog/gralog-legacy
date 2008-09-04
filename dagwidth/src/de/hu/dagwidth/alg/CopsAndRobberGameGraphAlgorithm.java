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

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;

import de.hu.dagwidth.alg.CopsAndRobberAlgorithm.CopsAndRobberVertex;
import de.hu.gralog.algorithm.Algorithm;
import de.hu.gralog.algorithm.InvalidPropertyValuesException;
import de.hu.gralog.algorithm.result.AlgorithmResult;
import de.hu.gralog.algorithm.result.AlgorithmResultContent;
import de.hu.gralog.app.UserException;
import de.hu.gralog.finitegames.graph.GameGraphTypeInfo;
import de.hu.gralog.finitegames.graph.GameGraphVertex;
import de.hu.gralog.graph.GralogGraphFactory;
import de.hu.gralog.graph.GralogGraphSupport;
import de.hu.gralog.graph.types.elements.LabeledGraphVertex;

public class CopsAndRobberGameGraphAlgorithm<V extends LabeledGraphVertex, E extends DefaultEdge> implements Algorithm {

	private GralogGraphSupport<V,E,?, ListenableDirectedGraph<V,E>> graph;
	private int dagWidth;
	private boolean robberMonotone;
	private boolean copMonotone;

	public GralogGraphSupport<V,E,?, ListenableDirectedGraph<V,E>> getGraph() {
		return graph;
	}

	public void setGraph(GralogGraphSupport<V,E,?, ListenableDirectedGraph<V,E>> graph) {
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

	public AlgorithmResult execute(  ) throws InvalidPropertyValuesException, UserException {
		InvalidPropertyValuesException e = new InvalidPropertyValuesException();
		if ( getGraph() == null )
			e.addPropertyError( "graph", InvalidPropertyValuesException.PROPERTY_REQUIRED );
		if ( getDagWidth() < 0 )
			e.addPropertyError( "dagWidth",  InvalidPropertyValuesException.GREATER_EQUAL_ZERO );
		if ( e.hasErrors() )
			throw e;
		
		AlgorithmResult result = new AlgorithmResult( getGameGraphFromCAR( (DirectedGraph<CopsAndRobberVertex, DefaultEdge>)CopsAndRobberAlgorithm.getCopsAndRobberGameGraph( getGraph().getGraph(), getDagWidth( ), isCopMonotone(), isRobberMonotone() ) ) );
		result.setSingleContent( new AlgorithmResultContent() );
		return result;
	}
	
	public GralogGraphSupport<GameGraphVertex, DefaultEdge,?, ListenableDirectedGraph<GameGraphVertex, DefaultEdge>> getGameGraphFromCAR( DirectedGraph<CopsAndRobberVertex, DefaultEdge> cargraph ) throws UserException {
		GralogGraphSupport<GameGraphVertex, DefaultEdge,?, ListenableDirectedGraph<GameGraphVertex, DefaultEdge>> graph = (GralogGraphSupport<GameGraphVertex, DefaultEdge,?, ListenableDirectedGraph<GameGraphVertex, DefaultEdge>>)GralogGraphFactory.createGraphSupport( new GameGraphTypeInfo() );
		Hashtable<CopsAndRobberVertex, GameGraphVertex> vertexes = new Hashtable<CopsAndRobberVertex, GameGraphVertex>();
		for ( CopsAndRobberVertex vertex : cargraph.vertexSet() ) {
			GameGraphVertex v = new GameGraphVertex( vertex.getLabel(), vertex.isPlayer0() );
			vertexes.put( vertex, v );
			graph.getGraph().addVertex( v );
		}
		for ( DefaultEdge edge : cargraph.edgeSet() ) {
			GameGraphVertex from = vertexes.get( cargraph.getEdgeSource( edge ) );
			GameGraphVertex to = vertexes.get( cargraph.getEdgeTarget( edge ) );
			graph.getGraph().addEdge( from, to );
		}
		return graph;
	}
}
