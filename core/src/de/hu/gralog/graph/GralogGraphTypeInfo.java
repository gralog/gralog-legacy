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

import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.VertexFactory;

import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;
import de.hu.gralog.jgrapht.util.JGraphTUtils;

/**
 * Each subclass of this class defines a Gralog-Graph-Type. This class basically
 * holds all information necessary to construct a Gralog-Graph ( in fact you can
 * use subclasses of this type to create a Gralog-Graph by using
 * {@link GralogGraphFactory#createGraphSupport(GralogGraphTypeInfo)} ), as
 * return values to its abstract functions.
 * 
 * <h1>Plugin-Developers</h1>
 * 
 * Every Gralog-Graph-Type is defined via a subclass of this class. Thus in
 * order to define your own Gralog-Type you have to extend this class and
 * provide the information necessary to construct the desired
 * {@link GralogGraphSupport} via implementing the abstract methods of this
 * class. You can then use
 * {@link GralogGraphFactory#createGraphSupport(GralogGraphTypeInfo)} to create
 * instances of your new Gralog-Graph-Type, or just provide this new Graph-Type
 * to Gralog by mentioning it in your <b>plugin.config</b>.
 * 
 * See {@link de.hu.gralog.graph.types.LabeledSimpleDirectedGraphTypeInfo} and
 * {@link de.hu.gralog.graph.types.LabeledSimpleUndirectedGraphTypeInfo} for
 * examples of already defined Gralog-Graph-Types.
 * 
 * @author Sebastian
 * 
 * @param <V>
 *            the vertexType
 * @param <E>
 *            the edgeType
 * @param <GB>
 *            the GraphBeanType
 * @param <G>
 *            the JGraphT-Graph-Type, which has to be a
 *            {@link org.jgrapht.graph.ListenableGraph}
 */
public abstract class GralogGraphTypeInfo<V, E, GB, G extends ListenableGraph<V, E>>
		implements GralogGraphTypeInfoFilter {

	/**
	 * 
	 * 
	 * @return the name of this Gralog-Graph-Type, that is displayed to the
	 *         user, when he wants to construct a new Gralog-Graph of this type.
	 */
	public abstract String getName();

	/**
	 * Please refer to the description of {@link GraphFactory} if you want to
	 * define your own Graph-Types in Gralog.
	 * 
	 * 
	 * @return the {@link GraphFactory} used by the Gralog-Graph to create an
	 *         instance of the underlying JGraphT-Graph. The function is not
	 *         allowed to return null.
	 */
	public abstract GraphFactory<G> getGraphFactory();

	/**
	 * Please refer to the description of {@link GraphBeanFactory} if you want
	 * to define your own type of GraphBeans for Gralog.
	 * 
	 * @return the {@link GraphBeanFactory} used by the Gralog-Graph to create
	 *         an instance of the underlying GraphBean. If your graph does not
	 *         have a GraphBean, this function has to return null.
	 */
	public abstract GraphBeanFactory<GB> getGraphBeanFactory();

	/**
	 * 
	 * Please refer to the description of
	 * {@link de.hu.gralog.graph.types.elements} if you want to define your own
	 * type of vertices and edges in Gralog.
	 * 
	 * @return the {@link VertexFactory} used by the Gralog-Graph to create its
	 *         vertices. This function is not allowed to return null.
	 */
	public abstract VertexFactory<V> getVertexFactory();

	/**
	 * 
	 * Please refer to {@link de.hu.gralog.jgraph.cellview} if you want to
	 * define your own VertexRenderer for Gralog.
	 * 
	 * @return {@link DefaultVertexRenderer} used by JGraph to render the
	 *         vertices of this graph. If this function returns null, an
	 *         instance of {@link DefaultVertexRenderer} is used to render the
	 *         vertices.
	 */
	public abstract DefaultVertexRenderer getVertexRenderer();

	/**
	 * 
	 * Please refer to {@link de.hu.gralog.jgraph.cellview} if you want to
	 * define your own EdgeRenderer for Gralog.
	 * 
	 * @return {@link DefaultEdgeRenderer} used by JGraph to render the edges of
	 *         this graph. If this function returns null, an instance of
	 *         {@link DefaultEdgeRenderer} is used to render the edges.
	 */
	public abstract DefaultEdgeRenderer getEdgeRenderer();

	/**
	 * This function is used by Gralog to infer whether the given
	 * {@link GralogGraphTypeInfo} is compatible with this
	 * {@link GralogGraphTypeInfo}. Gralog uses this function to determine
	 * whether or not the user is allowed to copy and paste parts of
	 * Gralog-Graphs that are defined by this {@link GralogGraphTypeInfo}-instance
	 * to / from Gralog-Graphs that are defined by the given
	 * {@link GralogGraphTypeInfo}.
	 * 
	 * Normally Plugin-Developers should not override this function. The given
	 * implementation checks for compatibility of:
	 * 
	 * <ul>
	 * <li>the JGraphT-Graphs</li>
	 * <li>the Graph-Bean</li>
	 * <li>the vertices</li>
	 * <li>the edges</li>
	 * <ul>
	 * 
	 * @param typeInfo
	 */
	public boolean filterTypeInfo(GralogGraphTypeInfo typeInfo) {
		// test jGraphTs for compatibility
		Object o1 = getGraphFactory().createGraph();
		Object o2 = typeInfo.getGraphFactory().createGraph();

		if (!o1.getClass().isInstance(o2))
			return true;

		// test underlying delegates for compatibility
		if (!(JGraphTUtils.getListenableBaseGraph((ListenableGraph) o1)
				.getClass().isInstance(JGraphTUtils
				.getListenableBaseGraph((ListenableGraph) o2))))
			return true;

		// test jGraphTs-edges for compatibility
		o1 = ((Graph) o1).getEdgeFactory().createEdge(
				getVertexFactory().createVertex(),
				getVertexFactory().createVertex());
		o2 = ((Graph) o2).getEdgeFactory().createEdge(
				typeInfo.getVertexFactory().createVertex(),
				typeInfo.getVertexFactory().createVertex());

		if (!(o1.getClass().isInstance(o2)))
			return true;

		// test jGraphTs-vertices for compatibility
		o1 = getVertexFactory().createVertex();
		o2 = typeInfo.getVertexFactory().createVertex();
		if (!(o1.getClass().isInstance(o2)))
			return true;

		// test graph-beans for compatibility
		if (getGraphBeanFactory() == null)
			return false;
		if (typeInfo.getGraphBeanFactory() == null)
			return true;

		o1 = getGraphBeanFactory().createBean();
		o2 = typeInfo.getGraphBeanFactory().createBean();
		if (!(o1.getClass().isInstance(o2)))
			return true;
		return false;
	}
}
