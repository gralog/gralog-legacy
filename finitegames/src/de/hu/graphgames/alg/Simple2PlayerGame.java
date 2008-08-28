/*
 * Created on 2006 by Sebastian Ordyniak
 *
 * Copyright 2006 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of Gralog.
 *
 * Gralog is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * Gralog is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Games; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */

package de.hu.graphgames.alg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Subgraph;

import de.hu.gralog.graph.DirectedGraph;
import de.hu.gralog.graph.alg.Algorithm;
import de.hu.gralog.graph.alg.AlgorithmResult;
import de.hu.gralog.graph.alg.AlgorithmResultContent;
import de.hu.gralog.graph.alg.InvalidPropertyValuesException;
import de.hu.gralog.jgrapht.graph.DisplaySubgraphMode;
import de.hu.gralog.jgrapht.graph.SubgraphFactory;
import de.hu.gralog.jgrapht.graph.DisplaySubgraph.DisplayMode;
import de.hu.graphgames.graph.GameGraphVertex;

public class Simple2PlayerGame<V extends GameGraphVertex,E extends DefaultEdge> implements Algorithm {

	private static final String DSM_WINNING_PLAYER1 = "winning positions player 0";
	
	private DirectedGraph<V, E> gameGraph;
	private boolean showSteps;
	
	public boolean isShowSteps() {
		return showSteps;
	}
	
	public void setShowSteps( boolean showSteps ) {
		this.showSteps = showSteps;
	}

	public DirectedGraph<V,E> getGameGraph() {
		return gameGraph;
	}

	public void setGameGraph(DirectedGraph<V, E> graph) {
		this.gameGraph = graph;
	}

	public AlgorithmResult execute(  ) throws InvalidPropertyValuesException {
		InvalidPropertyValuesException pe = new InvalidPropertyValuesException();
		
		if ( getGameGraph() == null )
			pe.addPropertyError( "gameGraph",  InvalidPropertyValuesException.PROPERTY_REQUIRED );
		
		if ( pe.hasErrors() )
			throw pe;
		
		ArrayList<V> win0 = new Simple2PlayerGameAlgorithm<V,E>( gameGraph ).execute();

		AlgorithmResult result = new AlgorithmResult( gameGraph );
		result.setDescription( "description" );
		
		DisplaySubgraphMode displaySubgraphMode = new DisplaySubgraphMode( );
		displaySubgraphMode.setVertexDisplayMode( DisplayMode.HIGH2, DisplayMode.HIGH1 );

		result.addDisplaySubgraphMode( DSM_WINNING_PLAYER1, displaySubgraphMode );
		
		if ( showSteps && win0.size() != 0 ) {
			ArrayList<AlgorithmResultContent> contentList = new ArrayList<AlgorithmResultContent>();
			
			Set<V> win1Step = new HashSet<V>();
			for ( V vertex : win0 ) {
				win1Step.add( vertex );
				Subgraph winning1 = SubgraphFactory.createSubgraph( gameGraph, win1Step, new HashSet<E>() );

				AlgorithmResultContent content = new AlgorithmResultContent();
				content.addDisplaySubgraph( DSM_WINNING_PLAYER1, winning1 );
				contentList.add( content );
			}
			result.setContentList( contentList );
		} else {
			Subgraph winning0 = SubgraphFactory.createSubgraph( gameGraph, new HashSet<V>( win0 ), new HashSet<E>() );
			
			AlgorithmResultContent content = new AlgorithmResultContent();
			content.addDisplaySubgraph( DSM_WINNING_PLAYER1, winning0 );
			result.setSingleContent( content );
		}

		return result;
	}
}
