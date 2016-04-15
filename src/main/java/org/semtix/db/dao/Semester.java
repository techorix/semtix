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

import org.semtix.config.UniConf;
import org.semtix.shared.daten.enums.SemesterArt;
import org.semtix.shared.daten.enums.Uni;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.GregorianCalendar;

/**
 * Klasse für Objekte von Semesterdaten (entspricht der Datenbanktabelle semester).
 * 
 * <p>In Objekten der Klasse <code>Semester</code> werden Informationen zu den angelegten Semstern 
 * erfasst. Es kann pro Universität immer nur jeweils ein Winter- und Sommersemester pro 
 * Jahr geben. In der Datenbank werden die Semesterarten (Winter, Sommer) durch Anhängen der 
 * Buchstaben W bzw. S an die Jahreszahl gespeichert. Beispiele: 2012W für Wintersemester 2012/13, 
 * 2012S für Sommersemester 2012. Bei den Wintersemestern ist immer die Jahreszahl ausschlaggebend, 
 * in welchem das Semester beginnt.</p>
 */
@Entity
public class Semester implements Serializable, Comparable {

    @Id
    @SequenceGenerator(name = "semesterID_seq",
            sequenceName = "semesterID_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "semesterID_seq")
    private int semesterID;						// ID des Semesters (Primärschlüssel in Datenbank)
	private int antraegeBewilligt;				// Anzahl der bewilligten Anträge im Semester
	private int punkteVergeben;					// Summe der vergebenen Punkte für Anträge im Semester
	private int punkteVoll;						// Punktzahl, ab wann es einen Vollzuschuss gibt

    @Enumerated
    private Uni uni;							// Universität, für welche das Semester angelegt wurde
	
	private String semesterJahr;				// Jahr des Semesters (Jahreszahl des Semesterbeginns)

    @Enumerated
    private SemesterArt semesterArt;			// Konstante (Enum) Semesterart: Sommersemester, Wintersemester
	
	private GregorianCalendar semesterAnfang;	// Datum, wann das Semester beginnt
	private GregorianCalendar stichtag;			// Stichtag, wann die Zuschüsse berechnet werden
	
	private BigDecimal beitragTicket;			// Geldbetrag des Semestertickets
	private BigDecimal beitragFonds;			// Geldbetrag des Fonds (Betrag der im Semester ausgeschüttet werden kann)
	private BigDecimal sozialfonds;				// Geldbetrag des Sozialfonds
	private BigDecimal punktWert;				// Geldbetrag, wieviel 1 Punkt wert ist


	private BigDecimal bedarfGrund;
	private BigDecimal bedarfKind;
	private BigDecimal bedarfWeiterePerson;
	private BigDecimal bedarfSchwangerschaft;
	private BigDecimal bedarfAlleinerziehend;
	private BigDecimal chronischKrank;
	private BigDecimal heizkostenpauschale;
	private BigDecimal kappungMiete;
	private BigDecimal auslandskosten;
	private BigDecimal medKosten;
	private BigDecimal schulden;
	private BigDecimal abcTarif;
	private BigDecimal kindergeld;
	private BigDecimal kindergeld2;
	private BigDecimal kindergeld3;




	/**
	 * Konstruktor mit Standardwerten
	 */
	public Semester() {


    }
	
	
	/**
	 * Konstruktor mit Übergabe der Felder als Parameter
	 * @param semesterID ID des Semesters (Primärschlüssel in Datenbank)
	 * @param uni Universität, für welche das Semester angelegt wurde
	 * @param antraegeBewilligt Anzahl der bewilligten Anträge im Semester
	 * @param punkteVergeben Summe der vergebenen Punkte für Anträge im Semester
	 * @param punkteVoll Punktzahl, ab wann es einen Vollzuschuss gibt
	 * @param semesterJahr Jahr des Semesters (Jahreszahl des Semesterbeginns)
	 * @param semesterArt Konstante (Enum) Semesterart: Sommersemester, Wintersemester
	 * @param semesterAnfang Datum, wann das Semester beginnt
	 * @param stichtag Stichtag, wann die Zuschüsse berechnet werden
	 * @param beitragTicket Geldbetrag des Semestertickets
	 * @param beitragFonds Geldbetrag des Fonds (Betrag der im Semester ausgeschüttet werden kann)
	 * @param sozialfonds Geldbetrag des Sozialfonds
	 * @param punktWert Geldbetrag, wieviel 1 Punkt wert ist
	 */
	public Semester(int semesterID, Uni uni, int antraegeBewilligt, int punkteVergeben,
			int punkteVoll, String semesterJahr, SemesterArt semesterArt, 
			GregorianCalendar semesterAnfang, GregorianCalendar stichtag,
			BigDecimal beitragTicket, BigDecimal beitragFonds,
			BigDecimal sozialfonds, BigDecimal punktWert) {
		
		
		this.semesterID = semesterID;
		this.uni = uni;
		this.antraegeBewilligt = antraegeBewilligt;
		this.punkteVergeben = punkteVergeben;
		this.punkteVoll = punkteVoll;
		this.semesterJahr = semesterJahr;
		this.semesterArt = semesterArt;
		this.semesterAnfang = semesterAnfang;
		this.stichtag = stichtag;
		this.beitragTicket = beitragTicket;
		this.beitragFonds = beitragFonds;
		this.sozialfonds = sozialfonds;
		this.punktWert = punktWert;
	}


    public void initObjectWithDefaultValues() {
        // Standardwerte für neue Semester setzen
        semesterID = -1;
        antraegeBewilligt = 0;
        punkteVergeben = 0;
        punkteVoll = 0;
        uni = UniConf.aktuelleUni;
        semesterJahr = "";
        semesterArt = null;
        semesterAnfang = null;
        stichtag = null;
        beitragTicket = new BigDecimal("0.0");
        beitragFonds = new BigDecimal("0.0");
        sozialfonds = new BigDecimal("0.0");
        punktWert = new BigDecimal("0.0");
    }

	/**
	 * Liefert ID des Semesters (Primärschlüssel in Datenbank)
	 * @return Semester-ID
	 */
	public int getSemesterID() {
		return semesterID;
	}

	/**
	 * Setzt ID des Semesters (Primärschlüssel in Datenbank)
	 * @param semesterID Semester-ID
	 */
	public void setSemesterID(int semesterID) {
		this.semesterID = semesterID;
	}

	/**
	 * Liefert Universität für das Semester
	 * @return Universität
	 */
	public Uni getUni() {
		return uni;
	}

	/**
	 * Setzt Universität für das Semester
	 * @param uni Universität
	 */
	public void setUni(Uni uni) {
		this.uni = uni;
	}

	/**
	 * Liefert Anzahl der bewilligten Anträge im Semester
	 * @return Anzahl bewilligte Anträge
	 */
	public int getAntraegeBewilligt() {
		return antraegeBewilligt;
	}

	/**
	 * Setzt Anzahl der bewilligten Anträge im Semester
	 * @param antraegeBewilligt Anzahl bewilligte Anträge
	 */
	public void setAntraegeBewilligt(int antraegeBewilligt) {
		this.antraegeBewilligt = antraegeBewilligt;
	}

	/**
	 * Liefert Summe der vergebenen Punkte im Semester
	 * @return Summe der vergebenen Punkte
	 */
	public int getPunkteVergeben() {
		return punkteVergeben;
	}

	/**
	 * Setzt Summe der vergebenen Punkte im Semester
	 * @param punkteVergeben Summe der vergebenen Punkte
	 */
	public void setPunkteVergeben(int punkteVergeben) {
		this.punkteVergeben = punkteVergeben;
	}

	/**
	 * Liefert die Anzahl Punkte, ab denen es einen Vollzuschuss gibt
	 * @return Anzahl Punkte
	 */
	public int getPunkteVoll() {
		return punkteVoll;
	}

	/**
	 * Setzt die Anzahl Punkte, ab denen es einen Vollzuschuss gibt
	 * @param punkteVoll Anzahl Punkte
	 */
	public void setPunkteVoll(int punkteVoll) {
		this.punkteVoll = punkteVoll;
	}

	/**
	 * Liefert das Semesterjahr (Jahreszahl Beginn des Semesters)
	 * @return Semesterjahr
	 */
	public String getSemesterJahr() {
		return semesterJahr;
	}

	/**
	 * Setzt das Semesterjahr (Jahreszahl Beginn des Semesters)
	 * @param semesterJahr Semesterjahr
	 */
	public void setSemesterJahr(String semesterJahr) {
		this.semesterJahr = semesterJahr;
	}

    /**
     * Liefert die Art des Semesters (Sommersemester, Wintersemester)
     * @return Semesterart (Enum)
	 */
	public SemesterArt getSemesterArt() {
		return semesterArt;
	}

	/**
	 * Setzt die Art des Semesters (Sommersemester, Wintersemester)
	 * @param semesterArt Semesterart (Enum)
	 */
	public void setSemesterArt(SemesterArt semesterArt) {
		this.semesterArt = semesterArt;
	}

	/**
	 * Liefert das Datum des Semesteranfangs
	 * @return Datum des Semesteranfangs
	 */
	public GregorianCalendar getSemesterAnfang() {
		return semesterAnfang;
	}

	/**
	 * Setzt das Datum des Semesteranfangs
	 * @param semesterAnfang Datum des Semesteranfangs
	 */
	public void setSemesterAnfang(GregorianCalendar semesterAnfang) {
		this.semesterAnfang = semesterAnfang;
	}

	/**
	 * Liefert das Datum des Stichtags der Zuschussberechnung
	 * @return Datum des Stichtags
	 */
	public GregorianCalendar getStichtag() {
		return stichtag;
	}

	/**
	 * Setzt das Datum des Stichtags der Zuschussberechnung
	 * @param stichtag Datum des Stichtags
	 */
	public void setStichtag(GregorianCalendar stichtag) {
		this.stichtag = stichtag;
	}

	/**
	 * Liefert den Geldbetrag für das Semester-Ticket
	 * @return Geldbetrag für das Semester-Ticket
	 */
	public BigDecimal getBeitragTicket() {
		return beitragTicket;
	}

	/**
	 * Setzt den Geldbetrag für das Semester-Ticket
	 * @param beitragTicket Geldbetrag für das Semester-Ticket
	 */
	public void setBeitragTicket(BigDecimal beitragTicket) {
		this.beitragTicket = beitragTicket;
	}

	/**
	 * Liefert den Geldbetrag für den Fonds, der für die Zuschüsse im Semester ausgeschüttet werden kann
	 * @return Geldbetrag für den Fonds
	 */
	public BigDecimal getBeitragFonds() {
		return beitragFonds;
	}

	/**
	 * Setzt den Geldbetrag für den Fonds, der für die Zuschüsse im Semester ausgeschüttet werden kann
	 * @param beitragFonds Geldbetrag für den Fonds
	 */
	public void setBeitragFonds(BigDecimal beitragFonds) {
		this.beitragFonds = beitragFonds;
	}

	/**
	 * Liefert den Geldbetrag für den Sozialfonds für das Semester
	 * @return Geldbetrag für den Sozialfonds
	 */
	public BigDecimal getSozialfonds() {
		return sozialfonds;
	}

	/**
	 * setzt den Geldbetrag für den Sozialfonds für das Semester
	 * @param sozialfonds Geldbetrag für den Sozialfonds
	 */
	public void setSozialfonds(BigDecimal sozialfonds) {
		this.sozialfonds = sozialfonds;
	}

	/**
	 * Liefert den Geldbetrag, der 1 Punkt wert ist
	 * @return Punktwert
	 */
	public BigDecimal getPunktWert() {
		return punktWert;
	}

    /**
     * Setzt den Geldbetrag, der 1 Punkt wert ist
     * @param punktWert Punktwert
	 */
	public void setPunktWert(BigDecimal punktWert) {
		this.punktWert = punktWert;
	}

	/**
	 * Liefert String mit Bezeichnung des Semesters (Bezeichnung + Jahr)
	 * @return Bezeichnung des Semesters (Bezeichnung + Jahr)
	 */
	public String getSemesterBezeichnung() {

		String semesterBezeichnung = "Semester???";

        if (semesterArt == SemesterArt.SOMMER)
            semesterBezeichnung = semesterArt.getBezeichnung() + " " + semesterJahr;

        else if (semesterArt == SemesterArt.WINTER)
            semesterBezeichnung = semesterArt.getBezeichnung() + " " + semesterJahr + "/" + String.valueOf(Integer.valueOf(semesterJahr) + 1).substring(2);

        return semesterBezeichnung;

    }

    public String getNextSemesterBezeichnung() {
        String semesterBezeichnung = "Semester???";


        if(semesterArt == SemesterArt.SOMMER)
            semesterBezeichnung = SemesterArt.WINTER.getBezeichnung() + " " + semesterJahr + "/" + String.valueOf(Integer.valueOf(semesterJahr)+1).substring(2);

        else if(semesterArt == SemesterArt.WINTER)
            semesterBezeichnung = SemesterArt.SOMMER.getBezeichnung() + " " + String.valueOf(Integer.valueOf(semesterJahr) + 1);

        return semesterBezeichnung;

    }
	
	/**
	 * Liefert String mit Kurzform des Semesters (Kurzform + Jahr)
	 * @return Kurzform des Semesters (Kurzform + Jahr)
	 */
	public String getSemesterKurzform() {

        String semesterKurzform = "Semester???";

        if (null == semesterArt)
            return semesterKurzform;

		if(semesterArt == SemesterArt.SOMMER)
			semesterKurzform = semesterArt.getKurzform() + " " + semesterJahr;

        if (semesterArt == SemesterArt.WINTER)
            semesterKurzform = semesterArt.getKurzform() + " " + semesterJahr + "/" + String.valueOf(Integer.valueOf(semesterJahr) + 1).substring(2);

        return semesterKurzform;

    }


    /**
     * toString-Methode überschrieben: Liefert String mit Kurzform des Semesters (Kurzform + Jahr)
     */
	public String toString() {
		return getSemesterKurzform();
	}
	
	/**
	 * Semesterwerte zurücksetzen (reset)
	 */
	public void reset() {

        this.antraegeBewilligt = 0;
        this.punkteVergeben = 0;
        this.punkteVoll = 0;
		this.stichtag = null;
		this.beitragTicket = new BigDecimal("0.0");
		this.beitragFonds = new BigDecimal("0.0");
		this.sozialfonds = new BigDecimal("0.0");
		this.punktWert = new BigDecimal("0.0");

	}

    public String getNextAntragFrist() {
        String antragfrist = "";


        if(semesterArt == SemesterArt.SOMMER) {
            antragfrist = "vom 01.Juni bis zum 31.Juli " + semesterJahr;

        } else if(semesterArt == SemesterArt.WINTER) {
            antragfrist = "vom 01.Januar bis zum ";
            int nextYear = Integer.valueOf(semesterJahr) + 1;
            int februarydays = 28;
            GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance();
            if (cal.isLeapYear(nextYear)) {
                februarydays++;
            }
            antragfrist += februarydays + ".Februar " + nextYear;

        }
        return antragfrist;
    }


	@Override
	public int compareTo(Object o) {
		return this.getSemesterAnfang().compareTo(((Semester) o).getSemesterAnfang());
	}

	public BigDecimal getBedarfGrund() {
		return bedarfGrund;
	}

	public void setBedarfGrund(BigDecimal bedarfGrund) {
		this.bedarfGrund = bedarfGrund;
	}

	public BigDecimal getBedarfKind() {
		return bedarfKind;
	}

	public void setBedarfKind(BigDecimal bedarfKind) {
		this.bedarfKind = bedarfKind;
	}

	public BigDecimal getBedarfWeiterePerson() {
		return bedarfWeiterePerson;
	}

	public void setBedarfWeiterePerson(BigDecimal bedarfWeiterePerson) {
		this.bedarfWeiterePerson = bedarfWeiterePerson;
	}

	public BigDecimal getBedarfSchwangerschaft() {
		return bedarfSchwangerschaft;
	}

	public void setBedarfSchwangerschaft(BigDecimal bedarfSchwangerschaft) {
		this.bedarfSchwangerschaft = bedarfSchwangerschaft;
	}

	public BigDecimal getBedarfAlleinerziehend() {
		return bedarfAlleinerziehend;
	}

	public void setBedarfAlleinerziehend(BigDecimal bedarfAlleinerziehend) {
		this.bedarfAlleinerziehend = bedarfAlleinerziehend;
	}

	public BigDecimal getHeizkostenpauschale() {
		return heizkostenpauschale;
	}

	public void setHeizkostenpauschale(BigDecimal heizkostenpauschale) {
		this.heizkostenpauschale = heizkostenpauschale;
	}

	public BigDecimal getKappungMiete() {
		return kappungMiete;
	}

	public void setKappungMiete(BigDecimal kappungMiete) {
		this.kappungMiete = kappungMiete;
	}

	public BigDecimal getAuslandskosten() {
		return auslandskosten;
	}

	public void setAuslandskosten(BigDecimal auslandskosten) {
		this.auslandskosten = auslandskosten;
	}

	public BigDecimal getMedKosten() {
		return medKosten;
	}

	public void setMedKosten(BigDecimal medKosten) {
		this.medKosten = medKosten;
	}

	public BigDecimal getSchulden() {
		return schulden;
	}

	public void setSchulden(BigDecimal schulden) {
		this.schulden = schulden;
	}

	public BigDecimal getAbcTarif() {
		return abcTarif;
	}

	public void setAbcTarif(BigDecimal abcTarif) {
		this.abcTarif = abcTarif;
	}

	public BigDecimal getKindergeld() {
		return kindergeld;
	}

	public void setKindergeld(BigDecimal kindergeld) {
		this.kindergeld = kindergeld;
	}

	public BigDecimal getKindergeld2() {
		return kindergeld2;
	}

	public void setKindergeld2(BigDecimal kindergeld2) {
		this.kindergeld2 = kindergeld2;
	}

	public BigDecimal getKindergeld3() {
		return kindergeld3;
	}

	public void setKindergeld3(BigDecimal kindergeld3) {
		this.kindergeld3 = kindergeld3;
	}

	public BigDecimal getChronischKrank() {
		return chronischKrank;
	}

	public void setChronischKrank(BigDecimal chronischKrank) {
		this.chronischKrank = chronischKrank;
	}
}
