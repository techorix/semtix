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

import org.semtix.config.SettingsExternal;
import org.semtix.db.DBHandlerAntrag;
import org.semtix.db.DBHandlerBerechnungszettel;
import org.semtix.db.dao.Antrag;
import org.semtix.shared.print.OdtTemplate;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

/**
 * Druckt eine Liste von Anträgen in Form von Bescheiden oder Auszahlungen. Bescheide werden doppelt gedruckt.
 * Created by Micha on 14.11.14.
 */
public class ActionPrintAntragList implements ActionListener {

	private List<Antrag> antraege;

	private GenericPanelStep panelStep = null;

	private String logText;

	private boolean bescheidundnichtauszahlung;

	public ActionPrintAntragList(List<Antrag> antraege, GenericPanelStep panelStep, String logText, boolean bescheidundnichtauszahlung) {

		super();

		this.panelStep = panelStep;
		this.antraege = antraege;
		this.logText = logText;
		this.bescheidundnichtauszahlung = bescheidundnichtauszahlung;

	}

	public void actionPerformed(ActionEvent e) {

		if (null == antraege || antraege.size() < 1) {
			JOptionPane.showMessageDialog(null, "Leider keine Anträge zum Ausdrucken vorhanden.");
		} else {
			String typ = "Auszahlungen";
			if (bescheidundnichtauszahlung) {
				typ = "Bescheide";
			}

			DBHandlerAntrag dbHandlerAntrag = new DBHandlerAntrag();

			try {
				OdtTemplate odtTemplate = new OdtTemplate();
				if (bescheidundnichtauszahlung) {

					//drucke Bescheide
					odtTemplate.printBescheide(antraege, 2);
					if (this.panelStep != null) {
						panelStep.logTextArea.logText(logText);
					}

					int eingabeGesendet = JOptionPane.showConfirmDialog(null, "Möchtest du jetzt die gesamte Liste der Anträge auf 'gesendet' setzen?",
							"Berechnungszettel löschen?", JOptionPane.YES_NO_OPTION);

					if (eingabeGesendet == JOptionPane.YES_OPTION) {
						for (Antrag a : antraege) {
							a.setGesendet(true);
							dbHandlerAntrag.updateAntrag(a);
						}

						int eingabeBRZ = JOptionPane.showConfirmDialog(null, "Möchtest du jetzt auch die Berechnungszettel dieser Anträge löschen?",
								"Berechnungszettel löschen?", JOptionPane.YES_NO_OPTION);
						if (eingabeBRZ == JOptionPane.YES_OPTION) {
							DBHandlerBerechnungszettel dbHandlerBerechnungszettel = new DBHandlerBerechnungszettel();
							for (Antrag a : antraege) {
								dbHandlerBerechnungszettel.deleteByAntragId(a.getAntragID());
							}
							if (this.panelStep != null) {
								panelStep.logTextArea.logText("Berechnungszettel für " + antraege.size() + " Anträge gelöscht.");
							}
						}
					}
				} else {

					//drucke Auszahlungsandordnungen
					odtTemplate.printAZAs(antraege, 2);

					if (this.panelStep != null) {
						panelStep.logTextArea.logText(logText);
					}

					int eingabeGesendet = JOptionPane.showConfirmDialog(null, "Möchtest du jetzt die gesamte Liste der Anträge auf 'gesendet' setzen?",
							"Berechnungszettel löschen?", JOptionPane.YES_NO_OPTION);
					if (eingabeGesendet == JOptionPane.YES_OPTION) {
						for (Antrag a : antraege) {
							a.setAuszahlung(true);
							dbHandlerAntrag.updateAntrag(a);
						}
					}
				}
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(null, "IOException beim Drucken der Bescheide|Anträge geworfen. \nVermutlich Template-Pfad oder Output-Pfad falsch");
			} catch (Exception e1) {
				if (SettingsExternal.DEBUG)
					e1.printStackTrace();
				else
					JOptionPane.showMessageDialog(null, "Fehler: Vermutlich Template-Pfad oder Output-Pfad falsch \n" + e1.getMessage());
			}
		}

	}
}


