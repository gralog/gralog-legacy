/*
 * Created on 2008 by Sebastian Ordyniak
 *
 * Copyright 2008 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of Gralog.
 *
 * Gralog is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * Gralog is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Gralog; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */
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
