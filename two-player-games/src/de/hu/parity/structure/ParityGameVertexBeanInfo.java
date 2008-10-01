package de.hu.parity.structure;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class ParityGameVertexBeanInfo extends SimpleBeanInfo {
	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[3];
	
	static {
		
		try {
			PROPERTY_DESCRIPTORS[0] = new PropertyDescriptor( "label", ParityGameVertex.class );
			PROPERTY_DESCRIPTORS[0].setShortDescription( "<html>" +
					"The label for that vertex." +
					"</html>" 
					);
			PROPERTY_DESCRIPTORS[1] = new PropertyDescriptor( "player0", ParityGameVertex.class );
			PROPERTY_DESCRIPTORS[1].setShortDescription( "<html>" +
					"If <i>true</i> this vertex belongs to player 0 " +
					"otherwise to player 1" +
					"</html>" 
					);
			PROPERTY_DESCRIPTORS[2] = new PropertyDescriptor( "priority", ParityGameVertex.class );
			PROPERTY_DESCRIPTORS[2].setShortDescription( "<html>" +
					"The <i>priority</i> of this vertex. " +
					"</html>" 
					);

		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
		return PROPERTY_DESCRIPTORS;
	}

}
