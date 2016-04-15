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
 * Enum mit Konstanten für Vorgangsarten.
 */
public enum Vorgangsart {

	NACHFRAGEBRIEF(1, "Nachfrage-Brief"),
	MAHNUNG(2, "Mahnung"),
	BEITRAG_NACHWEIS(3, "Beitragszahlung nachgewiesen"),
	ERSTRECHNUNG(4, "Erstrechnung"),
	ZWEITRECHNUNG(5, "Zweitrechnung"),
	ENTSCHEIDUNG(6, "Antrag entschieden"),
	BESCHEID_GEDRUCKT(7, "Bescheid gedruckt"),
	ZAHLUNGSAUFTRAG(8, "Zahlungsauftrag gedruckt"),
	NACHREICHUNG_EINGANG(9, "Nachreichung eingegangen"),
	NACHREICHUNG_GEPRUEFT(10, "Nachreichung geprüft");

	private static Map<Integer, Vorgangsart> idToVorgangsartMapping;
	private int index;
	private String text;
	
	
	Vorgangsart(int index, String text) {
		this.index = index;
		this.text = text;
	}
	
	
	
	// holt Vorgangsart anhand des index
	public static Vorgangsart getVorgangsartByID(int i) {
		if(idToVorgangsartMapping == null) {
			initMapping();
		}
		return idToVorgangsartMapping.get(i);
	}
	
	
	// schreibt Index und Vorgangsart in eine Map, damit auch über
	// den Index auf die Vorgansart zugegriffen werden kann
	private static void initMapping() {
		idToVorgangsartMapping = new HashMap<Integer, Vorgangsart>();
		for (Vorgangsart v : values()) {
			idToVorgangsartMapping.put(v.index, v);
		}
	}
	
	
	
	public int getIndex() {
		return index;
	}
	
	
	public String getText() {
		return text;
	}

	public String toString() {
		return text;
	}

}
