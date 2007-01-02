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

import de.hu.dagwidth.alg.ComputeHavenAlgorithm.HavenAlgorithmResultContentPersistenceDelegate;
import de.hu.gralog.graph.DirectedGraph;
import de.hu.gralog.graph.SelectionListener;
import de.hu.gralog.graph.alg.AlgorithmResultInteractiveContent;
import de.hu.gralog.jgrapht.graph.DisplaySubgraph;
import de.hu.gralog.jgrapht.graph.SubgraphFactory;
import de.hu.gralog.jgrapht.graph.DisplaySubgraph.DisplaySubgraphMode;
import de.hu.gralog.jgrapht.vertex.DefaultListenableVertex;

public class HavenAlgorithmResultContent<VI extends DefaultListenableVertex,EI extends DefaultEdge> extends AlgorithmResultInteractiveContent implements SelectionListener<VI,EI> {
	
	Hashtable<Set<VI>,Set<VI>> haven;
	DirectedGraph<VI,EI> graph = null;
	Set<VI> copPosition = new HashSet<VI>();
	Set<VI> currHaven = new HashSet<VI>();
	
	static {
		try {
			Introspector.getBeanInfo( HavenAlgorithmResultContent.class ).getBeanDescriptor().setValue( "persistenceDelegate", new HavenAlgorithmResultContentPersistenceDelegate() );
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}
	
	public HavenAlgorithmResultContent( DirectedGraph<VI,EI> graph, Hashtable<Set<VI>,Set<VI>> haven ) {
		super( graph );
		this.graph = graph;
		this.haven = haven;
		graph.addSelectionListener( this );
	}

	protected void computeSubgraphs() {
		subgraphs = new Hashtable<String, Subgraph>();
							
		Subgraph subgraph = SubgraphFactory.createSubgraph( graph, copPosition, new HashSet() );
		subgraphs.put( ComputeHavenAlgorithm.DSM_COP_POSITION, subgraph );
		
		subgraph = SubgraphFactory.createSubgraph( graph, currHaven, new HashSet() );
		subgraphs.put( ComputeHavenAlgorithm.DSM_HAVEN, subgraph );
		displaySubgraphCache = null;
	}
	
	
	@Override
	protected Hashtable<String, DisplaySubgraph> getDisplaySubgraphs(Hashtable<String, DisplaySubgraphMode> modes) {
		if ( subgraphs == null )
			computeSubgraphs();
		return super.getDisplaySubgraphs(modes);
	}

	public void valueChanged(ArrayList<VI> vertexes, ArrayList<EI> edges, GraphSelectionEvent event ) {
				
		if ( vertexes == null || vertexes.size() == 0 )
			copPosition.clear();
		else
			copPosition.addAll( vertexes );
				
		currHaven = haven.get( copPosition );
		if ( currHaven == null )
			currHaven = new HashSet<VI>();
		subgraphs = null;
		fireContentChanged();
	}
	
	
	
}