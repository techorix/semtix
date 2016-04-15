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

package org.semtix.gui.tabs.antrag.nachreichungen.pruefen;

import org.semtix.db.DBHandlerNachreichung;
import org.semtix.db.DBHandlerVorgaenge;
import org.semtix.db.dao.Nachreichung;
import org.semtix.shared.daten.enums.Vorgangsart;

import java.util.List;

/**
 * Klasse speichert die Daten zu ungeprüften Nachreichungen
 */
public class NachreichungModel {


	private List<Nachreichung> nachreichungListe;
	
	
	/**
	 * Erstellt neues Model
	 */
	public NachreichungModel() {

	}
	
	
	/**
	 * Setzt die ID für den betreffenden Antrag
	 * @param antragID AntragID
	 */
	public void setAntragID(int antragID) {

		nachreichungListe = new DBHandlerNachreichung().getUncheckedNachreichungenForAntrag(antragID);

	}
	
	
	/**
	 * Liefert Liste mit ungeprüften Nachreichungen
	 * @return Liste mit ungeprüften Nachreichungen
	 */
	public List<Nachreichung> getList() {
		return nachreichungListe;
	}
	
	

	/**
	 * nach Klick auf Checkbox wird der Status der Nachreichung im Model aktualisiert
	 * @param index Index der Nachreichung in der Liste
	 * @param state Zustand der Checkbox
	 */
	public void updateNachreichung(int index, boolean state) {
		nachreichungListe.get(index).setStatusChecked(state);
	}
	
	
	
	
	/**
     * Geprüfte Nachreichungen in Datenbank speichern. Sie spielen danach keine Rolle mehr.
     */
	public void saveNachreichungen() {

        DBHandlerNachreichung dbHandlerNachreichungen = new DBHandlerNachreichung();
        DBHandlerVorgaenge dbHandlerVorgaenge = new DBHandlerVorgaenge();

		for(Nachreichung n : nachreichungListe) {
			
			// nur wenn Status auf geprüft
			if(n.isStatusChecked()) {
				dbHandlerNachreichungen.update(n);
                dbHandlerVorgaenge.createVorgang(Vorgangsart.NACHREICHUNG_GEPRUEFT, n.getAntragID());
            }
				
	
		}

	}

}
