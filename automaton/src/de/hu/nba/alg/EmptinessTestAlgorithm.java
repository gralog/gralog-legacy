package de.hu.nba.alg;

import java.util.Iterator;
import java.util.Set;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.ListenableDirectedGraph;

import de.hu.dfa.structure.AutomatonBean;
import de.hu.dfa.structure.AutomatonEdge;
import de.hu.dfa.structure.AutomatonVertex;
import de.hu.gralog.structure.Structure;

public class EmptinessTestAlgorithm<V extends AutomatonVertex, E extends AutomatonEdge, GB extends AutomatonBean<V>, G extends ListenableDirectedGraph<V,E>> {
	
	private Structure<V,E,GB,G> structure;
	
	public EmptinessTestAlgorithm(Structure<V,E,GB,G> structure ) {
		this.structure = structure;
	}
	
	
	/**
	 * This algorithm returns, whether the language defined by the
	 * nondeterministic-Buechi-automaton graph is empty or not.
	 * 
	 * @return True, if the language is empty.
	 */
	public boolean languageIsEmpty() {
		StrongConnectivityInspector<V,E> sci
			= new StrongConnectivityInspector<V,E>( structure.getGraph() );
		Iterator<Set<V>> it = sci.stronglyConnectedSets().iterator();
		
		while (it.hasNext()) {
			// search through all strongly connected sets:
			Set<V> s = it.next();
			V vertexOfSet = s.iterator().next();
			
			// every set s, with only one element (without a loop) or which is out of reach
			// of the startVertex is ignored:
			if (
					((s.size() == 1) && !(structure.getGraph().containsEdge(vertexOfSet, vertexOfSet)))
					|| (DijkstraShortestPath.findPathBetween(structure.getGraph(), structure.getStructureBean().getStartVertex(), vertexOfSet) == null)
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