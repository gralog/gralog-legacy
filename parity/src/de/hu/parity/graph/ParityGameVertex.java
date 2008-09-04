/*
 * Created on 2006 by Sebastian Ordyniak
 *
 * Copyright 2006 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of Games.
 *
 * Games is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * Games is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Games; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */

package de.hu.parity.graph;

import de.hu.gralog.finitegames.graph.GameGraphVertex;



public class ParityGameVertex extends GameGraphVertex {

	private int priority = 0;
	
	public ParityGameVertex() {
		super();
	}
	
	public ParityGameVertex(String label, int priority, boolean player0) {
		super( label, player0 );
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		int oldValue = priority;
		this.priority = priority;
		propertyChangeSupport.firePropertyChange( "priority", oldValue, priority );
		displayChangeSupport.fireDisplayChangeDefault();
	}

	public String toString() {
		return getLabel() + " : " + Integer.toString(priority);
	}
}
