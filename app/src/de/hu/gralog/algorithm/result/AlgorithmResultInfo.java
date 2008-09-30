/*
 * Created on 26 Oct 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.algorithm.result;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.undo.UndoManager;

import de.hu.gralog.app.UserException;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.gui.components.HTMLEditorPane;
import de.hu.gralog.jgraph.GJGraph;
import de.hu.gralog.structure.Structure;


public class AlgorithmResultInfo implements AlgorithmResultListener {
	private String algorithmName = null;
	private Hashtable<String, Object> algorithmSettings = new Hashtable<String,Object>();
	private AlgorithmResult result = null;
	private Hashtable<Structure, GJGraph> jgraphs = null;
	private UndoManager undoManager = null;
	private AlgorithmResultContent currentContent = null;
	private transient ArrayList<AlgorithmInfoListener> listeners = new ArrayList<AlgorithmInfoListener>();
	
	public AlgorithmResultInfo( String algorithmName, Hashtable<String, Object> algorithmSettings, AlgorithmResult result, Hashtable<Structure, GJGraph>  jgraphs ) throws UserException {
		this.algorithmName = algorithmName;
		this.algorithmSettings = algorithmSettings;
		this.result = result;
		result.addListener( this );
		this.jgraphs = jgraphs;
		if ( result.hasContents() ) {
			setCurrentContent( result.getFirstContent() );
			init();
		}
	}
	
	public Hashtable<Structure, GJGraph> getGJGraphs() {
		return jgraphs;
	}
	
	public AlgorithmResult getAlgorithmResult() {
		return result;
	}
	
	public boolean hasContents() {
		return result.hasContents();
	}
	
	public Component getMessageComponent() {
		if ( result.hasContents() )
			return null;
		JEditorPane description = new HTMLEditorPane( result.getDescription() );
		return new JScrollPane( description );
	}
	
	public void setCurrentContent( AlgorithmResultContent content ) throws UserException{
		AlgorithmResultContent oldContent = this.currentContent;
		this.currentContent = content;
		
		Structure oldGraph = result.getStructure( oldContent );
		Structure newGraph = result.getStructure( this.currentContent );
		
		if ( oldContent != null ) {
			if ( oldContent.getDisplaySubgraphs( result.getDisplaySubgraphModes(), oldGraph ) != null )
				getGraph( oldGraph ).deregisterAllSubgraphs();
			if ( oldContent.getElementTips( result.getElementTipsDisplayModes() ) != null )
				getGraph( oldGraph ).deregisterAllElementTips();
		}
		// show new Content
		if ( this.currentContent.getDisplaySubgraphs( result.getDisplaySubgraphModes(), newGraph ) != null ) {
			for ( DisplaySubgraph subgraph : this.currentContent.getDisplaySubgraphs( result.getDisplaySubgraphModes(), newGraph ).values() )
				getGraph( newGraph ).registerSubgraph( subgraph );
		}
		if ( this.currentContent.getElementTips( result.getElementTipsDisplayModes() ) != null ) {
			for ( ElementTips tips : this.currentContent.getElementTips( result.getElementTipsDisplayModes() ).values() )
				getGraph( newGraph ).registerElementTips( tips );
		}
		
		if ( oldGraph != newGraph )
			fireGraphReplaced();
		fireContentChanged();
	}
	
	protected void fireGraphReplaced() {
		for ( AlgorithmInfoListener listener : listeners )
			listener.graphReplaced();
	}

	protected void fireContentChanged() {
		for ( AlgorithmInfoListener listener : listeners )
			listener.contentChanged();
	}

	protected void init() {
		for( GJGraph graph : jgraphs.values() )
			graph.setElementsAndStructureEditable( false );
	}
	
	public GJGraph getGraph( Structure graphT ) {
		GJGraph graph = jgraphs.get( graphT );
		if ( graph == null ) {
			graph = new GJGraph( graphT );
			graph.getModel().addUndoableEditListener( undoManager );
			jgraphs.put( graphT, graph );
		}
		if ( graph.getParent() == null )
			new JScrollPane( graph );
		return graph;
	}
	
	public GJGraph getGraph() throws UserException {
		return getGraph( result.getStructure( currentContent ) );
	}
	
	public Structure getAlgorithmResultGraph() throws UserException {
		return result.getStructure( null );
	}
	
	public Set<Structure> getAllGraphs() throws UserException {
		return getAllGraphs( result );
	}
	
	public static Set<Structure> getAllGraphs( AlgorithmResult result ) throws UserException {
		return result.getAllStructures();
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

	public Hashtable<String, ElementTips> getElementTips( AlgorithmResultContent content ) throws UserException {
		return result.getElementTips( content );
	}
	
	public Hashtable<String, DisplaySubgraph> getDisplaySubgraphs(AlgorithmResultContent content ) throws UserException {
		return result.getDisplaySubgraphs( content );
	}
	
	public Hashtable<String, ElementTipsDisplayMode> getElementTipsDisplayModes( boolean all ) {
		if ( all )
			return result.getElementTipsDisplayModes();
		try {
			return result.getElementTipsDisplayModes( currentContent );
		} catch (UserException e) {
			MainPad.getInstance().handleUserException( e );
		}
		return null;
	}
	
	public Hashtable<String, DisplaySubgraphMode> getDisplaySubgraphModes( boolean all ) {
		if ( all )
			return result.getDisplaySubgraphModes();
		try {
			return result.getDisplaySubgraphModes( currentContent );
		} catch (UserException e) {
			MainPad.getInstance().handleUserException( e );
		}
		return null;
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
	
	public void currentContentChanged( ) throws UserException {
		setCurrentContent( currentContent );
	}
	
}
