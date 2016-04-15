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


package org.semtix.gui.tabs.antrag;


import org.semtix.config.Settings;
import org.semtix.config.SettingsExternal;
import org.semtix.config.UserConf;
import org.semtix.db.DBHandlerVorgaenge;
import org.semtix.db.dao.Antrag;
import org.semtix.db.dao.AntragHaerte;
import org.semtix.db.dao.Vorgang;
import org.semtix.gui.tabs.antrag.nachreichungen.DialogFrist;
import org.semtix.gui.tabs.antrag.nachreichungen.pruefen.NachreichungControl;
import org.semtix.gui.tabs.antrag.nachreichungen.tabelle.ActionUnterlagenHinzufuegen;
import org.semtix.gui.tabs.antrag.nachreichungen.tabelle.UnterlagenTable;
import org.semtix.shared.bearbeitungsprotokoll.ProtokollControl;
import org.semtix.shared.daten.enums.AntragAblehnungsgrund;
import org.semtix.shared.daten.enums.AntragStatus;
import org.semtix.shared.daten.enums.Vorgangsart;
import org.semtix.shared.elements.LabelPerson;
import org.semtix.shared.elements.Layout;
import org.semtix.shared.elements.SForm;
import org.semtix.shared.elements.control.DocumentSizeFilter;
import org.semtix.shared.elements.control.FormChangeListener;
import org.semtix.shared.elements.control.FormChangeListener2;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Panel mit den Formularkomponeneten zur Anzeige und Bearbeitung von Antragsdaten. Über einen 
 * Observer ist diese Panel mit dem AntragModel verbunden, d.h. wenn sich im Model etwas ändert,
 * wird das Panel (View) aktualisiert.
 */
