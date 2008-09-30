/*
 * Created on 2008 by Sebastian Ordyniak
 *
 * Copyright 2008 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of GrALoG.
 *
 * GrALoG is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * GrALoG is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GrALoG; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */
package de.hu.gralog.jgraph.cellview;

import java.awt.Component;

import javax.swing.Renderer;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;

import de.hu.gralog.algorithm.result.DisplaySubgraph.DisplayMode;

/**
 * 
 * This interface extends the basic {@link Renderer JGraphRenderer} in order to
 * allow a display-mode-dependent display of CellViews. The feature is needed to
 * highlight special components of a GrALoG-structure.
 * 
 * @author Sebastian
 * 
 */

public interface DisplayModeRenderer extends CellViewRenderer {
	public Component getRendererComponent(JGraph graph, CellView view,
			boolean sel, boolean focus, boolean preview, DisplayMode displayMode);
}
