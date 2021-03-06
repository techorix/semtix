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

package org.semtix.gui.tabs.antrag;

/**
 * Klasse zur Speicherung der Antrag-ID und des Anfangsbuchstabens des Nachnamen.
 * Wird benötigt zur Anzeige von Anträgen und Navigation im BlätterPanel.
 *
 */
public class AntragIndex implements Comparable {
	
	private int antragID;
	private char capital;
	private int personID;
	
	
	/**
	 * Erstellt einen neuen AntragIndex
	 * @param antragID ID des Antrgas
	 * @param capital Anfangsbuchstabe (Nachname der Person)
	 *                @param personID Persson ID
	 */
	public AntragIndex(int antragID, char capital, int personID) {
		this.antragID = antragID;
		this.setPersonID(personID);
		this.capital = capital;
	}

	
	/**
	 * Liefert die AntragID
	 * @return AntragID
	 */
	public int getAntragID() {
		return antragID;
	}


	/**
	 * Setzt die AntraǵID
	 * @param antragID AntragID
	 */
	public void setAntragID(int antragID) {
		this.antragID = antragID;
	}


	/**
	 * Liefert den Anfangsbuchstaben (Nachname der Person)
	 * @return Anfangsbuchstaben
	 */
	public char getCapital() {
		return capital;
	}



	/**
	 * Setzt den Anfangsbuchstaben (Nachname der Person)
	 * @param capital Anfangsbuchstaben
	 */
	public void setCapital(char capital) {
		this.capital = capital;
	}


	@Override
	public int compareTo(Object o) {
		return Character.compare(getCapital(), ((AntragIndex) o).getCapital());
	}

	@Override
	public boolean equals(Object o) {
		return (this.antragID == ((AntragIndex) o).getAntragID());

	}

	public int getPersonID() {
		return personID;
	}

	public void setPersonID(int personID) {
		this.personID = personID;
	}
}
