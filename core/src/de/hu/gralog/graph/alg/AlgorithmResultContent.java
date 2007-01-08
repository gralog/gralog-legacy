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
import de.hu.gralog.jgrapht.graph.DisplaySubgraphMode;
import de.hu.gralog.jgrapht.graph.ElementTips;
import de.hu.gralog.jgrapht.graph.ElementTipsDisplayMode;

/**
 * The class represents a content which is displayed as part of an {@link AlgorithmResult}.
 * 
 * You can use a content to display a graph, associated subgraphs and / or elementtips ( for the
 * vertices and edges of that graph ). Please notice that you have to specify a graph for each content either
 * by attaching the graph to the content or by attaching the graph to the result the content belongs to. Subgraphs
 * and elementtips are given together with a description which has to be identical to the description given
 * for the corresponding subgraph-/elementtipmode in the result the content belongs to.
 * 
 * Please see {@link AlgorithmResult} for further information about contents and results.
 * 
 * @author ordyniak
 *
 */
public class AlgorithmResultContent {
	protected Graph graph = null;
	protected Hashtable<String, Subgraph> subgraphs = null;
	protected Hashtable<String, Hashtable> elementTips = null;
	
	private Hashtable<String, ElementTips> elementTipsCache = null;
	protected Hashtable<String, DisplaySubgraph> displaySubgraphCache = null;
	
	/**
	 * Contructs an AlgorithmResultContent. 
	 *
	 */
	public AlgorithmResultContent(  ) {
	}
	
	/**
	 * Constructs an AlgorithmResultContent.
	 * 
	 * @param graph the graph which should be displayed with this content
	 */
	public AlgorithmResultContent( Graph graph ) {
		setGraph( graph );
	}
	
	/**
	 * Set the graph of this content
	 * 
	 * @param graph the graph which should be displayed with this content
	 */
	public void setGraph( Graph graph ) {
		this.graph = graph;
	}
	
	/**
	 * 
	 * @return the graph which is displayed with this content
	 */
	public Graph getGraph() {
		return graph;
	}
	
	/**
	 * 
	 * @return a Hashtable containing all element-tips and their corresponding descriptions which
	 * should be displayed with this content
	 */
	public Hashtable<String, Hashtable> getTips() {
		return elementTips;
	}
	
	/**
	 * Add elementTips to be displayed with this content. Note that their has to be
	 * a corresponding elementTipMode with the same description in the AlgorithmResult
	 * this content belongs to.
	 * 
	 * @param description a description for this elementTip
	 * @param elementTips elementtips should be a Hashtable from vertices and / or edges of 
	 * the graph ( which is displayed with this content ) to String. When the content is displayed
	 * GraLog paints the Strings as Labels on the vertices and / or edges.
	 * 
	 */
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
	
	/**
	 * 
	 * @return a Hashtable of subgraphs and their descriptions which should be displayed with this 
	 * content
	 */
	public Hashtable<String, Subgraph> getSubgraphs() {
		return subgraphs;
	}
	
	/**
	 * Add subgraph to be displayed with this content. Note that their has to be
	 * a corresponding DisplaySubgraphMode with the same description in the AlgorithmResult
	 * this content belongs to.
	 * 
	 * @param description a description for this elementTip
	 * @param subgraph this clearly has to be a subgraph of the graph attached to this content -
	 * or if no graph is attached to this content then of the graph attached to the AlgorithmResult 
	 * this content belongs to
	 * 
	 */
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
