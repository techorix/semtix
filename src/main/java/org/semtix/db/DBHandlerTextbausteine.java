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

import org.semtix.db.dao.Textbaustein;
import org.semtix.db.hibernate.HibernateCRUD;

import java.util.Collections;
import java.util.List;

/**
 * DBHandler-Klasse zur Bearbeitung der Datenbank-Tabelle org.semtix.shared.textbausteine.
 * Mithilfe dieser DBHandler-Klasse können in der Datenbank neue Textbausteine angelegt, bestehende Textbausteine 
 * ausgelesen, geändert oder gelöscht werden.
 * @see Textbaustein
 */
public class DBHandlerTextbausteine {


    private HibernateCRUD<Textbaustein> dbhandler;


    public DBHandlerTextbausteine () {

        dbhandler  = new HibernateCRUD<Textbaustein>(Textbaustein.class);

    }

    public static void main(String[] argv) {
        createDefaultValues();
    }

    public static void createDefaultValues() {
        DBHandlerTextbausteine dbHandlerTextbausteine = new DBHandlerTextbausteine();
        dbHandlerTextbausteine.create(new Textbaustein(4, "example", "exampletext: Please look in constructor of Textbaustein.java for how to use this. The TextBaustein will be used in the program for mail correspondence and status management of applications and is a crucial part of the mitigating workload. You can use variables: {MonthFrom} {MonthTo} {Jahr} Which may be replaced by the respective Months and Year of the current Semester "));


    }

    public void update (Textbaustein textbaustein) {
        dbhandler.update(textbaustein);
    }

    public void delete(Textbaustein textbaustein) {
        dbhandler.delete(textbaustein);
    }

    public void create (Textbaustein textbaustein) {
        dbhandler.create(textbaustein);
    }

	/**
	 * Holt von der Datenbank eine Liste aller Textbausteine
	 * @return Liste mit allen Textbausteinen in der Datenbank
	 */
	public List<Textbaustein> getTextbausteinListe() {
        List<Textbaustein> textbausteine = dbhandler.getListOfAllElements();
        Collections.sort(textbausteine);
        return textbausteine;
    }


}
