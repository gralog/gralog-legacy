package de.hu.dfa.alg;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import de.hu.dfa.structure.AutomatonTypeInfo;
import de.hu.gralog.beans.propertydescriptor.ChooseStructurePropertyDescriptor;

public class ProcessingBeanInfo extends SimpleBeanInfo {

	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( Processing.class );
	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[3];
	
	static {
		BEAN_DESCRIPTOR.setDisplayName( "DFA Processing" );
		BEAN_DESCRIPTOR.setShortDescription( 
				"<html>" +
				"This algorithm shows how a <i>deterministic " +
				"finite state automaton</i> processes it's input.<br><br> " +
				"<b>Important:</b><br>" +
				"<i>Automaton</i> has to be <b>deterministic</b>, " +
				"otherwise the result of this algorithm is not defined." +
				"</html>" 
				);
		
		try {
			PROPERTY_DESCRIPTORS[0] = new PropertyDescriptor( "showSteps", Processing.class );
			PROPERTY_DESCRIPTORS[0].setShortDescription( "<html>" +
					"Set to <i>true</i> if you want to see " +
					"the steps needed by <i>automaton</i> to process the <i>inputPhrase</i>." +
					"</html>"
					);

			PROPERTY_DESCRIPTORS[1] = new ChooseStructurePropertyDescriptor( "automaton", Processing.class, new AutomatonTypeInfo() );
			PROPERTY_DESCRIPTORS[1].setShortDescription( "<html>" +
					"The <b>deterministic</b> finite state automaton processing the <i>inputPhrase</i>. " +
					"</html>" );

			PROPERTY_DESCRIPTORS[2] = new PropertyDescriptor( "inputPhrase", Processing.class );
			PROPERTY_DESCRIPTORS[2].setShortDescription( "<html>" +
					"The <i>inputPhrase</i> to be processed by <i>automaton</i>. <br><br>" +
					"<b>Important:</b><br> " +
					"The inputPhrase has to be a sequence of symbols " +
					"divided by \",\". For example: <i>\"a1,bar,foo\"</i>." +
					"</html>"
					);
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
