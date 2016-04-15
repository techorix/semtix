/*

  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
         Semesterticketbüro der Humboldt-Universität Berlin

  Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package org.semtix.gui.person.personensuche;

import org.semtix.config.UniConf;
import org.semtix.db.DBHandlerPerson;
import org.semtix.db.dao.Person;
import org.semtix.gui.MainControl;
import org.semtix.shared.actions.ActionCloseDialog;
import org.semtix.shared.daten.StringHelper;
import org.semtix.shared.elements.FormularTabReihenfolge;
import org.semtix.shared.elements.SForm;
import org.semtix.shared.elements.control.DocumentSizeFilter;
import org.semtix.shared.elements.control.MatrikelFilter;
import org.semtix.shared.tablemodels.TableModelPersonenListe;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

/**
* Dialog zur Suche und Filterung von Personeneinträgen.
*/
@SuppressWarnings("serial")
public class DialogPersonSuche
		extends JDialog {

	private MainControl mainControl;

	private JPanel mainPanel;

	private JLabel labelUniversitaet, labelAnzahl;
	private JTextField tfVorname, tfNachname, tfMatrikelnummer;

	private JTable tableView;
	private JScrollPane tableScroller;

	private TableRowSorter<TableModel> sorter;

	private TableModelPersonenListe tableModel;


	public DialogPersonSuche(MainControl mainControl) {

		this.mainControl = mainControl;

		setTitle("Personen suchen");

		// Icon für Dialog setzen
		URL filename = getClass().getResource("/images/app_logo_16x16.png");
		setIconImage(Toolkit.getDefaultToolkit().getImage(filename));

		setModal(true);
		setResizable(false);

		// MainPanel mit Abstand zum Dialogrand erstellen
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "actionEscapeDialog");
		mainPanel.getActionMap().put("actionEscapeDialog", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}

		});


		// Panel für Buttons "Zurücksetzen" und "Dialog schließen"
		JPanel buttonPanel = new JPanel();

		// Button zum Zurücksetzten der Filterung
		JButton resetButton = new JButton("Zurücksetzen");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});

		// Button zum Schließen des Dialogs
		JButton abbrechenButton = new JButton(new ActionCloseDialog(this, "Beenden"));

		buttonPanel.add(resetButton);
		buttonPanel.add(abbrechenButton);

		// Button zum Anlegen einer neuen Person
		JButton neuButton = new JButton("neu");
		neuButton.setPreferredSize(new Dimension(80, 80));

		neuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Werte für Person überprüfen, wenn "neu"-Button geklickt wurde
				checkNewPerson();
			}
		});

		// Label für die Beschriftung der Textfelder
		JLabel labelVorname = new JLabel("Vorname");
		JLabel labelNachname = new JLabel("Nachname");
		JLabel labelMatrikelnummer = new JLabel("Matrikelnummer");

        // Label für Anzeige der aktuell ausgewählten Universität
        labelUniversitaet = new JLabel(UniConf.aktuelleUni.toString());

		// Label für Anzeige der Anzahl der Datensätze in der Tabelle
		// (angezeigte Anzahl und Gesamtanzahl)
		labelAnzahl = new JLabel();

		// KeyListener für die 3 Textfelder (wenn Fokus auf Textfelder)
		KeyListener keyListener = new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				// Taste DOWN (Pfeil nach unten) gedrückt...
				if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
					// Fokus auf Tabelle setzen, damit über die Pfeiltasten in der Tabelle navigiert werden kann.
                    // nur wenn eine Zeile selektiert ist UND es mehr als 1 Zeile in der Tabelle hat
                    if (tableView.getSelectedRow() != -1 && getSorter().getViewRowCount() > 1) {
						tableView.requestFocus();
						tableView.setRowSelectionInterval(1, 1);
					}
				}
			}
		};

		// Textfeld Filtereingabe für Vorname
		tfVorname = new JTextField(20);
		tfVorname.getDocument().addDocumentListener(new PersonDocumentListener(this));
		tfVorname.addKeyListener(keyListener);
		((AbstractDocument) tfVorname.getDocument()).setDocumentFilter(new DocumentSizeFilter(50, DocumentSizeFilter.FULLTEXT_PATTERN));

		// Textfeld Filtereingabe für Nachname
		tfNachname = new JTextField(20);
		tfNachname.getDocument().addDocumentListener(new PersonDocumentListener(this));
		tfNachname.addKeyListener(keyListener);
		((AbstractDocument) tfNachname.getDocument()).setDocumentFilter(new DocumentSizeFilter(50, DocumentSizeFilter.FULLTEXT_PATTERN));

		// Textfeld Filtereingabe für Matrikelnummer
		tfMatrikelnummer = new JTextField(20);
		tfMatrikelnummer.getDocument().addDocumentListener(new PersonDocumentListener(this));
		tfMatrikelnummer.addKeyListener(keyListener);
		((AbstractDocument) tfMatrikelnummer.getDocument()).setDocumentFilter(new MatrikelFilter(10));

		// TableModel für Personendaten
		tableModel = new TableModelPersonenListe(new DBHandlerPerson().getPersonenListe(UniConf.aktuelleUni), false);

		// Tabelle zur Anzeige der Personendaten aus TableModel
		tableView = new JTable(tableModel);

		for (String s : tableModel.getColumnsIShouldHide(new String[]{"Name", "Vorname","Matrikelnummer"})) {
			tableView.removeColumn(tableView.getColumn(s));
		}

		// RowSorter für Tabelle setzen
		sorter = new TableRowSorter<TableModel>(tableModel);
		tableView.setRowSorter(sorter);
		// nur einzelne Tabellenzeilen können selektiert werden
		tableView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Breite der Tabellenspalte für Matrikelnummer setzen
		tableView.getColumnModel().getColumn(2).setPreferredWidth(20);

		// die Spalten der Tabelle können nicht vertauscht werden
		tableView.getTableHeader().setReorderingAllowed(false);

		// die Spaltenbreite kann nicht verändert/verschoben werden
		tableView.getTableHeader().setResizingAllowed(true);


		// MouseListener auf Tabelle...
		tableView.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// Doppelklick auf Tabellenzeile...
				if (e.getClickCount() == 2) {
					// Person auswählen und im Formular anzeigen
					selectPerson(tableModel.getPersonID(tableView.convertRowIndexToModel(tableView.getSelectedRow())));
				}
			}
		});

		// KeyListener auf Tabelle hinzufügen
		tableView.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				// ENTER-Taste wird gedrückt
				if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
					// Person auswählen und im Formular anzeigen
					selectPerson(tableModel.getPersonID(tableView.convertRowIndexToModel(tableView.getSelectedRow())));
				}
			}
		});

		// Tabelle in Scrollpane setzen
		tableScroller = new JScrollPane(tableView);
		tableScroller.setPreferredSize(new Dimension(450, 250));

		// 1. Zeile in Tabelle selektieren und zu oberstem Tabelleneintrag scrollen
		setSelectedRow();


		SForm formular = new SForm();
		formular.add(labelNachname, 0, 0, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 5));
		formular.add(labelVorname, 0, 1, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 5));
		formular.add(labelMatrikelnummer, 0, 2, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 10, 5));
		formular.add(labelUniversitaet, 0, 4, 3, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
		formular.add(labelAnzahl, 0, 5, 3, 1, 1.0, 1.0, 0, 17, new Insets(0, 0, 10, 0));
		formular.add(tfNachname, 1, 0, 1, 1, 0.0, 0.0, 2, 17, new Insets(0, 0, 0, 0));
		formular.add(tfVorname, 1, 1, 1, 1, 0.0, 0.0, 2, 17, new Insets(0, 0, 0, 0));
		formular.add(tfMatrikelnummer, 1, 2, 1, 1, 0.0, 0.0, 2, 17, new Insets(0, 0, 10, 0));
		formular.add(tableScroller, 0, 3, 3, 1, 0.0, 0.0, 1, 11, new Insets(0, 0, 10, 0));
		formular.add(neuButton, 2, 0, 1, 3, 0.0, 0.0, 0, 13, new Insets(0, 0, 0, 0));

		// Formular und Buttons zum MainPanel hinzufügen
		mainPanel.add(formular, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		// MainPanel dem Dialog hinzufüügen
		add(mainPanel);

		// Anzahl der angezeigten Personen akzualisieren
		updateAnzahl();

		// Nach Nachnamen sortieren
		sorter.toggleSortOrder(tableModel.getColumnIndex("Name"));

		// ***** Tab-Reihenfolge setzen *****
		JComponent[] order = new JComponent[]{tfNachname, tfVorname, tfMatrikelnummer};
		setFocusTraversalPolicy(new FormularTabReihenfolge(order));
		setFocusCycleRoot(true);

		setKeyBindings();

		pack();

		// Frame auf dem Bildschirm zentrieren
		setLocationRelativeTo(getParent());

		setVisible(true);

	}


	// Schliesst Dialog und übernimmt die Daten der neuen Person ins Formular.
	private void addNewPerson() {

		// Holt die Werte aus den Texteingabefeldern
		String nachname = tfNachname.getText().trim();
		String vorname = tfVorname.getText().trim();
		String matrikelnr = tfMatrikelnummer.getText().trim();

		if (nachname.length() > 0 && vorname.length() > 0 && matrikelnr.length() > 0) {
			mainControl.addTab(nachname, vorname, matrikelnr);
			// Dialog schließen
			dispose();
		}
		// Neuen Tab mit Daten der neuen Person öffnen
		else
			JOptionPane.showMessageDialog(null, "Alle drei Felder müssen ausgefüllt sein.");


	}


	// Schliesst Dialog und zeigt ausgewählte Person in Formular an.
	private void selectPerson(int personID) {

		// Neuen Tab mit Daten der ausgewählten Person öffnen
		mainControl.addTab(personID);

		// Dialog schließen
		dispose();

	}


	// Immer die erste Zeile der Tablle wird selektiert
	public void setSelectedRow() {

		// Wenn mindestens 1 Zeile angezeigt wird
		if (this.getSorter().getViewRowCount() > 0) {
			// 1. Zeile in Tabelle selektieren
			tableView.setRowSelectionInterval(0, 0);
			// auf obersten Tabelleneintrag scrollen
			tableScroller.getVerticalScrollBar().setValue(0);
		}

	}


	public TableRowSorter<TableModel> getSorter() {
		return sorter;
	}

	public String getTextVorname() {
		return tfVorname.getText();
	}

	public String getTextNachname() {
		return tfNachname.getText();
	}

	public String getTextMatrikelnummer() {
		return tfMatrikelnummer.getText();
	}

	// Anzahl der angezeigten Einträge aktualisieren
	public void updateAnzahl() {
		labelAnzahl.setText("Anzahl: " + this.getSorter().getViewRowCount() + " von " +
				this.getSorter().getModelRowCount());
	}


	// Überprüfung bei neuen Personen
	private void checkNewPerson() {

		// Geht Überprüfung schrittweise durch und bricht ab, wenn eine Überprüfung fehl schlägt.
		// 1.) Vor- und Nachname dürfen nicht leer sein
		// 2.) Wenn Matrikelnummer eingetragen, dann Länge nicht kleiner als 3
		// 3.) Matrikelnummer darf nicht schon in Datenbank vorhanden sein
		// 4.) Wenn Name schon in DB, Abfrage ob Person anlegen oder zu Person springen
		// 5.) Wenn alle Überprüfungen ohne Abbruch durchlaufen sind, wird neue Person im Formular angezeigt.

        // Holt die Werte aus den Texteingabefeldern
        String temp_nachname = tfNachname.getText().trim();
		String temp_vorname = tfVorname.getText().trim();
		String temp_matrikelnr = tfMatrikelnummer.getText().trim();


        int personIDByMatrikelNummer = 0;
        int personIDByName = 0;
        // Listen mit schon in der DB vorhandenen identischen Namen holen (getrennt nach Uni)
        for (Person p : tableModel.getPersonenListe()) {
            if (personIDByMatrikelNummer == 0 && temp_matrikelnr.equals(p.getMatrikelnr())) {
                personIDByMatrikelNummer = p.getPersonID();
            }

            if (personIDByName == 0 && StringHelper.removeDiacriticalMarks(temp_vorname).
                    equalsIgnoreCase(StringHelper.removeDiacriticalMarks(p.getVorname())) &&
                    StringHelper.removeDiacriticalMarks(temp_nachname).
                            equalsIgnoreCase(StringHelper.removeDiacriticalMarks(p.getNachname()))) {
                personIDByName = p.getPersonID();
            }

            if (personIDByMatrikelNummer != 0 && personIDByName != 0) {
                break;
            }
        }

		// 1.) ***** Vor- und Nachname dürfen nicht leer sein *****

		if (temp_nachname.equals("") || temp_vorname.equals("") || temp_matrikelnr.equals("")) {
			String message = "Um neue Personen anzulegen, \n müssen alle Felder \n ausgefüllt werden.";
            JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
        }

		// 2.) ***** Wenn Matrikelnummer eingetragen, dann Länge nicht kleiner als 3 *****

		else if (temp_matrikelnr.length() < 3) {
			String message = "Die Matrikelnummer muss \n mindestens 3 Ziffern haben. \n (max. 10 Ziffern)";
            JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
        }

		// 3.) ***** Matrikelnummer darf nicht schon in Datenbank vorhanden sein *****

		else if (personIDByMatrikelNummer > 0) {

			Object[] options = {"neue Suche", "Person anzeigen"};

			String message = "<html>Matrikelnummer " + temp_matrikelnr +
					" (" + UniConf.aktuelleUni.getUniName() + ") ist schon vorhanden.<br><br></html>";

			int selected = JOptionPane.showOptionDialog(null, message, "Fehler", JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE, null, options, options[1]);

			if (selected == 1) {
				selectPerson(personIDByMatrikelNummer);
			}


		}


		// 4.) ***** Wenn Name schon in DB, Abfrage ob Person anlegen oder zu Person springen *****

		// Prüft, ob neu einzutragender Name schon in Datenbank vorhanden ist
		// Nur, wenn in DB Personen gefunden wurden (ansonsten ist die Liste leer)
		else if (personIDByName > 0) {

			Object[] options = {"neue Suche", "Person anzeigen"};

			String message = "<html>Name " + temp_vorname + " " + temp_nachname +
					" (" + UniConf.aktuelleUni.getUniName() + ") ist schon vorhanden.<br><br></html>";

			int selected = JOptionPane.showOptionDialog(null, message, "Fehler", JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE, null, options, options[1]);

			if (selected == 1) {
				selectPerson(personIDByName);
			}

		}

		// 5.) ***** Wechselt zum Personen-Formular mit den eingegebenen Werten für eine neue Person *****
		else {
			addNewPerson();
		}

	}


	// Button "zurücksetzen" wurde geklickt
	private void reset() {

		// Textfelder leeren
		tfNachname.setText("");
		tfVorname.setText("");
		tfMatrikelnummer.setText("");

		// Cursor auf Textfeld Nachname setzen
		tfNachname.requestFocus();

		// TableRowSorter wird zurückgesetzt (reset)
		sorter.modelStructureChanged();

		// 1. Zeile in Tabelle selektieren
		setSelectedRow();

	}


	// KeyBindings (Hotkeys für Dialog) setzen
	@SuppressWarnings("serial")
	private void setKeyBindings() {

		// Reaktionen auf ENTER-Taste
		mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "actionSelectPerson");
		mainPanel.getActionMap().put("actionSelectPerson", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (tableView.getSelectedRow() != -1) {
					// Person auswählen und im Formular anzeigen
					selectPerson(tableModel.getPersonID(tableView.convertRowIndexToModel(tableView.getSelectedRow())));
				} else {
					checkNewPerson();
				}

			}

		});

	}
}
