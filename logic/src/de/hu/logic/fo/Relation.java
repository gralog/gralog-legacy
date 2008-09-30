/**
 * Created on 2006 by Stephan Kreutzer
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

package de.hu.logic.fo;

import java.util.HashSet;
import java.util.Set;

import de.hu.logic.structure.TransitionSystemVertex;


/**
 * Relations of this form can only be nullary or binary.
 * @author Stephan Kreutzer 
 *
 */
public class Relation 
{
	String _name;
	Set _set = null;
	int _arity = 1;
	
	boolean _true;
	
	
	public Relation(String name)
	{
		_name = name;
		_set = new HashSet();
		_arity = 1;
	}
	
	public Relation(String name, boolean nullary)
	{
		_name = name;
		_set = new HashSet();
		if(nullary)
			_arity = 0;
		else
			_arity = 1;
	}
	
	public Relation(String name, Set set)
	{
		_name = name;
		_set = set;
		_arity = 1;
	}
	
	public void setVertexSet(Set set)
	{
		_set = set;
	}
	
	public Set getVertexSet()
	{
		if(_set == null)
			return new HashSet();
		else
			return _set;
	}

	public void addVertex(Object element)
	{
		_set.add(element);
	}
	/**
	 * @return the _true
	 */
	public boolean is_true() 
	{
		return _true;
	}

	/**
	 * @param _true the _true to set
	 */
	public void set_true(boolean _true) {
		this._true = _true;
	}
	
	public String getName() {
		return _name;
	}
	
	public int getArity()
	{
		return _arity;
	}
	
	public void setName( String name ) {
		_name = name;
	}
	
	
	/**
	 * This method computes the union of _this and the relation R and returns the new relation.
	 * 
	 * @param R Relation to take union with
	 * @return the new relation.
	 */
	public Relation union(Relation R)
	{
		Relation set = new Relation("(" + _name + " \\cup " + R.getName() + ")");
		set.setVertexSet( _set );
		
		for ( Object t : R.getVertexSet() ) {
			if ( ! set.getVertexSet().contains( t ) )
				set.getVertexSet().add( t );
		}
		return set;
	}
	
	/**
	 * This method computes the intersection of _this and the relation R and returns the new relation.
	 * 
	 * @param R Relation to take union with
	 * @return the new relation.
	 */
	public Relation intersection(Relation R)
	{
		Relation set = new Relation("(" + _name + " \\cap " + R.getName() + ")");
		set.setVertexSet( _set );
		set.getVertexSet().retainAll( R.getVertexSet() );
		return set;
	}
	
	/**
	 * This method computes the negation of _this within the specified set as universe. I.e. the result
	 * consists of all elements in <i>universe</i> that are not in _this. 
	 * 
	 * @param universe The "universe" in which negation is performed
	 * @return the new relation.
	 */
	public Relation negate(Set<TransitionSystemVertex> universe)
	{
		Set<Object> set = new HashSet<Object>(universe);
		Relation rel = new Relation("neg " + _name );
		rel.setVertexSet(set);
		rel.getVertexSet().removeAll(_set);
		return rel;
	}
	

	public boolean equalContent( Relation rel ) {
		return _set.equals( rel._set );
	}
	
	

}
