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

package org.semtix.gui.admin.options;


import org.semtix.config.Settings;
import org.semtix.config.SettingsExternal;
import org.semtix.shared.elements.FormularTabReihenfolge;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

/**
 * Panel in den AdminTools für Einstellungen für Templates.
 */
@SuppressWarnings("serial")
public class OptionPanelPfade
        extends JPanel
        implements ActionListener {

    private static final Color SAVED = Color.BLACK;
    private static final Color UNSAVED = Color.RED;
    private SForm mainPanel;
    private JLabel lbAntragAbgelehnt, lbAntragBar, lbAntragKonto, lbAzaHu, lbAzaFinref, lbDeckblattHu, lbDeckblattKw, lbNachfrageDE, lbNachfrageEN, lbMahnungDE, lbMahnungEN;
    private JTextField tfAntragAbgelehnt, tfAntragBar, tfAntragKonto, tfAzaHu, tfAzaFinref, tfDeckblattHu, tfDeckblattKw, tfNachfrageDE, tfNachfrageEN, tfMahnungDE, tfMahnungEN;
    private String oldAntragAbgelehnt, oldAntragBar, oldAzaHu, oldAzaFinref, oldDeckblattHu, oldDeckblattKw,
            oldNachfrageDE, oldNachfrageEN, oldMahnungDE, oldMahnungEN, oldAntragKonto;


    public OptionPanelPfade() {

        this.setLayout(new BorderLayout());

        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel labelTitel = new JLabel("Dateinamen der Vorlagen im Vorlagenverzeichnis");
        labelTitel.setFont(labelTitel.getFont().deriveFont(Font.BOLD));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton saveButton = new JButton("Speichern");
        saveButton.addActionListener(this);
        JButton resetButton = new JButton("Zurücksetzen");
        resetButton.addActionListener(this);

        buttonPanel.add(saveButton);
        buttonPanel.add(resetButton);

        buildPanel();

        JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optionPanel.add(mainPanel);

        add(labelTitel, BorderLayout.NORTH);
        add(optionPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        init();

    }

    /**
     * Komponenten zum Panel hinzufügen.
     */
    public void buildPanel() {

        mainPanel = new SForm();

        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        lbAntragAbgelehnt = new JLabel("Bescheid für abgelehnten Antrag");
        lbAntragBar = new JLabel("Bescheid für Barauszahlung");
        lbAntragKonto = new JLabel("Bescheid für Überweisung");
        lbAzaHu = new JLabel("Auszahlungsanordnung HU");
        lbAzaFinref = new JLabel("Auszahlungsanordnung Finanzreferat");
        lbDeckblattHu = new JLabel("Deckblatt für Auszahlungsdatei HU");
        lbDeckblattKw = new JLabel("Deckblatt für Auszahlungsdatei KW");
        lbNachfrageDE = new JLabel("Brief Nachfragen deutsch");
        lbNachfrageEN = new JLabel("Brief Nachfragen englisch");
        lbMahnungDE = new JLabel("Brief Mahnung deutsch");
        lbMahnungEN = new JLabel("Brief Mahnung englisch");

        tfAntragAbgelehnt = new JTextField();
        tfAntragBar = new JTextField();
        tfAntragKonto = new JTextField();
        tfAzaHu = new JTextField();
        tfAzaFinref = new JTextField();
        tfDeckblattHu = new JTextField();
        tfDeckblattKw = new JTextField();
        tfNachfrageDE = new JTextField();
        tfNachfrageEN = new JTextField();
        tfMahnungDE = new JTextField();
        tfMahnungEN = new JTextField();

        setFocusListener(tfAntragAbgelehnt);
        setFocusListener(tfAntragBar);
        setFocusListener(tfAntragKonto);
        setFocusListener(tfAzaHu);
        setFocusListener(tfAzaFinref);
        setFocusListener(tfDeckblattHu);
        setFocusListener(tfNachfrageDE);
        setFocusListener(tfNachfrageEN);
        setFocusListener(tfMahnungDE);
        setFocusListener(tfMahnungEN);


        Insets insets = new Insets(2, 5, 2, 15);

        mainPanel.add(lbAntragAbgelehnt, 0, 0, 1, 1, 0.0, 0.0, 0, 17, insets);
        mainPanel.add(lbAntragBar, 0, 1, 1, 1, 0.0, 0.0, 0, 17, insets);
        mainPanel.add(lbAzaHu, 0, 2, 1, 1, 0.0, 0.0, 0, 17, insets);
        mainPanel.add(lbAzaFinref, 0, 3, 1, 1, 0.0, 0.0, 0, 17, insets);

        mainPanel.add(lbDeckblattHu, 0, 6, 1, 1, 0.0, 0.0, 0, 17, insets);
        mainPanel.add(lbDeckblattKw, 0, 7, 1, 1, 0.0, 0.0, 0, 17, insets);
        mainPanel.add(lbNachfrageDE, 0, 8, 1, 1, 0.0, 0.0, 0, 17, insets);
        mainPanel.add(lbNachfrageEN, 0, 9, 1, 1, 0.0, 0.0, 0, 17, insets);
        mainPanel.add(lbMahnungDE, 0, 10, 1, 1, 0.0, 0.0, 0, 17, insets);
        mainPanel.add(lbMahnungEN, 0, 11, 1, 1, 0.0, 0.0, 0, 17, insets);
        mainPanel.add(lbAntragKonto, 0, 12, 1, 1, 0.0, 0.0, 0, 17, insets);


        mainPanel.add(tfAntragAbgelehnt, 1, 0, 1, 1, 0.0, 0.0, 1, 18, insets);
        mainPanel.add(tfAntragBar, 1, 1, 1, 1, 0.0, 0.0, 1, 18, insets);
        mainPanel.add(tfAzaHu, 1, 2, 1, 1, 0.0, 0.0, 1, 18, insets);
        mainPanel.add(tfAzaFinref, 1, 3, 1, 1, 0.0, 0.0, 1, 18, insets);

        mainPanel.add(tfDeckblattHu, 1, 6, 1, 1, 0.0, 0.0, 1, 18, insets);
        mainPanel.add(tfDeckblattKw, 1, 7, 1, 1, 0.0, 0.0, 1, 18, insets);
        mainPanel.add(tfNachfrageDE, 1, 8, 1, 1, 0.0, 0.0, 1, 18, insets);
        mainPanel.add(tfNachfrageEN, 1, 9, 1, 1, 0.0, 0.0, 1, 18, insets);
        mainPanel.add(tfMahnungDE, 1, 10, 1, 1, 0.0, 0.0, 1, 18, insets);
        mainPanel.add(tfMahnungEN, 1, 11, 1, 1, 0.0, 0.0, 1, 18, insets);
        mainPanel.add(tfAntragKonto, 1, 12, 1, 1, 1.0, 1.0, 1, 18, insets);


        mainPanel.add(new JLabel(), 1, 30, 1, 1, 1.0, 1.0, 0, 18, insets);

        // ***** Tab-Reihenfolge setzen *****
        JComponent[] order = new JComponent[]{tfAntragAbgelehnt, tfAntragBar, tfAzaHu, tfAzaFinref,
                tfDeckblattHu, tfDeckblattKw, tfNachfrageDE, tfNachfrageEN,
                tfMahnungDE, tfMahnungEN, tfAntragKonto};
        setFocusTraversalPolicy(new FormularTabReihenfolge(order));
        setFocusCycleRoot(true);

    }


    /**
     * Panel initialisieren. In die Eingabefelder werden die Werte aus der Datenbank eingetragen.
     */
    public void init() {

        tfAntragAbgelehnt.setText(SettingsExternal.TEMPLATE_ANTRAG_ABGELEHNT);
        tfAntragBar.setText(SettingsExternal.TEMPLATE_ANTRAG_BAR);
        tfAntragKonto.setText(SettingsExternal.TEMPLATE_ANTRAG_KONTO);

        tfAzaHu.setText(SettingsExternal.TEMPLATE_AZA_HU);
        tfAzaFinref.setText(SettingsExternal.TEMPLATE_AZA_FINREF);
        tfDeckblattHu.setText(SettingsExternal.DECKBLATT_DATEI_HU);
        tfDeckblattKw.setText(SettingsExternal.DECKBLATT_DATEI_KW);
        tfNachfrageDE.setText(SettingsExternal.TEMPLATE_NACHFRAGE_DE);
        tfNachfrageEN.setText(SettingsExternal.TEMPLATE_NACHFRAGE_EN);
        tfMahnungDE.setText(SettingsExternal.TEMPLATE_MAHNUNG_DE);
        tfMahnungEN.setText(SettingsExternal.TEMPLATE_MAHNUNG_EN);

        setOldValues();

    }


    private void setOldValues() {

        oldAntragAbgelehnt = SettingsExternal.TEMPLATE_ANTRAG_ABGELEHNT;
        oldAntragBar = SettingsExternal.TEMPLATE_ANTRAG_BAR;
        oldAntragKonto = SettingsExternal.TEMPLATE_ANTRAG_KONTO;
        oldAzaHu = SettingsExternal.TEMPLATE_AZA_HU;
        oldAzaFinref = SettingsExternal.TEMPLATE_AZA_FINREF;
        oldDeckblattHu = SettingsExternal.DECKBLATT_DATEI_HU;
        oldDeckblattKw = SettingsExternal.DECKBLATT_DATEI_KW;
        oldNachfrageDE = SettingsExternal.TEMPLATE_NACHFRAGE_DE;
        oldNachfrageEN = SettingsExternal.TEMPLATE_NACHFRAGE_EN;
        oldMahnungDE = SettingsExternal.TEMPLATE_MAHNUNG_DE;
        oldMahnungEN = SettingsExternal.TEMPLATE_MAHNUNG_EN;

    }


    private void checkValues() {

        lbAntragAbgelehnt.setForeground((tfAntragAbgelehnt.getText().compareTo(oldAntragAbgelehnt) != 0) ? UNSAVED : SAVED);
        lbAntragBar.setForeground((tfAntragBar.getText().compareTo(oldAntragBar) != 0) ? UNSAVED : SAVED);
        lbAzaHu.setForeground((tfAzaHu.getText().compareTo(oldAzaHu) != 0) ? UNSAVED : SAVED);
        lbAzaFinref.setForeground((tfAzaFinref.getText().compareTo(oldAzaFinref) != 0) ? UNSAVED : SAVED);
        lbDeckblattHu.setForeground((tfDeckblattHu.getText().compareTo(oldDeckblattHu) != 0) ? UNSAVED : SAVED);
        lbDeckblattKw.setForeground((tfDeckblattKw.getText().compareTo(oldDeckblattKw) != 0) ? UNSAVED : SAVED);
        lbNachfrageDE.setForeground((tfNachfrageDE.getText().compareTo(oldNachfrageDE) != 0) ? UNSAVED : SAVED);
        lbNachfrageEN.setForeground((tfNachfrageEN.getText().compareTo(oldNachfrageEN) != 0) ? UNSAVED : SAVED);
        lbMahnungDE.setForeground((tfMahnungDE.getText().compareTo(oldMahnungDE) != 0) ? UNSAVED : SAVED);
        lbMahnungEN.setForeground((tfMahnungEN.getText().compareTo(oldMahnungEN) != 0) ? UNSAVED : SAVED);
        lbAntragKonto.setForeground((tfAntragKonto.getText().compareTo(oldAntragKonto) != 0) ? UNSAVED : SAVED);

    }


    /**
     * FocusListener den CurrencyFields hinzufügen
     *
     * @param cField Texteingabefeld
     */
    private void setFocusListener(final JTextField cField) {

        cField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
            }

            public void focusLost(FocusEvent arg0) {
                checkValues();
            }
        });

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        // Button 'Speichern' wurde angeklickt (Werte in DB speichern)
        if (e.getActionCommand().equals("Speichern")) {

            SettingsExternal.TEMPLATE_ANTRAG_ABGELEHNT = tfAntragAbgelehnt.getText();
            SettingsExternal.TEMPLATE_ANTRAG_BAR = tfAntragBar.getText();
            SettingsExternal.TEMPLATE_ANTRAG_KONTO = tfAntragKonto.getText();

            SettingsExternal.TEMPLATE_AZA_HU = tfAzaHu.getText();
            SettingsExternal.TEMPLATE_AZA_FINREF = tfAzaFinref.getText();
            SettingsExternal.DECKBLATT_DATEI_HU = tfDeckblattHu.getText();
            SettingsExternal.DECKBLATT_DATEI_KW = tfDeckblattKw.getText();
            SettingsExternal.TEMPLATE_NACHFRAGE_DE = tfNachfrageDE.getText();
            SettingsExternal.TEMPLATE_NACHFRAGE_EN = tfNachfrageEN.getText();
            SettingsExternal.TEMPLATE_MAHNUNG_DE = tfMahnungDE.getText();
            SettingsExternal.TEMPLATE_MAHNUNG_EN = tfMahnungEN.getText();

            try {
                SettingsExternal.einstellungenSpeichern();
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(null, "Konnte Einstellungen leider nicht Datei speichern. Schreibrechte überprüfen? (" + Settings.DEFAULT_PROPERTIES_GLOBAL + ")" + "\n \n \r \r " + e1.getMessage());
            }

            setOldValues();
            checkValues();

        }

        // Button 'Zurücksetzen' wurde geklickt (Werte zurücksetzen)
        if (e.getActionCommand().equals("Zurücksetzen")) {
            init();
            checkValues();
        }

    }

}
