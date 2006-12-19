/*
 * Created on 7 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.graph;

public abstract class GraphTypeInfo {
	public abstract String getName();
	public abstract GraphWithEditableElements newInstance();
	
	public boolean isInstance( GraphWithEditableElements graph ) {
		GraphWithEditableElements thisGraph = newInstance();

		if ( ! thisGraph.getClass().isInstance( graph ) )
			return false;
		Object v1 = graph.createVertex();
		Object v2 = thisGraph.createVertex();
			
		Object e1 = graph.getEdgeFactory().createEdge( graph.createVertex(), graph.createVertex() );
		Object e2 = thisGraph.getEdgeFactory().createEdge( thisGraph.createVertex(), thisGraph.createVertex() );
			
		if ( v2.getClass().isInstance( v1 ) && e2.getClass().isInstance( e1 ) )
			return true;
		return false;
	}
}
