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

package org.semtix.gui.tabs.personendaten;

import org.semtix.config.Settings;
import org.semtix.config.UniConf;
import org.semtix.db.DBHandlerConf;
import org.semtix.db.dao.Anmerkung;
import org.semtix.db.dao.Person;
import org.semtix.gui.tabs.antrag.nachreichungen.pruefen.NachreichungControl;
import org.semtix.shared.daten.StringHelper;
import org.semtix.shared.daten.enums.Uni;
import org.semtix.shared.elements.*;
import org.semtix.shared.elements.control.*;
import org.semtix.shared.tablemodels.TableModelAnmerkungen;
import org.semtix.shared.tablemodels.TableModelAntragUebersicht;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Klasse enthält ds Personenformular (View)
 */
public class PersonView extends KeyAdapter
		implements Observer {

	public JLabel lbIBAN;
	private PersonControl personControl;
    private SForm personPanel;
    private JScrollPane mainScrollPane;
	private JLabel lbTitelName, lbTitelMatrikelnr, lbTitelUni;
	private LabelKulanz lbKulanz;
	private LabelRatenzahlung lbRatenzahlung;
	private JLabel lbVorname, lbNachname, lbMatrikelnummer, lbGeburtsdatum,
		lbStrasse, lbPLZ, lbWohnort, lbLand, lbCO, lbWohneinheit, lbEmail;
	private JLabel lbKontoName, lbKontoStrasse, lbKontoWohnort, lbBIC;
	private JTextField tfVorname, tfNachname, tfMatrikelnummer, tfGeburtsdatum, tfStrasse,
            tfPLZ, tfWohnort, tfLand, tfCO, tfWohneinheit, tfEmail,
            tfKontoName, tfKontoStrasse, tfKontoWohnort, tfBIC, tfAnmerkung;

    private JFormattedTextField ftfIBAN;
	private JLabel labelDatumAngelegt, labelDatumLastChange, labelLoeschstatus;
    private JLabel labelPersonID;
    private JCheckBox cbTelefon, cbBarauszahlung;
	private ImageIcon iconBritishFlag, iconBritishFlagDisabled;
	private JToggleButton toggleButtonFlag;
	private JTable antragTable, anmerkungTable;
	private JButton speichernButton, resetButton, aktuellerAntragButton, nachreichungButton,
			bestimmterAntragButton, neuerAntragButton, mailButton, protokollButton, delButton, changeUniButton;
	private HashMap<JLabel, JTextField> hashMapCheckFields;
    private SForm infoPanel;

    private boolean emptyBICallowed, khbKeineBarauszahler;


    /**
     * Erstellt neue PersonView
     * @param personControl PersonControl
	 */
	@SuppressWarnings("serial")
	public PersonView(PersonControl personControl){


		checkConfigurationOptionsFromDB();

        this.personControl = personControl;
		

		// Label für Überschriften
//		JLabel lbTitle1 = new JLabel("Angaben zur Person");
		JLabel lbZahlungsdaten = new JLabel("Auszahlungsweise");
		JLabel lbAntragsuebersicht = new JLabel("Antragsübersicht");
		JLabel lbAnmerkung = new JLabel("Anmerkungen");
		
		// Schrift in Label auf fett setzen
//		lbTitle1.setFont(lbTitle1.getFont().deriveFont(Font.BOLD));
		lbZahlungsdaten.setFont(lbZahlungsdaten.getFont().deriveFont(Font.BOLD));
		lbAntragsuebersicht.setFont(lbAntragsuebersicht.getFont().deriveFont(Font.BOLD));
		lbAnmerkung.setFont(lbAnmerkung.getFont().deriveFont(Font.BOLD));
		
		lbVorname = new JLabel("Vorname");
		lbNachname = new JLabel("Nachname");
		lbMatrikelnummer = new JLabel("Matrikelnummer");
		lbGeburtsdatum = new JLabel("Geburtsdatum");
		lbStrasse = new JLabel("Straße und Hausnummer");
		lbPLZ = new JLabel("Postleitzahl");
		lbWohnort = new JLabel("Wohnort");
		lbLand = new JLabel("Land");
		lbCO = new JLabel("c/o");
		lbWohneinheit = new JLabel("Adresszusatz");
		lbEmail = new JLabel("E-Mail");
		
		
		tfNachname = new JTextField(30);
		tfVorname = new JTextField(30);
		tfGeburtsdatum = new JTextField(10);
		tfMatrikelnummer = new JTextField(10);
		tfCO = new JTextField(30);
		tfWohneinheit = new JTextField(30);
		tfStrasse = new JTextField(30);
		tfPLZ = new JTextField(10);
		tfWohnort = new JTextField(15);
		tfLand = new JTextField(20);
		tfEmail = new JTextField(30);
		tfAnmerkung = new JTextField(50);
		
		
		// Toggle-Button für Auswahl "englischsprachig"
		iconBritishFlag = new ImageIcon(getClass().getResource("/images/british_flag.gif"));
		iconBritishFlagDisabled = new ImageIcon(getClass().getResource("/images/british_flag_disabled.gif"));
		toggleButtonFlag = new JToggleButton(iconBritishFlagDisabled);
		toggleButtonFlag.setSelectedIcon(iconBritishFlag);
		toggleButtonFlag.setPreferredSize(new Dimension(40, 28));
		toggleButtonFlag.setToolTipText("englischsprachig");


        cbTelefon = new JCheckBox("Telefon vorhanden");
        cbBarauszahlung = new JCheckBox("Barauszahlung");

		
		// ***** Tabelle mit den Antragsübersichten *****
		antragTable = new JTable() {
			
			// Renderer färbt aktuellen Antrag in Übersichtstabelle
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				
			    Component c = super.prepareRenderer(renderer, row, column);

			    if(getSelectedRow() == row) {
			    	
			    	if (row == ((TableModelAntragUebersicht) getModel()).getRowAktuellerAntrag()) {
						((DefaultTableCellRenderer) renderer).setForeground(new Color(255, 200, 0));
					}
				    else {
				    	((DefaultTableCellRenderer)renderer).setForeground(Color.WHITE);
				    }
			    	
			    	return c;
			    	
			    }
			    else {
			    	
			    	((DefaultTableCellRenderer)renderer).setForeground(Color.BLACK);
			    	
			    	if (row == ((TableModelAntragUebersicht) getModel()).getRowAktuellerAntrag()) {
						((DefaultTableCellRenderer) renderer).setBackground(new Color(255, 255, 0));
					}
				    else {
				    	((DefaultTableCellRenderer)renderer).setBackground(null);
				    }
			    	
			    	return c;
			    	
			    }
			}
			    
		};
		
		JScrollPane scrollPaneAntraege = new JScrollPane(antragTable);
		// Dimension integer i eigentlich unnötig
		scrollPaneAntraege.setPreferredSize(new Dimension(220, 385));


		// Tabelle mit Anmerkungen
		anmerkungTable = new JTable();

		JScrollPane scrollPaneAnmerkungen = new JScrollPane(anmerkungTable);
		// Dimension integer i eigentlich unnötig
		scrollPaneAnmerkungen.setPreferredSize(new Dimension(220, 352));
		
		
		// ***** TitelPanel erstellen *****
		// Panel für Titelüberschrift, Name, Matrikelnummer und Universität

		lbTitelName = new LabelPerson("Name", SwingConstants.LEFT);
		lbTitelMatrikelnr = new LabelPerson("Matrikel", SwingConstants.RIGHT);
		lbTitelUni = new LabelPerson("Universität", SwingConstants.RIGHT);

		lbKulanz = new LabelKulanz();
		lbRatenzahlung = new LabelRatenzahlung();

		SForm headerPanel = new SForm();
		headerPanel.add(lbTitelName, 0, 0, 1, 1, 0.0, 0.0, 0, GridBagConstraints.WEST, new Insets(0, 5, 5, 0));
		headerPanel.add(new JPanel(), 1, 0, 1, 1, 1.0, 1.0, 2, 18, new Insets(5, 0, 5, 5));
		headerPanel.add(lbKulanz, 2, 0, 1, 1, 0.0, 0.0, 0, GridBagConstraints.EAST, new Insets(0, 5, 5, 0));
		headerPanel.add(new JPanel(), 3, 0, 1, 1, 1.0, 1.0, 2, 18, new Insets(0, 0, 5, 5));
		headerPanel.add(lbRatenzahlung, 4, 0, 1, 1, 0.0, 0.0, 0, GridBagConstraints.WEST, new Insets(0, 5, 5, 0));
		headerPanel.add(new JPanel(), 5, 0, 1, 1, 1.0, 1.0, 2, 18, new Insets(0, 0, 5, 0));
		headerPanel.add(lbTitelMatrikelnr, 6, 0, 1, 1, 0.0, 0.0, 0, GridBagConstraints.EAST, new Insets(0, 5, 5, 0));
		headerPanel.add(lbTitelUni, 7, 0, 1, 1, 0.0, 0.0, 0, GridBagConstraints.EAST, new Insets(0, 5, 5, 5));


		// Bankdaten, werden dann im Personpanel geaddet. Nicolai hat das alte Extrapanel aufgelöst
		// um das Layout zu vereinheitlichen.

		lbKontoName = new JLabel("Kontoinhaber_in (falls nicht Antragsteller_in):");
		lbKontoStrasse = new JLabel("Straße und Hausnummer");
		lbKontoWohnort = new JLabel("Wohnort");
		lbIBAN = new JLabel("IBAN");
		lbBIC = new JLabel("BIC");
		
		MaskFormatter formatter = null;
		try {
            //siehe unicode ᒧᚆ╷╻⟂
            //formatter = new MaskFormatter("UU## **** **** **** **** **** **** **** **");
            formatter = new MaskFormatter("****╷****╷****╷****╷****╷****╷****╷****╷**");
            formatter.setValidCharacters("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ ");
            formatter.setValueContainsLiteralCharacters(false);

		} catch (ParseException e) {
			e.printStackTrace();
		}


		ftfIBAN = new JFormattedTextField(formatter);
		ftfIBAN.setFocusLostBehavior(JFormattedTextField.COMMIT);

		ftfIBAN.setColumns(30);
		ftfIBAN.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent var1) {
				try {
					ftfIBAN.commitEdit();
				} catch (Exception e) {
					//DO NOTHING
				}
				checkIBAN();
			}

		});

        tfBIC = new JTextField(14);
		tfKontoName = new JTextField(30);
		tfKontoStrasse = new JTextField(30);
		tfKontoWohnort = new JTextField(20);

		// ***** InfoPanel erstellen *****

		labelPersonID = new JLabel("Person-ID: ");

        JLabel labelTitelAngelegt = new JLabel("Person angelegt:");
		labelTitelAngelegt.setForeground(Color.GRAY);
		JLabel labelTitelLastChange = new JLabel("Letzte Änderung:");
		labelTitelLastChange.setForeground(Color.GRAY);
		labelDatumAngelegt = new JLabel("");
		labelDatumAngelegt.setForeground(Color.GRAY);
		labelDatumLastChange = new JLabel("");
		labelDatumLastChange.setForeground(Color.GRAY);
		labelLoeschstatus = new JLabel("");
		
		labelPersonID.setFont(Layout.INFO_FONT);
		labelTitelAngelegt.setFont(Layout.INFO_FONT);
		labelDatumAngelegt.setFont(Layout.INFO_FONT);
		labelTitelLastChange.setFont(Layout.INFO_FONT);
		labelDatumLastChange.setFont(Layout.INFO_FONT);
		labelLoeschstatus.setFont(Layout.INFO_FONT);

        infoPanel = new SForm();
        Border border = BorderFactory.createLineBorder(Color.GRAY);
        Border margin = new EmptyBorder(5, 5, 5, 5);
        infoPanel.setBorder(new CompoundBorder(border, margin));
        infoPanel.setBackground(new Color(220, 220, 220));
		
		infoPanel.add(labelPersonID,           0,  1, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 5, 0));
		infoPanel.add(labelTitelAngelegt,      0,  6, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 0, 0, 0));
		infoPanel.add(labelDatumAngelegt,      0,  7, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 5, 0));
		infoPanel.add(labelTitelLastChange,    0,  8, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 0, 0, 0));
		infoPanel.add(labelDatumLastChange,    0,  9, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 5, 0));
		infoPanel.add(labelLoeschstatus,       0, 10, 1, 1, 1.0, 1.0, 0, 18, new Insets(0, 0, 0, 0));


        // PersonPanel mit Angaben zur Person

        personPanel = new SForm();

		personPanel.add(headerPanel, 0, 1, 5, 1, 0.0, 0.0, 2, 17, new Insets(5, 0, 10, 0));
