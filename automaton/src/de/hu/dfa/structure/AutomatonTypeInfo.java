package de.hu.dfa.structure;

import org.jgrapht.VertexFactory;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.graph.ListenableDirectedGraph;

import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;
import de.hu.gralog.structure.GraphFactory;
import de.hu.gralog.structure.StructureBeanFactory;
import de.hu.gralog.structure.StructureTypeInfo;

public class AutomatonTypeInfo extends StructureTypeInfo<AutomatonVertex, AutomatonEdge, AutomatonBean, ListenableDirectedGraph<AutomatonVertex, AutomatonEdge>> {

	@Override
	public String getName() {
		return "Automaton";
	}
	
	public String getDescription() {
		return 
			"<html>" +
			"This structure represents a non-deterministic " +
			"finite state acceptor, as defined in " +
			"<a href=\"http://en.wikipedia.org/wiki/Finite_state_machine\">Finite-State-Automaton</a>. " +
			"States are represented as vertices, " +
			" and transitions are represented as " +
			"labeled edges, with the label of an edge " +
			"representing the symbol of it's transition. " +
			"Start- respectively Accepting-States are specified " +
			"via the properties of the structure-bean " +
			"respectively the properties of it's vertices. " +
			"</html>";
	}

	@Override
	public GraphFactory<ListenableDirectedGraph<AutomatonVertex, AutomatonEdge>> getGraphFactory() {
		return new GraphFactory<ListenableDirectedGraph<AutomatonVertex, AutomatonEdge>>() {
			public ListenableDirectedGraph<AutomatonVertex, AutomatonEdge> createGraph() {
				DirectedMultigraph<AutomatonVertex, AutomatonEdge> multiGraph = new DirectedMultigraph<AutomatonVertex, AutomatonEdge>( AutomatonEdge.class );
				return new ListenableDirectedGraph<AutomatonVertex, AutomatonEdge>( multiGraph );
			}
		};
	}
	
	@Override
	public StructureBeanFactory<AutomatonBean> getStructureBeanFactory() {
		return new StructureBeanFactory<AutomatonBean>() {
			public AutomatonBean createBean(  ) {
				return new AutomatonBean();
			}
		};
	}

	@Override
	public VertexFactory<AutomatonVertex> getVertexFactory() {
		return new VertexFactory<AutomatonVertex>() {
			public AutomatonVertex createVertex() {
				return new AutomatonVertex();
			}
		};
	}

	@Override
	public DefaultVertexRenderer getVertexRenderer() {
		return new AutomatonVertexRenderer();
	}
	
	@Override
	public DefaultEdgeRenderer getEdgeRenderer() {
		return new DefaultEdgeRenderer();
	}
}
