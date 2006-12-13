/*
 * Created on 7 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.games.gui.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import de.hu.games.app.UserException;
import de.hu.games.graph.GraphTypeInfo;
import de.hu.games.graph.alg.Algorithm;
import de.hu.games.gui.MainPad;


public class Plugin {

	public class AlgorithmInfo {
		private String name;
		private Class<? extends Algorithm> type;
		private Algorithm algorithm;
		
		public AlgorithmInfo( String name, Class<? extends Algorithm> type ) throws UserException {
			this.name = name;
			this.type = type;
			try {
				this.algorithm = type.newInstance();
			} catch (InstantiationException e) {
				throw new UserException( "unable to load plugin", e );
			} catch (IllegalAccessException e) {
				throw new UserException( "unable to load plugin", e );
			}
		}
		
		public String getName() {
			return name;
		}
		
		public Class<? extends Algorithm> getType() {
			return type;
		}
		
		public Algorithm getAlgorithm() {
			return algorithm;
		}
		
		
		public String toString() {
			return Plugin.this.name + "." + name;
		}
		
	}
	
	private String name;
	private ArrayList<GraphTypeInfo> graphTypes = new ArrayList<GraphTypeInfo>();
	private ArrayList<AlgorithmInfo> algorithms = new ArrayList<AlgorithmInfo>();
		
	public Plugin( File file ) throws UserException {
		try {
			if ( ! file.exists() )
				throw new UserException( "unable to load plugin " + file, "The file does not exist" );
			if ( ! file.canRead() )
				throw new UserException( "unable to load plugin " + file, "The file is not readable" );
			if ( ! file.isFile() )
				throw new UserException( "unable to load plugin " + file, "The file is no file" );

			JarFile jarFile = new JarFile( file );
		
			ZipEntry entry = jarFile.getEntry( "plugin.config" );
			if ( entry == null )
				throw new UserException( "unable to load plugin " + file, "The file contains no plugin.config" );
		
			
			//MainPad.getInstance().getClassLoader().readJarFile( file.getAbsolutePath() );
			
			loadSettings( jarFile.getInputStream( entry ) );
		} catch( IOException e ) {
			throw new UserException( "unable to load jarFile " + file, e );
		} catch( JDOMException e ) {
			throw new UserException( "unable to load jarFile " + file, e );
		}
	}
	
	public Plugin( InputStream in ) throws UserException {
		try {
			loadSettings( in );
		} catch (JDOMException e) {
			throw new UserException( "unable to load plugin ", e );
		} catch (IOException e) {
			throw new UserException( "unable to load plugin ", e );
		}
	}
	
	protected void loadSettings( InputStream in ) throws JDOMException, IOException, UserException  {
		SAXBuilder parser = new SAXBuilder();
		loadSettings( parser.build( in ).getRootElement() );
	}
	
	protected void loadSettings( Element root ) throws UserException {
		name = root.getAttributeValue( "name" );
		Element graphs = root.getChild( "graphs" );
		if ( graphs != null )
			loadGraphsSettings( graphs );
		Element algorithms = root.getChild( "algorithms" );
		if ( algorithms != null )
			loadAlgorithmsSettings( algorithms );
	}
	
	protected void loadGraphsSettings( Element graphs ) throws UserException {
		for ( Element child : (List<Element>)graphs.getChildren() )
			loadGraphSettings( child );
	}
	
	protected void loadGraphSettings( Element graph ) throws UserException {
		String typeInfoClassName = graph.getAttributeValue( "typeInfoClass" );
		if ( typeInfoClassName == null )
			throw new UserException( "unable to load settings", "graphElement has no typeInfoClassattribute" );
		try {
			graphTypes.add( (GraphTypeInfo)MainPad.getInstance().getClassLoader().loadClass( typeInfoClassName ).newInstance() );
		} catch (InstantiationException e) {
			throw new UserException( "unable to load plugin", e );
		} catch (IllegalAccessException e) {
			throw new UserException( "unable to load plugin", e );
		} catch (ClassNotFoundException e) {
			throw new UserException( "unable to load plugin", e );
		}
	}
	
	protected void loadAlgorithmsSettings( Element algorithms ) throws UserException {
		for ( Element child : (List<Element>)algorithms.getChildren() )
			loadAlgorithmSettings( child );
	}
	
	protected void loadAlgorithmSettings( Element algorithm ) throws UserException {
		String name = algorithm.getAttributeValue( "name" );
		String typeName = algorithm.getAttributeValue( "class" );
		if ( name == null || typeName == null )
			throw new UserException( "unable to load plugin", "algorithm has no type or name attribute" );
		
		try {
			Class<? extends Algorithm> type = (Class<? extends Algorithm>)MainPad.getInstance().getClassLoader().loadClass( typeName );
			algorithms.add( new AlgorithmInfo( name, type ) );
		} catch (ClassNotFoundException e) {
			throw new UserException( "unable to load plugin", e );
		}
	}
	
	public ArrayList<GraphTypeInfo> getGraphTypes() {
		return graphTypes;
	}
	
	public ArrayList<AlgorithmInfo> getAlgorithms() {
		return algorithms;
	}
}
