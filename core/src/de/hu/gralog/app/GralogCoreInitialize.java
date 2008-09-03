package de.hu.gralog.app;

import java.beans.IntrospectionException;
import java.beans.Introspector;

import de.hu.gralog.algorithm.result.AlgorithmResult;
import de.hu.gralog.algorithm.result.AlgorithmResultContent;
import de.hu.gralog.algorithm.result.AlgorithmResultContentTreeNode;
import de.hu.gralog.algorithm.result.DisplaySubgraphMode;
import de.hu.gralog.algorithm.result.ElementTipsDisplayMode;
import de.hu.gralog.algorithm.result.DisplaySubgraph.DisplayMode;
import de.hu.gralog.algorithm.result.pd.AlgorithmResultContentPersistenceDelegate;
import de.hu.gralog.algorithm.result.pd.AlgorithmResultContentTreeNodePersistenceDelegate;
import de.hu.gralog.algorithm.result.pd.AlgorithmResultPersistenceDelegate;
import de.hu.gralog.algorithm.result.pd.DisplayModePersistenceDelegate;
import de.hu.gralog.algorithm.result.pd.DisplaySubgraphModePersistenceDelegate;
import de.hu.gralog.algorithm.result.pd.ElementTipsDisplayModePersistenceDelegate;
import de.hu.gralog.graph.GralogGraphSupport;
import de.hu.gralog.graph.pd.GralogGraphSupportPersistenceDelegate;

/**
 * This class associates persistenceDelegates to graph and AlgorithmResult
 * classes. It is called by Gralog before initializing the GUI.
 * 
 * @author Sebastian
 * 
 */
public class GralogCoreInitialize {

	public static void initialize() throws UserException {
		try {
			Introspector.getBeanInfo(GralogGraphSupport.class)
					.getBeanDescriptor().setValue("persistenceDelegate",
							new GralogGraphSupportPersistenceDelegate());

			Introspector.getBeanInfo(AlgorithmResult.class).getBeanDescriptor()
					.setValue("persistenceDelegate",
							new AlgorithmResultPersistenceDelegate());
			Introspector.getBeanInfo(AlgorithmResultContent.class)
					.getBeanDescriptor().setValue("persistenceDelegate",
							new AlgorithmResultContentPersistenceDelegate());
			Introspector
					.getBeanInfo(AlgorithmResultContentTreeNode.class)
					.getBeanDescriptor()
					.setValue(
							"persistenceDelegate",
							new AlgorithmResultContentTreeNodePersistenceDelegate());
			Introspector.getBeanInfo(DisplayMode.class).getBeanDescriptor()
					.setValue("persistenceDelegate",
							new DisplayModePersistenceDelegate());
			Introspector.getBeanInfo(DisplaySubgraphMode.class)
					.getBeanDescriptor().setValue("persistenceDelegate",
							new DisplaySubgraphModePersistenceDelegate());
			Introspector.getBeanInfo(ElementTipsDisplayMode.class)
					.getBeanDescriptor().setValue("persistenceDelegate",
							new ElementTipsDisplayModePersistenceDelegate());
		} catch (IntrospectionException e) {
			throw new UserException("fatal error initializing gralog-core", e);
		}
	}

}