public class AntragPanel
implements Observer {

	private final JComboBox comboAblehnungsbegruendung;
	private AntragControl antragControl;
	private JScrollPane mainScrollPane;
    private SForm mainPanel;
    private JLabel lbTitelName, lbTitelMatrikelnr, lbUniversitaet;
    private JLabel lbSemester, lbZuschussmonate;
	private JLabel labelDatumAngelegt, labelDatumLastChange;
	private JLabel labelAntragID;
	private JPanel panelErstrechnung, panelZweitrechnung;
	private JButton buttonErstrechnung, buttonZweitrechnung;
	private JLabel lbDateErstrechnung, lbDateZweitrechnung, lbOKErstrechnung, lbOKZweitrechnung;
	private JComboBox comboEntscheidung;
	private JTextField tfEinkommen, tfHaerte, tfSummeHaerten;
    private JTextArea teilZuschussBegruendung;
	private JLabel lbAnnahme, lbNachfrage, lbBerechnung, lbBegruendung, lbPunktzahlen, lbEntscheidung, lbBescheid, lbAuszahlung;
	private JList listeBegruendung;
	private JSpinner spinner;
    private JCheckBox cbErstsemester, cbTeilzuschuss, cbManuellAuszahlen, cbBescheidVersandt,
            cbAuszahlungAngeordnet, cbNothilfe, cbRatenzahlung, cbKulanz;
    private JButton buttonUnterlagen, speichernButton, resetButton, personButton, berechnungsButton,
			anfordernButton, mahnungButton, nachreichungButton, delButton, printBescheidButton, printAZAButton;

    private UnterlagenTable unterlagenTable;

	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AntragPanel.class);

	/**
	 * Erstellt ein neues AntragPanel
     * @param control Verbindung zum Controller (AntrgaControl)
	 */
	public AntragPanel(AntragControl control) {

		this.antragControl = control;

		// Label für Überschriften
		lbAnnahme = new JLabel("Annahme");
		lbNachfrage = new JLabel("Nachfrage & Mahnung");
		lbBerechnung = new JLabel("Berechnung");
		lbBegruendung = new JLabel("Härtebegründungen");
		lbPunktzahlen = new JLabel("Punkte");
		lbEntscheidung = new JLabel("Entscheidung");
		lbBescheid = new JLabel("Bescheid");
		lbAuszahlung = new JLabel("Auszahlung");

		// Schrift in Label auf fett setzen
		lbAnnahme.setFont(lbAnnahme.getFont().deriveFont(Font.BOLD));
		lbNachfrage.setFont(lbNachfrage.getFont().deriveFont(Font.BOLD));
		lbBerechnung.setFont(lbBerechnung.getFont().deriveFont(Font.BOLD));
		lbEntscheidung.setFont(lbEntscheidung.getFont().deriveFont(Font.BOLD));
        lbBegruendung.setFont(lbBegruendung.getFont().deriveFont(Font.BOLD));
        lbPunktzahlen.setFont(lbPunktzahlen.getFont().deriveFont(Font.BOLD));
		lbBescheid.setFont(lbBescheid.getFont().deriveFont(Font.BOLD));
		lbAuszahlung.setFont(lbAuszahlung.getFont().deriveFont(Font.BOLD));


		// ***** TitelPanel erstellen *****
		// Panel für Titelüberschrift, Name, Matrikelnummer und Universität

		lbTitelName = new LabelPerson("Name", SwingConstants.LEFT);
		lbTitelMatrikelnr = new LabelPerson("Matrikel",SwingConstants.RIGHT);
		lbUniversitaet = new LabelPerson("Universität", SwingConstants.RIGHT);
		lbSemester = new LabelPerson("Semester", SwingConstants.RIGHT);


		SForm headerPanel = new SForm();
		headerPanel.add(lbTitelName, 0, 0, 1, 1, 0.0, 0.0, 0, GridBagConstraints.WEST, new Insets(0, 0, 5, 0));
		headerPanel.add(new JPanel(), 1, 0, 1, 1, 1.0, 1.0, 2, 18, new Insets(5, 0, 5, 5));
		headerPanel.add(lbTitelMatrikelnr, 2, 0, 1, 1, 0.0, 0.0, 0, GridBagConstraints.EAST, new Insets(0, 5, 5, 0));
		headerPanel.add(lbUniversitaet, 3, 0, 1, 1, 0.0, 0.0, 0, GridBagConstraints.EAST, new Insets(0, 5, 5, 0));
		headerPanel.add(lbSemester, 4, 0, 1, 1, 0.0, 0.0, 0, GridBagConstraints.EAST, new Insets(0, 5, 5, 2));


		cbErstsemester = new JCheckBox("Erstsemester");
		cbTeilzuschuss = new JCheckBox("Teilzuschuss");
		cbManuellAuszahlen = new JCheckBox("Barauszahlung");
		cbBescheidVersandt = new JCheckBox("Bescheid versandt");
		cbAuszahlungAngeordnet = new JCheckBox("Auszahlung angeordnet");
		cbNothilfe = new JCheckBox("Nothilfe");
		cbRatenzahlung = new JCheckBox("Individualzahlung");
		cbKulanz = new JCheckBox("Kulanz");
		cbKulanz.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (cbKulanz.isSelected()) {
                    int i = 3;
                    if (!antragControl.checkKulanz(i)) {
                        JOptionPane.showMessageDialog(null, "Auch wenn in den letzten " + i + " Semestern kein Kulanzantrag in der DB gefunden wurde, \n kann eine manuelle Nachprüfung sinnvoll sein.");
                    } else {
                        int returnvalue = JOptionPane.showConfirmDialog(null, "Es wurde in den letzten " + i + " Semestern ein Kulanzantrag gefunden. \n Soll der Haken trotzdem gesetzt bleiben?");
                        cbKulanz.setSelected(returnvalue == JOptionPane.YES_OPTION);
                    }
                }
			}
		});

		lbZuschussmonate = new JLabel("Monate");
		spinner = new JSpinner(new SpinnerNumberModel(0, 0, 6, 1));
		JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor)spinner.getEditor();
		spinnerEditor.getTextField().setEditable(true);

        teilZuschussBegruendung = new JTextArea();
        teilZuschussBegruendung.setLineWrap(true);
		teilZuschussBegruendung.setWrapStyleWord(true);
		teilZuschussBegruendung.setRows(7);
		teilZuschussBegruendung.setColumns(22);

		SForm titelPanel = new SForm();

		JScrollPane scrollZuschuss = new JScrollPane(teilZuschussBegruendung);


        // ***** AntragPanel erstellen *****

		JLabel lbHaerte = new JLabel("Härte");
		JLabel lbPlus = new JLabel("+");
		JLabel lbGleich = new JLabel("Total");
		JLabel lbGleichzeichen = new JLabel("=");
		JLabel lbEinkommen = new JLabel("Einkommen");


		comboEntscheidung = new JComboBox(AntragStatus.values());

        comboEntscheidung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
				if (comboEntscheidung.getSelectedItem().equals(AntragStatus.ABGELEHNT)) {
					setTeilzuschussState(false);
					comboAblehnungsbegruendung.setSelectedItem(AntragAblehnungsgrund.ABLEHNUNGSGRUND);
					comboAblehnungsbegruendung.setEnabled(true);
					cbTeilzuschuss.setSelected(false);
					spinner.setValue(0);


				} else if (comboEntscheidung.getSelectedItem().equals(AntragStatus.GENEHMIGT)) {
					teilZuschussBegruendung.setText("");
					setTeilzuschussState(true);
					comboAblehnungsbegruendung.setEnabled(false);

				} else if (comboEntscheidung.getSelectedItem().equals(AntragStatus.NICHTENTSCHIEDEN)) {
					comboAblehnungsbegruendung.setEnabled(false);
					setTeilzuschussState(false);
					cbTeilzuschuss.setSelected(false);
					spinner.setValue(0);

				}
			}
		});

		comboAblehnungsbegruendung = new JComboBox(AntragAblehnungsgrund.values());
		comboAblehnungsbegruendung.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				teilZuschussBegruendung.setText(((AntragAblehnungsgrund) comboAblehnungsbegruendung.getSelectedItem()).getBegruendung());
				teilZuschussBegruendung.setEnabled(true);
			}
		});

		comboAblehnungsbegruendung.setEnabled(false);

		tfEinkommen = new JTextField(3);
		tfHaerte = new JTextField(3);
		tfSummeHaerten = new JTextField(3);


		tfEinkommen.setHorizontalAlignment(JTextField.CENTER);
		tfHaerte.setHorizontalAlignment(JTextField.CENTER);
		tfSummeHaerten.setHorizontalAlignment(JTextField.CENTER);


        tfEinkommen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				updateSummeHaerten();
			}


		});

        tfEinkommen.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				updateSummeHaerten();

			}
		});

        tfEinkommen.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseExited(MouseEvent e) {
				updateSummeHaerten();
			}
		});

        tfHaerte.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				updateSummeHaerten();
			}
		});

        tfHaerte.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				updateSummeHaerten();
			}
		});
        tfHaerte.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				updateSummeHaerten();
			}
		});


		((AbstractDocument) tfEinkommen.getDocument()).setDocumentFilter(new DocumentSizeFilter(3, DocumentSizeFilter.NUMBER_PATTERN));
		((AbstractDocument) tfHaerte.getDocument()).setDocumentFilter(new DocumentSizeFilter(3, DocumentSizeFilter.NUMBER_PATTERN));
		((AbstractDocument) tfSummeHaerten.getDocument()).setDocumentFilter(new DocumentSizeFilter(3, DocumentSizeFilter.NUMBER_PATTERN));


        unterlagenTable = new UnterlagenTable(antragControl);
        unterlagenTable.setMinimumSize(unterlagenTable.getPreferredSize());
		unterlagenTable.setMaximumSize(unterlagenTable.getPreferredSize());

		unterlagenTable.getColumnModel().getColumn(1).setPreferredWidth(60);
		unterlagenTable.getColumnModel().getColumn(1).setMaxWidth(unterlagenTable.getColumnModel().getColumn(1).getPreferredWidth());
		JScrollPane unterlagenTableScrollPane = new JScrollPane(unterlagenTable);

		buttonUnterlagen = new JButton(new ActionUnterlagenHinzufuegen(antragControl));


		// Button zum Anfordern von Unterlagen
		anfordernButton = new JButton("Nachfragebrief");

		// Button zum Drucken einer Mahnung für fehlende Unterlagen
		mahnungButton = new JButton("Mahnbrief");

		// Button zum Drucken einer Mahnung für fehlende Unterlagen
		nachreichungButton = new JButton("Nachreichung");

        listeBegruendung = new JList();
        listeBegruendung.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listeBegruendung.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2 || e.getButton() == MouseEvent.BUTTON2 || e.getButton() == MouseEvent.BUTTON3) {
                    int idx = listeBegruendung.getSelectedIndex();
                    if (idx != -1) {
                        AntragHaerte haerte = (AntragHaerte) listeBegruendung.getModel().getElementAt(idx);
                        new DialogEditBegruendung(haerte);
                    }
                }
            }
        });


		listeBegruendung.setFixedCellWidth(102);
		listeBegruendung.setFixedCellHeight(25);
		listeBegruendung.setPreferredSize(new Dimension(129, 352));
		listeBegruendung.setBorder(new LineBorder(Color.LIGHT_GRAY));
        listeBegruendung.setCellRenderer(new MyCellRenderer());


		final SForm antragPanel = new SForm();



		// ***** InfoPanel erstellen *****

		labelAntragID = new JLabel("Antrag-ID: ");

        JLabel labelTitelAngelegt = new JLabel("Antrag angelegt:");
		labelTitelAngelegt.setForeground(Color.GRAY);
		JLabel labelTitelLastChange = new JLabel("Letzte Änderung:");
		labelTitelLastChange.setForeground(Color.GRAY);
		labelDatumAngelegt = new JLabel("");
		labelDatumAngelegt.setForeground(Color.GRAY);
		labelDatumLastChange = new JLabel("");
		labelDatumLastChange.setForeground(Color.GRAY);

		labelAntragID.setFont(Layout.INFO_FONT);
		labelTitelAngelegt.setFont(Layout.INFO_FONT);
		labelDatumAngelegt.setFont(Layout.INFO_FONT);
		labelTitelLastChange.setFont(Layout.INFO_FONT);
		labelDatumLastChange.setFont(Layout.INFO_FONT);

		SForm infoPanel = new SForm();
		Border infoBorder = BorderFactory.createLineBorder(Color.GRAY);
		Border infoMargin = new EmptyBorder(5, 5, 5, 5);
		infoPanel.setBorder(new CompoundBorder(infoBorder, infoMargin));
		infoPanel.setBackground(new Color(220, 220, 220));

		infoPanel.add(labelAntragID,           0,  1, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 5, 0));
		infoPanel.add(labelTitelAngelegt,      0,  2, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 0, 0, 0));
		infoPanel.add(labelDatumAngelegt,      0,  3, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 5, 0));
		infoPanel.add(labelTitelLastChange,    0,  4, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 0, 0, 0));
		infoPanel.add(labelDatumLastChange,    0,  5, 1, 1, 1.0, 1.0, 0, 18, new Insets(0, 0, 5, 0));

        infoPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new ProtokollControl(antragControl.getAntragID());
			}
		});

		// ***** ButtonPanel erstellen *****

		// Personendaten in Datenbank speichern
		speichernButton = new JButton("Speichern");
		speichernButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					saveAntrag();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Hier ist etwas beim Speichern schiefgelaufen");
				}
			}
		});

		// Personendaten im Formular zurücksetzen
		resetButton = new JButton("Zurücksetzen");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				antragControl.resetAntrag();
			}
		});

		delButton = new JButton("Löschen");
		delButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				// JA = 0, NEIN = 1
				int eingabe = JOptionPane.showConfirmDialog(null, "Bist Du sicher, dass Du diesen Antrag komplett löschen möchtest?",
						"Wirklich Löschen?", JOptionPane.YES_NO_OPTION);

				if (eingabe == 0) {
					antragControl.deleteAntrag();
					antragControl.showPerson();
				}
			}

		}
		);

		// Zurück zum Formular mit den Personendaten
		personButton = new JButton("Personendaten");


		// Berechnungszettel anzeigen
		berechnungsButton = new JButton("Berechnungszettel");

		buttonErstrechnung = new JButton("Erstberechnung");
		buttonZweitrechnung = new JButton("Zweitberechnung");
		lbDateErstrechnung = new JLabel();
		lbDateZweitrechnung = new JLabel();
		lbOKErstrechnung = new JLabel("OK");
		lbOKZweitrechnung = new JLabel("OK");
		lbOKErstrechnung.setForeground(Color.GREEN);
		lbOKZweitrechnung.setForeground(Color.GREEN);
		lbOKErstrechnung.setFont(lbOKErstrechnung.getFont().deriveFont(Font.BOLD));
		lbOKZweitrechnung.setFont(lbOKZweitrechnung.getFont().deriveFont(Font.BOLD));

		lbOKErstrechnung.setVisible(false);
		lbOKZweitrechnung.setVisible(false);


		panelErstrechnung = new JPanel();
		panelErstrechnung.setBorder(new EmptyBorder(0, 0, 0, 0));

		panelZweitrechnung = new JPanel();
		panelZweitrechnung.setBorder(new EmptyBorder(0, 0, 0, 0));


		JPanel containerPanel1 = new JPanel(new GridLayout(1, 1));
		JPanel containerPanel2 = new JPanel(new GridLayout(1, 1));

		containerPanel1.add(lbOKErstrechnung);
		containerPanel2.add(lbOKZweitrechnung);

		panelErstrechnung.add(containerPanel1);
		panelErstrechnung.add(buttonErstrechnung);
