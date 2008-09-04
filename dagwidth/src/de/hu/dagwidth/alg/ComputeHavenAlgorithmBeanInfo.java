/*
 * Created on 26 Oct 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.dagwidth.alg;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import de.hu.gralog.beans.propertydescriptor.ChooseGraphPropertyDescriptor;
import de.hu.gralog.graph.types.LabeledSimpleDirectedGraphTypeInfo;

public class ComputeHavenAlgorithmBeanInfo extends SimpleBeanInfo {

	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( ComputeHavenAlgorithm.class );
	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[4];
	
	static {
		BEAN_DESCRIPTOR.setDisplayName( "Compute directed havens" );
		BEAN_DESCRIPTOR.setShortDescription( 
				"<html>This algorithm computes havens for the cops and robber game on " +
				"<b>graph</b>. See " +
				"<a href=\"http://www2.informatik.hu-berlin.de/lds/publications/stacs06.pdf\">" +
				"DAG-Width and Parity Games</a> for a definition of DAGs and havens.</html>" );
		
		try {
			PROPERTY_DESCRIPTORS[0] = new ChooseGraphPropertyDescriptor( "graph", ComputeHavenAlgorithm.class, new LabeledSimpleDirectedGraphTypeInfo() );
			PROPERTY_DESCRIPTORS[0].setShortDescription( "<html>The graph for which a haven should be computed.</html>" );
			
			PROPERTY_DESCRIPTORS[1] = new PropertyDescriptor( "dagWidth", ComputeHavenAlgorithm.class );
			PROPERTY_DESCRIPTORS[1].setShortDescription( "<html>The number of cops to capture the robber.</html>" );

			PROPERTY_DESCRIPTORS[2] = new PropertyDescriptor( "robberMonotone", ComputeHavenAlgorithm.class );
			PROPERTY_DESCRIPTORS[2].setShortDescription( "<html>If <b>true</b> the cops are restricted to make robbermonotone moves.</html>" );

			PROPERTY_DESCRIPTORS[3] = new PropertyDescriptor( "copMonotone", ComputeHavenAlgorithm.class );
			PROPERTY_DESCRIPTORS[3].setShortDescription( "<html>If <b>true</b> the cops are restricted to make copmonotone moves.</html>" );
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
