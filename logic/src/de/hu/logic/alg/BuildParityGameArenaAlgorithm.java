package de.hu.logic.alg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import org.jgrapht.graph.DefaultEdge;

import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.DirectedGraph;
import de.hu.logic.graph.Proposition;
import de.hu.logic.graph.TransitionSystem;
import de.hu.logic.graph.TransitionSystemEdge;
import de.hu.logic.graph.TransitionSystemVertex;
import de.hu.logic.modal.Formula;
import de.hu.parity.graph.ParityGameVertex;

public class BuildParityGameArenaAlgorithm {
	
	private TransitionSystem transitionSystem;
	private Formula formula;
	private int maxNesting;
	
	private DirectedGraph<ParityGameVertex, DefaultEdge> arena = new DirectedGraph<ParityGameVertex, DefaultEdge>(ParityGameVertex.class);
	
	private HashMap<String, Stack<String>> fpVarSubstitution;
	
	private HashMap<String, TransitionSystemVertex> tsVertices = new HashMap<String, TransitionSystemVertex>();
	private HashMap<String, ParityGameVertex> arenaVertices = new HashMap<String, ParityGameVertex>();
	private HashMap<String, Formula> fpVars = new HashMap<String, Formula>();
	
	public BuildParityGameArenaAlgorithm (TransitionSystem ts, Formula f) {
		transitionSystem = ts;
		formula = f;
	}
	
	private String buildVertexLabel(String vertex, Formula f) {
		return vertex.trim() + ", " + f.toString();
	}
	
	public DirectedGraph<ParityGameVertex, DefaultEdge> execute() throws UserException {
		for ( TransitionSystemVertex tsv : transitionSystem.vertexSet()) {
			tsVertices.put(tsv.getLabel(), tsv);
		}
//		zu Testzwecken, verwende folgende Formel (entspricht "(X3\and\nuX.\muX.(<>P\andX\andX2))"):
//		Formula formula = new Formula( Formula.and, new Formula(Formula.proposition, "X3"), new Formula(Formula.nu,"X",new Formula(Formula.mu,"X",new Formula(Formula.and, new Formula(Formula.and, new Formula (Formula.diamond, "", new Formula (Formula.proposition,"P")), new Formula (Formula.proposition,"X")), new Formula(Formula.proposition, "X2")))));
		formula = getOrderlyFormula(formula);
		
		maxNesting = getMaxFPNesting(formula, 0);

		for ( TransitionSystemVertex tsv : transitionSystem.vertexSet()) {
			buildArena(tsv.getLabel(), formula, 0);
		}
		
		return arena;
		
	}
	
	private int getMaxFPNesting(Formula f, int nesting) throws UserException {
		switch(f.type())
		{
		case Formula.bottom:
		case Formula.top:
		case Formula.proposition:
			return nesting;
			
		case Formula.and:
		case Formula.or:
			return Math.max(getMaxFPNesting(f.leftSubf(), nesting), getMaxFPNesting(f.rightSubf(), nesting));
			
		case Formula.neg:			
		case Formula.diamond:			
		case Formula.box:
			return getMaxFPNesting(f.subf(),nesting);
			
		case Formula.mu:
			// if nesting is odd, increase it
			return getMaxFPNesting(f.subf(),(nesting % 2 == 0)?(nesting+1):(nesting));
			
		case Formula.nu:
			// if nesting is even, increase it
			return getMaxFPNesting(f.subf(),(nesting % 2 == 0)?(nesting):(nesting+1));

//		case Formula.sub:
			
		default:
			throw new UserException( "ParseError", "The subformula: " + f + " has a wrong syntax.");
		}	
	}
	
	private boolean isNumeric(char c) {
	  if ((c >= '0') && (c <= '9')) return true;
	  return false;
	}
	
	public HashSet<String> getFreeVariables(Formula f) throws UserException {		
		return getFreeVariablesRec(f, new HashSet<String>());
	}
	
	private HashSet<String> getFreeVariablesRec(Formula f, HashSet<String> boundVariables) throws UserException {		
		HashSet<String> freeVariables = new HashSet<String>();
		
		switch(f.type())
		{
		case Formula.bottom:
		case Formula.top:
			return freeVariables;
			
		case Formula.proposition:
			if (!boundVariables.contains(f.ident()))
				freeVariables.add(f.ident());
			return freeVariables;
			
		case Formula.and:
		case Formula.or:
			freeVariables.addAll(getFreeVariablesRec(f.leftSubf(), boundVariables));
			freeVariables.addAll(getFreeVariablesRec(f.rightSubf(), boundVariables));
			return freeVariables;
			
		case Formula.neg:			
		case Formula.diamond:			
		case Formula.box:
			return getFreeVariablesRec(f.subf(), boundVariables);
			
		case Formula.mu:			
		case Formula.nu:
			HashSet<String> newBoundVariables = new HashSet<String>(boundVariables);
			newBoundVariables.add(f.ident());
			return getFreeVariablesRec(f.subf(), newBoundVariables);

//		case Formula.sub:
			
		default:
			throw new UserException( "ParseError", "The subformula: " + f + " has a wrong syntax.");
		}	
		
	}
	
