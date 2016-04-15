/*
 *
 *  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 *         Semesterticketbüro der Humboldt-Universität Berlin
 *
 *  Copyright (c) 2015-2016 Michael Mertins (MichaelMertins@gmail.com)
 *  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)
 *
 *    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.semtix.shared.tablemodels;

import org.semtix.shared.daten.StringHelper;

import javax.swing.*;

/**
 * Filtert eine Tabellenzeile und ignoriert dabei sowohl Case als auch Sonderzeichen
 * <br>
 * Created by MM on 19.04.15.
 */
public class MyRowFilter extends RowFilter {

    private String match;
    private int index;

    public MyRowFilter(String match, int index) {
        this.match = match;
        this.index = index;
    }

    @Override
    public boolean include(Entry entry) {
        String entryString = entry.getStringValue(index).toLowerCase();
		String alternativString = StringHelper.removeDiacriticalMarks(entryString);
		return (entryString.contains(match.toLowerCase()) || alternativString.contains(match.toLowerCase()));

    }
}
