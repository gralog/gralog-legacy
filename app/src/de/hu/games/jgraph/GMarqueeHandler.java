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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import org.jgraph.JGraph;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.CellView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.PortView;
import org.jgraph.graph.VertexView;

import de.hu.games.gui.MainPad;
import de.hu.games.gui.MainPad.EditorState;

public class GMarqueeHandler extends BasicMarqueeHandler {

	private GJGraph graph;
	private PortView port, startport;
	private Point2D point, edgeEndPoint, centerPoint, centerPointViewport, panStartPoint, panStartViewPosition;
	private boolean edgeAdded = false;
	private double startScale;
	
	@Override
	public boolean isForceMarqueeEvent(MouseEvent event) {
		return MainPad.getInstance().getEditorState() != EditorState.SELECT;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if ( e.getButton() != MouseEvent.BUTTON1 ) {
			return;
		}
		
		graph = (GJGraph) e.getSource();
		Graphics g = graph.getGraphics();
		Color bg = graph.getBackground();
		Color fg = graph.getMarqueeColor();
		
		
		if ( MainPad.getInstance().getEditorState() == EditorState.CREATE_VERTEX ) {
			graph.insertVertexAt( point );
		}
		if ( MainPad.getInstance().getEditorState() == EditorState.CREATE_EDGE ) {
			if ( port != null ) {
				if (startport == null )
					startport = port;
				else {
					if ( port != startport ) {
						graph.addEdge( startport.getParentView().getCell(), port.getParentView().getCell() );
						startport = null;
						edgeEndPoint = null;
						port = null;
						edgeAdded = true;
					} else {
						startport = null;
						edgeEndPoint = null;
					}
				}
			} else {
				g.setColor(fg);
				g.setXORMode(bg);
				overlay( graph, g, true );
				startport = null;
				edgeEndPoint = null;
			}
			
		}
		if ( MainPad.getInstance().getEditorState() == EditorState.PAN && panStartPoint == null ) {
			graph.setCursor( EditorState.PAN.getSecondCursor() );
			panStartPoint = getViewportCoor(graph, (Point)e.getPoint().clone());
			panStartViewPosition = (Point)((JViewport)graph.getParent()).getViewPosition().clone();
		}
		if ( MainPad.getInstance().getEditorState() == EditorState.INTERACTIVE_ZOOM && centerPoint == null ) {
			JViewport viewport = (JViewport)graph.getParent();
			centerPoint = graph.fromScreen( (Point2D)graph.getCenterPoint().clone() );
			centerPointViewport = getViewportCoor( graph, (Point)e.getPoint().clone() );
			startScale = graph.getScale(); 
		}
		if ( MainPad.getInstance().getEditorState() == EditorState.SELECT || MainPad.getInstance().getEditorState() == EditorState.MARQUE_ZOOM ) {
			super.mousePressed(e);
		} else 
			e.consume();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if ( e.getButton() != MouseEvent.BUTTON1 ) {
			return;
		}
		
		if ( MainPad.getInstance().getEditorState() == EditorState.CREATE_EDGE ) {
			if (edgeAdded) {
				mouseMoved( e );
				edgeAdded = false;
			}
		}
		if ( MainPad.getInstance().getEditorState() == EditorState.INTERACTIVE_ZOOM ) {
			centerPoint = null;
		}
		if ( MainPad.getInstance().getEditorState() == EditorState.PAN ) {
			graph.setCursor( EditorState.PAN.getCursor() );
			panStartPoint = null;
		}
		if ( MainPad.getInstance().getEditorState() == EditorState.SELECT || MainPad.getInstance().getEditorState() == EditorState.MARQUE_ZOOM) {
			super.mouseReleased(e);
		} else
			e.consume();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		graph = (GJGraph) e.getSource();
		point = e.getPoint();

		if ( MainPad.getInstance().getEditorState() == EditorState.INTERACTIVE_ZOOM && centerPoint != null ) {
			JViewport viewport = (JViewport)graph.getParent();
			Point2D pointViewport = getViewportCoor( graph, (Point)e.getPoint().clone() );
			double newScale = startScale + ( pointViewport.getY() - centerPointViewport.getY() ) / 100.0;
			if ( newScale != graph.getScale() && newScale > 0 ) {
				graph.setScale( newScale );
				Point2D cps = graph.toScreen( (Point2D)centerPoint.clone() );
				int x = (int)(cps.getX()- viewport.getExtentSize().getWidth() / 2);
				int y = (int)(cps.getY() - viewport.getExtentSize().getHeight() / 2);
				int w = (int)viewport.getExtentSize().getWidth();
				int h = (int)viewport.getExtentSize().getHeight();

				graph.scrollRectToVisible( new Rectangle( x, y, w, h ) );
			}
		}
		if ( MainPad.getInstance().getEditorState() == EditorState.PAN && panStartPoint != null ) {
			JViewport viewport = (JViewport)graph.getParent();
			
			int dx = (int)(panStartPoint.getX() - getViewportCoor( graph, (Point)e.getPoint().clone() ).getX());
			int dy = (int)(panStartPoint.getY() - getViewportCoor( graph, (Point)e.getPoint().clone() ).getY());

			if ( dx != 0 || dy != 0 ) {
				int x = (int)panStartViewPosition.getX() + dx;
				int y = (int)panStartViewPosition.getY() + dy;
			
				graph.scrollRectToVisible( new Rectangle(x, y, (int)viewport.getExtentSize().getWidth(), (int)viewport.getExtentSize().getHeight() ));
			}
		}
		if ( MainPad.getInstance().getEditorState() == EditorState.SELECT || MainPad.getInstance().getEditorState() == EditorState.MARQUE_ZOOM ) {
			super.mouseDragged(e);
		} else
			e.consume();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		graph = (GJGraph) e.getSource();
		
		point = e.getPoint();
		Graphics g = graph.getGraphics();
		Color bg = graph.getBackground();
		Color fg = graph.getMarqueeColor();

		if ( MainPad.getInstance().getEditorState() == EditorState.CREATE_EDGE ) {
			PortView newPort = null;
			CellView view = graph.getNextSelectableViewAt( null, point.getX(), point.getY() );
			if ( view instanceof VertexView )
				newPort = graph.getDefaultPortForCell( view.getCell() );
			if ( newPort != port || startport != null ) {
				g.setColor(fg);
				g.setXORMode(bg);
				overlay( graph, g, true );
				port = newPort;
				edgeEndPoint = point;
				g.setColor(fg);
				g.setXORMode(bg);
				overlay( graph, g, false );
			}
		}
		if ( MainPad.getInstance().getEditorState() == EditorState.SELECT || MainPad.getInstance().getEditorState() == EditorState.MARQUE_ZOOM ) {
			super.mouseMoved(e);

		} 
		e.consume();
	}
	
