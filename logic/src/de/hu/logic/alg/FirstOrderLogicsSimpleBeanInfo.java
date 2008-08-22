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

import de.hu.gralog.graph.GraphTypeInfo;
import de.hu.gralog.graph.GraphWithEditableElements;
import de.hu.gralog.graph.UndirectedGraph;
import de.hu.gralog.graph.alg.ChooseGraphPropertyDescriptor;
import de.hu.logic.graph.TransitionSystemTypeInfo;

public class FirstOrderLogicsSimpleBeanInfo extends SimpleBeanInfo {

	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( FirstOrderLogicsSimple.class );
	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[2];
	
	static {
		BEAN_DESCRIPTOR.setDisplayName( "Evaluate a First-Order, Least Fixed-Point or Inflationary Fixed-Point Logic Formula on a Transitionsystem" );
		BEAN_DESCRIPTOR.setShortDescription( 
				"<html>" +
				"This algorithm evaluates <b>formula</b> on <b>transitionSystem</b>. <br> " +
				"<br>" +
				"<b>Formular</b> is a Mue-Calculus-Formula with the following syntax: <br> " +
				"<ul> " +
				"<li><b>\\top</b> and <b>\\bot</b> stand for the formulas which are always true or always false.</li> " +
				"<li>Every proposition which is also contained in <b>transitionSystem</b> (case-sensitive).</li> " +
				"<li><b>\\and</b> and <b>\\or</b> stand for the logical and and or.</li> " +
				"<li><b>&lt &gt</b> and <b>[]</b> stand for the diamond and box operators.</li> " +
				"</ul>" +
				"All propositions contained in <b>formula</b> but not contained in <b>transitionSystem</b> are supposed to be empty. " +
				"All maximal subformulas with <b>\\and</b> or <b>\\or</b> need enclosing parenthesises. " +
				"So the formula <b>p \\and q \\and \r \\or q</b> " +
				"needs at least the following parenthesises (( p \\and q \\and r ) \\or q)." +
				"</html>" 
				);
		
		try {
			PROPERTY_DESCRIPTORS[0] = new ChooseGraphPropertyDescriptor( "structure", FirstOrderLogicsSimple.class, new FOGraphTypeInfo() );
			PROPERTY_DESCRIPTORS[0].setShortDescription( 
					"<html> " +
					"The structure on which <b>formula</b> is evaluated. " +
					"</html>"
					);
			PROPERTY_DESCRIPTORS[1] = new PropertyDescriptor( "formula", FirstOrderLogicsSimple.class );
			PROPERTY_DESCRIPTORS[1].setShortDescription( 
					"<html> " +
					"The Mu-Calculus formula to evaluate on <b>transitionSystem</b>. " +
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
			if ( graph instanceof UndirectedGraph ) {
				// graph.createVertex() instanceof ...
				return true;
			}
				
			return false;
		}
		
		
		
	}
	
}
