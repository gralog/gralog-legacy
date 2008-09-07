package de.hu.example.alg;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.jgrapht.graph.SimpleDirectedGraph;

import de.hu.example.alg.AdvancedProperties.WeekDay;
import de.hu.gralog.beans.propertydescriptor.ChooseDependableGraphElementPropertyDescriptor;
import de.hu.gralog.beans.propertydescriptor.ChooseGraphPropertyDescriptor;
import de.hu.gralog.beans.propertydescriptor.ChooseGraphTypeInfoPropertyDescriptor;
import de.hu.gralog.beans.propertydescriptor.EnumPropertyDescriptor;
import de.hu.gralog.beans.propertydescriptor.GraphElementFilter.VertexGraphElementFilter;
import de.hu.gralog.graph.GralogGraphTypeInfo;
import de.hu.gralog.graph.GralogGraphTypeInfoFilter;
import de.hu.gralog.graph.types.elements.LabeledGraphVertex;
import de.hu.gralog.jgrapht.util.JGraphTUtils;

/**
 * This is the <b>BeanInfo</b>-class for the {@link AdvancedProperties}
 * algorithm.  
 * 
 * It shows you how to use
 * {@link de.hu.gralog.beans.propertydescriptor.ChooseGraphPropertyDescriptor},
 * {@link de.hu.gralog.beans.propertydescriptor.ChooseDependableGraphElementPropertyDescriptor},
 * {@link de.hu.gralog.beans.propertydescriptor.ChooseGraphTypeInfoPropertyDescriptor} and
 * {@link de.hu.gralog.beans.propertydescriptor.EnumPropertyDescriptor} as
 * descriptors for the properties of your algorithm.
 * 
 * @author Sebastian
 *
 */
public class AdvancedPropertiesBeanInfo extends SimpleBeanInfo {

	/**
	 * This is the BeanDescriptor for this class, whose <b>displayName</b> and
	 * <b>shortDescription</b> are used by Gralog to give this algorithm
	 * a name and a description.
	 * 
	 */
	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( AdvancedProperties.class );
	
	/**
	 * These are the {@link java.beans.PropertyDescriptor PropertyDescriptors} for the
	 * properties of our Algorithm. Since our Algorithm takes four
	 * parameters, i.e. <b>graph</b>, <b>vertexOfGraph</b>, <b>graphType</b> and <b>WeekDay</b>, we
	 * need to define four of them.
	 * 
	 */
	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[4];
	
