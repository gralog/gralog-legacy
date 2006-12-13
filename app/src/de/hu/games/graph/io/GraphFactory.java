/*
 * Created on 8 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.games.graph.io;

import de.hu.games.app.UserException;
import de.hu.games.graph.GraphTypeInfo;
import de.hu.games.graph.GraphWithEditableElements;
import de.hu.games.gui.MainPad;

public class GraphFactory {
	public static GraphWithEditableElements createGraph( String className ) {
		try {
			GraphWithEditableElements graph = ((GraphTypeInfo)MainPad.getInstance().getClassLoader().loadClass( className ).newInstance()).newInstance();
			return graph;
		} catch (InstantiationException e) {
			MainPad.getInstance().handleUserException( new UserException( "unable to load graphTypeInfo " + className, e) );
		} catch (IllegalAccessException e) {
			MainPad.getInstance().handleUserException( new UserException( "unable to load graphTypeInfo " + className, e) );
		} catch (ClassNotFoundException e) {
			MainPad.getInstance().handleUserException( new UserException( "unable to load graphTypeInfo " + className, e) );
		}
		return null;
	}
}
