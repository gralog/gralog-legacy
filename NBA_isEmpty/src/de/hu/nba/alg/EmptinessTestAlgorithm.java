package de.hu.nba.alg;

import java.util.Iterator;
import java.util.Set;

import de.hu.gralog.graph.AutomatonGraph;
import de.hu.gralog.graph.AutomatonVertex;
import de.hu.gralog.graph.LabeledGraphEdge;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.StrongConnectivityInspector;

public class EmptinessTestAlgorithm {
	
	private AutomatonGraph graph;
	
	public EmptinessTestAlgorithm(AutomatonGraph graph ) {
		this.graph = graph;
	}
	
	
	/**
	 * This algorithm returns, whether the language defined by the
	 * nondeterministic-Buechi-automaton graph is empty or not.
	 * 
	 * @return True, if the language is empty.
	 */
	public boolean languageIsEmpty() {
		StrongConnectivityInspector<AutomatonVertex,LabeledGraphEdge> sci
			= new StrongConnectivityInspector<AutomatonVertex,LabeledGraphEdge>( graph );
		Iterator<Set<AutomatonVertex>> it = sci.stronglyConnectedSets().iterator();
		
		while (it.hasNext()) {
			// search throug all strongly connected sets:
			Set<AutomatonVertex> s = it.next();
			AutomatonVertex vertexOfSet = s.iterator().next();
			
			// every set s, with only one element (without a loop) or which is out of reach
			// of the startVertex is ignored:
			if (
					((s.size() == 1) && !(graph.containsEdge(vertexOfSet, vertexOfSet)))
					|| (DijkstraShortestPath.findPathBetween(graph, graph.getStartVertex(), vertexOfSet) == null)
				) {
				continue;
			} else {
				// there is (at least) one vertex of a strongly connected set,
				// that is in reach of the startVertex - thus the language is NOT empty.
				return false;
			}
		}
		
		// if there is no such set, the language IS empty:
		return true;
	}
	
}