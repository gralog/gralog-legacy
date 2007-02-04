package de.hu.nba.alg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.Subgraph;

import de.hu.gralog.graph.alg.Algorithm;
import de.hu.gralog.graph.alg.AlgorithmResult;
import de.hu.gralog.graph.alg.AlgorithmResultContent;
import de.hu.gralog.graph.alg.InvalidPropertyValuesException;
import de.hu.gralog.jgrapht.graph.DisplaySubgraphMode;
import de.hu.gralog.jgrapht.graph.SubgraphFactory;
import de.hu.gralog.jgrapht.graph.DisplaySubgraph.DisplayMode;
import de.hu.gralog.graph.AutomatonVertex;
import de.hu.gralog.graph.AutomatonGraph;
import de.hu.gralog.graph.LabeledGraphEdge;

public class EmptinessTest implements Algorithm {

	private static final String DSM_IS_EMPTY = "Vertices, that can be reached in an infinite loop.";

	private AutomatonGraph graph;


	public AutomatonGraph getGraph() {
		return graph;
	}

	public void setGraph(AutomatonGraph graph) {
		this.graph = graph;
	}

	public AlgorithmResult execute(  ) throws InvalidPropertyValuesException {
		InvalidPropertyValuesException pe = new InvalidPropertyValuesException();
			
		if ( graph.getStartVertex() == null )
			pe.addPropertyError( "startVertex",  InvalidPropertyValuesException.PROPERTY_REQUIRED );
		
		if ( getGraph() == null )
			pe.addPropertyError( "graph",  InvalidPropertyValuesException.PROPERTY_REQUIRED );
		
		if ( pe.hasErrors() )
			throw pe;

	
		
		ArrayList<AutomatonVertex> vertexList = new EmptinessTestAlgorithm( graph ).execute();

		AlgorithmResult result = new AlgorithmResult( graph );
		result.setDescription( "The highlighted vertices can be reached in an infinite loop." );
		
		DisplaySubgraphMode displaySubgraphMode = new DisplaySubgraphMode( );
		displaySubgraphMode.setVertexDisplayMode( DisplayMode.HIGH2, DisplayMode.HIGH1 );

		result.addDisplaySubgraphMode( DSM_IS_EMPTY, displaySubgraphMode );
		
		
		Set<AutomatonVertex> resultSetVertices = new HashSet<AutomatonVertex>(vertexList);

	
		Subgraph testSubGraph = SubgraphFactory.createSubgraph( graph, resultSetVertices, new HashSet<LabeledGraphEdge>() );
		
		AlgorithmResultContent content = new AlgorithmResultContent();
		content.addDisplaySubgraph( DSM_IS_EMPTY, testSubGraph );
		result.setSingleContent( content );


		return result;
	}
}
