/*
 * Created on 9 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.dagwidth.alg;


import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;

import de.hu.dagwidth.alg.CopsAndRobberAlgorithm.CopsAndRobberVertex;
import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.DirectedGraph;
import de.hu.gralog.graph.LabeledGraphVertex;
import de.hu.gralog.graph.alg.Algorithm;
import de.hu.gralog.graph.alg.AlgorithmResult;
import de.hu.gralog.graph.alg.AlgorithmResultContent;
import de.hu.gralog.graph.alg.InvalidPropertyValuesException;
import de.hu.gralog.jgrapht.graph.DisplaySubgraphMode;
import de.hu.gralog.jgrapht.graph.DisplaySubgraph.DisplayMode;
import de.hu.graphgames.alg.Simple2PlayerGameAlgorithm;

public class ComputeHavenAlgorithm<V extends LabeledGraphVertex,E extends DefaultEdge> implements Algorithm {

	private DirectedGraph<V,E> graph;
	private int dagWidth = 1;
	private boolean robberMonotone = false;
	private boolean copMonotone = false;
	
	public static final String DSM_COP_POSITION = "cop position";
	public static final String DSM_HAVEN = "haven";
	
	public boolean isCopMonotone() {
		return copMonotone;
	}

	public void setCopMonotone(boolean copMonotone) {
		this.copMonotone = copMonotone;
	}
	
	public boolean isRobberMonotone() {
		return robberMonotone;
	}

	public void setRobberMonotone(boolean robberMonotone) {
		this.robberMonotone = robberMonotone;
	}
	
	public int getDagWidth() {
		return dagWidth;
	}

	public void setDagWidth(int dagWidth) {
		this.dagWidth = dagWidth;
	}

	public DirectedGraph<V,E> getGraph() {
		return graph;
	}

	public void setGraph(
			DirectedGraph<V,E> graph) {
		this.graph = graph;
	}

	public AlgorithmResult execute() throws InvalidPropertyValuesException,
			UserException {
		InvalidPropertyValuesException error = new InvalidPropertyValuesException();
		if ( getGraph() == null )
			error.addPropertyError( "graph", InvalidPropertyValuesException.PROPERTY_REQUIRED );
		if ( getDagWidth() <= 0 )
			error.addPropertyError( "dagwidth", InvalidPropertyValuesException.GREATER_ZERO );
		if ( error.hasErrors() )
			throw error;
		
		AlgorithmResult result = new AlgorithmResult( );
		
		DisplaySubgraphMode mode = new DisplaySubgraphMode();
		mode.setVertexDisplayMode( DisplayMode.HIGH1, DisplayMode.SHOW );
		result.addDisplaySubgraphMode( DSM_HAVEN, mode );
		
		mode = new DisplaySubgraphMode();
		mode.setVertexDisplayMode( DisplayMode.HIGH2, DisplayMode.SHOW );
		result.addDisplaySubgraphMode( DSM_COP_POSITION, mode );
		
		DirectedGraph<CopsAndRobberVertex<V>, DefaultEdge> gameGraph = CopsAndRobberAlgorithm.getCopsAndRobberGameGraph( graph, getDagWidth(), isCopMonotone(), isRobberMonotone() );
		AlgorithmResultContent content = new HavenAlgorithmResultContent( graph, computeHaven( gameGraph ) );
		
		result.setSingleContent( content );
		return result;
	}
		
	private Hashtable<Set<V>, Set<V>> computeHaven( DirectedGraph<CopsAndRobberVertex<V>, DefaultEdge> gameGraph ) {
		ArrayList<CopsAndRobberVertex<V>> winningPlayer1 = new Simple2PlayerGameAlgorithm<CopsAndRobberVertex<V>, DefaultEdge>( gameGraph ).execute();
		
		Hashtable<Set<V>, Set<V>> haven = new Hashtable<Set<V>,Set<V>>();
		
		for ( CopsAndRobberVertex<V> vertex : winningPlayer1 ) {
			if ( vertex.isPlayer0() ) {
				Set<V> X = vertex.getX();
				Set<V> robber = haven.get( X );
				if ( robber == null ) {
					robber = new HashSet<V>();
					haven.put( X, robber );
				}
				robber.add( vertex.getRobber() );
			}
		}
		return haven;
		
	}
	
	public static class HavenAlgorithmResultContentPersistenceDelegate extends DefaultPersistenceDelegate {

		@Override
		protected Expression instantiate(Object oldInstance, Encoder out) {
			HavenAlgorithmResultContent node = (HavenAlgorithmResultContent)oldInstance;
			return new Expression( oldInstance, oldInstance.getClass(), "new", new Object[] { node.graph,  node.haven } );
		}
		
	}
	
	
}
