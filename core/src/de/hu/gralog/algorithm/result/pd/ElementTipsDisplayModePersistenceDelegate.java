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
