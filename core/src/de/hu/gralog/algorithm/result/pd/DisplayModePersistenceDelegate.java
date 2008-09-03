package de.hu.gralog.algorithm.result.pd;

import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;

public class DisplayModePersistenceDelegate extends DefaultPersistenceDelegate {

	@Override
	protected Expression instantiate(Object oldInstance, Encoder out) {
		return new Expression(oldInstance, oldInstance.getClass(),
				"parseString", new Object[] { oldInstance.toString() });
	}

}
