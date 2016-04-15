/*
 * Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 *        Semesterticketbüro der Humboldt-Universität Berlin
 *
 * Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
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

package org.semtix.gui.auszahlung.auszahlungsmodul;

import org.semtix.config.UniConf;
import org.semtix.db.dao.Person;
import org.semtix.gui.MainControl;
import org.semtix.shared.actions.ActionCloseDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
* Dialog zur Durchführung der Zuschussberechnung.
* Die Zuschussberechnung ist als Wizard angelegt, d.h. es gibt in dem Dialog 4 Seiten (Panel), 
* durch die vor- und zurückgeblättert werden kann, und in welche Werte zur 
* Zuschussberechnung für ein bestimmtes Semester eingegeben werden können.
* Hier werden auch die Klassen <code><a href="BerechnungsModel.html">BerechnungsModel</a></code> 
* und <code><a href="WizardControl.html">WizardControl</a></code> erstellt. 
* Dem WizardControl werden die View (DialogWizard) und Model (ModelAuszahlungsmodul) als
* Parameter übergeben.
*/
@SuppressWarnings("serial")
public class DialogWizard
extends JDialog {

	private static MainControl mainControl;

	private ModelAuszahlungsmodul bModel;
	
	private WizardControl wizardControl;
	
	private JPanel contentPanel, buttonPanel;
	
	private CardLayout cardLayout;
	
	private JButton closeButton, zurueckButton, weiterButton, fertigButton;


	/**
	 * Erstellt einen Dialog mit Wizard für die Zuschussberechnung.
     * @param mainControl MainControl
     */
	public DialogWizard(MainControl mainControl) {

        DialogWizard.mainControl = mainControl;

		setTitle("Zuschuss-Berechnung");
		
		// Model erstellen
		bModel = new ModelAuszahlungsmodul();
		
		// View erstellen
		wizardControl = new WizardControl(this, bModel);

		buildDialog();
		
		setModal(true);
		setResizable(false);
		
		pack();
		
		// Frame auf dem Bildschirm zentrieren
		setLocationRelativeTo(getParent());
		
		setVisible(true);

	}
	
	
	
	/**
	 * Fügt die Komponenten zum Dialog hinzu.
	 */
	public void buildDialog() {
		
		//setSize(950, 600);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(20, 20));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		JLabel labelTitel = new JLabel("<html><h1>Auszahlung " + UniConf.aktuelleUni.getUniName() + "</h1></html>");

		contentPanel = new JPanel();
		contentPanel.setPreferredSize(new Dimension(680, 350));
		
		cardLayout = new CardLayout();
		contentPanel.setLayout(cardLayout);


		for (GenericPanelStep step : wizardControl.getListSteps()) {
			contentPanel.add(step);
		}

		buttonPanel = new JPanel();
		
		closeButton = new JButton(new ActionCloseDialog(this, "Abbrechen"));
		
		zurueckButton = new JButton("< Zurück");
		zurueckButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				wizardControl.stepDown();
			}
		});
		
		// "Zurück"-Button zu Beginn des Dialogs ausblenden
		zurueckButton.setEnabled(false);
		
		weiterButton = new JButton("Weiter >");
		weiterButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				wizardControl.stepUp();
			}
		});
		
		fertigButton = new JButton("Fertig");
		fertigButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
                dispose();
			}
		});
		
		// "Fertig"-Button zu Beginn des Dialogs ausblenden
		fertigButton.setEnabled(false);
		
		buttonPanel.add(closeButton);
		buttonPanel.add(zurueckButton);
		buttonPanel.add(weiterButton);
		buttonPanel.add(fertigButton);
		
		//SForm formular = new SForm();
		//formular.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		

		mainPanel.add(labelTitel, BorderLayout.NORTH);
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		add(mainPanel);
		
		
		
	}
	
	
	/**
	 * "Weiter"-Button ein- oder ausblenden. Wenn nicht weiter geblättert werden kann (z.B. letzte Seite 
	 * im Wizard erreicht, oder fehlende Angaben im Formular), soll der Weiter-Button deaktiviert sein. 
	 * 
	 * @param status einblenden = true
	 */
	public void setWeiterButton(boolean status) {
		weiterButton.setEnabled(status);
	}
	

	/**
	 * "Zurück"-Button ein- oder ausblenden. Wenn nicht zurück geblättert werden kann (z.B. erste Seite 
	 * im Wizard erreicht), soll der Zurück-Button deaktiviert sein. 
	 * 
	 * @param status einblenden = true
	 */
	public void setZurueckButton(boolean status) {
		zurueckButton.setEnabled(status);
	}
	
	
	/**
	 * "Fertig"-Button ein- oder ausblenden
	 * @param status status
	 */
	public void setFertigButton(boolean status) {
		fertigButton.setEnabled(status);
	}


	/**
	 * Zeigt die gewünschte Seite (Panel) im CardLayout an. Dies geschieht durch Übergabe der 
	 * vorher festgelegten Namen der Panels, die dem CardLayout hinzugefügt wurden.
	 * @param card Name des Panels im CardLayout
	 */
	public void setCards(String card) {
		cardLayout.show(contentPanel, "" + card);
	}

	/**
	 * Nächste Karte
	 */
	public void nextCard() {
		cardLayout.next(contentPanel);
	}

	/**
	 * Eine Karte zurück
	 */
	public void lastCard() {
		cardLayout.previous(contentPanel);
	}

	protected static void showPerson(Person person) {
		mainControl.addTab(person);
	}
}
