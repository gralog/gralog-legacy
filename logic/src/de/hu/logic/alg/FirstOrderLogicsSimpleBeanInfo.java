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

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.UndirectedGraph;

import de.hu.gralog.beans.propertydescriptor.ChooseStructurePropertyDescriptor;
import de.hu.gralog.structure.StructureTypeInfo;
import de.hu.gralog.structure.StructureTypeInfoFilter;
import de.hu.logic.structure.TransitionSystem;



public class FirstOrderLogicsSimpleBeanInfo extends SimpleBeanInfo {

	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( FirstOrderLogicsSimple.class );
	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[2];
	
	static {
		BEAN_DESCRIPTOR.setDisplayName( "Evaluate a First-Order Logic Formula on a Structure (Graph, Transition System, etc.)" );
		BEAN_DESCRIPTOR.setShortDescription( 
				"<html>" +
				"This algorithm evaluates a First-Order-Formula on a structure. " +
				"<br> " +
				"<br> " +
				"<b>Formula</b> has to be a First-Order-Formula " +
				"with the following syntax: <br> " +

				"<ul> " +
				"<li><b>\\top</b> and <b>\\bot</b> " +
				"stand for the formulas which are always <i>true</i> or always <i>false</i>.</li> " +
				
				"<li>atomic formulas are either equalities, " +
				"written as \"<ident> = <ident>\", " +
				"where identifiers <ident> can contain letters and numbers, "+
				"or atomic formulas of the form \"R(x, y, z)\", " +
				"where R is a relation symbol and x, y, z ist a list of variables.<br/>"+
				"Admissible relation names depend on the structure. " +
				"For undirected or directed graphs the edge relation is \"E\". " +
				"For transition systems the "+
				"edge relation is \"E\" also but in addition the names " +
				"of all propositions can be used.</li> " +
				
				"<li><b>\\and</b> and <b>\\or</b> stand for the logical <i>and</i> and <i>or</i>.</li> " +
				"<li><b>\\exists</b> and <b>\\forall</b> " +
				"stand for the existential and universal quantifiers.</li> " +
				"</ul>" +
				
				"The precise grammar is:<br/>"+
				"formula ::= \\bot | \\top | &lt;ident&gt; = <ident> | <ident> '(' (<ident>,)*<ident> ')' | "+
				"'(' (<formula> \\and)* <formula> \\and <formula> ')' | "+
				"'(' (<formula> \\or)* <formula> \\or <formula> ')' | "+
				"\\neg <formula> | "+
				"\\exists <ident> <formula> | " +
				"\\forall <ident> <formula>" +
				"</html>" 
				);
		
		try {
			PROPERTY_DESCRIPTORS[0] = new ChooseStructurePropertyDescriptor( "structure", FirstOrderLogicsSimple.class, new FOGraphTypeInfo() );
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
	
	public static class FOGraphTypeInfo implements StructureTypeInfoFilter {

		public boolean filterTypeInfo( StructureTypeInfo typeInfo ) {
			Graph graph = typeInfo.getGraphFactory().createGraph();
			if ( graph instanceof UndirectedGraph || graph instanceof DirectedGraph || typeInfo.getStructureBeanFactory().createBean(  ) instanceof TransitionSystem) 
				return false;
			return true;
		}
	}
	
}
