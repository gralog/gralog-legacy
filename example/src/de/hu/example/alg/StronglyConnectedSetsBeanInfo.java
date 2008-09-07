package de.hu.example.alg;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import org.jgrapht.DirectedGraph;
import org.jgrapht.ListenableGraph;

import de.hu.gralog.beans.propertydescriptor.ChooseGraphPropertyDescriptor;
import de.hu.gralog.graph.GralogGraphTypeInfo;
import de.hu.gralog.graph.GralogGraphTypeInfoFilter;

/**
 * This is the <b>BeanInfo</b>-class for the {@link StronglyConnectedSets}
 * algorithm.  
 * 
 * @author Sebastian
 *
 */
public class StronglyConnectedSetsBeanInfo extends SimpleBeanInfo {

	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( StronglyConnectedSets.class );
	
	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[1];
	
	static {
		BEAN_DESCRIPTOR.setDisplayName( "StronglyConnectedSets" );
		BEAN_DESCRIPTOR.setShortDescription( "<html>" +
				"This algorithm shows you how to use <b>AlgorithmResult</b> " +
				"with <b>AlgorithmResultContentTreeNode</b> to show " +
				"the user a tree of contents. " +
				"It also shows you how to use <b>DisplaySubgraphMode</b> and " +
				"<b>ElementTipsDisplayMode</b> to highlight subgraphs and provide " +
				"tooltips for the elements of the graph." +
				"( to see the elementTips move the mouse over " +
				" one of the vertices in the graph. ) " +
				"</html>" );
		
		/**
		 * We are now ready to define the four properties and provide them with a description.
		 * 
		 */
		
		try {
			PROPERTY_DESCRIPTORS[0] = new ChooseGraphPropertyDescriptor( "graph", StronglyConnectedSets.class, new DirectedGraphTypeInfoFilter() );
			PROPERTY_DESCRIPTORS[0].setShortDescription( "<html> " +
						"The directed graph for which we compute its strongly connected components." +
						"</html>" );
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public BeanDescriptor getBeanDescriptor() {
		return BEAN_DESCRIPTOR;
	}

	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
		return PROPERTY_DESCRIPTORS;
	}

	/**
	 * This class serves as a {@link GralogGraphTypeInfoFilter}
	 * for the property <b>graph</b>
	 * defined above. It filters all
	 * {@link GralogGraphTypeInfo GralogGraphTypeInfos}
	 * that are not directed.
	 *  
	 * @author Sebastian
	 *
	 */
	public static class DirectedGraphTypeInfoFilter implements GralogGraphTypeInfoFilter {
		public boolean filterTypeInfo( GralogGraphTypeInfo typeInfo ) {
			ListenableGraph listenableGraph = typeInfo.getGraphFactory().createGraph();

			if ( ! ( listenableGraph instanceof DirectedGraph ) )
				return true;
			return false;
		}
	}
	
}
