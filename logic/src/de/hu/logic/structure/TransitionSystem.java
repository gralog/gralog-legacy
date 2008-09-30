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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jgrapht.event.GraphVertexChangeEvent;
import org.jgrapht.event.VertexSetListener;
import org.jgrapht.graph.ListenableDirectedGraph;

import de.hu.gralog.beans.support.DefaultPropertyAndDisplayChangeListenableBean;
import de.hu.gralog.beans.support.StructureBean;
import de.hu.gralog.structure.Structure;


public class TransitionSystem<V extends TransitionSystemVertex,E, G extends ListenableDirectedGraph<V,E>> extends DefaultPropertyAndDisplayChangeListenableBean implements VertexSetListener, PropertyChangeListener, StructureBean<V,E,TransitionSystem<V,E,G>,G> {
	
	private ArrayList<Proposition<V>> propositions = new ArrayList<Proposition<V>>();
	private transient Structure<V,E,TransitionSystem<V,E,G>, G> structure;
	
	public TransitionSystem(  ) {
	}

	public void setStructure(Structure<V, E, TransitionSystem<V,E,G>, G> graphSupport) {
		this.structure = graphSupport;
	}
	
	public Proposition<V>[] getPropositions() {
		return propositions.toArray( new Proposition[propositions.size()] );
	}
	
	public Proposition<V> getPropositions( int index ) {
		if ( index >= propositions.size() )
			return null;
		return propositions.get( index );
	}
	
	public void setPropositions( Proposition<V>[] propositions ) {
		this.propositions = new ArrayList<Proposition<V>>();
		for ( Proposition<V> prop : propositions )
			setPropositions( this.propositions.size(), prop );
	}
	
	public void setPropositions( int index, Proposition<V> proposition ) {
		Proposition<V> oldProposition = null;
		if ( index < propositions.size() )
			oldProposition = propositions.get( index );
		if ( proposition == null ) {
			verticesRemoveProposition( oldProposition );
			propositions.remove( index );
		}
		else {
			verticesInsertProposition( index, proposition );
			propositions.add( index, proposition );
			proposition.addPropertyChangeListener( this );
		}
		propertyChangeSupport.fireIndexedPropertyChange( "propositions", index, oldProposition, proposition );
	}
	
	public Proposition getProposition( String name ) {
		for ( Proposition proposition : propositions ) {
			if ( proposition.getName().equals( name ) )
				return proposition;
		}
		return null;
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
	
	public void propertyChange(PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
		Proposition<V> prop = (Proposition<V>)evt.getSource();

		if ( propertyName.equals( "name" ) ) {
			HashSet<V> updateVertices = new HashSet<V>( prop.getVertices() );
			displayChangeSupport.fireDisplayChange( updateVertices, null, false );
		}
		if ( propertyName.equals( "vertices" ) ) {
			V oldVertex = (V)evt.getOldValue();
			V newVertex = (V)evt.getNewValue();
			if ( newVertex == null )
				oldVertex.removeProposition( prop );
			else {
				newVertex.insertProposition( getPropositionIndexForVertex( propositions.indexOf( prop ), newVertex ), prop );
			}
		}
		propertyChangeSupport.firePropertyChange( evt );
		
	}
	
	protected void verticesRemoveProposition( Proposition<V> prop ) {
		for ( V v : prop.getVertices() )
			v.removeProposition( prop );
	}
	
	protected int getPropositionIndexForVertex( int index, V v ) {
		int vIndex = 0;
		for ( Proposition prop : v.getPropositions() ) {
			int indexOf = propositions.indexOf( prop );
			if ( indexOf >= index )
				return vIndex;
			vIndex++;
		}
		return vIndex;
	}
	
	protected void verticesInsertProposition( int index, Proposition<V> prop ) {
		for ( V v : prop.getVertices() ) {
			int vIndex = getPropositionIndexForVertex( index, v );
			v.insertProposition( vIndex, prop );
		}
	}

	public void vertexAdded(GraphVertexChangeEvent arg0) {
		// do nothing
	}

	public void vertexRemoved(GraphVertexChangeEvent event) {
		V vertex = (V)event.getVertex();
		for ( Proposition<V> prop : propositions ) {
			if ( prop.containsVertex( vertex ) )
				prop.vertices.remove( vertex );
		}
	}

}
