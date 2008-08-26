/**
 * Created 2008 by Stephan Kreutzer
 *
 * Copyright 2008 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (stephan.kreutzer@comlab.ox.ac.uk)
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
import java.util.Set;

import de.hu.gralog.app.UserException;



/**
 * This class implements the tree nodes in the result display of the simple FO evaluation algorithm.
 * Every node in the result tree to be seen in the algorithm result window corresponds to a 
 * FOTreeNode object.
 * 
 * The interplay between FOTreeNode and FOEvaluation works as follows.
 * After the user hits the execute button,  the FirstOrderLogicsSimpleResultContentTreeNode class executes the
 * initialise method of FOEvaluation. This creates a plain FOTreeNode object.
 * If the user clicks on the corresponding tree node, the method getResult of the FOTreeNode objects is called.
 * getResults then uses the evaluate method of the FOEvaluation object to compute the result for the current node.
 * 
 * The result computed for a node depends on the type of formula the current node represents and also
 * on whether it is the root node or not.
 * 
 * If the current node is a root node and the formula it represents has a free variable,
 * then the result is the set of all vertices satisfying the formula. Further, for each vertex in the structure
 * the node generates a child. The label of the child indicates how the free variable was interpreted and whether
 * this interpretation satisfies the formula. It then lists the subformula. If the formula has no free variable, 
 * then the normal display rules are applied.
 * 
 * If the current node is not the root node, then the result depends on the type of formula.
 * If the current formula starts with a quantifier \exists x phi(x) or \forall x phi(x)
 * then the result for the current node is { a : G |= phi(a) }.
 * Further, for each element v in the structure G there is a child whose label indicates whether setting
 * x = v satisfies the formula.
 * 
 *  If the current node is not a quantified formula then the interpretation for the variables occurring free
 *  in the current formula are shown as result. E.g. if phi := E(x, y) then the result contains the 
 *  vertices interpreting x and y.
 * 
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
	
	/**
	 * This method sets the _root field of the object. If set to true, the node assumes that
	 * it is the root node of the result tree. This influences the way the result is computed. See the class description
	 * for further information.
	 * By default, nodes are created as non-roots, so this method only needs to be called for the root node.
	 * @param root true if the current node is the root node, false otherwise.
	 */
	public void setRoot(boolean root)
	{
		_root = root;
		if(root ==true)
		{
			try
			{
				if(_f.width()==1)
				{
					_name = new String("{ " + _f.freeVar() + " : " + _name + " } ");
				}
			}
			catch(Exception e)
			{}		// do nothing. if there is an exception, the name isn't changed.
		}
	}

	/**
	 * Initialises the list of children.
	 * This depends on whether the current node is the root or not. See class description for further onformation.
	 * @throws UserException
	 */
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
						String sat;
						if(_eval.recursiveEvaluate(_f.subf(), inter))
						{
							sat = "true";
							_result.addVertex(element);
						}
						else sat = "false";
						if(_f.subf().type() == Formula.exists || _f.subf().type() == Formula.forall)
						{
							String var = _f.subf().ident();
							node = new FOTreeNode(_f.subf(), _eval, inter, 
										new String(_f.ident() + " = " + element.toString() + ": "+sat+"; { " +var+ " : "+ subf + "}"));
						}
						else
						{
							node = new FOTreeNode(_f.subf(), _eval, inter, 
								new String(_f.ident() + " = " + element.toString() +": "+sat+"; "+ subf));
						}
/*						if(_eval.recursiveEvaluate(_f.subf(), inter))
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
	  */
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
					String sat;
					if(_eval.recursiveEvaluate(_f, inter))
						sat = "true";
					else sat = "false";
					if(_f.type() == Formula.exists || _f.type() == Formula.forall)
					{
						String var = _f.ident();
						node = new FOTreeNode(_f, _eval, inter, 
									new String(_f.freeVar() + " = " + element.toString() + ": "+sat+"; { " +var+ " : "+ subf + "}"));
					}
					else
					{
						node = new FOTreeNode(_f, _eval, inter, 
							new String(_f.freeVar() + " = " + element.toString() +": "+sat+"; "+ subf));
					}

/*					if(_eval.recursiveEvaluate(_f, inter))
					{
						if(_f.type() == Formula.exists || _f.type() == Formula.forall)
						{
							String var = _f.ident();
							node = new FOTreeNode(_f, _eval, inter, 
										new String(_f.freeVar() + " = " + element.toString() + ": true; { " +var+ " : "+ subf + "}"));
						}
						else
						{
							node = new FOTreeNode(_f, _eval, inter, 
								new String(_f.freeVar() + " = " + element.toString() +": true; \\phi(" +_f.freeVar() + ") := "+ subf));
						}
					}
					else
					{
						if(_f.type() == Formula.exists || _f.type() == Formula.forall)
						{
							String var = _f.ident();
							node = new FOTreeNode(_f, _eval, inter, 
										new String(_f.freeVar() + " = " + element.toString() + ": false; { " +var+ " : "+ subf + "}"));
						}
						else
						{
							node = new FOTreeNode(_f, _eval, inter, 
								new String(_f.freeVar() + " = " + element.toString() +": false; \\phi(" +_f.freeVar() + ") := "+ subf));
						}
//						node = new FOTreeNode(_f, _eval, inter, 
//								new String("false; " + _f.freeVar() + " = " + element.toString() +": \\phi(" +_f.freeVar() + ") := "+  subf));
					}*/
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
//		System.out.println("computing result for "+_f.toString() + " free vars " + _f.freeVars());
		if(!(_f.type() == Formula.exists || _f.type() == Formula.forall || _root))
		{
			if(_result != null)
				return _result;
			_result = new Relation("");
			Set<String> freevars = _f.freeVars();
			for( String var : freevars)
			{
				_result.addVertex(_inter.getFO(var));
			}
			return _result;
		}
		else
		{
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
