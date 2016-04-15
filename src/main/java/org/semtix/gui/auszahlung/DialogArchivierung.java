/*

  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
         Semesterticketbüro der Humboldt-Universität Berlin

  Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package org.semtix.gui.auszahlung;

import org.semtix.db.DBHandlerPerson;
import org.semtix.db.dao.Person;
import org.semtix.gui.MainControl;
import org.semtix.shared.actions.ActionCloseDialog;
import org.semtix.shared.tablemodels.TableModelPersonenListe;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.util.List;

/**
 * Dialog zur Anzeige der Personen, die seit 5 oder mehr Semestern keinen Antrag mehr gestellt haben.
 *
 */
@SuppressWarnings("serial")
public class DialogArchivierung
extends JDialog {

    private final MainControl mainControl;
	org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DialogArchivierung.class);
	private JSpinner spinner;
    private JTable tabelle;
	private List<Person> personen;
	private TableModelPersonenListe tableModel;
	private static JLabel labelTitle1;
	private static String titleString1 = "Personen seit ";

	/**
	 * Erstellt einen neuen Dialog
	 * @param mainControl MainControl object
	 */
    public DialogArchivierung(MainControl mainControl) {

        this.mainControl = mainControl;

		setTitle("Archivierung");

		buildDialog();



	}

	public static void setAnzahl(int anzahl) {
		labelTitle1.setText(anzahl + " " + titleString1);
	}


	/**
	 * Fügt Komponenten zum Dialog hinzu
	 */
	public void buildDialog() {
		
		setSize(600, 500);
		
		// Frame auf dem Bildschirm zentrieren
		setLocationRelativeTo(getParent());
		
		setModal(true);
		setResizable(false);
		
		setLayout(new BorderLayout());

		JPanel buttonPanel = new JPanel();
						
		Border empty = BorderFactory.createEmptyBorder(10, 10, 10, 10);

        try {
            this.personen = new DBHandlerPerson().getListePersonenSeitSemesternOhneAntrag(5);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Kein Semester eingestellt oder keine Personen vorhanden", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
        this.tableModel = new TableModelPersonenListe(personen,false);

		this.tabelle = new JTable(tableModel);
		
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
                    //Bei Doppel-Klick: Öffne Person im Hintergrund
                    mainControl.addTab(tableModel.getPerson(tabelle.convertRowIndexToModel(tabelle.getSelectedRow())).getPersonID());
                }
            }
        });


		JScrollPane scroller = new JScrollPane(tabelle);
		
		JButton closeButton = new JButton(new ActionCloseDialog(this, "Beenden"));
		
		buttonPanel.add(closeButton);

		JPanel northPanel = new JPanel();
		spinner = new JSpinner(new SpinnerNumberModel(5, 0, 99, 1));
		JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor)spinner.getEditor();
		spinnerEditor.getTextField().setEditable(false);
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent changeEvent) {
				int value = Integer.valueOf(spinner.getValue().toString());
				personen=new DBHandlerPerson().getListePersonenSeitSemesternOhneAntrag(value);
				tableModel.updatePersonenListe(personen);
				tableModel.fireTableDataChanged();
			}
		});

		String titleString2 = " Semester(n) ohne Antrag (0=noch nie)";
//				"<br>" + "" +
//                "Universität: " + UniConf.aktuelleUni + "<br>" +
//				"Aktuelles Semester: " + SemesterConf.getSemester().getSemesterBezeichnung() + "</html>";

		labelTitle1 = new JLabel(titleString1);
		JLabel labelTitle2 = new JLabel(titleString2);

		northPanel.add(labelTitle1);
		northPanel.add(spinner);
		northPanel.add(labelTitle2);
//
//		labelTitle1.setFont(labelTitle1.getFont().deriveFont(Font.BOLD));
//		labelTitle2.setFont(labelTitle1.getFont().deriveFont(Font.BOLD));
//


		JButton archivieren = new JButton("Archivieren");
		archivieren.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
                DBHandlerPerson dbHandlerPerson = new DBHandlerPerson();
                //Wenn eine Person selektiert, dann nur diese archivieren:
                if (tabelle.getSelectedRow() != -1) {
                    Person p = tableModel.getPerson(tabelle.convertRowIndexToModel(tabelle.getSelectedRow()));
                    p.setArchiviert(true);
                    dbHandlerPerson.updatePerson(p);
                } else {
                    int returnvalue = JOptionPane.showConfirmDialog(null, "Wirklich alle Personen archivieren?");
                    if (returnvalue == JOptionPane.YES_OPTION) {
                        for (Person p : personen) {
                            p.setArchiviert(true);
                            dbHandlerPerson.updatePerson(p);
                        }
                    }
                }
            }
		});

		JButton drucken = new JButton("Liste drucken");
		drucken.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					tabelle.print();
				} catch (PrinterException e) {
					logger.error("Fehler beim Drucken von Archivierungsliste");
				}
			}
		});

		buttonPanel.add(drucken);
		buttonPanel.add(archivieren);



		add(northPanel, BorderLayout.NORTH);
		add(scroller, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		//this.pack();
		setVisible(true);

	}





}
