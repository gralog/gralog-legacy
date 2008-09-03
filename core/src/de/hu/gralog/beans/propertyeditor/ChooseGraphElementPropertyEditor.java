/*
 * Created on 2006 by Sebastian Ordyniak
 *
 * Copyright 2006 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of Games.
 *
 * Games is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * Games is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Games; 
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

import de.hu.gralog.beans.propertydescriptor.GraphElementFilter;
import de.hu.gralog.graph.GralogGraphSupport;

/**
 * This PropertyEditor allows to edit and renderer values that
 * are described by 
 * {@link de.hu.gralog.beans.propertydescriptor.ChooseGraphElementPropertyDescriptor}.
 * 
 * @author Sebastian
 *
 */
public class ChooseGraphElementPropertyEditor extends PropertyEditorSupport
		implements PropertyEditorRendererExtension {

	private final GraphElementFilter graphElementFilter;

	private final GralogGraphSupport graphSupport;

	private ChooseGraphElementPropertyEditorComponent editor;

	public ChooseGraphElementPropertyEditor(GralogGraphSupport graphSupport,
			GraphElementFilter graphElementFilter) {
		super();
		this.graphElementFilter = graphElementFilter;
		this.graphSupport = graphSupport;
		editor = new ChooseGraphElementPropertyEditorComponent();
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

	private GralogGraphSupport getGraphSupport() {
		return graphSupport;
	}

	List<Object> getChoosableElements() {
		ArrayList<Object> chooseableElements = new ArrayList<Object>();
		GralogGraphSupport graphSupport = getGraphSupport();
		if (graphSupport == null)
			return chooseableElements;
		for (Object vertex : graphSupport.getGraph().vertexSet()) {
			if (!graphElementFilter.filterElement(graphSupport, vertex))
				chooseableElements.add(vertex);
		}
		for (Object edge : graphSupport.getGraph().edgeSet()) {
			if (!graphElementFilter.filterElement(graphSupport, edge))
				chooseableElements.add(edge);
		}
		return chooseableElements;
	}

	Object getSelectedElement() {
		GralogGraphSupport graphSupport = getGraphSupport();
		if (graphSupport == null)
			return null;
		Set vertices = graphSupport.getGraphSelectionSupport()
				.getSelectedVertices();
		if (vertices != null
				&& vertices.size() == 1
				&& !graphElementFilter.filterElement(graphSupport, vertices
						.iterator().next()))
			return vertices.iterator().next();
		Set edges = graphSupport.getGraphSelectionSupport().getSelectedEdges();
		if (edges != null
				&& edges.size() == 1
				&& !graphElementFilter.filterElement(graphSupport, edges
						.iterator().next()))
			return edges.iterator().next();
		return null;
	}

	protected class ChooseGraphElementPropertyEditorComponent extends JPanel
			implements ActionListener {
		private JComboBox chooseCombo;

		private JButton button;

		public ChooseGraphElementPropertyEditorComponent() {
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
