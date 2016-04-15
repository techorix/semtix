/*
 *
 *  * Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 *  *        Semesterticketbüro der Humboldt-Universität Berlin
 *  *
 *  * Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
 *  * 2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)
 *  *
 *  *     This program is free software: you can redistribute it and/or modify
 *  *     it under the terms of the GNU Affero General Public License as
 *  *     published by the Free Software Foundation, either version 3 of the
 *  *     License, or (at your option) any later version.
 *  *
 *  *     This program is distributed in the hope that it will be useful,
 *  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *     GNU Affero General Public License for more details.
 *  *
 *  *     You should have received a copy of the GNU Affero General Public License
 *  *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.semtix.gui.info;

import org.semtix.shared.actions.ActionCloseDialog;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;

/**
 * Dialog mit Infos und Hilfen zur Bedienung der Anwendung.
 * Vorerst sind dort nur die Tastaturkürzel für die Dialoge "Suche nach Emailadressen" und "Suche nach Personen" eingetragen.
 */
@SuppressWarnings("serial")
public class DialogHilfe
extends JDialog {
	
	
	/**
	 * Erstellt einen Dialog für Infos und Hilfen zum Programm.
	 */
	public DialogHilfe(){

        setTitle("Tastaturkürzel");

        // Icon für Dialog setzen
		URL filename = getClass().getResource("/images/app_logo_16x16.png");
		setIconImage(Toolkit.getDefaultToolkit().getImage(filename));
		
		setModal(true);
		setResizable(false);
		
		// MainPanel mit Abstand zum Dialogrand erstellen
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JPanel buttonPanel = new JPanel();

        buttonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "actionEscapeDialog");
        buttonPanel.getActionMap().put("actionEscapeDialog", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                        dispose();
                }

        });
        // Button zum Schließen des Dialogs
		JButton abbrechenButton = new JButton(new ActionCloseDialog(this, "Beenden"));
		
		buttonPanel.add(abbrechenButton);
		
		JLabel lbTitel = new JLabel("Tastaturkürzel");
		lbTitel.setFont(lbTitel.getFont().deriveFont(Font.BOLD));
		
		JLabel lbF2Text = new JLabel("F2");
		JLabel lbF3Text = new JLabel("F3");
        JLabel lbF4Text = new JLabel("F4");
        JLabel lbSText = new JLabel("Strg+s");
        JLabel lbNNText = new JLabel("Strg+shift+n");
        JLabel lbNText = new JLabel("Strg+n");
        JLabel lbFText = new JLabel("Strg+f");
        JLabel lbWText = new JLabel("Strg+w");
        JLabel lbEscText = new JLabel("Esc");
        JLabel lbPageText = new JLabel("Strg+ ↑ / ↓");

        JLabel lbF2Description = new JLabel("Dialog Personen suchen");
        JLabel lbF3Description = new JLabel("Dialog nach Mailadressen suchen");
        JLabel lbF4Description = new JLabel("Berechnungszettel zum Antrag anzeigen");
        JLabel lbSDescription = new JLabel("Personendaten / Antrag speichern");
        JLabel lbNNDescription = new JLabel("Neue Person als Tab anlegen");
        JLabel lbNDescription = new JLabel("Neuen Antrag anlegen für Person");
        JLabel lbFDescription = new JLabel("Person suchen");
        JLabel lbWDescription = new JLabel("Tab schließen falls gesichert");
        JLabel lbEscDescription = new JLabel("Von Antrag nach Personendaten und zurück");
        JLabel lbPageDescription = new JLabel("Letzte / Nächste Person");

        SForm formular = new SForm();
        formular.add(lbTitel,           0, 0, 2, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 20, 0));
		formular.add(lbF2Text,          0, 1, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 10, 20));
		formular.add(lbF2Description,   1, 1, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 10, 0));
		formular.add(lbF3Text,          0, 2, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 10, 20));
		formular.add(lbF3Description,   1, 2, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 10, 0));
        formular.add(lbF4Text,          0, 3, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 10, 20));
        formular.add(lbF4Description,   1, 3, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 10, 0));
        formular.add(lbSText,           0, 4, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 10, 20));
        formular.add(lbSDescription,    1, 4, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 10, 0));
        formular.add(lbNNText,          0, 5, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 10, 20));
        formular.add(lbNNDescription,   1, 5, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 10, 0));
        formular.add(lbNText,           0, 6, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 10, 20));
        formular.add(lbNDescription,    1, 6, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 10, 0));
        formular.add(lbFText,           0, 7, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 10, 20));
        formular.add(lbFDescription,    1, 7, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 10, 0));
        formular.add(lbWText,           0, 8, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 10, 20));
        formular.add(lbWDescription,    1, 8, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 10, 0));
        formular.add(lbEscText,         0, 9, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 10, 20));
        formular.add(lbEscDescription,  1, 9, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 10, 0));
        formular.add(lbPageText, 0, 10, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 10, 20));
        formular.add(lbPageDescription, 1, 10, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 10, 0));

        // Formular und Buttons zum MainPanel hinzufügen
		mainPanel.add(formular, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		// MainPanel dem Dialog hinzufüügen
		add(mainPanel);
		
		this.pack();
		
		// Frame auf dem Bildschirm zentrieren
		setLocationRelativeTo(getParent());
		
		setVisible(true);
		
	}

}
