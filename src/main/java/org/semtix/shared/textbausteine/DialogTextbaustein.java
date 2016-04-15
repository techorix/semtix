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


import org.semtix.db.dao.Textbaustein;
import org.semtix.shared.actions.ActionCloseDialog;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dialog zum Neuanlegen oder Ändern der Textbausteine, die im DialogNachfragebriefe angezeigt werden.
 *
 */
@SuppressWarnings("serial")
public class DialogTextbaustein
extends JDialog
implements ActionListener {

	private TreeModelNachfragebriefe treeModel;
	private TreePath parent;
	private TreePath parentPath;
	
	private Textbaustein textbaustein;
	
	private JPanel buttonPanel;

    private JLabel labelTextDE;
    private JLabel labelTextEN;
    private JLabel labelParentID;
    private JLabel labelParentID2;
	
	private JTextField tfBezeichnung;
	private JTextArea textAreaDE, textAreaEN;
	
	private JButton okButton, abbrechenButton;


	/**
	 * Erstellt Dialog
	 * @param treeModel Datenmodell
	 * @param parent Pfad für Vater
	 * @param id id für Vater
	 */
	public DialogTextbaustein(TreeModelNachfragebriefe treeModel, TreePath parent, int id) {

        this.treeModel = treeModel;
		this.parent = parent;
		
		setTitle("Neuer Textbaustein");
		
		textbaustein = new Textbaustein();
		textbaustein.setParentID(id);
        buildDialog();

	}


	/**
	 * Erstellt Dialog zur Änderung schon vorhandener Textbausteine
	 * @param treeModel Datenmodell
	 * @param parentPath Pfad Vater
	 * @param textbaustein Textbausteinobjekt
	 */
	public DialogTextbaustein(TreeModelNachfragebriefe treeModel, TreePath parentPath, Textbaustein textbaustein) {

		this.treeModel = treeModel;
		this.parentPath = parentPath;
		this.textbaustein = textbaustein;

		setTitle("Textbaustein ändern");

		buildDialog();

	}
	
	
	/**
	 * Komponenten zum Dialog hinzufügen.
	 */
	public void buildDialog() {
		
		setSize(550, 400);
		
		// Frame auf dem Bildschirm zentrieren
		setLocationRelativeTo(getParent());
		
		setModal(true);
		setResizable(false);
		
		setLayout(new BorderLayout());
		
		int tfZeilenhoehe = 28;

		buttonPanel = new JPanel();

        JLabel labelBezeichnung = new JLabel("Kurzbezeichnung");
		labelTextDE = new JLabel("Textbaustein");
		labelTextEN = new JLabel("engl. Übersetzung");
		labelParentID = new JLabel("Parent-ID");
		labelParentID2 = new JLabel("" + textbaustein.getParentID());
		
		tfBezeichnung = new JTextField(textbaustein.getBezeichnung());
		tfBezeichnung.setPreferredSize(new Dimension(200, tfZeilenhoehe));
		textAreaDE = new JTextArea();
		textAreaEN = new JTextArea();
		
		textAreaDE.setLineWrap(true);
		textAreaEN.setLineWrap(true);
		textAreaDE.setWrapStyleWord(true);
		textAreaEN.setWrapStyleWord(true);
		
		textAreaDE.setText(textbaustein.getText_de());
		textAreaEN.setText(textbaustein.getText_en());
		
		textAreaDE.setCaretPosition(0);
		textAreaEN.setCaretPosition(0);
		
		JScrollPane scrollDE = new JScrollPane(textAreaDE);
		JScrollPane scrollEN = new JScrollPane(textAreaEN);
		
		scrollDE.setPreferredSize(new Dimension(200, 300));
		scrollEN.setPreferredSize(new Dimension(200, 300));
		
		
		
		okButton = new JButton("Speichern");
		okButton.addActionListener(this);
        okButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "actionEscapeDialog");
        okButton.getActionMap().put("actionEscapeDialog", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }

        });

        abbrechenButton = new JButton(new ActionCloseDialog(this, "Abbrechen"));

        SForm formular = new SForm();

		// Label hinzufügen
		formular.add(labelBezeichnung,          0, 0, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 5, 5, 5));
		formular.add(labelTextDE,               0, 1, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 5, 5, 5));
		formular.add(labelTextEN,               0, 2, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 5, 5, 5));
		formular.add(labelParentID,             0, 3, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 5, 5, 5));

		
		// Eingabefelder hinzufügen
		formular.add(tfBezeichnung,             1, 0, 1, 1, 0.0, 0.0, 2, 18, new Insets(5, 5, 5, 5));
		formular.add(scrollDE,                  1, 1, 1, 1, 0.0, 0.5, 1, 18, new Insets(5, 5, 5, 5));
		formular.add(scrollEN,                  1, 2, 1, 1, 0.0, 0.5, 1, 18, new Insets(5, 5, 5, 5));
		formular.add(labelParentID2,            1, 3, 1, 1, 1.0, 0.0, 0, 18, new Insets(5, 5, 5, 5));
		

		buttonPanel.add(okButton);
		buttonPanel.add(abbrechenButton);
		
		add(formular, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		setVisible(true);
		
	}
	
	
	
	

	/**
	 * Methode des ActionListerners (Klick auf Button)
	 * @param e ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Button 'Speichern' wurde geklickt
		if (e.getActionCommand().equals("Speichern")) {


            textbaustein.setBezeichnung(tfBezeichnung.getText());
			textbaustein.setText_de(textAreaDE.getText());
			textbaustein.setText_en(textAreaEN.getText());

			
			
			if(textbaustein.getTextbausteinID() == 0)
				treeModel.addTextbaustein(parent, textbaustein);
			else
				treeModel.updateTextbaustein(parentPath, textbaustein);


			dispose();
			
		}
		
	}

}
