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
import java.util.ArrayList;

import de.hu.gralog.graph.GraphTypeInfo;
import de.hu.gralog.gui.document.Document;
import de.hu.gralog.gui.document.GJGraphDocumentContent;
import de.hu.gralog.gui.views.EditorDesktopViewListener;

public class ChooseGraphPropertyEditor extends PropertyEditorSupport implements EditorDesktopViewListener {

	private ArrayList<Document> documents = new ArrayList<Document>();
	private GraphTypeInfo graphType = null;
	
	
	
	public ChooseGraphPropertyEditor( GraphTypeInfo graphType ) {
		super( );
		this.graphType = graphType;
		updateDocuments();
	}
	
	protected void updateDocuments() {
		documents.clear();
		for ( Document document : MainPad.getInstance().getDesktop().getOpenDocuments() ) {
			if ( document.getContent() instanceof GJGraphDocumentContent ) {
				GJGraphDocumentContent content = (GJGraphDocumentContent)document.getContent();
				if ( graphType.isInstance( content.getGraph().getGraphT() ) )
					documents.add( document );
			}
		}
	}

	@Override
	public String getAsText() {
		for ( Document document : documents ) {
			if ( document.getGraph().getGraphT() == getValue() )
				return document.getName();
		}
		return null;
	}

	@Override
	public String[] getTags() {
		String[] tags = new String[documents.size()];
		for ( int i = 0; i < tags.length; i++ )
			tags[i] = documents.get( i ).getName();
		return tags;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		for ( Document document : documents ) {
			if ( document.getName().equals( text ) ) {
				setValue( document.getGraph().getGraphT() );
				return;
			}
		}
		setValue( null );
	}

	public void currentDocumentSelectionChanged() {
		updateDocuments();
	}
}
