/*

  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
         Semesterticketbüro der Humboldt-Universität Berlin

  Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package org.semtix.db;

import org.semtix.config.Berechnung;
import org.semtix.config.SemesterConf;
import org.semtix.config.UniConf;
import org.semtix.db.dao.Antrag;
import org.semtix.db.dao.Person;
import org.semtix.db.dao.Semester;
import org.semtix.db.hibernate.HibernateCRUD;
import org.semtix.shared.daten.enums.Uni;

import java.util.ArrayList;
import java.util.List;


/**
 * DBHandler-Klasse zur Bearbeitung der Datenbank-Tabelle semester. Mithilfe dieser DBHandler-Klasse können in der Datenbank neue Semester angelegt,
 * bestehende Semester ausgelesen, geändert oder gelöscht werden.
 * 
 * {@link org.semtix.db.dao.Semester}
 */
public class DBHandlerSemester {


    HibernateCRUD<Semester> dbhandler;

    public DBHandlerSemester () {
        dbhandler = new HibernateCRUD<Semester>(Semester.class);
    }

    /**
     * @return ALL semesters
     */
    public List<Semester> getSemesterListe() {
        return dbhandler.getListOfAllElements();
    }

    /**
     * Methode zur Bestimmung der Semester in denen die Person noch keinen Antrag gestellt hat.
     *
     * Wenn eine Person neu angelegt werden soll und sie im aktuellen Semester schon einen Antrag angelegt hat, dann wird eine Combobox mit dieser Methode hier aufgerufen.
     *
     * Man kann dann in der Combobox ein alternatives Semester auswählen.
     *
     * Holt alle Anträge der Person und wenn in einem vergangenen Semester noch kein Antrag gestellt wurde, wird das Semester in die Liste aufgenommen
     *
     *
     * @param person Die betreffende Person
     *
     *               @return Rückgabe der Liste der Semester wo personensuche keinen Antrag gestellt hat (außer des aktuellen Semesters)
     *
	 */
	public List<Semester> getSemesterListe(Person person) {

        Uni uni = UniConf.aktuelleUni;

		if (null != person.getUni()) {
			uni = person.getUni();
		}

        List<Semester> allSemesters = dbhandler.getListOfAllElements();

        List<Antrag> alleAntraegeDerPerson = new DBHandlerAntrag().readAntragListe("personid",""+person.getPersonID());

        // aktuell eingestelltes Semester für Person anhand der uniID ermitteln
        int aktuelleSemesterID = (uni == Uni.HU) ? SemesterConf.semesterGlobalHU.getSemesterID() : SemesterConf.semesterGlobalKW.getSemesterID();

        List<Semester> semesterListe = new ArrayList<Semester>();

        for (Semester s : allSemesters) {
            if (s.getSemesterID() != aktuelleSemesterID && s.getUni().equals(uni)) {
                boolean schonAntragGestellt = false;
                for (Antrag a : alleAntraegeDerPerson) {
                    if (a.getSemesterID() == s.getSemesterID())
                        schonAntragGestellt = true;
                }

                if (! schonAntragGestellt)
                    semesterListe.add(s);
            }
        }

		return semesterListe;
	}

	/**
	 * Neues Semester in Datenbank anlegen (CREATE)
	 * 
	 * @param semester
	 *            Objekt Semester
	 */
    public void createSemester(Semester semester) {

        if (null == semester.getUni())
            semester.setUni(UniConf.aktuelleUni);

        //erst über Conf füllen
        Berechnung.fillSemester(semester);

        //dann über das letzte semester
        try {
            Semester currentSemester = SemesterConf.getSemester();
            semester.setBedarfGrund(currentSemester.getBedarfGrund());
            semester.setBedarfKind(currentSemester.getBedarfKind());
            semester.setBedarfWeiterePerson(currentSemester.getBedarfWeiterePerson());
            semester.setBedarfSchwangerschaft(currentSemester.getBedarfSchwangerschaft());
            semester.setBedarfAlleinerziehend(currentSemester.getBedarfAlleinerziehend());
            semester.setChronischKrank(currentSemester.getChronischKrank());
            semester.setHeizkostenpauschale(currentSemester.getHeizkostenpauschale());
            semester.setKappungMiete(currentSemester.getKappungMiete());
            semester.setAuslandskosten(currentSemester.getAuslandskosten());
            semester.setMedKosten(currentSemester.getMedKosten());
            semester.setSchulden(currentSemester.getSchulden());
            semester.setAbcTarif(currentSemester.getAbcTarif());
            semester.setKindergeld(currentSemester.getKindergeld());
            semester.setKindergeld2(currentSemester.getKindergeld2());
            semester.setKindergeld3(currentSemester.getKindergeld3());
        } catch (Exception e) {
            //egal obs klappt
        }


        dbhandler.create(semester);

	}

	/**
	 * Semester aus Datenbank lesen (SELECT)
	 * 
	 * @param semesterID
	 *            ID des gewünschten Semesters
	 * @return Objekt Semester
	 */
	public Semester readSemester(int semesterID) {

        return dbhandler.getByID(semesterID);

	}

	/**
	 * Bestimmtes Semester in Datenbank aktualisieren (UPDATE)
	 * 
	 * @param semester
	 *            Objekt Semester
	 */
	public void updateSemester(Semester semester) {



        dbhandler.update(semester);


	}

	/**
	 * Liest die Semesterdaten aus der Datenbank und gibt ArrayListe zurück.
	 * 
	 * @param uni
	 *            Universität, für welche die Semester zurückgegeben werden sollen
	 * @return Liste mit Semester-Objekten
	 */
	public List<Semester> getSemesterListe(Uni uni) {

        List<Semester> alleSemester = dbhandler.getListOfAllElements();
        List<Semester> semesterListe = new ArrayList<Semester>();

        for (Semester s : alleSemester) {
            if (s.getUni().getID() == uni.getID()) {
                semesterListe.add(s);
            }
        }

        return semesterListe;
	}




    public Semester getSemesterByID(int semesterID) {

        return dbhandler.getByID(semesterID);



    }

    public void delete(Semester semester) {
        dbhandler.delete(semester);
    }

	public void fixSemesterIds(int oldId, int newId) {

		Semester s = this.dbhandler.getByID(oldId);
		s.setSemesterID(newId);

		DBHandlerAntrag dbHandlerAntrag = new DBHandlerAntrag();
		for (Antrag a : dbHandlerAntrag.getAntragListeSemester(oldId)) {
			a.setSemesterID(newId);
			dbHandlerAntrag.updateAntrag(a);
		}

		dbhandler.update(s);
	}
}
