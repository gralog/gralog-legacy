package de.hu.nba.alg;

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

public class EmptinessTest<V extends LabeledGraphVertex,E extends DefaultEdge> implements Algorithm {

	private static final String DSM_IS_EMPTY = "Vertices, that can be reached in an infinite loop.";

	private V startVertex;
	private DirectedGraph<V, E> graph;


	public DirectedGraph<V,E> getGraph() {
		return graph;
	}

	public V getStartVertex() {
		return startVertex;
	}

	public void setStartVertex(V startVertex) {
		this.startVertex = startVertex;
	}

	public void setGraph(DirectedGraph<V, E> graph) {
		this.graph = graph;
	}

	public AlgorithmResult execute(  ) throws InvalidPropertyValuesException {
		InvalidPropertyValuesException pe = new InvalidPropertyValuesException();
			
//		if ( getStartVertex() == null )
//			pe.addPropertyError( "startVertex",  InvalidPropertyValuesException.PROPERTY_REQUIRED );
		
		if ( getGraph() == null )
			pe.addPropertyError( "graph",  InvalidPropertyValuesException.PROPERTY_REQUIRED );
		
		if ( pe.hasErrors() )
			throw pe;
		
		//TODO: the startVertex should not allways have to be the first one in the Set
		//		---> delete next line, when propertydescriptor ready
		startVertex = (new ArrayList<V>(graph.vertexSet())).get(0);
	
		
		ArrayList<V> vertexList = new EmptinessTestAlgorithm<V,E>( startVertex, graph ).execute();

		AlgorithmResult result = new AlgorithmResult( graph );
		result.setDescription( "The highlighted vertices can be reached in an infinite loop." );
		
		DisplaySubgraphMode displaySubgraphMode = new DisplaySubgraphMode( );
		displaySubgraphMode.setVertexDisplayMode( DisplayMode.HIGH2, DisplayMode.HIGH1 );

		result.addDisplaySubgraphMode( DSM_IS_EMPTY, displaySubgraphMode );
		
		
		Set<V> resultSetVertices = new HashSet<V>(vertexList);

	
		Subgraph testSubGraph = SubgraphFactory.createSubgraph( graph, resultSetVertices, new HashSet<E>() );
		
		AlgorithmResultContent content = new AlgorithmResultContent();
		content.addDisplaySubgraph( DSM_IS_EMPTY, testSubGraph );
		result.setSingleContent( content );


		return result;
	}
}
