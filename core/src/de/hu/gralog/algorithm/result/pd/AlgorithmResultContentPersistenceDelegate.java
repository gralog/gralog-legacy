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

package de.hu.gralog.algorithm.result.pd;

import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.Statement;
import java.util.Hashtable;
import java.util.Map;

import de.hu.gralog.algorithm.result.AlgorithmResultContent;
import de.hu.gralog.algorithm.result.AlgorithmResultContent.SubgraphInfo;
import de.hu.gralog.app.UserException;

public class AlgorithmResultContentPersistenceDelegate extends
		DefaultPersistenceDelegate {

	@Override
	protected void initialize(Class<?> type, Object oldInstance,
			Object newInstance, Encoder out) {
		try {
			AlgorithmResultContent content = (AlgorithmResultContent) oldInstance;

			if (content.getName() != null)
				out.writeStatement(new Statement(oldInstance, "setName",
						new Object[] { content.getName() }));

			if (content.getGraphSupport() != null)
				out.writeStatement(new Statement(oldInstance,
						"setGraphSupport", new Object[] { content
								.getGraphSupport() }));

			if (content.getSubgraphsInfo() != null) {
				for (Map.Entry<String, SubgraphInfo> entry : content
						.getSubgraphsInfo().entrySet())
					out.writeStatement(new Statement(oldInstance,
							"addDisplaySubgraph", new Object[] {
									entry.getKey(),
									entry.getValue().getVertices(),
									entry.getValue().getEdges() }));
			}

			if (content.getTips() != null) {
				for (Map.Entry<String, Hashtable> entry : content.getTips()
						.entrySet())
					out.writeStatement(new Statement(oldInstance,
							"addElementTips", new Object[] { entry.getKey(),
									entry.getValue() }));
			}
		} catch (UserException e) {
			out.getExceptionListener().exceptionThrown(e);
		}
	}

	@Override
	protected Expression instantiate(Object oldInstance, Encoder out) {
		return super.instantiate(oldInstance, out);
	}

}
