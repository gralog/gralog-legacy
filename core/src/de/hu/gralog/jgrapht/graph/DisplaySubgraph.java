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

package de.hu.gralog.jgrapht.graph;

import java.awt.Color;
import java.io.Serializable;
import java.util.Vector;


import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Subgraph;



/**
 * 
 * @author ordyniak
 *
 */
public class DisplaySubgraph implements DisplaySubgraphModeListener, Serializable {

	protected transient Vector listeners = new Vector();
	
	public static class DisplayMode implements Serializable {
		
		public static final DisplayMode SHOW = new DisplayMode( "SHOW" );
		public static final DisplayMode HIDE = new DisplayMode( "HIDE" );
		public static final DisplayMode HIGH1 = new DisplayMode( "HIGH1", Color.GREEN );
		public static final DisplayMode HIGH2 = new DisplayMode( "HIGH2", Color.RED );
		private static final DisplayMode[] ORDER = { HIGH2, HIGH1, SHOW, HIDE };
		
		private String name;
		private Color color;
		
		protected DisplayMode() {
			
		}
		
		private DisplayMode( String name ) {
			this( name, null );
		}
		
		private DisplayMode( String name, Color color ) {
			this.name = name;
			this.color = color;
		}
		
		private int getIndex( ) {
			for ( int i = 0; i < ORDER.length; i++ ) {
				if ( ORDER[i] == this )
					return i;
			}
			return -1;	
		}
		
		public boolean overwrites( DisplayMode displayMode ) {
			if ( getIndex() < displayMode.getIndex() )
				return true;
			return false;
		}
		
		public static DisplayMode[] getDisplayModes() {
			return ORDER;
		}
		
		public String toString() {
			return name;
		}
		
		public static DisplayMode parseString( String name ) {
			for ( DisplayMode mode : ORDER ) {
				if ( mode.name.equalsIgnoreCase( name ) )
					return mode;
			}
			return null;
		}
		
		public Color getColor() {
			return color;
		}
		
		private Object readResolve() {
			if ( name.equals( "SHOW" ) )
				return SHOW;
			if ( name.equals( "HIDE" ) )
				return HIDE;
			if ( name.equals( "HIGH1" ) )
				return HIGH1;
			return HIGH2;
		}
	}
	
	protected DisplaySubgraphMode mode;
	protected Subgraph subgraph; 
	
	protected DisplaySubgraph() {
		
	}
	
	public DisplaySubgraph( DisplaySubgraphMode mode, Subgraph subgraph ) {
		this.mode = mode;
		this.subgraph = subgraph;
		mode.addListener( this );
	}
	
	public Subgraph getSubgraph() {
		return subgraph;
	}
	
	public DisplaySubgraphMode getMode() {
		return mode;
	}
	
	public DisplayMode getDisplayMode( Object userObject ) {
		if ( userObject instanceof DefaultEdge ) {
			if ( getMode().getEdgeDisplayMode( false ) == getMode().getEdgeDisplayMode( true ) )
				return getMode().getEdgeDisplayMode( false );
			if ( subgraph.containsEdge( (DefaultEdge)userObject ) )
				return getMode().getEdgeDisplayMode( false );
			return getMode().getEdgeDisplayMode( true );
		}
		if ( getMode().getVertexDisplayMode( false ) == getMode().getVertexDisplayMode( true ) )
			return getMode().getVertexDisplayMode( false );
		if ( subgraph.containsVertex( userObject ) ) 
			return getMode().getVertexDisplayMode( false );
		return getMode().getVertexDisplayMode( true );
	}

	protected void fireSubGraphChanged() {
		for (int i = 0; i < listeners.size();  i++ )
			((DisplaySubgraphListener)listeners.get( i )).displayUpdated( this );
	}
	
	public void addDisplaySubgraphListener(DisplaySubgraphListener l) {
		if ( ! listeners.contains( l ) )
			listeners.add( l );
	}

	public void removeDisplaySubgraphListener(DisplaySubgraphListener l) {
		listeners.remove( l );
	}

	public void displaySubgraphModeChanged() {
		fireSubGraphChanged();
	}
	
}
