/*
 * Created on 9 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.games.graph;

import org.jgraph.event.GraphSelectionListener;

public interface SelectionListenableGraph<V,E> extends ViewableGraph<V,E>, GraphSelectionListener {
	public void addSelectionListener( SelectionListener<V,E> listener );
	public void removeSelectionListener( SelectionListener<V,E> listener );
}
