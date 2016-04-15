


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

import org.semtix.config.UniConf;
import org.semtix.db.DBHandlerSemester;
import org.semtix.db.dao.Semester;

import javax.swing.table.AbstractTableModel;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * TableModel mit Daten zu Semestern und deren Werte der Zuschussberechnungen. Die Tabelle dazu 
 * wird im Auszahlungsmodul im PenalStep1 angezeigt.
 */
@SuppressWarnings("serial")
public class TableModelSemester
extends AbstractTableModel {
	
	private String[] columnNames;
	private List<Semester> semesterListe;
	
	
	/**
	 * Erstellt ein TableModel
	 */
	public TableModelSemester() {
		
		semesterListe = new ArrayList<Semester>();
		fillList();
		
		// Spaltennamen der Tabelle
		columnNames = new String[] {"Semester", "Fonds", "Stichtag", "Vollzuschuss ab", "Punktwert"};
	

	}
	
	
	
	/**
	 * Liefert Semesterobjekt von gewünschter Semester-ID zurück.
	 * PROBLEM: Welche Zahl wird verwendet? semesterID oder row??!!
	 * @param semesterID Semester-ID
	 * @return Semester
	 */
	public Semester getSemester(int semesterID) {
		return semesterListe.get(semesterID);
	}
	
	
	/**
	 * Liste mit Semesterdaten für bestimmte Uni füllen und Tabellenansicht aktualisieren
	 */
	public void fillList() {
		
		DBHandlerSemester dbHandlerSemester = new DBHandlerSemester();
		semesterListe = dbHandlerSemester.getSemesterListe(UniConf.aktuelleUni);
		this.fireTableDataChanged();
	}


	public Semester getSemesterByRow(int selectedRow) {
		return semesterListe.get(selectedRow);
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
		
		
		if (columnIndex == 1){
			if(semesterListe.get(rowIndex).getBeitragFonds() == null)
				value = "-";
			else{
				NumberFormat currencyFormat = DecimalFormat.getCurrencyInstance();
				value = currencyFormat.format(semesterListe.get(rowIndex).getBeitragFonds());
			}
		}
			
		
		if (columnIndex == 2){
			if(semesterListe.get(rowIndex).getStichtag() == null)
				value = "-";
			else {
				SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
				value = df.format(semesterListe.get(rowIndex).getStichtag().getTime());
			}
				
		}
			
		
		if (columnIndex == 3){
			if(semesterListe.get(rowIndex).getPunkteVoll() == 0)
				value = "-";
			else
				value = "" + semesterListe.get(rowIndex).getPunkteVoll() + " P.";			
		}
			
		
		
		if (columnIndex == 4){
			if(semesterListe.get(rowIndex).getPunktWert() == null)
				value = "-";
			else{
				NumberFormat currencyFormat = DecimalFormat.getCurrencyInstance();
				value = currencyFormat.format(semesterListe.get(rowIndex).getPunktWert());
			}
		}



		return value;
	}


}
