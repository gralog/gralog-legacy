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
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.prefs.Preferences;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
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
import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.GraphTypeInfo;
import de.hu.gralog.graph.alg.Algorithm;
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
import de.hu.gralog.gui.data.AlgorithmsTree;
import de.hu.gralog.gui.data.JarLoader;
import de.hu.gralog.gui.data.PluginManager;
import de.hu.gralog.gui.data.Plugin.AlgorithmInfo;
import de.hu.gralog.gui.document.DocumentContentFactory;
import de.hu.gralog.gui.views.AlgorithmResultView;
import de.hu.gralog.gui.views.EditorDesktopView;
import de.hu.gralog.gui.views.EditorDesktopViewListener;
import de.hu.gralog.gui.views.ElementPropertyEditorView;
import de.hu.gralog.gui.views.ExecuteAlgorithmsView;
import de.hu.gralog.gui.views.GraphPropertyEditorView;
import de.hu.gralog.gui.views.OverviewPanelView;
import de.hu.gralog.jgraph.GJGraph;

public class MainPad extends JFrame {
		
	public static final FileOpenAction FILE_OPEN_ACTION = new FileOpenAction(  );
	
	public static final FileCloseAction FILE_CLOSE_ACTION = new FileCloseAction(  );
	public static final FileCloseOthersAction FILE_CLOSE_OTHERS_ACTION = new FileCloseOthersAction( );
	public static final FileCloseAllAction FILE_CLOSE_ALL_ACTION = new FileCloseAllAction(  );
	
	public static final FileSaveAction FILE_SAVE_ACTION = new FileSaveAction(  );
	public static final FileSaveAsAction FILE_SAVE_AS_ACTION = new FileSaveAsAction(  );
	public static final FileSaveAllAction FILE_SAVE_ALL_ACTION = new FileSaveAllAction(  );
	public static final FileRevertAction FILE_REVERT_ACTION = new FileRevertAction(  );
	
	public static final ShowAboutAction SHOW_ABOUT_ACTION = new ShowAboutAction(  );
	
	public static final FileRenameAction FILE_RENAME_ACTION = new FileRenameAction(  );
	
	public static final ExitAction EXIT_ACTION = new ExitAction(  );
	
	public static final LayoutDialogAction LAYOUT_DIALOG_ACTION = new LayoutDialogAction(  );
	public static final Zoom11Action ZOOM_11_ACTION = new Zoom11Action(  );
	public static final ZoomInAction ZOOM_IN_ACTION = new ZoomInAction(  );
	public static final ZoomOutAction ZOOM_OUT_ACTION = new ZoomOutAction(  );
	public static final ZoomFitInCanvasAction ZOOM_FIT_IN_CANVAS_ACTION = new ZoomFitInCanvasAction(  );
	public static final UndoAction UNDO_ACTION = new UndoAction();
	public static final RedoAction REDO_ACTION = new RedoAction();

	public static final EditCopyAction EDIT_COPY_ACTION = new EditCopyAction();
	public static final EditPasteAction EDIT_PASTE_ACTION = new EditPasteAction();
	public static final EditCutAction EDIT_CUT_ACTION = new EditCutAction();
	public static final EditDeleteAction EDIT_DELETE_ACTION = new EditDeleteAction();
	public static final EditSelectAllAction EDIT_SELECT_ALL_NODES_ACTION = new EditSelectAllAction( EditSelectAllAction.NODES );
	public static final EditSelectAllAction EDIT_SELECT_ALL_EDGES_ACTION = new EditSelectAllAction( EditSelectAllAction.EDGES );
	public static final EditSelectAllAction EDIT_SELECT_ALL_ACTION = new EditSelectAllAction(  );
	
