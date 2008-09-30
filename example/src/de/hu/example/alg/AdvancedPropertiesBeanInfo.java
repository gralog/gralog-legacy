package de.hu.example.alg;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.SimpleDirectedGraph;

import de.hu.example.alg.AdvancedProperties.WeekDay;
import de.hu.gralog.beans.propertydescriptor.ChooseDependableStructureElementPropertyDescriptor;
import de.hu.gralog.beans.propertydescriptor.ChooseStructurePropertyDescriptor;
import de.hu.gralog.beans.propertydescriptor.ChooseStructureTypeInfoPropertyDescriptor;
import de.hu.gralog.beans.propertydescriptor.EnumPropertyDescriptor;
import de.hu.gralog.beans.propertydescriptor.StructureElementFilter.VertexStructureElementFilter;
import de.hu.gralog.jgrapht.util.JGraphTUtils;
import de.hu.gralog.structure.StructureTypeInfo;
import de.hu.gralog.structure.StructureTypeInfoFilter;
import de.hu.gralog.structure.types.elements.LabeledStructureVertex;

/**
 * This is the <b>BeanInfo</b>-class for the {@link AdvancedProperties}
 * algorithm.  
 * 
 * It shows you how to use
 * {@link de.hu.gralog.beans.propertydescriptor.ChooseStructurePropertyDescriptor},
 * {@link de.hu.gralog.beans.propertydescriptor.ChooseDependableStructureElementPropertyDescriptor},
 * {@link de.hu.gralog.beans.propertydescriptor.ChooseStructureTypeInfoPropertyDescriptor} and
 * {@link de.hu.gralog.beans.propertydescriptor.EnumPropertyDescriptor} as
 * descriptors for the properties of your algorithm.
 * 
 * @author Sebastian
 *
 */
public class AdvancedPropertiesBeanInfo extends SimpleBeanInfo {

	/**
	 * This is the BeanDescriptor for this class, whose <b>displayName</b> and
	 * <b>shortDescription</b> are used by GrALoG to give this algorithm
	 * a name and a description.
	 * 
	 */
	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( AdvancedProperties.class );
	
	/**
	 * These are the {@link java.beans.PropertyDescriptor PropertyDescriptors} for the
	 * properties of our Algorithm. Since our Algorithm takes four
	 * parameters, i.e. <b>structure</b>, <b>vertexOfStructure</b>, <b>structureType</b> and <b>WeekDay</b>, we
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
				"<b>PropertyDescriptors</b> that are available in GrALoG. " + 
				"</html>" );
		
		/**
		 * We are now ready to define the four properties and provide them with a description.
		 * 
		 */
		
