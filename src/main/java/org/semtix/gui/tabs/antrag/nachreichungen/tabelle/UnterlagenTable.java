/*

  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
         Semesterticketbüro der Humboldt-Universität Berlin

  Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package org.semtix.gui.tabs.antrag.nachreichungen.tabelle;

import org.semtix.db.dao.Unterlagen;
import org.semtix.gui.tabs.antrag.AntragControl;
import org.semtix.shared.textbausteine.DialogUnterlagen;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;



/**
 * Panel mit Tabelle und Kontrollelementen zur Anzeige der Unterlagen und ihrem Status
 *
 */
@SuppressWarnings("serial")
public class UnterlagenTable
extends JTable {
	
	private JPopupMenu popupMenu;

	private AntragControl antragControl;


    public UnterlagenTable(AntragControl antragControl) {

        this.antragControl = antragControl;

        this.setModel(antragControl.getTableModelUnterlagen());

        this.setLayout(new BorderLayout());
		
		popupMenu = new JPopupMenu();
	    popupMenu.add(new JMenuItem(new ActionUnterlagenAendern(antragControl, this)));
	    popupMenu.add(new JMenuItem(new ActionUnterlagenLoeschen(antragControl, this)));
	    popupMenu.add(new JMenuItem(new ActionUnterlagenHinzufuegen(antragControl)));

	    // PopupMenu an Tabelle setzen
	    setComponentPopupMenu(popupMenu);
		
	    // MouseListener an Tabelle hinzufügen
		addMouseListener(new PopupMenuMouseListener(this, popupMenu));


		setPreferredScrollableViewportSize(new Dimension(500, 585));
		
		// nur einzelne Tabellenzeilen können selektiert werden
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				
		// Zellen erhalten keinen Fokusrahmen
		setFocusable(false);
				
		// die Spalten der Tabelle können nicht vertauscht werden
		getTableHeader().setReorderingAllowed(false);
				
		// die Spaltenbreite kann nicht verändert/verschoben werden
		getTableHeader().setResizingAllowed(true);
				
		// Zeilenhöhe setzen
		setRowHeight(80);

        // Hintergrundfarbe für Tabelle setzen
		setBackground(new Color(231, 238, 248));
				
		setShowGrid(false);
		setIntercellSpacing(new Dimension(0, 0));
		setShowHorizontalLines(true);
		setGridColor(new Color(200, 200, 200));
		
		// MouseListener für Klicks auf Tabelle
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				
				updateColorBox(me);
	
			}			
		} );
		
		// Renderer für die Zellen mit ImageIcon (grün, rot, rotgrün)
		UnterlagenTextCellRenderer textCellRenderer = new UnterlagenTextCellRenderer();
		
		// Renderer für die Zellen mit ImageIcon (grün, rot, rotgrün)
		UnterlagenCellRenderer cellRenderer = new UnterlagenCellRenderer();
		
		// Tabellenspaltenmodel von der Tabelle holen
		TableColumnModel columnModel = getColumnModel();
		
		// Den Tabellenspalten Breite und Renderer zuweisen
		columnModel.getColumn(0).setPreferredWidth(440);
		columnModel.getColumn(0).setMinWidth(440);

		columnModel.getColumn(0).setCellRenderer(textCellRenderer);
		columnModel.getColumn(1).setPreferredWidth(60);
		columnModel.getColumn(1).setMinWidth(60);
		columnModel.getColumn(1).setCellRenderer(cellRenderer);

	}

	
	
	// Mausklick ändert UnterlagenStatus
	private void updateColorBox(MouseEvent me) {
		
		JTable table = (JTable)me.getSource();
		Point point = me.getPoint();
		
		int currentRow = this.rowAtPoint(point);
		int column = table.columnAtPoint(point);
		
		if(column == 0){
			List<Unterlagen> listeUnterlagen = new ArrayList<Unterlagen>();
			listeUnterlagen.add(antragControl.getTableModelUnterlagen().getUnterlagen(table.getSelectedRow()));

			new DialogUnterlagen(antragControl, listeUnterlagen);

        }

        // nur wenn Klick auf Spalte mit UnterlagenStatus
        if(column == 1){
        	
        	// nur linke Maustaste soll den UnterlagenStatus ändern
			if (me.getButton() == MouseEvent.BUTTON1){
				antragControl.getTableModelUnterlagen().plus(currentRow);
			}
        	
        }
		
	}
	

	
	
	private class PopupMenuMouseListener extends MouseAdapter 
	{
        private final JPopupMenu popmen;
        private UnterlagenTable table;
	 
		public PopupMenuMouseListener(UnterlagenTable table, JPopupMenu popmen) 
		{ 
			this.table = table;
			this.popmen = popmen; 
		} 
		
		
		
		@Override 
		public void mouseReleased(MouseEvent me) { 

        	int currentRow = table.rowAtPoint(me.getPoint());

			table.setRowSelectionInterval(currentRow, currentRow);
			
		} // Ende Methode mouseReleased
	 
		
		@Override 
		public void mousePressed(MouseEvent me) { 
			
			
			
			int column = table.columnAtPoint(me.getPoint());
			
			// nur wenn Klick auf Spalte mit UnterlagenStatus
	        if(column == 0){
	        	
	        	if(me.isPopupTrigger()){
					if(!popmen.isVisible()){

				        int currentRow = table.rowAtPoint(me.getPoint());

						table.setRowSelectionInterval(currentRow, currentRow);

					}
					else{
						popmen.setVisible(false);
					}
				}
	        	
	        }

			
		} // Ende Methode mousePressed
	}
	
	
}

