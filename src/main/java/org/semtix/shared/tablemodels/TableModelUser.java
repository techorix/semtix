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


package org.semtix.shared.tablemodels;

import org.semtix.db.DBHandlerUser;
import org.semtix.db.dao.SemtixUser;
import org.semtix.shared.daten.enums.UserStatus;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * TableModel enthält Daten der Büro-Mitarbeiter_innen und zeigt diese in einer Tabelle an.
 * @see org.semtix.gui.admin.UserPanel
 */
@SuppressWarnings("serial")
public class TableModelUser
extends AbstractTableModel {

    // max. Anzahl für Buchstabenverteilung
    private final int anzahlBuchstaben = 10;
    private DBHandlerUser dbHandlerUser;
	// Überschriften der Tabellenspalten
	private String[] columnNames;
	// Liste mit Daten der Büro-Mitarbeiter_innen
	private List<SemtixUser> userListe;

	private boolean showOnlyActiveUsers;

	/**
	 * Erstellt neues TableModel mit Daten der Büro-Mitarbeiter_innen
	 *
	 * @param showOnlyActiveUsers  Nur Benutzer mit Status 'aktiv'
	 */
	public TableModelUser(boolean showOnlyActiveUsers) {

		this.showOnlyActiveUsers = showOnlyActiveUsers;

		// DBHandler für DB-Aktionen erstellen
		dbHandlerUser = new DBHandlerUser();
		
		// Liste der Büro-Mitabeiter_innen anlegen
		userListe = new ArrayList<SemtixUser>();
		

		
		// Überschriften für Tabellenspalten festlegen
		columnNames = new String[] {"Kürzel", "Name", "Privileg", "Status", "Buchstabenverteilung"};

		// Liste mit Daten füllen
		fillList();

	}

	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}


	/**
	 * Liefert Userobjekt mit gewünschter User-ID zurück
	 * @param userID ID des gewünschten Users
	 * @return Objekt mit User-Daten
	 */
	public SemtixUser getUser(int userID) {
		return userListe.get(userID);
	}

	
	/**
	 * Bestehenden User ändern
	 * @param user Objekt mit User-Daten
	 */
	public void updateUser(SemtixUser user) {
		dbHandlerUser.saveOrUpdateUser(user);
		fillList();
	}
	
	
	/**
	 * User löschen
	 * @param user Objekt mit User-Daten
	 */
	public void deleteUser(SemtixUser user) {
		dbHandlerUser.deleteUser(user.getUserID());
		fillList();
	}
	
	
	/**
	 * Füllt Liste mit aktuellen Userdaten
	 */
	public void fillList() {
		// Liste leeren
		userListe.clear();
		// Liste mit Daten aus Datenbank füllen
		if (!showOnlyActiveUsers) {
			userListe = dbHandlerUser.getUserListe();
		} else {
			userListe = new ArrayList<SemtixUser>();

			boolean fehler = false;

			for (SemtixUser user : dbHandlerUser.getUserListe()) {
				try {
					if (!user.getBuchstaben().equals("") && user.getStatus() == UserStatus.AKTIV)
						userListe.add(user);
				} catch (NullPointerException npe) {
					fehler = true;
				}
			}

			if (fehler)
				JOptionPane.showMessageDialog(null, "Bitte Benutzertabelle checken, ob alle Benutzer ein Kürzel haben.");

		}

		this.fireTableDataChanged();

	}


	@Override
	public int getColumnCount() {
		return columnNames.length;
	}


	@Override
	public int getRowCount() {
		return userListe.size();
	}


	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		String value = "";
		
		if (columnIndex == 0)
			value = userListe.get(rowIndex).getKuerzel();
		
		if (columnIndex == 1)
			value = userListe.get(rowIndex).getVorname() + " " + userListe.get(rowIndex).getNachname();
		
		if (columnIndex == 2)
			value = userListe.get(rowIndex).getPrivileg().toString();
		
		if (columnIndex == 3)
			value = userListe.get(rowIndex).getStatus().toString();
				
		if (columnIndex == 4)
			value = userListe.get(rowIndex).getBuchstaben();


		return value;
	}

}
