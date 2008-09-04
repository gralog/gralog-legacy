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

/**
 * This interface defines a listener that listens to
 * {@link AlgorithmResult AlgorithmResult's}. This listener is used by gralog
 * to inform gralog of changes in AlgorithmResult triggered by an
 * {@link AlgorithmResultInteractiveContent} and should not be used by plugin
 * developers.
 * 
 * @author Sebastian
 * 
 */
public interface AlgorithmResultListener {
	public void currentContentChanged() throws UserException;
}
