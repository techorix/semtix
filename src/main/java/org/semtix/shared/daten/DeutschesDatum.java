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


package org.semtix.shared.daten;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Hilfsklasse für verschiedene Lokalisierungsmethoden
 *
 * Created by MM on 09.02.15.
 */
public class DeutschesDatum {

    public static final SimpleDateFormat DATUMSFORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public static final SimpleDateFormat ZEITSTEMPEL = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    /**
     * Liefert einen formatierten ZEITSTEMPEL
     * @param timestamp Zeitstempel
     * @return formatierter Zeitstempel
     */
    public static String getFormatiertenZeitstempel(GregorianCalendar timestamp) {

        // Zeitstempel formatieren, falls nicht null
        if(timestamp != null) {
            return ZEITSTEMPEL.format(timestamp.getTime());
        }
        // falls Zeitstempel = null, wird ein leerer String zurückgeliefert
        else
            return "";
    }

    /**
     * Macht aus einem BigDecimal einen Währungstext a la 1.200,00€
     * Wenn Betrag == null , BigDecimal.ZERO, 0 €
     * @param betrag Betrag
     * @return String des Betrags
     */
    public static String getEuroFormatted(BigDecimal betrag) {
		if (betrag == null) {
			betrag = BigDecimal.ZERO;
		}
		betrag.setScale(2, RoundingMode.HALF_DOWN);
		NumberFormat format = NumberFormat.getNumberInstance(Locale.GERMAN);
		format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        Currency currency = Currency.getInstance("EUR");
        format.setCurrency(currency);
        if (null == betrag)
            betrag = BigDecimal.ZERO;

        return format.format(betrag) + "€";

    }

    /**
     * Macht aus einem Zeitstempel ein neues normalisiertes Datum ohne Stunden, Minuten, Sekunden, MS
     *
     * @param timeStamp Zeitstempel Objekt
     * @return neues, normalisiertes Zeitstempel Objekt
     */
    public static GregorianCalendar reduceGregorianCalendarToDay(GregorianCalendar timeStamp) {
        timeStamp.set(GregorianCalendar.HOUR_OF_DAY, 0);
        timeStamp.set(GregorianCalendar.MINUTE, 0);
        timeStamp.set(GregorianCalendar.SECOND, 0);
        timeStamp.set(GregorianCalendar.MILLISECOND, 0);

        return timeStamp;
    }
}
