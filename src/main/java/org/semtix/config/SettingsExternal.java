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


import org.apache.log4j.Logger;
import org.semtix.shared.daten.enums.Uni;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Einstellungen für die gesamte Anwendung.
 *
 * Die Werte werden aus der lokalen Properties-Datei gelesen.
 *
 *
 *
 *
 * Falls eine Datei noch nicht existiert, ist es ein Trick die Werte für die Templatepfade im Adminpanel zu änderun und zu speichern. Dann wird auch die Properties-Datei mit den meisten Default-Werten angelegt.
 *
 * Achtung: 'ausgabepfad' und 'vorlagenpfad' sind am Anfang auf _null_ gesetzt und müssen Manuell geschrieben werden.
 */
public class SettingsExternal {

    /**
     * Maximale Anzahl an Tabs mit Personendaten, die in der Anwendung geöffnet sein können.
     */
    public static int MAXTABS = 5;


    /**
     * Globale Debugging Variable : wenn true, dann werden Debugs in Konsole ausgegeben
	 */
    public static boolean DEBUG = false;
    /**
     * Pfad für Homedir
     */
    public static String HOMEDIR = System.getProperty("user.home");

    /**
     * Pfad für Template-Dateien
     */
    public static String TEMPLATE_PATH = null;
	/**
     * Pfad für Ausgabedateien
     */
    public static String OUTPUT_PATH = null;
    /**
     * Pfad für PDF-Dateien beim Drucken von Antraegen
     */
    public static String PDF_PATH = null;
    /**
     * Pfad für Hibernate-Settings
     */
    public static String HIBERNATE_CONF_XML = Settings.GLOBAL_CONF_DIR + "hibernate.cfg.xml";
    /**
     * Dateinamen der Templates
     */
    public static String TEMPLATE_ANTRAG_ABGELEHNT = "";
    public static String TEMPLATE_ANTRAG_BAR = "";
    public static String TEMPLATE_ANTRAG_KONTO = "";
    public static String TEMPLATE_ANTRAG_BAR_KULANZ = "";
    public static String TEMPLATE_ANTRAG_KONTO_KULANZ = "";
    public static String TEMPLATE_ANTRAG_ABGELEHNT_KULANZ = "";

    public static String TEMPLATE_AZA = "";
    public static String TEMPLATE_AZA_HU = "";
    public static String TEMPLATE_AZA_CHARITE = "";
    public static String TEMPLATE_AZA_FINREF = "";
    public static String DECKBLATT_DATEI = "";
    public static String DECKBLATT_DATEI_KW = "";
    public static String DECKBLATT_DATEI_HU = "";
    public static String TEMPLATE_NACHFRAGE_DE = "";
    public static String TEMPLATE_NACHFRAGE_EN = "";
    public static String TEMPLATE_MAHNUNG_DE = "";
    public static String TEMPLATE_MAHNUNG_EN = "";
    /**
     * Pfad zu den LOG4-Properties, damit man sie auch nach /etc/ packen kann
     */
    public static String LOG4JPATH = "/etc/semtixdb/log4j.properties";

    /**
     * Layout-Modus mit Farben die die Panels unterscheiden
     */
    public static boolean CHANGELAYOUTMODE = false;

    /**
     * Wenn dieser Wert 0 ist, wird nur die Eingabe genau einer Sonstigen Härte ermöglicht
     * <br>
     * Dieser Wert sollte nicht über 7 gesetzt werden wegen Layoutproblemen
     */
    public static int ZUSAETZLICHE_SONSTIGE_HAERTEN = 4;


    static Logger logger = Logger.getLogger(SettingsExternal.class);


    public static String gettemplateAntragAbgelehnt() {
        return SettingsExternal.TEMPLATE_PATH + "/" + TEMPLATE_ANTRAG_ABGELEHNT;
    }

    public static String gettemplateAntragBar() {
        return SettingsExternal.TEMPLATE_PATH + "/" + TEMPLATE_ANTRAG_BAR;
    }


    public static String gettemplateAntragKonto() {
        return SettingsExternal.TEMPLATE_PATH + "/" + TEMPLATE_ANTRAG_KONTO;

    }

    public static String getTemplateAntragAbgelehntKulanz() {
        return SettingsExternal.TEMPLATE_PATH + "/" + TEMPLATE_ANTRAG_ABGELEHNT_KULANZ;
    }

