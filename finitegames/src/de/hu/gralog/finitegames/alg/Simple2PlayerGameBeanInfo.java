/*
 * Created on 26 Oct 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.finitegames.alg;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import de.hu.gralog.beans.propertydescriptor.ChooseStructurePropertyDescriptor;
import de.hu.gralog.finitegames.graph.GameGraphTypeInfo;

public class Simple2PlayerGameBeanInfo extends SimpleBeanInfo {

	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( Simple2PlayerGame.class );
	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[2];
	
	static {
		BEAN_DESCRIPTOR.setDisplayName( "Simple Game Algorithm" );
		BEAN_DESCRIPTOR.setShortDescription( 
				"<html>" +
				"This algorithm computes the winning sets of a simple game on a gamegraph G=(V,V0,E)." +
				"The winning condition is the emptyset, so player 1 wins all plays ending in a vertex of player 0 " +
				"and all infinite plays." +
				"</html>" 
				);
		
		try {
			PROPERTY_DESCRIPTORS[0] = new PropertyDescriptor( "showSteps", Simple2PlayerGame.class );
			PROPERTY_DESCRIPTORS[0].setShortDescription( "<html>" +
					"Set to <b>true</b> if you want to see the steps " +
					"the algorithm needs to compute the winning set " + 
					"for player 0. Set to <b>false</b> otherwise.</html>"
					);

			PROPERTY_DESCRIPTORS[1] = new ChooseStructurePropertyDescriptor( "gameGraph", Simple2PlayerGame.class, new GameGraphTypeInfo() );
			PROPERTY_DESCRIPTORS[1].setShortDescription( "<html>This is the gamegraph G=(V,V0,E).</html>" );
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
