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
 * Alle Härten und deren jeweiliger Anerkennungsgrund
 * <br>
 * Die Härte mit der größten ID muss immer die SONSTIGE Härte sein
 */
public enum Haertegrund {

	STUDIENABSCHLUSS (1, "Studienabschluss","Studienabschlussphase (§ 2 Abs. 2 Nr.1)"),
    PRAKTIKUM (2, "Praktikum","Ableistung eines Praktikums (§ 2 Abs. 2 Nr. 2)"),
	ARBEITSERLAUBNIS (3, "Arbeitserlaubnis","Einschränkung der Arbeitserlaubnis (§ 2 Abs. 2 Nr. 3)"),
    MEDVERSORGUNG (4, "Mediz. Versorgung","Kosten für medizinische oder psychologische Versorgung (§ 2 Abs. 2 Nr. 8)"),
	SCHWANGERSCHAFT (5, "Schwangerschaft","Schwangerschaft (Personengruppe des § 30 SGB XII, § 2 Abs. 2 Nr. 4 )"),
    ALLEINERZIEHEND (6, "Alleinerziehung","Alleinerziehung (Personengruppe des § 30 SGB XII, § 2 Abs. 2 Nr. 4 )"),
	KIND (7, "Kind","Erziehung eines/einer Haushaltsangehörigen unter 18 Jahren (§ 2 Abs. 2 Nr. 6)"),
	SGB (8,"SGB II/XII","Erhalt von Leistungen nach SGB II oder XII (Personengruppe des § 30 SGB XII, § 2 Abs. 2 Nr. 4)"),
	BEH_CHRONKRANK (9, "Beh./Chron. krank","Behinderung/Chronische Krankheit (§ 2 Abs. 2 Nr. 5)"),
	PFLEGE (10, "Pflege Angehörige","Betreuung von pflegebedürftigen Angehörigen (§ 2 Abs. 2 Nr. 7)"),
    SONSTIGE(11, "Sonstige Härte", "Sonstige im Antrag angegebene Situation"); //SONSTIGE MUSS LETZTER GRUND SEIN


    private static Map<Integer, Haertegrund> idToHaertegrundMapping;
    private int haertegrundID;
	private String haertegrundName;
    private String haertegrundText;


	Haertegrund(int haertegrundID, String haertegrundName, String haertegrundText) {
		
		this.haertegrundID = haertegrundID;
		this.haertegrundName = haertegrundName;
        this.haertegrundText = haertegrundText;

	}


    // holt Härtegrund anhand der ID; bei größerer ID gibt SONSTIGE zurück
    public static Haertegrund getHaertegrundByID(int i) {
        if (i > 11)
            return SONSTIGE;

        if (idToHaertegrundMapping == null) {
            initMapping();
		}

        return idToHaertegrundMapping.get(i);
	}
	
	
	// schreibt ID und Härtegrund in eine Map, damit auch über
	// die ID auf den Härtegrund zugegriffen werden kann
	private static void initMapping() {
		idToHaertegrundMapping = new HashMap<Integer, Haertegrund>();
		for (Haertegrund h : values()) {
			idToHaertegrundMapping.put(h.haertegrundID, h);
		}
	}

	
	public int getID() {
		return haertegrundID;
	}
	
	public String getName() {
		return haertegrundName;
	}

    public String getHaertegrundText() { return haertegrundText; }

    public String toString(){
        return haertegrundName;
    }
}
