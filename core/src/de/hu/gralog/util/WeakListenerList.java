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
package de.hu.gralog.util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class WeakListenerList<L> {

	private final ArrayList<WeakReference<L>> listenerReferences = new ArrayList<WeakReference<L>>();

	protected WeakReference<L> getReference(L listener) {
		for (WeakReference<L> lr : listenerReferences) {
			L l = lr.get();
			if (l != null && l == listener)
				return lr;
		}
		return null;
	}

	public void addListener(L listener) {
		if (getReference(listener) == null)
			listenerReferences.add(new WeakReference<L>(listener));
	}

	public void removeListener(L listener) {
		WeakReference<L> ref = getReference(listener);
		listenerReferences.remove(ref);
	}

	public ArrayList<L> getListeners() {
		ArrayList<L> listeners = new ArrayList<L>();
		for (WeakReference<L> lr : new ArrayList<WeakReference<L>>(
				listenerReferences)) {
			L l = lr.get();
			if (l != null)
				listeners.add(l);
			else
				listenerReferences.remove(lr);
		}
		return listeners;
	}
}
