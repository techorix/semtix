/*
 * Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 *        Semesterticketbüro der Humboldt-Universität Berlin
 *
 * Copyright (c) 2015. Michael Mertins (MichaelMertins@gmail.com)
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


import org.semtix.db.dao.Antrag;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Schritt 6 des Dialogs zur Zuschussberechnung
 */
@SuppressWarnings("serial")
class PanelStep5
extends GenericPanelStep {

    private JPanel buttonPanel;

    private JButton printButton;

    /**
     * {see GenericPanelStep}
     *
     * @param bModel     ModelAuszahlungsmodul
     * @param titel      titel
     * @param untertitel untertitel
     */
    protected PanelStep5(ModelAuszahlungsmodul bModel, String titel, String untertitel) {
        super(bModel, titel, untertitel);
    }

    @Override
    protected void additionalInitStuff() {
        printButton = new JButton("Drucken der Barauszahlungsanordnungen...");

        buttonPanel = new JPanel();

        buttonPanel.add(printButton);

        formular.add(buttonPanel, 0, 8, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 0, 0));

        formular.add(new JPanel(), 0, 9, 1, 1, 1.0, 1.0, 2, 18, new Insets(0, 0, 0, 0));


    }

    @Override
    protected void updatePanel() {

        final List<Antrag> antraege = bModel.getAntragListeBarauszahler();


        for(ActionListener act : printButton.getActionListeners()) {
            printButton.removeActionListener(act);
        }




        printButton.addActionListener(new ActionPrintAntragList(antraege, this, "(5) Barauszahlungsanordnungen gedruckt und Anträge in DB auf ausgezahlt gesetzt", false));
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //IN DB auf ausgezahlt setzen
                bModel.updateAntraegeAuszahlung(antraege);
            }
        });



        this.logTextArea.refresh();
    }

}
