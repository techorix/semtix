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
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * 
 * Labelgestaltung für Namen und Matrikelnummer auf den einzelnen Seiten.
 * Schriftgrösse, Farbe und Rahmen werden festgelegt, Text und Ausrichtung wird übergeben.
 *
 */
@SuppressWarnings("serial")
public class LabelPerson
extends JLabel {
	
	/**
	 * Erstellt ein neues LabelPerson
	 * @param text Text in dem Label
	 * @param alignment Horizontale Ausrichtung im Label
	 */
	public LabelPerson(String text, int alignment) {
		
		// Setzt Rahmen um das Label
		Border border = BorderFactory.createLineBorder(Color.BLACK);
        Border margin = new EmptyBorder(5, 5, 5, 5);
		
        // Setzt den Text im Label
		setText(text);
		
		// Setzt Hintergrundfarbe /Füllfarbe im Label)
		this.setBackground(new Color(180, 180, 235));
		this.setOpaque(true);
		
		// Horizontale Textausrichtung im Label setzne
		setHorizontalAlignment(alignment);
		
		// Schriftgrösse und -stärke
		setFont(this.getFont().deriveFont(Font.BOLD, 16f));
		
		// Rahmen setzen
		setBorder(new CompoundBorder(border, margin));
		
	}

	/**
	 * Erstellt LabelPerson mit Linksausrichtung (default)
	 * @param text Left Aligned Text
	 */
	public LabelPerson(String text) {
		this(text, SwingConstants.LEFT);
	}

}
