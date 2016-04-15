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

package org.semtix.gui.auszahlung.auszahlungsmodul;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.semtix.config.SettingsExternal;
import org.semtix.db.dao.Antrag;
import org.semtix.db.dao.Semester;
import org.semtix.shared.daten.DeutschesDatum;
import org.semtix.shared.daten.enums.AntragStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ModelAuszahlungsModulTest {

    private ModelAuszahlungsmodul modelAuszahlungsmodul;


    @Before
    public void setUp() {

        //für Nachvollziehen von Fehlern:
        SettingsExternal.DEBUG = false;

        modelAuszahlungsmodul = new ModelAuszahlungsmodul();
        Semester semester = new Semester();
        semester.setBeitragFonds(new BigDecimal(1000));
        semester.setBeitragTicket(new BigDecimal(100));
        semester.setSozialfonds(new BigDecimal(10));
        modelAuszahlungsmodul.setSemester(semester);
    }

    @Test
    public void testBerechnungAlleVollzuschuss() {

        List<Antrag> antragList = new ArrayList<Antrag>();

        //TODO Anträge mit Teilzuschuss

        Antrag a = new Antrag();
        a.setPunkteHaerte(10);
        a.setPunkteEinkommen(10);
        a.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(a);

        Antrag b = new Antrag();
        b.setPunkteHaerte(10);
        b.setPunkteEinkommen(10);
        b.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(b);

        Antrag c = new Antrag();
        c.setPunkteHaerte(10);
        c.setPunkteEinkommen(10);
        c.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(c);

        Antrag d = new Antrag();
        d.setPunkteHaerte(10);
        d.setPunkteEinkommen(10);
        d.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(d);

        Antrag e = new Antrag();
        e.setPunkteHaerte(10);
        e.setPunkteEinkommen(10);
        e.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(e);

        Antrag f = new Antrag();
        f.setPunkteHaerte(10);
        f.setPunkteEinkommen(10);
        f.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(f);

        Antrag g = new Antrag();
        g.setPunkteHaerte(10);
        g.setPunkteEinkommen(10);
        g.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(g);

        Antrag h = new Antrag();
        h.setPunkteHaerte(10);
        h.setPunkteEinkommen(10);
        h.setAntragStatus(AntragStatus.NICHTENTSCHIEDEN);
        antragList.add(h);

        Antrag i = new Antrag();
        i.setPunkteHaerte(10);
        i.setPunkteEinkommen(10);
        i.setAntragStatus(AntragStatus.ABGELEHNT);
        antragList.add(i);

        Antrag j = new Antrag();
        j.setPunkteHaerte(10);
        j.setPunkteEinkommen(10);
        j.setAntragStatus(AntragStatus.ABGELEHNT);
        antragList.add(j);

		modelAuszahlungsmodul.setAntragListeForTest(antragList);
		modelAuszahlungsmodul.berechnungPunktwert();

        Assert.assertTrue(modelAuszahlungsmodul.getPunkteVollzuschuss() == 1);
        Assert.assertTrue(modelAuszahlungsmodul.getPunktwert().equals("Alle Vollzuschuss"));
        Assert.assertTrue(modelAuszahlungsmodul.getSummePunkte() == 140);

    }

    @Test
    public void testBerechnungPunktwertAlleGleich() {

        List<Antrag> antragList = new ArrayList<Antrag>();

        Antrag a = new Antrag();
        a.setPunkteHaerte(10);
        a.setPunkteEinkommen(10);
        a.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(a);

        Antrag b = new Antrag();
        b.setPunkteHaerte(10);
        b.setPunkteEinkommen(10);
        b.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(b);

        Antrag c = new Antrag();
        c.setPunkteHaerte(10);
        c.setPunkteEinkommen(10);
        c.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(c);

        Antrag d = new Antrag();
        d.setPunkteHaerte(10);
        d.setPunkteEinkommen(10);
        d.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(d);

        Antrag e = new Antrag();
        e.setPunkteHaerte(10);
        e.setPunkteEinkommen(10);
        e.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(e);

        Antrag f = new Antrag();
        f.setPunkteHaerte(10);
        f.setPunkteEinkommen(10);
        f.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(f);

        Antrag g = new Antrag();
        g.setPunkteHaerte(10);
        g.setPunkteEinkommen(10);
        g.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(g);

        Antrag h = new Antrag();
        h.setPunkteHaerte(10);
        h.setPunkteEinkommen(10);
        h.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(h);

        Antrag i = new Antrag();
        i.setPunkteHaerte(10);
        i.setPunkteEinkommen(10);
        i.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(i);

        Antrag j = new Antrag();
        j.setPunkteHaerte(10);
        j.setPunkteEinkommen(10);
        j.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(j);

		modelAuszahlungsmodul.setAntragListeForTest(antragList);

        modelAuszahlungsmodul.getSemester().setAntraegeBewilligt(10);

        modelAuszahlungsmodul.berechnungPunktwert();

        System.out.println("ERGEBNIS: " + modelAuszahlungsmodul.getPunkteVollzuschuss() + " " + modelAuszahlungsmodul.getPunktwert() + " " + modelAuszahlungsmodul.getSummePunkte());
        Assert.assertTrue(modelAuszahlungsmodul.getPunkteVollzuschuss() == 0);
        Assert.assertTrue(modelAuszahlungsmodul.getPunktwert().equals(DeutschesDatum.getEuroFormatted(new BigDecimal(5))));
        Assert.assertTrue(modelAuszahlungsmodul.getSummePunkte() == 200);

    }

    @Test
    public void testBerechnungPunktwert() {

        List<Antrag> antragList = new ArrayList<Antrag>();

        Antrag a = new Antrag();
        a.setPunkteHaerte(1);
        a.setPunkteEinkommen(1);
        a.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(a);

        Antrag b = new Antrag();
        b.setPunkteHaerte(2);
        b.setPunkteEinkommen(2);
        b.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(b);

        Antrag c = new Antrag();
        c.setPunkteHaerte(3);
        c.setPunkteEinkommen(3);
        c.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(c);

        Antrag d = new Antrag();
        d.setPunkteHaerte(4);
        d.setPunkteEinkommen(4);
        d.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(d);

        Antrag e = new Antrag();
        e.setPunkteHaerte(5);
        e.setPunkteEinkommen(5);
        e.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(e);

        Antrag f = new Antrag();
        f.setPunkteHaerte(10);
        f.setPunkteEinkommen(10);
        f.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(f);

        Antrag g = new Antrag();
        g.setPunkteHaerte(10);
        g.setPunkteEinkommen(10);
        g.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(g);

        Antrag h = new Antrag();
        h.setPunkteHaerte(20);
        h.setPunkteEinkommen(20);
        h.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(h);

        Antrag i = new Antrag();
        i.setPunkteHaerte(30);
        i.setPunkteEinkommen(30);
        i.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(i);

        Antrag j = new Antrag();
        j.setPunkteHaerte(40);
        j.setPunkteEinkommen(40);
        j.setAntragStatus(AntragStatus.GENEHMIGT);
        antragList.add(j);

		modelAuszahlungsmodul.setAntragListeForTest(antragList);

        modelAuszahlungsmodul.getSemester().setAntraegeBewilligt(10);

        modelAuszahlungsmodul.berechnungPunktwert();

        System.out.println("ERGEBNIS: " + modelAuszahlungsmodul.getPunkteVollzuschuss() + " " + modelAuszahlungsmodul.getPunktwert() + " " + modelAuszahlungsmodul.getSummePunkte());
        Assert.assertTrue(modelAuszahlungsmodul.getPunkteVollzuschuss() == 6);
        Assert.assertTrue(modelAuszahlungsmodul.getPunktwert().equals(DeutschesDatum.getEuroFormatted(new BigDecimal(20))));
        Assert.assertTrue(modelAuszahlungsmodul.getSummePunkte() == 250);

    }


}