	static {
		/**
		 * First we set the name for our Algorithm, that is displayed to the User.
		 * 
		 */
		BEAN_DESCRIPTOR.setDisplayName( "AdvancedProperties" );
		
		/**
		 * Now we set the description for our Algorithm, that again will be displayed
		 * to the user. Note that you are allowed to use arbitrary html in this String.
		 * 
		 */
		BEAN_DESCRIPTOR.setShortDescription( "<html>" +
				"This algorithm shows how to make use of the more advanced " +
				"<b>PropertyDescriptors</b> that are avaible in Gralog. " + 
				"</html>" );
		
		/**
		 * We are now ready to define the four properties and provide them with a description.
		 * 
		 */
		
		try {
			/**
			 * This defines the first property <b>graph</b> of the algorithm
			 * {@link AdvancedProperties}. 
			 * Since we want to let the user choose a GralogGraph as a value
			 * for this property we have to use a 
			 * {@link de.hu.gralog.beans.propertydescriptor.ChooseGraphPropertyDescriptor} as
			 * a PropertyDescriptor for it. As mentioned in
			 * the description for this property {@link AdvancedProperties}, 
			 * we provide our own {@link GralogGraphTypeInfoFilter} to
			 * allow only {@link org.jgrapht.graph.SimpleDirectedGraph} that
			 * have {@link de.hu.gralog.graph.types.elements.LabeledGraphVertex} as
			 * vertices to be selected. See below ( {@link LabeledSimpleDirectedGraphTypeInfoFilter} )
			 * for the definition of this GraphTypeInfoFilter.
			 * 
			 * Note: The last parameter for the constructor tells Gralog
			 * whether to make a copy of the selected graph before it is
			 * passed to the algorithm. Not making a copy can
			 * be useful for algorithm that either:
			 * 
			 * <ul>
			 * 		<li>do not manipulate the given graph and do not
			 * 		return the graph as part of an 
			 * 		{@link de.hu.gralog.algorithm.AlgorithmResult} - like this algorithm - and
			 * 		thus want to avoid Gralog spending time on copying the graph.
			 * 		</li>
			 * 		<li>want to manipulate the graph explicitly - and
			 * 		again do not return the graph as part of a
			 * 		{@link de.hu.gralog.algorithm.result.AlgorithmResult} -
			 * 		, e.g. to provide some automatic editing functionality to the user.</li>
			 * </ul>
			 * 
			 * Nevertheless for algorithms that use the graph in an 
			 * {@link de.hu.gralog.algorithm.result.AlgorithmResult}, it
			 * is strongly recommend to set this option to true, as otherwise
			 * the result shown to the user and the graph-document containing the
			 * graph are mutually affected by each others changes.
			 * 
			 */
			PROPERTY_DESCRIPTORS[0] = new ChooseGraphPropertyDescriptor( "graph", AdvancedProperties.class, new LabeledSimpleDirectedGraphTypeInfoFilter(), false );
			PROPERTY_DESCRIPTORS[0].setShortDescription( "<html> " +
						"This property shows you how to use <br>" +
						"<b>ChooseGraphPropertyDescriptor</b> <br>" +
						" and <b>GralogGraphTypeInfoFilter</b> <br>" +
						" in order to let you choose a <b>SimpleDirectedGraph</b> <br>" +
						" with <b>labeled</b> vertices." +
						"</html>" );

			/**
			 * This defines the second property <b>vertexOfGraph</b> for the algorithm
			 * {@link AdvancedProperties}.
			 *  
			 * This time we have to use 
			 * {@link de.hu.gralog.beans.propertydescriptor.ChooseDependableGraphElementPropertyDescriptor}.
			 * 
			 *  As you can see the third parameter of its constructor tells it
			 *  to select elements from the graph that is
			 *  a value of the property <b>graph</b> defined above and
			 *  for its last parameter we use
			 *  {@link de.hu.gralog.beans.propertydescriptor.GraphElementFilter.VertexGraphElementFilter}
			 *  to restrict the user to choose
			 *  among the vertices of this graph.
			 *  
			 */
			PROPERTY_DESCRIPTORS[1] = new ChooseDependableGraphElementPropertyDescriptor( "vertexOfGraph", AdvancedProperties.class, (ChooseGraphPropertyDescriptor)PROPERTY_DESCRIPTORS[0], new VertexGraphElementFilter() );
			PROPERTY_DESCRIPTORS[1].setShortDescription( "<html> " +
						"This property shows you how to use <br>" +
						"<b>ChooseDependableGraphElementPropertyDescriptor</b> " +
						"and <b>VertexGraphElementFilter</b> <br>" +
						"to let you choose a vertex from <br>" +
						"the <b>GralogGraph</b> that is a value of <br>" +
						"the property <b>graph</b>." +
						"</html>" );

			/**
			 * This defines the third property <b>graphType</b> of the algorithm
			 * {@link AdvancedProperties}.
			 * 
			 * It shows how to use 
			 * {@link de.hu.gralog.beans.propertydescriptor.ChooseGraphTypeInfoPropertyDescriptor}
			 * to let the user choose a Gralog-Graph-Type. We again
			 * use {@link LabeledSimpleDirectedGraphTypeInfoFilter} to
			 * restrict the user to Graph-Types that
			 * have a {@link org.jgrapht.graph.SimpleDirectedGraph} as 
			 * underlying JGraphT-Graph and has 
			 * {@link de.hu.gralog.types.elements.LabeledGraphVertex} as 
			 * its vertices. 
			 */
			PROPERTY_DESCRIPTORS[2] = new ChooseGraphTypeInfoPropertyDescriptor( "graphType", AdvancedProperties.class, new LabeledSimpleDirectedGraphTypeInfoFilter() );
			PROPERTY_DESCRIPTORS[2].setShortDescription( "<html> " +
					"This property shows you how to use <br>" +
					"<b>ChooseGraphTypeInfoPropertyDescriptor</b> <br>" +
					"to let you choose a <b>Gralog-Graph-Type</b> <br>" +
					"that has <b>LabeledGraphVertex</b> as <br>" +
					"its vertices and a <b> SimpleDirectedGraph</b> <br>" +
					"as its underlying JGraphT-Graph." +
					"</html>" );

			/**
			 * This defines the forth property <b>weekDay</b> of the algorithm
			 * {@link AdvancedProperties}.
			 *  
			 * It shows you how to use 
			 * {@link de.hu.gralog.beans.propertydescriptor.EnumPropertyDescriptor}
			 * to let the user choose from the values of a <b>JavaEnum</b>.
			 * 
			 */
			PROPERTY_DESCRIPTORS[3] = new EnumPropertyDescriptor( "weekDay", AdvancedProperties.class, WeekDay.values() );
			PROPERTY_DESCRIPTORS[3].setShortDescription( "<html> " +
						"It shows you how to use <br>" + 
						" an EnumPropertyDescriptor <br>" + 
						" to let the user choose from the values of a <b>JavaEnum</b>. " +
						"</html>" );
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	
	}

	/**
	 * Last but not least we have to override the methods
	 * {@link java.beans.SimpleBeanInfo#getBeanDescriptor()} and
	 * {@link java.beans.SimpleBeanInfo#getPropertyDescriptors()}
	 * in order to make our <b>BEAN_DESCRIPTOR</b> and <b>PROPERTY_DESCRIPTORS</b>
	 * avaible to Gralog. 
	 * 
	 */
	
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
	 * for the properties <b>graph</b> and <b>graphType</b>
	 * defined above. It checks filters all
	 * {@link GralogGraphTypeInfo GralogGraphTypeInfos}
	 * whose vertices do not extends
	 * {@link de.hu.gralog.graph.types.elements.LabeledGraphVertex} and
	 * those who do not extend 
	 * {@link org.jgrapht.graph.SimpleDirectedGraph} as their
	 * underlying JGraphT-Graph. 
	 * 
	 * As mentioned in the description of the property
	 * <b>graph</b> in the {@link AdvancedProperties}-Algorithm
	 * Class this can only be done by analysing the 
	 * JGraphT-Graph that is the base of the {@link org.jgrapht.graph.ListenableGraph}
	 * that underlies this {@link GralogGraphTypeInfo}.
	 * 
	 * To archive this {@link LabeledSimpleDirectedGraphTypeInfoFilter#filterTypeInfo(GralogGraphTypeInfo)}
	 * uses the method {@link JGraphTUtils#getListenableBaseGraph(org.jgrapht.ListenableGraph)} that
	 * retrieves the base of a {@link org.jgrapht.graph.ListenableGraph} that can not
	 * be done using methods from JGraphT alone.
	 * 
	 * @author Sebastian
	 *
	 */
	public static class LabeledSimpleDirectedGraphTypeInfoFilter implements GralogGraphTypeInfoFilter {
		public boolean filterTypeInfo( GralogGraphTypeInfo typeInfo ) {
			
			if ( ! ( typeInfo.getVertexFactory().createVertex() instanceof LabeledGraphVertex ) )
				return true;
			
			ListenableGraph listenableGraph = typeInfo.getGraphFactory().createGraph();
			
			if ( !( JGraphTUtils.getListenableBaseGraph( listenableGraph ) instanceof SimpleDirectedGraph ) )
				return true;
			return false;
		}
	}
	
}
