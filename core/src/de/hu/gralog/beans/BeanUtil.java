package de.hu.gralog.beans;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import de.hu.gralog.app.UserException;

public class BeanUtil {

	public static final Class getPropertyTypeClass(PropertyDescriptor propertyDescriptor ) {
		Class propType = propertyDescriptor.getPropertyType();
		if ( propType.isPrimitive() ) {
			if ( propType.getName().equals( "boolean" ) )
				return Boolean.class;
			if ( propType.getName().equals( "byte" ) )
				return Byte.class;
			if ( propType.getName().equals( "double" ) )
				return Double.class;
			if ( propType.getName().equals( "float" ) )
				return Float.class;
			if ( propType.getName().equals( "int" ) )
				return Integer.class;
			if ( propType.getName().equals( "long" ) )
				return Long.class;
			if ( propType.getName().equals( "short" ) )
				return Short.class;
		}
		return propType;
	}
	
	public static final Object getValue( Object bean, PropertyDescriptor propertyDescriptor ) throws UserException {
		try {
			return propertyDescriptor.getReadMethod().invoke( bean, new Object[] { } );
		} catch (IllegalArgumentException e) {
			throw new UserException( "unable to get property: " + propertyDescriptor.getName() + " from bean: " + bean, e );
		} catch (IllegalAccessException e) {
			throw new UserException( "unable to get property: " + propertyDescriptor.getName() + " from bean: " + bean, e );
		} catch (InvocationTargetException e) {
			throw new UserException( "unable to get property: " + propertyDescriptor.getName() + " from bean: " + bean, e );
		}
	}

	public static final void setValue( Object bean, PropertyDescriptor propertyDescriptor, Object value ) throws UserException {
		try {
			propertyDescriptor.getWriteMethod().invoke( bean, value );
		} catch (IllegalArgumentException e) {
			throw new UserException( "unable not set property: " + propertyDescriptor.getName() + " on bean: " + bean + " with value: " + value, e );
		} catch (IllegalAccessException e) {
			throw new UserException( "unable not set property: " + propertyDescriptor.getName() + " on bean: " + bean + " with value: " + value, e );
		} catch (InvocationTargetException e) {
			throw new UserException( "unable not set property: " + propertyDescriptor.getName() + " on bean: " + bean + " with value: " + value, e );
		}
	}
	
	public static final List<PropertyDescriptor> getRWPropertyDescriptors( Object bean ) throws UserException {
		List<PropertyDescriptor> propertyDescriptors = new ArrayList<PropertyDescriptor>();
		try {
			PropertyDescriptor[] allPropertyDescriptors = Introspector.getBeanInfo( bean.getClass() ).getPropertyDescriptors();

			for ( int i = 0;i < allPropertyDescriptors.length;i++ ) {
				PropertyDescriptor propertyDescriptor = allPropertyDescriptors[i];
				if ( propertyDescriptor.getReadMethod() != null && propertyDescriptor.getWriteMethod() != null )
					propertyDescriptors.add( propertyDescriptor );
			}
			
			return propertyDescriptors;
		} catch (IntrospectionException e) {
			throw new UserException( "could not introspect bean", e );
		}
	}
	
	public static final void cloneBean( Object sourceBean, Object targetBean ) throws UserException {
		List<PropertyDescriptor> propertyDescriptors = getRWPropertyDescriptors( sourceBean );
		for ( PropertyDescriptor pd : propertyDescriptors )
			setValue( targetBean, pd, getValue( sourceBean, pd ) );
	}

}
