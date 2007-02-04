package de.hu.nba.alg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;


import org.jgrapht.DirectedGraph;


import de.hu.gralog.graph.AutomatonGraph;
import de.hu.gralog.graph.AutomatonVertex;
import de.hu.gralog.graph.LabeledGraphEdge;
import de.hu.gralog.graph.LabeledGraphVertex;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.StrongConnectivityInspector;

public class EmptinessTestAlgorithm {
	
	private AutomatonGraph graph;
	
	public EmptinessTestAlgorithm(AutomatonGraph graph ) {
		this.graph = graph;
	}
	
	public ArrayList<AutomatonVertex> execute() {
		ArrayList<AutomatonVertex> resultList = new ArrayList<AutomatonVertex>();
		
		StrongConnectivityInspector<AutomatonVertex,LabeledGraphEdge> sci = new StrongConnectivityInspector<AutomatonVertex,LabeledGraphEdge>( graph );
		Iterator<Set<AutomatonVertex>> it = sci.stronglyConnectedSets().iterator();
		
		while (it.hasNext()) {
			Set<AutomatonVertex> s = it.next();
			AutomatonVertex nodeOfSet = s.iterator().next();
			
//			every strong connected set, with only one element (without a loop) or which is out of reach of the startVertex is ignored
			if (
					((s.size() == 1) && !(graph.containsEdge(nodeOfSet, nodeOfSet)))
					|| (DijkstraShortestPath.findPathBetween(graph, graph.getStartVertex(), nodeOfSet) == null)
				)
				continue;
			
			for (AutomatonVertex vertex : s) {
				resultList.add(vertex);				
			}
			
		}
		
		return resultList;
	}
}