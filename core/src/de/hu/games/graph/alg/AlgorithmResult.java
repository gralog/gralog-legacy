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

package de.hu.games.graph.alg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.event.UndoableEditListener;

import org.jgrapht.Graph;

import de.hu.games.jgrapht.graph.DisplaySubgraph;
import de.hu.games.jgrapht.graph.ElementTips;
import de.hu.games.jgrapht.graph.DisplaySubgraph.DisplaySubgraphMode;
import de.hu.games.jgrapht.graph.ElementTips.ElementTipsDisplayMode;


public class AlgorithmResult implements Serializable {

	private transient ArrayList<AlgorithmResultListener> listeners = new ArrayList<AlgorithmResultListener>();
	
	private String description = null;
	
	protected Graph graph = null;
	protected Hashtable<String, DisplaySubgraphMode> subgraphModes = null;
	protected Hashtable<String, ElementTipsDisplayMode> elementTipsModes = null;
	
	protected AlgorithmResultContent singleContent = null;
	protected ArrayList<AlgorithmResultContent> contentList = null;
	protected AlgorithmResultContentTreeNode contentTree = null;
	
	public AlgorithmResult() {
	}
	
	public AlgorithmResult( Graph graph ) {
		this.graph = graph;
	}
	
	public Graph getGraph() {
		return graph;
	}
	
	Graph getGraph( AlgorithmResultContent content ) {
		if ( content == null || content.getGraph() == null )
			return graph;
		return content.getGraph();
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void addElementTipsDisplayMode( String description, ElementTipsDisplayMode mode ) {
		if ( elementTipsModes == null )
			elementTipsModes = new Hashtable<String, ElementTipsDisplayMode>();
		elementTipsModes.put( description, mode );
	}

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
	
	public Hashtable<String, ElementTipsDisplayMode> getElementTipsDisplayModes() {
		return elementTipsModes;
	}
	
	public Hashtable<String, DisplaySubgraphMode> getDisplaySubgraphModes() {
		return subgraphModes;
	}
	
	public void setSingleContent( AlgorithmResultContent content ) {
		if ( content instanceof AlgorithmResultInteractiveContent )
			((AlgorithmResultInteractiveContent)content).setResult( this );
		this.singleContent = content;
	}
	
	public void setContentList( ArrayList<AlgorithmResultContent> content ) {
		this.contentList = content;
	}
	
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
	
	public ArrayList<AlgorithmResultContent> getContentList() {
		return contentList;
	}

	public AlgorithmResultContent getSingleContent() {
		return singleContent;
	}
	
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
