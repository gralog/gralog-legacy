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

import org.jgrapht.ListenableGraph;

import de.hu.gralog.app.UserException;
import de.hu.gralog.structure.support.StructureTypeInfoSupport;

/**
 * This class acts as a factory to construct GrALoG-Structures ({@link Structure}).
 * 
 * @author Sebastian
 * 
 */
public class StructureFactory {

	/*
	 * constructs a {@link StructureTypeInfoSupport} that can be seen as an
	 * Adapter to {@link StructureTypeInfo}
	 * 
	 */
	private static <V, E, GB, G extends ListenableGraph<V, E>> StructureTypeInfoSupport<V, E, GB, G> createTypeInfoSupport(
			StructureTypeInfo<V, E, GB, G> typeInfo, G graph, GB bean) {
		return new StructureTypeInfoSupport<V, E, GB, G>(typeInfo, graph,
				bean);
	}

	/**
	 * This function creates a GrALoG-Structure ({@link Structure} from a
	 * {@link StructureTypeInfo}-object.
	 * 
	 * @param <V>
	 *            the vertexType
	 * @param <E>
	 *            the edgeType
	 * @param <GB>
	 *            the GraphBeanType
	 * @param <G>
	 *            the JGraphT-Graph that has to implement
	 *            {@link org.jgrapht.graph.ListenableGraph}
	 * @param typeInfo
	 *            the {@link StructureTypeInfo}-instance that defines all
	 *            properties needed to construct the GrALoG-Graph
	 * @return the GrALoG-Structure
	 * @throws UserException
	 */
	public static <V, E, GB, G extends ListenableGraph<V, E>> Structure<V,E,GB,G> createStructure(
			StructureTypeInfo<V, E, GB, G> typeInfo) throws UserException {
		return createStructure(typeInfo, null);
	}

	/**
	 * This function creates a GrALoG-Structure ({@link Structure} from a
	 * {@link StructureTypeInfo}-object and a given JGraphT-Graph.
	 * 
	 * @param <V>
	 *            the vertexType
	 * @param <E>
	 *            the edgeType
	 * @param <GB>
	 *            the StructureBeanType
	 * @param <G>
	 *            the JGraphT-Graph that has to implement
	 *            {@link org.jgrapht.graph.ListenableGraph}
	 * @param typeInfo
	 *            the {@link StructureTypeInfo}-instance that defines all
	 *            properties needed to construct the GrALoG-Graph
	 * @param graph
	 *            the JGraphT-Graph that is used to initialize the GrALoG-Struture
	 * @return the GrALoG-Struture
	 * @throws UserException
	 */
	public static <V, E, GB, G extends ListenableGraph<V, E>> Structure<V, E, GB, G> createStructure(
			StructureTypeInfo<V, E, GB, G> typeInfo, G graph)
			throws UserException {
		return createStructure(typeInfo, graph, null);
	}

	/**
	 * This function creates a GrALoG-Struture ({@link Structure} from a
	 * {@link StructureTypeInfo}-object, a given JGraphT-Graph and a
	 * Struture-Bean.
	 * 
	 * @param <V>
	 *            the vertexType
	 * @param <E>
	 *            the edgeType
	 * @param <GB>
	 *            the Struture-Bean-Type
	 * @param <G>
	 *            the JGraphT-Graph that has to implement
	 *            {@link org.jgrapht.graph.ListenableGraph}
	 * @param typeInfo
	 *            the {@link StructureTypeInfo}-instance that defines all
	 *            properties needed to construct the GrALoG-Struture
	 * @param graph
	 *            the JGraphT-Graph that is used to initialize the GrALoG-Struture
	 * @param bean
	 *            the bean that is used to initialize the GrALoG-Struture
	 * 
	 * @return the GrALoG-Struture
	 * @throws UserException
	 */
	public static <V, E, GB, G extends ListenableGraph<V, E>> Structure<V, E, GB, G> createStructure(
			StructureTypeInfo<V, E, GB, G> typeInfo, G graph, GB bean)
			throws UserException {
		StructureTypeInfoSupport<V, E, GB, G> typeInfoSupport = createTypeInfoSupport(
				typeInfo, graph, bean);
		Structure<V, E, GB, G> gralogGraphSupport = new Structure<V, E, GB, G>(
				typeInfoSupport);
		return gralogGraphSupport;
	}

}
