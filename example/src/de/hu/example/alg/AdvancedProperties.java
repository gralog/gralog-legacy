package de.hu.example.alg;

import org.jgrapht.graph.ListenableDirectedGraph;

import de.hu.gralog.algorithm.Algorithm;
import de.hu.gralog.algorithm.InvalidPropertyValuesException;
import de.hu.gralog.algorithm.result.AlgorithmResult;
import de.hu.gralog.app.UserException;
import de.hu.gralog.structure.Structure;
import de.hu.gralog.structure.StructureTypeInfo;
import de.hu.gralog.structure.types.elements.LabeledStructureVertex;

/**
 * This algorithm shows you how to use most of the 
 * predefined PropertyDescriptors given in 
 * {@link de.hu.gralog.beans.propertyDescriptor} to
 * define more elaborated properties for
 * your algorithms, like properties that have
 * structures, elements of structures, structure-types or 
 * enumerations as values.
 * 
 * @author Sebastian
 *
 */
public class AdvancedProperties<Vgraph extends LabeledStructureVertex, 
								Egraph, 
								GBgraph, 
								Ggraph extends ListenableDirectedGraph<Vgraph,Egraph>,
								Vtype extends LabeledStructureVertex,
								Etype,
								GBtype,
								Gtype extends ListenableDirectedGraph<Vtype, Etype>> implements Algorithm {

	/**
	 * Our first property shows how to use {@link de.hu.gralog.beans.propertydescriptor.ChooseStructurePropertyDescriptor}
	 * to let the user choose a GrALoG-Structure that has 
	 * {@link de.hu.gralog.structure.types.elements.LabeledStructureVertex} as vertices
	 * and has a {@link org.jgrapht.graph.SimpleDirectedGraph} as it's 
	 * JGraphT-Graph.
	 * 
	 * <h2>Advanced ( in most cases you do not need to know about this )</h2>
	 * 
	 * Unfurtunaletly the second condition, i.e. that it's underlying
	 * JGraphT-Graph is of type {@link org.jgrapht.graph.SimpleDirectedGraph} can not
	 * be explicitly expressed via the type-definition of our property.
	 * This is due to the fact that the JGraphT-Graph underlying a 
	 * {@link de.hu.gralog.structure.Structure} has to be a 
	 * {@link org.jgrapht.graph.ListenableGraph} and until now
	 * JGraphT only distinguishes between {@link org.jgrapht.graph.ListenableDirectedGraph}
	 * and {@link org.jgrapht.graph.ListenableUndirectedGraph} - but
	 * there is no such thing as a ListenableSimpleDirectedGraph - so
	 * this condition has to be enforced by our StructureTypeInfoFilter 
	 * ( that is defined in the BeanInfo-class of this algrithm
	 * {@link de.hu.example.alg.AdvancedPropertiesBeanInfo.LabeledSimpleDirectedGraphStructureTypeInfoFilter} ), that 
	 * we use to define our 
	 * {@link de.hu.gralog.beans.propertydescriptor.ChooseStructurePropertyDescriptor}
	 * for this property.
	 * 
	 */
	private Structure<Vgraph, Egraph, GBgraph, Ggraph> structure;
	
	/**
	 * Our second property shows how to use 
	 * {@link de.hu.gralog.beans.propertydescriptor.ChooseDependableStructureElementPropertyDescriptor} 
	 * to let the user choose a vertex from the GrALoG-Structure assigned to our
	 * first property <b>structure</b>. 
	 * 
	 */
	private Vgraph vertexOfStructure;
	
	/**
	 * Our third property shows how to use 
	 * {@link de.hu.gralog.beans.propertydescriptor.ChooseStructureTypeInfoPropertyDescriptor}
	 * to let the user choose a {@link de.hu.gralog.structure.StructureTypeInfo}
	 * that has {@link de.hu.gralog.structure.types.elements.LabeledStructureVertex} as vertices
	 * and a {@link org.jgrapht.graph.DirectedGraph} as JGraphT-Graph.
	 * 
	 */
	private StructureTypeInfo<Vtype, Etype, GBtype, Gtype> structureType;
	
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
	
	public Structure<Vgraph, Egraph, GBgraph, Ggraph> getStructure() {
		return structure;
	}

	public void setStructure(Structure<Vgraph, Egraph, GBgraph, Ggraph> structure) {
		this.structure = structure;
	}

	public StructureTypeInfo<Vtype, Etype, GBtype, Gtype> getStructureType() {
		return structureType;
	}

	public void setStructureType(
			StructureTypeInfo<Vtype, Etype, GBtype, Gtype> structureType) {
		this.structureType = structureType;
	}

	public Vgraph getVertexOfStructure() {
		return vertexOfStructure;
	}

	public void setVertexOfStructure(Vgraph vertexOfStructure) {
		this.vertexOfStructure = vertexOfStructure;
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
		if ( getStructure() == null )
			e.addPropertyError( "structure", InvalidPropertyValuesException.PROPERTY_REQUIRED );
		if ( getStructureType() == null )
			e.addPropertyError( "structureType", InvalidPropertyValuesException.PROPERTY_REQUIRED );
		if ( getVertexOfStructure() == null )
			e.addPropertyError( "vertexOfStructure", InvalidPropertyValuesException.PROPERTY_REQUIRED );
		if ( getWeekDay() != WeekDay.SATURDAY && getWeekDay() != WeekDay.SUNDAY )
			e.addPropertyError( "weekDay", "Please choose a day on the weekend!" );
		if ( e.hasErrors() )
			throw e;
		
		return null;
	}

}
