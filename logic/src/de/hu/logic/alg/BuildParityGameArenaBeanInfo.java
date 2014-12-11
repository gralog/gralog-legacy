package de.hu.logic.alg;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import de.hu.gralog.beans.propertydescriptor.ChooseStructurePropertyDescriptor;
import de.hu.logic.structure.TransitionSystemTypeInfo;
import de.hu.logic.alg.BuildParityGameArena;

public class BuildParityGameArenaBeanInfo extends SimpleBeanInfo {

	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( BuildParityGameArena.class );
	private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[2];
	
	static {
		BEAN_DESCRIPTOR.setDisplayName( "Build a ParityGame-Arena from a Mu-Calculus-Formula on an Transitionsystem" );
		BEAN_DESCRIPTOR.setShortDescription( 
				"<html>" +
				"This algorithm builds a Parity-Game-Arena from a given " +
				"modal Mu-Calculus-Formula and a transitionsystem " +
				"<br>" +
				"<br> " +
				"<b>Formula</b> has to be a formula of the modal Mu-Calculus, " +
				"with the following syntax: <br> " +
				"<ul> " +
				
				"<li><b>\\top</b> respectively <b>\\bot</b> represent the formulaes, " +
				"that are always <i>true</i> respectively <i>false</i>.</li> " +
				
				"<li><b>P</b>, where <b>P</b> is the name of a proposition " +
				"that is contained in <i>transitionSystem</i> (case-sensitive).</li> " +
				
				"<li><b>\\and</b> respectively <b>\\or</b> represent the logical <i>and</i> " +
				"respectively <i>or</i>.</li> " +
				
				"<li><b>&lt;&gt;</b> respectively <b>[]</b> represent " +
				"the <i>diamond</i>- respectively <i>box</i>-operator of the modal Mu-Calculus.</li> " +
				
				"<li><b>&lt;LABEL&gt;</b> respectively <b>[LABEL]</b> represent " +
				"the <i>diamond</i>- respectively <i>box</i>-operator, " +
				"with a specified transition-label " +
				"<i>LABEL</i>.</li> " +
				
				"<li><b>\\mu X.</b> respectively <b>\\nu X.</b> represent the least- " +
				"respectively greatest fixpoint-operator for <i>X</i></li> " +

				"</ul>" +
				
				"All propositions contained in <i>formula</i> but not contained in " +
				"<i>transitionSystem</i> are supposed to be empty. " +
				"<br> " +
				"All maximal sub-formulaes containing <i>\\and</i> or <i>\\or</i> need " +
				"enclosing parenthesises.<br> " +
				
				"Thus the formula: " +
				"<pre>p \\and q \\and r \\or q</pre> " +
				"needs at least the following parenthesises: " +
				"<pre>(( p \\and q \\and r ) \\or q)</pre>." +
				"</html>" 
				);
		
		try {
			PROPERTY_DESCRIPTORS[0] = new ChooseStructurePropertyDescriptor( "transitionSystem", BuildParityGameArena.class, new TransitionSystemTypeInfo() );
			PROPERTY_DESCRIPTORS[0].setShortDescription( 
					"<html> " +
					"The <b>transitionsystem</b> for which the Parity-Game-Arena is build. " +
					"</html>"
					);
			PROPERTY_DESCRIPTORS[1] = new PropertyDescriptor( "formula", BuildParityGameArena.class );
			PROPERTY_DESCRIPTORS[1].setShortDescription( 
					"<html> " +
					"The modal Mu-Calculus <b>formula</b> for which the Parity-Game-Arena is build. " +
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
	
}
