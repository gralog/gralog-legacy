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

import java.awt.Component;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.EdgeRenderer;

import de.hu.gralog.algorithm.result.DisplaySubgraph.DisplayMode;

/**
 * 
 * This class should be overriden in order to implement your own EdgeRenderer.
 * It extends/changes the behavior of the JGraph-{@link EdgeRenderer EdgeRenderer}
 * by implementing {@link DisplayModeRenderer DisplayModeRenderer}.
 * 
 * @author Sebastian
 * 
 */
public class DefaultEdgeRenderer extends EdgeRenderer implements
		DisplayModeRenderer {

	public DefaultEdgeRenderer() {

	}

	public Component getRendererComponent(JGraph graph, CellView view,
			boolean sel, boolean focus, boolean preview, DisplayMode displayMode) {
		if (displayMode == DisplayMode.HIDE)
			return null;
		getRendererComponent(graph, view, sel, focus, preview);
		if (displayMode.getColor() != null)
			setForeground(displayMode.getColor());
		return this;
	}
}
