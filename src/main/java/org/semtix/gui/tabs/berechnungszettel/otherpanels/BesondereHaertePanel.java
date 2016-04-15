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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Das BesondereHaertePanel ist ein Teil des HaertePanels und wird bei der Auswahl von
 * besonderen Härten neben dem Härtepanel angezeigt (jeweils für besondere Härte 1 und 2)
 */
@SuppressWarnings("serial")
public class BesondereHaertePanel
        extends JPanel {

    private CardLayout cardLayout;

    private SForm besondereHaerte;

    private JLabel labelTitel, labelName;

    private JTextField sign1, sign2, sign3;

    private JCheckBox cb1ja, cb1nein, cb2ja, cb2nein, cb3ja, cb3nein;


    /**
     * Erstellt ein Panel für die Anzeige der besonderen Härten
     *
     * @param titel Titel der besonderen Härten
     */
    public BesondereHaertePanel(String titel) {

        setBackground(Color.WHITE);

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(Color.WHITE);

        besondereHaerte = new SForm();
        besondereHaerte.setBackground(Color.WHITE);

        buildPanel(titel);

        add(emptyPanel, "empty");
        add(besondereHaerte, "besonders");

    }


    /**
     * Komponenten zum Panel hinzufügen
     *
     * @param titel Titel
     */
    public void buildPanel(String titel) {

        labelTitel = new JLabel(titel);
        labelTitel.setFont(labelTitel.getFont().deriveFont(Font.BOLD));
        labelName = new JLabel("");
        JLabel labelJa = new JLabel("ja ");
        JLabel labelNein = new JLabel("/ nein");

        cb1ja = new JCheckBox();
        cb1nein = new JCheckBox();
        cb2ja = new JCheckBox();
        cb2nein = new JCheckBox();
        cb3ja = new JCheckBox();
        cb3nein = new JCheckBox();

        // Checkboxen mit Listenern versehen
        cb1ja.addItemListener(new MyCheckBoxItemListener(cb1nein));
        cb1nein.addItemListener(new MyCheckBoxItemListener(cb1ja));
        cb2ja.addItemListener(new MyCheckBoxItemListener(cb2nein));
        cb2nein.addItemListener(new MyCheckBoxItemListener(cb2ja));
        cb3ja.addItemListener(new MyCheckBoxItemListener(cb3nein));
        cb3nein.addItemListener(new MyCheckBoxItemListener(cb3ja));


        sign1 = new JTextField("");
        sign1.setPreferredSize(new Dimension(80, 24));
        sign1.setHorizontalAlignment(JTextField.CENTER);
        sign1.setMinimumSize(sign1.getPreferredSize());

        sign1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        ((AbstractDocument) sign1.getDocument()).setDocumentFilter(new DocumentSizeFilter(5, DocumentSizeFilter.ONLY_TEXT_PATTERN));
        sign1.setBackground(Layout.EDITABLE_FIELDS);

        sign2 = new JTextField("");
        sign2.setPreferredSize(new Dimension(80, 24));
        sign2.setMinimumSize(sign2.getPreferredSize());

        sign2.setHorizontalAlignment(JTextField.CENTER);
        sign2.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        ((AbstractDocument) sign2.getDocument()).setDocumentFilter(new DocumentSizeFilter(5, DocumentSizeFilter.ONLY_TEXT_PATTERN));
        sign2.setBackground(Layout.EDITABLE_FIELDS);

        sign3 = new JTextField("");
        sign3.setPreferredSize(new Dimension(80, 24));
        sign3.setMinimumSize(sign3.getPreferredSize());
        sign3.setHorizontalAlignment(JTextField.CENTER);
        sign3.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        ((AbstractDocument) sign3.getDocument()).setDocumentFilter(new DocumentSizeFilter(5, DocumentSizeFilter.ONLY_TEXT_PATTERN));
        sign3.setBackground(Layout.EDITABLE_FIELDS);


        Insets insets = new Insets(0, 0, 0, 0);

        besondereHaerte.add(labelTitel, 0, 0, 3, 1, 0.0, 0.0, 0, 17, insets);
        besondereHaerte.add(labelName, 0, 1, 3, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 5, 0));
        besondereHaerte.add(labelJa, 0, 3, 1, 1, 0.0, 0.0, 0, 10, insets);
        besondereHaerte.add(labelNein, 1, 3, 1, 1, 0.0, 0.0, 0, 10, insets);
        besondereHaerte.add(cb1ja, 0, 4, 1, 1, 0.0, 0.0, 0, 10, insets);
        besondereHaerte.add(cb1nein, 1, 4, 1, 1, 0.0, 0.0, 0, 10, insets);
        besondereHaerte.add(sign1, 2, 4, 1, 1, 0.0, 0.0, 0, 17, insets);
        besondereHaerte.add(cb2ja, 0, 5, 1, 1, 0.0, 0.0, 0, 10, insets);
        besondereHaerte.add(cb2nein, 1, 5, 1, 1, 0.0, 0.0, 0, 10, insets);
        besondereHaerte.add(sign2, 2, 5, 1, 1, 0.0, 0.0, 0, 17, insets);
        besondereHaerte.add(cb3ja, 0, 6, 1, 1, 0.0, 0.0, 0, 10, insets);
        besondereHaerte.add(cb3nein, 1, 6, 1, 1, 0.0, 0.0, 0, 10, insets);
        besondereHaerte.add(sign3, 2, 6, 1, 1, 0.0, 0.0, 0, 17, insets);

        besondereHaerte.add(new JLabel(), 0, 70, 3, 1, 1.0, 1.0, 0, 18, insets);


    }


    /**
     * Setzt die Bezeichnung der sonstigen Härte in ein Label
     */
    public void setName(String text) {
        labelName.setText(text);
    }


    /**
     * Anzeige des Panel im CardLayout
     *
     * @param status anzeigen? ja/nein
     */
    public void showPanel(boolean status) {

        String cardName = "empty";

        if (status)
            cardName = "besonders";

        cardLayout.show(this, cardName);
    }


    /**
     * Setzt alle Einträge zurück im Panel zurück
     */
    public void reset() {

        labelName.setText("");
        cb1ja.setSelected(false);
        cb1nein.setSelected(false);
        cb2ja.setSelected(false);
        cb2nein.setSelected(false);
        cb3ja.setSelected(false);
        cb3nein.setSelected(false);
        sign1.setText("");
        sign2.setText("");
        sign3.setText("");

    }


    /**
     * ItemListener für 2 Checkboxen, bei denen nur 1 gleichzeitig angeklickt sein kann.
     */
    class MyCheckBoxItemListener
            implements ItemListener {

        private JCheckBox cb;

        public MyCheckBoxItemListener(JCheckBox cb) {
            this.cb = cb;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                cb.setSelected(false);
            }
        }
    }

}
