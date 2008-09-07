package de.hu.example.alg;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * This is the <b>BeanInfo</b>-class for the {@link SimpleProperties}
 * algorithm. It defines a {@link java.beans.BeanDescriptor} that
 * has a <b>displayName</b> and a <b>shortDescription</b>, that
 * will be displayed to the User as the name and description of
 * the Algorithm <b>SimpleProperties</b>. It also
 * defines {@link java.beans.PropertyDescriptor PropertyDescriptors} for
 * this algorithm, that provide a description for each property. 
 * 
 * @author Sebastian
 *
 */
public class SimplePropertiesBeanInfo extends SimpleBeanInfo {

	/**
	 * This is the BeanDescriptor for this class, whose <b>displayName</b> and
	 * <b>shortDescription</b> are used by Gralog to give this algorithm
	 * a name and a description.
	 * 
	 */
	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( SimpleProperties.class );
	
	/**
	 * These are the {@link java.beans.PropertyDescriptor PropertyDescriptors} for the
	 * properties of our Algorithm. Since our Algorithm takes four
	 * parameters, i.e. <b>anInt</b>, <b>aDouble</b>, <b>aBoolean</b> and <b>aString</b>, we
	 * need to define four of them.
	 * 
	 */
	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[4];
	
	static {
		/**
		 * First we set the name for our Algorithm, that is displayed to the User.
		 * 
		 */
		BEAN_DESCRIPTOR.setDisplayName( "SimpleProperties" );
		
		/**
		 * Now we set the description for our Algorithm, that again will be displayed
		 * to the user. Note that you are allowed to use arbitrary html in this String.
		 * 
		 */
		BEAN_DESCRIPTOR.setShortDescription( "<html>" +
				"This algorithm shows how to take four parameters that" + 
				"have simple values." + 
				"</html>" );
		
		/**
		 * We are now ready to define the four properties and provide them with a description.
		 * 
		 */
		
		try {
			/**
			 * This defines the first property of the algorithm <b>anInt</b>. Since
			 * this property, like all our properties in this example, takes
			 * a standard value, i.e. an <b>int</b> we can use the standard 
			 * {@link java.beans.PropertyDescriptor} in this case. 
			 * 
			 */
			PROPERTY_DESCRIPTORS[0] = new PropertyDescriptor( "anInt", SimpleProperties.class );
			/**
			 * And now we provide our first property with a <b>description</b>.
			 * Note that you are again allowed to use arbitrary html in this String.
			 * 
			 */
			PROPERTY_DESCRIPTORS[0].setShortDescription( "<html> " +
						"This is a simple property that has an <b>int</b> as value <br>" + 
						"and uses the standard <b>java.beans.PropertyDescriptor</b>." + 
						"</html>" );

			/**
			 * This defines our second property of the algorithm <b>aDouble</b>. 
			 * We again use the standard {@link java.beans.PropertyDescriptor}
			 * to define it.
			 * 
			 */
			PROPERTY_DESCRIPTORS[1] = new PropertyDescriptor( "aDouble", SimpleProperties.class );
			
			/**
			 * And now we provide a <b>description</b> for <b>aDouble</b>.
			 * Note that you are again allowed to use arbitrary html in this String.
			 * 
			 */
			PROPERTY_DESCRIPTORS[1].setShortDescription( "<html> " +
						"This is a simple property that has a <b>double</b> as value <br>" + 
						"and uses the standard <b>java.beans.PropertyDescriptor</b>." + 
						"</html>" );

			/**
			 * This defines our third property of the algorithm <b>aBoolean</b>. 
			 * We again use the standard {@link java.beans.PropertyDescriptor}
			 * to define it.
			 * 
			 */
			PROPERTY_DESCRIPTORS[2] = new PropertyDescriptor( "aBoolean", SimpleProperties.class );
			
			/**
			 * And now we provide a <b>description</b> for <b>aBoolean</b>.
			 * Note that you are again allowed to use arbitrary html in this String.
			 * 
			 */
			PROPERTY_DESCRIPTORS[2].setShortDescription( "<html> " +
						"This is a simple property that has a <b>boolean</b> as value <br>" +
						"and uses the standard <b>java.beans.PropertyDescriptor</b>." + 
						"</html>" );

			/**
			 * This defines our forth property of the algorithm <b>aString</b>. 
			 * We again use the standard {@link java.beans.PropertyDescriptor}
			 * to define it.
			 * 
			 */
			PROPERTY_DESCRIPTORS[3] = new PropertyDescriptor( "aString", SimpleProperties.class );
			
			/**
			 * And now we provide a <b>description</b> for <b>aString</b>.
			 * Note that you are again allowed to use arbitrary html in this String.
			 * 
			 */
			PROPERTY_DESCRIPTORS[3].setShortDescription( "<html> " +
						"This is a simple property that has a <b>String</b> as value <br>" +
						"and uses the standard <b>java.beans.PropertyDescriptor</b>." + 
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

}
