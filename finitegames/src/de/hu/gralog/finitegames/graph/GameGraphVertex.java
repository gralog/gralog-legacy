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

package de.hu.gralog.finitegames.graph;

import de.hu.gralog.structure.types.elements.LabeledStructureVertex;


public class GameGraphVertex extends LabeledStructureVertex {

	private boolean player0 = true;
	
	public GameGraphVertex() {
		super();
	}
	
	public GameGraphVertex( String label, boolean player0 ) {
		super( label );
		this.player0 = player0;
	}

	public boolean isPlayer0() {
		return player0;
	}
	
	public void setPlayer0( boolean player0 ) {
		boolean oldValue = this.player0;
		this.player0 = player0;
		propertyChangeSupport.firePropertyChange( "player0", oldValue, player0 );
		displayChangeSupport.fireDisplayChangeDefault( );
	}
}
