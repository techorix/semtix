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

package org.semtix.gui.statistik;

import org.semtix.db.DBHandlerAntrag;
import org.semtix.db.DBHandlerVorgaenge;
import org.semtix.db.dao.Antrag;
import org.semtix.db.dao.Vorgang;
import org.semtix.shared.daten.DeutschesDatum;
import org.semtix.shared.daten.enums.Uni;
import org.semtix.shared.daten.enums.Vorgangsart;

import java.util.*;

/**
 * Wertet alle Vorgänge aus und erlaubt es, daraus eine CSV/Excel-Datei zu bauen o.ä.
 * <br>
 * Created by MM on 19.03.15.
 */
public class ArbeitNachDatum {

    private Uni uni;


    public ArbeitNachDatum(Uni uni) {

        this.uni = uni;
    }


    /**
     * @return Hashmap mit Vorgängentypenzahlen nach Datum
     */
    public HashMap<GregorianCalendar, int[]> getDatumVorgaenge() {

        HashMap<GregorianCalendar, List<Vorgang>> vorgangHashMap = new HashMap<GregorianCalendar, List<Vorgang>>();

        for (Vorgang v : new DBHandlerVorgaenge().getAllVorgaenge(uni)) {

            GregorianCalendar timeStamp = DeutschesDatum.reduceGregorianCalendarToDay(v.getZeitstempel());

            addOrCreateList(vorgangHashMap, timeStamp, v);

        }

        //Man könnte addOrCreateList() und countVorgaengeByType eigentlich auch zusammenlegen, aber okay. Divide and Conquer...

        HashMap<GregorianCalendar, int[]> vorgangtypenzahlenHashMap = new HashMap<GregorianCalendar, int[]>();

        for (Map.Entry<GregorianCalendar, List<Vorgang>> entry : vorgangHashMap.entrySet()) {
            vorgangtypenzahlenHashMap.put(entry.getKey(), countVorgaengeByType(entry.getValue()));
        }

        return vorgangtypenzahlenHashMap;
    }


    /**
     * @return HashMap mit Anzahl Anträgen nach Datum des Anlegens
     */
    public HashMap<GregorianCalendar, Integer> getDatumAntragAngelegt() {

        HashMap<GregorianCalendar, Integer> antragHashMap = new HashMap<GregorianCalendar, Integer>();

        for (Antrag a : new DBHandlerAntrag().getAntragListeUni(uni)) {

            GregorianCalendar timeStamp = DeutschesDatum.reduceGregorianCalendarToDay(a.getDatumAngelegt());

            addOrCreate(antragHashMap, timeStamp);

        }

        return antragHashMap;
    }


    /**
     * @return HashMap mit Anzahl Anträgen nach Datum der letzten Änderung
     */
    public HashMap<GregorianCalendar, Integer> getDatumAntragGeaendert() {

        HashMap<GregorianCalendar, Integer> antragHashMap = new HashMap<GregorianCalendar, Integer>();

        for (Antrag a : new DBHandlerAntrag().getAntragListeUni(uni)) {

            GregorianCalendar timeStamp = DeutschesDatum.reduceGregorianCalendarToDay(a.getDatumGeaendert());

            addOrCreate(antragHashMap, timeStamp);

        }

        return antragHashMap;
    }

    //kleine generische Methode
    private <T> void addOrCreateList(HashMap<GregorianCalendar, List<T>> hashMap, GregorianCalendar timeStamp, T t) {
        List<T> temp;
        if (hashMap.containsKey(timeStamp)) {
            temp = hashMap.get(timeStamp);
        } else {
            temp = new ArrayList<T>();
        }

        temp.add(t);

        hashMap.put(timeStamp, temp);

    }

    //kleine HilfsMethode
    private void addOrCreate(HashMap<GregorianCalendar, Integer> hashMap, GregorianCalendar timeStamp) {
        Integer temp = 1;
        if (hashMap.containsKey(timeStamp)) {
            temp = hashMap.get(timeStamp);
            temp++;
        }

        hashMap.put(timeStamp, temp);
    }




    /**
     * N-Zeit Methode zum Bestimmen Zählen der Vorgänge nach Vorgangsarten
     *
     * @param vorgaenge
     * @return array of Vorgaenge by Type where i in int[i] is id of Type -1
     */
    private int[] countVorgaengeByType(List<Vorgang> vorgaenge) {
        int[] types = new int[Vorgangsart.values().length];

        for (Vorgang v : vorgaenge) {
            types[v.getVorgangsart().getIndex() - 1]++;
        }

        return types;

    }

}
