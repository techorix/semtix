/*

  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
         Semesterticketbüro der Humboldt-Universität Berlin

  Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */


package org.semtix.shared.print;


import org.semtix.shared.daten.ArrayHelper;

import java.io.IOException;

/**
 * Einzige Klasse in der tatsächlich ein Odt-Dokument gedruckt wird
 *
 * Created by MM on 23.01.15.
 */
public class OdtPrinter {

    /**
     * Prints one file n times
     *
     * @param path file location
     * @param n    how many times
     * @throws IOException Dateizugrifffehler
     */
    public static void print(String path, int n) throws IOException {

        if (n == 1) {
            printFiles(new String[]{path});
        } else {
            String[] paths = new String[n];
            for (int j = 0; j < n; j++) {
                paths[j] = path;
            }
            printFiles(paths);
        }

    }


    /**
	 * Prints file with soffic bin. Ignores lock and does not display splash screen
	 *
     * @param pathnames space separated list of absolute filepaths that are to be printed
     * @throws IOException Dateizugrifffehler
     */
    public static void printFiles(String[] pathnames) throws IOException {


        String[] wholeCommand = ArrayHelper.concatenate(new String[]{"soffice", "--headless", "--nolockcheck", "-p",}, pathnames);

        wholeCommand = ArrayHelper.concatenate(wholeCommand, new String[]{" &"});

        Runtime.getRuntime().exec(wholeCommand);

    }
}
