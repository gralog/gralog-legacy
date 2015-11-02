/*
 * Created on 2008 by Sebastian Ordyniak
 *
 * Copyright 2008 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of GrALoG.
 *
 * GrALoG is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * GrALoG is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GrALoG; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */
package de.hu.gralog.structure;
import de.hu.gralog.structure.types.elements.*;

import java.io.File;
import java.util.Vector;
import javax.xml.transform.OutputKeys;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.Document;


import org.jgrapht.graph.*;
import org.jgrapht.EdgeFactory;
import org.jgrapht.ListenableGraph;
import org.jgrapht.VertexFactory;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphListener;
import org.jgrapht.event.GraphVertexChangeEvent;

import de.hu.gralog.app.UserException;
import de.hu.gralog.beans.event.DisplayChangeListenable;
import de.hu.gralog.beans.event.PropertyChangeListenable;
import de.hu.gralog.beans.support.StructureBean;
import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;
import de.hu.gralog.structure.support.StructureTypeInfoSupport;
import de.hu.gralog.structure.support.StructureDisplayChangeSupport;
import de.hu.gralog.structure.support.StructurePropertyChangeSupport;
import de.hu.gralog.structure.support.StructureSelectionSupport;

/**
 * This class represents a GrALoG-Structure.
 * <p>
 * A GrALoG-Structure is basically a mathematical graph-structure that
 * has arbitrary properties for it's vertices, edges and the structure itself.
 * 
 * In GrALoG
 * this is accomplished by using the JavaBeans-Framework to define vertices,
 * edges and so called Structure-Bean-Objects. It follows that vertices, edges and
 * Structure-Beans are seen by GrALoG as JavaBeans allowing them to be hugely
 * customizable, i.e. having arbitrary properties and providing their own
 * GUI-Components that allow the GrALoG-User to change these properties.
 * </p>
 * As mentioned above a Structure in GrALoG is represented by an instance of this
 * class, which serves the following proposes:
 * <p>
 * <ul>
 * <li> It holds the mathematical graph-structure, i.e. the vertices and
 * edges belonging to that GrALoG-Structure. 
 * GrALoG uses <a href="http://jgrapht.sourceforge.net/">JGraphT</a> to
 * store the graph-structure, 
 * thus each GrALoG-Structure has an associated JGraphT-graph,
 * that can be retrieved via {@link #getGraph()}. 
 * </li>
 * <li> It provides methods to create it's vertices and edges. See
 * {@link #createVertex()} and {@link #getEdgeFactory()}. </li>
 * <li> It holds the so called Structure-Bean-instance, which can be seen as the
 * holder of properties that belong to the whole structure. The Structure-Bean can be
 * retrieved via {@link #getStructureBean()}. </li>
 * <li> It listens to changes on it's vertices, edges and Structure-Bean and forwards
 * them to the listeners registered with this structure. See
 * {@link #getPropertyChangeSupport()} respectively
 * {@link #getDisplayChangeSupport()}. </li>
 * It provides the functionality for the user to select vertices and / or edges and
 * informs listeners on changes to this selection. See
 * {@link #getStructureSelectionSupport()}.
 * <li> It allows GrALoG to draw the structure using the renderers provided by
 * {@link #getVertexRenderer()} and {@link #getEdgeRenderer()}. </li>
 * </ul>
 * 
 * <h2>Plugin-Developers</h2>
 * 
 * Please refer to {@link StructureTypeInfo} if you are interested in defining
 * your own GrALoG-Structure-Type.
 * 
 * @author Sebastian
 * 
 * @param <V>
 *            the vertexType
 * @param <E>
 *            the edgeType
 * @param <GB>
 *            the Structure-Bean-Type
 * @param <G>
 *            the JGraphT-Type, which has to implement
 *            {@link org.jgrapht.graph.ListenableGraph}
 */
public class Structure<V, E, GB, G extends ListenableGraph<V, E>> {

	private final StructurePropertyChangeSupport propertyChangeSupport = new StructurePropertyChangeSupport();

	private final StructureDisplayChangeSupport<V, E> displayChangeSupport = new StructureDisplayChangeSupport<V, E>();

	private final StructureSelectionSupport<V, E> selectionSupport = new StructureSelectionSupport<V, E>();

	private final StructureTypeInfoSupport<V, E, GB, G> typeInfoSupport;

	/**
	 * Constructs a Structure using the given
	 * {@link StructureTypeInfoSupport}-object. This constructor is not
	 * public, please use {@link StructureFactory} to construct a GrALoG-Structure.
	 * 
	 * 
	 * @param typeInfoSupport
	 * @throws UserException
	 */
	Structure(StructureTypeInfoSupport<V, E, GB, G> typeInfoSupport)
			throws UserException {
		this.typeInfoSupport = typeInfoSupport;

		for (V v : typeInfoSupport.getGraph().vertexSet())
			registerBean(v);
		for (E e : typeInfoSupport.getGraph().edgeSet())
			registerBean(e);
		typeInfoSupport.getGraph().addGraphListener(new JGraphTListener());
		if (getStructureBean() != null) {
			if (getStructureBean() instanceof StructureBean)
				((StructureBean) getStructureBean()).setStructure(this);
			registerBean(getStructureBean());
		}
	}

