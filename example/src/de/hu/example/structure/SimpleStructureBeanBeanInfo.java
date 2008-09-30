package de.hu.example.structure;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import de.hu.gralog.beans.propertydescriptor.ChooseStructureElementPropertyDescriptor;
import de.hu.gralog.beans.propertydescriptor.StructureElementFilter.VertexStructureElementFilter;

/**
 * This is the <b>BeanInfo</b>-class for the {@link SimpleStructureBean}-
 * Structure-Bean. The only reason for this BeanInfo-Class is
 * that the property <b>goodVertex</b> of {@link SimpleStructureBean}
 * needs a {@link de.hu.gralog.beans.propertyeditor.ChooseStructureElementPropertyEditor}
 * to let the user choose among the vertices of the structure. 
 * 
 * @author Sebastian
 *
 */
public class SimpleStructureBeanBeanInfo extends SimpleBeanInfo {

	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[1];
	
	static {
		try {
			PROPERTY_DESCRIPTORS[0] = new ChooseStructureElementPropertyDescriptor( "goodVertex", SimpleStructureBean.class, new VertexStructureElementFilter() );
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
