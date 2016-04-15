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

package org.semtix.db;

import org.semtix.db.dao.Unterlagen;
import org.semtix.db.hibernate.HibernateCRUD;

import java.util.ArrayList;
import java.util.List;

/**
 * DBHandler-Klasse zur Bearbeitung der Datenbank-Tabelle unterlagen.
 * Mithilfe dieser DBHandler-Klasse können in der Datenbank neue Unterlagen angelegt, bestehende Unterlagen 
 * ausgelesen, geändert oder gelöscht werden.
 * @see Unterlagen
 */
public class DBHandlerUnterlagen {
	


    private HibernateCRUD<Unterlagen> dbhandler;


    /**
     * Default-Konstruktor
     */
    public DBHandlerUnterlagen () {

        dbhandler  = new HibernateCRUD<Unterlagen>(Unterlagen.class);

    }


    /**
	 * Neue Unterlagen in Datenbank anlegen (CREATE)
	 * @param unterlagen Objekt Unterlagen
	 */
    public void createUnterlagen(Unterlagen unterlagen) {

        dbhandler.create(unterlagen);

	}

	
	
	
	
	/**
	 * Bestimmte Unterlagen aus Datenbank lesen (SELECT)
	 * @param unterlagenID ID der gewünschten Unterlagen
	 * @return Objekt Unterlagen
	 */
	public Unterlagen readUnterlagen(int unterlagenID) {
		
        return dbhandler.getByID(unterlagenID);
	}

	
	
	
	
	/**
	 * Unterlagen in Datenbank aktualisieren (UPDATE)
	 * @param unterlagen Objekt der zu ändernden Unterlagen
	 */
	public void updateUnterlagen(Unterlagen unterlagen) {
		
        dbhandler.update(unterlagen);
	}
	
	
	
	
	

	/**
	 * Unterlagen aus Datenbank löschen (DELETE)
	 * @param unterlagenID ID der gewünschten Unterlagen
	 */
	public void deleteUnterlagen(int unterlagenID) {
		
        deleteUnterlagen(readUnterlagen(unterlagenID));
		
	}

    /**
     * Unterlagen aus Datenbank löschen (DELETE)
     * @param unterlagen Unterlagen
     */
    public void deleteUnterlagen(Unterlagen unterlagen) {

        dbhandler.delete(unterlagen);

    }
	
	

	/**
	 * holt alle Unterlagen-Einträge zu einem bestimmtem Antrag aus der Datenbank
	 * @param antragID ID des gewünschten Antrags
	 * @return Liste mit Unterlagen zu bestimmtem Antrag
	 */
	public List<Unterlagen> getUnterlagenListe(int antragID) {
		
		List<Unterlagen> unterlagenListe = new ArrayList<Unterlagen>();

        for (Unterlagen u : dbhandler.getListOfAllElements()) {
            if (u.getAntragID() == antragID)
                unterlagenListe.add(u);
        }
		

		
		return unterlagenListe;
		
	}
	

	
}
