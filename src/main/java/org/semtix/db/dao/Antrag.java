/*
 *
 *  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 *         Semesterticketbüro der Humboldt-Universität Berlin
 *
 *  Copyright (c) 2015-2016 Michael Mertins (MichaelMertins@gmail.com)
 *  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)
 *
 *    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.semtix.db.dao;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.semtix.shared.daten.enums.AntragStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;



/**
 * Klasse für Objekte von Anträgen (entspricht der Datenbanktabelle antrag).
 * 
 * <p>In diesen Objekten können alle Werte gespeichert werden, die zu einem bestimmten Antrag gehören.</p>
 */
@Entity
public class Antrag
		implements Cloneable, Serializable, Comparable {

    @Id
    @SequenceGenerator(name = "antragID_seq",
            sequenceName = "antragID_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "antragID_seq")
    private int antragID;								// ID für den Antrag (Primärschlüssel in Datenbank)

	private int personID;                               // ID der Person, zu welcher der Antrag gehört

	private int semesterID;                             // ID für das Semester, in welchem der Antrag gestellt wurde

    private int punkteEinkommen;						// Anzahl der Punkte für Einkommen
	private int punkteHaerte;							// Anzahl der Punkte für Härten
    private int anzahlMonate;							// Anzahl der Monate bei Teilzuschuss

    @Enumerated
    private AntragStatus antragStatus;					// Konstante (Enum) für Entscheidungs-Status des Antrags
	
	private String begruendung;							// Begründungstext (wird aus Textbausteinen erstellt)

	@OneToMany(fetch = FetchType.EAGER, targetEntity = AntragHaerte.class, mappedBy = "antragID", orphanRemoval = true)
	@Fetch(FetchMode.JOIN)
	@Cascade(CascadeType.ALL)
	@OrderBy
	private List<AntragHaerte> haerteListe;        // Liste mit den Härten für den Antrag: Das ist die einzige Stelle wo wirklich automatisch eine Liste von Objekten gespeichert und geladen werden sollte. Das könnte man noch besser mit Hibernate machen (siehe DBHandlerAntrag)


    private boolean teilzuschuss;						// Teilzuschuss ja/nein
    private boolean gesendet;							// Bescheid gesendet ja/nein
    private boolean gedruckt;							// Bescheid gedruckt ja/nein
    private boolean auszahlung;							// Auszahlung angeordnet ja/nein

    private boolean manAuszahlen;						// manuell auszahlen ja/nein
    private boolean raten;								// Ratenzahlung ja/nein
    private boolean nothilfe;							// Nothilfe ja/nein
    private boolean kulanz;								// Kulanz ja/nein
    private boolean erstsemester;						// ist Antragsteller_in Erstsemester ja/nein
    private boolean charite;                            // der Antragsteller ist an der Charite immatrikuliert
    
    private BigDecimal erstattung;						// Betrag, der nach der Zuschussberechnung für den Antrag ausgezahlt wird
    
    private GregorianCalendar datumAngelegt;			// Datum wann Antrag angelegt wurde
    private GregorianCalendar datumGeaendert;			// Datum wann Antrag zuletzt geändert wurde
	
	private int userAngelegt;							// ID des Users, welcher den Antrag angelegt hat
	private int userGeaendert;							// ID des Users, welcher den Antrag zuletzt geändert hat


    /**
     * Standardkonstruktor mit Startwerten
     */
    public Antrag() {

    }
	
    /**
     * Konstruktor mit Feldwerten als Parameter
     * @param antragID ID des Antrags (Primärschlüssel in Datenbank)
     * @param personID ID der Person, zu welcher der Antrag gehört
     * @param semesterID ID für das Semester, in welchem der Antrag gestellt wurde
     * @param antragStatus Konstante (Enum) für Entscheidungs-Status des Antrags
     * @param punkteEinkommen Anzahl der Punkte für Einkommen
     * @param punkteHaerte Anzahl der Punkte für Härten
     * @param teilzuschuss Teilzuschuss ja/nein
     * @param anzahlMonate Anzahl der Monate bei Teilzuschuss
     * @param begruendung Begründungstext (wird aus Textbausteinen erstellt)
     * @param gesendet Bescheid gesendet ja/nein
     * @param gedruckt Bescheid gedruckt ja/nein
     * @param auszahlung Auszahlung angeordnet ja/nein
     * @param erstattung Betrag, der nach der Zuschussberechnung für den Antrag ausgezahlt wird
     * @param manAuszahlen manuell auszahlen ja/nein
     * @param raten Ratenzahlung ja/nein
     * @param nothilfe Nothilfe ja/nein
     * @param kulanz Kulanz ja/nein
     * @param erstsemester ist Antragsteller_in Erstsemester ja/nein
     * @param haerteListe Liste mit den Härten für den Antrag
     * @param userAngelegt ID des Users, welcher den Antrag angelegt hat
     * @param datumAngelegt Datum wann Antrag angelegt wurde
     * @param userGeaendert ID des Users, welcher den Antrag zuletzt geändert hat
     * @param datumGeaendert Datum wann Antrag zuletzt geändert wurde
     */
	public Antrag(int antragID, int personID, int semesterID, AntragStatus antragStatus,
			int punkteEinkommen, int punkteHaerte, boolean teilzuschuss,
			int anzahlMonate, String begruendung, boolean gesendet,
			boolean gedruckt, boolean auszahlung, BigDecimal erstattung, 
			boolean manAuszahlen, boolean raten, boolean nothilfe, 
			boolean kulanz, boolean erstsemester,
			ArrayList<AntragHaerte> haerteListe,
			int userAngelegt, GregorianCalendar datumAngelegt,
			int userGeaendert, GregorianCalendar datumGeaendert) {
		
		
		this.antragID = antragID;
		this.personID = personID;
		this.semesterID = semesterID;
		this.antragStatus = antragStatus;
		this.punkteEinkommen = punkteEinkommen;
		this.punkteHaerte = punkteHaerte;
		this.anzahlMonate = anzahlMonate;
		this.begruendung = begruendung;
		this.teilzuschuss = teilzuschuss;
		this.gesendet = gesendet;
		this.gedruckt = gedruckt;
		this.auszahlung = auszahlung;
		this.erstattung = erstattung;
		this.manAuszahlen = manAuszahlen;
		this.raten = raten;
		this.nothilfe = nothilfe;
		this.kulanz = kulanz;
		this.erstsemester = erstsemester;
		this.haerteListe = haerteListe;
		this.userAngelegt = userAngelegt;
		this.datumAngelegt = datumAngelegt;
		this.userGeaendert = userGeaendert;
		this.datumGeaendert = datumGeaendert;
	}



    /**
	 * Kopie von Objekt Antrag erstellen. Wird benötigt, um einen Antrags wieder 
	 * auf die zuletzt gespeicherten Werte zurücksetzen zu können. (Zurücksetzen, reset)
	 */
    public Antrag clone() throws CloneNotSupportedException {

			return (Antrag) super.clone();

    }
	
	
	

	/**
	 * Liefert die ID des Antrags (Primärschlüssel in Datenbank)
	 * @return Antrag-ID
	 */
	public int getAntragID() {
		return antragID;
	}


	/**
	 * Setzt die ID des Antrags (Primärschlüssel in Datenbank)
	 * @param antragID Antrag-ID
	 */
	public void setAntragID(int antragID) {
		this.antragID = antragID;
	}


	/**
	 * Liefert die ID der Person, zu welcher der Antrag gehört
	 * @return Person-ID
	 */
	public int getPersonID() {
		return personID;
	}


	/**
	 * Setzt die ID der Person, zu welcher der Antrag gehört
	 * @param personID Person-ID
	 */
	public void setPersonID(int personID) {
		this.personID = personID;
	}


	/**
	 * Liefert die ID für das Semester, in welchem der Antrag gestellt wurde
	 * @return Semester-ID
	 */
	public int getSemesterID() {
		return semesterID;
	}


	/**
	 * Setzt die ID für das Semester, in welchem der Antrag gestellt wurde
	 * @param semesterID Semester-ID
	 */
	public void setSemesterID(int semesterID) {
		this.semesterID = semesterID;
	}


	/**
	 * Liefert die Konstante (Enum) für den Entscheidungs-Status des Antrags
	 * @return Antragstatus
	 */
	public AntragStatus getAntragStatus() {
		return antragStatus;
	}


	/**
	 * Setzt die Konstante (Enum) für den Entscheidungs-Status des Antrags
	 * @param antragStatus Antragstatus
	 */
	public void setAntragStatus(AntragStatus antragStatus) {
		this.antragStatus = antragStatus;
	}


	/**
	 * Liefert die Anzahl der Punkte für das Einkommen
	 * @return Punkte Einkommen
	 */
	public int getPunkteEinkommen() {
		return punkteEinkommen;
	}


	/**
	 * Setzt die Anzahl der Punkte für das Einkommen
	 * @param punkteEinkommen Punkte Einkommen
	 */
	public void setPunkteEinkommen(int punkteEinkommen) {
		this.punkteEinkommen = punkteEinkommen;
	}
	

	/**
	 * Liefert die Anzahl der Punkte für die Härten
	 * @return Punkte Härten
	 */
	public int getPunkteHaerte() {
		return punkteHaerte;
	}


	/**
	 * Setzt die Anzahl der Punkte für die Härten
	 * @param punkteHaerte Punkte Härten
	 */
	public void setPunkteHaerte(int punkteHaerte) {
		this.punkteHaerte = punkteHaerte;
	}


	/**
	 * Liefert die Anzahl der Monate bei Teilzuschuss
	 * @return Anzahl Monate
	 */
	public int getAnzahlMonate() {
		return anzahlMonate;
	}


	/**
	 * Setzt die Anzahl der Monate bei Teilzuschuss
	 * @param anzahlMonate Anzahl Monate
	 */
	public void setAnzahlMonate(int anzahlMonate) {
		this.anzahlMonate = anzahlMonate;
	}


	/**
	 * Liefert Begründungstext (wird aus Textbausteinen erstellt)
	 * @return Begründungstext
	 */
	public String getBegruendung() {
		return begruendung;
	}


	/**
	 * Setzt Begründungstext (wird aus Textbausteinen erstellt)
	 * @param text Begründungstext
	 */
	public void setBegruendung(String text) {
		this.begruendung = text;
	}


	/**
	 * Liefert ob Teilzuschuss gegeben ist
	 * @return Teilzuschuss ja/nein
	 */
	public boolean isTeilzuschuss() {
		return teilzuschuss;
	}


	/**
	 * Setzt ob Teilzuschuss gegeben ist
	 * @param teilzuschuss Teilzuschuss ja/nein
	 */
	public void setTeilzuschuss(boolean teilzuschuss) {
		this.teilzuschuss = teilzuschuss;
	}


	/**
	 * Liefert ob Bescheid gesendet wurde
	 * @return Bescheid gesendet ja/nein
	 */
	public boolean isGesendet() {
		return gesendet;
	}


	/**
	 * Setzt ob Bescheid gesendet wurde
	 * @param gesendet Bescheid gesendet ja/nein
	 */
	public void setGesendet(boolean gesendet) {
		this.gesendet = gesendet;
	}


	/**
	 * Liefert ob Bescheid gedruckt wurde
	 * @return Bescheid gedruckt ja/nein
	 */
	public boolean isGedruckt() {
		return gedruckt;
	}


	/**
	 * Setzt ob Bescheid gedruckt wurde
	 * @param gedruckt Bescheid gedruckt ja/nein
	 */
	public void setGedruckt(boolean gedruckt) {
		this.gedruckt = gedruckt;
	}
 

	/**
	 * Liefert ob Auszahlung angeordnet wurde
	 * @return Auszahlung angeordnet ja/nein
	 */
	public boolean isAuszahlung() {
		return auszahlung;
	}


	/**
	 * Setzt ob Auszahlung angeordnet wurde
	 * @param auszahlung Auszahlung angeordnet ja/nein
	 */
	public void setAuszahlung(boolean auszahlung) {
		this.auszahlung = auszahlung;
	}


	/**
	 * Liefert ob manuell ausgezahlt werden soll
	 * @return manuell auszahlen ja/nein
	 */
	public boolean isManAuszahlen() {
		return manAuszahlen;
	}


	/**
	 * Setzt ob manuell ausgezahlt werden soll
	 * @param manAuszahlen manuell auszahlen ja/nein
	 */
	public void setManAuszahlen(boolean manAuszahlen) {
		this.manAuszahlen = manAuszahlen;
	}


	/**
	 * Liefert ob Ratenzahlung ausgewählt wurde
	 * @return Ratenzahlung ja/nein
	 */
	public boolean isRaten() {
		return raten;
	}


	/**
	 * Setzt ob Ratenzahlung ausgewählt wurde
	 * @param raten Ratenzahlung ja/nein
	 */
	public void setRaten(boolean raten) {
		this.raten = raten;
	}


	/**
	 * Liefert ob Nothilfe ausgewählt wurde
	 * @return Nothilfe ja/nein
	 */
	public boolean isNothilfe() {
		return nothilfe;
	}


	/**
	 * Setzt ob Nothilfe ausgewählt wurde
	 * @param nothilfe Nothilfe ja/nein
	 */
	public void setNothilfe(boolean nothilfe) {
		this.nothilfe = nothilfe;
	}


	/**
	 * Liefert ob Kulanz ausgewählt wurde
	 * @return Kulanz ja/nein
	 */
	public boolean isKulanz() {
		return kulanz;
	}


	/**
	 * Setzt ob Kulanz ausgewählt wurde
	 * @param kulanz Kulanz ja/nein
	 */
	public void setKulanz(boolean kulanz) {
		this.kulanz = kulanz;
	}
	
	
	/**
	 * Liefert ob Antragsteller_in Erstsemester ist
	 * @return ist Antragsteller_in Erstsemester ja/nein
	 */
	public boolean isErstsemester() {
		return erstsemester;
	}


	/**
	 * Setzt ob Antragsteller_in Erstsemester ist
	 * @param erstsemester ist Antragsteller_in Erstsemester ja/nein
	 */
	public void setErstsemester(boolean erstsemester) {
		this.erstsemester = erstsemester;
	}

	/**
	 * Liefert Geldbetrag, der nach der Zuschussberechnung für den Antrag ausgezahlt wird
	 * @return Auszahlungsbetrag nach Zuschussberechnung
	 */
	public BigDecimal getErstattung() {
		return erstattung;
	}


	/**
	 * Setzt Geldbetrag, der nach der Zuschussberechnung für den Antrag ausgezahlt wird
	 * @param erstattung Auszahlungsbetrag nach Zuschussberechnung
	 */
	public void setErstattung(BigDecimal erstattung) {
		this.erstattung = erstattung;
	}
	
	
	/**
	 * Liefert Liste mit Härten und deren Status (angegeben, anerkannt, abgelehnt)
	 * @return Liste mit Antragshärten
	 */
	public List<AntragHaerte> getHaerteListe() {
		return haerteListe;
	}
    
	
	/**
	 * Setzt Liste mit Härten und deren Status (angegeben, anerkannt, abgelehnt)
	 * @param haerteListe v
	 */
	public void setHaerteListe(List<AntragHaerte> haerteListe) {
		this.haerteListe = haerteListe;
	}


	/**
	 * Liefert ID von User, welche/r den Antrag angelegt hat
	 * @return User-ID
	 */
	public int getUserAngelegt() {
		return userAngelegt;
	}


	/**
	 * Setzt ID von User, welche/r den Antrag angelegt hat
	 * @param userAngelegt User-ID
	 */
	public void setUserAngelegt(int userAngelegt) {
		this.userAngelegt = userAngelegt;
	}


	/**
	 * Liefert ID von User, welche/r den Antrag zuletzt bearbeitet hat
	 * @return User-ID
	 */
	public int getUserGeaendert() {
		return userGeaendert;
	}


	/**
	 * Setzt ID von User, welche/r den Antrag zuletzt bearbeitet hat
	 * @param userGeaendert User-ID
	 */
	public void setUserGeaendert(int userGeaendert) {
		this.userGeaendert = userGeaendert;
	}
	
	
	/**
	 * Liefert Zeitstempel wann Antrag angelegt wurde
	 * @return Zeitstempel Antrag angelegt
	 */
	public GregorianCalendar getDatumAngelegt() {
		return datumAngelegt;
	}


	/**
	 * Setzt Zeitstempel wann Antrag angelegt wurde
	 * @param datumAngelegt Zeitstempel Antrag angelegt
	 */
	public void setDatumAngelegt(GregorianCalendar datumAngelegt) {
		this.datumAngelegt = datumAngelegt;
	}


	/**
	 * Liefert Zeitstempel wann Antrag zuletzt geändert wurde
	 * @return Zeitstempel Antrag zuletzt geändert
	 */
	public GregorianCalendar getDatumGeaendert() {
		return datumGeaendert;
	}


	/**
	 * Setzt Zeitstempel wann Antrag zuletzt geändert wurde
	 * @param datumGeaendert Zeitstempel Antrag zuletzt geändert
	 */
	public void setDatumGeaendert(GregorianCalendar datumGeaendert) {
		this.datumGeaendert = datumGeaendert;
	}


    public boolean isCharite() {
        return charite;
    }

    public void setCharite(boolean charite) {
        this.charite = charite;
    }

	@Override
	public int compareTo(Object o) {
		return new Integer(this.antragID).compareTo(new Integer(((Antrag) o).antragID));
	}
}
