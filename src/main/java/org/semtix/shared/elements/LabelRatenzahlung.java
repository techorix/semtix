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
 * Formularkomponente: Zeigt im PersonFormular neben dem Namen mit dem Buchstaben "R" in einem 
 * farbigen Käschen an, ob für diese Person Ratenzahlung gesetzt ist.
 *
 */
@SuppressWarnings("serial")
public class LabelRatenzahlung
extends JLabel{
	
	/**
	 * Erstellt ein neues LabelRatenzahlung
	 */
	public LabelRatenzahlung(){
		
		// Rahmen festlegen
		Border border = BorderFactory.createLineBorder(Color.BLACK);
        Border margin = new EmptyBorder(5, 5, 5, 5);
		
        // Buchstabe in dem Kästchen
		setText("I");
		
		// Tooltip festlegen (Überfahren mit der Maus)
		setToolTipText("<html>Diese Person ist <b>Individualzahler</b> (gewesen). <br> &nbsp; <br> <i>Siehe Antrag<i></html>");
		
		// Größe des Kästchens festlegen
		setPreferredSize(new Dimension(26,26));
		
		// Farbe der Schrift in dem Kästchen
		setForeground(Color.WHITE);
		
		// Hintergrundfarbe (Füllfarbe des Kästchens)
		setBackground(new Color(246, 129, 129));
		setOpaque(true);
		
		// Horizontale Textausrichtung im Label
		setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		
		// Schriftgrösse und -stärke
		setFont(this.getFont().deriveFont(Font.BOLD, 18f));
		
		// Rahmen setzen
		setBorder(new CompoundBorder(border, margin));
		
	}

}
