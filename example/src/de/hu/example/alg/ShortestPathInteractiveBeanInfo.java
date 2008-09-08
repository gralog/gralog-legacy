package de.hu.example.alg;

import java.beans.BeanDescriptor;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.beans.Statement;

import de.hu.gralog.app.UserException;
import de.hu.gralog.beans.propertydescriptor.ChooseGraphPropertyDescriptor;
import de.hu.gralog.graph.GralogGraphTypeInfo;
import de.hu.gralog.graph.GralogGraphTypeInfoFilter;

/**
 * This is the <b>BeanInfo</b>-class for the {@link ShortestPathInteractive}
 * algorithm.  
 * 
 * The only porpose of this <b>BeanInfo</b>-class is to
 * define the {@link ChooseGraphPropertyDescriptor} for the 
 * property <b>graph</b> of {@link ShortestPathInteractive}.
 *  
 * @author Sebastian
 *
 */
public class ShortestPathInteractiveBeanInfo extends SimpleBeanInfo {

	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( ShortestPathInteractive.class );
	
	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[1];
	
	static {
		BEAN_DESCRIPTOR.setDisplayName( "ShortestPathInteractive" );
		BEAN_DESCRIPTOR.setShortDescription( "<html>" +
				"This algorithm shows you how to use " +
				"<b>AlgorithmResultInteractiveContent</b> " +
				"to allow userinteraction after the " +
				"result is displayed to the user." +
				"It also shows you how to define a " +
				"<b>java.beans.Customizer</b> for a Bean." +
				"</html>" );
		
		
		try {
			PROPERTY_DESCRIPTORS[0] = new ChooseGraphPropertyDescriptor( "graph", ShortestPathInteractive.class, new AllGraphTypeInfoFilter() );
			PROPERTY_DESCRIPTORS[0].setShortDescription( "<html> " +
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
	 * defined above. It accepts every graph.
	 *  
	 * @author Sebastian
	 *
	 */
	public static class AllGraphTypeInfoFilter implements GralogGraphTypeInfoFilter {
		public boolean filterTypeInfo( GralogGraphTypeInfo typeInfo ) {
			return false;
		}
	}
	
}
