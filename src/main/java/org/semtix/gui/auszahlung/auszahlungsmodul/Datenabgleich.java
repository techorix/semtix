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

package org.semtix.gui.auszahlung.auszahlungsmodul;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.semtix.config.SemesterConf;
import org.semtix.db.DBHandlerDatenabgleich;
import org.semtix.db.dao.Person;
import org.semtix.shared.daten.enums.AntragAblehnungsgrund;
import org.semtix.shared.daten.enums.SemesterArt;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

/**
 * Für den Datenabgleich mit dem Immatrikulationsbüro müssen XLS Dateien geschrieben werden, die unseren Datenstand enthalten und danach eine
 * korrigierte Version vom Immatrikulationsbüro eingelesen werden, welche den Stand des Immatrikulationsbüros enthält
 * 
 * @author Michael
 */
public class Datenabgleich {

	private DBHandlerDatenabgleich dbhandler;

	public Datenabgleich() {
		dbhandler = new DBHandlerDatenabgleich();

	}

	/**
	 * Liest eine XLS Datei ein und gleicht sie mit der Datenbank ab
	 *
	 * @param path /Pfad/angabe/zur/Datei.xyz 
	 */
	public void einlesen(String path) {

		try {

			FileInputStream file = new FileInputStream(new File(path));

			// Get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(file);

			// Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows from first sheet
			Iterator<Row> rowIterator = sheet.iterator();

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				Cell[] cells = new Cell[row.getLastCellNum()];
				int i = 0;
				boolean exmatrikuliert = false;
				while (cellIterator.hasNext()) {

					Cell cell = cellIterator.next();
					cells[i] = cell;

					i++;

					if (i == 5 && cell.getCellType() == Cell.CELL_TYPE_STRING) {
						if (cell.getStringCellValue().equalsIgnoreCase("X")) {
							// if this cell contains an X we have to see if a)
							// Antrag is invalid b) Person is Teilimmatrikuliert
							// c) How many months Teilimmatrikuliert

							exmatrikuliert = true;

						}
					}
				}

				// we have to see if a) Antrag is invalid b) Person is
				// Teilimmatrikuliert c) How many months Teilimmatrikuliert
				if (exmatrikuliert) {
					Cell semesterCell = cells[6];

					// Semester aufschlüsseln nach Jahr und Typ
					String semesterJahrPerson = null;
					String semesterTypPerson = null;
					if (semesterCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						String semester = "" + semesterCell.getNumericCellValue();
						semesterJahrPerson = semester.substring(0, 4).trim();
						semesterTypPerson = semester.substring(4, 5).trim();
						if (semesterTypPerson.equals("1")) {
							semesterTypPerson = "S";
						} else if (semesterTypPerson.equals("2")) {
							semesterTypPerson = "W";
						}
					}

					// get current selected Semester
					String semesterTypAktuell = SemesterConf.getSemester().getSemesterArt().getBuchstabe().trim();
					String semesterJahrAktuell = SemesterConf.getSemester().getSemesterJahr().trim();

					// if its really the same semester get the date of
					// exmatriculation and check how many months
					if (semesterJahrAktuell.equalsIgnoreCase(semesterJahrPerson) && semesterTypAktuell.equalsIgnoreCase(semesterTypPerson)) {
						// get cell with exmatriculation date
						Cell exmatriculationDateCell = cells[5];

						// Find out date of exmatriculation / round up date to next month
						int exmatriculationmonth = 0;
						if (HSSFDateUtil.isCellDateFormatted(exmatriculationDateCell)) {
							Date date = exmatriculationDateCell.getDateCellValue();
							
//							Calendar cal = Calendar.getInstance();
//							cal.setTime(date);
//							int monat = cal.get(Calendar.MONTH);
							
							SimpleDateFormat df = new SimpleDateFormat("MM");
							exmatriculationmonth = Integer.parseInt(df.format(date));
							exmatriculationmonth++;
						}

						int monateZuschuss = 0;

						// Sommersemester
						if (semesterTypAktuell.equals("S")) {

							monateZuschuss = 6 - (10 - exmatriculationmonth);

							// Wintersemester
						} else if (semesterTypAktuell.equals("W")) {
							if (exmatriculationmonth > 4) {
								monateZuschuss = 6 - (16 - exmatriculationmonth);
							} else {
								monateZuschuss = 6 - (4 - exmatriculationmonth);
							}
						}

						if (monateZuschuss < 6) {

							Cell manrCell = cells[0];
							Cell nachnameCell = cells[2];
							Cell vornameCell = cells[3];

							String matrikelnummer = "" + manrCell.getNumericCellValue();
							matrikelnummer = matrikelnummer.substring(0, matrikelnummer.indexOf('.'));

							String nachname = getStringFromCell(nachnameCell);
							String vorname = getStringFromCell(vornameCell);

							// write Teilzuschuss to DB
							dbhandler.setAntragToTeilzuschuss(monateZuschuss, matrikelnummer);
						}
					} else {

						Cell manrCell = cells[0];
						Cell nachnameCell = cells[2];
						Cell vornameCell = cells[3];

						String matrikelnummer = "" + manrCell.getNumericCellValue();
						matrikelnummer = matrikelnummer.substring(0, matrikelnummer.indexOf('.'));

						String nachname = getStringFromCell(nachnameCell);
						String vorname = getStringFromCell(vornameCell);

						//
						dbhandler.denyAntrag(AntragAblehnungsgrund.EXMATRIKULIERT.getBegruendung(), matrikelnummer);

					}

				}

			}
			file.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String getStringFromCell(Cell cell) {
		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			return cell.getStringCellValue();
		}

		return null;
	}

	/**
	 * Schreibt eine CSV Datei an den angegebenen Pfad
	 *
	 * @param path Achtung File.separator benutzen!
     * @return anzahl der gefundenen Datenbankfehler bei der Operation
     *
     *             @throws FileNotFoundException Datei Nicht Gefunden
	 *             @throws UnsupportedEncodingException Encoding nicht unterstützt
	 */
    public int ausgeben(String path) throws FileNotFoundException, UnsupportedEncodingException {

		//Latin1-Encoding instead of "UTF-8":
		PrintWriter writer = new PrintWriter(path, "ISO-8859-1");
		//Windows Carriage Return
		// real CSV:
		writer.print("#,Semester,Geburtsdatum,Nachname,Vorname,Matrikelnummer \r\n");


		String aktuellesSemester = SemesterConf.getSemester().getSemesterJahr();
		if (SemesterConf.getSemester().getSemesterArt() == SemesterArt.SOMMER) {
			aktuellesSemester += "1";
		} else {
			aktuellesSemester +="2";
		}

		List<Person> students = dbhandler.getListeAntragsteller();

		int i=0;

        int fehler = 0;

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

        // besser traditionelle for-schleife nehmen hier
        for (Person student : students) {
			if (null != student) {
				i++;

				String geburtsdatum = "00.00.0000";
				String vorname = "";
				String nachname = "";
				String matrikelnummer = "0000";

				//Überspringt alle Einträge mit falschen Werten
				try {
					GregorianCalendar cal = student.getGebdatum();

					geburtsdatum = df.format(cal.getTime());

					vorname = student.getVorname();

					nachname = student.getNachname();

					matrikelnummer = student.getMatrikelnr();

					writer.print(Integer.toString(i) + "," + aktuellesSemester + "," + geburtsdatum + "," + nachname + "," + vorname + "," + matrikelnummer + "\r\n");


				} catch (Exception e) {
					fehler++;
				}
			}
		}

		writer.close();

        return fehler;
    }

}
