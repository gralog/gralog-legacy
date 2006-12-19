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

package de.hu.gralog.jgraph;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;
import javax.swing.TransferHandler;

import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.plaf.basic.BasicGraphUI;

public class GJGraphUI extends BasicGraphUI {

	public GJGraphUI() {
		super();
	}
	
	/**
	 * Paints the renderer of <code>view</code> to <code>g</code> at
	 * <code>bounds</code>. Recursive implementation that paints the children
	 * first.
	 * <p>
	 * The reciever should NOT modify <code>clipBounds</code>, or
	 * <code>insets</code>. The <code>preview</code> flag is passed to the
	 * renderer, and is not used here.
	 */
	public void paintCell(Graphics g, CellView view, Rectangle2D bounds,
			boolean preview) {
		// First Paint View
		boolean hide = false;
		if (view != null && bounds != null) {
			String tooltip = ((GJGraph)graph).getToolTip( ((DefaultGraphCell)view.getCell()).getUserObject() );
			if ( tooltip != null && tooltip.length() > 0 ) {
				JLabel label = new JLabel( tooltip );
				Rectangle2D textBounds = label.getFont().getStringBounds( tooltip, ((Graphics2D)g).getFontRenderContext() );
				int x = (int)(bounds.getX() + (bounds.getWidth()) / 2 - textBounds.getWidth() / 2);
				int y = (int)(bounds.getY() - 10 - textBounds.getHeight());
				rendererPane.paintComponent(g, label, graph, x, y, (int)textBounds.getWidth(),
						(int)textBounds.getHeight(), true);
			}
			
			boolean bfocus = (view == this.focus);
			boolean sel = graph.isCellSelected(view.getCell());
			Component component = view.getRendererComponent(graph, sel, bfocus,
					preview);
			if ( component != null )
				rendererPane.paintComponent(g, component, graph, (int) bounds
						.getX(), (int) bounds.getY(), (int) bounds.getWidth(),
						(int) bounds.getHeight(), true);
			else
				hide = true;
		}
		// Then Paint Children
		if (!view.isLeaf() && !hide) {
			CellView[] children = view.getChildViews();
			for (int i = 0; i < children.length; i++)
				paintCell(g, children[i], children[i].getBounds(), preview);
		}
	}
	
	/**
	 * Creates an instance of TransferHandler. Used for subclassers to provide
	 * different TransferHandler.
	 */
	protected TransferHandler createTransferHandler() {
		return new GGraphTransferHandler();
	}
}
