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


import org.apache.log4j.Logger;
import org.semtix.config.UniConf;
import org.semtix.db.DBHandlerAntrag;
import org.semtix.db.DBHandlerConf;
import org.semtix.db.DBHandlerSemester;
import org.semtix.db.dao.Antrag;
import org.semtix.db.dao.Semester;
import org.semtix.shared.daten.DeutschesDatum;
import org.semtix.shared.daten.enums.AntragStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Model mit Daten und Berechnungen für das Auszahlungsmodul
 */
public class ModelAuszahlungsmodul {

    private int summePunkte, punkteVollzuschuss;
    private Semester semester;
    private GregorianCalendar stichtag;
    private BigDecimal fonds, ticketpreis, sozialfonds, punktwert;

    private String toLog;

	private List<Antrag> alleGenehmigtenAntraege;
	private List<Antrag> alleGenehmigtenAntraegeFiltered;
	private List<Antrag> alleEntschiedenenAntraegeFiltered;

	private Logger logger = Logger.getLogger(ModelAuszahlungsmodul.class);

	public ModelAuszahlungsmodul() {

        punkteVollzuschuss = 1;
        punktwert = null;
        summePunkte = 0;
        toLog="";

		alleGenehmigtenAntraege = null;
		alleGenehmigtenAntraegeFiltered = null;
		alleEntschiedenenAntraegeFiltered = null;

		DBHandlerConf dbHandlerConf = new DBHandlerConf();

        toLog = dbHandlerConf.read("log_auszahlungsmodul");


    }


    /**
     * Liefert Semester-ID für die Zuschussberechnung
     *
     * @return Semester-ID
     */
    public Semester getSemester() {
        return semester;
    }


    /**
     * Setzt Semester für die Zuschussberechnung
     *
     * @param semester Semester
     */
    public void setSemester(Semester semester) {
        this.semester = semester;
		this.alleGenehmigtenAntraege = null;
		this.alleGenehmigtenAntraegeFiltered = null;
		this.alleEntschiedenenAntraegeFiltered = null;
		if (null != semester.getSozialfonds())
            this.sozialfonds = semester.getSozialfonds();
        if (null != semester.getBeitragFonds())
            this.fonds = semester.getBeitragFonds();
        if (null != semester.getBeitragTicket())
            this.ticketpreis = semester.getBeitragTicket();
        if (0 != semester.getPunkteVergeben())
            this.summePunkte = semester.getPunkteVergeben();
        if (0 != semester.getPunkteVoll())
            this.punkteVollzuschuss = semester.getPunkteVoll();
        if (null != semester.getPunktWert()) {
            this.punktwert = semester.getPunktWert();
        }

    }


    /**
     * Liefert Semester-ID für die Zuschussberechnung
     *
     * @return Semester-ID
     */
    public int getSemesterID() {
        return semester.getSemesterID();
    }


    /**
     * Liefert die Anzahl der bewilligten Anträge für die Zuschussberechnung
     *
     * @return Anzahl bewilligte Anträge
     */
    public int getAnzahlAntraegeBewilligt() {
        return this.getAntragListeGenehmigt().size();
    }

    /**
     * Liefert die Summe der Punkte für die Zuschussberechnung
     *
     * @return Summe Punkte
     */
    public int getSummePunkte() {
        return summePunkte;
    }


    /**
     * Liefert die Punktzahl, ab der es einen Vollzuschuss gibt
     *
     * @return Punktzahl
     */
    public int getPunkteVollzuschuss() {
        return punkteVollzuschuss;

    }


    /**
     * Liefert das Datum des Stichtags für die Zuschussberechnung
     *
     * @return Datum Stichtag
     */
    public GregorianCalendar getStichtag() {
        return stichtag;
    }


    /**
     * Setzt das Datum des Stichtags für die Zuschussberechnung
     *
     * @param stichtag Datum Stichtag
     */
    public void setStichtag(GregorianCalendar stichtag) {
        this.stichtag = stichtag;
    }


    /**
     * Liefert den Fonds zur Zuschussberechnung
     *
     * @return Fonds (formatiert als Währungsstring)
     */
    public String getFonds() {
		return DeutschesDatum.getEuroFormatted(fonds);
	}


    /**
     * Setzt den Fonds zur Zuschussberechnung
     *
     * @param fondsText Fonds
     */
    public void setFonds(String fondsText) {
        this.fonds = new BigDecimal(fondsText.replace(',', '.'));
    }


