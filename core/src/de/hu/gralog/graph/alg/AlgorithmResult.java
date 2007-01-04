/*
 * Created on 2006 by Sebastian Ordyniak
 *
 * Copyright 2006 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of Games.
 *
 * Games is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * Games is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Games; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */

package de.hu.gralog.graph.alg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.event.UndoableEditListener;

import org.jgrapht.Graph;

import de.hu.gralog.jgrapht.graph.DisplaySubgraph;
import de.hu.gralog.jgrapht.graph.ElementTips;
import de.hu.gralog.jgrapht.graph.DisplaySubgraph.DisplaySubgraphMode;
import de.hu.gralog.jgrapht.graph.ElementTips.ElementTipsDisplayMode;

/**
 * This class basically serves as a container for {@link AlgorithmResultContent AlgorithmResultContent's}. It also
 * holds global information which is shared along all contents like a description,
 * displaymodes for subgraphs and element-tips.
 * <p> 
 * An AlgorithmResult consists either of
 * <ul>
 * 	<li>a single content, see {@link #setSingleContent(AlgorithmResultContent)}</li>
 * 	<li>a list of contents, see {@link #setContentList(ArrayList)}</li>
 *  <li>a tree of contents, see {@link #setContentTree(AlgorithmResultContentTreeNode)}</li>
 * </ul>
 * <p>
 * GraLog display's one content at a time but allows the user to switch between different contents. 
 * Each content consists of a graph, a list of subgraphs and a list of so called element-tips. 
 * AlgorithmResult stores the contents together with the displaying-modes for all subgraphs
 * and element-tips contained in any content. So suppose you decide to display a certain subgraph 
 * and you want to highlight all vertices contained in this subgraph. Then the first thing you have
 * to do is to define a {@link de.hu.gralog.jgrapht.graph.DisplaySubgraph.DisplaySubgraphMode} and
 * add it to your result, like this:
 * <p>
 * <pre>
 * 		AlgorithmResult result = new AlgorithmResult();
 * 
 * 		DisplaySubgraphMode mode = new DisplaySubgraphMode();
 * 		mode.setVertexDisplayMode( DisplayMode.HIGH1, DisplayMode.SHOW );
 * 		result.addDisplaySubgraphMode( "a description for your subgraph", mode );
 * </pre>
 * <p>
 * Note that the description given in {@link #addDisplaySubgraphMode(String, DisplaySubgraphMode)} also
 * acts as identifier for this subgraph. So when you add the actual subgraph to your content
 * via {@link AlgorithmResultContent#addDisplaySubgraph(String, org.jgrapht.graph.Subgraph)} you have
 * to use the same description again. 
 * 
 * @author ordyniak
 *
 */
public class AlgorithmResult implements Serializable {

	private transient ArrayList<AlgorithmResultListener> listeners = new ArrayList<AlgorithmResultListener>();
	
	private String description = null;
	
	protected Graph graph = null;
	protected Hashtable<String, DisplaySubgraphMode> subgraphModes = null;
	protected Hashtable<String, ElementTipsDisplayMode> elementTipsModes = null;
	
	protected AlgorithmResultContent singleContent = null;
	protected ArrayList<AlgorithmResultContent> contentList = null;
	protected AlgorithmResultContentTreeNode contentTree = null;
	
	/**
	 * Contructs an AlgorithmResult
	 *
	 */
	
	public AlgorithmResult() {
	}
	
	/**
	 * Contructs an AlgorithmResult for a given graph. The graph
	 * is used as the default graph for contents which have no graph attached.
	 * 
	 * @param graph
	 */
	public AlgorithmResult( Graph graph ) {
		this.graph = graph;
	}
	
	/**
	 * 
	 * 
	 * @return the graph of this result
	 */
	public Graph getGraph() {
		return graph;
	}
	
	Graph getGraph( AlgorithmResultContent content ) {
		if ( content == null || content.getGraph() == null )
			return graph;
		return content.getGraph();
	}
	
	/**
	 * Sets the description of this Result, which is displayed in GraLog.
	 * The description can be given as html.
	 * 
	 * @param description an html string
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 
	 * @return the description for this Result, this can be an html-string
	 */
	
	public String getDescription() {
		return description;
	}
	
