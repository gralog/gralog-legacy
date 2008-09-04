/*
 * Created on 2006 by Sebastian Ordyniak
 *
 * Copyright 2006 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of Gralog.
 *
 * Gralog is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * Gralog is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Gralog; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */

package de.hu.gralog.algorithm;

import java.io.Serializable;

import de.hu.gralog.algorithm.result.AlgorithmResult;
import de.hu.gralog.app.UserException;

/**
 * This interface has to be implement by all GraLog-Algorithms. An algorithm in
 * GraLog is basically a JavaBean with an execute-function defined by this
 * interface. Please see {@link de.hu.gralog.beans} for a generak introduction
 * to JavaBeans and further references.
 * 
 * <h1>Plugin-Developers</h1>
 * 
 * Lets suppose you want to implement an algorithm which takes two parameters:
 * <ul>
 * <li> a parameter <b>counter</b> which should be a positive integer </li>
 * <li> a required parameter <b>graph</b> whose underlying JGraphT-Graph should
 * be a {@link org.jgrapht.graph.SimpleDirectedGraph} whose vertices extend
 * {@link de.hu.gralog.graph.types.elements.LabeledGraphVertex} and whose edges
 * extend {@link org.jgrapht.graph.DefaultEdge}. </li>
 * </ul>
 * <p>
 * The first thing you have to do is to implement your Algorithm-Class which
 * should look something like this:
 * <p>
 * 
 * <pre>
 * public class YourAlgorithm&lt;V extends LabeledGraphVertex, E extends DefaultEdge, G extends DirectedGraph&lt;V, E&gt;&gt;
 * 		implements Algorithm {
 * 
 * 	private int counter;
 * 
 * 	private GralogGraphSupport&lt;V, E, ?, G&gt; graph;
 * 
 * 	public void setCounter(int counter) {
 * 		this.counter = counter;
 * 	}
 * 
 * 	public int getCounter() {
 * 		return counter;
 * 	}
 * 
 * 	public void setGraph(GralogGraphSupport&lt;V, E, ?, G&gt; graph) {
 * 		this.graph = graph;
 * 	}
 * 
 * 	public GralogGraphSupport&lt;V, E, ?, G&gt; getGraph() {
 * 		return graph;
 * 	}
 * 
 * 	public AlgorithmResult execute() throws InvalidPropertyValuesException,
 * 			UserException {
 * 		InvalidPropertyValuesException e = new InvalidPropertyValuesException();
 * 
 * 		if (getGraph() == null)
 * 			e.addPropertyError(&quot;graph&quot;,
 * 					InvalidPropertyValuesException.PROPERTY_REQUIRED);
 * 		if (getCounter() &lt;= 0)
 * 			e.addPropertyError(&quot;counter&quot;,
 * 					InvalidPropertyValuesException.GREATER_ZERO);
 * 		if (e.hasErrors())
 * 			throw e;
 * 
 * 		// do something and build result
 * 		return result;
 * 	}
 * }
 * </pre>
 * 
 * <p>
 * As you can see each parameter is reflected as a member variable of
 * YourAlgorithm. Furthermore there are two functions for each parameter which
 * are called getter and setter ( the naming convention for these functions
 * comes from the JavaBeans-Specification ). Since there is a getter and a
 * setter for each parameter GraLog knows that YourAlgorithm takes two
 * parameters: counter and graph. Before the user executes YourAlgorithm GraLog
 * allows the user to edit these parameters, so when execute is called you can
 * suppose that all parameters are set according to the users opinion.
 * <p>
 * As we have said there are restrictions on the parameters. So the first thing
 * you should do is to check for these restrictions in your execute-method. In
 * this case it is checked whether graph is not equal to null and counter is
 * creater than zero and if not an InvalidPropertyValuesException is thrown
 * which gives the user the possibility to alter his input.
 * <p>
 * The next step is to define a BeanInfo for YourAlgorithm. The easiest way to
 * do this is to define a class YourAlgorithmBeanInfo - the naming convention
 * again comes from JavaBean - in the same package as YourAlgorithm, which
 * should look like this:
 * <p>
 * 
 * <pre>
 *     	public class YourAlgorithmBeanInfo extends SimpleBeanInfo {
 *     
 *     		private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( YourAlgorithm.class );
 *     		private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[2];
 *     
 *    		static {
 *    			BEAN_DESCRIPTOR.setDisplayName( &quot;A name for YourAlgorithm&quot; );
 *    			BEAN_DESCRIPTOR.setShortDescription( 
 *    					&quot;A description for your algorithm in html.&quot; );
 *    
 *     			try {
 *     				PROPERTY_DESCRIPTORS[0] = new PropertyDescriptor( &quot;counter&quot;, YourAlgorithm.class );
 *     				PROPERTY_DESCRIPTORS[0].setShortDescription( &quot;A description for the property counter.&quot; );
 *     				PROPERTY_DESCRIPTORS[1] = new ChooseGraphPropertyDescriptor( &quot;graph&quot;, YourAlgorithm.class, new LabeledSimpleDirectedGraphTypeInfo() );
 *    	 			PROPERTY_DESCRIPTORS[1].setShortDescription( &quot;A description for the property graph.&quot; );
 *    			} catch( IntrospectionException e ) {
 *    				e.printStackTrace();
 *    			}
 *    
 *    			public BeanDescriptor getBeanDescriptor() {
 *    				return BEAN_DESCRIPTOR;
 *    			}
 *    
 *    			public PropertyDescriptor[] getPropertyDescriptors() {
 *    				return PROPERTY_DESCRIPTORS;
 *    			}
 *    	}
 * </pre>
 * 
 * <p>
 * YourAlgorithmBeanInfo basically defines a so called
 * {@link java.beans.BeanDescriptor} and an array of
 * {@link java.beans.PropertyDescriptor} one for each parameter. The
 * BeanDescriptor describes properties of the algorithm such as a name and a
 * description in html. Each PropertyDescriptor describes a property of your
 * Algorithm. This algorithm uses two PropertyDescriptors - a more detailed
 * introduction to propertydescriptors and the avaible types of
 * propertydescriptors can be found in
 * {@link de.hu.gralog.beans.propertydescriptor}. It follows a short
 * introduction to the descriptors used in our example:
 * 
 * <p>
 * <table>
 * <tr>
 * <td>Class</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>{@link java.beans.PropertyDescriptor}</td>
 * <td> This PropertyDescriptors should be used for all basic types like
 * boolean, int, double, String .. . It offers a standard editor for these
 * types. </td>
 * </tr>
 * <tr>
 * <td>{@link de.hu.gralog.beans.propertydescriptor.ChooseGraphPropertyDescriptor}</td>
 * <td> This PropertyDescriptor should be used whenever you want the user to
 * choose one of the currently opened graphs in GraLog. It allows to filter
 * those which are of a certain type - in the example of the type
 * LabeledSimpleDirectedGraph. </td>
 * </tr>
 * </table>
 * <p>
 * The last thing you have to do in your algorithm ( after making your
 * calculations ) is to return an
 * {@link de.hu.gralog.algorithm.result.AlgorithmResult}. Please refer to the
 * documentation of {@link de.hu.gralog.algorithm.result.AlgorithmResult} how to
 * do this.
 * 
 * @author Sebastian Ordyniak
 * 
 */
public interface Algorithm extends Serializable {
	/**
	 * This function is called when all parameters have been set by the user. It
	 * returns an {@link de.hu.gralog.algorithm.result.AlgorithmResult} which
	 * describes the output of your algorithm.
	 * 
	 * @return it is possible to return null, which tells Gralog not to display
	 *         anything. This can be useful when your Algorithm only wants to
	 *         manipulate a Gralog-Graph but does not need to show a result.
	 * 
	 * @throws InvalidPropertyValuesException
	 *             should be thrown if not all parameters where set correctly
	 * @throws UserException
	 *             should be thrown if an error occurs during your calculations
	 *             and you know what went wrong
	 */
	AlgorithmResult execute() throws InvalidPropertyValuesException,
			UserException;
}
