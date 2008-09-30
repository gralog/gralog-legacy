/*
 * Created on 7 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.logic.structure;

import org.jgrapht.VertexFactory;
import org.jgrapht.graph.ListenableDirectedGraph;

import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;
import de.hu.gralog.structure.GraphFactory;
import de.hu.gralog.structure.StructureBeanFactory;
import de.hu.gralog.structure.StructureTypeInfo;

public class TransitionSystemTypeInfo extends StructureTypeInfo<TransitionSystemVertex, TransitionSystemEdge, TransitionSystem<TransitionSystemVertex, TransitionSystemEdge, ListenableDirectedGraph<TransitionSystemVertex, TransitionSystemEdge>>, ListenableDirectedGraph<TransitionSystemVertex, TransitionSystemEdge>> {

	public String getName() {
		return "TransitionSystem";
	}
	
	public String getDescription() {
		return
			"<html>" +
			"This structure represents a finite transitionsystem " +
			"with labeled transitions. States " +
			"are represented as vertices and " +
			"transitions are represented as labeled edges. " +
			"Propositions are defined as properties of this structure and " +
			"have to be specified via the <i>Structure-Properties-View</i>." +
			"</html>";
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
	public StructureBeanFactory<TransitionSystem<TransitionSystemVertex, TransitionSystemEdge, ListenableDirectedGraph<TransitionSystemVertex, TransitionSystemEdge>>> getStructureBeanFactory() {
		return new StructureBeanFactory<TransitionSystem<TransitionSystemVertex, TransitionSystemEdge, ListenableDirectedGraph<TransitionSystemVertex, TransitionSystemEdge>>>() {
			
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
