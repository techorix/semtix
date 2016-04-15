/*

  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
         Semesterticketbüro der Humboldt-Universität Berlin

  Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */


package org.semtix.shared.daten.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Ablehnungsgründe für alle Antragshärten
 */
public enum HaerteAblehnungsgrund {

	A (1, "Wurde nicht anerkannt","Nach § 2 Absatz 2 Nr. 11 der Sozialfondssatzung kann in Abstimmung mehrerer MitarbeiterInnen des Semesterticketbüros in Einzelfällen eine sonstige vergleichbare soziale Härte anerkannt werden. Wir haben uns bezüglich der in deinem Antrag angegebenen Situation gegen die Vergabe der 5 zusätzlichen Härtepunkte für eine sonstige vergleichbare Härte entschieden."),
    B (2, "Kosten sind keine Härte", "Die von dir angegebene sonstige Härte bezog sich auf Kosten, die dir im relevanten Zeitraum entstanden sind. Sofern diese Kosten nachgewiesen wurden, sind sie in der Bedarfsberechnung berücksichtigt worden, eine sonstige Härte wird dafür jedoch nicht vergeben."),
	C (3, "Ist bereits durch eine andere Härte abgedeckt","Die von dir angegebene Härte wird bereits durch eine andere Härte abgedeckt."),
	D (4, "Anderes Verständnis der Härte","Der von dir eingereichte Nachweis entspricht nicht § 2 Absatz 2 der Sozialfondssatzung. Die Härte ist deshalb nicht gegeben."),
	E (5, "Nicht nachgewiesen","Da du die Härte trotz wiederholter Nachfragen nicht nachgewiesen hast, konnten wir sie in deinem Antrag leider nicht berücksichtigen."),
	F (6, "Kind ist volljährig","Du hast angegeben eine/n Haushaltsängehörige/n unter achtzehn Jahren zu erziehen. Aus den Nachweisen geht aber hervor, dass diese/r Haushaltsanghörige volljährig ist."),
	G (7, "Nicht alle Nachweise wurden anerkannt","Nicht alle von dir eingereichten Nachweise entsprechen § 2 Absatz 2 Nr. 10 der Sozialfondssatzung. Eine besondere Härte nach § 2 Absatz 2 Nr. 10 der Sozialfonds-Satzung ist daher leider nicht gegeben."),
	H (8, "Zu wenige Stunden / zu kurz","Das von dir angegebene Praktikum unterschritt die Länge von 3 Monaten und/oder die Arbeitszeit von 30 Stunden wöchentlich. Eine besondere Härte nach §2 Abs.2 der Sozialfonds-Satzung ist daher leider nicht gegeben."),
	I (9, "Ausreichendes Einkommen","Das Einkommen überschreitet den Bedarf im Sinne von §2 Absatz 3 und 4 der Sozialfondssatzung. Deshalb kann dem Antrag trotz eventuell anerkannter Härten (s.u.) nicht stattgegeben werden."),
	J (10, "Nicht im BRZ","Die von dir angegebene Härte lag nicht im Berechnungszeitraum. Eine besondere Härte nach §2 Abs.2 der Sozialfonds-Satzung ist daher leider nicht gegeben."),
	K (11, "EU-Arbeitnehmer_Innenfreizügigkeit","Seit dem 1. Januar 2014 bestehen für alle Mitgliedsstaaten der Europäischen Union keine Arbeitsbeschränkungen mehr, somit kann die Härte Eingeschränkte Arbeitserlaubnis nicht gegeben werden."),
	L (12, "Keine Abschlussprüfung","Studienbegleitende Prüfungen sind keine Härte im Sinne der Sozialfonds-Satzung nach §2 Abs. 2 Nr. 1 (Studienabschlussphase)."),
	M (13, "Nicht im BRZ + Toleranzzeitraum","Die von dir angegebene Abschlussprüfung oder Anmeldung zum Studienabschluss lag außerhalb unseres Berechnungszeitraums. Die Härte „Studienabschluss“ kann daher nicht vergeben werden. Du kannst diese Härte möglicherweise im nächsten Antrag geltend machen."),
	N (14, "Schwangerschaft wird zu Kind","Auf Grund §4 Abs. 2 S. 2 haben wir dir deine Schwangerschaft nicht anerkannt, da sich durch die Geburt deines Kindes im Berechnungszeitraum die besondere Härte „Erziehung eines/r Haushaltsangehörigen unter achtzehn Jahren“ nach §2 Abs.2 Nr. 8 der Sozialfonds-Satzung ergibt."),
	O (15, "Leistungen nach einem anderen SGB","Du hast Leistungen nach SGB II oder XII angegeben. Die von dir nachgewiesenen Leistungen ergeben sich aber nicht aus den Sozialgesetzbüchern II und/oder XII. Eine besondere Härte nach §2 Abs. 2 ist daher leider nicht gegeben."),
	P (16, "Schwangerschaft nicht nachgewiesen","Die von dir angegebene Schwangerschaft wurde nicht mit geeigneten Nachweisen belegt. Eine besondere Härte nach §2 Abs.2 der Sozialfonds-Satzung ist daher leider nicht gegeben."),
	Q (17, "Schwangerschaft nicht im BRZ","Die von dir angegebene Schwangerschaft lag nicht im Berechnungszeitraum. Eine besondere Härte nach §2 Abs.2 der Sozialfonds-Satzung ist daher leider nicht gegeben"),
	R(18, "Medizinische Kosten unter 250", "Die Kosten für medizinische oder psychologische Versorgung, welche nicht durch eine Krankenkasse getragen werden, überschritten innerhalb des Berechnungszeitraums laut deiner eingereichten Nachweise nicht den Betrag von 250 Euro. Eine besondere Härte nach §2 Abs.2 Nr. 10 der Sozialfonds-Satzung ist daher leider nicht gegeben. Wir haben dir jedoch den Betrag als Mehrbedarf nach §2 Abs. 3 der Sozialfonds-Satzung angerechnet."),
	S(19, "Kind nicht im BRZ geboren", "Da dein Kind erst nach Ende des Berechnungszeitraums geboren wurde, können wir dir die Härte Erziehung eines/einer Haushaltsangehörigen unter 18 Jahren (§ 2 Abs. 2 Nr. 6) nicht anrechnen.");


	private static Map<Integer, HaerteAblehnungsgrund> idToStatusMapping;
	private int id;
	private String name;
	private String begruendung;


	HaerteAblehnungsgrund(int id, String name, String begruendung) {
		this.id = id;
		this.name = name;
		this.begruendung = begruendung;
	}

	// holt Härtegrund anhand der ID
	public static HaerteAblehnungsgrund getHaertegrundByID(int i) {
		if(idToStatusMapping == null) {
			initMapping();
		}
		return idToStatusMapping.get(i);
	}

	/**
	 * schreibt statusID und AntragStatus in eine Map, damit auch über 
	 * die ID auf den AntragStatus zugegriffen werden kann
	 */
	private static void initMapping() {
		idToStatusMapping = new HashMap<Integer, HaerteAblehnungsgrund>();
		for (HaerteAblehnungsgrund a : values()) {
			idToStatusMapping.put(a.id, a);
		}
	}
	
	
	/**
	 * Liefert die StatusID
	 * @return StatusID
	 */
	public int getID() {
		return id;
	}

	public String toString() {
		return name;
	}

	public String getBegruendung() {
		return begruendung;
	}

}
