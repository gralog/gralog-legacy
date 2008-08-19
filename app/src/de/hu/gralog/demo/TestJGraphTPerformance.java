/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2006, by Barak Naveh and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* ----------------------
 * JGraphAdapterDemo.java
 * ----------------------
 * (C) Copyright 2003-2006, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 03-Aug-2003 : Initial revision (BN);
 * 07-Nov-2003 : Adaptation to JGraph 3.0 (BN);
 *
 */
package de.hu.gralog.demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JFrame;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;
import org.jgrapht.DirectedGraph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;
import org.jgrapht.graph.DirectedMultigraph;


/**
 * A demo applet that shows how to use JGraph to visualize JGraphT graphs.
 *
 * @author Barak Naveh
 * @since Aug 3, 2003
 */
public class TestJGraphTPerformance
    extends JApplet
{

    //~ Static fields/initializers --------------------------------------------

    private static final long serialVersionUID = 3256444702936019250L;
    private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
    private static final Dimension DEFAULT_SIZE = new Dimension(530, 320);

    //~ Instance fields -------------------------------------------------------

    //
    private JGraphModelAdapter jgAdapter;

    //~ Methods ---------------------------------------------------------------

    /**
     * An alternative starting point for this demo, to also allow running this
     * applet as an application.
     *
     * @param args ignored.
     */
    public static void main(String [] args)
    {
    	TestJGraphTPerformance applet = new TestJGraphTPerformance();
        applet.initJGraph();

        JFrame frame = new JFrame();
        frame.getContentPane().add(applet);
        frame.setTitle("JGraphT Adapter to JGraph Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void initJGraph() {
        int countVertices = 100;
        GraphModel model = new DefaultGraphModel();
	JGraph graph = new JGraph(model);

	DefaultGraphCell[] vertices = new DefaultGraphCell[countVertices];

        for ( int i = 0; i < countVertices;i++ )
            vertices[i] = createVertex("v" + i, 20, 20, 40, 20, null, false);

       	graph.getGraphLayoutCache().insert(vertices);

        for ( int i = 0; i < countVertices; i++ ) {
            for ( int r = 0; r < countVertices; r++ ) {
                org.jgraph.graph.DefaultEdge edge = new org.jgraph.graph.DefaultEdge();
        	// Fetch the ports from the new vertices, and connect them with the edge
                edge.setSource(vertices[i].getChildAt(0));
                edge.setTarget(vertices[r].getChildAt(0));
                
                graph.getGraphLayoutCache().insert( edge );
            }
        }

        adjustDisplaySettings(graph);
        getContentPane().add(graph);
        resize(DEFAULT_SIZE);

    }
    
    public static DefaultGraphCell createVertex(String name, double x,
			double y, double w, double h, Color bg, boolean raised) {

		// Create vertex with the given name
		DefaultGraphCell cell = new DefaultGraphCell(name);

		// Set bounds
		GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double(
				x, y, w, h));

		// Set fill color
		if (bg != null) {
			GraphConstants.setGradientColor(cell.getAttributes(), Color.orange);
			GraphConstants.setOpaque(cell.getAttributes(), true);
		}

		// Set raised border
		if (raised)
			GraphConstants.setBorder(cell.getAttributes(), BorderFactory
					.createRaisedBevelBorder());
		else
			// Set black border
			GraphConstants.setBorderColor(cell.getAttributes(), Color.black);

		// Add a Port
		DefaultPort port = new DefaultPort();
		cell.add(port);
		port.setParent(cell);

		return cell;
	}


    
    /**
     * {@inheritDoc}
     */
    public void init()
    {
    	int countVertices = 200;
    	boolean positionVertices = false;
    	boolean addEdges = true;
        // create a JGraphT graph
        ListenableGraph<String, DefaultEdge> g =
            new ListenableDirectedMultigraph<String, DefaultEdge>(
                DefaultEdge.class);

        for ( int i = 0; i < countVertices; i++ ) {
        	g.addVertex( "v" + i );
        }
        System.out.println( "vertices added to jgrapht done " );
        
        if ( addEdges ) {
            System.out.println( "adding edges" );

        	for ( String v1 : g.vertexSet()) {
        		for ( String v2 : g.vertexSet()) {
        			if ( v1 != v2 )
        				g.addEdge( v1, v2 );
        		}
        	}
            System.out.println( "adding edges done" );
        }
        
        // create a visualization using JGraph, via an adapter
        System.out.println( "loading jgraphModelAdapter ..." );
        jgAdapter = new JGraphModelAdapter<String, DefaultEdge>(g);
        System.out.println( "jgraphModelAdapter loaded" );
        
        System.out.println( "constructing jgraph ..." );
        JGraph jgraph = new JGraph(jgAdapter);
        System.out.println( "jgraph constructed" );

        
        adjustDisplaySettings(jgraph);
        getContentPane().add(jgraph);
        resize(DEFAULT_SIZE);

        if ( positionVertices ) {
        	System.out.println( "position vertices ... " );

        	for ( int i = 0; i < countVertices; i++ ) {
        		positionVertexAt( "v" + i, 50, 10 + i*40);
        	}
        	System.out.println( "... done" );
        }

        // that's all there is to it!...
    }

    private void adjustDisplaySettings(JGraph jg)
    {
        jg.setPreferredSize(DEFAULT_SIZE);

        Color c = DEFAULT_BG_COLOR;
        String colorStr = null;

        try {
            colorStr = getParameter("bgcolor");
        } catch (Exception e) {
        }

        if (colorStr != null) {
            c = Color.decode(colorStr);
        }

        jg.setBackground(c);
    }

    @SuppressWarnings("unchecked") // FIXME hb 28-nov-05: See FIXME below
    private void positionVertexAt(Object vertex, int x, int y)
    {
        DefaultGraphCell cell = jgAdapter.getVertexCell(vertex);
        AttributeMap attr = cell.getAttributes();
        Rectangle2D bounds = GraphConstants.getBounds(attr);

        Rectangle2D newBounds =
            new Rectangle2D.Double(
                x,
                y,
                bounds.getWidth(),
                bounds.getHeight());

        GraphConstants.setBounds(attr, newBounds);

        // TODO: Clean up generics once JGraph goes generic
        AttributeMap cellAttr = new AttributeMap();
        cellAttr.put(cell, attr);
        jgAdapter.edit(cellAttr, null, null, null);
    }

    //~ Inner Classes ---------------------------------------------------------

    /**
     * a listenable directed multigraph that allows loops and parallel edges.
     */
    private static class ListenableDirectedMultigraph<V, E>
        extends DefaultListenableGraph<V, E>
        implements DirectedGraph<V, E>
    {
        private static final long serialVersionUID = 1L;

        ListenableDirectedMultigraph(Class<E> edgeClass)
        {
            super(new DirectedMultigraph<V, E>(edgeClass));
        }
    }
}