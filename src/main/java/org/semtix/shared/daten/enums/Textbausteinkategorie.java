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

package org.semtix.shared.daten.enums;

/**
 * Konstanten für die Arten von Textbausteinen für die Nachfragebriefe. Die Konstanten beinhalten 
 * eine Bezeichnung für die Anzeige in der Anwendung und einen Index für die Speicherung in der 
 * Datenbank. 
 */
public enum Textbausteinkategorie {

    AUSGABEN("Ausgaben", 1), EINNAHMEN("Einnahmen", 2), FEHLENDE_ANGABEN("Fehlende Angaben", 3), HAERTE("Härte", 4),
	INFO("Info", 5), KOSTEN("Kosten", 6), SCHULDEN("Schulden", 7), SONSTIGES("Sonstiges", 8);

	private String name;
	private int index;


	/**
	 * Erstellt neue Konstante mit Bezeichnung und Index.
	 * @param name Bezeichnung der Konstanten
	 * @param index Index der Konstanten
	 */
    Textbausteinkategorie(String name, int index) {
        this.name = name;
		this.index = index;
	}
	
	
	/**
	 * Liefert Index der Konstanten.
	 * @return Index der Konstanten
	 */
	public int getIndex(){
		return index;
	}
	
	
	/**
	 * Liefert Bezeichnung der Konstanten.
	 * @return Bezeichnung der Konstanten
	 */
	public String getName(){
		return name;
	}
	

	/**
	 * Liefert Bezeichnung der Konstanten.
	 * @return Bezeichnung der Konstanten
	 */
	public String toString(){
		return name;
	}

}
