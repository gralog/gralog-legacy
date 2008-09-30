/*
 * Created on 2008 by Sebastian Ordyniak
 *
 * Copyright 2008 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of GrALoG.
 *
 * GrALoG is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * GrALoG is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GrALoG; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */
package de.hu.gralog.structure.support;

import org.jgrapht.ListenableGraph;
import org.jgrapht.VertexFactory;

import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;
import de.hu.gralog.structure.GraphFactory;
import de.hu.gralog.structure.StructureBeanFactory;
import de.hu.gralog.structure.StructureTypeInfo;

/**
 * This class is used by {@link de.hu.gralog.structure.Structure} as an
 * Adapter to {@link de.hu.gralog.structure.StructureTypeInfo}. It is of no use
 * to Plugin-Developers.
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
 *            the JGraphT-Type
 */
public class StructureTypeInfoSupport<V, E, GB, G extends ListenableGraph<V, E>> {

	private final StructureTypeInfo<V, E, GB, G> typeInfo;

	private final String name;

	private final GraphFactory<G> graphFactory;

	private final G graph;

	private final StructureBeanFactory<GB> graphBeanFactory;

	private GB bean;

	private final VertexFactory<V> vertexFactory;

	private final DefaultVertexRenderer vertexRenderer;

	private final DefaultEdgeRenderer edgeRenderer;

	public StructureTypeInfoSupport(
			StructureTypeInfo<V, E, GB, G> typeInfo, G graph, GB bean) {
		this.typeInfo = typeInfo;
		name = typeInfo.getName();
		graphFactory = typeInfo.getGraphFactory();
		if (graph == null)
			this.graph = graphFactory.createGraph();
		else
			this.graph = graph;
		graphBeanFactory = typeInfo.getStructureBeanFactory();
		vertexFactory = typeInfo.getVertexFactory();
		vertexRenderer = typeInfo.getVertexRenderer();
		edgeRenderer = typeInfo.getEdgeRenderer();
		this.bean = bean;
	}

	public String getName() {
		return name;
	}

	public G getGraph() {
		return graph;
	}

	public GB getStructureBean() {
		if (bean == null) {
			if (graphBeanFactory == null)
				return null;
			bean = graphBeanFactory.createBean();
		}
		return bean;
	}

	public VertexFactory<V> getVertexFactory() {
		return vertexFactory;
	}

	public DefaultVertexRenderer getVertexRenderer() {
		return vertexRenderer;
	}

	public DefaultEdgeRenderer getEdgeRenderer() {
		return edgeRenderer;
	}

	public StructureTypeInfo<V, E, GB, G> getTypeInfo() {
		return typeInfo;
	}
}
