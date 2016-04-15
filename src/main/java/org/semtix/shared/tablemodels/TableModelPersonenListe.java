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

import org.semtix.db.dao.Person;
import org.semtix.gui.auszahlung.DialogArchivierung;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Datenklasse (TableModel) zum Dialog Datenabgleich (enthält die Daten, 
 * die im Dialog Datenabgleich angezeigt werden)
 */
@SuppressWarnings("serial")
public class TableModelPersonenListe extends AbstractTableModel {

	private final boolean editable;
	private String[] columnNames;
	private List<Person> personenListe;

	
	/**
	 * Erstellt ein neues TableModel
	 * @param personenListe Liste der Personen die die Tabelle darstellt
	 * @param editable soll die Tabelle editierbare Zellen haben (ja|nein)
	 */
	public TableModelPersonenListe(List<Person> personenListe, boolean editable) {

		this.editable = editable;

		this.personenListe = personenListe;

        Collections.sort(this.personenListe);

		columnNames = new String[]{"E-Mail", "Geburtstag", "Name", "Vorname", "Matrikelnummer"};

	}

	/**
	 * Hilfsmethode die die Spaltennamen annimmt, die wir in der fertigen Tabelle sehen wollen und die Namen zurückgibt, die wir dann entfernen sollten.
	 *
	 * @param columnsIWantToShow names from columnNames that we want to see in the tablemodel object
	 * @return array of column names we have to hide in order to only see the columns we want
	 */
	public String[] getColumnsIShouldHide(String[] columnsIWantToShow) {
		List<String> hidecolumns = new ArrayList<String>();
		List<String> showcolumns = Arrays.asList(columnsIWantToShow);
		for (String s : columnNames) {
			if (!showcolumns.contains(s))
				hidecolumns.add(s);
		}

		//nicht nötig die Größe des Rückgabearrays anzugeben
		return hidecolumns.toArray(new String[]{});
	}

	public void updatePersonenListe(List<Person> personenListe) {
		this.personenListe = personenListe;
		DialogArchivierung.setAnzahl(personenListe.size());
	}

	public List<Person> getPersonenListe() {
		return this.personenListe;
	}


	//wenn man die Daten editieren will muss das hier true sein
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return editable;
	}

	//wenn man die Daten editieren will
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

		if (columnIndex == 0)
			personenListe.get(rowIndex).setEmail((String) aValue);

		else if (columnIndex == 1) {

		}
		else if (columnIndex == 2)
			personenListe.get(rowIndex).setNachname((String) aValue);

		else if (columnIndex == 3)
			personenListe.get(rowIndex).setVorname((String) aValue);

		else if (columnIndex == 4)
			personenListe.get(rowIndex).setEmail((String) aValue);
	}


	/**
	 * Liefert Personobjekt mit gewünschter Personen-ID zurück
	 *
	 * @param personID Person-ID
	 * @return Person Objekt
	 */
	public Person getPerson(int personID) {
		return personenListe.get(personID);
	}

	public int getPersonID(int index) {
		return personenListe.get(index).getPersonID();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	public int getRowCount() {
		return personenListe.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {


        if (null != personenListe.get(rowIndex)) {
            if (columnIndex == 0) {
                if (null != personenListe.get(rowIndex).getEmail())
                    return personenListe.get(rowIndex).getEmail();
            } else if (columnIndex == 1) {
                if (null != personenListe.get(rowIndex).getGebdatum())
					return "";//Performance: DeutschesDatum.DATUMSFORMAT.format(personenListe.get(rowIndex).getGebdatum().getTime());
			} else if (columnIndex == 2) {
                if (null != personenListe.get(rowIndex).getNachname())
                    return personenListe.get(rowIndex).getNachname();
            } else if (columnIndex == 3) {
                if (null != personenListe.get(rowIndex).getVorname())
                    return personenListe.get(rowIndex).getVorname();

            } else if (columnIndex == 4)
                if (null != personenListe.get(rowIndex).getMatrikelnr())
                    return personenListe.get(rowIndex).getMatrikelnr();
        }
		return "";

	}

	/**
	 * Gets the index of the column
	 *
	 * @param name Name of Column you are looking for
	 * @return Index of Column or -1 if not found
	 */
	public int getColumnIndex(String name) {
		for (int i = 0; i < columnNames.length; i++) {
			if (columnNames[i].equals(name)) {
				return i;
			}
		}

		return -1;
	}

	public Person getPersonByRow(int rowIndex) {
		return personenListe.get(rowIndex);
	}
}