	public Formula getNNF(Formula f) throws UserException {
		return getNNFRec(f, new HashSet<String>(), false);
	}


	/**
	 * Negates the given formula rekursively. If a fixed-point operator is to be negated, the
	 * fixed-point variable is substituted by its negation before the negation of the subformula.
	 * Thus it "stays the same".
	 * 
	 * @param negVar	Contains all fixed-point variables, that are to be negated
	 * @param neg		This flag is true, if the formula is to be negated
	 */
	private Formula getNNFRec(Formula f, HashSet<String> negVar, boolean neg) throws UserException {
		int tmpType;
		
		switch(f.type())
		{
		case Formula.bottom:
		case Formula.top:
			if(neg) {
				// switch type
				tmpType = (f.type() == Formula.top) ? (Formula.bottom) : (Formula.top);
				return new Formula(tmpType, f.ident());
			} else
				return f;
			
		case Formula.proposition:
			// negate, if necessary
			if(neg ^ negVar.contains(f.ident()))
				return new Formula(Formula.neg, f);
			else
				return f;
			
		case Formula.and:
		case Formula.or:
			// switch type, if (neg == true)
			tmpType = ((f.type() == Formula.or) ^ neg) ? (Formula.or) : (Formula.and);			
			return new Formula(tmpType, getNNFRec(f.leftSubf(), negVar, neg), getNNFRec(f.rightSubf(), negVar, neg));
			
		case Formula.neg:
			// switch negation-flag
			return getNNFRec(f.subf(), negVar, !neg);
			
		case Formula.diamond:
		case Formula.box:
			// switch type, if (neg == true)
			tmpType = ((f.type() == Formula.box) ^ neg) ? (Formula.box) : (Formula.diamond);			
			return new Formula(tmpType, getNNFRec(f.subf(), negVar, neg));
			
		case Formula.mu:
		case Formula.nu:
			HashSet<String> newNegVar = new HashSet<String>(negVar);
			if (neg) {
				// switch type
				tmpType = (f.type() == Formula.nu) ? (Formula.mu) : (Formula.nu);
				newNegVar.add(f.ident());
			} else {
				tmpType = f.type();
				if (newNegVar.contains(f.ident()))
					newNegVar.remove(f.ident());
			}
			return new Formula(tmpType, f.ident(), getNNFRec(f.subf(), newNegVar, neg));

//		case Formula.sub:
			
		default:
			throw new UserException( "ParseError", "The subformula: " + f + " has a wrong syntax.");
		}	
	}
	
	public Formula getOrderlyFormula(Formula f) throws UserException {
		fpVarSubstitution = new HashMap<String, Stack<String>>();
		
		f = getNNF(f);
		
		/* All names of free variables are reserved, so that fpVars with
		 * the same name will have to be renamed
		 */
		for (String v : getFreeVariables(f)) {
			fpVarSubstitution.put(v, new Stack<String>());
		}
		
		Formula tmpForm = getOrderlyFormulaRec(f);
		
		if (!tmpForm.valid())
			throw new UserException( "SyntaxError", "A fixed-point variable is used negatively.\n\n"
									+ "Orderly formula in NNF: \n'" + tmpForm.toString() + "'" );

		return tmpForm;
	}
	