	/**
	 * Adds listeners to vertices, edges or GraphBean
	 * 
	 * @param bean
	 */
	protected void registerBean(Object bean) {
		if (bean instanceof PropertyChangeListenable)
			propertyChangeSupport.registerBean((PropertyChangeListenable) bean);
		if (bean instanceof DisplayChangeListenable)
			displayChangeSupport
					.registerBean((DisplayChangeListenable<V, E>) bean);
	}

	/**
	 * 
	 * 
	 * @return the associated JGraphT-Graph
	 */
	public G getGraph() {
		return typeInfoSupport.getGraph();
	}

	/**
	 * 
	 * 
	 * @return {@link StructurePropertyChangeSupport}, that you can use to add and
	 *         remove
	 *         {@link java.beans.PropertyChangeListener PropertyChangeListeners}
	 *         to/from this structure. These listeners are informed about all changes to
	 *         vertices, edges and Structure-Bean belonging to this structure.
	 */
	public StructurePropertyChangeSupport getPropertyChangeSupport() {
		return propertyChangeSupport;
	}

	/**
	 * 
	 * 
	 * @return {@link StructureDisplayChangeSupport}, that you can use to add and
	 *         remove
	 *         {@link de.hu.gralog.beans.event.DisplayChangeListener DisplayChangeListeners}
	 *         to / from this structure. These listeners are informed about 
	 *         all changes to vertices, edges and Structure-Bean belonging to this structure.
	 */
	public StructureDisplayChangeSupport<V, E> getDisplayChangeSupport() {
		return displayChangeSupport;
	}

	/**
	 * 
	 * 
	 * @return {@link StructureSelectionSupport}, that you can use to add and
	 *         remove
	 *         {@link de.hu.gralog.structure.event.SelectionListener SelectionListeners}
	 *         to this structure. These listeners are informed whenever the user
	 *         changes his selection of vertices / edges in the structure. You can
	 *         also retrieve the currently selected vertices / edges from this
	 *         object.
	 */
	public StructureSelectionSupport<V, E> getStructureSelectionSupport() {
		return selectionSupport;
	}

	/**
	 * 
	 * @return the name of the underlying {@link StructureTypeInfo}
	 */
	public String getName() {
		return typeInfoSupport.getName();
	}

	/**
	 * 
	 * @return the Structure-Bean, if any, registered with this GrALoG-Structure
	 */
	public GB getStructureBean() {
		return typeInfoSupport.getStructureBean();
	}

	/**
	 * 
	 * @return if true GrALoG allows the user to edit the properties of the Structure-Bean
	 */
	public boolean isStructureEditable() {
		boolean editable = getStructureBean() != null
				&& getStructureBean() instanceof PropertyChangeListenable;
		return editable;
	}

	/**
	 * 
	 * @return the {@link VertexFactory} used by this GrALoG-Structure to construct
	 *         new vertices
	 */
	public VertexFactory<V> getVertexFactory() {
		return typeInfoSupport.getVertexFactory();
	}

	/**
	 * Creates a vertex using {@link #getVertexFactory()}
	 * 
	 * @return the created vertex
	 */
	public V createVertex() {
		return getVertexFactory().createVertex();
	}

	/**
	 * 
	 * 
	 * @return true if GrALoG allows the user to edit the properties of vertices
	 */
	public boolean isVertexEditable() {
		return createVertex() instanceof PropertyChangeListenable;
	}

	/**
	 * 
	 * @param vertex
	 * @return true if vertex is an instance of <b>vertexType</b>(V)
	 */
	public boolean isVertex(Object vertex) {
		return createVertex().getClass().isInstance(vertex);
	}

	/**
	 * 
	 * 
	 * @return the edgeFactory used by the underlying JGraphT-Graph
	 */
	public EdgeFactory<V, E> getEdgeFactory() {
		return getGraph().getEdgeFactory();
	}

	/**
	 * 
	 * 
	 * @return true GrALoG allows the user to edit the properties of edges
	 */
	public boolean isEdgeEditable() {
		return getEdgeFactory().createEdge(createVertex(), createVertex()) instanceof PropertyChangeListenable;
	}

	/**
	 * 
	 * 
	 * @param edge
	 * @return true, if edge is an instanceof <b>edgeType</b> (E)
	 */
	public boolean isEdge(Object edge) {
		return getEdgeFactory().createEdge(createVertex(), createVertex())
				.getClass().isInstance(edge);
	}

	/**
	 * 
	 * 
	 * @return the {@link org.jgraph.graph.VertexRenderer} used by GrALoG to
	 *         render the vertices of this structure.
	 */
	public DefaultVertexRenderer getVertexRenderer() {
		return typeInfoSupport.getVertexRenderer();
	}

	/**
	 * 
	 * 
	 * @return the {@link org.jgraph.graph.EdgeRenderer} used by GrALoG to
	 *         render the edges of this structure.
	 */
	public DefaultEdgeRenderer getEdgeRenderer() {
		return typeInfoSupport.getEdgeRenderer();
	}

