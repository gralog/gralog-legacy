/*
 * Created on 9 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.games.graph;

import java.util.ArrayList;

import org.jgraph.event.GraphSelectionEvent;

public interface SelectionListener<V,E> {
	public void valueChanged( ArrayList<V> selectedVertexes, ArrayList<E> selectedEdges, GraphSelectionEvent event );
}
