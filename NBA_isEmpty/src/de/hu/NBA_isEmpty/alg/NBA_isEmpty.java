package de.hu.NBA_isEmpty.alg;

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
import de.hu.gralog.graph.LabeledGraphVertex;

public class NBA_isEmpty<V extends LabeledGraphVertex,E extends DefaultEdge> implements Algorithm {

	private static final String DSM_IS_EMPTY = "Vertices, that can be reached in an infinite loop.";

	private V startVertex;
	private DirectedGraph<V, E> gameGraph;


	public DirectedGraph<V,E> getGameGraph() {
		return gameGraph;
	}

	public V getStartVertex() {
		return startVertex;
	}

	public void setStartVertex(V startVertex) {
		this.startVertex = startVertex;
	}

	public void setGameGraph(DirectedGraph<V, E> graph) {
		this.gameGraph = graph;
	}

	public AlgorithmResult execute(  ) throws InvalidPropertyValuesException {
		InvalidPropertyValuesException pe = new InvalidPropertyValuesException();
			
//		if ( getStartVertex() == null )
//			pe.addPropertyError( "startVertex",  InvalidPropertyValuesException.PROPERTY_REQUIRED );
		
		if ( getGameGraph() == null )
			pe.addPropertyError( "gameGraph",  InvalidPropertyValuesException.PROPERTY_REQUIRED );
		
		if ( pe.hasErrors() )
			throw pe;
		
		//TODO: the startVertex should not allways have to be the first one in the Set
		//		---> delete next line, when propertydescriptor ready
		startVertex = (new ArrayList<V>(gameGraph.vertexSet())).get(0);
	
		
		ArrayList<V> vertexList = new NBA_isEmptyAlgorithm<V,E>( startVertex, gameGraph ).execute();

		AlgorithmResult result = new AlgorithmResult( gameGraph );
		result.setDescription( "The highlighted vertices can be reached in an infinite loop." );
		
		DisplaySubgraphMode displaySubgraphMode = new DisplaySubgraphMode( );
		displaySubgraphMode.setVertexDisplayMode( DisplayMode.HIGH2, DisplayMode.HIGH1 );

		result.addDisplaySubgraphMode( DSM_IS_EMPTY, displaySubgraphMode );
		
		
		Set<V> resultSetVertices = new HashSet<V>(vertexList);

	
		Subgraph testSubGraph = SubgraphFactory.createSubgraph( gameGraph, resultSetVertices, new HashSet<E>() );
		
		AlgorithmResultContent content = new AlgorithmResultContent();
		content.addDisplaySubgraph( DSM_IS_EMPTY, testSubGraph );
		result.setSingleContent( content );


		return result;
	}
}
