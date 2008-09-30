package de.hu.example.alg;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Customizer;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jgraph.event.GraphSelectionEvent;
import org.jgrapht.Graph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.Subgraph;

import de.hu.gralog.algorithm.result.AlgorithmResultContent;
import de.hu.gralog.algorithm.result.AlgorithmResultInteractiveContent;
import de.hu.gralog.algorithm.result.DisplaySubgraph;
import de.hu.gralog.algorithm.result.DisplaySubgraphMode;
import de.hu.gralog.app.UserException;
import de.hu.gralog.structure.Structure;
import de.hu.gralog.structure.event.SelectionListener;

/**
 * This example shows you how to override
 * {@link AlgorithmResultInteractiveContent} in order
 * to allow user-interaction after the result is
 * displayed to the user. It also shows
 * you how to define your own {@link Customizer}
 * for a JavaBean {@link ContentCustomizer}.
 * Note that {@link ContentCustomizer} is defined
 * as a Customizer for this class 
 * ( see {@link ShortestPathInteractiveContentBeanInfo} on
 * how this is done ). The {@link ContentCustomizer}
 * listens to SelectionEvents fired by the
 * {@link de.hu.gralog.structure.support.StructureSelectionSupport}-object
 * of the {@link Structure}-instance and
 * tells {@link ShortestPathInteractiveContent} when
 * to fire ContentChangeEvents.
 * 
 * The example allows the user to select a start- and
 * an endVertex and displays the shortest path
 * between these vertices.  
 * 
 * @author Sebastian
 *
 */
public class ShortestPathInteractiveContent extends
		AlgorithmResultInteractiveContent {

	public static final String DM_SHORTEST_PATH = "Shortest Path";
	
	private Object startVertex;
	private Object endVertex;

	/**
	 * Always provide a default-constructor - at least
	 * if you do not define your own {@link java.beans.PersistenceDelegate} -
	 * since the DefaultPersistenceDelegate for this class uses it. 
	 *
	 */
	public ShortestPathInteractiveContent() {
		super();
	}
	
	public ShortestPathInteractiveContent(Structure structure) {
		super( structure );
	}

	/**
	 * Returns the vertices contained in the given list
	 * of edges.
	 * 
	 * @param graph a graph
	 * @param edges a list of edges of graph
	 * @return all vertices that are contained in edges
	 */
	private Set getVertices( Graph graph, List edges ) {
		Set vertices = new HashSet();
		for ( Object edge : edges ) {
			vertices.add( graph.getEdgeSource( edge ) );
			vertices.add( graph.getEdgeTarget( edge ) );
		}
		return vertices;
	}
	
	/**
	 * We have to override this method, since we change the DisplaySubgraphs of
	 * this content dynamically.
	 *  
	 */
	@Override
	protected Hashtable<String, DisplaySubgraph> getDisplaySubgraphs(Hashtable<String, DisplaySubgraphMode> modes, Structure structure ) throws UserException {
		Subgraph subgraph;
		if ( startVertex != null || endVertex != null ) {
			List edgeList = DijkstraShortestPath.findPathBetween( this.structure.getGraph(), startVertex, endVertex );
			subgraph = new Subgraph( this.structure.getGraph(), getVertices( this.structure.getGraph(), edgeList ), new HashSet( edgeList ) );
		} else 
			subgraph = new Subgraph( this.structure.getGraph(), new HashSet(), new HashSet() );
		Hashtable<String, DisplaySubgraph> displaySubgraphs = new Hashtable<String, DisplaySubgraph>();
		displaySubgraphs.put( DM_SHORTEST_PATH, new DisplaySubgraph( modes.get( DM_SHORTEST_PATH ), subgraph ) );
		return displaySubgraphs;
	}

	/**
	 * Called by the customizer, after the user changed his selection.
	 * This method calls {@link AlgorithmResultInteractiveContent#fireContentChanged()}
	 * to inform GrALoG, that the Content of this {@link AlgorithmResultContent}
	 * has changed. This causes GrALoG to redisplay the content. 
	 * 
	 * @param start
	 * @param end
	 */
	void setStartAndEndVertex( Object start, Object end ) {
		if ( start != startVertex || end != endVertex ) {
			startVertex = start;
			endVertex = end;
			try {
				fireContentChanged();
			} catch( UserException e ) {
				
			}
		}
	}
	
	/**
	 * This is the {@link Customizer} defined for this class. The
	 * connection between {@link ShortestPathInteractiveContent} and
	 * the Customizer is done in {@link ShortestPathInteractiveContentBeanInfo}. 
	 * 
	 * As you can see the Customizer is defined as a {@link javax.swing.JPanel}.
	 * Gralog displays the customizer as part of the {@link de.hu.gralog.algorithm.result.AlgorithmResult}.
	 *  
	 * @author Sebastian
	 *
	 */
	public static class ContentCustomizer extends JPanel implements Customizer, SelectionListener {
		private JLabel label = new JLabel( "Select a startVertex!" );
		private JButton button = new JButton( "Reset" );
		
		private ShortestPathInteractiveContent content;
		
		private Object startVertex;
		private Object endVertex;
		
		public ContentCustomizer() {
			super( new BorderLayout() );
			
			JPanel innerPanel = new JPanel( new BorderLayout() );
			innerPanel.add( label, BorderLayout.CENTER );
			button.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					resetSelection();
				}
			});
			button.setToolTipText( "Reset the current-selection and start again." );
			innerPanel.add( button, BorderLayout.EAST );
			
			add( innerPanel, BorderLayout.CENTER );
		}

		/**
		 * This method implements {@link Customizer#setObject(java.lang.Object)}. It
		 * is used by GrALoG to assign the bean, i.e. the instance of
		 * {@link ShortestPathInteractiveContent}, to this Customizer.
		 * 
		 * Note that we use the content to add a {@link SelectionListener}
		 * to the <b>structure</b>-instance.
		 *   
		 */
		public void setObject(Object bean) {
			content = (ShortestPathInteractiveContent)bean;
			content.structure.getStructureSelectionSupport().addSelectionListener( this );
			update();
		}
		
		public void update() {
			if ( startVertex == null ) {
				label.setText( "Select a startVertex!" );
				button.setEnabled( false );
				content.setStartAndEndVertex( startVertex, endVertex );
			}
			else {
				if ( endVertex == null )
					label.setText( "From: " + startVertex + ", Select an endVertex!" );
				else {
					content.setStartAndEndVertex( startVertex, endVertex );
					label.setText( "Press Reset to start again!" );
				}
				button.setEnabled( true );

			}
		}
		
		public void resetSelection() {
			startVertex = null;
			endVertex = null;
			update();
		}

		/**
		 * This method implements {@link SelectionListener#valueChanged(java.util.Set, java.util.Set, org.jgraph.event.GraphSelectionEvent)} and
		 * is called by {@link Structure} when the selection has changed.
		 *  
		 * 
		 * @param selectedVertices
		 * @param selectedEdges
		 * @param event
		 */
		public void valueChanged(Set selectedVertices, Set selectedEdges, GraphSelectionEvent event) {
			if ( selectedVertices == null || selectedVertices.size() == 0 )
				return;
			if ( endVertex != null )
				return;
			if ( selectedVertices.size() == 1 ) {
				if ( startVertex == null )
					startVertex = selectedVertices.iterator().next();
				else
					endVertex = selectedVertices.iterator().next();
				update();
			}
		}
		
		
	}
}
