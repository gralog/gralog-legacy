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

package de.hu.logic.modal;

import java.util.HashSet;

/**
 * Base class for all formulas.
 * @author kreutzer
 *
 */
public class Formula 
{
	public static final int bottom = 0;   // false
	public static final int top = 1;      // true
	public static final int proposition = 2;
	public static final int and    = 3;
	public static final int or = 4;
	public static final int neg = 5;
	public static final int diamond = 6;
	public static final int box = 7;
	public static final int mu = 8;
	public static final int nu = 9;
	public static final int sub = 10;		// substitution. _ident contains the name of the formula to be substituted at this position. 
	
	
	Formula _sf, _rightSF;
	int _type;
    String _ident;
	
	
	/**
	 * Constructor for atomic formulae (i.e. formulae without sub-formulae)
	 * @param type Identifier for the formula, one of the defined types
	 * @param ident Additional information. For proposition symbols this is the name of the symbol 
	 */
	public Formula(int type, String ident)
	{
		_type = type;
		_ident = ident;
	}
	
	/**
	 * Constructor for binary operators (i.e. formulae with two sub-formulae)
	 * @param type Identifier for the formula, one of the defined types
	 * @param left Left sub-formula 
	 * @param right right sub-formula 
	 */
	public Formula(int type, Formula left, Formula right)
	{
		_type = type;
		_sf = left;
		_rightSF = right;
	}
	
	/**
	 * Constructor for unary operators (i.e. neg)
	 * @param sf sub-formula 
	 */
	public Formula(int type, Formula sf)
	{
		_type = type;
		_sf = sf;
		_ident = "";
	}
	

	/**
	 * Constructor for modal and fixed-point operators
	 * they have one subformula and an additional identifier specifying the fixed-point variable or
	 * the label of the edge, in case of modal opertors. For instance, a formula
	 * \mu X.\phi would call this constructor with type mu, ident "X" and sf \phi.
	 * <a> \phi would call this with type diamond, ident "a" and sf \phi.
	 * 
	 * @param type Identifier for the formula, one of the defined types
	 * @param sf   This parameter points to the sub-formula 
	 * @param ident Additional information. For proposition symbols this is the name of the symbol
	 */
	public Formula(int type, String ident, Formula sf)
	{
		_type = type;
		_sf = sf;
		_ident = ident;
	}
	
	public int type()
	{
		return _type; 
	}
	
	public String ident()
	{
		return _ident;
	}
	
	public Formula subf()
	{
		return _sf;
	}
	
	public Formula leftSubf()
	{
		return _sf;
	}
	
	public Formula rightSubf()
	{
		return _rightSF;
	}
	
	
	public String toString()
	{
		switch(_type)
		{
		case bottom: return "false"; //break;
		case top: return "true"; //break;
		case proposition: return _ident; //break;
		case and: return "("+leftSubf() + " and " + rightSubf() + ")"; //break;
		case or: return "("+leftSubf() + " or " + rightSubf() + ")"; //break;
		case neg: return "neg "+subf(); //break;
		case diamond: return "<"+_ident+">" + subf(); //break;
		case box: return "["+_ident+"]" + subf(); //break;
		case mu: return "\\mu "+_ident + "." + subf(); //break;
		case nu: return "\\nu "+_ident + "." + subf(); //break;
		case sub: return "\\sub(" + _ident + ")";
		}
		return "";
	}
	
	/**
	 * checks whether the formula is valid, i.e. whether no least or greatest fixed-point variable is used negatively
	 * @return returns true if no variable is bound twice in the formula and no fixed-point variable is used negatively. Returns false otherwise.
	 */
	public boolean valid()
	{
		HashSet<String> set = new HashSet<String>();
		return recValid(set, false);
	}

	
	private boolean recValid(HashSet<String> fpvar, boolean neg)
	{
		switch(_type)
		{
		case bottom: 
		case top: return true;
		case proposition: 
			if(fpvar.contains(_ident) && neg)
				return false;
			return true;
		case and: 
		case or: 
			return (leftSubf().recValid(fpvar, neg) && rightSubf().recValid(fpvar, neg));
		case Formula.neg:  
			return subf().recValid(fpvar, !neg);
		case diamond: 
		case box: return subf().recValid(fpvar, neg);
		case mu: 
		case nu: 
			fpvar.add(_ident);
			return subf().recValid(fpvar, neg);
		}
		return true;
	}
}
