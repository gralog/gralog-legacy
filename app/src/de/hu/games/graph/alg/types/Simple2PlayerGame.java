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

package de.hu.games.graph.alg.types;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Subgraph;

import de.hu.games.graph.DirectedGraph;
import de.hu.games.graph.alg.Algorithm;
import de.hu.games.graph.alg.AlgorithmResult;
import de.hu.games.graph.alg.AlgorithmResultContent;
import de.hu.games.graph.alg.InvalidPropertyValuesException;
import de.hu.games.graph.types.GameGraphVertex;
import de.hu.games.jgrapht.graph.SubgraphFactory;
import de.hu.games.jgrapht.graph.DisplaySubgraph.DisplayMode;
import de.hu.games.jgrapht.graph.DisplaySubgraph.DisplaySubgraphMode;

public class Simple2PlayerGame<V extends GameGraphVertex,E extends DefaultEdge> extends Algorithm {

	private static final String DSM_WINNING_PLAYER1 = "winning positions player 1";
	
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
		
		ArrayList<V> win1 = new Simple2PlayerGameAlgorithm<V,E>( gameGraph ).execute();

		AlgorithmResult result = new AlgorithmResult( gameGraph );
		result.setDescription( "description" );
		
		DisplaySubgraphMode displaySubgraphMode = new DisplaySubgraphMode( );
		displaySubgraphMode.setVertexDisplayMode( DisplayMode.HIGH2, DisplayMode.HIGH1 );

		result.addDisplaySubgraphMode( DSM_WINNING_PLAYER1, displaySubgraphMode );
		
		if ( showSteps ) {
			ArrayList<AlgorithmResultContent> contentList = new ArrayList<AlgorithmResultContent>();
			
			Set<V> win1Step = new HashSet<V>();
			for ( V vertex : win1 ) {
				win1Step.add( vertex );
				Subgraph winning1 = SubgraphFactory.createSubgraph( gameGraph, win1Step, new HashSet<E>() );

				AlgorithmResultContent content = new AlgorithmResultContent();
				content.addDisplaySubgraph( DSM_WINNING_PLAYER1, winning1 );
				contentList.add( content );
			}
			result.setContentList( contentList );
		} else {
			Subgraph winning1 = SubgraphFactory.createSubgraph( gameGraph, new HashSet<V>( win1 ), new HashSet<E>() );
			
			AlgorithmResultContent content = new AlgorithmResultContent();
			content.addDisplaySubgraph( DSM_WINNING_PLAYER1, winning1 );
			result.setSingleContent( content );
		}

		return result;
	}
}
