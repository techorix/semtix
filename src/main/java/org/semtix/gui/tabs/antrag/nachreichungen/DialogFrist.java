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


package org.semtix.gui.tabs.antrag.nachreichungen;

import org.semtix.gui.tabs.antrag.AntragControl;
import org.semtix.shared.actions.ActionCloseDialog;
import org.semtix.shared.daten.enums.Vorgangsart;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//import persontab.antrag.AntragControl;


/**
 * Dialog zur Fristsetzung bei Anforderung fehlender Unterlagen
 * für Nachfragen und Mahnungen.
 */
@SuppressWarnings("serial")
public class DialogFrist
extends JDialog {

    private final SimpleDateFormat dateformatter = new SimpleDateFormat("dd.MM.yyyy");
    private AntragControl antragControl;
	private Vorgangsart vorgangsart;
	private Date fristDatum;
	private String fristText;
	private JLabel labelFrist;
	
	private JSpinner dateSpinner;

	private JRadioButton radio1, radio2;
	
	
	
	public DialogFrist(AntragControl antragControl, Vorgangsart vorgangsart) {

		this.antragControl = antragControl;
		this.vorgangsart = vorgangsart;
		
		setTitle("Frist für " + vorgangsart.getText() + " setzen");
		
		setSize(300, 250);
		
		// Frame auf dem Bildschirm zentrieren
		setLocationRelativeTo(getParent());
		
		setModal(true);
		setResizable(false);
		
		setLayout(new BorderLayout());
		
		
		int fristTageStandard = 0;
		
		if(vorgangsart == Vorgangsart.NACHFRAGEBRIEF) {
			fristTageStandard = 14;
		}
		
		if(vorgangsart == Vorgangsart.MAHNUNG) {
			fristTageStandard = 7;
		}
		
		
		
		Calendar cal = Calendar.getInstance();
	    Date startDatum = cal.getTime();
	    cal.add(Calendar.DATE, fristTageStandard);
	    fristDatum = cal.getTime();

		
		
		// Spinner mit Datumsauswahl für manuelle Fristsetzung
		SpinnerDateModel model = new SpinnerDateModel(fristDatum, startDatum, null, Calendar.DATE);
		
        dateSpinner = new JSpinner(model);
        
        // Setzt den DefaultEditor (Datum nicht im Textfeld änderbar, nur über Pfeile)
        dateSpinner.setEditor(new JSpinner.DefaultEditor(dateSpinner));

		
        String text = "<html>Bitte Frist für Anforderung der fehlenden Unterlagen auswählen. " +
        		fristTageStandard + " Tage Frist (Standard) oder manuelle Auswahl des Datums.</html>";
		
		JPanel buttonPanel = new JPanel();

		
		fristText = dateformatter.format(fristDatum.getTime());
		
		labelFrist = new JLabel(fristText);
		
		radio1 = new JRadioButton();
		radio2 = new JRadioButton();
		
		radio1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				enableLabelfrist(true);
				enableDateSpinner(false);
			}
		});
		
		radio2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableLabelfrist(false);
				enableDateSpinner(true);
			}
		});
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(radio1);
		buttonGroup.add(radio2);
		
		radio1.setSelected(true);
//        radio1.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyReleased(KeyEvent e) {
//                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
//                    dispose();
//            }
//        });
        dateSpinner.setEnabled(false);
		
		SForm formular = new SForm();
		formular.add(new JLabel(text),     0, 0, 2, 1, 0.0, 0.0, 2, 18, new Insets(5, 5, 5, 5));
		formular.add(radio1,               0, 1, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 5, 5, 5));
		formular.add(radio2,               0, 2, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 5, 5, 5));
		formular.add(labelFrist,           1, 1, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 5, 5, 5));
		formular.add(dateSpinner,          1, 2, 1, 1, 1.0, 1.0, 0, 18, new Insets(5, 5, 5, 5));
		
		JButton okButton = new JButton("Drucken");
        okButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "actionEscapeDialog");
        okButton.getActionMap().put("actionEscapeDialog", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }

        });


		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                printUnterlagen(false);
            }
		});
		
		JButton abbrechenButton = new JButton(new ActionCloseDialog(this, "Abbrechen"));

        JButton nochmalDruckenButton = new JButton("Nochmal");
        nochmalDruckenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                printUnterlagen(true);
            }
        });

		buttonPanel.add(okButton);
        buttonPanel.add(nochmalDruckenButton);
        buttonPanel.add(abbrechenButton);


		add(formular, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		setVisible(true);


    }
	

	
	
	
	private Date getFrist() {
		
		if (radio2.isSelected())
			return (Date) dateSpinner.getValue();
		else
			return fristDatum;

	}
	
	
	// Label mit Fristdatum (Standard) aktivieren/deaktivieren
	private void enableLabelfrist(boolean state) {
		labelFrist.setEnabled(state);
	}
	
	
	// Spinner zur manuellen Auswahl von Fristdatum aktivieren/deaktivieren
	private void enableDateSpinner(boolean state) {
		dateSpinner.setEnabled(state);
	}
	
	
	// Frist an AntragControl liefern und Dialog schließen
    private void printUnterlagen(boolean wiederholt) {
        antragControl.printUnterlagen(getFrist(), vorgangsart, wiederholt);
        dispose();
	}
}
