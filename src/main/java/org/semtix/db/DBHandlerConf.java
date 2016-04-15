/*
 * Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 * Semesterticketbüro der Humboldt-Universität Berlin
 *
 * Copyright (c) 2015. Michael Mertins (MichaelMertins@gmail.com)
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

import org.semtix.db.dao.Conf;
import org.semtix.db.hibernate.HibernateCRUD;

/**
 * DBHandler-Klasse zur Bearbeitung der Datenbank-Tabelle conf.
 * Mithilfe dieser DBHandler-Klasse können in der Datenbank neue Konfigurationswerte angelegt, bestehende Konfigurationswerte 
 * ausgelesen, geändert oder gelöscht werden.
 */
public class DBHandlerConf {



    private HibernateCRUD<Conf> dbhandler;


    public DBHandlerConf () {

        dbhandler  = new HibernateCRUD<Conf>(Conf.class);

    }

    public static void main(String[] argv) {

        createDefaultValues();
    }

    public static void createDefaultValues() {

        DBHandlerConf dbHandlerConf = new DBHandlerConf();
        dbHandlerConf.create("Grundbedarf", "475");
        dbHandlerConf.create("Kind", "353");
        dbHandlerConf.create("Weitere_Person", "311");
        dbHandlerConf.create("Schwangerschaft", "59");
        dbHandlerConf.create("Alleinerziehend", "124.00");
        dbHandlerConf.create("Chronisch_Krank", "59.00");
        dbHandlerConf.create("Heizpauschale", "74");
        dbHandlerConf.create("Kappung_Miete", "280");
        dbHandlerConf.create("Auslandskosten", "124");
        dbHandlerConf.create("Med_Psych_Kosten", "250");
        dbHandlerConf.create("Schulden", "30.0");
        dbHandlerConf.create("ABC_Tarif", "150");
        dbHandlerConf.create("Kindergeld", "184.00");
        dbHandlerConf.create("Kindergeld2", "190.00");
        dbHandlerConf.create("Kindergeld3", "215.00");
        dbHandlerConf.create("soznr", "1");


    }

    /**
	 * Wert zu bestehendem Key aus der Datenbank lesen
	 * @param key Schlüssel
	 * @return Wert
	 */
	public String read(String key) {
        Conf c = dbhandler.read("key",key);
        if (null != c)
		    return c.getValue();
        else
            return null;
	}

	/**
	 * Konfigurationswert in Datenbank aktualisieren
	 * @param key Schlüssel
	 * @param value Wert
	 */
	public void update(String key, String value) {

        dbhandler.saveOrUpdate(new Conf(key, value));
    }

	public void create (String key, String value) {
        dbhandler.create(new Conf(key,value));
    }

}
