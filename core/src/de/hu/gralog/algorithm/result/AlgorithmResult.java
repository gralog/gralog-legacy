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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.swing.event.UndoableEditListener;

import de.hu.gralog.app.UserException;
import de.hu.gralog.structure.Structure;

/**
 * This class basically serves as a container for
 * {@link AlgorithmResultContent AlgorithmResultContent's}. It also holds
 * global information that is shared among all Algorithm-Result-Contents 
 * like a description and
 * display-modes for subgraphs and element-tips.
 * <p>
 * An AlgorithmResult consists either of
 * <ul>
 * <li>no contents, in this case GrALoG displays the description for this result
 * but does not open a new Algorithm-Result-Document</li>
 * <li>a single-content, see {@link #setSingleContent(AlgorithmResultContent)}</li>
 * <li>a list of contents, see {@link #setContentList(ArrayList)}</li>
 * <li>a tree of contents, see
 * {@link #setContentTree(AlgorithmResultContentTreeNode)}</li>
 * </ul>
 * <p>
 * GrALoG display's one content at a time but allows the user to switch between
 * different contents. Each content consists of a graph, a list of subgraphs and
 * a list of so called element-tips. AlgorithmResult stores the contents
 * together with the display-modes for all subgraphs and element-tips
 * contained in any content. So suppose you decide to display a certain subgraph
 * and you want to highlight all vertices contained in this subgraph. Then the
 * first thing you have to do is to define a
 * {@link de.hu.gralog.algorithm.result.DisplaySubgraphMode} and add it to your
 * result, like this:
 * <p>
 * 
 * <pre>
 * AlgorithmResult result = new AlgorithmResult();
 * 
 * DisplaySubgraphMode mode = new DisplaySubgraphMode();
 * mode.setVertexDisplayMode(DisplayMode.HIGH1, DisplayMode.SHOW);
 * result.addDisplaySubgraphMode(&quot;a description for your subgraph&quot;, mode);
 * </pre>
 * 
 * <p>
 * Note that the description given in
 * {@link #addDisplaySubgraphMode(String, DisplaySubgraphMode)} also acts as
 * identifier for this subgraph. So when you add the actual subgraph to your
 * content via
 * {@link AlgorithmResultContent#addDisplaySubgraph(String, Set, Set)} you have
 * to use the same description again.
 * 
 * @author ordyniak
 * 
 */
public class AlgorithmResult implements Serializable {

	private transient ArrayList<AlgorithmResultListener> listeners = new ArrayList<AlgorithmResultListener>();

	private String description = null;

	protected Structure structure = null;

	protected Hashtable<String, DisplaySubgraphMode> subgraphModes = null;

	protected Hashtable<String, ElementTipsDisplayMode> elementTipsModes = null;

	protected AlgorithmResultContent singleContent = null;

	protected ArrayList<AlgorithmResultContent> contentList = null;

	protected AlgorithmResultContentTreeNode contentTree = null;

	protected boolean openContentsAsStructures = false;
	
	/**
	 * Contructs an AlgorithmResult
	 * 
	 */

	public AlgorithmResult() {
	}

	/**
	 * Contructs an AlgorithmResult for a given structure. The structure is used as the
	 * default structure for contents that have no structure attached.
	 * 
	 * @param structure
	 */
	public AlgorithmResult(Structure structure) {
		this.structure = structure;
	}

	/**
	 * 
	 * 
	 * @return the graph of this result
	 */
	public Structure getStructure() {
		return structure;
	}

	/**
	 * Returns the structure associated with a given {@link AlgorithmResultContent}
	 * 
	 * @param content
	 * @return the structure for this content
	 * @throws UserException
	 */
	Structure getStructure(AlgorithmResultContent content)
			throws UserException {
		if (content == null || content.getStructure() == null)
			return structure;
		return content.getStructure();
	}

