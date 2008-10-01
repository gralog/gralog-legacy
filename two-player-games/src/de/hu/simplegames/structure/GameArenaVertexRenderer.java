package de.hu.simplegames.structure;

import java.awt.Component;
import java.awt.geom.Point2D;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.VertexView;

import de.hu.gralog.algorithm.result.DisplaySubgraph.DisplayMode;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;
import de.hu.gralog.jgraph.cellview.EllipseVertexRenderer;

public class GameArenaVertexRenderer extends DefaultVertexRenderer {
	
	private static final DefaultVertexRenderer rendererPlayer0 = new EllipseVertexRenderer(  );
	private static final DefaultVertexRenderer rendererPlayer1 = new DefaultVertexRenderer(  );
	
	public GameArenaVertexRenderer() {
	}
	
	protected GameArenaVertex getVertex( CellView view ) {
		DefaultGraphCell cell = (DefaultGraphCell)view.getCell();
		return (GameArenaVertex)cell.getUserObject();
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