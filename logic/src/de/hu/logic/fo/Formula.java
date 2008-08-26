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

package de.hu.logic.fo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Formula class. Provides access to a first-order based formula (FO, LFP, IFP...)
 * The various variables in the class have the following meaning:
 * Let f be a formula.
 * f._type indicates the type of the formula.
 * switch(f._type)
 * case bottom, top: f is false or true
 * case proposition: f is atomic R(x, y, t)
 * 		_ident contains the name of the relation (e.g. R)
 * 		_varList contains the sequence of variables (e.g. x, y, t)
 * case eq: f is of the form x = y
 * 		_ident contains x
 * 		_ident2 contains y
 * case and, or: f is conjunction or disjunction f = f1 \and/\or f2
 * 		_sf contains f1 and _rightSF contains f2
 * case neg: f = \neg g
 * 		_sf = g
 * case exists, forall:  f = \exists x g  or f = \forall x g
 * 		_ident contains x
 * 		_sf contains g
 * 
 * @author Stephan Kreutzer
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
	public static final int exists = 6;
	public static final int forall = 7;
	public static final int lfp = 8;
	public static final int gfp = 9;
	public static final int ifp = 13;
	public static final int dfp = 14;
	public static final int eq = 10;		
	public static final int var = 11;		// variables. _ident contains the variable name	
	public static final int sub = 12;		// substitution. _ident contains the name of the formula to be substituted at this position. 
	
	
	Formula _sf, _rightSF;
	int _type;
    String _ident, _ident2;
    
    /** 
     * the _varList field is used by formulas that represent atoms (R(x, y, z)) to store the sequence 
     * of variables
     */
    ArrayList<String> _varList;
	
    /** 
     * the _innerVarList field is used by fixed point formulas to represent the variables for the fixed point variable.
     * e.g. [\lfp_{R, x, z} ...](a,b)  then x, z are the innerVarList.  a,b would appear in _varList  
     */
    ArrayList<String> _innerVarList;
	
    /** 
     * the _freeVars field is used to store the list of free variables.
     * It is initialised by a call to initFreeVars()
     */
    HashSet<String> _freeVars = null;
	
	
	/**
	 * Constructor for atomic formulae (i.e. formulae without sub-formulae)
	 * @param type Identifier for the formula, one of the defined types
	 * @param ident Additional information. For proposition symbols this is the name of the symbol 
	 */
	public Formula(int type, String ident)
	{
		_type = type;
		_ident = ident;
		_varList = new ArrayList<String>();
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
		_varList = new ArrayList<String>();
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
		_varList = new ArrayList<String>();
	}
	

	/**
	 * Constructor for first order quantifiers operators
	 * they have one subformula and an additional identifier specifying the variable or
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
	

	/**
	 * Constructor for first order quantifiers operators
	 * they have one subformula and an additional identifier specifying the variable or
	 * 
	 * @param type Identifier for the formula, one of the defined types
	 * @param sf   This parameter points to the sub-formula 
	 * @param varlist variable list for fixed point operators
	 * @param ident Additional information. For proposition symbols this is the name of the symbol
	 */
	public Formula(int type, String ident, ArrayList<String> varlist, Formula sf)
	{
		_type = type;
		_sf = sf;
		_ident = ident;
		_varList = varlist;
	}
	
	/**
	 * Constructor used for atomic formulas corresponding to a relation symbol ((R(x, y, z))
	 * @param type Identifier for the formula, should be proposition
	 * @param ident Name of the relation symbol (e.g. R)
	 * @param varlist list, in fact sequence, of variables occurring in the relation.
	 */
	public Formula(int type, String ident, ArrayList<String> varlist)
	{
		_type = type;
		_sf = null;
		_ident = ident;
		_varList = varlist;
	}
	

	/**
	 * Used for equality   "s1 = s2"
	 * @param type Should be Formula.eq
	 * @param s1 Variable 1
	 * @param s2 Variable 
	 */
	public Formula(int type, String s1, String s2)
	{
		_type = type;
		_sf = null;
		_ident = s1;
		_ident2 = s2;
	}

	/**
	 * Constructor used for fixed point formulas. 
	 * If [\lfp_{R, x, y, z} \phi]{x', y', z'} then the parameters mean the following (see parameter description)
	 * @param type Type of the formula, e.g. Formula.lfp
	 * @param ident Name of the fixed point variable (e.g. R)
	 * @param innerVarList inner variable list, e.g. x, y, z
	 * @param outerVars outer variable list, e.g. x', y', z'
	 * @param f subformula
	 */
	public Formula(int type, String ident, ArrayList<String> innerVarList, ArrayList<String> outerVars, Formula f)
	{
		_type = type;
		_ident = ident;
		_varList = outerVars;
		_innerVarList = innerVarList;
		_sf = f;
	}
	
	/**
	 * This method returns the _varList field of the formula. See above for a description of _varList
	 * @return _varList field.
	 */
	public ArrayList<String> getVarList()
	{
		return _varList;
	}
	
	public int type()
	{
		return _type; 
	}
	
	public String ident()
	{
		return _ident;
	}
	
	public String ident2()
	{
		return _ident2;
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
		case proposition:
			StringBuffer str = new StringBuffer(_ident + "(");
			for (int i =0; i<_varList.size(); i++)
			{
				str.append(_varList.get(i));
				if(i+1<_varList.size())
					str.append(", ");
			}
			str.append(")");
			return str.toString(); //break;
		case eq: return _ident + " = " + _ident2;
		case and: return "("+leftSubf() + " and " + rightSubf() + ")"; //break;
		case or: return "("+leftSubf() + " or " + rightSubf() + ")"; //break;
		case neg: return "neg "+subf(); //break;
		case exists: return "\\exists "+_ident+ " " + subf(); //break;
		case forall: return "\\forall "+_ident+ " " +  subf(); //break;
		case lfp:
		case ifp:
			if(_type == lfp)
				str = new StringBuffer("[\\lfp_{ "+_ident + ",");
			else
				str = new StringBuffer("[\\ifp_{ "+_ident + ",");
			Iterator<String> iter = _innerVarList.iterator();
			while(iter.hasNext())
			{
			    str.append(iter.next());
			    if(iter.hasNext())
			    	str.append(", ");
			    else
			    	str.append(" } ");
			    
			}
			str.append(subf());
			str.append(" ](");
			iter = _varList.iterator();
			while(iter.hasNext())
			{
			    str.append(iter.next());
			    if(iter.hasNext())
			    	str.append(", ");
			    else
			    	str.append(" ) ");
			    
			}			
			return str.toString(); //break;
//		case nu: return "\\nu "+_ident + "." + subf(); //break;
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
		//TODO: still the old routine from modal logics
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
		case exists: 
		case forall:
		case ifp:
		case dfp:
			return subf().recValid(fpvar, neg);
		case lfp: 
		case gfp:
			HashSet<String> new_fpvar = new HashSet<String>(fpvar);
			new_fpvar.add(_ident);
			return subf().recValid(new_fpvar, neg);
		}
		return true;
	}

	/**
	 * Returns the set of free variables and initialises the set if it has not been initialised before.
	 * @return Set of free variables
	 */
	public Set freeVars()
	{
		if(_freeVars == null)
			initFreeVars();
		return _freeVars;
	}
	
	public String freeVar() throws Exception
	{
		if(width() != 1)
			throw new Exception("Program error. Only call freeVar if there is a free variable");
		return _freeVars.iterator().next();
	}
	/** 
	 * Computes the list of free variables and stores it in _freeVars.
	 *
	 */
	public void initFreeVars()
	{
		switch(_type)
		{
		case bottom: 
		case top: 
			_freeVars = new HashSet<String>();
			break;
		case proposition:
			_freeVars = new HashSet<String>(this._varList);
			break;
		case eq: 
			_freeVars = new HashSet<String>(2);
			_freeVars.add(_ident);
			_freeVars.add(_ident2);
			break;
		case and: 
		case or: 
			leftSubf().initFreeVars();
			rightSubf().initFreeVars();
//			Set left, right;
//			left = leftSubf().freeVars();
//			right = rightSubf().freeVars();
//			System.out.println("left: " + left);
//			System.out.println("right: " + right);
			_freeVars = new HashSet<String>(leftSubf().freeVars());
			_freeVars.addAll(rightSubf().freeVars());
//			System.out.println("left + right: " + _freeVars);
			break;
		case Formula.neg:  
			_freeVars = new HashSet<String>(subf().freeVars());
			break;
		case exists: 
		case forall:
			_freeVars = new HashSet<String>( subf().freeVars() );
			_freeVars.remove(_ident);
			break;
		case lfp:
		case ifp:
			_freeVars = new HashSet<String>(subf().freeVars());
			_freeVars.removeAll(_innerVarList);
			_freeVars.addAll(_varList);
			break;
		}
	}
	
	public boolean isBoolean()
	{
		return width() == 0;
	}
	
	/** returns the width of the formula, i.e. the number of free variables.
	 * 
	 * @return width of the formula
	 */
	public int width()
	{
		if(_freeVars == null)
			initFreeVars();
		return _freeVars.size();
		
	}
}
