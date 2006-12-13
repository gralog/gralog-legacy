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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;

import de.hu.dagwidth.alg.CopsAndRobberAlgorithm.CopsAndRobberVertex;
import de.hu.dagwidth.alg.CopsAndRobberAlgorithm.DummyVertexFilter;
import de.hu.dagwidth.alg.DAGConstruction.DAGVertex;
import de.hu.dagwidth.alg.DAGConstruction.VertexVertexFilter;
import de.hu.games.app.UserException;
import de.hu.games.graph.DirectedGraph;
import de.hu.games.graph.LabeledGraphVertex;
import de.hu.games.graph.alg.Algorithm;
import de.hu.games.graph.alg.AlgorithmResult;
import de.hu.games.graph.alg.AlgorithmResultContent;
import de.hu.games.graph.alg.Algorithms;
import de.hu.games.graph.alg.InvalidPropertyValuesException;
import de.hu.games.jgrapht.graph.GraphUtils;
import de.hu.games.jgrapht.graph.DisplaySubgraph.DisplayMode;
import de.hu.games.jgrapht.graph.DisplaySubgraph.DisplaySubgraphMode;
import de.hu.graphgames.alg.Simple2PlayerGameAlgorithm;

public class ComputeCopsStrategyAlgorithm<V extends LabeledGraphVertex,E extends DefaultEdge> extends Algorithm {

	private DirectedGraph<V,E> graph;
	private int dagWidth = 0;
	private boolean robberMonotone = false;
	private boolean copMonotone = false;
	public static final String DSM_ALLOWED_ROBBER_POSITIONS = "allowed robber positions";
	public static final String DSM_COP_POSITION = "cop position";

	public boolean isCopMonotone() {
		return copMonotone;
	}

