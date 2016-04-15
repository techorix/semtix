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

package org.semtix.db;

import org.semtix.db.dao.Anmerkung;
import org.semtix.db.dao.Person;
import org.semtix.db.hibernate.HibernateCRUD;
import org.semtix.shared.daten.enums.Uni;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * DBHandler-Klasse zur Bearbeitung der Datenbank-Tabelle anmerkungen.
 * Mithilfe dieser DBHandler-Klasse können in der Datenbank neue anmerkungen angelegt, bestehende Anmerkungen 
 * ausgelesen, geändert oder gelöscht werden.
 * @see org.semtix.db.dao.Anmerkung
 */
public class DBHandlerAnmerkungen {

    private HibernateCRUD<Anmerkung> dbhandler;

    //TODO optional: DB hier und da mal säubern, falls es doch noch Anmerkungen ohne Person gibt

    public DBHandlerAnmerkungen () {

       dbhandler  = new HibernateCRUD<Anmerkung>(Anmerkung.class);

    }


    public static void importFromFile(String datenpfad, String separator, Uni uni) throws IOException, ParseException {
        DBHandlerAnmerkungen dbHandlerAnmerkungen = new DBHandlerAnmerkungen();
        DBHandlerPerson dbHandlerPerson = new DBHandlerPerson();
        List<Person> antragsteller = dbHandlerPerson.getPersonenListe();

        //zwei maps, eine für jede Uni, wegen Matrikelnummer-Bug
        HashMap<String, Integer> personIdFormMatrikelnummerMap = new HashMap<String, Integer>();
        HashMap<String, Integer> personIdFormMatrikelnummerMapKW = new HashMap<String, Integer>();

        for (Person p : antragsteller) {
            if (personIdFormMatrikelnummerMap.containsKey(p.getMatrikelnr()) || personIdFormMatrikelnummerMapKW.containsKey(p.getMatrikelnr())) {
                System.out.println("Fehler: Matrikelnummer " + p.getMatrikelnr() + " doppelt vorhanden. Doppelter: " + p.getVorname() + " " + p.getNachname());
            } else {
                if (p.getUni().equals(Uni.KW))
                    personIdFormMatrikelnummerMapKW.put(p.getMatrikelnr(), p.getPersonID());
                else
                    personIdFormMatrikelnummerMap.put(p.getMatrikelnr(), p.getPersonID());
            }
        }


        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(datenpfad), "windows-1252"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] fields = line.split(separator);
            if (fields.length >= 5) {
                String matrikelnr = fields[1];
                String matrikelnrKW = "";
                if (uni == Uni.KW)
                    matrikelnrKW = matrikelnr.substring(matrikelnr.length() - 4, matrikelnr.length());

                String datum = fields[3];
                String text = fields[4];

                int personId = 0;
                //nur vorhandene Antragsteller bekommen Anmerkungen
                if (personIdFormMatrikelnummerMap.containsKey(matrikelnr)) {
                    personId = personIdFormMatrikelnummerMap.get(matrikelnr);
                } else if (uni == Uni.KW && personIdFormMatrikelnummerMapKW.containsKey(matrikelnrKW)) {
                    personId = personIdFormMatrikelnummerMapKW.get(matrikelnrKW);
                }

                if (personId > 0) {



                        int userId = 1;

					DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
					Date zeitstempelDa = formatter.parse(datum);
                        GregorianCalendar zeitstempelHier = new GregorianCalendar();
                        zeitstempelHier.setTime(zeitstempelDa);


                    for (Anmerkung anmerkung : dbHandlerAnmerkungen.getAnmerkungenListe(personId)) {
                        if (anmerkung.getZeitstempel().equals(zeitstempelHier)) {
                            dbHandlerAnmerkungen.deleteAnmerkung(anmerkung);
                        }
                    }

                        Anmerkung a = new Anmerkung();

                        a.setZeitstempel(zeitstempelHier);
                        a.setPersonId(personId);
                        a.setUserId(userId);
                        a.setText(text);

                        dbHandlerAnmerkungen.createAnmerkung(a);


                }
            }
        }
    }

    /**
     * Neue Anmerkung in Datenbank eintragen (CREATE).
     * @param anmerkung Objekt mit Anmerkungsdaten
     */
    public void createAnmerkung(Anmerkung anmerkung) {

        dbhandler.create(anmerkung);

    }

    /**
     * Anmerkung in Datenbank aktualisieren (UPDATE)
     * @param anmerkung Objekt mit Anmerkungsdaten
     */
    public void updateAnmerkung(Anmerkung anmerkung) {

        dbhandler.update(anmerkung);

	}

	/**
     * Anmerkung aus Datenbank löschen (DELETE)
     * @param anmerkung Anmerkung
     */
    public void deleteAnmerkung(Anmerkung anmerkung) {


        dbhandler.delete(anmerkung);


    }

    /**
     * Holt aus der Datenbak alle Anmerkungen zu einer bestimmten Person
     *
     * @param personID ID der Person, zu der alle Anmerkungen geholt werden sollen
     * @return Liste der Anmerkungen
     */
    public List<Anmerkung> getAnmerkungenListe(int personID) {

        List<Anmerkung> liste = dbhandler.getListOfAllElements();

        List<Anmerkung> results = new ArrayList<Anmerkung>();

        if (null != liste) {
            for (Anmerkung a : liste) {
                if (a.getPersonId() == personID) {
                    results.add(a);
                }
            }
        }
        return results;

	}
}



