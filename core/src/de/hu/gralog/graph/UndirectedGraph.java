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

package de.hu.gralog.graph;

import java.util.ArrayList;

import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.graph.DefaultGraphCell;
import org.jgrapht.graph.DefaultEdge;

import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;
import de.hu.gralog.jgrapht.edge.DefaultListenableEdge;
import de.hu.gralog.jgrapht.graph.GraphPropertyListenableUndirectedGraph;
import de.hu.gralog.jgrapht.graph.GraphType;
import de.hu.gralog.jgrapht.vertex.DefaultListenableVertex;

/**
 * This class is a template for undirected graphs in GrALoG.
 * Please refer to the documentation of {@link DirectedGraph}.
 * 
 * @author ordyniak
 *
 */
public class UndirectedGraph<V extends DefaultListenableVertex, E extends DefaultEdge> extends GraphPropertyListenableUndirectedGraph<V, E>  implements GraphWithEditableElements<V, E> {
	private transient ArrayList<SelectionListener> selectionListeners = new ArrayList<SelectionListener>();
	
	private Class<? extends V> vertexClass;
	
	private transient GraphTypeInfo typeInfo;
	private transient DefaultEdgeRenderer EDGE_RENDERER = null;
	private transient DefaultVertexRenderer VERTEX_RENDERER = null;
	
	public UndirectedGraph( Class<? extends V> vertexClass )  {
		this( GraphType.SIMPLE_GRAPH, vertexClass );
	}
	
	public UndirectedGraph( Class<? extends V> vertexClass, Class<? extends E> edgeClass ) {
		this( GraphType.SIMPLE_GRAPH, vertexClass, edgeClass );
	}
	
	public UndirectedGraph( GraphType graphType, Class<? extends V> vertexClass )  {
		super( graphType );
		this.vertexClass = vertexClass;
	}
	
	public UndirectedGraph( GraphType graphType, Class<? extends V> vertexClass, Class<? extends E> edgeClass ) {
		super( graphType, edgeClass );
		this.vertexClass = vertexClass;
	}
	
	public void setTypeInfo( GraphTypeInfo typeInfo ) {
		this.typeInfo = typeInfo;
	}
	
	public GraphTypeInfo getTypeInfo() {
		return typeInfo;
	}
	
	public V createVertex() {
		V vertex = null;
		try {
			vertex = vertexClass.newInstance();
		} catch (InstantiationException e) {
			
		} catch (IllegalAccessException e) {
			
		}
		return vertex;
	}

	public DefaultEdgeRenderer getEdgeRenderer() {
		if ( EDGE_RENDERER == null ) {
			E edge = getEdgeFactory().createEdge( createVertex(), createVertex() );
			if ( edge instanceof DefaultListenableEdge )
				EDGE_RENDERER = ((DefaultListenableEdge)edge).getRenderer();
			else
				EDGE_RENDERER = new DefaultEdgeRenderer();
		}
		return EDGE_RENDERER;
	}

	public DefaultVertexRenderer getVertexRenderer() {
		if ( VERTEX_RENDERER == null )
			VERTEX_RENDERER = createVertex().getRenderer();
		return VERTEX_RENDERER;
	}

	public boolean isGraphEditable() {
		return false;
	}

	public boolean isVertexEditable() {
		return true;
	}

	public boolean isEdgeEditable() {
		if ( getEdgeFactory().createEdge( createVertex(), createVertex() ) instanceof DefaultListenableEdge )
			return true;
		return false;
	}
	
	public void addSelectionListener(SelectionListener<V, E> listener) {
		if ( ! selectionListeners.contains( listener ) )
			selectionListeners.add( listener );
	}

	public void removeSelectionListener(SelectionListener<V, E> listener) {
		selectionListeners.remove( listener );
	}

	private void fireSelectionChangedEvent( ArrayList<V> vertexes, ArrayList<E> edges, GraphSelectionEvent event ) {
		for ( SelectionListener<V,E> listener : selectionListeners )
			listener.valueChanged( vertexes, edges, event );
	}
	
	public void valueChanged(GraphSelectionEvent event) {
		ArrayList<V> vertexes = new ArrayList<V>();
		ArrayList<E> edges = new ArrayList<E>();
		
		for ( Object objectCell : event.getCells() ) {
			if ( objectCell instanceof DefaultGraphCell && event.isAddedCell( objectCell ) ) {
				DefaultGraphCell cell = (DefaultGraphCell)objectCell;
				Object userObject = cell.getUserObject();
				
				if ( userObject instanceof DefaultEdge )
					edges.add( (E)userObject );
				else
					vertexes.add( (V)userObject );
			}
		}
		fireSelectionChangedEvent( vertexes, edges, event );
	}
}
