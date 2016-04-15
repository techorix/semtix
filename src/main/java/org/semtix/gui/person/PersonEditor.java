/*
 * Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 *        Semesterticketbüro der Humboldt-Universität Berlin
 *
 * Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
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

package org.semtix.gui.person;

import org.semtix.config.Settings;
import org.semtix.config.UniConf;
import org.semtix.db.DBHandlerPerson;
import org.semtix.shared.actions.ActionCloseDialog;
import org.semtix.shared.tablemodels.TableModelPersonenListe;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;

/**
 * Klasse mit PersonenTabelle zum Editieren und Drucken. Kann als Demo später noch irgendwo nützlich sein.
 * Created by MM on 19.04.15.
 */
public class PersonEditor extends JDialog {


    private JPanel buttonPanel;

    private JLabel labelTitle;

    private TableModelPersonenListe tableModel;

    private JTable tabelle;

    private JButton closeButton;


    public PersonEditor() {


        setTitle("Personen en masse editieren");


        buildDialog();

    }


    /**
     * Fügt Komponenten dem Dialog hinzu
     */
    public void buildDialog() {

        setSize(600, 500);

        // Frame auf dem Bildschirm zentrieren
        setLocationRelativeTo(getParent());

        setModal(true);
        setResizable(false);

        setLayout(new BorderLayout());

        buttonPanel = new JPanel();

        closeButton = new JButton(new ActionCloseDialog(this, "Schließen"));


        labelTitle = new JLabel("<html>Liste aller Personen zum direkten Editieren. <I>Experimentelles Feature</i></html>");

        labelTitle.setFont(labelTitle.getFont().deriveFont(Font.BOLD));

        Border empty = BorderFactory.createEmptyBorder(10, 10, 10, 10);

        labelTitle.setBorder(empty);

        final DBHandlerPerson dbHandlerPerson = new DBHandlerPerson();

        tableModel = new TableModelPersonenListe(dbHandlerPerson.getPersonenListe(UniConf.aktuelleUni), true);

        tabelle = new JTable(tableModel);

        // nur einzelne Tabellenzeilen können selektiert werden
        tabelle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // die Spalten der Tabelle können nicht vertauscht werden
        tabelle.getTableHeader().setReorderingAllowed(true);

        // die Spaltenbreite kann nicht verändert/verschoben werden
        tabelle.getTableHeader().setResizingAllowed(true);

        tabelle.setRowSelectionAllowed(true);


        TableColumnModel colModel = tabelle.getColumnModel();
        final JTextField tf = new JTextField();
        tf.setBorder(new EmptyBorder(0, 0, 0, 0));

        tf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dbHandlerPerson.updatePerson(tableModel.getPersonByRow(tabelle.getSelectedRow()));
            }
        });

        for (int i = 0; i < colModel.getColumnCount(); i++) {
            colModel.getColumn(i).setCellEditor(new DefaultCellEditor(tf));
            colModel.getColumn(i).setCellRenderer(new DefaultTableCellRenderer());
        }


        JScrollPane mainScrollPane = new JScrollPane(tabelle);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(Settings.SCROLL_UNIT);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        mainScrollPane.setBorder(BorderFactory.createEmptyBorder());

//        JScrollPane scroller = new JScrollPane(mainScrollPane);
//
//        scroller.setPreferredSize(new Dimension(450, 220));
//        scroller.setMinimumSize(scroller.getPreferredSize());
//        scroller.getVerticalScrollBar().setUnitIncrement(Settings.SCROLL_UNIT);
//        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        scroller.setBorder(empty);


        JButton printTableButton = new JButton("Tabelle Drucken");
        printTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tabelle.print();
                } catch (PrinterException e1) {
                    JOptionPane.showMessageDialog(null, "Konnte leider nicht drucken.", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonPanel.add(printTableButton);


        this.add(labelTitle, BorderLayout.NORTH);
        this.add(mainScrollPane, BorderLayout.CENTER);


        buttonPanel.add(closeButton);


        this.add(buttonPanel, BorderLayout.SOUTH);
        // this.pack();
        setVisible(true);

    }

}


