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

package org.semtix;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.semtix.config.*;
import org.semtix.db.DBHandlerConf;
import org.semtix.db.DBHandlerSemester;
import org.semtix.db.DBHandlerTextbausteine;
import org.semtix.db.DBHandlerUser;
import org.semtix.db.dao.Semester;
import org.semtix.db.dao.SemtixUser;
import org.semtix.gui.MainControl;
import org.semtix.gui.person.personensuche.DialogPersonSuche;
import org.semtix.shared.daten.enums.SemesterArt;
import org.semtix.shared.daten.enums.Uni;
import org.semtix.shared.daten.enums.UserStatus;
import org.xml.sax.InputSource;

import javax.swing.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

/**
 * Startklasse enthält die main-Methode, setzt das <i>Look and Feel</i> und initialisiert einige Werte.
 */
public class Semtix {

    private static String ALTERNATIVECONFIGPATH = "";

    Logger logger = Logger.getLogger(Semtix.class);


    /**
     * Startet die Anwendung und initialisiert zu Beginn einige Werte.
	 * @throws IOException Dateizugriffehler
	 * @throws ParseException Fehler beim Parsen
	 */
    public Semtix() throws IOException, ParseException {

        String path = SettingsExternal.LOG4JPATH;
        if (new File(path).exists())
            PropertyConfigurator.configure(path);
        else
            logger.warn("Benutze eingebaute log4j.properties, bitte /etc/semtixdb/log4j.properties setzen.");

        Properties einstellungen = null;

        if (ALTERNATIVECONFIGPATH.length() > 0) {

            try {
                einstellungen = PropertiesManagement.getProperties(ALTERNATIVECONFIGPATH);
                Settings.DEFAULT_PROPERTIES_GLOBAL = ALTERNATIVECONFIGPATH;
            } catch (IOException i) {
                try {
                    einstellungen = PropertiesManagement.getProperties(Settings.DEFAULT_PROPERTIES_GLOBAL);
                } catch (IOException e) {
                    logger.warn("Konnte weder " + ALTERNATIVECONFIGPATH + " noch " + Settings.DEFAULT_PROPERTIES_GLOBAL + " finden.");
                }
            }

        } else {
            try {
                einstellungen = PropertiesManagement.getProperties(Settings.DEFAULT_PROPERTIES_GLOBAL);
            } catch (IOException e) {
                logger.warn("Konnte " + Settings.DEFAULT_PROPERTIES_GLOBAL + " nicht finden.");
            }
        }

        if (einstellungen != null) {
            SettingsExternal.init(einstellungen);
        } else {
            logger.error("[Fehler] Bitte überprüfen Sie, ob /etc/semtixdb/semtixconf.properties vorhanden ist.");
        }


        // Konstante mit aktuellem User (= am System eingeloggter Benutzer) wird angelegt
        UserConf.init();

        // aktuelle Universität zu Programmstart auf HU setzen
        UniConf.setUniversitaet(Uni.HU);

        // Konfiguration zum Programmstart initialisieren
        SemesterConf.init();

        // Initialisieren der vorgegeben Werte für den Berechnungszettel
        Berechnung.init();


        //Überprüft den Hibernate-Modus für die automatische Datenbankerstellung und füllt beim Neuanlegen die DB mit Defaultwerten
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            InputSource inputSource = new InputSource(SettingsExternal.HIBERNATE_CONF_XML);
            //Besser ist noch: org.hibernate.cfg.Settings settings;    settings.isAutoCreateSchema()
            String result = xpath.evaluate("//property[@name='hibernate.hbm2ddl.auto']", inputSource);
            if (result.equalsIgnoreCase("create___disabled")) {
                //create all default database data

				JOptionPane.showMessageDialog(null, "Achtung: Die Datenbank wird überschrieben. Wenn Sie das nicht wollen, holen Sie Ihren Admin.");

                DBHandlerTextbausteine.createDefaultValues();

                DBHandlerConf.createDefaultValues();

                GregorianCalendar semesterAnfang = new GregorianCalendar(2015, Calendar.APRIL, 1);

                DBHandlerSemester dbhsemester = new DBHandlerSemester();
                Semester soseHU = new Semester();
                soseHU.setUni(Uni.HU);
                soseHU.setSemesterArt(SemesterArt.SOMMER);
                soseHU.setSemesterJahr("2015");
                soseHU.setSemesterAnfang(semesterAnfang);

                dbhsemester.createSemester(soseHU);

                Semester soseKW = new Semester();
                soseKW.setUni(Uni.KW);
                soseKW.setSemesterArt(SemesterArt.SOMMER);
                soseKW.setSemesterJahr("2015");
                soseKW.setBeitragFonds(new BigDecimal("10.00"));
                soseKW.setBeitragTicket(new BigDecimal("184.10"));
                soseKW.setSemesterAnfang(semesterAnfang);

                dbhsemester.createSemester(soseKW);

                //DBHandlerAntrag.importFromFile("/home/eins/daten/antraege", "\\$", 0);

                DBHandlerUser dbHandlerUser = new DBHandlerUser();

                SemtixUser user = new SemtixUser();
                user.setKuerzel("EINS");
                user.setLoginName("eins");
                user.setNachname("Einstein");
                user.setVorname("Albert");
                user.setStatus(UserStatus.AKTIV);
                dbHandlerUser.saveOrUpdateUser(user);

                SemtixUser user2 = new SemtixUser();
                user2.setKuerzel("XU");
                user2.setLoginName("xubuntu");
                user2.setNachname("Linux");
                user2.setVorname("Xubuntu");
                user2.setStatus(UserStatus.AKTIV);
                dbHandlerUser.saveOrUpdateUser(user2);

            }
        } catch(XPathExpressionException e) {
            logger.error("Parsen der Hibernate.cfg.xml", e);
        } catch (Exception e) {
            logger.error("Sonstige Exception", e);
        }

        // Objekt MainControl erzeugen
        MainControl mainControl = new MainControl();

        // Beim Start des HauptFrames wird standardmässig
        // der Dialog zur Personensuche angezeigt.
        new DialogPersonSuche(mainControl);


    }


    /**
     * main-Methode welche beim Start der Anwendung aufgerufen wird.
     *
     * @param args Programmparameter auf Kommandozeile
     */
    public static void main(String[] args) {

        if (null != args && args.length > 0 && new File(args[0]).exists())
            ALTERNATIVECONFIGPATH = args[0];
        else if (null != args && args.length > 0)
            System.out.println("[INFO] Sie können den Pfad zu 'semtixconf.properties' als Kommandozeilenparameter übergeben.");

        if (SettingsExternal.DEBUG)
            System.out.println("Verwende Konfigurationspfad: " + ALTERNATIVECONFIGPATH + " : : " + Settings.DEFAULT_PROPERTIES_GLOBAL + " (erste Angabe gilt) ");

        // Look&Feel setzen
        try {
//            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.setLookAndFeel(("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"));
//            UIManager.setLookAndFeel(("com.sun.java.swing.plaf.gtk.GTKLookAndFeel"));
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Semtix starten
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Semtix();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


    }

}
