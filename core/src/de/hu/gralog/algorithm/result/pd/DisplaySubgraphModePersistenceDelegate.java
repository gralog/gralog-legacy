package de.hu.gralog.algorithm.result.pd;

import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.Statement;

import de.hu.gralog.algorithm.result.DisplaySubgraphMode;

public class DisplaySubgraphModePersistenceDelegate extends
		DefaultPersistenceDelegate {

	@Override
	protected void initialize(Class<?> type, Object oldInstance,
			Object newInstance, Encoder out) {
		DisplaySubgraphMode mode = (DisplaySubgraphMode) oldInstance;

		out.writeStatement(new Statement(oldInstance, "setVertexDisplayMode",
				new Object[] { mode.getVertexDisplayMode(false),
						mode.getVertexDisplayMode(true) }));
		out.writeStatement(new Statement(oldInstance, "setEdgeDisplayMode",
				new Object[] { mode.getEdgeDisplayMode(false),
						mode.getEdgeDisplayMode(true) }));
		out.writeStatement(new Statement(oldInstance, "setVisible",
				new Object[] { mode.isVisible() }));
	}

	@Override
	protected Expression instantiate(Object oldInstance, Encoder out) {
		return super.instantiate(oldInstance, out);
	}

}
