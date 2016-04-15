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

package org.semtix.gui;


import org.semtix.config.SemesterConf;
import org.semtix.config.UniConf;
import org.semtix.config.UserConf;
import org.semtix.shared.daten.enums.Uni;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * Statuszeile am unteren Rand des Frames zur Anzeige von Programminformationen.
 * (aktuelles Datum, aktuell eingestellte Universität und Arbeitssemester, eingeloggter User und Datenbankname)
 *
 */
@SuppressWarnings("serial")
public class StatusPanel
extends JPanel {
	
	// Konstante Hintergrundfarbe für HU
	private static final Color BGCOLORHU = new Color(220, 220, 220);
	// Konstante Hintergrundfarbe für KW
	private static final Color BGCOLORKW = new Color(255, 0, 0);
	// Konstante Schriftfarbe für HU
	private static final Color FGCOLORHU = new Color(0, 0, 0);
	// Konstante Schriftfarbe für KW
	private static final Color FGCOLORKW = new Color(55, 255, 255);
    private JLabel labelCurrentDate, labelUsername, labelUniversitaet;

	
	/**
	 * Erzeugt ein StatusPanel.
	 */
	public StatusPanel() {
				
		this.setLayout(new BorderLayout(5, 5));
		
		// User wird nur zu Programmstart angelegt und angezeigt (muss später nicht mehr aktualisiert werden)
        String user = "";
        if (null != UserConf.CURRENT_USER)
            user = UserConf.CURRENT_USER.getKuerzel();

        // Label für Datum, Uni und User anlegen
		labelCurrentDate = new JLabel();
		labelUniversitaet = new JLabel();
		labelUsername = new JLabel(user);

		// Label dem StatusPanel hinzufügen
		add(labelCurrentDate, BorderLayout.LINE_START);
	    labelUniversitaet.setHorizontalAlignment(SwingConstants.CENTER);
	    add(labelUniversitaet, BorderLayout.CENTER);
	    add(labelUsername, BorderLayout.LINE_END);
		
	}


    /**
	 * StatusPanel aktualisieren (aktuelles Datum, aktuell eingestellte Uni + Semester) 
	 * + farblich unterschiedliche Darstellung bei HU oder KW
	 */
	public void updatePanel() {
		
		// aktuelles Datum formatieren
		GregorianCalendar currentDate = new GregorianCalendar();
		SimpleDateFormat df = new SimpleDateFormat("E', 'dd.MM.yyyy");
		String datum = df.format(currentDate.getTime());
		
		// Daten aus Conf-Dateien in Label des StatusPanel schreiben
		labelCurrentDate.setText(datum);
        if (null != SemesterConf.getSemester())
            labelUniversitaet.setText(UniConf.aktuelleUni.toString() + " " + SemesterConf.getSemester().getSemesterKurzform());
        else
            labelUniversitaet.setText(UniConf.aktuelleUni.toString());

        // farbliche Änderung der Statuszeile je nach Auswahl der Universität (HU oder KW)
        // bei aktueller Uni KW wird die Statuszeile rot eingefärbt
        if (UniConf.aktuelleUni == Uni.KW) {
			setBackground(BGCOLORKW);
			labelCurrentDate.setForeground(FGCOLORKW);
			labelUniversitaet.setForeground(FGCOLORKW);
			labelUsername.setForeground(FGCOLORKW);
		}
		else {
			setBackground(BGCOLORHU);
			labelCurrentDate.setForeground(FGCOLORHU);
			labelUniversitaet.setForeground(FGCOLORHU);
			labelUsername.setForeground(FGCOLORHU);
		}
		
	}
	
}
