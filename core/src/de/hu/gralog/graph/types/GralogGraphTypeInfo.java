package de.hu.gralog.graph.types;

import org.jgrapht.EdgeFactory;
import org.jgrapht.VertexFactory;

import de.hu.gralog.beans.GralogGraphBean;
import de.hu.gralog.graph.GralogGraph;
import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;

public interface GralogGraphTypeInfo<V,E,GB extends GralogGraphBean,G extends GralogGraph<V,E,GB>> {
	
	public String getName();
	
	public Class<G> getGralogGraphClass();
	
	public GraphBeanFactory<GB> getGraphBeanFactory();
	
	public VertexFactory<V> getVertexFactory();
	
	public EdgeFactory<V,E> getEdgeFactory();
	
	public DefaultVertexRenderer getVertexRenderer();
	
	public DefaultEdgeRenderer getEdgeRenderer();
}
