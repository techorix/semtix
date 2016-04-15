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

import java.util.HashMap;
import java.util.Map;

/**
 * Konstanten für Semester-Status der Semtix-Büromitarbeiter_innen (User).
 * (fällt evtl. weg, falls nicht mehr nach lokalen und globalen Arbeitssemestern unterschieden werden soll)
 */
public enum UserSemesterStatus {
	
	/**
	 * Lokales Arbeitssemester
	 */
	LOKAL (1, "lokal"),
	/**
	 * Globales Arbeitssemester
	 */
	GLOBAL (2, "global");
	
	// Mapping von Status-ID und Semester-Status, um über die ID auf den Status zuzugreifen
	private static Map<Integer, UserSemesterStatus> idToStatusMapping;
	private String statusText;
	private int statusID;


	UserSemesterStatus(int statusID, String statusText) {
		
		this.statusID = statusID;
		this.statusText = statusText;

	}
	
	
	/**
	 * Liefert den Semester-Status anhand der Status-ID 
	 * @param i Status-ID
	 * @return Semester-Status
	 */
	public static UserSemesterStatus getStatus(int i) {
		if(idToStatusMapping == null) {
			initMapping();
		}
		return idToStatusMapping.get(i);
	}
	
	

	/**
	 * Schreibt Status-ID und Semester-Status in eine Map, damit auch über 
	 * die ID auf den Semester-Status zugegriffen werden kann.
	 */
	private static void initMapping() {
		idToStatusMapping = new HashMap<Integer, UserSemesterStatus>();
		for (UserSemesterStatus u : values()) {
			idToStatusMapping.put(u.statusID, u);
		}
	}
	
	
	/**
	 * Liefert die Status-ID zum Semester-Status.
	 * @return Status-ID
	 */
	public int getID() {
		return statusID;
	}

	
	/**
	 * Liefert den Text des Semester-Status.
	 */
	public String toString() {
		return statusText;
	}


}
