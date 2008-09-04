/*
 * Created on 7 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.logic.graph;

import org.jgrapht.VertexFactory;
import org.jgrapht.graph.ListenableDirectedGraph;

import de.hu.gralog.graph.GralogGraphTypeInfo;
import de.hu.gralog.graph.GraphBeanFactory;
import de.hu.gralog.graph.GraphFactory;
import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;

public class TransitionSystemTypeInfo extends GralogGraphTypeInfo<TransitionSystemVertex, TransitionSystemEdge, TransitionSystem<TransitionSystemVertex, TransitionSystemEdge, ListenableDirectedGraph<TransitionSystemVertex, TransitionSystemEdge>>, ListenableDirectedGraph<TransitionSystemVertex, TransitionSystemEdge>> {

	public String getName() {
		return "TransitionSystem";
	}

	@Override
	public GraphFactory<ListenableDirectedGraph<TransitionSystemVertex, TransitionSystemEdge>> getGraphFactory() {
		return new GraphFactory<ListenableDirectedGraph<TransitionSystemVertex, TransitionSystemEdge>>() {
			public ListenableDirectedGraph<TransitionSystemVertex, TransitionSystemEdge> createGraph() {
				return new ListenableDirectedGraph<TransitionSystemVertex, TransitionSystemEdge>( TransitionSystemEdge.class );
			}
		};
	}

	@Override
	public GraphBeanFactory<TransitionSystem<TransitionSystemVertex, TransitionSystemEdge, ListenableDirectedGraph<TransitionSystemVertex, TransitionSystemEdge>>> getGraphBeanFactory() {
		return new GraphBeanFactory<TransitionSystem<TransitionSystemVertex, TransitionSystemEdge, ListenableDirectedGraph<TransitionSystemVertex, TransitionSystemEdge>>>() {
			
			public TransitionSystem<TransitionSystemVertex, TransitionSystemEdge, ListenableDirectedGraph<TransitionSystemVertex, TransitionSystemEdge>> createBean(  ) {
				return new TransitionSystem<TransitionSystemVertex, TransitionSystemEdge, ListenableDirectedGraph<TransitionSystemVertex, TransitionSystemEdge>>(  );
			}
		};
	}

	@Override
	public VertexFactory<TransitionSystemVertex> getVertexFactory() {
		return new VertexFactory<TransitionSystemVertex>() {
			public TransitionSystemVertex createVertex() {
				return new TransitionSystemVertex();
			}
		};
	}

	@Override
	public DefaultVertexRenderer getVertexRenderer() {
		return new TransitionSystemVertexRenderer();
	}

	@Override
	public DefaultEdgeRenderer getEdgeRenderer() {
		return new DefaultEdgeRenderer();
	}

}
