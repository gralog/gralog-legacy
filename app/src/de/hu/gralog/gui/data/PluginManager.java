/*
 * Created on 7 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.gui.data;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.ArrayList;

import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.GraphTypeInfo;
import de.hu.gralog.gui.data.Plugin.AlgorithmInfo;

public class PluginManager {

	private ArrayList<Plugin> plugins = new ArrayList<Plugin>();
	
	public PluginManager() {
		
	}
	
	public void loadPlugins( File directory ) throws UserException {
		if ( ! directory.exists() )
			throw new UserException( "unable to load plugins from directory " + directory, "directory does not exists" );
		if ( ! directory.canRead() )
			throw new UserException( "unable to load plugins from directory " + directory, "directory is not readable" );
		if ( ! directory.isDirectory() )
			throw new UserException( "unable to load plugins from directory " + directory, "directory is no directory" );
		
		File[] jarFiles = directory.listFiles( new FileFilter() {

			public boolean accept(File pathname) {
				return pathname.canRead() && pathname.isFile() && pathname.getName().endsWith( ".jar" );
			}
			
		});
		
		for ( File file : jarFiles )
			plugins.add( new Plugin( file ) );
	}
	
	public void loadPlugin( InputStream in ) throws UserException {
		plugins.add( new Plugin( in ) );
	}
	
	public ArrayList<GraphTypeInfo> getGraphTypes() {
		ArrayList<GraphTypeInfo> graphTypes = new ArrayList<GraphTypeInfo>();
		
		for ( Plugin plugin : plugins )
			graphTypes.addAll( plugin.getGraphTypes() );
		return graphTypes;
	}
	
	public ArrayList<AlgorithmInfo> getAlgorithms() {
		ArrayList<AlgorithmInfo> algorithms = new ArrayList<AlgorithmInfo>();
		
		for ( Plugin plugin : plugins )
			algorithms.addAll( plugin.getAlgorithms() );
		return algorithms;
	}
}
