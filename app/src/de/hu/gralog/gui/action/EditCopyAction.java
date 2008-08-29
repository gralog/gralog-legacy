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
import de.hu.gralog.jgraph.GGraphTransferHandler;

public class EditCopyAction extends AbstractAction {
	
	public EditCopyAction(  ) {
		super( "copy", MainPad.createImageIcon( "designer_d_editcopy.png" ) );
	}
	
	public void actionPerformed(ActionEvent e) {
		GGraphTransferHandler.getCopyAction().actionPerformed(
				new ActionEvent(MainPad.getInstance().getDesktop().getCurrentGraph(), e.getID() , e.getActionCommand() ));
	}
	
}
