package de.hu.gralog.graph.types;

import org.jgrapht.EdgeFactory;
import org.jgrapht.VertexFactory;
import org.jgrapht.graph.DefaultEdge;

import de.hu.gralog.beans.GralogGraphBean;
import de.hu.gralog.graph.SimpleDirectedGralogGraph;
import de.hu.gralog.graph.types.elements.LabeledGraphVertex;
import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;

public class LabeledSimpleDirectedGraphTypeInfo<GB extends GralogGraphBean> 
	implements GralogGraphTypeInfo<LabeledGraphVertex,DefaultEdge,GB,SimpleDirectedGralogGraph<LabeledGraphVertex, DefaultEdge,GB>> {

	public String getName() {
		return "LabeledSimpleDirectedGraph";
	}

	public Class<SimpleDirectedGralogGraph<LabeledGraphVertex, DefaultEdge, GB>> getGralogGraphClass() {
		return (Class<SimpleDirectedGralogGraph<LabeledGraphVertex, DefaultEdge, GB>>)SimpleDirectedGralogGraph.class;
	}

	public GraphBeanFactory<GB> getGraphBeanFactory() {
		return null;
	}

	public VertexFactory<LabeledGraphVertex> getVertexFactory() {
		return new VertexFactory<LabeledGraphVertex>() {
			public LabeledGraphVertex createVertex() {
				return new LabeledGraphVertex();
			}
		};
	}

	public EdgeFactory<LabeledGraphVertex,DefaultEdge> getEdgeFactory() {
		return null;
	}

	public DefaultVertexRenderer getVertexRenderer() {
		return new DefaultVertexRenderer();
	}

	public DefaultEdgeRenderer getEdgeRenderer() {
		return new DefaultEdgeRenderer();
	}

}
