package de.hu.gralog.algorithm.result.pd;

import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.Statement;
import java.util.Map;

import de.hu.gralog.algorithm.result.AlgorithmResult;
import de.hu.gralog.algorithm.result.DisplaySubgraphMode;
import de.hu.gralog.algorithm.result.ElementTipsDisplayMode;

public class AlgorithmResultPersistenceDelegate extends
		DefaultPersistenceDelegate {

	@Override
	protected void initialize(Class<?> type, Object oldInstance,
			Object newInstance, Encoder out) {
		AlgorithmResult result = (AlgorithmResult) oldInstance;

		if (result.getDescription() != null)
			out.writeStatement(new Statement(oldInstance, "setDescription",
					new Object[] { result.getDescription() }));
		if (result.getDisplaySubgraphModes() != null) {
			for (Map.Entry<String, DisplaySubgraphMode> entry : result
					.getDisplaySubgraphModes().entrySet())
				out.writeStatement(new Statement(oldInstance,
						"addDisplaySubgraphMode", new Object[] {
								entry.getKey(), entry.getValue() }));
		}

		if (result.getElementTipsDisplayModes() != null) {
			for (Map.Entry<String, ElementTipsDisplayMode> entry : result
					.getElementTipsDisplayModes().entrySet())
				out.writeStatement(new Statement(oldInstance,
						"addElementTipsDisplayMode", new Object[] {
								entry.getKey(), entry.getValue() }));
		}

		if (result.getSingleContent() != null)
			out.writeStatement(new Statement(oldInstance, "setSingleContent",
					new Object[] { result.getSingleContent() }));
		if (result.getContentList() != null)
			out.writeStatement(new Statement(oldInstance, "setContentList",
					new Object[] { result.getContentList() }));
		if (result.getContentTree() != null)
			out.writeStatement(new Statement(oldInstance, "setContentTree",
					new Object[] { result.getContentTree() }));
	}

	@Override
	protected Expression instantiate(Object oldInstance, Encoder out) {
		AlgorithmResult result = (AlgorithmResult) oldInstance;

		if (result.getGraphSupport() != null)
			return new Expression(oldInstance, oldInstance.getClass(), "new",
					new Object[] { result.getGraphSupport() });
		return new Expression(oldInstance, oldInstance.getClass(), "new", null);
	}

}
