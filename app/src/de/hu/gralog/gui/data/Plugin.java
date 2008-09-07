/*
 * Created on 7 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.gui.data;

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

import de.hu.gralog.algorithm.Algorithm;
import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.GralogGraphTypeInfo;
import de.hu.gralog.gui.MainPad;


public class Plugin {

	public class AlgorithmInfo {
		private String name;
		private Class<? extends Algorithm> type;
		private Algorithm algorithm;
		
		public AlgorithmInfo( String name, Class<? extends Algorithm> type ) throws UserException {
			this.name = name;
			this.type = type;
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
		
		public String getPath() {
			return Plugin.this.name + "." + name;
		}
		
		public String toString() {
			return name;
		}
		
		void loadAlgorithm() throws UserException {
			try {
				this.algorithm = type.newInstance();
			} catch (InstantiationException e) {
				throw new UserException( "unable to load plugin", e );
			} catch (IllegalAccessException e) {
				throw new UserException( "unable to load plugin", e );
			}
		}
	}
	
	private String name;
	private ArrayList<Class<GralogGraphTypeInfo>> graphTypeClasses = new ArrayList<Class<GralogGraphTypeInfo>>();
	private ArrayList<GralogGraphTypeInfo> graphTypeInfos = null;
	private ArrayList<AlgorithmInfo> algorithms = new ArrayList<AlgorithmInfo>();
	private boolean algorithmsLoaded = false;
		
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
		
			
			MainPad.getInstance().readJarFile( jarFile );
			
			
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
			graphTypeClasses.add( (Class<GralogGraphTypeInfo>)MainPad.getInstance().getJarLoader().loadClass( typeInfoClassName ) );
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
			Class<? extends Algorithm> type = (Class<? extends Algorithm>)MainPad.getInstance().getJarLoader().loadClass( typeName );
			algorithms.add( new AlgorithmInfo( name, type ) );
		} catch (ClassNotFoundException e) {
			throw new UserException( "unable to load plugin", e );
		}
	}
	
	public ArrayList<GralogGraphTypeInfo> getGraphTypeClasses() {
		if ( graphTypeInfos == null ) {
			graphTypeInfos = new ArrayList<GralogGraphTypeInfo>();
			for ( Class<GralogGraphTypeInfo> typeInfoClass : graphTypeClasses ) {
				try {
					graphTypeInfos.add( typeInfoClass.newInstance() );
				} catch (InstantiationException e) {
					MainPad.getInstance().handleUserException( new UserException( "unable to load plugin", e ) );
				} catch (IllegalAccessException e) {
					MainPad.getInstance().handleUserException( new UserException( "unable to load plugin", e ) );
				}
			}
		}
		
		return graphTypeInfos;
	}
	
	public ArrayList<AlgorithmInfo> getAlgorithms() {
		if ( ! algorithmsLoaded ) {
			for ( AlgorithmInfo info : algorithms ) {
				try {
					info.loadAlgorithm();
				} catch (UserException e) {
					MainPad.getInstance().handleUserException( e );
				}
			}
			algorithmsLoaded = true;
		}
		return algorithms;
	}
}
