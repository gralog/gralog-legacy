/*
 * Created on 10 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.dagwidth.alg;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.graph.GraphSelectionModel;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Subgraph;

import de.hu.dagwidth.alg.ComputeCopsStrategyAlgorithm.CopStrategyAlgorithmResultContentPersistenceDelegate;
import de.hu.dagwidth.alg.ComputeCopsStrategyAlgorithm.CopStrategyPersistenceDelegate;
import de.hu.dagwidth.alg.DAGConstruction.VertexVertexFilter;
import de.hu.games.graph.DirectedGraph;
import de.hu.games.graph.SelectionListener;
import de.hu.games.graph.alg.AlgorithmResultInteractiveContent;
import de.hu.games.graph.alg.Algorithms;
import de.hu.games.jgrapht.graph.DisplaySubgraph;
import de.hu.games.jgrapht.graph.SubgraphFactory;
import de.hu.games.jgrapht.graph.DisplaySubgraph.DisplaySubgraphMode;
import de.hu.games.jgrapht.vertex.DefaultListenableVertex;

public class CopStrategyAlgorithmResultContent<VI extends DefaultListenableVertex,EI extends DefaultEdge> extends AlgorithmResultInteractiveContent implements SelectionListener<VI,EI> {
	
	ComputeCopsStrategyAlgorithm.CopStrategy<VI,EI> strategy = null;
	private VI robber = null;
	private Set<VI> cops = new HashSet<VI>();
	private Set<VI> prevCops = new HashSet<VI>();
	DirectedGraph<VI,EI> graph = null;
	ArrayList<GamePosition> positions = new ArrayList<GamePosition>();
	
	static {
		try {
			Introspector.getBeanInfo( CopStrategyAlgorithmResultContent.class ).getBeanDescriptor().setValue( "persistenceDelegate", new CopStrategyAlgorithmResultContentPersistenceDelegate() );
			Introspector.getBeanInfo( ComputeCopsStrategyAlgorithm.CopStrategy.class ).getBeanDescriptor().setValue( "persistenceDelegate", new CopStrategyPersistenceDelegate() );
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}
	
	public CopStrategyAlgorithmResultContent( DirectedGraph<VI,EI> graph, ComputeCopsStrategyAlgorithm.CopStrategy<VI,EI> strategy ) {
		super( graph );
		this.graph = graph;
		this.strategy = strategy;
		graph.addSelectionListener( this );
	}

	protected void computeSubgraphs() {
		subgraphs = new Hashtable<String, Subgraph>();
							
		Subgraph subgraph = SubgraphFactory.createSubgraph( graph, new HashSet( cops ), new HashSet() );
		subgraphs.put( ComputeCopsStrategyAlgorithm.DSM_COP_POSITION, subgraph );
		
		Set<VI> copsIntersection = new HashSet<VI>( prevCops );
		copsIntersection.retainAll( cops );
		
		Set<VI> robberSpace = Algorithms.reach( graph, robber, false, new VertexVertexFilter<VI>( copsIntersection ) );
		subgraph = SubgraphFactory.createSubgraph( graph, new HashSet( robberSpace ), new HashSet() );
		subgraphs.put( ComputeCopsStrategyAlgorithm.DSM_ALLOWED_ROBBER_POSITIONS, subgraph );
		displaySubgraphCache = null;
	}
	
	
	@Override
	protected Hashtable<String, DisplaySubgraph> getDisplaySubgraphs(Hashtable<String, DisplaySubgraphMode> modes) {
		if ( subgraphs == null )
			computeSubgraphs();
		return super.getDisplaySubgraphs(modes);
	}

	public void valueChanged(ArrayList<VI> vertexes, ArrayList<EI> edges, GraphSelectionEvent event ) {
		if ( vertexes.size() == 1 ) {
			prevCops = cops;
			cops = strategy.getMove( vertexes.get( 0 ), cops );
			robber = vertexes.get( 0 );
			subgraphs = null;
			fireContentChanged();
			savePosition();
		}
		((GraphSelectionModel)event.getSource()).clearSelection();
	}
	
	private void savePosition() {
		positions.add( new GamePosition( cops, robber ) );
	}
	
	public void prevPosition( ) {
		if ( positions.size() > 0 ) {
			positions.remove( positions.size() - 1 );
			if ( positions.size() > 1 ) 
				prevCops = positions.get( positions.size() - 2 ).cops;
			else
				prevCops = new HashSet<VI>();
			
			if ( positions.size() > 0 ) {
				cops = positions.get( positions.size() - 1 ).cops;
				robber = positions.get( positions.size() - 1 ).robber;
			} else {
				cops = new HashSet<VI>();
				robber = null;
			}
			subgraphs = null;
			fireContentChanged();
		}
	}
	
	public boolean hasPreviosPosition() {
		return positions.size() > 0;
	}
	
	public void newGame() {
		prevCops = new HashSet<VI>();
		cops = new HashSet<VI>();
		robber = null;
		subgraphs = null;
		positions.clear();
		fireContentChanged();
	}
	
	class GamePosition {
		Set<VI> cops;
		VI robber;
		
		GamePosition( Set<VI> cops, VI robber ) {
			this.cops = cops;
			this.robber = robber;
		}
	}
	
}