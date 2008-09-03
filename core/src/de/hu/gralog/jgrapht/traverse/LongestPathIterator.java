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

package de.hu.gralog.jgrapht.traverse;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;
import org.jgrapht.traverse.TopologicalOrderIterator;

/**
 * This iterator computes the length of the longest path from
 * any sourcevertex to any other vertex in a directed acyclic graph.
 * 
 * @author Sebastian
 *
 * @param <V> the vertexType
 * @param <E> the edgeType
 */
public class LongestPathIterator<V, E> extends TopologicalOrderIterator<V, E> {

	private Map<V, Double> lengthsData = new HashMap<V, Double>();

	public LongestPathIterator(DirectedGraph<V, E> graph) {
		super(graph);
		for (V vertex : graph.vertexSet()) {
			if (graph.inDegreeOf(vertex) == 0)
				lengthsData.put(vertex, new Double(0));
		}
	}

	private double calculatePathLength(V vertex, E edge) {
		if (edge == null)
			return 0;
		V fromVertex = Graphs.getOppositeVertex(getGraph(), edge, vertex);
		Double fromVertexData = lengthsData.get(fromVertex);
		return fromVertexData.doubleValue() + getGraph().getEdgeWeight(edge);
	}

	@Override
	protected void encounterVertex(V vertex, E edge) {
		super.encounterVertex(vertex, edge);
		lengthsData.put(vertex, new Double(calculatePathLength(vertex, edge)));
	}

	@Override
	protected void encounterVertexAgain(V vertex, E edge) {
		super.encounterVertexAgain(vertex, edge);
		Double vertexData = lengthsData.get(vertex);
		double newPathLength = calculatePathLength(vertex, edge);
		if (vertexData.doubleValue() < newPathLength)
			lengthsData.put(vertex, new Double(newPathLength));
	}

	public double getLongestPathLength(V vertex) {
		return lengthsData.get(vertex).doubleValue();
	}
}
