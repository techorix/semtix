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

package org.semtix.config;

import org.semtix.db.DBHandlerUser;
import org.semtix.db.dao.SemtixUser;

import javax.swing.*;


/**
 * Enthält und setzt den aktuellen User der Anwendung
 */
public class UserConf {
	
	
	// User-Objekt des aktuellen Benutzers
	public static SemtixUser CURRENT_USER;
	
	
	
	/**
     * Initialisiert den aktuellen Benutzer anhand des Benutzerlogins und legt ihn ggf. neu an, wenn noch nicht in DB vorhanden
     */
	public static void init(){

        String username = System.getProperty("user.name");
        if (username != null) {
            DBHandlerUser dbHandlerUser = new DBHandlerUser();
            try {
                CURRENT_USER = dbHandlerUser.getCurrentUser(username);
            } catch (Exception e) {
                SemtixUser newUser = new SemtixUser();
                String login = username;
                newUser.setLoginName(login);
                dbHandlerUser.saveOrUpdateUser(newUser);
                CURRENT_USER = newUser;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Username ist null");
        }
    }

}
