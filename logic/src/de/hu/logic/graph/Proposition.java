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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import de.hu.gralog.graph.NestedGraphPropertyBean;

public class Proposition implements NestedGraphPropertyBean {


	
	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport( this );
	private String name = "name";
	private ArrayList<TransitionSystemVertex> vertexes = new ArrayList<TransitionSystemVertex>();
	
	public Proposition() {
		this( "name" );
	}
	
	public Proposition( String name ) {
		this.name = name;
	}
	
	protected Proposition( String name, ArrayList<TransitionSystemVertex> vertexes ) {
		this.name = name;
		this.vertexes = new ArrayList<TransitionSystemVertex>( vertexes );
	}

	public String getName() {
		return name;
	}
	
	public void setName( String name ) {
		String oldName = this.name;
		this.name = name;
		propertyChangeSupport.firePropertyChange( "name", oldName, name );
	}
	
	public TransitionSystemVertex getVertexesArray( int index ) {
		if ( index >= vertexes.size() )
			return null;
		return vertexes.get( index );
	}
	
	public TransitionSystemVertex[] getVertexesArray() {
		return vertexes.toArray( new TransitionSystemVertex[ vertexes.size() ] );
	}
	
	public void setVertexesArray( TransitionSystemVertex[] vertexes ) {
		this.vertexes = new ArrayList<TransitionSystemVertex>();
		for ( TransitionSystemVertex vertex : vertexes )
			this.vertexes.add( vertex );
	}
	
	public void setVertexesArray( int index, TransitionSystemVertex vertex ) {
		TransitionSystemVertex oldVertex = null;
		if ( index < vertexes.size() )
			oldVertex = vertexes.get( index );
		if ( vertex == null )
			vertexes.remove( index );
		else
			vertexes.add( index, vertex );
		propertyChangeSupport.fireIndexedPropertyChange( "vertexes", index, oldVertex, vertex );
	}
	
	public boolean containsVertex( TransitionSystemVertex vertex ) {
		return vertexes.contains( vertex );
	}
		
	public void addVertex( TransitionSystemVertex vertex ) {
		setVertexesArray( vertexes.size(), vertex );
	}
	
	public void removeVertex( TransitionSystemVertex vertex ) {
		setVertexesArray( vertexes.indexOf( vertex ), null );
	}
	
	/**
	 * Sets the content of the relation to be "set".
	 * @param set New content of the relation
	 */
	public void setVertexes(Set<TransitionSystemVertex> vertexes )
	{
		this.vertexes = new ArrayList<TransitionSystemVertex>( vertexes );
	}
	
	/**
	 * Sets the content of the relation to be "set".
	 * @param set New content of the relation
	 */
	public void setVertexes(ArrayList<TransitionSystemVertex> vertexes )
	{
		this.vertexes = new ArrayList<TransitionSystemVertex>( vertexes );
	}
	
	
	
	public ArrayList<TransitionSystemVertex> getVertexes() {
		return vertexes;
	}
	
	public Proposition copy( ) {
		return new Proposition( name, vertexes );
	}
	
	public boolean equalContent( Proposition proposition ) {
		return vertexes.equals( proposition.vertexes );
	}
	
	/**
	 * This method computes the union of _this and the relation R and returns the new relation.
	 * 
	 * @param R Relation to take union with
	 * @return the new relation.
	 */
	public Proposition union(Proposition R)
	{
		Proposition set = new Proposition("(" + name + " \\cup " + R.name + ")");
		set.setVertexes( vertexes );
		
		for ( TransitionSystemVertex t : R.getVertexes() ) {
			if ( ! set.getVertexes().contains( t ) )
				set.getVertexes().add( t );
		}
		return set;
	}
	
	/**
	 * This method computes the intersection of _this and the relation R and returns the new relation.
	 * 
	 * @param R Relation to take union with
	 * @return the new relation.
	 */
	public Proposition intersection(Proposition R)
	{
		Proposition set = new Proposition("(" + name + " \\cap " + R.name + ")");
		set.setVertexes( vertexes );
		set.getVertexes().retainAll( R.getVertexes() );
		return set;
	}
	
	/**
	 * This method computes the negation of _this within the specified set as universe. I.e. the result
	 * consists of all elements in <i>universe</i> that are not in _this. 
	 * 
	 * @param universe The "universe" in which negation is performed
	 * @return the new relation.
	 */
	public Proposition negate(Set<TransitionSystemVertex> universe)
	{
		Set<TransitionSystemVertex> set = new HashSet<TransitionSystemVertex>(universe);
		Proposition rel = new Proposition("neg " + name );
		rel.setVertexes(set);
		rel.getVertexes().removeAll(vertexes);
		return rel;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.addPropertyChangeListener( l );
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.removePropertyChangeListener( l );
	}

}
