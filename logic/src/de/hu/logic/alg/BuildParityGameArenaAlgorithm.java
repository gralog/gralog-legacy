package de.hu.logic.alg;

import java.util.HashMap;

import org.jgrapht.graph.DefaultEdge;

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
	
	private DirectedGraph<ParityGameVertex, DefaultEdge> arena = new DirectedGraph<ParityGameVertex, DefaultEdge>(ParityGameVertex.class);
	
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
	
	public DirectedGraph<ParityGameVertex, DefaultEdge> execute() {
		for ( TransitionSystemVertex tsv : transitionSystem.vertexSet()) {
			tsVertices.put(tsv.getLabel(), tsv);
		}

		for ( TransitionSystemVertex tsv : transitionSystem.vertexSet()) {
			buildArena(tsv.getLabel(), formula);
		}
		
		return arena;
		
	}
	
	public ParityGameVertex buildArena(String tsVertex, Formula f) {
		
		String vertexLabel = buildVertexLabel(tsVertex, f);
		
		if (arenaVertices.containsKey(vertexLabel))
			return arenaVertices.get(vertexLabel);
		
		
		ParityGameVertex returnVertex = new ParityGameVertex();
		returnVertex.setLabel(vertexLabel);

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
				arena.addEdge( returnVertex, buildArena(tsVertex, fpOperators.get(f.ident())) );			
			break;

		case Formula.and:
			returnVertex.setPlayer0(false);
			
			arena.addEdge( returnVertex, buildArena(tsVertex, f.leftSubf()) );
			arena.addEdge( returnVertex, buildArena(tsVertex, f.rightSubf()) );
			break;
			
		case Formula.or:
			returnVertex.setPlayer0(true);
			
			arena.addEdge( returnVertex, buildArena(tsVertex, f.leftSubf()) );
			arena.addEdge( returnVertex, buildArena(tsVertex, f.rightSubf()) );
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
				arena.addEdge( returnVertex, buildArena(transitionSystem.getEdgeTarget(e).getLabel(), f.subf()) );				
			}
			break;
			
		case Formula.box:
			returnVertex.setPlayer0(false);	
			
			for (TransitionSystemEdge e : transitionSystem.outgoingEdgesOf(tsVertices.get(tsVertex))) {
				arena.addEdge( returnVertex, buildArena(transitionSystem.getEdgeTarget(e).getLabel(), f.subf()) );				
			}
			break;
			
		case Formula.mu:
		case Formula.nu:
			returnVertex.setPlayer0(false);
			if (!fpOperators.containsKey(f.ident()))
				fpOperators.put(f.ident(), f);
			
			arena.addEdge( returnVertex, buildArena(tsVertex, f.subf()) );
			break;

//		case Formula.sub:
			
		default:
			return null;
		}		
		
		return returnVertex;
	}

}
