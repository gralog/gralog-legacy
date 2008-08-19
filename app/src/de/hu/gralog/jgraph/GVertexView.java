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
import java.awt.geom.Point2D;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.VertexView;


public class GVertexView extends VertexView {

	protected transient GJGraph graph;
	
	public GVertexView(GJGraph graph, Object cell) {
		super(cell);
		this.graph = graph;
	}

	public Point2D getPerimeterPoint(EdgeView view, Point2D source, Point2D p) {
		return graph.getVertexRenderer().getPerimeterPoint( this, source, p );
	}

	public Component getRendererComponent(JGraph graph, boolean selected, boolean focus, boolean preview ) {
		return this.graph.getVertexRenderer().getRendererComponent( this.graph, this, selected, focus, preview, this.graph.getCellDisplayMode( ((DefaultGraphCell)getCell()).getUserObject()) );
	}

/*	@Override
	protected AttributeMap createAttributeMap() {
		return AttributeMap.emptyAttributeMap;
	}*/
}