//		panelErstrechnung.add(lbDateErstrechnung);

		panelZweitrechnung.add(containerPanel2);
		panelZweitrechnung.add(buttonZweitrechnung);
//		panelZweitrechnung.add(lbDateZweitrechnung);


		// Berechnungszettel anzeigen

		// Button um Bescheid für einzelnen Antrag zu drucken
		printBescheidButton = new JButton("Bescheid drucken");
		printBescheidButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				int eingabe = 0;
				if (!cbBescheidVersandt.isSelected()) {
					// JA = 0, NEIN = 1
					eingabe = JOptionPane.showConfirmDialog(null, "Beim Drucken des Bescheides wird der Status auf VERSENDET gesetzt.",
							"Wirklich Drucken?", JOptionPane.YES_NO_OPTION);
				}
				if (eingabe == 0) {
					try {
						cbBescheidVersandt.setSelected(true);
						saveAntrag();
						antragControl.printAntrag();
					} catch (IOException ioe) {
						JOptionPane.showMessageDialog(null, "Bescheid konnte leider nicht gedruckt werden. \nEventuell Pfadeinstellungen überprüfen.");
						if (SettingsExternal.DEBUG)
							ioe.printStackTrace();
					} catch (NullPointerException npe) {
						JOptionPane.showMessageDialog(null, "Bescheid konnte leider nicht gedruckt werden. \nWahrscheinlich fehlen noch Werte, die im Bescheid stehen müssen. \nZ.B. Punktwert, oder der Bescheid muss noch entschieden werden.");
						if (SettingsExternal.DEBUG)
							npe.printStackTrace();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Template für Bescheid konnte leider nicht erstellt werden. \nWahrscheinlich fehlen noch Werte, die im Bescheid stehen müssen. \nZ.B. Punktwert, oder der Bescheid muss noch entschieden werden.");
						if (SettingsExternal.DEBUG)
							e.printStackTrace();
					}
				}
			}
		});

		// Button um Bescheid für einzelnen Antrag zu drucken
		printAZAButton = new JButton("AZA drucken");
		printAZAButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				int eingabe = 0;
				if (!cbAuszahlungAngeordnet.isSelected()) {
					// JA = 0, NEIN = 1
					eingabe = JOptionPane.showConfirmDialog(null, "Drucken setzt 'Auszahlung angeordnet'.",
							"Wirklich Drucken?", JOptionPane.YES_NO_OPTION);
				}
				if (eingabe == 0) {
					try {
						cbAuszahlungAngeordnet.setSelected(true);
						saveAntrag();
						antragControl.printAntragAZA();
					} catch (IOException ioe) {
						JOptionPane.showMessageDialog(null, "AZA konnte leider nicht gedruckt werden. \nEventuell Pfadeinstellungen überprüfen.");
						if (SettingsExternal.DEBUG)
							ioe.printStackTrace();
					} catch (NullPointerException npe) {
						JOptionPane.showMessageDialog(null, "AZA konnte leider nicht gedruckt werden. \nWahrscheinlich fehlen noch Werte, die im Bescheid stehen müssen. \nZ.B. Punktwert, oder der Bescheid muss noch entschieden werden.");
						if (SettingsExternal.DEBUG)
							npe.printStackTrace();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Template für AZA konnte leider nicht erstellt werden. \nWahrscheinlich fehlen noch Werte, die im Bescheid stehen müssen. \nZ.B. Punktwert, oder der Bescheid muss noch entschieden werden.");
						if (SettingsExternal.DEBUG)
							e.printStackTrace();
					}
				}
			}
		});


		SForm buttonPanel = new SForm();

		// Buttons hinzufügen
		int i=0;




		// titel
		antragPanel.add(headerPanel, 0, 0, 13, 1, 1.0, 1.0, 2, GridBagConstraints.WEST, new Insets(0, 0, 5, 0));
