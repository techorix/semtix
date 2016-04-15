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

package org.semtix.gui.tabs.berechnungszettel.otherpanels;

import org.semtix.gui.tabs.berechnungszettel.BerechnungControl;
import org.semtix.gui.tabs.berechnungszettel.BerechnungModel;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

/**
 * Panel mit Seite 1 des Berechnungszettels.
 */
@SuppressWarnings("serial")
public class ZettelPanel1
        extends SForm
        implements Observer {

    private HeadPanel headPanel;
    private HaertePanel haertePanel;
    private MoneyPanel moneyPanel;
    private SchlussPanel schlussPanel;
    private BerechnungControl berechnungControl;

    /**
     * Erstellt ein neues ZettelPanel1
     *
     * @param berechnungsControl BerechnungControl
     */
    public ZettelPanel1(BerechnungControl berechnungsControl) {

        this.berechnungControl = berechnungsControl;

        setPreferredSize(new Dimension(1000, 1416));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));


        headPanel = new HeadPanel();
        haertePanel = new HaertePanel(berechnungsControl);
        moneyPanel = new MoneyPanel(berechnungsControl);


        schlussPanel = new SchlussPanel();


        JLabel titelUeberschrift = new JLabel("Berechnungszettel");
        titelUeberschrift.setFont(titelUeberschrift.getFont().deriveFont(Font.BOLD, 16f));
        titelUeberschrift.setHorizontalAlignment(SwingConstants.CENTER);
        add(titelUeberschrift, 0, 0, 1, 1, 1.0, 1.0, 2, 17, new Insets(4, 2, 0, 0));

        add(headPanel, 0, 1, 1, 1, 1.0, 1.0, 2, 18, new Insets(5, 5, 5, 5));

        add(moneyPanel, 0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.BOTH, 18, new Insets(5, 5, 5, 5));

        add(haertePanel, 0, 4, 1, 1, 1.0, 1.0, 2, 18, new Insets(5, 5, 5, 0));

        add(schlussPanel, 0, 5, 1, 1, 1.0, 1.0, 2, 18, new Insets(5, 5, 5, 5));

    }


    /**
     * Liefert das HeadPanel
     *
     * @return HeadPanel
     */
    public HeadPanel getHeadPanel() {
        return headPanel;
    }

    /**
     * Liefert das HaertePanel
     *
     * @return HaertePanel
     */
    public HaertePanel getHaertePanel() {
        return haertePanel;
    }

    /**
     * Liefert das BedarfPanel
     *
     * @return BedarfPanel
     */
    public MoneyPanel getMoneyPanel() {
        return moneyPanel;
    }


    @Override
    public void update(Observable arg0, Object arg1) {
        berechnungControl.setSaveStatus(true);

        // Formatierung für Geldbeträge
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);

        BerechnungModel berechnungModel = (BerechnungModel) arg0;

        // Punkte und Beträge ins SchlussPanel setzen
        schlussPanel.setBetragBedarfPlusSchulden(nf.format(berechnungModel.getSummeBedarfPlusSchulden()));
        schlussPanel.setBetragEinkommen(nf.format(berechnungModel.getSummeEinkommen()));
        schlussPanel.setBetragGesamt(nf.format(berechnungModel.getDifferenzBetrag()));
        schlussPanel.setPunkteEinkommen("" + berechnungModel.getPunkteEinkommen());
        schlussPanel.setPunkteHaerten("" + berechnungModel.getPunkteHaerte());

        // wenn Differenzbetrag (Bedarf minus Einkommen) kleiner gleich null Antrag abgelehnt,
        // ansonsten Punktzahl anzeigen
        if (berechnungModel.getDifferenzBetrag().compareTo(BigDecimal.ZERO) <= 0) {
            schlussPanel.setPunkteGesamt("abgelehnt!");
        } else {
            schlussPanel.setPunkteGesamt("" + berechnungModel.getGesamtPunkte());
        }

    }


}
