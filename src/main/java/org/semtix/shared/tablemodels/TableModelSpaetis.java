/*
 *
 *  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 *         Semesterticketbüro der Humboldt-Universität Berlin
 *
 *  Copyright (c) 2015-2016 Michael Mertins (MichaelMertins@gmail.com)
 *  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)
 *
 *    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.semtix.shared.tablemodels;


import org.semtix.db.DBHandlerAntrag;
import org.semtix.db.DBHandlerPerson;
import org.semtix.db.dao.Antrag;
import org.semtix.db.dao.Person;
import org.semtix.db.dao.Semester;

import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.List;

/**
 * TableModel für Spätauszahler
 */
public class TableModelSpaetis extends AbstractTableModel {

    private String[] columnNames;
    private List<Person> personenListe;
    private List<Antrag> antragList;
    private HashMap<Integer, Antrag> antragMap = new HashMap<>();

    /**
     *
     * Erstellt ein neues TableModel
     *
     * @param semester  Semester Objekt
     */
    // enthält die Daten, die im Dialog Datenabgleich angezeigt werden
    public TableModelSpaetis(Semester semester) {


		antragList = new DBHandlerAntrag().getListNochNichtAusgezahlt(semester);

        for (Antrag a : antragList) {
            antragMap.put(a.getPersonID(), a);
        }

        personenListe = new DBHandlerPerson().getPersonsForAntraege(antragMap.keySet());


        // Spaltennamen für die Tabelle
        columnNames = new String[]{"Name", "Vorname", "Matrikelnummer", "Status"};

    }


    /**
     * Liefert Personobjekt mit gewünschter Personen-ID zurück
     *
     * @param personID Person-ID
     * @return Person
     */
    public Person getPerson(int personID) {
        return personenListe.get(personID);
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
        if (columnIndex == 0) {
            return personenListe.get(rowIndex).getVorname();
        } else if (columnIndex == 1) {
            return personenListe.get(rowIndex).getNachname();
        } else if (columnIndex == 2) {
            return personenListe.get(rowIndex).getMatrikelnr();
        } else if (columnIndex == 3) {
            StringBuilder sb = new StringBuilder("<html>");
            Antrag a = antragMap.get(personenListe.get(rowIndex).getPersonID());
            if (a.isNothilfe()) sb.append("<font color='red'>Nothilfe</font>");
            if (a.isErstsemester()) sb.append("<font color='green'>Ersti</font>");
            if (a.isRaten()) sb.append("<font color='blue'>IndvdZhl</font>");
            sb.append("</html>");
            return sb.toString();
        }
        return "";
    }

}
