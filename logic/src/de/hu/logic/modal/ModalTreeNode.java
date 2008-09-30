/**
 * Created 2008 by Stephan Kreutzer
 *
 * Copyright 2008 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.hu.gralog.app.UserException;
import de.hu.logic.general.EvaluationTreeNode;
import de.hu.logic.structure.Proposition;


/**
 * @author Stephan Kreutzer 
 *
 * This class implements the tree nodes in the evaluation algorithm for the mu calculus
 * based on a tree display. The main feature of the class is that results for a tree node are generated "on the fly"
 * at least to some degree.
 * 
 * The general procedure is as follows.
 * 
 * Every ModalTreeNode has a result field _res which contains the result of the current
 * formula and stage. If the node is selected it is this result that is being shown. The
 * result is obtained from the tree node by a call to getResult(). If the result has not been
 * computed, getResult() computes the result by a call to the evaluation algorithm given to the 
 * ModalTreeNode object as parameter to the constructor. Based on the formula type and the result 
 * the number of children is computed (initialiseChildrenCount()).
 * 
 * The list of children is computed once the tree node is expanded. This generates a call to getChildren().
 * This method generates ModalTreeNodes for its children and also computes the results for
 * the children. This is done for performance reasons as in fixed point inductions the 
 * result for the child corresponding to stage n+1 can easily be computed from the result for stage
 * n. Setting the result for the children (using setResult()) triggers the computation of the 
 * "number of children" for the children of the current node but it does not trigger the "getChildren()". This is to
 * avoid resursively generate all intermediate results which might consuming too much memory.
 * 
 */
public class ModalTreeNode implements EvaluationTreeNode 
{

	String _name;		// the name to be displayed at the tree node.
	ArrayList<EvaluationTreeNode> _childrenList = null;	//the list of children.	
	TreeNodeEvaluation _eval;	// the evaluation algorithm to be used
	Proposition _res;			// the result of the evaluation of the current formula 
	Formula _f;					// the current formula
	Interpretation _inter = new Interpretation();	// the context in which the current formula is evaluated
	int _stage = -1;				// the number of the fixed point stage of the current formula
	String _ident = "";				// the fixed point variable to be used if _stage != -1 
	int _noChildren = -1; 	// number of children of the current node
	
	

	/**
	 * Constructor for modal tree nodes where the result has not yet been
	 * computed. 
	 * @param f Formula
	 * @param eval evaluation algorithm 
	 */
	public ModalTreeNode(Formula f, TreeNodeEvaluation eval)
	{
		
		_name = f.toString();
		_f = f;
		_eval = eval;
		_res = null;
		_childrenList = null;
	}


	/**
	 * Constructor for modal tree nodes where the result has not yet been
	 * computed. 
	 * @param f Formula
	 * @param name Name of the tree node
	 * @param eval evaluation algorithm 
	 */
	public ModalTreeNode(Formula f, String name, TreeNodeEvaluation eval)
	{
		_name = name;
		_f = f;
		_eval = eval;
		_res = null;
		_childrenList = null;
	}

	/**
	 * sets the context, i.e. interpretation of the free variables, in which the current formula
	 * is to be evaluated.
	 * 
	 * @param inter Interpretation object providing an interpretation for the free variables
	 */
	public void setInterpretation(Interpretation inter)
	{
		_inter = inter;
	}
	
	/**
	 * Suppose we have a formula \mu X. \phi. The tree node corresponding to stage n of the iteration
	 * on \phi has to know the fixed point variable the fixed point iteration refers to. This
	 * is stored in the _ident variable and can be set using this method setIdent.
	 * 
	 * @param ident The name of the fixed point variable.
	 */
	public void setIdent(String ident)
	{
		_ident = ident;
	}
	
	
	/**
	 * Suppose we have a formula \mu X. \phi. The tree node corresponding to stage n of the iteration
	 * on \phi has to know the fixed point variable the fixed point iteration refers to. This
	 * is stored in the _ident variable and can be obtained using this method getIdent.
	 * 
	 * @result The name of the fixed point variable.
	 */
	public String getIdent()
	{
		return _ident;
	}
	
	
	/**
	 * Suppose we have a formula \mu X. \phi. The tree node corresponding to stage n of the iteration
	 * on \phi has to know the stage of the iteration it refers to. This
	 * is stored in the _stage variable and can be set using this method.
	 * 
	 * @result The name of the fixed point variable.
	 */
	public void setStage(int stage)
	{
		_stage = stage;
		_name = "Stage " + stage + ": " + _f.toString();
	}

	
	/**
	 * A helper method returning true if f is a fixed point formula.
	 * @param f
	 * @return returns true if f is a fixed point formula.
	 */
	Boolean isFPFormula(Formula f)
	{
		return ((f.type() == Formula.mu) ||(f.type() == Formula.nu)); 
	}

	/**
	 * Internal method used to compute the result of the current node as well as the list of children.
	 * For efficiency, this version performs a "look ahead" evaluation, i.e. the parent node generates the results of the
	 * children, at least for those children reflecting stages of a fixed point evaluation.
	 * In this way, we can generate the result for the child of stage n+1 based on the result at stage n.
	 * @throws UserException
	 */
	void initialiseResult() throws UserException
	{
		_res = _eval.recursiveEvaluate(_f, _inter);
		initialiseChildrenCount();
	}
	
