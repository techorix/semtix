/*
 * Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 * Semesterticketbüro der Humboldt-Universität Berlin
 *
 * Copyright (c) 2015. Michael Mertins (MichaelMertins@gmail.com)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.semtix.gui.tabs.personendaten;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * PopupMenuMouseListener für Tabelle mit Anmerkungen. Mit Klick der rechten Maustaste auf 
 * die Tabelle mit den Anmerkungen öffnet sich ein PopupMenu mit Möglichkeiten 
 * zur Bearbeitung der Anmerkungne 
 */
public class PopupMenuMouseListener
extends MouseAdapter {
	
	private final JPopupMenu popmen; 
	 
	/**
	 * Erstellt neuen PopupMenuMouseListener
	 * @param popmen JPopupMenu
	 */
	public PopupMenuMouseListener(JPopupMenu popmen) 
	{ 
		this.popmen = popmen; 
	} 
 
	@Override 
	public void mousePressed(MouseEvent me) { 
	  
		if(me.isPopupTrigger()){
			if(!popmen.isVisible()){
      	  
				JTable source = (JTable)me.getSource();
				int row = source.rowAtPoint(me.getPoint());
				int column = source.columnAtPoint(me.getPoint());

				if (! source.isRowSelected(row))
					source.changeSelection(row, column, false, false);

				popmen.show(source, me.getX(), me.getY());
            
			}
			else{
				popmen.setVisible(false);
			}
		}

	} 

}
