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
package de.hu.gralog.beans.propertydescriptor;

import de.hu.gralog.graph.GralogGraphSupport;

/**
 * This interface allows to define filters for elements of a
 * {@link GralogGraphSupport}. It contains to default filters: One that filters
 * all vertices and another to filter all edges.
 * 
 * @author Sebastian
 * 
 */
public interface GraphElementFilter {
	/**
	 * 
	 * 
	 * @param graphSupport
	 * @param element
	 * @return true if the element should be filtered out
	 */
	public boolean filterElement(GralogGraphSupport graphSupport, Object element);

	/**
	 * A DefaultFilter that filters all vertices in the graph.
	 * 
	 * @author Sebastian
	 * 
	 */
	public static class VertexGraphElementFilter implements GraphElementFilter {

		public boolean filterElement(GralogGraphSupport graphSupport,
				Object element) {
			if (graphSupport.isVertex(element))
				return false;
			return true;
		}

	}

	/**
	 * A DefaultFilter that filters all edges in the graph.
	 * 
	 * @author Sebastian
	 * 
	 */
	public static class EdgeGraphElementFilter implements GraphElementFilter {

		public boolean filterElement(GralogGraphSupport graphSupport,
				Object element) {
			if (graphSupport.isEdge(element))
				return false;
			return true;
		}

	}

}
