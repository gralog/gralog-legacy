/*
 * Created on 27 Oct 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.graph.alg;

import java.util.ArrayList;

public class AlgorithmResultContentTreeNode extends AlgorithmResultContent {

	protected ArrayList<AlgorithmResultContentTreeNode> children = new ArrayList<AlgorithmResultContentTreeNode>();
	protected AlgorithmResultContentTreeNode parent = null;
	
	public AlgorithmResultContentTreeNode() {
		super();
	}
	
	public void addChild( AlgorithmResultContentTreeNode child ) {
		child.parent = this;
		children.add( child );
	}
	
	public ArrayList<AlgorithmResultContentTreeNode> getChildren() {
		return children;
	}
	
	public AlgorithmResultContentTreeNode getParent() {
		return parent;
	}
	
}
