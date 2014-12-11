/*
 * Created on 10 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.dagwidth.alg;

import org.jgrapht.VertexFactory;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;

import de.hu.dagwidth.alg.DAGConstruction.DAGVertex;
import de.hu.gralog.beans.support.*;
import de.hu.gralog.structure.*;
import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;

public class DAGVertexGraphTypeInfo<GB extends StructureBean> extends StructureTypeInfo<DAGVertex, DefaultEdge, GB, ListenableDirectedGraph<DAGVertex, DefaultEdge>> {

	@Override
	public String getName() {
		return "DAGGraph";
	}

	@Override
	public GraphFactory<ListenableDirectedGraph<DAGVertex, DefaultEdge>> getGraphFactory() {
		return new GraphFactory<ListenableDirectedGraph<DAGVertex, DefaultEdge>>() {
			public ListenableDirectedGraph<DAGVertex, DefaultEdge> createGraph() {
				return new ListenableDirectedGraph<DAGVertex, DefaultEdge>( DefaultEdge.class );
			}
		};
	}

	@Override
	public StructureBeanFactory<GB> getStructureBeanFactory() {
		return null;
	}

	@Override
	public VertexFactory<DAGVertex> getVertexFactory() {
		return new VertexFactory<DAGVertex>() {
			public DAGVertex createVertex() {
				return new DAGVertex();
			}
		};
	}

	@Override
	public DefaultVertexRenderer getVertexRenderer() {
		return null;
	}

	@Override
	public DefaultEdgeRenderer getEdgeRenderer() {
		return null;
	}

	@Override
	public String getDescription() {
		return "Directed Acyclic Graph";
	}

	
}