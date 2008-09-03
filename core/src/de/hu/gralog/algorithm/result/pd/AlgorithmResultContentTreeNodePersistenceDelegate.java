package de.hu.gralog.algorithm.result.pd;

import java.beans.Encoder;
import java.beans.Statement;

import de.hu.gralog.algorithm.result.AlgorithmResultContentTreeNode;
import de.hu.gralog.app.UserException;

public class AlgorithmResultContentTreeNodePersistenceDelegate extends
		AlgorithmResultContentPersistenceDelegate {

	@Override
	protected void initialize(Class<?> type, Object oldInstance,
			Object newInstance, Encoder out) {
		super.initialize(type, oldInstance, newInstance, out);

		AlgorithmResultContentTreeNode node = (AlgorithmResultContentTreeNode) oldInstance;
		try {
			for (AlgorithmResultContentTreeNode child : node.getChildren())
				out.writeStatement(new Statement(oldInstance, "addChild",
						new Object[] { child }));
		} catch (UserException e) {
			out.getExceptionListener().exceptionThrown(e);
		}
	}
}