	/**
	 * Add an elementtip-mode to this result. If an elementtip-mode under the same 
	 * description already exists the mode is overriden. Please note that
	 * when adding the corresponding element-tip to your content via 
	 * {@link AlgorithmResultContent#addElementTips(String, Hashtable)} you
	 * have to use the same description.
	 * 
	 * @param description - A description for this elementtip-mode which is displayed in gralog.
	 * @param mode - An elementtip-mode which is used for all elementtips with the same description.
	 */
	public void addElementTipsDisplayMode( String description, ElementTipsDisplayMode mode ) {
		if ( elementTipsModes == null )
			elementTipsModes = new Hashtable<String, ElementTipsDisplayMode>();
		elementTipsModes.put( description, mode );
	}

	/**
	 * Add a subgraph-mode to this result. If a subgraph-mode under the same 
	 * description already exists the mode is overriden. Please note that
	 * when adding the corresponding subgraph to your content via 
	 * {@link AlgorithmResultContent#addDisplaySubgraph(String, org.jgrapht.graph.Subgraph)} you
	 * have to use the same description.
	 * 
	 * @param description - A description for this subgraph-mode which is displayed in gralog.
	 * @param mode - A subgraph-mode which is used for all subgraphs with the same description.
	 */
	public void addDisplaySubgraphMode( String description, DisplaySubgraphMode subgraph ) {
		if ( subgraphModes == null )
			subgraphModes = new Hashtable<String, DisplaySubgraphMode>();
		subgraphModes.put( description, subgraph );
	}
	
	Hashtable<String, ElementTips> getElementTips( AlgorithmResultContent content ) {
		return content.getElementTips( elementTipsModes );
	}
	
	Hashtable<String, DisplaySubgraph> getDisplaySubgraphs( AlgorithmResultContent content ) {
		return content.getDisplaySubgraphs( subgraphModes );
	}
	
	/**
	 *  
	 * @return a Hashtable containing all stored element-tips displaymodes together with their
	 * descriptions
	 */
	public Hashtable<String, ElementTipsDisplayMode> getElementTipsDisplayModes() {
		return elementTipsModes;
	}
	
	/**
	 *  
	 * @return a Hashtable containing all stored subgraph displaymodes together with their
	 * descriptions
	 */
	public Hashtable<String, DisplaySubgraphMode> getDisplaySubgraphModes() {
		return subgraphModes;
	}
	
	/**
	 * Set a single content for this result. Each result can consists either of
	 * 	- a single content
	 *  - a content list
	 *  - a content treenode
	 * 
	 * @param content
	 */
	public void setSingleContent( AlgorithmResultContent content ) {
		if ( content instanceof AlgorithmResultInteractiveContent )
			((AlgorithmResultInteractiveContent)content).setResult( this );
		this.singleContent = content;
	}

	/**
	 * Set a content list for this result. Each result can consists either of
	 * 	- a single content
	 *  - a content list
	 *  - a content treenode
	 * 
	 * @param content
	 */
	public void setContentList( ArrayList<AlgorithmResultContent> content ) {
		this.contentList = content;
	}
	
	/**
	 * Set a content treenode for this result. Each result can consists either of
	 * 	- a single content
	 *  - a content list
	 *  - a content treenode
	 * 
	 * @param content
	 */
	public void setContentTree( AlgorithmResultContentTreeNode content ) {
		this.contentTree = content;
	}
	
	AlgorithmResultContent getFirstContent() {
		if ( singleContent != null )
			return singleContent;
		if ( contentList != null )
			return contentList.get( 0 );
		if ( contentTree != null )
			return contentTree;
		return null;
	}
	
	/**
	 * 
	 * @return the list of contents stored for this result
	 */
	public ArrayList<AlgorithmResultContent> getContentList() {
		return contentList;
	}

	/**
	 * 
	 * @return the single content stored for this result
	 */
	public AlgorithmResultContent getSingleContent() {
		return singleContent;
	}
	
	/**
	 * 
	 * @return the content-tree stored for this result
	 */
	public AlgorithmResultContentTreeNode getContentTree() {
		return contentTree;
	}
	
	void fireCurrentContentChanged() {
		for ( AlgorithmResultListener listener : listeners )
			listener.currentContentChanged();
	}
	
	void addListener( AlgorithmResultListener listener ) {
		if ( ! listeners.contains( listener ) )
			listeners.add( listener );
	}
	
	void addUndoableEditListener(UndoableEditListener listener) {
		if ( subgraphModes != null ) {
			for ( DisplaySubgraphMode mode : this.subgraphModes.values() )
				mode.addUndoableEditListener( listener );
		}
		if ( elementTipsModes != null ) {
			for ( ElementTipsDisplayMode mode : this.elementTipsModes.values() )
				mode.addUndoableEditListener( listener );
		}
	}
}
