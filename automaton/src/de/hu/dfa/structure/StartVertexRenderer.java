package de.hu.dfa.structure;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.border.Border;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultGraphCell;

import de.hu.gralog.algorithm.result.DisplaySubgraph.DisplayMode;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;

public class StartVertexRenderer extends DefaultVertexRenderer {
	
	private boolean isFinalState;
	
	protected AutomatonVertex getVertex( CellView view ) {
		DefaultGraphCell cell = (DefaultGraphCell)view.getCell();
		return (AutomatonVertex)cell.getUserObject();
	}

	public Component getRendererComponent(JGraph graph, CellView view, boolean sel, boolean focus, boolean preview, DisplayMode displayMode ) {
		AutomatonVertex vertex = getVertex( view );
		isFinalState = vertex.isAcceptingState();
		return super.getRendererComponent( graph, view, sel, focus, preview, displayMode );
	}
	
	@Override
	public void paint(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		if (gradientColor != null && !preview && isOpaque()) {
			setOpaque(false);
			g2d.setPaint(new GradientPaint(0, 0, getBackground(),
					getWidth(), getHeight(), gradientColor, true));
			g2d.fillRect(0, 0, getWidth(), getHeight() );
		}
		Border saveBorder = getBorder();
		boolean saveOpaque = isOpaque();
		boolean saveSelected = selected;
		
		setBorder(null);
		setOpaque(true);
		selected = false;
		
		super.paint(g);
		
		setBorder( saveBorder );
		setOpaque( saveOpaque );
		selected = saveSelected;
		
		if (bordercolor != null) {
			g.setColor(bordercolor);
			g2d.setStroke(new BasicStroke( borderWidth ));
			g.drawRect(borderWidth - 1, borderWidth - 1, (int)getSize().getWidth() - borderWidth, (int)getSize().getHeight() - borderWidth );
			if ( isFinalState )	{	// draw inner line
				g.drawRect(borderWidth + 3, borderWidth + 3, (int)getSize().getWidth() - borderWidth - 8, (int)getSize().getHeight() - borderWidth - 8 );
			}
		}
		paintSelectionBorder( g );
	}
}
