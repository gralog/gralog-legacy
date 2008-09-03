package de.hu.gralog.graph.support;

import org.jgrapht.ListenableGraph;
import org.jgrapht.VertexFactory;

import de.hu.gralog.graph.GralogGraphTypeInfo;
import de.hu.gralog.graph.GraphBeanFactory;
import de.hu.gralog.graph.GraphFactory;
import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;

/**
 * This class is used by {@link GralogGraphSupport} as an Adapter
 * to {@link GralogGraphTypeInfo}. It is of no use to Plugin-Developers. 
 * 
 * @author Sebastian
 *
 * @param <V> the vertexType
 * @param <E> the edgeType
 * @param <GB> the GraphBeanType
 * @param <G> the JGraphT-Type
 */
public class GralogGraphTypeInfoSupport<V, E, GB, G extends ListenableGraph<V, E>> {

	private final GralogGraphTypeInfo<V, E, GB, G> typeInfo;

	private final String name;

	private final GraphFactory<G> graphFactory;

	private final G graph;

	private final GraphBeanFactory<GB> graphBeanFactory;

	private GB bean;

	private final VertexFactory<V> vertexFactory;

	private final DefaultVertexRenderer vertexRenderer;

	private final DefaultEdgeRenderer edgeRenderer;

	public GralogGraphTypeInfoSupport(
			GralogGraphTypeInfo<V, E, GB, G> typeInfo, G graph, GB bean) {
		this.typeInfo = typeInfo;
		name = typeInfo.getName();
		graphFactory = typeInfo.getGraphFactory();
		if (graph == null)
			this.graph = graphFactory.createGraph();
		else
			this.graph = graph;
		graphBeanFactory = typeInfo.getGraphBeanFactory();
		vertexFactory = typeInfo.getVertexFactory();
		vertexRenderer = typeInfo.getVertexRenderer();
		edgeRenderer = typeInfo.getEdgeRenderer();
		this.bean = bean;
	}

	public String getName() {
		return name;
	}

	public G getGraph() {
		return graph;
	}

	public GB getGraphBean() {
		if (bean == null) {
			if (graphBeanFactory == null)
				return null;
			bean = graphBeanFactory.createBean();
		}
		return bean;
	}

	public VertexFactory<V> getVertexFactory() {
		return vertexFactory;
	}

	public DefaultVertexRenderer getVertexRenderer() {
		return vertexRenderer;
	}

	public DefaultEdgeRenderer getEdgeRenderer() {
		return edgeRenderer;
	}

	public GralogGraphTypeInfo<V, E, GB, G> getTypeInfo() {
		return typeInfo;
	}
}
