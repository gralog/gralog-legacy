package de.hu.nba.alg;

import org.jgrapht.graph.ListenableDirectedGraph;

import de.hu.gralog.algorithm.Algorithm;
import de.hu.gralog.algorithm.InvalidPropertyValuesException;
import de.hu.gralog.algorithm.result.AlgorithmResult;
import de.hu.gralog.algorithm.result.AlgorithmResultContent;
import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.GralogGraphFactory;
import de.hu.gralog.graph.GralogGraphSupport;
import de.hu.gralog.plugin.dfa.graph.AutomatonGraphBean;
import de.hu.gralog.plugin.dfa.graph.AutomatonGraphTypeInfo;
import de.hu.gralog.plugin.dfa.graph.AutomatonVertex;
import de.hu.gralog.plugin.dfa.graph.LabeledGraphEdge;

public class EmptinessTest<V extends AutomatonVertex, E extends LabeledGraphEdge, GB extends AutomatonGraphBean<V>, G extends ListenableDirectedGraph<V,E>> implements Algorithm {

	private GralogGraphSupport<V,E,GB,G> automaton;

	public GralogGraphSupport<V,E,GB,G> getAutomaton() {
		return automaton;
	}

	public void setGraph(GralogGraphSupport<V,E,GB,G> automaton) {
		this.automaton = automaton;
	}

	public AlgorithmResult execute() throws InvalidPropertyValuesException, UserException {
		InvalidPropertyValuesException pe = new InvalidPropertyValuesException();
			
		if ( automaton.getGraphBean().getStartVertex() == null )
			pe.addPropertyError( "startVertex",  InvalidPropertyValuesException.PROPERTY_REQUIRED );
		
		if ( getAutomaton() == null )
			pe.addPropertyError( "automaton",  InvalidPropertyValuesException.PROPERTY_REQUIRED );
		
		if ( pe.hasErrors() )
			throw pe;

	
		
		boolean languageIsEmpty = new EmptinessTestAlgorithm<V,E,GB,G>( automaton ).languageIsEmpty();

		AlgorithmResult result = new AlgorithmResult( automaton );

		String resultString = "The language defined by the nondeterministic-Buechi-automaton " + (languageIsEmpty?"IS":"is NOT") + " empty.";
		
		result.setDescription(resultString);
		
		GralogGraphSupport<AutomatonVertex,LabeledGraphEdge,AutomatonGraphBean,ListenableDirectedGraph<AutomatonVertex,LabeledGraphEdge>> resultGraph = GralogGraphFactory.createGraphSupport( new AutomatonGraphTypeInfo() );
		resultGraph.getGraph().addVertex(new AutomatonVertex(resultString));

		AlgorithmResultContent content = new AlgorithmResultContent();
		content.setGraphSupport(resultGraph);

		result.setSingleContent( content );

		return result;
	}
}
