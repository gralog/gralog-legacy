/*
 * Created on 9 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.graph.event;

import java.util.Set;

import org.jgraph.event.GraphSelectionEvent;

/**
 * A Listener for selectionevents of graphs.
 * 
 * 
 * @author ordyniak
 * 
 */
public interface SelectionListener<V, E> {
	public void valueChanged(Set<V> selectedVertexes, Set<E> selectedEdges,
			GraphSelectionEvent event);
}
