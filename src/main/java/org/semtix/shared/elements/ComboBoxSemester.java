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



package org.semtix.shared.elements;


import org.semtix.db.DBHandlerSemester;
import org.semtix.db.dao.Semester;
import org.semtix.shared.daten.enums.Uni;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
* Formularkomponente für eine ComboBox zur Anzeige der Semester, die in der Datenbank gespeichert sind.
* 
*/
@SuppressWarnings("serial")
public class ComboBoxSemester
extends JComboBox {
	
	private List<Semester> semesterListe;
	
	
	
	/**
	 * Erstellt eine ComboBox mit Semestereinträgen für eine bestimmte Universität.
	 * @param uni ausgewählte Universität
	 */
	public ComboBoxSemester(Uni uni) {
		
		semesterListe = new ArrayList<Semester>();
		
		fillList(uni);

	}
	
	
	
	/**
	 * Füllt die Liste mit Semestereinträgen für eine bestimmte Universität und übergibt der ComboBox das Model
	 * @param uni ausgewählte Universität
	 */
	public void fillList(Uni uni) {
		
		// DBHandler erstellen
		DBHandlerSemester dbHandlerSemester = new DBHandlerSemester();
		
		// Holt die Liste der Semester
		semesterListe = dbHandlerSemester.getSemesterListe(uni);
		
		if(semesterListe.size() > 0) {
			
			// Model erstellen
			DefaultComboBoxModel modelSemester = new DefaultComboBoxModel();
			for(Semester s : semesterListe)
				modelSemester.addElement(s);
			
			// Der ComboBox wird das Model zugewiesen
			setModel(modelSemester);			
	
		}
		
	}
	
	
	/**
	 * Liefert die Liste mit den Semestereinträgen.
	 * @return Semester-Liste
	 */
	public List<Semester> getSemesterListe() {
		return semesterListe;
	}

}
