/*
 * Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 *        Semesterticketbüro der Humboldt-Universität Berlin
 *
 * Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
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

package org.semtix.shared.elements.control;

import org.semtix.shared.daten.DeutschesDatum;

import javax.swing.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * InputVerifier zur Überprüfung des Textfeldes fürs Geburtsdatum.  Der InputVerifier überprüft 
 * die Gültigkeit der eingegebenen Zeichen nach Verlassen des Textfeldes (Fokusverlust).
 *
 *
 */
public class InputDateVerifier
extends InputVerifier {


	@Override
	public boolean verify(JComponent input) {
		
		// Rückgabewert (gültig = true)
		boolean returnValue = true;
		
		// Übergebene Komponente ist Textfeld
		JTextField tf = (JTextField) input;
		
		// Datumsstring aus Textfeld holen
		String datum = tf.getText().trim();
		
		// Zähler für Punkte im Datumsstring
		int counter = 0;
		
		char search = '.';
        char search2 = ',';

		String day = "";
		String month = "";
		String year = "";
		
		// Anzahl der Punkte zählen
		for (int i=0; i<datum.length(); i++){
            if (datum.charAt(i) == search || datum.charAt(i) == search2) counter++;
        }
		
		
		// leeres Geburtsdatumsfeld kann verlassen werden
		if(datum.equals("")) {
			return true;
		}
			
		// keine Punkte im Datumsstring
        else if (counter == 0 && (datum.length() == 6 || datum.length() == 8)) {

			// der Tag sind die Zeichen 1 und 2
			day = datum.substring(0, 2);
			
			// der Monat sind die Zeichen 3 und 4
			month = datum.substring(2, 4);

            if (datum.length() == 6)
                year = convertYear(datum.substring(4, 6));
            else if (datum.length() == 8)
                year = datum.substring(4, 8);
		}
		// 2 Punkte im Datumsstring
		else if (counter == 2) {
			
			// Datumstring an Punkten in Tag, Monat und Jahr splitten
            String[] splitArray = null;
            if (datum.contains(".")) {
                splitArray = datum.split("\\.");
            } else {
                splitArray = datum.split(",");
            }

            day = splitArray[0];
			month = splitArray[1];
			year = splitArray[2];
			
			// Abbruch wenn Tag oder Monat grösser als 2 Zeichen
			if(day.length() > 2 || month.length() > 2) {
				
				showDialogFormat(datum);
				
				return false;
				
			}
			
			if(year.length() == 2 || year.length() == 4) {
				
				// Falls Tag nur aus einer Zahl, führende Null anfügen
				if(day.length()==1) {
					day = "0" + day;
				}
				
				// Falls Monat nur aus einer Zahl, führende Null anfügen
				if(month.length()==1) {
					month = "0" + month;
				}
				
				// Falls Jahr nur aus 2 Ziffern, auf 4 Ziffern ergänzen
				if(year.length()==2) {
					year = convertYear(splitArray[2]);
					//splitArray[2] = "19" + splitArray[2];
				}					
				
			}
			// Abbruch wenn Jahr nicht 2 oder 4 Zeichen hat
			else {
				
				showDialogFormat(datum);
				
				return false;
				
			}
				
			
					
			
		}
		// Abbruch wenn Datumsstring nicht leer und nicht ohne oder mit 2 Punktne ist
		else {
			
			showDialogFormat(datum);
			
			return false;
			
		}
		
		
		// Standarddatum (TT.MM.JJJJ)
		datum = day + "." + month + "." + year;
		
		// zuerst auf gültiges Datum überprüfen
		//returnValue = checkDate(tf.getText());
		returnValue = checkDate(datum);
		
		// falls gültiges Datum, auf Altersspanne überprüfen, in welcher das Alter liegen darf
		if(returnValue)
			returnValue = checkAge(datum);
		
		if(returnValue)
			tf.setText(datum);
			
		return returnValue;
		



	}
	
	
	// Überprüft Datumsstring auf gültiges Datum
	private boolean checkDate(String datum) {

		// Parsen des Datums. Wenn ungültig, fliegt ParseException...
		try {
            DeutschesDatum.DATUMSFORMAT.parse(datum);
            return true;
		} catch (ParseException e) {
			showMessageDialog("Kein gültiges Datum: " + datum);
			e.printStackTrace();
			return false;
		}

	}
	
	
	// Überprüft Datumsstring auf zulässige Altersspanne
	private boolean checkAge(String datum) {

        // Datumstring an Punkten in Tag, Monat und Jahr splitten
		String[] splitArray = datum.split("\\.");
		
		int aktuellesJahr = new GregorianCalendar().get(Calendar.YEAR);
		int alter = aktuellesJahr - Integer.parseInt(splitArray[2]);

		if(alter < 14 || alter > 100) {
			showMessageDialog("Alter nur zwischen 14 und 100 möglich! (Alter: " + alter + ")");
			return false;			
		}
		else
			return true;
			
	}
	
	
	// 2-stellige in 4-stellige Jahreszahl umwandeln
	private String convertYear(String year) {
		
		
		Calendar referenceYear = new GregorianCalendar();

        referenceYear.add(Calendar.YEAR, -14);

        String yearTemp = "" + referenceYear.get(Calendar.YEAR);
		String prefix = yearTemp.substring(0, 2);
		String birthYearTemp = prefix + year;


        Calendar birthYear = new GregorianCalendar();
        birthYear.set(Calendar.YEAR, Integer.valueOf(birthYearTemp));


        if(birthYear.after(referenceYear)) {
			birthYear.add(Calendar.YEAR, -100);
		}


        return "" + birthYear.get(Calendar.YEAR);
		
	}
	
	
	
	// Dialog mit Fehlermeldung anzeigen
	private void showMessageDialog(String message) {

        JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);

    }
	
	
	// Fehlermeldung bei falschem Datumsformat
	private void showDialogFormat(String datum) {
		
		String message = "<html>Das eingegebene Geburtsdatum<br>" +
				"<b>" + datum + "</b> hat ein falsches Format.<br><br>" +
				"Zulässige Formate:<br>" +
				"ohne Punkte: <b>TTMM(JJ)JJ</b><br>" +
				"mit 2 Punkten: <b>(T)T.(M)M.(JJ)JJ</b></html>";

        JOptionPane.showMessageDialog(null, message, "Falsches Format", JOptionPane.ERROR_MESSAGE);

    }

}
