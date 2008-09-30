package de.hu.example.structure;

import org.jgrapht.VertexFactory;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.graph.ListenableDirectedGraph;

import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;
import de.hu.gralog.structure.GraphFactory;
import de.hu.gralog.structure.StructureBeanFactory;
import de.hu.gralog.structure.StructureTypeInfo;
import de.hu.gralog.structure.types.elements.LabeledStructureEdge;

/**
 * This class defines our new GrALoG-Structure, i.e. <b>SimpleStructure</b>.
 * It extends {@link de.hu.gralog.structure.StructureTypeInfo} and
 * basically assembles all the information needed by GrALoG
 * to deal with this new Structure-Type. Please see the description
 * of {@link de.hu.gralog.structure.StructureTypeInfo} for further information.
 * 
 * This example uses:
 * 
 * <ul>
 * 		<li>A DirectedMultiGraph as an underlying JGraphT-Graph,
 * 		that means you can add multiply edges between the same vertices to it
 * 		</li>
 * 		<li>{@link SimpleStructureBean} as the Structure-Bean.</li>
 * 		<li>{@link SimpleVertex} as vertices</li>
 * 		<li>{@link LabeledStructureEdge} as edges</li>
 * 		<li>{@link SimpleVertexVertexRenderer} as a Renderer for the vertices</li>
 * </ul>
 * 
 * @author Sebastian
 *
 */
public class SimpleStructureTypeInfo extends StructureTypeInfo<SimpleVertex, LabeledStructureEdge, SimpleStructureBean, ListenableDirectedGraph<SimpleVertex,LabeledStructureEdge>> {

	public SimpleStructureTypeInfo() {
		super();
	}

	@Override
	public String getName() {
		return "SimpleStructure";
	}

	public String getDescription() {
		return
			"<html>" +
			"This structure shows you how to implement your own " +
			"<i>Structure-Bean</i> and elements in GrALoG." +
			"</html>";
	}
	
	@Override
	public GraphFactory<ListenableDirectedGraph<SimpleVertex, LabeledStructureEdge>> getGraphFactory() {
		return new GraphFactory<ListenableDirectedGraph<SimpleVertex,LabeledStructureEdge>>() {
			public ListenableDirectedGraph<SimpleVertex, LabeledStructureEdge> createGraph() {
				return new ListenableDirectedGraph<SimpleVertex, LabeledStructureEdge>( new DirectedMultigraph<SimpleVertex, LabeledStructureEdge>( LabeledStructureEdge.class ) );
			}
		};
	}

	@Override
	public StructureBeanFactory<SimpleStructureBean> getStructureBeanFactory() {
		return new StructureBeanFactory<SimpleStructureBean>() {
			public SimpleStructureBean createBean() {
				return new SimpleStructureBean();
			}
		};
	}

	@Override
	public VertexFactory<SimpleVertex> getVertexFactory() {
		return new VertexFactory<SimpleVertex>() {
			public SimpleVertex createVertex() {
				return new SimpleVertex();
			}
		};
	}

	@Override
	public DefaultVertexRenderer getVertexRenderer() {
		return new SimpleVertexVertexRenderer();
	}

	@Override
	public DefaultEdgeRenderer getEdgeRenderer() {
		return null;
	}

}
