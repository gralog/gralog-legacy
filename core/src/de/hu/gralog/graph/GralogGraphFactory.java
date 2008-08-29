package de.hu.gralog.graph;

import java.lang.reflect.InvocationTargetException;

import de.hu.gralog.app.UserException;
import de.hu.gralog.beans.GralogGraphBean;
import de.hu.gralog.graph.types.GralogGraphTypeInfo;

public class GralogGraphFactory {

	private static <V,E,GB extends GralogGraphBean, G extends GralogGraph<V,E,GB>> G createGraph( Class<G> gralogGraphClass, GralogGraphSupport<V,E,GB,G> gralogGraphSupport ) throws UserException {
		try {
			return gralogGraphClass.getConstructor( new Class[] { GralogGraphSupport.class } ).newInstance( new Object[] { gralogGraphSupport } );
		} catch (IllegalArgumentException e) {
			throw new UserException( "unable to create GralogGraph of type: " + gralogGraphClass.getName(), e );
		} catch (SecurityException e) {
			throw new UserException( "unable to create GralogGraph of type: " + gralogGraphClass.getName(), e );
		} catch (InstantiationException e) {
			throw new UserException( "unable to create GralogGraph of type: " + gralogGraphClass.getName(), e );
		} catch (IllegalAccessException e) {
			throw new UserException( "unable to create GralogGraph of type: " + gralogGraphClass.getName(), e );
		} catch (InvocationTargetException e) {
			throw new UserException( "unable to create GralogGraph of type: " + gralogGraphClass.getName(), e );
		} catch (NoSuchMethodException e) {
			throw new UserException( "unable to create GralogGraph of type: " + gralogGraphClass.getName(), e );
		}
	}
	
	private static <V,E,GB extends GralogGraphBean,G extends GralogGraph<V,E,GB>> GralogGraphTypeInfoSupport<V,E,GB,G> createTypeInfoSupport( GralogGraphTypeInfo<V,E,GB,G> typeInfo, GB bean ) {
		return new GralogGraphTypeInfoSupport<V,E,GB,G>( typeInfo, bean );
	}
	
	public static <V,E,GB extends GralogGraphBean,G extends GralogGraph<V,E,GB>> G createGraph( GralogGraphTypeInfo<V,E,GB,G> typeInfo ) throws UserException {
		return createGraph( typeInfo, null );
	}

	static <V,E,GB extends GralogGraphBean,G extends GralogGraph<V,E,GB>> G createGraph( GralogGraphTypeInfo<V,E,GB,G> typeInfo, GB bean ) throws UserException {
		GralogGraphTypeInfoSupport<V,E,GB,G> typeInfoSupport = createTypeInfoSupport( typeInfo, bean );
		GralogGraphSupport<V,E,GB,G> gralogGraphSupport = new GralogGraphSupport<V,E,GB,G>( typeInfoSupport );
		G gralogGraph = createGraph( typeInfo.getGralogGraphClass(), gralogGraphSupport );
		gralogGraphSupport.setGraph( gralogGraph );
		return gralogGraph;
	}

}
