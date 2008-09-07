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

package de.hu.gralog.gui.action;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;

import org.jgraph.graph.GraphConstants;

import de.hu.gralog.gui.MainPad;

public class Zoom11Action extends AbstractAction {

	protected static Set LAYOUT_ATTRIBUTES = new HashSet();
	
	/**
	 * @return
	 */
	static {
		LAYOUT_ATTRIBUTES.add(GraphConstants.BOUNDS);
		LAYOUT_ATTRIBUTES.add(GraphConstants.POINTS);
		LAYOUT_ATTRIBUTES.add(GraphConstants.LABELPOSITION);
		LAYOUT_ATTRIBUTES.add(GraphConstants.ROUTING);
	}

	
	public Zoom11Action(  ) {
		super( "1:1", MainPad.createImageIcon( "stock_zoom-1.png") );
		super.putValue( SHORT_DESCRIPTION, "Zoom 1:1" );
	}

	public void actionPerformed(ActionEvent arg0) {
		MainPad.getInstance().getDesktop().getCurrentGraph().setScale( 1 );
	}
}