	/**
	 * Sets the description of this Result, which is displayed in GrALoG. The
	 * description can be given as html.
	 * 
	 * @param description
	 *            an html string
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

	boolean hasContents() {
		if ( getSingleContent() == null && getContentList() == null && getContentTree() == null )
			return false;
		return true;
	}
	
	/**
	 * Set this option to true, if you want GrALoG to open all graphs contained
	 * in this result to be opened in a separate window. When this option is
	 * true the user will not be displayed an {@link AlgorithmResult} but
	 * instead GrALoG opens a new graphdocument for each graph contained in this
	 * algorithmresult. Setting this option to true is for example useful when
	 * your algorithm is a graph-generator.
	 * 
	 * @param openContentsAsStructures
	 */
	public void setOpenContentsAsStructures(boolean openContentsAsStructures) {
		this.openContentsAsStructures = openContentsAsStructures;
	}

	/**
	 * @see #setOpenContentsAsStructures(boolean)
	 * 
	 * @return the status of openContentAsStructures
	 */
	public boolean isOpenContentsAsStructures() {
		return openContentsAsStructures;
	}

	/**
	 * Add an element-tip-display-mode to this result. If an element-tip-display-mode under the
	 * same description already exists the mode is overriden. Please note that
	 * when adding the corresponding element-tip to your content via
	 * {@link AlgorithmResultContent#addElementTips(String, Hashtable)} you have
	 * to use the same description.
	 * 
	 * @param description -
	 *            A description for this element-tip-display-mode that is displayed in
	 *            GrALoG.
	 * @param mode -
	 *            An element-tip-display-mode that is used for all element-tips with the
	 *            same description.
	 */
	public void addElementTipsDisplayMode(String description,
			ElementTipsDisplayMode mode) {
		if (elementTipsModes == null)
			elementTipsModes = new Hashtable<String, ElementTipsDisplayMode>();
		elementTipsModes.put(description, mode);
	}

	/**
	 * Add a subgraph-display-mode to this result. If a subgraph-display-mode under the same
	 * description already exists the mode is overriden. Please note that when
	 * adding the corresponding subgraph to your content via
	 * {@link AlgorithmResultContent#addDisplaySubgraph(String, SubgraphInfo)}
	 * you have to use the same description.
	 * 
	 * @param description -
	 *            A description for this subgraph-display-mode which is displayed in
	 *            GrALoG.
	 * @param subgraph -
	 *            A subgraph-display-mode which is used for all subgraphs with the same
	 *            description.
	 */
	public void addDisplaySubgraphMode(String description,
			DisplaySubgraphMode subgraph) {
		if (subgraphModes == null)
			subgraphModes = new Hashtable<String, DisplaySubgraphMode>();
		subgraphModes.put(description, subgraph);
	}

	/**
	 * Returns a Hashtable containing all descriptions and corresponding
	 * {@link ElementTips} registered with the given
	 * {@link AlgorithmResultContent}
	 * 
	 * @param content
	 * @return
	 * @throws UserException
	 */
	Hashtable<String, ElementTips> getElementTips(AlgorithmResultContent content)
			throws UserException {
		return content.getElementTips(elementTipsModes);
	}

	/**
	 * Returns a Hashtable containing all descriptions and corresponding
	 * {@link DisplaySubgraph DisplaySubgraphs} registered with the given
	 * {@link AlgorithmResultContent}
	 * 
	 * @param content
	 * @return
	 * @throws UserException
	 */
	Hashtable<String, DisplaySubgraph> getDisplaySubgraphs(
			AlgorithmResultContent content) throws UserException {
		if (content.getStructure() == null)
			return content
					.getDisplaySubgraphs(subgraphModes, getStructure());
		return content.getDisplaySubgraphs(subgraphModes, content
				.getStructure());
	}

	/**
	 * 
	 * @return a Hashtable containing all stored element-tip-display-modes
	 *         together with their descriptions
	 */
	public Hashtable<String, ElementTipsDisplayMode> getElementTipsDisplayModes() {
		return elementTipsModes;
	}

	/**
	 * 
	 * @return a Hashtable containing all stored element-tip-display-modes
	 *         together with their descriptions
	 */
	Hashtable<String, ElementTipsDisplayMode> getElementTipsDisplayModes( AlgorithmResultContent content ) throws UserException {
		Hashtable<String, ElementTipsDisplayMode> modes = new Hashtable<String, ElementTipsDisplayMode>();
		if ( getElementTips( content ) != null ) {
			for ( String key : getElementTips( content ).keySet() )
				modes.put( key, elementTipsModes.get( key ) );
		}
		return modes;
	}
	
