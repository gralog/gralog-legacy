/*
 * Created on 27 Oct 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.graph.alg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jgrapht.Graph;

/**
 * This class extends {@link AlgorithmResultContent} in order to form a tree of contents. 
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
	
	protected Set<Graph> getAllGraphs() {
		HashSet<Graph> graphs = new HashSet<Graph>();
		if ( getGraph() != null )
			graphs.add( getGraph() );
		for ( AlgorithmResultContentTreeNode content : getChildren() ) {
			graphs.addAll( content.getAllGraphs() );
		}
		return graphs;
	}
}
