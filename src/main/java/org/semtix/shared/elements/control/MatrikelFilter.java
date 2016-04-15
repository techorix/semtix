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

package org.semtix.shared.elements.control;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

/**
 * DocumentFilter überprüft in Textfeld eingegebenen Wert auf maximale
 * Zeichenlänge und Muster (hier Matrikelnummer, die nur aus Ziffern besteht,
 * nicht mit Null anfangen darf und maximal 7 Ziffern haben darf).
 *
 */
public class MatrikelFilter
		extends DocumentSizeFilter {


	/**
	 * Erstellt einen neuen MatrikelFilter
	 * @param maxChars maximal zulässige Zeichenlänge
	 */
	public MatrikelFilter(final int maxChars) {
		
		maxCharacters = maxChars;
		pattern = DIGIT_PATTERN;
		
	}

	
	@Override
    public void replace(FilterBypass fb, int offs, int length, String text, AttributeSet a)
            throws BadLocationException {

		// die erste Ziffer darf nicht Null sein
		if (offs == 0 && !text.matches("[1-9]\\d*")) {
			//do nothing
		} else {
			super.replace(fb, offs, length, text, a);
		}
        
    }

}
