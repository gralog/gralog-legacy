package de.hu.NBA_isEmpty.alg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;


import org.jgrapht.DirectedGraph;


import de.hu.gralog.graph.LabeledGraphVertex;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.StrongConnectivityInspector;

public class NBA_isEmptyAlgorithm<V extends LabeledGraphVertex,E extends DefaultEdge> {

	private V startVertex;
	private DirectedGraph<V,E> gameGraph;
	
	public NBA_isEmptyAlgorithm(V startVertex, DirectedGraph<V,E> gameGraph ) {
		this.startVertex = startVertex;
		this.gameGraph = gameGraph;
	}
	
	public ArrayList<V> execute() {
		ArrayList<V> resultList = new ArrayList<V>();
		
		StrongConnectivityInspector<V,E> sci = new StrongConnectivityInspector<V,E>( gameGraph );
		Iterator<Set<V>> it = sci.stronglyConnectedSets().iterator();
		
		while (it.hasNext()) {
			Set<V> s = it.next();
			V nodeOfSet = (V)s.toArray()[0];
			
//			every strong connected set, with only one element or which is out of reach of the startVertex is ignored
			if ((s.size() == 1) || (DijkstraShortestPath.findPathBetween(gameGraph, startVertex, nodeOfSet) == null))
				continue;
			
			for (V vertex : s) {
				resultList.add(vertex);				
			}
			
		}
		
		return resultList;
	}
}