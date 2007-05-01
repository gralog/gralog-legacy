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

package de.hu.gralog.jgraph;

import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.VertexView;

import de.hu.gralog.graph.ElementAttributes;
import de.hu.gralog.graph.GraphWithEditableElements;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;
import de.hu.gralog.jgraph.cellview.VertexDisplayModeRenderer;
import de.hu.gralog.jgrapht.edge.DefaultListenableEdge;
import de.hu.gralog.jgrapht.ext.JGraphViewableGraphModelAdapter;
import de.hu.gralog.jgrapht.graph.DisplaySubgraph;
import de.hu.gralog.jgrapht.graph.DisplaySubgraphListener;
import de.hu.gralog.jgrapht.graph.ElementTips;
import de.hu.gralog.jgrapht.graph.ElementTipsListener;
import de.hu.gralog.jgrapht.graph.DisplaySubgraph.DisplayMode;
import de.hu.gralog.jgrapht.vertex.DefaultListenableVertex;

/**
 * 
 * This class overrides JGraph in order to:
 * - display {@link GraphWithEditableElements GraphWithEditableElements}
 * - listen to elementchanges of the underlying {@link GraphWithEditableElements GraphWithEditableElements}
 * - add the functionality in order to highlight subgraphs, and to listen to changes of such subgraphs
 * - add tooltips funtionality to vertices and edges
 * - use a custom MarqueeHandler {@link GMarqueeHandler EditableMarqueeHandler}
 * - fix some errors concerning the adaption to jgrapht (now you can change the structure of your grapht-object after
 *   adding it to GJGraph)
 * - provide some userinterface customization of JGraph {@link GJGraphUI GJGraphUI}
 * 
 * 
 * @author Sebastian
 *
 */
public class GJGraph extends JGraph implements DisplaySubgraphListener, ElementTipsListener, Cloneable {
	
	private final DefaultEdgeRenderer defaultEdgeRenderer = new DefaultEdgeRenderer(  );
	private final VertexDisplayModeRenderer defaultVertexRenderer = new DefaultVertexRenderer(  );
	
	private GraphWithEditableElements grapht;
	private Vector registeredSubgraphs = new Vector();
	private Vector highlightObjects = new Vector();
	protected ArrayList<ElementTips> registeredElementTips = new ArrayList<ElementTips>();
	private boolean elementsAndStructureEditable = true;
	
	public GJGraph(GraphWithEditableElements grapht) {
		super( );
		
		this.grapht = grapht;
		this.setGraphLayoutCache( new GGraphLayoutCache( new GJGraphCellViewFactory() ) );
		this.setMarqueeHandler( new GMarqueeHandler() );
		this.setModel( new JGraphViewableGraphModelAdapter( grapht ) );
		
		this.getSelectionModel().addGraphSelectionListener( grapht );

		this.setSizeable( false );
		this.setEditable( false );
		this.setPortsScaled( true );
		this.setTolerance( 5 );
		this.setGridEnabled( true );
		this.setGridMode( CROSS_GRID_MODE );
		this.setGridSize( 40 );
		this.setGridVisible( true );
		this.setAutoscrolls( false );
		this.setAutoResizeGraph( true );
		this.setDragEnabled( true );
		this.setDropEnabled( true );
		this.setCloneable( true );
		
		KeyStroke keyStroke = KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0 );
		ActionMap actionMap = new ActionMap();
		actionMap.put(keyStroke, MainPad.EDIT_DELETE_ACTION );
		InputMap inputMap = new InputMap();
		inputMap.put( keyStroke, keyStroke );
		this.setActionMap( actionMap );
		this.setInputMap(0, inputMap );
		
