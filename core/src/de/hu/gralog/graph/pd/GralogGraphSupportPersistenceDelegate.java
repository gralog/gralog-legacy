package de.hu.gralog.graph.pd;

import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.Statement;

import de.hu.gralog.graph.GralogGraphFactory;
import de.hu.gralog.graph.GralogGraphSupport;
import de.hu.gralog.graph.GralogGraphTypeInfo;

/**
 * 
 * @author Sebastian
 *
 */
public class GralogGraphSupportPersistenceDelegate extends
		DefaultPersistenceDelegate {

	@Override
	protected void initialize(Class<?> type, Object oldInstance,
			Object newInstance, Encoder out) {
		GralogGraphSupport support = (GralogGraphSupport) oldInstance;

		for (Object v : support.getGraph().vertexSet())
			out.writeStatement(new Statement(oldInstance, "addVertex",
					new Object[] { v }));

		for (Object e : support.getGraph().edgeSet()) {
			Object s = support.getGraph().getEdgeSource(e);
			Object t = support.getGraph().getEdgeTarget(e);
			out.writeStatement(new Statement(oldInstance, "addEdge",
					new Object[] { s, t, e }));
		}
	}

	@Override
	protected Expression instantiate(Object oldInstance, Encoder out) {
		GralogGraphSupport support = (GralogGraphSupport) oldInstance;
		GralogGraphTypeInfo typeInfo = support.getTypeInfo();
		Object bean = support.getGraphBean();

		return new Expression(oldInstance, GralogGraphFactory.class,
				"createGraphSupport", new Object[] { typeInfo, null, bean });
	}

}
