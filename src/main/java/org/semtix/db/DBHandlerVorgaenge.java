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


import org.semtix.config.UserConf;
import org.semtix.db.dao.Antrag;
import org.semtix.db.dao.Vorgang;
import org.semtix.db.hibernate.HibernateCRUD;
import org.semtix.shared.daten.enums.Uni;
import org.semtix.shared.daten.enums.Vorgangsart;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * DBHandler-Klasse zur Bearbeitung der Datenbank-Tabelle vorgaenge.
 * Mithilfe dieser DBHandler-Klasse können in der Datenbank neue Vorgänge angelegt, bestehende Vorgänge 
 * ausgelesen, geändert oder gelöscht werden.
 * @see Vorgang
 */
public class DBHandlerVorgaenge {


    private final HibernateCRUD<Vorgang> dbhandler;

    /**
     * Default Konstruktor
     */
    public DBHandlerVorgaenge () {
        dbhandler = new HibernateCRUD<Vorgang>(Vorgang.class);
    }

	
	
	/**
	 * Neuen Vorgang in Datenbank anlegen (CREATE)
	 * @param vorgang Objekt Vorgang, welcher neu angelegt werden soll
	 */
    public void createVorgang(Vorgang vorgang) {

        vorgang.setZeitstempel(new GregorianCalendar());
        vorgang.setUserID(UserConf.CURRENT_USER.getUserID());

        dbhandler.create(vorgang);

	}


	
	/**
	 * holt alle Vorgänge zu einem bestimmtem Antrag aus der Datenbank
	 * @param antragID ID des Antrags, z uwelchem die Vorgänge geholt werden
	 * @return List emit Vorgängen
	 */
	public List<Vorgang> getVorgaengeListe(int antragID) {
		
	    return dbhandler.readList("antragid",""+antragID);

	}
	
	
	
	
	/**
	 * Holt Vorgänge zu bestimmter Vorgangsart und bestimmtem Antrag aus der Datenbank.
	 * 
	 * @param vorgangsart Vorgangsart
	 * @param antragID ID des Antrags
	 * @return vorgang Objekt Vorgang
	 */
	public Vorgang getVorgangByVorgangsart(Vorgangsart vorgangsart, int antragID) {
		

        for (Vorgang v : getVorgaengeListe(antragID)) {
            if (v.getVorgangsart().equals(vorgangsart))
                return v;
        }

        return null;
		
	}


    /**
     * Benutzt createVorgang(Vorgang v)
     *
     * @param art Vorgangsart Objekt
     * @param antragID ID des Antrags
     */
    public void createVorgang(Vorgangsart art, int antragID) {
        Vorgang vorgang = new Vorgang();
        vorgang.setAntragID(antragID);
        vorgang.setVorgangsart(art);

        createVorgang(vorgang);
    }


    /**
     * Holt ALLE Vorgänge
     *
     * @return Liste ALLER Vorgänge
     */
    public List<Vorgang> getAllVorgaenge() {
        return dbhandler.getListOfAllElements();
    }

    /**
     * Holt alle Vorgänge für eine bestimmte Uni
     * <br>
     * (Anmerkung: Kostenintensive Methode weil Vorgang keine Referenz auf Uni hat)
     * <br>
     * @param uni Uni HU oder KW
     * @return Vorgänge die zu Anträgen gehören, deren Antragsteller an uni sind
     */
    public List<Vorgang> getAllVorgaenge(Uni uni) {
        //AntragIDs aller Anträge der Uni
        List<Integer> antragIDs = new ArrayList<Integer>();
        for (Antrag a : new DBHandlerAntrag().getAntragListeUni(uni)) {
            antragIDs.add(a.getAntragID());
        }

        List<Vorgang> resultList = new ArrayList<Vorgang>();
        for (Vorgang v : getAllVorgaenge()) {
            if (antragIDs.contains(v.getAntragID())) {
                resultList.add(v);
            }
        }

        return resultList;
    }

    /**
     * Löscht einzelnen Vorgang
     *
     * @param v Vorgang-Objekt
     */
    public void delete(Vorgang v) {
        dbhandler.delete(v);
    }
}
