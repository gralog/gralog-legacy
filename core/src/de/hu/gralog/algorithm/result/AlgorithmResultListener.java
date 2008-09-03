/*
 * Created on 9 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.algorithm.result;

import de.hu.gralog.app.UserException;

/**
 * This interface defines a listener that listens to
 * {@link AlgorithmResult AlgorithmResult's}. This listener is used by gralog
 * to inform gralog of changes in AlgorithmResult triggered by an
 * {@link AlgorithmResultInteractiveContent} and should not be used by plugin
 * developers.
 * 
 * @author Sebastian
 * 
 */
public interface AlgorithmResultListener {
	public void currentContentChanged() throws UserException;
}
