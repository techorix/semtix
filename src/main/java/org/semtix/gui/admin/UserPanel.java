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

package org.semtix.gui.admin;


import org.semtix.db.dao.SemtixUser;
import org.semtix.shared.daten.enums.UserPrivileg;
import org.semtix.shared.daten.enums.UserStatus;
import org.semtix.shared.elements.SForm;
import org.semtix.shared.tablemodels.TableModelUser;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * In diesem Panel der AdminTools werden die eingetragenen Büromitarbeiter_innen angezeigt. Es 
 * können neue User hinzugefügt, bestehende gändert oder gelöscht werden.
 */
@SuppressWarnings("serial")
public class UserPanel
extends JPanel {
	
	
	private JComboBox comboFilterPrivileg, comboFilterStatus;
	
	private TableModelUser tableModel;
	private JTable datenTabelle;
	private TableRowSorter<TableModel> sorter;
	
	
	/**
	 * Erstellt ein neues Panel mit Anzeige der eingetragenen Büromitarbeiter_innen
	 */
	public UserPanel() {

		// BorderLayout für das Panel festlegen
		setLayout(new BorderLayout());
		
		// Abstand zwischen Panel und Dialogrand
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		// Panel für die Buttons "Neu", "Ändern" und "Löschen"
		JPanel buttonPanel = new JPanel();
		
		// Button zum Anlegen von neuen Büro-Mitarbeiter_innen
		JButton newButton = new JButton("Neu");
		newButton.setToolTipText("Neuen User anlegen");
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				UserDialog userDialog = new UserDialog(tableModel);
				userDialog.setUser(new SemtixUser());
				userDialog.setVisible(true);
			}
		});
		
		// Button zum Bearbeiten eines Datensatzes
		JButton editButton = new JButton("Bearbeiten");
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editUser();
			}
		});
		
		// Button zum Löschen eines Datensatzes
		JButton deleteButton = new JButton("Löschen");
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteUser();
			}
		});

		// Buttons dem Panel hinzufügen
		buttonPanel.add(newButton);
		buttonPanel.add(editButton);
		buttonPanel.add(deleteButton);
		
		// Beschriftungen der ComboBoxen
		JLabel labelFilterPrivileg = new JLabel("Privileg");
		JLabel labelFilterStatus = new JLabel("Status");
		
		
		// ComboBoxModel für Privilegauswahl
		DefaultComboBoxModel modelUserPrivileg = new DefaultComboBoxModel();
		modelUserPrivileg.addElement("alle");
		modelUserPrivileg.addElement(UserPrivileg.USER);
		modelUserPrivileg.addElement(UserPrivileg.FINANZ);
		modelUserPrivileg.addElement(UserPrivileg.ADMIN);
		
		// ComboBox das Model zuweisen (Filtern nach Privileg)
		comboFilterPrivileg = new JComboBox(modelUserPrivileg);
		
		// ComboBoxModel für Statusauswahl
		DefaultComboBoxModel modelUserStatus = new DefaultComboBoxModel();
		modelUserStatus.addElement("alle");
		modelUserStatus.addElement(UserStatus.AKTIV);
		modelUserStatus.addElement(UserStatus.PASSIV);
		
		// ComboBox das Model zuweisen (Filtern nach Status)
		comboFilterStatus = new JComboBox(modelUserStatus);
		
		// ItemListener erkennt Änderungen an ComboBoxen und führt Filterung aus
		ItemListener listener = new ItemListener() { 		
			  public void itemStateChanged(ItemEvent e) { 
				  if (e.getStateChange() == ItemEvent.SELECTED) {
				  filter();
				  }
			  } 
		}; 
		
		// ItemListener den ComboBoxen zuweisen
		comboFilterPrivileg.addItemListener(listener);
		comboFilterStatus.addItemListener(listener);
		
		// TableModel mit Daten der Büro-Mitarbeiter_innen erstellen
		tableModel = new TableModelUser(false);
		
		// Tabelle für Daten der Büro-Mitarbeiter_innen erstellen und TabelModel zuweisen
		datenTabelle = new JTable(tableModel);
		
		// Tabellensortierung (Filterung) mit Verknüpfung zum TableModel erstellen
		sorter = new TableRowSorter<TableModel>(tableModel);
		
		// Tabellensortierung (Filterung) der Tabelle zuweisen
		datenTabelle.setRowSorter(sorter);
		
		// nur einzelne Tabellenzeilen können selektiert werden
		datenTabelle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// die Spalten der Tabelle können nicht vertauscht werden
		datenTabelle.getTableHeader().setReorderingAllowed(false);
		
		// die Spaltenbreite kann nicht verändert/verschoben werden
		datenTabelle.getTableHeader().setResizingAllowed(false);
		
		// Doppelklick auf die Tabelle...
		datenTabelle.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		        if (e.getClickCount()==2) {
		        	editUser();
		        }
		    } 
		});
		
		// Tabelle in ein ScrollPane packen, damit bei zu großer Tabelle gescrollt werden kann
		JScrollPane listScroller = new JScrollPane(datenTabelle);
		
		// Formular erstellen und Komponenten hinzufügen
		SForm formular = new SForm();
		
		formular.add(labelFilterPrivileg,       0, 1, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 5, 5, 5));
		formular.add(labelFilterStatus,         2, 1, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 20, 5, 5));
		
		formular.add(comboFilterPrivileg,       1, 1, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 5, 5, 5));
		formular.add(comboFilterStatus,         3, 1, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 5, 5, 5));
		formular.add(listScroller,              0, 2, 4, 1, 1.0, 1.0, 1, 17, new Insets(5, 5, 5, 5));

		
		// Formular und Buttons dem Hauptpanel hinzufügen
		add(formular, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
	}
	


	
	/**
	 * Liefert den TableRowSorter für die Tabelle mit den Büro-Mitarbeiter_innen
	 * @return TableRowSorter
	 */
	public TableRowSorter<TableModel> getSorter() {
		return sorter;
	}
	
	
	/**
	 * Button "Ändern" wurde angeklickt
	 */
	private void editUser() {
		
		// Falls kein User in Tabelle selektiert kommt eine fehlermeldung
		if(datenTabelle.getSelectedRow() == -1){
			String message = "Kein User ausgewählt.";
            JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
        }
		// ansonsten wird der ausgewählte User in einem Dialog zum Ändern angezeigt
		else{
			int row = datenTabelle.convertRowIndexToModel(datenTabelle.getSelectedRow());
            UserDialog userDialog = new UserDialog(tableModel);
            userDialog.setUser(tableModel.getUser(row));
            userDialog.setVisible(true);
		}
		
	}
	
	
	
	/**
	 * Button "Löschen" wurde angeklickt
	 */
	private void deleteUser() {
		
		// Falls kein User in Tabelle selektiert kommt eine fehlermeldung
		if(datenTabelle.getSelectedRow() == -1){
			String message = "Kein User ausgewählt.";
            JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
        }
		// ansosnten muss erst noch einmal die Löschung bestätigt werden, bevor tatsächlich gelöscht wird
		else{
			int row = datenTabelle.convertRowIndexToModel(datenTabelle.getSelectedRow());
			SemtixUser user = tableModel.getUser(row);
			Object[] options = {"Abbrechen", "Ja, löschen"};
			String message = "Soll User " + user.getVorname() + " " + user.getNachname() + " wirklich gelöscht werden?";
			int selected = JOptionPane.showOptionDialog(null, message, "Fehler", JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE, null, options, options[1]);
			if(selected == 1){
				tableModel.deleteUser(user);
			}
		}
		
	}

	
	
	
	/**
	 * RowFilter wird zusammengestellt und gesetzt. Die Datensätze der Büro-Mitarbeiter_innen 
	 * können nach Privileg und Status gefiltert werden.
	 */
    private void filter() {
    	
    	// die eingegebenen Suchtexte werden aus den ComboBoxen geholt
    	String filterString1 = comboFilterPrivileg.getSelectedItem().toString();
    	String filterString2 = comboFilterStatus.getSelectedItem().toString();

    	
    	// eine Liste mit den 2 Filtern wird erstellt
    	java.util.List<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>(2);
    	
    	if (filterString1.equals("alle")) {
    		filters.add(RowFilter.regexFilter(("User|Finanz|Admin"), 2)); //Filter für Spalte 2 (Privileg)
    	}
    	else {
    		filters.add(RowFilter.regexFilter(filterString1, 2)); //Filter für Spalte 2 (Privileg)
    	}
    	
    	if (filterString2.equals("alle")) {
    		filters.add(RowFilter.regexFilter("aktiv|passiv", 3)); //Filter für Spalte 3 (Status)
    	}
    	else {
    		filters.add(RowFilter.regexFilter(filterString2, 3)); //Filter für Spalte 3 (Status)
    	}
    	
    	
    	// RowFilter.andFilter wird erstellt
    	RowFilter<Object, Object> filter = RowFilter.andFilter(filters);
    	
    	// Filter wird gesetzt
    	getSorter().setRowFilter(filter);
    	
    }
	
	

}
