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
import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import de.hu.gralog.beans.propertydescriptor.StructureElementFilter;
import de.hu.gralog.structure.Structure;

/**
 * This PropertyEditor allows to edit and renderer values that are described by
 * {@link de.hu.gralog.beans.propertydescriptor.ChooseStructureElementPropertyDescriptor}.
 * 
 * @author Sebastian
 * 
 */
public class ChooseStructureElementPropertyEditor extends PropertyEditorSupport
		implements PropertyEditorRendererExtension {

	private final StructureElementFilter structureElementFilter;

	private final Structure structure;

	private ChooseStructureElementPropertyEditorComponent editor;

	public ChooseStructureElementPropertyEditor(Structure structure,
			StructureElementFilter structureElementFilter) {
		super();
		this.structureElementFilter = structureElementFilter;
		this.structure = structure;
		editor = new ChooseStructureElementPropertyEditorComponent();
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

	private Structure getStructure() {
		return structure;
	}

	List<Object> getChoosableElements() {
		ArrayList<Object> chooseableElements = new ArrayList<Object>();
		Structure graphSupport = getStructure();
		if (graphSupport == null)
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
		Structure structure = getStructure();
		if (structure == null)
			return null;
		Set vertices = structure.getStructureSelectionSupport()
				.getSelectedVertices();
		if (vertices != null
				&& vertices.size() == 1
				&& !structureElementFilter.filterElement(structure, vertices
						.iterator().next()))
			return vertices.iterator().next();
		Set edges = structure.getStructureSelectionSupport().getSelectedEdges();
		if (edges != null
				&& edges.size() == 1
				&& !structureElementFilter.filterElement(structure, edges
						.iterator().next()))
			return edges.iterator().next();
		return null;
	}

	protected class ChooseStructureElementPropertyEditorComponent extends JPanel
			implements ActionListener {
		private JComboBox chooseCombo;

		private JButton button;

		public ChooseStructureElementPropertyEditorComponent() {
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
