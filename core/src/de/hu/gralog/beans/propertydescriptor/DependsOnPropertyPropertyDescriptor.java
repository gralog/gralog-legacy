package de.hu.gralog.beans.propertydescriptor;

import java.beans.PropertyDescriptor;
import java.util.Set;

/**
 * This interface allows to define PropertyDescriptors whose value depends on
 * other properties in the bean. If this properties get updated by the user
 * Gralog will reddisplay the PropertyEditor corresponding to this
 * PropertyDescriptor.
 * 
 * @author Sebastian
 * 
 */
public interface DependsOnPropertyPropertyDescriptor {
	/**
	 * 
	 * 
	 * @return a set of PropertyDescriptors this Property depends on
	 */
	public Set<PropertyDescriptor> getDependsOnPropertyDescriptors();
}
