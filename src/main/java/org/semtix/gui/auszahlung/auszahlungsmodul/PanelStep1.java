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

package org.semtix.gui.auszahlung.auszahlungsmodul;


import org.semtix.config.SemesterConf;
import org.semtix.config.Settings;
import org.semtix.config.UniConf;
import org.semtix.db.DBHandlerDatenabgleich;
import org.semtix.db.DBHandlerPerson;
import org.semtix.shared.tablemodels.TableModelPersonenListe;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 * Datenabgleich mit Immatrikulationsbüro
 * 
 * Dazu gehört das erzeugen einer Excel-Datei die dem Immatrikulationsbüro zu Korrekturzwecken vorgelegt wird.
 * 
 * Ebenfalls kann hiermit die vom Immatrikulationsbüro korrigierte Datei eingelesen werden.
 * 
 */
@SuppressWarnings("serial")
class PanelStep1
extends GenericPanelStep {


    private JPanel buttonPanel;

    private TableModelPersonenListe tableModel;
    
    private JTable tabelle;

    private JButton saveButton, openButton;


    /**
     * {see GenericPanelStep}
     *
     * @param bModel     ModelAuszahlungsmodul
     * @param titel      titel
     * @param untertitel untertitel
     */
    protected PanelStep1(ModelAuszahlungsmodul bModel, String titel, String untertitel) {
        super(bModel, titel, untertitel);
    }

    @Override
    protected void additionalInitStuff() {
        buttonPanel = new JPanel();

        DBHandlerPerson dbHandler = new DBHandlerPerson();

        tableModel = new TableModelPersonenListe(dbHandler.getListeAntragsteller(SemesterConf.getSemester()),false);

        tabelle = new JTable(tableModel);

        // nur einzelne Tabellenzeilen können selektiert werden
        tabelle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // die Spalten der Tabelle können nicht vertauscht werden
        tabelle.getTableHeader().setReorderingAllowed(false);

        // die Spaltenbreite kann nicht verändert/verschoben werden
        tabelle.getTableHeader().setResizingAllowed(false);

        tabelle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    DialogWizard.showPerson(tableModel.getPersonByRow(tabelle.getSelectedRow()));
                }
            }
        });

        for (String s : tableModel.getColumnsIShouldHide(new String[]{"Vorname", "Name", "Matrikelnummer"})) {
            tabelle.removeColumn(tabelle.getColumn(s));
        }

        JScrollPane scroller = new JScrollPane(tabelle);
        scroller.setPreferredSize(new Dimension(450, 255));
        scroller.setMinimumSize(scroller.getPreferredSize());
        scroller.getVerticalScrollBar().setUnitIncrement(Settings.SCROLL_UNIT);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        class OpenListe implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser();
                c.setAcceptAllFileFilterUsed(true);
                c.addChoosableFileFilter(new FileNameExtensionFilter("Excel-Datei", "xls", "xlsx"));


                int rVal = c.showOpenDialog(PanelStep1.this);
                if (rVal == JFileChooser.APPROVE_OPTION) {


                    logTextArea.logText("(1) Datei importiert");

                    Datenabgleich dad = new Datenabgleich();
                    dad.einlesen(c.getCurrentDirectory().toString() + File.separator + c.getSelectedFile().getName());

                }
                if (rVal == JFileChooser.CANCEL_OPTION) {

                }
            }
        }

        //Save to CSV
        class SaveListe implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser();
                c.setAcceptAllFileFilterUsed(false);
                FileFilter filter = new FileNameExtensionFilter("CSV: ISO-8859-1", "csv");
                // Filter wird unserem JFileChooser hinzugefügt
                c.addChoosableFileFilter(filter);


                // Demonstrate "Save" dialog:
                int rVal = c.showSaveDialog(PanelStep1.this);

                if (rVal == JFileChooser.APPROVE_OPTION) {


                    Datenabgleich dad = new Datenabgleich();
                    String dateiname = c.getSelectedFile().getName();
                    if (!dateiname.endsWith(".csv"))
                        dateiname += ".csv";

                    String ausgabe = c.getCurrentDirectory().toString() + File.separator + dateiname;

                    int fehler = 0;

                    try {

                        fehler = dad.ausgeben(ausgabe);

                        logTextArea.logText("(1) Datei exportiert");

                    } catch (FileNotFoundException e1) {
                        JOptionPane.showMessageDialog(null, "Fehler: Konnte die Datei unter diesem Pfad nicht speichern. \n " + e1.getMessage());
                    } catch (UnsupportedEncodingException e2) {
                        JOptionPane.showMessageDialog(null, "Fehler wegen Windows-Datei-Encoding. \n" + e2.getMessage());
                    }

                    if (fehler > 0) {
                        JOptionPane.showMessageDialog(null, "Ausgabe erfolgreich: bitte trotzdem Personentabelle in der Datenbank säubern. Enthält " + fehler + " mangelhafte Datensätze.");
                    }


                }
                if (rVal == JFileChooser.CANCEL_OPTION) {

                }
            }
        }

        saveButton = new JButton("Daten Speichern");
        saveButton.addActionListener(new SaveListe());

        openButton = new JButton("Daten Lesen");
        openButton.addActionListener(new OpenListe());


        JButton printButton = new JButton("Tabelle Drucken");
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    tabelle.print();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Drucken hat leider nicht geklappt... " + e.getMessage());
                }
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(printButton);
        buttonPanel.add(openButton);

        formular.add(scroller, 0, 2, 5, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 0, 0));
        formular.add(buttonPanel,0, 3, 5, 1, 0.0, 0.0, 0, 18, new Insets(0, 0, 0, 0));
        formular.add(new JPanel(), 0, 4, 5, 2, 1.0, 1.0, 2, 18, new Insets(0, 0, 0, 0));

    }


}
