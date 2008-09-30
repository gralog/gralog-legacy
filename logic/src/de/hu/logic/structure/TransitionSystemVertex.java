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

package de.hu.logic.structure;

import java.util.ArrayList;

import de.hu.gralog.structure.types.elements.LabeledStructureVertex;

/**
 * @author kreutzer
 *
 */
public class TransitionSystemVertex extends LabeledStructureVertex
{
	private transient ArrayList<Proposition<? extends TransitionSystemVertex>> propositions = new ArrayList<Proposition<? extends TransitionSystemVertex>>();
	
	public TransitionSystemVertex()
	{
		super();
	}
	
	public TransitionSystemVertex(String label)
	{
		super( label );
	}
	
	public ArrayList<Proposition<? extends TransitionSystemVertex>> getPropositions() {
		return propositions;
	}
	
	public void removeProposition( Proposition<? extends TransitionSystemVertex> prop ) {
		propositions.remove( prop );
		displayChangeSupport.fireDisplayChangeDefault();
	}
	
	public void insertProposition( int index, Proposition<? extends TransitionSystemVertex> prop ) {
		propositions.add( index, prop );
		displayChangeSupport.fireDisplayChangeDefault();
	}
}
