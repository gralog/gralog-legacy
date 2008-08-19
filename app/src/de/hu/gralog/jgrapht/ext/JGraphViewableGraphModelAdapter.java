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

package de.hu.gralog.jgrapht.ext;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.beans.IndexedPropertyChangeEvent;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.undo.UndoableEdit;

import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.ParentMap;
import org.jgrapht.ext.JGraphModelAdapter;

import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.ElementAttributes;
import de.hu.gralog.graph.ViewableGraph;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.jgrapht.GraphPropertyListenableGraph;
import de.hu.gralog.jgrapht.ListenableElement;
import de.hu.gralog.jgrapht.event.GraphPropertyListener;

/**
 * 
 * This class should be used as JGraphModel in order to display a {@link ViewableGraph ViewableGraph}.
 * It extends the functionality of {@link JGraphModelAdapter JGraphModelAdapter} for an ElementListenableGraph in order
 * to forward Elementchanges to JGraph.  
 * 
 * @author Sebastian
 *
 */
public class JGraphViewableGraphModelAdapter<V,E> extends JGraphModelAdapter<V,E> {

	private boolean undoDisabled = true;
	
	public static final Object NULL_VALUE = new Object();
	
	private GraphPropertyListener graphPropertyListener;
	
	private final GraphPropertyListenableGraph graph;
	
	public JGraphViewableGraphModelAdapter(GraphPropertyListenableGraph jGraphTGraph) {
		this( jGraphTGraph, null );
	}
	
	public JGraphViewableGraphModelAdapter( GraphPropertyListenableGraph jGraphTGraph, Hashtable<V,Point> vertexPositions ) {
		super(jGraphTGraph, ElementAttributes.getVertexAttributes(  ), ElementAttributes.getEdgeAttributes( jGraphTGraph ), new MyCellFactory<V,E>( vertexPositions ) );
		this.graph = jGraphTGraph;

		graphPropertyListener = new MyGraphPropertyListener();
		jGraphTGraph.addGraphPropertyListener( graphPropertyListener );
		undoDisabled = false;
	}

    
    private class MyGraphPropertyListener implements GraphPropertyListener {

		public void propertyChanged(Object graphSource, PropertyChangeEvent e) {
			Object oldValue = e.getOldValue();
			if ( e instanceof IndexedPropertyChangeEvent )
				oldValue = new IndexedPropertyValue( ((IndexedPropertyChangeEvent)e).getIndex(), e.getOldValue() );
			else {
				if ( oldValue == null )
					oldValue = NULL_VALUE;
			}
			
			AttributeMap property = new AttributeMap();
			property.put( e.getPropertyName(), oldValue );
			
			AttributeMap object = new AttributeMap();
			object.put( e.getSource(), property );
			
			AttributeMap value = new AttributeMap();
			value.put( GraphConstants.VALUE, object );
			
			AttributeMap attributes = new AttributeMap();
			if ( graphSource != graph ) {
				if ( graph.containsVertex( graphSource ) )
					attributes.put( getVertexCell( graphSource ), value );
				else
					attributes.put( getEdgeCell( (E)graphSource ), value );
			} else
				attributes.put( graph, value );
			GraphModelEdit edit = createEdit( null, null, attributes, null, null, null );
			postEdit( edit );
			fireGraphChanged(JGraphViewableGraphModelAdapter.this, edit );
		}

		public void propertyChanged(Object graphSource, PropertyChangeEvent e, Object[] elementsToUpdateView) {
			propertyChanged( graphSource, e );
			if ( elementsToUpdateView != null )
				cellsChanged( getCellsForUserObjects( elementsToUpdateView ));
		}
    	
    }
    
    public Object[] getCellsForUserObjects( Object[] userObjects ) {
    	ArrayList cells = new ArrayList();
    	
    	for ( Object userObject : userObjects ) {
    		Object cell = getVertexCell( userObject );
    		if ( cell == null )
    			cell = getEdgeCell( (E)userObject );
    		if ( cell != null )
    			cells.add( cell );
    	}
    	
    	return cells.toArray();
    }
    
