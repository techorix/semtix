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

package org.semtix.gui.semester;

import org.semtix.config.UniConf;
import org.semtix.db.DBHandlerSemester;
import org.semtix.db.dao.Semester;
import org.semtix.shared.actions.ActionCloseDialog;
import org.semtix.shared.daten.enums.SemesterArt;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
* Dialog zum Anlegen eines neuen Semesters. 
*/
@SuppressWarnings("serial")
public class DialogNeuesSemester
extends JDialog {
	
	private JButton okButton;
	
	private JLabel labelSemesterKurz, labelSemesteranfangAuswahl, labelMessage;
	
	private JComboBox comboSemesterArt, comboSemesterJahr;

	
	/**
	 * Erstellt einen Dialog zum Anlegen eines neuen Semesters.
	 */
	public DialogNeuesSemester() {
		
		setTitle("Neues Semester");
				
		buildDialog();
	
	}



	/**
	 * Komponenten zum Dialog hinzufügen.
	 */
	public void buildDialog() {
	
	setSize(660, 430);
	
	// Frame auf dem Bildschirm zentrieren
	setLocationRelativeTo(getParent());
	
	setModal(true);
	setResizable(false);
	setLayout(new BorderLayout());

	JPanel buttonPanel = new JPanel();
	
	okButton = new JButton("OK");
	okButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			addSemester();
		}
	});
	
	okButton.setEnabled(false);

	buttonPanel.add(okButton);


        buttonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "actionEscapeDialog");
        buttonPanel.getActionMap().put("actionEscapeDialog", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }

        });

	// Button zum Schließen des Dialogs
	buttonPanel.add(new JButton(new ActionCloseDialog(this, "Beenden")));
	
	JLabel labelText = new JLabel("<html><b>Neues Semester für " + UniConf.aktuelleUni + " anlegen.</b></html>");
	JLabel labelSemesterArt = new JLabel("Semester-Art:");
	JLabel labelSemesterJahr = new JLabel("Jahr:");
	JLabel labelSemester = new JLabel("Semester:");
	JLabel labelSemesteranfang = new JLabel("Semesteranfang:");
	labelSemesterKurz = new JLabel();
	labelSemesteranfangAuswahl = new JLabel();
	labelMessage = new JLabel(" ");
	labelMessage.setForeground(Color.RED);

	// Model für Semesterart anlegen und füllen
	DefaultComboBoxModel model = new DefaultComboBoxModel();
	model.addElement("-- Semester-Art wählen --");
	model.addElement(SemesterArt.SOMMER);
	model.addElement(SemesterArt.WINTER);
	
	// Model der ComboBox zuweisen
	comboSemesterArt = new JComboBox(model);

	// ActionListener für ComboBox SemesterArt hinzufügen
	comboSemesterArt.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			// bei Auswahl in ComboBox Daten in View aktualisieren
			updateSemester();
		}
	});

	
	String[] semesterJahr = new String[9];
	semesterJahr[0] = "-- Jahr auswählen --";
	
	Calendar cal = new GregorianCalendar();
	
	// Jahreszahlen für ComboBox (Semester-Jahr) festlegen
	for(int i = 1; i <= 8; i++){
		semesterJahr[i] = "" + (cal.get(Calendar.YEAR) + i - 4);
	}
	
	comboSemesterJahr = new JComboBox(semesterJahr);
	comboSemesterJahr.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			updateSemester();
		}
	});

	
	
	SForm formular = new SForm();
	
	// Rahmen setzen (Abstand zum Dialogrand)
	formular.setBorder(new EmptyBorder(10, 10, 10, 10));
	
	// Label hinzufügen
	formular.add(labelText,                   0, 0, 2, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
	formular.add(labelSemesterArt,            0, 1, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
	formular.add(comboSemesterArt,            1, 1, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
	formular.add(labelSemesterJahr,           0, 2, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
	formular.add(comboSemesterJahr,           1, 2, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
	formular.add(labelSemester,               0, 3, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
	formular.add(labelSemesterKurz,           1, 3, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
	formular.add(labelSemesteranfang,         0, 4, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
	formular.add(labelSemesteranfangAuswahl,  1, 4, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
	formular.add(labelMessage,                1, 5, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
	
	

	
	add(formular, BorderLayout.CENTER);
	add(buttonPanel, BorderLayout.SOUTH);
	
	this.pack();
	setVisible(true);
	
	}
	
	
	
	// Anzeige nach Auswahl der Comboboxen ändern
	private void updateSemester() {
		
		// eine der beiden Comboboxen (Semesterart oder -jahr) ist auf Index 0
		if(comboSemesterArt.getSelectedIndex()==0 || comboSemesterJahr.getSelectedIndex()==0){
			
			// Text für Semesterkurzform leeren
			labelSemesterKurz.setText("");
			
			// Text für Datum Semesteranfang leeren
			labelSemesteranfangAuswahl.setText("");
			
			// Warnhinweis aus;
			labelMessage.setText(" ");
			
			// Button zum Anlegen von neuem Semester deaktivieren
			okButton.setEnabled(false);
		}
		
		// Semesterart und -jahr wurden ausgewählt
		else {
			
			boolean setButton = true;
			
			DBHandlerSemester dbHandlerSemester = new DBHandlerSemester();
			
			SemesterArt semesterArt = (SemesterArt) comboSemesterArt.getSelectedItem();
			String semesterJahr = (String)comboSemesterJahr.getSelectedItem();


            //in DB überprüfen ob Semester schon vorhanden
            for (Semester s : dbHandlerSemester.getSemesterListe()) {
                if (s.getUni().equals(UniConf.aktuelleUni) && s.getSemesterJahr().equalsIgnoreCase(semesterJahr + semesterArt.getBuchstabe())) {
                    // Warnhinweis an
                    labelMessage.setText("Semester schon in DB vorhanden!");

                    // Button zum Anlegen von neuem Semester deaktivieren
                    setButton = false;
                    break;
                }
            }




			
			// Text für Sommersemester setzen
			if(comboSemesterArt.getSelectedItem() == SemesterArt.SOMMER){
				
				// Text für Semesterkurzform setzen
				labelSemesterKurz.setText(SemesterArt.SOMMER.getKurzform() + comboSemesterJahr.getSelectedItem());
				
				// Text für Datum Semesteranfang setzen
				labelSemesteranfangAuswahl.setText("01.04." + comboSemesterJahr.getSelectedItem());

			}
			
			// Text für Wintersemester setzen
			if(comboSemesterArt.getSelectedItem() == SemesterArt.WINTER){
				
				String s = ((String) comboSemesterJahr.getSelectedItem()).substring(2);
				int jahrPlusEins = Integer.parseInt(s)+1;
				
				// Text für Semesterkurzform setzen
				labelSemesterKurz.setText(SemesterArt.WINTER.getKurzform() + comboSemesterJahr.getSelectedItem() + "/" + jahrPlusEins);
				
				// Text für Datum Semesteranfang setzen
				labelSemesteranfangAuswahl.setText("01.10." + comboSemesterJahr.getSelectedItem());

			}

			// Button zum Anlegen von neuem Semester deaktivieren
			okButton.setEnabled(setButton);
		}
		
	}


	// Semester zur DB hinzufügen (OK-Button geklickt)
	private void addSemester() {
		
		DBHandlerSemester dbHandlerSemester = new DBHandlerSemester();
		
		SemesterArt semesterArt = (SemesterArt) comboSemesterArt.getSelectedItem();
		String semesterJahr = (String)comboSemesterJahr.getSelectedItem();

        boolean alreadyExists = false;
        for (Semester s : dbHandlerSemester.getSemesterListe(UniConf.aktuelleUni)) {
            if (s.getSemesterJahr().equals(semesterJahr) && s.getSemesterArt().equals(semesterArt))
                alreadyExists = true;
        }

        if (alreadyExists) {

            labelMessage.setText("Semester schon in DB vorhanden.");

        } else {

            int month = (semesterArt == SemesterArt.SOMMER) ? Calendar.APRIL : Calendar.OCTOBER;
            int year = Integer.parseInt(semesterJahr);

            GregorianCalendar semesterAnfang = new GregorianCalendar(year, month, 1);

            Semester semester = new Semester();
            semester.setSemesterJahr(semesterJahr);
            semester.setSemesterArt(semesterArt);
            semester.setSemesterAnfang(semesterAnfang);


            dbHandlerSemester.createSemester(semester);

            labelMessage.setText("Semester in DB eingetragen.");

        }

        // Button zum Anlegen von neuem Semester deaktivieren
        okButton.setEnabled(false);
    }
	
}
