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
package de.hu.gralog.beans.support;

import org.jgrapht.ListenableGraph;

import de.hu.gralog.structure.Structure;

/**
 * This interface should be implemented by Structure-Beans in order to get access to
 * the {@link Structure}-instance they belong to.
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
 *            the JGraphTType, which has to be a {@link ListenableGraph}
 */
public interface StructureBean<V, E, GB extends StructureBean, G extends ListenableGraph<V, E>> {
	/**
	 * This function is called by {@link Structure} during
	 * initialization.
	 * 
	 * @param structure
	 */
	public void setStructure(Structure<V, E, GB, G> structure);
}
