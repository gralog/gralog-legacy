package de.hu.nba.alg;

import org.jgrapht.graph.ListenableDirectedGraph;

import de.hu.dfa.structure.AutomatonBean;
import de.hu.dfa.structure.AutomatonEdge;
import de.hu.dfa.structure.AutomatonTypeInfo;
import de.hu.dfa.structure.AutomatonVertex;
import de.hu.gralog.algorithm.Algorithm;
import de.hu.gralog.algorithm.InvalidPropertyValuesException;
import de.hu.gralog.algorithm.result.AlgorithmResult;
import de.hu.gralog.algorithm.result.AlgorithmResultContent;
import de.hu.gralog.app.UserException;
import de.hu.gralog.structure.Structure;
import de.hu.gralog.structure.StructureFactory;

public class EmptinessTest<V extends AutomatonVertex, E extends AutomatonEdge, GB extends AutomatonBean<V>, G extends ListenableDirectedGraph<V,E>> implements Algorithm {

	private Structure<V,E,GB,G> automaton;

	public Structure<V,E,GB,G> getAutomaton() {
		return automaton;
	}

	public void setAutomaton(Structure<V,E,GB,G> automaton) {
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

		AlgorithmResult result = new AlgorithmResult(  );

		String resultString = "The language defined by the given non-deterministic-automaton " + (languageIsEmpty?"IS":"is NOT") + " empty.";
		
		result.setDescription(resultString);
		
		return result;
	}
}