		ToolTipManager.sharedInstance().registerComponent( this );
	}

	public GJGraph(GraphModel model, GraphLayoutCache view, GraphWithEditableElements graph) {
		super( model, view );
		this.grapht = graph;
	}

	public JGraphViewableGraphModelAdapter getGModel() {
		return (JGraphViewableGraphModelAdapter)super.getModel();
	}
	public GraphWithEditableElements getGraphT() {
		return grapht;
	}
	
	public GMarqueeHandler getEditableMarqueeHandler() {
		return (GMarqueeHandler)getMarqueeHandler();
	}
	
	public void setElementsAndStructureEditable( boolean editable ) {
		this.elementsAndStructureEditable = editable;
		if ( editable ) {
			setCloneable( true );
		} else {
			setDragEnabled( false );
			setDropEnabled( false );
			setCloneable( false );
		}
	}
	
	public boolean isElementsAndStructureEditable() {
		return elementsAndStructureEditable;
	}
	
	public boolean addEdge( Object source, Object target ) {
		Object back = grapht.addEdge( ((DefaultGraphCell)source).getUserObject(), ((DefaultGraphCell)target).getUserObject() );
		clearSelection();
		return back != null;
	}
	
	public void insertVertexAt(Point2D point) {
		snap( point );
		fromScreen( point );
		DefaultGraphCell cell = ((JGraphViewableGraphModelAdapter)this.getModel()).getCellFactory().createVertexCell( grapht.createVertex() );
		cell.add( new DefaultPort() );
		
		AttributeMap cellattrs = ElementAttributes.getVertexAttributes(  );
		Rectangle2D bounds = GraphConstants.getBounds( cellattrs );
		
		bounds.setRect( point.getX(), point.getY(), bounds.getWidth(), bounds.getHeight() );
		
		AttributeMap attrs = new AttributeMap();
		attrs.put( cell, cellattrs );
		
		getModel().insert( new Object[] { cell }, attrs, null, null, null );
		GraphWithEditableElements g = ((GraphWithEditableElements)grapht);
		
		this.clearSelection();
	}
	
	public void positionVertexAt(Object vertex, Point2D point) {
		snap( point );
		fromScreen( point );
		
		DefaultGraphCell cell = getGModel().getVertexCell( vertex );
		Rectangle2D bounds = (Rectangle2D)this.getGraphLayoutCache().getMapping( cell, true ).getBounds().clone();
		bounds.setRect( point.getX(), point.getY(), bounds.getWidth(), bounds.getHeight() );
		
		AttributeMap editAttr = new AttributeMap();
		GraphConstants.setBounds( editAttr, bounds );
		AttributeMap changes = new AttributeMap();
		changes.put( cell, editAttr );
		getGModel().edit( changes, null, null, null );
	}
	
	public void registerSubgraph( DisplaySubgraph subgraph ) {
		if ( ! registeredSubgraphs.contains( subgraph ) ) {
			subgraph.addDisplaySubgraphListener( this );
			registeredSubgraphs.add( subgraph );
		}
		graphDidChange();
	}
	
	public void deregisterAllSubgraphs() {
		registeredSubgraphs.removeAllElements();
		graphDidChange();
	}
	
	public void highlightObjects( Vector objects ) {
		if ( objects == highlightObjects )
			return;
		highlightObjects = objects;
		graphDidChange();
	}
	
	public void registerElementTips( ElementTips elementTip ) {
		if ( ! registeredElementTips.contains( elementTip ) ) {
			elementTip.addElementTipsListener( this );
			registeredElementTips.add( elementTip );
			graphDidChange();
		}
	}
	
	public void deregisterAllElementTips() {
		registeredElementTips.clear();
		graphDidChange();
	}
	
	public String getToolTip( Object userObject ) {
		for ( ElementTips tip : registeredElementTips ) {
			if ( tip.getMode().isVisible() && userObject != null ) {
				Object t = tip.getElementTips().get( userObject );
				if ( t != null )
					return t.toString();
			}
		}
		return null;
	}
	
	public void displayUpdated(DisplaySubgraph subgraph) {
		graphDidChange();
	}

	public DisplayMode getCellDisplayMode( Object userObject ) {
		DisplayMode displayModeHighest = DisplayMode.SHOW;
		
		for (int i = 0; i < registeredSubgraphs.size();i++ ) {
			DisplaySubgraph subgraph = (DisplaySubgraph)registeredSubgraphs.get( i );
			if ( subgraph.getMode().isVisible() ) {
				DisplayMode displayMode = subgraph.getDisplayMode( userObject );
				if ( displayMode.overwrites( displayModeHighest ) )
					displayModeHighest = displayMode;
			}
		}
		
		if ( registeredSubgraphs.size() == 0 && highlightObjects != null && highlightObjects.contains( userObject ) )
			displayModeHighest = DisplayMode.HIGH2;
		return displayModeHighest;
	}
	
	public void updateUI() {
		setUI(new GJGraphUI());
		invalidate();
	}
	
	private GJGraph getGraph() {
		return this;
	}
	
	public DefaultEdgeRenderer getEdgeRenderer() {
		if ( grapht.getEdgeRenderer() != null )
			return grapht.getEdgeRenderer();
		return defaultEdgeRenderer;
	}
	
	public VertexDisplayModeRenderer getVertexRenderer() {
		if (grapht.getVertexRenderer() != null )
			return grapht.getVertexRenderer();
		return defaultVertexRenderer;
	}
	
	
	
	/**
	 * Converts the specified value to string. If the value is an instance of
	 * CellView then the corresponding value or cell is used.
	 */
	public String convertValueToString(Object value) {
		if (value instanceof CellView) {
			CellView view = (CellView)value;
			if ( view instanceof VertexView ) {
				if ( ((DefaultGraphCell)view.getCell()).getUserObject() instanceof DefaultListenableVertex )
					value = view.getCell();
				else
					return null;
			}
			if ( value instanceof EdgeView ) {
				if ( ((DefaultGraphCell)view.getCell()).getUserObject() instanceof DefaultListenableEdge )
					value = view.getCell();
				else
					return null;
			}
		}
		return String.valueOf(value);
	}
	
	/**
	 * Returns the given rectangle applied to the grid.
	 * 
	 * @param r
	 *            a rectangle in screen coordinates.
	 * @return the same rectangle applied to the grid.
	 */
	public Rectangle2D snap(Rectangle2D r) {
		if (gridEnabled && r != null) {
			double sgs = gridSize * getScale();
			r.setFrame(Math.round(Math.round(r.getX() / sgs) * sgs), Math
					.round(Math.round(r.getY() / sgs) * sgs), Math
					.round(Math.round(r.getWidth() / sgs + 0.5 ) * sgs), Math
					.round(Math.round(r.getHeight() / sgs + 0.5 ) * sgs));
		}
		return r;
	}
	
	private class GJGraphCellViewFactory extends DefaultCellViewFactory {

		protected EdgeView createEdgeView(Object cell) {
			return new GEdgeView( getGraph(), cell );
		}

		protected VertexView createVertexView(Object cell) {
			return new GVertexView( getGraph(), cell );
		}
		
	}

	public void elementTipsChanged() {
		graphDidChange();
	}
	
	@Override
	public GJGraph clone() {
		return getGraphT().getTypeInfo().copyGraph(this);
	}
}
