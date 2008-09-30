package de.hu.dfa.structure;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class AutomatonVertexBeanInfo extends SimpleBeanInfo {

	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[2];
	
	static {		
		try {
			PROPERTY_DESCRIPTORS[0] = new PropertyDescriptor( "label", AutomatonVertex.class );
			PROPERTY_DESCRIPTORS[0].setShortDescription( 
					"<html>" + 
					"The label for this state." +
					"</html>"
					);
			PROPERTY_DESCRIPTORS[1] = new PropertyDescriptor( "acceptingState", AutomatonVertex.class );
			PROPERTY_DESCRIPTORS[0].setShortDescription( 
					"<html>" +
					"If <i>true</i> the state is an accepting-state." +
					"</html>"
					);

		} catch( IntrospectionException e ) {
			e.printStackTrace();
		}
	}

	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
		return PROPERTY_DESCRIPTORS;
	}
	
}