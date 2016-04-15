/*
 * Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 *        Semesterticketbüro der Humboldt-Universität Berlin
 *
 * Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
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

import org.semtix.config.UserConf;
import org.semtix.shared.daten.enums.UnterlagenStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * Klasse für Objekte von Unterlagen (entspricht der Datenbanktabelle unterlagen)
 * 
 * <p>In Objekten der Klasse <code>Unterlagen</code> werden Informationen über für einem 
 * Antrag fehlende Unterlagen erfasst. Dies geschieht auf der Seite mit dem Antragsformular. Der 
 * UnterlagenStatus der fehlenden Unterlagen wird farblich angezeigt: "vorhanden", "nachgefordert", 
 * "gemahnt", "fehlt". Fehlende Unterlagen können nachgefordert oder gemahnt werden.</p>
 *
 * <p>Der Text der Unterlagen entspricht dem individuell angepassten deutschen oder englischen Text eines Textbausteins.</p>
 */
@Entity
public class Unterlagen implements Serializable, Comparable {

    @Id
    @SequenceGenerator(name = "unterlagenID_seq",
            sequenceName = "unterlagenID_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "unterlagenID_seq")
    private int unterlagenID;					// ID der Unterlagen (Primärschlüssel in Datenbank)

    //TODO hier auch noch die Relation mit Antrag über Hibernate regeln
    private int antragID;                        // ID des Antrags, für den die Unterlagen erstellt wurden

    private int userID;                            // User-ID, wer die fehlenden Unterlagen angelegt hat


    @Column(length=10000)
    private String text;						// Text der Unterlagen

    @Enumerated
    private UnterlagenStatus unterlagenStatus;	// Status der Unterlagen (siehe Enum UnterlagenStatus)
	private GregorianCalendar fristNachfrage;	// Gesetzte Frist für eine Nachfrage nach fehlenden Unterlagen
	private GregorianCalendar fristMahnung;		// Gesetzte Frist für eine Mahnung nach fehlenden Unterlagen


    /**
     * Konstruktor mit Standardwerten
     */
	public Unterlagen() {
		unterlagenID = -1;
		antragID = -1;
		text = "";
		unterlagenStatus = UnterlagenStatus.FEHLT;
		fristNachfrage = null;
		fristMahnung = null;


        if (null != UserConf.CURRENT_USER)
		    userID = UserConf.CURRENT_USER.getUserID();
        else
            userID = -1;
	}
	
	
	
	/**
	 * Konstruktor mit Parametern
	 * @param unterlagenID ID der Unterlagen (Primärschlüssel in Datenbank)
	 * @param antragID ID des Antrags, für den die Unterlagen erstellt wurden
	 * @param text Text der Unterlagen
	 * @param unterlagenStatus Status der Unterlagen (siehe Enum UnterlagenStatus)
	 * @param fristNachfrage Gesetzte Frist für eine Nachfrage nach fehlenden Unterlagen
	 * @param fristMahnung Gesetzte Frist für eine Mahnung nach fehlenden Unterlagen
	 * @param angefordert Unterlagen angefordert? ja/nein
	 * @param gemahnt Unterlagen gemahnt? ja/nein
	 * @param userID User-ID, wer die fehlenden Unterlagen angelegt hat
	 */
	public Unterlagen(int unterlagenID, int antragID, String text, UnterlagenStatus unterlagenStatus,
			GregorianCalendar fristNachfrage, GregorianCalendar fristMahnung, boolean angefordert, 
			boolean gemahnt, int userID) {


		this.unterlagenID = unterlagenID;
		this.antragID = antragID;
		this.text = text;
		this.unterlagenStatus = unterlagenStatus;
		this.fristNachfrage = fristNachfrage;
		this.fristMahnung = fristMahnung;

        this.userID = userID;
	}


	/**
	 * Liefert die ID der Unterlagen (Primärschlüssel in Datenbank)
	 * @return Unterlagen-ID
	 */
	public int getUnterlagenID() {
		return unterlagenID;
	}


	/**
	 * Setzt die ID der Unterlagen (Primärschlüssel in Datenbank)
	 * @param unterlagenID Unterlagen-ID
	 */
	public void setUnterlagenID(int unterlagenID) {
		this.unterlagenID = unterlagenID;
	}


	/**
	 * Liefert die ID des Antrags, für den die Unterlagen angelegt wurden
	 * @return Antrag-ID
	 */
	public int getAntragID() {
		return antragID;
	}


	/**
	 * Setzt die ID des Antrags, für den die Unterlagen angelegt wurden
	 * @param antragID Antrag-ID
	 */
	public void setAntragID(int antragID) {
		this.antragID = antragID;
	}


	/**
	 * Liefert den Text zu den fehlenden Unterlagen
	 * @return Unterlagentext
	 */
	public String getText() {
		return text;
	}


	/**
	 * Setzt den Text zu den fehlenden Unterlagen
	 * @param text Unterlagentext
	 */
	public void setText(String text) {
		this.text = text;
	}


	/**
	 * Liefert den UnterlagenStatus ("vorhanden", "nachgefordert", "gemahnt", "fehlt")
	 * @return UnterlagenStatus (Enum)
	 */
	public UnterlagenStatus getUnterlagenStatus() {
		return unterlagenStatus;
	}


	/**
	 * Setzt den UnterlagenStatus ("vorhanden", "nachgefordert", "gemahnt", "fehlt")
	 * @param unterlagenStatus UnterlagenStatus (Enum)
	 */
	public void setUnterlagenStatus(UnterlagenStatus unterlagenStatus) {
		this.unterlagenStatus = unterlagenStatus;
	}


	/**
	 * Liefert die gesetzte Frist für eine Nachfrage nach fehlenden Unterlagen
	 * @return Nachfragefrist
	 */
	public GregorianCalendar getFristNachfrage() {
		return fristNachfrage;
	}


	/**
	 * Setzt die gesetzte Frist für eine Nachfrage nach fehlenden Unterlagen
	 * @param fristNachfrage Nachfragefrist
	 */
	public void setFristNachfrage(GregorianCalendar fristNachfrage) {
		this.fristNachfrage = fristNachfrage;
	}
	
	
	/**
	 * Liefert die gesetzte Frist für eine Mahnung nach fehlenden Unterlagen
	 * @return Mahnfrist
	 */
	public GregorianCalendar getFristMahnung() {
		return fristMahnung;
	}


	/**
	 * Setzt die gesetzte Frist für eine Mahnung nach fehlenden Unterlagen
	 * @param fristMahnung Mahnfrist
	 */
	public void setFristMahnung(GregorianCalendar fristMahnung) {
		this.fristMahnung = fristMahnung;
	}


	/**
	 * Liefert ID von User, welche/r die fehlende Unterlagen angelegt hat
	 * @return User-ID
	 */
	public int getUserID() {
		return userID;
	}


	/**
	 * setzt ID von User, welche/r die fehlende Unterlagen angelegt hat
	 * @param userID User-ID
	 */
	public void setUserID(int userID) {
		this.userID = userID;
	}


	@Override
	public boolean equals(Object o) {
		return this.getUnterlagenID() == ((Unterlagen) o).getUnterlagenID();
	}

	@Override
	public int compareTo(Object o) {
		if (equals(o)) {
			return 0;
		} else {

			return Integer.compare(this.unterlagenID, ((Unterlagen) o).unterlagenID);
		}
	}
}
