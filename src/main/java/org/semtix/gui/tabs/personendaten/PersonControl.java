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

import org.semtix.config.SemesterConf;
import org.semtix.config.Settings;
import org.semtix.config.UserConf;
import org.semtix.db.DBHandlerPerson;
import org.semtix.db.dao.Anmerkung;
import org.semtix.db.dao.Antrag;
import org.semtix.db.dao.Person;
import org.semtix.gui.tabs.TabControl;
import org.semtix.gui.tabs.TabInterface;
import org.semtix.shared.actions.Email;
import org.semtix.shared.bearbeitungsprotokoll.ProtokollControl;
import org.semtix.shared.daten.enums.Uni;
import org.semtix.shared.tablemodels.TableModelAnmerkungen;
import org.semtix.shared.tablemodels.TableModelAntragUebersicht;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Controller-Klasse dient zur Steuerung für Model und View von Personendaten.
 *
 */
public class PersonControl
		implements TabInterface {
	
	
	private PersonModel personModel;
	private PersonView personView;
	private TabControl tabControl;
	
	private TableModelAntragUebersicht tableModelAntragUebersicht;
	private TableModelAnmerkungen tableModelAnmerkungen;
	
	private JPanel mainPanel, bottomPanel;
	private boolean hasToBeSavedYet;

	/**
	 * Erstellt einen neuen PersonControl
	 * @param tc TabControl
	 */
	public PersonControl(TabControl tc) {

		this.tabControl = tc;
		
		// Model für die Persondaten
		personModel = new PersonModel();
		
		// TableModel für die Tabelle mit der Antragsübersicht zu einer Person
		tableModelAntragUebersicht = new TableModelAntragUebersicht();
		
		// TableModel für die Anzeige der Anmerkungen zu einer Person
		tableModelAnmerkungen = new TableModelAnmerkungen();
		
		// Formular mit den Persoendaten
		personView = new PersonView(this);
		
		// View als Observer beim Model anmelden
		personModel.addObserver(personView);
		
		// Tabelle Antragsübersicht initialisieren
		personView.initTableAntraege(tableModelAntragUebersicht);
		
		// Tabelle Anmerkungen initialisieren
		personView.initTableAnmerkungen(tableModelAnmerkungen);
		
		// Listener zur View hinzufügen
		personView.addListener(this);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.YELLOW);
		
		Dimension dimButton = new Dimension(30, 30);
		Insets insets = new Insets(-9, -9, -9, -9);
		
		// Button zum Aufruf des Filter-Dialoges
		JButton filterButton = new JButton("<html><b>F</b></html>");
		filterButton.setToolTipText("zurück zum Filter");

		filterButton.setPreferredSize(dimButton);
		filterButton.setMargin(insets);
		
		
		
		filterButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				tabControl.setCards("Antrag");
			}
			
		});
		
		JButton personTabButton = new JButton("als Persontab öffnen");
		
		personTabButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				tabControl.getMainControl().addTab(personModel.getPerson().getPersonID());
				tabControl.setCards("Antrag");
			}
			
		});
		
		
		bottomPanel.add(filterButton);
		bottomPanel.add(personTabButton);
		
		mainPanel.add(personView.getScrollPane());
		//mainPanel.add(bottomPanel, BorderLayout.SOUTH);
		
		// Scrollpane mit PersonView in TabView (CardLayout) hinzufügen
		//tabControl.addView(personView.getScrollPane(), "Person");
		tabControl.addView(mainPanel, "Person");
		
	}
	
	
	/**
	 * Setzt das Panel mit den Buttons in den SOUTH-Bereich des MainPanels
	 */
	public void setBottomPanel() {
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);
	}
	
	/**
     * Setzt neue Person im Model (Nachname, Vorname, Matrikelnummer)
     * @param nachname Nachname
     * @param vorname Vorname
     * @param matrikelnr Matrikelnummer
     */
	public void setNewPerson(String nachname, String vorname, String matrikelnr) {

        personModel.getPerson().setNachname(nachname);
        personModel.getPerson().setVorname(vorname);
        personModel.getPerson().setMatrikelnr(matrikelnr);
        personModel.getPerson().setWohnort("Berlin");
        personView.setPerson(personModel.getPerson());
        personView.setSaveStatus(true, false);

        // bei neuen Personen sind Buttons für aktuellen oder
        // bestimmten Antrag deaktiviert
        personView.enableButtonAntragAktuell(false);
        personView.enableButtonNachreichung(false);
        personView.enableButtonAntragBestimmt(false);


		personView.selectFirstantrag();

        savePerson();
    }
	
	/**
     * Liefert die aktuell im PersonModel gesetzte Person
     * @return Person
     */
    public Person getPerson() {
        return personModel.getPerson();
    }
	
	/**
	 * Setzt vorhandene Person im PersonModel (anhand personID)
	 * @param personID unique ID der Person
	 */
	public void setPerson(int personID) {

		// Person anhand der Person-ID im Model setzen
		personModel.setPerson(personID);

		fillTables(personID);


	}

	/**
	 * Setzt vorhandene Person im PersonModel (anhand Person Object)
	 *
	 * @param person Person
	 */
	public void setPerson(Person person) {

		// Person anhand der Person-ID im Model setzen
		personModel.setPerson(person);

		fillTables(person.getPersonID());

	}

	public void updateNachreichungen() {
		personView.setNachreichungen(personModel.uncheckedNachreichungen());
	}

	private void fillTables(int personID) {

		// Tabelle mit Antragsübersicht für Person füllen
		tableModelAntragUebersicht.fillList(personID);

		// Tabelle mit Anmerkungen für Person füllen
		tableModelAnmerkungen.setPersonID(personID);

        personView.enableButtonAntragNeu(true);


		// Wenn Tabelle mit Antragsübersicht leer, Buttons deaktivieren
		if (tableModelAntragUebersicht.getList().size() == 0) {
			personView.enableButtonAntragAktuell(false);
			personView.enableButtonNachreichung(false);
			personView.enableButtonAntragBestimmt(false);
			personView.enableButtonProtokoll(false);
        }

		// Wenn Antrag für aktuelles Semester in Übersichtstabelle
		// vorhanden ist, dann wird Button "Neuer Antrag" deaktiviert
		if (tableModelAntragUebersicht.getRowAktuellerAntrag() >= 0) {
			personView.enableButtonAntragAktuell(true);
			personView.enableButtonAntragNeu(false);
			personView.enableButtonNachreichung(true);
			personView.setNachreichungen(personModel.uncheckedNachreichungen());
		}
		// wenn nicht, dann wird Button "Aktueller Antrag" deaktiviert
		else {
			personView.enableButtonAntragAktuell(false);
			personView.enableButtonNachreichung(false);
		}

		// 1. Antrag in Übersichtstabelle selektieren
		personView.selectFirstantrag();

		personView.setSaveStatus(false, false);
	}

	/**
	 * Personendaten speichern (Klick auf Button "Speichern" im PersonFormular)
     * @return true if successfully saved person
	 */
    public boolean savePerson() {

        // Flag erlaubt das Speichern
		boolean saveFlag = true;
		
		// Personendaten aus Formular holen
		Person tempPerson = personView.getPerson();

		if (!Settings.SAVE_PERSON_ANYWAY) {

			// Überprüfung Pflichtfelder Vorname und Nachname (dürfen nicht leer bleiben)
			// Ansonsten speichern nicht möglich
			if (tempPerson.getVorname().equals("") || tempPerson.getNachname().equals("")) {
				String message = "Vor- oder Nachname dürfen nicht leergelassen werden.";
				JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
				saveFlag = false;
			}

			// Überprüfung Matrikelnummer (muss mindestens 3 Ziffern haben)
			// Ansonsten speichern nicht möglich
			else if (tempPerson.getMatrikelnr().length() < 3) {
				String message = "Matrikelnummer muss 3 oder mehr Ziffern haben.";
				JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
				saveFlag = false;
			}

			// Überprüfung auf schon vorhandene Matrikelnummer in DB bei neuen Personen (personID = -1)
			// oder wenn Matrikelnr. in Formularfeld nicht identisch mit Matrikelnr. im Model
			else if (personModel.getPerson().getPersonID() == -1) {

				if (checkExistingMatrikel(tempPerson.getMatrikelnr())) {
					String message = "Matrikelnummer existiert bereits in der Datenbank.";
					JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
					saveFlag = false;
				}


			} else if (!tempPerson.getMatrikelnr().equals(personModel.getPerson().getMatrikelnr())) {

				if (checkExistingMatrikel(tempPerson.getMatrikelnr())) {
					String message = "Matrikelnummer existiert bereits in der Datenbank.";
					JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
					saveFlag = false;
				}

			} else if (!personView.checkIBAN()) {
				if (tempPerson.getIBAN().startsWith("DE") ||
						(!IbanTest.correctLen(tempPerson.getIBAN()))) {
					JOptionPane.showMessageDialog(null, "<html><i>IBAN</i> wurde nicht angegeben (Person Barzahler?), hat die falsche Länge, <br> " +
							"einen unbekannten Ländercode oder hat aus sonstigen Gründen (Zahlendreher?) <br> eine <font color='grey'>inkorrekte Checksumme</font>.<br><p>&nbsp;<br> " +
							"Die aktualisierten Persondaten können deshalb nicht gespeichert werden. </p></html>", "Fehler: Falsche IBAN", JOptionPane.ERROR_MESSAGE);
					saveFlag = false;
				} else {
					JOptionPane.showMessageDialog(null, "<html><i>IBAN</i> hat eine korrekte Länge und einen bekannten Ländercode.<br><p>&nbsp;<br>" +
							"Da es sich um einen Sonderfall handeln kann, wird die IBAN trotz falscher<br>" +
							"Checksumme gespeichert.</htmL>", "IBAN-Checksumme inkorrekt", JOptionPane.WARNING_MESSAGE);

				}

			}

		}

		
		// Speichern nach Überprüfung nun möglich
		if(saveFlag) {
			
			// Referenz auf Person-Objekt in Model holen
			Person person = personModel.getPerson();
			
			// Werte aus Formular ins Model übertragen
			// Uni, DatumAngelegt, DatumGeaendert werden nicht im Model überschrieben
			person.setMatrikelnr(tempPerson.getMatrikelnr());
			person.setNachname(tempPerson.getNachname());
			person.setVorname(tempPerson.getVorname());
			person.setGebdatum(tempPerson.getGebdatum());
			person.setEnglischsprachig(tempPerson.isEnglischsprachig());
			person.setCo(tempPerson.getCo());
			person.setStrasse(tempPerson.getStrasse());
			person.setWohneinheit(tempPerson.getWohneinheit());
			person.setPlz(tempPerson.getPlz());
			person.setWohnort(tempPerson.getWohnort());
			person.setLand(tempPerson.getLand());
            person.setHatTelefon(tempPerson.isHatTelefon());
            person.setEmail(tempPerson.getEmail());
            person.setBarauszahler(tempPerson.isBarauszahler());
            person.setUserAngelegt(tempPerson.getUserAngelegt());
			person.setUserGeaendert(tempPerson.getUserGeaendert());
			person.setIBAN(tempPerson.getIBAN().trim());
			person.setBIC(tempPerson.getBIC());
			person.setKontoInhaber_Name(tempPerson.getKontoInhaber_Name());
			person.setKontoInhaber_Strasse(tempPerson.getKontoInhaber_Strasse());
			person.setKontoInhaber_Wohnort(tempPerson.getKontoInhaber_Wohnort());

            // Model schreibt Persondaten in Datenbank
			personModel.savePerson();
			
			// Save-Status wird wieder deaktiviert
			setSaveStatus(false);

            return true;
        } else {

            // Pflichtfelder überprüfen und Label rot markieren wenn leer
            personView.checkFields();

            return false;
        }


    }
	
	
	
	/**
	 * Setzt angezeigte Person auf Zustand beim letzten Speichern zurück.
	 */
	public void resetPerson(){
		int returnvalue = JOptionPane.showConfirmDialog(null, "Wirklich zurücksetzen?");
		if (returnvalue == JOptionPane.YES_OPTION) {
            personModel.resetPerson();
            setSaveStatus(false);
        }
	}


	/**
	 * Fügt eine Anmerkung für die angezeigte Person hinzu
	 * @param text Text der Anmerkung
	 */
	public void addAnmerkung(String text) {

        int personId = personModel.getPerson().getPersonID();
        if ( personId <= 0)
            JOptionPane.showMessageDialog(null,"Fehler: Person nocht nicht gespeichert.");
        else {
            Anmerkung anmerkung = new Anmerkung();
            anmerkung.setPersonId(personId);
            anmerkung.setUserId(UserConf.CURRENT_USER.getUserID());
            anmerkung.setText(text);
            anmerkung.setZeitstempel(new GregorianCalendar());
            tableModelAnmerkungen.setPersonID(personId);
            tableModelAnmerkungen.addAnmerkung(anmerkung);
        }
	}
	
	
	/**
	 * Aktualisiert eine Anmerkung für die angezeigte Person
	 * @param anmerkung Anmerkung
	 */
	public void updateAnmerkung(Anmerkung anmerkung) {
		tableModelAnmerkungen.updateAnmerkung(anmerkung);
	}
	
	
	/**
	 * Löscht eine Anmerkung aus der Anmerkungsübersicht
     * @param a Die zu löschende Anmerkung
     */
	public void deleteAnmerkung(Anmerkung a) {

		tableModelAnmerkungen.deleteAnmerkung(a);
	}
	
	/**
	 * Öffnet Antragsformular mit Antrag einer Person für aktuelles Semester (wenn vorhanden)
	 */
	public void antragAktuell() {

		if (tabControl.hasToBeSaved()) {
			savePerson();
		}

		int aktuellerAntragRow = tableModelAntragUebersicht.getRowAktuellerAntrag();
		
		if (aktuellerAntragRow == -1) {
			String message = "In der Übersicht ist kein Antrag\nfür das aktuelle Semester vorhanden.";
            JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);

        }
		else {
			int antragID = tableModelAntragUebersicht.getAntragID(aktuellerAntragRow);
			tabControl.getAntragControl().setAntrag(antragID, personModel.getPerson());
		}
		
	}
	
	
	
	
	/**
	 * Öffnet Antragsformular mit einem bestimmten Antrag einer Person (ausgewählt aus Übersichtstabelle)
	 * @param row selektierte Zeile in Übersichtstabelle
	 */
	public void antragBestimmt(int row) {
		if (tabControl.hasToBeSaved()) {
			savePerson();
		}


		// wenn nur 1 Antrag in Übersichtstabelle, dann wird dieser bei Klick
		// auf Button "Bestimmter Antrag" angezeigt (Tabellenzeile = 0)
		if(tableModelAntragUebersicht.getList().size() == 1) {
			tabControl.getAntragControl().setAntrag(tableModelAntragUebersicht.getAntragID(0),
					personModel.getPerson());
		}
		// wenn mehr als 1 Antrag in Übersichtstabelle, dann muss davon 1 Antrag selektiert sein
		// sonst wird eine Fehlermeldung ausgegeben
		else if(tableModelAntragUebersicht.getList().size() > 1) {
			if (row == -1) {
				String message = "Es muss ein Antrag\nin der Übersicht ausgewählt sein.";
                JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
            }
			else{
				// selektierter Antrag wird angezeigt
				tabControl.getAntragControl().setAntrag(tableModelAntragUebersicht.getAntragID(row),
						personModel.getPerson());
			}
			
		}

	}
	
	
	

	/**
	 * Öffnet Antragsformular mit einem neuen Antrag einer Person für aktuelles Semester
	 */
	public void antragNeu() {
		if (tabControl.hasToBeSaved()) {
			savePerson();
		}

		boolean antragAnzeigen = false;


        if (personModel.getPerson().getPersonID() <= 0) {
            String message = "Neue Person muss erst in DB angelegt sein,\nbevor neue Anträge angelegt werden können.";
            JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
        } else {

			// Model mit Daten der Antragsübersicht für die angezeigte Person
            List<Antrag> antragsListe = tableModelAntragUebersicht.getList();

            if(antragsListe.size() == 0) {
				antragAnzeigen = true;
			}
			else {
				
				int antragID = -1;
				
				// Liste durchgehen und nach aktuellem Semester (ID) suchen
                for (Antrag a : antragsListe) {
                    if(SemesterConf.getSemester().getSemesterID() == a.getSemesterID()) {
						antragID = a.getAntragID();
					}
				}
				
				// Wenn Antrag für aktuelles Semester in Antragübersicht, Dialog anzeigen
				if(antragID > 0)
					new DialogAntragVorhanden(antragID, personModel.getPerson(), tabControl);
				// ansonsten Formular für neuen Antrag anzeigen
				else
					antragAnzeigen = true;
			}
		}
		
		if(antragAnzeigen) {
			tabControl.getAntragControl().setNewAntrag(personModel.getPerson());
		}

	}

    public void schließen() {
        tabControl.closeTab();
    }

    /**
     * liefert die Kurzschreibweise für das Semester des aktuellen Antrags zurück
     * @return Semesterkurzform
	 */
	public String getSemesterAktuellerAntrag() {
		
		String semesterKurzform = "";
		
		int aktuellerAntragRow = tableModelAntragUebersicht.getRowAktuellerAntrag();
		
		if (aktuellerAntragRow == -1) {
			semesterKurzform = " - kein Antrag für aktuelles Semester vorhanden -";
		}
		else {
            semesterKurzform = (String) tableModelAntragUebersicht.getValueAt(aktuellerAntragRow, 1);
        }
		
		return semesterKurzform;
		
	}
	
	
	
	
	/**
	 * liefert die ID des aktuellen Antrags zurück
	 * @return ID aktueller Antrag
	 */
	public int getIDAktuellerAntrag() {
		
		int antragID = -1;
		
		int aktuellerAntragRow = tableModelAntragUebersicht.getRowAktuellerAntrag();
		
		if (aktuellerAntragRow != -1) {
			antragID = tableModelAntragUebersicht.getList().get(aktuellerAntragRow).getAntragID();
		}
		
		return antragID;
		
	}

	
	
	

	
	
	
	
	/**
	 * Zeigt das Bearbeitungsprotokoll für bestimmten Antrag einer Person an (ausgewählt aus Übersichtstabelle)
	 * @param row Zeile der Übersichtstabelle
	 */
	public void bearbeitungsProtokoll(int row) {
		
		// wenn nur 1 Antrag in Übersichtstabelle, dann wird für diesen
		// das Bearbeitungsprotokoll angezeigt (Tabellenzeile = 0)
		if(tableModelAntragUebersicht.getList().size() == 1) {
			new ProtokollControl(tableModelAntragUebersicht.getAntragID(0));
		}
		// wenn mehr als 1 Antrag in Übersichtstabelle, dann muss davon 1 Antrag selektiert sein
		// sonst wird eine Fehlermeldung ausgegeben
		else if(tableModelAntragUebersicht.getList().size() > 1) {
			// Antrag für aktuelles Semester in Übersicht, dann dafür Protokoll anzeigen
			if(tableModelAntragUebersicht.getAktuelleAntragID() != -1) {
				new ProtokollControl(tableModelAntragUebersicht.getAktuelleAntragID());
			}
			// kein aktueller Antrag und keine Zeile selektiert -> Fehlermeldung ausgeben
			else if (row == -1) {
				String message = "Es muss ein Antrag\nin der Übersicht ausgewählt sein.";
                JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
            }
			// kein aktueller Antrag und Zeile selektiert -> Protokoll für diesen Antrag anzeigen
			else{
				// Bearbeitungsprotokoll für selektierten Antrag wird angezeigt
				new ProtokollControl(tableModelAntragUebersicht.getAntragID(row));
			}
			
		}

	}

	
	
	
	/**
	 * Öffnet das Standardmailprogramm, um eine Mail an Person zu senden 
	 * (nur wenn Mailadresse vorhanden/in DB gespeichert)
	 */
	public void sendMail() {
		
		String emailAdress = personModel.getPerson().getEmail().trim();
		String betreff = "Betreff:%20...";
		String mailtext = "Hallo%20" + personModel.getPerson().getVorname().replaceAll(" ","%20");
		
		String strURI = "mailto:" + emailAdress	+ "?subject=" + betreff + "&body=" + mailtext;

		Email.send(emailAdress, strURI);
		
	}


    /**
	 * Wechselt bei Person die Universität (Humboldt-Uni oder Kunsthochschule Weißensee)
	 */
	public void changeUniversity() {


		if (tabControl.hasToBeSaved()) {
			JOptionPane.showMessageDialog(null, "Bitte zuerst Person speichern", "Fehler", JOptionPane.ERROR_MESSAGE);
		} else {

			Uni uniAktuell = personModel.getPerson().getUni();

			Uni uniWechseln = (uniAktuell == Uni.HU) ? Uni.KW : Uni.HU;


			// Message für JOptionPane zum Wechseln der Universität
			final String uniChangeMessage = "<html><b>Soll wirklich die Universität gewechselt werden?</b><br>" +
					"Momentan eingestellt: " + uniAktuell + "<br>" +
					"Wechseln nach: " + uniWechseln + "<br>" +
					"<p> Löscht alle Anträge, für es in der neuen Uni noch kein entsprechendes Semester gibt." +
					"<p> Im Anschluss wird das Fenster geschlossen.<br> " +
					"Um die Person weiter zu bearbeiten, muss manuell auf die andere Uni gewechselt werden <br></html>";

			// JA = 0, NEIN = 1
			int eingabe = JOptionPane.showConfirmDialog(null, uniChangeMessage,
					"Universität wechseln", JOptionPane.YES_NO_OPTION);

			if (eingabe == 0) {

				// Uni im PersonModel wechseln
				personModel.changeUniversitaet(uniWechseln);

				tabControl.closeTab();

			}
		}

	}

	
	/**
	 * Bei Änderungen im PersonFormular wird angezeigt, dass sich etwas 
	 * geändert hat und eventuell gespeichert werden muss.
	 */
	@Override
	public void setSaveStatus(boolean status) {
		// Save-Status im TabComponent (Sternchen vor Namen) ändern
		tabControl.setSaveStatus(status);
		
		// Save-Status in der PersonView (Buttons) ändern
		personView.setSaveStatus(status, status);
		
	}
	
	

	
	
	
	/**
	 * Überprüfen, ob Matrikelnummer schon in Datenbank exisitert.
	 * 
	 * @param matrikelnr zu prüfende Matrikelnummer
	 * @return exisitiert Matrikelnummer bereits? ja/nein
	 */
	private boolean checkExistingMatrikel(String matrikelnr) {

		return null != new DBHandlerPerson().getPersonByMatrikelnummer(matrikelnr);

	}

    public void deletePerson() {

        personModel.deletePerson();

        tabControl.closeTab();

    }


	public void showLastPerson() {

		personModel.setLastPerson();

		fillTables(personModel.getPerson().getPersonID());

	}

	public void showNextPerson() {

		personModel.setNextPerson();

        fillTables(personModel.getPerson().getPersonID());

	}
}
