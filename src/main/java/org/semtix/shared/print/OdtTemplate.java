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

package org.semtix.shared.print;


import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.document.SyntaxKind;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import org.semtix.config.SettingsExternal;
import org.semtix.db.DBHandlerConf;
import org.semtix.db.DBHandlerPerson;
import org.semtix.db.DBHandlerSemester;
import org.semtix.db.dao.*;
import org.semtix.shared.daten.DeutschesDatum;
import org.semtix.shared.daten.MyException;
import org.semtix.shared.daten.enums.AntragStatus;
import org.semtix.shared.daten.enums.HaerteAblehnungsgrund;
import org.semtix.shared.daten.enums.Vorgangsart;

import javax.swing.*;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Klasse für das Drucken über Odt (Open|Libre) Office Templates
 *
 * Bescheide: Ablehnungsbescheide, Barauszahlungsbescheide und Kontoauszahlungsbescheide werden per Vorlage ausgewählt
 * und auch daraufhin geprüft, ob Kulanz vorliegt.
 *
 * @author Michael Mertins
 */
public class OdtTemplate {


	//Hilfsmethode Standardardadresse setzen usw.
	private static Map<String, Object> initData(Person person, Semester semester) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("vorname", person.getVorname());
		data.put("nachname", person.getNachname());
		data.put("strasse", person.getStrasse());
		data.put("plz", person.getPlz());
		data.put("wohnort", person.getWohnort());
		data.put("matrikelnr", person.getMatrikelnr());
		data.put("semester", semester.getSemesterBezeichnung());

		if (null != person.getCo() && person.getCo().length() > 0)
			data.put("co", person.getCo());

		if (null != person.getWohneinheit() && person.getWohneinheit().length() > 0)
			data.put("zusatz", person.getWohneinheit());

