/*
 * Created on 9 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.algorithm.result;

import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.GralogGraph;

public class AlgorithmResultInteractiveContent extends AlgorithmResultContent {
	private AlgorithmResult result = null;
	
	public AlgorithmResultInteractiveContent( GralogGraph graph ) {
		super( graph );
	}
	
	void setResult( AlgorithmResult result ) {
		this.result = result;
	}

	protected void fireContentChanged() throws UserException {
		result.fireCurrentContentChanged();
	}
}
