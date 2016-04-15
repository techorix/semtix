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

public class UniqueUpperCaseFilter
        extends DocumentSizeFilter {

    public UniqueUpperCaseFilter(int maxChars) {
        maxCharacters = maxChars;
        pattern = DocumentSizeFilter.TEXT_PATTERN;
    }


    @Override
    public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a)
            throws BadLocationException {

        String uStr = str.toUpperCase();

        //nur wenn er nicht schon vorkommt
        if (!fb.getDocument().getText(0, fb.getDocument().getLength()).contains(uStr)) {
            //geben wir den Eingabestring weiter
            super.replace(fb, offs, length, uStr, a);
        }

    }

}
