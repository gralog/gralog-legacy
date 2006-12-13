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

package de.hu.games.jgraph;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Map;

import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.GraphTransferable;
import org.jgraph.graph.ParentMap;

import de.hu.games.app.UserException;
import de.hu.games.gui.MainPad;

public class GGraphTransferable extends GraphTransferable {

	protected GDataFlavor graphDataFlavor;
	
	public GGraphTransferable(GJGraph graph, Object[] cells, Map attrMap, Rectangle2D bounds,
			ConnectionSet cs, ParentMap pm) {
		super(cells, attrMap, bounds, cs, pm);
		try {
			this.graphDataFlavor = new GDataFlavor( graph );
		} catch( ClassNotFoundException e ) {
			MainPad.getInstance().handleUserException( new UserException( "unable to create DataFlavor for graph", e ) );
		}
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		return this;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { graphDataFlavor };
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		if ( ! (flavor instanceof GDataFlavor) )
			return false;
		if ( ((GDataFlavor)flavor).getGraph().getGraphT().getClass() == graphDataFlavor.getGraph().getGraphT().getClass() )
			return true;
		return false;
	}
	
	
}
