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

import de.hu.gralog.beans.propertydescriptor.ChooseStructurePropertyDescriptor;
import de.hu.parity.structure.ParityGameArenaTypeInfo;

public class StrategyImprovementAlgorithmBeanInfo extends SimpleBeanInfo {

	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( StrategyImprovementAlgorithm.class );
	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[1];
	
	static {
		BEAN_DESCRIPTOR.setDisplayName( "StrategyImprovementAlgorithm" );
		BEAN_DESCRIPTOR.setShortDescription( 
				"<html> " +
				"This algorithm uses the Discrete Strategy Improvement Algorithm " +
				"introduced by Jens V&ouml;ge " +
				" in " +
				"<a href=\"http://citeseer.ist.psu.edu/rd/0%2C306405%2C1%2C0.25%2CDownload/http://citeseer.ist.psu.edu/cache/papers/cs/14285/http:zSzzSzwww-i7.informatik.rwth-aachen.dezSzzCz7evoegezSzpaperszSzCAV00.pdf/a-discrete-strategy-improvement.pdf\">promotion</a> " +
				"to compute the winning sets and strategies for both players " +
				"in a max-parity-game. " +
				"It is indented to show how this algorithm works " +
				"and provides a very detailed result.<br><br>" +
				"<b>Important:</b><br>" +
				"In order for this algorithm to work the <i>parityGameArena</i> " +
				"is only allowed to have edges from player0-vertices to player1-vertices " +
				"and vice-versa, but no edges from and to vertices belonging to the same player. " +
				"</html>" );
		try {
			PROPERTY_DESCRIPTORS[0] = new ChooseStructurePropertyDescriptor( "parityGameArena", StrategyImprovementAlgorithm.class, new ParityGameArenaTypeInfo() );
			PROPERTY_DESCRIPTORS[0].setShortDescription( "<html>" +
					"The <i>parityGameArena</i>. See the description of this algorithm for further information." +
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