    /**
     * Liefert den Ticketpreis zur Zuschussberechnung
     *
     * @return Ticketpreis (formatiert als Währungsstring)
     */
    public String getTicketpreis() {
		return DeutschesDatum.getEuroFormatted(ticketpreis);
	}


    /**
     * Setzt den Ticketpreis zur Zuschussberechnung
     *
     * @param ticketpreisText Ticketpreis
     */
    public void setTicketpreis(String ticketpreisText) {
        this.ticketpreis = new BigDecimal(ticketpreisText.replace(',', '.'));
    }


    /**
     * Liefert den Sozialfondsbetrag zur Zuschussberechnung
     *
     * @return Sozialfondsbetrag (formatiert als Währungsstring)
     */
    public String getSozialfonds() {
		return DeutschesDatum.getEuroFormatted(sozialfonds);
	}


    /**
     * Setzt den Sozialfondsbetrag zur Zuschussberechnung
     *
     * @param sozialfondsText Sozialfondsbetrag
     */
    public void setSozialfonds(String sozialfondsText) {
        this.sozialfonds = new BigDecimal(sozialfondsText.replace(',', '.'));
    }


    /**
     * Liefert den Punktwert zur Zuschussberechnung
     *
     * @return Punktwert (formatiert als Währungsstring)
     */
    public String getPunktwert() {

        if (null == punktwert) {
            return "Alle Vollzuschuss";
        } else {
            return DeutschesDatum.getEuroFormatted(punktwert);
        }
    }


    /**
     * Liefert Liste mit Semestern, die noch keinen Stichtag eingetragen haben (noch keine Berechnung erfolgt)
     *
     * @return Semesterliste
     */
    public List<Semester> getSemesterListe() {
        List<Semester> semesterListe = new ArrayList<Semester>();

        DBHandlerSemester dbHandlerSemester = new DBHandlerSemester();
        List<Semester> semesterList = dbHandlerSemester.getSemesterListe(UniConf.aktuelleUni);

        for (Semester s : semesterList) {

            DBHandlerAntrag dbhAntrag = new DBHandlerAntrag();

			List<Object[]> antraegeByStatus = dbhAntrag.countAntraegeByStatus(s.getSemesterID());

			int anzahlBewAntraege = 0;
			for (Object[] o : antraegeByStatus) {
                if (o[0].equals(AntragStatus.GENEHMIGT)) {
                    anzahlBewAntraege = ((Long) o[1]).intValue();
					break;
				}
			}

            s.setAntraegeBewilligt(anzahlBewAntraege);
            s.setStichtag(s.getSemesterAnfang());

            semesterListe.add(s);

        }


        return semesterListe;
    }




