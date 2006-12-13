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

package de.hu.logic.graph;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import de.hu.games.graph.DirectedGraph;


public class TransitionSystem extends DirectedGraph<TransitionSystemVertex, TransitionSystemEdge> implements PropertyChangeListener {
	
	private ArrayList<Proposition> propositions = new ArrayList<Proposition>();
	
	public TransitionSystem() {
		super( TransitionSystemVertex.class, TransitionSystemEdge.class );
	}

	public Proposition[] getPropositions() {
		return propositions.toArray( new Proposition[propositions.size()] );
	}
	
	public Proposition getPropositions( int index ) {
		if ( index >= propositions.size() )
			return null;
		return propositions.get( index );
	}
	
	public void setPropositions( Proposition[] propositions ) {
		this.propositions = new ArrayList<Proposition>();
		for ( Proposition prop : propositions ) {
			this.propositions.add( prop );
			prop.addPropertyChangeListener( this );
		}
	}
	
	public void setPropositions( int index, Proposition proposition ) {
		Proposition oldProposition = null;
		if ( index < propositions.size() )
			oldProposition = propositions.get( index );
		if ( proposition == null )
			propositions.remove( index );
		else {
			propositions.add( index, proposition );
			proposition.addPropertyChangeListener( this );
		}
		fireGraphPropertyChange( this, new IndexedPropertyChangeEvent( this, "propositions", oldProposition, proposition, index ) );
	}
	
	public Proposition getProposition( String name ) {
		for ( Proposition proposition : propositions ) {
			if ( proposition.getName().equals( name ) )
				return proposition;
		}
		return null;
	}
	
	public ArrayList<Proposition> getPropositions( TransitionSystemVertex vertex ) {
		ArrayList<Proposition> propositions = new ArrayList<Proposition>();
		for ( Proposition proposition : this.propositions ) {
			if ( proposition.containsVertex( vertex ) )
				propositions.add( proposition );
		}
		return propositions;
	}
	
	/**
	 * Returns the signature of the transition system, i.e. a HashSet containing the names of all relations 
	 * of the transition system.
	 * @return HashSet containing the names of relations in the TS.
	 */
	public Set<String> getSignature() {
		HashSet<String> propNames = new HashSet<String>();
		for ( Proposition proposition : propositions )
			propNames.add( proposition.getName() );
		return propNames;
	}
	
	public void addProposition( Proposition proposition ) {
		setPropositions( propositions.size(), proposition );
	}
	
	public void removeProposition( String name ) {
		int index = 0;
		for ( Proposition proposition : new ArrayList<Proposition>( propositions ) ) {
			if ( proposition.getName().equals( name ) )
				setPropositions( index, null );
			index++;
		}
	}
	
	@Override
	public boolean addVertex( TransitionSystemVertex vertex ) {
		if ( super.addVertex( vertex ) ) {
			vertex.setGraph( this );
			return true;
		}
		return false;
	}
	
	public boolean isGraphEditable() {
		return true;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		fireGraphPropertyChange( this, evt );
	}
}
