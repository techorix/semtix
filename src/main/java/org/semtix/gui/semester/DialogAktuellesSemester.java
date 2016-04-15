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

package org.semtix.gui.semester;

import org.semtix.config.Berechnung;
import org.semtix.config.SemesterConf;
import org.semtix.config.UniConf;
import org.semtix.config.UserConf;
import org.semtix.db.DBHandlerUser;
import org.semtix.db.dao.Semester;
import org.semtix.db.dao.SemtixUser;
import org.semtix.gui.MainControl;
import org.semtix.shared.actions.ActionCloseDialog;
import org.semtix.shared.daten.enums.UserSemesterStatus;
import org.semtix.shared.elements.ComboBoxSemester;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Einstellmöglichkeiten für aktuelles Semester (Arbeitssemester)
 */
@SuppressWarnings("serial")
public class DialogAktuellesSemester
extends JDialog
implements ActionListener {
	
	private MainControl mainControl;
	
	private JLabel labelTitle;

	private ComboBoxSemester comboSemesterLocal;

    private JButton changeButton, closeButton;
	
	
	
	
	
	public DialogAktuellesSemester(MainControl mainControl) {
		
		this.mainControl = mainControl;

		setTitle("Aktuelles Arbeitssemester");

		buildDialog();

	}
	
	
	
	
	public void buildDialog() {
		
		setSize(300, 300);
		
		// Frame auf dem Bildschirm zentrieren
		setLocationRelativeTo(getParent());
		
		setModal(true);
		setResizable(false);
		
		setLayout(new BorderLayout());
		
		labelTitle = new JLabel();
		labelTitle.setFont(labelTitle.getFont().deriveFont(Font.BOLD));
        labelTitle.setForeground(Color.GRAY);
        Border border = BorderFactory.createTitledBorder("Eingestelltes Semester:");
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 10, 5, 10);
        Border compound = BorderFactory.createCompoundBorder(border, emptyBorder);
        labelTitle.setBackground(new Color(190, 255, 190));
        labelTitle.setBorder(compound);
        labelTitle.setToolTipText("<html>Globale Semester werden im Adminpanel gesetzt. <br> " +
                "Lokale Semester kannst du <i>hier</i> setzten. <br>" +
                "Die Einstellungen gelten bis zum Ende der Sitzung</html>");

		

		JPanel buttonPanel = new JPanel();


        buttonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "actionEscapeDialog");
        buttonPanel.getActionMap().put("actionEscapeDialog", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }

        });

		// ComboBox mit Semesterdaten zur aktuellen Universität füllen
		comboSemesterLocal = new ComboBoxSemester(UniConf.aktuelleUni);
        comboSemesterLocal.setPreferredSize(new Dimension(220, 50));
        comboSemesterLocal.setMaximumSize(comboSemesterLocal.getPreferredSize());
        comboSemesterLocal.setBackground(new Color(190, 255, 190));
        comboSemesterLocal.setToolTipText("<html>Hier kannst du ein temporäres (lokales) Arbeitsseemester <br> " +
                "einstellen. <br>" +
                "Das kann z.B. nützlich sein, wenn du einen Antrag <br> " +
                "aus einem vorherigen Semester anlegen willst.</html>");
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(30, 10, 0, 10));

        JLabel bezeichner = new JLabel("Lokales Arbeitssemeester auswählen:");
        bezeichner.setFont(this.getFont().deriveFont(Font.BOLD, 12f));
        bezeichner.setForeground(new Color(0, 155, 0));
        panel.setOpaque(true);
        panel.add(bezeichner);
        panel.add(comboSemesterLocal);
        changeButton = new JButton("Ändern");
		changeButton.addActionListener(this);
		closeButton = new JButton(new ActionCloseDialog(this, "Beenden"));
		
		buttonPanel.add(changeButton);
		buttonPanel.add(closeButton);


		add(labelTitle, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
		
		initPanel();
		
		//this.pack();
		setVisible(true);

	}
	

	
	// Daten ins Panel setzen
	public void initPanel() {
		
		labelTitle.setText("<html>Universität: " + UniConf.aktuelleUni + "<br>" +
                "Semester: <font color='green'>" + SemesterConf.getSemester() + "</font><br>" +
                "Status: <font color='darkgrey'>" + SemesterConf.getSemesterStatus() + "</font></html>");


        List<Semester> semesterListe = comboSemesterLocal.getSemesterListe();
		int semesterID;

        if (null != semesterListe) {
            for (Semester s : semesterListe) {
                Semester semester = SemesterConf.getSemester();
                if (null != semester) {
                    if (s.getSemesterID() == semester.getSemesterID())
                        comboSemesterLocal.setSelectedItem(s);
                }

            }

        }


		if (UniConf.aktuelleUni.name().equals("KW")) {
			semesterID = SemesterConf.semesterLokalKW.getSemesterID();
        } else {
            semesterID = SemesterConf.semesterLokalHU.getSemesterID();
		}

        try {
            for (Semester s : semesterListe)
                if (s.getSemesterID() == semesterID)
                    comboSemesterLocal.setSelectedItem(s);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "NULL-Semester vorhanden. Vermutlich noch kein Semester angelegt?");
        }


    }
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {

		
		// Button 'Aktuelles Semester ändern' wurde geklickt
		if (e.getActionCommand().equals("Ändern")) {
			
			UserSemesterStatus statusHU = SemesterConf.semesterStatusHU;
			UserSemesterStatus statusKW = SemesterConf.semesterStatusKW;
			Semester semesterHU = SemesterConf.semesterLokalHU;
			Semester semesterKW = SemesterConf.semesterLokalKW;


            SemtixUser user = UserConf.CURRENT_USER;

            if (UniConf.aktuelleUni.name().equals("KW")) {
                statusKW = UserSemesterStatus.LOKAL;
                semesterKW = ((Semester) comboSemesterLocal.getSelectedItem());
                user.setSemLokalKW(semesterKW.getSemesterID());
                user.setSemesterStatusKW(statusKW);
            } else {
                statusHU = UserSemesterStatus.LOKAL;
                semesterHU = ((Semester) comboSemesterLocal.getSelectedItem());
                user.setSemLokalHU(semesterHU.getSemesterID());
                user.setSemesterStatusHU(statusHU);
            }



			DBHandlerUser dbHandlerUser = new DBHandlerUser();
			dbHandlerUser.saveOrUpdateUser(user);
			
			// Ausgewählte Werte in die statischen Variablen der MainConf schreiben
			SemesterConf.semesterStatusHU = statusHU;
			SemesterConf.semesterStatusKW = statusKW;
			SemesterConf.semesterLokalHU = semesterHU;
			SemesterConf.semesterLokalKW = semesterKW;


            Berechnung.init();

            // Anzeige im Dialog aktualisieren
			initPanel();
			
			// Statuszeile aktualisieren
			mainControl.updateStatusPanel();
			
		}

	}

}
