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

import org.semtix.config.Berechnung;
import org.semtix.config.Settings;
import org.semtix.gui.tabs.berechnungszettel.BerechnungControl;
import org.semtix.gui.tabs.berechnungszettel.BerechnungModel;
import org.semtix.shared.daten.ArrayHelper;
import org.semtix.shared.daten.DeutschesDatum;
import org.semtix.shared.elements.*;
import org.semtix.shared.elements.control.DocumentSizeFilter;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

/**
 * Panel im Berechnungszettel (Seite 1), welches die Angaben zum Bedarf anzeigt
 */
@SuppressWarnings("serial")
public class MoneyPanel
        extends SForm
        implements Observer {

	private static final String KEINEMIETKAPPUNG = "Mietkappung deaktivieren";
	private final String HEIZKOSTENPAUSCHALE = "Heizkostenpauschale aktivieren";
	private final String AKTIVIEREN = "<html>Betrag wird bei Aktivierung zugewiesen<br>Betrag siehe Adminpanel</html>";

	private BerechnungControl berechnungControl;

    private NewCurrencyField tfGrundbedarf, cfBedarfKinder, cfBetragWeiterePersonen, tfMehrbedarf,
            tfAnzeigeMiete, tfHeizpauschale;
    private NewCurrencyField cfKrankenkasse, tfUnterhalt, tfMedPsychKosten, tfBesondereErnaehrung,
            tfZusaetzlicheKosten, tfEingabeMiete, cfSummeWertBedarf, tfTarifABC, tfAuslandskosten;
    private NewCurrencyField cfEinkommenBetrag1, cfEinkommenBetrag2, cfEinkommenBetrag3,
            cfEinkommenBetrag4, cfWohngeld, cfKindergeld, cfSummeWertEinkommen, cfSchulden;
    private NewCurrencyField[] arrayCurrencyField;

    private FormularTextfeld[] arraytfEinkommen;

    private JTextField tfAnzahlKinder, tfAnzahlWeiterePersonen;

    private JTextArea textArea;

    private JLabel lbPersonenAnzahl, lbTarifABCText;
    private JLabel lbEinkommenText1, lbEinkommenText2, lbEinkommenText3, lbEinkommenText4, lbAnrechnungsVerhaeltnis;

    private WarnLabel warnLabelKind, warnLabelKind2, warnLabelMedPsychKosten;
    private WarnLabelKappung warnLabelKappungMiete, warnLabelKappungWohngeld, warnLabelKappungSchulden;

    private JCheckBox cbTarifABC, cbMietkappung, cbHeizpauschale, cbAuslandskosten, cbSchulden, cbKindergeld, cbWohngeld;


    /**
     * Erstellt ein neues BedarfPanel
     *
     * @param berechnungControl BerechnungControl
     */
    public MoneyPanel(final BerechnungControl berechnungControl) {

        this.berechnungControl = berechnungControl;

        setBackground(Color.WHITE);


        JLabel lbTitelBedarf = new JLabel("Bedarf");
        lbTitelBedarf.setFont(lbTitelBedarf.getFont().deriveFont(Font.BOLD, 13f));

        JLabel lbGrundbedarf = new JLabel("Grundbedarf");
        JLabel lbKinder = new JLabel("Kinder (bis 18 J.)");
        JLabel lbWeiterePersonen = new JLabel("weitere Personen (andere)");
        JLabel lbMehrbedarf = new JLabel("Mehrbedarf");
        JLabel lbKrankenkasse = new JLabel("Krankenkasse");
        JLabel lbMiete = new JLabel("Miete");
        JLabel lbAuslandskosten = new JLabel("Auslandskosten");
        JLabel lbUnterhalt = new JLabel("Unterhalt/Unterstützung");
        JLabel lbMedKosten = new JLabel("Med./Psych. Kosten");
        JLabel lbBesondereErnaehrung = new JLabel("besondere Ernährung");
        JLabel lbZusKosten = new JLabel("zusätzliche Kosten");

        JLabel lbPersonenzahl = new JLabel("Personenzahl");
        lbPersonenzahl.setFont(lbPersonenzahl.getFont().deriveFont(10f));

        JLabel lbBetrag = new JLabel("Betrag");
        lbBetrag.setFont(lbBetrag.getFont().deriveFont(10f));

		WarnLabelKappung lbKK = new WarnLabelKappung(KEINEMIETKAPPUNG);
        lbKK.setVisible(true);
        lbKK.setEnabled(true);
		lbKK.setIconVisible(true);

        JLabel lbHKP = new JLabel("HK");
        lbHKP.setFont(lbBetrag.getFont().deriveFont(10f));
		lbHKP.setToolTipText(HEIZKOSTENPAUSCHALE);

        JLabel lbSummeBedarf = new JLabel("Summe Bedarf");
        lbSummeBedarf.setFont(lbSummeBedarf.getFont().deriveFont(Font.BOLD));

        tfGrundbedarf = new NewCurrencyField(10);                // Betrag Grundbedarf
        cfBedarfKinder = new NewCurrencyField(10);                    // Betrag Kinder
        cfBetragWeiterePersonen = new NewCurrencyField(10);        // Betrag weitere Personen (andere)
        tfMehrbedarf = new NewCurrencyField(10);                // Betrag Mehrbedarf
        cfKrankenkasse = new NewCurrencyField(10);                // Betrag Krankenkasse
        tfAnzeigeMiete = new NewCurrencyField(10);                // Betrag Miete
        tfAuslandskosten = new NewCurrencyField(10);            // Betrag Auslandskosten
        tfHeizpauschale = new NewCurrencyField(6);              // Betrag Heiz. Pauschale
        tfUnterhalt = new NewCurrencyField(10);                    // Betrag Unterhalt/Unterstützung
        tfMedPsychKosten = new NewCurrencyField(10);            // Betrag Med./Psych. Kosten
        tfBesondereErnaehrung = new NewCurrencyField(10);        // Betrag besondere Ernährung
        tfZusaetzlicheKosten = new NewCurrencyField(10);        // Betrag zusätzliche Kosten
        tfEingabeMiete = new NewCurrencyField(6);               // Eingabefeld Miete

        tfAnzahlKinder = new JTextField(4);                        // Anzahl Kinder (bis 18 J.)
        tfAnzahlKinder.setPreferredSize(new Dimension(30, 24));
        tfAnzahlKinder.setMinimumSize(tfAnzahlKinder.getPreferredSize());


        tfAnzahlWeiterePersonen = new JTextField(4);            // Anzahl weitere Personen (andere)
        tfAnzahlWeiterePersonen.setPreferredSize(new Dimension(30, 24));
        tfAnzahlWeiterePersonen.setMinimumSize(tfAnzahlWeiterePersonen.getPreferredSize());


        // Textfeldinhalt auf zentriert setzen
        tfAnzahlKinder.setHorizontalAlignment(JTextField.CENTER);
        tfAnzahlWeiterePersonen.setHorizontalAlignment(JTextField.CENTER);

        // Rahmen um Textfelder setzen
        tfAnzahlKinder.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        tfAnzahlWeiterePersonen.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // DocumentSizeFilter für Textfelder setzen (max. Anzahl an Zeichen und Pattern)
        ((AbstractDocument) tfAnzahlKinder.getDocument()).setDocumentFilter(new DocumentSizeFilter(2, DocumentSizeFilter.POSITIVE_NUMBER_PATTERN));
        ((AbstractDocument) tfAnzahlWeiterePersonen.getDocument()).setDocumentFilter(new DocumentSizeFilter(2, DocumentSizeFilter.POSITIVE_NUMBER_PATTERN));


        lbTarifABCText = new JLabel("Außerhalb Tarif ABC (" +
                NumberFormat.getCurrencyInstance().format(Berechnung.ABC_TARIF) + " / 6 gerundet)");

        tfTarifABC = new NewCurrencyField(10);


        // ***** Checkboxen *****

        cbTarifABC = new JCheckBox();
		cbTarifABC.setToolTipText(AKTIVIEREN);
		cbMietkappung = new JCheckBox();
		cbMietkappung.setToolTipText(KEINEMIETKAPPUNG);
		cbHeizpauschale = new JCheckBox();
		cbHeizpauschale.setToolTipText(HEIZKOSTENPAUSCHALE);
		cbAuslandskosten = new JCheckBox();
		cbAuslandskosten.setToolTipText(AKTIVIEREN);


        lbPersonenAnzahl = new FormularLabel("", 1, 30, 24);    // Personenanzahl (bei Miete)
        lbPersonenAnzahl.setHorizontalAlignment(JLabel.CENTER);


        cfSummeWertBedarf = new NewCurrencyField(10);
        cfSummeWertBedarf.setEditable(false);

        textArea = new JTextArea();

        Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
        Border emptyBorder = new EmptyBorder(5, 5, 5, 5);
        CompoundBorder compoundBorder = new CompoundBorder(lineBorder, emptyBorder);
        textArea.setBorder(compoundBorder);

        textArea.setPreferredSize(new Dimension(300, 150));
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);

        // Verhindert das automatische Scrollen der Textarea
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);


        warnLabelKind = new WarnLabel("entsprechende Härte und das Kindergeld checken");

        warnLabelMedPsychKosten = new WarnLabel("<html>Med./Psych. Kosten über Schwelle. Automatisch bei Härten anerkannt...</html>");
        warnLabelKappungMiete = new WarnLabelKappung("Kappung der Miete");


        Insets insets2 = new Insets(2, 0, 2, 0);
        Insets insetsm = new Insets(0, 0, -1, 0);


        add(lbTitelBedarf, 0, 1, 1, 1, 0.0, 0.0, 0, 18, insets2);
        add(lbPersonenzahl, 1, 1, 2, 1, 0.0, 0.0, 0, GridBagConstraints.NORTHWEST, new Insets(2, 20, 2, 2));
        add(lbBetrag, 6, 1, 2, 1, 0.0, 0.0, 0, 11, insets2);

        add(lbGrundbedarf, 0, 2, 1, 1, 0.0, 0.0, 0, GridBagConstraints.WEST, insetsm);
        add(lbKK, 4, 2, 1, 1, 0.0, 0.0, 0, GridBagConstraints.SOUTHWEST, new Insets(0, 0, 0, 2));
        add(lbHKP, 5, 2, 1, 1, 0.0, 0.0, 0, GridBagConstraints.SOUTHWEST, new Insets(0, 0, 0, 2));
        add(tfGrundbedarf, 6, 2, 1, 1, 0.0, 0.0, 0, GridBagConstraints.NORTHWEST, insetsm);


        add(lbMiete, 0, 3, 1, 1, 0.0, 0.0, 0, 17, insetsm);
        add(lbPersonenAnzahl, 1, 3, 1, 1, 0.0, 0.0, 0, GridBagConstraints.CENTER, insetsm);
        add(tfEingabeMiete, 2, 3, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, -5, -1, 0));
        add(tfHeizpauschale, 3, 3, 1, 1, 0.0, 0.0, 0, 18, new Insets(0, 2, -1, 0));
        add(cbMietkappung, 4, 3, 1, 1, 0.0, 0.0, 0, 18, insetsm);
        add(cbHeizpauschale, 5, 3, 1, 1, 0.0, 0.0, 0, 18, insetsm);
        add(tfAnzeigeMiete, 6, 3, 1, 1, 0.0, 0.0, 0, 18, insetsm);
        add(warnLabelKappungMiete, 7, 3, 1, 1, 0.0, 0.0, 0, 16, insetsm);


        add(lbKrankenkasse, 0, 4, 1, 1, 0.0, 0.0, 0, 17, insetsm);
        add(cfKrankenkasse, 6, 4, 1, 1, 0.0, 0.0, 0, 18, insetsm);


        add(lbKinder, 0, 5, 1, 1, 0.0, 0.0, 0, GridBagConstraints.WEST, insetsm);
        add(tfAnzahlKinder, 1, 5, 1, 1, 0.0, 0.0, 0, GridBagConstraints.CENTER, insetsm);
        add(cfBedarfKinder, 6, 5, 1, 1, 0.0, 0.0, 0, 18, insetsm);
        add(warnLabelKind, 7, 5, 1, 1, 0.0, 0.0, 0, 16, insetsm);


        add(lbWeiterePersonen, 0, 6, 1, 1, 0.0, 0.0, 0, 17, insetsm);
        add(tfAnzahlWeiterePersonen, 1, 6, 1, 1, 0.0, 0.0, 0, GridBagConstraints.CENTER, insetsm);
        add(cfBetragWeiterePersonen, 6, 6, 1, 1, 0.0, 0.0, 0, 18, insetsm);


        add(lbTarifABCText, 0, 7, 2, 1, 0.0, 0.0, 0, 17, insetsm);
        add(cbTarifABC, 5, 7, 1, 1, 0.0, 0.0, 0, 18, insetsm);
        add(tfTarifABC, 6, 7, 1, 1, 0.0, 0.0, 0, 18, insetsm);


        add(lbAuslandskosten, 0, 8, 1, 1, 0.0, 0.0, 0, 17, insetsm);
        add(cbAuslandskosten, 5, 8, 1, 1, 0.0, 0.0, 0, 18, insetsm);
        add(tfAuslandskosten, 6, 8, 1, 1, 0.0, 0.0, 0, 18, insetsm);


        add(lbUnterhalt, 0, 9, 2, 1, 0.0, 0.0, 0, 17, insetsm);
        add(tfUnterhalt, 6, 9, 1, 1, 0.0, 0.0, 0, 18, insetsm);


        add(lbBesondereErnaehrung, 0, 10, 2, 1, 0.0, 0.0, 0, 17, insetsm);
        add(tfBesondereErnaehrung, 6, 10, 1, 1, 0.0, 0.0, 0, 18, insetsm);


        add(lbMedKosten, 0, 11, 2, 1, 0.0, 0.0, 0, 17, insetsm);
        add(tfMedPsychKosten, 6, 11, 1, 1, 0.0, 0.0, 0, 18, insetsm);
        add(warnLabelMedPsychKosten, 7, 11, 1, 1, 0.0, 0.0, 0, 16, insetsm);


        add(lbZusKosten, 0, 12, 1, 1, 0.0, 0.0, 0, 17, insetsm);
        add(tfZusaetzlicheKosten, 6, 12, 1, 1, 0.0, 0.0, 0, 18, insetsm);


        add(lbMehrbedarf, 0, 13, 1, 1, 0.0, 0.0, 0, 17, insetsm);
        add(tfMehrbedarf, 6, 13, 1, 1, 0.0, 0.0, 0, 18, insetsm);


        add(lbSummeBedarf, 0, 16, 5, 1, 0.0, 0.0, 0, GridBagConstraints.EAST, insets2);
        add(cfSummeWertBedarf, 6, 16, 1, 1, 0.0, 0.0, 0, 17, insets2);


        JPanel fillpanel = new JPanel();
        fillpanel.setBackground(Color.WHITE);
        add(fillpanel, 8, 2, 1, 7, 1.0, 1.0, GridBagConstraints.BOTH, 17, insets2);
        add(textArea, 9, 2, 1, 7, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.NORTHEAST, new Insets(2, 2, 2, 9));


        // ***** Titel/Beschriftung des Einkommens

        JLabel lbTitelEinkommen = new JLabel("Einkommen");
        lbTitelEinkommen.setFont(lbTitelEinkommen.getFont().deriveFont(Font.BOLD, 13f));

        JLabel lbBetragEinkommen = new JLabel("Betrag");
        lbBetragEinkommen.setFont(lbBetragEinkommen.getFont().deriveFont(10f));


        // ***** Zeile mit Wohngeld *****


        JLabel lbWohngeld = new JLabel("Wohngeld");
        cfWohngeld = new NewCurrencyField(10);
        cbWohngeld = new JCheckBox("Auf 0 setzen");



        warnLabelKappungWohngeld = new WarnLabelKappung("Kappung Miete/Wohngeld");


        // ***** Kindergeld *****
        JLabel lbKindergeld = new JLabel("Kindergeld");
        cfKindergeld = new NewCurrencyField(10);
        warnLabelKind2 = new WarnLabel("entsprechende Härte und das Kindergeld checken");

        cbKindergeld = new JCheckBox("Auf 0 setzen");

        // ***** Einkommenfelder, die von den Einkommenrechnern von Zettel 2 übernommen werden *****

        // Text des Einkommen
        lbEinkommenText1 = new FormularLabel("", 1, 280, 24);
        lbEinkommenText1.setToolTipText("Nicht editierbar. Siehe andere Seite");
        lbEinkommenText2 = new FormularLabel("", 1, 280, 24);
        lbEinkommenText2.setToolTipText("Nicht editierbar. Siehe andere Seite");

        lbEinkommenText3 = new FormularLabel("", 1, 280, 24);
        lbEinkommenText3.setToolTipText("Nicht editierbar. Siehe andere Seite");

        lbEinkommenText4 = new FormularLabel("", 1, 280, 24);
        lbEinkommenText4.setToolTipText("Nicht editierbar. Siehe andere Seite");

        // Betragsfeld für das Einkommen
        cfEinkommenBetrag1 = new NewCurrencyField(10);
        cfEinkommenBetrag2 = new NewCurrencyField(10);
        cfEinkommenBetrag3 = new NewCurrencyField(10);
        cfEinkommenBetrag4 = new NewCurrencyField(10);

        // Betragsfelder können nicht editiert werden
        cfEinkommenBetrag1.setEditable(false);
        cfEinkommenBetrag2.setEditable(false);
        cfEinkommenBetrag3.setEditable(false);
        cfEinkommenBetrag4.setEditable(false);


        // ***** Zeile mit Summe Einkommen *****

        JLabel lbSummeEinkommen = new JLabel("Summe Einkommen");
        lbSummeEinkommen.setFont(lbSummeBedarf.getFont().deriveFont(Font.BOLD));

        cfSummeWertEinkommen = new NewCurrencyField(10);
        cfSummeWertEinkommen.setEditable(false);


        this.cfSchulden = new NewCurrencyField(10);


        warnLabelKappungSchulden = new WarnLabelKappung("Schuldenkappung entsprechend des Einkommen");


        this.cbSchulden = new JCheckBox();
        this.cbSchulden.setText("");
        this.cbSchulden.setSelected(true);
        this.cbSchulden.setForeground(Color.GRAY);


        int row = 17;

        add(lbTitelEinkommen, 0, row, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 0, 5, 5));

        int rowCount = Settings.ANZAHL_FREIE_EINKOMMENSFELDER;

        // Array mit Anzahl der Textfelder anlegen
        arraytfEinkommen = new FormularTextfeld[rowCount];

        // Array mit Anzahl der Betragsfelder anlegen
        arrayCurrencyField = new NewCurrencyField[rowCount];

        for (int i = 0; i < rowCount; i++) {

            final int z = i;


            // Textfelder für Einkommen (frei verfügbar)
            arraytfEinkommen[z] = new FormularTextfeld("", 1, 280, 24, JTextField.LEFT);
            arraytfEinkommen[z].addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    berechnungControl.getBerechnungModel().setFreiesEinkommensfeldName(z, arraytfEinkommen[z].getText());
                }
            });

            // Betragsfelder für Eingabe von Einkommen
            arrayCurrencyField[z] = new NewCurrencyField(10);

            arrayCurrencyField[z].addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent arg0) {
                    berechnungControl.getBerechnungModel().setFreiesEinkommensfeld(z, arrayCurrencyField[z].getBValue());
                }
            });


            add(arraytfEinkommen[z], 0, ++row, 2, 1, 0.0, 0.0, 0, 17, insetsm);
            add(arrayCurrencyField[z], 6, row, 1, 1, 0.0, 0.0, 0, 18, insetsm);

        }


        add(lbWohngeld, 0, ++row, 1, 1, 0.0, 0.0, 0, 17, insetsm);
        add(cbWohngeld, 1, row, 1, 1, 0.0, 0.0, 0, 17, insetsm);


        add(cfWohngeld, 6, row, 1, 1, 0.0, 0.0, 0, 18, insetsm);
        add(warnLabelKappungWohngeld, 7, row, 1, 1, 0.0, 0.0, 0, 17, insetsm);


        add(lbKindergeld, 0, ++row, 1, 1, 0.0, 0.0, 0, 17, insetsm);
        add(cbKindergeld, 1, row, 1, 1, 0.0, 0.0, 0, 17, insetsm);
        add(cfKindergeld, 6, row, 1, 1, 0.0, 0.0, 0, 18, insetsm);
        add(warnLabelKind2, 7, row, 1, 1, 0.0, 0.0, 0, 16, insetsm);


        add(lbEinkommenText1, 0, ++row, 2, 1, 0.0, 0.0, 0, 18, insetsm);
        add(cfEinkommenBetrag1, 6, row, 1, 1, 0.0, 0.0, 0, 18, insetsm);

        add(lbEinkommenText2, 0, ++row, 2, 1, 0.0, 0.0, 0, 18, insetsm);
        add(cfEinkommenBetrag2, 6, row, 1, 1, 0.0, 0.0, 0, 18, insetsm);

        add(lbEinkommenText3, 0, ++row, 2, 1, 0.0, 0.0, 0, 18, insetsm);
        add(cfEinkommenBetrag3, 6, row, 1, 1, 0.0, 0.0, 0, 18, insetsm);

        add(lbEinkommenText4, 0, ++row, 2, 1, 0.0, 0.0, 0, 18, insetsm);
        add(cfEinkommenBetrag4, 6, row, 1, 1, 0.0, 0.0, 0, 18, insetsm);


        add(lbSummeEinkommen, 0, ++row, 5, 1, 0.0, 0.0, 0, GridBagConstraints.EAST, insets2);
        add(cfSummeWertEinkommen, 6, row, 1, 1, 0.0, 0.0, 0, 18, insets2);

        add(new JLabel("Schulden"), 0, ++row, 1, 1, 0.0, 0.0, 0, 17, insets2);
        add(cbSchulden, 1, row, 2, 1, 0.0, 0.0, 0, 13, insets2);
        add(cfSchulden, 6, row, 1, 1, 0.0, 0.0, 0, 16, insets2);
        add(warnLabelKappungSchulden, 7, row, 1, 1, 0.0, 0.0, 0, 16, insets2);


        // ***** Tab-Reihenfolge setzen *****
        JComponent[] order = new JComponent[]{tfEingabeMiete, cbMietkappung, cbHeizpauschale, cfKrankenkasse, tfAnzahlKinder, tfAnzahlWeiterePersonen,
                cbTarifABC, cbAuslandskosten,
				tfUnterhalt, tfBesondereErnaehrung, tfMedPsychKosten, tfZusaetzlicheKosten, tfMehrbedarf};
		JComponent[] otherArray = new JComponent[]{cfWohngeld, cfKindergeld};
        JComponent[] einkommensArray = new JComponent[arrayCurrencyField.length * 2];
        int j = 0;
        for (int i = 0; i < arrayCurrencyField.length; i++) {
            einkommensArray[j] = arraytfEinkommen[i];
            j++;
            einkommensArray[j] = arrayCurrencyField[i];
            j++;
        }
        otherArray = ArrayHelper.concatenate(einkommensArray, otherArray);
        order = ArrayHelper.concatenate(order, otherArray);


        setFocusTraversalPolicy(new FormularTabReihenfolge(order));

        setFocusCycleRoot(true);


        HashSet<KeyStroke> keys = new HashSet<KeyStroke>();
        keys.add(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false));
        keys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0, false));
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, keys);

        //nicht zu editierende Felder:
        tfHeizpauschale.setEditable(false);
		tfTarifABC.setEditable(false);
		tfAnzeigeMiete.setEditable(false);

        setListener();
    }

    private void setListener() {

        cfKrankenkasse.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent arg0) {
                berechnungControl.getBerechnungModel().setKrankenkasse(cfKrankenkasse.getBValue());
            }
        });


        tfUnterhalt.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent arg0) {
                berechnungControl.getBerechnungModel().setUnterhalt(tfUnterhalt.getBValue());
            }
        });

        tfMedPsychKosten.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent arg0) {
                berechnungControl.getBerechnungModel().setMedKosten(tfMedPsychKosten.getBValue());
            }
        });

        tfBesondereErnaehrung.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent arg0) {
                berechnungControl.getBerechnungModel().setErnaehrung(tfBesondereErnaehrung.getBValue());
            }
        });

        tfZusaetzlicheKosten.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent arg0) {
                berechnungControl.getBerechnungModel().setZusaetzlicheKosten(tfZusaetzlicheKosten.getBValue());
            }
        });


        tfAnzahlKinder.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                berechnungControl.getBerechnungModel().setAnzahlKinder(tfAnzahlKinder.getText().trim().equals("") ? 0 : Integer.valueOf(tfAnzahlKinder.getText()));

            }
        });


        tfAnzahlWeiterePersonen.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                berechnungControl.getBerechnungModel().setAnzahlWeiterePersonen(tfAnzahlWeiterePersonen.getText().trim().equals("") ? 0 : Integer.valueOf(tfAnzahlWeiterePersonen.getText()));
            }
        });


        tfEingabeMiete.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent arg0) {
                berechnungControl.getBerechnungModel().setMiete(tfEingabeMiete.getBValue());
            }
        });


        tfMehrbedarf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                berechnungControl.getBerechnungModel().setMehrbedarf(tfMehrbedarf.getBValue());
            }
        });


        cbTarifABC.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                berechnungControl.getBerechnungModel().setTarifABC((e.getStateChange() == ItemEvent.SELECTED));
            }
        });

        cbMietkappung.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                berechnungControl.getBerechnungModel().setMietkappungMiete((e.getStateChange() == ItemEvent.SELECTED));
            }
        });

        cbHeizpauschale.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                berechnungControl.getBerechnungModel().setHeizpauschale((e.getStateChange() == ItemEvent.SELECTED));
            }
        });


        cbAuslandskosten.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                berechnungControl.getBerechnungModel().setAuslandskosten((e.getStateChange() == ItemEvent.SELECTED));
            }
        });

        cfKindergeld.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent arg0) {
                berechnungControl.getBerechnungModel().setKindergeld(cfKindergeld.getBValue());
            }
        });

        cbKindergeld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (cbKindergeld.isSelected()) {
                    berechnungControl.getBerechnungModel().setKindergeld(BigDecimal.ZERO);
                }
            }
        });

        cfWohngeld.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent arg0) {
                berechnungControl.getBerechnungModel().setWohngeld(cfWohngeld.getBValue());
            }
        });

        cbWohngeld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (cbWohngeld.isSelected()) {
                    berechnungControl.getBerechnungModel().setWohngeld(BigDecimal.ZERO);
                }
            }
        });

        cfSchulden.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent arg0) {
                berechnungControl.getBerechnungModel().setSchulden(cfSchulden.getBValue());
            }
        });


        cbSchulden.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                berechnungControl.getBerechnungModel().setSchuldenGekappt(cbSchulden.isSelected());
            }
        });

    }


    /**
     * View aktualisieren (Observer)
     */
    @Override
    public void update(Observable o, Object arg) {
        BerechnungModel moneyModel = (BerechnungModel) o;


        //BEDARF
        tfAnzahlKinder.setText(new Integer(moneyModel.getAnzahlKinder()).toString());
        cfBedarfKinder.setBValue(moneyModel.getBetragKinder());
        tfAnzahlWeiterePersonen.setText(new Integer(moneyModel.getAnzahlWeiterePersonen()).toString());
        cfBetragWeiterePersonen.setBValue(moneyModel.getBetragWeiterePersonen());
        tfMehrbedarf.setBValue(moneyModel.getMehrbedarf());
        tfEingabeMiete.setBValue(moneyModel.getMiete());
        tfAnzeigeMiete.setBValue(moneyModel.getMieteEffektiv());
        tfHeizpauschale.setBValue(moneyModel.getHeizpauschale());
        tfAuslandskosten.setBValue(moneyModel.getAuslandskosten());
        tfUnterhalt.setBValue(moneyModel.getUnterhalt());
        tfMedPsychKosten.setBValue(moneyModel.getMedKosten());
        tfBesondereErnaehrung.setBValue(moneyModel.getErnaehrung());
        tfZusaetzlicheKosten.setBValue(moneyModel.getZusaetzlicheKosten());
        cfKrankenkasse.setBValue(moneyModel.getKrankenkasse());
        tfTarifABC.setBValue(moneyModel.getTarifABC());
        cfWohngeld.setBValue(moneyModel.getWohngeld());

        cfSummeWertBedarf.setBValue(moneyModel.getSummeBedarf());

        //EINKOMMEN
        for (int i = 0; i < Settings.ANZAHL_FREIE_EINKOMMENSFELDER; i++) {
            arrayCurrencyField[i].setBValue(moneyModel.getFreiesEinkommensfeldValue(i));
        }

        lbEinkommenText1.setText(moneyModel.getEinkommenName(0));
        lbEinkommenText2.setText(moneyModel.getEinkommenName(1));
        lbEinkommenText3.setText(moneyModel.getEinkommenName(2));
        lbEinkommenText4.setText(moneyModel.getEinkommenName(3));

        cfKindergeld.setBValue(moneyModel.getKindergeld());

        cfEinkommenBetrag1.setBValue(moneyModel.getEinkommen(0));
        cfEinkommenBetrag2.setBValue(moneyModel.getEinkommen(1));
        cfEinkommenBetrag3.setBValue(moneyModel.getEinkommen(2));
        cfEinkommenBetrag4.setBValue(moneyModel.getEinkommen(3));

        cfSummeWertEinkommen.setBValue(moneyModel.getSummeEinkommen());
        cfSchulden.setBValue(moneyModel.getSchulden());

        //TEXTFELDER
        lbPersonenAnzahl.setText("" + moneyModel.getGesamtAnzahlPersonen());
        textArea.setText(moneyModel.getText());
        cbSchulden.setText(moneyModel.getSchuldenProzent() + " % des Eink. = " + DeutschesDatum.getEuroFormatted(moneyModel.getSchuldenKappe()));

        //ANZEIGEN JA/NEIN: Labels,Icons usw.
        warnLabelKappungWohngeld.setIconVisible(moneyModel.isMieteGekappt());

        warnLabelKappungMiete.setIconVisible(moneyModel.isMieteGekappt());
        warnLabelKappungSchulden.setIconVisible(moneyModel.isSchuldenGekappt());
        warnLabelKind.setIconVisible(moneyModel.isWarnungKind());
        warnLabelKind2.setIconVisible(moneyModel.isWarnungKind());

        warnLabelMedPsychKosten.setIconVisible(moneyModel.isWarnungMedPsych());
    }


    /**
     * Nicht alle Werte müssen geupdatet werden. Manche müssen nur einmal initialisiert werden
     */
    public void initValues() {
        BerechnungModel model = berechnungControl.getBerechnungModel();
        tfGrundbedarf.setBValue(model.getGrundBedarf());
        for (int i = 0; i < Settings.ANZAHL_FREIE_EINKOMMENSFELDER; i++) {
            arraytfEinkommen[i].setText(model.getFreiesEinkommensfeldName(i));
        }

        cbMietkappung.setSelected(model.isKeineMietkappungMiete());
        cbHeizpauschale.setSelected(model.isHeizpauschale());
		cbAuslandskosten.setSelected(model.isAuslandkosten());
		cbTarifABC.setSelected(model.isTarifABC());
		update(model, null);
    }


}



