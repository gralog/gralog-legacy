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

package de.hu.gralog.gui;

import java.beans.PropertyEditorSupport;

import org.jgrapht.Graph;

import de.hu.gralog.gui.document.Document;
import de.hu.gralog.gui.document.GJGraphDocumentContent;
import de.hu.gralog.gui.views.EditorDesktopViewListener;

public class ChooseVertexPropertyEditor extends PropertyEditorSupport implements EditorDesktopViewListener {

	private Graph graph = null;
	
	public ChooseVertexPropertyEditor(  ) {
		super( );
		updateGraph();
	}
	
	protected void updateGraph() {
		Document document = MainPad.getInstance().getDesktop().getCurrentDocument();
		
		if ( document != null && document.getContent() instanceof GJGraphDocumentContent )
			graph = document.getContent().getGraph().getGraphT();
	}

	@Override
	public String getAsText() {
		for ( Object vertex : graph.vertexSet() ) {
			if ( vertex == getValue() )
				return vertex.toString();
		}
		return null;
	}

	@Override
	public String[] getTags() {
		String[] tags = new String[graph.vertexSet().size()];
		int i = 0;
		for ( Object vertex : graph.vertexSet() )
			tags[i++] = vertex.toString();
		return tags;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		for ( Object vertex : graph.vertexSet() ) {
			if ( vertex.toString().equals( text ) ) {
				setValue( vertex );
				return;
			}
		}
		
		setValue( null );
	}

	public void currentDocumentSelectionChanged() {
		updateGraph();
	}
}
