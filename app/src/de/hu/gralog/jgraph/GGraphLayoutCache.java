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

import java.awt.geom.Rectangle2D;
import java.util.Map;

import org.jgraph.event.GraphLayoutCacheEvent;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewFactory;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

public class GGraphLayoutCache extends GraphLayoutCache {

	public GGraphLayoutCache(GraphModel model, CellViewFactory factory) {
		super(model, factory);
	}

	@Override
	public void cellViewsChanged(CellView[] cellViews) {
		if ( cellViews != null ) {
			final Object[] cells = new Object[cellViews.length];
			for ( int i = 0;i < cellViews.length;i++ )
				cells[i] = cellViews[i].getCell();
			fireGraphLayoutCacheChanged(this,
					new GraphLayoutCacheEvent.GraphLayoutCacheChange() {
				
				public Object[] getInserted() {
					return null;
				}
				
				public Object[] getRemoved() {
					return null;
				}
				
				public Map getPreviousAttributes() {
					return null;
				}
				
				public Object getSource() {
					return this;
				}
				
				public Object[] getChanged() {
					return cells;
				}
				
				public Map getAttributes() {
					return null;
				}
				
				public Object[] getContext() {
					return null;
				}

				public Rectangle2D getDirtyRegion() {
					return null;
				}

				public void setDirtyRegion(Rectangle2D dirty) {
				}
				
			});
		}
	}

}
