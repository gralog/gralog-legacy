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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hu.gralog.app.UserException;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.gui.data.Plugin.AlgorithmInfo;
import de.hu.gralog.structure.StructureTypeInfo;

public class PluginManager {

	static final Logger logger = LoggerFactory.getLogger(PluginManager.class);

	private final ArrayList<Plugin> plugins = new ArrayList<Plugin>();

	public PluginManager() {

	}

	public void loadPlugins(final File directory) throws UserException {
		logger.debug("entering loadPlugin() with file: {} " + directory);
		if (!directory.exists()) {
			throw new UserException("unable to load plugins from directory "
					+ directory, "directory does not exists");
		}
		if (!directory.canRead()) {
			throw new UserException("unable to load plugins from directory "
					+ directory, "directory is not readable");
		}
		if (!directory.isDirectory()) {
			throw new UserException("unable to load plugins from directory "
					+ directory, "directory is no directory");
		}

		final File[] jarFiles = directory.listFiles(new FileFilter() {

			public boolean accept(final File pathname) {
				return pathname.canRead() && pathname.isFile()
						&& pathname.getName().endsWith(".jar");
			}

		});

		// FIXME: this code throws an exception for every jar that is in a
		// plugin folder, but that is no plugin. Should simply ignore them and
		// maybe give a warning in a logfile
		for (final File file : jarFiles) {
			try {
				this.plugins.add(new Plugin(file));
				logger.info("loaded Plugin from file: {}", file);
			}
			catch (final UserException e) {
				MainPad.getInstance().handleUserException(e);
			}
		}
		logger.debug("exiting loadPlugin()");
	}

	public void loadPlugin(final InputStream in) throws UserException {
		this.plugins.add(new Plugin(in));
	}

	public ArrayList<StructureTypeInfo> getStructureTypes() {
		final ArrayList<StructureTypeInfo> structureTypes = new ArrayList<StructureTypeInfo>();

		for (final Plugin plugin : this.plugins) {
			structureTypes.addAll(plugin.getStructureTypeClasses());
		}
		return structureTypes;
	}

	public ArrayList<AlgorithmInfo> getAlgorithms() {
		final ArrayList<AlgorithmInfo> algorithms = new ArrayList<AlgorithmInfo>();

		for (final Plugin plugin : this.plugins) {
			algorithms.addAll(plugin.getAlgorithms());
		}
		return algorithms;
	}

	public ArrayList<Plugin> getPlugins() {
		return this.plugins;
	}
}
