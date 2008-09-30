/*
 * Created on 2006 by Sebastian Ordyniak
 *
 * Copyright 2006 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of GrALoG.
 *
 * GrALoG is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * GrALoG is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GrALoG; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */

package de.hu.gralog.algorithm.result;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jgrapht.graph.Subgraph;

import de.hu.gralog.app.UserException;
import de.hu.gralog.structure.Structure;

/**
 * The class represents a content which is displayed as part of an
 * {@link AlgorithmResult}.
 * 
 * You can use a content to display a structure, associated subgraphs and / or
 * element-tips ( for the vertices and / or edges of that structure ). Please notice
 * that you have to specify a structure for each content either by attaching the
 * structure to the content or by attaching the structure to the result, the content
 * belongs to. Subgraphs and element-tips are given together with a description,
 * which has to be identical to the description given for the corresponding
 * subgraph-/element-tip-display-mode of the result the content belongs to.
 * 
 * Please see {@link AlgorithmResult} for further information about contents and
 * results.
 * 
 * @author ordyniak
 * 
 */
public class AlgorithmResultContent {
	protected Structure structure = null;

	protected Hashtable<String, SubgraphInfo> subgraphs = null;

	protected Hashtable<String, Hashtable> elementTips = null;

	protected Hashtable<String, Subgraph> subgraphsCache = null;

	private Hashtable<String, ElementTips> elementTipsCache = null;

	protected Hashtable<String, DisplaySubgraph> displaySubgraphCache = null;

	private String name = null;
	
	private String description = null;

	/**
	 * Contructs an AlgorithmResultContent without a corresponding structure. You
	 * can specify a structure later by using
	 * {@link #setStructure(Structure)}. If you do not specify a
	 * structure then the structure associated with the {@link AlgorithmResult} is
	 * displayed with this content.
	 * 
	 */
	public AlgorithmResultContent() {
	}

	/**
	 * Constructs an AlgorithmResultContent with the structure to be displayed with
	 * this content.
	 * 
	 * @param structure
	 *            the structure which should be displayed with this content
	 */
	public AlgorithmResultContent(Structure structure) {
		setStructure(structure);
	}

	/**
	 * Sets the name of this AlgorithmResultContent that is displayed in GrALoG.
	 * Currently GrALoG only supports displaying names for
	 * {@link AlgorithmResultContentTreeNode AlgorithmResultContentTreeNode's}
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the name given to this AlgorithmResultContent (@see
	 * #setName(String)})
	 * 
	 * @return the name as a string
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the description for this content that will be displayed in GrALoG
	 * 
	 * @param description the description as an arbitrary html-String
	 */
	public void setDescription( String description ) {
		this.description = description;
	}

	/**
	 * 
	 * 
	 * @return the description of this content
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Set the structure for this content. This method has the same effect as setting
	 * the structure via the constructor
	 * {@link #AlgorithmResultContent(Structure)}.
	 * 
	 * @param structure
	 *            the structure which should be displayed with this content
	 */
	public void setStructure(Structure structure) {
		this.structure = structure;
	}

	/**
	 * @see #setStructure(Structure)
	 * 
	 * @return the structure which is displayed with this content, if null the structure
	 *         associated with the {@link AlgorithmResult} containing this
	 *         content is displayed with this content.
	 */
	public Structure getStructure() throws UserException {
		return structure;
	}

	/**
	 * This method is called by GrALoG when saving this AlgorithmResultContent,
	 * to infer all element-tips together with their descriptions, that have been
	 * registered with this content.
	 * 
	 * @return a Hashtable containing all element-tips and their corresponding
	 *         descriptions which should be displayed with this content
	 */
	public Hashtable<String, Hashtable> getTips() throws UserException {
		return elementTips;
	}

