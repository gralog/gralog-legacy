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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.jar.JarFile;
import java.util.prefs.Preferences;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.MenuElement;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowAdapter;
import net.infonode.docking.OperationAbortedException;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.theme.DockingWindowsTheme;
import net.infonode.docking.theme.ShapedGradientDockingTheme;
import net.infonode.docking.title.LengthLimitedDockingWindowTitleProvider;
import net.infonode.docking.util.DeveloperUtil;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.ViewMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hu.gralog.algorithm.Algorithm;
import de.hu.gralog.algorithm.InvalidPropertyValuesException;
import de.hu.gralog.algorithm.InvalidPropertyValuesException.PropertyError;
import de.hu.gralog.app.GralogCoreInitialize;
import de.hu.gralog.app.UserException;
import de.hu.gralog.gui.action.ChangeEditorStateAction;
import de.hu.gralog.gui.action.EditCopyAction;
import de.hu.gralog.gui.action.EditCutAction;
import de.hu.gralog.gui.action.EditDeleteAction;
import de.hu.gralog.gui.action.EditPasteAction;
import de.hu.gralog.gui.action.EditSelectAllAction;
import de.hu.gralog.gui.action.ExitAction;
import de.hu.gralog.gui.action.FileCloseAction;
import de.hu.gralog.gui.action.FileCloseAllAction;
import de.hu.gralog.gui.action.FileCloseOthersAction;
import de.hu.gralog.gui.action.FileNewAction;
import de.hu.gralog.gui.action.FileOpenAction;
import de.hu.gralog.gui.action.FileRenameAction;
import de.hu.gralog.gui.action.FileRevertAction;
import de.hu.gralog.gui.action.FileSaveAction;
import de.hu.gralog.gui.action.FileSaveAllAction;
import de.hu.gralog.gui.action.FileSaveAsAction;
import de.hu.gralog.gui.action.LayoutDialogAction;
import de.hu.gralog.gui.action.RedoAction;
import de.hu.gralog.gui.action.ShowAboutAction;
import de.hu.gralog.gui.action.ShowViewAction;
import de.hu.gralog.gui.action.ShowWindowLayout;
import de.hu.gralog.gui.action.UndoAction;
import de.hu.gralog.gui.action.Zoom11Action;
import de.hu.gralog.gui.action.ZoomFitInCanvasAction;
import de.hu.gralog.gui.action.ZoomInAction;
import de.hu.gralog.gui.action.ZoomOutAction;
import de.hu.gralog.gui.components.HTMLEditorPane;
import de.hu.gralog.gui.data.AlgorithmsTree;
import de.hu.gralog.gui.data.JarLoader;
import de.hu.gralog.gui.data.Plugin.AlgorithmInfo;
import de.hu.gralog.gui.data.PluginManager;
import de.hu.gralog.gui.data.PluginsTree;
import de.hu.gralog.gui.data.PluginsTree.PluginsTreeNode;
import de.hu.gralog.gui.document.DocumentContentFactory;
import de.hu.gralog.gui.document.FileFormat;
import de.hu.gralog.gui.views.AlgorithmResultView;
import de.hu.gralog.gui.views.EditorDesktopView;
import de.hu.gralog.gui.views.EditorDesktopViewListener;
import de.hu.gralog.gui.views.ElementPropertyEditorView;
import de.hu.gralog.gui.views.GraphPropertyEditorView;
import de.hu.gralog.gui.views.OverviewPanelView;
import de.hu.gralog.gui.views.PluginsView;
import de.hu.gralog.jgraph.GJGraph;
import de.hu.gralog.structure.StructureTypeInfo;
import de.hu.gralog.util.WeakListenerList;

public class MainPad extends JFrame {

	private static final long serialVersionUID = 2783313317237162513L;

	final static Logger logger = LoggerFactory.getLogger(MainPad.class);

	public static final Object[] YES_NO_CANCEL_BUTTON_TEXT = { "Yes", "No",
			"Cancel" };

	public static final FileOpenAction FILE_OPEN_ACTION = new FileOpenAction();

	public static final FileCloseAction FILE_CLOSE_ACTION = new FileCloseAction();
	public static final FileCloseOthersAction FILE_CLOSE_OTHERS_ACTION = new FileCloseOthersAction();
	public static final FileCloseAllAction FILE_CLOSE_ALL_ACTION = new FileCloseAllAction();

