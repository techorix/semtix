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

package org.semtix.config;

import java.io.*;
import java.util.Properties;

/**
 * Einfache Klasse zum Holen und Speichern der Properties.
 *
 * Created by Michael Mertins on 06.01.15.
 */
public class PropertiesManagement {

    public static Properties getProperties(String pfad) throws IOException {
        Properties p = new Properties();


        File f = new File(pfad);
        if(f.exists() && !f.isDirectory()) {
            InputStream fis = new FileInputStream(f);
            p.load(fis);
        }

        return p;
    }

    public static Properties getPropertiesXML(String pfad) throws IOException {
        Properties p = new Properties();


        File f = new File(pfad);
        if(f.exists() && !f.isDirectory()) {
            InputStream fis = new FileInputStream(f);
            p.loadFromXML(fis);

        }

        return p;
    }

    public static void saveProperties(Properties props, String pfad, String comment) throws IOException {
        File f = new File(pfad);
        OutputStream out = new FileOutputStream(f);
        props.store(out, comment);
    }

}
