/*

  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
         Semesterticketbüro der Humboldt-Universität Berlin

  Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package org.semtix.config;

import org.semtix.shared.daten.DeutschesDatum;

import java.util.GregorianCalendar;

/**
 * Globale Konstanten (Werte, Einstellungen, Bezeichnungen) für die Anwendung. Diese Werte sind Hartkodiert.
 *
 * Z.B. /etc/semtixdb/semtixconf.properties als Ort für die Einstellungen
 *
 */
public class Settings {
	
	/**
	 * Name der Anwendung, wird im Frametitel angezeigt.
	 */
	public static final String APP_NAME = "Semtix";
	
	
	/**
	 * Fortlaufende Versionsnummer der Anwendung (Build).
	 * Erscheint als Programmversion zusammen mit dem Datum der letzten Änderung im Dialog "über das Programm"
	 */
    public static final String APP_VERSION = "Produktiversion";


    /**
	 * Datum letzte Änderung der Anwendung (Build).
	 * Erscheint als Programmversion zusammen mit der Versionsnummer der Anwendung (Build) im Dialog "über das Programm"
	 */
    public static final String APP_BUILD_DATE = DeutschesDatum.DATUMSFORMAT.format(new GregorianCalendar().getTime());


    /**
     * Scrollgeschwindigkeit bei JPanels in Scrollpane.
	 * Bei einigen JPanels mit Scrollbalken war die Scrollgeschwindigkeit sehr langsam (Grund unbekannt).
	 * Mit dieser Konstante wird die Scrollgeschwindigkeit bei den ScrollPanes neu gesetzt.
	 */
	public static final int SCROLL_UNIT = 10;

    public static final int ANZAHL_FREIE_EINKOMMENSFELDER = 6;

    /**
     * Unbedingt den Pfadseparator am Ende beachten
     */
    public static String GLOBAL_CONF_DIR = "/etc/semtixdb/";

    public static String DEFAULT_PROPERTIES_NAME = "semtixconf.properties";

    public static String DEFAULT_PROPERTIES_GLOBAL = GLOBAL_CONF_DIR + DEFAULT_PROPERTIES_NAME;

	public static boolean SAVE_PERSON_ANYWAY = false;

}