	/**
	 * Add element-tips to be displayed with this content. Note that there has to
	 * be a corresponding element-tip-display-mode with the same description in the
	 * AlgorithmResult this content belongs to.
	 * 
	 * @param description
	 *            a description for this element-tip
	 * @param elementTips
	 *            elementtips should be a Hashtable from vertices and / or edges
	 *            of the structure ( which is displayed with this content ) to
	 *            String. When the content is displayed GrALoG paints the
	 *            Strings as tool-tips on the vertices and / or edges.
	 * 
	 */
	public void addElementTips(String description, Hashtable elementTips) {
		if (this.elementTips == null)
			this.elementTips = new Hashtable<String, Hashtable>();
		this.elementTips.put(description, elementTips);
	}

	/**
	 * This method is called by GrALoG when this content is displayed, to infer
	 * all element-tips that are registered with this content and correspond to
	 * an element-tips-display-mode registered with the corresponding
	 * AlgorithmResult. Subclasses can override this method in order to build
	 * their element-tips on the fly.
	 * 
	 * @param modes
	 * @throws UserException
	 */
	protected Hashtable<String, ElementTips> getElementTips(
			Hashtable<String, ElementTipsDisplayMode> modes)
			throws UserException {
		if (elementTips == null)
			return null;
		if (elementTipsCache == null) {
			elementTipsCache = new Hashtable<String, ElementTips>();
			for (Map.Entry<String, ElementTipsDisplayMode> entry : modes
					.entrySet()) {
				Hashtable et = elementTips.get(entry.getKey());
				if (et != null) {
					ElementTips tips = new ElementTips(entry.getValue(), et);
					elementTipsCache.put(entry.getKey(), tips);
				}
			}
		}
		return elementTipsCache;
	}

	/**
	 * This method is called by GrALoG to save this AlgorithmResultContent. It
	 * returns all SubgraphInfo's together with their description that have been
	 * registered with this AlgorithmResultContent.
	 * 
	 * @return a Hashtable containing description and SubgraphInfo for all
	 *         subgraphInfo's registered with this AlgorithmResultContent
	 */
	public Hashtable<String, SubgraphInfo> getSubgraphsInfo() {
		return subgraphs;
	}

	/**
	 * Add a subgraph to be displayed with this content. Note that their has to
	 * be a corresponding subgraph-display-mode with the same description in the
	 * AlgorithmResult this content belongs to (@see
	 * AlgorithmResult#addDisplaySubgraphMode(String, DisplaySubgraphMode)).
	 * 
	 * @param description
	 *            a description for this subgraph
	 * @param subgraphInfo
	 *            subgraphInfo containing the vertices and edges to be contained
	 *            in this subgraph
	 * 
	 * Clearly subgraphInfo should only contain vertices and edges that are
	 * contained in the structure belonging to this AlgorithmResultContent.
	 * 
	 */
	public void addDisplaySubgraph(String description, SubgraphInfo subgraphInfo) {
		if (subgraphs == null)
			subgraphs = new Hashtable<String, SubgraphInfo>();
		subgraphs.put(description, subgraphInfo);
	}

	/**
	 * Adds a subgraph to be displayed with this content. Other than
	 * 
	 * @see #addDisplaySubgraph(String, SubgraphInfo) this method lets you
	 *      directedly specify the vertices and edges to be contained in the
	 *      subgraph. Note that their has to be a corresponding
	 *      subgraph-display-mode with the same description in the AlgorithmResult
	 *      this content belongs to (@see
	 *      AlgorithmResult#addDisplaySubgraphMode(String,
	 *      DisplaySubgraphMode)).
	 * 
	 * @param description
	 *            a description for this subgraph
	 * @param vertices
	 *            the vertices to be added to this subgraph, if null the
	 *            subgraph will contain all vertices of the graph associated to
	 *            this content
	 * @param edges
	 *            the edges to be added to this subgraph, if null the subgraph
	 *            will contain all edges between vertices contained in this
	 *            subgraph
	 * 
	 * Clearly the vertices and edges should be contained in the structure belonging
	 * to this content.
	 * 
	 */
	public void addDisplaySubgraph(String description, Set vertices, Set edges) {
		if (subgraphs == null)
			subgraphs = new Hashtable<String, SubgraphInfo>();
		subgraphs.put(description, new SubgraphInfo(vertices, edges));
	}

