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

package org.semtix.config;

import org.apache.log4j.Logger;
import org.semtix.db.DBHandlerConf;
import org.semtix.db.DBHandlerSemester;
import org.semtix.db.dao.Semester;

import java.math.BigDecimal;

/**
 * Globale Werte für den Berechnungszettel. Werden in Datenbank (conf) gespeichert.
 */
public class Berechnung {
	private static Logger logger = Logger.getLogger(Berechnung.class);


	//Punktzahl für eine anerkannte Härte
	public static final int HAERTE_PUNKTZAHL = 5;
	//Betrag für Kindergeld
	public static BigDecimal KINDERGELD;
	public static BigDecimal KINDERGELD2;
	public static BigDecimal KINDERGELD3;
	// Betrag für Grundbedarf
	public static BigDecimal GRUNDBEDARF;
	// Betrag für Bedarf Kind
	public static BigDecimal KIND;
	// Betrag für Bedarf weitere Personen
	public static BigDecimal WEITERE_PERSON;
	// Betrag für Schwangerschaft
	public static BigDecimal SCHWANGERSCHAFT;
	// Betrag für Alleinerziehend
	public static BigDecimal ALLEINERZIEHEND;
	// Betrag für chronisch krank
	public static BigDecimal CHRONISCH_KRANK;
	// Betrag für Heiz.-Pauschale (Multiplikator je nach Anzahl Personen)
	public static BigDecimal HEIZPAUSCHALE;
	// Betrag für Kappungsgrenze der Miete
	public static BigDecimal KAPPUNG_MIETE;
	// Betrag für Auslandskosten
	public static BigDecimal AUSLANDSKOSTEN;
	// Betrag für Med./Psych. Kosten (auf 6 Monate)
	public static BigDecimal MED_PSYCH_KOSTEN;
	// Betrag für Schuldenanteil Einkommen (Prozent)
	public static BigDecimal SCHULDEN;
	// Betrag für ABC-Tarif (Fahrkarte)
	public static BigDecimal ABC_TARIF;


	public static void init() {
		Semester semester = SemesterConf.getSemester();
		if (semester.getBedarfAlleinerziehend() == null
				|| semester.getBedarfKind() == null
				|| semester.getChronischKrank() == null
				|| semester.getKindergeld() == null
				) {
			logger.warn("Schreibe Berechnungswerte aus Tabelle Conf in aktuelles Semester");
			fillSemester(semester);
			new DBHandlerSemester().updateSemester(semester);
			init();
		} else {
			werteZuweisen(semester);
		}
	}

	public static BigDecimal getValue(String key) {
		DBHandlerConf dbHandlerConf = new DBHandlerConf();
		String result = dbHandlerConf.read(key);
		if (null == result || result.length() == 0)
			return BigDecimal.ZERO;
		else
			return new BigDecimal(result);
	}

	public static void fillSemester(Semester semester) {
		semester.setBedarfGrund(getValue("Grundbedarf"));
		semester.setBedarfKind(getValue("Kind"));
		semester.setBedarfWeiterePerson(getValue("Weitere_Person"));
		semester.setBedarfSchwangerschaft(getValue("Schwangerschaft"));
		semester.setBedarfAlleinerziehend(getValue("Alleinerziehend"));
		semester.setChronischKrank(getValue("Chronisch_Krank"));
		semester.setHeizkostenpauschale(getValue("Heizpauschale"));
		semester.setKappungMiete(getValue("Kappung_Miete"));
		semester.setAuslandskosten(getValue("Auslandskosten"));
		semester.setMedKosten(getValue("Med_Psych_Kosten"));
		semester.setSchulden(getValue("Schulden"));
		semester.setAbcTarif(getValue("ABC_Tarif"));
		semester.setKindergeld(getValue("Kindergeld"));
		semester.setKindergeld2(getValue("Kindergeld2"));
		semester.setKindergeld3(getValue("Kindergeld3"));
	}

	private static void werteZuweisen(Semester semester) {
		GRUNDBEDARF = semester.getBedarfGrund();
		KIND = semester.getBedarfKind();
		WEITERE_PERSON = semester.getBedarfWeiterePerson();
		SCHWANGERSCHAFT = semester.getBedarfSchwangerschaft();
		ALLEINERZIEHEND = semester.getBedarfAlleinerziehend();
		CHRONISCH_KRANK = semester.getChronischKrank();
		HEIZPAUSCHALE = semester.getHeizkostenpauschale();
		KAPPUNG_MIETE = semester.getKappungMiete();
		AUSLANDSKOSTEN = semester.getAuslandskosten();
		MED_PSYCH_KOSTEN = semester.getMedKosten();
		SCHULDEN = semester.getSchulden();
		ABC_TARIF = semester.getAbcTarif();
		KINDERGELD = semester.getKindergeld();
		KINDERGELD2 = semester.getKindergeld2();
		KINDERGELD3 = semester.getKindergeld3();
	}
}

