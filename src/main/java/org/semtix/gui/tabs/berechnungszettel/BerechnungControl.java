/*

  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
         Semesterticketbüro der Humboldt-Universität Berlin

  Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */


package org.semtix.gui.tabs.berechnungszettel;

import org.semtix.db.DBHandlerUser;
import org.semtix.db.DBHandlerVorgaenge;
import org.semtix.db.dao.Person;
import org.semtix.db.dao.Vorgang;
import org.semtix.gui.tabs.TabControl;
import org.semtix.gui.tabs.TabInterface;
import org.semtix.gui.tabs.antrag.AntragModel;
import org.semtix.gui.tabs.berechnungszettel.otherpanels.HeadPanel;
import org.semtix.shared.daten.enums.AntragAblehnungsgrund;
import org.semtix.shared.daten.enums.AntragStatus;
import org.semtix.shared.daten.enums.Vorgangsart;

import javax.swing.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;


/**
 * Klasse zur Initialisierung und Steuerung der Views
 *
 * Die einzelnen Listener enthalten die meisten Control-Operationen
 *
 * Das Model enthält die Businesslogik
 */
public class BerechnungControl implements TabInterface {

	private TabControl tabControl;

	private BerechnungModel berechnungModel;
	private BerechnungView berechnungView;


	/**
	 * Erstellt neue BerechnungControl
	 *
	 * @param tabControl TbaControl
	 */
	public BerechnungControl(TabControl tabControl) {

		this.tabControl = tabControl;

		berechnungView = new BerechnungView(this);

		// Scrollpane mit BerechnungView in TabView (CardLayout) hinzufügen
		tabControl.addView(berechnungView.getBerechnungPanel(), "Berechnung");


	}


	/**
	 * Setzt das Panel im CardLayout
	 *
	 * @param cardName Name im CardLayout
	 */
	public void setCards(String cardName) {
		tabControl.setCards(cardName);
	}


	/**
	 * Liefert das BerechnungModel
	 *
	 * @return BerechnungModel
	 */
	public BerechnungModel getBerechnungModel() {
		return berechnungModel;
	}


	/**
	 * Initialisiert den Berechnungszettel mit den Antrag- und Persondaten
	 *
	 * @param antragModel Antrag-Objekt
	 * @param person      Person-Objekt
	 */
	public void init(AntragModel antragModel, Person person) {

		berechnungModel = new BerechnungModel(antragModel);
		berechnungModel.addObserver(berechnungView.getZettel1());
		berechnungModel.addObserver(berechnungView.getZettel1().getMoneyPanel());
		berechnungModel.addObserver(berechnungView.getZettel1().getHaertePanel());


		// ***** Startwerte in Zettel1 einsetzen (Name, Matrikelnummer, aktuelles Datum...) *****

		// Datumsformat festlegen
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

		HeadPanel headPanel = berechnungView.getZettel1().getHeadPanel();

		headPanel.setNachname(person.getNachname());
		headPanel.setVorname(person.getVorname());
		headPanel.setMatrikelnummer(person.getMatrikelnr());
		headPanel.setDatum(df.format(new GregorianCalendar().getTime()));

		DBHandlerVorgaenge dbHandlerVorgaenge = new DBHandlerVorgaenge();
		Vorgang vorgang = dbHandlerVorgaenge.getVorgangByVorgangsart(Vorgangsart.ERSTRECHNUNG, antragModel.getAntrag().getAntragID());
		if (vorgang != null) {
			headPanel.setZeichnung1(new DBHandlerUser().readUser(vorgang.getUserID()).getKuerzel());
		}

		Vorgang vorgang2 = dbHandlerVorgaenge.getVorgangByVorgangsart(Vorgangsart.ZWEITRECHNUNG, antragModel.getAntrag().getAntragID());
		if (vorgang2 != null) {
			headPanel.setZeichnung2(new DBHandlerUser().readUser(vorgang2.getUserID()).getKuerzel());
		}


		// ***** Init *****

		berechnungView.getZettel1().getHaertePanel().init();

		berechnungView.getZettel1().getMoneyPanel().initValues();

		berechnungView.getZettel2().init();

		setSaveStatus(false);


	}

