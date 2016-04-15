/*

  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
         Semesterticketbüro der Humboldt-Universität Berlin

  Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package org.semtix.db.dao;

import javax.persistence.*;
import java.io.Serializable;
import java.util.GregorianCalendar;


/**
 * Klasse für Objekte von Anmerkungen (entspricht der Datenbanktabelle anmerkung).
 * Für Personen können Anmerkungen im PersonenFormular angelegt und angezeigt werden. Diese 
 * Anmerkungen sind immer personen- und nicht antragsbezogen. Sie werden im PersonenFormular 
 * unten chronologisch in einer Tabellenstruktur angezeigt. Neben einem Zeitstempel, wann die 
 * Anmerkung angelegt wurde, enthält sie noch einen Text sowie die ID des Users, welche/r 
 * die Anmerkung angelegt hat.
 */
@Entity
public class Anmerkung implements Serializable, Comparable {


    @Id
    @SequenceGenerator(name="anmerkungId_seq",
            sequenceName="anmerkungId_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="anmerkungId_seq")
	private int anmerkungId;				// fortlaufende ID (Primärschlüssel in Datenbank)

    private int personId;                    // Person-ID für welche die Anmerkung angelegt wurde

    private int userId;                        // User-ID welche die Anmerkung angelegt hat

    private String text;                    // Text der Anmerkung

    private GregorianCalendar zeitstempel;	// Zeitstempel wann Anmerkung angelegt wurde





	/**
	 * Leerer Standardkonstruktor
	 */
	public Anmerkung() { }
	
	
	/**
	 * Konstruktor mit den Feldern als Parameter
	 * @param anmerkungId fortlaufende ID (Primärschlüssel in Datenbank)
	 * @param personId Person-ID für welche die Anmerkung angelegt wurde
	 * @param userId User-ID welche die Anmerkung angelegt hat
	 * @param text Text der Anmerkung
	 * @param timestamp Zeitstempel wann Anmerkung angelegt wurde
	 */
	public Anmerkung(int anmerkungId, int personId, int userId, String text,
                     GregorianCalendar timestamp) {
		
		this.anmerkungId = anmerkungId;
		this.personId = personId;
		this.userId = userId;
		this.text = text;
		this.zeitstempel = timestamp;
		
	}

    public Anmerkung(int personId, int userId, String text,
                     GregorianCalendar timestamp) {

        this.personId = personId;
        this.userId = userId;
        this.text = text;
        this.zeitstempel = timestamp;

    }


	/**
	 * Liefert fortlaufende ID (Primärschlüssel in Datenbank)
	 * @return Anmerkung-ID
	 */
	public int getAnmerkungId() {
		return anmerkungId;
	}


	/**
	 * Setzt fortlaufende ID (Primärschlüssel in Datenbank)
	 * @param anmerkungId Anmerkung-ID
	 */
	public void setAnmerkungId(int anmerkungId) {
		this.anmerkungId = anmerkungId;
	}

	
	/**
	 * Liefert Person-ID für welche die Anmerkung angelegt wurde
	 * @return Person-ID
	 */
	public int getPersonId() {
		return personId;
	}


	/**
	 * Setzt Person-ID für welche die Anmerkung angelegt wurde
	 * @param personId Person-ID
	 */
	public void setPersonId(int personId) {
		this.personId = personId;
	}


	/**
	 * Liefert User-ID welche die Anmerkung angelegt hat
	 * @return User-ID
	 */
	public int getUserId() {
		return userId;
	}


	/**
	 * Setzt User-ID welche die Anmerkung angelegt hat
	 * @param userId User-ID
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}


	/**
	 * Liefert Text der Anmerkung
	 * @return Anmerkungstext
	 */
	public String getText() {
		return text;
	}


	/**
	 * Setzt Text der Anmerkung
	 * @param text Anmerkungstext
	 */
	public void setText(String text) {
		this.text = text;
	}


	/**
	 * Liefert Zeitstempel wann Anmerkung angelegt wurde
	 * @return Zeitstempel
	 */
	public GregorianCalendar getZeitstempel() {
		return zeitstempel;
	}



	/**
	 * Setzt Zeitstempel wann Anmerkung angelegt wurde
	 * @param zeitstempel Zeitstempel
	 */
	public void setZeitstempel(GregorianCalendar zeitstempel) {
		this.zeitstempel = zeitstempel;
	}


	@Override
	public boolean equals(Object o) {
		return this.getAnmerkungId() == ((Anmerkung) o).getAnmerkungId();
	}

	@Override
	public int compareTo(Object o) {
		return this.getZeitstempel().compareTo(((Anmerkung) o).getZeitstempel());
	}
}
