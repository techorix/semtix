/*

  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
         Semesterticketbüro der Humboldt-Universität Berlin

  Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package org.semtix.db;

import org.semtix.db.dao.AntragHaerte;
import org.semtix.db.hibernate.HibernateCRUD;

import java.util.ArrayList;
import java.util.List;

/**
 * DBHandler-Klasse zur Bearbeitung der Datenbank-Tabelle antraghaerte.
 * Mithilfe dieser DBHandler-Klasse können in der Datenbank neue Antraghärten angelegt, bestehende Antraghärten 
 * ausgelesen, geändert oder gelöscht werden.
 * @see AntragHaerte
 */
public class DBHandlerAntraghaerte {

    private HibernateCRUD<AntragHaerte> dbhandler;

    public DBHandlerAntraghaerte () {

        dbhandler  = new HibernateCRUD<AntragHaerte>(AntragHaerte.class);

    }


    /**
	 * Neue Antraghärte in Datenbank anlegen (CREATE).
	 * @param antragHaerte Objekt Antraghärte
	 * <b>Achtung:</b> evtl. Fehler: antragID beim Anlegen von neuem Antrag noch nicht bekannt/vorhanden???
	 */
	public void create(AntragHaerte antragHaerte) {

        dbhandler.create(antragHaerte);
		
	}

	
	/**
	 * Eine Antraghärte aus der Datenbank auslesen (SELECT).
	 * @param antragHaerteID ID der Antraghärte
	 * @return Objekt Antraghärte
	 */
	public AntragHaerte read(int antragHaerteID) {

		return dbhandler.getByID(antragHaerteID);
	}


	
	/**
	 * Liest die Antraghärten für bestimmten Antrag (antrag_id) aus der Datenbank
	 * @param antrag_id ID des gewünschten Antrags
	 * @return Liste mit Antraghärten
	 */
	public List<AntragHaerte> getAntragHaerteListe(int antrag_id) {

        List<AntragHaerte> listAll = dbhandler.getListOfAllElements();
        List<AntragHaerte> listAhAntrag = new ArrayList<AntragHaerte>();

        for (AntragHaerte ah : listAll) {
            if (ah.getAntragID() == antrag_id)
                listAhAntrag.add(ah);
        }


        return listAhAntrag;
    }
	
	
	

	/**
	 * einzelne Antraghärte in DB aktualisieren (UPDATE)
	 * @param antragHaerte Objekt Antraghärte
	 */
	public void update(AntragHaerte antragHaerte) {
		
        dbhandler.update(antragHaerte);
		
	}


    /**
     * Eine bestimmte Antraghärte aus DB löschen
     * @param antragHaerte ID der Antraghärte
     */
    public void delete(AntragHaerte antragHaerte) {

        dbhandler.delete(antragHaerte);

    }


    /**
     * Löscht alle Antraghärten eines Antrags
     *
     * @param antragID ID des betreffenden Antrags
     */
    public void deleteAll(int antragID) {
        for (AntragHaerte ah : getAntragHaerteListe(antragID)) {
            dbhandler.delete(ah);
        }

    }

    /**
     * Erstellt Liste von Antraghärten
     *
     * @param list Liste von Antraghärten
     */
    public void createAll(List<AntragHaerte> list) {
        for (AntragHaerte ah : list) {
            dbhandler.create(ah);
        }
    }
}
