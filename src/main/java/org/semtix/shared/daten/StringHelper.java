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


package org.semtix.shared.daten;


import java.text.Normalizer;
import java.util.GregorianCalendar;

/**
 * Hilfsfunktionen auf Strings
 * Created by MM on 06.03.15.
 */
public class StringHelper {

    /**
     * @param string that is completely normalized. all characters with umlauts, accents or others have been reduced to their basic ascii character, e.g. ä to a,ô to o etc.
     * @return String der nur ASCII-Zeichen enthält bzw. die ASCII-Zeichen-Versionn des Originals
     */
    public static String removeDiacriticalMarks(String string) {

        return Normalizer.normalize(string, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    /**
     * Wandelt Datum im Stringformat in GregorianCalender um.
     *
     * @param date Datum im Stringformat
     * @return Datum im Format GregorianCalendar
     */
    public static GregorianCalendar convertStringToDate(String date) {

        GregorianCalendar gc = null;

        int year = 0;
        int month = 0;
        int day = 0;

        //date = date.trim();

        if (date.length() > 0) {

            String[] tempsplit = date.split("\\.");

            if (!tempsplit[2].equals("    "))
                year = Integer.parseInt(tempsplit[2]);

            if (!tempsplit[1].equals("  "))
                month = Integer.parseInt(tempsplit[1]) - 1;

            if (!tempsplit[0].equals("  "))
                day = Integer.parseInt(tempsplit[0]);

            if (year != 0 || month != 0 || day != 0)
                gc = new GregorianCalendar(year, month, day);

        }

        return gc;
    }
}