	@Override
	public void handleMarqueeEvent(MouseEvent e, JGraph graph, Rectangle2D bounds) {
		if ( MainPad.getInstance().getEditorState() == EditorState.MARQUE_ZOOM ) {
			final JViewport viewport = (JViewport)graph.getParent();
			final JGraph graphFinal = graph;
			final Rectangle2D boundsFinal = bounds;
			
			Dimension viewSize = viewport.getExtentSize();
			double scale = Math.min( viewSize.getWidth() / bounds.getWidth(), viewSize.getHeight() / bounds.getHeight() );
			graph.setScale( scale );
			SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					Rectangle2D	screenBounds = graphFinal.toScreen( (Rectangle2D) boundsFinal.clone() );
					viewport.setViewPosition( new Point( (int)screenBounds.getX(), (int)screenBounds.getY() ) );
				}
			});
			
		} else 
			super.handleMarqueeEvent(e, graph, bounds);
	}

	protected void paintPort() {
		if ( port == null )
			return;
		boolean offset = (GraphConstants.getOffset(port.getAllAttributes()) != null);
		Rectangle2D bounds = !offset ? port.getParentView().getBounds() : port.getBounds();
		bounds = graph.toScreen(new Rectangle(bounds
				.getBounds()));
		int s = 3;
		bounds.setRect((int) bounds.getX() - s, (int) bounds.getY() - s, (int) (bounds.getWidth() + 2 * s), (int) (bounds.getHeight() + 2 * s));
		graph.getUI().paintCell( graph.getGraphics(), port, bounds, true );
	}

	@Override
	public void overlay(JGraph graph, Graphics g, boolean clear) {
		if ( MainPad.getInstance().getEditorState() == EditorState.CREATE_EDGE ) {
			paintPort();
			if ( startport != null  && port != startport ) {
				if ( port != null ) {
					Point2D from = graph.toScreen( startport.getParentView().getPerimeterPoint( null, startport.getLocation(), port.getLocation() ) );
					Point2D to = graph.toScreen( port.getParentView().getPerimeterPoint( null, port.getLocation(), startport.getLocation() ) );
					drawEdge( (GJGraph) graph, g, from.getX(), from.getY(), to.getX(), to.getY() );
				}
				else {
					if ( edgeEndPoint != null ) {
						Point2D from = graph.toScreen( startport.getParentView().getPerimeterPoint( null, startport.getLocation(), graph.fromScreen( edgeEndPoint ) ) );
						Point2D to = graph.toScreen( edgeEndPoint );
						drawEdge( (GJGraph) graph, g, from.getX(), from.getY(), to.getX(), to.getY() );
					}
				}
			}
		}
		if ( MainPad.getInstance().getEditorState() == EditorState.SELECT || MainPad.getInstance().getEditorState() == EditorState.MARQUE_ZOOM ) {
			super.overlay(graph, g, clear);
		}
	}
	
	protected void drawEdge( GJGraph graph, Graphics g, double x1, double y1, double x2, double y2 ) {
		g.drawLine( (int)x1, (int)y1, (int)x2, (int)y2 );
	}
	
	protected Point2D getViewportCoor( GJGraph graph, Point2D point ) {
		Point viewPosition = (Point)((JViewport)graph.getParent()).getViewPosition().clone();
		point.setLocation( point.getX() - viewPosition.getX(), point.getY() - viewPosition.getY() );
		return point;
	}
}
