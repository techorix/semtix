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
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * Schritt 4 des Dialogs zur Zuschussberechnung
 */
@SuppressWarnings("serial")
class PanelStep4
        extends GenericPanelStep {

    private JTextField tfSemester, tfFonds, tfStichtag, tfVollzuschuss, tfPunktwert;

    private JPanel buttonPanel;

    private JButton printEntschieden;

    /**
     * {see GenericPanelStep}
     *
     * @param bModel     ModelAuszahlungsmodul
     * @param titel      titel
     * @param untertitel untertitel
     */
    protected PanelStep4(ModelAuszahlungsmodul bModel, String titel, String untertitel) {
        super(bModel, titel, untertitel);
    }

    @Override
    protected void additionalInitStuff() {
        buttonPanel = new JPanel();

        JLabel lbSemester = new JLabel("Semester:");
        JLabel lbFonds = new JLabel("Fonds:");
        JLabel lbStichtag = new JLabel("Stichtag:");
        JLabel lbVollzuschuss = new JLabel("Vollzuschuss ab:");
        JLabel lbPunktwert = new JLabel("Punktwert:");

        tfSemester = new JTextField(12);
        tfFonds = new JTextField(12);
        tfStichtag = new JTextField(12);
        tfVollzuschuss = new JTextField(12);
        tfPunktwert = new JTextField(12);

        tfSemester.setFocusable(false);
        tfFonds.setFocusable(false);
        tfStichtag.setFocusable(false);
        tfVollzuschuss.setFocusable(false);
        tfPunktwert.setFocusable(false);

        JLabel drucken = new JLabel("Drucken:");
        printEntschieden = new JButton("Entschiedene Anträge");


        buttonPanel.add(drucken);
        buttonPanel.add(printEntschieden);


        formular.add(lbSemester, 0, 3, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
        formular.add(lbFonds, 0, 4, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
        formular.add(lbStichtag, 0, 5, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
        formular.add(lbVollzuschuss, 0, 6, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
        formular.add(lbPunktwert, 0, 7, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));

        formular.add(tfSemester, 1, 3, 1, 1, 0.0, 0.0, 2, 18, new Insets(0, 0, 0, 0));
        formular.add(tfFonds, 1, 4, 1, 1, 0.0, 0.0, 2, 18, new Insets(0, 0, 0, 0));
        formular.add(tfStichtag, 1, 5, 1, 1, 0.0, 0.0, 2, 18, new Insets(0, 0, 0, 0));
        formular.add(tfVollzuschuss, 1, 6, 1, 1, 0.0, 0.0, 2, 18, new Insets(0, 0, 0, 0));
        formular.add(tfPunktwert, 1, 7, 1, 1, 0.0, 0.0, 2, 18, new Insets(0, 0, 0, 0));

        formular.add(drucken, 0, 8, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
        formular.add(printEntschieden, 1, 8, 1, 1, 0.0, 0.0, 2, 18, new Insets(0, 0, 0, 0));

        formular.add(new JPanel(), 0, 9, 2, 1, 1.0, 1.0, 2, 18, new Insets(0, 0, 0, 0));
    }


    // Daten aus Model in Panel einfügen
    public void updatePanel() {
		for (ActionListener act : printEntschieden.getActionListeners()) {
			printEntschieden.removeActionListener(act);
        }

		printEntschieden.setText("Entschiedene Anträge drucken");
		printEntschieden.addActionListener(new ActionPrintAntragList(bModel.getAntragListeEntschiedenFiltered(), this,
				"(4) Entschiedene Bescheide gedruckt", true));

        GregorianCalendar datum = bModel.getStichtag();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String stichtag = df.format(datum.getTime());

        tfSemester.setText(bModel.getSemester().getSemesterKurzform());
        tfFonds.setText(bModel.getFonds());
        tfStichtag.setText(stichtag);
		tfVollzuschuss.setText(Integer.toString(bModel.getPunkteVollzuschuss()));
		tfPunktwert.setText(bModel.getPunktwert());

        this.logTextArea.refresh();
    }

}
