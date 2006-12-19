/*
 * Created on 26 Oct 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.graph.alg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JScrollPane;
import javax.swing.undo.UndoManager;

import org.jgrapht.Graph;

import de.hu.gralog.graph.GraphWithEditableElements;
import de.hu.gralog.jgraph.GJGraph;
import de.hu.gralog.jgrapht.graph.DisplaySubgraph;
import de.hu.gralog.jgrapht.graph.ElementTips;
import de.hu.gralog.jgrapht.graph.DisplaySubgraph.DisplaySubgraphMode;
import de.hu.gralog.jgrapht.graph.ElementTips.ElementTipsDisplayMode;


public class AlgorithmResultInfo implements AlgorithmResultListener {
	private String algorithmName = null;
	private Hashtable<String, Object> algorithmSettings = new Hashtable<String,Object>();
	private AlgorithmResult result = null;
	private Hashtable<Graph, GJGraph> jgraphs = null;
	private UndoManager undoManager = null;
	private AlgorithmResultContent currentContent = null;
	private transient ArrayList<AlgorithmInfoListener> listeners = new ArrayList<AlgorithmInfoListener>();
	
	public AlgorithmResultInfo( String algorithmName, Hashtable<String, Object> algorithmSettings, AlgorithmResult result, Hashtable<Graph, GJGraph>  jgraphs ) {
		this.algorithmName = algorithmName;
		this.algorithmSettings = algorithmSettings;
		this.result = result;
		result.addListener( this );
		this.jgraphs = jgraphs;
		setCurrentContent( result.getFirstContent() );
		init();
	}
	
	public Hashtable<Graph, GJGraph> getGJGraphs() {
		return jgraphs;
	}
	
	public AlgorithmResult getAlgorithmResult() {
		return result;
	}
	
	public void setCurrentContent( AlgorithmResultContent content ) {
		AlgorithmResultContent oldContent = this.currentContent;
		this.currentContent = content;
		
		Graph oldGraph = result.getGraph( oldContent );
		Graph newGraph = result.getGraph( this.currentContent );
		
		if ( oldContent != null ) {
			if ( oldContent.getDisplaySubgraphs( result.getDisplaySubgraphModes() ) != null )
				getGraph( oldGraph ).deregisterAllSubgraphs();
			if ( oldContent.getElementTips( result.getElementTipsDisplayModes() ) != null )
				getGraph( oldGraph ).deregisterAllElementTips();
		}
		// show new Content
		if ( this.currentContent.getDisplaySubgraphs( result.getDisplaySubgraphModes() ) != null ) {
			for ( DisplaySubgraph subgraph : this.currentContent.getDisplaySubgraphs( result.getDisplaySubgraphModes() ).values() )
				getGraph( newGraph ).registerSubgraph( subgraph );
		}
		if ( this.currentContent.getElementTips( result.getElementTipsDisplayModes() ) != null ) {
			for ( ElementTips tips : this.currentContent.getElementTips( result.getElementTipsDisplayModes() ).values() )
				getGraph( newGraph ).registerElementTips( tips );
		}
		
		if ( oldGraph != newGraph )
			fireGraphReplaced();
	}
	
	protected void fireGraphReplaced() {
		for ( AlgorithmInfoListener listener : listeners )
			listener.graphReplaced();
	}
		
	protected void init() {
		for( GJGraph graph : jgraphs.values() )
			graph.setElementsAndStructureEditable( false );
	}
	
	protected GJGraph getGraph( Graph graphT ) {
		GJGraph graph = jgraphs.get( graphT );
		if ( graph == null ) {
			graph = new GJGraph( (GraphWithEditableElements)graphT );
			graph.getModel().addUndoableEditListener( undoManager );
			jgraphs.put( graphT, graph );
		}
		if ( graph.getParent() == null )
			new JScrollPane( graph );
		return graph;
	}
	
	public GJGraph getGraph() {
		return getGraph( result.getGraph( currentContent ) );
	}
	
	public Graph getAlgorithmResultGraph() {
		return result.getGraph( null );
	}
	
	public Set<Graph> getAllGraphs() {
		Set<Graph> graphs = new HashSet();
		if ( result.getGraph() != null )
			graphs.add( result.getGraph() );
		for ( AlgorithmResultContent content : getAllContents() ) {
			if ( content.getGraph() != null )
				graphs.add( content.getGraph() );
		}
		return graphs;
	}
	
	protected Set<AlgorithmResultContent> getAllContents() {
		Set<AlgorithmResultContent> contents = new HashSet<AlgorithmResultContent>();
		if ( getSingleContent() != null )
			contents.add( getSingleContent() );
		if ( getContentList() != null )
			contents.addAll( getContentList() );
		if ( getContentTree() != null ) {
			ContentTreeIterator iterator = new ContentTreeIterator( getContentTree() );
			while ( iterator.hasNext() )
				contents.add( iterator.next() );
		}
		return contents;
	}
	
	public AlgorithmResultContent getSingleContent() {
		return result.getSingleContent();
	}
	
	public ArrayList<AlgorithmResultContent> getContentList() {
		return result.getContentList();
	}
	
	public AlgorithmResultContentTreeNode getContentTree() {
		return result.getContentTree();
	}
	
	public String getAlgorithmName() {
		return algorithmName;
	}
	
	public Hashtable<String,Object> getAlgorithmSettings() {
		return algorithmSettings;
	}
	
	public String getDescription() {
		return result.getDescription();
	}

	public Hashtable<String, ElementTips> getElementTips( AlgorithmResultContent content ) {
		return result.getElementTips( content );
	}
	
	public Hashtable<String, DisplaySubgraph> getDisplaySubgraphs(AlgorithmResultContent content ) {
		return result.getDisplaySubgraphs( content );
	}
	
	public Hashtable<String, ElementTipsDisplayMode> getElementTipsDisplayModes() {
		return result.getElementTipsDisplayModes();
	}
	
	public Hashtable<String, DisplaySubgraphMode> getDisplaySubgraphModes() {
		return result.getDisplaySubgraphModes();
	}
	
	public void registerUndoManager(UndoManager undoManager) {
		this.undoManager = undoManager;
		result.addUndoableEditListener( undoManager );
		
		for ( GJGraph graph : jgraphs.values() )
			graph.getModel().addUndoableEditListener( undoManager );
	}
	
	public void addListener( AlgorithmInfoListener listener ) {
		if ( ! listeners.contains( listener ) )
			listeners.add( listener );
	}
	
	public void removeListener( AlgorithmInfoListener listener ) {
		listeners.remove( listener );
	}
	
	public void currentContentChanged( ) {
		setCurrentContent( currentContent );
	}
	
	private static class ContentTreeIterator implements Iterator<AlgorithmResultContentTreeNode> {

		ArrayList<AlgorithmResultContentTreeNode> queue = new ArrayList<AlgorithmResultContentTreeNode>();
		
		public ContentTreeIterator( AlgorithmResultContentTreeNode root ) {
			queue.add( root );
		}
		
		public boolean hasNext() {
			return queue.size() != 0;
		}

		public AlgorithmResultContentTreeNode next() {
			AlgorithmResultContentTreeNode next = queue.remove( 0 );
			queue.addAll( next.getChildren() );
			return next;
		}

		public void remove() {
		}
		
	}
}
