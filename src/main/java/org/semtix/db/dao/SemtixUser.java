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


import org.semtix.shared.daten.enums.UserPrivileg;
import org.semtix.shared.daten.enums.UserSemesterStatus;
import org.semtix.shared.daten.enums.UserStatus;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Klasse für Objekte von Daten der Büromitarbeiter_innen.
 * 
 * <p><b>Login-Name:</b><br>
 * Der Loginname am Betriebssystem dient zur Erkennung und Identifizierung bei der Anwendung, d.h. anhand des 
 * Loginnamens des Betriebssystems wird der User der Anwendung bestimmt.</p>
 * 
 * <p><b>Lokale Semester:</b><br>
 * In der alten/bisherigen Semtix-Anwendung konnte ein Arbeitssemester eingestellt werden, sowohl lokal (nur 
 * für die eine Arbeitsstation) als auch global für alle User. Dies wurde entsprechend den Beschreibungen im 
 * Lastenheft auch in der neuen Anwendung umgesetzt, soll aber nach neuestem Stand wegfallen.</p>
 * 
 * <p><b>Buchstaben:</b><br>
 * Jede/r Büromitarbeiter_in hat bestimmte Anfangsbuchstaben vom Nachnamen der Personen, welche bearbeitet werden. 
 * Diese sind im <code>String buchstaben</code> gespeichert.</p>
 * 
 * <p><b>UserPrivileg:</b><br>
 * Anhand des {@link org.semtix.shared.daten.enums.UserPrivileg} sollen verschiedene Rechte für User festgelegt werden. Zur Zeit gibt es folgende
 * UserPrivilegien: User, Admin und Finanz.</p>
 * 
 * <p><b>UserStatus:</b><br>
 * Mit dem {@link org.semtix.shared.daten.enums.UserStatus} können dem User folgende Zustände festgelegt werden: aktiv, passiv.</p>
 */
@Entity
public class SemtixUser implements Serializable {

