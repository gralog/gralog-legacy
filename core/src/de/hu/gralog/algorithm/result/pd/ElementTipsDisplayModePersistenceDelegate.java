package de.hu.gralog.algorithm.result.pd;

import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.Statement;

import de.hu.gralog.algorithm.result.ElementTipsDisplayMode;

public class ElementTipsDisplayModePersistenceDelegate extends
		DefaultPersistenceDelegate {

	@Override
	protected void initialize(Class<?> type, Object oldInstance,
			Object newInstance, Encoder out) {
		ElementTipsDisplayMode mode = (ElementTipsDisplayMode) oldInstance;

		out.writeStatement(new Statement(oldInstance, "setVisible",
				new Object[] { mode.isVisible() }));
	}

	@Override
	protected Expression instantiate(Object oldInstance, Encoder out) {

		return super.instantiate(oldInstance, out);
	}

}
