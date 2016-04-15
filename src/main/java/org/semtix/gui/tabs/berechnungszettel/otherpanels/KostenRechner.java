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

package org.semtix.gui.tabs.berechnungszettel.otherpanels;

import org.semtix.db.dao.LabeledDecimalList;
import org.semtix.gui.tabs.berechnungszettel.BerechnungControl;
import org.semtix.shared.daten.enums.BerechnungsZettelCFTypen;
import org.semtix.shared.elements.FormularTabReihenfolge;
import org.semtix.shared.elements.NewCurrencyField;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Komponente zur Berechnung von Kosten auf Seite 2 des Berechnungszettel. Die
 * Kostenrechner sind zunäcst eingeklappt und können durch Auswahl einer Kostenart
 * ausgeklappt und mit Beträgen gefüllt werden.
 */
@SuppressWarnings("serial")
public class KostenRechner
        extends JPanel {

    private final static String[] kostenArten = {" - Kostenart auswählen - ",
            "Unterhalt/Unterstützung",
            "Med./Psych. Kosten",
            "besondere Ernährung",
            "zusätzliche Kosten",
            "Schulden",
            "Krankenkasse",
            "Miete",
            "Nebenrechnung"};
    private int index;
    private BerechnungControl berechnungControl;
    private int divider;
    private JLabel labelTitel, labelBetraege, labelSummeText, labelGesamtsummeText;
    private NewCurrencyField labelSumme, labelGesamtsumme;
    private JSpinner spinner;
    private JComboBox comboKosten;
    private List<NewCurrencyField> cfList;
    private BerechnungsZettelCFTypen type;
    private BerechnungsZettelCFTypen typeForKind;

    /**
     * Erstellt einen neuen Kostenrechner
     *
     * @param index             Index des Kostenrechners
     * @param berechnungControl BerechnungControl
     */
    public KostenRechner(int index, BerechnungControl berechnungControl) {

        this.index = index;
        this.berechnungControl = berechnungControl;

        divider = 6;

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(280, 370));
        setMaximumSize(getPreferredSize());
        setMinimumSize(getPreferredSize());

        labelBetraege = new JLabel("Beträge");
        labelSummeText = new JLabel("Summe");
        labelGesamtsummeText = new JLabel("/");

        labelSumme = new NewCurrencyField(10);
        labelSumme.setBackground(Color.PINK);
        labelSumme.setOpaque(true);
        labelSumme.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        labelSumme.setEditable(false);


        labelGesamtsumme = new NewCurrencyField(10);
        labelGesamtsumme.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        labelGesamtsumme.setEditable(false);

        //JComboBox mit Kostenarten wird erstellt
        comboKosten = new JComboBox(kostenArten);
        //comboKosten = new JComboBox();

        comboKosten.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
//                clearPanel();

                if (!e.getItem().equals(" - Kostenart auswählen - ")) {
                    setPanelVisible(true);
				} else {
					clearPanel();
					updateModel();
				}


            }
        });


        comboKosten.setEditable(false);
        comboKosten.setMaximumRowCount(comboKosten.getModel().getSize());



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

        for (int i = 0; i < 10; i++) {
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


        form.add(comboKosten, 0, 0, 3, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 10, 0));
        form.add(labelBetraege, 0, 2, 2, 1, 0.0, 0.0, 0, 13, new Insets(0, 5, 0, 5));

        for (NewCurrencyField cf : cfList) {
            form.add(cf, 2, 2 + cfList.indexOf(cf), 1, 1, 0.0, 0.0, 1, 10, new Insets(0, 0, 0, 0));
        }

        form.add(labelSummeText, 0, 14, 2, 1, 0.0, 0.0, 0, 13, new Insets(0, 5, 0, 5));
        form.add(labelGesamtsummeText, 0, 15, 1, 1, 0.0, 0.0, 0, 13, new Insets(0, 5, 0, 0));
        form.add(spinner, 1, 15, 1, 1, 0.0, 0.0, 0, 13, new Insets(0, 5, 0, 5));
        form.add(labelSumme, 2, 14, 1, 1, 0.0, 0.0, 2, 13, new Insets(10, 0, 10, 0));
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

        labelBetraege.setVisible(flag);
        labelSummeText.setVisible(flag);
        labelGesamtsummeText.setVisible(flag);
        labelSumme.setVisible(flag);
        labelGesamtsumme.setVisible(flag);
        spinner.setVisible(flag);

        for (NewCurrencyField cf : cfList) {
            cf.setVisible(flag);
        }

    }





    /**
     * KostenModel mit Kostenart und Kosten aktualisieren
     */
    public void updateModel() {
		type = getTypeForKind();

		if (null != type) {
			List<BigDecimal> singleValues = new ArrayList<BigDecimal>();
			for (NewCurrencyField cf : cfList) {
				singleValues.add(cf.getBValue());
			}

			berechnungControl.getBerechnungModel().setKostenRechner(index, type, divider, singleValues,
					labelGesamtsumme.getBValue());

			berechnungControl.getBerechnungModel().updateViews();

		}
	}


    /**
     * Addiert die Summe der Betragsfelder und aktualisiert das Summenfeld
     */
    private void updateSumme() {

        BigDecimal summe = new BigDecimal("0.0");

        for (NewCurrencyField cf : cfList) {
            summe = summe.add(cf.getBValue());
        }

        labelSumme.setBValue(summe);
        labelGesamtsumme.setBValue(summe.divide(new BigDecimal("" + divider), 2, BigDecimal.ROUND_HALF_UP));


        updateModel();

    }


    public void init() {
		LabeledDecimalList ldl = berechnungControl.getBerechnungModel().getKostenFeld(index);
		if (ldl.getTyp() != BerechnungsZettelCFTypen.PLATZHALTER) {
			int i = 0;
            for (BigDecimal value : ldl.getValueList()) {
                cfList.get(i).setBValue(value);
                i++;
            }
            setPanelVisible(true);
            comboKosten.setSelectedItem(kostenArten[getKindForType(ldl.getTyp())]);
            divider = ldl.getDivisor();
            spinner.setValue(divider);
		} else {
			comboKosten.setSelectedIndex(0);
			clearPanel();
		}
	}

	private void clearPanel() {
		for (int i = 0; i < 10; i++) {
			cfList.get(i).setBValue(BigDecimal.ZERO);
		}
		divider = 6;
		spinner.setValue(6);
		labelSumme.setBValue(BigDecimal.ZERO);
		labelGesamtsumme.setBValue(BigDecimal.ZERO);

		setPanelVisible(false);
	}

	private BerechnungsZettelCFTypen getTypeForKind() {
		String kind = (String) comboKosten.getSelectedItem();

        if (kind.equals(kostenArten[1])) {
            return BerechnungsZettelCFTypen.UNTERHALT;
        } else if (kind.equals(kostenArten[2])) {
            return BerechnungsZettelCFTypen.MEDKOSTEN;
        } else if (kind.equals(kostenArten[3])) {
            return BerechnungsZettelCFTypen.BESONDEREERNAEHRUNG;
        } else if (kind.equals(kostenArten[4])) {
            return BerechnungsZettelCFTypen.ZUSAETZLICH;
        } else if (kind.equals(kostenArten[5])) {
            return BerechnungsZettelCFTypen.SCHULDEN;
        } else if (kind.equals(kostenArten[6])) {
            return BerechnungsZettelCFTypen.KRANKENKASSE;
        } else if (kind.equals(kostenArten[7])) {
            return BerechnungsZettelCFTypen.MIETE;
        } else {
			return BerechnungsZettelCFTypen.PLATZHALTER;
		}
    }

    private int getKindForType(BerechnungsZettelCFTypen type) {
        if (type.equals(BerechnungsZettelCFTypen.UNTERHALT)) {
            return 1;
        } else if (type.equals(BerechnungsZettelCFTypen.MEDKOSTEN)) {
            return 2;
        } else if (type.equals(BerechnungsZettelCFTypen.BESONDEREERNAEHRUNG)) {
            return 3;
        } else if (type.equals(BerechnungsZettelCFTypen.ZUSAETZLICH)) {
            return 4;
        } else if (type.equals(BerechnungsZettelCFTypen.SCHULDEN)) {
            return 5;
        } else if (type.equals(BerechnungsZettelCFTypen.KRANKENKASSE)) {
            return 6;
        } else if (type.equals(BerechnungsZettelCFTypen.MIETE)) {
            return 7;
        } else {
            return 0;
        }
    }
}