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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import javax.swing.JEditorPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import de.hu.gralog.app.UserException;


public class HTMLEditorPane extends JEditorPane {

	public HTMLEditorPane( String text ) {
		this();
		setText( text );
	}
	
	public HTMLEditorPane() {
		super();
		setEditable( false );
		HTMLEditorKit editorKit = new HTMLEditorKit();
		
		StyleSheet styles = editorKit.getStyleSheet();
		InputStream is = getClass().getResourceAsStream( "/de/hu/games/resources/base.css");
		if ( is != null ) {
			Reader r;
			try {
				r = new BufferedReader(
						new InputStreamReader(is, "ISO-8859-1"));
				styles.loadRules(r, null);
				r.close();
			} catch (UnsupportedEncodingException e) {
				MainPad.getInstance().handleUserException( new UserException( "falsches encoding der Datei base.css", e ) );
			} catch (IOException e) {
				MainPad.getInstance().handleUserException( new UserException( "Fehler beim Lesen von base.css", e ) );
			}
		}
		
        
//        Enumeration rules = styles.getStyleNames();
//        while (rules.hasMoreElements()) {
//            String name = (String) rules.nextElement();
//            Style rule = styles.getStyle(name);
//            System.out.println(rule.toString());
//        }
        setEditorKit( editorKit );
		addHyperlinkListener( new OpenBrowserHyperlinkListener() );
	}
}
