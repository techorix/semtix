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


package org.semtix.gui.auszahlung.auszahlungsmodul;


import org.semtix.config.UniConf;
import org.semtix.db.DBHandlerPerson;
import org.semtix.db.dao.Antrag;
import org.semtix.db.dao.Person;
import org.semtix.shared.daten.DeutschesDatum;
import org.semtix.shared.print.OdtTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Schritt 6 des Dialogs zur Zuschussberechnung
 */
@SuppressWarnings("serial")
class PanelStep6
		extends GenericPanelStep {


	private JPanel buttonPanel;

	private JButton saveButton;


	/**
	 * {see GenericPanelStep}
	 *
	 * @param bModel     ModelAuszahlungsmodul
	 * @param titel      titel
	 * @param untertitel untertitel
	 */
	public PanelStep6(final ModelAuszahlungsmodul bModel, String titel, String untertitel) {
		super(bModel, titel, untertitel);
	}

	@Override
	protected void additionalInitStuff() {

		saveButton = new JButton("Datei speichern und Deckblatt drucken");

		saveButton.addActionListener(new SaveL());

		buttonPanel = new JPanel();

		buttonPanel.add(saveButton);

		formular.add(buttonPanel, 0, 8, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 0, 0));

		formular.add(new JPanel(), 0, 9, 1, 1, 1.0, 1.0, 2, 18, new Insets(0, 0, 0, 0));

	}

	/**
	 * Schreibt antreage in datei
	 *
	 * @param datei    Dateiname/Pfad
	 * @param antraege Liste Anträge
	 */
	private BigDecimal writeCSV(String datei, List<Antrag> antraege) {
		PrintWriter writer = null;
		BigDecimal summe = new BigDecimal(0);

		NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
		nf.setGroupingUsed(true);

		try {
			//Latin1-Encoding instead of "UTF-8":
			writer = new PrintWriter(datei, "ISO-8859-1");
			//Windows Carriage Return
			writer.print("#|Inhaber|Adresse|PLZ|Ort|Land|IBAN|BIC|Semester|Verwendungszweck|Betrag" + "\r\n");
			int i = 0;

			DBHandlerPerson dbhPerson = new DBHandlerPerson();

			List<Person> personen = dbhPerson.getPersonenListe(UniConf.aktuelleUni);


			for (Antrag a : antraege) {

				for (Person p : personen) {

					if (a.getPersonID() == p.getPersonID()) {

						String name = p.getNachname() + ", " + p.getVorname();
						if (null != p.getKontoInhaber_Name()) {
							if (p.getKontoInhaber_Name().length() > 0) {
								name = p.getKontoInhaber_Name();
							}
						}


						String strasse = p.getStrasse();
						if (null != p.getKontoInhaber_Strasse()) {
							if (p.getKontoInhaber_Strasse().length() > 0) {
								name = p.getKontoInhaber_Strasse();
							}
						}


						String wohnort = p.getWohnort();
						if (null != p.getKontoInhaber_Wohnort()) {
							if (p.getKontoInhaber_Wohnort().length() > 0) {
								name = p.getKontoInhaber_Wohnort();
							}
						}


						BigDecimal erstattung = a.getErstattung();

						if (a.isTeilzuschuss()) {
							Double d = Double.valueOf(a.getAnzahlMonate()) / 6;
							BigDecimal vollzuschuss = bModel.getSemester().getBeitragTicket().add(bModel.getSemester().getSozialfonds());
							erstattung = vollzuschuss.multiply(new BigDecimal(d));
						}


						String iban = p.getIBAN().trim();
						iban = iban.replaceAll(" ", "");

						String formattedBetrag = DeutschesDatum.getEuroFormatted(erstattung);
						formattedBetrag = formattedBetrag.substring(0, formattedBetrag.length() - 1);
						String blaBetrag = formattedBetrag.replaceAll(",", ".");
						summe = summe.add(new BigDecimal(blaBetrag));

						i++;
						writer.print(i + "|" + name.trim() + "|" + strasse.trim() + "|" + p.getPlz().trim() + "|" +
								wohnort.trim() + "|" + p.getLand().trim() + "|" + iban + "|" +
								p.getBIC().trim() + "|" + bModel.getSemester() + "|" + "Semtix " +
								bModel.getSemester().getSemesterKurzform() + " " + p.getNachname().trim() + ", " +
								p.getVorname().trim() + "|" + formattedBetrag + "\r\n");

					}
				}
			}
			String formattedSumme = DeutschesDatum.getEuroFormatted(summe);
			formattedSumme = formattedSumme.substring(0, formattedSumme.length() - 1);

			writer.print(i++ + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "Semtix " + " " + ", " + "|" +
					formattedSumme + "\r\n");

			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return summe;
	}

	@Override
	protected void updatePanel() {
		refreshLog();
	}

	class SaveL implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			List<Antrag> antraege = bModel.getAntragListeUeberweisungen();

			if (null == antraege || antraege.size() < 1) {
				JOptionPane.showMessageDialog(null, "Leider keine Anträge zum Speichern vorhanden.");
			} else {
				JFileChooser c = new JFileChooser();
				// Demonstrate "Save" dialog:
				int rVal = c.showSaveDialog(PanelStep6.this);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					String fehlendeIBAN = "";
					int fehlendeIBANzahl = 0;
					for (Antrag a : antraege) {

						DBHandlerPerson dbhPerson = new DBHandlerPerson();
						Person p = dbhPerson.readPerson(a.getPersonID());

						if (null == p.getIBAN() || p.getIBAN().length() <= 1) {

							fehlendeIBANzahl++;

							if (fehlendeIBAN.length() > 0) {
								fehlendeIBAN += ",";
							}

							if (fehlendeIBANzahl % 4 == 0) {
								fehlendeIBAN += "\n";
							}

							fehlendeIBAN += p.getVorname() + " " + p.getNachname();

						}
					}
					int rVal2 = JOptionPane.OK_OPTION;
					if (fehlendeIBAN.length() > 0) {
						rVal2 = JOptionPane.showConfirmDialog(null, fehlendeIBANzahl + " Personen haben noch keinen IBAN-Eintrag... \n" + fehlendeIBAN, "Wollen Sie wirklich eine Auszahlungsdatei erstellen?", JOptionPane.YES_NO_OPTION);
					}

					if (rVal2 == JOptionPane.OK_OPTION) {
						String filename = c.getSelectedFile().getName();

						BigDecimal auszahlungssumme = writeCSV(c.getSelectedFile().getAbsolutePath(), antraege);

						bModel.updateAntraegeAuszahlung(antraege);

						OdtTemplate drucker = new OdtTemplate();
						try {
							drucker.printDeckblatt(filename, antraege.size(), auszahlungssumme);
							logTextArea.logText("(6) Auszahlungsdatei für alle genehmigten Überweisungsanträge erstellt und Anträge als ausgezahlt markiert");
						} catch (IOException ioe) {
							JOptionPane.showMessageDialog(null, "Drucken des Deckblattes fehlgeschlagen. \n Bitte Pfadeinstellungen überprüfen. \n" + ioe.getMessage());
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, "Drucken des Deckblattes fehlgeschlagen. \n Bitte Pfadeinstellungen überprüfen. \n" + e1.getMessage());
						}
					}
				}
			}
		}
	}
}




