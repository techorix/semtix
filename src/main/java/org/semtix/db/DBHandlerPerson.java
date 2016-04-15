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


import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.semtix.config.SemesterConf;
import org.semtix.config.UniConf;
import org.semtix.config.UserConf;
import org.semtix.db.dao.Anmerkung;
import org.semtix.db.dao.Antrag;
import org.semtix.db.dao.Person;
import org.semtix.db.dao.Semester;
import org.semtix.db.hibernate.HibernateCRUD;
import org.semtix.db.hibernate.HibernateUtil;
import org.semtix.shared.daten.StringHelper;
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
 * DBHandler-Klasse zur Bearbeitung der Datenbank-Tabelle personensuche.
 * Mithilfe dieser DBHandler-Klasse können in der Datenbank neue Personen angelegt, bestehende Personen 
 * ausgelesen, geändert oder gelöscht werden.
 * @see Person
 */
public class DBHandlerPerson {

    HibernateCRUD<Person> dbhandler;

    public DBHandlerPerson() {
        dbhandler = new HibernateCRUD<Person>(Person.class);
    }

    /**
     * Statische Methode die allePersonen durch geht und schaut ob der Vorname UND Nachname in gegebenenfalls auch einer anderen Schreibweise schon vorhanden ist
     * <br>
     * Ist keine direke Datenbankoperation, aber liegt hier, weil ggf. an anderer Stelle wiederverwendet und hier die Operateionen auf dao.Person gebündelt werden.
     *
	 * @param temp_vorname  zu suchender Vorname (trimmed)
	 * @param temp_nachname zu suchender Nachname (trimmed)
	 * @param allePersonen  in dieser Liste wird gesucht
     * @return id nr wenn gefunden, sonst -1
     */
    public static int checkName(String temp_vorname, String temp_nachname, List<Person> allePersonen) {

		temp_vorname = StringHelper.removeDiacriticalMarks(temp_vorname).toLowerCase();
		temp_nachname = StringHelper.removeDiacriticalMarks(temp_nachname).toLowerCase();

        for (Person p : allePersonen) {
			if (StringHelper.removeDiacriticalMarks(p.getVorname().toLowerCase()).equals(temp_vorname) && StringHelper.removeDiacriticalMarks(p.getNachname().toLowerCase()).equals(temp_nachname))
				return p.getPersonID();
        }

        return -1;

    }

	/**
	 * Importiert PersonenDaten
	 *
	 * @param argv irrelevanter Main-Methoden-Parameter
	 */
	public static void main(String[] argv) {
		try {
			importFromFile("/home/eins/daten/output", "\\$", Uni.KW);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
	 * Importiert Daten aus Datei für automatisches Füllen der Datenbank nach neuerstellung
	 *
	 * Geht bei KW davon aus, dass die Daten bereinigt sind, Bei UNI==HU werden alle Personen mit kurzer MaNr ignoriert
	 *
	 * @param datenpfad Absoluter Pfad
	 * @param separator Trennzeichen zwischen Ordnern (z.B. \ oder / )
	 * @param uni Relevante Uni
	 * @throws IOException Wenn Datei nicht gefunden
	 * @throws ParseException Wenn Datei Formatfehler enthält
	 */
	public static void importFromFile(String datenpfad, String separator, Uni uni) throws IOException, ParseException {
		DBHandlerPerson dbHandlerPerson = new DBHandlerPerson();

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(datenpfad), "windows-1252"));
		String line;
		while ((line = br.readLine()) != null) {
			String[] fields = line.split(separator);
			String matrikelnr = fields[0];

			//nur für HU:
			if (matrikelnr.length() > 4 && null != dbHandlerPerson.getPersonByMatrikelnummer(matrikelnr)) {

				Person person = dbHandlerPerson.getPersonByMatrikelnummer(matrikelnr);

				String nachname = fields[1];
				String vorname = fields[2];
				String gebdatum = fields[3];
				String co = fields[5];
				String strasse = fields[6];
				String wen = fields[7];
				String plz = fields[8];
				String stadt = fields[9];
				String land = fields[10];
				String email = fields[12];
				String barauszahler = fields[13];
				String ibanbic = fields[14];

				System.out.println(matrikelnr);


				if (gebdatum.length() > 0) {
					DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
					Date geburtstag = formatter.parse(gebdatum);
					GregorianCalendar geburtszeit = new GregorianCalendar();
					geburtszeit.setTime(geburtstag);
					person.setGebdatum(geburtszeit);
				}

				person.setBarauszahler(barauszahler.contains("1"));

				if (!person.isBarauszahler()) {
					continue;
				}


				person.setMatrikelnr(matrikelnr);
				person.setNachname(nachname);
				person.setVorname(vorname);

				person.setCo(co);
				person.setStrasse(strasse);
				person.setWohneinheit(wen);
				person.setPlz(plz);
				person.setWohnort(stadt);
				person.setLand(land);
				person.setEmail(email);

				person.setUni(uni);

				dbHandlerPerson.updatePerson(person);
			}
			//sonst löschen!
		}

    }

