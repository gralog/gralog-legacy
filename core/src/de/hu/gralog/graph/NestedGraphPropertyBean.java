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

import java.beans.PropertyChangeListener;

/**
 * This class is a helper to define properties that are indexed for graphs, vertices and edges.
 * <p>
 * Here is an example for using an indexed property in a graph:
 * <p>
 * <pre>
 * public class MyGraph extends DirectedGraph<MyVertexClass,MyEdgeClass> {
 * 		private ArrayList<IndexedPropertyClass> indexedProperties = new ArrayList<IndexedPropertyClass>();
 * 
 * 		public MyGraph() {
 * 			super( MyVertexClass, MyEdgeClass );
 * 		}
 * 	
 * 		public IndexedPropertyClass[] getIndexedProperties() {
 * 			return indexedProperties.toArray( new IndexedPropertyClass[indexedProperties.size()] );
 * 		}
 * 
 * 		public IndexedPropertyClass getIndexedProperties( int index ) {
 * 			if ( index >= indexedProperties.size() )
 * 				return null;
 * 			return indexedProperties.get( index );
 * 		}
 * 
 * 		public void setIndexedProperties( IndexPropertyClass[] indexedProperties ) {
 * 			this.indexedProperties = new ArrayList<IndexedPropertyClass>();
 * 			for ( IndexedPropertyClass prop : indexedProperties ) {
 * 				this.indexedProperties.add( prop );
 * 				prop.addPropertyChangeListener( this );
 * 			}
 * 		}
 *
 * 		public void setIndexedProperties( int index, IndexedPropertyClass indexedProperty ) {
 * 			IndexedProperty oldValue = null;
 * 			if ( index < indexedProperties.size() )
 * 				oldValue = indexedProperties.get( index );
 * 			if ( indexedProperty == null )
 * 				indexedProperties.remove( index );
 * 			else {
 * 				indexedProperties.add( index, indexedProperty );
 * 				indexedProperty.addPropertyChangeListener( this );
 * 			}
 * 			fireGraphPropertyChange( this, new IndexedPropertyChangeEvent( this, "indexProperties", oldValue, indexedProperty, index ) );
 * 		}
 * 
 * 		public boolean isGraphEditable() {
 * 			return true;
 * 		}
 * }
 * </pre>
 * <p>
 * The class IndexedPropertyClass would look like this:
 * <p>
 * <pre>
 * public class IndexedPropertyClass implements NestedGraphPropertyBean {
 * 		private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport( this );
 * 		private String name = "name";
 * 
 * 		public IndexPropertyClass() {
 * 			this( "name" );
 * 		}
 *
 *		public String getName() {
 *			return name;
 *		}
 *
 *		public void setName( String name ) {
 *			String oldName = this.name;
 *			this.name = name;
 *			propertyChangeSupport.firePropertyChange( "name", oldName, name );
 *		}
 *
 *		public void addPropertyChangeListener(PropertyChangeListener l) {
 *			propertyChangeSupport.addPropertyChangeListener( l );
 *		}
 *
 *		public void removePropertyChangeListener(PropertyChangeListener l) {
 *			propertyChangeSupport.removePropertyChangeListener( l );
 *		}
 * } 
 * </pre>
 * <p>
 * And last but not least the BeanInfo for MyGraph has to look like this:
 * <p>
 * <pre>
 * public class MyGraphBeanInfo extends SimpleBeanInfo {
 * 		private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( MyGraph.class );
 * 		private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[1];
 * 
 * 		static {
 * 			try {
 * 				PROPERTY_DESCRIPTORS[0] = new IndexedPropertyDescriptor( "indexedProperties", MyGraph.class );
 * 			} catch (IntrospectionException e) {
 * 				// TODO Auto-generated catch block
 * 				e.printStackTrace();
 * 			}
 * 		}
 * 
 * 		public BeanDescriptor getBeanDescriptor() {
 * 			return BEAN_DESCRIPTOR;
 * 		}
 * 
 * 		public PropertyDescriptor[] getPropertyDescriptors() {
 * 			return PROPERTY_DESCRIPTORS;
 * 		}
 * }
 * </pre>
 * <p>
 * Note that when dealing with index properties it is often worth to think about a customizer for
 * your class.
 *  
 * @author ordyniak
 *
 */
public interface NestedGraphPropertyBean {
	public void addPropertyChangeListener( PropertyChangeListener l );
	public void removePropertyChangeListener( PropertyChangeListener l );
}
