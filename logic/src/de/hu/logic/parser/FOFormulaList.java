/*
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

package de.hu.logic.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.hu.logic.fo.Formula;

/**
 * This class is used by the parser parsing a file containing several formulas.
 * It contains a list of formulas with names as well as an indication what type of logic is used.
 * @author kreutzer
 *
 */
public class FOFormulaList 
{
	HashMap<String, Formula> _map;
	String _type;		// type of logic as specified in the "type"-tag in the file
	String _mainformula;
	
	public FOFormulaList()
	{	
		_map = new HashMap<String,Formula>();
	
	}

	public FOFormulaList(String type)
	{	
		_map = new HashMap<String,Formula>();
		_type = type;
	}


	public String getType() {
		return _type;
	}

	public void setType(String _type) {
		this._type = _type;
	}
	
	public void addFormula(String name, Formula f)
	{
		_map.put(name, f);
	}

	/**
	 * returns the main formula in the file. The main formula is the formula called "main", or, if there is no such formula, 
	 * and the FormulaList only contains a single formula, it is this one. 
	 * @return Main formula.
	 * @throws ParseException Throws an exception if the main formula cannot be determined.
	 */
	public Formula getMainFormula() throws ParseException
	{
		if(_map.containsKey("main"))
			return _map.get("main");
		else if(_map.size() == 1)
		{
			Iterator iter = _map.keySet().iterator();
			return _map.get((String) iter.next());
		}
		else throw new ParseException(ParseException.NO_MAIN_FORMULA, "No main formula found!");
			
	}
	
	public Formula getFormula(String name) 
	{
		return _map.get(name);
	}
	
	public void addAll(FOFormulaList fl) throws ParseException
	{
		Iterator iter = fl._map.keySet().iterator();
		String str;
		while(iter.hasNext())
		{
			str = (String) iter.next();
			if(_map.containsKey(str))
				throw new ParseException(ParseException.DUPLICATE_FORMULA, "Duplicate formula: " + str);
					
		}
		_map.putAll(fl._map);
	}

	
	/**
	 * This formula performs any substitution contained in the formula.
	 * Formulas to be read from a file may use a construct "\sub(phi)" where "phi" is a string.
	 * This means that the formula with name "phi" from the current formula list has to be substituted at this point.
	 * The method substitute performs this substitution. Before it starts with this, it calls checkSubstitution() method to
	 * check that the substitions are not cyclic.
	 * @return The formula with all substitutions performed. The basis is the "main" formula as returned by the getMainFormula method. 
	 */
	public Formula substituteMain() throws ParseException
	{
		Formula main = getMainFormula();
		return substitute(main);
	}
	
	/**
	 * This formula performs any substitution contained in the formula.
	 * Formulas to be read from a file may use a construct "\sub(phi)" where "phi" is a string.
	 * This means that the formula with name "phi" from the current formula list has to be substituted at this point.
	 * The method substitute performs this substitution. At the moment, the method does not check for cyclic substitutions.
	 * @return The formula with all substitutions performed. The basis is the "main" formula as returned by the getMainFormula method. 
	 */
	protected Formula substitute(Formula f) throws ParseException
	{
		
//		if(f == null)
//			throw new ParseException(ParseException.SUBST_UNKNOWN_FORMULA, "The formula " + name + "can not be found for substitution.");
		switch(f.type())
		{
		case Formula.bottom: 
		case Formula.top: 
		case Formula.proposition: return f;
		case Formula.and: return new Formula(Formula.and, substitute(f.leftSubf()), substitute(f.rightSubf()));  
		case Formula.or: return new Formula(Formula.or, substitute(f.leftSubf()), substitute(f.rightSubf()));
		case Formula.neg: return new Formula(Formula.neg, substitute(f.subf()));
		case Formula.exists: return new Formula(Formula.exists, f.ident(), substitute(f.subf()));
		case Formula.forall: return new Formula(Formula.forall, f.ident(), substitute(f.subf()));
		case Formula.lfp: return new Formula(Formula.lfp, f.ident(), f.getVarList(), substitute(f.subf()));
		case Formula.gfp: return new Formula(Formula.gfp, f.ident(), f.getVarList(), substitute(f.subf()));
		case Formula.ifp: return new Formula(Formula.lfp, f.ident(), f.getVarList(), substitute(f.subf()));
		case Formula.dfp: return new Formula(Formula.gfp, f.ident(), f.getVarList(), substitute(f.subf()));
		case Formula.sub:
			Formula g = getFormula(f.ident());
			if(g == null)
				throw  new ParseException(ParseException.SUBST_UNKNOWN_FORMULA, "The formula " + f.ident() + "can not be found for substitution.");
			return substitute(g);
		}
		return null;
	}
	
	public void printList()
	{
		Iterator iter = _map.entrySet().iterator();
		Map.Entry e;
		while(iter.hasNext())
		{
			e = (Map.Entry) iter.next();
			System.out.println("Formula " + e.getKey() + ": " + e.getValue());
		}
	}
	
}