    public static String getTemplateAntragBarKulanz() {
        return SettingsExternal.TEMPLATE_PATH + "/" + TEMPLATE_ANTRAG_BAR_KULANZ;
    }


    public static String getTemplateAntragKontoKulanz() {
        return SettingsExternal.TEMPLATE_PATH + "/" + TEMPLATE_ANTRAG_KONTO_KULANZ;
    }

    public static String gettemplateAza() {
        return SettingsExternal.TEMPLATE_PATH + "/" + TEMPLATE_AZA;
    }

    public static String gettemplateAzaHu() {
        return SettingsExternal.TEMPLATE_PATH + "/" + TEMPLATE_AZA_HU;
    }

    public static String gettemplateAzaCharite() {
        return SettingsExternal.TEMPLATE_PATH + "/" + TEMPLATE_AZA_CHARITE;
    }

    public static String gettemplateAzaFinref() {
        return SettingsExternal.TEMPLATE_PATH + "/" + TEMPLATE_AZA_FINREF;
    }

    public static String getdeckblattDatei() {
        StringBuilder path = new StringBuilder(SettingsExternal.TEMPLATE_PATH + File.separator);
        if (UniConf.aktuelleUni == Uni.HU) {
            path.append(DECKBLATT_DATEI_HU);
        } else {
            path.append((DECKBLATT_DATEI_KW));
        }

        if (new File(path.toString()).exists()) {
            return path.toString();
        } else {
            return TEMPLATE_PATH + File.separator + DECKBLATT_DATEI;
        }
    }

    public static String gettemplateNachfrageDe() {
        return SettingsExternal.TEMPLATE_PATH + "/" + TEMPLATE_NACHFRAGE_DE;
    }

    public static String gettemplateNachfrageEn() {
        return SettingsExternal.TEMPLATE_PATH + "/" + TEMPLATE_NACHFRAGE_EN;
    }

    public static String gettemplateMahnungDe() {
        return SettingsExternal.TEMPLATE_PATH + "/" + TEMPLATE_MAHNUNG_DE;
    }

    public static String gettemplateMahnungEn() {
        return SettingsExternal.TEMPLATE_PATH + "/" + TEMPLATE_MAHNUNG_EN;
    }

