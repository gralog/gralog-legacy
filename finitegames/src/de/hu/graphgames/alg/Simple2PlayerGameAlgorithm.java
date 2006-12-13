/*
 * Created on 8 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.graphgames.alg;

import java.util.ArrayList;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import de.hu.graphgames.graph.GameGraphVertex;

public class Simple2PlayerGameAlgorithm<V extends GameGraphVertex,E extends DefaultEdge> {
	
	private DirectedGraph<V,E> gameGraph;
	private ArrayList<V> vertexArray;
	private ArrayList<V> win1 = new ArrayList<V>();
	
	private boolean[] wa;
	private int[] ze;
	
	public Simple2PlayerGameAlgorithm( DirectedGraph<V,E> gameGraph ) {
		this.gameGraph = gameGraph;
		vertexArray = new ArrayList<V>( gameGraph.vertexSet() );
		
		init();
	}
	
	protected void init() {
		wa = new boolean[vertexArray.size()];
		ze = new int[vertexArray.size()];
		
		for ( int i = 0; i < vertexArray.size(); i++ ) {
			wa[i] = false;
			ze[i] = gameGraph.outDegreeOf( vertexArray.get( i ) );
		}
	}
	
	public ArrayList<V> execute() {
		for ( int i = 0; i < vertexArray.size(); i++ ) {
			if ( vertexArray.get( i ).isPlayer0() && ze[i] == 0 )
				propagate( i );
		}
		
		return win1;
	}
	
	private void propagate( int i ) {
		if ( wa[i] == false ) {
			wa[i] = true;
			win1.add( vertexArray.get( i ) );
			for ( E preEdge : gameGraph.incomingEdgesOf( vertexArray.get( i ) ) ) {
				V u = gameGraph.getEdgeSource( preEdge );
				int ui = vertexArray.indexOf( u );
				ze[ui]--;
				if ( (! u.isPlayer0()) || ze[ui] == 0 )
					propagate( ui );
					
			}
		}
	}
}