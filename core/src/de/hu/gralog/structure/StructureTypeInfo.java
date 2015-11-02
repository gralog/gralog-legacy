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
package de.hu.gralog.structure;

import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.VertexFactory;

import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;
import de.hu.gralog.jgrapht.util.JGraphTUtils;

/**
 * Each subclass of this class defines a GrALoG-Structure-Type. 
 * This class basically
 * holds all the information that is necessary to construct a GrALoG-Structure 
 * ( in fact you can
 * use subclasses of this type to create a GrALoG-Structure by using
 * {@link StructureFactory#createStructure(StructureTypeInfo)} ), as
 * return values to its abstract functions.
 * 
 * <h1>Plugin-Developers</h1>
 * 
 * Every GrALoG-Structure-Type is defined via a subclass of this class. Thus in
 * order to define your own GrALoG-Structure-Type you have to extend this class and
 * provide the information necessary to construct the desired
 * {@link Structure} by implementing the abstract methods of this
 * class. You can then use
 * {@link StructureFactory#createStructure(StructureTypeInfo)} to create
 * instances of your new GrALoG-Structure, or just provide this new Structure-Type
 * to GrALoG by mentioning it in your <b>plugin.config</b>.
 * 
 * See {@link de.hu.gralog.structure.types.LabeledSimpleDirectedGraphStructureTypeInfo} and
 * {@link de.hu.gralog.structure.types.LabeledSimpleUndirectedGraphStructureTypeInfo} for
 * examples of already defined GrALoG-Structures.
 * 
 * @author Sebastian
 * 
 * @param <V>
 *            the vertexType
 * @param <E>
 *            the edgeType
 * @param <GB>
 *            the StructureBeanType
 * @param <G>
 *            the JGraphT-Graph-Type, which has to be a
 *            {@link org.jgrapht.graph.ListenableGraph}
 */
public abstract class StructureTypeInfo<V, E, GB, G extends ListenableGraph<V, E>>
		implements StructureTypeInfoFilter {

	/**
	 * 
	 * 
	 * @return the name of this GrALoG-Structure, that is displayed to the
	 *         user.
	 */
	public abstract String getName();

	/**
	 * 
	 * 
	 * @return the description of this GrALoG-Structure, that is displayed to the
	 *         user. This can be an arbitrary html-String.
	 */
	public abstract String getDescription();
	
	/**
	 * Please refer to the description of {@link GraphFactory} if you want to
	 * define your own Graph-Types in GrALoG.
	 * 
	 * 
	 * @return the {@link GraphFactory} used by the GrALoG-Structure to create an
	 *         instance of the associated JGraphT-Graph. The function is not
	 *         allowed to return null.
	 */
	public abstract GraphFactory<G> getGraphFactory();

	/**
	 * Please refer to the description of {@link StructureBeanFactory}, if you want
	 * to define your own Structure-Bean.
	 * 
	 * @return the {@link StructureBeanFactory} used by the GrALoG-Structure to create
	 *         an instance of the underlying Structure-Bean. If your structure does not
	 *         have a Structure-Bean, this function has to return null.
	 */
	public StructureBeanFactory<GB> getStructureBeanFactory()
        {
            return null;
        }

	/**
	 * 
	 * Please refer to the description of
	 * {@link de.hu.gralog.structure.types.elements} if you want to define your own
	 * type of vertices and edges in GrALoG.
	 * 
	 * @return the {@link VertexFactory} used by the GrALoG-Structure to create it's
	 *         vertices. This function is not allowed to return null.
	 */
	public abstract VertexFactory<V> getVertexFactory();

	/**
	 * 
	 * Please refer to {@link de.hu.gralog.jgraph.cellview} if you want to
	 * define your own VertexRenderer for GrALoG.
	 * 
	 * @return {@link DefaultVertexRenderer} used by JGraph to render the
	 *         vertices of this structure. If this function returns null, an
	 *         instance of {@link DefaultVertexRenderer} is used to render the
	 *         vertices.
	 */
	public DefaultVertexRenderer getVertexRenderer()
        {
            return null;
        }

	/**
	 * 
	 * Please refer to {@link de.hu.gralog.jgraph.cellview} if you want to
	 * define your own EdgeRenderer for GrALoG.
	 * 
	 * @return {@link DefaultEdgeRenderer} used by JGraph to render the edges of
	 *         this structure. If this function returns null, an instance of
	 *         {@link DefaultEdgeRenderer} is used to render the edges.
	 */
	public DefaultEdgeRenderer getEdgeRenderer()
        {
            return null;
        }

	/**
	 * This function is used by GrALoG to infer whether the given
	 * {@link StructureTypeInfo} is compatible with this
	 * {@link StructureTypeInfo}. GrALoG uses this function to determine
	 * whether or not the user is allowed to copy and paste parts of
	 * GrALoG-Structures that are defined by this {@link StructureTypeInfo}-instance
	 * to / from GrALoG-Structures that are defined by the given
	 * {@link StructureTypeInfo}.
	 * 
	 * Normally Plugin-Developers should not override this function. The given
	 * implementation checks for compatibility of:
	 * 
	 * <ul>
	 * <li>the JGraphT-Graph</li>
	 * <li>the Structure-Bean</li>
	 * <li>the vertices</li>
	 * <li>the edges</li>
	 * <ul>
	 * 
	 * @param typeInfo
	 */
	public boolean filterTypeInfo(StructureTypeInfo typeInfo) {
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
		if (getStructureBeanFactory() == null)
			return false;
		if (typeInfo.getStructureBeanFactory() == null)
			return true;

		o1 = getStructureBeanFactory().createBean();
		o2 = typeInfo.getStructureBeanFactory().createBean();
		if (!(o1.getClass().isInstance(o2)))
			return true;
		return false;
	}
}
