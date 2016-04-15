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

package org.semtix.gui.tabs.berechnungszettel.otherpanels;

import org.semtix.shared.elements.Layout;
import org.semtix.shared.elements.SForm;
import org.semtix.shared.elements.control.DocumentSizeFilter;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;

/**
 * Panel im Berechnungszettel (Seite 1), welches die Angaben zu Person und Zeichnung anzeigt
 */
@SuppressWarnings("serial")
public class HeadPanel
        extends SForm {

    private JLabel valueMatrikelnr, valueNachname, valueVorname, valueDatum;
    private JTextField valueZeichnung1, valueZeichnung2;


    /**
     * Erstellt ein neues HeadPanel
     */
    public HeadPanel() {

        setBackground(Color.WHITE);

        String[] headTitles = {"Matrikelnr.", "Nachname", "Vorname", "Zeichnung 1", "Zeichnung 2", "Datum"};

        valueMatrikelnr = new JLabel(" ");
        valueNachname = new JLabel(" ");
        valueVorname = new JLabel(" ");

        valueZeichnung1 = new JTextField(5);
        valueZeichnung1.setBackground(Layout.EDITABLE_FIELDS);
        valueZeichnung1.setHorizontalAlignment(JTextField.CENTER);
        valueZeichnung1.setBorder(null);
        ((AbstractDocument) valueZeichnung1.getDocument()).setDocumentFilter(new DocumentSizeFilter(5, DocumentSizeFilter.ONLY_TEXT_PATTERN));

        valueZeichnung2 = new JTextField(5);
        valueZeichnung2.setBackground(Layout.EDITABLE_FIELDS);
        valueZeichnung2.setHorizontalAlignment(JTextField.CENTER);
        valueZeichnung2.setBorder(null);
        ((AbstractDocument) valueZeichnung2.getDocument()).setDocumentFilter(new DocumentSizeFilter(5, DocumentSizeFilter.ONLY_TEXT_PATTERN));

        valueDatum = new JLabel(" ");

        JComponent[] headLabels = {valueMatrikelnr, valueNachname, valueVorname, valueZeichnung1, valueZeichnung2, valueDatum};


        for (int i = 0; i < 6; i++) {

            JLabel tempLabel = new JLabel(headTitles[i]);
            tempLabel.setHorizontalAlignment(JLabel.CENTER);

            tempLabel.setFont(tempLabel.getFont().deriveFont(Font.BOLD));

            // Text zentrieren, wenn headLabel ein JLabel ist
            if (headLabels[i] instanceof JLabel)
                ((JLabel) headLabels[i]).setHorizontalAlignment(JLabel.CENTER);


            SForm tempPanel = new SForm();
            tempPanel.setBackground(Color.WHITE);
            tempPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            tempPanel.add(tempLabel, 0, 0, 1, 1, 0.0, 0.0, 2, 11, new Insets(2, 2, 2, 2));
            tempPanel.add(headLabels[i], 0, 1, 1, 1, 0.0, 1.0, 0, 11, new Insets(2, 2, 2, 2));

            add(tempPanel, i * 2, 0, 1, 1, 1.0, 0.0, 2, 11, new Insets(5, 6, 5, 6));

        }

    }


    /**
     * Setzt den Nachnamen ins Panel
     *
     * @param nachname Nachname
     */
    public void setNachname(String nachname) {
        valueNachname.setText(nachname);
    }


    /**
     * Setzt den Vornamen ins Panel
     *
     * @param vorname Vorname
     */
    public void setVorname(String vorname) {
        valueVorname.setText(vorname);
    }


    /**
     * Setzt die Matrikelnummer ins Panel
     *
     * @param matrikelnummer Matrikelnummer
     */
    public void setMatrikelnummer(String matrikelnummer) {
        valueMatrikelnr.setText(matrikelnummer);
    }


    /**
     * Setzt das aktuelle Datum ins Panel
     *
     * @param datum aktuelles Datum
     */
    public void setDatum(String datum) {
        valueDatum.setText(datum);
    }


    /**
     * Setzt das Kürzel für Zeichnung 1 ins Panel
     *
     * @param user Kürzel Zeichnung 1
     */
    public void setZeichnung1(String user) {
        valueZeichnung1.setText(user);
    }


    /**
     * Setzt das Kürzel für Zeichnung 2 ins Panel
     *
     * @param user Kürzel Zeichnung 2
     */
    public void setZeichnung2(String user) {
        valueZeichnung2.setText(user);
    }


}
