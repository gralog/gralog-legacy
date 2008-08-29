package de.hu.gralog.graph;

import java.beans.BeanDescriptor;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.SimpleBeanInfo;
import java.beans.Statement;

import de.hu.gralog.graph.types.GralogGraphTypeInfo;

public class GralogGraphBeanInfo extends SimpleBeanInfo {

	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( GralogGraph.class );
	
	static {
		BEAN_DESCRIPTOR.setValue( "persistenceDelegate", new GralogGraphPersistenceDelegate() );
	}

	@Override
	public BeanDescriptor getBeanDescriptor() {
		return BEAN_DESCRIPTOR;
	}
	
	public static class GralogGraphPersistenceDelegate extends DefaultPersistenceDelegate {

		@Override
		protected void initialize(Class<?> type, Object oldInstance, Object newInstance, Encoder out) {
			
			GralogGraph graph = (GralogGraph)oldInstance;
			
			for ( Object vertex : graph.vertexSet() )
				out.writeStatement( new Statement( oldInstance, "addVertex", new Object[] { vertex } ) );
			for ( Object edge : graph.edgeSet() )
				out.writeStatement( new Statement( oldInstance, "addEdge", new Object[] { graph.getEdgeSource( edge ), graph.getEdgeTarget( edge ), edge } ) );
		}

		@Override
		protected Expression instantiate(Object oldInstance, Encoder out) {
			GralogGraphTypeInfo typeInfo = ((GralogGraph) oldInstance).getGralogSupport().getTypeInfo();
			Object bean = ((GralogGraph) oldInstance).getGralogSupport().getGraphBean();

			if ( bean == null )
				return new Expression( oldInstance, GralogGraphFactory.class, "createGraph", new Object[] { typeInfo } );
			else
				return new Expression( oldInstance, GralogGraphFactory.class, "createGraph", new Object[] { typeInfo, bean } );
		}
		
	}
}