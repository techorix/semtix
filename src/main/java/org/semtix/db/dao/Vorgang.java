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

import org.semtix.shared.daten.enums.Vorgangsart;

import javax.persistence.*;
import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * Klasse für Objekte von Vorgängen im Bearbeitungsprotokoll.
 *
 * <p>Im Bearbeitungsprotokoll werden alle Vorgänge zu einem Antrag festgehalten. Anhand der Antrags-ID
 * können alle Vorgänge einem Antrag zugeordnet werden. Über die 
 * Konstante (enum) {@link org.semtix.shared.daten.enums.Vorgangsart} wird festgelegt, um was für einen
 * Vorgang es sich handelt. Ausserdem können noch Zeitstempel (wann wurde der Vorgang angelegt) und 
 * User (wer hat den Vorgang angelegt) gespeichert werden.</p>
 */
@Entity
public class Vorgang implements Serializable {

    @Id
    @SequenceGenerator(name = "vorgangID_seq",
            sequenceName = "vorgangID_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "vorgangID_seq")
    private int vorgangID;						// ID des Vorgangs (Primärschlüssel in Datenbank)

    private int antragID;                        // ID des Antrags, zu dem der Vorgang gehört

    @Enumerated
    private Vorgangsart vorgangsart;			// Konstante mit Art des Vorgangs (siehe Enum Vorgangsart)

	private GregorianCalendar zeitstempel;		// Zeitstempel, wann der Vorgang angelegt wurde

    private int userID;                            // User, welche/r den Vorgang angelegt hat


    /**
	 * Konstruktor mit Standardwerten
	 */
	public Vorgang() {
	}



	/**
	 * Konstruktor mit Übergabe der Felder als Parameter
	 * @param vorgangID ID des Vorgangs (Primärschlüssel in Datenbank)
	 * @param vorgangsart Konstante mit Art des Vorgangs (siehe Enum Vorgangsart)
	 * @param antragID ID des Antrags, zu dem der Vorgang gehört
	 * @param userID User, welche/r den Vorgang angelegt hat
	 * @param zeitstempel Zeitstempel, wann der Vorgang angelegt wurde
	 */
	public Vorgang(int vorgangID, Vorgangsart vorgangsart, int antragID, int userID,
			GregorianCalendar zeitstempel) {

		this.vorgangID = vorgangID;
		this.vorgangsart = vorgangsart;
		this.antragID = antragID;
		this.setUserID(userID);
		this.zeitstempel = zeitstempel;
		
	}



	/**
	 * Liefert ID des Vorgangs (Primärschlüssel in Datenbank)
	 * @return ID des Vorgangs
	 */
	public int getVorgangID() {
		return vorgangID;
	}



	/**
	 * Setzt ID des Vorgangs (Primärschlüssel in Datenbank)
	 * @param vorgangID ID des Vorgangs
	 */
	public void setVorgangID(int vorgangID) {
		this.vorgangID = vorgangID;
	}



	/**
	 * Liefert Vorgangsart (Konstante enum)
	 * @return Vorgangsart
	 */
	public Vorgangsart getVorgangsart() {
		return vorgangsart;
	}



	/**
	 * Setzt Vorgangsart (Konstante enum)
	 * @param vorgangsart Vorgangsart
	 */
	public void setVorgangsart(Vorgangsart vorgangsart) {
		this.vorgangsart = vorgangsart;
	}



	/**
	 * Liefert die ID des Antrags, für den der Vorgang angelegt wurde
	 * @return ID des Antrags
	 */
	public int getAntragID() {
		return antragID;
	}



	/**
	 * Setzt die ID des Antrags, für den der Vorgang angelegt wurde
	 * @param antragID ID des Antrags
	 */
	public void setAntragID(int antragID) {
		this.antragID = antragID;
	}

	/**
	 * Liefert Zeitstempel, wann der Vorgang angelegt wurde
	 * @return Zeitstempel
	 */
	public GregorianCalendar getZeitstempel() {
		return zeitstempel;
	}



	/**
	 * Setzt Zeitstempel, wann der Vorgang angelegt wurde
	 * @param zeitstempel Zeitstempel
	 */
	public void setZeitstempel(GregorianCalendar zeitstempel) {
		this.zeitstempel = zeitstempel;
	}

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
