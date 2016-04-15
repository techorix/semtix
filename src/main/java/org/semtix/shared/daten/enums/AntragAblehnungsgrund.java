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
 * Gründe für die Ablehnung eines Antrags.
 *
 * Created by MM on 11.05.15.
 */
public enum AntragAblehnungsgrund {

    ABLEHNUNGSGRUND(0, "<ABLEHNUNGSGRUND>", ""),
    UNSCHLUESSIG(1, "Unschlüssig", "Da du auf unsere Nachfragebriefe nicht reagiert hast und deine Einnahmen die Kosten für Miete und/oder Pflege-/ Krankenversicherung nicht decken, mussten wir deinen Antrag leider wegen Unschlüssigkeit ablehnen. "),
    ZUREICH(2, "Einkommen zu hoch", "Das Einkommen überschreitet den Bedarf im Sinne von §2 Absatz 3 und 4 der Sozialfondssatzung. Deshalb kann dem Antrag trotz eventuell anerkannter Härten (s.u.) nicht stattgegeben werden."),
    ZURUECKGEZOGEN(3, "Antrag zurückgezogen", "Da du deinen Antrag zurückgezogen hast, haben wir diesen deinem Wunsch entsprechend abgelehnt."),
    NICHTNACHGEWIESEN(4, "Nichts nachgewiesen", "Da du auf unsere Nachfragebriefe nicht reagiert hast und dein Antrag unvollständig ist, mussten wir deinen Antrag leider ablehnen."),
    EXMATRIKULIERT(5, "Nicht immatrikuliert", "Der Datenabgleich mit dem Immatrikulationsbüro hat ergeben, dass Du für das Antragssemester nicht eingeschrieben bist. Deshalb muss dein Antrag leider abgelehnt werden.");


    private String name;
    private String begruendung;
    private int id;


    AntragAblehnungsgrund(int id, String name, String begruendung) {
        this.id = id;
        this.name = name;
        this.begruendung = begruendung;
    }

    public String toString() {
        return name;
    }

    public String getBegruendung() {
        return begruendung;
    }

}
