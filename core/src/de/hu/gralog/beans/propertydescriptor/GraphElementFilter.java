package de.hu.gralog.beans.propertydescriptor;

import de.hu.gralog.graph.GralogGraphSupport;

/**
 * This interface allows to define filters for elements of a {@link GralogGraphSupport}.
 * It contains to default filters: One that filters all vertices and
 * another to filter all edges. 
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
	 * @return, if true the element will be filtered out
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
