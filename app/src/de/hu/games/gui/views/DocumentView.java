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

package de.hu.games.gui.views;

import net.infonode.docking.View;
import de.hu.games.gui.document.Document;
import de.hu.games.gui.document.DocumentListener;

public class DocumentView extends View implements DocumentListener {
	
	private Document document;
		
	public DocumentView( Document document ) {
		super( document.getTitle(), null, document.getContent().getComponent() );
		this.document = document;
		
		document.addDocumentListener( this );
	}
	
	public Document getDocument() {
		return document;
	}
	
	public void updateTitle() {
		getViewProperties().setTitle( document.getTitle() );
	}

	public void documentModifiedStatusChanged( Document document ) {
		updateTitle();
	}

	public void documentComponentReplaced( Document document ) {
		setComponent( document.getContent().getComponent() );
	}

	public void documentReverted(Document document) {
		
	}
}
