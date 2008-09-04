/*
 * Created on 2006 by Sebastian Ordyniak
 *
 * Copyright 2006 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of Gralog.
 *
 * Gralog is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * Gralog is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Gralog; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */
package de.hu.gralog.algorithm.result;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.GralogGraphSupport;

/**
 * This class extends {@link AlgorithmResultContent} in order to form a tree of
 * contents.
 * 
 * This class assumes that the whole tree is build by the algorithm. If you want
 * a lazy implementation of the tree, i.e. the children are only calculated when
 * a user decides to see them you have to override this class. Special care has
 * been taken for {@link #getAllGraphs()} - see the description of this method.
 * 
 * @author ordyniak
 * 
 */
public class AlgorithmResultContentTreeNode extends AlgorithmResultContent {

	protected transient ArrayList<AlgorithmResultContentTreeNode> children = new ArrayList<AlgorithmResultContentTreeNode>();

	protected transient AlgorithmResultContentTreeNode parent = null;

	/**
	 * An empty Contructor
	 * 
	 */
	public AlgorithmResultContentTreeNode() {
		super();
	}

	/**
	 * Add a child to this node
	 * 
	 * @param child
	 */
	public void addChild(AlgorithmResultContentTreeNode child) {
		child.parent = this;
		children.add(child);
	}

	/**
	 * 
	 * @return an ArrayList of the children of this node never returns null
	 */
	public ArrayList<AlgorithmResultContentTreeNode> getChildren()
			throws UserException {
		return children;
	}

	/**
	 * 
	 * @return the parent of this node - returns null if this is the rootnode
	 */
	public AlgorithmResultContentTreeNode getParent() {
		return parent;
	}

	/**
	 * This method returns all graphs associated with the subtree rooted at this
	 * node. Note that this method is called by Gralog before displaying the
	 * actual content. If you do want a lazy implementation of your Tree you
	 * have to override this method.
	 * 
	 * @throws UserException
	 */
	protected Set<GralogGraphSupport> getAllGraphs() throws UserException {
		HashSet<GralogGraphSupport> graphs = new HashSet<GralogGraphSupport>();
		if (getGraphSupport() != null)
			graphs.add(getGraphSupport());
		for (AlgorithmResultContentTreeNode content : getChildren()) {
			graphs.addAll(content.getAllGraphs());
		}
		return graphs;
	}
}