		try {
			/**
			 * This defines the first property <b>structure</b> of the algorithm
			 * {@link AdvancedProperties}. 
			 * Since we want to let the user choose a GrALoG-Structure as a value
			 * for this property we have to use a 
			 * {@link de.hu.gralog.beans.propertydescriptor.ChooseStructurePropertyDescriptor} as
			 * the PropertyDescriptor for this property. As mentioned in
			 * the description for this property {@link AdvancedProperties}, 
			 * we provide our own {@link StructureTypeInfoFilter} to
			 * allow only {@link org.jgrapht.graph.SimpleDirectedGraph} that
			 * have {@link de.hu.gralog.structure.types.elements.LabeledStructureVertex} as
			 * vertices to be selected. See below ( {@link LabeledSimpleDirectedGraphStructureTypeInfoFilter} )
			 * for the definition of this StructureTypeInfoFilter.
			 * 
			 * Note: The last parameter for the constructor tells GrALoG
			 * whether to make a copy of the selected structure before it is
			 * passed to the algorithm. Not making a copy can
			 * be useful for algorithm that either:
			 * 
			 * <ul>
			 * 		<li>do not manipulate the given structure and do not
			 * 		return the structure as part of an 
			 * 		{@link de.hu.gralog.algorithm.AlgorithmResult} - like this algorithm - and
			 * 		thus want to avoid GrALoG spending time on copying the structure.
			 * 		</li>
			 * 		<li>want to manipulate the structure explicitly - and
			 * 		again do not return the structure as part of a
			 * 		{@link de.hu.gralog.algorithm.result.AlgorithmResult} -
			 * 		, e.g. to provide some automatic editing functionality to the user.</li>
			 * </ul>
			 * 
			 * Nevertheless for algorithms that use the structure in an 
			 * {@link de.hu.gralog.algorithm.result.AlgorithmResult}, it
			 * is strongly recommend to set this option to true, as otherwise
			 * the result shown to the user and the structure-document containing the
			 * structure are mutually affected by each others changes.
			 * 
			 */
			PROPERTY_DESCRIPTORS[0] = new ChooseStructurePropertyDescriptor( "structure", AdvancedProperties.class, new LabeledSimpleDirectedGraphStructureTypeInfoFilter(), false );
			PROPERTY_DESCRIPTORS[0].setShortDescription( "<html> " +
						"This property shows you how to use <br>" +
						"<b>ChooseStructurePropertyDescriptor</b> <br>" +
						" and <b>StructureTypeInfoFilter</b> <br>" +
						" in order to let you choose a <b>SimpleDirectedGraph</b> <br>" +
						" with <b>labeled</b> vertices." +
						"</html>" );

			/**
			 * This defines the second property <b>vertexOfStructure</b> for the algorithm
			 * {@link AdvancedProperties}.
			 *  
			 * This time we have to use 
			 * {@link de.hu.gralog.beans.propertydescriptor.ChooseDependableStructureElementPropertyDescriptor}.
			 * 
			 *  As you can see the third parameter of it's constructor tells it
			 *  to select elements from the structure that is
			 *  a value of the property <b>structure</b> defined above and
			 *  for it's last parameter we use
			 *  {@link de.hu.gralog.beans.propertydescriptor.StructureElementFilter.VertexStructureElementFilter}
			 *  to restrict the user to choose
			 *  among the vertices of this structure.
			 *  
			 */
			PROPERTY_DESCRIPTORS[1] = new ChooseDependableStructureElementPropertyDescriptor( "vertexOfStructure", AdvancedProperties.class, (ChooseStructurePropertyDescriptor)PROPERTY_DESCRIPTORS[0], new VertexStructureElementFilter() );
			PROPERTY_DESCRIPTORS[1].setShortDescription( "<html> " +
						"This property shows you how to use <br>" +
						"<b>ChooseDependableStructureElementPropertyDescriptor</b> " +
						"and <b>VertexStructureElementFilter</b> <br>" +
						"to let you choose a vertex from <br>" +
						"the <b>structure</b> that is a value of <br>" +
						"the property <b>structure</b>." +
						"</html>" );

			/**
			 * This defines the third property <b>structureType</b> of the algorithm
			 * {@link AdvancedProperties}.
			 * 
			 * It shows how to use 
			 * {@link de.hu.gralog.beans.propertydescriptor.ChooseStructureTypeInfoPropertyDescriptor}
			 * to let the user choose a GrALoG-Structure-Type. We again
			 * use {@link LabeledSimpleDirectedGraphStructureTypeInfoFilter} to
			 * restrict the user to Structure-Types that
			 * have a {@link org.jgrapht.graph.SimpleDirectedGraph} as 
			 * underlying JGraphT-Graph and  
			 * {@link de.hu.gralog.structure.types.elements.LabeledStructureVertex} as 
			 * vertices. 
			 */
			PROPERTY_DESCRIPTORS[2] = new ChooseStructureTypeInfoPropertyDescriptor( "structureType", AdvancedProperties.class, new LabeledSimpleDirectedGraphStructureTypeInfoFilter() );
			PROPERTY_DESCRIPTORS[2].setShortDescription( "<html> " +
					"This property shows you how to use <br>" +
					"<b>ChooseStructureTypeInfoPropertyDescriptor</b> <br>" +
					"to let you choose a <b>Gralog-Structure-Type</b> <br>" +
					"that has <b>LabeledStructureVertex</b> as <br>" +
					"it's vertices and a <b> SimpleDirectedGraph</b> <br>" +
					"as it's underlying JGraphT-Graph." +
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
	 * avaible to GrALoG. 
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
	 * This class serves as a {@link StructureTypeInfoFilter}
	 * for the properties <b>structure</b> and <b>structureType</b>
	 * defined above. It filters all
	 * {@link StructureTypeInfo StructureTypeInfos}
	 * whose vertices do not extends
	 * {@link de.hu.gralog.structure.types.elements.LabeledStructureVertex} and
	 * those who do not extend 
	 * {@link org.jgrapht.graph.SimpleDirectedGraph} as their
	 * underlying JGraphT-Graph. 
	 * 
	 * As mentioned in the description of the property
	 * <b>structure</b> in the {@link AdvancedProperties}-Algorithm
	 * Class this can only be done by analysing the 
	 * JGraphT-Graph that is the base of the {@link org.jgrapht.graph.ListenableGraph}
	 * that underlies this {@link StructureTypeInfo}.
	 * 
	 * To archive this {@link LabeledSimpleDirectedGraphStructureTypeInfoFilter#filterTypeInfo(StructureTypeInfo)}
	 * uses the method {@link JGraphTUtils#getListenableBaseGraph(org.jgrapht.ListenableGraph)} that
	 * retrieves the base of a {@link org.jgrapht.graph.ListenableGraph} that can not
	 * be done using methods from JGraphT alone.
	 * 
	 * @author Sebastian
	 *
	 */
	public static class LabeledSimpleDirectedGraphStructureTypeInfoFilter implements StructureTypeInfoFilter {
		public boolean filterTypeInfo( StructureTypeInfo typeInfo ) {
			
			if ( ! ( typeInfo.getVertexFactory().createVertex() instanceof LabeledStructureVertex ) )
				return true;
			
			ListenableGraph listenableGraph = typeInfo.getGraphFactory().createGraph();
			
			if ( !( JGraphTUtils.getListenableBaseGraph( listenableGraph ) instanceof SimpleDirectedGraph ) )
				return true;
			return false;
		}
	}
	
}