	/**
	 * 
	 * @return a Hashtable containing all stored subgraph-display-modes together
	 *         with their descriptions
	 */
	public Hashtable<String, DisplaySubgraphMode> getDisplaySubgraphModes( ) {
		return subgraphModes;
	}

	
	/**
	 * 
	 * @return a Hashtable containing all stored subgraph-display-modes together
	 *         with their descriptions for this content
	 */
	Hashtable<String, DisplaySubgraphMode> getDisplaySubgraphModes( AlgorithmResultContent content ) throws UserException {
		Hashtable<String, DisplaySubgraphMode> modes = new Hashtable<String, DisplaySubgraphMode>();
		if ( getDisplaySubgraphs( content ) != null ) {
			for ( String key : getDisplaySubgraphs( content ).keySet() )
				modes.put( key, subgraphModes.get( key ) );
		}
		return modes;
	}

	/**
	 * Set a single content for this result. Each result consists either of 
	 * a single-content, a content-list or a content-tree.
	 * 
	 * @param content
	 */
	public void setSingleContent(AlgorithmResultContent content) {
		if (content instanceof AlgorithmResultInteractiveContent)
			((AlgorithmResultInteractiveContent) content).setResult(this);
		this.singleContent = content;
	}

	/**
	 * Set a content list for this result. Each result consists either of 
	 * a single-content, a content-list or a content-tree.
	 * 
	 * @param content
	 */
	public void setContentList(ArrayList<AlgorithmResultContent> content) {
		this.contentList = content;
	}

	/**
	 * Set a content-tree for this result. Each result consists either
	 * of a single-content, a content-list or a content-tree.
	 * 
	 * @param content
	 */
	public void setContentTree(AlgorithmResultContentTreeNode content) {
		this.contentTree = content;
	}

	/**
	 * 
	 * 
	 * @return the first content to be displayed by GrALoG when opening this
	 *         result
	 */
	AlgorithmResultContent getFirstContent() {
		if (singleContent != null)
			return singleContent;
		if (contentList != null)
			return contentList.get(0);
		if (contentTree != null)
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
	 * @return the single-content stored for this result
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

	/**
	 * 
	 * 
	 * @return all graphs associated with this result ( contains all graphs
	 *         associated with contents contained in this result )
	 * @throws UserException
	 */
	Set<Structure> getAllStructures() throws UserException {
		HashSet<Structure> structures = new HashSet<Structure>();
		if (this.structure != null)
			structures.add(structure);
		structures.addAll(getAllStructuresFromContents());
		return structures;
	}

	/**
	 * 
	 * @return all graphs that are associated to contents contained in this
	 *         result
	 * @throws UserException
	 */
	Set<Structure> getAllStructuresFromContents() throws UserException {
		HashSet<Structure> structures = new HashSet<Structure>();
		if (getSingleContent() != null
				&& getSingleContent().getStructure() != null)
			structures.add(getSingleContent().getStructure());
		if (getContentList() != null) {
			for (AlgorithmResultContent content : getContentList()) {
				if (content.getStructure() != null)
					structures.add(content.getStructure());
			}
		}
		if (getContentTree() != null)
			structures.addAll(getContentTree().getAllStructures());
		return structures;
	}

	/**
	 * Informs all listeners that the current content displayed by this result
	 * has changed.
	 * 
	 * @throws UserException
	 */

	void fireCurrentContentChanged() throws UserException {
		for (AlgorithmResultListener listener : listeners)
			listener.currentContentChanged();
	}

	/**
	 * Adds an {@link AlgorithmResultListener} to this result that is informed
	 * about changes in this result and contents that are contained in this
	 * result.
	 * 
	 * @param listener
	 */
	void addListener(AlgorithmResultListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	/**
	 * Adds an {@link UndoableEditListener} to this result.
	 * 
	 * @param listener
	 */
	void addUndoableEditListener(UndoableEditListener listener) {
		if (subgraphModes != null) {
			for (DisplaySubgraphMode mode : this.subgraphModes.values())
				mode.addUndoableEditListener(listener);
		}
		if (elementTipsModes != null) {
			for (ElementTipsDisplayMode mode : this.elementTipsModes.values())
				mode.addUndoableEditListener(listener);
		}
	}
}