    /**
     * Berechnung des Punktwertes auf Basis der gefilterten Antragsliste die aus normalen genehmigten Anträgen ohne Sonderfällen besteht
     */
    public void berechnungPunktwert() {

        this.summePunkte = 0;
        this.punktwert = null;
		int anzahlAntraege = getAntragListeGenehmigtFiltered().size();
		int anzahlTeilzuschuesse = 0;


        BigDecimal fonds = this.fonds;
		BigDecimal vollzuschuss = ticketpreis.add(sozialfonds);
		BigDecimal vollzuschussSechstel = vollzuschuss.divide(new BigDecimal(6), 2, BigDecimal.ROUND_HALF_DOWN);
		BigDecimal teilzuschussVerminderung = BigDecimal.ZERO;


		HashMap<Integer, Integer> punkteVerteilung = new HashMap<Integer, Integer>();
		for (Antrag a : getAntragListeGenehmigtFiltered()) {
			int punkteEinkommen = a.getPunkteEinkommen() > 0 ? a.getPunkteEinkommen() : 0;
            int punkteHaerte = a.getPunkteHaerte() > 0 ? a.getPunkteHaerte() : 0;
            Integer punkte = punkteEinkommen + punkteHaerte;
            summePunkte += punkte;

			System.out.println("Antrag " + a.getAntragID() + " hat " + punkte + " also sind wir bei " + summePunkte);

			if (a.isTeilzuschuss()) {
				anzahlTeilzuschuesse++;
				int anzahlMonateWeniger = 6 - a.getAnzahlMonate();
				teilzuschussVerminderung = teilzuschussVerminderung.add(vollzuschussSechstel.multiply(new BigDecimal(anzahlMonateWeniger)));
			}

            if (null == punkteVerteilung.get(punkte))
                punkteVerteilung.put(punkte,1);
            else {
                Integer anzahl = punkteVerteilung.get(punkte);
                anzahl++;
                punkteVerteilung.put(punkte,anzahl);
            }
        }

        TreeMap<Integer,Integer> punkteVerteilungTree = new TreeMap<Integer, Integer>(punkteVerteilung);
        BigDecimal alleVollzuschuss = vollzuschuss.multiply(new BigDecimal(anzahlAntraege));
		BigDecimal vermindertAlleVollzuschuss = alleVollzuschuss.subtract(teilzuschussVerminderung);

        System.out.println("Wenn alle Vollzuschuss bekommen würden, bräuchten wir einen Fonds von " + alleVollzuschuss + " Wir haben: " + DeutschesDatum.getEuroFormatted(fonds));
        System.out.println("Bei der Berechnung werden auch " + anzahlTeilzuschuesse + " Anträge mit Teilzuschuss miteinbezogen. Deshalb vermindert sich der nötige Fonds um: " + DeutschesDatum.getEuroFormatted(teilzuschussVerminderung));

		if (fonds.compareTo(vermindertAlleVollzuschuss) < 0) {
            if (punkteVerteilungTree.size() == 1) {
                this.punkteVollzuschuss = 0;
                this.punktwert = fonds.divide(new BigDecimal(summePunkte));
            } else {
                int restpunkte = summePunkte;
                BigDecimal tempPunktWert = null;
                int tempPunkteVollzuschuss = 1;

                while (true) {
                    this.punktwert = tempPunktWert;
                    this.punkteVollzuschuss = tempPunkteVollzuschuss;
                    Map.Entry<Integer, Integer> mapEntry = punkteVerteilungTree.pollLastEntry();
                    int punkte = mapEntry.getKey();
                    int wieOftVorhanden = mapEntry.getValue();
                    int punktsumme = punkte * wieOftVorhanden;
                    restpunkte = restpunkte - punktsumme;

					//TODO an dieser stelle sind Teilzuschüsse ganz schwer zu berücksichtigen:
					//TODO Müsste man die teilzuschüsse und deren monate nach Punkten sortieren
					//TODO Müsste sie von der mapEntry entfernen und separat abziehen
					fonds = fonds.subtract((vollzuschuss.multiply(new BigDecimal(wieOftVorhanden))));

                    if (restpunkte > 0) {
						tempPunktWert = fonds.divide(new BigDecimal(restpunkte), 2, RoundingMode.DOWN);
						tempPunkteVollzuschuss = punkte;
						if (tempPunktWert.multiply(new BigDecimal(punkte)).compareTo(vollzuschuss) < 0)
							break;

						System.out.println("Punkte " + punkte + " gibt es " + wieOftVorhanden + " mal. Punktwert ist " + tempPunktWert + " Fonds ist " + fonds + " Restpunkte sind " + restpunkte);

                    } else {
                        break;
                    }
                }
            }

        }

    }


    /**
     * Schreibt die geänderten Semesterdaten in die Datenbank.
     */
    public void updateSemester() {

        semester.setAntraegeBewilligt(getAnzahlAntraegeBewilligt());
        semester.setPunkteVergeben(summePunkte);


        semester.setBeitragTicket(ticketpreis);
        semester.setBeitragFonds(fonds);
        semester.setSozialfonds(sozialfonds);
        semester.setStichtag(stichtag);
        semester.setPunktWert(punktwert);

        //Wenn alle Vollzuschuss bekommen, dann bekommt, wer mindestens einen Punkt hat, Ticket+Sozialfond erstattet (Vollzuschuss)
        if (getPunktwert().contains("Alle Vollzuschuss")) {
            semester.setPunkteVoll(1);
        } else {
            semester.setPunkteVoll(punkteVollzuschuss);
        }


        DBHandlerSemester dbHandlerSemester = new DBHandlerSemester();
        dbHandlerSemester.updateSemester(semester);

		BigDecimal vollzuschuss = ticketpreis.add(sozialfonds);

        /**
         * Anträge müssen noch um den Betrag erweitert werden.
         */
        for (Antrag a : getAntragListeGenehmigt()) {
            int punkteDesAntrags = a.getPunkteHaerte() + a.getPunkteEinkommen();

			if (a.isTeilzuschuss()) {
				Double d = Double.valueOf(a.getAnzahlMonate()) / 6;
				a.setErstattung(vollzuschuss.multiply(new BigDecimal(d)));
			} else {
				if (punkteDesAntrags >= punkteVollzuschuss) {
					a.setErstattung(vollzuschuss);
				} else if (punkteDesAntrags >= 1) {
					try {
						a.setErstattung(punktwert.multiply(new BigDecimal(punkteDesAntrags)));
					} catch (Exception e) {
						//Parse Fehler beim Float; Sollte eigentlich nicht auftreten
					}
				}
			}
			DBHandlerAntrag dbHandlerAntrag = new DBHandlerAntrag();
            dbHandlerAntrag.updateAntrag(a);

        }

    }

