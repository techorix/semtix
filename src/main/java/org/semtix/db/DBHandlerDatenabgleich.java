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

package org.semtix.db;

import org.semtix.config.SemesterConf;
import org.semtix.config.SettingsExternal;
import org.semtix.db.dao.Antrag;
import org.semtix.db.dao.Person;
import org.semtix.shared.daten.enums.AntragStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse für Datenbankaktionen für Datenbankabgleich
 * 
 * Der Datenbankabgleich ist lediglich ein Check ob der Antragsteller überhaupt Immatrikuliert ist. Hierbei werden <b>alle</b> Antragsteller
 * berücksichtigt und mit dem Immatrikulationsbüro abgeglichen. Es werden also auch unvollständige, bereits abgelehnte Anträge behandelt. Diese müssen
 * dann später herausgefiltert werden.
 *
 * Diese Klasse ist hauptsächlich auf Grund der Migration von SQL nach Hibernate nötig. Wenn die Datenbank anders angelegt worden wäre, hätte man über die jeweiligen Objekt-referenzen sehr einfach Zugriff.
 */
public class DBHandlerDatenabgleich {


	// ID der aktuell eingestellten Universität
	private int semID = 0;
    private DBHandlerPerson dbhandlerPerson;
    private DBHandlerAntrag dbhandlerAntrag;

    private List<Antrag> antraegeSemester;

	/**
	 * Konstruktor, erzeugt lokale Variablen aus Konfiguration, Semester ID Uni ID
	 *
	 */
	public DBHandlerDatenabgleich() {
		// ID der aktuell eingestellten Universität
		semID = SemesterConf.getSemester().getSemesterID();
        dbhandlerPerson = new DBHandlerPerson();
        dbhandlerAntrag = new DBHandlerAntrag();
        antraegeSemester = dbhandlerAntrag.getAntragListeSemester(semID);
    }

	/**
	 * Holt Liste mit Personen die (in diesem Semester) einen Antrag gestellt haben.
	 * 
	 * @return Liste Personen
	 */
	public List<Person> getListeAntragsteller() {

        List<Person> antragSteller = new ArrayList<Person>();

        boolean antraegeOhnePersonVorhanden = false;

        //gehe Liste aller Anträge des Semesters durch und hole Person Antragsteller
        //Ich gehe davon aus, dass die Semester eindeutig sind und auch die Uni schon berücksichtigt ist
        for (Antrag a : antraegeSemester) {
            Person p = dbhandlerPerson.getPersonById(a.getPersonID());
            if (null != p) {
                antragSteller.add(p);
            } else {
                if (SettingsExternal.DEBUG)
                    System.out.println("Antrag " + a.getAntragID() + " wird gelöscht, weil ihm keine Person zugeordnet ist.");
                //personensuche ist null, also Antrag löschen
                dbhandlerAntrag.delete(a);
                antraegeOhnePersonVorhanden = true;
            }
        }

        if (antraegeOhnePersonVorhanden) {
            antraegeSemester = dbhandlerAntrag.getAntragListeSemester(semID);
        }

		// ArrayList wird an aufrufende Funktion übergeben
		return antragSteller;

	}

	/**
	 * Findet Person an Hand der Matrikelnummer, Findet Antrag an Hand der Person und Setzt Antrag auf Teilzuschuss Setzt Zahl der Monate für Teilzuschuss
	 *
	 * @param monateZuschuss
	 *            Anzahl Monate die Zuschuss gewährt werden
	 * @param matrikelnummer
	 *            Matrikelnummer der betreffenden Person
	 */
	public void setAntragToTeilzuschuss(int monateZuschuss, String matrikelnummer) {

        Antrag a = getAntragByMatrikelNummer(matrikelnummer);

        if (null != a) {
           a.setTeilzuschuss(true);
           a.setAnzahlMonate(monateZuschuss);
            dbhandlerAntrag.updateAntrag(a);
       }
	}

	/**
	 * Lehnt Antrag ab
	 * 
	 * @param begruendung
	 *            Ablehnungsbegründung
	 * @param matrikelnummer
	 *            Matrikelnummer der betreffenden Person
	 */
	public void denyAntrag(String begruendung, String matrikelnummer) {

        Antrag a = getAntragByMatrikelNummer(matrikelnummer);

        if (null != a) {
            a.setBegruendung(begruendung);
            a.setAntragStatus(AntragStatus.ABGELEHNT);
            dbhandlerAntrag.updateAntrag(a);
        }
	}

    private Antrag getAntragByMatrikelNummer(String matrikelnummer) {
        Person p = dbhandlerPerson.getPersonByMatrikelnummer(matrikelnummer);
        for (Antrag a : antraegeSemester) {
            if (null != a) {
                if (a.getPersonID() == p.getPersonID())
                    return a;
            }
        }
        return null;
    }

}
