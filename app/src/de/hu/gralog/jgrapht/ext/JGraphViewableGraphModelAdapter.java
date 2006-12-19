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

import java.beans.IndexedPropertyChangeEvent;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
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

	private GraphPropertyListener graphPropertyListener;
	
	protected DefaultCellFactory cellFactory = new DefaultCellFactory();
	private final GraphPropertyListenableGraph graph;
	
	public JGraphViewableGraphModelAdapter(GraphPropertyListenableGraph jGraphTGraph) {
		super(jGraphTGraph, ElementAttributes.getVertexAttributes(  ), ElementAttributes.getEdgeAttributes( jGraphTGraph ), new MyCellFactory() );
		this.graph = jGraphTGraph;

		graphPropertyListener = new MyGraphPropertyListener();
		jGraphTGraph.addGraphPropertyListener( graphPropertyListener );
		
	}
    
    private class MyGraphPropertyListener implements GraphPropertyListener {

		public void propertyChanged(Object graphSource, PropertyChangeEvent e) {
			Object oldValue = e.getOldValue();
			if ( e instanceof IndexedPropertyChangeEvent )
				oldValue = new IndexedPropertyValue( ((IndexedPropertyChangeEvent)e).getIndex(), e.getOldValue() );
			
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
    	
    }
    
    public DefaultCellFactory getCellFactory() {
    	return cellFactory;
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
    		if ( !isPort( cell ) && !isEdge( cell ) )
    			vertices.add( cell );
    	}
    	return vertices.toArray();
    }
    
    public Iterator getCellIterator() {
    	return new CellIterator();
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
        
        /**
         * @see org._3pq.jgrapht.ext.JGraphModelAdapter.CellFactory#createVertexCell(Object)
         */
        public DefaultGraphCell createVertexCell( VV jGraphTVertex ) {
            return new DefaultGraphCell( jGraphTVertex, new AttributeMap() );
        }


		public DefaultEdge createEdgeCell(EE jGraphTEdge) {
			return new DefaultEdge( jGraphTEdge );
		}
    }

	protected Map handleAttributes(Map attributes) {
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
    
    @Override
	public Object valueForCellChanged(Object cell, Object newValue) {
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
    					
    					Object oldValue = descriptor.getReadMethod().invoke( bean, new Object[] {} );
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
}
