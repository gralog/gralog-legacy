/*
 * Created on 2008 by Sebastian Ordyniak
 *
 * Copyright 2008 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of GrALoG.
 *
 * GrALoG is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * GrALoG is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GrALoG; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */
package de.hu.gralog.beans.propertyeditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import de.hu.gralog.app.UserException;
import de.hu.gralog.beans.BeanUtil;
import de.hu.gralog.beans.propertydescriptor.ChooseDependableStructureElementPropertyDescriptor;
import de.hu.gralog.beans.propertydescriptor.StructureElementFilter;
import de.hu.gralog.structure.Structure;

/**
 * This PropertyEditor allows to edit and renderer values that are described by
 * {@link de.hu.gralog.beans.propertydescriptor.ChooseDependableStructureElementPropertyDescriptor}.
 * 
 * @author Sebastian
 * 
 */
public class ChooseDependableStructureElementPropertyEditor extends
		PropertyEditorSupport implements PropertyEditorRendererExtension {

	private final StructureElementFilter structureElementFilter;

	private final PropertyDescriptor structurePD;

	private final Object bean;

	private ChooseDependableElementPropertyEditorComponent editor;

	public ChooseDependableStructureElementPropertyEditor(
			ChooseDependableStructureElementPropertyDescriptor pd, Object bean) {
		super();
		this.structureElementFilter = pd.getStructureElementFilter();
		this.structurePD = pd.getDependsOnPropertyDescriptors().iterator().next();
		this.bean = bean;
		editor = new ChooseDependableElementPropertyEditorComponent();
	}

	public Component getCustomRenderer() {
		editor.updateElements();
		return editor;
	}

	@Override
	public boolean supportsCustomEditor() {
		return true;
	}

	@Override
	public Component getCustomEditor() {
		editor.updateElements();
		return editor;
	}

	private Structure getGraphSupport() {
		try {
			return (Structure) BeanUtil.getValue(bean, structurePD);
		} catch (UserException e) {
			e.printStackTrace();
			return null;
		}
	}

	List<Object> getChoosableElements() {
		ArrayList<Object> chooseableElements = new ArrayList<Object>();
		Structure graphSupport = getGraphSupport();
		if (getGraphSupport() == null)
			return chooseableElements;
		for (Object vertex : graphSupport.getGraph().vertexSet()) {
			if (!structureElementFilter.filterElement(graphSupport, vertex))
				chooseableElements.add(vertex);
		}
		for (Object edge : graphSupport.getGraph().edgeSet()) {
			if (!structureElementFilter.filterElement(graphSupport, edge))
				chooseableElements.add(edge);
		}
		return chooseableElements;
	}

	Object getSelectedElement() {
		Structure graphSupport = getGraphSupport();
		if (graphSupport == null)
			return null;
		Set vertices = graphSupport.getStructureSelectionSupport()
				.getSelectedVertices();
		if (vertices != null
				&& vertices.size() == 1
				&& !structureElementFilter.filterElement(graphSupport, vertices
						.iterator().next()))
			return vertices.iterator().next();
		Set edges = graphSupport.getStructureSelectionSupport().getSelectedEdges();
		if (edges != null
				&& edges.size() == 1
				&& !structureElementFilter.filterElement(graphSupport, edges
						.iterator().next()))
			return edges.iterator().next();
		return null;
	}

	protected class ChooseDependableElementPropertyEditorComponent extends
			JPanel implements ActionListener {
		private JComboBox chooseCombo;

		private JButton button;

		public ChooseDependableElementPropertyEditorComponent() {
			super(new BorderLayout());

			chooseCombo = new JComboBox();
			comboBoxUpdate();
			chooseCombo.addActionListener(this);
			chooseCombo.setActionCommand("SELECTED");
			add(chooseCombo, BorderLayout.CENTER);

			button = new JButton("S");
			button.setActionCommand("CURRENT");
			button.addActionListener(this);
			add(button, BorderLayout.EAST);
		}

		private void comboBoxUpdate() {
			chooseCombo.setModel(new DefaultComboBoxModel(
					getChoosableElements().toArray()));
			chooseCombo.setSelectedItem(getValue());
		}

		private void updateElements() {
			comboBoxUpdate();
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("SELECTED")) {
				setValue(chooseCombo.getSelectedItem());
			} else {
				if (getSelectedElement() != null) {
					setValue(getSelectedElement());
					chooseCombo.setSelectedItem(getValue());
				}
			}

		}

	}

}
