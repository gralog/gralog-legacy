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

package de.hu.gralog.gui;

import java.awt.Component;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.jgrapht.Graph;

import de.hu.gralog.graph.GraphWithEditableElements;
import de.hu.gralog.graph.alg.ChooseGraphPropertyDescriptor;
import de.hu.gralog.graph.alg.ChooseVertexPropertyDescriptor;
import de.hu.gralog.gui.document.Document;
import de.hu.gralog.gui.document.GJGraphDocumentContent;

public class PropertyEditorTable extends JTable {
	
	protected final HTMLStringEditorAndRenderer HTML_STRING_EDITOR_AND_RENDERER = new HTMLStringEditorAndRenderer();
	
	protected static final TextPropertyEditorRenderer TEXT_PROPERTY_EDITOR_RENDERER = new TextPropertyEditorRenderer();
	protected static final TagsTableCellEditor TAGS_PROPERTY_EDITOR_EDITOR = new TagsTableCellEditor();
	
	
	public PropertyEditorTable() {
		super(  );
		setDefaultEditor( Byte.class, new MyNumberEditor() );
		setDefaultEditor( Float.class, new MyNumberEditor() );
		setDefaultEditor( Long.class, new MyNumberEditor() );
		setDefaultEditor( Short.class, new MyNumberEditor() );
		setDefaultEditor( Integer.class, new MyNumberEditor() );
		setDefaultEditor( Double.class, new MyNumberEditor() );
		setDefaultRenderer( GraphWithEditableElements.class, new GraphCellRenderer() );
		super.setRowHeight( 27 );
	}
	
	public AbstractBeanEditorTableModel getPropertyEditorTableModel() {
		return (AbstractBeanEditorTableModel)getModel();
	}
	
	protected Class getPropertyType(PropertyDescriptor propertyDescriptor ) {
		Class propType = propertyDescriptor.getPropertyType();
		if ( propType.isPrimitive() ) {
			if ( propType.getName().equals( "boolean" ) )
				return Boolean.class;
			if ( propType.getName().equals( "byte" ) )
				return Byte.class;
			if ( propType.getName().equals( "double" ) )
				return Double.class;
			if ( propType.getName().equals( "float" ) )
				return Float.class;
			if ( propType.getName().equals( "int" ) )
				return Integer.class;
			if ( propType.getName().equals( "long" ) )
				return Long.class;
			if ( propType.getName().equals( "short" ) )
				return Short.class;
		}
		return propType;
	}
	
	protected TableCellEditor getCellEditor( PropertyEditor propertyEditor ) {
		if ( propertyEditor.getTags() != null ) {
			TAGS_PROPERTY_EDITOR_EDITOR.setPropertyEditor( propertyEditor );
			return TAGS_PROPERTY_EDITOR_EDITOR;
		}
		return null;
	}
	
	@Override
	public TableCellEditor getCellEditor(int row, int column) {
		column = convertColumnIndexToModel( column );
		PropertyDescriptor propertyDescriptor = getPropertyEditorTableModel().getPropertyDescriptor( row, column );
		
		if ( propertyDescriptor instanceof ChooseGraphPropertyDescriptor )
			return getCellEditor( new ChooseGraphPropertyEditor( ((ChooseGraphPropertyDescriptor)propertyDescriptor).getGraphType() ) );
		if ( propertyDescriptor instanceof ChooseVertexPropertyDescriptor )
			return getCellEditor( new ChooseVertexPropertyEditor( ) );
		
		if ( propertyDescriptor == null )
			return getDefaultEditor( String.class );
		if ( propertyDescriptor.getPropertyEditorClass() == null )
			return getDefaultEditor( getPropertyType( propertyDescriptor ) );
		
		return getCellEditor( propertyDescriptor.createPropertyEditor( null ) );
	}

	public TableCellRenderer getCellRenderer( PropertyEditor propertyEditor ) {
		if ( ! propertyEditor.isPaintable() ) {
			TEXT_PROPERTY_EDITOR_RENDERER.setPropertyEditor( propertyEditor );
			return TEXT_PROPERTY_EDITOR_RENDERER;
		}
		return null;
	}
	
	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		column = convertColumnIndexToModel( column );
		PropertyDescriptor propertyDescriptor = getPropertyEditorTableModel().getPropertyDescriptor( row, column );
		
