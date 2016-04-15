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


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;


/**
 * Schritt 3 des Dialogs zur Zuschussberechnung.
 */
@SuppressWarnings("serial")
class PanelStep3
        extends GenericPanelStep {

    private JTextField tfStichtag, tfFonds, tfTicketpreis, tfSozialfonds, tfAnzahlAntraege, tfAnzahlPunkte, tfVollzuschuss, tfPunktwert;

    private JButton calcButton;


    /**
     * {see GenericPanelStep}
     *
     * @param bModel     ModelAuszahlungsmodul
     * @param titel      titel
     * @param untertitel untertitel
     */
    protected PanelStep3(ModelAuszahlungsmodul bModel, String titel, String untertitel) {
        super(bModel, titel, untertitel);
    }


    @Override
    protected void additionalInitStuff() {

        JLabel labelTitel2 = new JLabel("Werte");
        labelTitel2.setFont(labelTitel2.getFont().deriveFont(Font.BOLD));

        JLabel labelText2 = new JLabel("Stichtag:");
        JLabel labelText3 = new JLabel("zur Verfügung stehender Fonds:");
        JLabel lbTicketpreis = new JLabel("Ticketpreis:");
        JLabel lbSozialfonds = new JLabel("Sozialfonds:");
        JLabel labelText4 = new JLabel("Gesamtzahl bewilligter Anträge:");
        JLabel labelText5 = new JLabel("Gesamtzahl vergebener Punkte");

        JLabel lbVollzuschuss = new JLabel("Vollzuschuss ab:");
        JLabel lbPunktwert = new JLabel("Punktwert:");

        tfStichtag = new JTextField(10);
        tfFonds = new JTextField(10);
        tfTicketpreis = new JTextField(10);
        tfSozialfonds = new JTextField(10);
        tfAnzahlAntraege = new JTextField(10);
        tfAnzahlPunkte = new JTextField(10);
        tfVollzuschuss = new JTextField(12);
        tfPunktwert = new JTextField(12);

        tfStichtag.setFocusable(false);
        tfFonds.setFocusable(false);
        tfTicketpreis.setFocusable(false);
        tfSozialfonds.setFocusable(false);
        tfAnzahlAntraege.setFocusable(false);
        tfAnzahlPunkte.setFocusable(false);
        tfVollzuschuss.setFocusable(false);
        tfPunktwert.setFocusable(false);

        calcButton = new JButton("Zuschüsse Berechnen");

        calcButton.addActionListener(new CalcL());

        formular.add(labelText2, 0, 4, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
        formular.add(labelText3, 0, 5, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
        formular.add(lbTicketpreis, 0, 6, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
        formular.add(lbSozialfonds, 0, 7, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
        formular.add(labelText4, 0, 8, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
        formular.add(labelText5, 0, 9, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
        formular.add(lbVollzuschuss, 0, 10, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
        formular.add(lbPunktwert, 0, 11, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));

        formular.add(tfStichtag, 1, 4, 1, 1, 0.0, 0.0, 2, 18, new Insets(0, 0, 0, 0));
        formular.add(tfFonds, 1, 5, 1, 1, 0.0, 0.0, 2, 18, new Insets(0, 0, 0, 0));
        formular.add(tfTicketpreis, 1, 6, 1, 1, 0.0, 0.0, 2, 18, new Insets(0, 0, 0, 0));
        formular.add(tfSozialfonds, 1, 7, 1, 1, 0.0, 0.0, 2, 18, new Insets(0, 0, 0, 0));
        formular.add(tfAnzahlAntraege, 1, 8, 1, 1, 0.0, 0.0, 2, 18, new Insets(0, 0, 0, 0));
        formular.add(tfAnzahlPunkte, 1, 9, 1, 1, 0.0, 0.0, 2, 18, new Insets(0, 0, 0, 0));
        formular.add(tfVollzuschuss, 1, 10, 1, 1, 0.0, 0.0, 2, 18, new Insets(0, 0, 0, 0));
        formular.add(tfPunktwert, 1, 11, 1, 1, 0.0, 0.0, 2, 18, new Insets(0, 0, 0, 0));

        formular.add(calcButton, 1, 12, 1, 1, 0.0, 0.0, 2, 18, new Insets(0, 0, 0, 0));
        formular.add(new JPanel(), 0, 13, 2, 1, 1.0, 1.0, 2, 18, new Insets(0, 0, 0, 0));

    }


    /**
     * Update-Methode. Füllt u.a. dieses Panel mit den Werten aus dem Model {see ModelAuszahlungsmodul}
     */
    @Override
    protected void updatePanel() {

        GregorianCalendar datum = bModel.getStichtag();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String stichtag = df.format(datum.getTime());

        tfStichtag.setText(stichtag);
        tfFonds.setText(bModel.getFonds());
        tfTicketpreis.setText(bModel.getTicketpreis());
        tfSozialfonds.setText(bModel.getSozialfonds());
		tfAnzahlAntraege.setText(Integer.toString(bModel.getAntragListeGenehmigtFiltered().size()));
		tfPunktwert.setText(bModel.getPunktwert());
		tfVollzuschuss.setText(Integer.toString(bModel.getPunkteVollzuschuss()));
		tfAnzahlPunkte.setText(Integer.toString(bModel.getSummePunkte()));

        this.logTextArea.refresh();
    }

    /*
        Berechnung und füllen der leeren Eingabefelder
    */
    private class CalcL implements ActionListener {


        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            bModel.berechnungPunktwert();
            logTextArea.logText("(3) Berechnung abgeschlossen");
            bModel.updateSemester();
            updatePanel();
        }
    }
}
