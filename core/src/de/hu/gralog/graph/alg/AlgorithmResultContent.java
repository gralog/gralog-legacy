/*
 * Created on 27 Oct 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.graph.alg;

import java.util.Hashtable;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.Subgraph;

import de.hu.gralog.jgrapht.graph.DisplaySubgraph;
import de.hu.gralog.jgrapht.graph.ElementTips;
import de.hu.gralog.jgrapht.graph.DisplaySubgraph.DisplaySubgraphMode;
import de.hu.gralog.jgrapht.graph.ElementTips.ElementTipsDisplayMode;

public class AlgorithmResultContent {
	protected Graph graph = null;
	protected Hashtable<String, Subgraph> subgraphs = null;
	protected Hashtable<String, Hashtable> elementTips = null;
	
	private Hashtable<String, ElementTips> elementTipsCache = null;
	protected Hashtable<String, DisplaySubgraph> displaySubgraphCache = null;
	
	public AlgorithmResultContent(  ) {
	}
	
	public AlgorithmResultContent( Graph graph ) {
		setGraph( graph );
	}
	
	public void setGraph( Graph graph ) {
		this.graph = graph;
	}
	
	public Graph getGraph() {
		return graph;
	}
	
	public Hashtable<String, Hashtable> getTips() {
		return elementTips;
	}
	
	public void addElementTips( String description, Hashtable elementTips ) {
		if ( this.elementTips == null )
			this.elementTips = new Hashtable<String, Hashtable>();
		this.elementTips.put( description, elementTips );
	}
	
	protected Hashtable<String, ElementTips> getElementTips( Hashtable<String, ElementTipsDisplayMode> modes ) {
		if ( elementTips == null )
			return null;
		if ( elementTipsCache == null ) {
			elementTipsCache = new Hashtable<String, ElementTips>();
			for ( Map.Entry<String, ElementTipsDisplayMode> entry : modes.entrySet() ) {
				Hashtable et = elementTips.get( entry.getKey() );
				if ( et != null ) {
					ElementTips tips = new ElementTips( entry.getValue(), et );
					elementTipsCache.put( entry.getKey(), tips );
				}
			}
		}
		return elementTipsCache;
	}
	
	public Hashtable<String, Subgraph> getSubgraphs() {
		return subgraphs;
	}
	
	public void addDisplaySubgraph( String description, Subgraph subgraph ) {
		if ( subgraphs == null )
			subgraphs = new Hashtable<String, Subgraph>();
		subgraphs.put( description, subgraph );
	}
	
	protected Hashtable<String, DisplaySubgraph> getDisplaySubgraphs( Hashtable<String, DisplaySubgraphMode> modes ) {
		if ( subgraphs == null )
			return null;
		if ( displaySubgraphCache == null ) {
			displaySubgraphCache = new Hashtable<String, DisplaySubgraph>();
			for ( Map.Entry<String, DisplaySubgraphMode> entry : modes.entrySet() ) {
				Subgraph subgraph = subgraphs.get( entry.getKey() );
				if ( subgraph != null ) {
					DisplaySubgraph display = new DisplaySubgraph( entry.getValue(), subgraph );
					displaySubgraphCache.put( entry.getKey(), display );
				}
			}
		}
		return displaySubgraphCache;
	}
}
