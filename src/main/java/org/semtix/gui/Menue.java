/*
 *
 *  * Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 *  *        Semesterticketbüro der Humboldt-Universität Berlin
 *  *
 *  * Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
 *  * 2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)
 *  *
 *  *     This program is free software: you can redistribute it and/or modify
 *  *     it under the terms of the GNU Affero General Public License as
 *  *     published by the Free Software Foundation, either version 3 of the
 *  *     License, or (at your option) any later version.
 *  *
 *  *     This program is distributed in the hope that it will be useful,
 *  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *     GNU Affero General Public License for more details.
 *  *
 *  *     You should have received a copy of the GNU Affero General Public License
 *  *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */



package org.semtix.gui;


import org.semtix.gui.admin.DialogAdminTools;
import org.semtix.gui.auszahlung.DialogArchivierung;
import org.semtix.gui.auszahlung.DialogSpaetis;
import org.semtix.gui.auszahlung.auszahlungsmodul.DialogWizard;
import org.semtix.gui.filter.ActionFilterAntraege;
import org.semtix.gui.info.DialogAbout;
import org.semtix.gui.info.DialogHilfe;
import org.semtix.gui.info.DialogSemester;
import org.semtix.gui.info.DialogUserInfo;
import org.semtix.gui.person.emailsuche.ActionSucheEmail;
import org.semtix.gui.person.personensuche.ActionSuchePerson;
import org.semtix.gui.semester.DialogAktuellesSemester;
import org.semtix.gui.semester.DialogNeuesSemester;
import org.semtix.gui.statistik.DialogStatistikArbeit;
import org.semtix.gui.statistik.DialogStatistikBuchstaben;
import org.semtix.shared.textbausteine.DialogNachfragebriefe;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
* Menüleiste für den Hauptframe. 
*/
@SuppressWarnings("serial")
public class Menue
extends JMenuBar
implements ActionListener {
	
	private MainControl mainControl;
	
	private ActionSuchePerson actionSuchePerson;
	private ActionSucheEmail actionSucheEmail;
	private ActionFilterAntraege actionFilterAntraege;
	
	private JMenuItem radioButtonHU, radioButtonKW;

	/**
	 * Erzeugt ein Menü für den Hauptframe.
	 * @param mc MainControl
	 */
	public Menue(MainControl mc) {

		this.mainControl = mc;
		
		actionSuchePerson = new ActionSuchePerson("Person suchen", mainControl);
		actionSucheEmail = new ActionSucheEmail(mainControl);
		actionFilterAntraege = new ActionFilterAntraege("Auswahl filtern", mainControl);
		
		radioButtonHU = new JRadioButtonMenuItem("Humboldt-Uni");
		radioButtonKW = new JRadioButtonMenuItem("Weißensee");

		
		JMenu m;
		JMenu subm;
		JMenu subsubm;
		JMenuItem mi;

		m = new JMenu("Datenbank");
		mi = new JMenuItem("Aktuelles Semester");
		mi.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new DialogAktuellesSemester(mainControl);
			}
		});
		m.add(mi);
		
		mi = new JMenuItem("Neues Semester anlegen...");
		mi.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new DialogNeuesSemester();
			}
		});
		m.add(mi);
		
		m.addSeparator();


        mi = new JMenuItem("Textbausteine");
        mi.addActionListener(this);
        m.add(mi);

        m.addSeparator();
		
		mi = new JMenuItem("Auszahlungsmodul starten...");
		mi.addActionListener(this);
		m.add(mi);

		mi = new JMenuItem("Auszahlung von Spätis...");
		mi.addActionListener(this);
		m.add(mi);

		mi = new JMenuItem("Archivierung");
		mi.setActionCommand("Archivierung");
		mi.addActionListener(this);
		m.add(mi);
		
		m.addSeparator();
		
		mi = new JMenuItem("Beenden");
		mi.addActionListener(this);
		m.add(mi);

		add(m);

		m = new JMenu("Person");
		mi = new JMenuItem(actionSuchePerson);
		m.add(mi);
		mi = new JMenuItem(actionSucheEmail);
		m.add(mi);
		mi = new JMenuItem(actionFilterAntraege);
		m.add(mi);
        add(m);

		m = new JMenu("Berichte");
		mi = new JMenuItem("Buchstabenverteilung");
		mi.addActionListener(this);
        m.add(mi);
        mi = new JMenuItem("Aktivitätenstatistik");
        mi.addActionListener(this);
        m.add(mi);
		add(m);

		
		
		// ***** AdminTools *****
		
		m = new JMenu("Admin");
		mi = new JMenuItem("AdminTools");
		mi.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new DialogAdminTools(mainControl);
			}
		});
        mi.setEnabled(true);
        m.add(mi);
		add(m);
		

		m = new JMenu("Hilfe");
        mi = new JMenuItem("Tastaturkürzel");
        mi.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new DialogHilfe();
			}
		});
		m.add(mi);
		
		mi = new JMenuItem("Über das Programm");
		mi.addActionListener(this);
		m.add(mi);
		add(m);
		
		m = new JMenu("Info");
        mi = new JMenuItem("Aktueller Benutzer");
        mi.addActionListener(this);
		m.add(mi);
		mi = new JMenuItem("Aktuelle Semester");
		mi.addActionListener(this);
		m.add(mi);
		add(m);

	}


    /**
     * Fängt die Menu-Actions auf und startet die entsprechenden GUI-Dialoge
     *
	 * @param e Actio

	 */
    public void actionPerformed(ActionEvent e) {


        if (e.getActionCommand().equals("Beenden")) {
			mainControl.cleanUpBeforeExit();
        }


        if (e.getActionCommand().equals("Textbausteine")) {
            new DialogNachfragebriefe();
		}


		
		if (e.getActionCommand().equals("Auszahlungsmodul starten...")) {
			new DialogWizard(mainControl);
		}
		
		
		if (e.getActionCommand().equals("Archivierung")) {
            new DialogArchivierung(mainControl);
        }
		
		if (e.getActionCommand().equals("Auszahlung von Spätis...")) {
			new DialogSpaetis(mainControl);
		}
				
		if (e.getActionCommand().equals("Buchstabenverteilung")) {
			new DialogStatistikBuchstaben();
		}

        if (e.getActionCommand().equals("Aktivitätenstatistik")) {
            new DialogStatistikArbeit();
        }

        if (e.getActionCommand().equals("Über das Programm")) {
			new DialogAbout();
		}

        if (e.getActionCommand().equals("Aktueller Benutzer")) {
            new DialogUserInfo();
		}

		if (e.getActionCommand().equals("Aktuelle Semester")) {
			new DialogSemester();
		}
	
	}

}
