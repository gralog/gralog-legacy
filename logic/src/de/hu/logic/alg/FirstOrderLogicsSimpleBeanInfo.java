/*
 * Created on 26 Oct 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.logic.alg;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import de.hu.gralog.graph.DirectedGraph;
import de.hu.gralog.graph.GraphTypeInfo;
import de.hu.gralog.graph.GraphWithEditableElements;
import de.hu.gralog.graph.UndirectedGraph;
import de.hu.gralog.graph.alg.ChooseGraphPropertyDescriptor;
import de.hu.logic.graph.TransitionSystem;

public class FirstOrderLogicsSimpleBeanInfo extends SimpleBeanInfo {

	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( FirstOrderLogicsSimple.class );
	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[2];
	
	static {
		BEAN_DESCRIPTOR.setDisplayName( "Evaluate a First-Order Logic Formula on a Structure (Graph, Transition System, etc.)" );
		BEAN_DESCRIPTOR.setShortDescription( 
				"<html>" +
				"This algorithm evaluates the given <i>formula</i> on the given <i>structure</i>. <br> " +
				"<br>" +
				"Formulas can be written as: <br> " +
				"<ul> " +
				"<li><b>\\top</b> and <b>\\bot</b> stand for the formulas which are always true or always false.</li> " +
				"<li>atomic formulas are either equality written as \"<ident> = <ident>\", where identifiers <ident> can contain letters and numbers, "+
				"or atomic formulas of the form \"R(x, y, z)\", where R is a relation symbol and x, y, z ist a list of variables.<br/>"+
				"Admissible relation names depend on the structure. For undirected or directed graphs the edge relation is \"E\". For transition systems the "+
				"edge relation is \"E\" also but in addition the names of all propositions can be used.</li> " +
				"<li><b>\\and</b> and <b>\\or</b> stand for the logical and and or.</li> " +
				"<li><b>\\exists</b> and <b>\\forall</b> stand for the existential and universal quantifiers.</li> " +
				"</ul>" +
				"The precise grammar is<br/>"+
				"formula ::= \\bot | \\top | &lt;ident&gt; = <ident> | <ident> '(' (<ident>,)*<ident> ')' | "+
				"'(' (<formula> \\and)* <formula> \\and <formula> ')' | "+
				"'(' (<formula> \\or)* <formula> \\or <formula> ')' | "+
				"\\neg <formula> | "+
				"\\exists <ident> <formula> | " +
				"\\forall <ident> <formula>" +
				"</html>" 
				);
		
		try {
			PROPERTY_DESCRIPTORS[0] = new ChooseGraphPropertyDescriptor( "structure", FirstOrderLogicsSimple.class, new FOGraphTypeInfo() );
			PROPERTY_DESCRIPTORS[0].setShortDescription( 
					"<html> " +
					"The structure on which the <i>formula</i> is evaluated. " +
					"</html>"
					);
			PROPERTY_DESCRIPTORS[1] = new PropertyDescriptor( "formula", FirstOrderLogicsSimple.class );
			PROPERTY_DESCRIPTORS[1].setShortDescription( 
					"<html> " +
					"The first-order formula to be evaluated on the <i>structure</i>. " +
					"</html>"
					);

		} catch (IntrospectionException e) {
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
	
	public static class FOGraphTypeInfo extends GraphTypeInfo {

		@Override
		public String getName() {
			return null;
		}

		@Override
		public GraphWithEditableElements newInstance() {
			
			return null;
		}

		@Override
		public boolean isInstance(GraphWithEditableElements graph) {
			if ( graph instanceof UndirectedGraph || graph instanceof DirectedGraph || graph instanceof TransitionSystem) 
			{
				// graph.createVertex() instanceof ...
				return true;
			}
				
			return false;
		}
		
		
		
	}
	
}
