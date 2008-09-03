package de.hu.gralog.graph;

import org.jgrapht.ListenableGraph;

/**
 * This interface defines a Factory of JGraphT-Graphs that
 * can be associated to Gralog-Graphs via 
 * {@link de.hu.gralog.graph.GralogGraphTypeInfo.getGraphFactory()}
 * in order to store the underlying mathematical structure
 * of your Gralog-Graph. 
 * <p>
 * Note that you can use any JGraphT-Graph as long as you wrapp
 * this graph in a {@link org.jgrapht.graph.ListenableGraph}, which can
 * be either {@link org.jgrapht.graph.ListenableDirectedGraph} or 
 * {@link org.jgrapht.jgraph.ListenableUndirectedGraph}.
 * 
 * <h1>Plugin-Developers</h1>
 * 
 * Here is an example implementing a GraphFactory that
 * returns a {@link org.jgrapht.graph.DirectedMultiGraph} - that is wrapped in
 * a {@link org.jgrapht.graph.ListenableDirectedGraph}, i.e. a directed graph that
 * is allowed to have multiple edges and loops:
 * 
 * <pre>
 * 		public class DirectedMultiGraphGraphFactory 
 * 				implements GraphFactory<ListenableDirectedGraph<LabeledGraphVertex, DefaultEdge> {
 * 			public ListenableDirectedGraph<LabeledGraphVertex, DefaultEdge> createGraph() {
 * 				return new ListenableDirectedGraph<LabeledGraphVertex,DefaultEdge>( new DirectedMultiGraph<LabeledGraphVertex, DefaultEgde>( DefaultEdge.class ) );
 * 			}
 * 		}
 * </pre>
 * 
 * Please refer to {@link de.hu.gralog.graph.types} for further examples and
 * to <a href="http://jgrapht.sourceforge.net/">JGraphT</a> for information on JGraphT.
 * 
 * @author Sebastian
 * 
 * @param <G> the graphType
 */
public interface GraphFactory<G extends ListenableGraph> {
	public G createGraph();
}
