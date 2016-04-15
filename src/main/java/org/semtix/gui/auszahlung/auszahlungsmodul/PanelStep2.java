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


package org.semtix.gui.auszahlung.auszahlungsmodul;


import org.semtix.db.dao.Semester;
import org.semtix.shared.daten.StringHelper;
import org.semtix.shared.daten.enums.SemesterArt;
import org.semtix.shared.elements.control.CurrencyFilter;
import org.semtix.shared.elements.control.DocumentSizeFilter;
import org.semtix.shared.tablemodels.TableModelSemester;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Schritt 2 des Dialogs zur Zuschussberechnung. In diesem Panel können benötigte Daten zur
 * Zuschussberechnung eingegeben werden, wie z.B. Fonds, Ticketpreis, Sozialfondsbetrag). 
 * Außerdem wird eine Übersicht der Semester mit deren Zuschussdaten angezeigt.
 */
@SuppressWarnings("serial")
class PanelStep2
extends GenericPanelStep {

	private TableModelSemester tableModel;

	private JTable tableSemester;

	private JComboBox comboSemester;

	private JLabel lbStichtag, lbFonds, lbTicketpreis, lbSozialfonds, lbSemester;

	private JTextField tfStichtag, tfFonds, tfTicketpreis, tfSozialfonds;

	private NumberFormat nf;
    private JLabel erstiFonds;


    private String erstiStringS = "Ohne Erstifond in Höhe von  5%";
    private String erstiStringW = "Ohne Erstifond in Höhe von 10%";
	private String erstiWarnung;

    /**
     * {see GenericPanelStep}
	 *
	 * @param bModel     ModelAuszahlungsmodul
	 * @param titel      titel
	 * @param untertitel untertitel
	 */
	protected PanelStep2(ModelAuszahlungsmodul bModel, String titel, String untertitel) {
		super(bModel, titel, untertitel);
	}

	@Override
	protected void additionalInitStuff() {
		lbSemester = new JLabel("Werte eintragen für Semester:");
		lbStichtag = new JLabel("Stichtag:");
		lbFonds = new JLabel("Fonds:");
		lbTicketpreis = new JLabel("Ticketpreis:");
		lbSozialfonds = new JLabel("Sozialbeitrag:");

		erstiWarnung = "Bitte Erstifond vorher ausrechnen und abziehen";

		nf = NumberFormat.getNumberInstance(Locale.GERMAN);
		nf.setGroupingUsed(false);


		// aktuelles Datum, konvertiert zu String
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		String aktuellesDatum = df.format(new GregorianCalendar().getTime());

		tfStichtag = new JTextField(10);
		tfStichtag.setText(aktuellesDatum);
		((AbstractDocument) tfStichtag.getDocument()).setDocumentFilter(new DocumentSizeFilter(10, DocumentSizeFilter.DATE_PATTERN));

		// Textfeld Fonds, max. 6 Ziffern vor und 2 nach dem Komma
		tfFonds = new JTextField(10);
		((AbstractDocument) tfFonds.getDocument()).setDocumentFilter(new CurrencyFilter(6));

		tfFonds.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent var1) {
				if (tfFonds.getText().length() > 0) {
					if (((Semester) comboSemester.getSelectedItem()).getSemesterArt() == SemesterArt.SOMMER) {
						erstiFonds.setText(erstiStringS);
					} else {
						erstiFonds.setText(erstiStringW);
					}

					erstiFonds.setForeground(Color.BLACK);
				} else {
					erstiFonds.setText(erstiWarnung);
					erstiFonds.setForeground(Color.RED);
				}
			}
		});

		// Textfeld Ticketpreis, max. 4 Ziffern vor und 2 nach dem Komma
		tfTicketpreis = new JTextField(10);
		((AbstractDocument) tfTicketpreis.getDocument()).setDocumentFilter(new CurrencyFilter(4));

		// Textfeld Sozial-Fonds, max. 4 Ziffern vor und 2 nach dem Komma
		tfSozialfonds = new JTextField(10);
		((AbstractDocument) tfSozialfonds.getDocument()).setDocumentFilter(new CurrencyFilter(4));


		comboSemester = new JComboBox();

		comboSemester.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {

					Semester item = (Semester) comboSemester.getModel().getSelectedItem();

					if (null != item.getBeitragFonds()) {
						tfFonds.setText(nf.format(item.getBeitragFonds().setScale(2, BigDecimal.ROUND_HALF_DOWN)));
						if (item.getSemesterArt() == SemesterArt.SOMMER) {
							erstiFonds.setText(erstiStringS);
						} else {
							erstiFonds.setText(erstiStringW);
						}

						erstiFonds.setForeground(Color.BLACK);
					} else {
						tfFonds.setText("");
						erstiFonds.setText(erstiWarnung);
						erstiFonds.setForeground(Color.RED);
					}
					if (null != item.getSozialfonds())
                        tfSozialfonds.setText(nf.format(item.getSozialfonds().setScale(2, BigDecimal.ROUND_HALF_DOWN)));
					else
						tfSozialfonds.setText("");

                    if (null != item.getBeitragTicket())
                        tfTicketpreis.setText(nf.format(item.getBeitragTicket().setScale(2, BigDecimal.ROUND_HALF_DOWN)));
					else
						tfTicketpreis.setText("");
                }
			}
		});


		tableModel = new TableModelSemester();

		tableSemester = new JTable(tableModel);

		// nur einzelne Tabellenzeilen können selektiert werden
		tableSemester.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// die Spalten der Tabelle können nicht vertauscht werden
		tableSemester.getTableHeader().setReorderingAllowed(false);

		// die Spaltenbreite kann nicht verändert/verschoben werden
		tableSemester.getTableHeader().setResizingAllowed(false);

		tableSemester.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {

				int semsterID = tableModel.getSemesterByRow(tableSemester.getSelectedRow()).getSemesterID();

				for (int i = 0; i < comboSemester.getModel().getSize(); i++) {
					Semester s = (Semester) comboSemester.getModel().getElementAt(i);
					if (s.getSemesterID() == semsterID) {
						comboSemester.getModel().setSelectedItem(s);
						//comboSemester.setSelectedIndex(i);
					}
				}

			}
		});

		JScrollPane scroller = new JScrollPane(tableSemester);
		scroller.setPreferredSize(new Dimension(450, 120));
		scroller.setMinimumSize(scroller.getPreferredSize());

		erstiFonds = new JLabel(erstiWarnung);
		erstiFonds.setForeground(Color.RED);


		// Komponenten zum GridBagLayout hinzufügen
		formular.add(scroller, 0, 2, 5, 1, 0.0, 0.0, 2, 18, new Insets(0, 0, 10, 0));
		formular.add(lbSemester, 0, 3, 3, 1, 0.0, 0.0, 0, 17, new Insets(10, 0, 0, 0));
		formular.add(comboSemester, 3, 3, 2, 1, 0.0, 0.0, 0, 18, new Insets(10, 0, 0, 0));

		formular.add(lbStichtag, 0, 4, 1, 1, 0.0, 0.0, 0, 17, new Insets(10, 0, 0, 0));
		formular.add(tfStichtag, 1, 4, 1, 1, 0.0, 0.0, 0, 18, new Insets(10, 0, 0, 0));
		formular.add(lbFonds, 0, 5, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
		formular.add(tfFonds, 1, 5, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 0, 0));
        formular.add(erstiFonds, 0, 6, 5, 1, 0.0, 0.0, 0, 17, new Insets(10, 0, 0, 0));

		formular.add(new JSeparator(JSeparator.VERTICAL), 2, 4, 1, 2, 0.0, 0.0, GridBagConstraints.VERTICAL, 18, new Insets(10, 10, 0, 10));

		formular.add(lbTicketpreis, 3, 4, 1, 1, 0.0, 0.0, 0, 17, new Insets(10, 0, 0, 0));
		formular.add(tfTicketpreis, 4, 4, 1, 1, 0.0, 0.0, 0, 18, new Insets(10, 0, 0, 0));
		formular.add(lbSozialfonds, 3, 5, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
		formular.add(tfSozialfonds, 4, 5, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 0, 0));
        formular.add(new JPanel(), 0, 7, 5, 1, 1.0, 1.0, 2, 18, new Insets(0, 0, 0, 0));

	}


	/**
	 * Selektiertes Semester aus ComboBox auslesen
	 *
	 * @return selektiertes Semester
	 */
	public Semester getSemester() {
		return (Semester) comboSemester.getSelectedItem();
	}


	/**
	 * Liefert Datum, der im Textfeld Stichtag eingegeben wurde.
	 *
	 * @return Datum aus Textfeld Stichtag
	 */
	public GregorianCalendar getStichtag() {

		// Stichtag als GregorianCalendar anlegen
		GregorianCalendar stichtag = null;

		// falls Textfeld nicht leer, String in GregorianCalendar umwandeln
		if (!tfStichtag.getText().trim().equals(""))
			stichtag = StringHelper.convertStringToDate(tfStichtag.getText());

		return stichtag;

	}


	/**
	 * Liefert Wert, der im Textfeld Fonds eingegeben wurde.
	 *
	 * @return Wert aus Textfeld Fonds
	 */
	public String getFonds() {
		return tfFonds.getText().trim();
	}


	/**
	 * Liefert Wert, der im Textfeld Ticketpreis eingegeben wurde.
	 *
	 * @return Wert aus Textfeld Ticketpreis
	 */
	public String getTicketpreis() {
		return tfTicketpreis.getText().trim();
	}


	/**
	 * Liefert Wert, der im Textfeld Sozialfonds eingegeben wurde.
	 *
	 * @return Wert aus Textfeld Sozialfonds
	 */
	public String getSozialfonds() {
		return tfSozialfonds.getText().trim();
	}


	/**
	 * Model mit Semesterdaten in ComboBox setzen
	 *
	 * @param model Model der ComboBox für die Semestereinträge
	 */
	public void setComboBoxSemester(ComboBoxModel model) {
		comboSemester.setModel(model);
	}


	/**
	 * Wenn es keine verfügbaren Semester zur Auswahl für die Zuschussberechnung gibt,
	 * muss das angezeigt und es müssen bestimmte Komponenten deaktiviert werden.
	 */
	public void setNullSemester() {

		// Text in ComboBox anzeigen und deaktivieren
		comboSemester.addItem("- kein Semester verfügbar -");
		comboSemester.setEnabled(false);

		// Textfelder deaktivieren
		tfStichtag.setEnabled(false);
		tfFonds.setEnabled(false);
		tfTicketpreis.setEnabled(false);
		tfSozialfonds.setEnabled(false);

	}


	/**
	 * Die Texteingabefelder müssen auf Gültigkeit der eingegebenen Werte überprüft werden.
	 *
	 * @return Flag ob Überprüfung erfolgreich war
	 */
	@Override
	protected boolean checkTransition () {

		bModel.setSemester(getSemester());


		// Flag ob Überprüfung erfolgreich war
		boolean checkFlag = true;

		// vor Überprüfung Beschriftung auf Standardfarbe setzen
		lbStichtag.setForeground(Color.BLACK);
		lbFonds.setForeground(Color.BLACK);
		lbTicketpreis.setForeground(Color.BLACK);
		lbSozialfonds.setForeground(Color.BLACK);
		lbSemester.setForeground(Color.BLACK);

		// Text für mögliche Fehlermeldungen (HTML-Format)
		String message = "<html>";


		// Textfeld Stichtag darf nicht leer bleiben
		if (tfStichtag.getText().trim().equals("")) {
            message += "Es muss ein Datum für den Stichtag angegeben werden<br>";
            lbStichtag.setForeground(Color.RED);
			checkFlag = false;
		} else {

			// Datum auf Gültigkeit überprüfen
			String datumString = tfStichtag.getText().trim();
			DateFormat parser = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMANY);
			try {
				parser.setLenient(false);
				parser.parse(datumString);
			} catch (Exception e) {
                message += "'" + datumString + "' ist kein gültiges Datum<br>";
                lbStichtag.setForeground(Color.RED);
				checkFlag = false;
			}

		}


		// Textfeld Fonds darf nicht leer bleiben
		if (tfFonds.getText().trim().equals("")) {
            message += "Es muss ein Betrag für den Fonds angegeben werden<br>";
            lbFonds.setForeground(Color.RED);
			checkFlag = false;
		}


		// Textfeld Ticketpreis darf nicht leer bleiben
		if (tfTicketpreis.getText().trim().equals("")) {
            message += "Es muss ein Betrag für den Ticketpreis angegeben werden<br>";
            lbTicketpreis.setForeground(Color.RED);
			checkFlag = false;
		}


		// Textfeld Sozialfonds darf nicht leer bleiben
		if (tfSozialfonds.getText().trim().equals("")) {
            message += "Es muss ein Betrag für den Sozial-Fonds angegeben werden";
            lbSozialfonds.setForeground(Color.RED);
			checkFlag = false;
		}


		// Ende der Fehlermeldung (HTML-Format)
		message += "</html>";


		// wenn Überprüfungs-Flag = false: Fehlermeldung anzeigen
		if (!checkFlag) {
            JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
        } else {
		//Vergleichen, ob sich was geändert hat:
			try {

                if (null != getSemester().getBeitragFonds() && null != getSemester().getSozialfonds() && null != getSemester().getBeitragTicket()) {
                    if ((
                            nf.parse(tfFonds.getText()).doubleValue() == getSemester().getBeitragFonds().doubleValue()
                    ) && (
                            nf.parse(tfSozialfonds.getText()).doubleValue() == getSemester().getSozialfonds().doubleValue()
                    ) &&
                            nf.parse(tfTicketpreis.getText()).doubleValue() == getSemester().getBeitragTicket().doubleValue()
                            ) {
                        //no changes
                    }
                } else {
					//Wenn keine Änderungen, dann kein Log-Eintrag
					logTextArea.logText("(2) Semester ausgefüllt");
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

		if (checkFlag) {
			bModel.setStichtag(getStichtag());
			bModel.setFonds(getFonds());
			bModel.setTicketpreis(getTicketpreis());
			bModel.setSozialfonds(getSozialfonds());

//			bModel.updateSemester();
			tableModel.fillList();
		}


		return checkFlag;

	}


	/**
	 * Auswahlliste der für Zuschussberechnungen verfügbaren Semester aktualisieren.
	 */
	@Override
	public void updatePanel() {

		List<Semester> semester = bModel.getSemesterListe();

		// Model für ComboBoxSemester in Panel1 setzen
		// wenn mind. 1 Semester in Liste (also nicht leer)
		if (semester.size() > 0) {

			// Model erstellen
			DefaultComboBoxModel modelSemester = new DefaultComboBoxModel();
			for (Semester s : semester)
				modelSemester.addElement(s);

			// Der ComboBox wird das Model zugewiesen
			setComboBoxSemester(modelSemester);

		}
		// kein Semester in Auswahlliste vorhanden
		else {
			// visuelle Anzeige, dass kein Semester verfügbar
			setNullSemester();
		}


		refreshLog();
	}

}