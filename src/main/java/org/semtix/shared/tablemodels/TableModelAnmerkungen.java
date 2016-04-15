/*

  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
         Semesterticketbüro der Humboldt-Universität Berlin

  Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package org.semtix.shared.tablemodels;

import org.semtix.db.DBHandlerAnmerkungen;
import org.semtix.db.DBHandlerUser;
import org.semtix.db.dao.Anmerkung;
import org.semtix.db.dao.SemtixUser;
import org.semtix.shared.daten.DeutschesDatum;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model-Klasse enthält die Daten für die Tabelle Anmerkungen im Personenformular
 */
@SuppressWarnings("serial")
public class TableModelAnmerkungen
extends AbstractTableModel {

    private DBHandlerAnmerkungen dbHandlerAnmerkungen;
	
    private DBHandlerUser dbHandlerUser;

	private int personID;

	private List<Anmerkung> listeAnmerkungen;

	private String[] columnNames;
	
	/**
	 * Erstellt ein neues TableModel
	 */
	public TableModelAnmerkungen() {
		
		dbHandlerAnmerkungen = new DBHandlerAnmerkungen();
		dbHandlerUser = new DBHandlerUser();
		
		listeAnmerkungen = new ArrayList<Anmerkung>();

		columnNames = new String[]{"", "", ""};

	}
	
	/**
	 * Setzt die Person anhand der Person-ID und füllt die Liste mit den Anmerkungen
	 * @param pid ID der Person
	 */
	public void setPersonID(int pid) {
		this.personID = pid;
		fillList();
	}
	
	
	/**
	 * Die Liste wird mit den Anmerkungen zu einer bestimmten Person aus der datenbank gefüllt
	 */
	public void fillList() {
		listeAnmerkungen = dbHandlerAnmerkungen.getAnmerkungenListe(personID);
		Collections.sort(listeAnmerkungen, Collections.reverseOrder());
		fireTableDataChanged();
	}
	

	
	/**
	 * Fügt eine neue Anmerkung zur Datenbank hinzu
	 * @param anmerkung Anmerkung Objekt
	 */
	public void addAnmerkung(Anmerkung anmerkung) {
		dbHandlerAnmerkungen.createAnmerkung(anmerkung);
		fillList();
	}
	
	
	/**
	 * Aktualisiert eine bestehende Anmerkung in der Datenbank
	 * @param anmerkung Anmerkung Objekt
	 */
	public void updateAnmerkung(Anmerkung anmerkung) {
		dbHandlerAnmerkungen.updateAnmerkung(anmerkung);
		fillList();
	}
	
	
	/**
	 * Löscht eine Anmerkung in der Datenbank
 	 * @param a Anmerkung Objekt
	 */
	public void deleteAnmerkung(Anmerkung a) {
		dbHandlerAnmerkungen.deleteAnmerkung(a);
		fillList();
	}
	
	
	/**
	 * Liefert eine Anmerkung aus der Liste anhand der Anmerkung-ID
	 * @param anmerkungID Anmerkung-ID
	 * @return Annmerkung
	 */
	public Anmerkung getAnmerkung(int anmerkungID) {
		return listeAnmerkungen.get(anmerkungID);
		
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
		return listeAnmerkungen.size();
	}



	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		String value = "";
				
		if (columnIndex == 0)
			value = "" + DeutschesDatum.getFormatiertenZeitstempel(listeAnmerkungen.get(rowIndex).getZeitstempel());
		
		if (columnIndex == 1) {
			SemtixUser user = dbHandlerUser.readUser(listeAnmerkungen.get(rowIndex).getUserId());
			value = "" + user.getKuerzel();
		}
		
		if (columnIndex == 2)
			value = "" + listeAnmerkungen.get(rowIndex).getText();
		
		
		return value;
	}

}
