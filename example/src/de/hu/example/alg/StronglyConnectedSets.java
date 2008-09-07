package de.hu.example.alg;

import java.util.Hashtable;
import java.util.Set;

import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.ListenableDirectedGraph;

import de.hu.gralog.algorithm.Algorithm;
import de.hu.gralog.algorithm.InvalidPropertyValuesException;
import de.hu.gralog.algorithm.result.AlgorithmResult;
import de.hu.gralog.algorithm.result.AlgorithmResultContentTreeNode;
import de.hu.gralog.algorithm.result.DisplaySubgraphMode;
import de.hu.gralog.algorithm.result.ElementTipsDisplayMode;
import de.hu.gralog.algorithm.result.DisplaySubgraph.DisplayMode;
import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.GralogGraphSupport;

/**
 * This algorithm shows you how to use: 
 * 
 * <ul>
 * 		<li>
 * 			{@link de.hu.gralog.algorithm.result.AlgorithmResult}
 * 		</li>
 * 		<li> 
 * 			{@link de.hu.gralog.algorithm.result.AlgorithmResultContentTreeNode}
 * 		</li>
 * 		<li>
 * 			{@link de.hu.gralog.algorithm.result.DisplaySubgraphMode} and
 * 		</li>
 * 		<li>
 * 			{@link de.hu.gralog.algorithm.result.ElementTipsDisplayMode}
 * 		</li>
 * </ul>
 * 
 * in order to display a tree of contents to the user that highlight
 * different subgraphs of the graph and elementTips that provide
 * ToolTips for the vertices of these graphs. It also shows you
 * how to use <a href="http://www.jgrapht.org">JGraphT</a> to
 * run algorithms on your graph.
 * 
 * @author Sebastian
 *
 * @param <V>
 * @param <E>
 * @param <GB>
 * @param <G>
 */
public class StronglyConnectedSets<V,E,GB, G extends ListenableDirectedGraph<V,E>> 
		implements Algorithm {

	/**
	 * This algorithm has only one property, namely the graph
	 * for which we compute the strongly connected components.
	 * 
	 */
	private GralogGraphSupport<V,E,GB,G> graph;
	
	public GralogGraphSupport<V, E, GB, G> getGraph() {
		return graph;
	}

	public void setGraph(GralogGraphSupport<V, E, GB, G> graph) {
		this.graph = graph;
	}

	/**
	 * These act as identifiers for the {@link de.hu.gralog.algorithm.result.DisplaySubgraphMode}
	 * and {@link de.hu.gralog.algorithm.result.ElementTipsDisplayMode} we add
	 * to our {@link de.hu.gralog.algorithm.result.AlgorithmResult}. 
	 * 
	 */
	private static final String DM_COMPONENT = "The strongly connected component.";
	private static final String ET_COMPONENT = "The component of the vertex.";
	
	/**
	 * The execute-function  
	 * 
	 */
	public AlgorithmResult execute() throws InvalidPropertyValuesException,
			UserException {
		// first we check the parameters
		InvalidPropertyValuesException e = new InvalidPropertyValuesException();
		if ( getGraph() == null )
			e.addPropertyError( "graph", InvalidPropertyValuesException.PROPERTY_REQUIRED );
		if ( e.hasErrors() )
			throw e;
		
		// now we start to build our AlgorithmResult
		AlgorithmResult result = new AlgorithmResult( graph );
		result.setDescription( "The result contains a child of <b>graph</b> for every " +
				"of its strongly connected components." );
		
		// we construct a displayMode and add it to our AlgorithmResult
		DisplaySubgraphMode displayMode = new DisplaySubgraphMode();
		displayMode.setVertexDisplayMode( DisplayMode.HIGH1, DisplayMode.SHOW );
		displayMode.setEdgeDisplayMode( DisplayMode.HIGH1, DisplayMode.SHOW );
		result.addDisplaySubgraphMode( DM_COMPONENT, displayMode );
		
		// we construct an elementTipsMode and add it to our AlgorithmResult
		ElementTipsDisplayMode elementTipsMode = new ElementTipsDisplayMode();
		result.addElementTipsDisplayMode( ET_COMPONENT, elementTipsMode );
		
		/*
		 *  now we can define the root-node of our AlgorithmResultContentTree and
		 *  give it a name, which is later displayed in Gralog
		 */
		AlgorithmResultContentTreeNode contentRoot = new AlgorithmResultContentTreeNode();
		contentRoot.setName( "graph" );
		
		// do not forget to add the root to our AlgorithmResult!
		result.setContentTree( contentRoot );

		/**
		 * we are now ready to initialize the elementips for vertices of <b>graph</b>
		 */
		int i = 1;
		Hashtable<V, String> elementTips = new Hashtable<V,String>();

		/**
		 * This is an example how to use the JGraphT-Graph underlying
		 * {@link GralogGraphSupport} as input for the JGraphT-library
		 * 
		 * Note: We can not pass {@link #getGraph()} directly to the constructor of
		 * {@link org.jgrapht.alg.StrongConnectivityInspector}, but
		 * instead its underlying JGraphT-Graph ( {@link GralogGraphSupport#getGraph()}.
		 *  
		 */
		StrongConnectivityInspector<V,E> inspector = new StrongConnectivityInspector<V,E>( getGraph().getGraph() );

		/**
		 * Now we are ready to add children to our <b>contentRoot</b>, i.e.
		 * a child for each component of the graph.
		 * 
		 */
		for ( Set<V> connectedSet : inspector.stronglyConnectedSets() ) {
			/**
			 * Construct the child of contentRoot that represents
			 * the i-th component by highlighting its vertices and
			 * edges
			 * 
			 */
			AlgorithmResultContentTreeNode component = new AlgorithmResultContentTreeNode();
			component.setName( "The " + i + "-th component." );
			component.addDisplaySubgraph( DM_COMPONENT, connectedSet, null );
			
			/**
			 * Along the way, we construct our elementTips and
			 * add it to each child of <b>contentRoot</b>
			 */
			for ( V vertex : connectedSet )
				elementTips.put( vertex, "This vertex belongs to the " + i + "-th component." );
			component.addElementTips( ET_COMPONENT , elementTips );
			
			/**
			 * Now we attach <b>component</b> to <b>contentRoot</b>
			 * 
			 */
			contentRoot.addChild( component );
			i++;
		}
		contentRoot.addElementTips( ET_COMPONENT, elementTips );
		return result;
	}

}
