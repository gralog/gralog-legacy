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

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import de.hu.gralog.beans.support.DefaultPropertyChangeListenableBean;

public class Proposition<V extends TransitionSystemVertex> extends DefaultPropertyChangeListenableBean {

	private String name = "name";
	ArrayList<V> vertices = new ArrayList<V>();
	
	public Proposition() {
		this( "name" );
	}
	
	public Proposition( String name ) {
		this.name = name;
	}
	
	protected Proposition( String name, ArrayList<V> vertices ) {
		this.name = name;
		this.vertices = new ArrayList<V>( vertices );
	}

	public String getName() {
		return name;
	}
	
	public void setName( String name ) {
		String oldName = this.name;
		this.name = name;
		propertyChangeSupport.firePropertyChange( "name", oldName, name );
	}
	
	public TransitionSystemVertex getVertexArray( int index ) {
		if ( index >= vertices.size() )
			return null;
		return vertices.get( index );
	}
	
	public TransitionSystemVertex[] getVertexArray() {
		return vertices.toArray( new TransitionSystemVertex[ vertices.size() ] );
	}
	
	public void setVertexArray( V[] vertices ) {
		this.vertices = new ArrayList<V>();
		for ( V vertex : vertices )
			this.vertices.add( vertex );
	}
	
	public void setVertexArray( int index, V vertex ) {
		V oldVertex = null;
		if ( index < vertices.size() )
			oldVertex = vertices.get( index );
		if ( vertex == null )
			vertices.remove( index );
		else 
			vertices.add( index, vertex );
		propertyChangeSupport.fireIndexedPropertyChange( "vertices", index, oldVertex, vertex );
	}
	
	public boolean containsVertex( V vertex ) {
		return vertices.contains( vertex );
	}
		
	public void addVertex( V vertex ) {
		setVertexArray( vertices.size(), vertex );
	}
	
	public void removeVertex( V vertex ) {
		setVertexArray( vertices.indexOf( vertex ), null );
	}
	
	/**
	 * Sets the content of the relation to be "set".
	 * @param set New content of the relation
	 */
	public void setVertices(Set<V> vertexes )
	{
		this.vertices = new ArrayList<V>( vertexes );
	}
	
	/**
	 * Sets the content of the relation to be "set".
	 * @param set New content of the relation
	 */
	public void setVertices(ArrayList<V> vertexes )
	{
		this.vertices = new ArrayList<V>( vertexes );
	}
	
	
	
	public ArrayList<V> getVertices() {
		return vertices;
	}
	
	public Proposition copy( ) {
		return new Proposition( name, vertices );
	}
	
	public boolean equalContent( Proposition proposition ) {
		return vertices.equals( proposition.vertices );
	}
	
	/**
	 * This method computes the union of _this and the relation R and returns the new relation.
	 * 
	 * @param R Relation to take union with
	 * @return the new relation.
	 */
	public Proposition<V> union(Proposition<V> R)
	{
		Proposition<V> set = new Proposition<V>("(" + name + " \\cup " + R.name + ")");
		set.setVertices( vertices );
		
		for ( V t : R.getVertices() ) {
			if ( ! set.getVertices().contains( t ) )
				set.getVertices().add( t );
		}
		return set;
	}
	
	/**
	 * This method computes the intersection of _this and the relation R and returns the new relation.
	 * 
	 * @param R Relation to take union with
	 * @return the new relation.
	 */
	public Proposition<V> intersection(Proposition<V> R)
	{
		Proposition<V> set = new Proposition<V>("(" + name + " \\cap " + R.name + ")");
		set.setVertices( vertices );
		set.getVertices().retainAll( R.getVertices() );
		return set;
	}
	
	/**
	 * This method computes the negation of _this within the specified set as universe. I.e. the result
	 * consists of all elements in <i>universe</i> that are not in _this. 
	 * 
	 * @param universe The "universe" in which negation is performed
	 * @return the new relation.
	 */
	public Proposition<V> negate(Set<V> universe)
	{
		Set<V> set = new HashSet<V>(universe);
		Proposition rel = new Proposition("neg " + name );
		rel.setVertices(set);
		rel.getVertices().removeAll(vertices);
		return rel;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.addPropertyChangeListener( l );
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.removePropertyChangeListener( l );
	}
}
