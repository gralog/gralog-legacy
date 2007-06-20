/*
 * Created on 7 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.graph;

import java.util.HashMap;

/**
 * A GraphTypeInfo acts as a factory for graphs defined in GrALog. 
 * If you want to define your own graph type you have to extend this class.
 * Notice that you should overwrite the copyGraph function in order to avoid very long execution periods.
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
	
	/**
	 * You have to overwrite this function in order to avoid very long execution periods. 
	 * 
	 * The function should create a copy of grapht and return it. It has to store a mapping from the old
	 * vertices to the new ones in the passed HashMap vMap. Take care that this mapping and the copied
	 * graph are propperly formed. If they are not, gralog will automatically use the default (VERY slow)
	 * function and state this on the console.
	 * 
	 * Here is an example for a proper copyGraph function:
	 * <p>
	 * <pre>
	 * public GraphWithEditableElements copyGraph( GraphWithEditableElements grapht, HashMap vMap ) {
	 * 	MyGraphClass oldGraph = (MyGraphClass) grapht;			// original GraphT
	 * 	MyGraphClass newGraph = new MyGraphClass();			// cloned GraphT
	 * 	newGraph.setTypeInfo( this );
	 * 	
	 * 	// Clone the vertices and add them to the cloned GraphT "newGraph":
	 * 	for( MyVertexClass v : oldGraph.vertexSet() ) {
	 * 		MyVertexClass v2 = new MyVertexClass( v.getLabel(), v.myVertexAttribute() );	
	 * 		vMap.put( v, v2 ); 	// "vMap" stores for each vertex "v" in oldGraph the corresponding vertex "v2" in newGraph
	 * 		newGraph.addVertex( v2 );
	 * 	}
	 * 	
	 * 	// Set your special graphproperties:
	 * 	MyVertexClass newStartVertex = (MyVertexClass) vMap.get( oldGraph.getStartVertex() );
	 * 	newGraph.setStartVertex( newStartVertex );
	 * 	
	 * 	// Clone the edges and add them to the cloned GraphT:		
	 * 	for( MyEdgeClass e : oldGraph.edgeSet() ) {
	 * 		MyEdgeClass e2 = new MyEdgeClass( e.getLabel() );
	 * 		
	 * 		newGraph.addEdge(
	 * 			(MyVertexClass) vMap.get(oldGraph.getEdgeSource(e)),
	 * 			(MyVertexClass) vMap.get(oldGraph.getEdgeTarget(e)),
	 * 			e2
	 * 		);
	 * 	}
	 * 	
	 * 	return newGraph; 
	 * }
	 * </pre>
	 * 
	 * @param grapht the graph that has to be copied
	 * @param vMap in this HashMap you have to store a mapping from the old vertices to the new ones
	 * @return a copy of grapht
	 */
	public GraphWithEditableElements copyGraph( GraphWithEditableElements grapht, HashMap vMap ) {
		return null;
	}
}
