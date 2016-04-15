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
 * Konstanten zur Unterscheidung der Universitäten.
 * Vorerst sind dies nur die Humbold-Universität und die Kunsthochschule Weißensee. Innerhalb der 
 * Datenbank werden die Universitäten mit ihrer ID-Nummer (1 für Humboldt-Uni und 2 für 
 * Kunschhochschule Weißensee) gespeichert.
 */
public enum Uni {
	
	/**
	 * Humboldt-Universität (ID: 1)
	 */
	HU (1, "Humboldt-Universität"),
	/**
	 * Kunsthochschule Weißensee (ID: 2)
	 */
    KW (2, "Kunsthochschule Weißensee");


	// Mapping von Uni-ID und Uni, um über die ID auf die Uni zuzugreifen
	private static Map<Integer, Uni> idToUniMapping;
    private int universitaetID;
    private String universitaetName;


	Uni(int universitaetID, String universitaetName) {
		
		this.universitaetID = universitaetID;
		this.universitaetName = universitaetName;

	}
	
	
	/**
	 * Liefert die Uni anhand der Uni-ID 
	 * @param i Uni-ID
	 * @return Uni
	 */
	public static Uni getUniByID(int i) {
		if(idToUniMapping == null) {
			initMapping();
		}
		return idToUniMapping.get(i);
	}
	
	

	/**
	 * Schreibt Uni-ID und Uni in eine Map, damit auch über 
	 * die ID auf die Uni zugegriffen werden kann.
	 */
	private static void initMapping() {
		idToUniMapping = new HashMap<Integer, Uni>();
		for (Uni u : values()) {
			idToUniMapping.put(u.universitaetID, u);
		}
	}
	
	
	
	/**
	 * Liefert die Uni-ID zur Uni.
	 * @return Uni-ID
	 */
	public int getID() {
		return universitaetID;
	}
	
	
	/**
	 * Liefert den Text der Uni.
     * @return Name der Uni
	 */
	public String getUniName() {
		return universitaetName;
	}
	
	
	/**
	 * Liefert den Text der Uni (doppelte Methode - kann evtl. weg...)
	 * @return Name der Uni {see #getUniName()}
	 */
	public String toString() {
		return universitaetName;
	}


}
