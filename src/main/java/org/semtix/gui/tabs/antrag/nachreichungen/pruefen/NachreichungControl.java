/*

  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
         Semesterticketbüro der Humboldt-Universität Berlin

  Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package org.semtix.gui.tabs.antrag.nachreichungen.pruefen;

import org.semtix.db.DBHandlerNachreichung;
import org.semtix.db.DBHandlerSemester;
import org.semtix.db.DBHandlerVorgaenge;
import org.semtix.gui.tabs.antrag.AntragControl;
import org.semtix.gui.tabs.personendaten.PersonControl;
import org.semtix.shared.daten.enums.Vorgangsart;

import javax.swing.*;

/**
 * Control-Klasse zum Bearbeiten der ungeprüften Nachreichungen
 */
public class NachreichungControl {

	private PersonControl personControl;
	private AntragControl antragControl;
	
	private DialogNachreichung dialog;
	
	private NachreichungModel nachreichungModel;
	
	private int antragID;
	private String semester, name, matrikelnr;
	
	
	/**
	 * TODO Nachreichungsbutton hinzu + Nachreichungsfunktion (Anzahl) hier kapseln für Personen und Anträge gleichzeitig
	 *
	 * Erstellt ein neues Control
	 * @param personControl PersonControl
	 * @param antragControl AntragControl
	 */
	public NachreichungControl(PersonControl personControl, AntragControl antragControl) {

		this.antragControl = antragControl;
		this.personControl = personControl;

		nachreichungModel = new NachreichungModel();
		
		if(antragControl == null) {
			antragID = personControl.getIDAktuellerAntrag();
			semester = personControl.getSemesterAktuellerAntrag();
			name = personControl.getPerson().toString();
			matrikelnr = personControl.getPerson().getMatrikelnr();
		}
		else {
            try {
                antragID = antragControl.getAntragID();
                int semesterID = antragControl.getAntrag().getSemesterID();

                semester = new DBHandlerSemester().readSemester(semesterID).getSemesterKurzform();

                name = antragControl.getPerson().toString();
                matrikelnr = antragControl.getPerson().getMatrikelnr();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Fehler: Kann keine Nachreichungen anlegen. Z.B. Weil noch kein Semester eingestellt wurde.");
            }
        }

		
		nachreichungModel.setAntragID(antragID);
	
		
		dialog = new DialogNachreichung(this);
		
		dialog.updateView();
		
		dialog.pack();
		
		// Frame auf dem Bildschirm zentrieren
		dialog.setLocationRelativeTo(dialog.getParent());
		
		dialog.setVisible(true);

	}
	
	
	
	/**
	 * Liefert das Semester für die ungeprüften Nachreichungen
	 * @return Semester
	 */
	public String getSemester() {
		return semester;
	}
	
	
	/**
	 * Liefert den Namen der Person, für welche die Nachreichung eingegangen ist
	 * @return Name
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * Liefert die Matrikelnummer der Person, für welche die Nachreichung eingegangen ist
	 * @return Matrikelnummer
	 */
	public String getMatrikelnr() {
		return matrikelnr;
	}
	
	
	/**
	 * LIefert das Model für die Nachreichungen
	 * @return Model
	 */
	public NachreichungModel getModel() {
		return nachreichungModel;
	}

	
	
	
	
	/**
	 * neue ungeprüfte Nachreichung in Datenbank eintragen
	 * @param nachreichungArt Art der Nachreichung
	 */
	public void addNachreichung(NachreichungArt nachreichungArt) {

        new DBHandlerNachreichung().create(nachreichungArt, antragID);

        new DBHandlerVorgaenge().createVorgang(Vorgangsart.NACHREICHUNG_EINGANG, antragID);

		updateView();

	}
	
	
	
	/**
	 * Nachreichungen aktualisieren
	 * @param index Index der Nachreichung inb der Liste
	 * @param state Zustand der Nachreichung, die geändert werden soll
	 */
	public void updateNachreichung(int index, boolean state) {
		nachreichungModel.updateNachreichung(index, state);
	}
	
	
	
	/**
	 * Geprüfte Nachreichungen in Datenbank speichern
	 */
	public void saveNachreichungen() {
		nachreichungModel.saveNachreichungen();

		//TODO Dialog mit Nachfragen anzeigen und eine auf erledigt setzen?

		updateView();

	}
	
	
	/**
	 * Antragformular aktualisieren
	 */
	public void updateView() {
		if (antragControl != null) {
			antragControl.updateNachreichungen();
		}
		if (personControl != null) {
			personControl.updateNachreichungen();
		}
		dialog.dispose();
	}

}
