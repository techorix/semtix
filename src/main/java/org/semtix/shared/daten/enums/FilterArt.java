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

package org.semtix.shared.daten.enums;

/**
 * Konstanten zur Filterung der Anträge nach Filterart
 */
public enum FilterArt {

    ERSTRECHNEN("Erstrechnen oder Nachfragen"),
    MAHNEN("Erneut Nachfragen oder Mahnen"),
    ZWEITRECHNEN("Zweitrechnen"),
    //  KULANZRECHNEN("Kulanz rechnen"),
    UNVOLLSTAENDIG("unvollständig rechnen"),
    ABGELEHNT("abgelehnte Anträge"),
    ANGENOMMEN("angenommene Anträge"),
    UNENTSCHIEDEN("nicht entschiedene Anträge"),
    NUR_SONSTIGE("nur nach den sonstigen ");


    String status;


    FilterArt(String status) {
        this.status = status;
    }


    public String toString() {
		return status;
	}

}
