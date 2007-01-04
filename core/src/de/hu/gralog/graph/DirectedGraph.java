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


import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphSelectionModel;
import org.jgrapht.graph.DefaultEdge;

import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;
import de.hu.gralog.jgrapht.edge.DefaultListenableEdge;
import de.hu.gralog.jgrapht.graph.GraphPropertyListenableDirectedGraph;
import de.hu.gralog.jgrapht.graph.GraphType;
import de.hu.gralog.jgrapht.vertex.DefaultListenableVertex;

/**
 * This class is a template for directed graphs in GrALoG. Since it is
 * typed you can specify vertex and edge-classes at construction time. It
 * is also possible to choose among three different flavours of directed graph
 * at construction time namely:
 * 
 * <p>
 * 	<ul>
 * 		<li>SIMPLE_GRAPH - this is a graph without multiple edges or loops</li>
 * 		<li>MULTI_GRAPH - this is a graph without loops but with multiple edges</li>
 * 		<li>PSEUDO_GRAPH - this is a graph with both loops and multiple edges</li>
 * 	</ul>
 * <p>
 * 
 * The only reason to subclass this class is that you need to store some information
 * with your graph - which should not be stored with each vertex or edge. In this case
 * you have to override {@link #isGraphEditable()} to return true. If {@link #isGraphEditable()}
 * returns true GrALoG looks for all properties defined by your graph ( which follow the
 * JavaBean-Specification ) and makes them editable. Here is a simple example for this:
 * <p>
 * <pre>
 * public class MyDirectedGraph extends DirectedGraph<MyVertexClass, MyEdgeClass> {
 * 
 * 		private int counter;		
 * 
 * 		public MyDirectedGraph() {
 * 		}
 * 
 * 		public int getCounter() {
 * 			return counter;
 * 		}
 * 
 * 		public void setCounter( int counter ) {
 * 			int oldValue = this.counter;
 * 			this.counter = counter;
 * 			fireGraphPropertyChange( this, new PropertyChangeEvent( this, "counter", oldValue, counter );
 * 		}
 * 
 * 		public boolean isGraphEditable() {
 * 			return true;
 * 		}
 * }
 * </pre>
 * <p>
 * Notice that in order to allow GrALoG to listen to changes made to your graph you have to
 * fire a {@link java.beans.PropertyChangeEvent} each time a property is changed 
 * ( you should use {@link #fireGraphPropertyChange(Object, PropertyChangeEvent)} to do this ).
 * Note that you also have to define a BeanInfo for your graph - that has to be a class with the
 * same name as your graph suffixed with BeanInfo which should look like this:
 * <p>
 * <pre>
 * public class MyDirectedGraphBeanInfo extends SimpleBeanInfo {
 *	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[1];
 * 	
 * 	static {
 * 		try {
 * 			PROPERTY_DESCRIPTORS[0] = new PropertyDescriptor( "counter", MyDirectedGraph.class );
 * 		} catch (IntrospectionException e) {
 * 			// TODO Auto-generated catch block
 * 			e.printStackTrace();
 * 		}
 * 	}
 *
 * 	public PropertyDescriptor[] getPropertyDescriptors() {
 * 		return PROPERTY_DESCRIPTORS;
 * 	}
 * }
 * </pre>
 * <p>
 * Note that you can use the PropertyDescriptor to define your own editor for your properties you 
 * can also define a customizer for the whole graph ( please refer to <a href="{@docRoot}/../beans.101.pdf">JavaBean-Spec</a>
 * if you do not know what a customizer is ). If you want to define properties which are indexed please
 * refer to {@link NestedGraphPropertyBean}.
 * <p>
 * All graphs in GrALoG are jgrapht-classes please refer to <a target="_blank" href="{@docRoot}/../jgrapht/index.html">JGraphT-Api</a> for
 * information how to use these graphs. There are also some extensions to jgrapht in {@link de.hu.gralog.jgrapht.graph} and
 * {@link de.hu.gralog.jgrapht.traverse}.
 * 
 * @author ordyniak
 *
 */
public class DirectedGraph<V extends DefaultListenableVertex, E extends DefaultEdge> extends GraphPropertyListenableDirectedGraph<V, E>  implements GraphWithEditableElements<V, E> {
	private transient ArrayList<SelectionListener> selectionListeners = new ArrayList<SelectionListener>();
	
	private Class<? extends V> vertexClass;
	
	private transient GraphTypeInfo typeInfo;
	private transient DefaultEdgeRenderer EDGE_RENDERER = null;
	private transient DefaultVertexRenderer VERTEX_RENDERER = null;
	