//		personPanel.add(lbTitle1,              0,  2, 5, 1, 0.0, 0.0, 0, 17, new Insets(10, 7, 2, 2));
		personPanel.add(lbNachname,            0,  3, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
		personPanel.add(lbVorname,             1,  3, 2, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
		personPanel.add(lbGeburtsdatum,        3,  3, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
		personPanel.add(lbMatrikelnummer,      4,  3, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
		personPanel.add(lbCO,                  0,  5, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
		personPanel.add(lbWohneinheit,         1,  5, 2, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
		personPanel.add(lbStrasse,             0,  7, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
		personPanel.add(lbPLZ,                 1,  7, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
		personPanel.add(lbWohnort,             2,  7, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
		personPanel.add(lbLand,                3,  7, 2, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
		personPanel.add(lbEmail,               0,  9, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
		personPanel.add(lbZahlungsdaten, 0, 12, 1, 1, 0.0, 0.0, 0, 17, new Insets(10, 7, 0, 0));

		personPanel.add(lbIBAN, 1, 12, 2, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
		personPanel.add(ftfIBAN, 1, 13, 2, 1, 0.0, 0.0, 0, 17, new Insets(1, 4, 5, 4));
		personPanel.add(lbBIC, 3, 12, 2, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
		personPanel.add(tfBIC, 3, 13, 2, 1, 0.0, 0.0, 0, 17, new Insets(1, 4, 5, 4));
		personPanel.add(cbBarauszahlung, 0, 13, 1, 1, 0.0, 0.0, 0, GridBagConstraints.WEST, new Insets(5, 7, 1, 20));



		personPanel.add(lbKontoName,           0, 14, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
		personPanel.add(lbKontoStrasse,        1, 14, 2, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
		personPanel.add(lbKontoWohnort,        3, 14, 2, 1, 0.0, 0.0, 0, 17, new Insets(5, 7, 1, 2));
		personPanel.add(lbAntragsuebersicht,              0, 16, 5, 1, 0.0, 0.0, 0, 17, new Insets(10, 7, 2, 2));
		personPanel.add(lbAnmerkung, 1, 16, 4, 1, 0.0, 0.0, 0, 17, new Insets(10, 7, 2, 2));
		
		
		personPanel.add(tfNachname,                  0,  4, 1, 1, 0.0, 0.0, 0, 17, new Insets(1, 4, 5, 4));
		personPanel.add(tfVorname,                   1,  4, 2, 1, 0.0, 0.0, 0, 17, new Insets(1, 4, 5, 4));
		personPanel.add(tfGeburtsdatum,              3,  4, 1, 1, 0.0, 0.0, 0, 17, new Insets(1, 4, 5, 4));
		personPanel.add(tfMatrikelnummer,            4,  4, 1, 1, 0.0, 0.0, 0, 17, new Insets(1, 4, 5, 4));
		personPanel.add(tfCO,                        0,  6, 1, 1, 0.0, 0.0, 0, 17, new Insets(1, 4, 5, 4));
		personPanel.add(tfWohneinheit,               1,  6, 2, 1, 0.0, 0.0, 0, 17, new Insets(1, 4, 5, 4));
		personPanel.add(toggleButtonFlag, 3, 6, 1, 1, 0.0, 0.0, 0, 17, new Insets(1, 4, 5, 4));
		personPanel.add(tfStrasse,                   0,  8, 1, 1, 0.0, 0.0, 0, 17, new Insets(1, 4, 5, 4));
		personPanel.add(tfPLZ,                       1,  8, 1, 1, 0.0, 0.0, 0, 17, new Insets(1, 4, 5, 4));
		personPanel.add(tfWohnort,                   2,  8, 1, 1, 0.0, 0.0, 2, 17, new Insets(1, 4, 5, 4));
		personPanel.add(tfLand,                      3,  8, 2, 1, 0.0, 0.0, 2, 17, new Insets(1, 4, 5, 4));
		personPanel.add(tfEmail,                     0, 10, 1, 1, 0.0, 0.0, 0, 17, new Insets(1, 4, 5, 4));
		personPanel.add(cbTelefon,                   1, 10, 2, 1, 0.0, 0.0, 0, 17, new Insets(1, 4, 5, 4));

		personPanel.add(tfKontoName,                 0, 15, 1, 1, 0.0, 0.0, 0, 17, new Insets(1, 4, 5, 4));
		personPanel.add(tfKontoStrasse,              1, 15, 2, 1, 0.0, 0.0, 0, 17, new Insets(1, 4, 5, 4));
		personPanel.add(tfKontoWohnort,              3, 15, 2, 1, 0.0, 0.0, 2, 17, new Insets(1, 4, 5, 4));
		personPanel.add(scrollPaneAntraege, 0, 17, 1, 2, 0.0, 0.0, 2, GridBagConstraints.NORTHWEST, new Insets(1, 4, 5, 4));
		personPanel.add(tfAnmerkung, 1, 17, 4, 1, 0.0, 0.0, 2, 17, new Insets(1, 4, 5, 4));
		personPanel.add(scrollPaneAnmerkungen, 1, 18, 4, 1, 1.0, 1.0, 2, 17, new Insets(1, 4, 5, 4));
		
		
		
		

		
		// ***** ButtonPanel erstellen *****
		
		// Personendaten in Datenbank speichern
		speichernButton = new JButton("Speichern");
				
		// Personendaten im Formular zurücksetzen
		resetButton = new JButton("Zurücksetzen");

		// Antrag zu aktuellem Semester anzeigen (wenn vorhanden)
		aktuellerAntragButton = new JButton("Aktueller Antrag");
		
		// ungeprüfte Nachreichung zu aktuellem Antrag anlegen (wenn vorhanden)
		nachreichungButton = new JButton("Nachreichung");

		// Bestimmten Antrag einer Person anzeigen (ausgewählt aus Übersichtstabelle)
		bestimmterAntragButton = new JButton("Bestimmter Antrag");
		
		// neuen Antrag für Person anlegen
		neuerAntragButton = new JButton("Neuer Antrag");
		
		// Mail an Person senden, wenn Mailadresse vorhanden/in DB gespeichert
		mailButton = new JButton("E-Mail");
		
		protokollButton = new JButton("Bearbeitungsprotokoll");

        delButton = new JButton("Löschen");

		changeUniButton = new JButton("Uni wechseln");


        SForm buttonPanel = new SForm();
		
		// Buttons hinzufügen
		buttonPanel.add(infoPanel, 0, 0, 1, 1, 0.0, 0.0, 2, 11, new Insets(5, 10, 5, 10));

		buttonPanel.add(speichernButton, 0, 1, 1, 1, 0.0, 0.0, 2, 17, new Insets(25, 5, 15, 5));
		buttonPanel.add(resetButton, 0, 2, 1, 1, 0.0, 0.0, 2, 17, new Insets(5, 5, 15, 5));
		buttonPanel.add(delButton, 0, 3, 1, 1, 0.0, 0.0, 2, 17, new Insets(5, 5, 15, 5));
		buttonPanel.add(changeUniButton, 0, 4, 1, 1, 1.0, 1.0, 2, 17, new Insets(5, 5, 25, 5));

		buttonPanel.add(mailButton, 0, 5, 1, 1, 0.0, 0.0, 2, 17, new Insets(5, 5, 25, 5));


		buttonPanel.add(neuerAntragButton, 0, 6, 1, 1, 0.0, 0.0, 2, 17, new Insets(25, 5, 25, 5));
		buttonPanel.add(aktuellerAntragButton, 0, 7, 1, 1, 0.0, 0.0, 2, 17, new Insets(5, 5, 25, 5));
		buttonPanel.add(bestimmterAntragButton, 0, 8, 1, 1, 0.0, 0.0, 2, 17, new Insets(5, 5, 25, 5));
		buttonPanel.add(nachreichungButton, 0, 10, 1, 1, 0.0, 0.0, 2, 17, new Insets(25, 5, 25, 5));




		// ***** Gesamtes Panel (Formular und Buttons) erstellen *****
		
		SForm mainPanel = new SForm();
		
		// Panel hinzufügen
		mainPanel.add(personPanel,     0, 0, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 5, 5, 5));
		mainPanel.add(buttonPanel,     1, 0, 1, 1, 1.0, 1.0, 0, 18, new Insets(5, 5, 5, 5));
		
		// Gesamtpanel in Scrollpane stecken
		mainScrollPane = new JScrollPane(mainPanel);
		
		// Scrollgeschwindigkeit festlegen
		mainScrollPane.getVerticalScrollBar().setUnitIncrement(Settings.SCROLL_UNIT);
		
		
		
		// DocumentSizeFilter für Textfelder setzen (max. Anzahl an Zeichen und Pattern)
		((AbstractDocument) tfNachname.getDocument()).setDocumentFilter(new DocumentSizeFilter(50, DocumentSizeFilter.TEXT_PATTERN));
		((AbstractDocument) tfVorname.getDocument()).setDocumentFilter(new DocumentSizeFilter(50, DocumentSizeFilter.TEXT_PATTERN));
        ((AbstractDocument) tfGeburtsdatum.getDocument()).setDocumentFilter(new DocumentSizeFilter(10, DocumentSizeFilter.DATE_PATTERN));
		((AbstractDocument) tfMatrikelnummer.getDocument()).setDocumentFilter(new MatrikelFilter(10));
		((AbstractDocument) tfCO.getDocument()).setDocumentFilter(new DocumentSizeFilter(100, DocumentSizeFilter.FULLTEXT_PATTERN));
		((AbstractDocument) tfWohneinheit.getDocument()).setDocumentFilter(new DocumentSizeFilter(80, DocumentSizeFilter.FULLTEXT_PATTERN));
		((AbstractDocument) tfStrasse.getDocument()).setDocumentFilter(new DocumentSizeFilter(80, DocumentSizeFilter.FULLTEXT_PATTERN));
		((AbstractDocument) tfPLZ.getDocument()).setDocumentFilter(new DocumentSizeFilter(11, DocumentSizeFilter.PLZ_PATTERN));
		((AbstractDocument) tfWohnort.getDocument()).setDocumentFilter(new DocumentSizeFilter(50, DocumentSizeFilter.FULLTEXT_PATTERN));
		((AbstractDocument) tfLand.getDocument()).setDocumentFilter(new DocumentSizeFilter(50, DocumentSizeFilter.FULLTEXT_PATTERN));
		((AbstractDocument) tfEmail.getDocument()).setDocumentFilter(new DocumentSizeFilter(80, DocumentSizeFilter.EMAIL_PATTERN));
        ((AbstractDocument) tfBIC.getDocument()).setDocumentFilter(new DocumentSizeFilter(11, DocumentSizeFilter.BIC_PATTERN));
        ((AbstractDocument) tfKontoName.getDocument()).setDocumentFilter(new DocumentSizeFilter(80, DocumentSizeFilter.TEXT_PATTERN));
		((AbstractDocument) tfKontoStrasse.getDocument()).setDocumentFilter(new DocumentSizeFilter(80, DocumentSizeFilter.FULLTEXT_PATTERN));
		((AbstractDocument) tfKontoWohnort.getDocument()).setDocumentFilter(new DocumentSizeFilter(80, DocumentSizeFilter.FULLTEXT_PATTERN));
	        
        
        
        // ***** Zusammengehörende Labels und Textfelder in HashMap zusammenfassen *****
		//  Formularcheck der Pflichtfelder: färbt Label rot, bei fehlerhaften oder fehlenden Angaben
		
		hashMapCheckFields = new HashMap<JLabel, JTextField>();
		
		hashMapCheckFields.put(lbNachname, tfNachname);
		hashMapCheckFields.put(lbVorname, tfVorname);
		hashMapCheckFields.put(lbGeburtsdatum, tfGeburtsdatum);
		hashMapCheckFields.put(lbMatrikelnummer, tfMatrikelnummer);
		hashMapCheckFields.put(lbStrasse, tfStrasse);
		hashMapCheckFields.put(lbPLZ, tfPLZ);
		hashMapCheckFields.put(lbWohnort, tfWohnort);

        setKeyBindings();


	}


	/**
	 * Liefert Scrollpane über PersonControl an TabView (CardLayout)
	 * @return Scrollpane mit Formular
	 */
	public JScrollPane getScrollPane() {
		return mainScrollPane;
	}

	private void checkConfigurationOptionsFromDB() {
		DBHandlerConf dbHandlerConf = new DBHandlerConf();

		try {
			emptyBICallowed = Boolean.parseBoolean(dbHandlerConf.read("emptyBICAllowed"));
			khbKeineBarauszahler = Boolean.parseBoolean(dbHandlerConf.read("keineBarauszahlerKHB"));
		} catch (Exception e) {
			emptyBICallowed = false;
			khbKeineBarauszahler = false;
		}
	}

	
	
	
	/**
	 * Initialisiert die Tabelle mit der Antragsübersicht
	 * @param tableModel TableModelAntragUebersicht
	 */
	public void initTableAntraege(TableModelAntragUebersicht tableModel) {
		
		// TableModel für Tabelle mit der Antragsübersicht setzen
		antragTable.setModel(tableModel);

		antragTable.setAutoCreateRowSorter(true);

		// nur einzelne Tabellenzeilen können selektiert werden
		antragTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// die Spalten der Tabelle können nicht vertauscht werden
		antragTable.getTableHeader().setReorderingAllowed(true);
		
		// die Spaltenbreite kann nicht verändert/verschoben werden
		antragTable.getTableHeader().setResizingAllowed(true);
		
		// Zellen erhalten keinen Fokusrahmen
		antragTable.setFocusable(false);
		
	}
	
	
	
	

	/**
	 * Initialisiert die Tabelle mit den Anmerkungen
	 * @param tableModel TableModelAnmerkungen
	 */
	public void initTableAnmerkungen(final TableModelAnmerkungen tableModel) {

		// TableModel für Tabelle mit den Anmerkungen setzen
		anmerkungTable.setModel(tableModel);

		// nur einzelne Tabellenzeilen können selektiert werden
		anmerkungTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        anmerkungTable.setRowSelectionAllowed(true);

		// die Spalten der Tabelle können nicht vertauscht werden
		anmerkungTable.getTableHeader().setReorderingAllowed(false);

		// die Spaltenbreite kann nicht verändert/verschoben werden
		anmerkungTable.getTableHeader().setResizingAllowed(true);
		// Zellen erhalten keinen Fokusrahmen
		anmerkungTable.setFocusable(false);

		// Zeilenhöhe setzen
		anmerkungTable.setRowHeight(60);

		// Hintergrundfarbe für Tabelle setzen
		anmerkungTable.setBackground(Color.WHITE);

		anmerkungTable.setShowGrid(true);
		anmerkungTable.setIntercellSpacing(new Dimension(2, 2));
		anmerkungTable.setShowHorizontalLines(true);
		anmerkungTable.setGridColor(new Color(200, 200, 200));

		anmerkungTable.setTableHeader(null);

		// Tabellenspaltenmodel von der Tabelle holen
		TableColumnModel columnModel = anmerkungTable.getColumnModel();

		// Den Tabellenspalten Breite und Renderer zuweisen.
		columnModel.getColumn(0).setPreferredWidth(40);
		columnModel.getColumn(1).setPreferredWidth(10);
		columnModel.getColumn(2).setPreferredWidth(242);
		columnModel.getColumn(2).setCellRenderer(new MultiLineTableRenderer());

	}


	
	
	/**
	 * Fügt Komponenten des Formulars verschiedene Listener mit Verbindung zu PersonControl hinzu
	 * @param personControl PersonControl
	 */
	public void addListener(final PersonControl personControl) {
		

		speichernButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				personControl.savePerson();
			}
		});
		

		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				personControl.resetPerson();
			}
		});

		changeUniButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				personControl.changeUniversity();
			}
		});

		aktuellerAntragButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				personControl.antragAktuell();
			}
		});
		
		
		nachreichungButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new NachreichungControl(personControl, null);
			}
		});
		

		bestimmterAntragButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				personControl.antragBestimmt(antragTable.getSelectedRow());
			}
		});

		neuerAntragButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				personControl.antragNeu();
			}
		});

		mailButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				personControl.sendMail();
			}
		});


        delButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = JOptionPane.showConfirmDialog(null, "Wirklich löschen?");
                if (JOptionPane.YES_OPTION == returnValue)
                    personControl.deletePerson();
            }
        });

		protokollButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				personControl.bearbeitungsProtokoll(antragTable.getSelectedRow());
			}
		});


        infoPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				personControl.bearbeitungsProtokoll(antragTable.getSelectedRow());
			}
		});


		// MouseListener für Doppelklick auf Tabelle mit Antragsübersicht
		antragTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount()==2){
					// ausgewählten Antrag anzeigen
					personControl.antragBestimmt(antragTable.getSelectedRow());
				}
			}
		});
		
		
		
		
		// ***** FocusListener hinzufügen *****
		
		// Textfeld Geburtsdatum erhält Fokus: alles im Textfeld selektieren
        tfGeburtsdatum.addFocusListener(new FocusAdapter() {
        	public void focusGained(FocusEvent e) {tfGeburtsdatum.selectAll();}
        });


		tfAnmerkung.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
                if (tfAnmerkung.getText().trim().length() > 0) {
                    personControl.addAnmerkung(tfAnmerkung.getText().trim());
                    tfAnmerkung.setText("");
                }
            }
		});


        // Listener für die Checkbox Barauszahlung (blendet Bankverbindung aus)
        // ActionListener, weil ItemListener Probleme in Verbindung mit JOptionPane macht
        cbBarauszahlung.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
                setBarzahlung(((JCheckBox) e.getSource()).isSelected());
            }
        });

        if (UniConf.aktuelleUni.getID() == Uni.KW.getID())
            cbBarauszahlung.setEnabled(!khbKeineBarauszahler);


        // Popup-Menü für Tabelle Anmerkungen erstellen
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("Anzeigen");
	    menuItem1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = anmerkungTable.convertRowIndexToModel(anmerkungTable.getSelectedRow());
				Anmerkung anmerkung = ((TableModelAnmerkungen) anmerkungTable.getModel()).getAnmerkung(row);
				new DialogShowAnmerkung(anmerkung);
				anmerkungTable.clearSelection();
			}
        });
        popupMenu.add(menuItem1);
        JMenuItem menuItem2 = new JMenuItem("Ändern");
        menuItem2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = anmerkungTable.convertRowIndexToModel(anmerkungTable.getSelectedRow());
				Anmerkung anmerkung = ((TableModelAnmerkungen) anmerkungTable.getModel()).getAnmerkung(row);
				new DialogEditAnmerkung(personControl, anmerkung);
				anmerkungTable.clearSelection();
			}
	    });
	    popupMenu.add(menuItem2);
	    JMenuItem menuItem3 = new JMenuItem("Löschen");
	    menuItem3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
                //Info: aus irgendwelchen Gründen verändert der Nachfragedialog den selectedRow-Wert und deshalb muss ohne Nachfragen gelöscht werden
                int row = anmerkungTable.convertRowIndexToModel(anmerkungTable.getSelectedRow());
                Anmerkung anmerkung = ((TableModelAnmerkungen) anmerkungTable.getModel()).getAnmerkung(row);
                personControl.deleteAnmerkung(anmerkung);
				anmerkungTable.clearSelection();
			}
	    });
	    popupMenu.add(menuItem3);
	    
	    anmerkungTable.addMouseListener(new PopupMenuMouseListener(popupMenu));

        anmerkungTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row = anmerkungTable.convertRowIndexToModel(anmerkungTable.getSelectedRow());
					Anmerkung anmerkung = ((TableModelAnmerkungen) anmerkungTable.getModel()).getAnmerkung(row);
					new DialogEditAnmerkung(personControl, anmerkung);
					anmerkungTable.clearSelection();
				}
            }
        });


        // ***** InputVerifier hinzufügen *****
        // überprüft Inhalt nach Verlassen des Textfeldes

        // Überprüfung und Formatierung des Geburtsdatums
        tfGeburtsdatum.setInputVerifier(new InputDateVerifier());
        
        // Email auf Gültigkeit überprüfen
        tfEmail.setInputVerifier(new InputEmailVerifier());
	
		
		
		
		// ***** ChangeListener hinzufügen *****
		
		// Listener erkennt, wenn sich an den Eingabekomponenten etwas geändert hat
		
		// Textfelder mit Listener versehen, die erkennen, wenn es eine Änderung gegeben hat
		JTextField[] textfelder = new JTextField[] {tfNachname, tfVorname, tfGeburtsdatum, tfMatrikelnummer,
				tfStrasse, tfPLZ, tfWohnort, tfLand, tfCO, tfWohneinheit, tfEmail, tfKontoName, tfKontoStrasse,
				tfKontoWohnort, ftfIBAN, tfBIC};
		
		// DocumetListener für Textfelder
		FormChangeListener formChangeListener = new FormChangeListener(personControl);
		
		// ItemListener für Checkboxen, ComboBoxen, ToggleButton
		FormChangeListener2 formChangeListener2 = new FormChangeListener2(personControl);
		
		for (JTextField jtf : textfelder) {
			jtf.getDocument().addDocumentListener(formChangeListener);
		}
		
		cbTelefon.addItemListener(formChangeListener2);
		cbBarauszahlung.addItemListener(formChangeListener2);
		toggleButtonFlag.addItemListener(formChangeListener2);
	
	}

	
	


	/**
	 * Holt die Daten aus den Formularfeldern und erstellt ein Person-Objekt.
	 * @return Person
	 */
	public Person getPerson() {
		
		// personID ist schon im PersonModel gespeichert und wird nicht benötigt
		int pid = -1;
		
		Uni uni = UniConf.aktuelleUni;

		GregorianCalendar geburtsdatum = null;


		// falls Textfeld nicht leer, String in GregorianCalendar umwandeln
		if(!tfGeburtsdatum.getText().trim().equals(""))
			geburtsdatum = StringHelper.convertStringToDate(tfGeburtsdatum.getText());
		
		
		
		
		try {
			ftfIBAN.commitEdit();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String ibanShort = ((String) ftfIBAN.getValue()).toUpperCase().trim();



        return new Person(pid, tfMatrikelnummer.getText().trim(), uni,
                tfNachname.getText().trim(), tfVorname.getText().trim(), geburtsdatum,
                toggleButtonFlag.isSelected(), tfCO.getText().trim(), tfStrasse.getText().trim(),
                tfWohneinheit.getText().trim(), tfPLZ.getText().trim(), tfWohnort.getText().trim(),
                tfLand.getText().trim(), cbTelefon.isSelected(), tfEmail.getText().trim(),
                cbBarauszahlung.isSelected(), -1, null, -1, null,
                ibanShort, tfBIC.getText().trim(),
                tfKontoName.getText().trim(), tfKontoStrasse.getText().trim(), tfKontoWohnort.getText().trim());
    }

	/**
     * Setzt Personendaten einer bestimmten Person ins Formular
     * @param person ausgewählte Person
     */
    public void setPerson(Person person) {

        if (person.getUni().equals(Uni.KW))
			if (khbKeineBarauszahler)
				cbBarauszahlung.setEnabled(false);

        lbTitelName.setText(person.toString());

        if (!person.getMatrikelnr().equals(""))
            lbTitelMatrikelnr.setText(person.getMatrikelnr());
        else
			lbTitelMatrikelnr.setText("Matrikelnr ?");

		lbTitelUni.setText(person.getUni().getUniName());

        tfNachname.setText(person.getNachname());
        tfVorname.setText(person.getVorname());
        tfMatrikelnummer.setText(person.getMatrikelnr());
        tfStrasse.setText(person.getStrasse());
        tfPLZ.setText(person.getPlz());
        tfWohnort.setText(person.getWohnort());
        tfLand.setText(person.getLand());
        tfCO.setText(person.getCo());
        tfWohneinheit.setText(person.getWohneinheit());
        tfEmail.setText(person.getEmail());
        ftfIBAN.setValue(person.getIBAN());
        tfBIC.setText(person.getBIC());
        tfKontoName.setText(person.getKontoInhaber_Name());
        tfKontoStrasse.setText(person.getKontoInhaber_Strasse());
        tfKontoWohnort.setText(person.getKontoInhaber_Wohnort());

        if (person.getGebdatum() != null) {
            GregorianCalendar gc = person.getGebdatum();
            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            tfGeburtsdatum.setText(df.format(gc.getTime()));
        } else {
            tfGeburtsdatum.setText("");
        }

        // bei Barauszahlung Komponenten für Kontoverbindungsdaten ausblenden
        if (person.isBarauszahler()) {
            enableBankdaten(false);
        } else
            enableBankdaten(true);

        cbTelefon.setSelected(person.isHatTelefon());

		cbBarauszahlung.setSelected(person.isBarauszahler());

        toggleButtonFlag.setSelected(person.isEnglischsprachig());

        if (person.getPersonID() == -1)
            speichernButton.setEnabled(true);

        // Formular auf Pflichtfelder überprüfen
        checkFields();

        // Seite nach ganz oben scrollen
        mainScrollPane.getVerticalScrollBar().getModel().setValue(0);


    }

	/**
     * Setzt den Status, ob es Änderungen am Formular gegeben hat und eventuell gespeichert
     * werden muss.
     * @param statusSpeichern Speichernbutton benutzbar machen?
     * @param statusReset Resetbutton benutzbar machen?
     */
    public void setSaveStatus(boolean statusSpeichern, boolean statusReset) {
        speichernButton.setEnabled(statusSpeichern);
        resetButton.setEnabled(statusReset);
    }

    /**
     * View wird vom Model aktualisiert (Observer)
     */
    @Override
    public void update(Observable o, Object arg) {

        Person person = (Person) arg;
        PersonModel personModel = (PersonModel) o;

		if (nachreichungButton.isEnabled())
			setNachreichungen(personModel.uncheckedNachreichungen());

        if (person != null) {

            // Personendaten aus PersonModel ins Formular eintragen
            setPerson(person);

            // ***** Daten für InfoPanel anzeigen*****
            labelPersonID.setText("Person-ID: " + person.getPersonID());
            labelDatumAngelegt.setText(personModel.getDatumAngelegt());
            labelDatumLastChange.setText(personModel.getDatumGeaendert());

            // Anzeige Kulanz an/aus
            lbKulanz.setVisible(personModel.isKulanz());

            // Anzeige Ratenzahlung an/aus
            lbRatenzahlung.setVisible(personModel.isRatenzahlung());

			checkConfigurationOptionsFromDB();

        } else {
            personControl.schließen();
        }

	}
	
	/**
     * Anzeige des Button für den aktuellen Antrag ändern
     * @param state aktivieren? ja/nein
     */
    public void enableButtonAntragAktuell(boolean state) {
        aktuellerAntragButton.setEnabled(state);
    }

    /**
     * Anzeige des Button für Nachreichung zu aktuellem Antrag ändern
     *
     * @param state aktivieren? ja/nein
     */
    public void enableButtonNachreichung(boolean state) {
        nachreichungButton.setEnabled(state);
    }

    /**
     * Anzeige des Button für bestimmten Antrag ändern
     *
     * @param state aktivieren? ja/nein
     */
    public void enableButtonAntragBestimmt(boolean state) {
        bestimmterAntragButton.setEnabled(state);
    }

    /**
     * Anzeige des Button für neuen Antrag ändern
     *
     * @param state aktivieren? ja/nein
     */
    public void enableButtonAntragNeu(boolean state) {
        neuerAntragButton.setEnabled(state);
    }

    /**
     * Anzeige des Button für Bearbeitungsprotokoll ändern
     *
     * @param state aktivieren? ja/nein
     */
    public void enableButtonProtokoll(boolean state) {
        protokollButton.setEnabled(state);
    }

    /**
     * Selektiert den obersten (aktuellsten) Antrag in der Übersichtstabelle, wenn
     * midestens 1 Antrag vorhanden ist.
     */
    public void selectFirstantrag() {
        if (antragTable.getRowCount() > 0)
            antragTable.setRowSelectionInterval(0, 0);
    }

    /**
     * Leert die Felder mit den Bankdaten
	 */
	private void clearBankdaten(){

		tfKontoName.setText("");
		tfKontoStrasse.setText("");
		tfKontoWohnort.setText("");
		ftfIBAN.setValue("");
		tfBIC.setText("");
		
	}


	/**
	 * Setzt die Anzahl eingegangener Nachreichungen
	 *
	 * @param anzahlNachreichungen Anzahl Nachreichungne
	 */
	public void setNachreichungen(int anzahlNachreichungen) {

		if (anzahlNachreichungen > 0) {
			String message = (anzahlNachreichungen == 1) ? " Nachreichung" : " Nachreichungen";
			nachreichungButton.setText(anzahlNachreichungen + message);
			nachreichungButton.setForeground(Color.RED);
		} else {
			nachreichungButton.setText("Nachreichung");
			nachreichungButton.setForeground(Color.BLACK);
		}

	}

	/**
	 * Prüft die Gültigkeit der IBAN
     *
     * @return true wenn gültig
	 */
	public boolean checkIBAN() {

        if (cbBarauszahlung.isSelected()) {
            lbIBAN.setForeground(Color.LIGHT_GRAY);
            return true;
        }

		String iban = ((String) ftfIBAN.getValue());

		try {
			if (iban.length() > 0 && IbanTest.ibanTest(iban)) {
				lbIBAN.setForeground(Color.BLACK);
			} else {
				lbIBAN.setForeground(Color.RED);
			}
		} catch (NumberFormatException nfe) {
			lbIBAN.setForeground(Color.RED);
		}
        return lbIBAN.getForeground().equals(Color.BLACK);

    }


    /**
     * Anzeige der Komponenten (Label, Textfelder...) der Bankverbindungsdaten
	 * @param state anzeigen? ja/nein
	 */
	private void enableBankdaten(boolean state){
		
		// Aussehen der Label und Komponenten ändern

		JLabel[] labels = {lbIBAN, lbBIC, lbKontoName, lbKontoStrasse, lbKontoWohnort};
		
		JComponent[] components = {ftfIBAN, tfBIC, tfKontoName, tfKontoStrasse, tfKontoWohnort};
		
		// Schriftfarbe der Labels setzen
        for (JLabel l : labels) {
            Color farbe = (state) ? Color.BLACK : Color.LIGHT_GRAY;
            l.setForeground(farbe);
        }

        // Zustand der Komponenten setzen
        for (Component c : components) {
            c.setEnabled(state);
        }

        if(state) {
            if (ftfIBAN.getValue().toString().trim().equals("")) {
                lbIBAN.setForeground(Color.RED);
            } else {
                lbIBAN.setForeground(Color.BLACK);
            }

            if (emptyBICallowed || tfBIC.getText().length() > 0)
                lbBIC.setForeground(Color.BLACK);
            else
                lbBIC.setForeground(Color.RED);


        }
	}
	
	
	
	
	
	

	/**
	 * Visuelle Änderung bei Auwahl der Checkbox Barzahlung 
	 * barStatus = true --> Barauszahlung angeklickt
	 * @param barStatus Barauszahlung angeklickt?
	 */
	private void setBarzahlung(boolean barStatus) {

		// wenn Barauszahlung angeklickt wird, Abfrage/Hinweis, dass alle Kontodaten gelöscht werden
		if(barStatus){
			
			int selected = 0;
			
			// Abfrage/Hinweis anzeigen
			Object[] options = {"fortfahren", "abbrechen"};	// Auswahlreihenfolge: abbrechen = 1, fortfahren = 0 ???
			String message = "<html>ACHTUNG: Mit der Auswahl Barauszahlung<br>werden alle bisherigen Kontodaten gelöscht.</html>";
			selected = JOptionPane.showOptionDialog(null, message, "Hinweis", JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE, null, options, options[1]);
			
			// Auswertung Abfrage/Hinweis: fortfahren (0), abbrechen (1)
			if(selected == 0){
				// Kontodaten löschen
				clearBankdaten();
				// Änderungen am Formular haben stattgefunden (Speicherhinweis)
				//personControl.updateSaveStatus(true);
			}
			else if(selected == 1){
				// Checkbox wieder auf false (nicht selektiert) setzen
				cbBarauszahlung.setSelected(false);
				barStatus = false;
			}
		}

		// wenn Barauszahlung selektiert, Bankdaten-Felder ausblenden	
		enableBankdaten(!barStatus);
		
	}
	
	

	
	
	
	
	/**
	 * Textfelder/Pflichtfelder überprüfen (wenn leer, Label rot färben)
	 */
	public void checkFields() {

		for(Map.Entry<JLabel, JTextField> entry : hashMapCheckFields.entrySet()) {
			
			if(entry.getValue().getText().trim().equals(""))
				entry.getKey().setForeground(Color.RED);
			else
				entry.getKey().setForeground(Color.BLACK);
			
		}

	}
	
	
	
	

	/**
	 * Setzt die KeyBindings (Hotkeys/Tastaturkürzel für das Personformular)
	 */
	@SuppressWarnings("serial")
	private void setKeyBindings() {


        mainScrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "actionSelectAntrag");
        mainScrollPane.getActionMap().put("actionSelectAntrag", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (antragTable.getSelectedRow() >= 0)
                    personControl.antragBestimmt(antragTable.getSelectedRow());
                else
                    personControl.antragBestimmt(1);
            }
        });


        // Taste STRG + S: Person speichern
        mainScrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK), "actionSavePerson");
        mainScrollPane.getActionMap().put("actionSavePerson", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                personControl.savePerson();
            }
        });


        // Taste STRG + N: Neuer Antrag
        mainScrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK), "actionAntragNeu");
        mainScrollPane.getActionMap().put("actionAntragNeu", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                personControl.antragNeu();
            }
        });

        // Taste STRG + Z: Zurücksetzen
        mainScrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK), "actionResetPerson");
        mainScrollPane.getActionMap().put("actionResetPerson", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                personControl.resetPerson();
            }
        });

		mainScrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("PAGE_UP"), "actionLastPerson");
		mainScrollPane.getActionMap().put("actionLastPerson", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				personControl.showLastPerson();
			}
		});

		mainScrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("PAGE_DOWN"), "actionNextPerson");
		mainScrollPane.getActionMap().put("actionNextPerson", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                personControl.showNextPerson();
            }
        });


        mainScrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.CTRL_MASK), "actionLastPerson");
        mainScrollPane.getActionMap().put("actionLastPerson", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                personControl.showLastPerson();
            }
        });

        mainScrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, InputEvent.CTRL_MASK), "actionNextPerson");
        mainScrollPane.getActionMap().put("actionNextPerson", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                personControl.showNextPerson();
            }
        });

    }

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
			personControl.showLastPerson();

		} else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
			personControl.showNextPerson();

		}


	}


}
