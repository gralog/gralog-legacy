/*
 * Created on 2006 by Sebastian Ordyniak
 *
 * Copyright 2006 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of Games.
 *
 * Games is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * Games is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Games; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */

package de.hu.gralog.graph.alg.types;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.ClosestFirstIterator;

import de.hu.gralog.graph.types.GameGraphVertex;
import de.hu.gralog.jgrapht.graph.SubgraphFactory;
import de.hu.gralog.jgrapht.traverse.LongestPathIterator;
import de.hu.gralog.jgrapht.traverse.VertexFilter;

public class Algorithms {

	public static <V,E> Set<V> reach( Graph<V,E> graph, V startVertex, boolean reverseEdges, VertexFilter<V> vertexFilter ) {
		Iterator<V> it = new BreadthFirstIterator<V,E>( SubgraphFactory.createInducedSubgraph( graph, vertexFilter, reverseEdges ), startVertex );
		HashSet<V> reach = new HashSet<V>();
		while ( it.hasNext() )
			reach.add( it.next() );
		return reach;
	}
	
	public static <V,E> Hashtable<V, Integer> min_distances( Graph<V,E> graph, V vertex ) {
		Hashtable<V, Integer> distances = new Hashtable<V, Integer>();
		ClosestFirstIterator<V,E> it = new ClosestFirstIterator<V,E>( SubgraphFactory.createInducedSubgraph( graph, null, true ), vertex );
		
		while ( it.hasNext() ) {
			V v = it.next();
			distances.put( v, new Integer( (int)it.getShortestPathLength( v ) ) );
		}
		
		return distances;
	}
	
	public static <V,E> Hashtable<V, Integer> max_distances( Graph<V,E> graph, V vertex ) {
		Hashtable<V,Integer> distances = new Hashtable<V,Integer>();
		
		LongestPathIterator<V,E> it = new LongestPathIterator<V,E>( (DirectedGraph<V,E>)SubgraphFactory.createInducedSubgraph( graph, null, true ), vertex);
		while ( it.hasNext() ) {
			V v = it.next();
			distances.put( v, new Integer( (int)it.getLongestPathLength( v )));
		}
		
		return distances;
	}
	
	public static class FiniteGameAlgorithm {

		public static <V extends GameGraphVertex,E> Set<V> getWinningPositionsPlayer0( DirectedGraph<V, E> graph ) {
			ArrayList<Set<V>> steps = getWinningPositionsPlayer0ByStep( graph );
			return steps.get( steps.size() - 1 );
		}
		
		
		
		public static <V extends GameGraphVertex,E> ArrayList<Set<V>> getWinningPositionsPlayer0ByStep( DirectedGraph<V, E> graph ) {
			Hashtable<V, VertexInfo> vertexInfos = new Hashtable<V,VertexInfo>();
			
			LinkedList<V> queue = new LinkedList<V>();
			
			ArrayList<Set<V>> steps = new ArrayList<Set<V>>();
			
			HashSet<V> win0 = new HashSet<V>();
			Iterator<V> vertexes = graph.vertexSet().iterator();
			while ( vertexes.hasNext() ) {
				V vertex = vertexes.next();
				VertexInfo info = new VertexInfo();
				info.setSucc( graph.outDegreeOf( vertex ) );
				if ( info.getSucc() != 0 || vertex.isPlayer0() )
					info.setWinPlayer0( false );
				else {
					win0.add( vertex );
					info.setWinPlayer0( false );
					queue.add( vertex );
				}
				vertexInfos.put( vertex, info );	
			}
			
			steps.add( (Set<V>)win0.clone() );
			
			while ( queue.size() != 0 ) {
				V vertex = queue.removeFirst();
				Iterator<E> predecessors = graph.incomingEdgesOf( vertex ).iterator();
				while ( predecessors.hasNext() ) {
					V predecessor = Graphs.getOppositeVertex( graph, predecessors.next(), vertex );
					VertexInfo predecessorInfo = vertexInfos.get( predecessor );
					if ( predecessorInfo.isWinPlayer0() ) {
						if ( predecessor.isPlayer0() ) {
							predecessorInfo.setWinPlayer0( false );
							win0.add( predecessor );
							queue.addLast( predecessor );
						} else {
							predecessorInfo.setSucc( predecessorInfo.getSucc() - 1 );
							if ( predecessorInfo.getSucc() == 0 ) {
								queue.addLast( predecessor );
								predecessorInfo.setWinPlayer0( false );
								win0.add( predecessor );
							}
						}
					}
				}
				if ( ! steps.get( steps.size() - 1 ).equals( win0 ) )
					steps.add( (Set<V>)win0.clone() );
			}
			return steps;
		}
		
		private static class VertexInfo {
			
			private int succ;
			private boolean winPlayer0;
			
			public VertexInfo(  ) {
			}

			public int getSucc() {
				return succ;
			}

			public void setSucc(int succ) {
				this.succ = succ;
			}

			public boolean isWinPlayer0() {
				return winPlayer0;
			}

			public void setWinPlayer0(boolean win) {
				this.winPlayer0 = win;
			}

		}
	}
}
