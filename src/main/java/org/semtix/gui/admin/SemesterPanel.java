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

package org.semtix.gui.admin;

import org.semtix.config.SettingsExternal;
import org.semtix.db.DBHandlerAntrag;
import org.semtix.db.DBHandlerSemester;
import org.semtix.db.dao.Antrag;
import org.semtix.db.dao.Semester;
import org.semtix.shared.daten.enums.AntragStatus;
import org.semtix.shared.daten.enums.Uni;
import org.semtix.shared.elements.SForm;
import org.semtix.shared.tablemodels.TableModelSemesterStatistik;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;



/**
 * Dieses Panel der AdminTools dient ausschleßlich der Anzeige der Daten der bisher eingetragenen Semester.
 */
@SuppressWarnings("serial")
public class SemesterPanel
extends JPanel {
	
	private JLabel textSemesterName, textSemesterJahr, textSemesterKurz, textSemesterBeginn,
		textAnzahlGesamt, textAnzahlBewilligt, textAnzahlAbgelehnt, textAnzahlNichtEntschieden; 
	
	private JTextField tfStichtag, tfTicketbeitrag, tfSozialfondsbeitrag, 
		tfAntraegeBewilligt, tfVollzuschuss, tfFonds, tfPunkteVergeben, tfPunktwert;
	
	private JComboBox comboUni;

    private JButton resetButton, deleteButton;

	private TableModelSemesterStatistik tableModel;
	private JTable datenTabelle;
	private TableRowSorter<TableModel> sorter;

    private int semesterID;

    /**
	 * Erstellt ein neues Panel mit Anzeige der eingetragenen Semester
	 */
	public SemesterPanel() {

		setLayout(new BorderLayout());
		
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
	    // ComboBox mit Liste der Universitäten
		comboUni = new JComboBox(Uni.values());
		
		ItemListener listenerUniversitaeten = new ItemListener() { 		
			  public void itemStateChanged(ItemEvent e) { 
				  if (e.getStateChange() == ItemEvent.SELECTED) {
					  Uni temp_universitaet = (Uni)comboUni.getSelectedItem();
					  filter(temp_universitaet.getID());
					  clearFields();
					  datenTabelle.clearSelection();
					  resetButton.setEnabled(false);
                      deleteButton.setEnabled(false);
                  }
			  } 
			}; 
		
		comboUni.addItemListener(listenerUniversitaeten);

		
		// Beschriftungen erstellen
		JLabel labelUniversitaet = new JLabel("Universität auswählen");
		JLabel labelSemesterArt = new JLabel("Semesterart");
		JLabel labelSemesterJahr = new JLabel("Jahr");
		JLabel labelSemesterKurz = new JLabel("Kurzbezeichnung");
		JLabel labelSemesterbeginn = new JLabel("Semesterbeginn");
		
		JLabel lbAntraegeGesamt = new JLabel("Anträge gesamt");
		JLabel lbAntraegeBewilligt = new JLabel("bewilligt");
		JLabel lbAntraegeAbgelehnt = new JLabel("abgelehnt");
		JLabel lbAntraegeNichtEntschieden = new JLabel("nicht entschieden");
		
		JLabel labelStichtag = new JLabel("Stichtag der Fondsauschüttung");
		JLabel labelTicketbeitrag = new JLabel("Ticket-Beitrag");
		JLabel labelSozialfondsbeitrag = new JLabel("Sozialfonds-Beitrag");
		JLabel labelAntraegeBewilligt = new JLabel("Bewilligte Anträge");
		JLabel labelVollzuschuss = new JLabel("Vollzuschuss ab Punktzahl");
		JLabel labelFonds = new JLabel("Verfügbarer Fonds");
		JLabel labelPunkteVergeben = new JLabel("Vergebene Punkte");
		JLabel labelPunktwert = new JLabel("Punktwert");
		
		textSemesterName = new JLabel(" ");
		textSemesterJahr = new JLabel(" ");
		textSemesterKurz = new JLabel(" ");
		textSemesterBeginn = new JLabel("");
		
		textAnzahlGesamt = new JLabel(" ");
		textAnzahlBewilligt = new JLabel(" ");
		textAnzahlAbgelehnt = new JLabel(" ");
		textAnzahlNichtEntschieden = new JLabel(" ");
		
		textSemesterName.setFont(textSemesterName.getFont().deriveFont(Font.BOLD));
		textSemesterJahr.setFont(textSemesterJahr.getFont().deriveFont(Font.BOLD));
		textSemesterKurz.setFont(textSemesterKurz.getFont().deriveFont(Font.BOLD));
		textSemesterBeginn.setFont(textSemesterBeginn.getFont().deriveFont(Font.BOLD));
		
		textAnzahlGesamt.setFont(textAnzahlGesamt.getFont().deriveFont(Font.BOLD));
		textAnzahlBewilligt.setFont(textAnzahlBewilligt.getFont().deriveFont(Font.BOLD));
		textAnzahlAbgelehnt.setFont(textAnzahlAbgelehnt.getFont().deriveFont(Font.BOLD));
		textAnzahlNichtEntschieden.setFont(textAnzahlNichtEntschieden.getFont().deriveFont(Font.BOLD));
		
		// Textfelder über die Methode buildTextField erstellen
		tfTicketbeitrag = buildTextField();
		tfSozialfondsbeitrag = buildTextField();
		tfStichtag = buildTextField();
		tfAntraegeBewilligt = buildTextField();
		tfVollzuschuss = buildTextField();
		tfFonds = buildTextField();
		tfPunkteVergeben = buildTextField();
		tfPunktwert = buildTextField();

        deleteButton = new JButton("Löschen");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = datenTabelle.getSelectedRow();

                if (row >= 0) {
                    row = datenTabelle.convertRowIndexToModel(row);
                    Object[] options = {"Nein, abbrechen", "Ja, löschen"};
                    String message = "<html><b>Semester Komplett Löschen</b><br>Soll dieses Semester inklusiver aller Anträge wirklich gelöscht werden?<br><br>" +
                            "<b>" + tableModel.getSemester(row).getSemesterKurzform() + "</b><br><br>" +
                            "Es werden alle Daten des Semesters sowie alle Anträge des Semesters, <br>" +
                            "sowie deren Vorgänge und Unterlagen gelöscht. <br> " +
                            "Statistiken über dieses Semester sind danach nicht mehr verfügbar. <br>" +
                            "Die Personendaten der Antragsteller bleiben jedoch erhalten. <br>  Personen können über einen separaten Menüpunkt archiviert werden.</html>";

                    int selected = JOptionPane.showOptionDialog(null, message, "Reset Semester", JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, options, options[1]);


                    if (selected == 1) {

                        Semester semester = tableModel.getSemester(row);

                        int returnvalue = JOptionPane.YES_OPTION;

						if (SettingsExternal.DEBUG)
							System.out.println(semester.getSemesterAnfang().get(GregorianCalendar.YEAR) + " : " + new GregorianCalendar().get(GregorianCalendar.YEAR));

                        //Noch eine Sicherheitsabfrage falls Semester noch nicht so lange zurückliegt.
                        if (semester.getSemesterAnfang().get(GregorianCalendar.YEAR) > (new GregorianCalendar().get(GregorianCalendar.YEAR) - 4)) {
                            returnvalue = JOptionPane.showConfirmDialog(null, "Das ausgewählte Semester liegt noch keine 5 Jahre zurück. Sind Sie wirklich sicher? Wirklich? Wirklich?");
                        }

                        if (returnvalue == JOptionPane.YES_OPTION) {

                            DBHandlerAntrag dbHandlerAntrag = new DBHandlerAntrag();

                            List<Antrag> antraegeSemester = dbHandlerAntrag.getAntragListeSemester(semester.getSemesterID());

                            for (Antrag a : antraegeSemester) {
								dbHandlerAntrag.delete(a);
							}

                            DBHandlerSemester dbHandlerSemester = new DBHandlerSemester();

                            dbHandlerSemester.delete(semester);

                            tableModel.fillList();

                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Bitte ein Semester auswählen");
                }
            }
        });

		resetButton = new JButton("Reset");
		resetButton.setEnabled(false);
		
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int row = datenTabelle.convertRowIndexToModel(datenTabelle.getSelectedRow());
				
				Object[] options = {"Nein, abbrechen", "Ja, zurücksetzen"};
				String message = "<html><b>Reset Semester</b><br>Soll dieses Semester wirklich zurückgesetzt werden?<br><br>" +
						"<b>" + tableModel.getSemester(row).getSemesterKurzform() + "</b><br><br>" +
						"Es werden nur die Werte zurückgesetzt bzw. geleert, die durch<br>eine vorherige Berechnung für das " +
						"Semester eingetragen bzw. berechnet<br>wurden: Stichtag, Fonds, Ticketpreis, Sozialfonds, Punktzahl " +
						"für Vollzuschuss und Punktwert.<br>Diese Daten können nach dem Reset nicht wieder hergestellt " +
						"werden!<br>Das Semester selbst wird nicht gelöscht.</html>";
				int selected = JOptionPane.showOptionDialog(null, message, "Reset Semester", JOptionPane.DEFAULT_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if(selected == 1){
					
					Semester semester = tableModel.getSemester(row);
					
					semester.reset();
					
					DBHandlerSemester dbHandlerSemester = new DBHandlerSemester();
					dbHandlerSemester.updateSemester(semester);

					// Formular mit Semesterdaten füllen
			    	fillForm(tableModel.getSemester(row));
					
				}
				
			}
		});


		final SForm semesterPanel = new SForm();


		JButton printButton = new JButton("Drucken");
		printButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					datenTabelle.print();
				} catch (PrinterException pe) {
					JOptionPane.showMessageDialog(semesterPanel, "Kann leider nicht drucken");
				}
			}
		});
		
		tableModel = new TableModelSemesterStatistik();
		datenTabelle = new JTable(tableModel);
		
		sorter = new TableRowSorter<TableModel>(tableModel);
		datenTabelle.setRowSorter(sorter);
		
		// nur einzelne Tabellenzeilen können selektiert werden
		datenTabelle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// die Spalten der Tabelle können nicht vertauscht werden
		datenTabelle.getTableHeader().setReorderingAllowed(false);
		
		// die Spaltenbreite kann nicht verändert/verschoben werden
		datenTabelle.getTableHeader().setResizingAllowed(false);
		
		// Klick auf Zeile in Tabelle...
		datenTabelle.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		        
		    	int row = datenTabelle.convertRowIndexToModel(datenTabelle.getSelectedRow());
		    	
		    	if(datenTabelle.getSelectedRow()==0 || datenTabelle.getSelectedRow()==1)
                    enableButton(true);
                else
                    enableButton(false);

		    	// Formular mit Semesterdaten füllen
		    	fillForm(tableModel.getSemester(row));
		            
		        }
  		});

		
		JScrollPane listScroller = new JScrollPane(datenTabelle);
		listScroller.setPreferredSize(new Dimension(450, 250));
		
		JPanel panelSelectUni = new JPanel();
		panelSelectUni.add(labelUniversitaet);
		panelSelectUni.add(comboUni);
		

		semesterPanel.add(labelSemesterArt,            0, 0,  1, 1, 0.20, 0.0, 2, 17, new Insets(5, 5, 2, 5));
		semesterPanel.add(labelSemesterJahr,           1, 0,  1, 1, 0.20, 0.0, 2, 17, new Insets(5, 5, 2, 5));
		semesterPanel.add(labelSemesterKurz,           2, 0,  1, 1, 0.20, 0.0, 2, 17, new Insets(5, 5, 2, 5));
		semesterPanel.add(labelSemesterbeginn,         3, 0,  1, 1, 0.20, 0.0, 2, 17, new Insets(5, 5, 2, 5));
		semesterPanel.add(textSemesterName,            0, 1,  1, 1, 0.20, 0.0, 0, 17, new Insets(2, 5, 5, 5));
		semesterPanel.add(textSemesterJahr,            1, 1,  1, 1, 0.20, 0.0, 0, 17, new Insets(2, 5, 5, 5));
		semesterPanel.add(textSemesterKurz,            2, 1,  1, 1, 0.20, 0.0, 0, 17, new Insets(2, 5, 5, 5));
		semesterPanel.add(textSemesterBeginn,          3, 1,  1, 1, 0.20, 0.0, 0, 17, new Insets(2, 5, 5, 5));
		
		
		SForm antragPanel = new SForm();
		
		antragPanel.add(lbAntraegeGesamt,            0, 0, 1, 1, 0.20, 0.0, 0, 17, new Insets(5, 5, 2, 5));
		antragPanel.add(lbAntraegeBewilligt,         1, 0, 1, 1, 0.20, 0.0, 0, 17, new Insets(5, 5, 2, 5));
		antragPanel.add(lbAntraegeAbgelehnt,         2, 0, 1, 1, 0.20, 0.0, 0, 17, new Insets(5, 5, 2, 5));
		antragPanel.add(lbAntraegeNichtEntschieden,  3, 0, 1, 1, 0.20, 0.0, 0, 17, new Insets(5, 5, 2, 5));
		antragPanel.add(textAnzahlGesamt,            0, 1, 1, 1, 0.20, 0.0, 2, 17, new Insets(2, 5, 5, 5));
		antragPanel.add(textAnzahlBewilligt,         1, 1, 1, 1, 0.20, 0.0, 2, 17, new Insets(2, 5, 5, 5));
		antragPanel.add(textAnzahlAbgelehnt,         2, 1, 1, 1, 0.20, 0.0, 2, 17, new Insets(2, 5, 5, 5));
		antragPanel.add(textAnzahlNichtEntschieden,  3, 1, 1, 1, 0.20, 0.0, 2, 17, new Insets(2, 5, 5, 5));
		
		
		SForm beitragPanel = new SForm();
		
		beitragPanel.add(labelTicketbeitrag,          0,  0,  1, 1, 0.5, 0.0, 0, 18, new Insets(5, 5, 2, 5));
		beitragPanel.add(labelSozialfondsbeitrag,     1,  0,  1, 1, 0.5, 0.0, 0, 18, new Insets(5, 5, 2, 5));
		beitragPanel.add(tfTicketbeitrag,             0,  1,  1, 1, 0.5, 0.0, 0, 18, new Insets(2, 5, 5, 5));
		beitragPanel.add(tfSozialfondsbeitrag,        1,  1,  1, 1, 0.5, 0.0, 0, 18, new Insets(2, 5, 5, 5));
		
		
		SForm berechnungPanel = new SForm();
		
		berechnungPanel.add(labelStichtag,               0, 0,  1, 1, 0.0, 0.0, 0, 17, new Insets(5, 5, 2, 5));
		berechnungPanel.add(labelAntraegeBewilligt,      0, 1,  1, 1, 0.0, 0.0, 0, 17, new Insets(2, 5, 2, 5));
		berechnungPanel.add(labelVollzuschuss,           0, 2,  1, 1, 0.0, 0.0, 0, 17, new Insets(2, 5, 5, 5));
		berechnungPanel.add(labelFonds,                  2, 0,  1, 1, 0.0, 0.0, 0, 17, new Insets(5, 15, 2, 5));
		berechnungPanel.add(labelPunkteVergeben,         2, 1,  1, 1, 0.0, 0.0, 0, 17, new Insets(2, 15, 2, 5));
		berechnungPanel.add(labelPunktwert,              2, 2,  1, 1, 0.0, 0.0, 0, 17, new Insets(2, 15, 5, 5));
		berechnungPanel.add(tfStichtag,                  1, 0,  1, 1, 0.0, 0.0, 0, 17, new Insets(5, 5, 2, 5));
		berechnungPanel.add(tfAntraegeBewilligt,         1, 1,  1, 1, 0.0, 0.0, 0, 17, new Insets(2, 5, 2, 5));
		berechnungPanel.add(tfVollzuschuss,              1, 2,  1, 1, 0.0, 0.0, 0, 17, new Insets(2, 5, 5, 5));
		berechnungPanel.add(tfFonds,                     3, 0,  1, 1, 0.0, 0.0, 0, 17, new Insets(5, 5, 2, 5));
		berechnungPanel.add(tfPunkteVergeben,            3, 1,  1, 1, 0.0, 0.0, 0, 17, new Insets(2, 5, 2, 5));
		berechnungPanel.add(tfPunktwert,                 3, 2,  1, 1, 0.0, 0.0, 0, 17, new Insets(2, 5, 5, 5));
		berechnungPanel.add(resetButton, 4, 0, 1, 1, 1.0, 1.0, 0, 10, new Insets(5, 5, 5, 5));
		berechnungPanel.add(deleteButton, 4, 1, 1, 1, 1.0, 1.0, 0, 10, new Insets(5, 5, 5, 5));
		berechnungPanel.add(printButton, 4, 2, 1, 1, 1.0, 1.0, 0, 10, new Insets(5, 5, 5, 5));


        SForm mainPanel = new SForm();

        // Hauptpanel zusammenstellen
		mainPanel.add(panelSelectUni,              0,  0,  1, 1, 0.0, 0.0, 2, 18, new Insets(0, 0, 10, 0));
		mainPanel.add(semesterPanel,               0,  1,  1, 1, 0.0, 0.0, 2, 18, new Insets(0, 0, 0, 0));
		
		mainPanel.add(new JSeparator(JSeparator.HORIZONTAL),    0, 2, 1, 1, 0.0, 0.0, 2, 17, new Insets(3, 0, 3, 0));
		
		mainPanel.add(antragPanel,                 0,  3,  1, 1, 0.0, 0.0, 2, 18, new Insets(0, 0, 0, 0));
		
		mainPanel.add(new JSeparator(JSeparator.HORIZONTAL),    0, 4, 1, 1, 0.0, 0.0, 2, 17, new Insets(3, 0, 3, 0));
		
		mainPanel.add(beitragPanel,                0,  5,  1, 1, 0.0, 0.0, 2, 18, new Insets(0, 0, 0, 0));
		
		mainPanel.add(new JSeparator(JSeparator.HORIZONTAL),    0, 6, 1, 1, 0.0, 0.0, 2, 17, new Insets(3, 0, 3, 0));
		
		mainPanel.add(berechnungPanel,             0,  7,  1, 1, 0.0, 0.0, 2, 18, new Insets(0, 0, 0, 0));
		
		mainPanel.add(listScroller,                0, 8, 1, 5, 1.0, 1.0, 1, 17, new Insets(5, 0, 0, 0));
		
		add(mainPanel, BorderLayout.CENTER);
		
		filter(((Uni)comboUni.getSelectedItem()).getID());
		
	}

	
	
	/**
	 * Textfelder mit gleichen Parametern erstellen
	 * @return erstelltes Textfeld
	 */
	private JTextField buildTextField() {
		
		JTextField textfeld = new JTextField(10);
		textfeld.setHorizontalAlignment(JTextField.RIGHT);
		textfeld.setFocusable(false);
		return textfeld;
		
	}

	// alle Felder zurücksetzen (leeren)
	private void clearFields() {
		
		textSemesterName.setText(" ");
		textSemesterJahr.setText(" ");
		textSemesterKurz.setText(" ");	
		textSemesterBeginn.setText(" ");
		
		textAnzahlGesamt.setText(" ");
		textAnzahlBewilligt.setText(" ");
		textAnzahlAbgelehnt.setText(" ");
		textAnzahlNichtEntschieden.setText(" ");
		
		tfTicketbeitrag.setText("");
		tfSozialfondsbeitrag.setText("");
		tfStichtag.setText("");
		tfAntraegeBewilligt.setText("");
		tfVollzuschuss.setText("");
		tfFonds.setText("");
		tfPunkteVergeben.setText("");
		tfPunktwert.setText("");
		
	}
	
	
	/**
     * Resetbutton und Löschbutten de-/aktivieren
     * @param status Status des Buttons
	 */
    private void enableButton(boolean status) {
        resetButton.setEnabled(status);
        deleteButton.setEnabled(status);
    }
	
	
	/**
	 * Daten ins Formular eintragen
	 * @param semester Semester-Objekt
	 */
	private void fillForm(Semester semester) {
		
		DBHandlerAntrag dbHandlerAntrag = new DBHandlerAntrag();

        this.semesterID = semester.getSemesterID();

        // Zahlen aus DB holen/berechnen
		List<Object[]> antraege = dbHandlerAntrag.countAntraegeByStatus(semesterID);
		int anzahlAntraegeNichtEntschieden = 0;
		int anzahlAntraegeBewilligt = 0;
		int anzahlAntraegeAbgelehnt = 0;

		for (Object[] o : antraege) {
			int value = ((Long) o[1]).intValue();
			AntragStatus status = (AntragStatus) o[0];
			if (status == AntragStatus.GENEHMIGT) {
				anzahlAntraegeBewilligt = value;
			} else if (status == AntragStatus.ABGELEHNT) {
				anzahlAntraegeAbgelehnt = value;
			} else if (status == AntragStatus.NICHTENTSCHIEDEN) {
				anzahlAntraegeNichtEntschieden = value;
			}
		}

		int anzahlAntraegeGesamt = anzahlAntraegeNichtEntschieden + anzahlAntraegeBewilligt + anzahlAntraegeAbgelehnt;

		int summePunkte = semester.getPunkteVergeben();

		textAnzahlGesamt.setText(Integer.toString(anzahlAntraegeGesamt));
		textAnzahlBewilligt.setText(Integer.toString(anzahlAntraegeBewilligt));
		textAnzahlAbgelehnt.setText(Integer.toString(anzahlAntraegeAbgelehnt));
		textAnzahlNichtEntschieden.setText(Integer.toString(anzahlAntraegeNichtEntschieden));
				
		String stringStichtag = "";
		GregorianCalendar gc1 = semester.getSemesterAnfang();
		GregorianCalendar gc2 = semester.getStichtag();
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		Date datumSemesteranfang = gc1.getTime();
		if (gc2 == null)
			stringStichtag = "";
		else
			stringStichtag = df.format(gc2.getTime());
		
		// Formatierung für Währungen/Geldbeträge in Textfeldern 
		NumberFormat currencyFormat = DecimalFormat.getCurrencyInstance();
		
		
		String stringBeitragFonds = "";
		if (semester.getBeitragFonds() == null)
			stringBeitragFonds = "-";
		else
			stringBeitragFonds = currencyFormat.format(semester.getBeitragFonds());

		
		String stringPunktwert = "";
		if (semester.getPunktWert() == null)
			stringPunktwert = "-";
		else
			stringPunktwert = currencyFormat.format(semester.getPunktWert());

		
		String stringTicketbeitrag = "";
		if (semester.getBeitragTicket() == null)
			stringTicketbeitrag = "-";
		else
			stringTicketbeitrag = currencyFormat.format(semester.getBeitragTicket());

		
		String stringSozialfondsbeitrag = "";
		if (semester.getSozialfonds() == null)
			stringSozialfondsbeitrag = "-";
		else
			stringSozialfondsbeitrag = currencyFormat.format(semester.getSozialfonds());


		textSemesterName.setText(semester.getSemesterArt().getBezeichnung());
		textSemesterJahr.setText(semester.getSemesterJahr());
		textSemesterKurz.setText(semester.getSemesterKurzform());	
		textSemesterBeginn.setText(df.format(datumSemesteranfang));	
		
		tfTicketbeitrag.setText(stringTicketbeitrag);
		tfSozialfondsbeitrag.setText(stringSozialfondsbeitrag);
		tfStichtag.setText(stringStichtag);
		tfAntraegeBewilligt.setText(Integer.toString(anzahlAntraegeBewilligt));
		tfVollzuschuss.setText(Integer.toString(semester.getPunkteVoll()));
		tfFonds.setText(stringBeitragFonds);
		tfPunkteVergeben.setText(Integer.toString(summePunkte));
		
		tfPunktwert.setText(stringPunktwert);
		
	}
	

	
	
	/**
	 * RowFilter für Tabelle wird zusammengestellt und gesetzt
	 * @param id Uni-ID, nach der gefiltert werden soll
	 */
    private void filter(int id) {
    	
    	// Filter wird gesetzt
    	sorter.setRowFilter(RowFilter.regexFilter(Integer.toString(id), 2));
    	
    }

}
