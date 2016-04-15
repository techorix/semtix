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

package org.semtix.db.dao;


import org.semtix.shared.daten.enums.Haertegrund;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Klasse für Objekte von Antraghärten (entspricht der Datenbanktabelle antraghaerte).
 * 
 * <p>Für jeden Antrag können Härten angegeben, anerkannt oder abgelehnt sein. Für jeden 
 * Härtegrund kann eine AntragHaerte angelegt werden. (siehe Enum {@link org.semtix.shared.daten.enums.Haertegrund})</p>
 */
@Entity
public class AntragHaerte implements Serializable {

    @Id
    @SequenceGenerator(name = "antragHaerteID_seq",
            sequenceName = "antragHaerteID_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "antragHaerteID_seq")
    private int antragHaerteID;			// ID für die Antraghärte (Primärschlüssel in Datenbank)

    private int antragID;                // ID für Antrag, zu welchem die Antraghärte gehört

    private int ablehnungsID;            // ID des Ablehnungsgrunds


	private boolean angegeben;			// ist die Antraghärte angegeben
	private boolean anerkannt;			// ist die Antraghärte anerkannt
	private boolean abgelehnt;			// ist die Antraghärte abgelehnt

    //Sonstige Härten können einen eigenen Text,Namen und eine eigene Punktzahl haben
    private String customText;
    private String customName;
    private int customPoints;

    @Enumerated
    private Haertegrund haertegrund;	// Konstante (Enum) Härtegrund
	
	
	/**
	 * Konstruktor mit Standardwerten
	 */
	public AntragHaerte() {

		
	}

    /**
     * Konstruktor mit Feldwerten als Parameter
     *
     * @param antragHaerteID ID
     * @param antragID ID
     * @param haertegrund Haertegrund der Härte
     * @param angegeben Härte angegeben ja/nein
     * @param anerkannt Härte anerkannt ja/nein
     * @param abgelehnt Härte abgelehnt ja/nein
     * @param ablehnungsGrundID ID des Ablehnungsgrundes (enum)
     * @param customName eigener Name für die Antraghärte (bei Sonstigen Härten wichtig)
     * @param customText eigener Text für die Antraghärte
     * @param customPoints eigene Punktzahl für Antraghärte (bei Sonstigen Härten wichtig)
     */
	public AntragHaerte(int antragHaerteID, int antragID, Haertegrund haertegrund,
                        boolean angegeben, boolean anerkannt, boolean abgelehnt, int ablehnungsGrundID, String customName, String customText, int customPoints) {

        this.antragHaerteID = antragHaerteID;
		this.antragID = antragID;
		this.haertegrund = haertegrund;
		this.angegeben = angegeben;
		this.anerkannt = anerkannt;
		this.abgelehnt = abgelehnt;
		this.ablehnungsID = ablehnungsGrundID;
        this.customName = customName;
        this.customText = customText;
        this.customPoints = customPoints;
    }


	/**
	 * Liefert die ID der Antraghärte (Primärschlüssel in Datenbank)
	 * @return ID der Antraghärte
	 */
	public int getAntragHaerteID() {
		return antragHaerteID;
	}


	/**
	 * Setzt die ID der Antraghärte (Primärschlüssel in Datenbank)
	 * @param antragHaerteID ID der Antraghärte
	 */
	public void setAntragHaerteID(int antragHaerteID) {
		this.antragHaerteID = antragHaerteID;
	}
	
	
	/**
	 * Liefert die ID für Antrag, zu welchem die Antraghärte gehört
	 * @return Antrag-ID
	 */
	public int getAntragID() {
		return antragID;
	}


	/**
	 * Setzt die ID für Antrag, zu welchem die Antraghärte gehört
	 * @param antragID Antrag-ID
	 */
	public void setAntragID(int antragID) {
		this.antragID = antragID;
	}
	
	
	/**
	 * Liefert den Härtegrund (siehe Enum {@link org.semtix.shared.daten.enums.Haertegrund})
	 * @return Härtegrund
	 */
	public Haertegrund getHaertegrund() {
		return haertegrund;
	}


	/**
	 * Setzt den Härtegrund (siehe Enum {@link org.semtix.shared.daten.enums.Haertegrund})
	 * @param haertegrund Härtegrund
	 */
	public void setHaertegrund(Haertegrund haertegrund) {
		this.haertegrund = haertegrund;
	}


	/**
	 * Liefert ob der Härtegrund angegeben ist
	 * @return angegeben ja/nein
	 */
	public boolean isAngegeben() {
		return angegeben;
	}


	/**
	 * Setzt ob der Härtegrund angegeben ist
	 * @param angegeben angegeben ja/nein
	 */
	public void setAngegeben(boolean angegeben) {
		this.angegeben = angegeben;
	}


	/**
	 * Liefert ob der Härtegrund anerkannt ist
	 * @return anerkannt ja/nein
	 */
	public boolean isAnerkannt() {
		return anerkannt;
	}


	/**
	 * Setzt ob der Härtegrund anerkannt ist
	 * @param anerkannt anerkannt ja/nein
	 */
	public void setAnerkannt(boolean anerkannt) {
		this.anerkannt = anerkannt;
	}
	
	
	/**
	 * Liefert ob der Härtegrund abgelehnt ist
	 * @return abgelehnt ja/nein
	 */
	public boolean isAbgelehnt() {
		return abgelehnt;
	}


	/**
	 * Setzt ob der Härtegrund abgelehnt ist
	 * @param abgelehnt abgelehnt ja/nein
	 */
	public void setAbgelehnt(boolean abgelehnt) {
		this.abgelehnt = abgelehnt;
	}

	/**
	 *
	 * @return ID des Ablehnungsgrunds {@link org.semtix.shared.daten.enums.HaerteAblehnungsgrund}
	 */
	public int getAblehnungsID() {
		return ablehnungsID;
	}

	/**
	 *
	 * @param ablehnungsID ID des Ablehnungsgrunds {@link org.semtix.shared.daten.enums.HaerteAblehnungsgrund}
	 */
	public void setAblehnungsID(int ablehnungsID) {
		this.ablehnungsID = ablehnungsID;
	}

    /**
     *
     * @return selbst erstellter nicht-standard Begründungstext
     */
    public String getCustomText() {
        return customText;
    }

    public void setCustomText(String customText) {
        this.customText = customText;
    }

    /**
     *
     * @return selbst erstellter nicht-standard Name der Härte (z.B. um "sonstige Härte" zu ersetzen)
     */
    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    /**
     * Für die Ausgabe von AntragHaerte in Listen usw. falls kein CustomName vergeben ist, wird der Name des Härtegrundes ausgegeben
     * @return customName wenn vorhanden, sonst Härtegrund
     */
    public String toString() {
        if (null == customName || customName.length() < 1) {
            return this.haertegrund.toString();
        } else {
            return customName;
        }
    }

    public int getCustomPoints() {
        return customPoints;
    }

    public void setCustomPoints(int customPoints) {
        this.customPoints = customPoints;
    }
}
