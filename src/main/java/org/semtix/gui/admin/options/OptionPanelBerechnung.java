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

package org.semtix.gui.admin.options;

import org.semtix.config.Berechnung;
import org.semtix.config.SemesterConf;
import org.semtix.db.DBHandlerSemester;
import org.semtix.db.dao.Semester;
import org.semtix.shared.elements.FormularTabReihenfolge;
import org.semtix.shared.elements.NewCurrencyField;
import org.semtix.shared.elements.PercentField;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

/**
 * Panel in den AdminTools für Einstellungen für den Berechnungszettel.
 *
 */
@SuppressWarnings("serial")
public class OptionPanelBerechnung
extends JPanel
implements ActionListener {

    private static final Color SAVED = Color.BLACK;
    private static final Color UNSAVED = Color.RED;
    private SForm mainPanel;
	private JLabel lbGrundbedarf, lbKind, lbKindergeld, lbWeiterePerson, lbSchwangerschaft, lbAlleinerziehend,
		lbChronischKrank, lbHeizPauschale, lbKappungMiete, lbAuslandskosten, lbMedKosten, lbSchulden, lbABCTarif;
	private NewCurrencyField tfGrundbedarf, tfKind, tfKindergeld, tfKindergeld2, tfKindergeld3, tfWeiterePerson, tfSchwangerschaft, tfAlleinerziehend,
			tfChronischKrank, tfHeizPauschale, tfKappungMiete, tfAuslandskosten, tfMedKosten, tfABCTarif;
	private BigDecimal oldGrundbedarf, oldKind, oldWeiterePerson, oldSchwangerschaft, oldAlleinerziehend,
            oldChronischKrank, oldHeizPauschale, oldKappungMiete, oldAuslandskosten, oldMedKosten, oldSchulden, oldABCTarif, oldKindergeld, oldKindergeld2, oldKindergeld3;
    private PercentField tfSchulden;

    public OptionPanelBerechnung() {
		
		this.setLayout(new BorderLayout());
		
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel labelTitel = new JLabel("Beträge für " + SemesterConf.getSemester().getSemesterKurzform());
		labelTitel.setToolTipText("Beträge für das aktuelle Semester.");
		labelTitel.setFont(labelTitel.getFont().deriveFont(Font.BOLD));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JButton saveButton = new JButton("Speichern");
		saveButton.addActionListener(this);
		JButton resetButton = new JButton("Zurücksetzen");
		resetButton.addActionListener(this);

        buttonPanel.add(saveButton);
		buttonPanel.add(resetButton);
		
		buildPanel();
		
		JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		optionPanel.add(mainPanel);
		
		add(labelTitel, BorderLayout.NORTH);
		add(optionPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		init();
		
	}
	
	/**
	 * Komponenten zum Panel hinzufügen.
	 */
	public void buildPanel() {

		mainPanel = new SForm();
		
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
				
		lbGrundbedarf = new JLabel("Grundbedarf");
		lbKind = new JLabel("Kind");
        lbKindergeld = new JLabel("Kindergeld/3.Kind/Jedes Weitere");
        lbWeiterePerson = new JLabel("weitere Person");
		lbSchwangerschaft = new JLabel("Schwangerschaft");
		lbAlleinerziehend = new JLabel("Alleinerziehend");
		lbChronischKrank = new JLabel("chronisch krank");
		lbHeizPauschale = new JLabel("Heiz.-Pauschale");
		lbKappungMiete = new JLabel("Kappung (Miete)");
		lbAuslandskosten = new JLabel("Auslandskosten");
		lbMedKosten = new JLabel("Med./Psych. Kosten (6 Monate)");
		lbSchulden = new JLabel("Schulden (in %)");
		lbABCTarif = new JLabel("Außerhalb ABC Tarif");

		tfGrundbedarf = new NewCurrencyField(8);
		tfKind = new NewCurrencyField(8);

		tfKindergeld = new NewCurrencyField(8);
		tfKindergeld2 = new NewCurrencyField(8);
		tfKindergeld3 = new NewCurrencyField(8);
		tfWeiterePerson = new NewCurrencyField(8);
		tfSchwangerschaft = new NewCurrencyField(8);
		tfAlleinerziehend = new NewCurrencyField(8);
		tfChronischKrank = new NewCurrencyField(8);
		tfHeizPauschale = new NewCurrencyField(8);
		tfKappungMiete = new NewCurrencyField(8);
		tfAuslandskosten = new NewCurrencyField(8);
		tfMedKosten = new NewCurrencyField(8);
		tfSchulden = new PercentField(8);
		tfABCTarif = new NewCurrencyField(8);
		
		setFocusListener(tfGrundbedarf);
		setFocusListener(tfKind);
		setFocusListener(tfKindergeld);
        setFocusListener(tfKindergeld2);
        setFocusListener(tfKindergeld3);

        setFocusListener(tfWeiterePerson);
        setFocusListener(tfSchwangerschaft);
		setFocusListener(tfAlleinerziehend);
		setFocusListener(tfChronischKrank);
		setFocusListener(tfHeizPauschale);
		setFocusListener(tfKappungMiete);
		setFocusListener(tfAuslandskosten);
		setFocusListener(tfMedKosten);
		setFocusListener(tfABCTarif);

		// Eigener FocusListener für Schulden weil PercentField
		tfSchulden.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				tfSchulden.setFocus(true);
			}
			public void focusLost(FocusEvent arg0) {
				//tfSchulden.setCurrencyMode();
				tfSchulden.setFocus(false);
				checkValues();
			}
		});
		
		Insets insets = new Insets(2, 5, 2, 15);

		mainPanel.add(lbGrundbedarf,       0,  0, 1, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(lbKind,              0,  1, 1, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(lbWeiterePerson,     0,  2, 1, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(lbSchwangerschaft,   0,  3, 1, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(lbAlleinerziehend,   0,  4, 1, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(lbChronischKrank,    0,  5, 1, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(lbHeizPauschale,     0,  6, 1, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(lbKappungMiete,      0,  7, 1, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(lbAuslandskosten,    0,  8, 1, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(lbMedKosten,         0,  9, 1, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(lbSchulden,          0, 10, 1, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(lbABCTarif,          0, 11, 1, 1, 0.0, 0.0, 0, 17, insets);
		mainPanel.add(lbKindergeld,        0, 12, 1, 1, 0.0, 0.0, 0, 17, insets);


		mainPanel.add(tfGrundbedarf,       1,  0, 1, 1, 0.0, 0.0, 1, 18, insets);
		mainPanel.add(tfKind,              1,  1, 1, 1, 0.0, 0.0, 1, 18, insets);
		mainPanel.add(tfWeiterePerson,     1,  2, 1, 1, 0.0, 0.0, 1, 18, insets);
		mainPanel.add(tfSchwangerschaft,   1,  3, 1, 1, 0.0, 0.0, 1, 18, insets);
		mainPanel.add(tfAlleinerziehend,   1,  4, 1, 1, 0.0, 0.0, 1, 18, insets);
		mainPanel.add(tfChronischKrank,    1,  5, 1, 1, 0.0, 0.0, 1, 18, insets);
		mainPanel.add(tfHeizPauschale,     1,  6, 1, 1, 0.0, 0.0, 1, 18, insets);
		mainPanel.add(tfKappungMiete,      1,  7, 1, 1, 0.0, 0.0, 1, 18, insets);
		mainPanel.add(tfAuslandskosten,    1,  8, 1, 1, 0.0, 0.0, 1, 18, insets);
		mainPanel.add(tfMedKosten,         1,  9, 1, 1, 0.0, 0.0, 1, 18, insets);
		mainPanel.add(tfSchulden,          1, 10, 1, 1, 0.0, 0.0, 1, 18, insets);
		mainPanel.add(tfABCTarif,          1, 11, 1, 1, 0.0, 0.0, 1, 18, insets);
		mainPanel.add(tfKindergeld,        1, 12, 1, 1, 0.0, 0.0, 1, 18, insets);
        mainPanel.add(tfKindergeld2, 2, 12, 1, 1, 0.0, 0.0, 1, 18, insets); //GGF besser untereinander
        mainPanel.add(tfKindergeld3, 3, 12, 1, 1, 0.0, 0.0, 1, 18, insets);


        mainPanel.add(new JLabel(), 1, 30, 1, 1, 1.0, 1.0, 0, 18, insets);

        // ***** Tab-Reihenfolge setzen *****
		JComponent[] order = new JComponent[] {tfGrundbedarf, tfKind, tfWeiterePerson, tfSchwangerschaft,
				tfAlleinerziehend, tfChronischKrank, tfHeizPauschale, tfKappungMiete, tfAuslandskosten,
                tfMedKosten, tfSchulden, tfABCTarif, tfKindergeld, tfKindergeld2, tfKindergeld3};
        setFocusTraversalPolicy(new FormularTabReihenfolge(order));
		setFocusCycleRoot(true);
		
	}
	
	
	/**
	 * Panel initialisieren. In die Eingabefelder werden die Werte aus der Datenbank eingetragen.
	 */
	public void init() {
		tfGrundbedarf.setValue(Berechnung.GRUNDBEDARF);
		tfKind.setValue(Berechnung.KIND);
		tfWeiterePerson.setValue(Berechnung.WEITERE_PERSON);
		tfSchwangerschaft.setValue(Berechnung.SCHWANGERSCHAFT);
		tfAlleinerziehend.setValue(Berechnung.ALLEINERZIEHEND);
		tfChronischKrank.setValue(Berechnung.CHRONISCH_KRANK);
		tfHeizPauschale.setValue(Berechnung.HEIZPAUSCHALE);
		tfKappungMiete.setValue(Berechnung.KAPPUNG_MIETE);
		tfAuslandskosten.setValue(Berechnung.AUSLANDSKOSTEN);
		tfMedKosten.setValue(Berechnung.MED_PSYCH_KOSTEN);
		tfSchulden.setValue(Berechnung.SCHULDEN);
		tfABCTarif.setValue(Berechnung.ABC_TARIF);
		tfKindergeld.setValue(Berechnung.KINDERGELD);
        tfKindergeld2.setValue(Berechnung.KINDERGELD2);
        tfKindergeld3.setValue(Berechnung.KINDERGELD3);

        setOldValues();
		
	}
	
	
	private void setOldValues() {
        oldGrundbedarf = Berechnung.GRUNDBEDARF;
        oldKind = Berechnung.KIND;
		oldWeiterePerson = Berechnung.WEITERE_PERSON;
		oldSchwangerschaft = Berechnung.SCHWANGERSCHAFT;
		oldAlleinerziehend = Berechnung.ALLEINERZIEHEND;
		oldChronischKrank = Berechnung.CHRONISCH_KRANK;
		oldHeizPauschale = Berechnung.HEIZPAUSCHALE;
		oldKappungMiete = Berechnung.KAPPUNG_MIETE;
		oldAuslandskosten = Berechnung.AUSLANDSKOSTEN;
		oldMedKosten = Berechnung.MED_PSYCH_KOSTEN;
		oldSchulden = Berechnung.SCHULDEN;
		oldABCTarif = Berechnung.ABC_TARIF;
		oldKindergeld = Berechnung.KINDERGELD;
        oldKindergeld2 = Berechnung.KINDERGELD2;
        oldKindergeld3 = Berechnung.KINDERGELD3;


    }


    private void checkValues() {

        lbGrundbedarf.setForeground((tfGrundbedarf.getBValue().compareTo(oldGrundbedarf) != 0) ? UNSAVED : SAVED);
		lbKind.setForeground((tfKind.getBValue().compareTo(oldKind) != 0) ? UNSAVED : SAVED);
		lbWeiterePerson.setForeground((tfWeiterePerson.getBValue().compareTo(oldWeiterePerson) != 0) ? UNSAVED : SAVED);
		lbSchwangerschaft.setForeground((tfSchwangerschaft.getBValue().compareTo(oldSchwangerschaft) != 0) ? UNSAVED : SAVED);
		lbAlleinerziehend.setForeground((tfAlleinerziehend.getBValue().compareTo(oldAlleinerziehend) != 0) ? UNSAVED : SAVED);
		lbChronischKrank.setForeground((tfChronischKrank.getBValue().compareTo(oldChronischKrank) != 0) ? UNSAVED : SAVED);
		lbHeizPauschale.setForeground((tfHeizPauschale.getBValue().compareTo(oldHeizPauschale) != 0) ? UNSAVED : SAVED);
		lbKappungMiete.setForeground((tfKappungMiete.getBValue().compareTo(oldKappungMiete) != 0) ? UNSAVED : SAVED);
		lbAuslandskosten.setForeground((tfAuslandskosten.getBValue().compareTo(oldAuslandskosten) != 0) ? UNSAVED : SAVED);
		lbMedKosten.setForeground((tfMedKosten.getBValue().compareTo(oldMedKosten) != 0) ? UNSAVED : SAVED);
		lbSchulden.setForeground((tfSchulden.getBValue().compareTo(oldSchulden) != 0) ? UNSAVED : SAVED);
		lbABCTarif.setForeground((tfABCTarif.getBValue().compareTo(oldABCTarif) != 0) ? UNSAVED : SAVED);
		lbKindergeld.setForeground(((tfKindergeld.getBValue().compareTo(oldKindergeld) != 0) || (tfKindergeld2.getBValue().compareTo(oldKindergeld2) != 0) || (tfKindergeld3.getBValue().compareTo(oldKindergeld3) != 0)) ? UNSAVED : SAVED);

	}
	

	/**
	 * FocusListener den CurrencyFields hinzufügen
	 * @param cField Texteingabefeld
	 */
	private void setFocusListener(final NewCurrencyField cField) {

		cField.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent arg0) {
				checkValues();
			}
		});
		
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		// Button 'Speichern' wurde angeklickt (Werte in DB speichern)
		if (e.getActionCommand().equals("Speichern")) {
			Semester semester = SemesterConf.getSemester();

			semester.setBedarfGrund(tfGrundbedarf.getBValue());
			semester.setBedarfKind(tfKind.getBValue());
			semester.setBedarfWeiterePerson(tfWeiterePerson.getBValue());
			semester.setBedarfSchwangerschaft(tfSchwangerschaft.getBValue());
			semester.setBedarfAlleinerziehend(tfAlleinerziehend.getBValue());
			semester.setChronischKrank(tfChronischKrank.getBValue());
			semester.setHeizkostenpauschale(tfHeizPauschale.getBValue());
			semester.setKappungMiete(tfKappungMiete.getBValue());
			semester.setAuslandskosten(tfAuslandskosten.getBValue());
			semester.setMedKosten(tfMedKosten.getBValue());
			semester.setSchulden(tfSchulden.getBValue());
			semester.setAbcTarif(tfABCTarif.getBValue());
			semester.setKindergeld(tfKindergeld.getBValue());
			semester.setKindergeld2(tfKindergeld2.getBValue());
			semester.setKindergeld3(tfKindergeld3.getBValue());

			DBHandlerSemester dbHandlerSemester = new DBHandlerSemester();
			dbHandlerSemester.updateSemester(semester);
			Berechnung.init();
			setOldValues();
			checkValues();
			
		}
		
		// Button 'Zurücksetzen' wurde geklickt (Werte zurücksetzen)
		if (e.getActionCommand().equals("Zurücksetzen")) {
			init();
			checkValues();			
		}
		
	}

}
