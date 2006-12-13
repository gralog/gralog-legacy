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

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import de.hu.games.gui.MainPad;
import de.hu.games.jgraph.GJGraph;

public class ZoomFitInCanvasAction extends AbstractAction {

	public ZoomFitInCanvasAction(  ) {
		super( "Fit In Canvas", MainPad.createImageIcon( "stock_zoom-page.png") );
	}

	public void actionPerformed(ActionEvent arg0) {
		final GJGraph graph = MainPad.getInstance().getDesktop().getCurrentGraph();
		graph.setScale(  1 );
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				JViewport viewport = (JViewport)graph.getParent();
				Dimension extSize = viewport.getExtentSize();
				Dimension originalGraphSize = viewport.getViewSize();
				double scale = Math.min( extSize.getWidth() / originalGraphSize.getWidth(), extSize.getHeight() / originalGraphSize.getHeight() );
				graph.setScale(  scale );
			}
		});
	}
}
