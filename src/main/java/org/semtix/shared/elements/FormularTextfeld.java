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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 
 * Textfeld für Formulare mit vorgegebenen Parametern (zum Beispiel Textvorbelegung, Grösse, Rahmen, Ausrichtung)
 *
 */
@SuppressWarnings("serial")
public class FormularTextfeld
extends JTextField
implements ActionListener {
	

	
	/**
	 * Konstruktor
	 * @param text Textvorbelegung
	 * @param border Rahmenstärke (schwarze Linie aussenrum)
	 * @param width Breite des Textfeldes
	 * @param height Höhe des Textfeldes
	 * @param align Ausrichtung des Textes im Textfeld
	 */
	public FormularTextfeld(String text, int border, int width, int height, int align) {
		
		setText(text);
		
		setPreferredSize(new Dimension(width, height));
		setMinimumSize(new Dimension(width, height));
		setHorizontalAlignment(align);
		Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, border);
		Border emptyBorder = new EmptyBorder(0, 5, 0, 0);
		CompoundBorder compoundBorder = new CompoundBorder(lineBorder, emptyBorder);
		setBorder(compoundBorder);
		
		// Hintergrundfarbe zeigt Eingabemöglichkeit an
		setBackground(Layout.EDITABLE_FIELDS);
		
		// ActionListener ans Textfeld hängen (Reaktion auf ENTER-Taste)
		addActionListener(this);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// Bei Drücken der ENTER-Taste springt der Fokus weiter
		transferFocus();
		
	}

}
