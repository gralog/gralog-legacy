/*
 * Created 2006 by Sebastian Ordyniak
 * Implemented 2008 by Stephan Kreutzer
 *
 * Copyright 2006 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of Gralog.
 *
 * Gralog is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * Gralog is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Games; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */
package de.hu.gralog.finitegames.alg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import de.hu.gralog.finitegames.graph.GameGraphVertex;








/**
 * @author Stephan Kreutzer 
 *
 * This class implements a linear time algorithm for solving simple 2 player games.
 * (Well, this would be linear time except for the references to the HashMap vertexDegree. On
 * any reasonably good hash function, this should be more or less linear time as collisions should be 
 * rare.)
 * The algorithm proceeds as follows.
 * In vertexDegree we store the outdegree of the vertices.  
 * First we store in vertexList all vertices of player 1 that have
 * no successors. We then proceed through the vertexList. This list only contains vertices from which 
 * player 0 wins. So the first element v is added to win0, the winning region for player 0.
 * We then inspect the predecessors u of v. If  u is a node for player 0, then he wins from u. Hence we 
 * add u to vertexList and set its degree in vertexDegree to -1 to mark that it has been processed.
 * If u is for player 1, then we have to inspect the degree of u in vertexDegree. If this is 1, then
 * Player 1 has no choice but to move to v. Hence Player 0 wins from u and we add it to vertexList and set its
 * degree in vertexDegree to -1. Otherwise, we decrease its degree by 1 in vertexDegree.
 * @param <V> Vertex class 
 * @param <E> Edge class
 */
public class Simple2PlayerGameAlgorithm<V extends GameGraphVertex,E extends DefaultEdge> {
	
	private DirectedGraph<V,E> gameGraph;	// game graph
	private ArrayList<V> win0;			// winning region for player 0.
	private Set<V> vertexSet;			// vertex set of the graph
	private LinkedList<V> vertexList; // list used in the algorithm.
	private HashMap<V, Integer> vertexDegree;		// stores the outdegrees of the vertices. the degrees will be decreased during the run of the alg.
	
	public Simple2PlayerGameAlgorithm( DirectedGraph<V,E> gameGraph ) {
		this.gameGraph = gameGraph;
		vertexSet = gameGraph.vertexSet();
		
		vertexList = new LinkedList<V>();
		vertexDegree = new HashMap<V, Integer>(vertexSet.size());
		init();
	}
	
	protected void init() {
		win0 = new ArrayList<V>();
		for(V v : vertexSet)
		{
			vertexDegree.put(v, gameGraph.outDegreeOf(v));
			if(gameGraph.outDegreeOf(v) == 0 && !v.isPlayer0())
			{
				vertexList.add(v);
			}
		}
	}
	


	public ArrayList<V> execute() 
	{
		V v,u;
		Integer i;
		while(!vertexList.isEmpty())
		{
			v = vertexList.remove();
			win0.add(v);
			for (E e : gameGraph.incomingEdgesOf(v))
			{
				u = gameGraph.getEdgeSource(e);
				i = vertexDegree.get(u);
				if(i>=0)	// this indicates that the vertex has not yet been processed.
				{
					if(u.isPlayer0())
					{
						vertexList.add(u);
						vertexDegree.put(u, -1);
					}
					else
					{
						if(i > 1)
						{
							vertexDegree.put(u, i-1);
						}
						else
						{
							vertexDegree.put(u, -1);
							vertexList.add(u);
						}
					}
				}
			}
		}
		
		return win0;
	}
	

}