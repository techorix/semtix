/*
 * Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 * Semesterticketbüro der Humboldt-Universität Berlin
 *
 * Copyright (c) 2015. Michael Mertins (MichaelMertins@gmail.com)
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

package org.semtix.gui.admin.options;

import javax.swing.*;
import java.awt.*;

/**
 * Controlklasse im AdminTool zur Steuerung der Einstellungen in den Formularen (Person, Antrag, Berechnungszettel)
 */
public class OptionControl {

    private JPanel optionPanel;
	
	private OptionPanelPerson optionPanelPerson;
	
	private JTabbedPane tabbedPane;
	
	
	/**
	 * Erstellt neue Controlklasse
	 */
	public OptionControl() {

		optionPanel = new JPanel();
		
		optionPanel.setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Personen-Formular", new OptionPanelPerson());
		tabbedPane.addTab("Antrag-Formular", new OptionPanelAntrag());
		tabbedPane.addTab("Berechnungszettel", new OptionPanelBerechnung());
        tabbedPane.addTab("Pfadangaben", new OptionPanelPfade());


		optionPanel.add(tabbedPane, BorderLayout.CENTER);
		
		
		
	}
	
	
	
	/**
	 * Liefert das Panel mit den Einstellungen.
	 * @return OptionPanel
	 */
	public JPanel getOptionPanel() {
		return optionPanel;
	}
	

}
