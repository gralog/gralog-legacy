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

package de.hu.dagwidth.alg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;

import de.hu.games.graph.DirectedGraph;
import de.hu.games.graph.LabeledGraphVertex;
import de.hu.games.graph.alg.Algorithms;
import de.hu.games.jgrapht.traverse.VertexFilter;
import de.hu.graphgames.graph.GameGraphVertex;

public class CopsAndRobberAlgorithm<V extends LabeledGraphVertex, E extends DefaultEdge> {

	private DirectedGraph<V,E> graph;
	private int width;
	private boolean copMonotone;
	private boolean robberMonotone;
	private CopsAndRobberVertexFactory<V> vertexFactory = new CopsAndRobberVertexFactory<V>();
	private Object cacheComponent = new Object();
	
	private DirectedGraph<CopsAndRobberVertex<V>, DefaultEdge> gameGraph;
	
	public CopsAndRobberAlgorithm( DirectedGraph<V,E> graph, int width, boolean copMonotone, boolean robberMonotone ) {
		this.graph = graph;
		this.copMonotone = copMonotone;
		this.robberMonotone = robberMonotone;
		this.width = width;
	}
	
	public static <V extends LabeledGraphVertex,E extends DefaultEdge> DirectedGraph<CopsAndRobberVertex<V>, DefaultEdge> getCopsAndRobberGameGraph( DirectedGraph<V,E> graph, int width, boolean copMonotone, boolean robberMonotone ) {
		return new CopsAndRobberAlgorithm<V,E>( graph, width, copMonotone, robberMonotone ).getCopsAndRobberGameGraph();
	}

	public static <V extends LabeledGraphVertex,E extends DefaultEdge> Iterator<DirectedGraph<CopsAndRobberVertex<V>, DefaultEdge>> getCopsAndRobberGameGraphs( DirectedGraph<V,E> graph, int width, boolean copMonotone, boolean robberMonotone ) {
		return new CopsAndRobberAlgorithm<V,E>( graph, width, copMonotone, robberMonotone ).iterator();
	}
	
	public DirectedGraph<CopsAndRobberVertex<V>, DefaultEdge> getCopsAndRobberGameGraph( ) {
		gameGraph = new DirectedGraph<CopsAndRobberVertex<V>, DefaultEdge>( (Class< ? extends CopsAndRobberVertex<V>>)CopsAndRobberVertex.class );
		Combinations<V> combinations = new Combinations<V>( width, graph.vertexSet() );
		while ( combinations.hasNext() ) {
			Set<V> combination = combinations.next();
			start( combination );
		}
		return gameGraph;
	}
	
	protected void start( Set<V> combination ) {
		CopsAndRobberVertex dummy = new CopsAndRobberVertex( combination );
		gameGraph.addVertex( dummy );
		if ( copMonotone )
			cacheComponent = dummy;
		moveRobber( dummy );
	}
	
	public void moveRobber( CopsAndRobberVertex<V> v0 ) {
		Iterator<V> vertexes;
		if ( v0.isDummyVertex() )
			vertexes = graph.vertexSet().iterator();
		else
			vertexes = Algorithms.reach( graph, v0.getRobber(), false, new CopsAndRobberVertexFilter<V>( v0.getX(), v0.getRemoved() ) ).iterator();
		while ( vertexes.hasNext() ) {
			V robber = vertexes.next();
			if ( v0.isDummyVertex() ) {
				if ( v0.getX().contains( robber ) )
					continue;
			} else {
				if ( v0.getX2().contains( robber ) )
					continue;
			}
			CopsAndRobberVertex<V> v1;
			if ( v0.isDummyVertex() )
				v1 = vertexFactory.getVertex( v0.getX(), robber, cacheComponent );
			else
				v1 = vertexFactory.getVertex( v0.getX2(), robber, cacheComponent );
			boolean visited = gameGraph.vertexSet().contains( v1 );
			if ( ! visited )
				gameGraph.addVertex( v1 );
			gameGraph.addEdge( v0, v1 );
			if ( !visited )
				moveCops( v1 );
		}
	}
	
