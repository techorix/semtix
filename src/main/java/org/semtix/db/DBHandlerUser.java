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

package org.semtix.db;


import org.semtix.db.dao.SemtixUser;
import org.semtix.db.hibernate.HibernateCRUD;

import java.util.List;

/**
 * DBHandler-Klasse zur Bearbeitung der Datenbank-Tabelle user.
 * Mithilfe dieser DBHandler-Klasse können in der Datenbank neue User angelegt, bestehende User 
 * ausgelesen, geändert oder gelöscht werden.
 * @see org.semtix.db.dao.SemtixUser
 */
public class DBHandlerUser {

    private HibernateCRUD<SemtixUser> dbhandler;

    public DBHandlerUser () {
        dbhandler = new HibernateCRUD<SemtixUser>(SemtixUser.class);
    }
	

	/**
	 * Neue User in Datenbank anlegen (CREATE)
	 * @param user Objekt User, welches neu angelegt werden soll
	 */
    public void saveOrUpdateUser(SemtixUser user) {

        if (null == user.getBuchstaben())
            user.setBuchstaben("");

        dbhandler.saveOrUpdate(user);

	}


	
	

	/**
	 * Bestimmte User aus Datenbank lesen (SELECT)
	 * @param userID ID der gewünschten User
	 * @return Objekt der gewünschnte User
	 */
	public SemtixUser readUser(int userID) {
		
	    return dbhandler.getByID(userID);

	}


	
	
	/**
	 * Bestimmten User aus Datenbank löschen (DELETE)
	 * @param userID ID des zu löschenden User
	 */
	public void deleteUser(int userID) {
		
        dbhandler.delete(dbhandler.getByID(userID));
		
	}
	
	
	
	

	/**
	 * Liest alle Büromitarbeiter_innen (User) aus der Datenbank und gibt ArrayListe zurück.
	 * @return Liste mit User (Büro-Miarbeiter_innen)
	 */
	public List<SemtixUser> getUserListe() {

       return dbhandler.getListOfAllElements();

	}
	
	
	

	/**
	 * Gibt Objekt User (anhand des Loginnamens) des aktuellen Benutzers zurück
	 * @param loginName Loginname
	 * @return User zum zugehörigen Loginnamen
	 */
	public SemtixUser getCurrentUser(String loginName) {

        for (SemtixUser u : getUserListe()) {
            if (u.getLoginName().equalsIgnoreCase(loginName)) {
                return u;
            }
        }

        SemtixUser newUser = new SemtixUser();
        newUser.setLoginName(loginName);
        dbhandler.create(newUser);

        return newUser;


	}


}
