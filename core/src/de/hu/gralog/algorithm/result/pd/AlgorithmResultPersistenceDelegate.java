/*
 * Created on 2008 by Sebastian Ordyniak
 *
 * Copyright 2008 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of GrALoG.
 *
 * GrALoG is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * GrALoG is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GrALoG; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */

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

		if (result.getStructure() != null)
			return new Expression(oldInstance, oldInstance.getClass(), "new",
					new Object[] { result.getStructure() });
		return new Expression(oldInstance, oldInstance.getClass(), "new", null);
	}

}
