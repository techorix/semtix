/*
 * Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 * Semesterticketbüro der Humboldt-Universität Berlin
 *
 * Copyright (c) 2015. Michael Mertins (MichaelMertins@gmail.com)
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

import org.semtix.gui.tabs.antrag.nachreichungen.pruefen.NachreichungArt;

import javax.persistence.*;
import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * Klasse für Objekte von ungeprüften Nachreichungen (entspricht der Datenbanktabelle nachreichungen).
 * 
 * <p>Wenn Nachreichungen zu fehlenden Unterlagen eines Antrags im Büro eingehen, kann dafür 
 * im PersonenFormular oder AntragsFormular eine unbearbeitete Nachreichung angelegt werden. So 
 * sieht der/die zuständige Büromitarbeiter_in gleich in der Anwendung, ob fehlende Unterlagen 
 * eingegangen sind. Die offenen Nachreichungen müssen von den Büromitarbeiter_innen auf "geprüft" 
 * gesetzt werden, d.h. es wurde registriert, daß eine Nachreichung für einen Antrag eingagngen ist und 
 * es kann sich darum gekümmert werden.</p>
 */
@Entity
public class Nachreichung implements Serializable {

    @Id
    @SequenceGenerator(name = "nachreichungID_seq",
            sequenceName = "nachreichungID_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "nachreichungID_seq")
    private int nachreichungID;						// ID für die Nachreichung (Primärschlüssel in Datenbank)
	private int antragID;							// ID für Antrag, zu welchem die Nachreichung gehört
	private int userEingang;						// User-ID, wer Nachreichung angenommen hat
	private int userChecked;						// User-ID, wer Nachreichung geprüft hat

    @Enumerated
    private NachreichungArt nachreichungArt;        // Art der Nachreichung (siehe Enum tabs.antrag.pruefen.NachreichungArt)
    private GregorianCalendar timestampEingang;		// Zeitstempel, wann Nachreichung eingegangen ist
	private GregorianCalendar timestampChecked;		// Zeitstempel, wann Nachreichung geprüft wurde
	private boolean statusChecked;					// wurde Nachreichung geprüft?
	
	
	/**
	 * Konstruktor mit Standardwerten
	 */
	public Nachreichung() {
		
		timestampEingang = new GregorianCalendar();
		
		statusChecked = false;
		
	}
	
	
	/**
	 * Konstruktor mit Feldwerten als Parameter
	 * @param nachreichungID ID für die Nachreichung (Primärschlüssel in Datenbank)
	 * @param antragID ID für Antrag, zu welchem die Nachreichung gehört
     * @param nachreichungArt Art der Nachreichung (siehe Enum tabs.antrag.pruefen.NachreichungArt)
     * @param userEingang User-ID, wer Nachreichung angenommen hat
	 * @param userChecked User-ID, wer Nachreichung geprüft hat
	 * @param timestampEingang Zeitstempel, wann Nachreichung eingegangen ist
	 * @param timestampChecked Zeitstempel, wann Nachreichung geprüft wurde
	 * @param statusChecked wurde Nachreichung geprüft?
	 */
	public Nachreichung(int nachreichungID, int antragID, NachreichungArt nachreichungArt,
			int userEingang, int userChecked,
			GregorianCalendar timestampEingang,
			GregorianCalendar timestampChecked,
			boolean statusChecked) {

		this.nachreichungID = nachreichungID;
		this.antragID = antragID;
		this.nachreichungArt = nachreichungArt;
		this.userEingang = userEingang;
		this.userChecked = userChecked;
		this.timestampEingang = timestampEingang;
		this.timestampChecked = timestampChecked;
		this.statusChecked = statusChecked;
		
	}




	/**
	 * Liefert ID für die Nachreichung (Primärschlüssel in Datenbank)
	 * @return ID der Nachreichung
	 */
	public int getNachreichungID() {
		return nachreichungID;
	}




	/**
	 * Setzt ID für die Nachreichung (Primärschlüssel in Datenbank)
	 * @param nachreichungID ID der Nachreichung
	 */
	public void setNachreichungID(int nachreichungID) {
		this.nachreichungID = nachreichungID;
	}




	/**
	 * Liefert ID für Antrag, zu welchem die Nachreichung gehört
	 * @return ID des Antrags
	 */
	public int getAntragID() {
		return antragID;
	}




	/**
	 * Setzt ID für Antrag, zu welchem die Nachreichung gehört
	 * @param antragID ID des Antrags
	 */
	public void setAntragID(int antragID) {
		this.antragID = antragID;
	}




	/**
     * Liefert Art der Nachreichung (siehe Enum {@link org.semtix.gui.tabs.antrag.nachreichungen.pruefen.NachreichungArt})
     * @return Nachreichungart
	 */
	public NachreichungArt getNachreichungArt() {
		return nachreichungArt;
	}




	/**
     * Setzt Art der Nachreichung (siehe Enum {@link org.semtix.gui.tabs.antrag.nachreichungen.pruefen.NachreichungArt})
     * @param nachreichungArt Nachreichungart
	 */
	public void setNachreichungArt(NachreichungArt nachreichungArt) {
		this.nachreichungArt = nachreichungArt;
	}




	/**
	 * Liefert User-ID, wer Nachreichung angenommen hat
	 * @return User-ID
	 */
	public int getUserEingang() {
		return userEingang;
	}




	/**
	 * Setzt User-ID, wer Nachreichung angenommen hat
	 * @param userEingang User-ID
	 */
	public void setUserEingang(int userEingang) {
		this.userEingang = userEingang;
	}




	/**
	 * Liefert User-ID, wer Nachreichung geprüft hat
	 * @return User-ID
	 */
	public int getUserChecked() {
		return userChecked;
	}




	/**
	 * Setzt User-ID, wer Nachreichung geprüft hat
	 * @param userChecked User-ID
	 */
	public void setUserChecked(int userChecked) {
		this.userChecked = userChecked;
	}




	/**
	 * Liefert Zeitstempel, wann Nachreichung eingegangen ist
	 * @return Zeitstempel Eingang der Nachreichung
	 */
	public GregorianCalendar getTimestampEingang() {
		return timestampEingang;
	}




	/**
	 * Setzt Zeitstempel, wann Nachreichung eingegangen ist
	 * @param timestampEingang Zeitstempel Eingang der Nachreichung
	 */
	public void setTimestampEingang(GregorianCalendar timestampEingang) {
		this.timestampEingang = timestampEingang;
	}




	/**
	 * Liefert Zeitstempel, wann Nachreichung geprüft wurde
	 * @return Zeitstempel Prüfung der Nachreichung
	 */
	public GregorianCalendar getTimestampChecked() {
		return timestampChecked;
	}




	/**
	 * Setzt Zeitstempel, wann Nachreichung geprüft wurde
	 * @param timestampChecked Zeitstempel Prüfung der Nachreichung
	 */
	public void setTimestampChecked(GregorianCalendar timestampChecked) {
		this.timestampChecked = timestampChecked;
	}




	/**
	 * Liefert ob Nachreichung geprüft wurde
	 * @return Nachreichung geprüft ja/nein
	 */
	public boolean isStatusChecked() {
		return statusChecked;
	}




	/**
	 * Setzt ob Nachreichung geprüft wurde
	 * @param statusChecked Nachreichung geprüft ja/nein
	 */
	public void setStatusChecked(boolean statusChecked) {
		this.statusChecked = statusChecked;
	}
		

}
