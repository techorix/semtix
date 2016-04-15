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

import org.semtix.db.DBHandlerSemester;
import org.semtix.db.dao.Person;
import org.semtix.db.dao.Semester;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Model-Klasse enthält die Semestern für die ComboBox im DialogAntragVorhanden.
 *
 */
@SuppressWarnings("serial")
public class SemesterComboModel
extends DefaultComboBoxModel {
	
	
	/**
	 * Erstellt ein neues SemesterComboModel
	 * @param person Person
	 */
	public SemesterComboModel(Person person) {

		DBHandlerSemester dbHandlerSemester = new DBHandlerSemester();
		
		List<Semester> semesterListe = new ArrayList<Semester>();
		
		// Holt die Liste der Semester für eine bestimmte Universität
		semesterListe = dbHandlerSemester.getSemesterListe(person);
		
		for(Semester s : semesterListe)
			addElement(s);
		
	}

}
