package de.hu.dfa.alg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.ListenableDirectedGraph;
import org.jgrapht.graph.Subgraph;

import de.hu.dfa.structure.AutomatonBean;
import de.hu.dfa.structure.AutomatonEdge;
import de.hu.dfa.structure.AutomatonVertex;
import de.hu.gralog.algorithm.Algorithm;
import de.hu.gralog.algorithm.InvalidPropertyValuesException;
import de.hu.gralog.algorithm.result.AlgorithmResult;
import de.hu.gralog.algorithm.result.AlgorithmResultContent;
import de.hu.gralog.algorithm.result.DisplaySubgraphMode;
import de.hu.gralog.algorithm.result.DisplaySubgraph.DisplayMode;
import de.hu.gralog.structure.Structure;

public class Processing<V extends AutomatonVertex, E extends AutomatonEdge, GB extends AutomatonBean<V>, G extends ListenableDirectedGraph<V,E>> implements Algorithm {

	private static final String DSM_PROCESSED_VERTICES_SET = "processed vertices";
	private static final String DSM_PROCESSED_EDGES_SET = "processed edges";
	
	private String inputPhrase;
	private Structure<V,E,GB,G> automaton;
	private boolean showSteps;
	
	public String getInputPhrase() {
		return inputPhrase;
	}

	public void setInputPhrase(String inputPhrase) {
		this.inputPhrase = inputPhrase;
	}

	public Structure<V,E,GB,G> getAutomaton() {
		return automaton;
	}

	public void setAutomaton( Structure<V,E,GB,G> automaton ) {
		this.automaton = automaton;
	}

	public boolean isShowSteps() {
		return showSteps;
	}
	
	public void setShowSteps( boolean showSteps ) {
		this.showSteps = showSteps;
	}

	public AlgorithmResult execute(  ) throws InvalidPropertyValuesException {
		InvalidPropertyValuesException pe = new InvalidPropertyValuesException();

		if ( getInputPhrase() == null )
			pe.addPropertyError( "inputPhrase",  InvalidPropertyValuesException.PROPERTY_REQUIRED );

		if ( getAutomaton() == null )
			pe.addPropertyError( "automaton",  InvalidPropertyValuesException.PROPERTY_REQUIRED );
		
		if ( getAutomaton().getStructureBean().getStartVertex() == null )
			pe.addPropertyError( "startVertex",  InvalidPropertyValuesException.PROPERTY_REQUIRED );
		
		if ( pe.hasErrors() )
			throw pe;
		
		ProcessingAlgorithm pa = new ProcessingAlgorithm( inputPhrase, getAutomaton() );
		ArrayList<E> edgeList = pa.getProcessedEdges();
		
		//TODO: Ausgabe vernünftig machen:
		String acceptString = "The inputPhrase " + (pa.inputPhraseIsAccepted()?"IS":"is NOT") + " accepted by the given DFA.";
		
		AlgorithmResult result = new AlgorithmResult( getAutomaton() );
		result.setDescription(acceptString);
		
		DisplaySubgraphMode displaySubgraphModeVertices = new DisplaySubgraphMode( );
		displaySubgraphModeVertices.setVertexDisplayMode( DisplayMode.HIGH2, DisplayMode.SHOW );

		DisplaySubgraphMode displaySubgraphModeEdges = new DisplaySubgraphMode( );
		displaySubgraphModeEdges.setEdgeDisplayMode( DisplayMode.HIGH2, DisplayMode.SHOW );

		result.addDisplaySubgraphMode( DSM_PROCESSED_VERTICES_SET, displaySubgraphModeVertices );
		result.addDisplaySubgraphMode( DSM_PROCESSED_EDGES_SET, displaySubgraphModeEdges );
		
		Set<V> resultSetVertices = new HashSet<V>();
		AlgorithmResultContent content = new AlgorithmResultContent();
		
		Subgraph verticesSubGraph, edgesSubGraph;
		
		if ( showSteps && edgeList.size() != 0) {
			// result will consist of a list of small pairs of subgraphs that indicate the
			// processed edge and next vertex:
			
			ArrayList<AlgorithmResultContent> contentList = new ArrayList<AlgorithmResultContent>();
			
			resultSetVertices.add(getAutomaton().getStructureBean().getStartVertex());
			content.addDisplaySubgraph( DSM_PROCESSED_VERTICES_SET, resultSetVertices, new HashSet<E>() );
			contentList.add( content );
			
			for ( E edge : edgeList ) {

				// only the edge-target ...
				resultSetVertices.clear();				
				resultSetVertices.add(getAutomaton().getGraph().getEdgeTarget(edge));				

				// ... and the edge will be shown
				resultSetVertices.add(getAutomaton().getGraph().getEdgeSource(edge));		
				HashSet<E> tempEdgeSet = new HashSet<E>();
				tempEdgeSet.add(edge);

				content = new AlgorithmResultContent();
				content.addDisplaySubgraph( DSM_PROCESSED_VERTICES_SET, resultSetVertices, new HashSet<E>() );
				content.addDisplaySubgraph( DSM_PROCESSED_EDGES_SET, resultSetVertices, tempEdgeSet );
				contentList.add( content );

			}
			
			result.setContentList( contentList );
			
		} else {
			
			// all vertices (including the startVertex) are added to the vertex set:
			resultSetVertices.add( getAutomaton().getStructureBean().getStartVertex());
			for ( E edge : edgeList)
				resultSetVertices.add( getAutomaton().getGraph().getEdgeTarget(edge));
			
			verticesSubGraph = new Subgraph( getAutomaton().getGraph(), resultSetVertices, new HashSet<E>() );
			edgesSubGraph = new Subgraph( getAutomaton().getGraph(), resultSetVertices, new HashSet<E>( edgeList ) );
			
			content.addDisplaySubgraph( DSM_PROCESSED_VERTICES_SET, resultSetVertices, new HashSet<E>());
			content.addDisplaySubgraph( DSM_PROCESSED_EDGES_SET, resultSetVertices, new HashSet<E>( edgeList ) );
			
			ArrayList<AlgorithmResultContent> contentList = new ArrayList<AlgorithmResultContent>();
			contentList.add( content );
			result.setContentList( contentList );
			
		}

		return result;
	}
}