    /**
     *
	 * @param temp_matrikelnr zu suchende Matrikelnummer
	 * @return person_id nr wenn gefunden, sonst -1
	 * @see DBHandlerPerson#checkName(String, String, List)
	 */
	public int checkMatrikelnummer(String temp_matrikelnr) {

		Person p = dbhandler.read("matrikelnr", temp_matrikelnr);

		if (p != null)
			return p.getPersonID();

		return -1;
	}

    /**
     * Holt alle nicht-archivierten Personen aus der DB
     *
     * @param uni     Universitäts-Enum KW oder HU
     * @return Liste von Antragsteller für die uni
     */
    public List<Person> getPersonenListe(Uni uni) {
        return dbhandler.readList("uni", "" + (uni.getID() - 1), "archiviert", "f", "nachname", "vorname");
    }

    /**
     * @see #getPersonenListe(org.semtix.shared.daten.enums.Uni)
     *
     * Holt wirklich ALLE Personen aus der Datenbank
     * Wird wirklich nur in Sonderfällen wie Datenimport benötigt
     *
     * @return ALLE PERSONEN
     */
    public List<Person> getPersonenListe() {

        return dbhandler.getListOfAllElements();
    }

    /**
     * Neue Person in Datenbank anlegen (CREATE).
     * @param person Objekt mit Persondaten
     * @return Primärschlüssel der neuangelegten Person
     */
	public int createPerson(Person person) {

        if (null != UserConf.CURRENT_USER) {
            person.setUserGeaendert(UserConf.CURRENT_USER.getUserID());

            person.setUserAngelegt(UserConf.CURRENT_USER.getUserID());
        }


        person.setDatumGeaendert(new GregorianCalendar());

        person.setDatumAngelegt(new GregorianCalendar());

        if (null == person.getNachname())
            person.setNachname("");

        //wenn schon vorhanden, dann gibt checkMatrikelnummer -1
        if (checkMatrikelnummer(person.getMatrikelnr()) != -1)
            return -1;
        else {
            dbhandler.create(person);
            return person.getPersonID();
        }
    }

	/**
	 * Das gleiche wie readPerson(personID)
	 * @param personID ID
	 * @return Person
	 */
	public Person getPersonById(int personID) {
		return readPerson(personID);
	}

    /**
     * Liest die Person an Hand der eindeutigen Matrikelnummer aus
     * @param matrikelnummer String
     * @return Personenobjekt {see org.semtix.db.dao.Person}
     */
    public Person getPersonByMatrikelnummer(String matrikelnummer) {
        return dbhandler.read("matrikelnr", matrikelnummer);
    }

    /**
     * Eine Person aus der Datenbank auslesen
     * @param personID ID der gewünschten Person
	 * @return Objekt mit den Persondaten
	 */
	public Person readPerson(int personID) {

        return dbhandler.getByID(personID);
	}

	/**
	 * Daten einer Person in der Datenbank aktualisieren (UPDATE).
	 * @param person Objekt mit Persondaten
	 */
	public void updatePerson(Person person) {

        person.setUserGeaendert(UserConf.CURRENT_USER.getUserID());

        person.setDatumGeaendert(new GregorianCalendar());

        dbhandler.update(person);
	}

    /**
	 * Eine bestimmte Person aus der Datenbank löschen (DELETE)
     *
     * Löscht auch gleichzeitig alle Anträge und Anmerkungen zur Person
     *
     * @param personID ID der zu löschenden Person
	 */
	public void deletePerson(int personID) {

        DBHandlerAnmerkungen dbHandlerAnmerkungen = new DBHandlerAnmerkungen();
        for (Anmerkung anmerkung : dbHandlerAnmerkungen.getAnmerkungenListe(personID)) {
            dbHandlerAnmerkungen.deleteAnmerkung(anmerkung);
        }

        new DBHandlerAntrag().deletePersonAntraege(personID);

        dbhandler.delete(dbhandler.getByID(personID));

    }

    /**
     * Die Methode markier derzeit nur dass jetzt in archivierte personen und nicht-archivierte unterschieden wird.
     *
     * @return Personen die archiviert wurden
     */
    public List<Person> archiviertePersonen() {
        List<Person> archivierte = new ArrayList<Person>();
        for (Person p : dbhandler.getListOfAllElements()) {
            if (p.isArchiviert())
                archivierte.add(p);
        }
        return archivierte;
    }


    /**
     * Überprüft ob die Person in den letzten Semestern einen Kulanzantrag gestellt hat
     *
     * @param personId       ID Der Person
     * @param anzahlSemester Anzahl der Semester
     * @return true wenn ja
     */
    public boolean hatKulanzAntragGestellt(int personId, int anzahlSemester) {
		Set<Integer> semesterIDs = bestimmeLetzteSemester(anzahlSemester);

        List<Antrag> antragList = new DBHandlerAntrag().readAntragListe("personid", "" + personId);

        for (Antrag a : antragList) {
            if (semesterIDs.contains(a.getSemesterID()) && a.isKulanz())
                return true;
        }

        return false;
    }

