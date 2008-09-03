package de.hu.gralog.beans.propertyeditor;

import java.awt.Component;
import java.beans.PropertyEditor;

/**
 * This interface extends {@link java.beans.PropertyEditor} in order
 * to allow customrenders. This is much more convinient than implementing
 * the {@link java.beans.PropertyEditor#paintValue(java.awt.Graphics, java.awt.Rectangle)}
 * method, where you have to paint the value by yourselfe. 
 * 
 * 
 * @author Sebastian
 *
 */
public interface PropertyEditorRendererExtension extends PropertyEditor {
	public Component getCustomRenderer();
}
