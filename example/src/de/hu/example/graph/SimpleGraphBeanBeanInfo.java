package de.hu.example.graph;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import de.hu.gralog.beans.propertydescriptor.ChooseGraphElementPropertyDescriptor;
import de.hu.gralog.beans.propertydescriptor.GraphElementFilter.VertexGraphElementFilter;

/**
 * This is the <b>BeanInfo</b>-class for the {@link SimpleGraphBean}-
 * GraphBean. The only reason for this BeanInfo-Class is
 * that the property <b>goodVertex</b> of {@link SimpleGraphBean}
 * needs {@link de.hu.gralog.beans.propertyeditor.ChooseGraphElementPropertyEditor}
 * to let the user choose among the vertices of the graph. 
 * 
 * @author Sebastian
 *
 */
public class SimpleGraphBeanBeanInfo extends SimpleBeanInfo {

	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[1];
	
	static {
		try {
			PROPERTY_DESCRIPTORS[0] = new ChooseGraphElementPropertyDescriptor( "goodVertex", SimpleGraphBean.class, new VertexGraphElementFilter() );
			PROPERTY_DESCRIPTORS[0].setShortDescription( "<html> " +
						"This is the good vertex." +
						"</html>" );
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
		return PROPERTY_DESCRIPTORS;
	}	
}
