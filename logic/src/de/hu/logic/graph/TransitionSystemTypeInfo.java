/*
 * Created on 7 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.logic.graph;

import de.hu.games.graph.GraphTypeInfo;
import de.hu.games.graph.GraphWithEditableElements;

public class TransitionSystemTypeInfo extends GraphTypeInfo {

	public String getName() {
		return "TransitionSystem";
	}

	public GraphWithEditableElements newInstance() {
		TransitionSystem system = new TransitionSystem();
		system.setTypeInfo( this );
		return system;
	}

}
