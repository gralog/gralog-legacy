package de.hu.nba.alg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;


import org.jgrapht.DirectedGraph;


import de.hu.gralog.graph.LabeledGraphVertex;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.StrongConnectivityInspector;

public class EmptinessTestAlgorithm<V extends LabeledGraphVertex,E extends DefaultEdge> {

	private V startVertex;
	private DirectedGraph<V,E> graph;
	
	public EmptinessTestAlgorithm(V startVertex, DirectedGraph<V,E> graph ) {
		this.startVertex = startVertex;
		this.graph = graph;
	}
	
	public ArrayList<V> execute() {
		ArrayList<V> resultList = new ArrayList<V>();
		
		StrongConnectivityInspector<V,E> sci = new StrongConnectivityInspector<V,E>( graph );
		Iterator<Set<V>> it = sci.stronglyConnectedSets().iterator();
		
		while (it.hasNext()) {
			Set<V> s = it.next();
			V nodeOfSet = s.iterator().next();
			
//			every strong connected set, with only one element or which is out of reach of the startVertex is ignored
			if ((s.size() == 1) || (DijkstraShortestPath.findPathBetween(graph, startVertex, nodeOfSet) == null))
				continue;
			
			for (V vertex : s) {
				resultList.add(vertex);				
			}
			
		}
		
		return resultList;
	}
}