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

package org.semtix.shared.tablemodels;

import org.semtix.config.SettingsExternal;
import org.semtix.config.UniConf;
import org.semtix.gui.statistik.ArbeitNachDatum;
import org.semtix.shared.daten.DeutschesDatum;
import org.semtix.shared.daten.enums.Vorgangsart;

import javax.swing.table.AbstractTableModel;
import java.util.*;

/**
 * @author Michael Mertins
 *         Created by MM on 05.05.15.
 */
public final class TableModelStatistikArbeit extends AbstractTableModel {

    ArbeitNachDatum statistiken;
    HashMap<GregorianCalendar, int[]> vorgaenge;
    HashMap<GregorianCalendar, Integer> angelegteAntraege;
    HashMap<GregorianCalendar, Integer> geaenderteAntraege;


    private Date datumBis;
    private Date datumVon;

    private String[] columnNames;

    private List<GregorianCalendar> eintraege = new ArrayList<GregorianCalendar>();

    public TableModelStatistikArbeit() {
        super();

        init();
    }

    private void init() {
        List<String> headers = new ArrayList<String>();
        headers.add("Datum");
        headers.add("Anträge angelegt");
        headers.add("Anträge geändert");

        for (Vorgangsart v : Vorgangsart.values()) {
            String vname = v.toString();
            int length = vname.length();
            vname = "<html>" + vname + "</html>";
            if (length > 19)
                vname = vname.replace(" ", "<br>");
            headers.add(vname);
        }

        columnNames = headers.toArray(new String[]{});

        datumBis = new Date(); //heute
        datumVon = new Date(datumBis.getTime() - 1209600000); // - 1 Woche

        statistiken = new ArbeitNachDatum(UniConf.aktuelleUni);
        vorgaenge = statistiken.getDatumVorgaenge();
        angelegteAntraege = statistiken.getDatumAntragAngelegt();
        geaenderteAntraege = statistiken.getDatumAntragGeaendert();

        generateEintraege();
    }

    //füllt liste von Tagen
    public void generateEintraege() {

        eintraege.clear();

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(datumVon);
        calendar = DeutschesDatum.reduceGregorianCalendarToDay(calendar);

        while (calendar.getTime().compareTo(datumBis) <= 0) {


            eintraege.add((GregorianCalendar) calendar.clone());

            if (SettingsExternal.DEBUG)
                System.out.println(DeutschesDatum.DATUMSFORMAT.format(calendar.getTime()) + " geänderte # " + geaenderteAntraege.get(calendar) + " angelegte # " + angelegteAntraege.get(calendar));

            //einen Tag addieren bis datumBis erreicht
            calendar.set(GregorianCalendar.DAY_OF_MONTH, calendar.get(GregorianCalendar.DAY_OF_MONTH) + 1);
        }

        fireTableDataChanged();
    }


    @Override
    public int getRowCount() {
        return eintraege.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        GregorianCalendar datum = eintraege.get(rowIndex);
        int[] vorgangZahlIndex = vorgaenge.get(datum);

        Object value = null;
        if (columnIndex == 0) {
            value = DeutschesDatum.DATUMSFORMAT.format(datum.getTime());
        } else if (columnIndex == 1) {
            value = angelegteAntraege.get(datum);
        } else if (columnIndex == 2) {
            value = geaenderteAntraege.get(datum);
        } else if (columnIndex > 2) {
            if (null != vorgangZahlIndex)
                value = vorgangZahlIndex[columnIndex - 3];
        }


        // Vorgänge auch noch...

        if (null == value)
            return "0";
        else
            return value;
    }

    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }


    public void setDatumVon(Date datumVon) {
        this.datumVon = datumVon;
        generateEintraege();
    }

    public void setDatumBis(Date datumBis) {
        this.datumBis = datumBis;
        generateEintraege();
    }
}
