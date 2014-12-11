/*
 * Created on 10 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.dagwidth.alg;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.jgraph.event.GraphSelectionEvent;
import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.DefaultEdge;

import de.hu.dagwidth.alg.ComputeHavenAlgorithm.HavenAlgorithmResultContentPersistenceDelegate;
import de.hu.gralog.algorithm.result.AlgorithmResultInteractiveContent;
import de.hu.gralog.algorithm.result.DisplaySubgraph;
import de.hu.gralog.algorithm.result.DisplaySubgraphMode;
import de.hu.gralog.app.UserException;
import de.hu.gralog.structure.*;
import de.hu.gralog.structure.event.SelectionListener;
import de.hu.gralog.structure.types.elements.DefaultListenableVertex;

public class HavenAlgorithmResultContent<VI extends DefaultListenableVertex,EI extends DefaultEdge, GI extends ListenableGraph<VI,EI>> extends AlgorithmResultInteractiveContent implements SelectionListener<VI,EI> {
	
	Hashtable<Set<VI>,Set<VI>> haven;
	Structure<VI,EI,?,GI> graph = null;
	Set<VI> copPosition = new HashSet<VI>();
	Set<VI> currHaven = new HashSet<VI>();
	
	static {
		try {
			Introspector.getBeanInfo( HavenAlgorithmResultContent.class ).getBeanDescriptor().setValue( "persistenceDelegate", new HavenAlgorithmResultContentPersistenceDelegate() );
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}
	
	public HavenAlgorithmResultContent( Structure<VI,EI,?,GI> graph, Hashtable<Set<VI>,Set<VI>> haven ) {
		super( graph );
		this.graph = graph;
		this.haven = haven;
		graph.getStructureSelectionSupport().addSelectionListener( this );
	}

	protected void computeSubgraphs() {
		subgraphs = new Hashtable<String, SubgraphInfo>();
							
		SubgraphInfo subgraphInfo =  new SubgraphInfo( copPosition, new HashSet() );
		subgraphs.put( ComputeHavenAlgorithm.DSM_COP_POSITION, subgraphInfo );
		
		subgraphInfo = new SubgraphInfo( currHaven, new HashSet() );
		subgraphs.put( ComputeHavenAlgorithm.DSM_HAVEN, subgraphInfo );
		displaySubgraphCache = null;
	}
	
	
	@Override
	protected Hashtable<String, DisplaySubgraph> getDisplaySubgraphs(Hashtable<String, DisplaySubgraphMode> modes, Structure graphSupport ) throws UserException {
		if ( subgraphs == null )
			computeSubgraphs();
		return super.getDisplaySubgraphs( modes, graphSupport );
	}

	public void valueChanged(Set<VI> vertexes, Set<EI> edges, GraphSelectionEvent event ) {
				
		if ( vertexes == null || vertexes.size() == 0 )
			copPosition.clear();
		else
			copPosition.addAll( vertexes );
				
		currHaven = haven.get( copPosition );
		if ( currHaven == null )
			currHaven = new HashSet<VI>();
		subgraphs = null;
		try {
			fireContentChanged();
		} catch (UserException e) {
			// cannot happen
		}
	}
	
	
	
}