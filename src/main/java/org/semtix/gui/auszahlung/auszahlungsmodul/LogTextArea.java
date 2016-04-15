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

package org.semtix.gui.auszahlung.auszahlungsmodul;


import org.semtix.config.UserConf;
import org.semtix.db.DBHandlerConf;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * Bestimmte TextArea die ein Log der Vorgänge innerhalb des Auszahlungsmodul abbildet
 *
 * Created by MM on 01.12.14.
 */
public class LogTextArea extends JTextArea {


    public LogTextArea() {
        super();

        this.setPreferredSize(new Dimension(160, 400));
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        this.setFont(new Font("Serif", Font.LAYOUT_LEFT_TO_RIGHT, 10));
        this.setLineWrap(true);
        this.setWrapStyleWord(true);

        refresh();
    }

    /**
     * Loggt string in TextArea und DB. Dabei wird auch die Maximale Feldgröße für die DB beachtet
     * @param string String zu loggen
     */
    public void logText(String string) {
        string = createLogPrefix() + string;
        String logtext = string + "\n" + getText();
        setText(logtext);
        writeLogToDB(logtext);
    }

    private void writeLogToDB(String logtext) {
        if (logtext.length() >= 1000) {
            logtext = logtext.substring(0,999);
        }
        DBHandlerConf dbHandlerConf = new DBHandlerConf();
        dbHandlerConf.update("log_auszahlungsmodul", logtext);
    }

    /**
     * Setzt den Text der aktuell hierfür in der DB gespeichert ist
     */
    public void refresh() {
        setText(getLogFromDB());
    }

    private String getLogFromDB() {
        DBHandlerConf dbHandlerConf = new DBHandlerConf();
        return dbHandlerConf.read("log_auszahlungsmodul");
    }


    /**
     * Hilfsmethode für die LogArea: Setzt ein Präfix vor den Eintrag
     * @return Präfix mit Datum / Benutzer / Schritt usw
     */
    private String createLogPrefix() {

        GregorianCalendar datum = new GregorianCalendar();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.");
        String stichtag = df.format(datum.getTime());
        String prefix = "";

        if (!getText().contains(stichtag))
            prefix = stichtag + "\n---------\n ";

        prefix += UserConf.CURRENT_USER.getKuerzel() + " ";

        return prefix;
    }

}