	public void moveCops( CopsAndRobberVertex<V> v0 ) {
		Iterator<V> removeIt = v0.getX().iterator();
		while ( removeIt.hasNext() ) {
			V remove = removeIt.next();
			Iterator<V> addIt = graph.vertexSet().iterator();
			while ( addIt.hasNext() ) {
				V add = addIt.next();
				if ( v0.getX().contains( add ) )
					continue;
				if ( copMonotone ) {
					Iterator<CopsAndRobberVertex<V>> ancestors = Algorithms.reach( gameGraph, v0, true, null ).iterator();
					boolean monotone = true;
					while ( ancestors.hasNext() ) {
						CopsAndRobberVertex<V> v = ancestors.next();
						if ( v.getRemoved() == add )
							monotone = false;
					}
					if ( ! monotone )
						continue;
				}
				if ( robberMonotone ) {
					Set<V> reachBefore = Algorithms.reach( graph, v0.getRobber(), false, new CopsAndRobberVertexFilter<V>( v0.getX() ) );
					Set<V> reachAfter = Algorithms.reach( graph, v0.getRobber(), false, new CopsAndRobberVertexFilter<V>( v0.getX(), remove ) );
					if ( ! reachBefore.containsAll( reachAfter ) )
						continue;
				}
				
				CopsAndRobberVertex<V> v1 = vertexFactory.getVertex( v0.getX(), v0.getRobber(), remove, add, cacheComponent );

				boolean visited = gameGraph.vertexSet().contains( v1 );
				if ( ! visited )
					gameGraph.addVertex( v1 );
				gameGraph.addEdge( v0, v1 );
				if ( ! visited )
					moveRobber( v1 );
			}
		}
	}
	
	public static class CopsAndRobberVertex<V extends LabeledGraphVertex> extends GameGraphVertex {
		private Set<V> X;
		private Set<V> X2;
		private V robber;

		private V removed;
		private V added;
		
		public CopsAndRobberVertex() {
			
		}

		public CopsAndRobberVertex( Set<V> X ) {
			super( "start", false );
			this.X = X;
		}
		
		public CopsAndRobberVertex( Set<V> X, V robber ) {
			super( "", true );
			this.X = X;
			this.robber = robber;
			setLabel( createLabel() );
		}
		
		public CopsAndRobberVertex( CopsAndRobberVertex<V> parent, V removed, V added ) {
			this( parent.getX(), parent.getRobber(), removed, added );
		}
		
		public CopsAndRobberVertex( Set<V> X, V robber , V removed, V added ) {
			super( "", false );
			this.X = X;
			this.robber = robber;
			this.removed = removed;
			this.added = added;
			setLabel( createLabel() );
		}

		public String createLabel() {
			String back = X.toString();
			if ( removed != null ) {
				back = back + ", " + removed;
				back = back + ", " + added;
			}
			back = back + " - " + robber;
			return back;
		}
		
		public Set<V> getX2() {
			if ( removed == null )
				return X;
			if ( X2 == null ) {
				X2 = new HashSet<V>( X );
				X2.remove( removed );
				X2.add( added );
			}
			return X2;
		}
		
		public boolean isDummyVertex() {
			return robber == null;
		}

		public V getAdded() {
			return added;
		}

		public V getRemoved() {
			return removed;
		}

		public V getRobber() {
			return robber;
		}

		public Set<V> getX() {
			return X;
		}

		public boolean equals( Object o ) {
			if ( this == o )
				return true;
			return false;
		}
	}
	
	public static class Combinations<V> implements Iterator<HashSet<V>> {
		private int k;
		private ArrayList<V> elements;
		private LinkedList<HashSet<V>> combinations = new LinkedList<HashSet<V>>();
		
		public Combinations( int k, Set<V> elements ) {
			this.k = k;
			this.elements = new ArrayList<V>( elements );
			fill();
		}

		
		public boolean hasNext() {
			return combinations.size() != 0;
		}

		public HashSet<V> next() {
			return combinations.removeFirst();
		}


		public void remove() {
			throw new UnsupportedOperationException();
		}

		public void fill() {
			comb(1,0, new HashSet<V>() );
		}
		
