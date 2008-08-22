package de.hu.logic.fo;

import de.hu.logic.graph.TransitionSystem;
import de.hu.logic.modal.Evaluation;

public class FOEvaluation implements Evaluation 
{
	public FOTreeNode evaluate(TransitionSystem t, Formula f)
	{
		FOTreeNode node = new FOTreeNode("root");
		
		return node;
	}
}
