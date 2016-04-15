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

package org.semtix.gui.tabs;

import org.semtix.db.dao.Person;
import org.semtix.gui.MainControl;
import org.semtix.gui.tabs.antrag.AntragControl;
import org.semtix.gui.tabs.antrag.AntragIndex;
import org.semtix.gui.tabs.berechnungszettel.BerechnungControl;
import org.semtix.gui.tabs.personendaten.PersonControl;

import javax.swing.*;
import java.util.List;

/**
 * Steuert das TabView (Panel mit CardLayout)
 */
public class TabControl {
	
	private MainControl mainControl;
	
	private TabView tabView;
	
	private ButtonTabComponent buttonTabComponent;

	private PersonControl personControl;
	private AntragControl antragControl;
	private BerechnungControl berechnungControl;

	private boolean isFilter;
	
	private List<AntragIndex> indexList;
	private boolean saveStatus;


	/**
	 * Standardkonstruktor erstellt TabView, ButtonTabComponent, PersonControl, 
	 * AntragControl, BerechnungsControl.
	 * 
	 * @param mainControl MainControl
	 */
	public TabControl(MainControl mainControl) {

        this.mainControl = mainControl;

		isFilter = false;

        tabView = new TabView();

		personControl = new PersonControl(this);

		antragControl = new AntragControl(this);

		berechnungControl = new BerechnungControl(this);
		
		buttonTabComponent = new ButtonTabComponent(this);
		
	}
	
	
	
	public void addView(JComponent view, String name) {
		tabView.addView(view, name);
	}
	
	
	// bestehende Person in PersonFormular anzeigen
	public void setPerson(int personID) {
		personControl.setPerson(personID);
		setSaveStatus(false);
		
		if(isFilter)
			personControl.setBottomPanel();
		
		tabView.setCards("Person");
	}


	public void setPerson(Person person) {
		personControl.setPerson(person);
		setSaveStatus(false);

		if (isFilter)
			personControl.setBottomPanel();

		tabView.setCards("Person");
	}
	
	// neue Person in PersonFormular anzeigen
	public void setPerson(String nachname, String vorname, String matrikelnr) {

		personControl.setNewPerson(nachname, vorname, matrikelnr);
        setSaveStatus(false);
        tabView.setCards("Person");
	}

    public MainControl getMainControl() {
        return mainControl;
    }

	public TabView getTabView() {
		return tabView;
	}

	public List<AntragIndex> getFilter() {
		return indexList;
	}
	
	public boolean isFilter() {
		return isFilter;
    }

	public void setFilter(List<AntragIndex> indexList) {

		this.indexList = indexList;

		antragControl.setFilter(indexList);

		antragControl.setPagingPanel();

        isFilter = true;

        setSaveStatus(false);

        tabView.setCards("Antrag");

	}
	
	public void resetFilter() {
		isFilter = false;
	}
	
	
	public ButtonTabComponent getTabComponent() {
		return buttonTabComponent;
	}
	
		
	
	
	public PersonControl getPersonControl() {
		return personControl;
	}
	
	
	
	
	public AntragControl getAntragControl() {
		return antragControl;
	}


	
	// TabComponent aktualisieren (Name und Farbe für Uni, Save-Status)
	public void updateTabComponent() {
		
		if(isFilter) {
			buttonTabComponent.update(null, getFilter(), hasToBeSaved());
		}
		else
			buttonTabComponent.update(personControl.getPerson(), null, hasToBeSaved());
		
	}
	

	
	
	// Zeigt an, ob im Formular Änderungen gemacht wurden und evtl. gespeichert werden muss
	// Name im Tab bekommt Sternchen und Buttons werden (de-)aktiviert
	public void setSaveStatus(boolean status){

		this.saveStatus = status;
		
		updateTabComponent();

	}


	public boolean hasToBeSaved() {
		return this.saveStatus;
	}
	
	
	public void showBerechnungszettel() {
		
		// BerechnungView (Zettel 1 und 2) initialisieren
        berechnungControl.init(antragControl.getAntragModel(), antragControl.getPerson());

		// Model aktualisiert View (Observer)
//		berechnungControl.getBerechnungModel().updateViews();

        // View (Berechnungszettel) im TabbedPane anzeigen
		tabView.setCards("Berechnung");
		
	}
	
	
	// Zeigt die gewünschte Seite (Panel) im Frame an
	public void setCards(String cardname) {
        if (saveStatus)
            closeTab();
        else
            tabView.setCards(cardname);
    }

	
	
	
	// Versucht den Tab zu schließen
	// Bei SaveModus = true kommt Abfrage wegen Speichern
	public void closeTab() {
		
		// SaveStatus = true: es gab Änderungen. Abfrage ob speichern oder nicht..
		if(saveStatus){
    		
			// Auswählbare Optionen in der OptionPane.
    		Object[] options = {"Abbrechen", "Verwerfen", "Speichern"};
    		 
    		// Ergebnis der Abfrage (selected): Speichern = 2; Verwerfen = 1; Abbrechen = 0
            int selected = JOptionPane.showOptionDialog(null,
                                                        "Die Daten wurden geändert. Soll gespeichert werden?",
                                                        "Info",
                                                        JOptionPane.DEFAULT_OPTION, 
                                                        JOptionPane.QUESTION_MESSAGE, 
                                                        null, options, options[0]);
            
            
            // geänderte Personendaten speichern
            if(selected == 2) {
                //Person personensuche = personControl.getPerson();
                //getPersonModel().savePerson(personensuche);

                if (tabView.getCurrentCardsName().equals("Person")) {
                    if (personControl.savePerson())
                        mainControl.removeTab(buttonTabComponent);
                } else if (tabView.getCurrentCardsName().equals("Antrag")) {
					try {
						antragControl.getAntragPanel().saveAntrag();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Hier ist etwas beim Speichern schiefgelaufen");
					}
					mainControl.removeTab(buttonTabComponent);
                } else if (tabView.getCurrentCardsName().equals("Berechnung")) {
                    berechnungControl.updateAntragModel(null);
                    setCards("Antrag");
                }
            } else {
                mainControl.removeTab(buttonTabComponent);
            }
        }
    	
		// SaveStatus = false: es gab keine Änderungen. Tab entfernen.
		else {
			mainControl.removeTab(buttonTabComponent);
		}
        

	}


}
