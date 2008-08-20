/**
 * 
 */
package de.hu.logic.modal;

import java.util.ArrayList;

import de.hu.logic.graph.Proposition;
import de.hu.logic.graph.TransitionSystem;
import de.hu.logic.graph.TransitionSystemVertex;

/**
 * @author kreutzer
 *
 */
public class DummyEvaluation implements Evaluation 
{
	TransitionSystem _trans;
	Formula _form;
	
	public DummyTreeNode evaluate(TransitionSystem trans, Formula f)
	{
		_form = f;
		_trans = trans;
		Proposition prop = new Proposition("");
		prop.setVertices(new ArrayList<TransitionSystemVertex>(trans.vertexSet()));
		return new DummyTreeNode("0", prop, this);
	}

	public TransitionSystem getT()
	{ return _trans; }
	
	public Formula getFormula()
	{ return _form; }
}
