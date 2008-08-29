package de.hu.gralog.graph;

import java.beans.IntrospectionException;
import java.beans.Introspector;

import org.jgrapht.event.GraphListener;
import org.jgrapht.event.VertexSetListener;
import org.jgrapht.graph.SimpleDirectedGraph;

import de.hu.gralog.beans.GralogGraphBean;
import de.hu.gralog.graph.GralogGraphBeanInfo.GralogGraphPersistenceDelegate;
import de.hu.gralog.jgrapht.graph.ListenableGraphSupport;

public class SimpleDirectedGralogGraph<V,E,GB extends GralogGraphBean> extends SimpleDirectedGraph<V,E> implements GralogGraph<V,E,GB> {

	private final GralogGraphSupport<V,E,GB, ? extends SimpleDirectedGralogGraph<V,E,GB>> gralogGraphSupport;
	private final ListenableGraphSupport<V,E> listenableGraphSupport;
	
	public SimpleDirectedGralogGraph( GralogGraphSupport<V,E,GB,? extends SimpleDirectedGralogGraph<V,E,GB>> gralogGraphSupport ) {
		super( gralogGraphSupport.getEdgeFactory() );
		this.listenableGraphSupport = new ListenableGraphSupport<V,E>(  );
		this.gralogGraphSupport = gralogGraphSupport;
	}

	public GralogGraphSupport<V, E, GB, ? extends SimpleDirectedGralogGraph<V,E,GB>> getGralogSupport() {
		return gralogGraphSupport;
	}
	
	@Override
	public boolean addEdge(V sourceVertex, V targetVertex, E e) {
		boolean modified = super.addEdge( sourceVertex, targetVertex, e );
		if ( modified ) {
			gralogGraphSupport.registerBean( e );
			listenableGraphSupport.fireEdgeAdded( e );
		}
		return modified;
	}

	@Override
	public E addEdge(V sourceVertex, V targetVertex) {
		E e = super.addEdge( sourceVertex, targetVertex );
		if ( e != null ) {
			gralogGraphSupport.registerBean( e );
			listenableGraphSupport.fireEdgeAdded( e );
		}
		return e;
	}

	@Override
	public boolean addVertex(V v) {
		boolean vertexAdded = super.addVertex( v );
		if ( vertexAdded ) {
			gralogGraphSupport.registerBean( v );
			listenableGraphSupport.fireVertexAdded( v );
		}
		return vertexAdded;
	}

	@Override
	public boolean removeEdge(E e) {
		boolean modified = super.removeEdge( e );
		if ( modified )
			listenableGraphSupport.fireEdgeRemoved(e);
		return modified;
	}

	@Override
	public E removeEdge(V sourceVertex, V targetVertex) {
		E e = super.removeEdge( sourceVertex, targetVertex );
		if ( e != null )
			listenableGraphSupport.fireEdgeRemoved( e );
		return e;
	}

	@Override
	public boolean removeVertex(V v) {
		boolean modified = super.removeVertex( v );
		if ( modified )
			listenableGraphSupport.fireVertexRemoved(v);
		return modified;
	}

	public void addGraphListener(GraphListener<V, E> l) {
		listenableGraphSupport.addGraphListener( l );
	}

	public void addVertexSetListener(VertexSetListener<V> l) {
		listenableGraphSupport.addVertexSetListener( l );
	}

	public void removeGraphListener(GraphListener<V, E> l) {
		listenableGraphSupport.removeGraphListener( l );
	}

	public void removeVertexSetListener(VertexSetListener<V> l) {
		listenableGraphSupport.removeVertexSetListener( l );
	}

}
