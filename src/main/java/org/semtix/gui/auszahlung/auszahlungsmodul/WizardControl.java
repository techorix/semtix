/*
 * Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 *        Semesterticketbüro der Humboldt-Universität Berlin
 *
 * Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
 * 2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.semtix.gui.auszahlung.auszahlungsmodul;


import java.util.ArrayList;
import java.util.List;

/**
 * Steuerung des Dialogs (Wizard) zur Zuschussberechnung.
 * Der Dialog (Wizard) enthält 4 Seiten (Panel), durch die
 * man sich durchklicken und Daten eingeben kann. Die Steuerung
 * dafür übernimmt die Klasse WizardControl.
 */
public class WizardControl {

    private DialogWizard dialog;

    private int panelIndex;

    private List<GenericPanelStep> listSteps;


    /**
     * Konstruktor mit der View und dem Model
     *
     * @param dialog DialogWizard (View)
     * @param bModel ModelAuszahlungsmodul (Model)
     */
    public WizardControl(DialogWizard dialog, ModelAuszahlungsmodul bModel) {

        this.dialog = dialog;

        this.listSteps = new ArrayList<GenericPanelStep>();


        listSteps.add(new PanelStep1(bModel, "Schritt 1: Datenabgleich mit Immatrikulationsbüro", "Fürs Immatrikulationsbüro speichern und dann deren Datei einlesen."));
        listSteps.add(new PanelStep2(bModel, "Schritt 2: Semesterdaten und Fondshöhe", "Verfügbares Semester unten auswählen, dann ausfüllen"));
        listSteps.add(new PanelStep3(bModel, "Schritt 3: Zuschussberechnung", "Berechnung der Zuschüsse ohne Erstis/Nothilfe/Ratenzahler"));
        listSteps.add(new PanelStep4(bModel, "Schritt 4: Ergebnis und Bescheide drucken", "Folgende Werte werden für das Drucken verwendet:"));
        listSteps.add(new PanelStep5(bModel, "Schritt 5: Barauszahlungen Drucken", "Hier kannst du die Bar-Auszahlungsanordnungen drucken lassen."));
        listSteps.add(new PanelStep6(bModel, "Schritt 6: Auszahlungsdatei erstellen", "Auszahlungsdatei erstellen und das zugehörige Deckblatt drucken"));
        panelIndex = 0;


    }


    /**
     * List of PanelSteps
     *
     * @return List of Panelsteps
     */
    List<GenericPanelStep> getListSteps() {
        return this.listSteps;
    }



    /**
     * Der Button "weiter" wurde geklickt und das nächste Panel im Wizard wird angezeigt
     */
    public void stepUp() {
        dialog.setZurueckButton(true);

        // darf ins nächste Panel wechseln
        if (listSteps.get(panelIndex).checkTransition()) {

            panelIndex++;

            if (panelIndex < listSteps.size()) {
                //update nächstes Panel
                listSteps.get(panelIndex).updatePanel();

                if (panelIndex == listSteps.size() - 1) {
                    dialog.setWeiterButton(false);
                    dialog.setFertigButton(true);
                }

                dialog.nextCard();
            }
        }


    }


    /**
     * Der Button "zurück" wurde geklickt und das vorherige Panel im Wizard wird angezeigt
     */
    public void stepDown() {

        dialog.setFertigButton(false);
        dialog.setWeiterButton(true);

        panelIndex--;

        if (panelIndex == 0) {
            dialog.setZurueckButton(false);
        } else {
            dialog.setZurueckButton(true);
        }


        dialog.lastCard();
    }


}