    public Object[] getEdgeCells() {
    	Iterator it = getCellIterator();
    	Vector edges = new Vector();
    	
    	while ( it.hasNext() ) {
    		Object cell = it.next();
    		if ( isEdge( cell ) )
    			edges.add( cell );
    	}
    	
    	return edges.toArray();
    }
    
    public Object[] getVertexCells() {
    	Iterator it = getCellIterator();
    	
    	Vector vertices = new Vector();
    	while ( it.hasNext() ) {
    		Object cell = it.next();
    		if ( isVertex( this, cell ) )
    			vertices.add( cell );
    	}
    	
    	return vertices.toArray();
    }
    
    public Iterator getCellIterator() {
    	return new CellIterator();
    }
    
    public DefaultGraphCell getGraphCell( Object userObject ) {
    	if ( userObject instanceof DefaultEdge )
    		return this.getEdgeCell( (E)userObject );
    	return getVertexCell( userObject );
    }

    private class CellIterator implements Iterator {

    	private Vector queue;
    	
    	public CellIterator() {
    		queue = new Vector();
    		for (int i = 0; i < getRootCount(); i++ )
    			queue.add( getRootAt( i ) );
    	}
    	
		public boolean hasNext() {
			return queue.size() != 0;
		}

		public Object next() {
			Object cell = queue.remove( 0 );
			for (int i = 0; i < getChildCount( cell ); i++)
				queue.add( getChild( cell, i ) );
			return cell;
		}

		public void remove() {
		}
    	
    }
    
    /**
     * A simple default cell factory.
     *
     * @author Barak Naveh
     *
     * @since Dec 12, 2003
     */
    public static class MyCellFactory<VV,EE> implements CellFactory<VV,EE>, Serializable {
    
    	private Hashtable<VV, Point> vertexPositions;
    	
    	public MyCellFactory( Hashtable<VV, Point> vertexPositions ) {
    		this.vertexPositions = vertexPositions;
    	}
    	
    	public void clearVertexPositions() {
    		vertexPositions = null;
    	}
    	
        /**
         * @see org._3pq.jgrapht.ext.JGraphModelAdapter.CellFactory#createVertexCell(Object)
         */
        public DefaultGraphCell createVertexCell( VV jGraphTVertex ) {
        	AttributeMap cellAttributes = new AttributeMap();
        	Rectangle2D.Double bounds = new Rectangle2D.Double( 50, 50, 40, 40 );
        	if ( vertexPositions != null ) {
        		Point position = vertexPositions.get( jGraphTVertex );
        		if ( position != null )
        			bounds.setRect( position.getX(), position.getY(), bounds.getWidth(), bounds.getHeight() );
        		else
        			clearVertexPositions();
        	}
        	GraphConstants.setBounds( cellAttributes, bounds );
            return new DefaultGraphCell( jGraphTVertex, cellAttributes );
        }


