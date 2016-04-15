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

import org.semtix.shared.elements.SForm;

import javax.swing.*;
import java.awt.*;

/**
 * Panel im Berechnungszettel (Seite 1), welches die Summen der Punkte, Einkommen und Bedarf
 * sowie die Gesamtsummen anzeigt
 */
@SuppressWarnings("serial")
public class SchlussPanel
        extends SForm {

    private JLabel lbBetragBedarf, lbBetragEinkommen, lbBetragGesamt,
            lbPunkteEinkommen, lbPunkteHaerten, lbPunkteGesamt;


    /**
     * Erstellt ein neues SchlussPanel
     */
    public SchlussPanel() {

        setBackground(Color.WHITE);

        JLabel lbGesamtpunktzahl = new JLabel("Gesamtpunktzahl");
        lbGesamtpunktzahl.setFont(lbGesamtpunktzahl.getFont().deriveFont(Font.BOLD));


        lbBetragBedarf = new JLabel();
        lbBetragEinkommen = new JLabel();
        lbBetragGesamt = new JLabel();
        lbPunkteEinkommen = new JLabel();
        lbPunkteHaerten = new JLabel();
        lbPunkteGesamt = new JLabel();


        lbBetragBedarf.setPreferredSize(new Dimension(100, 24));
        lbBetragEinkommen.setPreferredSize(new Dimension(100, 24));
        lbBetragGesamt.setPreferredSize(new Dimension(100, 24));
        lbPunkteEinkommen.setPreferredSize(new Dimension(100, 24));
        lbPunkteHaerten.setPreferredSize(new Dimension(100, 24));
        lbPunkteGesamt.setPreferredSize(new Dimension(100, 24));

        lbBetragBedarf.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        lbBetragEinkommen.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        lbBetragGesamt.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        lbPunkteEinkommen.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        lbPunkteHaerten.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        lbPunkteGesamt.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        lbBetragBedarf.setHorizontalAlignment(JLabel.CENTER);
        lbBetragEinkommen.setHorizontalAlignment(JLabel.CENTER);
        lbBetragGesamt.setHorizontalAlignment(JLabel.CENTER);
        lbPunkteEinkommen.setHorizontalAlignment(JLabel.CENTER);
        lbPunkteHaerten.setHorizontalAlignment(JLabel.CENTER);
        lbPunkteGesamt.setHorizontalAlignment(JLabel.CENTER);

        lbPunkteGesamt.setFont(lbPunkteGesamt.getFont().deriveFont(Font.BOLD));


        add(new JLabel("Bedarf+Schulden"), 0, 0, 1, 1, 0.0, 0.0, 0, 18, new Insets(2, 5, 2, 2));
        add(new JLabel("Einkommen"), 1, 0, 1, 1, 1.0, 1.0, 0, 11, new Insets(2, 2, 2, 2));
        add(new JLabel("Bedarf minus Einkommen"), 2, 0, 1, 1, 0.0, 0.0, 0, 12, new Insets(2, 2, 2, 5));
        add(new JLabel("Punkte Einkommen"), 0, 2, 1, 1, 0.0, 0.0, 0, 18, new Insets(2, 5, 2, 2));
        add(new JLabel("Punkte Härten"), 1, 2, 1, 1, 1.0, 1.0, 0, 11, new Insets(2, 2, 2, 2));
        add(lbGesamtpunktzahl, 2, 2, 1, 1, 0.0, 0.0, 0, 12, new Insets(2, 2, 2, 5));

        add(lbBetragBedarf, 0, 1, 1, 1, 0.0, 0.0, 2, 18, new Insets(2, 5, 10, 2));
        add(lbBetragEinkommen, 1, 1, 1, 1, 1.0, 1.0, 2, 11, new Insets(2, 90, 10, 90));
        add(lbBetragGesamt, 2, 1, 1, 1, 0.0, 0.0, 2, 12, new Insets(2, 2, 10, 5));
        add(lbPunkteEinkommen, 0, 3, 1, 1, 0.0, 0.0, 2, 18, new Insets(2, 5, 2, 2));
        add(lbPunkteHaerten, 1, 3, 1, 1, 1.0, 1.0, 2, 11, new Insets(2, 90, 2, 90));
        add(lbPunkteGesamt, 2, 3, 1, 1, 0.0, 0.0, 2, 12, new Insets(2, 2, 2, 5));

    }


    /**
     * Setzt den Betrag für Bedarf ins Panel
     *
     * @param betragBedarf Betrag Bedarf
     */
    public void setBetragBedarfPlusSchulden(String betragBedarf) {
        lbBetragBedarf.setText(betragBedarf);
    }


    /**
     * Setzt den Betrag für Einkommen ins Panel
     *
     * @param betragEinkommen Betrag Einkommen
     */
    public void setBetragEinkommen(String betragEinkommen) {
        lbBetragEinkommen.setText(betragEinkommen);
    }


    /**
     * Setzt den Gesamtbetrag ins Panel (Bedarf minus Einkommen)
     *
     * @param betragGesamt Gesamtbetrag v
     */
    public void setBetragGesamt(String betragGesamt) {
        lbBetragGesamt.setText(betragGesamt);
    }


    /**
     * Setzt die Punkte für Einkommen ins Panel
     *
     * @param punkteEinkommen Punkte Einkommen
     */
    public void setPunkteEinkommen(String punkteEinkommen) {
        lbPunkteEinkommen.setText(punkteEinkommen);
    }


    /**
     * Setzt die Punkte für Härten ins Panel
     *
     * @param punkteHaerten Punkte Härten
     */
    public void setPunkteHaerten(String punkteHaerten) {
        lbPunkteHaerten.setText(punkteHaerten);
    }


    /**
     * setzt die Gesamtpunktzahl ins Panel (Einkommen + Härten)
     *
     * @param punkteGesamt Gesamtpunktzahl (Einkommen + Härten)
     */
    public void setPunkteGesamt(String punkteGesamt) {
        lbPunkteGesamt.setText(punkteGesamt);

        if (punkteGesamt.equals("abgelehnt"))
            lbPunkteGesamt.setForeground(Color.RED);
        else
            lbPunkteGesamt.setForeground(Color.BLACK);
    }


}

