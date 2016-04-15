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

package org.semtix.shared.tablemodels;

import org.semtix.config.SemesterConf;
import org.semtix.db.DBHandlerAntrag;
import org.semtix.db.DBHandlerPerson;
import org.semtix.db.dao.Antrag;
import org.semtix.db.dao.Person;
import org.semtix.shared.daten.StringHelper;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 * TableModel für die Tabelle mit der Buchstaben-Verteilung zum Semester
 */
public class TableModelBuchstaben
        extends AbstractTableModel {

    private String[] columnNames;
    private List<String> initial;
    private HashMap<String, Integer> letterListe;


    /**
     * Erstellt ein neues TableModel
     */
    public TableModelBuchstaben() {

        columnNames = new String[]{"Buchstabe", "Anzahl"};

        updateBuchstaben(SemesterConf.getSemester().getSemesterID());

    }


    /**
     * Aktualisiert die Buchstabenverteilung zum Semester nach Semester-ID
     *
     * @param semesterID Semester-ID
     */
    public void updateBuchstaben(int semesterID) {

        DBHandlerAntrag dbHandlerAntrag = new DBHandlerAntrag();
        DBHandlerPerson dbHandlerPerson = new DBHandlerPerson();
        letterListe = new HashMap<String, Integer>();

        for (Antrag a : dbHandlerAntrag.getAntragListeSemester(semesterID)) {
            Person p = dbHandlerPerson.getPersonById(a.getPersonID());
            if (null != p) {
                String letter = StringHelper.removeDiacriticalMarks(p.getNachname().trim().substring(0, 1).toUpperCase());
                Integer anzahl = letterListe.get(letter);
                if (null == anzahl) {
                    anzahl = 1;
                } else {
                    anzahl++;
                }
                letterListe.put(letter, anzahl);
            }
        }

        this.initial = new ArrayList<String>(new TreeSet<String>(letterListe.keySet()));

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
        return initial.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        String s = initial.get(rowIndex);

        if (columnIndex == 0)
            return s;

        else if (columnIndex == 1)
            return "" + letterListe.get(s);

        else
            return null;
    }


}
