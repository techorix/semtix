/*
 * Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 *        Semesterticketbüro der Humboldt-Universität Berlin
 *
 * Copyright (c) 2015. Michael Mertins (MichaelMertins@gmail.com)
 * 2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)
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

package org.semtix.gui.tabs.antrag.nachreichungen.tabelle;

import org.semtix.db.DBHandlerUnterlagen;
import org.semtix.db.dao.Unterlagen;
import org.semtix.shared.daten.enums.UnterlagenStatus;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Model-Klasse enthält die Daten für die Tabelle mit den Unterlagen
 * zu den jeweiligen Anträgen.
 *
 */
@SuppressWarnings("serial")
public class TableModelUnterlagen
extends AbstractTableModel {
	
	private List<Unterlagen> listeUnterlagen;
	
	private String[] columnNames;
	
	private boolean isEN;
	
	
	
	public TableModelUnterlagen() {
		
		listeUnterlagen = new ArrayList<Unterlagen>();
		
		columnNames = new String[] {"Nachreichung", "Status"};
	
	}
	
	public boolean isEN(){
		return isEN;
	}

    public void setEN(boolean isEN) {
        this.isEN = isEN;
    }

    public void setUnterlagen(int antragID) {
		DBHandlerUnterlagen dbHandlerUnterlagen = new DBHandlerUnterlagen();
		listeUnterlagen = dbHandlerUnterlagen.getUnterlagenListe(antragID);

		Collections.reverse(listeUnterlagen);

		this.fireTableDataChanged();
		
	}


    // Liefert Liste mit den Unterlagen zurück
	public List<Unterlagen> getUnterlagenListe() {
		return listeUnterlagen;
	}
	
	// Liefert ein bestimmtes Unterlagen-Objekt zurück (anhand des Index in der Liste)
	public Unterlagen getUnterlagen(int index) {
		return listeUnterlagen.get(index);
	}


	
	public void addUnterlagen(Unterlagen unterlagen) {
		DBHandlerUnterlagen dbHandlerUnterlagen = new DBHandlerUnterlagen();
		dbHandlerUnterlagen.createUnterlagen(unterlagen);
		listeUnterlagen.add(unterlagen);

		Collections.sort(listeUnterlagen);
		Collections.reverse(listeUnterlagen);

		this.fireTableDataChanged();
	}
	
	
	public void updateUnterlagen(Unterlagen unterlagen) {
		DBHandlerUnterlagen dbHandlerUnterlagen = new DBHandlerUnterlagen();
		dbHandlerUnterlagen.updateUnterlagen(unterlagen);
				
		this.fireTableDataChanged();
	}
	
	
	public void deleteUnterlagen(int index) {
		Unterlagen unterlagen = listeUnterlagen.get(index);
		DBHandlerUnterlagen dbHandlerUnterlagen = new DBHandlerUnterlagen();
		dbHandlerUnterlagen.deleteUnterlagen(unterlagen.getUnterlagenID());
		listeUnterlagen.remove(index);
		this.fireTableDataChanged();
	}
	
	
	public void plus(int index) {
		Unterlagen unterlagen = listeUnterlagen.get(index);
		int unterlagenStatusID = unterlagen.getUnterlagenStatus().getID();
		
		unterlagenStatusID++;
        if (unterlagenStatusID == 5)
        	unterlagenStatusID = 1;
        
        unterlagen.setUnterlagenStatus(UnterlagenStatus.getUnterlagenStatusByID(unterlagenStatusID));
        
        DBHandlerUnterlagen dbHandlerUnterlagen = new DBHandlerUnterlagen();
		dbHandlerUnterlagen.updateUnterlagen(unterlagen);
        
        this.fireTableDataChanged();
	}
	
	
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public int getRowCount() {
		return listeUnterlagen.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

        if (columnIndex == 0)
			return listeUnterlagen.get(rowIndex).getText();

        if (columnIndex == 1)
			return listeUnterlagen.get(rowIndex).getUnterlagenStatus().getID();
		
		return null;
		
	}

	

}
