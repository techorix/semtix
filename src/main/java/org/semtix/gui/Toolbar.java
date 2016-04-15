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



import org.semtix.config.UniConf;
import org.semtix.gui.filter.ActionFilterAntraege;
import org.semtix.gui.person.personensuche.ActionSuchePerson;
import org.semtix.gui.tabs.ActionNewTab;
import org.semtix.shared.daten.enums.Uni;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
* Die ToolBar im Frame der Hauptanwendung enthält Buttons zur Ausführung bestimmter Aktionen.
*/
@SuppressWarnings("serial")
public class Toolbar
extends JToolBar {
	
	// ToggleButton zur Anzeige, welche Uni aktuell ausgewählt ist
	private JToggleButton buttonUniWechseln;

	
	/**
	 * Erstellt eine ToolBar für den Hauptframe.
	 * @param mainControl Referenz auf MainControl
	 */
	public Toolbar(final MainControl mainControl) {
		
		// ToolBar kann nicht bewegt werden
		setFloatable(false);
		
		// Button öfnet den Dialog zur Personsuche
		JButton buttonPersonSuche = new JButton(new ActionSuchePerson("<html>Person<br>suchen</html>", mainControl));
		buttonPersonSuche.setFocusable(false);
		buttonPersonSuche.setPreferredSize(new Dimension(100, 40));
		
		// Button öffnet den Dialog zu Anträgeanzeige (blättern und filtern)
		JButton buttonAntraegeBlaettern = new JButton(new ActionNewTab(mainControl));
		buttonAntraegeBlaettern.setFocusable(false);
		buttonAntraegeBlaettern.setPreferredSize(new Dimension(100, 40));
		
		// Button öffnet den Dialog zu Anträgeanzeige (blättern und filtern)
		JButton buttonFilter = new JButton(new ActionFilterAntraege("Filter", mainControl));
		buttonFilter.setFocusable(false);
		buttonFilter.setPreferredSize(new Dimension(100, 40));
		
		// ToggleButton zum Wechseln des Uni-Modus
		buttonUniWechseln = new JToggleButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // wenn aktuelle Uni = HU, dann nach KW wechseln
                if(UniConf.aktuelleUni == Uni.HU) {
                    mainControl.setUni(Uni.KW);
                    buttonUniWechseln.setText("KHB");
                    buttonUniWechseln.setOpaque(true);
                    buttonUniWechseln.setForeground(Color.RED);
                    buttonUniWechseln.setToolTipText("Arbeitsmodus " + Uni.KW.getUniName());

                    // wenn aktuelle Uni = KW, dann nach HU wechseln
                } else if(UniConf.aktuelleUni == Uni.KW) {
                    mainControl.setUni(Uni.HU);
                    buttonUniWechseln.setText("HU");
                    buttonUniWechseln.setForeground(Color.BLACK);
                    buttonUniWechseln.setToolTipText("Arbeitsmodus " + Uni.HU.getUniName());
                }
            }
        });

        buttonUniWechseln.setFont(buttonUniWechseln.getFont().deriveFont(Font.BOLD));
        buttonUniWechseln.setText("HU");
        buttonUniWechseln.setToolTipText("Arbeitsmodus " + UniConf.aktuelleUni.getUniName());
        buttonUniWechseln.setFocusable(false);
        buttonUniWechseln.setPreferredSize(new Dimension(80, 35));


        add(buttonPersonSuche);
        add(buttonAntraegeBlaettern);
        add(buttonFilter);
		addSeparator(new Dimension(40,30));
		add(buttonUniWechseln);
		
		
	}


}