    public static void init(Properties einstellungen) {

        if(HOMEDIR.length()==0) {
            HOMEDIR = System.getProperty("user.dir");
        }
        try {
            String ausgabepfad = einstellungen.getProperty("ausgabepfad").replaceAll("\\$HOME", HOMEDIR);
            if (ausgabepfad.length() > 0) {
                SettingsExternal.OUTPUT_PATH = ausgabepfad;
            }
        } catch (NullPointerException npe) {
            logger.warn("Ausgabepfad nicht in Properties angegeben.");
        }

        try {
            String ausgabepfad = einstellungen.getProperty("pdfpfad").replaceAll("\\$HOME", HOMEDIR);
            if (ausgabepfad.length() > 0) {
                SettingsExternal.PDF_PATH = ausgabepfad;
            }
        } catch (NullPointerException npe) {
            logger.warn("PDF-Ausgabepfad nicht in Properties angegeben.");
        }

        try {
            String vorlagenpfad = einstellungen.getProperty("vorlagenpfad").replaceAll("\\$HOME", HOMEDIR);
            if (vorlagenpfad.length() > 0) {
                SettingsExternal.TEMPLATE_PATH = vorlagenpfad;
            }
        } catch (NullPointerException npe) {
            logger.warn("Vorlagenpfad nicht in Properties angegeben.");
        }

        try {
            String hibernatepfad = einstellungen.getProperty("hibernatepfad");
            if (hibernatepfad.length() > 0) {
                SettingsExternal.HIBERNATE_CONF_XML = hibernatepfad;
            }
        } catch (NullPointerException npe) {
            logger.warn("Hibernatepfad nicht in Properties angegeben.");
        }

        try {
            String log4jpfad = einstellungen.getProperty("log4jpfad");
            if (log4jpfad.length() > 0) {
                LOG4JPATH = log4jpfad;
            }
        } catch (NullPointerException npe) {
            logger.warn("LOG4J-Pfad nicht in Properties angegeben.");
        }

        try {
            if (einstellungen.getProperty("colors").length() > 0)
                CHANGELAYOUTMODE = Boolean.parseBoolean(einstellungen.getProperty("colors"));
        } catch (Exception e) {
            logger.warn("Feld colors sollte ein boolean sein. Also z.B. 'true' oder 'false'");
        }

        try {
            if (einstellungen.getProperty("max.tabs").length() > 0)
                MAXTABS = Integer.parseInt(einstellungen.getProperty("max.tabs"));
        } catch (Exception e) {
            logger.warn("Feld max.tabs sollte eine Ganzzahl sein. Also z.B. 7");
        }

        try {
            if (einstellungen.getProperty("debug").length() > 0)
                DEBUG = Boolean.parseBoolean(einstellungen.getProperty("debug"));
        } catch (Exception e) {
            logger.warn("Feld debug sollte ein boolean sein. Also z.B. 'true' oder 'false'");
        }

        try {
            if (einstellungen.getProperty("add.sonstige.haerten").length() > 0)
                ZUSAETZLICHE_SONSTIGE_HAERTEN = Integer.parseInt(einstellungen.getProperty("add.sonstige.haerten"));
        } catch (Exception e) {
            logger.warn("Feld add.sonstige.haerten sollte eine Ganzzahl zwischen 0 und 7 sein.");
        }

        if (null == OUTPUT_PATH)
            logger.warn("Bitte überprüfen Sie ob 'ausgabepfad=<pfad>' in semtixconf.properties angegeben ist.");

        if (null == PDF_PATH)
            logger.warn("Bitte überprüfen Sie ob 'pdfpfad=<pfad>' in semtixconf.properties angegeben ist.");

        if (null == TEMPLATE_PATH)
            logger.warn("Bitte überprüfen Sie ob 'vorlagenpfad=<pfad>' in semtixconf.properties angegeben ist.");

        if (null == HIBERNATE_CONF_XML)
            logger.error("Bitte überprüfen Sie ob 'hibernatepfad=<pfad>' in semtixconf.properties angegeben ist. Hibernate ist für diese Anwendung zwingend notwendig.");


        try {
            if (einstellungen.getProperty("vorlage.bescheid.ablehnung").length() > 0)
                TEMPLATE_ANTRAG_ABGELEHNT = einstellungen.getProperty("vorlage.bescheid.ablehnung");
            if (einstellungen.getProperty("vorlage.bescheid.bar").length() > 0)
                TEMPLATE_ANTRAG_BAR = einstellungen.getProperty("vorlage.bescheid.bar");
            if (einstellungen.getProperty("vorlage.bescheid.konto").length() > 0)
                TEMPLATE_ANTRAG_KONTO = einstellungen.getProperty("vorlage.bescheid.konto");
            if (einstellungen.getProperty("vorlage.auszahlungsanordnung").length() > 0)
                TEMPLATE_AZA = einstellungen.getProperty("vorlage.auszahlungsanordnung");
            if (einstellungen.getProperty("vorlage.auszahlungsanordnung.hu").length() > 0)
                TEMPLATE_AZA_HU = einstellungen.getProperty("vorlage.auszahlungsanordnung.hu");
            if (einstellungen.getProperty("vorlage.auszahlungsanordnung.charite").length() > 0)
                TEMPLATE_AZA_CHARITE = einstellungen.getProperty("vorlage.auszahlungsanordnung.charite");
            if (einstellungen.getProperty("vorlage.auszahlungsanordnung.finref").length() > 0)
                TEMPLATE_AZA_FINREF = einstellungen.getProperty("vorlage.auszahlungsanordnung.finref");
            if (einstellungen.getProperty("deckblatt.auszahlungsdatei").length() > 0) {
                DECKBLATT_DATEI = einstellungen.getProperty("deckblatt.auszahlungsdatei");
            }
            if (einstellungen.getProperty("deckblatt.auszahlungsdatei.hu").length() > 0) {
                DECKBLATT_DATEI_HU = einstellungen.getProperty("deckblatt.auszahlungsdatei.hu");
            }
            if (einstellungen.getProperty("deckblatt.auszahlungsdatei.kw").length() > 0) {
                DECKBLATT_DATEI_KW = einstellungen.getProperty("deckblatt.auszahlungsdatei.kw");
            }
            if (einstellungen.getProperty("vorlage.nachfage.EN").length() > 0) {
                TEMPLATE_NACHFRAGE_EN = einstellungen.getProperty("vorlage.nachfage.EN");
            }
            if (einstellungen.getProperty("vorlage.nachfage.DE").length() > 0) {
                TEMPLATE_NACHFRAGE_DE = einstellungen.getProperty("vorlage.nachfage.DE");;
            }
            if (einstellungen.getProperty("vorlage.mahnung.EN").length() > 0) {
                TEMPLATE_MAHNUNG_EN = einstellungen.getProperty("vorlage.mahnung.EN");
            }
            if (einstellungen.getProperty("vorlage.mahnung.DE").length() > 0) {
                TEMPLATE_MAHNUNG_DE = einstellungen.getProperty("vorlage.mahnung.DE");
            }
        } catch (NullPointerException npe) {
            logger.warn("Eine oder mehrere Vorlagen sind nicht angegeben. Programm verwendet in diesen Fällen die voreingestellten Dateinamen.");
        }
    }

