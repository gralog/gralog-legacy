package de.hu.gralog.graph;

import org.jgrapht.EdgeFactory;
import org.jgrapht.ListenableGraph;
import org.jgrapht.VertexFactory;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphListener;
import org.jgrapht.event.GraphVertexChangeEvent;

import de.hu.gralog.app.UserException;
import de.hu.gralog.beans.event.DisplayChangeListenable;
import de.hu.gralog.beans.event.PropertyChangeListenable;
import de.hu.gralog.beans.support.GralogGraphBean;
import de.hu.gralog.graph.support.GralogGraphTypeInfoSupport;
import de.hu.gralog.graph.support.GraphDisplayChangeSupport;
import de.hu.gralog.graph.support.GraphPropertyChangeSupport;
import de.hu.gralog.graph.support.GraphSelectionSupport;
import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;

/**
 * This class defines a Gralog-Graph. 
 * <p>
 * A Gralog-Graph is basically 
 * a mathematical graph that allows to specify 
 * arbitrary properties for its vertices, edges and the graph itself. 
 * In Gralog this is accomplished by using the 
 * JavaBeans-Framework to define vertices, edges and a so 
 * called GraphBean object. It follows that vertices, edges and 
 * GraphBean are seen by Gralog as JavaBeans which allows 
 * them to be hugely customizable, i.e. having arbitrary properties 
 * and providing their own GUI-Components 
 * that are used by the Gralog-User to alter these properties.
 * </p>
 * As metioned above a Graph in Gralog is 
 * represented by an instance of this class, which serves 
 * the following proposes:
 * <p>
 * <ul>
 * 	<li>
 * 	It holds mathematical structure of the graph, i.e.
 * 	its vertices and edges. Gralog uses
 * 	<a href="http://jgrapht.sourceforge.net/">JGraphT</a> to store this
 * 	structure, thus	a Gralog-Graph has an 
 * 	underlying JGraphT-graph that can be 
 * 	retrieved via {@link #getGraph()}.
 * </li>
 * <li>
 * It provides methods to create its vertices and edges. See
 * {@link #createVertex()} and {@link #getEdgeFactory()}.
 * </li>
 * <li>
 * It holds the so called GraphBeans-instance, which can be seen
 * as a holder for properties that affect the whole graph. The
 * GraphBean can be retrieved via {@link #getGraphBean()}.
 * </li>
 * <li>
 * It listens to changes on its vertices, edges and GraphBean and forwards
 * them to the listeners registered with this graph. See
 * {@link #getPropertyChangeSupport()} respectively {@link #getDisplayChangeSupport()}.
 * </li>
 * It provides the functionality for the user to select vertices and edges and
 * informs listeners on changes of this selection. See {@link #getGraphSelectionSupport()}.
 * <li>
 * It allows Gralog to draw the graph using the Renderers provided by
 * {@link #getVertexRenderer()} and {@link #getEdgeRenderer()}.
 * </li>
 * </ul>
 * 
 * <h2>Plugin-Developers</h2>
 * 
 * Please refer to {@link GralogGraphTypeInfo} if you are interested in
 * defining your own Gralog-Graph-Type.
 * 
 * @author Sebastian
 *
 * @param <V> the vertexType
 * @param <E> the edgeType
 * @param <GB> the GraphBeanType
 * @param <G> the JGraphT-Type, which has to implement {@link org.jgrapht.graph.ListenableGraph<V,E>}
 */
public class GralogGraphSupport<V, E, GB, G extends ListenableGraph<V, E>> {

	private final GraphPropertyChangeSupport propertyChangeSupport = new GraphPropertyChangeSupport();

	private final GraphDisplayChangeSupport<V, E> displayChangeSupport = new GraphDisplayChangeSupport<V, E>();

	private final GraphSelectionSupport<V, E> selectionSupport = new GraphSelectionSupport<V, E>();

	private final GralogGraphTypeInfoSupport<V, E, GB, G> typeInfoSupport;

