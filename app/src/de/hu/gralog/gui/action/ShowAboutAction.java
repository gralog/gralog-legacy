/*
 * Created on 2006 by Sebastian Ordyniak
 *
 * Copyright 2006 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of Gralog.
 *
 * Gralog is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * Gralog is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Gralog; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */

package de.hu.gralog.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.hu.gralog.gui.MainPad;

public class ShowAboutAction extends AbstractAction {

	public ShowAboutAction(  ) {
		super( "About", null);
		super.putValue( SHORT_DESCRIPTION, "Show AboutDialog" );
	}

	public void actionPerformed(ActionEvent arg0) {
		JTextArea text = new JTextArea( 
				"Gralog version 1.0, Copyright (C) 2006 Sebastian Ordyniak and Stephan Kreutzer \n" +
				"\n" +
				"Gralog is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License \n" + 
				"as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version. \n" +
				"\n" +
				"Gralog is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; \n" + 
				"without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. \n" + 
				"See the GNU General Public License for more details. \n" +
				"\n" +
				"You should have received a copy of the GNU General Public License along with Gralog; \n" + 
				"if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA" 
                );
		
		text.setRows( 5 );
		text.setEditable( false );
		
		JOptionPane.showMessageDialog(
                MainPad.getInstance(),
                new JScrollPane( text ),
                "About",
                JOptionPane.INFORMATION_MESSAGE );
	}

}
