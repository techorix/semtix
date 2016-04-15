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

package org.semtix.gui.statistik;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import org.semtix.shared.actions.ActionCloseDialog;
import org.semtix.shared.elements.SForm;
import org.semtix.shared.elements.VerticalTableHeaderCellRenderer;
import org.semtix.shared.tablemodels.TableModelStatistikArbeit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;

/**
 * Dialog der eine Datei erstellt die die Statistiken nach Datum sortiert zu den Arbeitsvorgängen enthält
 * <br>
 * Created by MM on 20.03.15.
 */
public class DialogStatistikArbeit extends JDialog {


    private TableModelStatistikArbeit tableModel = new TableModelStatistikArbeit();


    public DialogStatistikArbeit() {
        setTitle("Statistik Arbeitsvorgänge");
        setSize(500, 600);

        // Frame auf dem Bildschirm zentrieren
        setLocationRelativeTo(getParent());
        setModal(true);
        setResizable(false);


        //Panel Structure:
        JPanel mainPanel = new JPanel();
        //containing:
        SForm titelPanel = new SForm();
        JScrollPane statistik;
        JPanel buttonPanel = new JPanel();

        //Panels' Layout
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setLayout(new BorderLayout(15, 15));



        //Title Panel
//        JLabel titel = new JLabel("Arbeit");
//        titel.setFont(titel.getFont().deriveFont(Font.BOLD));

        final JDatePickerImpl datePicker = createDatePicker("Datum von");
        datePicker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setDatumVon((Date) datePicker.getModel().getValue());
            }
        });


        final JDatePickerImpl datePicker2 = createDatePicker("Datum bis");
        datePicker2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setDatumBis((Date) datePicker2.getModel().getValue());
            }
        });

//        titelPanel.add(titel, 0, 0, 1, 1, 0.0, 0.0, 0, GridBagConstraints.CENTER, new Insets(5, 5, 5, 5));
        titelPanel.add(new JLabel("Vom"), 0, 0, 1, 1, 0.0, 0.0, 0, GridBagConstraints.EAST, new Insets(5, 5, 5, 5));
        titelPanel.add(datePicker, 1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST, new Insets(5, 5, 5, 5));
        titelPanel.add(new JLabel("Bis zum "), 2, 0, 1, 1, 0.0, 0.0, 0, GridBagConstraints.EAST, new Insets(5, 5, 5, 5));
        titelPanel.add(datePicker2, 3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST, new Insets(5, 5, 5, 5));



        //Statistik

        final JTable table = new JTable(tableModel);

        table.getTableHeader().setMaximumSize(new Dimension(new Double(table.getSize().getWidth()).intValue(), 60));
        table.getColumnModel().getColumn(0).setMinWidth(90);

        TableCellRenderer headerRenderer = new VerticalTableHeaderCellRenderer(false);
        TableCellRenderer headerRendererGrey = new VerticalTableHeaderCellRenderer(true);

        Enumeration<TableColumn> columns = table.getColumnModel().getColumns();
        boolean everyother = false;
        while (columns.hasMoreElements()) {
            if (everyother)
                columns.nextElement().setHeaderRenderer(headerRenderer);
            else
                columns.nextElement().setHeaderRenderer(headerRendererGrey);

            everyother = !everyother;
        }


        statistik = new JScrollPane(table);



        //ButtonPanel
        JButton dateiErstellenButton = new JButton(new SaveTableToCSV("Speichern", tableModel));
        JButton printButton = new JButton("Drucken");
        JButton exitButton = new JButton(new ActionCloseDialog(this, "Abbrechen"));

        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    table.print();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Konnte leider nicht drucken. \n \n" + e.getMessage());
                }
            }
        });

        buttonPanel.add(dateiErstellenButton);
        buttonPanel.add(printButton);
        buttonPanel.add(exitButton);
        buttonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "actionEscapeDialog");
        buttonPanel.getActionMap().put("actionEscapeDialog", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }

        });

        mainPanel.add(titelPanel, BorderLayout.NORTH);
        mainPanel.add(statistik, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setVisible(true);  //nötig?

    }


    /**
     * Hilfsmethode baut ein DatePicker-Object zusammen das graphisch angepasst ist und mit dem heutigen Datum initialisiert wird.
     **
     * @param toolTipText ToolTipText
     * @return JDatePickerImpl object
     */
    private JDatePickerImpl createDatePicker(String toolTipText) {

        //erstelle DatePicker mit heutigem Datum:
        GregorianCalendar calendar = new GregorianCalendar();
        final UtilDateModel model = new UtilDateModel();
        model.setDate(calendar.get(GregorianCalendar.YEAR), calendar.get(GregorianCalendar.MONTH), calendar.get(GregorianCalendar.DAY_OF_MONTH));
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);

        //Farbe des Datepickers ändern
        for (Component c : datePanel.getComponents()) {
            if (null != c)
                if (c instanceof JPanel) {
                    for (Component x : ((JPanel) c).getComponents())
                        if (x.getBackground().equals(SystemColor.activeCaption))
                            x.setBackground(Color.LIGHT_GRAY);
                }
        }

        datePicker.setToolTipText(toolTipText);

        return datePicker;
    }


    class SaveTableToCSV extends
            AbstractAction {

        TableModel tableModel;

        public SaveTableToCSV(String name, TableModel tableModel) {
            putValue(Action.NAME, name);
            putValue(Action.SHORT_DESCRIPTION, name);
            this.tableModel = tableModel;

        }
        public void actionPerformed(ActionEvent e) {
            JFileChooser c = new JFileChooser();
			int rVal = c.showSaveDialog(null);
			if (rVal == JFileChooser.APPROVE_OPTION) {
                String path = c.getCurrentDirectory().toString() + File.separator + c.getSelectedFile().getName();
                try {
                    PrintWriter writer = new PrintWriter(path);
                    String separator = ",";
                    String headerString = "";
                    for (int i = 0; i < tableModel.getColumnCount(); i++) {
                        headerString += tableModel.getColumnName(i) + separator;

                    }
                    writer.print(headerString + "\n");

                    for (int j = 0; j < tableModel.getRowCount(); j++) {
                        StringBuilder line = new StringBuilder();

                        for (int k = 0; k < tableModel.getColumnCount(); k++) {
                            line.append(tableModel.getValueAt(j, k));
                            line.append(separator);
                        }

                        line.append("\n");

                        writer.print(line.toString());
                    }

                    writer.close();

                } catch (FileNotFoundException e1) {
                    JOptionPane.showMessageDialog(null, "Fehler: Konnte die Datei unter diesem Pfad nicht speichern: " + path);
                }
            }

        }
    } //end SaveTableToCSV
}
