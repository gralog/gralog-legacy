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
import java.util.StringTokenizer;

import de.hu.gralog.gui.data.Plugin.AlgorithmInfo;


public class AlgorithmsTree {
	
	protected final AlgorithmTreeNode ROOT = new AlgorithmTreeNode( "ROOT" );
	protected final ArrayList<AlgorithmInfo> algorithms;
	
	public AlgorithmsTree( ArrayList<AlgorithmInfo> algorithms ) {
		this.algorithms = algorithms;
		buildTree(  );
	}
	
	public AlgorithmTreeNode getRoot() {
		return ROOT;
	}
	
	protected void buildTree( ) {
		buildTree( ROOT, algorithms, "" );
	}
	
	protected void buildTree( AlgorithmTreeNode node, ArrayList<AlgorithmInfo> algorithms, String prefix ) {
		ArrayList<ArrayList<AlgorithmInfo>> children = getChildren( new ArrayList<AlgorithmInfo>( algorithms ), prefix );
		for ( ArrayList<AlgorithmInfo> childInfo : children ) {
			String childName = getChildName( childInfo.get( 0 ).getPath(), prefix );
			AlgorithmTreeNode child;
			if ( childInfo.size() == 1 && childInfo.get( 0 ).getPath().equals( prefix + "." + childName ) )
				child = new AlgorithmTreeNode( childInfo.get( 0 ) );
			else {
				child = new AlgorithmTreeNode( childName );
				String newPrefix = childName;
				if ( prefix != "" )
					newPrefix = prefix + "." + newPrefix;
				buildTree( child, childInfo, newPrefix );
			}
			node.addChild( child );
		}
	}
	
	protected static ArrayList<AlgorithmInfo> getAlgorithmsWithPrefix( ArrayList<AlgorithmInfo> algorithms, String prefix, boolean deleteFromAlgorithms ) {
		ArrayList<AlgorithmInfo> algorithmsWithPrefix = new ArrayList<AlgorithmInfo>();
		for ( AlgorithmInfo info : new ArrayList<AlgorithmInfo>( algorithms ) ) {
			if ( prefix == "" || info.getPath().startsWith( prefix + "." ) || info.getPath().equals( prefix ) ) {
				algorithmsWithPrefix.add( info );
				if ( deleteFromAlgorithms  )
					algorithms.remove( info );
			}
		}
		return algorithmsWithPrefix;
	}
	
	protected static ArrayList<ArrayList<AlgorithmInfo>> getChildren( ArrayList<AlgorithmInfo> algorithms, String prefix ) {
		ArrayList<ArrayList<AlgorithmInfo>> children = new ArrayList<ArrayList<AlgorithmInfo>>();
		for ( AlgorithmInfo info : new ArrayList<AlgorithmInfo>( algorithms ) ) {
			if ( !algorithms.contains( info ) )
				continue;
			String childNamePrefix = getChildName( info.getPath(), prefix );
			if ( prefix != "" )
				childNamePrefix= prefix + "." + childNamePrefix;
			children.add( getAlgorithmsWithPrefix( algorithms, childNamePrefix, true ) );
		}
		return children;
	}
	
	protected static String getChildName( String algorithmName, String parentPrefix ) {
		StringTokenizer st = new StringTokenizer( algorithmName.substring( parentPrefix.length() ), ".", false );
		if ( !st.hasMoreElements())
			System.out.println( "No child found for algorithm: " + algorithmName + " and prefix: " + parentPrefix );
		return st.nextToken();
	}
	
	public static class AlgorithmTreeNode {
		protected AlgorithmTreeNode parent;
		protected final ArrayList<AlgorithmTreeNode> children = new ArrayList<AlgorithmTreeNode>();
		protected final Object data;
		
		public AlgorithmTreeNode( Object data ) {
			this.data = data;
		}
		
		public void addChild( AlgorithmTreeNode child ) {
			child.parent = this;
			children.add( child );
		}
		
		public ArrayList<AlgorithmTreeNode> getChildren() {
			return children;
		}
		
		public Object getData() {
			return data;
		}
		
		public boolean isLeave() {
			return getChildren().size() == 0;
		}
		
		public String toString() {
			return data.toString();
		}
	}
}
