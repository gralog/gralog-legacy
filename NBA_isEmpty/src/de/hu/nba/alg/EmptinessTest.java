package de.hu.nba.alg;

import org.jgrapht.graph.ListenableDirectedGraph;

import de.hu.gralog.algorithm.Algorithm;
import de.hu.gralog.algorithm.InvalidPropertyValuesException;
import de.hu.gralog.algorithm.result.AlgorithmResult;
import de.hu.gralog.algorithm.result.AlgorithmResultContent;
import de.hu.gralog.app.UserException;
import de.hu.gralog.structure.types.elements.LabeledStructureEdge;
import de.hu.gralog.structure.*;
import de.hu.dfa.structure.*;


public class EmptinessTest<V extends AutomatonVertex, E extends LabeledStructureEdge, GB extends AutomatonBean<V>, G extends ListenableDirectedGraph<V,E>> implements Algorithm {

	private static final long serialVersionUID = 1L;
	private Structure<V,E,GB,G> automaton;

	public Structure<V,E,GB,G> getAutomaton() {
		return automaton;
	}

	public void setGraph(Structure<V,E,GB,G> automaton) {
		this.automaton = automaton;
	}

	public AlgorithmResult execute() throws InvalidPropertyValuesException, UserException {
		InvalidPropertyValuesException pe = new InvalidPropertyValuesException();
			
		if ( automaton.getStructureBean().getStartVertex() == null )
			pe.addPropertyError( "startVertex",  InvalidPropertyValuesException.PROPERTY_REQUIRED );
		
		if ( getAutomaton() == null )
			pe.addPropertyError( "automaton",  InvalidPropertyValuesException.PROPERTY_REQUIRED );
		
		if ( pe.hasErrors() )
			throw pe;

	
		
		boolean languageIsEmpty = new EmptinessTestAlgorithm<V,E,GB,G>( automaton ).languageIsEmpty();

		AlgorithmResult result = new AlgorithmResult( automaton );

		String resultString = "The language defined by the nondeterministic-Buechi-automaton " + (languageIsEmpty?"IS":"is NOT") + " empty.";
		
		result.setDescription(resultString);
		
		Structure<AutomatonVertex,AutomatonEdge,AutomatonBean,ListenableDirectedGraph<AutomatonVertex,AutomatonEdge>> resultGraph = StructureFactory.createStructure( new AutomatonTypeInfo() );
		resultGraph.getGraph().addVertex(new AutomatonVertex(resultString));

		AlgorithmResultContent content = new AlgorithmResultContent();
		content.setStructure(resultGraph);

		result.setSingleContent( content );

		return result;
	}
}
