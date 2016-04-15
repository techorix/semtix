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

package org.semtix.shared.print;

import org.semtix.db.dao.Antrag;
import org.semtix.shared.tablemodels.TableModelAntragUebersicht;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Dialog zur Anzeige der Personen, die seit 5 oder mehr Semestern keinen Antrag mehr gestellt haben.
 */
@SuppressWarnings("serial")
public class DialogListPrint
		extends JDialog {

	private static JLabel labelTitle1;
	private static String titleString1 = "Anträge nach Antrag-IDS sortiert";
	private final List<Antrag> antragList;
	private final boolean istAZA;
	org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DialogListPrint.class);
	private int howOften;
	private JSpinner spinner;
	private JTable tabelle;
	private TableModelAntragUebersicht tableModel;
	private ArrayList<String> pathsToPrint = new ArrayList<>();
	private DruckThread thread;
	private JTextArea outputText;

	/**
	 * Erstellt einen neuen Dialog
	 * @param antragList List der Anträge
	 * @param istAZA ist Das eine AZA oder ein Bescheid
	 * @param howOften wie Oft
	 */
	public DialogListPrint(List<Antrag> antragList, int howOften, boolean istAZA) {

		this.antragList = antragList;
		this.howOften = howOften;
		this.istAZA = istAZA;

		setTitle("Liste von Anträgen drucken");

		buildDialog();


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

		Border empty = BorderFactory.createEmptyBorder(0, 0, 0, 0);

		this.tableModel = new TableModelAntragUebersicht(antragList);

		this.tabelle = new JTable(tableModel);

		// nur einzelne Tabellenzeilen können selektiert werden
		tabelle.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tabelle.setAutoCreateRowSorter(true);

		// die Spalten der Tabelle können nicht vertauscht werden
		tabelle.getTableHeader().setReorderingAllowed(true);

		// die Spaltenbreite kann nicht verändert/verschoben werden
		tabelle.getTableHeader().setResizingAllowed(true);
		tabelle.setBorder(empty);

		JScrollPane scrollTable = new JScrollPane(tabelle);
		scrollTable.setBorder(empty);

		outputText = new JTextArea();
		outputText.setRows(10);
		outputText.setFont(new Font("Serif", Font.LAYOUT_LEFT_TO_RIGHT, 10));
		outputText.setLineWrap(true);
		outputText.setWrapStyleWord(true);
		outputText.setBackground(new Color(248, 255, 133));
		JScrollPane scrollText = new JScrollPane(outputText);
		scrollText.setBorder(empty);


		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.add(scrollTable);
		contentPanel.add(scrollText);


		final JButton closeButton = new JButton("Beenden");

		closeButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent actionEvent) {
						close();
					}
				});

		buttonPanel.add(closeButton);

		JPanel northPanel = new JPanel();
		spinner = new JSpinner(new SpinnerNumberModel(howOften, 1, 9, 1));
		JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) spinner.getEditor();
		spinnerEditor.getTextField().setEditable(false);

		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent changeEvent) {
				howOften = Integer.valueOf(spinner.getValue().toString());
			}
		});


		labelTitle1 = new JLabel(titleString1);

		northPanel.add(labelTitle1);


		JButton generieren = new JButton("Dokumente generieren");
		generieren.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (thread != null && thread.isAlive()) {
					JOptionPane.showMessageDialog(null, "Es läuft bereits ein aktiver Vorgang.", "Fehler", JOptionPane.ERROR_MESSAGE);
				} else {
					thread = new GenerateThread();
					thread.setDaemon(true);
					thread.start();
				}
			}
		});


		JButton druckStarten = new JButton("Druck Starten");
		druckStarten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (pathsToPrint.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Noch keine Dokumente generiert.", "Fehler", JOptionPane.ERROR_MESSAGE);
				} else {
					if (thread != null && thread.isAlive()) {
						JOptionPane.showMessageDialog(null, "Es läuft bereits ein aktiver Vorgang.", "Fehler", JOptionPane.ERROR_MESSAGE);
					} else {
						thread = new DruckThread();
						if (JOptionPane.showConfirmDialog(null, pathsToPrint.size() + " generierte Anträge " + howOften + "-fach drucken?") == JOptionPane.YES_OPTION) {
							thread.setDaemon(true);
							thread.start();
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
					logger.error("Fehler beim Drucken von Liste");
				}
			}
		});

		JButton stopBtn = new JButton("▮▮ ▶");
		stopBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					thread.toggleStatus();
//					if (thread.isRunning()) {
//						JOptionPane.showMessageDialog(null, "Vorgang läuft wieder.");
//					} else {
//						JOptionPane.showMessageDialog(null, "Laufender Vorgang pausiert.\n Nochmal drücken für Wiederaufnahme.");
//					}
				} catch (Exception ex) {
					System.out.println("Vorgang gestoppt.");
				}
			}
		});
		buttonPanel.add(spinner);
		buttonPanel.add(stopBtn);
		buttonPanel.add(generieren);
		buttonPanel.add(druckStarten);
		buttonPanel.add(drucken);


		add(contentPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		//this.pack();
		setVisible(true);

	}

	private void close() {
		if (thread != null) {
			this.thread.interrupt();
			this.thread = null;
		}
		this.dispose();
	}

	class DruckThread extends Thread {

		boolean mayRun = true;

		public void toggleStatus() {
			mayRun = !mayRun;
		}

		@Override
		public void run() {
			for (int j = 1; j <= howOften; j++) {
				outputText.insert("\n" +
						"\n============================\n" +
						"\n STARTE DURCHGANG " + j + "\n" +
						"\n============================\n" +
						"\n", 0);

				for (String path : pathsToPrint) {
					if (mayRun) {
						try {
							OdtPrinter.print(path, 1);

							outputText.insert(path + " wurde an den Drucker gesendet. \n", 0);

							sleep(2000);
						} catch (InterruptedException | IOException e) {
							e.printStackTrace();
						}
					} else {
						while (!mayRun) {
							try {
								sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	class GenerateThread extends DruckThread {
		@Override
		public void run() {
			pathsToPrint = new ArrayList<>();
			try {
				if (tabelle.getSelectedRow() != -1 && tabelle.getSelectedRows().length > 0) {
					int[] selected = tabelle.getSelectedRows();
					for (int k = 0; k < selected.length; k++) {
						doIt(tableModel.getList().get(selected[k]));
					}
				} else {
					for (Antrag a : antragList) {
						doIt(a);
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			outputText.insert("Habe " + pathsToPrint.size() + " Druckdokumente erstellt.\n\n", 0);
		}

		private void doIt(Antrag a) throws IOException {
			if (mayRun) {
				String path = OdtTemplate.generateOutputFile(a, istAZA, true);
				outputText.insert(path + " generiert.\n", 0);
				pathsToPrint.add(path);
			} else {
				while (!mayRun) {
					try {
						sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
