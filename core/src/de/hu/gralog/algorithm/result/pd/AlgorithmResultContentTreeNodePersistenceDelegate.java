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
