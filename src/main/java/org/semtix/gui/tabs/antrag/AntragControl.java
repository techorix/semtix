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

import org.semtix.config.SemesterConf;
import org.semtix.db.DBHandlerAntrag;
import org.semtix.db.DBHandlerConf;
import org.semtix.db.DBHandlerPerson;
import org.semtix.db.DBHandlerVorgaenge;
import org.semtix.db.dao.*;
import org.semtix.gui.tabs.TabControl;
import org.semtix.gui.tabs.TabInterface;
import org.semtix.gui.tabs.antrag.nachreichungen.tabelle.TableModelUnterlagen;
import org.semtix.shared.actions.Email;
import org.semtix.shared.daten.DeutschesDatum;
import org.semtix.shared.daten.MyException;
import org.semtix.shared.daten.enums.AntragStatus;
import org.semtix.shared.daten.enums.UnterlagenStatus;
import org.semtix.shared.daten.enums.Vorgangsart;
import org.semtix.shared.print.OdtTemplate;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Control-Klasse zur Steuerung von View und Model für die Antragsbearbeitung. AntragPanel (View) und 
 * AntragModel (Model) werden in dieser Klasse erstellt.
 */
public class AntragControl
		implements TabInterface {
	
	private TabControl tabControl;
	
	private AntragModel antragModel;

	private TableModelUnterlagen tableModelUnterlagen;

	private AntragView antragView;
	
	private AntragPanel antragPanel;

    private PagingPanel pagingPanel;

    private List<AntragIndex> indexListe;

    private int index;

	
	
	/**
	 * Erstellt einen neuen AntragControl
	 * @param tabControl TabControl
	 */
	public AntragControl(TabControl tabControl) {

		this.tabControl = tabControl;
		
		// TableModel für Unterlagen erstellen
		tableModelUnterlagen = new TableModelUnterlagen();
		
		// AntragModel erstellen
		antragModel = new AntragModel();
		
		// AntragPanel erstellen
		antragPanel = new AntragPanel(this);
				
		// View als Observer beim Model anmelden
		antragModel.addObserver(antragPanel);
		
		antragPanel.addListener(this);

		// Panel mit Buttons zum Durchblättern durch die Anträge
		pagingPanel = new PagingPanel(tabControl);
		
		pagingPanel.setListener(this);
		
		// AntragView mit AntragPanel und PagingPanel erstellen
		antragView = new AntragView(antragPanel);

		// Panel zum TabView (CardLayout) hinzufügen
		tabControl.addView(antragView.getMainPanel(), "Antrag");

		
	}
	
	
	/**
	 * Fügt der AntragView ein Panel mit Buttons zum Durchblättern 
	 * durch die Anträge hinzu.
	 */
	public void setPagingPanel() {
		antragView.setPagingPanel(pagingPanel);
	}
	

	/**
     * Setzt den Index der Antragsliste
     * @param index Index
     */
    public void setIndex(int index) {
        this.index = index;
        updateAntrag();
    }
	

	
	/**
     * Indexliste aus DB füllen, wenn Filter vorhanden
     * @param filter Filter
     */
	public void setIndexListe(List<AntragIndex> filter) {

		this.indexListe = filter;

        if (filter != null) {

			// gefilterte Liste aus DB hat keine Einträge
			if (null == indexListe || indexListe.size() == 0) {

				JOptionPane.showMessageDialog(null, "Keine Daten gefunden", "Fehler", JOptionPane.WARNING_MESSAGE);

			} else {
				// Index auf erste Person in Liste setzen
				index = 0;

				pagingPanel.buildButtons(indexListe, this);
			}
		}

	}

	/**
	 * Aktualisiert die Index-Liste der Anträge
	 */
	public void updateAntrag(){
		
		// OK-Anzeige bei den Berechnungs-Buttons im AntragPanel zurücksetzen (entfernen)
//		antragPanel.resetOKBerechnung();

		String indexText = (indexListe.size()==0 ? "<html>keine<br>Anträge</html>" : (index+1) + "/" + indexListe.size());

		// Falls Einträge in DB gefunden wurden (mind. 1 Objekt in Index-Liste)
		if(indexListe.size()>0) {

            tableModelUnterlagen.setUnterlagen(indexListe.get(index).getAntragID());


            antragModel.setAntrag(indexListe.get(index).getAntragID());


            pagingPanel.setIndexAnzeige(indexText);

            updateButtons();


        }

    }
	
	
	
	/**
	 * Anzeige der Buttons zum Blättern und Filtern aktualisieren
	 */
	public void updateButtons(){
		
		if(indexListe.size()>0){
			
			// Button mit aktuellem Anfangsbuchstaben farbig markieren
			pagingPanel.setButtonColor(String.valueOf(getCapital()));
			
		}
		
		
		
		// wenn index<=0 erreicht (1. Eintrag)
		if(index<=0) {
			pagingPanel.enableBackwardButton(false);
			pagingPanel.enableFirstIndexButton(false);
		}
		else {
			pagingPanel.enableBackwardButton(true);
			pagingPanel.enableFirstIndexButton(true);
		}
			
		// wenn index Maximum erreicht (letzter Eintrag)
		if(index >= (indexListe.size()-1)) {
			pagingPanel.enableForwardButton(false);
			pagingPanel.enableLastIndexButton(false);
		}
		else {
			pagingPanel.enableForwardButton(true);
			pagingPanel.enableLastIndexButton(true);
		}

    }

	
	/**
	 * Liefert den Anfangsbuchstaben für einen bestimmten Index in der Antragsliste
	 * @return Anfangsbuchstabe
	 */
	public char getCapital(){
		return indexListe.get(index).getCapital();
	}
	
	

	/**
	 * Blättert vorwärts in der Indexliste mit den Anträgen
	 */
	public void plus(){
		index++;
		updateAntrag();
	}
	
	
	/**
	 * Blättert rückwärts in der Indexliste mit den Anträgen
	 */
	public void minus(){
		index--;
		updateAntrag();
	}
	
	
	/**
	 * Blättert zum ersten Eintrag in der Indexliste mit den Anträgen
	 */
	public void firstIndex(){
		index = 0;
		updateAntrag();
	}
	
	
	/**
	 * Blättert zum letzten Eintrag in der Indexliste mit den Anträgen
	 */
	public void lastIndex(){
		index = indexListe.size() - 1;
		updateAntrag();
	}
	
	
	
	/**
	 * Zeigt an, ob der Filter gesetzt wurde
	 * @return Filter gestzt? ja/nein
	 */
	public boolean isFilter() {
		return tabControl.isFilter();
	}

	
	/**
	 * Setzt einen Filter und aktualisiert das PagingPanel und das AntragsFormular
	 * @param filter Filter
	 */
	public void setFilter(List<AntragIndex> filter) {

		//TODO Filterkram usw. auslagern in TabControl o.ä. damit es auch mit Personen geht; z.B. Decorator oder einfach Vererbung. Dort dann am besten auch Tastaturkürzel usw. festlegen. Bei Drücken auf "F"-Button geschieht noch umbenennung des Tabs

		setIndexListe(filter);
		
		updateAntrag();
		
	}
	
	

	/**
	 * Setzt den Filter auf die Standardwerte zurück (reset)
	 */
	public void resetFilter(){
		
		tabControl.resetFilter();

		setIndexListe(tabControl.getFilter());
		
		//panel.buildButtons(this);
		
		updateAntrag();
		
	}



	// *******************************************************
	
	
	

	/**
	 * Legt einen neuen Vorgang für die Erst- oder Zweitrechnung im Bearbeitungsprotokoll an
	 * @param vorgangsart Vorgangsart
	 */
	public void setRechnung(Vorgangsart vorgangsart) {
		
		// DBHandler erstellen
		DBHandlerVorgaenge dbHandlerVorgaenge = new DBHandlerVorgaenge();
		DBHandlerAntrag dbHandlerAntrag = new DBHandlerAntrag();
		
		//Neuen Vorgang anlegen (mit Standardwerten)
		Vorgang vorgang = new Vorgang();
		
		// Antrag-ID im Vorgang setzen
		vorgang.setAntragID(antragModel.getAntrag().getAntragID());
		
		// Vorgangsart im Vorgang setzen
		vorgang.setVorgangsart(vorgangsart);
		
		// Neuen Vorgang in DB erstellen
		dbHandlerVorgaenge.createVorgang(vorgang);


        // Timestamp für "letzte Änderung" des Antrags aktualisieren

        dbHandlerAntrag.updateAntrag(antragModel.getAntrag());


        // View über Model aktualisieren (Observer)
        antragModel.updateView();

	}
	
	

	/**
	 * Zeigt einen neuen Antrag im Formular an
	 * @param person Person
	 */
	public void setNewAntrag(Person person) {
        if (null != SemesterConf.getSemester().getSemesterJahr()) {
            antragModel.setNewAntrag(person);
            antragPanel.setSaveStatus(false, false);
            tabControl.setSaveStatus(false);
            tabControl.setCards("Antrag");
        } else {
            JOptionPane.showMessageDialog(null, "Kein Semester eingestellt. \n Bitte erst ein Semester anlegen.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
	
	

	/**
	 * Zeigt einen bestehenden Antrag im Formular an
	 * @param antragID Antrag-ID
	 * @param person Person, zu welcher der Antrag gehört
	 */
	public void setAntrag(int antragID, Person person) {
		//filter.setAntragID(antragID);
        tableModelUnterlagen.setUnterlagen(antragID);
        antragModel.setAntrag(antragID, person);
        setSaveStatus(false);
		tabControl.setCards("Antrag");


        for (int i = 0; i < antragPanel.getTable().getRowCount(); i++) {
            int length = getTableModelUnterlagen().getUnterlagen(i).getText().length();
            if (length < 60)
                length = 60;

            else if (length > 500)
                length = 400;

            else if (length > 260)
                length = 260;

            antragPanel.getTable().setRowHeight(i, length / 2);
        }

	}
	
	
	/**
	 * Antragsdaten zurücksetzen (zum letzten gespeicherten Zustand)
	 */
	public void resetAntrag() {
		antragModel.resetAntrag();
		setSaveStatus(false);
	}
	
	
	
	/**
	 * Liefert das AntragModel
	 * @return AntragModel
	 */
	public AntragModel getAntragModel() {
		return antragModel;
	}
	
	
	/**
	 * Liefert das TableModel für die Unterlagen
	 * @return TableModelUnterlagen
	 */
	public TableModelUnterlagen getTableModelUnterlagen() {
		return tableModelUnterlagen;
	}
	
	
	/**
	 * Unterlagen zum TableModel hinzufügen
	 * @param unterlagen hinzufügende Unterlagen
	 */
	public void insertUnterlagen(Unterlagen unterlagen) {
		tableModelUnterlagen.addUnterlagen(unterlagen);
	}
	
	/**
	 * Unterlagen im TableModel aktualisieren
	 * @param unterlagen zu aktualisierende Unterlagen
	 */
	public void updateUnterlagen(Unterlagen unterlagen) {
		tableModelUnterlagen.updateUnterlagen(unterlagen);
	}
	
	/**
	 * Unterlagen im TableModel löschen
	 * @param row zu löschende Unterlagen
	 */
	public void deleteUnterlagen(int row) {
		tableModelUnterlagen.deleteUnterlagen(row);
	}

    public void deleteSelectedNachfrage() {
        tableModelUnterlagen.deleteUnterlagen(antragPanel.getSelectedNachreichungFromTable());
    }

	
	public void printAntrag() throws IOException {
		OdtTemplate odtTemplate = new OdtTemplate();
		odtTemplate.printBescheid(antragModel.getAntrag());
		antragModel.updateView();
	}


	public void printAntragAZA() throws IOException {
		OdtTemplate odtTemplate = new OdtTemplate();
		odtTemplate.printAZA(antragModel.getAntrag());
		antragModel.updateView();
	}
	
	
	/**
     * Nachfragebriefe oder Mahnungen erstellen und drucken. Vorher wird ggf. noch eine E-Mail geschrieben.
     * @param frist gesetzte Frist
	 * @param vorgangsart Vorgangsart
     * @param wiederholtesDrucken ist dieser Druckvorgang nur eine Wiederholung? Wenn ja, dann keine Vorgänge anlegen und die Unterlegen nicht verändern
     */
    public void printUnterlagen(Date frist, Vorgangsart vorgangsart, boolean wiederholtesDrucken) {

        List<Unterlagen> liste = tableModelUnterlagen.getUnterlagenListe();

        if (null != liste && liste.size() > 0) {
            Antrag antrag = antragModel.getAntrag();

            Person person = antragModel.getPerson();

            Semester semester = antragModel.getSemester();

            OdtTemplate templatePrint = new OdtTemplate();

            List<Unterlagen> unterlagenList = new ArrayList<Unterlagen>();

            for (Unterlagen u : liste) {
                if (!u.getUnterlagenStatus().equals(UnterlagenStatus.VORHANDEN)) {
                    unterlagenList.add(u);
                }
            }

            if (unterlagenList.size() > 0) {

                GregorianCalendar nachfragefrist = null;
                GregorianCalendar mahnfrist = null;
                for (Unterlagen u : unterlagenList) {
                    if (u.getUnterlagenStatus().equals(UnterlagenStatus.NACHGEFORDERT))
						if (null != u.getFristNachfrage() &&
								u.getFristNachfrage().compareTo(new GregorianCalendar()) > 0) {
							nachfragefrist = u.getFristNachfrage();
							break;
                        } else if (u.getUnterlagenStatus().equals(UnterlagenStatus.GEMAHNT))
                            if (u.getFristMahnung().compareTo(new GregorianCalendar()) > 0) {
                                mahnfrist = u.getFristMahnung();
                                break;
                            }
                }

                int returnvalue = JOptionPane.YES_OPTION;
                if (null != nachfragefrist) {
                    returnvalue = JOptionPane.showConfirmDialog(null, "Nachfragefrist läuft noch bis zum " + DeutschesDatum.DATUMSFORMAT.format(nachfragefrist.getTime()) + ". Fortfahren?");
                } else if (null != mahnfrist) {
                    returnvalue = JOptionPane.showConfirmDialog(null, "Mahnfrist läuft noch bis zum " + DeutschesDatum.DATUMSFORMAT.format(mahnfrist.getTime()) + ". Fortfahren?");
                }


                //E-Mail-Schicken und Drucken
                if (returnvalue == JOptionPane.YES_OPTION) {

					DBHandlerConf dbHandlerConf = new DBHandlerConf();
					String betreff, mailtext;
					if (Vorgangsart.MAHNUNG.equals(vorgangsart)) {
						betreff = dbHandlerConf.read("betreffMahnungEmail");
						mailtext = dbHandlerConf.read("textMahnungEmail");
					} else {
						betreff = dbHandlerConf.read("betreffNachfragEmail");
						mailtext = dbHandlerConf.read("textNachfrageEmail");
					}

					if (betreff == null)
						betreff = "";

					if (mailtext == null)
						mailtext = "";

					if (mailtext.contains("<Name>")) {
						//Namensstrings können Leerzeichen enthalten:
						String nachname = getPerson().getNachname().trim();
						String vorname = getPerson().getVorname().trim();
						nachname = nachname.replaceAll(" ", "%20");
						vorname = vorname.replaceAll(" ", "%20");
						mailtext = mailtext.replace("<Name>", vorname + "%20" + nachname);
					}

					String emailAdress = getPerson().getEmail();

                    String strURI = "mailto:" + emailAdress + "?subject=" + betreff + "&body=" + mailtext;

					Email.send(emailAdress, strURI);

                    DBHandlerVorgaenge dbHandlerVorgaenge = new DBHandlerVorgaenge();

                    GregorianCalendar zeit = null;

                    //Finde das letzte mal Nachfragen|Mahnen heraus
                    if (vorgangsart.equals(Vorgangsart.MAHNUNG)) {
                        List<Vorgang> vorgaenge = dbHandlerVorgaenge.getVorgaengeListe(antrag.getAntragID());
                        for (Vorgang v : vorgaenge) {
                            if (v.getVorgangsart().equals(Vorgangsart.MAHNUNG) || v.getVorgangsart().equals(Vorgangsart.NACHFRAGEBRIEF)) {
                                if (null == zeit)
                                    zeit = v.getZeitstempel();
                                else {
                                    if (v.getZeitstempel().compareTo(zeit) > 0) {
                                        zeit = v.getZeitstempel();
                                    }
                                }
                            }
                        }
                    }

                    try {
                        templatePrint.printLetter(unterlagenList, frist, person, semester, vorgangsart, zeit);
                    } catch (MyException e) {

                        JOptionPane.showMessageDialog(null, e.getMessage());

                        e.printStackTrace();
                    } catch (IOException ioe) {

                        JOptionPane.showMessageDialog(null, "Konnte Datei nicht erstellen");

                    } catch (NullPointerException npe) {

                        JOptionPane.showMessageDialog(null, npe.getLocalizedMessage() + npe.getMessage());
                    }


                    if (!wiederholtesDrucken) {
                        dbHandlerVorgaenge.createVorgang(vorgangsart, antrag.getAntragID());


                        GregorianCalendar cal = new GregorianCalendar();
                        cal.setTime(frist);

                        for (Unterlagen u : unterlagenList) {
                            if (vorgangsart.equals(Vorgangsart.MAHNUNG)) {
                                u.setFristMahnung(cal);
                                u.setUnterlagenStatus(UnterlagenStatus.GEMAHNT);
                            } else {
                                u.setFristNachfrage(cal);
                                u.setUnterlagenStatus(UnterlagenStatus.NACHGEFORDERT);
                            }

                            tableModelUnterlagen.updateUnterlagen(u);
                        }
                    }
                }
                } else {
                    JOptionPane.showMessageDialog(null, "Alle Unterlagen vorhanden. Ggf. bitte Status-Farben der Textbausteine überprüfen.");
                }
        } else {
            JOptionPane.showMessageDialog(null,"Es wurden noch keine Nachfrage-Textbausteine hinzufügt. Deshalb kann noch nicht gedruckt werden.");
        }
    }


    /**
	 * Zeigt das Formular mit den Personendaten an
	 */
	public void showPerson() {
		tabControl.setPerson(antragModel.getPerson().getPersonID());
	}
	
	
	
	/**
	 * Zeigt den Berechnungszettel an
	 */
	public void showBerechnungszettel() {
		tabControl.showBerechnungszettel();
	}
	
	
	/**
	 * Liefert die AntragID aus dem AntragModel (des momentan gesetzten Antrags)
	 * @return AntragID
	 */
	public int getAntragID() {
		return antragModel.getAntrag().getAntragID();
	}
	
	
	/**
	 * Liefert den momentan gestzten Antrag aus dem AntragModel
	 * @return Antrag
	 */
	public Antrag getAntrag() {
		return antragModel.getAntrag();
	}
	
	
	/**
	 * Liefert die Peson aus dem AntragModel (des momentan gesetzten Antrags)
	 * @return Person
	 */
	public Person getPerson() {
		return antragModel.getPerson();
	}
	
	
	/**
	 * Zeigt an, ob bei der momentan gesetzten Person englischsprachig ausgewählt ist
	 * @return englischsprachig? ja/nein
	 */
	public boolean isEN() {
		return tabControl.getPersonControl().getPerson().isEnglischsprachig();
	}
	
	
	/**
	 * Speichert den Antrag mit den aktuellen Daten im AntragModel
     * @param antragStatus AntragStatus
     * @param punkteEinkommen Punkte Einkommen
     * @param punkteHaerte Punkte Härten
     * @param teilzuschuss Teilzuschuss ja/nein
     * @param anzahlMonate Anzahl Monate für Teilzuschuss
     * @param raten Ratenzahlung ja/nein
     * @param nothilfe Nothilfe ja/nein
     * @param kulanz Kulanz ja/nein
     * @param erstsemester Erstsemester ja/nein
     * @param manAuszahlen Barauszahler ja/nein
     * @param auszahlung Schon ausgezahlt ja/nein
     * @param begruendung Begründungstext (für Teilauszahler)
	 * @param bescheidVersandt Bescheid schon versendet ja/nein
     */
	public void saveAntrag(AntragStatus antragStatus, int punkteEinkommen, int punkteHaerte,
                           boolean teilzuschuss, int anzahlMonate, boolean raten, boolean nothilfe,
						   boolean kulanz, boolean erstsemester, boolean manAuszahlen, boolean auszahlung, String begruendung, boolean bescheidVersandt) {
		
		Antrag antrag = antragModel.getAntrag();
		
		antrag.setAntragStatus(antragStatus);
		antrag.setPunkteEinkommen(punkteEinkommen);
		antrag.setPunkteHaerte(punkteHaerte);
		antrag.setTeilzuschuss(teilzuschuss);
		antrag.setAnzahlMonate(anzahlMonate);
		antrag.setRaten(raten);
		antrag.setNothilfe(nothilfe);
		antrag.setKulanz(kulanz);
		antrag.setErstsemester(erstsemester);

		antrag.setManAuszahlen(manAuszahlen);

		antrag.setAuszahlung(auszahlung);

        antrag.setBegruendung(begruendung);

		antrag.setGesendet(bescheidVersandt);
		antrag.setGedruckt(bescheidVersandt);

		antragModel.saveAntrag();
		
		// Save-Status wird wieder deaktiviert
		setSaveStatus(false);
		
	}
	
	
	
	/**
	 * AntragModel aktualisiert die View (OBserver)
	 */
	public void updateView() {
		antragModel.updateView();
	}


	/**
	 * Den Status zum Speichern in TabControl und AntragPanel setzen (Anzeige, dass sich 
	 * etwas geändert hat und eventuell gespeichert werden müsste)
	 */
	@Override
	public void setSaveStatus(boolean status) {
		
		// Save-Status im TabComponent (Sternchen vor Namen) ändern
		tabControl.setSaveStatus(status);
		
		// Save-Status in der PersonView (Buttons) ändern
		antragPanel.setSaveStatus(status, status);
		
	}

	/**
	 * Löscht den gerade angezeigten Antrag von der Datenbank
	 */
	public void deleteAntrag() {
		DBHandlerAntrag dbHandlerAntrag = new DBHandlerAntrag();
		dbHandlerAntrag.delete(this.getAntragID());

	}

	/**
	 * löscht den letzten Vorgang
	 *
	 * @param vorgangsart Art des Vorgangs
	 */
	public void removeRechnung(Vorgangsart vorgangsart) {

		DBHandlerVorgaenge dbHandlerVorgaenge = new DBHandlerVorgaenge();


		List<Vorgang> liste = dbHandlerVorgaenge.getVorgaengeListe(getAntragID());
		for (Vorgang v : liste) {
			if (v.getVorgangsart().equals(vorgangsart)) {
				dbHandlerVorgaenge.delete(v);
			}
		}


		// View über Model aktualisieren (Observer)
		antragModel.updateView();

	}

    /**
     * @return hat einen Kulanzantrag gestellt
     * @param i Wieviele Semester
     */
    public boolean checkKulanz(int i) {
        return new DBHandlerPerson().hatKulanzAntragGestellt(getPerson().getPersonID(), i);
    }


    public AntragPanel getAntragPanel() {
        return this.antragPanel;
    }

	public void updateNachreichungen() {
		antragPanel.setNachreichungen(antragModel.uncheckedNachreichungen());
	}
}
