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
import de.hu.gralog.structure.Structure;

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
 * in order to display a tree of contents to the user that highlights
 * different subgraphs of a structure and element-tips that provide
 * ToolTips for the vertices of these structures. It also shows you
 * how to use <a href="http://www.jgrapht.org">JGraphT</a> to
 * run algorithms on your structure.
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
	 * This algorithm has only one property, namely the structure
	 * for which we compute the strongly connected components.
	 * 
	 */
	private Structure<V,E,GB,G> structure;
	
	public Structure<V, E, GB, G> getStructure() {
		return structure;
	}

	public void setStructure(Structure<V, E, GB, G> structure) {
		this.structure = structure;
	}

	/**
	 * These Strings act as identifiers 
	 * for the {@link de.hu.gralog.algorithm.result.DisplaySubgraphMode}
	 * and {@link de.hu.gralog.algorithm.result.ElementTipsDisplayMode} that we will add
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
		if ( getStructure() == null )
			e.addPropertyError( "structure", InvalidPropertyValuesException.PROPERTY_REQUIRED );
		if ( e.hasErrors() )
			throw e;
		
		// now we start to build our AlgorithmResult
		AlgorithmResult result = new AlgorithmResult( structure );
		result.setDescription( "The result contains a child-content of <b>structure</b> for each " +
				"of structure's strongly connected components." );
		
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
		 *  give it a name, which is later displayed in GrALoG
		 */
		AlgorithmResultContentTreeNode contentRoot = new AlgorithmResultContentTreeNode();
		contentRoot.setName( "structure" );
		
		// do not forget to add the root to our AlgorithmResult!
		result.setContentTree( contentRoot );

		/**
		 * we are now ready to initialize the elementips for vertices of <b>structure</b>
		 */
		int i = 1;
		Hashtable<V, String> elementTips = new Hashtable<V,String>();

		/**
		 * This is an example how to use the JGraphT-Graph underlying
		 * {@link Structure} as input for the JGraphT-library.
		 * 
		 * Note: We can not pass {@link #getStructure()} directly to the constructor of
		 * {@link org.jgrapht.alg.StrongConnectivityInspector}, but
		 * instead it's underlying JGraphT-Graph ( {@link Structure#getGraph()}.
		 *  
		 */
		StrongConnectivityInspector<V,E> inspector = new StrongConnectivityInspector<V,E>( getStructure().getGraph() );

		/**
		 * Now we are ready to add children to our <b>contentRoot</b>, i.e.
		 * a child for each component of the structure.
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
			component.setName( "The component number: " + i );
			component.addDisplaySubgraph( DM_COMPONENT, connectedSet, null );
			
			/**
			 * Along the way, we construct our element-Tips and
			 * add it to each child of <b>contentRoot</b>
			 */
			for ( V vertex : connectedSet )
				elementTips.put( vertex, "This vertex belongs to component number: " + i );
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
