package de.hu.nba.alg;

import de.hu.gralog.graph.alg.Algorithm;
import de.hu.gralog.graph.alg.AlgorithmResult;
import de.hu.gralog.graph.alg.AlgorithmResultContent;
import de.hu.gralog.graph.alg.InvalidPropertyValuesException;
import de.hu.gralog.graph.AutomatonGraphTypeInfo;
import de.hu.gralog.graph.AutomatonVertex;
import de.hu.gralog.graph.AutomatonGraph;

public class EmptinessTest implements Algorithm {

	private AutomatonGraph graph;


	public AutomatonGraph getGraph() {
		return graph;
	}

	public void setGraph(AutomatonGraph graph) {
		this.graph = graph;
	}

	public AlgorithmResult execute() throws InvalidPropertyValuesException {
		InvalidPropertyValuesException pe = new InvalidPropertyValuesException();
			
		if ( graph.getStartVertex() == null )
			pe.addPropertyError( "startVertex",  InvalidPropertyValuesException.PROPERTY_REQUIRED );
		
		if ( getGraph() == null )
			pe.addPropertyError( "graph",  InvalidPropertyValuesException.PROPERTY_REQUIRED );
		
		if ( pe.hasErrors() )
			throw pe;

	
		
		boolean languageIsEmpty = new EmptinessTestAlgorithm( graph ).languageIsEmpty();

		AlgorithmResult result = new AlgorithmResult( graph );

		String resultString = "The language defined by the nondeterministic-Buechi-automaton " + (languageIsEmpty?"IS":"is NOT") + " empty.";
		
		result.setDescription(resultString);
		
		AutomatonGraph resultGraph = (AutomatonGraph) new AutomatonGraphTypeInfo().newInstance();
		resultGraph.addVertex(new AutomatonVertex(resultString));

		AlgorithmResultContent content = new AlgorithmResultContent();
		content.setGraph(resultGraph);

		result.setSingleContent( content );


		return result;
	}
}
