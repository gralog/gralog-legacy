package de.hu.gralog.jgrapht.graph;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Set;

import org.jgraph.graph.Edge;
import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphListener;
import org.jgrapht.event.GraphVertexChangeEvent;
import org.jgrapht.event.VertexSetListener;
import org.jgrapht.graph.GraphDelegator;
import org.jgrapht.util.TypeUtil;

/**
 */
public class ListenableGraphSupport<V, E>
{
    //~ Static fields/initializers ---------------------------------------------

    private static final long serialVersionUID = 3977575900898471984L;

    //~ Instance fields --------------------------------------------------------

    private ArrayList<GraphListener<V, E>> graphListeners =
        new ArrayList<GraphListener<V, E>>();
    private ArrayList<VertexSetListener<V>> vertexSetListeners =
        new ArrayList<VertexSetListener<V>>();
    private FlyweightEdgeEvent<V, E> reuseableEdgeEvent;
    private FlyweightVertexEvent<V> reuseableVertexEvent;
    private boolean reuseEvents;

    //~ Constructors -----------------------------------------------------------

    public ListenableGraphSupport()
    {
        this(false);
    }

    /**
     * Creates a new listenable graph. If the <code>reuseEvents</code> flag is
     * set to <code>true</code> this class will reuse previously fired events
     * and will not create a new object for each event. This option increases
     * performance but should be used with care, especially in multithreaded
     * environment.
     *
     * @param g the backing graph.
     * @param reuseEvents whether to reuse previously fired event objects
     * instead of creating a new event object for each event.
     *
     * @throws IllegalArgumentException if the backing graph is already a
     * listenable graph.
     */
    public ListenableGraphSupport(boolean reuseEvents)
    {
        this.reuseEvents = reuseEvents;
        reuseableEdgeEvent = new FlyweightEdgeEvent<V, E>(this, -1, null);
        reuseableVertexEvent = new FlyweightVertexEvent<V>(this, -1, null);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * If the <code>reuseEvents</code> flag is set to <code>true</code> this
     * class will reuse previously fired events and will not create a new object
     * for each event. This option increases performance but should be used with
     * care, especially in multithreaded environment.
     *
     * @param reuseEvents whether to reuse previously fired event objects
     * instead of creating a new event object for each event.
     */
    public void setReuseEvents(boolean reuseEvents)
    {
        this.reuseEvents = reuseEvents;
    }

    /**
     * Tests whether the <code>reuseEvents</code> flag is set. If the flag is
     * set to <code>true</code> this class will reuse previously fired events
     * and will not create a new object for each event. This option increases
     * performance but should be used with care, especially in multithreaded
     * environment.
     *
     * @return the value of the <code>reuseEvents</code> flag.
     */
    public boolean isReuseEvents()
    {
        return reuseEvents;
    }

    /**
     * @see ListenableGraph#addGraphListener(GraphListener)
     */
    public void addGraphListener(GraphListener<V, E> l)
    {
        addToListenerList(graphListeners, l);
    }

    /**
     * @see ListenableGraph#addVertexSetListener(VertexSetListener)
     */
    public void addVertexSetListener(VertexSetListener<V> l)
    {
        addToListenerList(vertexSetListeners, l);
    }

    /**
     * @see ListenableGraph#removeGraphListener(GraphListener)
     */
    public void removeGraphListener(GraphListener<V, E> l)
    {
        graphListeners.remove(l);
    }

    /**
     * @see ListenableGraph#removeVertexSetListener(VertexSetListener)
     */
    public void removeVertexSetListener(VertexSetListener<V> l)
    {
        vertexSetListeners.remove(l);
    }

    /**
     * Notify listeners that the specified edge was added.
     *
     * @param edge the edge that was added.
     */
    public void fireEdgeAdded(E edge)
    {
        GraphEdgeChangeEvent<V, E> e =
            createGraphEdgeChangeEvent(GraphEdgeChangeEvent.EDGE_ADDED, edge);

        for (int i = 0; i < graphListeners.size(); i++) {
            GraphListener<V, E> l = graphListeners.get(i);

            l.edgeAdded(e);
        }
    }

    /**
     * Notify listeners that the specified edge was removed.
     *
     * @param edge the edge that was removed.
     */
    public void fireEdgeRemoved(E edge)
    {
        GraphEdgeChangeEvent<V, E> e =
            createGraphEdgeChangeEvent(
                GraphEdgeChangeEvent.EDGE_REMOVED,
                edge);

        for (int i = 0; i < graphListeners.size(); i++) {
            GraphListener<V, E> l = graphListeners.get(i);

            l.edgeRemoved(e);
        }
    }

    /**
     * Notify listeners that the specified vertex was added.
     *
     * @param vertex the vertex that was added.
     */
    public void fireVertexAdded(V vertex)
    {
        GraphVertexChangeEvent<V> e =
            createGraphVertexChangeEvent(
                GraphVertexChangeEvent.VERTEX_ADDED,
                vertex);

        for (int i = 0; i < vertexSetListeners.size(); i++) {
            VertexSetListener<V> l = vertexSetListeners.get(i);

            l.vertexAdded(e);
        }

        for (int i = 0; i < graphListeners.size(); i++) {
            GraphListener<V, E> l = graphListeners.get(i);

            l.vertexAdded(e);
        }
    }

    /**
     * Notify listeners that the specified vertex was removed.
     *
     * @param vertex the vertex that was removed.
     */
    public void fireVertexRemoved(V vertex)
    {
        GraphVertexChangeEvent<V> e =
            createGraphVertexChangeEvent(
                GraphVertexChangeEvent.VERTEX_REMOVED,
                vertex);

        for (int i = 0; i < vertexSetListeners.size(); i++) {
            VertexSetListener<V> l = vertexSetListeners.get(i);

            l.vertexRemoved(e);
        }

        for (int i = 0; i < graphListeners.size(); i++) {
            GraphListener<V, E> l = graphListeners.get(i);

            l.vertexRemoved(e);
        }
    }

    private static <L extends EventListener> void addToListenerList(
        List<L> list,
        L l)
    {
        if (!list.contains(l)) {
            list.add(l);
        }
    }

    private GraphEdgeChangeEvent<V, E> createGraphEdgeChangeEvent(
        int eventType,
        E edge)
    {
        if (reuseEvents) {
            reuseableEdgeEvent.setType(eventType);
            reuseableEdgeEvent.setEdge(edge);

            return reuseableEdgeEvent;
        } else {
            return new GraphEdgeChangeEvent<V, E>(this, eventType, edge);
        }
    }

    private GraphVertexChangeEvent<V> createGraphVertexChangeEvent(
        int eventType,
        V vertex)
    {
        if (reuseEvents) {
            reuseableVertexEvent.setType(eventType);
            reuseableVertexEvent.setVertex(vertex);

            return reuseableVertexEvent;
        } else {
            return new GraphVertexChangeEvent<V>(this, eventType, vertex);
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * A reuseable edge event.
     *
     * @author Barak Naveh
     * @since Aug 10, 2003
     */
    private static class FlyweightEdgeEvent<VV, EE>
        extends GraphEdgeChangeEvent<VV, EE>
    {
        private static final long serialVersionUID = 3907207152526636089L;

        /**
         * @see GraphEdgeChangeEvent#GraphEdgeChangeEvent(Object, int, Edge)
         */
        public FlyweightEdgeEvent(Object eventSource, int type, EE e)
        {
            super(eventSource, type, e);
        }

        /**
         * Sets the edge of this event.
         *
         * @param e the edge to be set.
         */
        protected void setEdge(EE e)
        {
            this.edge = e;
        }

        /**
         * Set the event type of this event.
         *
         * @param type the type to be set.
         */
        protected void setType(int type)
        {
            this.type = type;
        }
    }

    /**
     * A reuseable vertex event.
     *
     * @author Barak Naveh
     * @since Aug 10, 2003
     */
    private static class FlyweightVertexEvent<VV>
        extends GraphVertexChangeEvent<VV>
    {
        private static final long serialVersionUID = 3257848787857585716L;

        /**
         * @see GraphVertexChangeEvent#GraphVertexChangeEvent(Object, int,
         * Object)
         */
        public FlyweightVertexEvent(Object eventSource, int type, VV vertex)
        {
            super(eventSource, type, vertex);
        }

        /**
         * Set the event type of this event.
         *
         * @param type type to be set.
         */
        protected void setType(int type)
        {
            this.type = type;
        }

        /**
         * Sets the vertex of this event.
         *
         * @param vertex the vertex to be set.
         */
        protected void setVertex(VV vertex)
        {
            this.vertex = vertex;
        }
    }
}