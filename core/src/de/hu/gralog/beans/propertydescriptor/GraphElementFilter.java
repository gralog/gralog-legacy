package de.hu.gralog.beans.propertydescriptor;

import de.hu.gralog.graph.GralogGraph;

public interface GraphElementFilter {
	public boolean filterElement( GralogGraph graph, Object element );
	
	public static class VertexGraphElementFilter implements GraphElementFilter {

		public boolean filterElement(GralogGraph graph, Object element) {
			if ( graph.containsVertex( element ) )
				return false;
			return true;
		}

	}
	
	public static class EdgeGraphElementFilter implements GraphElementFilter {

		public boolean filterElement(GralogGraph graph, Object element) {
			if ( graph.containsEdge( element ) )
				return false;
			return true;
		}

	}

}
