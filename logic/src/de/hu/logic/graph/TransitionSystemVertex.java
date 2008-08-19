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

package de.hu.logic.graph;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JLabel;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultGraphCell;

import de.hu.gralog.graph.LabeledGraphVertex;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;
import de.hu.gralog.jgrapht.graph.DisplaySubgraph.DisplayMode;

/**
 * @author kreutzer
 *
 */
public class TransitionSystemVertex extends LabeledGraphVertex
{
	private static final TransitionSystemVertexRenderer RENDERER = new TransitionSystemVertexRenderer();
	
	private transient TransitionSystem graph;
	
	public TransitionSystemVertex()
	{
		super();
	}
	
	public TransitionSystemVertex(String label)
	{
		super( label );
	}
	
	void setGraph( TransitionSystem graph ) {
		this.graph = graph;
	}
		
	public ArrayList<Proposition> getPropositions() {
		return graph.getPropositions( this );
	}
	
	@Override
	public DefaultVertexRenderer getRenderer() {
		return RENDERER;
	}
	
	private static class TransitionSystemVertexRenderer extends DefaultVertexRenderer {

		private transient String propositionsText = null;

		private void setPropositionsText( CellView view ) {
			ArrayList<Proposition> propositions = ((TransitionSystemVertex)((DefaultGraphCell)view.getCell()).getUserObject()).getPropositions();
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
}
