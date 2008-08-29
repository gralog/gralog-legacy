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

package de.hu.gralog.gui.components;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;

import de.hu.gralog.app.UserException;
import de.hu.gralog.gui.MainPad;
import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingExecutionException;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;

public class OpenBrowserHyperlinkListener implements HyperlinkListener {

	public void hyperlinkUpdate(HyperlinkEvent e) {
		if ( e.getEventType() == EventType.ACTIVATED ) {
			try {
				new BrowserLauncher( null ).openURLinBrowser( e.getURL().toString() );
			} catch (BrowserLaunchingInitializingException e1) {
				MainPad.getInstance().handleUserException( new UserException( "unable to open default browser", e1 ) );
			} catch (BrowserLaunchingExecutionException e1) {
				MainPad.getInstance().handleUserException( new UserException( "unable to open default browser", e1 ) );
			} catch (UnsupportedOperatingSystemException e1) {
				MainPad.getInstance().handleUserException( new UserException( "unable to open default browser", e1 ) );
			}
			
		}
	}
	
}