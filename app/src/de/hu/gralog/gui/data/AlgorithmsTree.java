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

package de.hu.gralog.gui.data;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import org.jgrapht.Graph;

import de.hu.gralog.graph.alg.Algorithm;

public class AlgorithmsTree implements AlgorithmsTreeListener {

	private TreeNode root;
	private AlgorithmsTree tree;
	private Graph graph;
	private Hashtable<Class, Algorithm> classToAlgorithm = new Hashtable<Class, Algorithm>();
	private ArrayList<AlgorithmsTreeListener> listeners = new ArrayList<AlgorithmsTreeListener>();
	
	private AlgorithmsTree( AlgorithmsTree tree, Graph graph ) {
		this.tree = tree;
		this.graph = graph;
		tree.addAlgorithmsTreeListener( this );
	}
	
	public AlgorithmsTree getAvaiableAlgorithms( Graph graph ) throws NoSuchMethodException {
		if ( tree != null )
			throw new NoSuchMethodException( "could only initiate new Tree from basetree!");
		return new AlgorithmsTree( this, graph );
	}
	
	public AlgorithmsTree( Properties properties ) throws NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		root = new TreeNode( "root" );
		addProperties( properties );
	}
	
	public void addProperties( Properties properties ) throws NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		Enumeration propertyNames = properties.propertyNames();
		while ( propertyNames.hasMoreElements() ) {
			String propertyName = (String)propertyNames.nextElement();
			if ( propertyName.startsWith( "algorithms.") && propertyName.endsWith( ".class" ) )
				addAlgorithmProperty( properties, propertyName );
		}
		algorithmTreeUpdated();
	}
	
	public TreeNode getRoot() {
		if ( tree == null ) 
			return root;
		if ( acceptsGraph( tree.root ) )
			return tree.root;
		return null;
	}
	
	public ArrayList<TreeNode> getLeaves(  ) {
		ArrayList<TreeNode> leaves = getRoot().getLeaves();
		for ( TreeNode leave : new ArrayList<TreeNode>( leaves ) ) {
			if ( !acceptsGraph( leave ) )
				leaves.remove( leave );
		}
			
		return leaves;
	}
	
	public TreeNode getChild( TreeNode parent, int index ) {
		int i = -1;
		Iterator<TreeNode> it = parent.children.iterator();
		while ( it.hasNext() ) {
			TreeNode child = it.next();
			if ( acceptsGraph( child ) )
				i++;
			if ( i == index )
				return child;
		}
		return null;
	}
	
	public int getChildCount( TreeNode parent ) {
		int count = 0;
		Iterator<TreeNode> it = parent.children.iterator();
		while ( it.hasNext() ) {
			if ( acceptsGraph( it.next() ) )
				count++;
		}
		return count;
	}
	
	public int getIndexOfChild( TreeNode parent, TreeNode child ) {
		int index = -1;
		Iterator<TreeNode> it = parent.children.iterator();
		while ( it.hasNext() ) {
			TreeNode childh = it.next();
			if ( acceptsGraph( childh ) )
				index++;
			if ( childh == child )
				return index;
		}
		return -1;
	}
	
	public boolean isLeave( TreeNode node ) {
		return node.isLeave();
	}
	
	private boolean acceptsGraph( TreeNode node ) {
//		if ( node.isLeave() )
//			return getAlgorithm( node.getAlgorithmClass() ).accepts( graph );
//		Iterator it = node.children.iterator();
//		while ( it.hasNext() ) {
//			if ( acceptsGraph( ((TreeNode)it.next())) )
//				return true;
//		}
		return true;
	}
	
	public Algorithm getAlgorithm( Class algorithmClass ) {
		if ( algorithmClass == null )
			return null;
		if ( tree != null )
			return tree.getAlgorithm( algorithmClass );
		return classToAlgorithm.get( algorithmClass );
	}
	
	protected void addAlgorithmProperty( Properties properties, String name ) throws NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		StringTokenizer st = new StringTokenizer( name.substring( 1, name.lastIndexOf( '.' ) ), "." );
		TreeNode parent = root;
		st.nextElement();
		while ( st.hasMoreElements() ) {
			String pathElement = (String)st.nextElement();
			TreeNode next = parent.getChild( pathElement );
			if ( next == null ) {
				if ( st.hasMoreTokens() )
					next = new TreeNode( pathElement, parent );
				else {
					next = new TreeNode( pathElement, getClass().getClassLoader().loadClass( properties.getProperty( name ) ), parent );
					putAlgorithm( next.getAlgorithmClass() );
				}
				parent.addChild( next );
			}
			parent = next;
		}
	}
	
	protected void putAlgorithm( Class algorithmClass ) throws InstantiationException, IllegalAccessException {
		Algorithm algorithm = classToAlgorithm.get( algorithmClass );
		if ( algorithm == null ) {
			algorithm = (Algorithm) algorithmClass.newInstance();
			classToAlgorithm.put( algorithmClass, algorithm );
		}
	}
	
	public void algorithmTreeUpdated() {
		for ( AlgorithmsTreeListener l : listeners )
			l.algorithmTreeUpdated();
	}
	
	public void addAlgorithmsTreeListener( AlgorithmsTreeListener tree ) {
		if ( !listeners.contains( tree ) )
			listeners.add( tree );
	}
	
	public void removeAlgorithmsTreeListener( AlgorithmsTreeListener tree ) {
		listeners.remove( tree );
	}
		
	public static class TreeNode {
		private String name;
		private Class algorithmClass;
		private TreeNode parent;
		
		private ArrayList<TreeNode> children = new ArrayList<TreeNode>();
		
		private TreeNode( String name ) {
			this( name, null, null );
		}
		
		private TreeNode( String name, TreeNode parent ) {
			this( name, null, parent );
		}
		
		private TreeNode( String name, Class algorithmClass ) {
			this( name, algorithmClass, null );
		}
		
		private TreeNode( String name, Class algorithmClass, TreeNode parent ) {
			this.name = name;
			this.algorithmClass = algorithmClass;
			this.parent = parent;
		}
		
		public String getAbsoluteName() {
			if ( parent == null )
				return null;
			if ( parent.parent == null )
				return getName();
			return parent.getAbsoluteName() + "." + getName();
		}
		
		public String getName() {
			return name;
		}
		
		public Class getAlgorithmClass() {
			return algorithmClass;
		}
		
		public boolean isLeave() {
			if ( algorithmClass == null )
				return false;
			return true;
		}
		
		private void addChild( TreeNode child ) throws NoSuchMethodException {
			if ( isLeave() )
				throw new NoSuchMethodException( "you cannot add a child to a leave node." );
			children.add( child );
		}
		
		private TreeNode getChild( String name ) throws NoSuchMethodException {
			if ( isLeave() )
				throw new NoSuchMethodException( "you cannot get a Child on a leave node" );

			Iterator<TreeNode> it = children.iterator();
			while ( it.hasNext() ) {
				TreeNode child = it.next();
				if ( child.name.equals( name ) )
					return child;
			}
			return null;
		}
		
		private ArrayList<TreeNode> getLeaves() {
			ArrayList<TreeNode> leaves = new ArrayList<TreeNode>();
			if ( children.size() == 0 )
				leaves.add( this );
			else {
				Iterator<TreeNode> it = children.iterator();
				while ( it.hasNext() )
					leaves.addAll( it.next().getLeaves() );
			}
			return leaves;
		}
		
		public String toString() {
			return getAbsoluteName();
		}
	}

}
