package de.hu.gralog.jgraph;

import java.awt.geom.Rectangle2D;

import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.plaf.basic.BasicGraphUI;
import org.jgraph.util.RectUtils;

import de.hu.gralog.jgrapht.ext.JGraphViewableGraphModelAdapter.GraphModelEdit;

public class BasicGraphUIFixDirtyRegion extends BasicGraphUI {

	public BasicGraphUIFixDirtyRegion() {
		super();
	}
	
	@Override
	protected GraphModelListener createGraphModelListener() {
		return new GraphModelHandler();
	}
	
	public class GraphModelHandler extends BasicGraphUI.GraphModelHandler {
		public void graphChanged(GraphModelEvent e) {
			Object[] removed = e.getChange().getRemoved();
			// Remove from selection & focus
			if (removed != null && removed.length > 0) {
				// Update Focus If Necessary
				if (focus != null) {
					Object focusedCell = focus.getCell();
					for (int i = 0; i < removed.length; i++) {
						if (removed[i] == focusedCell) {
							lastFocus = focus;
							focus = null;
							break;
						}
					}
				}
				// Remove from selection
				graph.getSelectionModel().removeSelectionCells(removed);
			}
			Rectangle2D oldDirty = null;
			Rectangle2D dirtyRegion;
			if ( e.getChange() instanceof GraphModelEdit )
				dirtyRegion = ((GraphModelEdit)e.getChange()).getDirtyRegion( graphLayoutCache );
			else
				dirtyRegion = e.getChange().getDirtyRegion();
			if (dirtyRegion == null) {
				oldDirty = graph.getClipRectangle(e.getChange());
			}
			
			if (graphLayoutCache != null ) {
				graphLayoutCache.graphChanged(e.getChange());
			}
			// Get arrays
			Object[] inserted = e.getChange().getInserted();
			Object[] changed = e.getChange().getChanged();
			// Insert
			if (inserted != null && inserted.length > 0) {
				// Update focus to first inserted cell
				for (int i = 0; i < inserted.length; i++)
					graph.updateAutoSize(graphLayoutCache.getMapping(
							inserted[i], false));
			}
			// Change (update size)
			if (changed != null && changed.length > 0) {
				for (int i = 0; i < changed.length; i++)
					graph.updateAutoSize(graphLayoutCache.getMapping(
							changed[i], false));
			}
			if (dirtyRegion == null) {
				Rectangle2D newDirtyRegion = graph.getClipRectangle(e.getChange());
				dirtyRegion = RectUtils.union(oldDirty, newDirtyRegion);
				if ( e.getChange() instanceof GraphModelEdit ) {
					((GraphModelEdit)e.getChange()).putDirtyRegion(graphLayoutCache, dirtyRegion);
					e.getChange().setDirtyRegion( dirtyRegion );
				}
				else
					e.getChange().setDirtyRegion( dirtyRegion );
			}
			
			if (dirtyRegion != null) {
				graph.addOffscreenDirty(dirtyRegion);
			}
			// Select if not partial
			if (!graphLayoutCache.isPartial()
					&& graphLayoutCache.isSelectsAllInsertedCells()
					&& graph.isEnabled()) {
				Object[] roots = DefaultGraphModel.getRoots(graphModel,
						inserted);
				if (roots != null && roots.length > 0) {
					lastFocus = focus;
					focus = graphLayoutCache.getMapping(roots[0], false);
					graph.setSelectionCells(roots);
				}
			}
			updateSize();
		}
	}



	
}
