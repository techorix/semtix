package org.semtix.gui.tabs.antrag.nachreichungen.tabelle;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Renderer zur Darstellung des Unterlagentextes in der Tabellenübersicht (UnterlagenTable)
 */
@SuppressWarnings("serial")
public class UnterlagenTextCellRenderer
extends JTextArea
implements TableCellRenderer {

	
	
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object obj,
			boolean isSelected, boolean hasFocus, int row, int column) {

		setLineWrap(true);
		setWrapStyleWord(true);
        setText((String) obj);

        return this;
    }

}
