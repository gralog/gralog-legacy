package de.hu.gralog.jgraph;

import org.jgraph.JGraph;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

public class JGraphFixDirtyRegion extends JGraph {

	public JGraphFixDirtyRegion() {
		super();
	}

	public JGraphFixDirtyRegion(GraphModel model) {
		super(model);
	}

	public JGraphFixDirtyRegion(GraphLayoutCache cache) {
		super(cache);
	}

	public JGraphFixDirtyRegion(GraphModel model, GraphLayoutCache cache) {
		super(model, cache);
	}

	public JGraphFixDirtyRegion(GraphModel model, BasicMarqueeHandler mh) {
		super(model, mh);
	}

	public JGraphFixDirtyRegion(GraphModel model, GraphLayoutCache layoutCache,
			BasicMarqueeHandler mh) {
		super(model, layoutCache, mh);
	}

	@Override
	public void updateUI() {
		setUI(new BasicGraphUIFixDirtyRegion());
		invalidate();
	}

	
}
