/*
 * Created on 7 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.graph;

import de.hu.gralog.jgraph.GJGraph;
import de.hu.gralog.graph.io.XMLDecoderIO;
/**
 * A GraphTypeInfo acts as a factory for graphs defined in GrALog. 
 * If you want to define your own graph type you have to extend this class.
 * 
 * @author ordyniak
 *
 */

public abstract class GraphTypeInfo {
	/**
	 * This name is displayed in GrALoG when refering to this type of graph.
	 * 
	 * @return the name of your graph type
	 */
	public abstract String getName();
	
	/**
	 * This is the factory-method needed to construct instances of this graph type.
	 * 
	 * If you want to define your own graph type the implementation should look like this:
	 * <p>
	 * <pre>
	 * 	public GraphWithEditableElements newInstance() {
	 * 		MyGraphClass<MyVertexClass, MyEdgeClass> graph = new MyGraphClass<MyVertexClass, MyEdgeClass>();
	 * 		graph.setTypeInfo( this );
	 * 		return graph;
	 * }
	 * </pre>
	 * 
	 * @return an instance of the graph corresponding to that graphTypeInfo
	 */
	public abstract GraphWithEditableElements newInstance();
	
	/**
	 * The function tests if graph is an instance of the type of graph defined by this GraphTypeInfo.
	 * In order to determine this relation the function compares graphClasses, vertexClasses and edgeClasses
	 * if their are instances of each other. The function returns true if this is
	 * the case for all of them 
	 * 
	 * @param graph 
	 * @return returns true if graph is an instance of the type of graph defined by this GraphTypeInfo
	 */
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
	
	public GJGraph copyGraph( GJGraph graph ) {
		return new XMLDecoderIO().getDataCopy( graph );
	}
}
