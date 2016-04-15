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

package org.semtix.gui.person.emailsuche;

import org.semtix.config.UniConf;
import org.semtix.db.DBHandlerPerson;
import org.semtix.db.dao.Person;
import org.semtix.gui.MainControl;
import org.semtix.shared.actions.ActionCloseDialog;
import org.semtix.shared.elements.SForm;
import org.semtix.shared.tablemodels.TableModelPersonenListe;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
* Dialog zur Personensuche und Filterung nach Emailadressen.
*/
@SuppressWarnings("serial")
public class DialogEmailSuche
extends JDialog{
	
	private MainControl mainControl;
	
	private JLabel labelUniversitaet, labelAnzahl;
	private JTextField tfEmail;

	private JTable table;
	private JScrollPane tableScroller;

	private TableModelPersonenListe tableModel;
	private TableRowSorter<TableModel> sorter;
	
	
	public DialogEmailSuche(MainControl mainControl) {
		
		this.mainControl = mainControl;
		
		setTitle("Mailadressen suchen");
		
		// Icon für Dialog setzen
		URL filename = getClass().getResource("/images/app_logo_16x16.png");
		setIconImage(Toolkit.getDefaultToolkit().getImage(filename));
		
		setModal(true);
		setResizable(false);
	
		// MainPanel mit Abstand zum Dialogrand erstellen
		JPanel mainPanel = new JPanel(new BorderLayout());
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
		resetButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				// Textfeld leeren
				tfEmail.setText("");
				// Cursor auf Textfeld setzen
				tfEmail.requestFocus();
				// 1. Zeile selektieren
				setSelectedRow();
				// TableRowSorter wird zurückgesetzt (reset)
				sorter.modelStructureChanged();
			}
		});
		
		// Button zum Schließen des Dialogs
		JButton abbrechenButton = new JButton(new ActionCloseDialog(this, "Beenden"));

		buttonPanel.add(resetButton);
		buttonPanel.add(abbrechenButton);
	
		JLabel labelEmail = new JLabel("Mailadresse");
		labelUniversitaet = new JLabel(UniConf.aktuelleUni.toString());
		labelAnzahl = new JLabel();
	
		tfEmail = new JTextField(15);
		tfEmail.getDocument().addDocumentListener(new EmailDocumentListener(this));
		
		// KeyListener zu Textfeld hinzufügen
		tfEmail.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				// ENTER-Taste gedrückt...
				if (evt.getKeyCode() == KeyEvent.VK_ENTER)
					selectPerson();
			
				// Taste DOWN (Pfeil nach unten) gedrückt...
				if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
					// Fokus auf Tabelle setzen, damit über die Pfeiltasten in der Tabelle navigiert werden kann.
					// nur wenn eine Zeile selektiert ist UND es mehr als 1 Zeile in der Tabelle hat 
					if(table.getSelectedRow() != -1 && getSorter().getViewRowCount() > 1) {
						table.requestFocus();
						table.setRowSelectionInterval(1, 1);
					}
				}
			}
		});
	
		// TableModel für Personendaten


		List<Person> listPersons = new ArrayList<Person>();

		for (Person p : new DBHandlerPerson().getPersonenListe(UniConf.aktuelleUni)) {
			if (null != p.getEmail() && p.getEmail().length() > 5)
				listPersons.add(p);
		}

		tableModel = new TableModelPersonenListe(listPersons,false);
		
		// Tabelle zur Anzeige der Personendaten aus TableModel
		table = new JTable(tableModel);

		for (String s : tableModel.getColumnsIShouldHide(new String[]{"E-Mail", "Name", "Vorname"})) {
			table.removeColumn(table.getColumn(s));
		}


		// RowSorter für Tabelle setzen
		sorter = new TableRowSorter<TableModel>(tableModel);
		table.setRowSorter(sorter);
	
		// nur einzelne Tabellenzeilen können selektiert werden
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// die Spalten der Tabelle können nicht vertauscht werden
		table.getTableHeader().setReorderingAllowed(false);
	
		// die Spaltenbreite kann nicht verändert/verschoben werden
		table.getTableHeader().setResizingAllowed(false);
	
		// MouseListener auf Tabelle...
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// Doppelklick auf Tabellenzeile...
				if (e.getClickCount()==2) 
					// Person auswählen und im Formular anzeigen
					selectPerson();
			} 
		});
		
		// KeyListener auf Tabelle hinzufügen
		table.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				// ENTER-Taste wird gedrückt
				if (ke.getKeyCode() == KeyEvent.VK_ENTER)
					// Person auswählen und im Formular anzeigen
					selectPerson();
			}
		});

		// Tabelle in Scrollpane setzen
		tableScroller = new JScrollPane(table);
		tableScroller.setPreferredSize(new Dimension(450, 250));
		
		// 1. Zeile selektieren
		setSelectedRow();
		
		SForm formular = new SForm();
		formular.add(labelEmail,        0, 0, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 10, 10));
		formular.add(tfEmail,           1, 0, 1, 1, 0.0, 0.0, 2, 17, new Insets(0, 0, 10, 0));
		formular.add(tableScroller,     0, 1, 2, 1, 0.0, 0.0, 1, 17, new Insets(0, 0, 10, 0));
		formular.add(labelUniversitaet, 0, 2, 2, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
		formular.add(labelAnzahl,       0, 3, 2, 1, 1.0, 1.0, 0, 17, new Insets(0, 0, 10, 0));

		// Formular und Buttons zum MainPanel hinzufügen
		mainPanel.add(formular, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
	
		// MainPanel dem Dialog hinzufüügen
		add(mainPanel);
	
		// Anzahl der angezeigten Personen akzualisieren
		updateAnzahl();
	
		pack();
		
		// Frame auf dem Bildschirm zentrieren
		setLocationRelativeTo(getParent());
		
		setVisible(true);

	}
	
	
	
	// Schliesst Dialog und zeigt ausgewählte Person in Formular an.
	private void selectPerson() {
		
		// nur falls eine Zeile in derTabelle selektiert
		if(table.getSelectedRow() != -1) {
			
			int row = table.convertRowIndexToModel(table.getSelectedRow());
			
			// Neuen Tab mit Daten der ausgewählten Person öffnen
			mainControl.addTab(tableModel.getPersonID(row));
	        
			// Dialog schließen
	        dispose();
	        
		}
	
	}
	
	
	// Immer die erste Zeile der Tablle wird selektiert
	public void setSelectedRow(){
		
		// Wenn mindestens 1 Zeile angezeigt wird
		if(this.getSorter().getViewRowCount() > 0) {
			// 1. Zeile in Tabelle selektieren
			table.setRowSelectionInterval(0, 0);
			// auf obersten Tabelleneintrag scrollen
			tableScroller.getVerticalScrollBar().setValue(0);			
		}
			
	}


	public TableRowSorter<TableModel> getSorter() {
		return sorter;
	}
	
	
	// Anzahl der angezeigten Einträge aktualisieren
	public void updateAnzahl() {
		labelAnzahl.setText("Anzahl: " + this.getSorter().getViewRowCount() + " von " +
				this.getSorter().getModelRowCount());
	}
	
	public String getTextEmail() {
		return tfEmail.getText();
	}

}
