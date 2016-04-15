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

package org.semtix.gui.tabs.personendaten;

import org.semtix.db.DBHandlerUser;
import org.semtix.db.dao.Anmerkung;
import org.semtix.db.dao.SemtixUser;
import org.semtix.shared.actions.ActionCloseDialog;
import org.semtix.shared.daten.DeutschesDatum;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog zeigt Anmerkung zu einer Person an.
 *
 */
@SuppressWarnings("serial")
public class DialogShowAnmerkung
extends JDialog {
	
	
	/**
	 * Erstellt neuen DialogShowAnmerkung
	 * @param anmerkung angezeigte Anmerkung
	 */
	public DialogShowAnmerkung(Anmerkung anmerkung) {
		
		setTitle("Anmerkung anzeigen");
		
		setSize(450, 250);
				
		setModal(true);
		setResizable(false);
		
		setLayout(new BorderLayout());
		
		DBHandlerUser dbHandlerUser = new DBHandlerUser();
		SemtixUser user = dbHandlerUser.readUser(anmerkung.getUserId());
		
		JLabel lbTimestamp = new JLabel(DeutschesDatum.getFormatiertenZeitstempel(anmerkung.getZeitstempel()));
		JLabel lbUser = new JLabel(user.getKuerzel());
		JTextArea textArea = new JTextArea(anmerkung.getText());
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		JScrollPane scrollPaneTextArea = new JScrollPane(textArea);
		scrollPaneTextArea.setPreferredSize(new Dimension(400, 150));

		
		SForm formular = new SForm();
		
		formular.add(lbTimestamp,             0,  1, 1, 1, 0.0, 0.0, 2, 18, new Insets(5, 5, 5, 5));
		formular.add(lbUser,                  0,  2, 1, 1, 0.0, 0.0, 2, 18, new Insets(5, 5, 5, 5));
		formular.add(scrollPaneTextArea,      0,  3, 1, 1, 1.0, 1.0, 2, 18, new Insets(5, 5, 5, 5));
		
		JPanel buttonPanel = new JPanel();
		
		JButton exitButton = new JButton(new ActionCloseDialog(this, "Beenden"));
		
		buttonPanel.add(exitButton);

		add(formular, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		pack();
		
		// Frame auf dem Bildschirm zentrieren
		setLocationRelativeTo(getParent());
				
		setVisible(true);	

	}

}
