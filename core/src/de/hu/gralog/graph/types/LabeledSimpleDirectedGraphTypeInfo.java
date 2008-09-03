package de.hu.gralog.graph.types;

import org.jgrapht.VertexFactory;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.jgrapht.graph.SimpleDirectedGraph;

import de.hu.gralog.graph.GralogGraphTypeInfo;
import de.hu.gralog.graph.GraphBeanFactory;
import de.hu.gralog.graph.GraphFactory;
import de.hu.gralog.graph.types.elements.LabeledGraphVertex;
import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;

/**
 * This class defines the <b>LabeledSimpleDirectedGraph</b> Gralog-Graph-Type.
 * This Graph-Type has:
 * 
 * <ul>
 * 		<li>{@link de.hu.gralog.graph.types.elements.LabeledGraphVertex} as <b>vertexType</b><br>
 * 			@see #getVertexFactory()
 * 		</li>
 * 		<li>{@link org.jgrapht.graph.DefaultEdge} as <b>edgeType</b><br>
 * 			@see #getGraphFactory()
 * 		</li>
 * 		<li>{@link org.jgrapht.graph.SimpleDirectedGraph} as <b>JGraphT-Type</b><br>
 * 			Note that since this is not a {@link org.jgrapht.graph.ListenableGraph} it
 * 			has to be wrapped in a {@link org.jgrapht.graph.ListenableDirectedGraph} in
 * 			order to make it usable for Gralog. See {@link #getGraphFactory()} for
 * 			how this is accomplished here.
 * 		</li>
 * 		<li>
 * 			no support for a GraphBean.<br>
 * 			@see #getGraphBeanFactory() 
 * 		</li>
 * </ul> 
 *  
 * @author Sebastian
 *
 * @see GralogGraphTypeInfo
 * 
 * @param <GB> the GraphBeanType
 */
public class LabeledSimpleDirectedGraphTypeInfo<GB>
		extends
		GralogGraphTypeInfo<LabeledGraphVertex, DefaultEdge, GB, ListenableDirectedGraph<LabeledGraphVertex, DefaultEdge>> {

	public String getName() {
		return "LabeledSimpleDirectedGraph";
	}

	public GraphFactory<ListenableDirectedGraph<LabeledGraphVertex, DefaultEdge>> getGraphFactory() {
		return new GraphFactory<ListenableDirectedGraph<LabeledGraphVertex, DefaultEdge>>() {
			public ListenableDirectedGraph<LabeledGraphVertex, DefaultEdge> createGraph() {
				return new ListenableDirectedGraph<LabeledGraphVertex, DefaultEdge>(
						new SimpleDirectedGraph<LabeledGraphVertex, DefaultEdge>(
								DefaultEdge.class));
			}
		};
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

	public DefaultVertexRenderer getVertexRenderer() {
		return null;
	}

	public DefaultEdgeRenderer getEdgeRenderer() {
		return null;
	}

}
