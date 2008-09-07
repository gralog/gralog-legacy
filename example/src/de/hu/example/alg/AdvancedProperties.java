package de.hu.example.alg;

import org.jgrapht.graph.ListenableDirectedGraph;

import de.hu.example.alg.AdvancedPropertiesBeanInfo.LabeledSimpleDirectedGraphTypeInfoFilter;
import de.hu.gralog.algorithm.Algorithm;
import de.hu.gralog.algorithm.InvalidPropertyValuesException;
import de.hu.gralog.algorithm.result.AlgorithmResult;
import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.GralogGraphSupport;
import de.hu.gralog.graph.GralogGraphTypeInfo;
import de.hu.gralog.graph.types.elements.LabeledGraphVertex;

/**
 * This algorithm shows you how to use most of the 
 * predefined PropertyDescriptors given in 
 * {@link de.hu.gralog.beans.propertyDescriptor} to
 * define more eleborated properties for
 * your algorithms, like properties that have
 * graphs, elements of graphs, graphTypes or 
 * enumerations as values.
 * 
 * @author Sebastian
 *
 */
public class AdvancedProperties<Vgraph extends LabeledGraphVertex, 
								Egraph, 
								GBgraph, 
								Ggraph extends ListenableDirectedGraph<Vgraph,Egraph>,
								Vtype extends LabeledGraphVertex,
								Etype,
								GBtype,
								Gtype extends ListenableDirectedGraph<Vtype, Etype>> implements Algorithm {

	/**
	 * Our first property shows how to use {@link de.hu.gralog.beans.propertydescriptor.ChooseGraphPropertyDescriptor}
	 * to let the user choose a GralogGraph that has 
	 * {@link de.hu.gralog.graph.types.elements.LabeledGraphVertex} as vertices
	 * and has a {@link org.jgrapht.graph.SimpleDirectedGraph} as its JGraphT-Graph.
	 * 
	 * <h2>Advanced ( in most cases you do not need to know about this )</h2>
	 * 
	 * Unfurtunaletly the second condition, i.e. that its underlying
	 * JGraphT-Graph is of type {@link org.jgrapht.graph.SimpleDirectedGraph} can not
	 * be explicitly expressed via the type-definition of our property.
	 * This is due to the fact that the JGraphT-Graph underlying a 
	 * {@link de.hu.gralog.graph.GralogGraphSupport} has to be a 
	 * {@link org.jgrapht.graph.ListenableGraph} and until now
	 * JGraphT only differentiates between {@link org.jgrapht.graph.ListenableDirectedGraph}
	 * and {@link org.jgrapht.graph.ListenableUndirectedGraph} - but
	 * there is no such thing as a ListenableSimpleDirectedGraph - so
	 * this condition has to be enforced by our GraphTypeInfoFilter 
	 * ( that is defined in the BeanInfo-class of this algrithm
	 * {@link LabeledSimpleDirectedGraphTypeInfoFilter} ), that 
	 * we use to define our 
	 * {@link de.hu.gralog.beans.propertydescriptor.ChooseGraphPropertyDescriptor}
	 * for this property.
	 * 
	 */
	private GralogGraphSupport<Vgraph, Egraph, GBgraph, Ggraph> graph;
	
	/**
	 * Our second property shows how to use 
	 * {@link de.hu.gralog.beans.propertydescriptor.ChooseDependableGraphElementPropertyDescriptor} 
	 * to let the user choose a vertex from the GralogGraph assigned to our
	 * first property <b>graph</b>. 
	 * 
	 */
	private Vgraph vertexOfGraph;
	
	/**
	 * Our third property shows how to use 
	 * {@link de.hu.gralog.beans.propertydescriptor.ChooseGraphTypeInfoPropertyDescriptor}
	 * to let the user choose a {@link de.hu.gralog.graph.GralogGraphTypeInfo}
	 * that has {@link de.hu.gralog.graph.types.elements.LabeledGraphVertex} as vertices
	 * and a {@link org.jgrapht.graph.DirectedGraph} as JGraphTGraph.
	 * 
	 */
	private GralogGraphTypeInfo<Vtype, Etype, GBtype, Gtype> graphType;
	
	/**
	 * Our last property shows how to use
	 * {@link de.hu.gralog.beans.propertydescriptor.EnumPropertyDescriptor}
	 * to let the user choose a among the values of an <b>enum</b>, that
	 * is defined below. 
	 * 
	 */
	private WeekDay weekDay;
	
	public enum WeekDay {
		MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
	}

	/**
	 * The getters- and setters for the above defined properties.
	 * 
	 */
	
	public GralogGraphSupport<Vgraph, Egraph, GBgraph, Ggraph> getGraph() {
		return graph;
	}

	public void setGraph(GralogGraphSupport<Vgraph, Egraph, GBgraph, Ggraph> graph) {
		this.graph = graph;
	}

	public GralogGraphTypeInfo<Vtype, Etype, GBtype, Gtype> getGraphType() {
		return graphType;
	}

	public void setGraphType(
			GralogGraphTypeInfo<Vtype, Etype, GBtype, Gtype> graphType) {
		this.graphType = graphType;
	}

	public Vgraph getVertexOfGraph() {
		return vertexOfGraph;
	}

	public void setVertexOfGraph(Vgraph vertexOfGraph) {
		this.vertexOfGraph = vertexOfGraph;
	}

	public WeekDay getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(WeekDay weekDay) {
		this.weekDay = weekDay;
	}

	/**
	 * The execute-function that does nothing but to enforce some constrains on
	 * the above properties and returns null. 
	 * 
	 */
	public AlgorithmResult execute() throws InvalidPropertyValuesException,
			UserException {
		InvalidPropertyValuesException e = new InvalidPropertyValuesException();
		if ( getGraph() == null )
			e.addPropertyError( "graph", InvalidPropertyValuesException.PROPERTY_REQUIRED );
		if ( getGraphType() == null )
			e.addPropertyError( "graphType", InvalidPropertyValuesException.PROPERTY_REQUIRED );
		if ( getVertexOfGraph() == null )
			e.addPropertyError( "vertexOfGraph", InvalidPropertyValuesException.PROPERTY_REQUIRED );
		if ( getWeekDay() != WeekDay.SATURDAY && getWeekDay() != WeekDay.SUNDAY )
			e.addPropertyError( "weekDay", "Please choose a day on the weekend!" );
		if ( e.hasErrors() )
			throw e;
		
		return null;
	}

}
