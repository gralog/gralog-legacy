package de.hu.gralog.beans.propertydescriptor;

import java.beans.PropertyDescriptor;
import java.util.Set;

public interface DependsOnPropertyPropertyDescriptor {
	public Set<PropertyDescriptor> getDependsOnPropertyDescriptors();
}
