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

package org.semtix.gui.tabs.personendaten;

import org.apache.log4j.Logger;
import org.semtix.config.SemesterConf;
import org.semtix.config.UniConf;
import org.semtix.db.*;
import org.semtix.db.dao.Antrag;
import org.semtix.db.dao.Person;
import org.semtix.db.dao.Semester;
import org.semtix.db.dao.SemtixUser;
import org.semtix.shared.daten.enums.Uni;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Observable;


/**
 * Model-Klasse enthält die Daten der Personen
 *
 */
public class PersonModel
extends Observable {
	
	private Person person;
	private Person resetPerson;

    private List<Person> allPersons = null;
    private DBHandlerPerson dbHandlerPerson;

    private Logger logger = Logger.getLogger(PersonModel.class);

	
	/**
	 * Erstellt neues PersonModel
	 */
	public PersonModel() {
		
		person = new Person();

		resetPerson  = new Person();

		dbHandlerPerson = new DBHandlerPerson();


	}
	
	
	
	
	/**
	 * Setzt vorhandene Person anhand von Person-ID im Model
	 * @param personID PersonID der gewünschten Person
	 */
	public void setPerson(int personID) {

		person = dbHandlerPerson.readPerson(personID);
		

		// Reset-Person zum Zurücksetzen anlegen
        try {
            resetPerson = person.clone();
        } catch (CloneNotSupportedException e) {
            logger.warn("Person-Clonen schiefgelaufen", e);
        }
        // View (Personenformular) aktualisieren
		updateView();
		
	}
	
	/**
     * Liefert die momentan gesetzte Person
     * @return Person
     */
    public Person getPerson() {
        return person;
    }
	
	/**
     * Setzt eine neue Person im Model
     * @param p Person
     */
    public void setPerson(Person p) {

        this.person = p;

        // Reset-Person zum Zurücksetzen anlegen
        try {
            resetPerson = person.clone();
        } catch (CloneNotSupportedException e) {
            logger.warn("Person-Clonen schiefgelaufen", e);
        }
        // View (Personenformular) aktualisieren
        updateView();

	}
	
	/**
	 * Setzt die Personendaten zurück (seit letzter Speicherung)
	 */
	public void resetPerson() {

        try {
            resetPerson = person.clone();
        } catch (CloneNotSupportedException e) {
            logger.warn("Person-Clonen schiefgelaufen", e);
        }
        updateView();

	}

	public void setNextPerson() {

        updatePersonList();

        int index = allPersons.indexOf(this.person);

        if (index < allPersons.size() - 1)
            setPerson(allPersons.get(index + 1));


	}

	public void setLastPerson() {

        updatePersonList();

        int index = allPersons.indexOf(this.person);

        if (index > 0)
            setPerson(allPersons.get(index - 1));
    }


	/**
	 * Schreibt die Personendaten aus dem Formular in die Datenbank
	 */
	public void savePerson() {

		DBHandlerPerson dbHandler = new DBHandlerPerson();

		if (person.getPersonID() == -1) {
            dbHandler.createPerson(person);

        } else {
            dbHandler.updatePerson(person);
			
		}

        try {
            resetPerson = person.clone();
        } catch (CloneNotSupportedException e) {
            logger.warn("Person-Clonen schiefgelaufen", e);
        }

		setPerson(person.getPersonID());

	}
	
	

	
	/**
	 * Liefert String mit Timestamp und User-Kürzel, wann Person angelegt wurde. 
	 * @return String mit Timestamp
	 */
	public String getDatumAngelegt() {

		String datumText = "";
		String userText = "";
		
		DBHandlerUser dbHandlerUser = new DBHandlerUser();
		
		SemtixUser user = dbHandlerUser.readUser(person.getUserAngelegt());

		if(user != null) {
			userText = user.getKuerzel();
		}
		
		
		
		if (person.getDatumAngelegt() != null) {

			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			
			datumText = df.format(person.getDatumAngelegt().getTime());

		}
		
		return datumText + " -- " + userText;
		
	}
	
	
	 
	/**
	 * Liefert String mit Timestamp und User-Kürzel, wann Person zuletzt geändert wurde.
	 * @return String mit Timestamp
	 */
	public String getDatumGeaendert() {

		String datumText = "";
		String userText = "";
		
		DBHandlerUser dbHandlerUser = new DBHandlerUser();
		
		SemtixUser user = dbHandlerUser.readUser(person.getUserGeaendert());
		
		if(user != null) {
			userText = user.getKuerzel();
		}
		
		
		if (person.getDatumGeaendert() != null) {

			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			
			datumText = df.format(person.getDatumGeaendert().getTime());

		}
		
		return datumText + " -- " + userText;
				
	}
	
	
	

	
	/**
	 * Gibt zurück, ob Hinweis auf Kulanz eingeblendet werden soll.
	 * @return einblenden? ja/nein
	 */
	public boolean isKulanz(){
        Semester aktuellesSemester = SemesterConf.getSemester();
        if (null != aktuellesSemester.getSemesterJahr())
            return new DBHandlerPerson().hatKulanzAntragGestellt(person.getPersonID(), 3);
        else
            return false;
    }
	
	
	
	/**
	 * Gibt zurück, ob Hinweis auf Ratenzahlung eingeblendet werden soll.
	 * @return einblenden? ja/nein
	 */
	public boolean isRatenzahlung(){
		return new DBHandlerAntrag().getAntragRatenzahlung(person.getPersonID());
	}


	/**
	 * Liefert Anzahl von ungeprüften Nachreichungen für Antrag des aktuellen Semesters
	 *
	 * @return Anzahl ungeprüfte Nachreichungen
	 */
	public int uncheckedNachreichungen() {

		Integer count = new DBHandlerNachreichung().getNachreichungenPruefen(person.getPersonID());
		if (count == null) {
			return 0;
		} else {
			return count;
		}

	}



	/**
	 * Setzt bei Person die Universität, wenn gewechselt werden soll (Humboldt-Uni oder Kunsthochschule Weißensee)
	 * @param uni Universität, zu der gewechselt wird
	 */
	public void changeUniversitaet(Uni uni) {

		person.setUni(uni);

		dbHandlerPerson.updatePerson(person);

		DBHandlerAntrag dbHandlerAntrag = new DBHandlerAntrag();
		List<Antrag> antraegeDerPerson = dbHandlerAntrag.getAntragListe(person.getPersonID());
		if (null != antraegeDerPerson && antraegeDerPerson.size() > 0) {
			DBHandlerSemester dbHandlerSemester = new DBHandlerSemester();
			List<Semester> neueSemester = dbHandlerSemester.getSemesterListe(uni);
			for (Antrag a : antraegeDerPerson) {
				Semester altesSemester = dbHandlerSemester.getSemesterByID(a.getSemesterID());
				boolean found = false;
				for (Semester neu : neueSemester) {
					if (altesSemester.getSemesterKurzform().equals(neu.getSemesterKurzform())) {
						a.setSemesterID(neu.getSemesterID());
						dbHandlerAntrag.updateAntrag(a);
						found = true;
						break;
					}
				}
				if (!found) {
					JOptionPane.showMessageDialog(null, "<html>Das Semester " + altesSemester.getSemesterKurzform() +
									" wurde in der neuen Uni nicht gefunden. " +
									"<br> <font color=\"red\">Der Antrag wird gelöscht</font>.</html> ",
							"Fehler", JOptionPane.ERROR_MESSAGE);

					dbHandlerAntrag.delete(a);

				}
			}
		}
	}

	
	
	
	/**
	 * Aktualisiert das Formular mit Personendaten über Observer
	 */
	private void updateView() {

		setChanged();
		notifyObservers(person);
		
	}

    public void deletePerson() {
        DBHandlerPerson dbHandlerPerson = new DBHandlerPerson();
        dbHandlerPerson.deletePerson(person.getPersonID());


    }

    private void updatePersonList() {
        if (null == allPersons || !allPersons.get(0).getUni().equals(UniConf.aktuelleUni)) {

            allPersons = dbHandlerPerson.getPersonenListe(UniConf.aktuelleUni);

//            Collections.sort(allPersons);
        }
    }
}