	public static final FileSaveAction FILE_SAVE_ACTION = new FileSaveAction();
	public static final FileSaveAsAction FILE_SAVE_AS_ACTION = new FileSaveAsAction();
	public static final FileSaveAllAction FILE_SAVE_ALL_ACTION = new FileSaveAllAction();
	public static final FileRevertAction FILE_REVERT_ACTION = new FileRevertAction();

	public static final ShowAboutAction SHOW_ABOUT_ACTION = new ShowAboutAction();

	public static final FileRenameAction FILE_RENAME_ACTION = new FileRenameAction();

	public static final ExitAction EXIT_ACTION = new ExitAction();

	public static final LayoutDialogAction LAYOUT_DIALOG_ACTION = new LayoutDialogAction();
	public static final Zoom11Action ZOOM_11_ACTION = new Zoom11Action();
	public static final ZoomInAction ZOOM_IN_ACTION = new ZoomInAction();
	public static final ZoomOutAction ZOOM_OUT_ACTION = new ZoomOutAction();
	public static final ZoomFitInCanvasAction ZOOM_FIT_IN_CANVAS_ACTION = new ZoomFitInCanvasAction();
	public static final UndoAction UNDO_ACTION = new UndoAction();
	public static final RedoAction REDO_ACTION = new RedoAction();

	public static final EditCopyAction EDIT_COPY_ACTION = new EditCopyAction();
	public static final EditPasteAction EDIT_PASTE_ACTION = new EditPasteAction();
	public static final EditCutAction EDIT_CUT_ACTION = new EditCutAction();
	public static final EditDeleteAction EDIT_DELETE_ACTION = new EditDeleteAction();
	public static final EditSelectAllAction EDIT_SELECT_ALL_NODES_ACTION = new EditSelectAllAction(
			EditSelectAllAction.NODES);
	public static final EditSelectAllAction EDIT_SELECT_ALL_EDGES_ACTION = new EditSelectAllAction(
			EditSelectAllAction.EDGES);
	public static final EditSelectAllAction EDIT_SELECT_ALL_ACTION = new EditSelectAllAction();

	public static final ChangeEditorStateAction ES_SELECT_ACTION = new ChangeEditorStateAction(
			EditorState.SELECT);
	public static final ChangeEditorStateAction ES_CREATE_VERTEX_ACTION = new ChangeEditorStateAction(
			EditorState.CREATE_VERTEX);
	public static final ChangeEditorStateAction ES_CREATE_EDGE_ACTION = new ChangeEditorStateAction(
			EditorState.CREATE_EDGE);
	public static final ChangeEditorStateAction ES_MARQUE_ZOOM_ACTION = new ChangeEditorStateAction(
			EditorState.MARQUE_ZOOM);
	public static final ChangeEditorStateAction ES_INTERACTIVE_ZOOM_ACTION = new ChangeEditorStateAction(
			EditorState.INTERACTIVE_ZOOM);
	public static final ChangeEditorStateAction ES_PAN_ACTION = new ChangeEditorStateAction(
			EditorState.PAN);

	public static final ShowWindowLayout SHOW_WINDOW_LAYOUT_ACTION = new ShowWindowLayout();

	public static final DockingWindowsTheme THEME = new ShapedGradientDockingTheme();

	protected static final JFileChooser FILE_CHOOSER = new JFileChooser();
	static {
		FILE_CHOOSER.setAcceptAllFileFilterUsed(true);
		FILE_CHOOSER.setFileHidingEnabled(true);

	}

	private static MainPad mainPad = new MainPad();
	private static final JarLoader jarLoader = new JarLoader(
			MainPad.class.getClassLoader());

	public FileNewAction[] FILE_NEW_ACTIONS;

	private static final boolean REVERT_ENABLED = false;
	private static final PluginManager PLUGIN_MANAGER = new PluginManager();

	private static final Preferences PREFERENCES = Preferences
			.userNodeForPackage(MainPad.class);
	private static final String PREFS_WINDOW_LAYOUT = "WINDOWLAYOUT";
	private static final String PREFS_CURRENT_DIRECTORY = "CURRENTDIRECTORY";
	public static final String PREFS_PLUGIN_DIRECTORIES = "PLUGIN_DIRECTORIES";

