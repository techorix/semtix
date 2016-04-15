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

package org.semtix.gui.admin;

import org.semtix.config.SemesterConf;
import org.semtix.db.dao.Semester;
import org.semtix.shared.daten.enums.Uni;
import org.semtix.shared.elements.ComboBoxSemester;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel in den AdminTools, über welches das aktuelle Semester (Arbeitssemester) geändert werden kann.
 */
@SuppressWarnings("serial")
public class AktuellesSemesterPanel
extends JPanel {
	
	private DialogAdminTools mainDialog;
	
	private SForm mainPanel;
	
	private JLabel lbSemGlobalHU, lbSemGlobalKW;
	
	private ComboBoxSemester comboSemLokalHU, comboSemLokalKW;

	
	/**
	 * Erstellt ein Panel für Änderungen am akteullen Semester.
	 * @param mainDialog Dialog, in dem dieses Panel angezeigt wird
	 */
	public AktuellesSemesterPanel(DialogAdminTools mainDialog) {
		
		this.mainDialog = mainDialog;
		
		this.setLayout(new BorderLayout());
		
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JLabel labelTitel = new JLabel("Aktuelles Semester (global) ändern");
		labelTitel.setFont(labelTitel.getFont().deriveFont(Font.BOLD));
		
		buildPanel();
		
		add(labelTitel, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
		
	}
	
	
	/**
	 * Fügt Komponenten zum Panel hinzu.
	 */
	public void buildPanel() {
		
		mainPanel = new SForm();
		
		JLabel lbTitelHU = new JLabel("Aktuelles Semester (global) für HU");
		lbTitelHU.setFont(lbTitelHU.getFont().deriveFont(Font.BOLD));
		
		JLabel lbTitelKW = new JLabel("Aktuelles Semester (global) für KW");
		lbTitelKW.setFont(lbTitelKW.getFont().deriveFont(Font.BOLD));
		
		JLabel lbText1HU = new JLabel("zur Zeit eingestellt:");
		JLabel lbText1KW = new JLabel("zur Zeit eingestellt:");
		JLabel lbText2HU = new JLabel("ändern auf:");
		JLabel lbText2KW = new JLabel("ändern auf:");
		
		lbSemGlobalHU = new JLabel(SemesterConf.semesterGlobalHU.getSemesterKurzform());
		lbSemGlobalKW = new JLabel(SemesterConf.semesterGlobalKW.getSemesterKurzform());
		
		comboSemLokalHU = new ComboBoxSemester(Uni.HU);
		comboSemLokalKW = new ComboBoxSemester(Uni.KW);
		
		JButton saveButtonHU = new JButton("OK");
		JButton saveButtonKW = new JButton("OK");
		
		saveButtonHU.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				SemesterConf.setGlobalSemester((Semester)comboSemLokalHU.getSelectedItem());
				updateSemesterHU();
				mainDialog.updateStatusPanel();
				
			}
			
		});
		
		saveButtonKW.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				SemesterConf.setGlobalSemester((Semester)comboSemLokalKW.getSelectedItem());
				updateSemesterKW();
				mainDialog.updateStatusPanel();
				
			}
			
		});
		
		Insets insets = new Insets(5, 5, 5, 5);
		
		
		mainPanel.add(lbTitelHU,                                     0, 1, 3, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(lbText1HU,                                     0, 2, 1, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(lbSemGlobalHU,                                 1, 2, 1, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(lbText2HU,                                     0, 3, 1, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(comboSemLokalHU,                               1, 3, 1, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(saveButtonHU,                                  2, 3, 1, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(new JSeparator(JSeparator.HORIZONTAL),         0, 5, 3, 1, 0.0, 0.0, 2, 17, new Insets(25, 5, 25, 5));
		mainPanel.add(lbTitelKW,                                     0, 7, 3, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(lbText1KW,                                     0, 8, 1, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(lbSemGlobalKW,                                 1, 8, 1, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(lbText2KW,                                     0, 9, 1, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(comboSemLokalKW,                               1, 9, 1, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(saveButtonKW,                                  2, 9, 1, 1, 0.0, 0.0, 0, 17, insets);
	
	}
	
	

	/**
	 * Label mit aktuell eingestelltem Semester (global) für HU aktualisieren
	 */
	private void updateSemesterHU() {
		lbSemGlobalHU.setText(SemesterConf.semesterGlobalHU.getSemesterKurzform());
	}
	
	
	/**
	 * Label mit aktuell eingestelltem Semester (global) für KW aktualisieren
	 */
	private void updateSemesterKW() {
		lbSemGlobalKW.setText(SemesterConf.semesterGlobalKW.getSemesterKurzform());
	}


}
