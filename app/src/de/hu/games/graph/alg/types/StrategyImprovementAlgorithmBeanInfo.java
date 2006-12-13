/*
 * Created on 26 Oct 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.games.graph.alg.types;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import de.hu.games.app.UserException;
import de.hu.games.graph.LabeledDirectedGraphTypeInfo;
import de.hu.games.graph.alg.ChooseGraphPropertyDescriptor;
import de.hu.games.graph.types.GameGraphTypeInfo;
import de.hu.games.graph.types.ParityGameGraphTypeInfo;
import de.hu.games.gui.MainPad;

public class StrategyImprovementAlgorithmBeanInfo extends SimpleBeanInfo {

	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( StrategyImprovementAlgorithm.class );
	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[1];
	
	static {
		BEAN_DESCRIPTOR.setDisplayName( "StrategyImprovementAlgorithm" );
		BEAN_DESCRIPTOR.setShortDescription( 
				"<html></html>" );
		try {
			PROPERTY_DESCRIPTORS[0] = new ChooseGraphPropertyDescriptor( "graph", StrategyImprovementAlgorithm.class, new ParityGameGraphTypeInfo() );
			PROPERTY_DESCRIPTORS[0].setShortDescription( "<html></html>" );
		} catch( IntrospectionException e ) {
			MainPad.getInstance().handleUserException( new UserException( "internal error" , e ) );
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
