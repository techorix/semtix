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

package org.semtix.config;

/**
 *
 * Singleton Class guarantees that only one instance of itself does exist. This may be necessary in order to manage hardware resources but also other settings.
 *
 * Wenn in einer Multithreaded Umgebung gearbeitet wird, wird es ggf. nicht immer sinnvoll sein, mit sovielen statischen Methoden und Feldern zu arbeiten.
 *
 * Auch wenn wir die Application webbasiert exposen könnte es sein, dass wir von mehreren Instanzen zugriff auf nur eine Einstellungs-Distanz wollen. Das gilt vermutlich aber nur für GlobalSettings die für alle Instanzen gleich sind.
 *
 * In diesen Feldern müssen wir Singleton-Pattern benutzen. Dieses ist hier so kostengünstig wie möglich implementiert und benutzt die kostenintensive synchronized funktion nur genau einmal. Nämlich zur Erstellung des Objektes
 *
 *
 * @author Michael Mertins
 * Created by MM on 21.04.15.
 */
public class Singleton {

    //nur Java 1.4+
    private volatile static Singleton uniqueInstance;

    private Singleton(){

    }


    public Singleton getInstance() {
        if (null == uniqueInstance) {
            synchronized (Singleton.class) {
                if (null == uniqueInstance) {
                    uniqueInstance = new Singleton();
                }
            }
        }

        return uniqueInstance;
    }

}