		public DefaultEdge createEdgeCell(EE jGraphTEdge) {
			return new DefaultEdge( jGraphTEdge );
		}
    }

/*	protected Map handleAttributes(Map attributes) {
		if (attributes != null) {
			Hashtable undo = new Hashtable(attributes.size());
			Iterator it = attributes.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				
				Object cell = entry.getKey();
				Map deltaNew = (Map) entry.getValue();
				
				Map deltaOld = null;
				AttributeMap attr = getAttributes(cell);
				if (attr != null) {
					deltaOld = attr.applyMap(deltaNew);
					if ( deltaOld == AttributeMap.emptyAttributeMap )
						deltaOld = new AttributeMap();
				} else {
					// Make room for the value
					deltaOld = new Hashtable(2);
				}
				undo.put(cell, deltaOld);
				// Handle new values
				Object newValue = deltaNew.get(GraphConstants.VALUE);
				if (newValue != null) {
					Object oldValue = valueForCellChanged(cell, newValue);
					if (oldValue != null)
						GraphConstants.setValue(deltaOld, oldValue);
					// TODO: Userobject of null is probably invalid
					else
						GraphConstants.setRemoveAttributes(deltaOld,
								new Object[] { GraphConstants.VALUE });
				}
			}
			return undo;
		}
		return null;
	}
*/   
    
    
    /**
	 * Extends DefaultGraphModel insert method to check for large changes and
	 * sets the <code>undoDisabled</code> flag to true. Disabling the undo
	 * provides large performance and memory footprint advantages on huge
	 * inserts.
	 */
	public void insert(Object[] roots, Map attributes, ConnectionSet cs,
			ParentMap pm, UndoableEdit[] edits) {
		GraphModelEdit edit = createEdit(roots, null, attributes, cs, pm, edits);
		if (edit != null) {
			edit.execute(); // fires graphChangeEvent
			if (edits != null) {
				for (int i = 0; i < edits.length; i++)
					if (edits[i] instanceof GraphLayoutCache.GraphLayoutCacheEdit)
						((GraphLayoutCache.GraphLayoutCacheEdit) edits[i])
								.execute();
			}
			if (!undoDisabled) {
				postEdit(edit); // fires undoableedithappened
			}
		}
	}