	private Formula getOrderlyFormulaRec(Formula f) throws UserException {
		switch(f.type())
		{
		case Formula.bottom:
		case Formula.top:
			return f;
			
		case Formula.proposition:
			Stack<String> substitute = fpVarSubstitution.get(f.ident());
			
			if (!substitute.empty()) 
				// rename proposition
				return new Formula(f.type(), substitute.peek());
			else
				return f;
			
		case Formula.and:
		case Formula.or:
			return new Formula(f.type(), getOrderlyFormulaRec(f.leftSubf()), getOrderlyFormulaRec(f.rightSubf()));
			
		case Formula.neg:
		case Formula.diamond:
		case Formula.box:
			return new Formula(f.type(), f.ident(), getOrderlyFormulaRec(f.subf()));
			
		case Formula.mu:			
		case Formula.nu:
			boolean opChanged = false;
			String op = f.ident();
			
			// if the identifier "op" is used, search for a free one:
			if (fpVarSubstitution.containsKey(op)) {
				int tmpNumber = 1;
				int i = op.length();
				
				// get the position, where the number in the operatorname starts
				while (isNumeric(op.charAt(i - 1)))
					i--;
				
				if (i != op.length()) {
					// get the number from the operatorname
					tmpNumber = Integer.parseInt(op.substring(i));					
				}
				
				// operatorname without the number
				String tmpPrefix = op.substring(0, i);
				
				// search for the next valid operatorname
				do {
					tmpNumber++;
					op = tmpPrefix + tmpNumber;
				} while (fpVarSubstitution.containsKey(op));
				
				opChanged = true;
			}

			// now that op is a valid operatorname, reserve it
			fpVarSubstitution.put(op, new Stack<String>());
			
			if (opChanged)
				// substitution for f.ident() in subformula is added
				fpVarSubstitution.get(f.ident()).push(op);
			
			Formula returnValue = new Formula(f.type(), op, getOrderlyFormulaRec(f.subf()));
			
			if (opChanged)
				// substitution for f.ident() in subformula is removed
				fpVarSubstitution.get(f.ident()).pop();
			
			return returnValue;
			
		default:
			throw new UserException( "ParseError", "The subformula: " + f + " has a wrong syntax.");
		}
	}
	
	private ParityGameVertex buildArena(String tsVertex, Formula f, int nesting) throws UserException {
		
		String vertexLabel = buildVertexLabel(tsVertex, f);
		
		if (arenaVertices.containsKey(vertexLabel))
			return arenaVertices.get(vertexLabel);
		
		
		ParityGameVertex returnVertex = new ParityGameVertex();
		returnVertex.setLabel(vertexLabel);
		returnVertex.setPriority(maxNesting);

		arena.addVertex(returnVertex);
		arenaVertices.put(vertexLabel, returnVertex);
		

		boolean fulfilsProposition;
		
		switch(f.type())
		{
		case Formula.bottom:
			returnVertex.setPlayer0(true);			
			break;
			
		case Formula.top:
			returnVertex.setPlayer0(false);			
			break;

		case Formula.proposition:
			fulfilsProposition = false;
			for (Proposition p : tsVertices.get(tsVertex).getPropositions()) {
				if (p.getName().equals(f.ident())) {
					fulfilsProposition = true;
					break;
				}
			}
			returnVertex.setPlayer0(!fulfilsProposition);
			
			if (fpVars.containsKey(f.ident()))
				arena.addEdge( returnVertex, buildArena(tsVertex, fpVars.get(f.ident()), nesting) );			
			break;
			
		case Formula.neg:
			fulfilsProposition = false;
			for (Proposition p : tsVertices.get(tsVertex).getPropositions()) {
				if (p.getName().equals(f.subf().ident())) {
					fulfilsProposition = true;
					break;
				}
			}
			returnVertex.setPlayer0(fulfilsProposition);		
			break;

		case Formula.and:
		case Formula.or:
			returnVertex.setPlayer0(f.type() == Formula.or);
			
			arena.addEdge( returnVertex, buildArena(tsVertex, f.leftSubf(), nesting) );
			arena.addEdge( returnVertex, buildArena(tsVertex, f.rightSubf(), nesting) );
			break;
			
		case Formula.diamond:
		case Formula.box:
			returnVertex.setPlayer0(f.type() == Formula.diamond);	
			
			for (TransitionSystemEdge e : transitionSystem.outgoingEdgesOf(tsVertices.get(tsVertex))) {
				arena.addEdge( returnVertex, buildArena(transitionSystem.getEdgeTarget(e).getLabel(), f.subf(), nesting) );				
			}
			break;
			
		case Formula.mu:
		case Formula.nu:
			returnVertex.setPlayer0(false);
			fpVars.put(f.ident(), f);
			
			// if (nesting is even and f a mu-Formula) or (uneven and a nu-Formula) increase nesting
			// (realized by XOR)
			if ((nesting % 2 == 0) ^ (f.type() == Formula.nu))
				nesting++;
			
			returnVertex.setPriority(nesting);
			
			arena.addEdge( returnVertex, buildArena(tsVertex, f.subf(), nesting) );
			break;

//		case Formula.sub:
			
		default:
			throw new UserException( "ParseError", "The subformula: " + f + " has a wrong syntax.");
		}		
		
		return returnVertex;
	}

}