    public static void einstellungenSpeichern() throws IOException {

        Properties einstellungen = new Properties();

        if (!(null == OUTPUT_PATH)) einstellungen.setProperty("ausgabepfad", OUTPUT_PATH);
        else
            System.out.println("Bitte Varable 'ausgabepfad' manuell in " + Settings.DEFAULT_PROPERTIES_GLOBAL + " setzen.");

        if (!(null == PDF_PATH)) einstellungen.setProperty("ausgabepfad", PDF_PATH);
        else
            System.out.println("Bitte Varable 'pdfpfad' manuell in " + Settings.DEFAULT_PROPERTIES_GLOBAL + " setzen.");

        if (!(null == TEMPLATE_PATH)) einstellungen.setProperty("vorlagenpfad", TEMPLATE_PATH);
        else
            System.out.println("Bitte Varable 'vorlagenpfad' manuell in " + Settings.DEFAULT_PROPERTIES_GLOBAL + " setzen.");

        einstellungen.setProperty("hibernatepfad", HIBERNATE_CONF_XML);

        einstellungen.setProperty("log4jpfad", LOG4JPATH);

        einstellungen.setProperty("vorlage.bescheid.ablehnung", TEMPLATE_ANTRAG_ABGELEHNT);
        einstellungen.setProperty("vorlage.bescheid.bar", TEMPLATE_ANTRAG_BAR);
        einstellungen.setProperty("vorlage.bescheid.konto", TEMPLATE_ANTRAG_KONTO);

        einstellungen.setProperty("vorlage.auszahlungsanordnung", TEMPLATE_AZA);
        einstellungen.setProperty("vorlage.auszahlungsanordnung.hu", TEMPLATE_AZA_HU);
        einstellungen.setProperty("vorlage.auszahlungsanordnung.finref", TEMPLATE_AZA_FINREF);

        einstellungen.setProperty("deckblatt.auszahlungsdatei", DECKBLATT_DATEI);
        einstellungen.setProperty("deckblatt.auszahlungsdatei.hu", DECKBLATT_DATEI_HU);
        einstellungen.setProperty("deckblatt.auszahlungsdatei.kw", DECKBLATT_DATEI_KW);

        einstellungen.setProperty("vorlage.nachfage.DE", TEMPLATE_NACHFRAGE_DE);
        einstellungen.setProperty("vorlage.nachfage.EN", TEMPLATE_NACHFRAGE_EN);

        einstellungen.setProperty("vorlage.mahnung.DE", TEMPLATE_MAHNUNG_DE);
        einstellungen.setProperty("vorlage.mahnung.EN", TEMPLATE_MAHNUNG_EN);

        einstellungen.setProperty("colors", "" + CHANGELAYOUTMODE);

        einstellungen.setProperty("debug", "" + DEBUG);

        einstellungen.setProperty("max.tabs", "" + MAXTABS);

        einstellungen.setProperty("log4jpfad", LOG4JPATH);

        einstellungen.setProperty("add.sonstige.haerten", "" + ZUSAETZLICHE_SONSTIGE_HAERTEN);

        PropertiesManagement.saveProperties(einstellungen, Settings.DEFAULT_PROPERTIES_GLOBAL, "Einstellungen Semtix");
    }
}
