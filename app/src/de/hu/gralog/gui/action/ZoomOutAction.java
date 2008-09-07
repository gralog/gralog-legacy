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

package de.hu.gralog.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.hu.gralog.gui.MainPad;

public class ZoomOutAction extends AbstractAction {

	public ZoomOutAction(  ) {
		super( "Zoom Out", MainPad.createImageIcon( "stock_zoom-out.png") );
		super.putValue( SHORT_DESCRIPTION, "Zoom Out" );
	}
	
	public void actionPerformed(ActionEvent arg0) {
		MainPad.getInstance().getDesktop().getCurrentGraph().setScale( MainPad.getInstance().getDesktop().getCurrentGraph().getScale() / 2 );
	}

}
