/*
 * Created on 26 Oct 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.logic.graph;

import java.beans.BeanDescriptor;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class TransitionSystemBeanInfo extends SimpleBeanInfo {
	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( TransitionSystem.class, TransitionSystemCustomizer.class );
	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[1];
	
	static {
		try {
			PROPERTY_DESCRIPTORS[0] = new IndexedPropertyDescriptor( "propositions", TransitionSystem.class );
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
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