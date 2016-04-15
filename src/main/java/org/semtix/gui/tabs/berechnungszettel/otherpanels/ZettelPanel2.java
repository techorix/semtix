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


import org.semtix.config.SettingsExternal;
import org.semtix.gui.tabs.berechnungszettel.BerechnungControl;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;


/**
 * Panel mit Seite 2 des Berechnungszettels.
 */
@SuppressWarnings("serial")
public class ZettelPanel2
        extends SForm {

    private BerechnungControl berechnungControl;
    private JTextArea bemerkungen;

    private List<EinkommenRechner> rechnerList = new ArrayList<EinkommenRechner>();
    private List<KostenRechner> kostenRechnerStack = new ArrayList<KostenRechner>();

    /**
     * Erstellt ein neues ZettelPanel2
     *
     * @param berechnungsControl BerechnungControl0,00 €
     */
    public ZettelPanel2(BerechnungControl berechnungsControl) {

        this.berechnungControl = berechnungsControl;
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(1000, 1416));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        buildPanel();

    }


    /**
     * Setzt die Komponenten auf das Panel
     */
    public void buildPanel() {
        SForm einkommenPanel = new SForm();
        einkommenPanel.setBackground(Color.WHITE);
        einkommenPanel.setPreferredSize(new Dimension(930, 410));
        einkommenPanel.setMaximumSize(einkommenPanel.getPreferredSize());
        einkommenPanel.setMinimumSize(einkommenPanel.getPreferredSize());


        EinkommenRechner ePanel1 = new EinkommenRechner(0, berechnungControl);
        EinkommenRechner ePanel2 = new EinkommenRechner(1, berechnungControl);
        EinkommenRechner ePanel3 = new EinkommenRechner(2, berechnungControl);
        EinkommenRechner ePanel4 = new EinkommenRechner(3, berechnungControl);

        rechnerList.add(ePanel1);
        rechnerList.add(ePanel2);
        rechnerList.add(ePanel3);
        rechnerList.add(ePanel4);



        JLabel titelUeberschrift = new JLabel("Einkommen");
        titelUeberschrift.setFont(titelUeberschrift.getFont().deriveFont(Font.BOLD, 14f));

        einkommenPanel.add(titelUeberschrift, 0, 0, 1, 1, 0.0, 0.0, 0, 18, new Insets(10, 0, 0, 10));
        einkommenPanel.add(ePanel1, 0, 1, 1, 1, 0.0, 0.0, 0, 18, new Insets(10, 10, 0, 0));
        einkommenPanel.add(ePanel2, 1, 1, 1, 1, 0.0, 0.0, 0, 18, new Insets(10, 10, 0, 0));
        einkommenPanel.add(ePanel3, 2, 1, 1, 1, 0.0, 0.0, 0, 18, new Insets(10, 10, 0, 0));
        einkommenPanel.add(ePanel4, 3, 1, 1, 1, 1.0, 1.0, 0, 18, new Insets(10, 10, 0, 0));


        SForm kostenPanel = new SForm();
        kostenPanel.setBackground(Color.WHITE);
        kostenPanel.setPreferredSize(new Dimension(930, 820));
        kostenPanel.setMinimumSize(kostenPanel.getPreferredSize());
        kostenPanel.setMaximumSize(kostenPanel.getPreferredSize());


        KostenRechner kPanel1 = new KostenRechner(0, berechnungControl);
        KostenRechner kPanel2 = new KostenRechner(1, berechnungControl);
        KostenRechner kPanel3 = new KostenRechner(2, berechnungControl);
        KostenRechner kPanel4 = new KostenRechner(3, berechnungControl);
        KostenRechner kPanel5 = new KostenRechner(4, berechnungControl);
        KostenRechner kPanel6 = new KostenRechner(5, berechnungControl);


        kostenRechnerStack.add(kPanel1);
        kostenRechnerStack.add(kPanel2);
        kostenRechnerStack.add(kPanel3);
        kostenRechnerStack.add(kPanel4);
        kostenRechnerStack.add(kPanel5);
        kostenRechnerStack.add(kPanel6);


        JLabel labelBemerkungen = new JLabel("Bemerkungen:");
        labelBemerkungen.setFont(titelUeberschrift.getFont().deriveFont(Font.BOLD, 14f));

        bemerkungen = new JTextArea();
        bemerkungen.setLineWrap(true);
        bemerkungen.setWrapStyleWord(true);

        Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
        Border emptyBorder = new EmptyBorder(5, 5, 5, 5);
        CompoundBorder compoundBorder = new CompoundBorder(lineBorder, emptyBorder);
        bemerkungen.setBorder(compoundBorder);
        bemerkungen.setPreferredSize(new Dimension(400, 100));
        bemerkungen.setMinimumSize(bemerkungen.getPreferredSize());
        bemerkungen.setMaximumSize(bemerkungen.getPreferredSize());
        bemerkungen.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                berechnungControl.getBerechnungModel().setBemerkung(bemerkungen.getText());
            }
        });


        SForm bemerkungPanel = new SForm();
        bemerkungPanel.setBackground(Color.WHITE);
        bemerkungPanel.add(labelBemerkungen, 0, 0, 1, 1, 0.0, 0.0, 0, 18, new Insets(10, 0, 2, 10));
        bemerkungPanel.add(bemerkungen, 0, 1, 1, 1, 1.0, 1.0, 1, 18, new Insets(2, 10, 10, 10));


        JLabel titelUeberschrift2 = new JLabel("Kosten");
        titelUeberschrift2.setFont(titelUeberschrift.getFont().deriveFont(Font.BOLD, 14f));
        kostenPanel.add(titelUeberschrift2, 0, 0, 1, 1, 0.0, 0.0, 0, 18, new Insets(10, 0, 0, 10));
        kostenPanel.add(kPanel1, 0, 1, 1, 1, 0.0, 0.0, 0, 18, new Insets(10, 10, 10, 10));
        kostenPanel.add(kPanel2, 1, 1, 1, 1, 0.0, 0.0, 0, 18, new Insets(10, 10, 10, 10));
        kostenPanel.add(kPanel3, 0, 2, 1, 1, 0.0, 0.0, 0, 18, new Insets(10, 10, 10, 10));
        kostenPanel.add(kPanel4, 1, 2, 1, 1, 0.0, 0.0, 0, 18, new Insets(10, 10, 10, 10));
        kostenPanel.add(kPanel5, 2, 1, 1, 1, 0.0, 0.0, 0, 18, new Insets(10, 10, 10, 10));
        kostenPanel.add(kPanel6, 2, 2, 1, 1, 1.0, 1.0, 0, 18, new Insets(10, 10, 10, 10));

        this.add(kostenPanel, 0, 0, 1, 1, 0.0, 0.0, 1, 18, new Insets(10, 10, 10, 10));
        this.add(einkommenPanel, 0, 1, 1, 1, 0.0, 0.0, 1, 18, new Insets(10, 10, 10, 10));
        this.add(bemerkungPanel, 0, 2, 1, 1, 0.0, 0.0, 1, 18, new Insets(10, 10, 10, 10));


        // Hintergrundfarbe zum Testen der Panelgrössen und Ausdehnungen
        if (SettingsExternal.CHANGELAYOUTMODE) {
            this.setBackground(Color.GREEN);
            einkommenPanel.setBackground(new Color(150, 255, 150));
            ePanel1.setBackground(new Color(230, 255, 230));
            ePanel2.setBackground(new Color(230, 255, 230));
            ePanel3.setBackground(new Color(230, 255, 230));
            ePanel4.setBackground(new Color(230, 255, 230));
            kostenPanel.setBackground(new Color(255, 150, 150));
            kPanel1.setBackground(new Color(200, 200, 255));
            kPanel2.setBackground(new Color(200, 200, 255));
            kPanel3.setBackground(new Color(200, 200, 255));
            kPanel4.setBackground(new Color(200, 200, 255));
            kPanel5.setBackground(new Color(200, 200, 255));
            kPanel6.setBackground(new Color(200, 200, 255));
            bemerkungPanel.setBackground(new Color(150, 150, 255));
        }

    }

    public void init() {
        bemerkungen.setText(berechnungControl.getBerechnungModel().getBemerkung());

        for (EinkommenRechner ar : rechnerList) {
            ar.initValues();
        }

        for (KostenRechner kr : kostenRechnerStack) {
            kr.init();
        }
    }
}
