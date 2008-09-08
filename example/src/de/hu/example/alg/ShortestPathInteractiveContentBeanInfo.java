package de.hu.example.alg;

import java.beans.BeanDescriptor;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.SimpleBeanInfo;
import java.beans.Statement;

import de.hu.example.alg.ShortestPathInteractiveContent.ContentCustomizer;
import de.hu.gralog.app.UserException;

/**
 * This is the <b>BeanInfo</b>-class for {@link ShortestPathInteractiveContent}.  
 * 
 * Its only propose is to assign the Customizer 
 * {@link ShortestPathInteractiveContent.ContentCustomizer} to
 * {@link ShortestPathInteractiveContent}.
 * 
 * @author Sebastian
 *
 */
public class ShortestPathInteractiveContentBeanInfo extends SimpleBeanInfo {

	/**
	 * The {@link ContentCustomizer} is passed as an argument to
	 * the {@link BeanDescriptor}-class.
	 * 
	 */
	private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor( ShortestPathInteractiveContent.class, ContentCustomizer.class );
	
	@Override
	public BeanDescriptor getBeanDescriptor() {
		return BEAN_DESCRIPTOR;
	}

}
