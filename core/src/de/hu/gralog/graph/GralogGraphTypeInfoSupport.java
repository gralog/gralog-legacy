package de.hu.gralog.graph;

import org.jgrapht.EdgeFactory;
import org.jgrapht.VertexFactory;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultEdge;

import de.hu.gralog.beans.GralogGraphBean;
import de.hu.gralog.graph.types.GralogGraphTypeInfo;
import de.hu.gralog.graph.types.GraphBeanFactory;
import de.hu.gralog.graph.types.elements.LabeledGraphVertex;
import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;

class GralogGraphTypeInfoSupport<V,E,GB extends GralogGraphBean, G extends GralogGraph<V,E,GB>> {

	private final GralogGraphTypeInfo<V,E,GB,G> typeInfo;
	private final String name;
	private final Class<G> graphClass;
	private final GraphBeanFactory<GB> graphBeanFactory;
	private final GB bean;
	private final VertexFactory<V> vertexFactory;
	private final EdgeFactory<V,E> edgeFactory;
	private final DefaultVertexRenderer vertexRenderer;
	private final DefaultEdgeRenderer edgeRenderer;
	
	
	GralogGraphTypeInfoSupport( GralogGraphTypeInfo<V,E,GB,G> typeInfo, GB bean ) {
		this.typeInfo = typeInfo;
		name = typeInfo.getName();
		graphClass = typeInfo.getGralogGraphClass();
		graphBeanFactory = typeInfo.getGraphBeanFactory();
		vertexFactory = typeInfo.getVertexFactory();
		edgeFactory = typeInfo.getEdgeFactory();
		vertexRenderer = typeInfo.getVertexRenderer();
		edgeRenderer = typeInfo.getEdgeRenderer();
		if ( bean != null )
			this.bean = bean;
		else {
			if ( graphBeanFactory != null )
				this.bean = graphBeanFactory.createBean();
			else
				this.bean = null;
		}
	}


	public String getName() {
		return name;
	}

	public Class<G> getGralogGraphClass() {
		return graphClass;
	}

	public GB getGraphBean() {
		return bean;
	}

	public VertexFactory<V> getVertexFactory() {
		return vertexFactory;
	}

	public EdgeFactory<V, E> getEdgeFactory() {
		if ( edgeFactory == null )
			return new ClassBasedEdgeFactory( DefaultEdge.class );
		return edgeFactory;
	}

	public DefaultVertexRenderer getVertexRenderer() {
		return vertexRenderer;
	}

	public DefaultEdgeRenderer getEdgeRenderer() {
		return edgeRenderer;
	}
	
	public GralogGraphTypeInfo<V,E,GB,G> getTypeInfo() {
		return typeInfo;
	}
}