		private void comb( int j, int m, HashSet<V> combination ) {
			if ( j > elements.size() )
				combinations.add( new HashSet<V>( combination ) );
			else {
				if ( k - m < elements.size() - j + 1 ) {
					combination.remove( elements.get( j - 1 ) );
					comb( j + 1, m, combination );
				}
				if ( m < k ) {
					combination.add( elements.get( j - 1 ) );
					comb( j + 1, m + 1, combination );
				}
			}
				
		}
	}

	public static class CopsAndRobberVertexFactory<V extends LabeledGraphVertex> {
		
		Hashtable<Object, Hashtable<Object, Hashtable<Set<V>, Hashtable<V, CopsAndRobberVertex<V>>>>> components = 
			new Hashtable<Object, Hashtable<Object, Hashtable<Set<V>, Hashtable<V, CopsAndRobberVertex<V>>>>>();
		
		private static final Object COPS_VERTEX = new Object();
		private static final Object ROBBER_VERTEX = new Object();
		
		public CopsAndRobberVertexFactory(  ) {
		}
		
		public Hashtable getCache( Hashtable cache, Object component ) {
			Hashtable back = (Hashtable)cache.get( component );
			if ( back == null ) {
				back = new Hashtable();
				cache.put( component, back );
			}
			return back;
		}
		
		public CopsAndRobberVertex<V> getVertex( Set<V> X, V robber, Object component ) {
			Hashtable all = getCache( components, component );
			Hashtable vertexSets = getCache( all, COPS_VERTEX );
			Hashtable vertexes = getCache( vertexSets, X );
			CopsAndRobberVertex<V> v = (CopsAndRobberVertex<V>)vertexes.get( robber );
			if ( v == null ) {
				v = new CopsAndRobberVertex<V>( X, robber );
				vertexes.put( robber, v );
			}
			return v;
		}
		
		public CopsAndRobberVertex<V> getVertex( Set<V> X, V robber, V remove, V add, Object component ) {
			Hashtable all = getCache( components, component );
			Hashtable vertexSets = getCache( all, ROBBER_VERTEX );
			Hashtable vertexRobber = getCache( vertexSets, X );
			Hashtable vertexRemoved = getCache( vertexRobber, robber );
			Hashtable vertexes = getCache( vertexRemoved, remove );
			
			CopsAndRobberVertex v = (CopsAndRobberVertex)vertexes.get( add );
			if ( v == null ) {
				v = new CopsAndRobberVertex<V>( X, robber, remove, add );

				vertexes.put( add, v );
			}
			return v;
		}
		
	}
	
	public static class  CopsAndRobberVertexFilter<V> implements VertexFilter<V> {

		private Set<V> X;
		private V removed;
		
		public CopsAndRobberVertexFilter( Set<V> X ) {
			this.X = X;
		}
		
		public CopsAndRobberVertexFilter( Set<V> X, V remove ) {
			this( X );
			this.removed = remove;
		}
		
		public boolean filterVertex(V vertex) {
			if ( X.contains( vertex ) && vertex != removed )
				return true;
			return false;
		}
		
	}
	
	public Iterator<DirectedGraph<CopsAndRobberVertex<V>, DefaultEdge>> iterator() {
		return new CombinationIterator();
	}
	
	public class CombinationIterator implements Iterator<DirectedGraph<CopsAndRobberVertex<V>, DefaultEdge>> {
		
		Combinations<V> combinations;
		
		public CombinationIterator() {
			combinations = new Combinations<V>( width, graph.vertexSet() );
		}

		public boolean hasNext() {
			return combinations.hasNext();
		}

		public DirectedGraph<CopsAndRobberVertex<V>, DefaultEdge> next() {
			Set<V> combination = combinations.next();
			gameGraph = new DirectedGraph<CopsAndRobberVertex<V>, DefaultEdge>( (Class<? extends CopsAndRobberVertex<V>>)CopsAndRobberVertex.class );
			start( combination );
			return gameGraph;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
		
	}
	
	public static class DummyVertexFilter<V extends CopsAndRobberVertex> implements VertexFilter<V> {

		public boolean filterVertex(V vertex) {
			if ( vertex.isDummyVertex() )
				return false;
			return true;
		}
		
	}
}
