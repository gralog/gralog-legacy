/*
 * Created on 2008 by Sebastian Ordyniak
 *
 * Copyright 2008 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
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
package de.hu.gralog.beans.propertyeditor;

import java.awt.Component;
import java.beans.PropertyEditor;

/**
 * This interface extends {@link java.beans.PropertyEditor} in order to allow
 * customrenders. This is much more convinient than implementing the
 * {@link java.beans.PropertyEditor#paintValue(java.awt.Graphics, java.awt.Rectangle)}
 * method, where you have to paint the value by yourselfe.
 * 
 * 
 * @author Sebastian
 * 
 */
public interface PropertyEditorRendererExtension extends PropertyEditor {
	public Component getCustomRenderer();
}
