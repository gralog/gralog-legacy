/*
 * Created on 26 Oct 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.parity.alg;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import de.hu.gralog.graph.alg.ChooseGraphPropertyDescriptor;
import de.hu.parity.graph.ParityGameGraphTypeInfo;

public class StrategyImprovementAlgorithmBeanInfo extends SimpleBeanInfo {

	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( StrategyImprovementAlgorithm.class );
	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[1];
	
	static {
		BEAN_DESCRIPTOR.setDisplayName( "StrategyImprovementAlgorithm" );
		BEAN_DESCRIPTOR.setShortDescription( 
				"<html>This algorithm computes the winning sets and strategies for both players of a max parity game. " +
				"To compute the winning sets it uses the Discrete Strategy improvement algorithm of Jens V&ouml;ge " +
				"introduced in <a href=\"http://citeseer.ist.psu.edu/rd/0%2C306405%2C1%2C0.25%2CDownload/http://citeseer.ist.psu.edu/cache/papers/cs/14285/http:zSzzSzwww-i7.informatik.rwth-aachen.dezSzzCz7evoegezSzpaperszSzCAV00.pdf/a-discrete-strategy-improvement.pdf\">promotion</a>." +
				"The output of the algorithm is a sequence of strategies for both players which are improved in each step " +
				"leading to an optimal strategy for both players." +
				"</html>" );
		try {
			PROPERTY_DESCRIPTORS[0] = new ChooseGraphPropertyDescriptor( "graph", StrategyImprovementAlgorithm.class, new ParityGameGraphTypeInfo() );
			PROPERTY_DESCRIPTORS[0].setShortDescription( "<html></html>" );
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
