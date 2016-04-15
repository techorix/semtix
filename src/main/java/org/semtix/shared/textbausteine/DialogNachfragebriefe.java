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

package org.semtix.shared.textbausteine;

import org.semtix.config.UserConf;
import org.semtix.db.DBHandlerConf;
import org.semtix.db.DBHandlerSemester;
import org.semtix.db.dao.Semester;
import org.semtix.db.dao.Textbaustein;
import org.semtix.db.dao.Unterlagen;
import org.semtix.gui.tabs.antrag.AntragControl;
import org.semtix.shared.actions.ActionCloseDialog;
import org.semtix.shared.daten.enums.SemesterArt;
import org.semtix.shared.daten.enums.Textbausteinkategorie;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Dialog zur Anzeige und Bearbeitung der Textbausteine für Nachfragebriefe. Es gibt einen 
 * Konstruktor für die Bearbeitung der Textbausteine sowie einne Konstruktor mit Übergabe
 * eines Unterlagen-Dialogs (Antragformular) zur Weitergabe eines ausgewählten Textbausteins 
 * zurück zum Unterlagen-Dialog.
 *
 */
@SuppressWarnings("serial")
public class DialogNachfragebriefe
extends JDialog
implements ActionListener {

	private TreeModelNachfragebriefe treeModelNachfragebriefe;
	private JTree treeTextbausteine;
	
	private JPanel leftPanel, buttonPanel;
	private JTextArea textArea1, textArea2;
	
	private JLabel nameTextbaustein;
	
	private String mode;
	
	private AntragControl antragControl;
	
	
	
	/**
	 * Erstellt einen Dialog mit Übersicht der Textbausteine für Nachfragebriefe. In diesem 
	 * können Textbausteine neu angelegt, bestehende geändert oder gelöscht werden.
	 */
	public DialogNachfragebriefe() {

		mode = "Neu/Ändern";
		
		buildDialog();

	}
	
	
	
	/**
	 * Erstellt einen Dialog mit Übersicht der Textbausteine für Nachfragebriefe. Es wird 
	 * ein Objekt eines Unterlagendialogs übergeben, in den ein ausgewählter Textbaustein 
	 * übergeben werden kann.
	 *
	 * @param antragControl AntragControl
	 */
	public DialogNachfragebriefe(AntragControl antragControl) {
		
		this.antragControl = antragControl;

		mode = "Textbaustein auswählen";
		
		buildDialog();
	}
	
	
	
	/**
	 * Komponenten zum Dialog hinzufügen.
	 */
	public void buildDialog() {
		
		setTitle("Textbausteine");

		setSize(750, 430);
		
		// Frame auf dem Bildschirm zentrieren
		setLocationRelativeTo(getParent());
		
		setModal(true);
		setResizable(false);
		
		setLayout(new BorderLayout());
		
		treeModelNachfragebriefe = new TreeModelNachfragebriefe();
		treeTextbausteine = new JTree(treeModelNachfragebriefe);

        JScrollPane scrollPaneTree = new JScrollPane(treeTextbausteine);

        // DISCONTIGUOUS war vorher
        treeTextbausteine.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);


		//Zeigt zuletzt selektierten Textbaustein rechts an
		treeTextbausteine.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(treeTextbausteine.getLastSelectedPathComponent());


                if (null != treeTextbausteine.getLastSelectedPathComponent() && node.isLeaf()) {

                    boolean istKategorie = false;
                    //Textbausteinkategorie
                    for (Textbausteinkategorie k : Textbausteinkategorie.values()) {
                        if (node.toString().equals(k.getName())) {
                            istKategorie = true;
                            setTextbaustein("", "", "");
                            break;
                        }
                    }

                    if (!istKategorie) {

                        if (node.getUserObject() instanceof Textbaustein) {
                            Textbaustein tb = (Textbaustein) node.getUserObject();

                            String titel = tb.getBezeichnung() + "\n";

                            String text1 = tb.getText_de();
                            String text2 = tb.getText_en();

                            setTextbaustein(titel, text1, text2);
                        }
                    }


				}

			}
		});

		buttonPanel = new JPanel();
		
		nameTextbaustein = new JLabel();
		
		JLabel titel1 = new JLabel("Textbaustein");
		JLabel titel2 = new JLabel("englische Übersetzung");
		titel1.setFont(titel1.getFont().deriveFont(Font.BOLD));
		titel2.setFont(titel2.getFont().deriveFont(Font.BOLD));
		
		SForm rightPanel = new SForm();
		
		textArea1 = new JTextArea();
		textArea2 = new JTextArea();
		
		textArea1.setEditable(false);
		textArea2.setEditable(false);

		textArea1.setLineWrap(true);
		textArea2.setLineWrap(true);
		textArea1.setWrapStyleWord(true);
		textArea2.setWrapStyleWord(true);
		
		JScrollPane scroll1 = new JScrollPane(textArea1);
		JScrollPane scroll2 = new JScrollPane(textArea2);
		
		scroll1.setPreferredSize(new Dimension(300, 150));
		scroll2.setPreferredSize(new Dimension(300, 150));
		
		Insets inset1 = new Insets(5, 10, 0, 5);
		
		rightPanel.add(titel1,             0, 0, 1, 1, 0.0, 0.0, 0, 18, inset1);
		rightPanel.add(nameTextbaustein,   1, 0, 1, 1, 0.0, 0.0, 0, 18, inset1);
		rightPanel.add(scroll1,            0, 1, 2, 1, 0.5, 0.5, 1, 18, new Insets(5, 5, 5, 5));
		rightPanel.add(titel2,             0, 2, 2, 1, 0.0, 0.0, 2, 18, inset1);
		rightPanel.add(scroll2,            0, 3, 2, 1, 0.5, 0.5, 1, 18, new Insets(5, 5, 5, 5));
		
		leftPanel = new JPanel();
		leftPanel.setBackground(Color.WHITE);
		leftPanel.setLayout(new BorderLayout());

		leftPanel.add(scrollPaneTree, BorderLayout.CENTER);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
		splitPane.setDividerLocation(300);
		

		JButton newButton = new JButton(mode);
		newButton.addActionListener(this);
		JButton deleteButton = new JButton("Löschen");
		deleteButton.addActionListener(this);
		JButton abbrechenButton = new JButton(new ActionCloseDialog(this, "Abbrechen"));

        abbrechenButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "actionEscapeDialog");
		abbrechenButton.getActionMap().put("actionEscapeDialog", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}

		});


		final DBHandlerConf dbHandlerConf = new DBHandlerConf();
		String usersExpandedAll = dbHandlerConf.read("expand_nachfrage");
		String[] usersExpandedArray;
		if (null != usersExpandedAll) {
			usersExpandedArray = usersExpandedAll.split(",");
		} else {
			usersExpandedArray = new String[]{};
		}

		final JCheckBox expandBox = new JCheckBox("Expandieren");
		final List<String> finalUsersExpandedList;
		if (usersExpandedArray.length > 0) {
			finalUsersExpandedList = new ArrayList<>(Arrays.asList(usersExpandedArray));
		} else {
			finalUsersExpandedList = new ArrayList<>();
		}

		expandBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				String benutzerKuerzel = UserConf.CURRENT_USER.getKuerzel();

				boolean isContained = finalUsersExpandedList.contains(benutzerKuerzel);

				if (expandBox.isSelected() != isContained) {
					if (expandBox.isSelected()) {
						finalUsersExpandedList.add(benutzerKuerzel);
					} else if (isContained) {
						finalUsersExpandedList.remove(benutzerKuerzel);
					}

					StringBuilder sb = new StringBuilder();
					for (String uk : finalUsersExpandedList) {
						sb.append(uk + ",");
					}
					if (sb.length() > 0) {
						sb.deleteCharAt(sb.length() - 1);
					}
					dbHandlerConf.update("expand_nachfrage", sb.toString());
				}


				expand(expandBox.isSelected());
			}
		});
		if (null != usersExpandedAll) {
			usersExpandedArray = usersExpandedAll.split(",");
			for (String kuerzel : usersExpandedArray) {
				if (kuerzel.equals(UserConf.CURRENT_USER.getKuerzel())) {
					if (!expandBox.isSelected()) {
						expandBox.setSelected(true);
						expand(true);
					}
				}
			}
		}

		buttonPanel.add(newButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(abbrechenButton);
		buttonPanel.add(expandBox);
		
		add(splitPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		setVisible(true);
		
	}

	private void expand(boolean selected) {
		for (int i = 1; i < treeTextbausteine.getRowCount(); i++) {
			if (selected) {
				treeTextbausteine.expandRow(i);
			} else {
				treeTextbausteine.collapseRow(i);
			}
		}
	}


	public void setTextbaustein(String titel, String textbaustein1, String textbaustein2) {
		
		nameTextbaustein.setText(titel);
		textArea1.setText(textbaustein1);
		textArea2.setText(textbaustein2);
		textArea1.setCaretPosition(0);
		textArea2.setCaretPosition(0);
		
	}
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("Neu/Ändern")) {
			
			if(treeTextbausteine.getSelectionPath() != null &&
                    treeTextbausteine.getSelectionPath().getLastPathComponent() instanceof Textbausteinkategorie) {

                Textbausteinkategorie nfb = (Textbausteinkategorie) treeTextbausteine.getSelectionPath().getLastPathComponent();

				TreePath parentPath = treeTextbausteine.getSelectionPath();

                new DialogTextbaustein(treeModelNachfragebriefe, parentPath, nfb.ordinal() + 1);

			} else if (treeTextbausteine.getSelectionPath() != null && treeTextbausteine.getLastSelectedPathComponent() instanceof Textbaustein) {

				TreePath parentPath = treeTextbausteine.getSelectionPath().getParentPath();

				new DialogTextbaustein(treeModelNachfragebriefe, parentPath,
						(Textbaustein) treeTextbausteine.getLastSelectedPathComponent());

				clear();

			}
			
		}
		
		
		
		
		if (e.getActionCommand().equals("Textbaustein auswählen")) {

				if (null != treeTextbausteine.getSelectionRows()) {
					List<Textbaustein> bausteine = new ArrayList<Textbaustein>();
					for (int i = 0; i < treeTextbausteine.getSelectionRows().length; i++) {
						DefaultMutableTreeNode node = new DefaultMutableTreeNode(treeTextbausteine.getPathForRow(treeTextbausteine.getSelectionRows()[i]).getLastPathComponent());
                        if (node.isLeaf() && node.getUserObject() instanceof Textbaustein) {
                            bausteine.add((Textbaustein) node.getUserObject());
						}
					}


                    //die _bausteine_ per AntragControl dem Antrag hinzufügen


                    for (Textbaustein tb : bausteine) {

                        String text = "";

                        if (antragControl.isEN()) {
                            text = tb.getText_en();
                        } else {
                            text = tb.getText_de();
                        }

                        //Beim erstellen der Unterlagen werden die Platzhalter ersetzt
                        if (text.contains("{")) {
                            Semester antragsSemester = new DBHandlerSemester().getSemesterByID(antragControl.getAntrag().getSemesterID());
                            String jahr = antragsSemester.getSemesterJahr();
                            String monatvon = "Januar";
                            String monatbis = "Juni";
                            String monatfrom = "January";
                            String monatto = "June";

                            //Sommersemester: Zeitraum normalerweise von Juli-Dezember des Vorjahres
                            if (antragsSemester.getSemesterArt().equals(SemesterArt.SOMMER)) {
                                int jahreszahl = Integer.parseInt(jahr);
                                jahreszahl--;
                                jahr = ""+jahreszahl;
                                monatvon = "Juli";
                                monatbis = "Dezember";
                                monatfrom = "July";
                                monatto = "December";
                            }

                            if (text.contains("Studienabschluss:") || text.contains("Final Degree:")) {
                                //hier steht nichts weil ich das erstmal rausgenommen habe. aber ansonsten ist bei diesem Text der Zeitraum um einen Monat nach hinten und einen Monat nach vorne verlängert
                            }
                            text = text.replaceAll("\\{Jahr\\}", jahr);
                            text = text.replaceAll("\\{MonatVon\\}", monatvon);
                            text = text.replaceAll("\\{MonatBis\\}", monatbis);
							text = text.replaceAll("\\{MonthFrom\\}", monatfrom);
							text = text.replaceAll("\\{MonthTo\\}", monatto);

                        }
                        Unterlagen unterlagen = new Unterlagen();
                        unterlagen.setText(text);
                        unterlagen.setAntragID(antragControl.getAntragID());
                        antragControl.insertUnterlagen(unterlagen);
                    }

				}
				dispose();

        }


        if (e.getActionCommand().equals("Löschen")) {
            if (null != treeTextbausteine.getSelectionRows()) {
                List<Textbaustein> bausteine = new ArrayList<Textbaustein>();
                for (int i = 0; i < treeTextbausteine.getSelectionRows().length; i++) {
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(treeTextbausteine.getPathForRow(treeTextbausteine.getSelectionRows()[i]).getLastPathComponent());
                    if (node.isLeaf() && node.getUserObject() instanceof Textbaustein) {
                        bausteine.add((Textbaustein) node.getUserObject());
                    }
                }

                treeModelNachfragebriefe.remove(bausteine);

				clear();
			}
        }
    }

	private void clear() {
		textArea1.setText("");
		textArea2.setText("");
		treeTextbausteine.clearSelection();
	}
}

