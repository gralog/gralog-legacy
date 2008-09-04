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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyEditorSupport;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 * This PropertyEditor allows to edit and renderer values that are described by
 * {@link de.hu.gralog.beans.propertydescriptor.EnumPropertyDescriptor}.
 * 
 * @author Sebastian
 * 
 */
public class EnumPropertyEditor extends PropertyEditorSupport implements
		PropertyEditorRendererExtension {

	private final Enum[] values;

	private EnumPropertyEditorComponent editor;

	public EnumPropertyEditor(Enum[] values) {
		super();
		this.values = values;
		editor = new EnumPropertyEditorComponent();
	}

	public Component getCustomRenderer() {
		editor.configure();
		return editor;
	}

	@Override
	public boolean supportsCustomEditor() {
		return true;
	}

	@Override
	public Component getCustomEditor() {
		editor.configure();
		return editor;
	}

	String[] getNames() {
		String[] names = new String[values.length];
		for (int i = 0; i < values.length; i++)
			names[i] = values[i].name();
		return names;
	}

	static String getNameForValue(Object value) {
		if (value == null)
			return null;
		return ((Enum) value).name();
	}

	Object getValueForName(String name) {
		if (name == null)
			return null;
		return Enum.valueOf(values[0].getDeclaringClass(), name);
	}

	protected class EnumPropertyEditorComponent extends JPanel implements
			ActionListener {
		private JComboBox chooseCombo;

		public EnumPropertyEditorComponent() {
			super(new BorderLayout());

			chooseCombo = new JComboBox();
			comboBoxUpdate();
			chooseCombo.addActionListener(this);
			chooseCombo.setActionCommand("SELECTED");
			add(chooseCombo, BorderLayout.CENTER);
		}

		private void comboBoxUpdate() {
			chooseCombo.setModel(new DefaultComboBoxModel(getNames()));
			chooseCombo.setSelectedItem(getNameForValue(getValue()));
		}

		public void configure() {
			chooseCombo.setSelectedItem(getNameForValue(getValue()));
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("SELECTED"))
				setValue(getValueForName((String) chooseCombo.getSelectedItem()));
		}

	}

}
