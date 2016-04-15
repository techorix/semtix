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

package org.semtix.shared.bearbeitungsprotokoll;

import org.semtix.db.DBHandlerUser;
import org.semtix.db.dao.Antrag;
import org.semtix.db.dao.Vorgang;
import org.semtix.shared.actions.ActionCloseDialog;
import org.semtix.shared.daten.DeutschesDatum;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Dialog zur Anzeige des Bearbeitungsprotokoll.
 * Soll aufgerufen werden können vom Antragsformular und zeigt
 * alle Vorgänge an, die zu diesem Antrag in der DB eingetragen sind.
 *
 */
@SuppressWarnings("serial")
public class DialogProtokoll
extends JDialog
implements Observer {
	
	private SForm protokollPanel;

	private JLabel lbSemester, lbName, lbMatrikelnr;
	
	private JLabel lbDateInsert, lbUserInsert, lbDateLastChange, lbUserLastChange;

	private Border emptyBorder = BorderFactory.createEmptyBorder(5, 10, 5, 10);
	private Border lineBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK);
	private Border lineBorderBold = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK);
	private Border compundBorder = BorderFactory.createCompoundBorder(lineBorder, emptyBorder);
	private Border compundBorderBold = BorderFactory.createCompoundBorder(lineBorderBold, emptyBorder);
	
	
	/**
	 * Erstellt einen Dialog mit dem Bearbeitungsprotokoll.
	 */
	public DialogProtokoll() {
		
		setTitle("Bearbeitungsprotokoll");

		setSize(550, 500);
		
		// Frame auf dem Bildschirm zentrieren
		setLocationRelativeTo(getParent());

		// Dialog wird modal gesetzt
		setModal(true);

		JLabel lbTitel = new JLabel("Vorgänge zum Antrag");
		// Verändert die Schriftgröße
		lbTitel.setFont(this.getFont().deriveFont(Font.BOLD, 16f));
		// Setzt die Schriftfarbe
		lbTitel.setForeground(new Color(0, 155, 0));


		lbSemester = new JLabel();
		lbName = new JLabel();
		lbMatrikelnr = new JLabel();

		lbName.setFont(lbName.getFont().deriveFont(Font.BOLD, 16f));
		lbMatrikelnr.setFont(lbMatrikelnr.getFont().deriveFont(Font.BOLD, 16f));
	
		
		SForm headPanel = new SForm();
		headPanel.setBackground(new Color(200, 200, 200));
		headPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
		
		
		headPanel.add(lbTitel,              0,  0, 2, 1, 0.0, 0.0, 2, 17, new Insets(5, 5, 5, 5));
		headPanel.add(lbSemester,           0,  1, 2, 1, 0.0, 0.0, 0, 17, new Insets(0, 5, 5, 5));
		headPanel.add(lbName,               0,  3, 1, 1, 0.0, 0.0, 0, 17, new Insets(15, 5, 5, 5));
		headPanel.add(lbMatrikelnr,         1,  3, 1, 1, 1.0, 1.0, 0, 13, new Insets(15, 5, 5, 5));
		
		
		protokollPanel = new SForm();
		protokollPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JScrollPane scrollPane = new JScrollPane(protokollPanel);
		scrollPane.setBorder(null);

		
		JLabel lbVorgang = new JLabel("Vorgang");
		JLabel lbDatum = new JLabel("Datum", JLabel.CENTER);
		JLabel lbUser = new JLabel("Bearbeiter_in", JLabel.CENTER);
		lbVorgang.setFont(lbVorgang.getFont().deriveFont(Font.BOLD));
		lbDatum.setFont(lbVorgang.getFont().deriveFont(Font.BOLD));
		lbUser.setFont(lbVorgang.getFont().deriveFont(Font.BOLD));
		lbVorgang.setBorder(compundBorderBold);
		lbDatum.setBorder(compundBorderBold);
		lbUser.setBorder(compundBorderBold);
		
		protokollPanel.add(lbVorgang,            0,  0, 1, 1, 0.4, 0.0, 2, 18, new Insets(5, 5, 5, 0));
		protokollPanel.add(lbDatum,              1,  0, 1, 1, 0.0, 0.0, 2, 11, new Insets(5, 0, 5, 0));
		protokollPanel.add(lbUser,               2,  0, 1, 1, 0.0, 0.0, 2, 11, new Insets(5, 0, 5, 5));


		JPanel buttonPanel = new JPanel();
		
		JButton abbrechenButton = new JButton(new ActionCloseDialog(this, "Schließen"));

		buttonPanel.add(abbrechenButton);
		
		
		SForm mainPanel = new SForm();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		mainPanel.add(headPanel,              0,  0, 2, 1, 0.0, 0.0, 2, 18, new Insets(0, 0, 0, 0));
		mainPanel.add(scrollPane,             0,  1, 2, 1, 1.0, 1.0, 1, 18, new Insets(0, 0, 0, 0));

		
		add(mainPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);	
		
	}
	
	
	/**
	 * Vorgänge werden im Bearbeitungsprotokoll angezeigt.
	 * @param vorgaengeListe Liste mit Vorgängen zum Antrag
	 */
	private void setData(ArrayList<Vorgang> vorgaengeListe) {
		
		int zeile = 1;
		
		// Vorgänge Antrag angelegt und zuletzt geändert
		JLabel lbTextInsert = addLabel("Eingabe des Antrags", JLabel.LEFT);
		lbDateInsert = addLabel("", JLabel.CENTER);
		lbUserInsert = addLabel("", JLabel.CENTER);
		
		JLabel lbTextLastChange = addLabel("Antrag zuletzt bearbeitet", JLabel.LEFT);
		lbDateLastChange = addLabel("", JLabel.CENTER);
		lbUserLastChange = addLabel("", JLabel.CENTER);

		protokollPanel.add(lbTextInsert,         0,  zeile, 1, 1, 0.4, 0.0, 2, 18, new Insets(0, 5, 0, 0));
		protokollPanel.add(lbDateInsert,         1,  zeile, 1, 1, 0.0, 0.0, 2, 11, new Insets(0, 0, 0, 0));
		protokollPanel.add(lbUserInsert,         2,  zeile, 1, 1, 0.0, 0.0, 2, 11, new Insets(0, 0, 0, 5));
		
		zeile++;
		
		protokollPanel.add(lbTextLastChange,     0,  zeile, 1, 1, 0.4, 0.0, 2, 18, new Insets(0, 5, 25, 0));
		protokollPanel.add(lbDateLastChange,     1,  zeile, 1, 1, 0.0, 0.0, 2, 11, new Insets(0, 0, 25, 0));
		protokollPanel.add(lbUserLastChange,     2,  zeile, 1, 1, 0.0, 0.0, 2, 11, new Insets(0, 0, 25, 5));
		
		zeile++;
		
		
		// Weitere Vorgänge in zeitlicher Reihenfolge anzeigen
		for(Vorgang v : vorgaengeListe) {

            String kuerzel = new DBHandlerUser().readUser(v.getUserID()).getKuerzel();
            if (null == kuerzel || kuerzel.length() < 1)
                kuerzel = "<k.A.>";

			JLabel lb1 = new JLabel(v.getVorgangsart().getText());
            JLabel lb2 = new JLabel(DeutschesDatum.ZEITSTEMPEL.format(v.getZeitstempel().getTime()), JLabel.CENTER);
            JLabel lb3 = new JLabel(kuerzel, JLabel.CENTER);
			lb1.setBorder(compundBorder);
			lb2.setBorder(compundBorder);
			lb3.setBorder(compundBorder);
			
			protokollPanel.add(lb1,         0,  zeile, 1, 1, 0.4, 0.0, 2, 18, new Insets(5, 5, 0, 0));
			protokollPanel.add(lb2,         1,  zeile, 1, 1, 0.0, 0.0, 2, 11, new Insets(5, 0, 0, 0));
			protokollPanel.add(lb3,         2,  zeile, 1, 1, 0.0, 0.0, 2, 11, new Insets(5, 0, 0, 5));
			
			zeile++;
			
		}
		
		protokollPanel.add(new JPanel(),       0,  zeile, 3, 1, 1.0, 1.0, 2, 18, new Insets(5, 5, 0, 0));
				
	}

	
	// Label für Antrag angelegt und zuletzt bearbeitet erstellen
	private JLabel addLabel(String text, int position) {

		JLabel label = new JLabel(text, position);
		
		// Formatierungen für Label (Rahmen unten, Hintergrundfarbe)
		label.setBorder(compundBorder);
		label.setBackground(new Color(190, 255, 190));
		label.setOpaque(true);
		
		return label;
		
	}



	/**
	 * Observer aktualisiert das Bearbeitungsprotokoll
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg) {

		// Daten zu Vorgängen eintragen
		setData((ArrayList<Vorgang>) arg);
		
		// ProtokollModel über Observable holen
		ProtokollModel protokollModel = (ProtokollModel) o;
		
		// Antrag von ProtokollModel holen
		Antrag antrag = protokollModel.getAntrag();
		
		// Personen- und Semesterdaten eintragen
		lbSemester.setText(protokollModel.getSemester());
		lbName.setText(protokollModel.getName());
		lbMatrikelnr.setText(protokollModel.getMatrikelnummer());

		// Antrag angelegt und zuletzt bearbeitet eintragen
        lbDateInsert.setText(DeutschesDatum.ZEITSTEMPEL.format(antrag.getDatumAngelegt().getTime()));
        lbUserInsert.setText(protokollModel.getUserAngelegt());
        lbDateLastChange.setText(DeutschesDatum.ZEITSTEMPEL.format(antrag.getDatumGeaendert().getTime()));
        lbUserLastChange.setText(protokollModel.getUserGeaendert());

	}

}
