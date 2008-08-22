package de.hu.logic.fo;

import java.util.HashSet;

import de.hu.gralog.graph.GraphWithEditableElements;
import de.hu.logic.modal.Evaluation;

public class FOEvaluation implements Evaluation 
{
	Structure _struct;
	Interpretation _inter;
	
	public FOTreeNode initialise(Structure t, Formula f)
	{
		_inter = new Interpretation(t);
		_struct = t;
		// here there should be a signature check. 
		FOTreeNode node = new FOTreeNode(f, this);
		
		return node;
	}
	
	public Relation recursiveEvaluate(Formula f)
	{
		switch(f.type())
		{
		case Formula.bottom: 
		case Formula.top: 
		case Formula.proposition:
			break;
		case Formula.eq: 
			break;
		case Formula.and: 
		case Formula.or: 
			break;
		case Formula.neg:  
			break;
		case Formula.exists: 
		case Formula.forall:
			break;
		case Formula.lfp:
		case Formula.ifp:
			break;
		}

		return new Relation(f.toString(), _struct.getUniverse());
	}
}
