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

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.border.Border;

import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexView;

public class EllipseVertexRenderer extends DefaultVertexRenderer {

	public Point2D getPerimeterPoint(VertexView view, Point2D source, Point2D p) {
		Rectangle2D r = view.getBounds();

		double x = r.getX();
		double y = r.getY();
		double a = (r.getWidth() + 1) / 2;
		double b = (r.getHeight() + 1) / 2;

		// x0,y0 - center of ellipse
		double x0 = x + a;
		double y0 = y + b;

		// x1, y1 - point
		double x1 = p.getX();
		double y1 = p.getY();

		// calculate straight line equation through point and ellipse center
		// y = d * x + h
		double dx = x1 - x0;
		double dy = y1 - y0;

		if (dx == 0)
			return new Point((int) x0, (int) (y0 + b * dy / Math.abs(dy)));

		double d = dy / dx;
		double h = y0 - d * x0;

		// calculate intersection
		double e = a * a * d * d + b * b;
		double f = -2 * x0 * e;
		double g = a * a * d * d * x0 * x0 + b * b * x0 * x0 - a * a * b * b;

		double det = Math.sqrt(f * f - 4 * e * g);

		// two solutions (perimeter points)
		double xout1 = (-f + det) / (2 * e);
		double xout2 = (-f - det) / (2 * e);
		double yout1 = d * xout1 + h;
		double yout2 = d * xout2 + h;

		double dist1 = Math.sqrt(Math.pow((xout1 - x1), 2)
				+ Math.pow((yout1 - y1), 2));
		double dist2 = Math.sqrt(Math.pow((xout2 - x1), 2)
				+ Math.pow((yout2 - y1), 2));

		// correct solution
		double xout, yout;

		if (dist1 < dist2) {
			xout = xout1;
			yout = yout1;
		} else {
			xout = xout2;
			yout = yout2;
		}

		return view.getAttributes().createPoint(xout, yout);
	}

	/**
	 * Return a slightly larger preferred size than for a rectangle.
	 */
	public Dimension getPreferredSize() {
		Dimension d = super.getOriginalPS();
		d.width += d.width / 8;
		d.height += d.height / 2;
		d.width = (int)((Math.floor((d.width / gridsize)-0.01) + 1) * gridsize - 1);
		d.height = (int)((Math.floor((d.height / gridsize)-0.01) + 1) * gridsize - 1);

		return d;
	}

	public void paint(Graphics g) {
		// int b = borderWidth;
		// Graphics2D g2 = (Graphics2D) g;
		// Dimension d = getSize();
		// boolean tmp = selected;
		Graphics2D g2d = (Graphics2D) g;
		if (gradientColor != null && !preview && isOpaque()) {
			setOpaque(false);
			g2d.setPaint(new GradientPaint(0, 0, getBackground(), getWidth(),
					getHeight(), gradientColor, true));
			g2d.fillOval(0, 0, getWidth(), getHeight());
		}
		Border saveBorder = getBorder();
		boolean saveOpaque = isOpaque();
		boolean saveSelected = selected;

		setBorder(null);
		setOpaque(true);
		selected = false;

		super.paint(g);

		setBorder(saveBorder);
		setOpaque(saveOpaque);
		selected = saveSelected;

		if (bordercolor != null) {
			g.setColor(bordercolor);
			g2d.setStroke(new BasicStroke(borderWidth));
			g.drawOval(borderWidth - 1, borderWidth - 1, (int) getSize()
					.getWidth()
					- borderWidth, (int) getSize().getHeight() - borderWidth);
		}
		paintSelectionBorder(g);
	}

	/**
	 * Provided for subclassers to paint a selection border.
	 */
	protected void paintSelectionBorder(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Stroke previousStroke = g2.getStroke();
		g2.setStroke(GraphConstants.SELECTION_STROKE);
		if (childrenSelected || selected) {
			if (childrenSelected)
				g.setColor(gridColor);
			else if (hasFocus && selected)
				g.setColor(lockedHandleColor);
			else if (selected)
				g.setColor(highlightColor);
			Dimension d = getSize();
			g.drawOval(0, 0, d.width - 1, d.height - 1);
		}
		g2.setStroke(previousStroke);
	}
}
