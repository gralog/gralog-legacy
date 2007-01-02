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

package de.hu.parity.alg;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Subgraph;

import de.hu.gralog.graph.DirectedGraph;
import de.hu.gralog.jgrapht.graph.GraphUtils;
import de.hu.gralog.jgrapht.graph.SubgraphFactory;
import de.hu.graphgames.graph.GameGraphVertex;

public class Strategy<V extends GameGraphVertex,E extends DefaultEdge> {

	protected DirectedGraph<V,E> graph;
	protected Set<V> domain;
	protected Hashtable<V,V> function = new Hashtable<V,V>();
	
	public Strategy( DirectedGraph<V,E> graph, Set<V> domain ) {
		this.graph = graph;
		this.domain = domain;
	}
	
	public Strategy( DirectedGraph<V,E> graph, boolean player0 ) {
		this( graph, GraphUtils.filterVertexes( graph.vertexSet(), new PlayerVertexFilter( player0 ) ) );
	}
	
	public Strategy( Strategy strategy ) {
		this( strategy.graph, strategy.domain );
		function = (Hashtable<V,V>)strategy.function.clone();
	}
	
	public Set getDomain() {
		return domain;
	}
	
	public void setValue( V x, V y) {
		if ( ! domain.contains( x ) )
			throw new IllegalArgumentException( "x is not in the given strategydomain" );
		if ( y != null && ! graph.containsEdge( x, y ) )
			throw new IllegalArgumentException( "there is no corresponding edge for that strategy" );
		if ( y == null )
			function.remove( x );
		else
			function.put( x, y );
	}
	
	public V getValue( V x ) {
		return function.get( x );
	}
	
	public Subgraph<V,E> getSubgraph() {
		Subgraph<V,E> subgraph = SubgraphFactory.createSubgraph( graph, null, new HashSet() );
		
		Iterator<V> it = domain.iterator();
		while ( it.hasNext() ) {
			V source = it.next();
			V target = getValue( source );
			subgraph.addEdge( source, target );
		}
		
		return subgraph;
	}
	
	public Subgraph<V,E> getSubgraphWithConcurrentEdges() {
		Subgraph<V,E> subgraph = getSubgraph();
		
		Iterator<V> it = graph.vertexSet().iterator();
		while ( it.hasNext() ) {
			V vertex = it.next();
			if ( ! domain.contains( vertex ) )
				Graphs.addAllEdges( subgraph, graph, graph.outgoingEdgesOf( vertex ) );
		}
		
		return subgraph;
	}
	
	public void initRandom() {
		function.clear();
		
		Iterator<V> domainIt = domain.iterator();
		while ( domainIt.hasNext() ) {
			V x = domainIt.next();
			List<V> vertexes = Graphs.successorListOf( graph, x );
			if ( vertexes.isEmpty() )
				break;
			int e = (int)(Math.random() * (vertexes.size() ));
			setValue( x, vertexes.get( e ) );
		}
	}
	
	public boolean equals( Object o ) {
		if ( o == null )
			return false;
		if ( ! (o instanceof Strategy) )
			return false;
		Strategy s = (Strategy)o;
		if ( s.graph == graph && s.domain.equals( domain ) && s.function.equals( function ) )
			return true;
		return false;
	}
}
