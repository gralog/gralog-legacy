package de.hu.dfa.alg;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.jgrapht.graph.ListenableDirectedGraph;

import de.hu.dfa.structure.AutomatonBean;
import de.hu.dfa.structure.AutomatonEdge;
import de.hu.dfa.structure.AutomatonVertex;
import de.hu.gralog.structure.Structure;

public class ProcessingAlgorithm<V extends AutomatonVertex, E extends AutomatonEdge, GB extends AutomatonBean<V>, G extends ListenableDirectedGraph<V,E>> {

	private String inputPhrase;
	private Structure<V,E,GB,G> automaton;
	
	private boolean accepts;
	private boolean acceptsIsValid = false; // is true, if the value in the variable accepts has been set
	
	public ProcessingAlgorithm( String inputPhrase, Structure<V,E,GB,G> automaton ) {
		this.inputPhrase = inputPhrase;
		this.automaton = automaton;
	}
	
	
	/**
	 * Simple algorithm, that - starting at the graphs startVertex - goes through all edges of the
	 * vertex and searches for the first, where the label includes the string, that is to be
	 * processed. After that it goes on with the according vertex and tries to process the next
	 * string of the inputPhrase.
	 * 
	 * @return a list of the processed edges.
	 */
	public ArrayList<AutomatonEdge> getProcessedEdges() {
		
		ArrayList<AutomatonEdge> edgeList = new ArrayList<AutomatonEdge>();
		
		V currentVertex = automaton.getStructureBean().getStartVertex();
		
		StringTokenizer stInput = new StringTokenizer(inputPhrase,",");
		
PROCESSING_VERTICES:
		while (stInput.hasMoreTokens()) {			
			// the current input string:
			String inputString = stInput.nextToken().trim();
			// empty inputStrings are omitted:
			if ( inputString.equals("") ) continue;
			
			for ( E outEdge : automaton.getGraph().outgoingEdgesOf(currentVertex)) {
				StringTokenizer stEdge = new StringTokenizer(outEdge.getLabel(),",");
				// search the label of the edge for the inputString:
				while (stEdge.hasMoreTokens()) {
					// if the edge fits, go on with the according neighbor-vertex:
					if (inputString.equals(stEdge.nextToken().trim())) {
						edgeList.add(outEdge);
						currentVertex = automaton.getGraph().getEdgeTarget(outEdge);
					continue PROCESSING_VERTICES; // continue with the next loop of the specified "while"
												  // (the most outer one)
					}
				}
			}

			// no fitting edge found --> the DFA does not accept:
			accepts = false;
			acceptsIsValid = true;			
			break;
			
		}
		
		if (!acceptsIsValid) {
			// the whole inputPhrase was processed
			acceptsIsValid = true;
			accepts = currentVertex.isAcceptingState();
		}
		
		return edgeList;
	}
	
	public boolean inputPhraseIsAccepted() {
		// if the inputPhrase has not been processed yet, start the algorithm:
		if (!acceptsIsValid) getProcessedEdges();
		
		return accepts;		
	}

}