package de.hu.nba.alg;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import de.hu.dfa.structure.AutomatonTypeInfo;
import de.hu.gralog.beans.propertydescriptor.ChooseStructurePropertyDescriptor;

public class EmptinessTestBeanInfo extends SimpleBeanInfo {

	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( EmptinessTest.class );
	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[1];
	
	static {
		BEAN_DESCRIPTOR.setDisplayName( "NBA EmptinessTest" );
		BEAN_DESCRIPTOR.setShortDescription( 
				"<html>" +
				"This algorithm checks, whether the given non-deterministic " +
				"finite state automaton defines an empty language. " +
				"</html>" 
				);
		
		try {
			PROPERTY_DESCRIPTORS[0] = new ChooseStructurePropertyDescriptor( "automaton", EmptinessTest.class, new AutomatonTypeInfo() );
			PROPERTY_DESCRIPTORS[0].setShortDescription( "<html> " +
					"The (non-)deterministic finite state automaton." +
					"</html>" );
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
