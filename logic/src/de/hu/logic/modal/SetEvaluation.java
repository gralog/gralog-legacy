/*
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

package de.hu.logic.modal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import de.hu.logic.graph.Proposition;
import de.hu.logic.graph.TransitionSystem;
import de.hu.logic.graph.TransitionSystemEdge;
import de.hu.logic.graph.TransitionSystemVertex;

/**
 * This is a very simple evaluation algorithm that evaluates the formula
 * from the inside out in the most naive way.
 * Note that if the formula uses a proposition symbol that is not part of
 * the signature of the TransitionSystem (nor a fixed-point variable) then
 * no error is generated but the propositions is considered to be false on 
 * all vertices.
 * @author Stephan Kreutzer
 *
 */
public class SetEvaluation
{
	HashMap<String,Proposition> _interp;
	TransitionSystem t; 
	
	private HashSet<String> _sig;	// used only internally in the checkSignatures method
	
	Proposition interpretation(String name)
	{
		Proposition p;
		if(_interp.containsKey(name))
			return _interp.get(name);
		else if((p=t.getProposition(name)) != null) 
		{
			return p.copy();
		}
		else
			return null;
	}
	
	/** The main method of the class. Call this method to evaluate the formula f in the transition system trans. 
	 * 
	 * @param trans Transition system
	 * @param f Formula to evaluate
	 * @return Returns the relation containing the set of vertices at which the formula becomes true.
	 * @throws EvaluationException Exception thrown if for some reason the formula cannot be evaluated in the system. See EvaluationException class description for the various reasons why this can happen.
	 */
	public Proposition evaluate(TransitionSystem trans, Formula f) throws EvaluationException
	{
		t = trans;
		_interp = new HashMap<String,Proposition>();
		_sig = new HashSet<String>(t.getSignature());
		checkSignatures(f);
		return recursiveEvaluate(f);
	}
	

	/**
	 * Checks if the transition system provides the relations used by the formula.
	 * @param f Formula to be ckecked.
	 * @return true if the formula matches the signature of the transition system.
	 * @throws EvaluationException Throws an exception in case of an error.
	 */
	protected boolean checkSignatures(Formula f) throws EvaluationException
	{
		_sig = new HashSet<String>(t.getSignature());
		return checkSignaturesRec(f);
	}
	
	/**
	 * This method checks whether the formula quantifies over a relation variable
	 * that occurs as a proposition in the transition system. At the moment the method assumes that all
	 * substitutions have been performed. 
	 * @return true if the signature of the formula and the TS are distinct, false otherwise
	 */
	private boolean checkSignaturesRec(Formula f) throws EvaluationException
	{
		switch(f.type())
		{
		case Formula.bottom: // no break
		case Formula.top:	// no break
			return true;
		case Formula.proposition: 
			if(!_sig.contains(f.ident()))
			{
				throw new EvaluationException(EvaluationException.SIGNATURE_MISMATCH, "Signatures do not match as proposition " + f.ident() + " is not declared");
			}
			return true; 
		case Formula.and:
		case Formula.or: 
			return checkSignaturesRec(f.rightSubf()) & checkSignaturesRec(f.leftSubf());  
		case Formula.neg: 
		case Formula.diamond:		// no break. both cases treated simultaneously 
		case Formula.box:
			return checkSignaturesRec(f.subf());  
		case Formula.mu:
		case Formula.nu:
			if(t.getProposition(f.ident()) == null)
			{
				_sig.add(f.ident());
				return checkSignaturesRec(f.subf());
			}
			else
				throw new EvaluationException(EvaluationException.SIGNATURE_MISMATCH, "Proposition " + f.ident() + " is defined multiple times.");
		default: return true;
		}
	}
	
	
	/**
	 * The main evaluation function. The method evaluate checks the signature first and then calls this method to 
	 * do the actual evaluation.
	 * @param f Formula
	 * @return returns the set of vertices satisfying the formula. 
	 */
	protected Proposition recursiveEvaluate(Formula f)
	{
		Proposition rel=null, r=null;
		switch(f.type())
		{
		case Formula.bottom: rel = new Proposition("false"); break;
		case Formula.top:  
			rel = new Proposition("true"); 
			rel.setVertices( t.vertexSet() );
			break;
		case Formula.proposition: 
			rel =  interpretation(f.ident());
			break;
		case Formula.and:
			r = recursiveEvaluate(f.rightSubf());
			rel = recursiveEvaluate(f.leftSubf());
			return rel.intersection(r);
		case Formula.or: 
			r = recursiveEvaluate(f.rightSubf());
			rel = recursiveEvaluate(f.leftSubf());
			return rel.union(r);
		case Formula.neg:
			rel = recursiveEvaluate(f.subf());
			return rel.negate(t.vertexSet());
		case Formula.diamond:		// no break. both cases treated simultaneously 
		case Formula.box:
			boolean box = false;
			if(f.type() == Formula.box)
				box = true;
			r = recursiveEvaluate(f.subf());
			rel = new Proposition("");
			Iterator<TransitionSystemVertex> iter = t.vertexSet().iterator();
			Iterator<TransitionSystemEdge> out;
			boolean boxAll = true;
			TransitionSystemVertex u,v;
			TransitionSystemEdge edge;
			while(iter.hasNext())
			{
				boxAll = true;
				u = iter.next();
					// iterator on the outgoing edges
				out = t.outgoingEdgesOf(u).iterator();
				while(out.hasNext())	// at the end: boxAll == true if box and the vertex is not to be added
				{
					edge = out.next();
							// check if the label of the edge matches the label in the modal operators.
					if(edge.getLabel().equals(f.ident()) || f.ident().equals(""))	// the f.ident().equals("") means that the operator <>phi matches all labels 
					{
						if(r.containsVertex( t.getEdgeTarget( edge ) ) )	
											// in the diamond case we have found a suitable successor. 
						{					// so we can add u and stop.
							if(!box)
							{	
								rel.addVertex(u);
								break;
							}
						}
						else	// in the box case we have found a counter example and can stop as well
						{
							if(box)
								boxAll = false;
						}
					}
				}
				if(box && boxAll)		// boxAll is set only in the case we haven't found a counter example 
					rel.addVertex(u);
			}
			break;
		case Formula.mu:
			rel = new Proposition(f.ident());
			_interp.put(f.ident(), rel);
			Proposition old = new Proposition("old");
			int i=0;
			do
			{
				i++;
				old = rel.copy();
				rel = recursiveEvaluate(f.subf());
				rel.setName("Stage " + i + " \\mu " + f.ident() + ".");
				_interp.put(f.ident(), rel);
			}
			while(!old.equalContent(rel));
			_interp.remove(f.ident());	// remove the fixed-point variable from the interpretation
			return rel;
		case Formula.nu: 
			rel = new Proposition(f.ident());
			rel.setVertices(t.vertexSet());		// init greatest fixed-point induction
			_interp.put(f.ident(), rel);
			old = new Proposition("old");
			i=0;
			do
			{
				i++;
				old = rel.copy();
				rel = recursiveEvaluate(f.subf());
				rel.setName("Stage " + i + " \\mu " + f.ident() + ".");
				_interp.put(f.ident(), rel);
			}
			while(!old.equalContent(rel));
			_interp.remove(f.ident());	// remove the fixed-point variable from the interpretation
			return rel;
		}
		return rel;
		
	}
}
