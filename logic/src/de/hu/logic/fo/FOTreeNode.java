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
import java.util.List;

import de.hu.gralog.app.UserException;
import de.hu.logic.general.EvaluationTreeNode;
import de.hu.logic.graph.Proposition;



/**
 * @author Stephan Kreutzer 
 *
 */
public class FOTreeNode implements FOEvaluationTreeNode {

	Formula _f;
	FOEvaluation _eval;
	boolean _root=false;
	Interpretation _inter;
	String _name;
	Relation _result = null;
	Boolean _true = false;
	ArrayList<FOEvaluationTreeNode> _children=null;
	
	public FOTreeNode(Formula f, FOEvaluation eval)
	{
	   _f = f;
	   _eval = eval;
	   _inter = new Interpretation(eval.getStructure());
	   _name = f.toString();
	}

	public FOTreeNode(Formula f, FOEvaluation eval, Interpretation inter)
	{
	   _f = f;
	   _eval = eval;
	   _inter = inter;
	   _name = f.toString();
	}
	
	
	public FOTreeNode(Formula f, FOEvaluation eval, Interpretation inter, String name)
	{
	   _f = f;
	   _eval = eval;
	   _inter = inter;
	   _name = name;
	}
	
	public void setRoot(boolean root)
	{
		_root = root;
	}

	void initialise() throws UserException
	{
		_children = new ArrayList<FOEvaluationTreeNode>(getChildrenCount());
		FOTreeNode node;
		if((!_root) || _f.isBoolean())
		{
			if((_f.type() == Formula.neg))
			{
				node = new FOTreeNode(_f.subf(), _eval, _inter);
				_children.add(node);
			}
			else if(_f.type() == Formula.exists || _f.type() == Formula.forall)
			{
				Interpretation inter;
				String subf = _f.subf().toString();
				_result = new Relation("");
				for( Object element : _eval.getStructure().getUniverse())
				{
					inter = _inter.clone();
					inter.setFOVar(_f.ident(), element);
					try{					
						if(_eval.recursiveEvaluate(_f.subf(), inter))
						{
							node = new FOTreeNode(_f.subf(), _eval, inter, 
									new String("true; " + _f.ident() + " = " + element.toString() +": " + subf));
							_result.addVertex(element);
						}
						else
						{
							node = new FOTreeNode(_f.subf(), _eval, inter, 
									new String("false; " + _f.ident() + " = " + element.toString() +": " + subf));
						}
	//					node = new FOTreeNode(_f.subf(), _eval, inter, 
	//							new String(_f.ident() + " = " + element.toString() +": " + subf));
						_children.add(node);
					}
					catch(Exception e)
					{
						throw new UserException(e.getMessage());
					}
					
				}
				
			}
			else if(_f.type() == Formula.and || _f.type() == Formula.or)
			{
				_children.add(new FOTreeNode(_f.leftSubf(), _eval, _inter));
				_children.add(new FOTreeNode(_f.rightSubf(), _eval, _inter));
			}
		}
		else	// we are at the root node and _f has a free variable
		{
			Interpretation inter;
			String subf = _f.toString();
			try{
				Relation res = new Relation(_f.freeVar());
				Relation rel;
				for( Object element : _eval.getStructure().getUniverse())
				{
					inter = _inter.clone();
					inter.setFOVar(_f.freeVar(), element);
					if(_eval.recursiveEvaluate(_f, inter))
					{
						node = new FOTreeNode(_f, _eval, inter, 
								new String("true; " + _f.freeVar() + " = " + element.toString() +": " + subf));
					}
					else
					{
						node = new FOTreeNode(_f, _eval, inter, 
								new String("false; " + _f.freeVar() + " = " + element.toString() +": " + subf));
					}
					_children.add(node);
				}
			}
			catch(Exception e)
			{
				throw new UserException(e.getMessage());
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see de.hu.logic.general.EvaluationTreeNode#getChildren()
	 */
	public List<FOEvaluationTreeNode> getChildren() throws UserException {
		if(_children == null)
			initialise();
		return _children;
	}

	/* (non-Javadoc)
	 * @see de.hu.logic.general.EvaluationTreeNode#getChildrenCount()
	 */
	public int getChildrenCount() throws UserException {
		if((_f.type() == Formula.and) || (_f.type() == Formula.or))
			return 2;
		else if((_f.type() == Formula.neg))
			return 1;
		else if(_f.type() == Formula.exists || _f.type() == Formula.forall)
			return this._eval.getStructure().getUniverse().size();
		else
			return 0;
	}


	/* (non-Javadoc)
	 * @see de.hu.logic.general.EvaluationTreeNode#getResult()
	 */
	public Relation getResult() throws UserException {
		if(!(_f.type() == Formula.exists || _f.type() == Formula.forall || _root))
			return new Relation("");
		try
		{
			if(_children == null)
				initialise();
			if(_result == null)
				_result = _eval.evaluate(_f, _inter);
			return _result; 
		}
		catch(Exception e)
		{
			throw new UserException("Program error: " + e.getMessage());
		}
	}
	
	void setResult(Relation res, boolean t)
	{
		_result = res;
		_true = t;
	}
	
	void root(boolean root)
	{
		_root = root;
	}

	public String getName() {
		return _name;
	}

	public void setName(String _name) {
		this._name = _name;
	}


}

// \exists x \forall y (E(x, y) \or x=y)
