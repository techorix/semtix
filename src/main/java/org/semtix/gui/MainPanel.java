/*
 * Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 *        Semesterticketbüro der Humboldt-Universität Berlin
 *
 * Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
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


import org.semtix.config.SettingsExternal;
import org.semtix.gui.tabs.ButtonTabComponent;
import org.semtix.gui.tabs.TabView;

import javax.swing.*;
import java.awt.*;



/**
 * Panel mit JTabbedPane für Anzeige der Personen- und Antragfilter-Tabs.
 */
@SuppressWarnings("serial")
public class MainPanel
extends JPanel {
	
	private JTabbedPane tabbedPane;
	

	/**
	 * Erzeugt JTabbedPane und fügt sie dem Panel hinzu.
	 */
	public MainPanel() {
		
		setLayout(new BorderLayout());
		
		tabbedPane = new JTabbedPane(); 
			
		add(tabbedPane, BorderLayout.CENTER);	
		
	}
	
	
	

	/**
	 * Tab in JTabbedPane anlegen wenn Maximale Tabanzahl noch nicht erreicht ist
	 * @param tabView einzelne View eines Tabs
	 * @param buttonTabComponent die Buttonkomponente (oben) des Tabs
	 */
	public void addTab(TabView tabView, ButtonTabComponent buttonTabComponent) {
		
		// nur anlegen falls max. Anzahl Tabs noch nicht erreicht
		if (allowMoreTabs()) {

			// TabView (Panel) zu TabbedPane hinzunfügen
			tabbedPane.add(tabView);
			
			// letztes hinzugefügtes Tab selektieren
			tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
			
			// TabComponent zu TabbedPane hinzufügen
			tabbedPane.setTabComponentAt(tabbedPane.getSelectedIndex(), buttonTabComponent);

		}
		
	}
	

	
	
	/**
	 * Tab aus JTabbedPane entfernen (nach Klick auf Close-Button im TabComponent).
	 * @param buttonTabComponent ButtonTabComponent
	 */
	public void removeTab(ButtonTabComponent buttonTabComponent) {
		
		int i = tabbedPane.indexOfTabComponent(buttonTabComponent);

		// Tab vom TabbedPane entfernen
		tabbedPane.remove(i);
		
	}

    /**
     * Removes the active/selected tab
     */
    public void removeTab() {
        int index = tabbedPane.getSelectedIndex();
        if (index != -1) {
            ButtonTabComponent btc = (ButtonTabComponent) tabbedPane.getTabComponentAt(index);
            btc.getTabControl().closeTab();
        }
    }

	/**
	 * Hinweis, wenn max. Anzahl an Tabs erreicht, um weitere Tabs zu verhindern.
	 * @return
	 */
	private boolean allowMoreTabs() {

		if (tabbedPane.getTabCount() >= SettingsExternal.MAXTABS) {
			String message = "Maximale Anzahl an Tabs erreicht.";
            JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
            return false;
		}
		else
			return true;
		
	}


}
