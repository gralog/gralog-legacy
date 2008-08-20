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
public class ModalTreeNode implements EvaluationTreeNode 
{

	String _name;
	ArrayList<EvaluationTreeNode> _list;
	TreeNodeEvaluation _eval;
	Proposition _res;
	
	public ModalTreeNode(String name, TreeNodeEvaluation eval)
	{
		_name = name;
		_list = new ArrayList<EvaluationTreeNode>();
		_eval = eval;
		_res = null;
	}

	
	
	/* (non-Javadoc)
	 * @see de.hu.logic.general.EvaluationTreeNode#getChildren()
	 */
	public List<EvaluationTreeNode> getChildren() {
		return _list;
	}

	/* (non-Javadoc)
	 * @see de.hu.logic.general.EvaluationTreeNode#getChildrenCount()
	 */
	public int getChildrenCount() {
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
//		if(_res==null)
			
		return _res;
	}
	
	public void setResult(Proposition res)
	{
		_res = res;
	}

	
}
