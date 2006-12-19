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

package de.hu.gralog.demo;

import java.io.File;
import java.io.FileFilter;
import java.util.prefs.Preferences;

import de.hu.gralog.gui.MainPad;

/**
 * A demo applet that shows how to use JGraph to visualize JGraphT graphs.
 *
 * @author Barak Naveh
 *
 * @since Aug 3, 2003
 */
public class GamesApp {
	
	private static String getPluginDir( String[] args ) {
		Preferences prefs = Preferences.userNodeForPackage( MainPad.class );
		String defaultPluginDir = null;
		if ( args.length != 0 )
			defaultPluginDir = args[0];
		else {
			defaultPluginDir = System.getProperty( "user.home", null );
			if ( defaultPluginDir != null )
				defaultPluginDir = defaultPluginDir + "/.games/plugins";
		}
		String pluginDirectories = prefs.get( MainPad.PREFS_PLUGIN_DIRECTORIES, defaultPluginDir );
		prefs.put( MainPad.PREFS_PLUGIN_DIRECTORIES, pluginDirectories );
		return pluginDirectories;
	}
	
	private static String buildCP( File pluginDirectory ) {
		String cp = "";
		if ( pluginDirectory.canRead() && pluginDirectory.isDirectory() ) {
			File[] jarFiles = pluginDirectory.listFiles( new FileFilter() {
			
				public boolean accept(File pathname) {
					return pathname.isFile() && pathname.canRead() && pathname.getName().endsWith( ".jar" );
				}
				
			});
			
			for (File jarFile : jarFiles )
				cp = cp + jarFile.getAbsolutePath() + ":";
			if ( cp != "" )
				cp = cp.substring( 1, cp.length() - 1 );
		}
		return cp;
	}
	
	public static void main(String[] args) {
		MainPad.getInstance();
	}
}
