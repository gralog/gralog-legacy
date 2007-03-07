package de.hu.logic.alg;

import java.util.ArrayList;
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
	
	private HashMap<String, Stack<String>> fpOpSubstitution;
	
	private HashMap<String, TransitionSystemVertex> tsVertices = new HashMap<String, TransitionSystemVertex>();
	private HashMap<String, ParityGameVertex> arenaVertices = new HashMap<String, ParityGameVertex>();
	private HashMap<String, Formula> fpOperators = new HashMap<String, Formula>();
	
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
//		zu Testzwecken, verwende Formel (entspricht "(X3\and\muX.\nuX.(<>P\andX\andX2))"):
//		Formula formula = new Formula( Formula.and, new Formula(Formula.proposition, "X3"), new Formula(Formula.mu,"X",new Formula(Formula.nu,"X",new Formula(Formula.and, new Formula(Formula.and, new Formula (Formula.diamond, "", new Formula (Formula.proposition,"P")), new Formula (Formula.proposition,"X")), new Formula(Formula.proposition, "X2")))));
		maxNesting = getMaxFPNesting(formula, 0);
		
		formula = getOrderlyFormula(formula);		

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
		return getFreeVariablesRek(f, new HashSet<String>(), new HashSet<String>());
	}
	
	private HashSet<String> getFreeVariablesRek(Formula f, HashSet<String> freeVariables, HashSet<String> boundVariables) throws UserException {		
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
			freeVariables.addAll(getFreeVariablesRek(f.leftSubf(), freeVariables, boundVariables));
			freeVariables.addAll(getFreeVariablesRek(f.rightSubf(), freeVariables, boundVariables));
			return freeVariables;
			
		case Formula.neg:			
		case Formula.diamond:			
		case Formula.box:
			return getFreeVariablesRek(f.subf(), freeVariables, boundVariables);
			
		case Formula.mu:			
		case Formula.nu:
			boundVariables.add(f.ident());
			return getFreeVariablesRek(f.subf(), freeVariables, boundVariables);

//		case Formula.sub:
			
		default:
			throw new UserException( "ParseError", "The subformula: " + f + " has a wrong syntax.");
		}	
		
	}
	
	public Formula getOrderlyFormula(Formula f) throws UserException {
		fpOpSubstitution = new HashMap<String, Stack<String>>();
		
		/* All names of free variables are reserved, so that fpOperaters with
		 * the same name will have to be renamed
		 */
		for (String v : getFreeVariables(f)) {
			fpOpSubstitution.put(v, new Stack<String>());
		}
		
		return getOrderlyFormulaRek(f);
	}
	
	private Formula getOrderlyFormulaRek(Formula f) throws UserException {
		switch(f.type())
		{
		case Formula.bottom:
		case Formula.top:
			return f;
			
		case Formula.proposition:
			Stack<String> substitute = null;
			
			if (fpOpSubstitution.containsKey(f.ident()))
				substitute = fpOpSubstitution.get(f.ident());
			else
				fpOpSubstitution.put(f.ident(), new Stack<String>());
			
			if ( (substitute != null) && (!substitute.empty()) )
				// rename proposition:
				return new Formula(f.type(), substitute.peek());
			// else:
			return f;
			
		case Formula.and:
		case Formula.or:
			return new Formula(f.type(), getOrderlyFormulaRek(f.leftSubf()), getOrderlyFormulaRek(f.rightSubf()));
			
		case Formula.neg:			
		case Formula.diamond:			
		case Formula.box:
			return new Formula(f.type(), f.ident(), getOrderlyFormulaRek(f.subf()));
			
		case Formula.mu:			
		case Formula.nu:			
			String newOp = f.ident();
			
			if (fpOpSubstitution.containsKey(newOp)) {
				int i = newOp.length();
				
				// get the position, where the number in the operatorname starts
				while (isNumeric(newOp.charAt(i - 1)))
					i--;
				
				int tmpNumber = 1;
				if (newOp.length() != i) {
					// get the number from the operatorname
					tmpNumber = Integer.parseInt(newOp.substring(i));					
				}
				
				// operatorname without the number
				String tmpPrefix = newOp.substring(0, i);
				
				// search for the next valid operatorname
				do {
					tmpNumber++;
					newOp = tmpPrefix + tmpNumber;
				} while (fpOpSubstitution.containsKey(newOp));
			}

			// now newOp is a valid operatorname
			fpOpSubstitution.put(newOp, new Stack<String>());
			
			boolean changed = !newOp.equals(f.ident());
			if (changed)
				// substitution for f.ident() in subformula is added
				fpOpSubstitution.get(f.ident()).push(newOp);
			
			Formula returnValue = new Formula(f.type(), newOp, getOrderlyFormulaRek(f.subf()));
			
			if (changed)
				// substitution for f.ident() in subformula is removed
				fpOpSubstitution.get(f.ident()).pop();
			
			return returnValue;
			
		default:
			throw new UserException( "ParseError", "The subformula: " + f + " has a wrong syntax.");
		}
	}
	
	public ParityGameVertex buildArena(String tsVertex, Formula f, int nesting) throws UserException {
		
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
			
			if (fpOperators.containsKey(f.ident()))
				arena.addEdge( returnVertex, buildArena(tsVertex, fpOperators.get(f.ident()), nesting) );			
			break;

		case Formula.and:
			returnVertex.setPlayer0(false);
			
			arena.addEdge( returnVertex, buildArena(tsVertex, f.leftSubf(), nesting) );
			arena.addEdge( returnVertex, buildArena(tsVertex, f.rightSubf(), nesting) );
			break;
			
		case Formula.or:
			returnVertex.setPlayer0(true);
			
			arena.addEdge( returnVertex, buildArena(tsVertex, f.leftSubf(), nesting) );
			arena.addEdge( returnVertex, buildArena(tsVertex, f.rightSubf(), nesting) );
			break;
			
		case Formula.neg:
			//TODO: test if in NNF
			fulfilsProposition = false;
			for (Proposition p : tsVertices.get(tsVertex).getPropositions()) {
				if (p.getName().equals(f.subf().ident())) {
					fulfilsProposition = true;
					break;
				}
			}
			returnVertex.setPlayer0(fulfilsProposition);		
			break;
			
		case Formula.diamond:
			returnVertex.setPlayer0(true);
			
			for (TransitionSystemEdge e : transitionSystem.outgoingEdgesOf(tsVertices.get(tsVertex))) {
				arena.addEdge( returnVertex, buildArena(transitionSystem.getEdgeTarget(e).getLabel(), f.subf(), nesting) );				
			}
			break;
			
		case Formula.box:
			returnVertex.setPlayer0(false);	
			
			for (TransitionSystemEdge e : transitionSystem.outgoingEdgesOf(tsVertices.get(tsVertex))) {
				arena.addEdge( returnVertex, buildArena(transitionSystem.getEdgeTarget(e).getLabel(), f.subf(), nesting) );				
			}
			break;
			
		case Formula.mu:
			returnVertex.setPlayer0(false);
			if (!fpOperators.containsKey(f.ident()))
				fpOperators.put(f.ident(), f);
			
			returnVertex.setPriority((nesting % 2 == 0)?(++nesting):(nesting));
			
			arena.addEdge( returnVertex, buildArena(tsVertex, f.subf(), nesting) );
			break;
			
		case Formula.nu:
			returnVertex.setPlayer0(false);
			if (!fpOperators.containsKey(f.ident()))
				fpOperators.put(f.ident(), f);
			
			returnVertex.setPriority((nesting % 2 == 0)?(nesting):(++nesting));
			
			arena.addEdge( returnVertex, buildArena(tsVertex, f.subf(), nesting) );
			break;

//		case Formula.sub:
			
		default:
			throw new UserException( "ParseError", "The subformula: " + f + " has a wrong syntax.");
		}		
		
		return returnVertex;
	}

}
