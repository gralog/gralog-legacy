/*
 * Created on 2006 by Sebastian Ordyniak
 *
 * Copyright 2006 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
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

package de.hu.gralog.algorithm.result;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * This class represents an element-tip that can be displayed by GrALoG.
 * Plugin-developers do not need this class.
 * 
 * @author Sebastian
 * 
 */

public class ElementTips implements ElementTipsDisplayModeListener {

	protected transient ArrayList<ElementTipsListener> listeners = new ArrayList<ElementTipsListener>();

	protected Hashtable elementTips;

	protected ElementTipsDisplayMode mode;

	public ElementTips(ElementTipsDisplayMode mode, Hashtable elementTips) {
		this.mode = mode;
		mode.addElementTipsDisplayModeListener(this);
		this.elementTips = elementTips;
	}

	public Hashtable getElementTips() {
		return elementTips;
	}

	public ElementTipsDisplayMode getMode() {
		return mode;
	}

	public void addElementTipsListener(ElementTipsListener l) {
		if (!listeners.contains(l))
			listeners.add(l);
	}

	protected void fireDataChanged() {
		for (ElementTipsListener l : listeners)
			l.elementTipsChanged();
	}

	public void elementTipsDisplayModeChanged() {
		fireDataChanged();
	}
}
