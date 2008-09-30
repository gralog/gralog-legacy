package de.hu.gralog;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import de.hu.gralog.gui.components.HTMLEditorPane;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame( "Test" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
//		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//		frame.setPreferredSize( ge.getDefaultScreenDevice().getDefaultConfiguration().getBounds().getSize() );

		JTable table = new JTable();
		table.setModel( new MyTableModel() );
		table.setDefaultRenderer( String.class, new MyTableCellRenderer() );
		table.setRowHeight( 100 );
		frame.getContentPane().add( table );
		
		frame.setPreferredSize( new Dimension( 200, 100 ) );
		frame.pack();
		frame.setVisible( true );
	}

	static class MyTableCellRenderer extends HTMLEditorPane implements TableCellRenderer {

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			setText( "<html>" + 
					"erste Zeile<br>" +
					"zweite Zeile<br>" +
					"dritte Zeile<br>" +
					"vierte Zeile<br>" +
					"fünfte Zeile<br>" +
					"sechste Zeils<br" +
					"</html>" );
			return this;
		}
		
	}
	
	static class MyTableModel extends AbstractTableModel {

		public int getRowCount() {
			return 1;
		}

		public int getColumnCount() {
			return 1;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			return 
			"<html>" + 
			"erste Zeile<br>" +
			"zweite Zeile<br>" +
			"dritte Zeile<br>" +
			"vierte Zeile<br>" +
			"fünfte Zeile<br>" +
			"sechste Zeils<br" +
			"</html>";
		}
		
	}
}
