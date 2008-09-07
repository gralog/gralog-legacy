/*
 * Created on 2008 by Sebastian Ordyniak
 *
 * Copyright 2008 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of Gralog.
 *
 * Gralog is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * Gralog is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Gralog; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */
package de.hu.gralog.jgraph.cellview;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.UIManager;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;

import de.hu.gralog.algorithm.result.DisplaySubgraph.DisplayMode;

/**
 * 
 * This static class provides default attributes for Edge- and VertexRenderers.
 * 
 * @author Sebastian
 * 
 */

public class ElementAttributes {

	private static final Hashtable EDGE_ATTRIBUTES = new Hashtable();

	private static final Hashtable DIRECTED_EDGE_ATTRIBUTES = new Hashtable();

	private static final Hashtable VERTEX_ATTRIBUTES = new Hashtable();

	public static AttributeMap getVertexAttributes() {

		AttributeMap map = (AttributeMap) VERTEX_ATTRIBUTES
				.get(DisplayMode.SHOW);
		if (map != null)
			return map;

		map = new AttributeMap();

		// GraphConstants.setBounds(map, new Rectangle2D.Double(50, 50, 40,
		// 40));

		GraphConstants.setBackground(map, Color.white);
		GraphConstants.setBorderColor(map, Color.black);
		GraphConstants.setLineWidth(map, 2);
		GraphConstants.setBorder(map, BorderFactory.createLineBorder(
				GraphConstants.getBorderColor(map), (int) GraphConstants
						.getLineWidth(map)));

		GraphConstants.setForeground(map, Color.black);
		GraphConstants.setFont(map, GraphConstants.DEFAULTFONT.deriveFont(
				Font.BOLD, 12));
		GraphConstants.setOpaque(map, true);
		GraphConstants.setAutoSize(map, false);

		VERTEX_ATTRIBUTES.put(DisplayMode.SHOW, map);
		return map;
	}

	public static AttributeMap getEdgeAttributes(Graph graph) {
		AttributeMap map;

		if (graph instanceof DirectedGraph)
			map = (AttributeMap) DIRECTED_EDGE_ATTRIBUTES.get(DisplayMode.SHOW);
		else
			map = (AttributeMap) EDGE_ATTRIBUTES.get(DisplayMode.SHOW);

		if (map != null)
			return map;

		map = new AttributeMap();

		GraphConstants.setLineBegin(map, GraphConstants.ARROW_NONE);
		GraphConstants.setBeginSize(map, 0);
		GraphConstants.setBeginFill(map, false);

		if (graph instanceof DirectedGraph) {
			GraphConstants.setLineEnd(map, GraphConstants.ARROW_TECHNICAL);
			GraphConstants.setEndSize(map, 10);
			GraphConstants.setEndFill(map, true);
		} else {
			GraphConstants.setLineEnd(map, GraphConstants.ARROW_NONE);
			GraphConstants.setEndSize(map, 0);
			GraphConstants.setEndFill(map, false);
		}

		GraphConstants.setLineWidth(map, 1);

		GraphConstants.setLineStyle(map, GraphConstants.STYLE_BEZIER);
		GraphConstants.setRouting(map, GraphConstants.ROUTING_DEFAULT);
		GraphConstants.setBorderColor(map, Color.BLACK);
		GraphConstants.setForeground(map, Color.BLACK);
		GraphConstants.setBackground(map, UIManager
				.getColor("Tree.textBackground"));
		GraphConstants.setOpaque(map, false);
		GraphConstants.setFont(map, GraphConstants.DEFAULTFONT.deriveFont(
				Font.BOLD, 12));
		GraphConstants.setLabelAlongEdge(map, false);

		GraphConstants.setLineColor(map, Color.BLACK);

		if (graph instanceof DirectedGraph)
			DIRECTED_EDGE_ATTRIBUTES.put(DisplayMode.SHOW, map);
		else
			EDGE_ATTRIBUTES.put(DisplayMode.SHOW, map);

		return map;
	}

}