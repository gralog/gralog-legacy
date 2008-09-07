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
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
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
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.VertexView;
import org.jgrapht.ListenableGraph;

import de.hu.gralog.algorithm.result.DisplaySubgraph;
import de.hu.gralog.algorithm.result.DisplaySubgraphListener;
import de.hu.gralog.algorithm.result.ElementTips;
import de.hu.gralog.algorithm.result.ElementTipsListener;
import de.hu.gralog.algorithm.result.DisplaySubgraph.DisplayMode;
import de.hu.gralog.beans.event.PropertyChangeListenable;
import de.hu.gralog.graph.GralogGraphSupport;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;
import de.hu.gralog.jgraph.cellview.VertexDisplayModeRenderer;
import de.hu.gralog.jgrapht.ext.JGraphViewableGraphModelAdapter;

/**
 * 
 * This class overrides JGraph in order to:
 * - display {@link GralogGraph GralogGraph}
 * - listen to elementchanges of the underlying {@link GralogGraph GralogGraph}
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
	
	private GralogGraphSupport graphSupport;
	private Vector registeredSubgraphs = new Vector();
	private Vector highlightObjects = new Vector();
	protected ArrayList<ElementTips> registeredElementTips = new ArrayList<ElementTips>();
	private boolean elementsAndStructureEditable = true;
	
	public GJGraph( GralogGraphSupport grapht ) {
		this( grapht, null );
	}
	
	public<V,E,GB, G extends ListenableGraph<V,E>> GJGraph( GralogGraphSupport<V,E,GB,G> graphSupport, Hashtable<V, Point> vertexPositions ) {
		super( new JGraphViewableGraphModelAdapter<V,E,GB,G>( graphSupport, vertexPositions ) );
		
		this.graphSupport = graphSupport;

		this.setGraphLayoutCache( new GGraphLayoutCache( getModel(), new GJGraphCellViewFactory() ) );
		this.getSelectionModel().addGraphSelectionListener( graphSupport.getGraphSelectionSupport() );
		this.setMarqueeHandler( new GMarqueeHandler() );

		this.setSizeable( false );
		this.setEditable( false );
		this.setPortsScaled( false );
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
		
		/* Performance settings */
		this.setPortsVisible( false );
		this.getGraphLayoutCache().setSelectsAllInsertedCells( true );
		this.getGraphLayoutCache().setSelectsLocalInsertedCells( true );
		this.setDoubleBuffered( true );
	}


	public GJGraph(GraphModel model, GraphLayoutCache view, GralogGraphSupport graphSupport ) {
		super( model, view );
		this.graphSupport = graphSupport;
	}

	public JGraphViewableGraphModelAdapter getGModel() {
		return (JGraphViewableGraphModelAdapter)super.getModel();
	}
	public GralogGraphSupport getGraphT() {
		return graphSupport;
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
		Object back = graphSupport.getGraph().addEdge( ((DefaultGraphCell)source).getUserObject(), ((DefaultGraphCell)target).getUserObject() );
		return back != null;
	}
	
	public void insertVertexAt(Point2D point) {
		snap( point );
		fromScreen( point );
		
		Object vertex = graphSupport.createVertex();
		DefaultGraphCell vertexCell = getGModel().getCellFactory().createVertexCell( vertex );
		vertexCell.add(new DefaultPort());
		
		Rectangle2D bounds = GraphConstants.getBounds( vertexCell.getAttributes() );
		bounds.setRect( point.getX(), point.getY(), bounds.getWidth(), bounds.getHeight() );
		
		vertexCell.setAttributes( (AttributeMap)getGModel().getDefaultVertexAttributes().clone() );
		GraphConstants.setBounds( vertexCell.getAttributes(), bounds );
		
		getGraphLayoutCache().insert( vertexCell );
	}
	
	public void registerSubgraph( DisplaySubgraph subgraph ) {
		if ( ! registeredSubgraphs.contains( subgraph ) ) {
			subgraph.addDisplaySubgraphListener( this );
			registeredSubgraphs.add( subgraph );
			getGraphLayoutCache().cellViewsChanged( getGraphLayoutCache().getAllViews() );
		}
	}
	
	public void deregisterAllSubgraphs() {
		registeredSubgraphs.removeAllElements();
		getGraphLayoutCache().cellViewsChanged( getGraphLayoutCache().getAllViews() );
	}
	
	public void highlightObjects( Vector objects ) {
		if ( objects == highlightObjects )
			return;
		Vector<CellView> views = new Vector<CellView>();
		
		for ( Object userObject : highlightObjects )
			views.add( getGraphLayoutCache().getMapping( getGModel().getGraphCell( userObject ) , false ) );
		
		highlightObjects = objects;

		for ( Object userObject : highlightObjects )
			views.add( getGraphLayoutCache().getMapping( getGModel().getGraphCell( userObject ) , false ) );
		Arrays a;
		if ( ! views.isEmpty() )
			getGraphLayoutCache().cellViewsChanged( views.toArray( new CellView[0] ) );
	}
	
	public void registerElementTips( ElementTips elementTip ) {
		if ( ! registeredElementTips.contains( elementTip ) ) {
			elementTip.addElementTipsListener( this );
			registeredElementTips.add( elementTip );
			
			Vector<CellView> views = new Vector<CellView>();
			for ( Object userObject : elementTip.getElementTips().keySet() )
				views.add( getGraphLayoutCache().getMapping( getGModel().getGraphCell( userObject ), false ) );
			
			getGraphLayoutCache().cellViewsChanged( views.toArray( new CellView[0] ) );
		}
	}
	
	public void deregisterAllElementTips() {
		registeredElementTips.clear();
		getGraphLayoutCache().cellViewsChanged( getGraphLayoutCache().getAllViews() );
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
	
	@Override
	public String getToolTipText(MouseEvent e) {
		if (e != null) {
			if ( getFirstCellForLocation(e.getX(), e.getY() ) != null ) {
				Object userObject = getGModel().getValue( getFirstCellForLocation(e.getX(), e.getY()) );
				return getToolTip( userObject );
			}
		}
		return null;
	}

	public void displayUpdated(DisplaySubgraph subgraph) {
		getGraphLayoutCache().cellViewsChanged( getGraphLayoutCache().getAllViews() );
	}

	public DisplayMode getCellDisplayMode( Object userObject ) {
		DisplayMode displayModeHighest = DisplayMode.HIDE;
		boolean existsMode = false;
		for (int i = 0; i < registeredSubgraphs.size();i++ ) {
			DisplaySubgraph subgraph = (DisplaySubgraph)registeredSubgraphs.get( i );
			if ( subgraph.getMode().isVisible() ) {
				existsMode = true;
				DisplayMode displayMode = subgraph.getDisplayMode( userObject );
				if ( displayMode.overwrites( displayModeHighest ) )
					displayModeHighest = displayMode;
			}
		}
		if ( ! existsMode )
			displayModeHighest = DisplayMode.SHOW;
		
		if ( registeredSubgraphs.size() == 0 && highlightObjects != null && highlightObjects.contains( userObject ) )
			displayModeHighest = DisplayMode.HIGH2;
		return displayModeHighest;
	}
	
	public void updateUI() {
		setUI(new GJGraphUI());
		invalidate();
	}
	
	public DefaultEdgeRenderer getEdgeRenderer() {
		if ( graphSupport.getEdgeRenderer() != null )
			return graphSupport.getEdgeRenderer();
		return defaultEdgeRenderer;
	}
	
	public VertexDisplayModeRenderer getVertexRenderer() {
		if (graphSupport.getVertexRenderer() != null )
			return graphSupport.getVertexRenderer();
		return defaultVertexRenderer;
	}
	
	/**
	 * Converts the specified value to string. If the value is an instance of
	 * CellView then the corresponding value or cell is used.
	 */
	public String convertValueToString(Object value) {
		Object cell = value;
		if ( value instanceof CellView )
			cell = ((CellView)value).getCell();
		if ( ! getModel().isPort( cell ) ) {
			Object userObject = getGModel().getValue( cell );
			if ( userObject instanceof PropertyChangeListenable )
				return String.valueOf( userObject );
			return null;
		}
		return String.valueOf(value);
	}
	
	private GJGraph getGraph() {
		return this;
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
		getGraphLayoutCache().cellViewsChanged( getGraphLayoutCache().getAllViews() );
	}

	
	/*@Override
	public GJGraph clone() {
		try {
			
			HashMap vMap = new HashMap();
			GraphTypeInfo gti = getGraphT().getTypeInfo();
			GralogGraph grapht = gti.copyGraph(getGraphT(), vMap);

			if (grapht == null || vMap.isEmpty()) {
				// copyGraph Funktion not properly overridden
				System.out.println(" [ \"copyGraph\"-function of " + getGraphT().getTypeInfo().getName()
						+" not properly overridden.\nWill use default (very slow) function. ]");
				return new XMLDecoderIO().getDataCopy( this );
			}
			
			GJGraph retGJGraph = new GJGraph(grapht);
			
			AttributeMap changes = new AttributeMap();

			
			// Position vertices in new GJGraph:
			for ( Object cell : this.getGModel().getVertexCells()) {
				
				DefaultGraphCell vertexCell = (DefaultGraphCell)cell;
				Object newVertex = vMap.get( vertexCell.getUserObject() );
	
				Rectangle2D bounds = GraphConstants.getBounds( this.getAttributes( vertexCell ) );
				Point vertexPosition = new Point( (int)bounds.getX(), (int)bounds.getY() );
	
				retGJGraph.snap( vertexPosition );
				retGJGraph.fromScreen( vertexPosition );
				
				DefaultGraphCell newCell = retGJGraph.getGModel().getVertexCell( newVertex );
				Rectangle2D newBounds = (Rectangle2D)retGJGraph.getGraphLayoutCache().getMapping( newCell, true ).getBounds().clone();
				newBounds.setRect( vertexPosition.getX(), vertexPosition.getY(), newBounds.getWidth(), newBounds.getHeight() );

				AttributeMap editAttr = new AttributeMap();	
				GraphConstants.setBounds( editAttr, newBounds );
				changes.put( newCell, editAttr );
			}
			
			retGJGraph.getGModel().edit( changes, null, null, null ); // dauert noch 94 - 180(1330) ms 
			
			return retGJGraph;
		
		} catch (Exception e) {
			System.out.println(" [ Exception in \"copyGraph\"-function of "
					+ getGraphT().getTypeInfo().getName() + ":\n"
					+ e.toString()
					+"\nWill use default (VERY slow) function. ]");
			return new XMLDecoderIO().getDataCopy( this );
		}
		
	}*/
}
