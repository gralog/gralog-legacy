/*
 * Created on 7 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.gui.data;

import java.beans.Introspector;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hu.gralog.algorithm.Algorithm;
import de.hu.gralog.app.UserException;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.structure.StructureTypeInfo;

public class Plugin {

	static final Logger LOGGER = LoggerFactory.getLogger(Plugin.class);
	static final Logger AILOGGER = LoggerFactory.getLogger(AlgorithmInfo.class);

	public class AlgorithmInfo {
		private final String name;
		private final String path;
		private String description;
		private final Class<? extends Algorithm> type;
		private Algorithm algorithm;

		public AlgorithmInfo(final String name,
				final Class<? extends Algorithm> type) throws UserException {
			AILOGGER.debug("entering constructor with name {} and type {}",
					name == null ? "null" : name, type == null ? "null" : type);
			this.path = name;
			this.name = name;

			/*
			 * if ( path.indexOf( '.' ) != -1 ) this.name = name.substring(
			 * name.lastIndexOf( '.' ) + 1 ); else this.name = name;
			 */this.type = type;
			AILOGGER.debug("exiting constructor");
		}

		public String getName() {
			return this.name;
		}

		public Class<? extends Algorithm> getType() {
			return this.type;
		}

		public String getDescription() {
			return this.description;
		}

		public Algorithm getAlgorithm() {
			return this.algorithm;
		}

		public String getPath() {
			return Plugin.this.name + "." + this.path;
		}

		@Override
		public String toString() {
			return this.name;
		}

		void loadAlgorithm() throws UserException {
			AILOGGER.debug("entering loadAlgorithm()");
			try {
				this.algorithm = this.type.newInstance();
			}
			catch (final InstantiationException e) {
				throw new UserException("unable to load plugin", e);
			}
			catch (final IllegalAccessException e) {
				throw new UserException("unable to load plugin", e);
			}

			try {
				AILOGGER.debug("getting description from beaninfo for type: {}", type==null?"null":type);
				this.description = Introspector.getBeanInfo(this.type)
						.getBeanDescriptor().getShortDescription();
			}
			catch (final Throwable e) {
				throw new UserException("unable to load plugin", e);
			}
			AILOGGER.debug("exiting loadAlgorithm()");
		}
	}

	private String name;
	private String description;
	private final ArrayList<Class<StructureTypeInfo>> structureTypeClasses = new ArrayList<Class<StructureTypeInfo>>();
	private ArrayList<StructureTypeInfo> structureTypeInfos = null;
	private final ArrayList<AlgorithmInfo> algorithms = new ArrayList<AlgorithmInfo>();
	private boolean algorithmsLoaded = false;

	public Plugin(final File file) throws UserException {
		LOGGER.debug("entering contructor with file: {}", file == null ? "null"
				: file);
		try {
			if (!file.exists()) {
				throw new UserException("unable to load plugin " + file,
						"The file does not exist");
			}
			if (!file.canRead()) {
				throw new UserException("unable to load plugin " + file,
						"The file is not readable");
			}
			if (!file.isFile()) {
				throw new UserException("unable to load plugin " + file,
						"The file is no file");
			}

			final JarFile jarFile = new JarFile(file);

			final ZipEntry entry = jarFile.getEntry("plugin.config");
			if (entry == null) {
				jarFile.close();
				throw new UserException("unable to load plugin " + file,
						"The file contains no plugin.config");
			}

			MainPad.getInstance().readJarFile(jarFile);

			this.loadSettings(jarFile.getInputStream(entry));
		}
		catch (final IOException e) {
			throw new UserException("unable to load jarFile " + file, e);
		}
		catch (final JDOMException e) {
			throw new UserException("unable to load jarFile " + file, e);
		}
		LOGGER.debug("exiting constructor");
	}

	public Plugin(final InputStream in) throws UserException {
		LOGGER.debug("entering contructor with InputStream: {}",
				in == null ? "null" : in);
		try {
			this.loadSettings(in);
		}
		catch (final JDOMException e) {
			throw new UserException("unable to load plugin ", e);
		}
		catch (final IOException e) {
			throw new UserException("unable to load plugin ", e);
		}
		LOGGER.debug("exiting constructor");
	}

	protected void loadSettings(final InputStream in) throws JDOMException,
			IOException, UserException {
		LOGGER.debug("entering loadSettings(in) with InputStream: {}",
				in == null ? "null" : in);
		final SAXBuilder parser = new SAXBuilder();
		this.loadSettings(parser.build(in).getRootElement());
		LOGGER.debug("exiting loadSettings(in)");
	}

	protected void loadSettings(final Element root) throws UserException {
		LOGGER.debug("entering loadSettings(root) with Element: {}",
				root == null ? "null" : root);
		this.name = root.getAttributeValue("name");
		final Element description = root.getChild("description");
		if (description != null) {
			this.loadDescription(description);
		}
		final Element graphs = root.getChild("structures");
		if (graphs != null) {
			this.loadGraphsSettings(graphs);
		}
		final Element algorithms = root.getChild("algorithms");
		if (algorithms != null) {
			this.loadAlgorithmsSettings(algorithms);
		}
		LOGGER.debug("exiting loadSettings(root)");
	}

	protected void loadDescription(final Element description)
			throws UserException {
		this.description = description.getTextTrim();
	}

	protected void loadGraphsSettings(final Element graphs)
			throws UserException {
		for (final Element child : (List<Element>) graphs.getChildren()) {
			this.loadGraphSettings(child);
		}
	}

	protected void loadGraphSettings(final Element graph) throws UserException {
		LOGGER.debug("entering loadGraphSettings(graph) with Element: {}",
				graph == null ? "null" : graph);
		final String typeInfoClassName = graph
				.getAttributeValue("typeInfoClass");
		if (typeInfoClassName == null) {
			throw new UserException("unable to load settings",
					"structure-Element has no typeInfoClassattribute");
		}
		try {
			this.structureTypeClasses.add((Class<StructureTypeInfo>) MainPad
					.getInstance().getJarLoader().loadClass(typeInfoClassName));
		}
		catch (final ClassNotFoundException e) {
			throw new UserException("unable to load plugin", e);
		}
		LOGGER.debug("exiting loadGraphSettings() with Element");
	}

	protected void loadAlgorithmsSettings(final Element algorithms)
			throws UserException {
		LOGGER.debug(
				"entering loadAlgorithmsSettings(algorithms) with Element: {}",
				algorithms == null ? "null" : algorithms);
		for (final Element child : (List<Element>) algorithms.getChildren()) {
			this.loadAlgorithmSettings(child);
		}
		LOGGER.debug("exiting loadAlgorithmsSettings(algorithms)");
	}

	protected void loadAlgorithmSettings(final Element algorithm)
			throws UserException {
		LOGGER.debug(
				"entering loadAlgorithmSettings(algorithm) with Element: {}",
				this.algorithms == null ? "null" : this.algorithms);
		final String name = algorithm.getAttributeValue("name");
		final String typeName = algorithm.getAttributeValue("class");
		if (name == null || typeName == null) {
			throw new UserException("unable to load plugin",
					"algorithm has no type or name attribute");
		}

		try {
			final Class<? extends Algorithm> type = (Class<? extends Algorithm>) MainPad
					.getInstance().getJarLoader().loadClass(typeName);
			this.algorithms.add(new AlgorithmInfo(name, type));
		}
		catch (final ClassNotFoundException e) {
			throw new UserException("unable to load plugin", e);
		}
		LOGGER.debug("exiting loadAlgorithmSettings(algorithms)");
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public ArrayList<StructureTypeInfo> getStructureTypeClasses() {
		LOGGER.debug("entering getStructureTypeClasses()");
		if (this.structureTypeInfos == null) {
			this.structureTypeInfos = new ArrayList<StructureTypeInfo>();
			for (final Class<StructureTypeInfo> typeInfoClass : this.structureTypeClasses) {
				try {
					this.structureTypeInfos.add(typeInfoClass.newInstance());
				}
				catch (final InstantiationException e) {
					MainPad.getInstance().handleUserException(
							new UserException("unable to load plugin", e));
				}
				catch (final IllegalAccessException e) {
					MainPad.getInstance().handleUserException(
							new UserException("unable to load plugin", e));
				}
			}
		}

		LOGGER.debug("exiting getStructureTypeClasses() with return value: {}",
				this.structureTypeInfos == null ? "null"
						: this.structureTypeInfos);

		return this.structureTypeInfos;
	}

	public ArrayList<AlgorithmInfo> getAlgorithms() {
		LOGGER.debug("entering getAlgorithms()");
		if (!this.algorithmsLoaded) {
			for (final AlgorithmInfo info : this.algorithms) {
				try {
					info.loadAlgorithm();
				}
				catch (final UserException e) {
					MainPad.getInstance().handleUserException(e);
				}
			}
			this.algorithmsLoaded = true;
		}
		LOGGER.debug("exiting getAlgorithms() with return value: {}",
				this.algorithms == null ? "null" : this.algorithms);
		return this.algorithms;
	}
}
