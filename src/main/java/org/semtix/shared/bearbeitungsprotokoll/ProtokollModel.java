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

package org.semtix.shared.bearbeitungsprotokoll;

import org.semtix.db.*;
import org.semtix.db.dao.Antrag;
import org.semtix.db.dao.Person;
import org.semtix.db.dao.Semester;
import org.semtix.db.dao.Vorgang;

import java.util.List;
import java.util.Observable;

/**
 * Datenklasse für die Werte im Bearbeitungsprotokoll.
 *
 */
public class ProtokollModel
extends Observable {
	
	private Person person;
	private Antrag antrag;
	private Semester semester;

	private List<Vorgang> vorgaengeListe;
	
	
	
	/**
	 * Erstellt eine Datenklasse fürs Bearbeitungsprotokoll.
	 */
	public ProtokollModel() {
		
		
		
		
	}
	
	
	/**
	 * Setzt die Antrags-ID des betreffenden Antrags, füllt die Liste mit den Vorgängen und 
	 * aktualisiert die View (Obsever).
	 * @param antragID ID des betreffenden Antrags
	 */
	public void setAntragsID(int antragID) {
		
		// DBHandler für Datenbankabfragen erstellen
		DBHandlerVorgaenge dbHandlerVorgaenge = new DBHandlerVorgaenge();
		DBHandlerAntrag dbHandlerAntrag = new DBHandlerAntrag();
		DBHandlerPerson dbHandlerPerson = new DBHandlerPerson();
		DBHandlerSemester dbHandlerSemester = new DBHandlerSemester();
		
		// Liste mit eingetragenen Vorgängen erstellen
		vorgaengeListe = dbHandlerVorgaenge.getVorgaengeListe(antragID);

		
		// Antrag zu Antrag-ID holen
		antrag = dbHandlerAntrag.readAntrag(antragID);
		
		// Personobjekt zu Antrag holen
		person = dbHandlerPerson.readPerson(antrag.getPersonID());
		
		// Semesterobjekt zu Antrag holen
		semester = dbHandlerSemester.readSemester(antrag.getSemesterID());


        // Viwe aktualisieren (Observer)
		updateView();

	}
	
	
	
	public String getName() {
		return person.toString();
	}
	
	
	public String getMatrikelnummer() {
		return person.getMatrikelnr();
	}
	
	
	public String getSemester() {
		return semester.getSemesterKurzform();
	}
	
	
	public String getUserAngelegt() {
		
		DBHandlerUser dbHandlerUser = new DBHandlerUser();
		
		return dbHandlerUser.readUser(antrag.getUserAngelegt()).getKuerzel();
		
	}
	
	
	public String getUserGeaendert() {
		
		DBHandlerUser dbHandlerUser = new DBHandlerUser();
		
		return dbHandlerUser.readUser(antrag.getUserGeaendert()).getKuerzel();
		
	}
	
	
	
	public Antrag getAntrag() {
		return antrag;
	}
	
	
	
	

	/**
	 * Formular mit Bearbeitungsprotokoll über Observer aktualisieren
	 */
	private void updateView() {
		
		setChanged();
		notifyObservers(vorgaengeListe);
		
	}
	

}
