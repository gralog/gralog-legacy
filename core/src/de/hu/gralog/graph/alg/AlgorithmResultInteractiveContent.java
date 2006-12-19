/*
 * Created on 9 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.graph.alg;

import org.jgrapht.Graph;

public class AlgorithmResultInteractiveContent extends AlgorithmResultContent {
	private AlgorithmResult result = null;
	
	public AlgorithmResultInteractiveContent( Graph graph ) {
		super( graph );
	}
	
	void setResult( AlgorithmResult result ) {
		this.result = result;
	}

	protected void fireContentChanged() {
		result.fireCurrentContentChanged();
	}
}
