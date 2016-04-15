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

package org.semtix.shared.elements;


import javax.swing.*;

/**
 * 
 * Label mit einen ein- und ausblendbaren Warnzeichen und Tooltip
 * für Beschreibung der Art der Warnung.
 *
 */
@SuppressWarnings("serial")
public class WarnLabel
extends JLabel {
	
	// Grafik mit Warndreieck
	private final Icon icon = new ImageIcon( getClass().getResource( "/images/warnung.png" ) );
	
	private String tooltip;
	
	
	/**
	 * Erstellt ein WarnLabel
	 * @param tooltip Text für tooltip
	 */
	public WarnLabel(String tooltip) {
		
		this.tooltip = tooltip;
		
	}
	
	
	/**
	 * Setzt Anzeige des Icon auf dem Label
	 * @param state Icon anzeigen ja/nein
	 */
	public void setIconVisible(boolean state) {
		
		if(state)  {
			setIcon(icon);
			setToolTipText(tooltip);
		}
		else {
			setIcon(null);
			setToolTipText(null);
		}
			
		
	}
	

}
