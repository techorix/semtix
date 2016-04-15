package org.semtix.gui.tabs.antrag.nachreichungen.tabelle;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Renderer zur Darstellung der Unterlagen mit farbigem Icon in der Tabellenübersicht (UnterlagenTable)
 */
@SuppressWarnings("serial")
public class UnterlagenCellRenderer
extends JLabel
implements TableCellRenderer {
	
	private Icon bild1 = new ImageIcon(getClass().getResource("/images/unterlagenstatus_erhalten.gif"));
	private Icon bild2 = new ImageIcon(getClass().getResource("/images/unterlagenstatus_nachgefragt.gif"));
	private Icon bild3 = new ImageIcon(getClass().getResource("/images/unterlagenstatus_gemahnt.gif"));
	private Icon bild4 = new ImageIcon(getClass().getResource("/images/unterlagenstatus_nichtnachgefragt.gif"));

	private Icon[] iconArray = {bild1, bild2, bild3, bild4};
	
	private int iconIndex = 0;
	
	
	public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
		
		this.setOpaque(true);
		
		this.setHorizontalAlignment(CENTER);
		
		iconIndex = (Integer) value - 1;

		this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                iconIndex++;
                if (iconIndex == iconArray.length)
                    iconIndex = 0;
                changeIcon(iconIndex);

            }

            public void mouseReleased(MouseEvent arg0) {
                iconIndex++;
                if (iconIndex == iconArray.length)
                    iconIndex = 0;
                changeIcon(iconIndex);

            }
        });

        this.setIcon(iconArray[iconIndex]);

        if (iconIndex == 0)
            this.setToolTipText("Nachreichung erhalten");
        else if (iconIndex == 1)
            this.setToolTipText("Nachreichung nachgefragt");
        else if (iconIndex == 2)
            this.setToolTipText("Nachreichung angemahnt");
        else if (iconIndex == 3)
            this.setToolTipText("Nachreichung noch nicht nachgefragt");


        return this;
    }

	
	
	/**
	 * angezeigtes Icon ändern
	 * @param index Index der Icons in der Liste
	 */
	public void changeIcon(int index) {
		this.setIcon(iconArray[index]);
	}
	

}
