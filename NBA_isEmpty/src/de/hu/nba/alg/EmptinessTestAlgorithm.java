package de.hu.nba.alg;

import java.util.Iterator;
import java.util.Set;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.ListenableDirectedGraph;

import de.hu.gralog.graph.AutomatonGraphBean;
import de.hu.gralog.graph.AutomatonVertex;
import de.hu.gralog.graph.GralogGraphSupport;
import de.hu.gralog.graph.LabeledGraphEdge;

public class EmptinessTestAlgorithm<V extends AutomatonVertex, E extends LabeledGraphEdge, GB extends AutomatonGraphBean<V>, G extends ListenableDirectedGraph<V,E>> {
	
	private GralogGraphSupport<V,E,GB,G> graphSupport;
	
	public EmptinessTestAlgorithm(GralogGraphSupport<V,E,GB,G> graphSupport ) {
		this.graphSupport = graphSupport;
	}
	
	
	/**
	 * This algorithm returns, whether the language defined by the
	 * nondeterministic-Buechi-automaton graph is empty or not.
	 * 
	 * @return True, if the language is empty.
	 */
	public boolean languageIsEmpty() {
		StrongConnectivityInspector<V,E> sci
			= new StrongConnectivityInspector<V,E>( graphSupport.getGraph() );
		Iterator<Set<V>> it = sci.stronglyConnectedSets().iterator();
		
		while (it.hasNext()) {
			// search through all strongly connected sets:
			Set<V> s = it.next();
			V vertexOfSet = s.iterator().next();
			
			// every set s, with only one element (without a loop) or which is out of reach
			// of the startVertex is ignored:
			if (
					((s.size() == 1) && !(graphSupport.getGraph().containsEdge(vertexOfSet, vertexOfSet)))
					|| (DijkstraShortestPath.findPathBetween(graphSupport.getGraph(), graphSupport.getGraphBean().getStartVertex(), vertexOfSet) == null)
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