//		antragPanel.add(titelPanel, 0, 1, 13, 1, 1.0, 1.0, 2, GridBagConstraints.WEST, new Insets(0, 0, 0, 0));

		// Auflösungsversuch titelPanel

		int zeile1 = 3;
		int zeile1_3 = zeile1;

		int spalte1 = 0;
		//erste Spalte ist 4 Felder breit
		int spalte2 = 4;
		//zweite Spalte ist 3 Felder breit
		int spalte3 = 7;


		//erste Spalte
		antragPanel.add(lbAnnahme, spalte1, zeile1, 2, 1, 0.0, 0.0, 0, 17, new Insets(15, 7, 1, 2));
		antragPanel.add(cbErstsemester, spalte1, zeile1 + 1, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 11, 10, 5));
		antragPanel.add(cbRatenzahlung, spalte1 + 1, zeile1 + 1, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 11, 10, 5));
		antragPanel.add(cbNothilfe, spalte1 + 2, zeile1 + 1, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 11, 10, 5));
		antragPanel.add(lbNachfrage, spalte1, zeile1 + 2, 2, 1, 0.0, 0.0, 0, GridBagConstraints.NORTHWEST, new Insets(0, 7, 0, 2));
		antragPanel.add(unterlagenTableScrollPane, spalte1, zeile1 + 3, 5, 17, 0.0, 0.0, 0, GridBagConstraints.NORTHWEST, new Insets(-15, 4, 5, 4));
		antragPanel.add(buttonUnterlagen, spalte1, zeile1 + 19, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 10, 10, 5));
		antragPanel.add(anfordernButton, spalte1 + 1, zeile1 + 19, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 10, 10, 5));
		antragPanel.add(mahnungButton, spalte1 + 2, zeile1 + 19, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 10, 10, 5));
		antragPanel.add(nachreichungButton, spalte1 + 3, zeile1 + 19, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 10, 10, 5));

		//zweite Spalte
		antragPanel.add(lbBerechnung, spalte2, zeile1, 3, 1, 0.0, 0.0, 0, 17, new Insets(15, 7, 1, 2));
		antragPanel.add(cbKulanz, spalte2, zeile1 + 1, 3, 1, 0.0, 0.0, 0, 17, new Insets(5, 11, 10, 5));
		antragPanel.add(berechnungsButton, spalte2, zeile1 + 2, 3, 1, 0.0, 0.0, 0, 17, new Insets(5, 10, 10, 5));
		antragPanel.add(lbBegruendung, spalte2, zeile1 + 3, 3, 1, 0.0, 0.0, 0, 17, new Insets(15, 7, 1, 2));
		antragPanel.add(listeBegruendung, spalte2, zeile1 + 4, 3, 10, 0.0, 0.0, 0, 17, new Insets(5, 18, 10, 5));
		antragPanel.add(lbPunktzahlen, spalte2, zeile1 + 14, 3, 1, 0.0, 0.0, 0, 17, new Insets(15, 7, 1, 2));
		antragPanel.add(lbEinkommen, spalte2 + 2, zeile1 + 15, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
		antragPanel.add(tfEinkommen, spalte2 + 1, zeile1 + 15, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
		antragPanel.add(lbPlus, spalte2, zeile1 + 16, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 10, 0, 0));
		antragPanel.add(lbHaerte, spalte2 + 2, zeile1 + 16, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
		antragPanel.add(tfHaerte, spalte2 + 1, zeile1 + 16, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
		antragPanel.add(lbGleichzeichen, spalte2, zeile1 + 17, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 10, 0, 0));
		antragPanel.add(lbGleich, spalte2 + 2, zeile1 + 17, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
		antragPanel.add(tfSummeHaerten, spalte2 + 1, zeile1 + 17, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));


		//dritte Spalte
		antragPanel.add(lbEntscheidung, spalte3, zeile1_3, 2, 1, 0.0, 0.0, 0, 17, new Insets(15, 7, 1, 2));
		antragPanel.add(comboEntscheidung, spalte3, zeile1_3 + 1, 3, 1, 0.0, 0.0, 2, 17, new Insets(5, 10, 10, 5));
		antragPanel.add(comboAblehnungsbegruendung, spalte3, zeile1_3 + 2, 3, 1, 0.0, 0.0, 2, 17, new Insets(5, 10, 10, 5));
		antragPanel.add(scrollZuschuss, spalte3, zeile1_3 + 3, 3, 5, 0.0, 0.0, 0, 17, new Insets(5, 10, 10, 5));
		antragPanel.add(cbTeilzuschuss, spalte3, zeile1_3 + 9, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 11, 10, 5));
		antragPanel.add(spinner, spalte3 + 1, zeile1_3 + 9, 1, 1, 0.0, 0.0, 0, 17, new Insets(1, 0, 5, 2));
		antragPanel.add(lbZuschussmonate, spalte3 + 2, zeile1_3 + 9, 1, 1, 0.0, 0.0, 0, 17, new Insets(1, 0, 5, 2));
		antragPanel.add(panelErstrechnung, spalte3, zeile1_3 + 10, 3, 1, 0.0, 0.0, 0, 17, new Insets(5, 10, 10, 5));
		antragPanel.add(panelZweitrechnung, spalte3, zeile1_3 + 11, 3, 1, 0.0, 0.0, 0, 17, new Insets(5, 10, 10, 5));
		antragPanel.add(infoPanel, spalte3, zeile1_3 + 12, 3, 1, 0.0, 0.0, 2, 11, new Insets(5, 10, 10, 5));
		antragPanel.add(lbBescheid, spalte3, zeile1_3 + 13, 2, 1, 0.0, 0.0, 0, 17, new Insets(15, 7, 1, 2));
		antragPanel.add(printBescheidButton, spalte3, zeile1_3 + 14, 3, 1, 1.0, 1.0, 2, 17, new Insets(5, 10, 10, 5));
		antragPanel.add(cbBescheidVersandt, spalte3, zeile1_3 + 15, 3, 1, 0.0, 0.0, 0, 17, new Insets(5, 11, 10, 5));
		antragPanel.add(lbAuszahlung, spalte3, zeile1_3 + 16, 2, 1, 0.0, 0.0, 0, 17, new Insets(15, 7, 1, 2));
		antragPanel.add(cbManuellAuszahlen, spalte3, zeile1_3 + 17, 3, 1, 0.0, 0.0, 0, 17, new Insets(5, 11, 10, 5));
		antragPanel.add(printAZAButton, spalte3, zeile1_3 + 18, 3, 1, 1.0, 1.0, 2, 17, new Insets(5, 10, 10, 5));
		antragPanel.add(cbAuszahlungAngeordnet, spalte3, zeile1_3 + 19, 3, 1, 0.0, 0.0, 0, 17, new Insets(0, 11, 10, 5));


		//Button Panel



		buttonPanel.add(speichernButton, 0, i++, 1, 1, 0.0, 0.0, 2, 17, new Insets(0, 0, 25, 5));
		buttonPanel.add(resetButton, 0, i++, 1, 1, 0.0, 0.0, 2, 17, new Insets(5, 0, 25, 5));
		buttonPanel.add(delButton, 0, i++, 1, 1, 0.0, 0.0, 2, 17, new Insets(5, 0, 25, 5));

		buttonPanel.add(personButton, 0, i++, 1, 1, 0.0, 0.0, 2, 11, new Insets(25, 0, 15, 5));


		antragPanel.add(buttonPanel, 12, 2, 1, 12, 1.0, 1.0, 2, 17, new Insets(20, 20, 0, 0));

		// ***** Gesamtes Panel (Formular und Buttons) erstellen *****

		mainPanel = new SForm();

		mainPanel.add(antragPanel, 0, 0, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 5, 0, 0));
		mainPanel.add(new JPanel(), 1, 0, 1, 1, 1.0, 1.0, 0, 18, new Insets(5, 0, 0, 5));

		// Gesamtpanel in Scrollpane stecken
		mainScrollPane = new JScrollPane(mainPanel);

		// Scrollgeschwindigkeit festlegen
		mainScrollPane.getVerticalScrollBar().setUnitIncrement(Settings.SCROLL_UNIT);

		setKeyBindings();


	}

    private void updateSummeHaerten() {
		try {
			int einkommen = Integer.parseInt(tfEinkommen.getText());
			int haerte = Integer.parseInt(tfHaerte.getText());
			Integer summe = einkommen + haerte;
			if (!summe.toString().equals(tfSummeHaerten.getText())) {
				if (einkommen >= 0 && haerte >= 0) {
					tfSummeHaerten.setText(summe.toString());
				} else {
					tfSummeHaerten.setText(new Integer(0).toString());
				}
				if (antragControl.getAntrag().getPunkteHaerte() != summe) {
					speichernButton.setEnabled(true);
				}
			}
		} catch (NumberFormatException nfe) {
			//do nothing
		}
	}


    /**
	 * Liefert die ScrollPane, welche das AntragPanel enthält
	 * @return ScrollPane mit AntragPanel
	 */
	public JScrollPane getScrollPane() {
		return mainScrollPane;
	}

    public JTable getTable() {
        return this.unterlagenTable;
    }

    /**
     * Fügt Listener zur View hinzu (die Listener rufen Methoden im übergebenen
     * AntragControl auf)
	 * @param antragControl AntragControl
	 */
	public void addListener(final AntragControl antragControl) {
		
		personButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					saveAntrag();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Hier ist etwas beim Speichern schiefgelaufen");
				}
				antragControl.showPerson();
			}
		});
		
		
		// Button für Berechnugszettel geklickt
		berechnungsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				antragControl.showBerechnungszettel();
			}
		});
		
		
		buttonErstrechnung.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                if (null != UserConf.CURRENT_USER.getKuerzel() && UserConf.CURRENT_USER.getKuerzel().length() > 0) {
					int rueckgabewert = JOptionPane.OK_OPTION;
					if (lbOKErstrechnung.isVisible()) {
						rueckgabewert = JOptionPane.showConfirmDialog(null, "Es wurde bereits erstgerechnet. Wollen Sie den Button und Protokoll zurücksetzen?");
					}

					if (rueckgabewert == JOptionPane.OK_OPTION) {
						try {
							saveAntrag();
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(null, "Hier ist etwas beim Speichern schiefgelaufen");
							logger.warn(ex.getMessage());
						}
						if (lbOKErstrechnung.isVisible()) {
							antragControl.removeRechnung(Vorgangsart.ERSTRECHNUNG);
							lbOKErstrechnung.setVisible(false);
						} else {
							antragControl.setRechnung(Vorgangsart.ERSTRECHNUNG);
							lbOKErstrechnung.setVisible(true);
						}

					}
				} else {
					JOptionPane.showMessageDialog(null, "Benutzer hat noch kein Kürzel und kann deshalb nicht mit einem Kürzel zeichnen.");
				}
            }
		});
		
		
		buttonZweitrechnung.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                if (null != UserConf.CURRENT_USER.getKuerzel() && UserConf.CURRENT_USER.getKuerzel().length() > 0) {
					int rueckgabewert = JOptionPane.OK_OPTION;
					if (lbOKZweitrechnung.isVisible()) {
						rueckgabewert = JOptionPane.showConfirmDialog(null, "Es wurde bereits zweitgerechnet. Wollen Sie den Button und Protokoll zurücksetzen?");
					}

					if (rueckgabewert == JOptionPane.OK_OPTION) {
						try {
							saveAntrag();
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(null, "Hier ist etwas beim Speichern schiefgelaufen");
						}

						if (lbOKZweitrechnung.isVisible()) {
							antragControl.removeRechnung(Vorgangsart.ZWEITRECHNUNG);
							lbOKZweitrechnung.setVisible(false);
						} else {
							antragControl.setRechnung(Vorgangsart.ZWEITRECHNUNG);
							lbOKZweitrechnung.setVisible(true);
						}
					}
				} else {
                    JOptionPane.showMessageDialog(null, "Benutzer hat noch kein Kürzel und kann deshalb nicht Kürzel-Zeichnen.");
                }
            }
		});
		
		
		
		
		anfordernButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new DialogFrist(antragControl, Vorgangsart.NACHFRAGEBRIEF);
			}
		});
		
		
		
		mahnungButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new DialogFrist(antragControl, Vorgangsart.MAHNUNG);
			}
		});
		
		
		nachreichungButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new NachreichungControl(null, antragControl);
			}
		});
		
		

		
		
		// Listener für die Checkbox Teilzuschuss (blendet Spinner Zuschussmonate aus/ein)
		cbTeilzuschuss.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
                setTeilzuschussState(cbTeilzuschuss.isSelected());
            }
        });

		
		
		// ***** ChangeListener hinzufügen *****
		// Listener erkennt, wenn sich an den Eingabekomponenten etwas geändert hat
		
		// DocumetListener für Textfelder
		FormChangeListener formChangeListener = new FormChangeListener(antragControl);
		
		// ItemListener für Checkboxen, ComboBoxen, ToggleButton
		FormChangeListener2 formChangeListener2 = new FormChangeListener2(antragControl);
		
		// Textfelder mit Listener versehen, die erkennen, wenn es eine Änderung gegeben hat
		JTextField[] textfelder = new JTextField[] {tfEinkommen, tfHaerte, tfSummeHaerten};


        for (JTextField jtf : textfelder) {
			jtf.getDocument().addDocumentListener(formChangeListener);
		}

        teilZuschussBegruendung.getDocument().addDocumentListener(formChangeListener);
