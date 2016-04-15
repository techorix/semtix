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

package org.semtix.gui.tabs.antrag;

import org.apache.log4j.Logger;
import org.semtix.config.SemesterConf;
import org.semtix.db.*;
import org.semtix.db.dao.*;
import org.semtix.shared.daten.enums.AntragStatus;
import org.semtix.shared.daten.enums.Vorgangsart;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


/**
 * Datenklasse (Model) für die Daten eines Antrags
 */
public class AntragModel
extends Observable {
	
	private Antrag antrag;			// Objekt Antrag
	private Antrag resetAntrag;		// gespeichertes Antrag-Objekt für Reset
	private Person person;			// Objekt Person
	private Semester semester;		// Objekt Semester
	
	private Vorgang vorgangErstrechnung, vorgangZweitrechnung;

    private Logger logger = Logger.getLogger(AntragModel.class);

    /**
	 * Erstellt ein neues AntragModel
	 */
	public AntragModel(){
		
		// Antrag erstellen
		antrag = new Antrag();
		
		// Antrag für Reset erstellen
		resetAntrag = new Antrag();
				
	}

    /**
     * Setzt bestehenden Antrag und Person (anhand der Antrag-ID)
     *
     * @param antragID AntragID
     * @param person   Person
     */
    public void setAntrag(int antragID, Person person) {

        this.person = person;

        if (antragID > 0) {

            // DB-Handler erstellen
            DBHandlerAntrag dbHandlerAntrag = new DBHandlerAntrag();
            DBHandlerSemester dbHandlerSemester = new DBHandlerSemester();

            this.antrag = dbHandlerAntrag.readAntrag(antragID);

            semester = dbHandlerSemester.readSemester(antrag.getSemesterID());

            try {
                // Reset-Antrag zum Zurücksetzen anlegen
                resetAntrag = antrag.clone();
            } catch (CloneNotSupportedException e) {
                logger.warn("Antrag-Clonen schiefgelaufen", e);
            }
            // Aktualisiert Vorgänge Erst- und Zweitrechnung (Bearbeitungsprotokoll)
            updateVorgaenge();

            // View aktualisieren
            updateView();

        }


    }

    /**
     * Setzt neuen Antrag für Person (für aktuelles Semester)
     * @param person Person
     */
    public void setNewAntrag(Person person){

		this.person = person;

        antrag = new Antrag();

        semester = SemesterConf.getSemester();

        if (null != semester)
            antrag.setSemesterID(semester.getSemesterID());

        antrag.setPersonID(person.getPersonID());

        antrag.setAntragStatus(AntragStatus.NICHTENTSCHIEDEN);

		List<Antrag> alteAntraege = new DBHandlerAntrag().getAntragListe(person.getPersonID());
		if (null == alteAntraege || alteAntraege.size() == 0) {
			int returnvalue = JOptionPane.showConfirmDialog(null, "<html>Erstihäkchen setzen? " +
					"<br> Das ist der erste Antrag dieser Person.</html>");
			if (returnvalue == JOptionPane.YES_OPTION) {
				antrag.setErstsemester(true);
			}
		}
		// Reset-Antrag zum Zurücksetzen anlegen
        try {
            // Reset-Antrag zum Zurücksetzen anlegen
            resetAntrag = antrag.clone();
        } catch (CloneNotSupportedException e) {
            logger.warn("Antrag-Clonen schiefgelaufen", e);
        }

        saveAntrag();

        // View aktualisieren
        updateView();

    }

    /**
     * Aktualisiert Vorgänge Erst- und Zweitrechnung
     */
    private void updateVorgaenge() {

        // DBHandler für Vorgänge holens
        DBHandlerVorgaenge dbHandlerVorgaenge = new DBHandlerVorgaenge();

        vorgangErstrechnung =
                dbHandlerVorgaenge.getVorgangByVorgangsart(Vorgangsart.ERSTRECHNUNG, antrag.getAntragID());

        vorgangZweitrechnung =
                dbHandlerVorgaenge.getVorgangByVorgangsart(Vorgangsart.ZWEITRECHNUNG, antrag.getAntragID());

    }

    /**
     * Liefert vorgang für Erstrechnung
     * @return Vorgang Erstrechnung
     */
    public Vorgang getVorgangErstrechnung() {
        updateVorgaenge();
        return vorgangErstrechnung;
    }

    /**
     * Liefert vorgang für Zweitrechnung
     * @return Vorgang Zweitrechnung
     */
    public Vorgang getVorgangZweitrechnung() {
        updateVorgaenge();
        return vorgangZweitrechnung;
    }

    /**
     * Aktualisiert die angegebenen Härten im Antrag
     * @param haerteListe Liste mit Härten
     */
    public void updateAntragHaerten(ArrayList<AntragHaerte> haerteListe) {

        antrag.setHaerteListe(haerteListe);

        // View aktualisieren
        updateView();

    }

    /**
     * Liefert den momentan im Model gesetzten Antrag
     * @return Antrag
     */
    public Antrag getAntrag() {
        return this.antrag;
    }

    /**
     * Setzt bestehenden Antrag (anhand der Antrag-ID)
     * @param antragID AntragID
     */
    public void setAntrag(int antragID) {


        if (antragID > 0) {

            // DB-Handler erstellen
            DBHandlerAntrag dbHandlerAntrag = new DBHandlerAntrag();
            DBHandlerPerson dbHandlerPerson = new DBHandlerPerson();
            DBHandlerSemester dbHandlerSemester = new DBHandlerSemester();

            antrag = dbHandlerAntrag.readAntrag(antragID);

            person = dbHandlerPerson.readPerson(antrag.getPersonID());

            semester = dbHandlerSemester.readSemester(antrag.getSemesterID());

            // Reset-Antrag zum Zurücksetzen anlegen
            try {
                // Reset-Antrag zum Zurücksetzen anlegen
                resetAntrag = antrag.clone();
            } catch (CloneNotSupportedException e) {
                logger.warn("Antrag-Clonen schiefgelaufen", e);
            }
            // Aktualisiert Vorgänge Erst- und Zweitrechnung (Bearbeitungsprotokoll)
            updateVorgaenge();

            // View aktualisieren
            updateView();

		}

	}

	/**
	 * Setzt die Antragdaten zurück auf Objekt reset-Antrag (seit letzter Speicherung)
	 */
	public void resetAntrag() {
        try {
            // Reset-Antrag zum Zurücksetzen anlegen
            resetAntrag = antrag.clone();
        } catch (CloneNotSupportedException e) {
            logger.warn("Antrag-Clonen schiefgelaufen", e);
        }

		updateView();
		
	}

	
	/**
	 * Liefert die momentan im Model gesetzte Person
	 * @return Person
	 */
	public Person getPerson(){
		return person;
	}
	
	
	/**
	 * Liefert das momentan im Model gesetzte Semester
	 * @return Semester
	 */
	public Semester getSemester(){
		return semester;
	}
	

	

	/**
	 * Liefert String mit Timestamp und User-Kürzel, wann Antrag angelegt wurde. 
	 * @return String mit Timestamp
	 */
	public String getDatumAngelegt() {

		String datumText = "";
		String userText = "";
		
		DBHandlerUser dbHandlerUser = new DBHandlerUser();
		
		SemtixUser user = dbHandlerUser.readUser(antrag.getUserAngelegt());

		if(user != null) {
			userText = user.getKuerzel();
		}
		
		
		
		if (antrag.getDatumAngelegt() != null) {

			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			
			datumText = df.format(antrag.getDatumAngelegt().getTime());

		}
		
		return datumText + " -- " + userText;
		
	}
	
	
	
	/**
	 * Liefert String mit Timestamp und User-Kürzel, wann Antrag zuletzt geändert wurde. 
	 * @return String mit Timestamp
	 */
	public String getDatumGeaendert() {

		String datumText = "";
		String userText = "";
		
		DBHandlerUser dbHandlerUser = new DBHandlerUser();
		
		SemtixUser user = dbHandlerUser.readUser(antrag.getUserGeaendert());
		
		if(user != null) {
			userText = user.getKuerzel();
		}
		
		
		if (antrag.getDatumGeaendert() != null) {

			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			
			datumText = df.format(antrag.getDatumGeaendert().getTime());

		}
		
		return datumText + " -- " + userText;
		
	}
				
		

	/**
	 * Setzt Punkte für Einkommen und Härten aus Berechnungszettel im AntragModel
     * @param punkteEinkommen Punkte Einkommen

	 * @param punkteHaerten Punkte Härten
	 */
	public void setPunkte(int punkteEinkommen, int punkteHaerten) {
		
		antrag.setPunkteEinkommen(punkteEinkommen);
		antrag.setPunkteHaerte(punkteHaerten);
		
		updateView();
		
	}
	
	
	
	/**
	 * Speichert Antragsdaten in Datenbank.
	 *
	 */
	public void saveAntrag() {

        DBHandlerAntrag dbHandlerAntrag = new DBHandlerAntrag();


        // neuer Antrag wird angelegt (INSERT: antragID = -1)
        if (antrag.getAntragID() <= 0) {
            // neuen Antrag in Datenbank anlegen und neuerstellte antragID zurückliefern
            dbHandlerAntrag.createAntrag(antrag);
        }
        // bestehender Antrag wird geändert (UPDATE: antragID grösser null)
        else if (antrag.getAntragID() > 0) {
            dbHandlerAntrag.updateAntrag(antrag);
        }


        // Antrag erneut aus DB laden und in View anzeigen, damit dort auch
		// die Timestamps für Antrag angelegt und zuletzt bearbeitet aktualisiert werden.
		setAntrag(antrag.getAntragID(), person);
		
	}

	
	/**
	 * Liefert Anzahl von ungeprüften Nachreichungen für Antrag
	 * @return Anzahl ungeprüfte Nachreichungen
	 */
	public int uncheckedNachreichungen() {

		Integer count = new DBHandlerNachreichung().getUncheckedNachreichungenCountForAntrag(antrag.getAntragID());
		if (count == null) {
			return 0;
		} else {
			return count;
		}

	}
	
	
	
	

	/**
	 * Aktualisiert das AntragPanel (über Observer)
	 */
	public void updateView(){
		setChanged();
		notifyObservers(antrag);
	}

}
