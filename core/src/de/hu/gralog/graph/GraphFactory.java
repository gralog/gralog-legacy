/*
 * Created on 2008 by Sebastian Ordyniak
 *
 * Copyright 2008 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
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
 * You should have received a copy of the GNU General Public License along with Gralog; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */
package de.hu.gralog.graph;

import org.jgrapht.ListenableGraph;

/**
 * This interface defines a Factory of JGraphT-Graphs that can be associated to
 * Gralog-Graphs via
 * {@link de.hu.gralog.graph.GralogGraphTypeInfo#getGraphFactory()} in order to
 * store the underlying mathematical structure of your Gralog-Graph.
 * <p>
 * Note that you can use any JGraphT-Graph as long as you wrapp this graph in a
 * {@link org.jgrapht.graph.ListenableGraph}, which can be either
 * a {@link org.jgrapht.graph.ListenableDirectedGraph} or a
 * {@link org.jgrapht.graph.ListenableUndirectedGraph}.
 * 
 * <h1>Plugin-Developers</h1>
 * 
 * Here is an example implementing a GraphFactory that returns a
 * {@link org.jgrapht.graph.DirectedMultiGraph} - that is wrapped in a
 * {@link org.jgrapht.graph.ListenableDirectedGraph}, i.e. a directed graph
 * that is allowed to have multiple edges and loops:
 * 
 * <pre>
 *  		public class DirectedMultiGraphGraphFactory 
 *  				implements GraphFactory&lt;ListenableDirectedGraph&lt;LabeledGraphVertex, DefaultEdge&gt; {
 *  			public ListenableDirectedGraph&lt;LabeledGraphVertex, DefaultEdge&gt; createGraph() {
 *  				return new ListenableDirectedGraph&lt;LabeledGraphVertex,DefaultEdge&gt;( new DirectedMultiGraph&lt;LabeledGraphVertex, DefaultEgde&gt;( DefaultEdge.class ) );
 *  			}
 *  		}
 * </pre>
 * 
 * Please refer to {@link de.hu.gralog.graph.types} for further examples and to
 * <a href="http://jgrapht.sourceforge.net/">JGraphT</a> for information on
 * JGraphT.
 * 
 * @author Sebastian
 * 
 * @param <G>
 *            the graphType
 */
public interface GraphFactory<G extends ListenableGraph> {
	public G createGraph();
}