	/**
	 * Assumes that _res  has been set.
	 *
	 */
	void initialiseChildrenCount()
	{
		if(isFPFormula(_f))
			_noChildren = Integer.parseInt(_res.getName());
		else if((_f.type() == Formula.and) || (_f.type() == Formula.or))
			_noChildren = 2;
		else if((_f.type() == Formula.box) || (_f.type() == Formula.neg) || (_f.type() == Formula.diamond))
			_noChildren = 1;
		else
			_noChildren = 0;
	}

	/**
	 * Internal method used to compute the list of children of the current node. See class description for a 
	 * general explanation of how the various methods interact.
	 * @throws UserException
	 */
	void initialiseChildren() throws UserException
	{
		//System.out.println("result computed " + _res.getName());
		
		ModalTreeNode node; 

		if(_stage == 0)	// stage 0 has no successors as the stage is empty
		{
			_childrenList = new ArrayList<EvaluationTreeNode>(0);
			
		}
		else
		{
			switch(_f.type())
			{
			case Formula.bottom: // no break
			case Formula.top:	// no break
			case Formula.proposition: 
				_childrenList = new ArrayList<EvaluationTreeNode>(0);
				break;
			case Formula.and:
			case Formula.or:	// add two successors and their results
				_childrenList = new ArrayList<EvaluationTreeNode>(2);
				node = new ModalTreeNode(_f.leftSubf(), _eval);
				node.setInterpretation(_inter);
				node.setResult(_eval.recursiveEvaluate(_f.leftSubf(), _inter));
				_childrenList.add(node);
				node = new ModalTreeNode(_f.rightSubf(), _eval);
				node.setInterpretation(_inter);
				node.setResult(_eval.recursiveEvaluate(_f.rightSubf(), _inter));
				_childrenList.add(node);
				break;
			case Formula.neg: 
			case Formula.diamond:		// no break. both cases treated simultaneously 
			case Formula.box:
				_childrenList = new ArrayList<EvaluationTreeNode>(1);
				node = new ModalTreeNode(_f.subf(), _eval);
				node.setInterpretation(_inter);
				node.setResult(_eval.recursiveEvaluate(_f.subf(), _inter));
				_childrenList.add(node);
				break;
			case Formula.mu:
			case Formula.nu:
				int stages = Integer.parseInt(_res.getName());
				//System.out.println("mu:" + stages);
				_childrenList = new ArrayList<EvaluationTreeNode>(stages);
				List<Proposition> list = _eval.fixedpointEvaluate(_f, _inter);
				Iterator<Proposition> iter = list.iterator();

				//System.out.println("List size: " + list.size() + " amd stage " + stages);
				Interpretation inter = (Interpretation) _inter.clone();
				Proposition prop = new Proposition("-1");
				
				for (int i=0;i<stages; i++)		// go through all stages. The result of this stage is in list.
				{
					node   = new ModalTreeNode(_f.subf(), _eval);
					node.setStage(i);
					node.setIdent(_f.ident());
					inter = (Interpretation) _inter.clone();	// important to clone this
					inter.put(_f.ident(), prop);
					node.setInterpretation(inter);
//					if(!iter.hasNext())
						//System.out.println("iter is null");
					prop = iter.next();
					node.setResult(prop);
					_childrenList.add(node);
				}
				break;
			}
		}
	}




	/* This function returns the list of children of the current node.
	 * @see de.hu.logic.general.EvaluationTreeNode#getChildren()
	 */
	
	public List<EvaluationTreeNode> getChildren() throws UserException {
		//System.out.println("getChilden of " + getName() + " at stage " + _stage + " with ident " + _ident);
		if(_childrenList == null)
			initialiseChildren();
		return _childrenList;
		
	}

	/* (non-Javadoc)
	 * @see de.hu.logic.general.EvaluationTreeNode#getChildrenCount()
	 */
	public int getChildrenCount()  throws UserException {
		//System.out.println("getChildrenCount of " + getName() + " at stage " + _stage + " with ident " + _ident);
		if(_noChildren == -1)
			initialiseResult();
		return _noChildren;
	}

	/* (non-Javadoc)
	 * @see de.hu.logic.general.EvaluationTreeNode#getName()
	 */
	public String getName() {
		if(_stage == 0)
			return "Stage 0: empty";
		else
			return _name;
	}

	/* (non-Javadoc)
	 * @see de.hu.logic.general.EvaluationTreeNode#getResult()
	 */
	public Proposition getResult()  throws UserException 
	{
//		System.out.println("getResult of " + getName() + " at stage " + _stage + " with ident " + _ident);
		if(_res == null)
			initialiseResult();
		return _res;
	}
	
	/**
	 * Method to set the result field of the node. Updates the childrenCount.
	 * 
	 * @param res Result 
	 */
	public void setResult(Proposition res)
	{
		_res = res;
		initialiseChildrenCount();
	}
	
}
