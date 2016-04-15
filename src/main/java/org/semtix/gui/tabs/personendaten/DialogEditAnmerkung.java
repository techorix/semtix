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

package org.semtix.gui.tabs.personendaten;

import org.semtix.config.UserConf;
import org.semtix.db.dao.Anmerkung;
import org.semtix.shared.actions.ActionCloseDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;

/**
 * Dialog zeigt Anmerkungstext zu einer Person an, der geändert werden kann.
 *
 */
@SuppressWarnings("serial")
public class DialogEditAnmerkung
extends JDialog {
	
	private JTextArea textArea;
	
	
	/**
	 * Neuer DiakogEditAnmerkung
	 * @param personControl PersonControl
	 * @param anmerkung zu editierende Anmerkung
	 */
	public DialogEditAnmerkung(final PersonControl personControl, final Anmerkung anmerkung) {
		
		setTitle("Anmerkung ändern");
				
		setSize(450, 250);
				
		setModal(true);
		setResizable(false);
		
		setLayout(new BorderLayout());
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPaneTextArea = new JScrollPane(textArea);
		scrollPaneTextArea.setPreferredSize(new Dimension(400,200));
		
		
		textArea.setText(anmerkung.getText());
		
		JPanel buttonPanel = new JPanel();
		
		JButton saveButton = new JButton("Speichern");
		
		saveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(textArea.getText().trim().isEmpty()) {
					String message = "Bitte Text für Anmerkung eingeben.";
                    JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
                }
				// ansonsten geänderte Anmerkung in Datenbank eintragen
				else {
					anmerkung.setUserId(UserConf.CURRENT_USER.getUserID());
					anmerkung.setZeitstempel(new GregorianCalendar());
					anmerkung.setText(textArea.getText().trim());
					personControl.updateAnmerkung(anmerkung);
					dispose();
				}
			}
		});
		
		
		JButton exitButton = new JButton(new ActionCloseDialog(this, "Schließen"));
		
		buttonPanel.add(saveButton);
		buttonPanel.add(exitButton);

		add(scrollPaneTextArea, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		pack();
		
		// Frame auf dem Bildschirm zentrieren
		setLocationRelativeTo(getParent());
				
		setVisible(true);		

	}

}
