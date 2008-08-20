/**
 * 
 */
package de.hu.logic.modal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.hu.logic.general.EvaluationTreeNode;
import de.hu.logic.graph.Proposition;
import de.hu.logic.graph.TransitionSystemEdge;
import de.hu.logic.graph.TransitionSystemVertex;

/**
 * @author kreutzer
 *
 */
public class ModalTreeNode implements EvaluationTreeNode 
{

	String _name;
	ArrayList<EvaluationTreeNode> _list;
	TreeNodeEvaluation _eval;
	Proposition _res;
	Formula _f;
	Interpretation _inter = new Interpretation();
	int _stage = -1;
	String _ident = "";
	

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
		_list = null;
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
		_list = null;
	}

	public void setInterpretation(Interpretation inter)
	{
		_inter = inter;
	}
	
	public void setIdent(String ident)
	{
		_ident = ident;
	}
	
	public String getIdent()
	{
		return _ident;
	}
	
	public void setStage(int stage)
	{
		_stage = stage;
		_name = "Stage " + stage + ": " + _f.toString();
	}

	void initialiseResultChildren()
	{
		System.out.println("initialiseResultChilden of " + getName() + " at stage " + _stage + " with ident " + _ident);
		if(_stage == -1) 	
				// we are not in a case where we have to evaluate a certain 
				// number of stages
		{
					// this is an optimisation as generateChildren for fixed point formulas
					// also generates the result
			_res = _eval.recursiveEvaluate(_f, _inter);
		}
		else if(_stage == 0)
		{
			System.out.println(getIdent());
			_res = new Proposition("false");
			_inter.put(_ident, _res);
		}
		else
		{
			_res = new Proposition("false");
			System.out.println("ident " + getIdent());
			for(int i=1;i<=_stage; i++)
			{
				_inter.put(getIdent(), _res);
				_res = _eval.recursiveEvaluate(_f, _inter);
			}
		}

		System.out.println("result computed " + _res.getName());
		
		ModalTreeNode node; 

		if(_stage == 0)
		{
			_list = new ArrayList<EvaluationTreeNode>(0);
			
		}
		else
		{
			switch(_f.type())
			{
			case Formula.bottom: // no break
			case Formula.top:	// no break
			case Formula.proposition: 
				_list = new ArrayList<EvaluationTreeNode>(0);
				break;
			case Formula.and:
			case Formula.or:
				_list = new ArrayList<EvaluationTreeNode>(2);
				node = new ModalTreeNode(_f.leftSubf(), _eval);
				node.setInterpretation(_inter);
				_list.add(node);
				node = new ModalTreeNode(_f.rightSubf(), _eval);
				node.setInterpretation(_inter);
				_list.add(node);
				break;
			case Formula.neg: 
			case Formula.diamond:		// no break. both cases treated simultaneously 
			case Formula.box:
				_list = new ArrayList<EvaluationTreeNode>(1);
				node = new ModalTreeNode(_f.subf(), _eval);
				node.setInterpretation(_inter);
				_list.add(node);
				break;
			case Formula.mu:
			case Formula.nu:
				int stages = Integer.parseInt(_res.getName());
				System.out.println("mu:" + stages);
				_list = new ArrayList<EvaluationTreeNode>(stages);
				for (int i=0;i<stages; i++)
				{
					node   = new ModalTreeNode(_f.subf(), _eval);
					node.setStage(i);
					node.setInterpretation((Interpretation)_inter.clone());
					node.setIdent(_f.ident());
					_list.add(node);
				}
				break;
			}
		}
	}


	/* (non-Javadoc)
	 * @see de.hu.logic.general.EvaluationTreeNode#getChildren()
	 */
	public List<EvaluationTreeNode> getChildren() {
		if(_list == null)
			initialiseResultChildren();
		return _list;
		
	}

	/* (non-Javadoc)
	 * @see de.hu.logic.general.EvaluationTreeNode#getChildrenCount()
	 */
	public int getChildrenCount() {
		if(_list == null)
			initialiseResultChildren();
		return _list.size();
	}

	/* (non-Javadoc)
	 * @see de.hu.logic.general.EvaluationTreeNode#getName()
	 */
	public String getName() {
		return _name;
	}

	/* (non-Javadoc)
	 * @see de.hu.logic.general.EvaluationTreeNode#getResult()
	 */
	public Proposition getResult() {
		return _res;
	}
	
}
