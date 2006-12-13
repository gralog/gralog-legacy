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

package de.hu.games.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.hu.games.gui.MainPad;
import de.hu.games.jgraph.GJGraph;

public class EditSelectAllAction extends AbstractAction {
	
	public static final String NODES = new String( "nodes" );
	public static final String EDGES = new String( "edges" );
	public static final String BOTH = new String( "both" );
	
	private String mode = BOTH;
	
	public EditSelectAllAction() {
		super( BOTH, null );
	}
	
	public EditSelectAllAction( String mode ) {
		super( mode, null );
		this.mode = mode;
	}
	
	public void actionPerformed(ActionEvent e) {
		GJGraph graph = MainPad.getInstance().getDesktop().getCurrentGraph();
		Object[] cells = null;
		if ( mode ==  BOTH )
			cells = graph.getGModel().getRoots().toArray();
		if ( mode == NODES )
			cells = graph.getGModel().getVertexCells();
		if ( mode == EDGES )
			cells = graph.getGModel().getEdgeCells();
		if ( cells != null )
			MainPad.getInstance().getDesktop().getCurrentGraph().setSelectionCells( cells );
	}
	
}
