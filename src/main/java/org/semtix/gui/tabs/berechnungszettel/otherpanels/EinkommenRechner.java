/*

  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
         Semesterticketbüro der Humboldt-Universität Berlin

  Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package org.semtix.gui.tabs.berechnungszettel.otherpanels;

import org.semtix.db.dao.LabeledDecimalList;
import org.semtix.gui.tabs.berechnungszettel.BerechnungControl;
import org.semtix.shared.elements.FormularTabReihenfolge;
import org.semtix.shared.elements.FormularTextfeld;
import org.semtix.shared.elements.NewCurrencyField;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Komponente zur Berechnung von Einkommen auf Seite 2 des Berechnungszettel. Die
 * Einkommenrechner sind zunäcst eingeklappt und können durch Eingabe eines Titels
 * ausgeklappt und mit Beträgen gefüllt werden.
 */
@SuppressWarnings("serial")
public class EinkommenRechner extends JPanel {

    private final BerechnungControl berechnungControl;
	private final int ROWCOUNTBORDER = 10;

	private int index, divider;

    private JLabel labelGesamtsummeText;

    private NewCurrencyField labelSumme, labelGesamtsumme;

    private FormularTextfeld tfTitel;

    private JSpinner spinner;

    private List<NewCurrencyField> cfList;


    /**
     * Erstellt einen neuen Einkommenrechner
     *
     * @param index             Index des Einkommenrechners
     * @param berechnungControl berechnungControl
     */
    public EinkommenRechner(final int index, final BerechnungControl berechnungControl) {

        this.index = index;
        this.berechnungControl = berechnungControl;

        divider = 6;

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(220, 370));

        labelGesamtsummeText = new JLabel("/");

        labelSumme = new NewCurrencyField(10);
        labelSumme.setBackground(Color.PINK);
        labelSumme.setOpaque(true);
        labelSumme.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));


        labelGesamtsumme = new NewCurrencyField(10);
        labelGesamtsumme.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        tfTitel = new FormularTextfeld("", 1, 210, 24, JTextField.LEFT);
        tfTitel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (tfTitel.getText().isEmpty())
                    setPanelVisible(false);
                else {
                    setPanelVisible(true);
                }

            }
        });


        tfTitel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                berechnungControl.getBerechnungModel().setEinkommenName(index, tfTitel.getText());
            }
        });

        spinner = new JSpinner(new SpinnerNumberModel(divider, 1, 12, 1));
        JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) spinner.getEditor();
        spinnerEditor.getTextField().setEditable(false);

        spinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                divider = (Integer) spinner.getModel().getValue();
                updateSumme();
            }
        });

        cfList = new ArrayList<NewCurrencyField>();
        List<JComponent> compListe = new ArrayList<JComponent>();
        compListe.add(tfTitel);

		for (int i = 0; i < ROWCOUNTBORDER; i++) {
			final NewCurrencyField cf = new NewCurrencyField(10);
            cf.addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    updateSumme();
                }
            });
            cfList.add(cf);
            compListe.add(cf);
        }


        SForm form = new SForm();
        form.setBackground(Color.WHITE);

        form.add(tfTitel, 0, 0, 3, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 10, 0));

        for (NewCurrencyField cf : cfList) {
            form.add(cf, 2, 2 + cfList.indexOf(cf), 1, 1, 0.0, 0.0, 1, 18, new Insets(0, 0, 0, 0));
        }

        form.add(labelGesamtsummeText, 0, 15, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 5, 0, 0));
        form.add(spinner, 1, 15, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 5, 0, 5));
        form.add(labelSumme, 2, 14, 1, 1, 0.0, 0.0, 2, 18, new Insets(10, 0, 10, 0));
        form.add(labelGesamtsumme, 2, 15, 1, 1, 1.0, 1.0, 2, 18, new Insets(2, 2, 2, 2));

        setPanelVisible(false);

        this.add(form);

        // ***** Tab-Reihenfolge setzen *****
        JComponent[] order = new JComponent[compListe.size()];
        compListe.toArray(order);
        setFocusTraversalPolicy(new FormularTabReihenfolge(order));
        setFocusCycleRoot(true);

    }


    /**
     * Ausklappen des Panels
     *
     * @param flag Ausklappen? ja/nein
     */
    public void setPanelVisible(Boolean flag) {

        labelGesamtsummeText.setVisible(flag);
        labelSumme.setVisible(flag);
        labelGesamtsumme.setVisible(flag);
        spinner.setVisible(flag);

        for (NewCurrencyField cf : cfList) {
            cf.setVisible(flag);
        }

    }


    /**
     * Addiert die Summe der Betragsfelder und aktualisiert das Summenfeld
     */
    private void updateSumme() {

        BigDecimal summe = BigDecimal.ZERO;

        List<BigDecimal> singleValues = new ArrayList<BigDecimal>();
        for (NewCurrencyField cf : cfList) {

            summe = summe.add(cf.getBValue());
            singleValues.add(cf.getBValue());

        }

        berechnungControl.getBerechnungModel().getEinkommensfeld(index).setValueList(singleValues);
        berechnungControl.getBerechnungModel().getEinkommensfeld(index).setDivisor(divider);


        labelSumme.setBValue(summe);

        BigDecimal summeDivided = summe.divide(new BigDecimal(divider), 2, BigDecimal.ROUND_HALF_UP);

		labelGesamtsumme.setBValue(summeDivided);

        berechnungControl.getBerechnungModel().setEinkommen(index, summeDivided);

    }

    public void initValues() {

		LabeledDecimalList ldl = berechnungControl.getBerechnungModel().getEinkommensfeld(index);

		tfTitel.setText("");

		for (int i = 0; i < ROWCOUNTBORDER; i++) {
			cfList.get(i).setBValue(BigDecimal.ZERO);
		}

		divider = 6;

		if (null != ldl && null != ldl.getValueList()) {

			tfTitel.setText(ldl.getLabel());

			int i = 0;
			for (BigDecimal value : ldl.getValueList()) {
				cfList.get(i).setBValue(value);
				i++;
			}
			divider = ldl.getDivisor();

			updateSumme();
		}

		spinner.getModel().setValue(divider);
		setPanelVisible(tfTitel.getText().trim().length() > 0);
	}

}
