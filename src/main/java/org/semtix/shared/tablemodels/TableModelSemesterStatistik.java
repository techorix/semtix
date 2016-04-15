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

import org.semtix.db.DBHandlerSemester;
import org.semtix.db.dao.Semester;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;


/**
 * TableModel enthält Daten der Semester und zeigt diese in einer Tabelle an.
 * @see org.semtix.gui.admin.SemesterPanel
 */
@SuppressWarnings("serial")
public class TableModelSemesterStatistik
extends AbstractTableModel {


	// Überschriften der Tabellenspalten
	private String[] columnNames;
	
	// Liste mit Daten der Semester
	private List<Semester> semesterListe;
	
	
	/**
	 * Erstellt neues TableModel mit Daten der Semester
	 */
	public TableModelSemesterStatistik() {
		
		// Liste der Semester anlegen
		semesterListe = new ArrayList<Semester>();
		
		// Liste mit Daten füllen
		fillList();
		
		// Überschriften für Tabellenspalten festlegen
		columnNames = new String[] {"Semesterart", "Jahr", "Universität"};
	

	}
	
	
	
	/**
	 * Liefert Semesterobjekt mit gewünschter Semester-ID zurück
	 * @param semesterID Semester-ID
	 * @return Objekt mit Semesterdaten
	 */
	public Semester getSemester(int semesterID) {
		return semesterListe.get(semesterID);
	}
	
	
	/**
	 * Füllt Liste mit aktuellen Semesterdaten
	 */
	public void fillList() {
		DBHandlerSemester dbHandlerSemester = new DBHandlerSemester();

		semesterListe = dbHandlerSemester.getSemesterListe();

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
		return semesterListe.size();
	}


	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		String value = "";
		
		if (columnIndex == 0)
			value = semesterListe.get(rowIndex).getSemesterKurzform();
		
		if (columnIndex == 1)
			value = "" + semesterListe.get(rowIndex).getSemesterJahr();
		
		if (columnIndex == 2)
			value = "" + semesterListe.get(rowIndex).getUni().getID();


		return value;
	}
	
	
	
	
}
