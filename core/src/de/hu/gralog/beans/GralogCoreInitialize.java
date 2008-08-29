package de.hu.gralog.beans;

import java.beans.IntrospectionException;
import java.beans.Introspector;

import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.SimpleDirectedGralogGraph;
import de.hu.gralog.graph.GralogGraphBeanInfo.GralogGraphPersistenceDelegate;

public class GralogCoreInitialize {

	public static void initialize() throws UserException {
		try {
			Introspector.getBeanInfo( SimpleDirectedGralogGraph.class ).getBeanDescriptor().setValue( "persistenceDelegate", new GralogGraphPersistenceDelegate() );
		} catch (IntrospectionException e) {
			throw new UserException( "fatal error initializing gralog-core", e );
		}
	}

}
