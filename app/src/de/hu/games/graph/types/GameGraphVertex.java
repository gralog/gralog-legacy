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

package de.hu.games.graph.types;

import java.awt.Component;
import java.awt.geom.Point2D;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.VertexView;

import de.hu.games.graph.LabeledGraphVertex;
import de.hu.games.jgraph.cellview.DefaultVertexRenderer;
import de.hu.games.jgraph.cellview.EllipseVertexRenderer;
import de.hu.games.jgrapht.graph.DisplaySubgraph.DisplayMode;

public class GameGraphVertex extends LabeledGraphVertex {

	private transient static final GameGraphVertexRenderer RENDERER = new GameGraphVertexRenderer();
	
	private boolean player0 = true;
	
	public GameGraphVertex() {
		super();
	}
	
	public GameGraphVertex( String label, boolean player0 ) {
		super( label );
		this.player0 = player0;
	}

	public boolean isPlayer0() {
		return player0;
	}

	public void setPlayer0(boolean player0) {
		boolean oldValue = this.player0;
		this.player0 = player0;
		firePropertyChange( "player0", oldValue, player0 );
	}
	
	@Override
	public DefaultVertexRenderer getRenderer() {
		return RENDERER;
	}
		
	private static class GameGraphVertexRenderer extends DefaultVertexRenderer {
		
		private static final DefaultVertexRenderer rendererPlayer0 = new EllipseVertexRenderer(  );
		private static final DefaultVertexRenderer rendererPlayer1 = new DefaultVertexRenderer(  );
		
		public GameGraphVertexRenderer() {
		}
		
		protected GameGraphVertex getVertex( CellView view ) {
			DefaultGraphCell cell = (DefaultGraphCell)view.getCell();
			return (GameGraphVertex)cell.getUserObject();
		}
		
		public Point2D getPerimeterPoint(VertexView view, Point2D source, Point2D p) {
			if ( getVertex( view ).isPlayer0() )
				return rendererPlayer0.getPerimeterPoint( view, source, p );
			return rendererPlayer1.getPerimeterPoint( view, source, p );
		}

		public Component getRendererComponent(JGraph graph, CellView view, boolean sel, boolean focus, boolean preview, DisplayMode displayMode ) {
			if ( getVertex( view ).isPlayer0() )
				return rendererPlayer0.getRendererComponent( graph, view, sel, focus, preview, displayMode );
			return rendererPlayer1.getRendererComponent( graph, view, sel, focus, preview, displayMode );
		}
		
	}
}
