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

import org.semtix.config.UserConf;
import org.semtix.db.dao.SemtixUser;
import org.semtix.shared.actions.ActionCloseDialog;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Infodialog zeigt Infos zum aktuell angemeldeten User an  (Menüpunkt: Info)
 */
@SuppressWarnings("serial")
public class DialogUserInfo
extends JDialog {

	
	/**
	 * Erstellt einen neuen Dialog
	 */
	public DialogUserInfo() {
		
		setTitle("User-Info");
		
		buildDialog();
		
	}
	
	
	
	/**
	 * Komponenten zum Dialog hinzufügen
	 */
	public void buildDialog() {
		
		setSize(660, 430);
		
		// Frame auf dem Bildschirm zentrieren
		setLocationRelativeTo(getParent());
		
		setModal(true);
		setResizable(false);
		
		setLayout(new BorderLayout());
		
		SemtixUser user = UserConf.CURRENT_USER;
		
		SForm infoPanel = new SForm();
		JPanel buttonPanel = new JPanel();

        JLabel labelUserName = new JLabel("Username");
		JLabel labelKuerzel = new JLabel("Kürzel");
		JLabel labelLoginName = new JLabel("Loginname (Betriebssystem)");
		JLabel labelPrivileg = new JLabel("Privileg");
		JLabel labelStatus = new JLabel("Status");
		JLabel labelBuchstaben = new JLabel("Buchstabenverteilung");
		
		JLabel valueUserName = new JLabel(user.getVorname() + " " + user.getNachname());
		JLabel valueKuerzel = new JLabel(user.getKuerzel());
		JLabel valueLoginName = new JLabel(user.getLoginName());
		JLabel valuePrivileg = new JLabel(user.getPrivileg().toString());
		JLabel valueStatus = new JLabel(user.getStatus().toString());
		JLabel valueBuchstaben = new JLabel(user.getBuchstaben());
		
		JButton abbrechenButton = new JButton(new ActionCloseDialog(this, "Beenden"));

        buttonPanel.add(abbrechenButton);
        buttonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "actionEscapeDialog");
        buttonPanel.getActionMap().put("actionEscapeDialog", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }

        });

		Insets insets = new Insets(5, 7, 1, 2);
		
		infoPanel.add(labelUserName,        0, 0, 1, 1, 0.0, 0.0, 0, 17, insets);
		infoPanel.add(labelKuerzel,         0, 1, 1, 1, 0.0, 0.0, 0, 17, insets);
		infoPanel.add(labelLoginName,       0, 2, 1, 1, 0.0, 0.0, 0, 17, insets);
		infoPanel.add(labelPrivileg,        0, 3, 1, 1, 0.0, 0.0, 0, 17, insets);
		infoPanel.add(labelStatus,          0, 4, 1, 1, 0.0, 0.0, 0, 17, insets);
		infoPanel.add(labelBuchstaben,      0, 5, 1, 1, 0.0, 0.0, 0, 17, insets);
		
		infoPanel.add(valueUserName,        1, 0, 1, 1, 0.0, 0.0, 0, 17, insets);
		infoPanel.add(valueKuerzel,         1, 1, 1, 1, 0.0, 0.0, 0, 17, insets);
		infoPanel.add(valueLoginName,       1, 2, 1, 1, 0.0, 0.0, 0, 17, insets);
		infoPanel.add(valuePrivileg,        1, 3, 1, 1, 0.0, 0.0, 0, 17, insets);
		infoPanel.add(valueStatus,          1, 4, 1, 1, 0.0, 0.0, 0, 17, insets);
		infoPanel.add(valueBuchstaben,      1, 5, 1, 1, 0.0, 0.0, 0, 17, insets);
		
		add(infoPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		this.pack();
		setVisible(true);
		
	}

}