    @Id
    @SequenceGenerator(name = "userID_seq",
            sequenceName = "userID_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "userID_seq")
    private int userID;				// ID des Users (Primärschlüssel in Datenbank)
	private int semLokalHU;			// ID des für den User eingestellten lokalen Semesters für HU
	private int semLokalKW;			// ID des für den User eingestellten lokalen Semesters für KW
	
	private String loginName;		// Loginname des Users am System
	private String kuerzel;			// Kürzel des Users (max. 5 Zeichen)
	private String vorname;			// Vorname des Users
	private String nachname;		// Nachname des Users
	private String buchstaben;		// Anfangsbuchstaben der Personen (Nachnamen), welche der User bearbeitet

    @Enumerated
    private UserPrivileg privileg = UserPrivileg.USER;		// Konstante (Enum): Privileg des Users (User, Admin, Finanz)

    @Enumerated
    private UserStatus status = UserStatus.AKTIV;            // Konstante (Enum): Status des Users (aktiv, passiv)


    // Status, welches Semester für HU angezeigt wird (lokal oder global) - Standard ist GLOBAL
	@Enumerated
    private UserSemesterStatus semesterStatusHU;
	
	// Status, welches Semester für KW angezeigt wird (lokal oder global) - Standard ist GLOBAL
	@Enumerated
    private UserSemesterStatus semesterStatusKW;
	
	
	
	/**
	 * Standard-Konstruktor
	 */
	public SemtixUser() {

	}
	
	
	/**
	 * Konstruktor mit Übergabe der Felder als Parameter
	 * @param userID ID des Users (Primärschlüssel in Datenbank)
	 * @param loginName Loginname des Users am System
	 * @param kuerzel Kürzel des Users (max. 5 Zeichen)
	 * @param vorname Vorname des Users
	 * @param nachname Nachname des Users
	 * @param buchstaben Anfangsbuchstaben der Personen (Nachnamen), welche der User bearbeitet
	 * @param privileg Konstante (Enum): Privileg des Users (User, Admin, Finanz)
	 * @param status Konstante (Enum): Status des Users (aktiv, passiv)
	 * @param semesterStatusHU Status, welches Semester für HU angezeigt wird (lokal oder global) - Standard ist GLOBAL
	 * @param semesterStatusKW Status, welches Semester für KW angezeigt wird (lokal oder global) - Standard ist GLOBAL
	 * @param semLokalHU ID des für den User eingestellten lokalen Semesters für HU
	 * @param semLokalKW ID des für den User eingestellten lokalen Semesters für KW
	 */
	public SemtixUser(int userID, String loginName, String kuerzel, String vorname,
                      String nachname, String buchstaben, UserPrivileg privileg, UserStatus status,
                      UserSemesterStatus semesterStatusHU, UserSemesterStatus semesterStatusKW,
                      int semLokalHU, int semLokalKW) {

		this.userID = userID;
		this.loginName = loginName;
		this.kuerzel = kuerzel;
		this.vorname = vorname;
		this.nachname = nachname;
		this.buchstaben = buchstaben;
		this.privileg = privileg;
		this.status = status;
		this.semesterStatusHU = semesterStatusHU;
		this.semesterStatusKW = semesterStatusKW;
		this.semLokalHU = semLokalHU;
		this.semLokalKW = semLokalKW;
	}


	
	/**
	 * Liefert ID des Users (Primärschlüssel in Datenbank)
	 * @return ID des Users
	 */
	public int getUserID() {
		return userID;
	}


	/**
	 * Setzt ID des Users (Primärschlüssel in Datenbank)
	 * @param userID ID des Users
	 */
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	
	/**
	 * Liefert ID des für den User eingestellten lokalen Semesters für HU
	 * @return ID lokales Semester HU
	 */
	public int getSemLokalHU() {
		return semLokalHU;
	}


	/**
	 * Setzt ID des für den User eingestellten lokalen Semesters für HU
	 * @param semLokalHU ID lokales Semester HU
	 */
	public void setSemLokalHU(int semLokalHU) {
		this.semLokalHU = semLokalHU;
	}
	
	
	/**
	 * Liefert ID des für den User eingestellten lokalen Semesters für KW
	 * @return ID lokales Semester KW
	 */
	public int getSemLokalKW() {
		return semLokalKW;
	}


	/**
	 * Setzt ID des für den User eingestellten lokalen Semesters für KW
	 * @param semLokalKW ID lokales Semester KW
	 */
	public void setSemLokalKW(int semLokalKW) {
		this.semLokalKW = semLokalKW;
	}

	
	/**
	 * Liefert Status, welches Semester für HU angezeigt wird (lokal oder global) - Standard ist GLOBAL
	 * @return UserSemesterStatus
	 */
	public UserSemesterStatus getSemesterStatusHU() {
		return semesterStatusHU;
	}


	/**
	 * Setzt Status, welches Semester für HU angezeigt wird (lokal oder global) - Standard ist GLOBAL
	 * @param semesterStatusHU UserSemesterStatus
	 */
	public void setSemesterStatusHU(UserSemesterStatus semesterStatusHU) {
		this.semesterStatusHU = semesterStatusHU;
	}
	
	
	/**
	 * Liefert Status, welches Semester für KW angezeigt wird (lokal oder global) - Standard ist GLOBAL
	 * @return v
	 */
	public UserSemesterStatus getSemesterStatusKW() {
		return semesterStatusKW;
	}


	/**
	 * Setzt Status, welches Semester für KW angezeigt wird (lokal oder global) - Standard ist GLOBAL
	 * @param semesterStatusKW UserSemesterStatus
	 */
	public void setSemesterStatusKW(UserSemesterStatus semesterStatusKW) {
		this.semesterStatusKW = semesterStatusKW;
	}
	

	/**
	 * Liefert Loginname des Users am System
	 * @return Loginname des Users
	 */
	public String getLoginName() {
		return loginName;
	}


	/**
	 * Setzt Loginname des Users am System
	 * @param loginName Loginname des Userss
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}


	/**
	 * Liefert Kürzel des Users (max. 5 Zeichen)
	 * @return Kürzel des Users
	 */
	public String getKuerzel() {
		return kuerzel;
	}


	/**
	 * Setzt Kürzel des Users (max. 5 Zeichen)
	 * @param kuerzel Kürzel des Users
	 */
	public void setKuerzel(String kuerzel) {
		this.kuerzel = kuerzel;
	}


	/**
	 * Liefert Vorname des Users
	 * @return Vorname des Users
	 */
	public String getVorname() {
		return vorname;
	}


	/**
	 * Setzt Vorname des Users
	 * @param vorname Vorname des Users
	 */
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}


	/**
	 * Liefert Nachname des Users
	 * @return v
	 */
	public String getNachname() {
		return nachname;
	}


	/**
	 * Setzt Nachname des Users
	 * @param nachname Nachname
	 */
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	
	
	/**
	 * Liefert Anfangsbuchstaben der Personen (Nachnamen), welche der User bearbeitet
	 * @return Anfangsbuchstaben
	 */
	public String getBuchstaben() {
		return buchstaben;
	}


	/**
	 * Setzt Anfangsbuchstaben der Personen (Nachnamen), welche der User bearbeitet
	 * @param buchstaben Anfangsbuchstaben
	 */
	public void setBuchstaben(String buchstaben) {
		this.buchstaben = buchstaben;
	}


	/**
	 * Liefert Konstante (Enum): Privileg des Users (User, Admin, Finanz)
	 * @return Privileg des Users
	 */
	public UserPrivileg getPrivileg() {
		return privileg;
	}


	/**
	 * Setzt Konstante (Enum): Privileg des Users (User, Admin, Finanz)
	 * @param privileg Privileg des Users
	 */
	public void setPrivileg(UserPrivileg privileg) {
		this.privileg = privileg;
	}


	/**
	 * Liefert Konstante (Enum): Status des Users (aktiv, passiv)
	 * @return Status des Users
	 */
	public UserStatus getStatus() {
		return status;
	}


	/**
	 * Setzt Konstante (Enum): Status des Users (aktiv, passiv)
	 * @param status v
	 */
	public void setStatus(UserStatus status) {
		this.status = status;
	}



}