	public static final ChangeEditorStateAction ES_SELECT_ACTION = new ChangeEditorStateAction( EditorState.SELECT );
	public static final ChangeEditorStateAction ES_CREATE_VERTEX_ACTION = new ChangeEditorStateAction( EditorState.CREATE_VERTEX );
	public static final ChangeEditorStateAction ES_CREATE_EDGE_ACTION = new ChangeEditorStateAction( EditorState.CREATE_EDGE );
	public static final ChangeEditorStateAction ES_MARQUE_ZOOM_ACTION = new ChangeEditorStateAction( EditorState.MARQUE_ZOOM );
	public static final ChangeEditorStateAction ES_INTERACTIVE_ZOOM_ACTION = new ChangeEditorStateAction( EditorState.INTERACTIVE_ZOOM );
	public static final ChangeEditorStateAction ES_PAN_ACTION = new ChangeEditorStateAction( EditorState.PAN );

	public static final ShowWindowLayout SHOW_WINDOW_LAYOUT_ACTION = new ShowWindowLayout();
	
	public static final DockingWindowsTheme THEME = new ShapedGradientDockingTheme();
	
	protected static final JFileChooser FILE_CHOOSER = new JFileChooser();
	static {
		FILE_CHOOSER.setAcceptAllFileFilterUsed( true );
		FILE_CHOOSER.setFileHidingEnabled( true );
	}
	
	private static MainPad mainPad = new MainPad();
		
	public FileNewAction[] FILE_NEW_ACTIONS;
	
	private static final PluginManager PLUGIN_MANAGER = new PluginManager();
	private static final JarLoader CLASS_LOADER = new JarLoader( "file://", MainPad.class.getClassLoader() );
	
	private static final Preferences PREFERENCES = Preferences.userNodeForPackage( MainPad.class );
	private static final String PREFS_WINDOW_LAYOUT = "WINDOWLAYOUT";
	private static final String PREFS_CURRENT_DIRECTORY = "CURRENTDIRECTORY";
	public static final String PREFS_PLUGIN_DIRECTORIES = "PLUGIN_DIRECTORIES";
	
	static {
		getInstance().loadSettings();
	}
	
	private static final EditorDesktopView DESKTOP = new EditorDesktopView();
		
	private static final View[] VIEWS = { DESKTOP, new ElementPropertyEditorView(), new GraphPropertyEditorView(), new OverviewPanelView(), new ExecuteAlgorithmsView(), new AlgorithmResultView() };
	private static final ShowViewAction[] SHOW_VIEW_ACTIONS = new ShowViewAction[VIEWS.length];
	static {
		for (int i=0;i < VIEWS.length; i++ )
			SHOW_VIEW_ACTIONS[i] = new ShowViewAction( VIEWS[i] );
	}
	
	private static RootWindow rootWindow;
	
	private Properties gamesProperties;
	private AlgorithmsTree algorithms;
	
	private ButtonGroup buttonGroupToolBar = new ButtonGroup();
	private EditorState editorState = EditorState.SELECT;
	
	private ArrayList editorStateListeners = new ArrayList();
	