	/**
	 * Constructs a GralogGraphSupport using the given {@link GralogGraphTypeInfoSupport}-object.
	 * This constructor is not public, please use {@link GralogGraphFactory} to
	 * construct a GralogGraph. 
	 * 
	 * 
	 * @param typeInfoSupport
	 * @throws UserException
	 */
	GralogGraphSupport(GralogGraphTypeInfoSupport<V, E, GB, G> typeInfoSupport)
			throws UserException {
		this.typeInfoSupport = typeInfoSupport;

		for (V v : typeInfoSupport.getGraph().vertexSet())
			registerBean(v);
		for (E e : typeInfoSupport.getGraph().edgeSet())
			registerBean(e);
		typeInfoSupport.getGraph().addGraphListener(new JGraphTListener());
		if (getGraphBean() != null) {
			if (getGraphBean() instanceof GralogGraphBean)
				((GralogGraphBean) getGraphBean()).setGraphSupport(this);
			registerBean(getGraphBean());
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
	 * @return the underlying JGraphT-Graph
	 */
	public G getGraph() {
		return typeInfoSupport.getGraph();
	}

	/**
	 * 
	 * 
	 * @return {@link GraphPropertyChangeSupport}, that you can use to add and remove
	 * {@link PropertyChangeListener PropertyChangeListeners} to this graph. These Listeners
	 * are informed on all changes in vertices, edges and the GraphBean.
	 */
	public GraphPropertyChangeSupport getPropertyChangeSupport() {
		return propertyChangeSupport;
	}

	/**
	 * 
	 * 
	 * @return {@link GraphPropertyChangeSupport}, that you can use to add and remove
	 * {@link DisplayChangeListener DisplayChangeListeners} to this graph. These Listeners
	 * are informed on all changes in vertices, edges and the GraphBean.
	 */
	public GraphDisplayChangeSupport<V, E> getDisplayChangeSupport() {
		return displayChangeSupport;
	}

	/**
	 * 
	 * 
	 * @return {@link GraphSelectionSupport}, that you can use to add and remove
	 * {@link SelectionListener SelectionListeners} to this graph. These listeners
	 * are informed whenever the user changes his selection of vertices / edges in
	 * the graph. You can also retrieve the currently selected vertices / edges from
	 * this object.
	 */
	public GraphSelectionSupport<V, E> getGraphSelectionSupport() {
		return selectionSupport;
	}

	/**
	 * 
	 * @return the name of the underlying {@link GralogGraphTypeInfo}
	 */
	public String getName() {
		return typeInfoSupport.getName();
	}

	/**
	 * 
	 * @return the GraphBean, if any, registered with the Gralog-Graph
	 */
	public GB getGraphBean() {
		return typeInfoSupport.getGraphBean();
	}

	/**
	 * 
	 * @return, if true Gralog allows the user to edit the properties of the bean 
	 */
	public boolean isBeanEditable() {
		boolean editable = getGraphBean() != null
				&& getGraphBean() instanceof PropertyChangeListenable;
		return editable;
	}

	/**
	 * 
	 * @return the {@link VertexFactory<V>} used by this Gralog-Graph to
	 * construct new vertices
	 */
	public VertexFactory<V> getVertexFactory() {
		return typeInfoSupport.getVertexFactory();
	}

	/**
	 * Creates a vertex using {@link #getVertexFactory()}
	 * 
	 * @return
	 */
	public V createVertex() {
		return getVertexFactory().createVertex();
	}

	/**
	 * 
	 * 
	 * @return, if true Gralog allows the user to edit the properties of vertices
	 */
	public boolean isVertexEditable() {
		return createVertex() instanceof PropertyChangeListenable;
	}

	/**
	 * 
	 * @param vertex
	 * @return true, if vertex is an instance of <b>vertexType</b>(V)
	 */
	public boolean isVertex(Object vertex) {
		return createVertex().getClass().isInstance(vertex);
	}

	/**
	 * 
	 * 
	 * @return, the edgeFactory used by the underlying JGraphT-Graph
	 */
	public EdgeFactory<V, E> getEdgeFactory() {
		return getGraph().getEdgeFactory();
	}

	/**
	 * 
	 * 
	 * @return, if true Gralog allows the user to edit the properties of edges
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
	 * @return the {@link org.jgraph.graph.VertexRenderer} used by Gralog to render
	 * the vertices of this graph.
	 */
	public DefaultVertexRenderer getVertexRenderer() {
		return typeInfoSupport.getVertexRenderer();
	}

	/**
	 * 
	 * 
	 * @return the {@link org.jgraph.graph.EdgeRenderer} used by Gralog to render
	 * the vertices of this graph.
	 */
	public DefaultEdgeRenderer getEdgeRenderer() {
		return typeInfoSupport.getEdgeRenderer();
	}

	/**
	 * 
	 * 
	 * @return the {@link GralogGraphTypeInfo} with which this Gralog-Graph was created. Can
	 * be used to construct new Gralog-Graphs of the same type by using {@link GralogGraphFactory}
	 */
	public GralogGraphTypeInfo<V, E, GB, G> getTypeInfo() {
		return typeInfoSupport.getTypeInfo();
	}

	/**
	 * Adds a vertex to the underlying JGraphT-graph. This
	 * method is used internally by Gralog and should not be called by 
	 * Plugin-Developers - use {@link org.jgrapht.Graph#addVertex(V)} instead. 
	 * 
	 * @param v
	 */
	public void addVertex(V v) {
		getGraph().addVertex(v);
	}

	/**
	 * Adds an edge to the underlying JGraphT-graph. This
	 * method is used internally by Gralog and should not be called by 
	 * Plugin-Developers - use {@link org.jgrapht.Graph#addEdge(V, V, E)} instead. 
	 * 
	 * @param v
	 */
	public void addEdge(V s, V t, E e) {
		getGraph().addEdge(s, t, e);
	}

	/**
	 * The listener that listens to structurechanges of the underlying JGraphT-Graph.
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
}
