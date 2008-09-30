package de.hu.example.structure;

import java.awt.Component;
import java.awt.geom.Point2D;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.VertexView;

import de.hu.gralog.algorithm.result.DisplaySubgraph.DisplayMode;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;
import de.hu.gralog.jgraph.cellview.EllipseVertexRenderer;

/**
 * This is an example for how to implement your own Renderer in GrAloG.
 * Please see {@link de.hu.gralog.jgraph.cellview} for more
 * information on how to implement your own Renderers.
 * 
 * This example displays a {@link SimpleVertex}. It 
 * displays the vertex as an Ellipse if {@link SimpleVertex#isTheGoodVertex()}
 * returns true ( using 
 * {@link de.hu.gralog.jgraph.cellview.EllipseVertexRenderer}, 
 * otherwise it uses {@link de.hu.gralog.jgraph.cellview.DefaultVertexRenderer}
 * to display the vertex as a Rectangle. 
 * 
 * @author Sebastian
 *
 */
public class SimpleVertexVertexRenderer extends DefaultVertexRenderer {

	private static final EllipseVertexRenderer GOOD_VERTEX_RENDERER = new EllipseVertexRenderer();
	
	private boolean isGoodVertex = false;
	
	protected SimpleVertex getVertex( CellView view ) {
		DefaultGraphCell cell = (DefaultGraphCell)view.getCell();
		return (SimpleVertex)cell.getUserObject();
	}
	
	@Override
	public Point2D getPerimeterPoint(VertexView view, Point2D p1, Point2D p2) {
		if ( getVertex( view ).isTheGoodVertex() )
			return GOOD_VERTEX_RENDERER.getPerimeterPoint( view, p1, p2 );
		return super.getPerimeterPoint( view, p1, p2 );
	}

	@Override
	public Component getRendererComponent(JGraph graph, CellView view, boolean arg2, boolean arg3, boolean arg4, DisplayMode arg5) {
		if ( getVertex( view ).isTheGoodVertex() )
			return GOOD_VERTEX_RENDERER.getRendererComponent( graph, view, arg2, arg3, arg4, arg5 );
		return super.getRendererComponent( graph, view, arg2, arg3, arg4, arg5);
	}

	

}
