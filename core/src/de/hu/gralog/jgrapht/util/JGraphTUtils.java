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
package de.hu.gralog.jgrapht.util;

import java.lang.reflect.Field;

import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.GraphDelegator;

/**
 * This class provides utility methods to use JGraphT-graphs.
 * 
 * @author Sebastian
 * 
 */
public class JGraphTUtils {

	/**
	 * This method returns the JGraphT-Graph that is wrapped by a
	 * {@link ListenableGraph}. It can be useful for Plugin-Developers in order
	 * to infer the reall JGraphT-Type of this Graph, i.e. wether the Graph is
	 * Simple, Multi, Pseudo etc. .
	 * 
	 * @param <V>
	 *            the vertexType
	 * @param <E>
	 *            the edgeType
	 * @param <G>
	 *            the JGraphT-Type
	 * @param graph
	 * @return the basegraph
	 */
	public static <V, E, G extends Graph<V, E>> G getListenableBaseGraph(
			ListenableGraph<V, E> graph) {
		if (!(graph instanceof GraphDelegator))
			return null;
		return (G)getDelegate((GraphDelegator) graph);
	}

	protected static <V, E, G extends Graph<V, E>> G getDelegate(
			GraphDelegator<V, E> delegator) {
		try {
			Field delegateField = GraphDelegator.class
					.getDeclaredField("delegate");
			delegateField.setAccessible(true);

			return (G) delegateField.get(delegator);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}
}
