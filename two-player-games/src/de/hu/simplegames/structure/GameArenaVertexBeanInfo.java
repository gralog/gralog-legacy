package de.hu.simplegames.structure;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class GameArenaVertexBeanInfo extends SimpleBeanInfo {
	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[2];
	
	static {
		
		try {
			PROPERTY_DESCRIPTORS[0] = new PropertyDescriptor( "label", GameArenaVertex.class );
			PROPERTY_DESCRIPTORS[0].setShortDescription( "<html>" +
					"The label for that vertex." +
					"</html>" 
					);
			PROPERTY_DESCRIPTORS[1] = new PropertyDescriptor( "player0", GameArenaVertex.class );
			PROPERTY_DESCRIPTORS[1].setShortDescription( "<html>" +
					"If <i>true</i> this vertex belongs to player 0 " +
					"otherwise to player 1" +
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