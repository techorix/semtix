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

import org.semtix.config.SemesterConf;
import org.semtix.db.DBHandlerAntrag;
import org.semtix.db.DBHandlerPerson;
import org.semtix.db.DBHandlerSemester;
import org.semtix.db.dao.Antrag;
import org.semtix.db.dao.Person;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Model-Klasse enthält die Daten für die Tabelle AntragUebersicht im Personenformular
 */
@SuppressWarnings("serial")
public class TableModelAntragUebersicht
        extends AbstractTableModel {

    private String[] columnNames;

    private List<Antrag> antragListe;

	private boolean istDruckDialog = false;

    /**
     * Erstellt ein neues TableModel
     */
    public TableModelAntragUebersicht() {

        antragListe = new ArrayList<Antrag>();

        columnNames = new String[]{"Semester", "Status", "Punkte"};


    }

	public TableModelAntragUebersicht(List<Antrag> antragListe) {

		this.antragListe = antragListe;

        columnNames = new String[]{"Name", "MaNr"};

		istDruckDialog = true;
	}


    /**
     * Liefert Antrag-ID aus Übersichtstabelle zurück
     *
     * @param row Zeile in Übersichtstabelle
     * @return Antrag-ID
     */
    public int getAntragID(int row) {
        return antragListe.get(row).getAntragID();
    }


    /**
     * Füllt die Liste mit der Antragsübersicht für eine bestimmte Person
     * und aktualisiert die Ansicht der Tabelle
     *
     * @param personID ID
     */
    public void fillList(int personID) {
        DBHandlerAntrag dbHandlerAntraege = new DBHandlerAntrag();

        antragListe = dbHandlerAntraege.getAntragListe(personID);

        Collections.sort(antragListe, new Comparator<Antrag>() {
            @Override
            public int compare(Antrag o1, Antrag o2) {
                DBHandlerSemester dbHandlerSemester = new DBHandlerSemester();
                if (o1.getSemesterID() == o2.getSemesterID())
                    return 0;
                else
                    return dbHandlerSemester.getSemesterByID(o2.getSemesterID()).compareTo(dbHandlerSemester.getSemesterByID(o1.getSemesterID()));
            }
        });

        this.fireTableDataChanged();
    }


    /**
     * Liefert die Liste mit der Antragsübersicht
     *
     * @return Liste Antragsübersicht
     */
    public List<Antrag> getList() {

        return antragListe;

    }


    /**
     * Liefert Antrag-ID von Antrag für aktuelles Semester
     * (Wenn nicht in Liste oder Liste leer, dann Rückgabewert -1)
     *
     * @return Antrag-ID von Antrag für aktuelles Semester
     */
    public int getAktuelleAntragID() {

        int antragID = -1;

        if (antragListe.size() > 0) {

            for (Antrag a : antragListe) {
                if (a.getSemesterID() == SemesterConf.getSemester().getSemesterID())
                    antragID = a.getAntragID();
            }

        }

        return antragID;

    }


    /**
     * Liefert Zeilennummer der Antragübersichts-Tabelle, falls ein Antrag für das aktuelle Semester exisitert.
     *
     * @return Zeilennummer
     */
    public int getRowAktuellerAntrag() {

        int row = -1;

        if (antragListe.size() > 0) {

            for (Antrag a : antragListe) {
                if (a.getSemesterID() == SemesterConf.getSemester().getSemesterID())
                    row = antragListe.indexOf(a);
            }


        }

        return row;
    }


    // Methoden des TableModels

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public int getRowCount() {
        return antragListe.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Person p = null;
		if (istDruckDialog) {
			p = new DBHandlerPerson().getPersonById(antragListe.get(rowIndex).getPersonID());
        }

		if (columnIndex == 0 && istDruckDialog) {
			return p.getNachname() + ", " + p.getVorname();
		} else if (columnIndex == 0) {
			try {
				return new DBHandlerSemester().getSemesterByID(antragListe.get(rowIndex).getSemesterID()).getSemesterKurzform();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Fehler: Bitte überprüfen ob bereits ein (globales) Semester eingestellt wurde.");
			}
		}

		if (columnIndex == 1 && istDruckDialog) {
			return p.getMatrikelnr();
        } else if (columnIndex == 1) {
			return antragListe.get(rowIndex).getAntragStatus().toString();
		}

		if (columnIndex == 2 && !istDruckDialog)
			return (antragListe.get(rowIndex).getPunkteHaerte() + antragListe.get(rowIndex).getPunkteEinkommen());


		return "";

    }

}
