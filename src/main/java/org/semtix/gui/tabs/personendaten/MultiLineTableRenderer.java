package org.semtix.gui.tabs.personendaten;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;


/**
 * Renderer für mehrzeilige Zellen in JTable (da JTable normalerweise nur 
 * einzeilige Zellen darstellen kann)
 */
@SuppressWarnings("serial")
public class MultiLineTableRenderer
extends JTextArea implements TableCellRenderer{

	
	/**
	 * Erstellt einen MultiLineTableRenderer
	 */
	public MultiLineTableRenderer() {
		
		setEditable(false);
        setLineWrap(true);
        setWrapStyleWord(true);
		this.setBackground(Color.WHITE);
	
	}

	
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, 
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		if (isSelected) {
	        setForeground(table.getSelectionForeground());
	        setBackground(table.getSelectionBackground());
		}
		else {
	        setForeground(table.getForeground());
	        setBackground(table.getBackground());
		}
		
		setFont(table.getFont());
		
		if (hasFocus) {
			setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
			if (table.isCellEditable(row, column)) {
				setForeground(UIManager.getColor("Table.focusCellForeground"));
				setBackground(UIManager.getColor("Table.focusCellBackground"));
			}
		}
		else {
			setBorder(new EmptyBorder(2, 2, 2, 2));
		}
		
		
		this.setText(value.toString());
		
        return this;
	}
	
}
