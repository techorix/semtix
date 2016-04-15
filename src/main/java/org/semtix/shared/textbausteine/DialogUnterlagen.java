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

package org.semtix.shared.textbausteine;

import org.semtix.db.dao.Unterlagen;
import org.semtix.gui.tabs.antrag.AntragControl;
import org.semtix.gui.tabs.antrag.nachreichungen.tabelle.UnterlagenStatusPanel;
import org.semtix.shared.actions.ActionCloseDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Dialog zum Anzeigen, Erstellen und Ändern von fehlenden Unterlagen
 */
@SuppressWarnings("serial")
public class DialogUnterlagen
extends JDialog {
	

	private List<Unterlagen> unterlagenliste;

	private AntragControl antragControl;
	
	private JPanel buttonPanel;
	
	private JButton abbrechenButton, speichernButton, loeschenButton;

	private JScrollPane scrollPane;

	private JPanel gridbag;

	/**
	 * Erstellt einen neuen Dialog
	 * @param antragControl AntragControl
	 * @param unterlagenliste Liste von Unterlagen
	 */
	public DialogUnterlagen(AntragControl antragControl, List<Unterlagen> unterlagenliste) {

		this.antragControl = antragControl;

		this.unterlagenliste = unterlagenliste;

		setTitle("Nachreichung ändern");

		buildDialog();

	}

	
	
	/**
	 * Fügt Komponenten zum Dialog hinzu
	 */
	public void buildDialog() {

		setModal(true);
		setResizable(false);
		
		setLayout(new BorderLayout());

		gridbag = new JPanel();

			gridbag.setLayout(new GridLayout(unterlagenliste.size(), 1));
			for (Unterlagen unterlagen : unterlagenliste) {
				gridbag.add(new UnterlagenStatusPanel(unterlagen));
			}



		scrollPane = new JScrollPane(gridbag);
		scrollPane.setPreferredSize(new Dimension(420, 180));


		buttonPanel = new JPanel();
		
		speichernButton = new JButton("Speichern");
		speichernButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				unterlagenSpeichern();
			}
		});
		
		loeschenButton = new JButton("Löschen");
		loeschenButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				unterlagenLoeschen();
			}
		});
		
		
		abbrechenButton = new JButton(new ActionCloseDialog(this, "Abbrechen"));
        abbrechenButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "actionEscapeDialog");
        abbrechenButton.getActionMap().put("actionEscapeDialog", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }

        });


		buttonPanel.add(speichernButton);

		buttonPanel.add(loeschenButton);

		buttonPanel.add(abbrechenButton);

		add(scrollPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		this.pack();
		
		// Frame auf dem Bildschirm zentrieren
		setLocationRelativeTo(getParent());
		
		setVisible(true);
		
	}

	
	/**
	 * Button "Speichern" geklickt: Speichert alle Unterlagen im Dialog bezüglich des Antrags
	 */
	private void unterlagenSpeichern() {
		for (int i=0;i<gridbag.getComponentCount();i++) {
			Component component = gridbag.getComponent(i);
			if (component instanceof UnterlagenStatusPanel) {
				Unterlagen unterlagen = ((UnterlagenStatusPanel) component).getUnterlagen();
				if ((unterlagen.getText().trim()).equals("")) {
					JOptionPane.showMessageDialog(null, "Der Text darf nicht leer sein.",
							"Hinweis", JOptionPane.WARNING_MESSAGE);
				} else {
					antragControl.updateUnterlagen(unterlagen);

					dispose();
				}
			}
		}
	}
	
	
	
	/**
	 * Button "Löschen" geklickt
	 */
	private void unterlagenLoeschen() {
		
		// Optionen zur Auswahl festlegen (0, 1, 2 - von rechts nach links ausgerichtet!)
		Object[] options = {"Abbrechen", "Löschen"};
		
		String text = "Soll der Eintrag wirklich gelöscht werden?";
		
		// OptionPane mit Rückgabewert anzeigen
		int selected = JOptionPane.showOptionDialog(null, text, "Hinweis", JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		// Reaktion je nach Auswahl der Option
		if(selected == 1) {

            antragControl.deleteSelectedNachfrage();

            dispose();
        }

    }



}
