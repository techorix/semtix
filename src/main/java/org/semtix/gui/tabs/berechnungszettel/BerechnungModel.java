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

package org.semtix.gui.tabs.berechnungszettel;


import org.semtix.config.Berechnung;
import org.semtix.config.Settings;
import org.semtix.db.DBHandlerBerechnungszettel;
import org.semtix.db.dao.Antrag;
import org.semtix.db.dao.AntragHaerte;
import org.semtix.db.dao.Berechnungszettel;
import org.semtix.db.dao.LabeledDecimalList;
import org.semtix.gui.tabs.antrag.AntragModel;
import org.semtix.shared.daten.DeutschesDatum;
import org.semtix.shared.daten.enums.BerechnungsZettelCFTypen;
import org.semtix.shared.daten.enums.Haertegrund;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

/**
 * Klasse speichert die Zustände des Berechnungszettels
 *
 * Sämtliche Werte und Zustände die die Views brauchen werden von hier abgerufen
 *
 * Das Model weiss dabei selbst am besten, ob sich ein Zustand geändert hat. In diesem Fall benachrichtigt es seine
 * Abonnenten
 */
public class BerechnungModel
		extends Observable {


	private final AntragModel antragModel;
	private Berechnungszettel berechnungszettel;

	/**
	 * Erstellt neues BerechnungsModel
	 *
	 * @param antragModel das dazugehörige AntragModel
	 *                    (Pseudo-OCL)  CONSTRAINT:: berechnungszettel.getAntragID() EQUALS antrag.getAntragID
	 */
	public BerechnungModel(AntragModel antragModel) {

		this.antragModel = antragModel;
		this.berechnungszettel = new DBHandlerBerechnungszettel().getBRZByAntragID(getAntrag().getAntragID());

		if (null == this.berechnungszettel) {
			this.berechnungszettel = new Berechnungszettel(getAntrag().getAntragID());

			DBHandlerBerechnungszettel dbhandler = new DBHandlerBerechnungszettel();
			dbhandler.create(berechnungszettel);

			//6 Kostenfelder für Detailrechnungen
			for (int i = 0; i < 6; i++) {
				LabeledDecimalList kostenfeld = new LabeledDecimalList();
				kostenfeld.setTyp(BerechnungsZettelCFTypen.PLATZHALTER);
				kostenfeld.setBrzId(berechnungszettel.getBrzId());
				berechnungszettel.getBerechnungsWerteListe().add(kostenfeld);
			}

			//Freie Einkommensfelder
			for (int i = 0; i < Settings.ANZAHL_FREIE_EINKOMMENSFELDER; i++) {
				LabeledDecimalList einkommensfeld = new LabeledDecimalList();
				einkommensfeld.setTyp(BerechnungsZettelCFTypen.FREIES_EINKOMMENSFELD);
				einkommensfeld.setBrzId(berechnungszettel.getBrzId());
				berechnungszettel.getBerechnungsWerteListe().add(einkommensfeld);
			}

			//Zugeordnete Einkommensfelder
			for (int i = 0; i < 4; i++) {
				LabeledDecimalList einkommensfeld = new LabeledDecimalList();
				einkommensfeld.setTyp(BerechnungsZettelCFTypen.EINKOMMENSFELD);
				einkommensfeld.setBrzId(berechnungszettel.getBrzId());
				berechnungszettel.getBerechnungsWerteListe().add(einkommensfeld);
			}

			if (getAntrag().isKulanz())
				setBemerkung("Kulanz");

			dbhandler.saveOrUpdateBRZ(berechnungszettel);

		}

	}

	public BigDecimal getGrundBedarf() {
		return Berechnung.GRUNDBEDARF;
	}

	public String getBemerkung() {
		if (getAntrag().isKulanz()) {
			if (null == berechnungszettel.getBemerkung() || berechnungszettel.getBemerkung().length() == 0) {
				berechnungszettel.setBemerkung("Kulanz");

			} else if (!berechnungszettel.getBemerkung().contains("Kulanz")) {
				berechnungszettel.setBemerkung(berechnungszettel.getBemerkung() + " Kulanz");
			}
		}
		return berechnungszettel.getBemerkung();
	}

	public void setBemerkung(String bemerkung) {
		berechnungszettel.setBemerkung(bemerkung);
	}

	public BigDecimal getMehrbedarf() {
		if (null == getLabeledDecimalListByType(BerechnungsZettelCFTypen.MEHRBEDARF)) {
			BigDecimal mehrbedarf = BigDecimal.ZERO;
			if (null != getAntrag().getHaerteListe() && getAntrag().getHaerteListe().size() > 0) {
				for (AntragHaerte ah : getAntrag().getHaerteListe()) {
					if (ah.isAnerkannt()) {
						if (ah.getHaertegrund().equals(Haertegrund.SCHWANGERSCHAFT))
							mehrbedarf = mehrbedarf.add(Berechnung.SCHWANGERSCHAFT);
						if (ah.getHaertegrund().equals(Haertegrund.ALLEINERZIEHEND))
							mehrbedarf = mehrbedarf.add(Berechnung.ALLEINERZIEHEND);
						if (ah.getHaertegrund().equals(Haertegrund.BEH_CHRONKRANK))
							mehrbedarf = mehrbedarf.add(Berechnung.CHRONISCH_KRANK);
					}
				}
			}
			return mehrbedarf;
		} else {
			return getLabeledDecimalListByType(BerechnungsZettelCFTypen.MEHRBEDARF).getValue();
		}

	}

	public void setMehrbedarf(BigDecimal betrag) {
		createOrUpdate(BerechnungsZettelCFTypen.MEHRBEDARF, betrag, true);
	}

	public void setPunkteHaerten(int punkteHaerten) {
		this.getAntrag().setPunkteHaerte(punkteHaerten);
		updateViews();
	}

	public int getAnzahlKinder() {
		return berechnungszettel.getKinder();
	}

	public void setAnzahlKinder(int anzahl) {
		berechnungszettel.setKinder(anzahl);

		BigDecimal temp = BigDecimal.ZERO;
		if (anzahl > 0) {
			if (anzahl == 1 || anzahl == 2) {
				temp = Berechnung.KINDERGELD.multiply(new BigDecimal(anzahl));
			} else {
				temp = Berechnung.KINDERGELD.multiply(new BigDecimal(2));

				temp = temp.add(Berechnung.KINDERGELD2);

				if (anzahl > 3) {
					int anzahlUeber3 = anzahl - 3;
					temp = temp.add(Berechnung.KINDERGELD3.multiply(new BigDecimal(anzahlUeber3)));
				}
			}


		}

		if (anzahl > 0) {
			setHaerteKind(true);
		}

		createOrUpdate(BerechnungsZettelCFTypen.KINDERGELD, temp);

		createOrUpdate(BerechnungsZettelCFTypen.KINDER, Berechnung.KIND.multiply(new BigDecimal(anzahl)));
	}

	public BigDecimal getKrankenkasse() {
		return justGetValueForType(BerechnungsZettelCFTypen.KRANKENKASSE);
	}

	public void setKrankenkasse(BigDecimal betrag) {
		createOrUpdate(BerechnungsZettelCFTypen.KRANKENKASSE, betrag, true);
	}

	public BigDecimal getUnterhalt() {
		return justGetValueForType(BerechnungsZettelCFTypen.UNTERHALT);
	}

	public void setUnterhalt(BigDecimal betrag) {
		createOrUpdate(BerechnungsZettelCFTypen.UNTERHALT, betrag, true);
	}

	public BigDecimal getMedKosten() {
		return justGetValueForType(BerechnungsZettelCFTypen.MEDKOSTEN);
	}

	public void setMedKosten(BigDecimal betrag) {
		createOrUpdate(BerechnungsZettelCFTypen.MEDKOSTEN, betrag, true);

		if (isWarnungMedPsych()) {
			setHaerteMedkosten(true);
		}
		updateViews();
	}

	public BigDecimal getErnaehrung() {
		return justGetValueForType(BerechnungsZettelCFTypen.BESONDEREERNAEHRUNG);
	}

	public void setErnaehrung(BigDecimal betrag) {
		createOrUpdate(BerechnungsZettelCFTypen.BESONDEREERNAEHRUNG, betrag, true);
	}

	/**
	 * Liefert den Betrag der Berechnung der Heiz.-Pauschale in Abhängigkeit von der Gesamtzahl der Personen
	 *
	 * @return Betrag Heiz.-Pauschale
	 */
	public BigDecimal getHeizpauschale() {

		BigDecimal hp = new BigDecimal("0.0");

		if (berechnungszettel.isHeizkostenPauschaleCheck()) {
			if (getGesamtAnzahlPersonen() <= 2)
				hp = new BigDecimal("" + Berechnung.HEIZPAUSCHALE);
			else if (getGesamtAnzahlPersonen() <= 4)
				hp = new BigDecimal("" + Berechnung.HEIZPAUSCHALE).multiply(new BigDecimal("2"));
			else if (getGesamtAnzahlPersonen() <= 6)
				hp = new BigDecimal("" + Berechnung.HEIZPAUSCHALE).multiply(new BigDecimal("3"));
			else
				hp = new BigDecimal("" + Berechnung.HEIZPAUSCHALE).multiply(new BigDecimal("4"));

		}

		return hp;
	}

	public boolean isHeizpauschale() {
		return berechnungszettel.isHeizkostenPauschaleCheck();
	}

	public void setHeizpauschale(boolean heizpauschale) {
		berechnungszettel.setHeizkostenPauschaleCheck(heizpauschale);
		updateViews();
	}

	public BigDecimal getMieteEffektiv() {
		BigDecimal betrag = getMiete().add(getHeizpauschale());

		// Mietkappungsbetrag (multipliziert mit Gesamtanzahl Personen)
		BigDecimal betragMietKappung = new BigDecimal(getGesamtAnzahlPersonen()).multiply(Berechnung.KAPPUNG_MIETE);

		// wenn Gesamtmiete grösser als Mietkappungsbetrag
		if ((!isKeineMietkappungMiete()) && betrag.compareTo(betragMietKappung) > 0) {
			betrag = betragMietKappung;

		}

		return betrag;
	}


	public BigDecimal getMiete() {
		return justGetValueForType(BerechnungsZettelCFTypen.MIETE);
	}

	public void setMiete(BigDecimal betrag) {
		createOrUpdate(BerechnungsZettelCFTypen.MIETE, betrag, true);
	}

	/**
	 * Verhältnis zwischen dem Mietkappungsbetrag und der tatsächlichen Gesamtmiete
	 *
	 * @return "1.0" wenn Kappung nicht aktiv, sonst Verhältnis effektive Miete / tatsächliche Gesamtmiete
	 */
	public BigDecimal getAnrechnungsverhaeltnis() {
		BigDecimal gesamtmiete = getMiete().add(getHeizpauschale());
		if (gesamtmiete.compareTo(getMieteEffektiv()) == 1) {
			return getMieteEffektiv().divide(gesamtmiete, 4, BigDecimal.ROUND_UP);
		} else {
			return new BigDecimal("1.0");
		}
	}

	public boolean isAuslandkosten() {
		return berechnungszettel.isAuslandskostenCheck();
	}

	public BigDecimal getAuslandskosten() {
		BigDecimal value = justGetValueForType(BerechnungsZettelCFTypen.AUSLANDSKOSTEN);
		if (value.equals(BigDecimal.ZERO) && isHaerteAuslandkosten()) {
			return Berechnung.AUSLANDSKOSTEN;
		}
		return value;
	}

	public void setAuslandskosten(boolean checked) {
		berechnungszettel.setAuslandskostenCheck(checked);
		if (checked) {
			createOrUpdate(BerechnungsZettelCFTypen.AUSLANDSKOSTEN, Berechnung.AUSLANDSKOSTEN);
		} else {
			createOrUpdate(BerechnungsZettelCFTypen.AUSLANDSKOSTEN, BigDecimal.ZERO);
		}
	}

	public int getGesamtAnzahlPersonen() {
		return 1 + berechnungszettel.getKinder() + berechnungszettel.getWeiterePersonen();
	}

	/**
	 * Liefert den Bedarf für die angegebene Anzahl Kinder
	 *
	 * @return Betrag Kinder
	 */
	public BigDecimal getBetragKinder() {
		return justGetValueForType(BerechnungsZettelCFTypen.KINDER);
	}


	/**
	 * Liefert den Betrag für angegebene weitere Personen
	 *
	 * @return Betrag für angegebene weitere Personen
	 */
	public BigDecimal getBetragWeiterePersonen() {
		return justGetValueForType(BerechnungsZettelCFTypen.WEITEREPERSONEN);
	}

	public int getAnzahlWeiterePersonen() {
		return berechnungszettel.getWeiterePersonen();
	}

	public void setAnzahlWeiterePersonen(int anzahl) {
		berechnungszettel.setWeiterePersonen(anzahl);

		createOrUpdate(BerechnungsZettelCFTypen.WEITEREPERSONEN, Berechnung.WEITERE_PERSON.multiply(new BigDecimal("" + anzahl)));
	}

	public boolean isTarifABC() {
		return berechnungszettel.isAußerhalbABCCheck();
	}

	public BigDecimal getTarifABC() {
		return justGetValueForType(BerechnungsZettelCFTypen.AUSSERHALBTARIFABC);
	}

	public void setTarifABC(boolean checked) {
		berechnungszettel.setAußerhalbABCCheck(checked);
		if (checked) {

			BigDecimal abcTarif = Berechnung.ABC_TARIF.divide(new BigDecimal("" + 6), 0, BigDecimal.ROUND_FLOOR);

			createOrUpdate(BerechnungsZettelCFTypen.AUSSERHALBTARIFABC, abcTarif);
		} else {
			createOrUpdate(BerechnungsZettelCFTypen.AUSSERHALBTARIFABC, BigDecimal.ZERO);
		}
	}

	public BigDecimal getKindergeld() {
		return justGetValueForType(BerechnungsZettelCFTypen.KINDERGELD);
	}

	public void setKindergeld(BigDecimal betrag) {
		createOrUpdate(BerechnungsZettelCFTypen.KINDERGELD, betrag);
	}

	public BigDecimal getZusaetzlicheKosten() {
		return justGetValueForType(BerechnungsZettelCFTypen.ZUSAETZLICH);
	}


	public void setZusaetzlicheKosten(BigDecimal betrag) {
		createOrUpdate(BerechnungsZettelCFTypen.ZUSAETZLICH, betrag, true);
	}

	public BigDecimal getWohngeld() {
		return getAnrechnungsverhaeltnis().multiply(justGetValueForType(BerechnungsZettelCFTypen.WOHNGELD));
	}

	public void setWohngeld(BigDecimal betrag) {
		createOrUpdate(BerechnungsZettelCFTypen.WOHNGELD, betrag);
	}

	public boolean isSchuldenGekappt() {
		return berechnungszettel.isSchuldenkappungCheck();
	}

	public void setSchuldenGekappt(boolean checked) {
		berechnungszettel.setSchuldenkappungCheck(checked);
		updateViews();
	}

	public BigDecimal getSchulden() {
		BigDecimal betrag = justGetValueForType(BerechnungsZettelCFTypen.SCHULDEN);
		if (isSchuldenGekappt())
			return getKappungSchuldenBetrag(betrag);
		else return betrag;
	}

	public void setSchulden(BigDecimal betrag) {
		createOrUpdate(BerechnungsZettelCFTypen.SCHULDEN, betrag, true);
	}


	/**
	 * Errechnet die Summe des Bedarfs
	 *
	 * @return Summe Bedarf
	 */
	public BigDecimal getSummeBedarf() {

		BigDecimal summeBedarf = new BigDecimal("0.0");

		summeBedarf = summeBedarf.add(getGrundBedarf());
		summeBedarf = summeBedarf.add(getBetragKinder());
		summeBedarf = summeBedarf.add(getBetragWeiterePersonen());
		summeBedarf = summeBedarf.add(getMehrbedarf());
		summeBedarf = summeBedarf.add(getKrankenkasse());
		summeBedarf = summeBedarf.add(getMieteEffektiv());
		summeBedarf = summeBedarf.add(getAuslandskosten());
		summeBedarf = summeBedarf.add(getUnterhalt());
		summeBedarf = summeBedarf.add(getMedKosten());
		summeBedarf = summeBedarf.add(getErnaehrung());
		summeBedarf = summeBedarf.add(getZusaetzlicheKosten());
		summeBedarf = summeBedarf.add(getTarifABC());


		return summeBedarf;

	}


	/**
	 * Setzt die Summedes Bedarfs unter Berücksichtigung der Schulden und des Schuldenkappungsbetrages
	 *
	 * @return Summe Bedarf Plus  Schulden
	 */
	public BigDecimal getSummeBedarfPlusSchulden() {
		return getSummeBedarf().add(getSchulden());

	}


	/**
	 * Stellt den Text zusammen, der im BedarfPanel angezeigt wird
	 *
	 * @return Text
	 */
	public String getText() {

		BigDecimal betragKinder = getBetragKinder();
		StringBuilder text = new StringBuilder("Grundbedarf: " + DeutschesDatum.getEuroFormatted(getGrundBedarf()) + "\n");

		int anzahlKinder = getAnzahlKinder();
		int anzahlWeiterePersonen = berechnungszettel.getWeiterePersonen();

		if (anzahlKinder > 0)
			text.append(anzahlKinder + " Kind(er): " + DeutschesDatum.getEuroFormatted(betragKinder) +
					" (" + anzahlKinder + " * " + DeutschesDatum.getEuroFormatted(Berechnung.KIND) + ")\n");


		if (anzahlWeiterePersonen > 0)
			text.append(anzahlWeiterePersonen + " weitere Person(en): " + DeutschesDatum.getEuroFormatted(getBetragWeiterePersonen()) +
					" (" + anzahlWeiterePersonen + " * " + DeutschesDatum.getEuroFormatted(Berechnung.WEITERE_PERSON) + ")\n");

		if (null != getAntrag().getHaerteListe() && getAntrag().getHaerteListe().size() > 0) {
			for (AntragHaerte ah : getAntrag().getHaerteListe()) {
				if (ah.isAnerkannt()) {
					if (ah.getHaertegrund().equals(Haertegrund.SCHWANGERSCHAFT))
						text.append("Schwangerschaft: " + DeutschesDatum.getEuroFormatted(Berechnung.SCHWANGERSCHAFT) + "\n");

					if (ah.getHaertegrund().equals(Haertegrund.ALLEINERZIEHEND))
						text.append("Alleinerziehend: " + DeutschesDatum.getEuroFormatted(Berechnung.ALLEINERZIEHEND) + "\n");

					if (ah.getHaertegrund().equals(Haertegrund.BEH_CHRONKRANK))
						text.append("chronisch krank: " + DeutschesDatum.getEuroFormatted(Berechnung.CHRONISCH_KRANK) + "\n");
				}
			}
		}
		if (getKrankenkasse().compareTo(BigDecimal.ZERO) == 1)
			text.append("Krankenkasse: " + DeutschesDatum.getEuroFormatted(getKrankenkasse()) + "\n");

		if (getAuslandskosten().compareTo(BigDecimal.ZERO) == 1)
			text.append("Auslandskosten: " + DeutschesDatum.getEuroFormatted(getAuslandskosten()) + "\n");


		NumberFormat defaultFormat = NumberFormat.getPercentInstance();
		defaultFormat.setMinimumFractionDigits(2);

		text.append("Anrechnungsverhältnis: " + defaultFormat.format(getAnrechnungsverhaeltnis()) + "\n");

		return text.toString();

	}


	/**
	 * Liefert Differenzbetrag von Bedarf und Einkommen
	 *
	 * @return Differenzbetrag von Bedarf und Einkommen
	 */
	public BigDecimal getDifferenzBetrag() {
		return getSummeBedarfPlusSchulden().subtract(getSummeEinkommen());

	}


	/**
	 * Berechnet und liefert die Einkommenspunkte (Differenzbetrag geteilt durch 17 und dann auf ganze Zahl
	 * aufgerundet)
	 *
	 * @return Einkommenspunkte
	 */
	public int getPunkteEinkommen() {
		return getDifferenzBetrag().divide(new BigDecimal("17"), 0, BigDecimal.ROUND_UP).intValue();
	}


	/**
	 * Liefert die Punkte aus dem HärtePanel
	 *
	 * @return Punkte aus dem HärtePanel
	 */
	public int getPunkteHaerte() {
		return getAntrag().getPunkteHaerte();
	}


	/**
	 * Liefert die Gesamtpunktzahl (Punkte Einkommen + Punkte Härten)
	 *
	 * @return Gesamtpunktzahl
	 */
	public int getGesamtPunkte() {
		return getPunkteEinkommen() + getPunkteHaerte();
	}


	/**
	 * Aktualisiert ZettelPanel1 (Observer)
	 */
	public void updateViews() {

		setChanged();
		notifyObservers();

	}


	public BigDecimal getSchuldenProzent() {
		return Berechnung.SCHULDEN;
	}


	/**
	 * @return true wenn das Anrechnungsverhältnis kleiner 1, false sonst
	 */
	public boolean isMieteGekappt() {
		return (getAnrechnungsverhaeltnis().compareTo(new BigDecimal("1.0")) == -1);
	}


	/**
	 * @return Summe Einkommen
	 */
	public BigDecimal getSummeEinkommen() {

		BigDecimal summe = new BigDecimal("0.0");

		for (int i = 0; i < Settings.ANZAHL_FREIE_EINKOMMENSFELDER; i++) {
			summe = summe.add(getFreiesEinkommensfeldValue(i));
		}

		for (int i = 0; i < 4; i++) {
			summe = summe.add(getEinkommen(i));
		}


		summe = summe.add(getWohngeld());

		summe = summe.add(getKindergeld());

		return summe;

	}


	public boolean isWarnungKind() {
		return getAnzahlKinder() > 0;
	}

	public boolean isWarnungMedPsych() {
		BigDecimal medPsychKosten = new BigDecimal("" + Berechnung.MED_PSYCH_KOSTEN).divide(new BigDecimal("6"), 2, BigDecimal.ROUND_HALF_UP);
		return medPsychKosten.compareTo(getMedKosten()) < 0;
	}


	//Hilfsmethode für Standard-Getter
	private BigDecimal justGetValueForType(BerechnungsZettelCFTypen typ) {
		BigDecimal sum = BigDecimal.ZERO;
		if (null != getLabeledDecimalListByType(typ)) {
			for (LabeledDecimalList ldl : berechnungszettel.getBerechnungsWerteListe()) {
				if (typ.equals(ldl.getTyp())) {
					sum = sum.add(ldl.getValue());
				}
			}
		}

		return sum;
	}

	private void createOrUpdate(BerechnungsZettelCFTypen typ, BigDecimal betrag) {
		createOrUpdate(typ, betrag, false);
	}

	//Hilfsmethode für LabeledDecimalList
	private void createOrUpdate(BerechnungsZettelCFTypen typ, BigDecimal betrag, boolean overwrite) {

		if (overwrite) {
			for (Iterator<LabeledDecimalList> iter = berechnungszettel.getBerechnungsWerteListe().listIterator(); iter.hasNext(); ) {
				LabeledDecimalList list = iter.next();
				if (list.getTyp() == typ) {
					iter.remove();
				}
			}
		}

		LabeledDecimalList ldl = getLabeledDecimalListByType(typ);
		if (null == ldl) {
			ldl = new LabeledDecimalList();
			ldl.setTyp(typ);
			ldl.setValue(betrag);
			ldl.setBrzId(berechnungszettel.getBrzId());
			berechnungszettel.getBerechnungsWerteListe().add(ldl);
			updateViews();
		} else {
			if (!ldl.getValue().equals(betrag)) {
				ldl.setValue(betrag);
				updateViews();
			}
		}
	}


	/**
	 * Liefert den Betrag der Schuldenkappung
	 *
	 * @return Betrag Schuldenkappung
	 */
	private BigDecimal getKappungSchuldenBetrag(BigDecimal betrag) {

		BigDecimal einkommenSumme = getSummeEinkommen();

		//4 Stellen hier wichtig wegen Genauigkeit bei Division durch 100!
		BigDecimal kappungBetrag = einkommenSumme.divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP).multiply(Berechnung.SCHULDEN);

		//Schuldenkappungsbetrag falls Schulden z.B. 0 und Einkommen > 0
		if (betrag.compareTo(kappungBetrag) <= 0)
			return betrag;
		else
			return kappungBetrag;


	}

	public void setFreiesEinkommensfeld(int z, BigDecimal betrag) {
		getFreieEinkommensFelder(z).setValue(betrag);
		updateViews();
	}

	public void setFreiesEinkommensfeldName(int z, String text) {
		getFreieEinkommensFelder(z).setLabel(text);
	}

	public String getFreiesEinkommensfeldName(int z) {
		LabeledDecimalList ldl = getFreieEinkommensFelder(z);
		if (null != ldl && ldl.getLabel() != null)
			return ldl.getLabel();
		else
			return "";
	}

	public BigDecimal getFreiesEinkommensfeldValue(int z) {
		LabeledDecimalList ldl = getFreieEinkommensFelder(z);
		if (null != ldl && ldl.getValue() != null)
			return ldl.getValue();
		else
			return BigDecimal.ZERO;
	}

	public BigDecimal getEinkommen(int z) {
		LabeledDecimalList ldl = getEinkommensfeld(z);
		if (null != ldl && ldl.getValue() != null)
			return ldl.getValue();
		else
			return BigDecimal.ZERO;
	}

	public void setEinkommen(int z, BigDecimal betrag) {
		getEinkommensfeld(z).setValue(betrag);
		updateViews();
	}

	public void setEinkommenName(int z, String label) {
		getEinkommensfeld(z).setLabel(label);
		updateViews();
	}

	public String getEinkommenName(int z) {
		LabeledDecimalList ldl = getEinkommensFeld(z);
		if (null != ldl && ldl.getValue() != null)
			return ldl.getLabel();
		else
			return "";
	}

	public LabeledDecimalList getEinkommensfeld(int z) {
		return getEinkommensFeld(z);
	}

	public BigDecimal getSchuldenKappe() {
		return getKappungSchuldenBetrag(getSummeEinkommen());
	}

	public void saveBRZ() {
		new DBHandlerBerechnungszettel().saveOrUpdateBRZ(berechnungszettel);
	}

	public void setKostenRechner(int index, BerechnungsZettelCFTypen type, int divider, List<BigDecimal> values,
								 BigDecimal sum) {
		LabeledDecimalList ldl = getKostenFeld(index);
		ldl.setTyp(type);
		ldl.setValue(sum);
		ldl.setBrzId(berechnungszettel.getBrzId());
		ldl.setValueList(values);
		ldl.setDivisor(divider);

		if (type != BerechnungsZettelCFTypen.PLATZHALTER) {
			for (int i = berechnungszettel.getBerechnungsWerteListe().size() - 1; i > 10; i--) {
				if (berechnungszettel.getBerechnungsWerteListe().get(i).getTyp() == type) {
					berechnungszettel.getBerechnungsWerteListe().remove(i);
				}
			}
		}
	}

	public void deleteBRZ() {
		new DBHandlerBerechnungszettel().delete(berechnungszettel);
	}

	public Antrag getAntrag() {
		return antragModel.getAntrag();
	}

	public boolean isHaerteKind() {
		for (AntragHaerte ah : getAntrag().getHaerteListe()) {
			if (ah.getHaertegrund() == Haertegrund.KIND) {
				return (ah.isAnerkannt() && ah.isAngegeben());
			}
		}
		return false;
	}

	public void setHaerteKind(boolean haerteKind) {
		for (AntragHaerte ah : getAntrag().getHaerteListe()) {
			if (ah.getHaertegrund() == Haertegrund.KIND) {
				ah.setAnerkannt(haerteKind);
				ah.setAngegeben(haerteKind);
			}
		}
	}

	public boolean isHaerteMedkosten() {
		if (getMedKosten().compareTo(Berechnung.MED_PSYCH_KOSTEN) >= 0) {
			for (AntragHaerte ah : getAntrag().getHaerteListe()) {
				if (ah.getHaertegrund() == Haertegrund.MEDVERSORGUNG) {
					ah.setAnerkannt(true);
					ah.setAngegeben(true);
				}
			}
			return true;
		}
		for (AntragHaerte ah : getAntrag().getHaerteListe()) {
			if (ah.getHaertegrund() == Haertegrund.MEDVERSORGUNG) {
				return (ah.isAnerkannt() && ah.isAngegeben());
			}
		}
		return false;
	}

	public void setHaerteMedkosten(boolean haerteMed) {
		for (AntragHaerte ah : getAntrag().getHaerteListe()) {
			if (ah.getHaertegrund() == Haertegrund.MEDVERSORGUNG) {
				ah.setAnerkannt(haerteMed);
				ah.setAngegeben(haerteMed);
			}
		}
	}

	public boolean isHaerteAuslandkosten() {
		for (AntragHaerte ah : getAntrag().getHaerteListe()) {
			if (ah.getHaertegrund() == Haertegrund.ARBEITSERLAUBNIS) {
				return (ah.isAnerkannt() && ah.isAngegeben());
			}
		}
		return false;
	}

	public AntragModel getAntragModel() {
		return antragModel;
	}

	public void setMietkappungMiete(boolean mietkappung) {
		berechnungszettel.setKeineMietkappung(mietkappung);
		updateViews();
	}

	public boolean isKeineMietkappungMiete() {
		return berechnungszettel.isKeineMietkappung();
	}


	public LabeledDecimalList getLabeledDecimalListByType(BerechnungsZettelCFTypen type) {
		for (LabeledDecimalList list : this.berechnungszettel.getBerechnungsWerteListe()) {
			if (list.getTyp().equals(type))
				return list;
		}
		return null;
	}


	//holt das Element an n-ter Stelle
	public LabeledDecimalList getFreieEinkommensFelder(int n) {
		int i = 0;
		for (LabeledDecimalList list : this.berechnungszettel.getBerechnungsWerteListe()) {
			//gefunden
			if (list.getTyp().equals(BerechnungsZettelCFTypen.FREIES_EINKOMMENSFELD)) {
				if (i == n)
					return list;
				else if (i < n)
					i++;
				else
					continue;
			}
		}

		return null;

	}

	//holt das Kostenfeld an n-ter stelle
	public LabeledDecimalList getKostenFeld(int n) {
		return this.berechnungszettel.getBerechnungsWerteListe().get(n);
	}

	//holt das Element an n-ter Stelle
	public LabeledDecimalList getEinkommensFeld(int n) {
		int i = 0;
		for (LabeledDecimalList list : this.berechnungszettel.getBerechnungsWerteListe()) {
			//gefunden
			if (list.getTyp().equals(BerechnungsZettelCFTypen.EINKOMMENSFELD)) {
				if (i == n)
					return list;
				else if (i < n)
					i++;
				else
					continue;
			}
		}

		return null;

	}
}