	static {
		getInstance().loadSettings();
	}

	private static final EditorDesktopView DESKTOP = new EditorDesktopView();

	private static final View[] VIEWS = { DESKTOP,
			new ElementPropertyEditorView(), new GraphPropertyEditorView(),
			new OverviewPanelView(), new PluginsView(),
			new AlgorithmResultView() };
	private static final ShowViewAction[] SHOW_VIEW_ACTIONS = new ShowViewAction[VIEWS.length];
	static {
		for (int i = 0; i < VIEWS.length; i++) {
			SHOW_VIEW_ACTIONS[i] = new ShowViewAction(VIEWS[i]);
		}
	}

	private static RootWindow rootWindow;

	private ButtonGroup buttonGroupToolBar = new ButtonGroup();
	private EditorState editorState = EditorState.SELECT;

	private final WeakListenerList<EditorStateListener> editorStateListeners = new WeakListenerList<EditorStateListener>();

	static {
		logger.debug("entering static initialization");
		final MainPadViewMap views = new MainPadViewMap(VIEWS);
		rootWindow = DockingUtil.createRootWindow(views, false);
		rootWindow.getRootWindowProperties().addSuperObject(
				THEME.getRootWindowProperties());
		rootWindow
				.getRootWindowProperties()
				.getDockingWindowProperties()
				.setTitleProvider(
						new LengthLimitedDockingWindowTitleProvider(10));

		rootWindow.addListener(new MainPadViewListener());
		// MainPadViewListener mainPadViewListener = new MainPadViewListener();
		for (int i = 0; i < views.getViewCount(); i++) {
			final View view = views.getView(i);
			// view.addListener( mainPadViewListener );
			if (view instanceof EditorDesktopViewListener) {
				DESKTOP.addEditorDesktopViewListener((EditorDesktopViewListener) view);
			}
		}

		getInstance().getContentPane().add(rootWindow);
		getInstance().setJMenuBar(getInstance().createMenuBar());
		getInstance().getContentPane().add(getInstance().createToolBar(),
				BorderLayout.NORTH);
		getInstance().pack();
		getInstance().setExtendedState(Frame.MAXIMIZED_BOTH);
		getInstance().setVisible(true);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					getInstance().loadWindowLayout();
				}
				catch (final Throwable t) {
					getInstance()
							.handleUserException(
									new UserException(
											"unable to load window layout", t));
				}
			}
		});
		logger.debug("exiting static initialization");
	}

	public Cursor createCursor(final String fileName, final Point hotSpot) {
		final ImageIcon icon = createImageIcon(fileName);
		final Cursor cursor = MainPad.getInstance().getToolkit()
				.createCustomCursor(icon.getImage(), hotSpot, "Cursor");
		return cursor;
	}

	private MainPad() {
		super("Gralog");
		logger.debug("entering constructor");

		try {
			GralogCoreInitialize.initialize();
		}
		catch (final UserException e) {
			e.printStackTrace();
		}
		Locale.setDefault(new Locale("en", "UK"));
		super.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		super.addWindowListener(new MainPadWindowListener());
		super.setLayout(new BorderLayout());
		final GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		super.setPreferredSize(ge.getDefaultScreenDevice()
				.getDefaultConfiguration().getBounds().getSize());
		logger.debug("exiting constructor");
	}

	public void showWindowLayoutFrame() {
		DeveloperUtil.createWindowLayoutFrame("Windowlayout", rootWindow)
				.setVisible(true);
	}

	public void readJarFile(final JarFile file) {
		jarLoader.readJarFile(file);
	}

	public void loadSettings() {
		logger.debug("entering loadSettings");
		final String currentDirectory = this.getCurrentDirectory();
		if (currentDirectory == null) {
			logger.debug("current directory is null");
		}
		else {
			logger.debug("current directory: {}", currentDirectory);
		}
		this.setCurrentDirIfValid(currentDirectory);
		this.loadGralogCore();
		final String pluginDirectories = this.setupPluginDirectories();
		this.loadPluginsOnPath(pluginDirectories);

		this.FILE_NEW_ACTIONS = new FileNewAction[this.getStructureTypeInfos()
				.size()];
		int i = 0;
		for (final StructureTypeInfo graphType : this.getStructureTypeInfos()) {
			this.FILE_NEW_ACTIONS[i++] = new FileNewAction(graphType);
		}
		logger.debug("exiting loadSettings");
	}

	private void loadPluginsOnPath(final String pluginDirectories) {
		logger.debug("entering loadPluginsOnPath() with path: {}",
				pluginDirectories);
		final StringTokenizer st = new StringTokenizer(pluginDirectories, ";");
		while (st.hasMoreTokens()) {
			try {
				final File dir = new File(st.nextToken());
				if (dir.exists() && dir.canRead()) {
					logger.info("loading plugins from path: {}", dir);
					PLUGIN_MANAGER.loadPlugins(dir);
				}
				else {
					logger.info(
							"Plugin Directory {} does not exist or is not readable, cannot locate plugins there.",
							dir);
				}
			}
			catch (final UserException e) {
				logger.error("could not load plugin: {}", e);
				this.handleUserException(e);
			}
		}
		logger.debug("exiting loadPluginsOnPath()");
	}

	private String setupPluginDirectories() {
		logger.debug("entering setupPluginDirectories()");
		final StringBuilder pluginDirectories = new StringBuilder();
		pluginDirectories.append(System.getProperty("user.home", ""));
		logger.trace("default Dir from user.home property: {}",
				pluginDirectories);
		if (pluginDirectories.length() != 0) {
			pluginDirectories.append(File.separatorChar).append("gralog")
					.append(File.separatorChar).append("plugins");
			logger.trace("default dir after appending gralog plugins: {}",
					pluginDirectories);
		}
		pluginDirectories.append(pluginDirectories.length() == 0 ? "" : ';');
		pluginDirectories.append('.').append(File.separatorChar)
				.append("plugins");
		logger.debug("exiting setupPluginDirectories() and returning: {}",
				pluginDirectories);
		return pluginDirectories.toString();
	}

	private void loadGralogCore() {
		try {
			final InputStream in = this.getClass().getResourceAsStream(
					"/plugin.config");
			if (in == null) {
				this.handleUserException(new UserException(
						"unable to load core plugin"));
			}
			else {
				PLUGIN_MANAGER.loadPlugin(in);
			}
		}
		catch (final UserException e) {
			this.handleUserException(e);
		}
	}

	private void setCurrentDirIfValid(final String currentDirectory) {
		if (currentDirectory != null) {
			final File file = new File(currentDirectory);
			if (file.exists() && file.isDirectory() && file.canRead()) {
				FILE_CHOOSER.setCurrentDirectory(file);
			}
		}
	}

	private String getCurrentDirectory() {
		return PREFERENCES.get(PREFS_CURRENT_DIRECTORY, null);
	}

	public ArrayList<StructureTypeInfo> getStructureTypeInfos() {
		return PLUGIN_MANAGER.getStructureTypes();
	}

	public JarLoader getJarLoader() {
		return jarLoader;
	}

	protected void loadWindowLayout() {
		boolean loaded = false;

		try {
			final byte[] windowLayoutValue = PREFERENCES.getByteArray(
					PREFS_WINDOW_LAYOUT, null);

			if (windowLayoutValue != null) {
				final ObjectInputStream in = new ObjectInputStream(
						new ByteArrayInputStream(windowLayoutValue));
				rootWindow.read(in);
				loaded = true;
			}
		}
		catch (final IOException e) {
			this.handleUserException(new UserException(
					"error reading windowlayoutconfig", e));
		}

		if (!loaded) {
			rootWindow
					.setWindow(new SplitWindow(true, 0.28684628f,
							new SplitWindow(false, 0.42189282f,
									new TabWindow(new DockingWindow[] {
											VIEWS[3], VIEWS[4] }),
									new TabWindow(new DockingWindow[] {
											new TabWindow(new DockingWindow[] {
													VIEWS[1], VIEWS[2] }),
											VIEWS[5] })), VIEWS[0]));
			return;
		}

		this.getDesktop().restoreFocusAfterStart();
	}

	public File showFileDialog(final Class documentContentType,
			final String title) {
		FILE_CHOOSER.resetChoosableFileFilters();
		if (documentContentType != null) {
			for (final FileFormat format : DocumentContentFactory.getInstance()
					.getFileFormats(documentContentType)) {
				FILE_CHOOSER.addChoosableFileFilter(format.getFileFilter());
			}
		}
		else {
			for (final FileFormat format : DocumentContentFactory.getInstance()
					.getFileFormats()) {
				FILE_CHOOSER.addChoosableFileFilter(format.getFileFilter());
			}
		}

		final int retValue = FILE_CHOOSER.showDialog(this, title);
		if (retValue == JFileChooser.APPROVE_OPTION) {
			File file = FILE_CHOOSER.getSelectedFile();
			final FileFilter filter = FILE_CHOOSER.getFileFilter();
			final FileFormat format = DocumentContentFactory.getInstance()
					.getFileFormat(filter);
			if (!file.exists() && format != null && !format.acceptsFile(file)) {
				file = new File(file.getAbsolutePath() + "."
						+ format.getExtension());
			}

			return file;
		}
		return null;
	}

	protected void saveWindowLayout() {
		try {
			final ByteArrayOutputStream outByteArray = new ByteArrayOutputStream();
			final ObjectOutputStream out = new ObjectOutputStream(outByteArray);
			rootWindow.write(out);
			out.close();

			PREFERENCES.putByteArray(PREFS_WINDOW_LAYOUT,
					outByteArray.toByteArray());
		}
		catch (final IOException e) {
			this.handleUserException(new UserException(
					"unable to save windowlayloutconfig", e));
		}
	}

	public void saveSettings() {
		this.saveWindowLayout();
		PREFERENCES.put(PREFS_CURRENT_DIRECTORY, FILE_CHOOSER
				.getCurrentDirectory().getAbsolutePath());
	}

	protected void setButtonTextOrIconToNull(final JComponent comp,
			final boolean icon) {
		if (comp instanceof JMenu) {
			final JMenu menu = (JMenu) comp;
			for (final MenuElement item : menu.getSubElements()) {
				this.setButtonTextOrIconToNull((JComponent) item, icon);
			}
			return;
		}
		if ((comp instanceof AbstractButton)) {
			if (icon) {
				((AbstractButton) comp).setIcon(null);
			}
			else {
				((AbstractButton) comp).setText(null);
			}
			return;
		}
		for (int i = 0; i < comp.getComponentCount(); i++) {
			this.setButtonTextOrIconToNull((JComponent) comp.getComponent(i),
					icon);
		}
	}

	protected JToolBar createToolBar() {
		final JToolBar toolBar = new JToolBar();

		toolBar.add(FILE_OPEN_ACTION);
		toolBar.add(FILE_SAVE_ACTION);

		this.buttonGroupToolBar = new ButtonGroup();
		this.buttonGroupToolBar.add(new JToggleButton(ES_SELECT_ACTION));
		this.buttonGroupToolBar.add(new JToggleButton(ES_PAN_ACTION));
		this.buttonGroupToolBar.add(new JToggleButton(ES_CREATE_VERTEX_ACTION));
		this.buttonGroupToolBar.add(new JToggleButton(ES_CREATE_EDGE_ACTION));
		this.buttonGroupToolBar.add(new JToggleButton(ES_MARQUE_ZOOM_ACTION));
		this.buttonGroupToolBar.add(new JToggleButton(
				ES_INTERACTIVE_ZOOM_ACTION));
		this.setEditorState(EditorState.SELECT);
		toolBar.addSeparator();

		final Enumeration buttons = this.buttonGroupToolBar.getElements();
		while (buttons.hasMoreElements()) {
			final JToggleButton button = (JToggleButton) buttons.nextElement();
			toolBar.add(button);
		}
		toolBar.addSeparator();

		toolBar.add(new JButton(ZOOM_FIT_IN_CANVAS_ACTION));
		toolBar.add(new JButton(ZOOM_11_ACTION));
		toolBar.add(new JButton(ZOOM_OUT_ACTION));
		toolBar.add(new JButton(ZOOM_IN_ACTION));
		toolBar.add(new JButton(LAYOUT_DIALOG_ACTION));

		toolBar.addSeparator();

		toolBar.add(new JButton(UNDO_ACTION));
		UNDO_ACTION.setEnabled(false);
		toolBar.add(new JButton(REDO_ACTION));
		REDO_ACTION.setEnabled(false);

		this.setButtonTextOrIconToNull(toolBar, false);
		return toolBar;
	}

	protected JMenuBar createMenuBar() {
		final JMenuBar menuBar = new JMenuBar();
		final JMenu file = new JMenu("File");
		/*
		 * JMenu fileNew = new JMenu( "New" ); for ( FileNewAction action :
		 * FILE_NEW_ACTIONS ) fileNew.add( action );
		 * 
		 * file.add( fileNew );
		 */
		file.add(FILE_OPEN_ACTION);

		file.addSeparator();
		file.add(FILE_CLOSE_ACTION);
		file.add(FILE_CLOSE_ALL_ACTION);
		file.addSeparator();
		file.add(FILE_SAVE_ACTION);
		file.add(FILE_SAVE_AS_ACTION);
		file.add(FILE_SAVE_ALL_ACTION);
		file.add(FILE_REVERT_ACTION);
		FILE_REVERT_ACTION.setEnabled(REVERT_ENABLED);
		file.addSeparator();
		file.add(FILE_RENAME_ACTION);
		file.addSeparator();
		file.add(EXIT_ACTION);

		menuBar.add(file);

		final JMenu edit = new JMenu("Edit");

		edit.add(UNDO_ACTION);
		edit.add(REDO_ACTION);
		edit.addSeparator();
		edit.add(EDIT_COPY_ACTION);
		edit.add(EDIT_CUT_ACTION);
		edit.add(EDIT_PASTE_ACTION);
		edit.add(EDIT_DELETE_ACTION);
		edit.addSeparator();
		final JMenu select = new JMenu("Select All");
		select.add(EDIT_SELECT_ALL_NODES_ACTION);
		select.add(EDIT_SELECT_ALL_EDGES_ACTION);
		select.add(EDIT_SELECT_ALL_ACTION);
		edit.add(select);

		menuBar.add(edit);

		final JMenu tools = new JMenu("Tools");
		tools.add(ES_SELECT_ACTION);
		tools.add(ES_PAN_ACTION);
		tools.add(ES_CREATE_VERTEX_ACTION);
		tools.add(ES_CREATE_EDGE_ACTION);
		tools.add(ES_MARQUE_ZOOM_ACTION);
		tools.add(ES_INTERACTIVE_ZOOM_ACTION);
		tools.addSeparator();
		tools.add(LAYOUT_DIALOG_ACTION);
		menuBar.add(tools);

		final JMenu view = new JMenu("View");
		final JMenu zoom = new JMenu("Zoom");
		zoom.add(ZOOM_FIT_IN_CANVAS_ACTION);
		zoom.add(ZOOM_11_ACTION);
		zoom.add(ZOOM_IN_ACTION);
		zoom.add(ZOOM_OUT_ACTION);
		view.add(zoom);

		final JMenu views = new JMenu("Open View");
		for (final ShowViewAction action : SHOW_VIEW_ACTIONS) {
			views.add(action);
		}
		view.add(views);

		menuBar.add(view);

		final JMenu info = new JMenu("Info");
		info.add(SHOW_ABOUT_ACTION);
		// info.add( SHOW_WINDOW_LAYOUT_ACTION );

		menuBar.add(info);

		// setButtonTextOrIconToNull( menuBar, true );
		return menuBar;
	}

	public EditorState getEditorState() {
		return this.editorState;
	}

	public void setEditorState(final EditorState editorState) {
		final Enumeration buttons = this.buttonGroupToolBar.getElements();
		while (buttons.hasMoreElements()) {
			final JToggleButton button = (JToggleButton) buttons.nextElement();
			if (((ChangeEditorStateAction) button.getAction()).getEditorState() == editorState) {
				button.setSelected(true);
			}
		}

		final EditorState from = this.editorState;
		this.editorState = editorState;
		this.fireEditorStateChanged(from, editorState);
	}

	public ArrayList<AlgorithmInfo> getAlgorithms() {
		return PLUGIN_MANAGER.getAlgorithms();
	}

	public static AlgorithmsTree getAlgorithmTree(
			final ArrayList<AlgorithmInfo> algorithms) {
		return new AlgorithmsTree(algorithms);
	}

	public PluginsTreeNode getPluginsTree() {
		return PluginsTree.buildTree(PLUGIN_MANAGER.getPlugins());
	}

	protected Algorithm getAlgorithmByName(final String classname) {
		Algorithm algorithm = null;
		try {
			algorithm = (Algorithm) this.getJarLoader().loadClass(classname)
					.newInstance();
		}
		catch (final InstantiationException e) {
			this.handleUserException(new UserException("algorithm: "
					+ classname + " could not be initiated", e));
		}
		catch (final IllegalAccessException e) {
			this.handleUserException(new UserException("algorithm: "
					+ classname + " could not be accessed", e));
		}
		catch (final ClassNotFoundException e) {
			this.handleUserException(new UserException("algorithm: "
					+ classname + " could not be found", e));
		}
		return algorithm;
	}

	public static MainPad getInstance() {
		return mainPad;
	}

	public EditorDesktopView getDesktop() {
		return DESKTOP;
	}

	public static Component getUserExceptionComponent(final UserException e) {
		if (e.getCause() instanceof InvalidPropertyValuesException) {
			final InvalidPropertyValuesException i = (InvalidPropertyValuesException) e
					.getCause();
			final JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

			for (final PropertyError pe : i.getErrors()) {
				final JPanel errorPanel = new JPanel(new BorderLayout());
				errorPanel
						.add(new JLabel(pe.getProperty()), BorderLayout.NORTH);

				final JEditorPane messagePane = new HTMLEditorPane(
						pe.getMessage());
				errorPanel.add(new JScrollPane(messagePane),
						BorderLayout.CENTER);

				panel.add(errorPanel);
			}
			return panel;
		}
		String text = "no description";
		if (e.getCause() != null) {
			final StringWriter stackTrace = new StringWriter();
			e.getCause().printStackTrace(new PrintWriter(stackTrace));
			text = stackTrace.toString();
		}
		else {
			if (e.getDescription() != null) {
				text = e.getDescription();
			}
		}
		final JTextArea textArea = new JTextArea(text);
		textArea.setRows(4);
		textArea.setEditable(false);
		textArea.setBackground(new JLabel().getBackground());
		textArea.setForeground(new JLabel().getForeground());

		return new JScrollPane(textArea);
	}

	public void handleUserException(final UserException e) {
		logger.error("handling user exception: {}", e);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(MainPad.getInstance(),
						getUserExceptionComponent(e), e.getMessage(),
						JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	public static ImageIcon createImageIcon(final String image) {
		final URL url = Object.class
				.getResource("/de/hu/gralog/resources/images/newimages/"
						+ image);
		return new ImageIcon(url);
	}

	private class MainPadWindowListener extends WindowAdapter {
		@Override
		public void windowClosing(final WindowEvent arg0) {
			boolean abort = false;
			try {
				DESKTOP.prepareSerializing();
			}
			catch (final OperationAbortedException e) {
				abort = true;
			}
			if (!abort) {
				MainPad.this.saveSettings();
				MainPad.this.dispose();
			}
		}

	}

	public void exit() {
		new MainPadWindowListener().windowClosing(null);
	}

	public static class EditorState {
		public static final EditorState SELECT = new EditorState(
				MainPad.createImageIcon("designer_arrow.png"),
				"designer_arrow_cursor.png", new Point(8, 1), "Select");
		public static final EditorState CREATE_VERTEX = new EditorState(
				MainPad.createImageIcon("addpointcursor.png"),
				"addpointcursor_cursor.png", new Point(8, 8), "Add Node");
		public static final EditorState CREATE_EDGE = new EditorState(
				MainPad.createImageIcon("freehandcursor.png"),
				"freehandcursor_cursor.png", new Point(8, 8), "Add Edge");
		public static final EditorState MARQUE_ZOOM = new EditorState(
				MainPad.createImageIcon("stock_zoom-object.png"),
				"stock_zoom-object_cursor.png", new Point(0, 0), "Marquee Zoom");
		public static final EditorState PAN = new EditorState(
				MainPad.createImageIcon("drag.png"), "drag_cursor.png",
				new Point(8, 0), "dragging_cursor.png", new Point(16, 0), "Pan");
		public static final EditorState INTERACTIVE_ZOOM = new EditorState(
				MainPad.createImageIcon("stock_zoom-page-width.png"),
				"stock_zoom-page-width_cursor.png", new Point(0, 0),
				"Interactive Zoom");

		private final String displayName;
		private final String cursorName;
		private final Point hotSpot;
		private Point secondHotSpot;
		private String secondCursorName;
		private Cursor cursor;
		private Cursor secondCursor;
		private final ImageIcon icon;

		public EditorState(final ImageIcon icon, final String cursorName,
				final Point hotSpot, final String displayName) {
			this.displayName = displayName;
			this.icon = icon;
			this.cursorName = cursorName;
			this.hotSpot = hotSpot;
		}

		public EditorState(final ImageIcon icon, final String cursorName,
				final Point hotSpot, final String secondCursorName,
				final Point secondHotSpot, final String displayName) {
			this.displayName = displayName;
			this.icon = icon;
			this.cursorName = cursorName;
			this.hotSpot = hotSpot;
			this.secondCursorName = secondCursorName;
			this.secondHotSpot = secondHotSpot;
		}

		public Cursor getCursor() {
			if (this.cursor == null) {
				this.cursor = MainPad.getInstance().createCursor(
						this.cursorName, this.hotSpot);
			}
			return this.cursor;
		}

		public Cursor getSecondCursor() {
			if (this.secondCursor == null && this.secondCursorName != null) {
				this.secondCursor = MainPad.getInstance().createCursor(
						this.secondCursorName, this.secondHotSpot);
			}
			return this.secondCursor;
		}

		public ImageIcon getIcon() {
			return this.icon;
		}

		public String getDisplayName() {
			return this.displayName;
		}
	}

	public static interface EditorStateListener {
		public void editorStateChanged(EditorState from, EditorState to);
	}

	protected void fireEditorStateChanged(final EditorState from,
			final EditorState to) {
		for (final EditorStateListener l : this.editorStateListeners
				.getListeners()) {
			l.editorStateChanged(from, to);
		}
	}

	public void addEditorStateListener(final EditorStateListener l) {
		this.editorStateListeners.addListener(l);
	}

	public void removeEditorStateListener(final EditorStateListener l) {
		this.editorStateListeners.removeListener(l);
	}

	public void showView(final View view) {
		view.restore();
	}

	private static class MainPadViewListener extends DockingWindowAdapter {

		private boolean containsView(final DockingWindow window, final View view) {
			if (window == view) {
				return true;
			}
			for (int i = 0; i < window.getChildWindowCount(); i++) {
				if (this.containsView(window.getChildWindow(i), view)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public void windowClosed(final DockingWindow window) {

			for (final ShowViewAction action : SHOW_VIEW_ACTIONS) {
				if (this.containsView(window, action.getView())) {
					action.setEnabled(true);
				}
			}
		}

		@Override
		public void windowRestored(final DockingWindow window) {
			for (final ShowViewAction action : SHOW_VIEW_ACTIONS) {
				if (this.containsView(window, action.getView())) {
					action.setEnabled(false);
				}
			}
		}

		@Override
		public void windowAdded(final DockingWindow arg0,
				final DockingWindow arg1) {
			this.windowRestored(arg1);
		}
	}

	private static class MainPadViewMap extends ViewMap {

		public MainPadViewMap(final View[] views) {
			super(views);
		}

		@Override
		public View readView(final ObjectInputStream in) throws IOException {
			final View view = super.readView(in);
			if (view instanceof EditorDesktopView) {
				((EditorDesktopView) view).readView(in);
			}
			return view;
		}

		@Override
		public void writeView(final View view, final ObjectOutputStream out)
				throws IOException {
			super.writeView(view, out);
			if (view instanceof EditorDesktopView) {
				((EditorDesktopView) view).writeView(out);
			}
		}

	}

	public void documentGraphReplaced(final GJGraph oldGraph,
			final GJGraph newGraph) {
		// do nothing
	}

	public boolean isRevertEnabled() {
		return REVERT_ENABLED;
	}

	public boolean shouldOverrideFileDialog(final File file) {
		return JOptionPane.YES_OPTION == JOptionPane.showOptionDialog(this,
				"Do you want to override this file?", "File: " + file.getName()
						+ " already exists!", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE, null, YES_NO_CANCEL_BUTTON_TEXT,
				YES_NO_CANCEL_BUTTON_TEXT[2]);
	}
}
