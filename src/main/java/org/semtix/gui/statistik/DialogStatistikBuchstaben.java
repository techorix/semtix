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


package org.semtix.gui.statistik;


import org.semtix.config.UniConf;
import org.semtix.db.dao.Semester;
import org.semtix.shared.actions.ActionCloseDialog;
import org.semtix.shared.elements.ComboBoxSemester;
import org.semtix.shared.elements.SForm;
import org.semtix.shared.tablemodels.TableModelBuchstaben;
import org.semtix.shared.tablemodels.TableModelUser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Dialog zum Menüpunkt "Statistik Buchstabenverteilung" (sowohl für die 
 * Büromitarbeiter_innen als auch für das Semester)
 */
@SuppressWarnings("serial")
public class DialogStatistikBuchstaben
extends JDialog {

	private TableModelBuchstaben tableModelBuchstaben;
	private TableModelUser tableModelUser;
	
	private JScrollPane tableScrollPane1, tableScrollPane2;

	
	/**
	 * Erstellt einen Dialog v
	 */
	public DialogStatistikBuchstaben() {
		
		setTitle("Statistik");
		
		buildDialog();
		
	}
	
	
	
	/**
	 * Fügt Komponenten dem Dialog hinzu
	 */
	public void buildDialog() {
		
		setSize(500, 600);
		
		// Frame auf dem Bildschirm zentrieren
		setLocationRelativeTo(getParent());
		
		setModal(true);
		setResizable(false);
		
		JPanel mainPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		SForm userPanel = new SForm();
		SForm semesterPanel = new SForm();
		JTabbedPane tabbedPane = new JTabbedPane(); 
		
		userPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		semesterPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

		mainPanel.setLayout(new BorderLayout(15, 15));
		
		JLabel titelText = new JLabel("<html><b>Buchstabenverteilung</b><br>Allgemeine Verteilung der Anfangs-Buchstaben " +
				"der Namen auf die Büro-Mitarbeiter_innen (wer welche Buchstaben bearbeitet) und die Verteilung " +
				"auf ein bestimmtes Semester (" + UniConf.aktuelleUni + ")</html>");
		
		JLabel labelSemesterAuswahl = new JLabel("Semester auswählen");

		JButton exitButton = new JButton(new ActionCloseDialog(this, "Abbrechen"));

		buttonPanel.add(exitButton);

        buttonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "actionEscapeDialog");
        buttonPanel.getActionMap().put("actionEscapeDialog", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }

        });

		ComboBoxSemester comboSemester = new ComboBoxSemester(UniConf.aktuelleUni);
		
		comboSemester.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					tableModelBuchstaben.updateBuchstaben(((Semester)e.getItem()).getSemesterID());
					tableScrollPane1.getVerticalScrollBar().setValue(0);
				}
			}
		});
		
		// TableModel für Buchstabenverteilung
		tableModelBuchstaben = new TableModelBuchstaben();


		JTable tabelleBuchstaben = new JTable(tableModelBuchstaben);

		// TableModel für Buchstabenverteilung
		tableModelUser = new TableModelUser(true);
		JTable tabelleUser = new JTable(tableModelUser);

		tabelleUser.removeColumn(tabelleUser.getColumn("Name"));
		tabelleUser.removeColumn(tabelleUser.getColumn("Privileg"));
		tabelleUser.removeColumn(tabelleUser.getColumn("Status"));

		// nur einzelne Tabellenzeilen können selektiert werden
		tabelleBuchstaben.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabelleUser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// die Spalten der Tabelle können nicht vertauscht werden
		tabelleBuchstaben.getTableHeader().setReorderingAllowed(false);
		tabelleUser.getTableHeader().setReorderingAllowed(false);
	
		// die Spaltenbreite kann nicht verändert/verschoben werden
		tabelleBuchstaben.getTableHeader().setResizingAllowed(false);
		tabelleUser.getTableHeader().setResizingAllowed(false);
		
		// Tabelle in Scrollpane setzen
		tableScrollPane1 = new JScrollPane(tabelleBuchstaben);
		tableScrollPane2 = new JScrollPane(tabelleUser);


        semesterPanel.add(labelSemesterAuswahl,   0, 0, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
		semesterPanel.add(comboSemester,          1, 0, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
		semesterPanel.add(tableScrollPane1,       0, 1, 2, 1, 1.0, 1.0, 1, 18, new Insets(0, 0, 0, 0));

		userPanel.add(tableScrollPane2,     0, 0, 1, 1, 1.0, 1.0, 1, 18, new Insets(0, 0, 0, 0));

		
		tabbedPane.addTab("Büro-Mitarbeiter_innen", userPanel);
		tabbedPane.addTab("Semester", semesterPanel);
		

		mainPanel.add(titelText, BorderLayout.NORTH);
		mainPanel.add(tabbedPane, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		add(mainPanel);
		
		setVisible(true);
		
	}


}