		return data;


	}

	/**
	 * Wählt je nach Antragstyp das richtige Template aus, füllt die darin enthaltenen Platzhalter mit Antragsdaten und
	 * gibt den Pfad des erzeugten Exemplars zurück
	 *
	 * Anmerkung für Entwickler: boolean Antrag.isCharte() wird bereits ausgewertet
	 *
	 * @param antrag                       Antrag
	 * @param istAuszahlungUndKeinBescheid Auszahlung: true, Bescheid: false
	 * @param toTemp soll der Outputfile in den Temp-Ordner geschrieben werden ja/nein
	 * @return pfad der erzeugten Datei
	 * @throws IOException Dateizugrifffehler
	 */
	public static String generateOutputFile(Antrag antrag, Boolean istAuszahlungUndKeinBescheid, Boolean toTemp) throws IOException {

		Person person = new DBHandlerPerson().readPerson(antrag.getPersonID());

		Semester semester = new DBHandlerSemester().readSemester(antrag.getSemesterID());

		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-mm-dd");
		String dateFormatted = fmt.format(now.getTime());
		Map<String, Object> data = initData(person, semester);

		BigDecimal vollzuschuss = semester.getSozialfonds().add(semester.getBeitragTicket());
		BigDecimal pw;
		if (null == semester.getPunktWert()) {
			pw = BigDecimal.ZERO;
		} else {
			pw = semester.getPunktWert();
		}
		if (pw.compareTo(BigDecimal.ZERO) <= 0) {
			pw = vollzuschuss;
		}
		Integer summepunkte = antrag.getPunkteEinkommen() + antrag.getPunkteHaerte();

		if (antrag.isTeilzuschuss()) {
			Double d = Double.valueOf(antrag.getAnzahlMonate()) / 6;
			data.put("betrag", DeutschesDatum.getEuroFormatted(vollzuschuss.multiply(new BigDecimal(d))));
		} else {
			BigDecimal betrag = pw.multiply(new BigDecimal(summepunkte));
			if (betrag.compareTo(vollzuschuss) >= 0) {
				data.put("betrag", DeutschesDatum.getEuroFormatted(vollzuschuss));
			} else {
				data.put("betrag", DeutschesDatum.getEuroFormatted(betrag));
			}
		}

		String nachname = getNachname(antrag.getPersonID()).replaceAll("\\s","_");
		String vorname  = getVorname(antrag.getPersonID()).replaceAll("\\s","_");
		String fileName = "bescheid_" + nachname + "_" + vorname + "_" + dateFormatted + ".odt";
		String templatePath;

		if (istAuszahlungUndKeinBescheid) {


			//bei AZA kein c/o oder WEN oder so:

			if (data.containsKey("zusatz"))
				data.remove("zusatz");

			if (data.containsKey("co"))
				data.remove("co");

// azas ebenfalls nach nachnamen sortieren, analog zu bescheiden (s.o.)
// vornamen mit einbeziehen

			fileName = "aza_" + nachname +
					"_" + vorname + "_" + dateFormatted + ".odt";

			templatePath = SettingsExternal.gettemplateAza();


			if (antrag.isNothilfe())
				templatePath = SettingsExternal.gettemplateAzaFinref();
			if (antrag.isRaten())
				templatePath = SettingsExternal.gettemplateAzaHu();
			if (antrag.isCharite())
				templatePath = SettingsExternal.gettemplateAzaCharite();


			if (antrag.isManAuszahlen()) {
				data.put("aa", "N");
				data.put("iban", "");
				data.put("bic", "");
			} else {
				data.put("iban", person.getIBAN());
				data.put("bic", person.getBIC());
				data.put("aa", "J");
			}


			DBHandlerConf dbHandlerConf = new DBHandlerConf();
			String nr = "1";
			try {
				String temp = dbHandlerConf.read("soznr");
				int soznr = Integer.parseInt(temp);
				if (soznr > 0) {
					nr = "" + (++soznr);
					dbHandlerConf.update("soznr", nr);
				}
			} catch (NumberFormatException e) {
				dbHandlerConf.update("soznr", nr);
			} catch (NullPointerException npe) {
				dbHandlerConf.update("soznr", nr);
			} catch (Exception e) {
				dbHandlerConf.update("soznr", nr);
			}

			data.put("soz_nummer", nr);


		} else {

			data.put("next_semester", semester.getNextSemesterBezeichnung());
			data.put("next_antragsfrist", semester.getNextAntragFrist());
			if (antrag.isKulanz())
				data.put("kulanz", "Kulanz");


			//Antrag abgelehnt?
			if (antrag.getAntragStatus() == AntragStatus.ABGELEHNT) {
				templatePath = SettingsExternal.gettemplateAntragAbgelehnt();
				data.put("begruendung_ablehnung", antrag.getBegruendung());

				//Antrag angenommen
			} else {
				data.put("zusaetzliche_haerten", "text § t");

				try {
					data.put("punkte_einkommen", Integer.toString(antrag.getPunkteEinkommen()));
					data.put("punkte_haerte", Integer.toString(antrag.getPunkteHaerte()));
					data.put("punktwert", DeutschesDatum.getEuroFormatted(pw));
					data.put("vollzuschuss", DeutschesDatum.getEuroFormatted(vollzuschuss));
					data.put("punkte_voll", Integer.toString(semester.getPunkteVoll()));


				} catch (NullPointerException npe) {
					JOptionPane.showMessageDialog(null, "Antrag oder Semester enthält null-werte");
					if (SettingsExternal.DEBUG)
						npe.printStackTrace();
				}

				boolean hasHaerte = false;
				StringBuilder haerten = new StringBuilder("Neben Angaben zu deinem Bedarf und deinen Einkünften hast du uns folgende besondere Härte(n) genannt:\n");
				for (AntragHaerte haerte : antrag.getHaerteListe()) {
					if (haerte.isAnerkannt() || haerte.isAbgelehnt()) {
						hasHaerte = true;
						haerten.append("\t- " + haerte.getHaertegrund().getHaertegrundText() + "\n");
					}
				}
				haerten.append("\n");

				if (hasHaerte) {
					data.put("haerten", haerten.toString());
				}

				StringBuilder ablehnungsgrund = new StringBuilder("Begründung für (nicht) anerkannte Härte(n): <br />");
				StringBuilder ahaerten = new StringBuilder("Aufgrund der von dir erbrachten Nachweise werden folgende besondere Härten anerkannt und berücksichtigt:\n");

				boolean hasAblehnungsgrund = false;
				boolean hasAhaerte = false;
				for (AntragHaerte haerte : antrag.getHaerteListe()) {
					if (haerte.isAnerkannt()) {
						hasAhaerte = true;
						ahaerten.append("\t- " + haerte.getHaertegrund().getHaertegrundText() + "\n");
					} else if (haerte.isAbgelehnt()) {
						hasAblehnungsgrund = true;
						ablehnungsgrund.append("-<b>" + haerte.getHaertegrund().getHaertegrundText() + "</b>: " + HaerteAblehnungsgrund.getHaertegrundByID(haerte.getAblehnungsID()).getBegruendung() + "<br />");
					}
				}
//				ablehnungsgrund.append("<br />");
				ahaerten.append("\n");

				if (hasAblehnungsgrund) {
					data.put("ablehnungsgrund", ablehnungsgrund.toString());
				} else {
					data.put("ablehnungsgrund", "");
				}
				if (hasAhaerte) {
					data.put("ahaerten", ahaerten.toString());
				}

				if (antrag.isTeilzuschuss()) {
					StringBuilder zuschuss = new StringBuilder(antrag.getBegruendung());
					zuschuss.append(" " + data.get("betrag"));
					data.put("teilzuschuss", zuschuss.toString());
				}

				if (antrag.isManAuszahlen()) {
					templatePath = SettingsExternal.gettemplateAntragBar();
				} else {
					templatePath = SettingsExternal.gettemplateAntragKonto();
					data.put("iban", person.getIBAN());
					data.put("bic", person.getBIC());

					if (!(null == person.getKontoInhaber_Name() || person.getKontoInhaber_Name().length() == 0))
						data.put("kontoinhaber", person.getKontoInhaber_Name() + ", " + person.getKontoInhaber_Strasse() + ", " + person.getKontoInhaber_Wohnort());

				}
			}

		}

		String pfad = SettingsExternal.OUTPUT_PATH;
		if (toTemp) {
			pfad = System.getProperty("java.io.tmpdir");
		}

		fileName = pfad + File.separator + fileName;

		if (SettingsExternal.DEBUG)
			System.out.println("Drucke Datei " + templatePath + " als " + fileName);

		return fillTemplate(templatePath, data, fileName);


	}

	private static String getVorname(int personID) {
		DBHandlerPerson dbHandlerPerson = new DBHandlerPerson();
		return dbHandlerPerson.getPersonById(personID).getVorname();
	}

	private static String getNachname(int personID) {
		DBHandlerPerson dbHandlerPerson = new DBHandlerPerson();
		return dbHandlerPerson.getPersonById(personID).getNachname();
	}

	/**
	 * Erzeugt den Outputfile mit Hilfe des Templates und gibt dessen Pfad zurück, damit dieser gedruckt werden kann
	 *
	 * @param templatePath Pfad
	 * @param data         Daten
	 * @param pfad         Ausgabepfad
	 * @return Pfad des Outputfiles
	 * @throws IOException when File not Found
	 */
	private static String fillTemplate(String templatePath, Map<String, Object> data, String pfad) throws IOException {

		try {
			// 1) Load ODT file and set Velocity template engine and cache it to the registry
			InputStream in = new FileInputStream(new File(templatePath));
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Freemarker);

			// 1b) Create fields metadata to manage text styling
			FieldsMetadata metadata = report.createFieldsMetadata();
			metadata.addFieldAsTextStyling("text", SyntaxKind.Html);
//			metadata.addFieldAsTextStyling("haerten", SyntaxKind.Html);
//			metadata.addFieldAsTextStyling("ahaerten", SyntaxKind.Html);
			metadata.addFieldAsTextStyling("ablehnungsgrund", SyntaxKind.Html);

			// 2) Create Java model context
			IContext context = report.createContext();
			for (Map.Entry<String, Object> entry : data.entrySet()) {
				if (entry.getKey().equals("text")) {
					String text = (String) entry.getValue();
					if (text.contains("&")) {
						text = text.replaceAll("&","+");
						context.put(entry.getKey(), text);
						continue;
					}
				}
				context.put(entry.getKey(), entry.getValue());
			}


			//Sehr simples überschreiben verhindern. Besser Wäre, wenn Dateiendung nicht verändert würde oder ein besserer Timestamp/Dateiname verwendet würden
			int i = 2;
			while (Files.exists(Paths.get(pfad))) {
				pfad += i;
				i++;
			}

			OutputStream out = new FileOutputStream(new File(pfad));
			report.process(context, out);

			return pfad;
		} catch (XDocReportException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Zweifaches Drucken eines Bescheides auf Basis eines Antrags
	 *
	 * @param antrag Antragobjekt
	 * @throws IOException Dateizugriffehler
	 */
	public void printBescheid(Antrag antrag) throws IOException {
		OdtRenderer.print(generateOutputFile(antrag, false));
	}

	/**
	 * Zweifaches Drucken einer AZA auf Basis eines Antrags
	 *
	 * @param antrag Antragobjekt
	 * @throws IOException Dateizugriffehler
	 */
	public void printAZA(Antrag antrag) throws IOException {
		OdtPrinter.print(generateOutputFile(antrag, true), 2);
	}

	/**
	 * Druckt Liste von Bescheiden in i-facher Ausführung
	 *
	 * @param antraege Liste Antraege
	 * @param i        Wie oft drucken
	 * @throws IOException Dateizugrifffehler
 	 * @throws InterruptedException Thread interrupted
	 */
	public void printBescheide(List<Antrag> antraege, int i) throws IOException, InterruptedException {
		printList(antraege, i, false);

	}

	/**
	 * Druckt Liste von Auszahlungsanordnungen in i-facher Ausführung
	 *
	 * @param antraege Liste
	 * @param i        wie oft
	 * @throws IOException Dateizugrifffehler
 	 * @throws InterruptedException Thread interrupted
	 */
	public void printAZAs(List<Antrag> antraege, int i) throws IOException, InterruptedException {
		printList(antraege, i, true);

	}

	/**
	 * Druckt Liste von AZAs oder Bescheiden ins Temp-Verzeichnis!
	 *
	 * 1. Alle Output-Files generieren
	 * 2. Alle Output-Files drucken
	 * 3. Alle Output-Files nochmal drucken, siehe <code>i</code>
	 *
	 * @param antraege Liste
	 * @param i        wie oft
	 * @param istAZA   ist eine AZA?
	 * @throws IOException          Dateizugriffehler
	 * @throws InterruptedException Fehler im Thread
	 */
	private void printList(List<Antrag> antraege, int i, boolean istAZA) {

		Collections.sort(antraege);

		new DialogListPrint(antraege, i, istAZA);

	}

	/**
	 * Druckt das Deckblatt für die Dateiübergabe der Barauszahler (deckblatt_dateiuebergabe.odt)
	 *
	 * @param dateiname         Dateiname
	 * @param anzahlDatensaetze Anzahl
	 * @param auszahlungssumme  Summe
	 * @throws IOException Dateizugriffehler
	 */
	public void printDeckblatt(String dateiname, int anzahlDatensaetze, BigDecimal auszahlungssumme) throws IOException {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("dateiname", dateiname);
		data.put("datensaetze", Integer.toString(anzahlDatensaetze));

		data.put("auszahlungssumme", DeutschesDatum.getEuroFormatted(auszahlungssumme));

		OdtPrinter.print(fillTemplate(SettingsExternal.getdeckblattDatei(), data, "deckblatt_dateiuebergabe.odt"), 1);

	}

	/**
	 * Druckt Nachfrage oder Mahnungsbrief
	 *
	 * @param unterlagenListe Liste der Unterlagen
	 * @param frist           Datum
	 * @param person          personenobjekt
	 * @param semester        semesterobjekt
	 * @param vorgangsart     vorgangsartobjekt
	 * @param letzteFrist     relevante Frist zum Prüfen
	 * @throws MyException einfache Exception wenn noch keine vorherige Nachfrage passiert ist
	 * @throws IOException Dateizugrifffehler
	 */
	public void printLetter(List<Unterlagen> unterlagenListe, Date frist, Person person, Semester semester, Vorgangsart vorgangsart, GregorianCalendar letzteFrist) throws MyException, IOException {

		StringBuilder unterlagenText = new StringBuilder();

		for (Unterlagen u : unterlagenListe) {
			String text = u.getText().trim();
			String[] teil = text.split(":", 2);
			if (teil.length == 1) {
				unterlagenText.append("- " + teil[0] + "<br></br>");
			} else if (teil.length == 2) {
				unterlagenText.append("- <b>" + teil[0] + "</b>: " + teil[1] + "<br></br>");
			} else if (teil.length > 2) {
				StringBuilder sb = new StringBuilder("- <b>" + teil[0] + "</b>");
				for (int i = 1; i < teil.length; i++) {
					sb.append(" :" + teil[i]);
				}
				sb.append("<br></br>");
				unterlagenText.append(sb.toString());
			}
		}



		String dateFormatted = DeutschesDatum.getFormatiertenZeitstempel(new GregorianCalendar());


		String fileName = "brief_an_" + person.getVorname() + "_" + person.getNachname() + "_" + dateFormatted + ".odt";

		String template;

		if (vorgangsart.equals(Vorgangsart.MAHNUNG)) {
			if (person.isEnglischsprachig())
				template = SettingsExternal.gettemplateMahnungEn();
			else
				template = SettingsExternal.gettemplateMahnungDe();
		} else {
			if (person.isEnglischsprachig())
				template = SettingsExternal.gettemplateNachfrageEn();
			else
				template = SettingsExternal.gettemplateNachfrageDe();
		}

		Map<String, Object> data = initData(person, semester);
		data.put("vorname", person.getVorname());
		data.put("nachname", person.getNachname());
		data.put("strasse", person.getStrasse());
		data.put("plz", person.getPlz());
		data.put("wohnort", person.getWohnort());
		data.put("matrikelnr", person.getMatrikelnr());
		data.put("semester", semester.getSemesterBezeichnung());

		if (null != person.getCo() && person.getCo().length() > 0)
			data.put("co", person.getCo());

		data.put("text", unterlagenText.toString());


		if (vorgangsart.equals(Vorgangsart.MAHNUNG)) {
			if (null == letzteFrist)
				throw new MyException("Noch keine vorherige Nachfrage passiert");

			data.put("nachfragefrist", DeutschesDatum.DATUMSFORMAT.format(letzteFrist.getTime()));
			data.put("mahnfrist", DeutschesDatum.DATUMSFORMAT.format(frist.getTime()));
		} else {
			data.put("nachfragefrist", DeutschesDatum.DATUMSFORMAT.format(frist.getTime()));
		}

		String templatefilename = fillTemplate(template, data, fileName);

		OdtPrinter.print(templatefilename, 2);
	}

	private String generateOutputFile(Antrag antrag, Boolean istAuszahlungUndKeinBescheid) throws IOException {
		return generateOutputFile(antrag, istAuszahlungUndKeinBescheid, false);
	}


}