	/**
	 * Punkte für Härten und Einkommen in AntragFormular übernehmen und vom Berechnungszettel
	 * zum AntragFormular wechseln
	 *
	 * @param vorgangsart Vorgangsartobjekt
	 */
	public void updateAntragModel(Vorgangsart vorgangsart) {

		berechnungModel.saveBRZ();


		// Punkte für Einkommen und Härten aus Berechnungszettel im AntragModel setzen
		berechnungModel.getAntragModel().setPunkte(berechnungModel.getPunkteEinkommen(), berechnungModel.getPunkteHaerte());

		// Antragsentscheidung wenn Vorgangsart (Erst- oder Zweitrechnung) übergeben wurde
		if (vorgangsart != null) {
			// Vorgang in Bearbeitungsprotokoll eintragen
			Vorgang vorgang = new Vorgang();
			vorgang.setAntragID(berechnungModel.getAntrag().getAntragID());
			vorgang.setVorgangsart(vorgangsart);
			DBHandlerVorgaenge dbHandlerVorgaenge = new DBHandlerVorgaenge();
			dbHandlerVorgaenge.createVorgang(vorgang);
			berechnungModel.getAntrag().setAntragStatus(AntragStatus.GENEHMIGT);
		}

		BigDecimal mieteKV = berechnungModel.getMiete().add(berechnungModel.getKrankenkasse());
		BigDecimal einkommenSchulden = berechnungModel.getSummeEinkommen().add(berechnungModel.getSchulden());

		if (!(berechnungModel.getAntrag().getAntragStatus() == AntragStatus.ABGELEHNT)) {
			//IMMER Ablehnen wenn Bedarf + Schulden < Einkommen
			if (berechnungModel.getSummeBedarfPlusSchulden().compareTo(berechnungModel.getSummeEinkommen()) == -1) {
				int returnstatus = JOptionPane.showConfirmDialog(null, "<html>Das Einkommen überschreitet den Bedarf. Der Antrag wird daher abgelehnt.</html>");
				if (returnstatus == JOptionPane.YES_OPTION) {
					berechnungModel.getAntrag().setAntragStatus(AntragStatus.ABGELEHNT);
					berechnungModel.getAntrag().setBegruendung(AntragAblehnungsgrund.ZUREICH.getBegruendung());

				}
			}

			//Antrag muss schlüssig sein: Miete (ohne Heizkosten) + KV > Einkommen + Schulden
			else if (mieteKV.compareTo(einkommenSchulden) == 1) {
				int returnstatus = JOptionPane.showConfirmDialog(null, "<html>Antrag wegen Unschlüssigkeit ablehnen? <br><p> Miete+KV = " + mieteKV + " Einkommen+Schulden = " + einkommenSchulden + "</html>");
				if (returnstatus == JOptionPane.YES_OPTION) {
					berechnungModel.getAntrag().setAntragStatus(AntragStatus.ABGELEHNT);
					berechnungModel.getAntrag().setBegruendung(AntragAblehnungsgrund.UNSCHLUESSIG.getBegruendung());
				}
			}

			// Antrag entscheiden (genehmigt oder abgelehnt - je nach Punktzahl)
			else if (berechnungModel.getPunkteEinkommen() == 0) {
				int returnstatus = JOptionPane.showConfirmDialog(null, "<html>Antrag ablehnen? Keine Härtepunkte bei Einkommen.<br>" + berechnungModel.getPunkteHaerte() + " Punkte aus besonderen Härten.</html>");
				if (returnstatus == JOptionPane.YES_OPTION) {
					berechnungModel.getAntrag().setAntragStatus(AntragStatus.ABGELEHNT);
					berechnungModel.getAntrag().setBegruendung(AntragAblehnungsgrund.NICHTNACHGEWIESEN.getBegruendung());
				}
			}
		}


		berechnungModel.getAntragModel().saveAntrag();

		setSaveStatus(false);

		setCards("Antrag");

	}


	public void deleteBRZ() {
		setSaveStatus(false);
		berechnungModel.deleteBRZ();
		// AntragFormular anzeigen
		setCards("Antrag");
	}

	@Override
	public void setSaveStatus(boolean status) {
		tabControl.setSaveStatus(status);
	}
}
