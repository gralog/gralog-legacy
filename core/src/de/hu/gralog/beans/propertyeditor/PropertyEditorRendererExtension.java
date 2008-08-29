package de.hu.gralog.beans.propertyeditor;

import java.awt.Component;
import java.beans.PropertyEditor;

public interface PropertyEditorRendererExtension extends PropertyEditor {
	public Component getCustomRenderer();
}
