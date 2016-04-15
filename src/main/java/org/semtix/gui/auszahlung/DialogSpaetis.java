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

package org.semtix.gui.auszahlung;

import org.semtix.config.SemesterConf;
import org.semtix.db.DBHandlerAntrag;
import org.semtix.db.dao.Antrag;
import org.semtix.db.dao.Semester;
import org.semtix.gui.MainControl;
import org.semtix.shared.actions.ActionCloseDialog;
import org.semtix.shared.daten.enums.AntragStatus;
import org.semtix.shared.print.OdtTemplate;
import org.semtix.shared.tablemodels.TableModelSpaetis;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Dialog zum Menüpunkt "Datenabgleich anzeigen" (zeigt eine Liste der Personen an, die 
 * im aktuellen Semester einen Antrag gestellt haben)
 */
@SuppressWarnings("serial")
public class DialogSpaetis extends JDialog {

	private MainControl mainControl;

	private JPanel buttonPanel;

	private JLabel labelTitle;

	private TableModelSpaetis tableModel;

	private JTable tabelle;

	private JButton printButton,closeButton;

	private Semester semester;

	private List<Antrag> alleOffenenGenehmigten;

	private List<Integer> selection;

	/**
	 * Erstellt einen neuen Dialog "Spätis Verwalten..." mit dem man basierend auf dem aktuell ausgewählten Semester die Späts verwalten kann
	 * @param mainControl MainControl
	 */
	public DialogSpaetis(MainControl mainControl) {

		this.mainControl = mainControl;

		this.semester = SemesterConf.getSemester();

		this.selection = new ArrayList<Integer>();

		setTitle("Spätis verwalten");


		DBHandlerAntrag dbhAntrag = new DBHandlerAntrag();

		alleOffenenGenehmigten = new ArrayList<Antrag>();

		List<Antrag> antraege = dbhAntrag.getAntragListeSemester(semester.getSemesterID());

		if (null != antraege) {
			if (antraege.size() > 0) {
				for (Antrag a : antraege) {
					if (a.getAntragStatus() == AntragStatus.GENEHMIGT && (! a.isAuszahlung())) {
						alleOffenenGenehmigten.add(a);
					}
				}
			}
		}


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


		labelTitle = new JLabel("<html>Liste der Personen aus dem aktuellen Semester, deren Antrag bewilligt aber noch nicht ausgezahlt wurde. </html>");

		labelTitle.setFont(labelTitle.getFont().deriveFont(Font.BOLD));

		Border empty = BorderFactory.createEmptyBorder(10, 10, 10, 10);

		labelTitle.setBorder(empty);

		if (alleOffenenGenehmigten.size() > 0) {

			tableModel = new TableModelSpaetis(this.semester);

			tabelle = new JTable(tableModel);

			// nur einzelne Tabellenzeilen können selektiert werden
			tabelle.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

			tabelle.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						//Bei Doppel-Klick: Öffne Person im Hintergrund
						mainControl.addTab(tableModel.getPerson(tabelle.convertRowIndexToModel(tabelle.getSelectedRow())).getPersonID());
					}
				}
			});
			// die Spalten der Tabelle können nicht vertauscht werden
			tabelle.getTableHeader().setReorderingAllowed(false);

			// die Spaltenbreite kann nicht verändert/verschoben werden
			tabelle.getTableHeader().setResizingAllowed(true);

			tabelle.setRowSelectionAllowed(true);

			ListSelectionModel listSelectionModel = tabelle.getSelectionModel();
			listSelectionModel.addListSelectionListener(new SharedListSelectionHandler());
			tabelle.setSelectionModel(listSelectionModel);


			JScrollPane scroller = new JScrollPane(tabelle);

			scroller.setPreferredSize(new Dimension(450, 220));
			scroller.setMinimumSize(scroller.getPreferredSize());

            printButton = new JButton("AZAs Drucken");
            printButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					List<Antrag> selectedAntraege = new ArrayList<Antrag>();

					if (selection.size() > 0) {

						for (Integer i : selection) {

							selectedAntraege.add(alleOffenenGenehmigten.get(i));
						}

					} else {

						selectedAntraege = alleOffenenGenehmigten;

					}

					DBHandlerAntrag dbHandlerAntrag = new DBHandlerAntrag();
					Semester semester = SemesterConf.getSemester();
					if (
							null == semester.getBeitragTicket() ||
									null == semester.getSozialfonds() ||
									null == semester.getPunktWert()) {

						JOptionPane.showMessageDialog(null, "Semester wurde noch nicht gerechnet.", "Fehler", JOptionPane.WARNING_MESSAGE);

					} else {

						int punkteVollzuschuss = semester.getPunkteVoll();
						BigDecimal vollzuschuss = semester.getBeitragTicket().add(semester.getSozialfonds());
						for (Antrag a : selectedAntraege) {
							a.setAuszahlung(true);
							int punkte = a.getPunkteEinkommen() + a.getPunkteHaerte();
							if (punkte >= punkteVollzuschuss)
								a.setErstattung(vollzuschuss);
							else
								a.setErstattung(semester.getPunktWert().multiply(new BigDecimal(punkte)));

							dbHandlerAntrag.updateAntrag(a);
						}

						try {
							new OdtTemplate().printAZAs(selectedAntraege, 2);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});

			JButton printTableButton = new JButton("Tabelle Drucken");
			printTableButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						tabelle.print();
					} catch (PrinterException e1) {
						JOptionPane.showMessageDialog(buttonPanel, "Konnte leider nicht drucken.");
					}
				}
			});

			buttonPanel.add(printTableButton);
			buttonPanel.add(printButton);


			this.add(labelTitle,BorderLayout.NORTH);
			this.add(scroller,BorderLayout.CENTER);







		} else {
			JLabel keineSpaetis = new JLabel("<html><b>Keine Spätis für das aktuelle Semester gefunden.</b></html>");

			keineSpaetis.setForeground(Color.RED);

			keineSpaetis.setHorizontalAlignment(SwingConstants.CENTER);

			keineSpaetis.setSize(200,20);

			this.add(keineSpaetis, BorderLayout.CENTER);
		}


		buttonPanel.add(closeButton);


		this.add(buttonPanel,BorderLayout.SOUTH);
		// this.pack();
		setVisible(true);

	}


	/**
	 * füllt selection bei jeder Selection neu mit Indizes
	 */
	class SharedListSelectionHandler implements ListSelectionListener {
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				//reinitilisieren
				selection = new ArrayList<Integer>();

				if (lsm.isSelectionEmpty()) {
					//nothing selected
				} else {
					// Find out which indexes are selected.
					int minIndex = lsm.getMinSelectionIndex();
					int maxIndex = lsm.getMaxSelectionIndex();
					for (int i = minIndex; i <= maxIndex; i++) {
						if (lsm.isSelectedIndex(i)) {
							selection.add(i);
						}
					}

				}
			}
	}


}

