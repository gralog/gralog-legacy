/*
 * Created on 26 Oct 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.gralog.plugin.generator.alg;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import de.hu.gralog.beans.propertydescriptor.ChooseGraphTypeInfoPropertyDescriptor;
import de.hu.gralog.beans.propertydescriptor.EnumPropertyDescriptor;
import de.hu.gralog.plugin.generator.alg.Generator.GraphGeneratorTypes;
import de.hu.gralog.plugin.generator.alg.Generator.LabeledGraphVertexGraphTypeInfoFilter;

public class GeneratorBeanInfo extends SimpleBeanInfo {

	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( Generator.class );
	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[5];
	
	static {
		BEAN_DESCRIPTOR.setDisplayName( "JGraphTGenerator" );
		BEAN_DESCRIPTOR.setShortDescription( 
				"<html>" +
				"This algorithm generates a graph of type <b>graphType</b> to " +
				"with the via <b>generatorType</b> specified Generator. " +
				"Every generated vertex gets a label that consists of <b>vertexLabelPrefix</b> followed " +
				"by a number from 1 to <b>numOfVertices</b>.<br><br> " +
				"<b>generatorType</b> is one of the following ( see <a href=\"http://www.jgrapht.org/javadoc/org/jgrapht/generate/package-summary.html\">JGraphT-Generators</a>):" +
				"<ul>" +
				"  <li><b>EMPTY</b> uses <a href=\"http://www.jgrapht.org/javadoc/org/jgrapht/generate/EmptyGraphGenerator.html\">EmptyGraphGenerator</a> " +
				"      to generate an empty graph on <b>numOfVertices</b> vertices.</li>" +
				"  <li><b>LINEAR</b> uses <a href=\"http://www.jgrapht.org/javadoc/org/jgrapht/generate/LinearGraphGenerator.html\">LinearGraphGenerator</a> " +
				"      to generate a path on <b>numOfVertices</b> vertices.</li>" +
				"  <li><b>RANDOM</b> uses <a href=\"http://www.jgrapht.org/javadoc/org/jgrapht/generate/RandomGraphGenerator.html\">RandomGraphGenerator</a> " +
				"      to generate a random graph with <b>numOfVertices</b> vertices and <b>numOfEdges</b> edges.</li>" +
				"  <li><b>RING</b> uses <a href=\"http://www.jgrapht.org/javadoc/org/jgrapht/generate/RingGraphGenerator.html\">RingGraphGenerator</a> " +
				"      to generate a circle with <b>numOfVertices</b> vertices.</li>" +
				"  <li><b>WHEEL</b> uses <a href=\"http://www.jgrapht.org/javadoc/org/jgrapht/generate/WheelGraphGenerator.html\">WheelGraphGenerator</a> " +
				"      to generate a wheel with <b>numOfVertices</b> vertices.</li>" +
				"</ul>" +
				"</html>" 
				);
		
		try {
			PROPERTY_DESCRIPTORS[0] = new ChooseGraphTypeInfoPropertyDescriptor( "graphType", Generator.class, new LabeledGraphVertexGraphTypeInfoFilter() );
			PROPERTY_DESCRIPTORS[0].setShortDescription( "<html>" +
					"The type of the graph that should be generated." +
					"</html>" );

			PROPERTY_DESCRIPTORS[1] = new EnumPropertyDescriptor( "generatorType", Generator.class, GraphGeneratorTypes.values() );
			PROPERTY_DESCRIPTORS[1].setShortDescription( "<html>" +
					"The type of generator used to generate the graph. See the algorithmdescription for details." +
					"</html>"
					);

			PROPERTY_DESCRIPTORS[2] = new PropertyDescriptor( "numOfVertices", Generator.class );
			PROPERTY_DESCRIPTORS[2].setShortDescription( "<html>" +
					"The number of vertices in the generated graph." +
					"</html>"
					);
			
			PROPERTY_DESCRIPTORS[3] = new PropertyDescriptor( "numOfEdges", Generator.class );
			PROPERTY_DESCRIPTORS[3].setShortDescription( "<html>" +
					"The number of edges in the generated graph. This property is only used " +
					"by the <b>RANDOM</b> generator. See algorithmdescription for details." +
					"</html>"
					);
			PROPERTY_DESCRIPTORS[4] = new PropertyDescriptor( "vertexLabelPrefix", Generator.class );
			PROPERTY_DESCRIPTORS[4].setShortDescription( "<html>" +
					"A prefix for all vertexlabels of the generated graph." +
					"</html>"
					);
			
		} catch( IntrospectionException e ) {
			e.printStackTrace();
		}
	}

	@Override
	public BeanDescriptor getBeanDescriptor() {
		return BEAN_DESCRIPTOR;
	}

	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
		return PROPERTY_DESCRIPTORS;
	}
	
}
