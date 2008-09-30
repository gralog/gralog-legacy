package de.hu.logic.fo;

import java.util.ArrayList;
import java.util.Set;

import de.hu.logic.modal.Evaluation;

public class FOEvaluation implements Evaluation 
{
	LogicStructure _struct;
	Interpretation _inter;
	
	public LogicStructure getStructure()
	{
		return _struct;
	}
	
	public FOTreeNode initialise(LogicStructure t, Formula f)
	{
		_inter = new Interpretation(t);
		_struct = t;
		// here there should be a signature check.
		FOTreeNode node = new FOTreeNode(f, this);
		node.setRoot(true);
		return node;
	}
	
	public Relation evaluate(Formula f, Interpretation inter) throws Exception
	{
		Set<String> vars = f.freeVars();
		if(vars.size() == 0)
		{
			Relation rel = new Relation("", true);
			rel.set_true(recursiveEvaluate(f, inter));
			return rel;
		}
		else
		{
			String free = (String) vars.iterator().next();
			Relation rel = new Relation("", false);		// generate new unary relation
			for( Object element : _struct.getUniverse())
			{
				inter.setFOVar(free, element);
				if(recursiveEvaluate(f, inter))
					rel.addVertex(element);
			}
			return rel;
		}
		
	}
	

	public boolean recursiveEvaluate(Formula f, Interpretation inter) throws Exception
	{
		ArrayList<String> varlist;
		switch(f.type())
		{
		case Formula.bottom:
			return false;
		case Formula.top:
			return true;
		case Formula.proposition:
			return inter.contains(f.ident(), f.getVarList());
		case Formula.eq: 
			Object v1 = inter.getFO(f.ident());
			Object v2 = inter.getFO(f.ident2());
			return v1==v2;
		case Formula.and:
			return recursiveEvaluate(f.leftSubf(), inter) && recursiveEvaluate(f.rightSubf(), inter);  
		case Formula.or: 
			return recursiveEvaluate(f.leftSubf(), inter) || recursiveEvaluate(f.rightSubf(), inter);  
		case Formula.neg:  
			return !recursiveEvaluate(f.subf(), inter);  
		case Formula.exists:
//			System.out.println("Exists " + f.ident());
			for( Object element : _struct.getUniverse())
			{
//				System.out.println("Element" + element.toString());
				inter.setFOVar(f.ident(), element);
				if(recursiveEvaluate(f.subf(), inter))
					return true;
			}
			return false;
		case Formula.forall:
			for( Object element : _struct.getUniverse())
			{
				inter.setFOVar(f.ident(), element);
				if(!recursiveEvaluate(f.subf(), inter))
					return false;
			}
			return true;
		case Formula.lfp:
		case Formula.ifp:
			throw new Exception("Fixed point quantifiers not yet implemented");
		}
		throw new Exception("Unhandled case in evaluation");
	}
}
