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

package org.semtix.gui.filter;


import org.semtix.db.DBHandlerAntrag;
import org.semtix.gui.MainControl;
import org.semtix.gui.tabs.TabControl;
import org.semtix.gui.tabs.antrag.AntragIndex;
import org.semtix.shared.daten.enums.FilterArt;
import org.semtix.shared.daten.enums.Status;

import javax.swing.*;
import java.util.List;

/**
 * Control-Klasse zur Steuerung der Filterung nach ANträgen
 */
public class FilterControl {
	
	private MainControl mainControl;
	
	private TabControl tabControl;
	
	private DialogAntragFilter filterDialog;
	
	private Filter filter;
	
	
	/**
	 * Erstellt eine neue Control-Klasse zur Filterung nach Anträgen
	 * @param tabControl TbaControl
	 * @param mc MainControl
	 */
	public FilterControl(TabControl tabControl, MainControl mc) {
		
		this.tabControl = tabControl;
		this.mainControl = mc;

		this.filter = new Filter();

		this.filterDialog = new DialogAntragFilter(this);

	}
	
	/**
	 * Setzt die Filter aus dem Dialog
     * @param filterArt FilterArt
     */
	public void setFilter(FilterArt filterArt) {

		this.filter.setFilterArt(filterArt);

		// keine Anfangsbuchstaben selektiert = kein Ergebnis möglich!
		if(filter.getBuchstaben().equals("keine")) {
			// Fehlermeldung/Hinweis ausgeben
			String message = "Keine Anfangsbuchstaben ausgewählt.";
			JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.WARNING_MESSAGE);
		} else {
			List<AntragIndex> indexListe = new DBHandlerAntrag().getAntragIndexListe(filter);

			if (null != indexListe && indexListe.size() > 0) {
				// falls noch kein Tab für den Filter eröffnet wurde, muss
				// dies vorher gemacht werden
				if (tabControl == null) {
					mainControl.addTab(indexListe);


				}

				// es gibt schon einen Tab und dort muss nur noch der FIlter gesetzt werden
				else {
					// Filter setzen, View aktualisieren
					tabControl.setFilter(indexListe);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Keine Daten gefunden.", "Fehler", JOptionPane.WARNING_MESSAGE);
			}
		}
		
	}
	
	
	
	/**
	 * Semester-ID im Filter setzen
	 * @param semesterID Semester-ID
	 */
	public void setSemesterID(int semesterID) {
		filter.setSemesterID(semesterID);
	}
	
	
	/**
	 * Erstsemester im Filter setzen
	 * @param status Erstsemester
	 */
	public void setErstsemester(Status status) {
		filter.setErstsemester(status);
	}
	public void setKulanz(Status status) {
		filter.setKulanz(status);
	}
	public void setRatenzahlung(Status status) {
		filter.setRatenzahlung(status);
	}
	public void setNachreichung(Status status) {
		filter.setNachreichung(status);
	}
	public void setNothilfe(Status status) {
		filter.setNothilfe(status);
	}
	public void setSpaetis(Status status) {
		filter.setSpaetis(status);
	}

	public void setBarauszahler(Status status) {
		filter.setBarauszahler(status);
	}

	public void setTeilzuschuss(Status status) {
		filter.setTeilzuschuss(status);
	}

    public void setArchiviert(Status status) {
        filter.setArchiviert(status);
    }



	
	/**
	 * Buchstaben im Filter setzen
	 * @param buchstaben Buchstaben
	 */
	public void setBuchstaben(String buchstaben) {
		filter.setBuchstaben(buchstaben);
	}


	/**
	 * Liefert das Filter-Objekt mit den gesetzten Filtern
	 * @return Filter
	 */
	public Filter getFilter() {
		return filter;
	}


}
