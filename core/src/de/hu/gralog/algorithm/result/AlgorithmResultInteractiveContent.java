/*
 * Created on 9 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.algorithm.result;

import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.GralogGraphSupport;

/**
 * This class extends {@link AlgorithmResultContent} in order to add
 * interactivity to AlgorithmResultContents. It provides a protected method
 * {@link #fireContentChanged()} for subclasses to call whenever the content has
 * changed triggering gralog to redisplay the content.
 * 
 * @author Sebastian
 * 
 */

public class AlgorithmResultInteractiveContent extends AlgorithmResultContent {
	private AlgorithmResult result = null;

	/**
	 * @see AlgorithmResultContent#AlgorithmResultContent(GralogGraphSupport)
	 * 
	 * @param graphSupport
	 */

	public AlgorithmResultInteractiveContent(GralogGraphSupport graphSupport) {
		super(graphSupport);
	}

	/**
	 * This method is called by gralog before the result is displayed.
	 * 
	 * @param result
	 */
	void setResult(AlgorithmResult result) {
		this.result = result;
	}

	/**
	 * Calling this method will inform gralog to redisplay this content.
	 * 
	 * @throws UserException
	 */
	protected void fireContentChanged() throws UserException {
		result.fireCurrentContentChanged();
	}
}
