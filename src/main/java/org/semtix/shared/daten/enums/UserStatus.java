


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
 * Konstanten für Status der Semtix-Büromitarbeiter_innen (User). Innerhalb der 
 * Datenbank wird der User-Status mit der ID-Nummer (1 für aktiv und 2 für
 * passiv) gespeichert.
 * 
 * <ul><li>aktiv</li>
 * <li>passiv</li></ul>
 */
public enum UserStatus {

	/**
	 * User ist aktiv
	 */
	AKTIV (1, "aktiv"),
	/**
	 * User ist passiv
	 */
	PASSIV (2, "passiv");
	
	// Mapping von Status-ID und User-Status, um über die ID auf den User-Status zuzugreifen
	private static Map<Integer, UserStatus> idToStatusMapping;
	private String statusText;
	private int statusID;


	UserStatus(int statusID, String statusText) {
		
		this.statusID = statusID;
		this.statusText = statusText;

	}
	
	
	/**
	 * Liefert den User-Status anhand der Status-ID 
	 * @param i Status-ID
	 * @return User-Status
	 */
	public static UserStatus getStatus(int i) {
		if(idToStatusMapping == null) {
			initMapping();
		}
		return idToStatusMapping.get(i);
	}
	

	/**
	 * Schreibt Status-ID und User-Status in eine Map, damit auch über 
	 * die ID auf den User-Status zugegriffen werden kann.
	 */
	private static void initMapping() {
		idToStatusMapping = new HashMap<Integer, UserStatus>();
		for (UserStatus u : values()) {
			idToStatusMapping.put(u.statusID, u);
		}
	}
	
	
	/**
	 * Liefert die Status-ID zum User-Status.
	 * @return Status-ID
	 */
	public int getID() {
		return statusID;
	}

	
	/**
	 * Liefert den Text des User-Status.
	 */
	public String toString() {
		return statusText;
	}

}
