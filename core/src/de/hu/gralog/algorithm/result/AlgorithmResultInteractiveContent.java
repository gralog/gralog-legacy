/*
 * Created on 2006 by Sebastian Ordyniak
 *
 * Copyright 2006 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
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
package de.hu.gralog.algorithm.result;

import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.GralogGraphSupport;

/**
 * This class extends {@link AlgorithmResultContent} in order to add
 * interactivity to AlgorithmResultContents. It provides a protected method
 * {@link #fireContentChanged()} for subclasses to call whenever the content has
 * changed triggering gralog to redisplay the content.
 * 
 * @author Sebastian
 * 
 */

public class AlgorithmResultInteractiveContent extends AlgorithmResultContent {
	private AlgorithmResult result = null;

	/**
	 * @see AlgorithmResultContent#AlgorithmResultContent(GralogGraphSupport)
	 * 
	 * @param graphSupport
	 */

	public AlgorithmResultInteractiveContent(GralogGraphSupport graphSupport) {
		super(graphSupport);
	}

	/**
	 * This method is called by gralog before the result is displayed.
	 * 
	 * @param result
	 */
	void setResult(AlgorithmResult result) {
		this.result = result;
	}

	/**
	 * Calling this method will inform gralog to redisplay this content.
	 * 
	 * @throws UserException
	 */
	protected void fireContentChanged() throws UserException {
		result.fireCurrentContentChanged();
	}
}
