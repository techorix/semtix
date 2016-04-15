/*
 *
 *  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 *         Semesterticketbüro der Humboldt-Universität Berlin
 *
 *  Copyright (c) 2015-2016 Michael Mertins (MichaelMertins@gmail.com)
 *  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)
 *
 *    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.semtix.gui.admin;

import org.semtix.config.UserConf;
import org.semtix.db.DBHandlerUser;
import org.semtix.db.dao.SemtixUser;
import org.semtix.gui.MainControl;
import org.semtix.gui.admin.options.OptionControl;
import org.semtix.shared.actions.ActionCloseDialog;
import org.semtix.shared.daten.enums.UserPrivileg;

import javax.swing.*;
import java.awt.*;

/**
* Dialog mit Einstellungsmöglichkeiten für Admins (AdminTools). 
* <ul>
* <li>Einstellungen und Vorgabewerte für die Formulare Person, Antrag und Berechnungszettel</li>
* <li>Büro-Mitarbeiter_innen</li>
* <li>Semesterdaten</li>
* <li>aktuelles Semester (Arbeitssemester)</li>
** </ul>
*/
@SuppressWarnings("serial")
public class DialogAdminTools
extends JDialog {
	
	private MainControl mainControl;



	/**
	 * Erstellt einen Dialog für die AdminTools und zeigt ihn an.
	 * @param mainControl Maincontrol
	 */
	public DialogAdminTools(MainControl mainControl) {
		boolean found = true;
		if (UserConf.CURRENT_USER.getPrivileg() != UserPrivileg.ADMIN) {
			JOptionPane.showMessageDialog(null, "Der angemeldete User ist kein Admin!");
			DBHandlerUser dbHandlerUser = new DBHandlerUser();
			for (SemtixUser u : dbHandlerUser.getUserListe()) {
				if (u.getPrivileg() == UserPrivileg.ADMIN) {
					found = false;
					JOptionPane.showMessageDialog(null,
							"Account " + u.getLoginName() + " verfügt über Admin-Rechte!");
					break;
				}
			}
			if (found) {
				JOptionPane.showMessageDialog(null, "<html>Es ist noch kein Admin-User angelegt. " +
						"<br>Bitte anlegen.</html>");
			}
		}
		if (found) {

			this.mainControl = mainControl;

			setTitle("AdminTools");

			setSize(800, 600);

			// Frame auf dem Bildschirm zentrieren
			setLocationRelativeTo(getParent());

			setModal(true);
			setResizable(false);

			setLayout(new BorderLayout());

			OptionControl optionControl = new OptionControl();

			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

			// Button zum Schließen des Dialogs
			buttonPanel.add(new JButton(new ActionCloseDialog(this, "Beenden")));

			JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Einstellungen", optionControl.getOptionPanel());
			tabbedPane.addTab("Büro-Mitarbeiter_innen", new UserPanel());
			tabbedPane.addTab("Semesterdaten", new SemesterPanel());
			tabbedPane.addTab("Aktuelles Semester", new AktuellesSemesterPanel(this));

			add(tabbedPane, BorderLayout.CENTER);
			add(buttonPanel, BorderLayout.SOUTH);

			setVisible(true);
		}

	}
	
	
	/**
	 * Das StatusPanel im Hauptframe wird aktualisert (z.B. bei Änderungen am aktuellen Semester)
	 */
	public void updateStatusPanel() {
		mainControl.updateSemester();
	}


}
