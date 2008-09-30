package de.hu.logic.structure;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JLabel;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultGraphCell;

import de.hu.gralog.algorithm.result.DisplaySubgraph.DisplayMode;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;

public class TransitionSystemVertexRenderer extends DefaultVertexRenderer {

	private transient String propositionsText = null;

	private void setPropositionsText( CellView view ) {
		ArrayList<Proposition<? extends TransitionSystemVertex>> propositions = ((TransitionSystemVertex)((DefaultGraphCell)view.getCell()).getUserObject()).getPropositions();
		propositionsText = "";
		for ( Proposition proposition : propositions ) 
			propositionsText = propositionsText + proposition.getName() + ", ";
		if ( propositionsText.length() != 0 )
			propositionsText = propositionsText.substring( 0, propositionsText.length() - 2 );
		propositionsText += " ";
	}
	
	@Override
	public Component getRendererComponent(JGraph graph, CellView view, boolean sel, boolean focus, boolean preview, DisplayMode displayMode) {
		DefaultVertexRenderer component = (DefaultVertexRenderer)super.getRendererComponent(graph, view, sel, focus, preview, displayMode);
		setPropositionsText( view );
		return component;
	}

	@Override
	public void paint(Graphics g) {
		super.paint( g );
		JLabel label = new JLabel( propositionsText );

		label.setBounds( super.getBounds() );
		label.setVerticalAlignment( JLabel.TOP );
		label.setHorizontalAlignment( JLabel.RIGHT );
		Font font = label.getFont();
		label.setFont( font.deriveFont( (float)(font.getSize2D() / 1.6 ) ) );
		label.validate();
		label.paint( g );
		
	}
	
}