	/**
	 * Constructs a DirectedGraph with the given vertexClass and {@link org.jgrapht.graph.DefaultEdge} as
	 * edgeclass. The flavour of this graph is set to SIMPLE_GRAPH. 
	 * 
	 * @param vertexClass
	 */
	public DirectedGraph( Class<? extends V> vertexClass )  {
		this( GraphType.SIMPLE_GRAPH, vertexClass );
	}
	
	/**
	 * Constructs a DirectedGraph with the given vertexClass and edgeclass. 
	 * The flavour of this graph is set to SIMPLE_GRAPH. 
	 * 
	 * @param vertexClass
	 * @param edgeClass
	 */
	public DirectedGraph( Class<? extends V> vertexClass, Class<? extends E> edgeClass ) {
		this( GraphType.SIMPLE_GRAPH, vertexClass, edgeClass );
	}
	
	/**
	 * Constructs a DirectedGraph with the given flavour and vertexClass. The edgeClass
	 * is set to {@link org.jgrapht.graph.DefaultEdge}.
	 * 
	 * @param graphType the flavour of this graph {@link GraphType}
	 * @param vertexClass
	 */
	public DirectedGraph( GraphType graphType, Class<? extends V> vertexClass ) {
		super( graphType );
		this.vertexClass = vertexClass;
	}
	
	/**
	 * Constructs a directed graph with the given flavour, vertex- and edgeclass.
	 * 
	 * @param graphType the flavour of this graph {@link GraphType}
	 * @param vertexClass
	 * @param edgeClass
	 */
	public DirectedGraph( GraphType graphType, Class<? extends V> vertexClass, Class<? extends E> edgeClass ) {
		super( graphType, edgeClass );
		this.vertexClass = vertexClass;
	}
	
	/**
	 * @see GraphWithEditableElements#setTypeInfo(GraphTypeInfo)
	 */
	public void setTypeInfo( GraphTypeInfo typeInfo ) {
		this.typeInfo = typeInfo;
	}
	
	/**
	 * @see GraphWithEditableElements#getTypeInfo()
	 */
	public GraphTypeInfo getTypeInfo() {
		return typeInfo;
	}
	
	/**
	 * @see GraphWithEditableElements#createVertex()
	 */
	public V createVertex() {
		V vertex = null;
		try {
			vertex = vertexClass.newInstance();
		} catch (InstantiationException e) {
			
		} catch (IllegalAccessException e) {
			
		}
		return vertex;
	}

	/**
	 * @return the edgeRenderer for that graph this is usually the renderer defined edgeclass
	 */
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

	/**
	 * @return the vertexRenderer of this graph this is usually the renderer defined by the vertexClass
	 */
	public DefaultVertexRenderer getVertexRenderer() {
		if ( VERTEX_RENDERER == null )
			VERTEX_RENDERER = createVertex().getRenderer();
		return VERTEX_RENDERER;
	}

	/**
	 * This function has to be overriden if you want to make your graph editable
	 * 
	 * @return true if the graph should be editable
	 */
	public boolean isGraphEditable() {
		return false;
	}

	/**
	 * @return true if the vertices of this graph are editable
	 */
	public boolean isVertexEditable() {
		return true;
	}

	/**
	 * @return true if the edges of this graph are editable
	 */
	public boolean isEdgeEditable() {
		if ( getEdgeFactory().createEdge( createVertex(), createVertex() ) instanceof DefaultListenableEdge )
			return true;
		return false;
	}

	/**
	 * Adds a SelectionListener to this graph. The listener is called whenever the user changes
	 * the selection in any JGraph containing this graph
	 * 
	 * @param listener
	 */
	public void addSelectionListener(SelectionListener<V, E> listener) {
		if ( ! selectionListeners.contains( listener ) )
			selectionListeners.add( listener );
	}

	/**
	 * remove a selectionListener 
	 * 
	 * @param listener
	 */
	public void removeSelectionListener(SelectionListener<V, E> listener) {
		selectionListeners.remove( listener );
	}

	/**
	 * 
	 * @param vertexes
	 * @param edges
	 * @param event
	 */
	private void fireSelectionChangedEvent( ArrayList<V> vertexes, ArrayList<E> edges, GraphSelectionEvent event ) {
		for ( SelectionListener<V,E> listener : selectionListeners )
			listener.valueChanged( vertexes, edges, event );
	}
	
	/**
	 * This function should only be called by JGraph when the selection has changed.
	 * 
	 * @param event
	 */
	public void valueChanged(GraphSelectionEvent event) {
		ArrayList<V> vertexes = new ArrayList<V>();
		ArrayList<E> edges = new ArrayList<E>();
		
		for ( Object objectCell : ((GraphSelectionModel)event.getSource()).getSelectionCells() ) {
			if ( objectCell instanceof DefaultGraphCell ) {
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
