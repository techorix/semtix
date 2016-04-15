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

package org.semtix.gui.tabs.antrag;

import org.semtix.db.DBHandlerAntraghaerte;
import org.semtix.db.dao.AntragHaerte;
import org.semtix.shared.daten.enums.HaerteAblehnungsgrund;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Dialog zeigt Anmerkungstext zu einer Person an, der geändert werden kann.
 *
 */
@SuppressWarnings("serial")
public class DialogEditBegruendung
extends JDialog {

	private JTextArea textArea;

    private JButton saveButton;

    private AntragHaerte antragHaerte;

    public DialogEditBegruendung(final AntragHaerte antragHaerte) {

        this.antragHaerte = antragHaerte;

		setTitle("Eigenen Begründungstext setzen");
				
		setSize(450, 250);
				
		setModal(true);
		setResizable(false);
		
		setLayout(new BorderLayout());
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPaneTextArea = new JScrollPane(textArea);
		scrollPaneTextArea.setPreferredSize(new Dimension(400,200));
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    dialogSchließen();
                else
                    saveButton.setEnabled(true);

            }
        });

        if (null == antragHaerte.getCustomText() || antragHaerte.getCustomText().length() < 3) {
            if (antragHaerte.isAnerkannt()) {
                textArea.setText(antragHaerte.getHaertegrund().getHaertegrundText());
            } else if (antragHaerte.isAbgelehnt()) {
                textArea.setText(HaerteAblehnungsgrund.getHaertegrundByID(antragHaerte.getAblehnungsID()).getBegruendung());
            }
        } else {
            textArea.setText(antragHaerte.getCustomText());
        }

		JPanel buttonPanel = new JPanel();
		
		saveButton = new JButton("Speichern");
        saveButton.setEnabled(false);
		
		saveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(textArea.getText().trim().isEmpty()) {
                    String message = "Bitte Begründungstext eingeben.";
                    JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
                }
				// ansonsten geänderte Anmerkung in Datenbank eintragen
				else {
                    speichern();

					dispose();
				}
			}
		});


        JButton exitButton = new JButton("Abbrechen");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dialogSchließen();
            }
        });

        buttonPanel.add(saveButton);
		buttonPanel.add(exitButton);

		add(scrollPaneTextArea, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		pack();
		
		// Frame auf dem Bildschirm zentrieren
		setLocationRelativeTo(getParent());
				
		setVisible(true);



    }

    private void dialogSchließen() {
        if (saveButton.isEnabled()) {
            int returnvalue = JOptionPane.showConfirmDialog(null, "Speichern?");
            if (returnvalue == JOptionPane.YES_OPTION)
                speichern();

        }

        dispose();
    }

    private void speichern() {
        antragHaerte.setCustomText(textArea.getText());
        DBHandlerAntraghaerte dbHandlerAntraghaerte = new DBHandlerAntraghaerte();
        dbHandlerAntraghaerte.update(antragHaerte);
        this.saveButton.setEnabled(false);
    }

}
