package de.hu.example.graph;

import org.jgrapht.VertexFactory;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.graph.ListenableDirectedGraph;

import de.hu.gralog.graph.GralogGraphTypeInfo;
import de.hu.gralog.graph.GraphBeanFactory;
import de.hu.gralog.graph.GraphFactory;
import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;

/**
 * This class defines our new GralogGraph, i.e. <b>SimpleGraphExample</b>.
 * It extends {@link de.hu.gralog.graph.GralogGraphTypeInfo} and
 * basically assembles all the information needed for Gralog
 * to deal with this new GraphType. Please see the description
 * of {@link de.hu.gralog.graph.GralogGraphTypeInfo} for further information.
 * 
 * This example uses:
 * 
 * <ul>
 * 		<li>A DirectedMultiGraph as an underlying JGraphT-Graph,
 * 		that means you can add multiply edges between the same vertices to it
 * 		</li>
 * 		<li>{@link SimpleGraphBean} as the GraphBean.</li>
 * 		<li>{@link SimpleVertex} as vertices</li>
 * 		<li>{@link SimpleEdge} as edges</li>
 * 		<li>{@link SimpleVertexVertexRenderer} as a Renderer for the vertices</li>
 * </ul>
 * 
 * @author Sebastian
 *
 */
public class SimpleGraphTypeInfo extends GralogGraphTypeInfo<SimpleVertex, SimpleEdge, SimpleGraphBean, ListenableDirectedGraph<SimpleVertex,SimpleEdge>> {

	public SimpleGraphTypeInfo() {
		super();
	}

	@Override
	public String getName() {
		return "SimpleGraphExample";
	}

	@Override
	public GraphFactory<ListenableDirectedGraph<SimpleVertex, SimpleEdge>> getGraphFactory() {
		return new GraphFactory<ListenableDirectedGraph<SimpleVertex,SimpleEdge>>() {
			public ListenableDirectedGraph<SimpleVertex, SimpleEdge> createGraph() {
				return new ListenableDirectedGraph<SimpleVertex, SimpleEdge>( new DirectedMultigraph<SimpleVertex, SimpleEdge>( SimpleEdge.class ) );
			}
		};
	}

	@Override
	public GraphBeanFactory<SimpleGraphBean> getGraphBeanFactory() {
		return new GraphBeanFactory<SimpleGraphBean>() {
			public SimpleGraphBean createBean() {
				return new SimpleGraphBean();
			}
		};
	}

	@Override
	public VertexFactory<SimpleVertex> getVertexFactory() {
		return new VertexFactory<SimpleVertex>() {
			public SimpleVertex createVertex() {
				return new SimpleVertex();
			}
		};
	}

	@Override
	public DefaultVertexRenderer getVertexRenderer() {
		return new SimpleVertexVertexRenderer();
	}

	@Override
	public DefaultEdgeRenderer getEdgeRenderer() {
		return null;
	}

}
