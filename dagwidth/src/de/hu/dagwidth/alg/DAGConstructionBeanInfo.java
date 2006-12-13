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

import de.hu.games.graph.LabeledDirectedGraphTypeInfo;
import de.hu.games.graph.alg.ChooseGraphPropertyDescriptor;

public class DAGConstructionBeanInfo extends SimpleBeanInfo {

	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( DAGConstruction.class );
	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[3];
	
	static {
		BEAN_DESCRIPTOR.setDisplayName( "DAG Construction" );
		BEAN_DESCRIPTOR.setShortDescription( 
				"<html>This algorithm computes a DAG-Decomposition of <b>graph</b>. See <a href=\"http://www2.informatik.hu-berlin.de/lds/publications/stacs06.pdf\">DAG-Width and Parity Games</a> for a definition of DAG-Decomposition.</html>" );
		
		try {
			PROPERTY_DESCRIPTORS[0] = new ChooseGraphPropertyDescriptor( "graph", DAGConstruction.class, new LabeledDirectedGraphTypeInfo() );
			PROPERTY_DESCRIPTORS[0].setShortDescription( "<html>The graph for which a DAG-Decomposition should be computed.</html>" );
			
			PROPERTY_DESCRIPTORS[1] = new PropertyDescriptor( "dagWidth", DAGConstruction.class );
			PROPERTY_DESCRIPTORS[1].setShortDescription( "<html>The DAG-Width of <b>graph</b>. If 0 then the algorithms computes the DAG-Width of <b>graph</b> by itself.</html>" );

			PROPERTY_DESCRIPTORS[2] = new PropertyDescriptor( "maxDAGsCount", DAGConstruction.class );
			PROPERTY_DESCRIPTORS[2].setShortDescription( "<html>The maximal number of DAG-Decompositions to compute.</html>" );
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