	static {
		MainPadViewMap views = new MainPadViewMap( VIEWS );
		rootWindow = DockingUtil.createRootWindow( views, false );
		rootWindow.getRootWindowProperties().addSuperObject( THEME.getRootWindowProperties() );
		rootWindow.getRootWindowProperties().getDockingWindowProperties().setTitleProvider( new LengthLimitedDockingWindowTitleProvider( 10 )
 );
		
		rootWindow.addListener( new MainPadViewListener() );
//		MainPadViewListener mainPadViewListener = new MainPadViewListener();
		for ( int i = 0; i < views.getViewCount(); i++ ) {
			View view = views.getView( i );
//			view.addListener( mainPadViewListener );
			if ( view instanceof EditorDesktopViewListener )
				DESKTOP.addEditorDesktopViewListener( (EditorDesktopViewListener)view );
		}
				
		getInstance().getContentPane().add( rootWindow );
		getInstance().setJMenuBar( getInstance().createMenuBar() );
		getInstance().getContentPane().add( getInstance().createToolBar(), BorderLayout.NORTH );
		getInstance().pack();
		getInstance().setExtendedState( Frame.MAXIMIZED_BOTH );
		getInstance().setVisible( true );
				
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				try {
					getInstance().loadWindowLayout();
				} catch( Throwable t ) {
					getInstance().handleUserException( new UserException( "unable to load window layout", t) );
				}
			}
		});
	}
	
	public Cursor createCursor(String fileName, Point hotSpot ) {
		ImageIcon icon = createImageIcon( fileName );
		Cursor cursor = MainPad.getInstance().getToolkit().createCustomCursor( icon.getImage(), hotSpot, "Cursor" );
		return cursor;
	}
	
	private MainPad( ) {
		super("Games");

		super.setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
		super.addWindowListener( new MainPadWindowListener() );
		super.setLayout( new BorderLayout() );
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		super.setPreferredSize( ge.getDefaultScreenDevice().getDefaultConfiguration().getBounds().getSize() );
	}
	
	public void showWindowLayoutFrame() {
		DeveloperUtil.createWindowLayoutFrame( "Windowlayout", rootWindow ).setVisible( true );
	}
	
	public void loadSettings() {
		String currentDirectory = PREFERENCES.get( PREFS_CURRENT_DIRECTORY, null );
		if ( currentDirectory != null ) {
			File file = new File( currentDirectory );
			if ( file.exists() && file.isDirectory() && file.canRead() )
				FILE_CHOOSER.setCurrentDirectory( file );
		}
		
		try {
			InputStream in = this.getClass().getResourceAsStream( "/plugin.config" );
			if ( in == null )
				handleUserException( new UserException( "unable to load core plugin" ) );
			else
				PLUGIN_MANAGER.loadPlugin( in );
		} catch (UserException e) {
			handleUserException( e );
		}
		
		String defaultDir = System.getProperty( "user.home", null );
		if ( defaultDir != null )
			defaultDir = defaultDir + "/gralog/plugins";
		String pluginDirectories = PREFERENCES.get( PREFS_PLUGIN_DIRECTORIES, defaultDir );
		if ( pluginDirectories != null ) {
			StringTokenizer st = new StringTokenizer( pluginDirectories, ":" );
			while ( st.hasMoreTokens() ) {
				try {
					PLUGIN_MANAGER.loadPlugins( new File( st.nextToken() ) );
				} catch (UserException e) {
					handleUserException( e );
				}
			}
		}
		
		FILE_NEW_ACTIONS = new FileNewAction[PLUGIN_MANAGER.getGraphTypes().size()];
		int i = 0;
		for ( GraphTypeInfo graphType : PLUGIN_MANAGER.getGraphTypes() ) {
			FILE_NEW_ACTIONS[i++] = new FileNewAction( graphType );
		}
	}
	
	public JarLoader getClassLoader() {
		return CLASS_LOADER;
	}
	
	protected void loadWindowLayout() {
		boolean loaded = false;
		
		try {
			byte[] windowLayoutValue = PREFERENCES.getByteArray( PREFS_WINDOW_LAYOUT, null );
			
			if ( windowLayoutValue != null ) {
				ObjectInputStream in = new ObjectInputStream( new ByteArrayInputStream( windowLayoutValue ) );
				rootWindow.read( in );
				loaded = true;
			}
		} catch (IOException e) {
			handleUserException( new UserException( "error reading windowlayoutconfig", e) );
		}
		
		if ( ! loaded ) {
			rootWindow.setWindow(new SplitWindow(true, 0.28684628f, 
				    new SplitWindow(false, 0.42189282f, 
				        new TabWindow(new DockingWindow[]{
				            VIEWS[3], 
				            VIEWS[4] }), 
				        new TabWindow(new DockingWindow[]{
				            new TabWindow(new DockingWindow[]{
				                VIEWS[1], 
				                VIEWS[2]}), 
				            VIEWS[5]})), 
				    VIEWS[0]));
			return;
		}
		
		getDesktop().restoreFocusAfterStart();
	}
	
	public File showFileDialog( Class documentContentType, String title ) {
		FILE_CHOOSER.resetChoosableFileFilters();
		if ( documentContentType != null ) {
			for ( FileFormat format : DocumentContentFactory.getInstance().getFileFormats( documentContentType ) ) 
				FILE_CHOOSER.addChoosableFileFilter( format.getFileFilter() );
		} else {
			for ( FileFormat format : DocumentContentFactory.getInstance().getFileFormats(  ) ) 
				FILE_CHOOSER.addChoosableFileFilter( format.getFileFilter() );
		}
		
		int retValue = FILE_CHOOSER.showDialog( this, title );
		if ( retValue == MainPad.FILE_CHOOSER.APPROVE_OPTION ) {
			File file = FILE_CHOOSER.getSelectedFile();
			FileFilter filter = FILE_CHOOSER.getFileFilter();
			FileFormat format = DocumentContentFactory.getInstance().getFileFormat( filter );
			if ( ! file.exists() && format != null && ! format.acceptsFile( file ) )
				file = new File( file.getAbsolutePath() + "." + format.getExtension() );
			
			return file;
		}
		return null;
	}
	
	protected void saveWindowLayout() {
		try {
			ByteArrayOutputStream outByteArray = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream( outByteArray );
			rootWindow.write( out );
			out.close();
			
			PREFERENCES.putByteArray( PREFS_WINDOW_LAYOUT, outByteArray.toByteArray() );
		} catch (IOException e) {
			handleUserException( new UserException( "unable to save windowlayloutconfig", e ) );
		}
	}
	
	public void saveSettings() {
		saveWindowLayout();
		PREFERENCES.put( PREFS_CURRENT_DIRECTORY , FILE_CHOOSER.getCurrentDirectory().getAbsolutePath() );
	}
	
	protected void setButtonTextOrIconToNull( JComponent comp, boolean icon ) {
		if ( comp instanceof JMenu ) {
			JMenu menu = (JMenu)comp;
			for ( MenuElement item : menu.getSubElements() )
				setButtonTextOrIconToNull( (JComponent)item, icon );
			return;
		}
		if ( (comp instanceof AbstractButton) ) {
			if ( icon ) 
				((AbstractButton)comp).setIcon( null );
			else
				((AbstractButton)comp).setText( null );
			return;
		} 
		for ( int i = 0; i < comp.getComponentCount(); i++ )
			setButtonTextOrIconToNull( (JComponent)comp.getComponent( i ), icon );
	}	
	
	protected JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar();

		toolBar.add( FILE_OPEN_ACTION );
		toolBar.add( FILE_SAVE_ACTION );

		buttonGroupToolBar = new ButtonGroup();
		buttonGroupToolBar.add( new JToggleButton( ES_SELECT_ACTION ) );
		buttonGroupToolBar.add( new JToggleButton( ES_PAN_ACTION ) );
		buttonGroupToolBar.add( new JToggleButton( ES_CREATE_VERTEX_ACTION ) );
		buttonGroupToolBar.add( new JToggleButton( ES_CREATE_EDGE_ACTION ) );
		buttonGroupToolBar.add( new JToggleButton( ES_MARQUE_ZOOM_ACTION ) );
		buttonGroupToolBar.add( new JToggleButton( ES_INTERACTIVE_ZOOM_ACTION ) );
		setEditorState( EditorState.SELECT );
		toolBar.addSeparator();

		Enumeration buttons = buttonGroupToolBar.getElements();
		while ( buttons.hasMoreElements() ) {
			JToggleButton button = (JToggleButton)buttons.nextElement();
			toolBar.add( button );
		}
		toolBar.addSeparator();

		toolBar.add( new JButton( ZOOM_FIT_IN_CANVAS_ACTION ) );
		toolBar.add( new JButton( ZOOM_11_ACTION ) );
		toolBar.add( new JButton( ZOOM_OUT_ACTION ) );
		toolBar.add( new JButton( ZOOM_IN_ACTION ) );
		toolBar.add( new JButton( LAYOUT_DIALOG_ACTION ) );

		toolBar.addSeparator();
		
		toolBar.add( new JButton( UNDO_ACTION ) );
		UNDO_ACTION.setEnabled( false );
		toolBar.add( new JButton( REDO_ACTION ) );
		REDO_ACTION.setEnabled( false );
		
		setButtonTextOrIconToNull( toolBar, false );
		return toolBar;
	}
	
	protected JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu( "File" );
		JMenu fileNew = new JMenu( "New" );
		for ( FileNewAction action : FILE_NEW_ACTIONS )
			fileNew.add( action );
		
		file.add( fileNew );
		
		file.add( FILE_OPEN_ACTION );
	
		file.addSeparator();
		file.add( FILE_CLOSE_ACTION );
		file.add( FILE_CLOSE_ALL_ACTION );
		file.addSeparator();
		file.add( FILE_SAVE_ACTION );
		file.add( FILE_SAVE_AS_ACTION );
		file.add( FILE_SAVE_ALL_ACTION );
		file.add( FILE_REVERT_ACTION );
		file.addSeparator();
		file.add( FILE_RENAME_ACTION );
		file.addSeparator();
		file.add( EXIT_ACTION );
		
		menuBar.add( file );
		
		JMenu edit = new JMenu( "Edit" );
		
		edit.add( UNDO_ACTION );
		edit.add( REDO_ACTION );
		edit.addSeparator();
		edit.add( EDIT_COPY_ACTION );
		edit.add( EDIT_CUT_ACTION );
		edit.add( EDIT_PASTE_ACTION );
		edit.add( EDIT_DELETE_ACTION );
		edit.addSeparator();
		JMenu select = new JMenu( "Select All" );
		select.add( EDIT_SELECT_ALL_NODES_ACTION );
		select.add( EDIT_SELECT_ALL_EDGES_ACTION );
		select.add( EDIT_SELECT_ALL_ACTION );
		edit.add( select );
		
		menuBar.add( edit );
		
		JMenu tools = new JMenu( "Tools" );
		tools.add( ES_SELECT_ACTION );
		tools.add( ES_PAN_ACTION );
		tools.add( ES_CREATE_VERTEX_ACTION );
		tools.add( ES_CREATE_EDGE_ACTION );
		tools.add( ES_MARQUE_ZOOM_ACTION );
		tools.add( ES_INTERACTIVE_ZOOM_ACTION );
		menuBar.add( tools );
		
		JMenu view = new JMenu( "View");
		JMenu zoom = new JMenu( "Zoom" );
		zoom.add( ZOOM_FIT_IN_CANVAS_ACTION );
		zoom.add( ZOOM_11_ACTION );
		zoom.add( ZOOM_IN_ACTION );
		zoom.add( ZOOM_OUT_ACTION );
		view.add( zoom );

		JMenu views = new JMenu( "Open View");
		for( ShowViewAction action : SHOW_VIEW_ACTIONS )
			views.add( action );
		view.add( views );
		
		menuBar.add( view );
		
		JMenu info = new JMenu( "Info");
		info.add( SHOW_ABOUT_ACTION );
		//info.add( SHOW_WINDOW_LAYOUT_ACTION );
		
		menuBar.add( info );
		
		//setButtonTextOrIconToNull( menuBar, true );
		return menuBar;
	}

	public EditorState getEditorState() {
		return editorState;
	}
	
	public void setEditorState(EditorState editorState) {
		Enumeration buttons = buttonGroupToolBar.getElements();
		while ( buttons.hasMoreElements() ) {
			JToggleButton button = (JToggleButton)buttons.nextElement();
			if ( ((ChangeEditorStateAction)button.getAction()).getEditorState() == editorState )
				button.setSelected( true );
		}
		
		EditorState from = this.editorState;
		this.editorState = editorState;
		fireEditorStateChanged( from, editorState );
	}
	
	protected void loadProperties() {
		gamesProperties = loadProperties( getClass().getResourceAsStream( "/de/hu/gralog/resources/algorithms.conf") );
		createAlgorithms();
	}
	
	protected Properties loadProperties( InputStream input ) {
		Properties properties = new Properties();
		try {
			properties.load( input );
		} catch (IOException e) {
			handleUserException( new UserException( "IOException when reading Properties ", e ) );
		}
		return properties;
	}
	
	protected Properties loadProperties( File file ) {
		try {
			return loadProperties( new FileInputStream( file ) );
		} catch (FileNotFoundException e) {
			handleUserException( new UserException( "file not found " + file.getName(), e ) );
		}
		return new Properties();
	}
	
	public void addProperties( File file ) throws NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		algorithms.addProperties( loadProperties( file ) );
	}
	
	protected void createAlgorithms() {
		try {
			algorithms = new AlgorithmsTree( gamesProperties );
		} catch (ClassNotFoundException e) {
			handleUserException( new UserException( "error building algorithmtree", e) );
		} catch (NoSuchMethodException e) {
			handleUserException( new UserException( "error building algorithmtree", e) );
		} catch (InstantiationException e) {
			handleUserException( new UserException( "error building algorithmtree", e) );
		} catch (IllegalAccessException e) {
			handleUserException( new UserException( "error building algorithmtree", e) );
		}
	}
	
	public ArrayList<AlgorithmInfo> getAlgorithms() {
		return PLUGIN_MANAGER.getAlgorithms();
	}
	
	public AlgorithmsTree getAlgorithmTree() {
		return algorithms;
	}
	
	protected Algorithm getAlgorithmByName( String classname ) {
		Algorithm algorithm = null;
		try {
			algorithm = (Algorithm) getClass().getClassLoader().loadClass( classname ).newInstance();
		} catch (InstantiationException e) {
			handleUserException( new UserException( "algorithm: " + classname + " could not be initiated", e ) );
		} catch (IllegalAccessException e) {
			handleUserException( new UserException( "algorithm: " + classname + " could not be accessed", e ) );
		} catch (ClassNotFoundException e) {
			handleUserException( new UserException( "algorithm: " + classname + " could not be found", e ) );
		}
		return algorithm;
	}
	
	public static MainPad getInstance() {
		return mainPad;
	}
	
	public EditorDesktopView getDesktop() {
		return DESKTOP;
	}

	public void handleUserException( final UserException e ) {
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog( MainPad.getInstance(), e.getComponent(), e.getMessage(), JOptionPane.ERROR_MESSAGE );
			}
		});
	}
	
	public static ImageIcon createImageIcon( String image ) {
		URL url = Object.class.getResource( "/de/hu/gralog/resources/images/newimages/" + image );
		if ( url == null )
			url = Object.class.getResource( "/de/hu/gralog/resources/images/images/" + image );
		return new ImageIcon( url );
	}
	
	private class MainPadWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent arg0) {
			boolean abort = false;
			try {
				DESKTOP.prepareSerializing();
			} catch(OperationAbortedException e ) {
				abort = true;
			}
			if ( !abort ) {
				saveSettings();
				dispose();
			}
		}
		
	}

	public void exit() {
		new MainPadWindowListener().windowClosing( null );
	}
	
	public static class EditorState {
		public static final EditorState SELECT = new EditorState( MainPad.createImageIcon( "designer_arrow.png"), "designer_arrow_cursor.png", new Point( 8, 1 ), "Select" );
		public static final EditorState CREATE_VERTEX = new EditorState( MainPad.createImageIcon( "addpointcursor.png"), "addpointcursor_cursor.png", new Point( 8, 8 ), "Add Node" );
		public static final EditorState CREATE_EDGE = new EditorState( MainPad.createImageIcon( "freehandcursor.png"),  "freehandcursor_cursor.png", new Point( 8, 8 ), "Add Edge" );
		public static final EditorState MARQUE_ZOOM = new EditorState( MainPad.createImageIcon( "stock_zoom-object.png"),  "stock_zoom-object_cursor.png", new Point( 0, 0 ), "Marquee Zoom" );
		public static final EditorState PAN = new EditorState( MainPad.createImageIcon( "drag.png"),  "drag_cursor.png", new Point( 8, 0 ), "dragging_cursor.png", new Point( 16, 0 ), "Pan" );
		public static final EditorState INTERACTIVE_ZOOM = new EditorState( MainPad.createImageIcon( "stock_zoom-page-width.png"),  "stock_zoom-page-width_cursor.png", new Point( 0, 0 ), "Interactive Zoom" );
		
		private String displayName;
		private String cursorName;
		private Point hotSpot;
		private Point secondHotSpot;
		private String secondCursorName;
		private Cursor cursor;
		private Cursor secondCursor;
		private ImageIcon icon;
		
		public EditorState( ImageIcon icon, String cursorName, Point hotSpot, String displayName ) {
			this.displayName = displayName;
			this.icon = icon;
			this.cursorName = cursorName;
			this.hotSpot = hotSpot;
		}
		
		public EditorState( ImageIcon icon, String cursorName, Point hotSpot, String secondCursorName, Point secondHotSpot, String displayName ) {
			this.displayName = displayName;
			this.icon = icon;
			this.cursorName = cursorName;
			this.hotSpot = hotSpot;
			this.secondCursorName = secondCursorName;
			this.secondHotSpot = secondHotSpot;
		}
				
		public Cursor getCursor() {
			if ( cursor == null )
				cursor = MainPad.getInstance().createCursor( cursorName, hotSpot );
			return cursor;
		}
		
		public Cursor getSecondCursor() {
			if ( secondCursor == null && secondCursorName != null )
				secondCursor = MainPad.getInstance().createCursor( secondCursorName, secondHotSpot );
			return secondCursor;
		}
		
		public ImageIcon getIcon() {
			return icon;
		}
		
		public String getDisplayName() {
			return displayName;
		}
	}
	
	public static interface EditorStateListener {
		public void editorStateChanged( EditorState from, EditorState to );
	}
	
	protected void fireEditorStateChanged( EditorState from, EditorState to) {
		for (int i = 0;i < editorStateListeners.size(); i++)
			((EditorStateListener)editorStateListeners.get( i )).editorStateChanged( from, to );
	}
	
	public void addEditorStateListener( EditorStateListener l ) {
		if ( ! editorStateListeners.contains( l ) )
			editorStateListeners.add( l );
	}
	
	public void removeEditorStateListener( EditorStateListener l ) {
		editorStateListeners.remove( l );
	}
	
	public void showView( View view ) {
		view.restore();
	}
	
	private static class MainPadViewListener extends DockingWindowAdapter {

		
		private boolean containsView( DockingWindow window, View view ) {
			if ( window == view )
				return true;
			for ( int i = 0; i < window.getChildWindowCount(); i++ ) {
				if ( containsView( window.getChildWindow( i ), view ) )
					return true;
			}
			return false;
		}
		
		@Override
		public void windowClosed(DockingWindow window) {
			
			for ( ShowViewAction action : SHOW_VIEW_ACTIONS ) {
				if ( containsView( window, action.getView() ) )
					action.setEnabled( true );
			}
		}

		@Override
		public void windowRestored(DockingWindow window) {
			for ( ShowViewAction action : SHOW_VIEW_ACTIONS ) {
				if ( containsView( window, action.getView() ) )
					action.setEnabled( false );
			}
		}

		@Override
		public void windowAdded(DockingWindow arg0, DockingWindow arg1) {
			windowRestored( arg1 );
		}
	}
	
	private static class MainPadViewMap extends ViewMap {

		public MainPadViewMap( View[] views ) {
			super( views );
		}
		
		public View readView(ObjectInputStream in) throws IOException {
			View view = super.readView( in );
			if ( view instanceof EditorDesktopView )
				((EditorDesktopView)view).readView( in );
			return view;
		}

		public void writeView(View view, ObjectOutputStream out) throws IOException {
			super.writeView( view, out);
			if ( view instanceof EditorDesktopView )
				((EditorDesktopView)view).writeView( out );
		}
		
	}

	public void documentGraphReplaced(GJGraph oldGraph, GJGraph newGraph) {
		// do nothing
	}
}
