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


import org.semtix.config.Berechnung;
import org.semtix.config.SettingsExternal;
import org.semtix.db.dao.AntragHaerte;
import org.semtix.gui.tabs.berechnungszettel.BerechnungControl;
import org.semtix.gui.tabs.berechnungszettel.BerechnungModel;
import org.semtix.shared.daten.enums.HaerteAblehnungsgrund;
import org.semtix.shared.daten.enums.Haertegrund;
import org.semtix.shared.elements.FormularTextfeld;
import org.semtix.shared.elements.SForm;
import org.semtix.shared.elements.WiderDropDownCombo;
import org.semtix.shared.elements.control.DocumentSizeFilter;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Panel im Berechnungszettel (Seite 1), welches die Härten anzeigt
 *
 * init setzt eine Liste von Antraghärten
 *
 * getHaerteListe holt die aktuell eingestellten Härten
 */
@SuppressWarnings("serial")
public class HaertePanel
        extends SForm implements Observer {

    private BerechnungControl berechnungControl;

    private List<BesondereHaertePanel> besondereHaertePanelList;

    private List<RowPanel> listRowPanel;

    private String STANDARDTEXT = "Sonstige im Antrag angegebene Situation";
    private boolean initrunning;


    /**
     * Erstellt ein neues HaertePanel
     *
     * @param berechnungControl BerechnungControl
     */
    public HaertePanel(BerechnungControl berechnungControl) {

        this.berechnungControl = berechnungControl;

        setBackground(Color.WHITE);

        listRowPanel = new ArrayList<RowPanel>();

        SForm leftPanel = new SForm();
        SForm rightPanel = new SForm();

        leftPanel.setBackground(Color.WHITE);
        rightPanel.setBackground(Color.WHITE);

        JLabel titelHaerten = new JLabel("<html><b>Härten</b></html>");
        titelHaerten.setPreferredSize(new Dimension(230, 24));

        JLabel lbAngegeben = new JLabel("<html>ange-<br>geben</html>", JLabel.CENTER);
        JLabel lbAnerkannt = new JLabel("<html>aner-<br>kannt</html>", JLabel.CENTER);
        JLabel lbAbgelehnt = new JLabel("<html>abge-<br>lehnt</html>", JLabel.CENTER);
        JLabel lbBegruendung = new JLabel("<html>begruen-<br>dung</html>", JLabel.CENTER);

        JLabel lbPunkte = new JLabel("Punkte", JLabel.CENTER);


        besondereHaertePanelList = new ArrayList<BesondereHaertePanel>();

        for (int i = 1; i <= SettingsExternal.ZUSAETZLICHE_SONSTIGE_HAERTEN + 1; i++) {
            BesondereHaertePanel besondereHaertePanel = new BesondereHaertePanel(i + ". Besondere Härte?");
            besondereHaertePanelList.add(besondereHaertePanel);

            int j = 0;
            int k = 4; //modulo'ator ;-)
            //darstellung der Besonderen Härte-Panels
            if (i > 4 && SettingsExternal.ZUSAETZLICHE_SONSTIGE_HAERTEN < 8) {
                j = 1;
            }

            rightPanel.add(besondereHaertePanel, j, (i - 1) % k, 1, 1, 1.0, 1.0, 2, 18, new Insets(5, 2, 5, 2));
        }


        SForm titelPanel1 = new SForm();
        titelPanel1.setBackground(Color.WHITE);

        titelHaerten.setPreferredSize(new Dimension(220, 24));
        titelHaerten.setMinimumSize(titelHaerten.getPreferredSize());


        titelPanel1.add(titelHaerten, 0, 0, 1, 1, 1.0, 1.0, 2, 18, new Insets(0, 2, 0, 2));
        titelPanel1.add(lbAngegeben, 1, 0, 1, 1, 1.0, 1.0, 0, 18, new Insets(0, 30, 0, 30));
        titelPanel1.add(lbAnerkannt, 2, 0, 1, 1, 1.0, 1.0, 0, 18, new Insets(0, 10, 0, 30));
        titelPanel1.add(lbAbgelehnt, 3, 0, 1, 1, 1.0, 1.0, 0, 18, new Insets(0, 10, 0, 30));
        titelPanel1.add(lbBegruendung, 4, 0, 1, 1, 1.0, 1.0, 0, 18, new Insets(0, 0, 0, 0));
        titelPanel1.add(lbPunkte, 5, 0, 1, 1, 1.0, 1.0, 0, 18, new Insets(0, 30, 0, 2));

        leftPanel.add(titelPanel1, 0, 0, 1, 1, 1.0, 1.0, 2, 18, new Insets(0, 0, 10, 0));


        // Für jeden Härtegrund + die zusätzlichen Sonstigen Härten eine Zeile anlegen
        for (int i = 1; i <= Haertegrund.values().length + SettingsExternal.ZUSAETZLICHE_SONSTIGE_HAERTEN; i++) {

            Haertegrund haertegrund = Haertegrund.getHaertegrundByID(i);

            // Label für anzeige des Härtegrundes erstellen
            JLabel lbHaerte = new JLabel(haertegrund.getName());


            RowPanel rowPanel;

            if (haertegrund.equals(Haertegrund.SONSTIGE)) {
                // Textfelder für Sonstige Härten
                final JTextField tfSonstige = new FormularTextfeld("", 1, 150, 24, JTextField.LEFT);

                // Zeile für Sonstige Härten erstellen und hinzufügen
                rowPanel = new RowPanel(tfSonstige, i);
            } else {
                // Zeile (mit Label und Checkboxen) für Härtegrund erstellen
                rowPanel = new RowPanel(lbHaerte, i);
            }

            // Zeile der View hinzufügen
            leftPanel.add(rowPanel, 0, i, 1, 1, 1.0, 1.0, 2, 18, new Insets(0, 0, 4, 0));

            // Zeile der Liste hinzufügen (für spätere Erreichbarkeit)
            listRowPanel.add(rowPanel);

        }

        add(leftPanel, 0, 0, 1, 1, 1.0, 1.0, 0, 18, new Insets(5, 0, 5, 0));

        add(rightPanel, 1, 0, 1, 1, 1.0, 1.0, 0, 18, new Insets(5, 0, 5, 0));

    }


    /**
     * Beim Aufruf des Berechnungszettels Werte für Härten eintragen
     */
    public void init() {
        this.initrunning = true;

        List<AntragHaerte> haerteList;

        if (null == berechnungControl.getBerechnungModel().getAntrag().getHaerteListe()) {
            haerteList = createDefaultHaerteList(new ArrayList<AntragHaerte>(listRowPanel.size()));
        } else if (berechnungControl.getBerechnungModel().getAntrag().getHaerteListe().size() != listRowPanel.size()) {
            haerteList = createDefaultHaerteList(berechnungControl.getBerechnungModel().getAntrag().getHaerteListe());
        } else {
            haerteList = berechnungControl.getBerechnungModel().getAntrag().getHaerteListe();
        }


        int i = -1; // für sonstige härten
        for (AntragHaerte a : haerteList) {
            RowPanel r = listRowPanel.get(a.getHaertegrund().getID() - 1);
            if (a.getHaertegrund().equals(Haertegrund.SONSTIGE)) {

                r = listRowPanel.get(Haertegrund.SONSTIGE.getID() + i);

                i++;

            }

            r.cbAngegeben.setSelected(a.isAngegeben());

            r.cbAnerkannt.setSelected(a.isAnerkannt());

            r.cbAbgelehnt.setSelected(a.isAbgelehnt());

            if (a.getCustomPoints() > 0 && a.isAnerkannt()) {
                r.lbPunktzahl.setText("" + a.getCustomPoints());
            }

            if (null != a.getCustomName() && a.getCustomName().length() > 0) {
                r.setSonstigeHaerteTextfieldValue(a.getCustomName());
            }

            if (a.isAbgelehnt()) {
                HaerteAblehnungsgrund grund = HaerteAblehnungsgrund.getHaertegrundByID(a.getAblehnungsID());
                r.comboBoxAblehnungsGruende.setSelectedItem(grund);
            }
        }

        this.initrunning = false;

    }

    private List<AntragHaerte> createDefaultHaerteList(List<AntragHaerte> haerteListe) {
        haerteListe.clear();

        for (RowPanel r : listRowPanel) {
            AntragHaerte ah = new AntragHaerte();
            ah.setAngegeben(false);
            ah.setAnerkannt(false);
            ah.setAbgelehnt(false);
            ah.setAntragID(berechnungControl.getBerechnungModel().getAntrag().getAntragID());
            ah.setHaertegrund(Haertegrund.getHaertegrundByID(r.getID()));
            haerteListe.add(ah);
        }

        return haerteListe;
    }


    /**
     * Erstellt Liste mit den angegebenen Härten für den Antrag aus der View heraus
     **/
    public void createNewHaertenliste() {


        if (!initrunning) {
            List<AntragHaerte> haertenListe = berechnungControl.getBerechnungModel().getAntrag().getHaerteListe();

            for (RowPanel r : listRowPanel) {
                AntragHaerte a = haertenListe.get(listRowPanel.indexOf(r));
                a.setAngegeben(r.cbAngegeben.isSelected());
                a.setAnerkannt(r.cbAnerkannt.isSelected());
                a.setAbgelehnt(r.cbAbgelehnt.isSelected());

                if (a.isAbgelehnt())
                    a.setAblehnungsID(((HaerteAblehnungsgrund) r.comboBoxAblehnungsGruende.getSelectedItem()).getID());

                if ((r.id >= Haertegrund.SONSTIGE.getID())) {
                    if (a.isAngegeben() && (!r.getSonstigeHaerteTextFieldText().trim().equalsIgnoreCase(STANDARDTEXT))) {
                        a.setCustomName(r.getSonstigeHaerteTextFieldText().trim());
                    }
                    if (a.isAnerkannt()) {
                        try {
                            a.setCustomPoints(Integer.parseInt(r.lbPunktzahl.getText()));
                        } catch (NumberFormatException nfe) {
                            a.setCustomPoints(0);
                        }
                    }
                }
            }
        }
    }

    /**
     * Wenn mehr als 0 Kinder, dann sollte die Härte automatisch gesetzt werden
     *
     * @param state true or false
     */
    public void setKindAngegeben(boolean state) {
        setHaerteAngegeben(Haertegrund.KIND.getID(), state);
    }

    /**
     * Setzt state wo HaerteID gefunden
     *
     * @param haerteid HaerteID
     * @param state    true | false
     */
    public void setHaerteAngegeben(int haerteid, boolean state) {
        for (RowPanel r : listRowPanel) {
            if (r.getID() == haerteid) {
                if (!state && (r.cbAnerkannt.isSelected() || r.cbAngegeben.isSelected())) {
                    //do nothing
                } else {
                    r.cbAnerkannt.setSelected(state);
                    r.cbAngegeben.setSelected(state);
                }
                break;
            }
        }
    }


    /**
     * Härte Med. Versorgung wird automatisch gesetzt, wenn Kosten bestimmte Höhe erreichen
     *
     * @param state Härte setzen? ja/nein
     */
    public void setMedKosten(boolean state) {
        setHaerteAngegeben(Haertegrund.MEDVERSORGUNG.getID(), state);
    }


    /**
     * Setzt Punktsumme für anerkannte Härten im Model
     *
     * TODO ebenfalls besser über observer/observable
     */
    public void setPunkteToModel() {

        // Summe der Punkte für anerkannte Härten
        int punkteHaerten = 0;

        // durch Liste mit RowPanel durchgehen und wenn anerkannt zur Summe addieren
        for (RowPanel r : listRowPanel) {

            if (r.isAnerkannt()) {
                if (r.getID() >= Haertegrund.SONSTIGE.getID()) {
                    punkteHaerten += r.getTextBoxValue();
                } else {
                    punkteHaerten += Berechnung.HAERTE_PUNKTZAHL;
                }
            }

        }

        createNewHaertenliste();

		//Punktsumme zum Model übertragen
		berechnungControl.getBerechnungModel().setPunkteHaerten(punkteHaerten);

        berechnungControl.setSaveStatus(true);

    }


    /**
     * Alle Härten zurücksetzen (die Selektionen aufheben)
     */
    public void reset() {

        for (RowPanel r : listRowPanel) {
            r.cbAngegeben.setSelected(false);
            r.cbAnerkannt.setSelected(false);
            r.cbAbgelehnt.setSelected(false);
        }


    }

    private void showBesondereHaertePanel(int index, boolean show) {
        BesondereHaertePanel bp = besondereHaertePanelList.get(index - Haertegrund.values().length);
        bp.showPanel(show);
    }

    @Override
    public void update(Observable o, Object arg) {
        BerechnungModel model = (BerechnungModel) o;
        setKindAngegeben(model.isHaerteKind());
        setMedKosten(model.isHaerteMedkosten());
    }

    /**
     * Klasse der einzelne Panel für jeweils eine Härte
     */
    class RowPanel
            extends SForm {

        private final WiderDropDownCombo comboBoxAblehnungsGruende;
        int id;
        JCheckBox cbAngegeben, cbAnerkannt, cbAbgelehnt;
        private Component component;
        private JTextField lbPunktzahl;


        /**
         * Erstellt eine neue RowPanel
         *
         * @param comp Komponente des RowPanel (Textfeld oder Label)
         * @param id   ID des RowPanel
         */
        public RowPanel(Component comp, final int id) {

            this.component = comp;
            this.id = id;

            this.setBackground(Color.WHITE);

            component.setPreferredSize(new Dimension(220, 24));
            component.setMinimumSize(component.getPreferredSize());

            if (component instanceof JTextField) {
                final JTextField tf = (JTextField) component;
                // maximale Zeichenanzahl für Textfelder
                ((AbstractDocument) tf.getDocument()).setDocumentFilter(new DocumentSizeFilter(50, DocumentSizeFilter.ANYTEXT_PATTERN));

                tf.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent arg0) {
                        tf.selectAll();
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        setPunkteToModel();
                    }
                });

                tf.setText(STANDARDTEXT);
            }


            cbAngegeben = new JCheckBox();
            cbAnerkannt = new JCheckBox();
            cbAbgelehnt = new JCheckBox();

            cbAngegeben.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent itemEvent) {
                    setPunkteToModel();
                    if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                        component.setFont(component.getFont().deriveFont(Font.BOLD));
                        if (id >= Haertegrund.SONSTIGE.getID()) {
                            showBesondereHaertePanel(id, true);
                        }
                    } else {
                        component.setFont(component.getFont().deriveFont(Font.PLAIN));
                        if (id >= Haertegrund.SONSTIGE.getID()) {
                            showBesondereHaertePanel(id, false);
                        }

                    }
                }
            });


            cbAnerkannt.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent itemEvent) {
                    if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                        cbAbgelehnt.setSelected(false);
                        lbPunktzahl.setText("" + Berechnung.HAERTE_PUNKTZAHL);
                        setPunkteToModel();
                        if (id >= Haertegrund.SONSTIGE.getID()) {
                            lbPunktzahl.setEditable(true);
                        }
                    } else {
                        lbPunktzahl.setText("");
                        setPunkteToModel();
                    }
                }
            });

            HaerteAblehnungsgrund[] ausgewaehlteGruende = null;

            if (id >= Haertegrund.SONSTIGE.getID()) {
                ausgewaehlteGruende = new HaerteAblehnungsgrund[]{HaerteAblehnungsgrund.A, HaerteAblehnungsgrund.B, HaerteAblehnungsgrund.C};
            } else if (id == Haertegrund.BEH_CHRONKRANK.getID() || id == Haertegrund.PFLEGE.getID() || id == Haertegrund.ALLEINERZIEHEND.getID()) {
                ausgewaehlteGruende = new HaerteAblehnungsgrund[]{HaerteAblehnungsgrund.D, HaerteAblehnungsgrund.E, HaerteAblehnungsgrund.J};
            } else if (id == Haertegrund.KIND.getID()) {
                ausgewaehlteGruende = new HaerteAblehnungsgrund[]{HaerteAblehnungsgrund.E, HaerteAblehnungsgrund.F, HaerteAblehnungsgrund.S};
            } else if (id == Haertegrund.MEDVERSORGUNG.getID()) {
                ausgewaehlteGruende = new HaerteAblehnungsgrund[]{HaerteAblehnungsgrund.E, HaerteAblehnungsgrund.G, HaerteAblehnungsgrund.R};
            } else if (id == Haertegrund.PRAKTIKUM.getID()) {
                ausgewaehlteGruende = new HaerteAblehnungsgrund[]{HaerteAblehnungsgrund.H, HaerteAblehnungsgrund.I, HaerteAblehnungsgrund.J, HaerteAblehnungsgrund.E};
            } else if (id == Haertegrund.ARBEITSERLAUBNIS.getID()) {
                ausgewaehlteGruende = new HaerteAblehnungsgrund[]{HaerteAblehnungsgrund.K, HaerteAblehnungsgrund.E, HaerteAblehnungsgrund.D};
            } else if (id == Haertegrund.STUDIENABSCHLUSS.getID()) {
                ausgewaehlteGruende = new HaerteAblehnungsgrund[]{HaerteAblehnungsgrund.L, HaerteAblehnungsgrund.M, HaerteAblehnungsgrund.E};
            } else if (id == Haertegrund.SCHWANGERSCHAFT.getID()) {
                ausgewaehlteGruende = new HaerteAblehnungsgrund[]{HaerteAblehnungsgrund.Q, HaerteAblehnungsgrund.N, HaerteAblehnungsgrund.P};
            } else if (id == Haertegrund.SGB.getID()) {
                ausgewaehlteGruende = new HaerteAblehnungsgrund[]{HaerteAblehnungsgrund.O, HaerteAblehnungsgrund.E, HaerteAblehnungsgrund.J};
            }

            comboBoxAblehnungsGruende = new WiderDropDownCombo(ausgewaehlteGruende);
            comboBoxAblehnungsGruende.setEnabled(false);
            comboBoxAblehnungsGruende.setWide(true);
            comboBoxAblehnungsGruende.setSize(20, 10);
            comboBoxAblehnungsGruende.setPrototypeDisplayValue("XXXXXXXX");
            comboBoxAblehnungsGruende.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    setPunkteToModel();
                }
            });


            cbAbgelehnt.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent itemEvent) {
                    if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                        cbAnerkannt.setSelected(false);
                        lbPunktzahl.setText("");
                        setPunkteToModel();
                        comboBoxAblehnungsGruende.setEnabled(true);

                    } else {
                        setPunkteToModel();
                        comboBoxAblehnungsGruende.setEnabled(false);
                    }
                }
            });

            lbPunktzahl = new JTextField(" ");

            lbPunktzahl.setHorizontalAlignment(JLabel.CENTER);
            lbPunktzahl.setPreferredSize(new Dimension(40, 24));
            lbPunktzahl.setMinimumSize(lbPunktzahl.getPreferredSize());

            lbPunktzahl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            lbPunktzahl.setEditable(false);

            lbPunktzahl.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    if (id >= Haertegrund.SONSTIGE.getID())
                        setPunkteToModel();
                }
            });

            lbPunktzahl.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (id >= Haertegrund.SONSTIGE.getID())
                        setPunkteToModel();
                }
            });

            add(component, 0, 0, 1, 1, 1.0, 1.0, 2, 18, new Insets(0, 2, 0, 2));
            add(cbAngegeben, 1, 0, 1, 1, 1.0, 1.0, 0, 18, new Insets(0, 30, 0, 30));
            add(cbAnerkannt, 2, 0, 1, 1, 1.0, 1.0, 0, 18, new Insets(0, 30, 0, 30));
            add(cbAbgelehnt, 3, 0, 1, 1, 1.0, 1.0, 0, 18, new Insets(0, 30, 0, 30));
            add(comboBoxAblehnungsGruende, 4, 0, 1, 1, 1.0, 1.0, 0, 18, new Insets(0, 0, 0, 0));
            add(lbPunktzahl, 5, 0, 1, 1, 1.0, 1.0, 0, 18, new Insets(0, 30, 0, 2));


        }

        /**
         * Liefert die ID des RowPanels
         *
         * @return ID
         */
        public int getID() {
            return id;
        }


        /**
         * Zeigt an, ob Härte angegeben ist
         *
         * @return angegebne? ja/nein
         */
        public boolean isAngegeben() {
            return cbAngegeben.isSelected();
        }

        /**
         * Zeigt an, ob Härte anerkannt ist
         *
         * @return anerkannt? ja/nein
         */
        public boolean isAnerkannt() {
            return cbAnerkannt.isSelected();
        }

        /**
         * Zeigt an, ob Härte abgelehnt ist
         *
         * @return abgelehnt? ja/nein
         */
        public boolean isAbgelehnt() {
            return cbAbgelehnt.isSelected();
        }

        /**
         * Manche Punkte (z.B. sonstige Härten) sind editierbar und können ausgelesen werden
         *
         * @return Integer-Wert der eingegebenen Punktzahl für die Härte, 0 wenn Parse-Fehler
         */
        public int getTextBoxValue() {
            try {
                return Integer.parseInt(lbPunktzahl.getText());
            } catch (NumberFormatException nfe) {
                return 0;
            }

        }

        /**
         * Text der Sonstigen Härte
         *
         * @return eingegebener Text bei Sonstige Härte oder leerer String
         */
        public String getSonstigeHaerteTextFieldText() {
            if (component instanceof JTextField) {
                return ((JTextField) component).getText();
            } else {
                return "";
            }


        }


        /**
         * Setzt Wert des Sonstige-Härte-Textes
         *
         * @param customName Text
         */
        public void setSonstigeHaerteTextfieldValue(String customName) {
            if (component instanceof JTextField) {
                ((JTextField) component).setText(customName);
            }
        }
    }


}