    /**
     * Liefert eine Liste von Personen die seit i Semestern KEINEN Antrag gestellt haben. Wenn i==0, dann sind das Personen, die noch NIE einen Antrag gestellt haben.
     * @param i Wieviele Semester
     * @return Liste der Personen die in i Semestern KEINEN Antrag gestellt haben
     */
    public List<Person> getListePersonenSeitSemesternOhneAntrag(int i) {

		//Letzte i Semester in einer Liste:
		Set<Integer> semesterIDs = bestimmeLetzteSemester(i);

		Session session = HibernateUtil.getSessionFactory().openSession();

		Criteria critAntrag = session.createCriteria(Antrag.class);

		if (null != semesterIDs && semesterIDs.size() > 0) {
			Property semesterId = Property.forName("semesterID");
			critAntrag.add(semesterId.in(semesterIDs));
		}

        //nötig weil sonst outer join doppelte resultate
        critAntrag.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        List<Antrag> antraegeDerSemester = critAntrag.list();


		//Personen und Anträge
		HashSet<Integer> personIds = new HashSet<>();
		for (Antrag a : antraegeDerSemester) {
			personIds.add(a.getPersonID());
		}

		//We are looking for persons that are not in the list:
		Criteria critPerson = session.createCriteria(Person.class);
		critPerson.add(Restrictions.eq("uni", UniConf.aktuelleUni));
		critPerson.add(Restrictions.not(Restrictions.in("personID", personIds)));
        critPerson.addOrder(Order.asc("nachname"));
        critAntrag.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<Person> personsNotInList = critPerson.list();

		session.close();

		return personsNotInList;

    }

    /**
     * Gibt die letzten i Semester zurück, wenn i=0, werden alle Semester selektiert. Das ist für den Fall, dass jemand noch gar keinen Antrag gestellt hat.
     *
     * (Hilfsmethode für getListePersonenSeitSemesternOhneAntrag())
     * @param i Semesterzahl
     * @return Liste mit den letzten i Semester-IDs
     */
	private Set<Integer> bestimmeLetzteSemester(int i) {
		//Semesterdaten zusammensammeln:
        Semester aktuellesSemester = SemesterConf.getSemester();


        DBHandlerSemester dbHandlerSemester = new DBHandlerSemester();
		List<Semester> alleSemesterAktuellerUni = dbHandlerSemester.getSemesterListe(UniConf.aktuelleUni);
		HashSet<Integer> semesterIDListe = new HashSet<>();

		if (i > 0) {

            //das aktuelle Semester immer dabei
            semesterIDListe.add(aktuellesSemester.getSemesterID());

            int tempjahr = Integer.parseInt(aktuellesSemester.getSemesterJahr());
            String tempBuchstabe = aktuellesSemester.getSemesterArt().getBuchstabe();

            HashMap<Integer,String> jahrBuchstabeMap = new HashMap<Integer, String>();

            //mache i Schritte und bestimme die Jahre und Endbuchstaben der letzten i Semester
            for (int j = 0; j < i; j++) {
                if (jahrBuchstabeMap.containsKey(tempjahr))
                    jahrBuchstabeMap.put(tempjahr,"SW"); //beide Semester
                else
                    jahrBuchstabeMap.put(tempjahr,tempBuchstabe);

                if (tempBuchstabe.equals("W"))
                    tempBuchstabe = "S";
                else {
                    tempBuchstabe = "W";
                    tempjahr--;
                }
            }

            //jetzt werden für die letzten i Semester noch die IDs geholt:
			for (Semester semester : alleSemesterAktuellerUni) {
				Integer jahr = Integer.parseInt(semester.getSemesterJahr());
                if (jahrBuchstabeMap.containsKey(jahr) && jahrBuchstabeMap.get(jahr).contains(semester.getSemesterArt().getBuchstabe())) {
                    semesterIDListe.add(semester.getSemesterID());
                }
            }


		} else {
			for (Semester s : alleSemesterAktuellerUni) {
				semesterIDListe.add(s.getSemesterID());
			}
		}


        return  semesterIDListe;
    }


    public List<Person> getListeAntragsteller(Semester semester) {
        int semesterId = semester.getSemesterID();

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Person> antragSteller = session.createQuery(
                "from Person p where p.personID in (select a.personID from Antrag a where a.semesterID="
                        + semesterId + ") order by p.nachname")
                .list();
        session.close();

        return antragSteller;
    }

	public List<Person> getPersonsForAntraege(Set<Integer> personIds) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(Person.class);
		criteria.add(Restrictions.in("personID", personIds));
		criteria.addOrder(Order.asc("nachname"));
		List<Person> result = criteria.list();
		session.close();

		return result;
	}
}