		if ( propertyDescriptor instanceof ChooseGraphPropertyDescriptor )
			return getCellRenderer( new ChooseGraphPropertyEditor( ((ChooseGraphPropertyDescriptor)propertyDescriptor).getGraphType() ) );
		if ( propertyDescriptor instanceof ChooseVertexPropertyDescriptor )
			return getCellRenderer( new ChooseVertexPropertyEditor(  ) );
		
		if ( propertyDescriptor == null )
			return getDefaultRenderer( String.class );
		if ( propertyDescriptor.getPropertyEditorClass() == null )
			return getDefaultRenderer( getPropertyType( propertyDescriptor ) );
		return getCellRenderer( propertyDescriptor.createPropertyEditor( null ) );
	}
	
	
	
	public static class MyNumberEditor extends DefaultCellEditor {
		
		public MyNumberEditor(  ) {
			super( new JFormattedTextField() );
			
			final JFormattedTextField textField = (JFormattedTextField)getComponent();
			delegate = new EditorDelegate() {
				public void setValue(Object value) {
					textField.setValue( value );
				}
				
				public Object getCellEditorValue() {
					return textField.getValue();
				}
			};
			textField.setHorizontalAlignment(JTextField.RIGHT);
			textField.setFocusLostBehavior( JFormattedTextField.COMMIT_OR_REVERT );
		}
						
	}
	
	public static class HTMLStringEditorAndRenderer extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

		protected final JScrollPane scrollPane;
		protected final HTMLEditorPane editor;
		
		public HTMLStringEditorAndRenderer() {
			super();
			editor = new HTMLEditorPane();
			scrollPane = new JScrollPane( editor );
		}
		
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			editor.setText( (String)value );
			return scrollPane;
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			editor.setText( (String) value );
			return scrollPane;
		}

		public Object getCellEditorValue() {
			return editor.getText();
		}
		
	}
	
	public static class GraphCellRenderer extends JLabel implements TableCellRenderer {
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Graph g;
			if ( value != null && Graph.class.isInstance( value ) ) {
				for ( Document doc : MainPad.getInstance().getDesktop().getOpenDocuments() ) {
					if ( doc.getContent() instanceof GJGraphDocumentContent && ((GJGraphDocumentContent)doc.getContent()).getGraph().getGraphT() == value )
						setText( doc.getName() );
				}
			} else
				setText( null );
			return this;
		}
		
	}
	
	public static class TextPropertyEditorRenderer extends JLabel implements TableCellRenderer {

		private PropertyEditor propertyEditor;
		
		public TextPropertyEditorRenderer(  ) {
			
		}
		
		public void setPropertyEditor( PropertyEditor propertyEditor ) {
			this.propertyEditor = propertyEditor;
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			propertyEditor.setValue( value );
			setText( propertyEditor.getAsText() );
			return this;
		}
		
	}
	
	public static class TagsTableCellEditor extends DefaultCellEditor {
		
		public TagsTableCellEditor(  ) {
			super( new JComboBox(  ) );
		}
		
		public void setPropertyEditor( final PropertyEditor propertyEditor ) {
			final JComboBox comboBox = (JComboBox)getComponent();
			comboBox.setModel( new DefaultComboBoxModel( propertyEditor.getTags() ) );
			delegate = new EditorDelegate() {
				public void setValue( Object value ) {
					propertyEditor.setValue( value );
					comboBox.setSelectedItem( propertyEditor.getAsText() );
				}
				
				public Object getCellEditorValue() {
					propertyEditor.setAsText( (String)comboBox.getSelectedItem() );
					return propertyEditor.getValue();
				}
			};
		}
	}
	
	
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		if ( getModel() instanceof GraphElementEditorTableModel ) {
			GraphElementEditorTableModel model = (GraphElementEditorTableModel)getModel();
			
			Vector highlight = new Vector();
			for ( int row : getSelectedRows() )
				highlight.add( model.getUserObject( row ) );
			MainPad.getInstance().getDesktop().getCurrentGraph().highlightObjects( highlight );
		}
		
		super.valueChanged(e);
	}
}
