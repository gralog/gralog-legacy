/*
 * Created on 27 Oct 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.graph.alg;

import java.util.ArrayList;

/**
 * This class extends {@link AlgorithmResultContent} in order to form a tree of contents. 
 * 
 * @author ordyniak
 *
 */
public class AlgorithmResultContentTreeNode extends AlgorithmResultContent {

	protected ArrayList<AlgorithmResultContentTreeNode> children = new ArrayList<AlgorithmResultContentTreeNode>();
	protected AlgorithmResultContentTreeNode parent = null;
	
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
	public void addChild( AlgorithmResultContentTreeNode child ) {
		child.parent = this;
		children.add( child );
	}
	
	/**
	 * 
	 * @return an ArrayList of the children of this node never returns null
	 */
	public ArrayList<AlgorithmResultContentTreeNode> getChildren() {
		return children;
	}
	
	/**
	 * 
	 * @return the parent of this node - returns null if this is the rootnode
	 */
	public AlgorithmResultContentTreeNode getParent() {
		return parent;
	}
	
}
