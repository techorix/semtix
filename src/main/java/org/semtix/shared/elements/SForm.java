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

import org.semtix.config.SettingsExternal;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Hilfsklasse zum Anlegen von Formularen als Panel mit einem GridBagLayout. Um vereinfacht 
 * Formulare zu erstellen, hat diese Klasse nur 1 Methode, mit der Komponenten mit Parametern 
 * zum GridBagLayout hinzugefügt werden können.
 * 
 * <p>Beispiel wie eine Komponente dem Panel mit GridBagLayout hinzugefügt wird:</p>
 * <pre>
 * {@code
 * // Panel mit GridBagLayout (SForm) erstellen
 * SForm formular = new SForm();
 * 
 * // Label zum SForm hinzufügen
 * formular.add(new JLabel("LabelText"),  0, 0, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 5, 5, 5));
 * }
 * </pre>
 *
 */
@SuppressWarnings("serial")
public class SForm
extends JPanel {

	
	private GridBagLayout gbl;

	
	/**
	 * Erstellt ein neues Panel mit GridBagLayout.
	 */
	public SForm() {
		
		gbl = new GridBagLayout();

		if (SettingsExternal.CHANGELAYOUTMODE) {
			this.setBackground(new Color(randInt(128, 255), randInt(128, 255), randInt(128, 255)));
		}

		setLayout(gbl);
		
	}
	
	
	

	/**
	 * Fügt dem Panel eine Komponente mit den GridBagConstraints dem GridBagLayout hinzu.
	 * @param c Komponente, die hinzugefügt werden soll
	 * @param x Horizontale Position im GridBagLayout
	 * @param y Vertikale Position im GridBagLayout
	 * @param width Anzahl an Gitterzellen, über die sich der Anzeigebereich horizontal erstreckt (ausgehend vom gridx)
	 * @param height Anzahl an Gitterzellen, über die sich der Anzeigebereich vertikal erstreckt (ausgehend vom gridy)
	 * @param weightx gewichtete Verteilung des vorhandenen Platzes auf die einzelnen Gitterzellen (horizontal)
	 * @param weighty gewichtete Verteilung des vorhandenen Platzes auf die einzelnen Gitterzellen (vertikal)
	 * @param fill spezifiziert, ob und wie die Komponente ihren Anzeigebereich ausfüllt
	 * @param anchor Position einer Komponente innerhalb ihres Anzeigebereichs, falls sie diesen nicht voll ausfüllt
	 * @param insets zusätzlicher Zwischenraum an den Rändern des Anzeigebereichs der Komponente
	 */
	public void add(Component c, 
			int x, int y, 
	        int width, int height, 
	        double weightx, double weighty,
	        int fill, int anchor, Insets insets) 

		{ 
		
		GridBagConstraints gbc = new GridBagConstraints(); 
		gbc.fill = fill; 
		gbc.anchor = anchor;
		gbc.gridx = x; gbc.gridy = y; 
		gbc.gridwidth = width; gbc.gridheight = height; 
		gbc.weightx = weightx; gbc.weighty = weighty; 
		gbc.insets = insets;
		gbl.setConstraints(c, gbc); 
		add(c); 
		
		}

	public static int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}
}
