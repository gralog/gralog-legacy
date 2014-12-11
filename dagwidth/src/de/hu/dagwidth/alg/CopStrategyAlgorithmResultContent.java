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
import org.jgrapht.graph.ListenableDirectedGraph;

import de.hu.dagwidth.alg.ComputeCopsStrategyAlgorithm.CopStrategyAlgorithmResultContentPersistenceDelegate;
import de.hu.dagwidth.alg.ComputeCopsStrategyAlgorithm.CopStrategyPersistenceDelegate;
import de.hu.dagwidth.alg.DAGConstruction.VertexVertexFilter;
import de.hu.gralog.algorithm.result.AlgorithmResultInteractiveContent;
import de.hu.gralog.algorithm.result.DisplaySubgraph;
import de.hu.gralog.algorithm.result.DisplaySubgraphMode;
import de.hu.gralog.algorithms.jgrapht.Algorithms;
import de.hu.gralog.app.UserException;
import de.hu.gralog.structure.*;
import de.hu.gralog.structure.event.*;
import de.hu.gralog.structure.types.elements.*;

public class CopStrategyAlgorithmResultContent<VI extends DefaultListenableVertex,EI extends DefaultEdge, G extends ListenableDirectedGraph<VI,EI>> extends AlgorithmResultInteractiveContent implements SelectionListener<VI,EI> {
	
	ComputeCopsStrategyAlgorithm.CopStrategy<VI,EI> strategy = null;
	private VI robber = null;
	private Set<VI> cops = new HashSet<VI>();
	private Set<VI> prevCops = new HashSet<VI>();
	Structure<VI,EI,?,G> graph = null;
	ArrayList<GamePosition> positions = new ArrayList<GamePosition>();
	
	static {
		try {
			Introspector.getBeanInfo( CopStrategyAlgorithmResultContent.class ).getBeanDescriptor().setValue( "persistenceDelegate", new CopStrategyAlgorithmResultContentPersistenceDelegate() );
			Introspector.getBeanInfo( ComputeCopsStrategyAlgorithm.CopStrategy.class ).getBeanDescriptor().setValue( "persistenceDelegate", new CopStrategyPersistenceDelegate() );
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}
	
	public CopStrategyAlgorithmResultContent( Structure<VI,EI,?,G> graph, ComputeCopsStrategyAlgorithm.CopStrategy<VI,EI> strategy ) {
		super( graph );
		this.graph = graph;
		this.strategy = strategy;
		graph.getStructureSelectionSupport().addSelectionListener(this);
	}

	protected void computeSubgraphs() {
		subgraphs = new Hashtable<String, SubgraphInfo>();
							
		SubgraphInfo subgraphInfo = new SubgraphInfo( new HashSet( cops ), new HashSet() );
		subgraphs.put( ComputeCopsStrategyAlgorithm.DSM_COP_POSITION, subgraphInfo );
		
		Set<VI> copsIntersection = new HashSet<VI>( prevCops );
		copsIntersection.retainAll( cops );
		
		Set<VI> robberSpace = Algorithms.reach( graph.getGraph(), robber, false, new VertexVertexFilter<VI>( copsIntersection ) );
		subgraphInfo = new SubgraphInfo( new HashSet( robberSpace ), new HashSet() );
		subgraphs.put( ComputeCopsStrategyAlgorithm.DSM_ALLOWED_ROBBER_POSITIONS, subgraphInfo );
		displaySubgraphCache = null;
	}
	
	
	@Override
	protected Hashtable<String, DisplaySubgraph> getDisplaySubgraphs(Hashtable<String, DisplaySubgraphMode> modes, Structure graphSupport ) throws UserException {
		if ( subgraphs == null )
			computeSubgraphs();
		return super.getDisplaySubgraphs(modes, graphSupport );
	}

	public void valueChanged(Set<VI> vertexes, Set<EI> edges, GraphSelectionEvent event ) {
		if ( vertexes.size() == 1 ) {
			prevCops = cops;
			cops = strategy.getMove( vertexes.iterator().next(  ), cops );
			robber = vertexes.iterator().next();
			subgraphs = null;
			try {
				fireContentChanged();
			} catch( UserException e ) {
				// can not happen
			}
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
			try {
				fireContentChanged();
			} catch (UserException e) {
				// can not happen
			}
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
		
		try {
			fireContentChanged();
		} catch (UserException e) {
			// cannot happen
		}
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