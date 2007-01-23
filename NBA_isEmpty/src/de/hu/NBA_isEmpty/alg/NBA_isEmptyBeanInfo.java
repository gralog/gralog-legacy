package de.hu.NBA_isEmpty.alg;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import de.hu.gralog.graph.alg.ChooseGraphPropertyDescriptor;
import de.hu.gralog.graph.LabeledDirectedGraphTypeInfo;

public class NBA_isEmptyBeanInfo extends SimpleBeanInfo {

	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( NBA_isEmpty.class );
	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[1];
	
	static {
		BEAN_DESCRIPTOR.setDisplayName( "Simple Game Algorithm" );
		BEAN_DESCRIPTOR.setShortDescription( 
				"<html>" +
				"This algorithm checks, whether the NBA defines an empty language." +
				"</html>" 
				);
		
		try {
			PROPERTY_DESCRIPTORS[0] = new ChooseGraphPropertyDescriptor( "gameGraph", NBA_isEmpty.class, new LabeledDirectedGraphTypeInfo() );
			PROPERTY_DESCRIPTORS[0].setShortDescription( "<html>This is  the graph.</html>" );
			
//			PROPERTY_DESCRIPTORS[1] = new ChooseVertexPropertyDescriptor( "startVertex", TestAlg.class, new LabeledGraphVertex() );
//			PROPERTY_DESCRIPTORS[1].setShortDescription( "<html>" +
//					"Select the <b>Vertex</b>, where the algorithm shall start.</html>"
//					);
		} catch( IntrospectionException e ) {
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
	
}
