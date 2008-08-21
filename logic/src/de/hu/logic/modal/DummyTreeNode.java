/**
 * 
 */
package de.hu.logic.modal;

import java.util.ArrayList;
import java.util.List;

import de.hu.logic.general.EvaluationTreeNode;
import de.hu.logic.graph.Proposition;

/**
 * @author kreutzer
 *
 */
public class DummyTreeNode implements EvaluationTreeNode 
{

	String _name;
	ArrayList<EvaluationTreeNode> _list;
	Proposition _prop;
	DummyEvaluation _eval;
	Proposition _res;
	
	public DummyTreeNode(String name, Proposition res, DummyEvaluation eval)
	{
		System.out.println("Const");
		_name = name;
		_list = new ArrayList<EvaluationTreeNode>(10);
		_eval = eval;
		_res = res;
	}
	/* (non-Javadoc)
	 * @see de.hu.logic.general.EvaluationTreeNode#getChildren()
	 */
	public List<EvaluationTreeNode> getChildren() 
	{
		System.out.println("Get Children");
		for (int i=0;i<10;i++)
		{
			_list.add(new DummyTreeNode(_name + ":"+i, _res, _eval));
		}
		return _list;
	}

	/* (non-Javadoc)
	 * @see de.hu.logic.general.EvaluationTreeNode#getChildrenCount()
	 */
	public int getChildrenCount() {

		return 10;
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