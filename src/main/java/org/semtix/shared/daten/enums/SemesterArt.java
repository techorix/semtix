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
 * Konstanten für die Semesterarten Sommer-Semester und Winter-Semester.
 */
public enum SemesterArt {
	
	SOMMER ("S", "Sommer-Semester", "SoSe"),
	WINTER ("W", "Winter-Semester", "WiSe");
	
	private String buchstabe, bezeichnung, kurzform;


	SemesterArt(String buchstabe, String bezeichnung, String kurzform) {
		
		this.buchstabe = buchstabe;
		this.bezeichnung = bezeichnung;
		this.kurzform = kurzform;
		
	}

	

	public String getBuchstabe() {
		return buchstabe;
	}


	public String getBezeichnung() {
		return bezeichnung;
	}


	public String getKurzform() {
		return kurzform;
	}
	
	
	public String toString() {
		return bezeichnung;
	}
	
}