	/**
	 * Applies <code>attributes</code> to the cells specified as keys. Returns
	 * the <code>attributes</code> to undo the change.
	 */
	protected Map handleAttributes(Map attributes) {
		if ( !undoDisabled )
			return super.handleAttributes( attributes );
		if (attributes != null) {
			Iterator it = attributes.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				Object cell = entry.getKey();
				Map deltaNew = (Map) entry.getValue();
				AttributeMap attr = getAttributes(cell);
				
				if (attr != null) {
					attr.applyMap(deltaNew);
				}
				// Handle new values
				Object newValue = deltaNew.get(GraphConstants.VALUE);
				if (newValue != null) {
					valueForCellChanged(cell, newValue);
				}
			}
		}
		return null; 
	}

    @Override
	public Object valueForCellChanged(Object cell, Object newValue) {
    	if ( undoDisabled ) {
    		try {
    			AttributeMap valueMap = (AttributeMap)newValue;
    			for ( Object bean : valueMap.keySet() ) {
    				AttributeMap propertyMap = (AttributeMap)valueMap.get( bean );
    				Hashtable<String, PropertyDescriptor> propertyDescriptors = new Hashtable<String, PropertyDescriptor>();
    				for ( PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo( bean.getClass() ).getPropertyDescriptors() )
    					propertyDescriptors.put( propertyDescriptor.getName(), propertyDescriptor );
    				
    				for ( String propertyName : (Set<String>)propertyMap.keySet() ) {
    					if ( propertyDescriptors.get( propertyName ) instanceof IndexedPropertyDescriptor ) {
    						IndexedPropertyDescriptor descriptor = (IndexedPropertyDescriptor)propertyDescriptors.get( propertyName );
    						IndexedPropertyValue propertyValue = (IndexedPropertyValue)propertyMap.get( propertyName );
    						
    						graph.removeGraphPropertyListener( graphPropertyListener );
    						
    						descriptor.getIndexedWriteMethod().invoke( bean, new Object[] { new Integer( propertyValue.index ), propertyValue.value } );
    						
    						graph.addGraphPropertyListener( graphPropertyListener );
    					} else {
    						PropertyDescriptor descriptor = propertyDescriptors.get( propertyName );
    						Object propertyValue = propertyMap.get( propertyName );
    						
    						if ( propertyValue == NULL_VALUE )
    							propertyValue = null;
    						
    						graph.removeGraphPropertyListener( graphPropertyListener );
    						
    						descriptor.getWriteMethod().invoke( bean, new Object[] { propertyValue } );
    						
    						graph.addGraphPropertyListener( graphPropertyListener );
    					}
    				}
    			}
    		} catch (IntrospectionException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IllegalArgumentException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IllegalAccessException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (InvocationTargetException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		return null;
    	}
    	
    	AttributeMap undo = new AttributeMap();
    	
    	try {
    		AttributeMap valueMap = (AttributeMap)newValue;
    		for ( Object bean : valueMap.keySet() ) {
    			AttributeMap undoPropertyMap = new AttributeMap();
    			undo.put( bean, undoPropertyMap );
    			
    			AttributeMap propertyMap = (AttributeMap)valueMap.get( bean );
    			Hashtable<String, PropertyDescriptor> propertyDescriptors = new Hashtable<String, PropertyDescriptor>();
    			for ( PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo( bean.getClass() ).getPropertyDescriptors() )
    				propertyDescriptors.put( propertyDescriptor.getName(), propertyDescriptor );
    			
    			for ( String propertyName : (Set<String>)propertyMap.keySet() ) {
    				if ( propertyDescriptors.get( propertyName ) instanceof IndexedPropertyDescriptor ) {
    					IndexedPropertyDescriptor descriptor = (IndexedPropertyDescriptor)propertyDescriptors.get( propertyName );
    					IndexedPropertyValue propertyValue = (IndexedPropertyValue)propertyMap.get( propertyName );
    					
    					Object oldValue = descriptor.getIndexedReadMethod().invoke( bean, new Object[] { new Integer( propertyValue.index ) } );
    					undoPropertyMap.put( propertyName, new IndexedPropertyValue( propertyValue.index, oldValue ) );
    					
    					graph.removeGraphPropertyListener( graphPropertyListener );
    					
    					descriptor.getIndexedWriteMethod().invoke( bean, new Object[] { new Integer( propertyValue.index ), propertyValue.value } );
    					
    					graph.addGraphPropertyListener( graphPropertyListener );
    				} else {
    					PropertyDescriptor descriptor = propertyDescriptors.get( propertyName );
    					Object propertyValue = propertyMap.get( propertyName );
    					
    					if ( propertyValue == NULL_VALUE )
    						propertyValue = null;
    					
    					Object oldValue = descriptor.getReadMethod().invoke( bean, new Object[] {} );
    					if ( oldValue == null )
    						oldValue = NULL_VALUE;
    					undoPropertyMap.put( propertyName, oldValue );
    					
    					graph.removeGraphPropertyListener( graphPropertyListener );
    					
    					descriptor.getWriteMethod().invoke( bean, new Object[] { propertyValue } );
    					
    					graph.addGraphPropertyListener( graphPropertyListener );
    				}
    			}
    		}
    	} catch (IntrospectionException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (IllegalArgumentException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (IllegalAccessException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (InvocationTargetException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	return undo;
    }
    
    	
/*    	if ( newValue instanceof ElementChangeEvent ) {
    		ElementChangeEvent e = (ElementChangeEvent)newValue;
    		undo.put( e.getAttribute(), e.getOldValue() );
    		return undo;
    	}
    	
    	Object userObject = ((DefaultGraphCell)cell).getUserObject();
    	
		PropertyDescriptor[] objectProperties = null;
		try {
			objectProperties = Introspector.getBeanInfo( userObject.getClass() ).getPropertyDescriptors();
		} catch (IntrospectionException e) {
			MainPad.getInstance().handleUserException( new UserException("could not introspect userobject", e ) );
		}
		
		Hashtable<String, PropertyDescriptor> properties = new Hashtable<String, PropertyDescriptor>();
		for (int i = 0; i < objectProperties.length; i++)
			properties.put( objectProperties[i].getName(), objectProperties[i] );

    	Iterator attrs = ((AttributeMap)newValue).entrySet().iterator();
    	while ( attrs.hasNext() ) {
    		Map.Entry attr = (Map.Entry)attrs.next();
    		try {
				Object oldValue = properties.get( attr.getKey() ).getReadMethod().invoke( userObject, new Object[] {} );
				undo.put( attr.getKey(), oldValue );
				if ( userObject instanceof ListenableElement )
					((ListenableElement)userObject).disableListeningForNextChange();
				properties.get( attr.getKey() ).getWriteMethod().invoke( userObject, new Object[] { attr.getValue() } );
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
    	}
    	return undo;
*/	

	/**
	 * Removes <code>cells</code> from the model. Notifies the model- and undo
	 * listeners of the change.
	 * 
	 * the difference to DefaultGraphModel.remove( Object[] roots ) is that this implementation
	 * also deletes all edges which are connected to roots. 
	 */
	public void remove(Object[] roots) {
		Set edgesAndRoots = DefaultGraphModel.getEdges( this, roots );
		for ( Object root : roots )
			edgesAndRoots.add( root );
		GraphModelEdit edit = createRemoveEdit( edgesAndRoots.toArray() );
		if (edit != null) {
			edit.execute();
			postEdit(edit);
		}
	}
    
	@Override
	protected Object cloneUserObject(Object userObject) {
		if ( userObject instanceof ListenableElement ) {
			
			try {
				Object clone = ((ListenableElement)userObject).cloneMe();
				return clone;
			} catch (UserException e) {
				MainPad.getInstance().handleUserException( e );
			}
			
		}
		return super.cloneUserObject( userObject );
	}
	
	private static class IndexedPropertyValue {
		public int index;
		public Object value;
		
		public IndexedPropertyValue( int index, Object value ) {
			this.index = index;
			this.value = value;
		}
	}
	
	public Hashtable<V, Point> getVertexPositions() {
		Hashtable<V, Point> vertexPositions = new Hashtable<V, Point>();
		
		for ( Object cell : getVertexCells() ) {
			V userObject = (V)getValue( cell );
			Rectangle2D bounds = GraphConstants.getBounds( ((DefaultGraphCell)cell).getAttributes() );
			vertexPositions.put( userObject, new Point( (int)bounds.getX(), (int)bounds.getY() ) );
		}
		return vertexPositions;
	}
	
	/*
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	protected void fireGraphChanged(Object source,
			GraphModelEvent.GraphModelChange edit) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		GraphModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == GraphModelListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new GraphModelEvent(source, edit);
				GraphModelListener listener = ((GraphModelListener) listeners[i + 1]);
				
				listener.graphChanged(e);
		
			}
		}
	}

	protected GraphModelEdit createEdit(Object[] inserted, Object[] removed,
			Map attributes, ConnectionSet cs, ParentMap pm, UndoableEdit[] edits) {
		GraphModelEdit edit = new GraphModelEdit(inserted, removed, attributes,
				cs, pm);
		if (edit != null) {
			if (edits != null)
				for (int i = 0; i < edits.length; i++)
					edit.addEdit(edits[i]);
			edit.end();
		}
		return edit;
	}
	
	protected GraphModelEdit createRemoveEdit(Object[] cells) {
		// Remove from GraphStructure
		ConnectionSet cs = ConnectionSet.create(this, cells, true);
		// Remove from Group Structure
		ParentMap pm = ParentMap.create(this, cells, true, false);
		// Construct Edit
		GraphModelEdit edit = createEdit(null, cells, null, cs, pm, null);
		if (edit != null)
			edit.end();
		return edit;
	}
	
	public class GraphModelEdit extends DefaultGraphModel.GraphModelEdit {

		private HashMap<GraphLayoutCache, Rectangle2D> dirtyRegionCache = new HashMap<GraphLayoutCache, Rectangle2D>();
		
		public GraphModelEdit(Object[] inserted, Object[] removed, Map attributes, ConnectionSet connectionSet, ParentMap parentMap) {
			super(inserted, removed, attributes, connectionSet, parentMap);
		}
		
		public void putDirtyRegion( GraphLayoutCache cache, Rectangle2D dirtyRegion ) {
			dirtyRegionCache.put( cache, dirtyRegion );
		}
		
		public Rectangle2D getDirtyRegion( GraphLayoutCache cache ) {
			return dirtyRegionCache.get( cache );
		}
	}
	
}
