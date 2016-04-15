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

import org.semtix.config.UserConf;
import org.semtix.db.dao.Semester;
import org.semtix.db.dao.SemtixUser;
import org.semtix.shared.actions.ActionCloseDialog;
import org.semtix.shared.daten.enums.Uni;
import org.semtix.shared.daten.enums.UserPrivileg;
import org.semtix.shared.daten.enums.UserSemesterStatus;
import org.semtix.shared.daten.enums.UserStatus;
import org.semtix.shared.elements.ComboBoxSemester;
import org.semtix.shared.elements.SForm;
import org.semtix.shared.elements.control.DocumentSizeFilter;
import org.semtix.shared.elements.control.UniqueUpperCaseFilter;
import org.semtix.shared.tablemodels.TableModelUser;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * Admintools: Dialog zur Änderung oder Neueingabe von Büromitarbeiter_innen. Dieser Dialog 
 * wird von der Übersichtsseite mit den Büro-Mitarbeiter_innen aufgerufen, wenn ein neuer User 
 * angelegt oder ein bestehender User aktualisiert werden soll.
 */
@SuppressWarnings("serial")
public class UserDialog
extends JDialog {
	
	private int userID;
	
	private TableModelUser tableModel;
	
	private JButton speichernButton, abbrechenButton;
	
	private JLabel labelLoginname, labelKuerzel, labelVorname, labelNachname,
		labelPrivileg, labelStatus, labelBuchstaben, labelSemLokalHU, labelSemLokalKW;
	
	private JTextField tfLoginname, tfKuerzel, tfVorname, tfNachname, tfBuchstaben;
	
	private JComboBox comboPrivileg;
	
	private ComboBoxSemester comboSemLokalHU, comboSemLokalKW;

	private JRadioButton aktivRadioButton, passivRadioButton;
	private ButtonGroup statusButtonGroup;
	
	
	/**
	 * Erstellt einen neuen Dialog zum Anlegen oder Ändern von Büro-Mitarbeiter_innen
	 * @param tableModel TableModel mit den Daten der Büro-Mitarbeiter_innen
	 */
	public UserDialog(TableModelUser tableModel) {
		
		this.tableModel = tableModel;
		
		
		setSize(250, 250);
		
		// Frame auf dem Bildschirm zentrieren
		setLocationRelativeTo(getParent());
		
		setModal(true);
		setResizable(false);
		
		setLayout(new BorderLayout());

		JPanel buttonPanel = new JPanel();
		
		// Erstellt Button zum Speichern der Daten
		speichernButton = new JButton("Speichern");
		speichernButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveUser();
			}
		});
		
		// Erstellt Button zum Schließen des Dialogs
		abbrechenButton = new JButton(new ActionCloseDialog(this, "Abbrechen"));

		// Buttons dem ButtonPanel hinzufügen
		buttonPanel.add(speichernButton);
		buttonPanel.add(abbrechenButton);
		
		labelLoginname = new JLabel("Loginname");
		labelKuerzel = new JLabel("Kürzel");
		labelVorname = new JLabel("Vorname");
		labelNachname = new JLabel("Nachname");
		labelPrivileg = new JLabel("Privileg");
		labelStatus = new JLabel("Status");
		labelBuchstaben = new JLabel("Buchstabenverteilung");
		labelSemLokalHU = new JLabel("Semester lokal (HU)");
		labelSemLokalKW = new JLabel("Semester lokal (KW)");
		JLabel labelMaxBuchstaben = new JLabel("(max. 10 Buchstaben)");
		
		
		tfLoginname = new JTextField(20);
		tfKuerzel = new JTextField(10);
		tfVorname = new JTextField(20);
		tfNachname = new JTextField(20);
		tfBuchstaben = new JTextField(10);

		((AbstractDocument) tfLoginname.getDocument()).setDocumentFilter(new DocumentSizeFilter(20, DocumentSizeFilter.TEXT_PATTERN));
		((AbstractDocument) tfKuerzel.getDocument()).setDocumentFilter(new DocumentSizeFilter(5, DocumentSizeFilter.TEXT_PATTERN));
		((AbstractDocument) tfVorname.getDocument()).setDocumentFilter(new DocumentSizeFilter(50, DocumentSizeFilter.TEXT_PATTERN));
		((AbstractDocument) tfNachname.getDocument()).setDocumentFilter(new DocumentSizeFilter(50, DocumentSizeFilter.TEXT_PATTERN));
		((AbstractDocument) tfBuchstaben.getDocument()).setDocumentFilter(new UniqueUpperCaseFilter(10));

		
		comboPrivileg = new JComboBox();		
		
		DefaultComboBoxModel modelUserPrivileg = new DefaultComboBoxModel();
		modelUserPrivileg.addElement(UserPrivileg.USER);
		modelUserPrivileg.addElement(UserPrivileg.FINANZ);
		modelUserPrivileg.addElement(UserPrivileg.ADMIN);
		comboPrivileg.setModel(modelUserPrivileg);
		
		
		aktivRadioButton = new JRadioButton("aktiv");
		passivRadioButton = new JRadioButton("passiv");
		statusButtonGroup = new ButtonGroup();
		statusButtonGroup.add(aktivRadioButton);
		statusButtonGroup.add(passivRadioButton);
		
		
		
		comboSemLokalHU = new ComboBoxSemester(Uni.HU);
		comboSemLokalKW = new ComboBoxSemester(Uni.KW);
		
		
		// Formular erstellen und Komponenten hinzufügen
		SForm formular = new SForm();
		
		// Innenabstände in den Zellen des GridBagLayouts festlegen
		Insets insets = new Insets(5, 7, 1, 2);
		
		formular.add(labelLoginname,       0, 0, 1, 1, 0.0, 0.0, 0, 17, insets);
		formular.add(labelKuerzel,         0, 1, 1, 1, 0.0, 0.0, 0, 17, insets);
		formular.add(labelVorname,         0, 2, 1, 1, 0.0, 0.0, 0, 17, insets);
		formular.add(labelNachname,        0, 3, 1, 1, 0.0, 0.0, 0, 17, insets);
		formular.add(labelPrivileg,        0, 4, 1, 1, 0.0, 0.0, 0, 17, insets);
		formular.add(labelStatus,          0, 5, 1, 1, 0.0, 0.0, 0, 17, insets);
		formular.add(labelBuchstaben,      0, 6, 1, 1, 0.0, 0.0, 0, 17, insets);
		formular.add(labelMaxBuchstaben,   2, 6, 1, 1, 0.0, 0.0, 0, 17, insets);
		formular.add(labelSemLokalHU,      0, 7, 1, 1, 0.0, 0.0, 0, 17, insets);
		formular.add(labelSemLokalKW,      0, 8, 1, 1, 0.0, 0.0, 0, 17, insets);
		
		formular.add(tfLoginname,          1, 0, 2, 1, 0.0, 0.0, 0, 17, insets);
		formular.add(tfKuerzel,            1, 1, 2, 1, 0.0, 0.0, 0, 17, insets);
		formular.add(tfVorname,            1, 2, 2, 1, 0.0, 0.0, 0, 17, insets);
		formular.add(tfNachname,           1, 3, 2, 1, 0.0, 0.0, 0, 17, insets);
		formular.add(comboPrivileg,        1, 4, 2, 1, 0.0, 0.0, 0, 17, insets);
		formular.add(aktivRadioButton,     1, 5, 1, 1, 0.0, 0.0, 0, 17, insets);
		formular.add(passivRadioButton,    2, 5, 1, 1, 0.0, 0.0, 0, 17, insets);
		formular.add(tfBuchstaben,         1, 6, 1, 1, 0.0, 0.0, 0, 17, insets);
		formular.add(comboSemLokalHU,      1, 7, 2, 1, 0.0, 0.0, 0, 17, insets);
		formular.add(comboSemLokalKW,      1, 8, 2, 1, 0.0, 0.0, 0, 17, insets);
		
		// Formular und Buttons dem Hauptpanel hinzufügen
		add(formular, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		this.pack();
		
	}
	

	
	
	

	
	/**
	 * Userdaten aus Formular auslesen und in Objekt der Klasse User schreiben
	 * @return Objekt mit Userdaten
	 */
	private SemtixUser getUser() {
        int semLokalHU = -1;
        int semLokalKW = -1;

        if (null != comboSemLokalHU.getSelectedItem())
            semLokalHU = ((Semester) comboSemLokalHU.getSelectedItem()).getSemesterID();
        if (null != comboSemLokalKW.getSelectedItem())
            semLokalKW = ((Semester) comboSemLokalKW.getSelectedItem()).getSemesterID();



		UserStatus userStatus = aktivRadioButton.isSelected() ? UserStatus.AKTIV : UserStatus.PASSIV;


        SemtixUser user = new SemtixUser(userID, tfLoginname.getText(), tfKuerzel.getText(), tfVorname.getText(),
                tfNachname.getText(), tfBuchstaben.getText().trim(), (UserPrivileg) comboPrivileg.getSelectedItem(),
                userStatus, UserSemesterStatus.GLOBAL, UserSemesterStatus.GLOBAL, semLokalHU, semLokalKW);
		
		return user;
		
	}

	

	/**
	 * Userdaten in Formular übernehmen
	 * @param user Objekt mit Userdaten
	 */
	public void setUser(SemtixUser user) {
		
		userID = user.getUserID();
		
		// User-ID = 0: neuer User
		if(userID == 0) {
			setTitle("Neuen User anlegen");
		}
		// ansonsten: User ändern
		else {
			setTitle("User ändern");
		}
		
		tfLoginname.setText(user.getLoginName());
		tfKuerzel.setText(user.getKuerzel());
		tfVorname.setText(user.getVorname());
		tfNachname.setText(user.getNachname());
		tfBuchstaben.setText(user.getBuchstaben());
		
		
		if(user.getPrivileg()==null)
			comboPrivileg.setSelectedIndex(0);
		else
			comboPrivileg.setSelectedItem(user.getPrivileg());
		
		
		if(user.getStatus() == UserStatus.PASSIV)
			passivRadioButton.setSelected(true);
		else
			aktivRadioButton.setSelected(true);
		
	}
	
	
	/**
	 * Userdaten speichern
	 */
	private void saveUser() {
		
		Boolean checkFlag = true;
		
		// Textfelder trimmen (Leerzeichen vorne und hinten entfernen)
		tfLoginname.setText(tfLoginname.getText().trim());
		tfKuerzel.setText(tfKuerzel.getText().trim());
		tfVorname.setText(tfVorname.getText().trim());
		tfNachname.setText(tfNachname.getText().trim());
					
		// Buchstaben alphabetisch sortieren
		char[] myCharArray = tfBuchstaben.getText().trim().toCharArray();
		Arrays.sort(myCharArray);
		tfBuchstaben.setText(String.valueOf(myCharArray));
		
		// Eingaben im Formular überprüfen
		if(tfLoginname.getText().equals("")) {
			labelLoginname.setForeground(Color.RED);
			checkFlag = false;
		}
		else
			labelLoginname.setForeground(Color.BLACK);
		
		if(tfKuerzel.getText().equals("")) {
			labelKuerzel.setForeground(Color.RED);
			checkFlag = false;
		}
		else
			labelKuerzel.setForeground(Color.BLACK);
		
		if(tfVorname.getText().equals("")) {
			labelVorname.setForeground(Color.RED);
			checkFlag = false;
		}
		else
			labelVorname.setForeground(Color.BLACK);
		
		if(tfNachname.getText().equals("")) {
			labelNachname.setForeground(Color.RED);
			checkFlag = false;
		}
		else
			labelNachname.setForeground(Color.BLACK);
		
		
		
		// wenn keine Fehler mehr, dann Daten in DB schreiben
		if (checkFlag) {
			tableModel.updateUser(getUser());
		
			// Aktuell angemeldeten User updaten
			UserConf.init();
			
			// Sub-Dialog schließen
			dispose();
		
		}		
		
	}


}

