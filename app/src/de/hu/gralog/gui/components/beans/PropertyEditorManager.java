package de.hu.gralog.gui.components.beans;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;

import de.hu.gralog.app.UserException;
import de.hu.gralog.beans.propertydescriptor.ChooseStructureElementPropertyDescriptor;
import de.hu.gralog.beans.propertydescriptor.ChooseStructurePropertyDescriptor;
import de.hu.gralog.beans.propertydescriptor.ChooseStructureTypeInfoPropertyDescriptor;
import de.hu.gralog.gui.MainPad;
import de.hu.gralog.gui.components.beans.propertyeditors.ChooseStructurePropertyEditor;
import de.hu.gralog.gui.components.beans.propertyeditors.ChooseStructureTypeInfoPropertyEditor;

public class PropertyEditorManager {

	public static final PropertyEditor getPropertyEditor( PropertyDescriptor propertyDescriptor, Object bean ) {
		if ( propertyDescriptor == null )
			return null;
		if ( propertyDescriptor instanceof ChooseStructurePropertyDescriptor )
			return new ChooseStructurePropertyEditor( ((ChooseStructurePropertyDescriptor)propertyDescriptor).getStructureType() );
		if ( propertyDescriptor instanceof ChooseStructureElementPropertyDescriptor )
			return propertyDescriptor.createPropertyEditor( MainPad.getInstance().getDesktop().getCurrentGraph().getGraphT() );
		if ( propertyDescriptor instanceof ChooseStructureTypeInfoPropertyDescriptor )
			return new ChooseStructureTypeInfoPropertyEditor( ((ChooseStructureTypeInfoPropertyDescriptor)propertyDescriptor).getStructureTypeInfoFilter() );

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
