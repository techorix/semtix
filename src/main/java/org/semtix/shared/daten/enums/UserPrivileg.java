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
 * Konstanten für Privilegien der Semtix-Büromitarbeiter_innen (User).
 * Für die Benutzung der Semtix-Anwendung sind verschiedene Privilegien festgelgt. Innerhalb der 
 * Datenbank werden diese Privilegien mit ihrer ID-Nummer (1 für User, 2 für Finanz und 
 * 3 für Admin) gespeichert.
 * 
 * <ul><li>User: normaler User</li>
 * <li>Finanz: Büromitarbeiter_in im Bereich Finanzen</li>
 * <li>Admin: Büromitarbeiter_in mit Adminrechten</li></ul>
 */
public enum UserPrivileg {
	
	/**
	 * normaler User (Status-ID: 1)
	 */
	USER (1, "User"),
	/**
	 * Büromitarbeiter_in im Bereich Finanzen (Status-ID: 2)
	 */
    FINANZ (2, "Finanz"),
    /**
	 * Büromitarbeiter_in mit Adminrechten (Status-ID: 3)
	 */
	ADMIN (3, "Admin");


	// Mapping von Privileg-ID und User-Privileg, um über die ID auf den User-Privileg zuzugreifen
	private static Map<Integer, UserPrivileg> idToPrivilegMapping;
	private int privilegID;
	private String privilegText;


	UserPrivileg(int privilegID, String privilegText) {
		
		this.privilegID = privilegID;
		this.privilegText = privilegText;

	}
	
	

	/**
	 * Liefert den User-Privileg anhand der Privileg-ID 
	 * @param i Privileg-ID
	 * @return User-Privileg
	 */
	public static UserPrivileg getPrivileg(int i) {
		if(idToPrivilegMapping == null) {
			initMapping();
		}
		return idToPrivilegMapping.get(i);
	}
	
	

	/**
	 * Schreibt Privileg-ID und User-Privileg in eine Map, damit auch über 
	 * die ID auf den User-Privileg zugegriffen werden kann.
	 */
	private static void initMapping() {
		idToPrivilegMapping = new HashMap<Integer, UserPrivileg>();
		for (UserPrivileg u : values()) {
			idToPrivilegMapping.put(u.privilegID, u);
		}
	}
	
	
	/**
	 * Liefert die Privileg-ID zum User-Privileg.
	 * @return Privileg-ID
	 */
	public int getID() {
		return privilegID;
	}
	
	
	/**
	 * Liefert den Text des User-Privileg.
	 */
	public String toString() {
		return privilegText;
	}

}
