package de.hu.example.alg;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import de.hu.gralog.beans.propertydescriptor.ChooseStructurePropertyDescriptor;
import de.hu.gralog.structure.StructureTypeInfo;
import de.hu.gralog.structure.StructureTypeInfoFilter;

/**
 * This is the <b>BeanInfo</b>-class for the {@link ShortestPathInteractive}
 * algorithm.  
 * 
 * The only porpose of this <b>BeanInfo</b>-class is to
 * define the {@link ChooseStructurePropertyDescriptor} for the 
 * property <b>structure</b> of {@link ShortestPathInteractive}.
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
				"to allow user-interaction after the " +
				"result is displayed to the user." +
				"It also shows you how to define a " +
				"<b>java.beans.Customizer</b> for a bean." +
				"</html>" );
		
		
		try {
			PROPERTY_DESCRIPTORS[0] = new ChooseStructurePropertyDescriptor( "structure", ShortestPathInteractive.class, new AllStructuresTypeInfoFilter() );
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
	 * This class serves as a {@link StructureTypeInfoFilter}
	 * for the property <b>structure</b>
	 * defined above. It accepts every structure.
	 *  
	 * @author Sebastian
	 *
	 */
	public static class AllStructuresTypeInfoFilter implements StructureTypeInfoFilter {
		public boolean filterTypeInfo( StructureTypeInfo typeInfo ) {
			return false;
		}
	}
	
}