	public void setCopMonotone(boolean copMonotone) {
		this.copMonotone = copMonotone;
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

	public boolean isRobberMonotone() {
		return robberMonotone;
	}

	public void setRobberMonotone(boolean robberMonotone) {
		this.robberMonotone = robberMonotone;
	}

	@Override
	public AlgorithmResult execute() throws InvalidPropertyValuesException,
			UserException {
		
		int currWidth = getDagWidth();
		if ( currWidth == 0 )
			currWidth++;
		
		AlgorithmResult result = new AlgorithmResult( );
		
		DisplaySubgraphMode mode = new DisplaySubgraphMode();
		mode.setVertexDisplayMode( DisplayMode.HIGH1, DisplayMode.SHOW );
		result.addDisplaySubgraphMode( DSM_ALLOWED_ROBBER_POSITIONS, mode );
		
		mode = new DisplaySubgraphMode();
		mode.setVertexDisplayMode( DisplayMode.HIGH2, DisplayMode.SHOW );
		result.addDisplaySubgraphMode( DSM_COP_POSITION, mode );
		
		AlgorithmResultContent content = null;
		do {
			Iterator<DirectedGraph<CopsAndRobberVertex<V>, DefaultEdge>> it = CopsAndRobberAlgorithm.getCopsAndRobberGameGraphs( graph, currWidth++, true, true );
			while ( it.hasNext() ) {
				DirectedGraph<DAGVertex, DefaultEdge> outGraph = getDAG( it.next() );
			
				if ( outGraph != null ) {
					content = new CopStrategyAlgorithmResultContent<V,E>( getGraph(), new CopStrategy<V,E>( outGraph ) );
					break;
				}
			}	
		} while ( content == null && getDagWidth() == 0 );
		
		result.setSingleContent( content );
		return result;
	}
	
	

	
	public static class CopStrategy<VI,EI> {
		public DirectedGraph<DAGVertex, DefaultEdge> graph;
		private ArrayList<DAGVertex> roots = new ArrayList<DAGVertex>();
		private Hashtable<DAGVertex, Set<VI>> reachCache = new Hashtable<DAGVertex, Set<VI>>();
		
		public CopStrategy( DirectedGraph<DAGVertex, DefaultEdge> graph ) {
			this.graph = graph;
			buildRoots();
		}
		
		private Set<VI> getReach( DAGVertex from ) {
			
			Set<VI> reach = reachCache.get( from );
			if ( reach == null ) {
				reach = new HashSet<VI>();
				Iterator<DAGVertex> dagReach = Algorithms.reach( graph, from, false, null ).iterator();
				while ( dagReach.hasNext() )
					reach.addAll( dagReach.next().X );
				reachCache.put( from, reach );
			}
			
			return reach;
		}
		
		private void buildRoots() {
			for ( DAGVertex v : graph.vertexSet() ) {
				if ( graph.inDegreeOf( v ) == 0 )
					roots.add( v );
			}
		}
		
		public Set<VI> getMove( VI robber, Set<VI> cops ) {
			DAGVertex currVertex = null;
			
			for ( DAGVertex root : roots ) {
				if ( getReach( root ).contains( robber ) ) {
					currVertex = root;
					break;
				}
			}
			
			if ( cops.size() == 0 )
				return currVertex.X;
			
			while ( (! currVertex.X.equals( cops )) ) {
				for ( DefaultEdge edge : graph.outgoingEdgesOf( currVertex ) ) {
					DAGVertex succ = graph.getEdgeTarget( edge );
					
					if ( getReach( succ ).contains( robber ) ) {
						currVertex = succ;
						break;
					}
				}
			}
			
			for ( DefaultEdge edge : graph.outgoingEdgesOf( currVertex ) ) {
				DAGVertex succ = graph.getEdgeTarget( edge );
				
				if ( getReach( succ ).contains( robber ) )
					return succ.X;
			}
			return null;
		}
	}
	
	public static class CopStrategyAlgorithmResultContentPersistenceDelegate extends DefaultPersistenceDelegate {

		@Override
		protected Expression instantiate(Object oldInstance, Encoder out) {
			CopStrategyAlgorithmResultContent node = (CopStrategyAlgorithmResultContent)oldInstance;
			return new Expression( oldInstance, oldInstance.getClass(), "new", new Object[] { node.graph,  node.strategy } );
		}
		
	}
	
	public static class CopStrategyPersistenceDelegate extends DefaultPersistenceDelegate {

		@Override
		protected Expression instantiate(Object oldInstance, Encoder out) {
			CopStrategy strategy = (CopStrategy)oldInstance;
			return new Expression( oldInstance, oldInstance.getClass(), "new", new Object[] { strategy.graph } );
		}
		
	}

	
	public <VI extends CopsAndRobberVertex<V>, EI extends DefaultEdge> DirectedGraph<DAGVertex, DefaultEdge> getDAG( DirectedGraph<VI, EI> gameGraph ) {
		Set<VI> winningPlayer1 = new HashSet<VI>( new Simple2PlayerGameAlgorithm<VI,EI>( gameGraph ).execute() );
		
		Set<VI> winningStartVertexes = new HashSet<VI>( GraphUtils.filterVertexes( gameGraph.vertexSet(), new DummyVertexFilter<VI>() ));
		winningStartVertexes.removeAll( winningPlayer1 );
		
		if ( winningStartVertexes.size()  == 0 )
			return null;
		
		VI start = winningStartVertexes.iterator().next();
		
		DirectedGraph<DAGVertex, DefaultEdge> outGraph = (DirectedGraph<DAGVertex, DefaultEdge>)new DAGVertexGraphTypeInfo().newInstance();
		
		LinkedList<VI> queue = new LinkedList<VI>();
		queue.add( start );
		
		Hashtable<VI, Set<V>> reachableHash = new Hashtable<VI, Set<V>>();
		reachableHash.put( start, graph.vertexSet() );
		
		outGraph.addVertex( new DAGVertex( start ) );
		
		while ( queue.size() != 0 ) {
			VI v = queue.removeFirst();
			DAGVertex vd = new DAGVertex( v );
			
			Set<V> reachable = reachableHash.get( v );
			
			Hashtable<V, Set<V>> division;
			Set<V> seperator = new HashSet<V>( v.getX2() );

			division = getMinDivision( reachable, seperator, v.getRobber() );
			
			Iterator<EI> edges = gameGraph.outgoingEdgesOf( v ).iterator();
			while ( edges.hasNext() ) {
				VI v1 = gameGraph.getEdgeTarget( edges.next() );
				
				if ( division.containsKey( v1.getRobber() ) ) {
					Iterator<EI> edgesv1 = gameGraph.outgoingEdgesOf( v1 ).iterator();
					VI v2 = null;
					while ( edgesv1.hasNext() ) {
						VI v2h = gameGraph.getEdgeTarget( edgesv1.next() );
						if ( ! winningPlayer1.contains( v2h ) ) {
							v2 = v2h;
							if ( v2.getAdded() == v2.getRobber() )
								break;
						}
					}
					DAGVertex v2d = new DAGVertex( v2 );
					if ( ! outGraph.containsVertex( v2d ) )
						outGraph.addVertex( v2d );
					outGraph.addEdge( vd, v2d );
					queue.addLast( v2 );
					reachable = division.get( v2.getRobber() );
					reachable.remove( v2.getAdded() );
					reachableHash.put( v2, reachable );
				}
			}
		}
		
		return outGraph;
	}
	
	private Hashtable<V, Set<V>> getMinDivision( Set<V> vertexes, Set<V> seperator, V previous ) {
		Hashtable<V, Set<V>> division = new Hashtable<V, Set<V>>();
		Set<V> notReachable = new HashSet<V>( vertexes );
		notReachable.removeAll( seperator );
		while ( notReachable.size() != 0 ) {
			V v = notReachable.iterator().next();
			Set<V> reachableV;
			Set<V> reachV;
			Set<V> reachVCopy;
			do {
			    reachableV = Algorithms.reach( graph, v, false, new VertexVertexFilter<V>( seperator ) );
				reachV = Algorithms.reach( graph, v, true, new VertexVertexFilter<V>( seperator ) );
				reachVCopy = new HashSet<V>( reachV );
				reachV.removeAll( reachableV );
				reachV.retainAll( vertexes );
				if ( ! reachV.isEmpty() )
					v = reachV.iterator().next();
			} while (  !reachV.isEmpty() );
			if ( previous != null ) {
				reachVCopy.retainAll( reachableV );
				reachVCopy.retainAll( vertexes );
				Iterator<E> edges = graph.outgoingEdgesOf( previous ).iterator();
				while ( edges.hasNext() ) {
					V o = graph.getEdgeTarget( edges.next() );
					if ( reachVCopy.contains( o ) ) {
						v = o;
						break;
					}
				}
			}
			division.put( v , reachableV );
			notReachable.removeAll( reachableV );
		}
		return division;
	}
}
