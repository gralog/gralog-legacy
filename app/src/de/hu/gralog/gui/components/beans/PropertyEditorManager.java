package de.hu.gralog.gui.components.beans;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;

import de.hu.gralog.app.UserException;
import de.hu.gralog.beans.propertydescriptor.ChooseGraphElementPropertyDescriptor;
import de.hu.gralog.beans.propertydescriptor.ChooseGraphPropertyDescriptor;
import de.hu.gralog.beans.propertydescriptor.ChooseGraphTypeInfoPropertyDescriptor;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.gui.components.beans.propertyeditors.ChooseGraphPropertyEditor;
import de.hu.gralog.gui.components.beans.propertyeditors.ChooseGraphTypeInfoPropertyEditor;

public class PropertyEditorManager {

	public static final PropertyEditor getPropertyEditor( PropertyDescriptor propertyDescriptor, Object bean ) {
		if ( propertyDescriptor == null )
			return null;
		if ( propertyDescriptor instanceof ChooseGraphPropertyDescriptor )
			return new ChooseGraphPropertyEditor( ((ChooseGraphPropertyDescriptor)propertyDescriptor).getGraphType() );
		if ( propertyDescriptor instanceof ChooseGraphElementPropertyDescriptor )
			return propertyDescriptor.createPropertyEditor( MainPad.getInstance().getDesktop().getCurrentGraph().getGraphT() );
		if ( propertyDescriptor instanceof ChooseGraphTypeInfoPropertyDescriptor )
			return new ChooseGraphTypeInfoPropertyEditor( ((ChooseGraphTypeInfoPropertyDescriptor)propertyDescriptor).getGraphTypeInfoFilter() );

		PropertyEditor propertyEditor = propertyDescriptor.createPropertyEditor( bean );
		if ( propertyEditor != null )
			return propertyEditor;
		
		if ( propertyDescriptor.getPropertyEditorClass() != null ) {
			try {
				propertyEditor = (PropertyEditor) propertyDescriptor.getPropertyEditorClass().newInstance();
			} catch (InstantiationException e) {
				MainPad.getInstance().handleUserException( new UserException( "unable to instanciate PropertyEditor for property: " + propertyDescriptor.getName() ));
			} catch (IllegalAccessException e) {
				MainPad.getInstance().handleUserException( new UserException( "unable to instanciate PropertyEditor for property: " + propertyDescriptor.getName() ));
			}
		}
		return propertyEditor;
	}

}
