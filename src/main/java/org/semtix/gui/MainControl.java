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



package org.semtix.gui;


import org.apache.log4j.Logger;
import org.semtix.config.SettingsExternal;
import org.semtix.config.UniConf;
import org.semtix.db.dao.Person;
import org.semtix.db.hibernate.HibernateUtil;
import org.semtix.gui.tabs.ButtonTabComponent;
import org.semtix.gui.tabs.TabControl;
import org.semtix.gui.tabs.antrag.AntragIndex;
import org.semtix.shared.daten.enums.Uni;

import javax.swing.*;
import java.io.File;
import java.util.List;


/**
 * Hauptcontroller steuert die MainView (Hauptfenster)
 */
public class MainControl {

    Logger logger = Logger.getLogger(MainControl.class);
    private MainView mainView;
	private MainPanel mainPanel;
	private boolean saveStatus;

	/**
	 * Erzeugt MainControl
	 */
	public MainControl() {

		// MainView mit Frame, Menue, ToolBar, Statuszeile und
		// MainPanel erstellen
		mainView = new MainView(this);
		
		// Holt sich vom MainView das MainPanel
		mainPanel = mainView.getMainPanel();
		
		// Statuszeile aktualisieren
		updateStatusPanel();
		
	}
	
	
	/**
	 * TabControl für Anträge erstellen und der MainView hinzufügen.
     *
     * @param indexList Der Filter
     */
    public void addTab(List<AntragIndex> indexList) {
        TabControl tabControl = new TabControl(this);
        tabControl.setFilter(indexList);
        mainPanel.addTab(tabControl.getTabView(), tabControl.getTabComponent());
    }


    /**
	 * TabControl für vorhandene Person erstellen und der MainView hinzufügen.
	 *
     * @param personID ID
     */
	public void addTab(int personID){
		TabControl tabControl = new TabControl(this);
		tabControl.setPerson(personID);
		mainPanel.addTab(tabControl.getTabView(), tabControl.getTabComponent());
	}


    /**
     * TabControl für vorhandene Person erstellen und der MainView hinzufügen.
     *
     * @param person Person Objekt
     */
    public void addTab(Person person) {
        TabControl tabControl = new TabControl(this);
        tabControl.setPerson(person);
        mainPanel.addTab(tabControl.getTabView(), tabControl.getTabComponent());
    }

	/**
	 * TabControl für neue Person erstellen und der MainView hinzufügen.
     *
     * @param nachname nachname
     * @param vorname vorname
     * @param matrikelnr matrikelnummer
     */
	public void addTab(String nachname, String vorname, String matrikelnr){
        int returnvalue = JOptionPane.showConfirmDialog(null, "Wirklich " + vorname + " " + nachname + " (" + matrikelnr + ") anlegen? ");
        if (returnvalue == JOptionPane.YES_OPTION) {
            TabControl tabControl = new TabControl(this);
            tabControl.setPerson(nachname, vorname, matrikelnr);

            mainPanel.addTab(tabControl.getTabView(), tabControl.getTabComponent());
        }
    }
	
	
	/**
	 * TabControl von der MainView entfernen.
     *
     * @param buttonTabComponent Tab das zu entfernen ist
     */
	public void removeTab(ButtonTabComponent buttonTabComponent) {
		mainPanel.removeTab(buttonTabComponent);
	}

	
	/**
	 * Aktuelle Universität in UniConf setzen und MainView aktualisieren.
     * @param uni Uni-Objekt
     */
	public void setUni(Uni uni){
		// aktuelle Uni in UniConf setzen
		UniConf.setUniversitaet(uni);
		// MainView aktualisieren
		mainView.changeUni();
	}
	
	

	/**
	 * Aktualisiert das StatusPanel mit der Anzeige des aktuell gesetzten Semesters
	 */
	public void updateSemester() {
		mainView.updateStatusPanel();
	}
	
	
	
	/**
	 * Statuszeile aktualisieren.
	 */
	public void updateStatusPanel(){
		mainView.updateStatusPanel();
	}
	

	

	/**
	 * Aufräumarbeiten bevor das Fenster geschlossen und die Anwendung
	 * beendet wird (Klick auf Kreuz rechts oben oder im Menü).
	 */
	public void cleanUpBeforeExit() {


        String message = "";

        if (null != SettingsExternal.OUTPUT_PATH) {
            File directory = new File(SettingsExternal.OUTPUT_PATH);

            if (!directory.exists()) {
                message = directory + " existiert nicht ";
            } else if (!directory.isDirectory()) {
                message = directory + " ist kein Verzeichnis ";

            } else if (null == directory.listFiles()) {
                message = directory + " hat wahrscheinlich falsche Berechtigungen";
            } else {
                for (File file : directory.listFiles()) {
                    file.delete();
                }
            }
        } else {
            message = "Ausgabeverzeichnis nicht in properties angegeben.";
        }


        //alle temporären Dateien löschen. Verzeichnisse bleiben bestehen.
        if (message.length() > 0)
            logger.warn(message);


		//TODO optional: Vor dem Schließen schauen, ob ein Tab noch ungespeichert ist bzw. die Tabs einzeln schließen.
		// DAZU LISTE VON TABS FÜHREN UND JEWEILS NACHSCHAUEN OB MINDESTENS EINE UNGESAVED
		// DANN NACHFRAGEN OB SICHERN ?
		// WENN JA; ALLE TABS SICHERN
		// WENN ABBRECHEN NICHTS
		// WENN NEIN: Schließen

        // Datenbankverbindung schließen
        HibernateUtil.shutdown();

        // Fenster schließen
        mainView.close();
    	
    	// Programm beenden
    	System.exit(0);
		
	}
}
