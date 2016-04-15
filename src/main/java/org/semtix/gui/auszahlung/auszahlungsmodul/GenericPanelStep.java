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

import org.semtix.shared.elements.SForm;

import javax.swing.*;
import java.awt.*;

/**
 * Oberklasse für Panelsteps ist abstrakt damit sie nicht selbst verwendet wird
 */
abstract class GenericPanelStep extends JPanel {

    protected ModelAuszahlungsmodul bModel;

    protected SForm formular;

    protected JLabel labelTitel,labelUntertitel;

    protected LogTextArea logTextArea;

    protected GenericPanelStep() {
    }

    /**
     * Konstruktor
     *
     * @param bModel     Model
     * @param titel      Titel
     * @param untertitel Untertitel
     */
    protected GenericPanelStep(ModelAuszahlungsmodul bModel, String titel, String untertitel) {

        this.logTextArea = new LogTextArea();

        this.bModel = bModel;

        SForm logForm = new SForm();
        logForm.add(logTextArea, 0,0,1,1,1.0,1.0,GridBagConstraints.BOTH,18,new Insets(10,0,10,10));


        this.formular = new SForm();

        setLayout(new BorderLayout());

        labelTitel = new JLabel(titel);
        labelTitel.setFont(labelTitel.getFont().deriveFont(Font.BOLD, 16));
        labelTitel.setForeground(new Color(0, 0, 120));

        labelUntertitel = new JLabel(untertitel);
        labelUntertitel.setFont(labelUntertitel.getFont().deriveFont(Font.ITALIC));

        formular.add(labelUntertitel,  0, 1, 5, 1, 0.0, 0.0, GridBagConstraints.BOTH, 18, new Insets(10, 10, 10, 10));

        add(labelTitel, BorderLayout.NORTH);
        add(logForm,BorderLayout.WEST);
        add(formular,BorderLayout.CENTER);

        additionalInitStuff();

    }

    /**
     * Abstract method that has to be implemented by subclasses
     */
    abstract protected void additionalInitStuff();

    protected void refreshLog() {
        this.logTextArea.refresh();
    }

    protected void updatePanel() {
        //do nothing
    }

    protected boolean checkTransition() {
        return true;
    }

}
