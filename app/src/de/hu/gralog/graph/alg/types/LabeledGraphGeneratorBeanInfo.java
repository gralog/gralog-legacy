/*
 * Created on 26 Oct 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.graph.alg.types;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class LabeledGraphGeneratorBeanInfo extends SimpleBeanInfo {

	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( LabeledGraphGenerator.class );
	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[3];
	
	static {
		BEAN_DESCRIPTOR.setDisplayName( "Generator for labeled graphs" );
		BEAN_DESCRIPTOR.setShortDescription( 
				"<html>This algorithm computes a random graph G(<b>vertexCount</b>,<b>egdePropabilty</b>). " +
				"The graph is directed if <b>directed</b> is set to true, otherwise the graph is undirected. " +
				"</html>" );
		
		try {
			PROPERTY_DESCRIPTORS[0] = new PropertyDescriptor( "directed", LabeledGraphGenerator.class );
			PROPERTY_DESCRIPTORS[0].setShortDescription( "<html>Set to true if you want to generate an directed graph</html>" );
			
			PROPERTY_DESCRIPTORS[1] = new PropertyDescriptor( "vertexCount", LabeledGraphGenerator.class );
			PROPERTY_DESCRIPTORS[1].setShortDescription( "<html>The number of vertexes in the generated graph.</html>" );

			PROPERTY_DESCRIPTORS[2] = new PropertyDescriptor( "edgePropability", LabeledGraphGenerator.class );
			PROPERTY_DESCRIPTORS[2].setShortDescription( "<html>The propabilty for an edge between to vertexes in G.</html>" );
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
