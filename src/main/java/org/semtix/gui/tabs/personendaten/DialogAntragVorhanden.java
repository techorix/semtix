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

import org.semtix.db.dao.Person;
import org.semtix.db.dao.Semester;
import org.semtix.gui.tabs.TabControl;
import org.semtix.shared.actions.ActionCloseDialog;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//import persontab.TabControl;

/**
 * Dialog erscheint, wenn "Neuer Antrag" angeklickt wurde,
 * aber für das aktuelle Semester schon ein Antrag in der
 * Datenbank exisitert. Abfrage, ob für ein anderes Semester
 * ein Antrag angelegt oder der aktuelle Antrag
 * angezeigt werden soll.
 */
@SuppressWarnings("serial")
public class DialogAntragVorhanden
extends JDialog {
	
	private int antragID;
	private Person person;
	private TabControl tabControl;
	
	private JRadioButton rbAnlegen, rbAnzeigen;
	
	private JLabel lbAnlegen, lbAnzeigen;
	
	private JComboBox comboSemester;
	
	
	/**
	 * Erstellt einen neuen Dialog
	 * @param antragID AntragID
	 * @param person Person
	 * @param tabControl TabControl
	 */
	public DialogAntragVorhanden(int antragID, Person person, TabControl tabControl) {
		
		this.antragID = antragID;
		this.person = person;
		this.tabControl = tabControl;
		
		setTitle("Antrag bereits vorhanden");
		
		setSize(450, 250);
		
		// Frame auf dem Bildschirm zentrieren
		setLocationRelativeTo(getParent());
		
		setModal(true);
		setResizable(false);
		
		setLayout(new BorderLayout());

		JPanel buttonPanel = new JPanel();
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				leaveDialog();	
			}
		});
		
		
		JButton exitButton = new JButton(new ActionCloseDialog(this, "Beenden"));
		
		buttonPanel.add(okButton);
		buttonPanel.add(exitButton);
		
		JLabel lbText = new JLabel( "<html>Es gibt bereits einen Zuschussantrag für <br>" +
				"das aktuell eingestellte Semester.<br>" +
				"Wie soll weiter verfahren werden?<br><br></html>" );
		lbText.setFont(lbText.getFont().deriveFont(Font.BOLD));
		
		rbAnlegen = new JRadioButton();
		rbAnlegen.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				lbAnzeigen.setEnabled(false);
				lbAnlegen.setEnabled(true);
				comboSemester.setEnabled(true);
			}
		});
		
		
		rbAnzeigen = new JRadioButton();
		rbAnzeigen.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				lbAnzeigen.setEnabled(true);
				lbAnlegen.setEnabled(false);
				comboSemester.setEnabled(false);
			}
		});
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rbAnlegen);
		buttonGroup.add(rbAnzeigen);
		
		// Radiobutton "Anzeigen" zu Beginn auf ausgewählt setzen
		rbAnzeigen.setSelected(true);
		
		lbAnzeigen = new JLabel("Zuschussantrag für aktuelles Semester anzeigen");
		lbAnlegen = new JLabel("einen neuen Antrag anlegen für ");
		
		lbAnlegen.setEnabled(false);

		comboSemester = new JComboBox();
		comboSemester.setModel(new SemesterComboModel(person));
		comboSemester.setEnabled(false);

		SForm formular = new SForm();
		formular.add(lbText,             0, 0, 3, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
		formular.add(rbAnzeigen,         0, 1, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
		formular.add(lbAnzeigen,         1, 1, 2, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
		formular.add(rbAnlegen,          0, 2, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
		formular.add(lbAnlegen,          1, 2, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
		formular.add(comboSemester,      2, 2, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
		
		formular.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		add(formular, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		pack();
		setVisible(true);
		
	}

	
	private void leaveDialog() {
		if(rbAnzeigen.isSelected()) {
			tabControl.getAntragControl().setAntrag(antragID, person);
		}
		if(rbAnlegen.isSelected()) {
			Semester s = (Semester)comboSemester.getSelectedItem();
			//antragOverviewTable.showAntrag(antragID);
		}		
		
		this.dispose();
	}


}