	/**
	 * 
	 * 
	 * @return the {@link StructureTypeInfo} associated to this GrALoG-Structure. 
	 * Can be used to construct new GrALoG-Structures of the same
	 * type by using {@link StructureFactory#createStructure(StructureTypeInfo)}^
	 * 
	 */
	public StructureTypeInfo<V, E, GB, G> getTypeInfo() {
		return typeInfoSupport.getTypeInfo();
	}

	/**
	 * Adds a vertex to the underlying JGraphT-graph. This method is used
	 * internally by GrALoG and should not be called by Plugin-Developers - 
	 * you should use the addVertex-method of the underlying JGraphT instead.
	 * 
	 * @param v
	 */
	public boolean addVertex(V v) {
		return getGraph().addVertex(v);
	}

	/**
	 * Adds an edge to the underlying JGraphT-graph. This method is used
	 * internally by GrALoG and should not be called by Plugin-Developers - you
	 * should use the addEdge-function of the underlying JGraphT-Graph instead
	 * 
	 * @param s
	 *            the sourceVertex
	 * @param t
	 *            the targetVertex
	 * @param e
	 *            the edge
	 */
	public boolean addEdge(V s, V t, E e) {
		return getGraph().addEdge(s, t, e);
	}

	/**
	 * The listener that listens to structural-changes of the underlying
	 * JGraphT-Graph.
	 * 
	 * @author Sebastian
	 * 
	 */
	private class JGraphTListener implements GraphListener<V, E> {

		public void edgeAdded(GraphEdgeChangeEvent e) {
			registerBean(e.getEdge());
		}

		public void edgeRemoved(GraphEdgeChangeEvent e) {
		}

		public void vertexAdded(GraphVertexChangeEvent e) {
			registerBean(e.getVertex());
		}

		public void vertexRemoved(GraphVertexChangeEvent e) {
		}

	}
        
        public void WriteToFile(String fileName) throws ParserConfigurationException, TransformerException
        {
            WriteToFile(new File(fileName));
        }
        
        public void WriteToFile(File file) throws ParserConfigurationException, TransformerException
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            
            Element graphml = doc.createElement("graphml");
            WriteToXml(doc, graphml);
            doc.appendChild(graphml);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");            
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        }
        
        public Element WriteToXml(Document doc, Element node)
        {
            //Namespace y = Namespace.getNamespace("y", "http://www.yworks.com/xml/graphml");
            String y = "http://www.yworks.com/xml/graphml";
            
            Element key = doc.createElement("key");
            key.setAttribute("for", "node");
            key.setAttribute("id", "d6");
            key.setAttribute("yfiles.type", "nodegraphics");
            node.appendChild(key);
            
            G graph = typeInfoSupport.getGraph();
            Element graphnode = doc.createElement("graph");
            node.appendChild(graphnode);
            
            
            /*
            if(this instanceof ListenableDirectedGraph) {
                    graphnode.setAttribute("edgedefault", "directed");
            }
            if(this instanceof ListenableUndirectedGraph) {
                    graphnode.setAttribute("edgedefault","undirected");
            }
            */
            
            
            Vector<V> vertices = new Vector<V>();
            int i = 1;
            for (V v : graph.vertexSet())
            {
                vertices.add(v);
                i++;

                // v has its own WriteToXml method
                if(v instanceof DefaultListenableVertex)
                {
                    DefaultListenableVertex lv = (DefaultListenableVertex)v;
                    lv.WriteToXml(doc, graphnode, "n"+i);
                }
                // v has no own WriteToXml method - write a default GraphML node
                else 
                {
                    Element vertexnode = doc.createElement("node");
                    vertexnode.setAttribute("id", "n"+i);
                    graphnode.appendChild(vertexnode);
                }
            }
            
            i = 1;
            for(V v : graph.vertexSet())
            {
                for(E e : graph.edgesOf(v))
                {
                    Object src = graph.getEdgeSource(e);
                    if(src != v)
                        continue;
                    Object dst = graph.getEdgeTarget(e);
                    
                    // slow and stupid - I want to store the indexes in a map...
                    int j = 1;
                    for(V w : graph.vertexSet())
                    {
                        if(w == dst)
                        {
                            
                            // e has its own WriteToXml method
                            if(e instanceof DefaultListenableEdge)
                            {
                                DefaultListenableEdge le = (DefaultListenableEdge)e;
                                le.WriteToXml(doc, graphnode, "n"+i, "n"+j);
                            }
                            // e has no own WriteToXml method - write a default GraphML edge
                            else
                            {
                                Element edgenode = doc.createElement("edge");
                                edgenode.setAttribute("source", "n"+i);
                                edgenode.setAttribute("target", "n"+j);
                                edgenode.setAttribute("directed","false");
                                graphnode.appendChild(edgenode);
                            }
                            break;
                            
                        }
                        j++;
                    }
                }
                i++;
            }
            
            return graphnode;
        }
        
}