	/**
	 * This method is called from GrALoG before displaying the content and
	 * should return all subgraphs together with their corresponding description
	 * that should be displayed with this content. The default method uses the
	 * parameter {@link #structure} to construct all subgraphs for all
	 * subgraphInfo's registered with this content. Subclasses can override this
	 * method to change the subgraphs that should be displayed by this content
	 * or to construct these on the fly.
	 * 
	 * @param modes
	 *            the displaysubgraphmodes registered with the corresponding
	 *            {@link AlgorithmResult}
	 * @param structure
	 *            the GrALoG-structure needed to construct actual subgraphs from the
	 *            subgraphInfos registered with this content
	 * 
	 * @return a Hashtable of subgraphs and their descriptions which should be
	 *         displayed with this content
	 */
	protected Hashtable<String, DisplaySubgraph> getDisplaySubgraphs(
			Hashtable<String, DisplaySubgraphMode> modes,
			Structure structure) throws UserException {
		if (subgraphs == null)
			return null;
		if (displaySubgraphCache == null) {
			displaySubgraphCache = new Hashtable<String, DisplaySubgraph>();
			for (Map.Entry<String, DisplaySubgraphMode> entry : modes
					.entrySet()) {
				SubgraphInfo subgraphInfo = subgraphs.get(entry.getKey());
				if (subgraphInfo != null) {
					Subgraph subgraph = subgraphInfo.getSubgraph(structure);
					DisplaySubgraph display = new DisplaySubgraph(entry
							.getValue(), subgraph);
					displaySubgraphCache.put(entry.getKey(), display);
				}
			}
		}
		return displaySubgraphCache;
	}

	/**
	 * This method is used by GrALoG to show a name for this
	 * AlgorithmResultContent. It currently returns the name of this
	 * AlgorithmResultContent
	 * 
	 * @see #setName(String).
	 * 
	 */
	public String toString() {
		return name;
	}

	/**
	 * This class represents a subgraph by its edges and vertices. Note that
	 * setting the parameters vertices / edges to null results in a subgraph
	 * containing all vertices / edges of the corresponding structure.
	 * 
	 * @author Sebastian
	 * 
	 * @param <V>
	 *            the type of vertices stored in this SubgraphInfo
	 * @param <E>
	 *            the type of edges stored in this SubgraphInfo
	 */
	public static class SubgraphInfo<V, E> {
		private Set<V> vertices;

		private Set<E> edges;

		private Subgraph<V, E, ?> subgraphCache;

		public SubgraphInfo() {

		}

		/**
		 * Construct a subgraphInfo with the specified vertices and edges
		 * 
		 * @param vertices
		 *            the vertices contained in this subgraphInfo, if null the
		 *            corresponding subgraph will contain all vertices of the
		 *            underlying graph
		 * 
		 * @param edges
		 *            the edges contained in this subgraphInfo, if null the
		 *            corresponding subgraph will be induced, i.e. containing
		 *            all edges between the given vertices
		 * 
		 */
		public SubgraphInfo(Set<V> vertices, Set<E> edges) {
			if ( vertices != null )
				this.vertices = new HashSet<V>(vertices);
			if ( edges != null )
				this.edges = new HashSet<E>(edges);
		}

		public Subgraph getSubgraph(Structure graphSupport) {
			if (subgraphCache == null)
				subgraphCache = new Subgraph(graphSupport.getGraph(), vertices,
						edges);
			return subgraphCache;
		}

		public Set<V> getVertices() {
			return vertices;
		}

		public Set<E> getEdges() {
			return edges;
		}
	}
}