	public List<Antrag> getAntragListeEntschiedenFiltered() {
		if (null == alleEntschiedenenAntraegeFiltered) {
			logger.debug("Hole alle entschiedenen Anträge, die nicht Spätis sind, aus der DB für Semester " + semester.getNextSemesterBezeichnung());
			DBHandlerAntrag dbHandlerAntrag = new DBHandlerAntrag();
			alleEntschiedenenAntraegeFiltered = dbHandlerAntrag.getAntragListeEntschiedenSemesterFiltered(semester.getSemesterID());
		}

		return alleEntschiedenenAntraegeFiltered;
	}

    /**
     * @see #berechnungPunktwert()
     *
     * Wird vor allem bei der Punktwertberechnung benutzt.
     * @return genehmigte Antrage die weder Erstsemester noch Nothilfe noch Ratenzahler sind
     */
	public List<Antrag> getAntragListeGenehmigtFiltered() {

		if (null == alleGenehmigtenAntraegeFiltered) {
			alleGenehmigtenAntraegeFiltered = new ArrayList<Antrag>();
			logger.debug("Hole alle genehmigten Anträge, die nicht Spätis sind, aus der DB für Semester " + semester.getNextSemesterBezeichnung());
			for (Antrag a : getAntragListeEntschiedenFiltered()) {
				if (a.getAntragStatus() == AntragStatus.GENEHMIGT) {
					alleGenehmigtenAntraegeFiltered.add(a);
				}
			}
		}
		logger.debug("Für die Berechnung benutzen wir " + alleGenehmigtenAntraegeFiltered.size() + " Anträge.");

		return alleGenehmigtenAntraegeFiltered;
	}


	/** Alle Genehmigten Anträge des Semesters
	 * @return Antrag-Liste */
    public List<Antrag> getAntragListeGenehmigt() {

		if (null == this.alleGenehmigtenAntraege) {

			logger.debug("Hole alle genehmigten Anträge für Semester " + semester.getSemesterBezeichnung());

			DBHandlerAntrag dbHandlerAntrag = new DBHandlerAntrag();

			alleGenehmigtenAntraege = dbHandlerAntrag.getAntragListeSemesterGenehmigt(semester.getSemesterID());

		}

		return alleGenehmigtenAntraege;
	}



    /**
     * Anträge von Barauszahlern
     * @see #getAntragListeGenehmigt()
	 * @see #getAntragListeGenehmigtFiltered()
	 *
     * @return nur genehmigte Anträge von Barauszahlern welche nicht Nothilfe o.ä. sind
     */
    public List<Antrag> getAntragListeBarauszahler() {

        List<Antrag> barauszahler = new ArrayList<Antrag>();

		for (Antrag a : getAntragListeGenehmigtFiltered()) {
			if (a.isManAuszahlen()) {
                barauszahler.add(a);
            }
        }

        return barauszahler;

    }


    /**
     * Genehmigte Überweisungsanträge die nicht Nothilfe usw. sind
     * @return Liste von Anträgen die nicht Barauszahler sind
     */
    public List<Antrag> getAntragListeUeberweisungen() {
        List<Antrag> ueberweisungen = new ArrayList<Antrag>();
		for (Antrag a : getAntragListeGenehmigtFiltered()) {
			if (! a.isManAuszahlen()) {
                ueberweisungen.add(a);
            }
        }

        return ueberweisungen;
    }




    /**
     * Setzt antraege auf ausgezahlt
     * @param antraege Anträge die geupdatet werden soll mit ausgezahlt = true
     */
    public void updateAntraegeAuszahlung(List<Antrag> antraege) {
        DBHandlerAntrag dbHandlerAntrag = new DBHandlerAntrag();
        for (Antrag a : antraege) {
            a.setAuszahlung(true);
            dbHandlerAntrag.updateAntrag(a);
        }
    }

	public void setAntragListeForTest(List<Antrag> antragListe) {
		this.alleEntschiedenenAntraegeFiltered = antragListe;
	}
}
