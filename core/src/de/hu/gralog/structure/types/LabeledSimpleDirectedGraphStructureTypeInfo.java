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
package de.hu.gralog.structure.types;

import org.jgrapht.VertexFactory;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.jgrapht.graph.SimpleDirectedGraph;

import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;
import de.hu.gralog.structure.GraphFactory;
import de.hu.gralog.structure.StructureBeanFactory;
import de.hu.gralog.structure.StructureTypeInfo;
import de.hu.gralog.structure.types.elements.LabeledStructureVertex;

/**
 * This class defines the <b>LabeledSimpleDirectedGraphStructure</b> Structure-Type.
 * This Structure-Type has:
 * 
 * <ul>
 * <li>A {@link de.hu.gralog.structure.types.elements.LabeledStructureVertex} as a
 * <b>vertexType</b>.<br>
 * 
 * {@link #getVertexFactory()} 
 * </li>
 * <li>
 * 	{@link org.jgrapht.structure.DefaultEdge} as <b>edgeType</b><br>
 * 	{@link #getGraphFactory()} 
 * </li>
 * <li>{@link org.jgrapht.graph.SimpleDirectedGraph} as <b>JGraphT-Type</b><br>
 *      Note that since this is not a {@link org.jgrapht.structure.ListenableGraph}
 *      it has to be wrapped in a
 *      {@link org.jgrapht.structure.ListenableDirectedGraph} in order to make it
 *      usable for GrALoG. See {@link #getGraphFactory()} for how this is
 *      accomplished here. 
 * </li>
 * <li> no support for a Structure-Bean.<br>
 * {@link #getStructureBeanFactory()} 
 * </li>
 * </ul>
 * 
 * @author Sebastian
 * 
 * @see StructureTypeInfo
 * 
 * @param <GB>
 *            the GraphBeanType
 */
public class LabeledSimpleDirectedGraphStructureTypeInfo<GB>
		extends
		StructureTypeInfo<LabeledStructureVertex, DefaultEdge, GB, ListenableDirectedGraph<LabeledStructureVertex, DefaultEdge>> {

	public String getName() {
		return "LabeledSimpleDirectedGraphStructure";
	}

	public String getDescription() {
		return 
			"<html>" +
			"This structure represents a " +
			"simple directed graph, " + 
			"i.e. a directed graph with no loops and " +
			"multiple edges, " +
			"whose vertices have a label." +
			"</html>";
	}
	
	public GraphFactory<ListenableDirectedGraph<LabeledStructureVertex, DefaultEdge>> getGraphFactory() {
		return new GraphFactory<ListenableDirectedGraph<LabeledStructureVertex, DefaultEdge>>() {
			public ListenableDirectedGraph<LabeledStructureVertex, DefaultEdge> createGraph() {
				return new ListenableDirectedGraph<LabeledStructureVertex, DefaultEdge>(
						new SimpleDirectedGraph<LabeledStructureVertex, DefaultEdge>(
								DefaultEdge.class));
			}
		};
	}


	public VertexFactory<LabeledStructureVertex> getVertexFactory() {
		return new VertexFactory<LabeledStructureVertex>() {
			public LabeledStructureVertex createVertex() {
				return new LabeledStructureVertex();
			}
		};
	}
}