//        teilZuschussBegruendung.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyTyped(KeyEvent e) {
//				antragControl.setSaveStatus(true);
//			}
//		});
        cbErstsemester.addItemListener(formChangeListener2);
		cbTeilzuschuss.addItemListener(formChangeListener2);
		cbManuellAuszahlen.addItemListener(formChangeListener2);
		cbBescheidVersandt.addItemListener(formChangeListener2);
//		cbBescheidVersandt.addItemListener(new ItemListener() {
//			@Override
//			public void itemStateChanged(ItemEvent e) {
//				try {
//					saveAntrag();
//				} catch (Exception ex) {
//					JOptionPane.showMessageDialog(null, "Hier ist etwas schief gelaufen beim Speichern.");
//
//				}
//			}
//		});
		cbAuszahlungAngeordnet.addItemListener(formChangeListener2);
		cbNothilfe.addItemListener(formChangeListener2);
		cbRatenzahlung.addItemListener(formChangeListener2);
		cbKulanz.addItemListener(formChangeListener2);
		comboEntscheidung.addItemListener(formChangeListener2);
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if ((Integer) spinner.getValue() == 0)
					teilZuschussBegruendung.setText("");
				else if ((Integer) spinner.getValue() == 1)
					teilZuschussBegruendung.setText("Der Zuschuss wird an dich anteilig für einen Beitragszeitraum von einem Monat ausgezahlt. Damit ergibt sich ein Zahlungsbetrag in Höhe von ");
				else
					teilZuschussBegruendung.setText("Der Zuschuss wird an dich anteilig für einen Beitragszeitraum von " + spinner.getValue() + " Monaten ausgezahlt. Damit ergibt sich ein Zahlungsbetrag in Höhe von ");

				antragControl.setSaveStatus(true);
			}
		});


    }


    /**
	 * Setzt den Status für die Buttons Speichern un dZurücksetzen (reset)
	 * @param statusSpeichern Status Speichern
	 * @param statusReset Status reset
	 */
	public void setSaveStatus(boolean statusSpeichern, boolean statusReset) {
		speichernButton.setEnabled(statusSpeichern);
		resetButton.setEnabled(statusReset);
	}
	
	




	
	
	

	/**
     * Methode update vom Observer: fügt das Antrag-Object in das Panel ein
     */
	@Override
	public void update(Observable o, Object arg) {
		
		Antrag antrag = (Antrag)arg;
		AntragModel antragModel = (AntragModel) o;

		setTeilzuschussState(antrag.isTeilzuschuss());

//		cbTeilzuschuss.setSelected(antrag.isTeilzuschuss());

		lbTitelName.setText(antragModel.getPerson().getNachname() + ", " + antragModel.getPerson().getVorname());
		lbTitelMatrikelnr.setText(antragModel.getPerson().getMatrikelnr());
        try {

            lbSemester.setText(antragModel.getSemester().getSemesterKurzform());
            lbUniversitaet.setText(antragModel.getSemester().getUni().getUniName());
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null,"Bitte setzen Sie ein gültiges aktuelles Semester");
        }

        // ***** Daten für InfoPanel anzeigen*****
        labelAntragID.setText("Antrag-ID: " + antrag.getAntragID());
		labelDatumAngelegt.setText(antragModel.getDatumAngelegt());
		labelDatumLastChange.setText(antragModel.getDatumGeaendert());
				
		setNachreichungen(antragModel.uncheckedNachreichungen());


		
		// ***** Anzeige für Erst- und Zweitrechnung *****

		// Vorgänge aus DB holen (wenn keine vorhanden, dann gleich null)
		Vorgang vorgang1 = antragModel.getVorgangErstrechnung();
		Vorgang vorgang2 = antragModel.getVorgangZweitrechnung();

		// wenn bereits erstgerechnet wurde
		lbOKErstrechnung.setVisible(vorgang1 != null);


		// wenn bereits zweitgerechnet wurde
		lbOKZweitrechnung.setVisible(vorgang2 != null);

		// Antragsdaten ins Formular eintragen
		setAntrag(antrag);
				
	}

    /**
	 * @return Ausgewählte Zeile der Unterlagen
	 */
    public int getSelectedNachreichungFromTable() {
        return this.unterlagenTable.getSelectedRow();
    }

	/**
	 * Setzt die Anzahl eingegangener Nachreichungen
	 * @param anzahlNachreichungen Anzahl Nachreichungne
	 */
	public void setNachreichungen(int anzahlNachreichungen) {
		
		if(anzahlNachreichungen > 0) {
			String message = (anzahlNachreichungen == 1) ? " Nachreichung" : " Nachreichungen";
			anfordernButton.setEnabled(false);
			mahnungButton.setEnabled(false);
			nachreichungButton.setText(anzahlNachreichungen + message);
			nachreichungButton.setForeground(Color.RED);
		}
		else {
			anfordernButton.setEnabled(true);
			mahnungButton.setEnabled(true);
			nachreichungButton.setText("Nachreichung");
			nachreichungButton.setForeground(Color.BLACK);
		}
		
	}
	
	
	
	
	/**
	 * Setzt Antragsdaten ins Formular
	 * @param antrag Antrag
	 */
	private void setAntrag(Antrag antrag) {

		if (null == antrag.getAntragStatus())
			comboEntscheidung.setSelectedItem(AntragStatus.NICHTENTSCHIEDEN);
		else {
			comboEntscheidung.setSelectedItem(antrag.getAntragStatus());
			if (antrag.getAntragStatus().equals(AntragStatus.ABGELEHNT)) {
				comboAblehnungsbegruendung.setEnabled(true);
				if (antrag.getBegruendung().length() > 0) {
					for (AntragAblehnungsgrund aag : AntragAblehnungsgrund.values()) {
						if (antrag.getBegruendung().equals(aag.getBegruendung())) {
							comboAblehnungsbegruendung.setSelectedItem(aag);
							break;
						}
					}
				}
			}
		}

        if (null != antrag.getHaerteListe() && antrag.getHaerteListe().size() > 0) {
            listeBegruendung.setEnabled(true);
            listeBegruendung.setBackground(Color.WHITE);
            lbBegruendung.setForeground(Color.BLACK);

            List<AntragHaerte> angenommenAbgelehnt = new ArrayList<AntragHaerte>();
            for (AntragHaerte haerte : antrag.getHaerteListe()) {
                if (haerte.isAnerkannt() || haerte.isAbgelehnt()) {
                    angenommenAbgelehnt.add(haerte);
                }
            }
            AntragHaerte[] gruende = angenommenAbgelehnt.toArray(new AntragHaerte[angenommenAbgelehnt.size()]);
            listeBegruendung.setListData(gruende);

			if (gruende.length == 1)
				lbBegruendung.setText("Härtebegründung");
			else
				lbBegruendung.setText("Härtebegründungen");

        } else {

            listeBegruendung.setEnabled(false);
            listeBegruendung.setBackground(Color.LIGHT_GRAY);
            lbBegruendung.setForeground(Color.LIGHT_GRAY);


        }


		// Textfelder füllen
		tfEinkommen.setText("" + antrag.getPunkteEinkommen());
		tfHaerte.setText("" + antrag.getPunkteHaerte());
		updateSummeHaerten();

        setTeilzuschussState(antrag.isTeilzuschuss());


        teilZuschussBegruendung.setText(antrag.getBegruendung());

        if (teilZuschussBegruendung.getText().length() > 0)
            teilZuschussBegruendung.setEnabled(true);

		cbTeilzuschuss.setSelected(antrag.isTeilzuschuss());

		if (antrag.getAnzahlMonate() > 0)
			spinner.getModel().setValue(antrag.getAnzahlMonate());

        cbManuellAuszahlen.setSelected(antrag.isManAuszahlen() || antragControl.getPerson().isBarauszahler());

		cbAuszahlungAngeordnet.setSelected(antrag.isAuszahlung());
		cbNothilfe.setSelected(antrag.isNothilfe());
		cbRatenzahlung.setSelected(antrag.isRaten());
		cbKulanz.setSelected(antrag.isKulanz());
		cbErstsemester.setSelected(antrag.isErstsemester());
		cbBescheidVersandt.setSelected(antrag.isGesendet());



	}
	
	
	
	/**
	 * Spinner mit Anzahl Monaten ein-/ausblenden, je nach Status der Checkbox Teilzuschuss
	 * @param state anzeigen? ja/nein
	 */
    private void setTeilzuschussState(boolean state) {

        spinner.setEnabled(state);
		lbZuschussmonate.setForeground(state ? Color.BLACK : Color.LIGHT_GRAY);
		teilZuschussBegruendung.setEnabled(state);
		cbTeilzuschuss.setEnabled(state);

	}
	
	
	/**
	 * Speichert den Antrag über antragControl in der Datenbank
	 * @throws Exception Exception
	 */
	public void saveAntrag() throws Exception {

		AntragStatus antragStatus = (AntragStatus) comboEntscheidung.getSelectedItem();

        //wenn Entscheidung getroffen und vorher nicht entschieden, dann Vorgang anlegen
        AntragStatus antragStatusVorher = antragControl.getAntrag().getAntragStatus();


        if (!antragStatus.equals(antragStatusVorher)) {
            DBHandlerVorgaenge dbHandlerVorgaenge = new DBHandlerVorgaenge();
            dbHandlerVorgaenge.createVorgang(Vorgangsart.ENTSCHEIDUNG, antragControl.getAntragID());
        }

		int punkteEinkommen = 0;
		try {
			punkteEinkommen = Integer.valueOf(tfEinkommen.getText());
		} catch (NumberFormatException nfe) {
			logger.warn("PunkteEinkommen haben falsches Format. Setzt 0");
		}

		int punkteHaerte = 0;
		try {
			punkteHaerte = Integer.valueOf(tfHaerte.getText());
		} catch (NumberFormatException nfe) {
			logger.warn("PunkteHärte haben falsches Format. Setzt 0");
		}

        boolean teilzuschuss = cbTeilzuschuss.isSelected();
		int anzahlMonate = ((SpinnerNumberModel) spinner.getModel()).getNumber().intValue();

		boolean manAuszahlen = cbManuellAuszahlen.isSelected();
		boolean raten = cbRatenzahlung.isSelected();
		boolean nothilfe = cbNothilfe.isSelected();
		boolean kulanz = cbKulanz.isSelected();
		boolean erstsemester = cbErstsemester.isSelected();
		boolean auszahlung = cbAuszahlungAngeordnet.isSelected();
		boolean bescheidVersandt = cbBescheidVersandt.isSelected();

        String begruendung = teilZuschussBegruendung.getText();


		antragControl.saveAntrag(antragStatus, punkteEinkommen, punkteHaerte, teilzuschuss,
				anzahlMonate, raten, nothilfe, kulanz, erstsemester, manAuszahlen, auszahlung, begruendung, bescheidVersandt);
		
	}
	
	
	
	
	/**
	 * Setzt KeyBindings (Hotkeys für das Antragformular)
	 */
	@SuppressWarnings("serial")
	private void setKeyBindings() {
		
		
		// Taste F4: von Antrag in Berechnungszettel wechseln
        mainScrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F4"), "actionBerechnungszettel");
        mainScrollPane.getActionMap().put("actionBerechnungszettel", new AbstractAction() {
            @Override
			public void actionPerformed(ActionEvent arg0) {
				antragControl.showBerechnungszettel();
			}

		});

        mainScrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "actionPersonView");
        mainScrollPane.getActionMap().put("actionPersonView", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                antragControl.showPerson();
            }

        });

		mainScrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK), "actionSaveAntrag");
		mainScrollPane.getActionMap().put("actionSaveAntrag", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					saveAntrag();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Hier ist etwas beim Speichern schiefgelaufen");
				}

			}

		});


        // STRG + Z zurücksetzen
        mainScrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK), "actionAntragZuruecksetzen");
        mainScrollPane.getActionMap().put("actionAntragZuruecksetzen", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                antragControl.resetAntrag();
            }

        });
    }

	//zeigt den AntragHaerte-Begründungsnamentext in Abhängigkeit vom Status an
	private class MyCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list,
                                                      Object value, int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index,
                    isSelected, cellHasFocus);


            AntragHaerte ah = (AntragHaerte) value;


            setForeground(Color.RED);

            if (ah.isAnerkannt()) {
                setForeground(Color.BLUE);
            }

			// label.setFont(label.getFont().deriveFont(Font.BOLD));

            return (this);
        }
    }
}
