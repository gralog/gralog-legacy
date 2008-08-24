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

import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author Stephan Kreutzer 
 *
 */

public class Interpretation 
{
	Structure _str;
	HashMap<String,Object> _foVars;		// Map providing interpretation for first-order vars
	HashMap<String, Relation> _soVars;	// second order variables
	
	public Interpretation(Structure str)
	{
		_str = str;
		_foVars = new HashMap<String, Object>();
		_soVars = new HashMap<String, Relation>();	}
	
	/**
	 * Sets the interpretation for a first-order variable.
	 * @param var
	 * @param element
	 */
	public void setFOVar(String var, Object element)
	{
		_foVars.put(var, element);
	}
	
	public Object getFO(String var)
	{
		return _foVars.get(var);
	}

	public Relation getSO(String var) 
	{
		return _soVars.get(var);
	}

	public void setSOVar(String var, Relation rel) {
		_soVars.put(var, rel);
	}
	
	public boolean contains(String relation, ArrayList<String> varlist) throws Exception
	{
				// get interpretation for the variables in varlist
		ArrayList<Object> elemlist = new ArrayList<Object>(varlist.size());
		for( String elem : varlist)
		{
			if(_foVars.containsKey(elem))
				elemlist.add(_foVars.get(elem));
			else
				throw new Exception("Variable "+elem+" undefined.");
		}
		
		if(_str.getSignature().contains(relation))
		{
			return _str.contains(relation, elemlist);
		}
		else
			throw new Exception("Signature mismatch: Relation "+relation+" unknown.");
	}
	
	public Interpretation clone()
	{
		Interpretation inter = new Interpretation(_str);
		inter._foVars = (HashMap<String, Object>)_foVars.clone();
		inter._soVars = (HashMap<String, Relation>)_soVars.clone();
		return inter;
	}
}
