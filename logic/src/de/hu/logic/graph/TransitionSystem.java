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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.hu.gralog.graph.DirectedGraph;
import de.hu.gralog.jgrapht.graph.GraphType;


public class TransitionSystem extends DirectedGraph<TransitionSystemVertex, TransitionSystemEdge> implements PropertyChangeListener {
	
	private ArrayList<Proposition> propositions = new ArrayList<Proposition>();
	private transient HashMap<TransitionSystemVertex, ArrayList<Proposition>> vertexPropositionsCache = new HashMap<TransitionSystemVertex,ArrayList<Proposition>>();
	
	public TransitionSystem() {
		super(  GraphType.MULTI_GRAPH, TransitionSystemVertex.class, TransitionSystemEdge.class );
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
		PropertyChangeEvent event = new IndexedPropertyChangeEvent( this, "propositions", oldProposition, proposition, index );
		updateVertexPropositionsCache( event );
		fireGraphPropertyChange( this,  event);
	}
	
	public Proposition getProposition( String name ) {
		for ( Proposition proposition : propositions ) {
			if ( proposition.getName().equals( name ) )
				return proposition;
		}
		return null;
	}
	
	public ArrayList<Proposition> getPropositions( TransitionSystemVertex vertex ) {
		ArrayList<Proposition> propositions = vertexPropositionsCache.get( vertex );
		if ( propositions == null ) {
			propositions = new ArrayList<Proposition>();
			for ( Proposition proposition : this.propositions ) {
				if ( proposition.containsVertex( vertex ) )
					propositions.add( proposition );
			}
			vertexPropositionsCache.put( vertex, propositions );
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

	protected ArrayList<TransitionSystemVertex> updateVertexPropositionsCache( Proposition proposition, TransitionSystemVertex oldVertex, TransitionSystemVertex newVertex ) {
		boolean remove = true;
		TransitionSystemVertex vertex = oldVertex;
		if ( vertex == null ) {
			vertex = newVertex;
			remove = false;
		}
		
		ArrayList<Proposition> vertexPropositions = vertexPropositionsCache.get( vertex );
		if ( vertexPropositions != null ) {
			if ( remove )
				vertexPropositions.remove( proposition );
			else
				vertexPropositionsCache.remove( vertex );
		}
		
		ArrayList<TransitionSystemVertex> changedVertices = new ArrayList<TransitionSystemVertex>(  );
		changedVertices.add( vertex );
		return changedVertices;
	}
	
	protected ArrayList<TransitionSystemVertex> updateVertexPropositionsCache( Proposition oldProposition, Proposition newProposition ) {
		if ( oldProposition == null )
			return null;
		ArrayList<TransitionSystemVertex> changedVertices = new ArrayList<TransitionSystemVertex>();
		for ( TransitionSystemVertex vertex : oldProposition.getVertexes() ) {
			changedVertices.add( vertex );
			ArrayList<Proposition> vertexPropositions = vertexPropositionsCache.get( vertex );
			if ( vertexPropositions != null )
				vertexPropositions.remove( oldProposition );
		}
		return changedVertices;
	}
	
	protected ArrayList<TransitionSystemVertex> updateVertexPropositionsCache( PropertyChangeEvent evt ) {
		if ( evt.getSource().getClass() == Proposition.class )
			return updateVertexPropositionsCache( (Proposition)evt.getSource(), (TransitionSystemVertex) evt.getOldValue(), (TransitionSystemVertex) evt.getNewValue() );
		return updateVertexPropositionsCache( (Proposition) evt.getOldValue(), (Proposition) evt.getNewValue() );
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		ArrayList<TransitionSystemVertex> changedVertices = null; 
		if ( evt.getSource().getClass() != TransitionSystemVertex.class )
			changedVertices = updateVertexPropositionsCache( evt );
		
		fireGraphPropertyChange( this, evt, changedVertices.toArray() );
	}
}
