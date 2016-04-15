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

import org.semtix.config.SemesterConf;
import org.semtix.config.UniConf;
import org.semtix.shared.actions.ActionCloseDialog;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Infodialog zeigt an, welches Semester aktuell als Arbeitssemester eingestellt ist (Menüpunkt: Info)
 */
@SuppressWarnings("serial")
public class DialogSemester
extends JDialog {


    private JPanel buttonPanel;
	private SForm infoPanel;
	private JButton abbrechenButton;
	
	private JLabel labelUniversitaet, labelSemester, labelStatusGesetzt,
		labelLocalHU, labelLocalKW,	labelGlobalHU, labelGlobalKW;
	
	private JLabel valueUniversitaet, valueSemester, valueStatusGesetzt,
		valueLocalHU, valueLocalKW,	valueGlobalHU, valueGlobalKW;
	
	
	/**
	 * Erstellt einen neuen Dialog
	 */
	public DialogSemester() {
		
		setTitle( "Info: Aktuelles Semester" );

        buildDialog();
		
	}
	
	
	
	/**
	 * Komponenten zum Dialog hinzufügen
	 */
	public void buildDialog() {
		
		setSize(340, 300);
		
		// Frame auf dem Bildschirm zentrieren
		setLocationRelativeTo(getParent());

        setModal(true);
        setResizable(false);

        setLayout(new BorderLayout());

        infoPanel = new SForm();

        buttonPanel = new JPanel();

        abbrechenButton = new JButton(new ActionCloseDialog(this, "Beenden"));
		
		buttonPanel.add(abbrechenButton);

        buttonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "actionEscapeDialog");
        buttonPanel.getActionMap().put("actionEscapeDialog", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }

        });

		labelUniversitaet = new JLabel("Universität:");
		labelSemester = new JLabel("Semester: ");
		labelStatusGesetzt = new JLabel("Status gesetzt:");
		
		labelLocalHU = new JLabel("Lokal HU:");
		labelLocalKW = new JLabel("Lokal KW:");
		labelGlobalHU = new JLabel("Global HU:");
		labelGlobalKW = new JLabel("Global KW:");
	
		
		valueUniversitaet = new JLabel(UniConf.aktuelleUni.toString());
		valueSemester = new JLabel(SemesterConf.getSemester().getSemesterKurzform());
		valueStatusGesetzt = new JLabel(SemesterConf.getSemesterStatus().toString());
		
		valueLocalHU = new JLabel(SemesterConf.semesterLokalHU.getSemesterKurzform() + " (" + SemesterConf.semesterLokalHU.getSemesterID() + ")");
		valueLocalKW = new JLabel(SemesterConf.semesterLokalKW.getSemesterKurzform() + " (" + SemesterConf.semesterLokalKW.getSemesterID() + ")");
		valueGlobalHU = new JLabel(SemesterConf.semesterGlobalHU.getSemesterKurzform() + " (" + SemesterConf.semesterGlobalHU.getSemesterID() + ")");
		valueGlobalKW = new JLabel(SemesterConf.semesterGlobalKW.getSemesterKurzform() + " (" + SemesterConf.semesterGlobalKW.getSemesterID() + ")");
		
		labelLocalHU.setEnabled(false);
		valueLocalHU.setEnabled(false);
		labelGlobalHU.setEnabled(false);
		valueGlobalHU.setEnabled(false);
		labelLocalKW.setEnabled(false);
		valueLocalKW.setEnabled(false);
		labelGlobalKW.setEnabled(false);
		valueGlobalKW.setEnabled(false);

		
		
		if (SemesterConf.semesterStatusKW.equals("global") &&
				UniConf.aktuelleUni.getID() == 2) {
			labelGlobalKW.setEnabled(true);
			valueGlobalKW.setEnabled(true);
		}
		
		if (SemesterConf.semesterStatusHU.equals("global") &&
				UniConf.aktuelleUni.getID() == 1) {
			labelGlobalHU.setEnabled(true);
			valueGlobalHU.setEnabled(true);
		}
		
		if (SemesterConf.semesterStatusKW.equals("lokal") &&
				UniConf.aktuelleUni.getID() == 2) {
			labelLocalKW.setEnabled(true);
			valueLocalKW.setEnabled(true);
		}
			
		if (SemesterConf.semesterStatusHU.equals("lokal") &&
				UniConf.aktuelleUni.getID() == 1) {
			labelLocalHU.setEnabled(true);
			valueLocalHU.setEnabled(true);
		}

		
		Insets insets = new Insets(5, 7, 1, 2);
		
		infoPanel.add(labelUniversitaet,            0, 0, 1, 1, 0.0, 0.0, 0, 17, insets);
		infoPanel.add(labelSemester,            0, 1, 1, 1, 0.0, 0.0, 0, 17, insets);
		infoPanel.add(labelStatusGesetzt,       0, 2, 1, 1, 0.0, 0.0, 0, 17, insets);
		infoPanel.add(new JSeparator(JSeparator.HORIZONTAL), 0, 3, 2, 1, 0.0, 0.0, 2, 17, new Insets(5, 5, 5, 5));
		infoPanel.add(labelLocalHU,             0, 4, 1, 1, 0.0, 0.0, 0, 17, insets);
		infoPanel.add(labelLocalKW,             0, 5, 1, 1, 0.0, 0.0, 0, 17, insets);
		infoPanel.add(labelGlobalHU,            0, 6, 1, 1, 0.0, 0.0, 0, 17, insets);
		infoPanel.add(labelGlobalKW,            0, 7, 1, 1, 0.0, 0.0, 0, 17, insets);
		
		infoPanel.add(valueUniversitaet,            1, 0, 1, 1, 0.0, 0.0, 0, 17, insets);
		infoPanel.add(valueSemester,            1, 1, 1, 1, 0.0, 0.0, 0, 17, insets);
		infoPanel.add(valueStatusGesetzt,       1, 2, 1, 1, 0.0, 0.0, 0, 17, insets);
		infoPanel.add(valueLocalHU,             1, 4, 1, 1, 0.0, 0.0, 0, 17, insets);
		infoPanel.add(valueLocalKW,             1, 5, 1, 1, 0.0, 0.0, 0, 17, insets);
		infoPanel.add(valueGlobalHU,            1, 6, 1, 1, 0.0, 0.0, 0, 17, insets);
		infoPanel.add(valueGlobalKW,            1, 7, 1, 1, 0.0, 0.0, 0, 17, insets);
		
		add(infoPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		//this.pack();
		setVisible(true);
		
	}

}
