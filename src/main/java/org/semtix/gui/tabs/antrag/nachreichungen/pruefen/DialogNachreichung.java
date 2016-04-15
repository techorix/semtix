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

package org.semtix.gui.tabs.antrag.nachreichungen.pruefen;

import com.lowagie.text.Font;
import org.semtix.db.dao.Nachreichung;
import org.semtix.shared.actions.ActionCloseDialog;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

/**
 * Dialog zur Anzeige und Bearbeitung von ungeprüften Nachreichungen
 */
@SuppressWarnings("serial")
public class DialogNachreichung
extends JDialog {
	
	private NachreichungControl control;
	
	private SForm nachreichungView;
	
	private JLabel lbSemester, lbName, lbMatrikelnr;
	
	private JRadioButton rbPapier, rbEmail, rbAusgedruckteEmail;
	
	private JButton checkedButton;
	
	// Format für Anzeige des Zeitstempels
	private SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	
	
	/**
	 * Erstellt einen neuen Dialog
	 * @param _control NachreichungControl
	 */
	public DialogNachreichung(NachreichungControl _control) {
		
		this.control = _control;
		
		setTitle("Ungeprüfte Nachreichungen");
		
		setSize(500, 300);
				
		setModal(true);
		//setResizable(false);
		
		setLayout(new BorderLayout(10, 10));
		
		
		
		// ***** HeadPanel

		lbSemester = new JLabel();
		lbName = new JLabel();
		lbMatrikelnr = new JLabel();
		
		lbName.setFont(lbName.getFont().deriveFont(Font.BOLD, 14f));
		lbSemester.setFont(lbSemester.getFont().deriveFont(Font.BOLD, 14f));
		lbMatrikelnr.setFont(lbMatrikelnr.getFont().deriveFont(Font.BOLD, 14f));
		
		SForm headPanel = new SForm();
		headPanel.setBackground(new Color(200, 200, 200));
		headPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
		
		
		headPanel.add(lbSemester,           0,  0, 2, 1, 0.0, 0.0, 0, 18, new Insets(5, 5, 5, 5));
		headPanel.add(lbName,               0,  1, 1, 1, 0.0, 0.0, 0, 17, new Insets(10, 5, 5, 5));
		headPanel.add(lbMatrikelnr,         1,  1, 1, 1, 1.0, 1.0, 0, 13, new Insets(10, 5, 5, 5));

		
		
		// ***** ButtonPanel		
		
		JPanel buttonPanel = new JPanel();
		
		JButton exitButton = new JButton(new ActionCloseDialog(this, "Abbrechen"));
        exitButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "actionEscapeDialog");
        exitButton.getActionMap().put("actionEscapeDialog", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }

        });


		buttonPanel.add(exitButton);
		
		
		// ***** Panel für neue Nachreichungen
		
		JLabel lbTextNeu = new JLabel("Neue Nachreichung");
		
		rbPapier = new JRadioButton("Papier");
		rbEmail = new JRadioButton("Email");
		rbAusgedruckteEmail = new JRadioButton("ausgedruckte Email");
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rbPapier);
		buttonGroup.add(rbEmail);
		buttonGroup.add(rbAusgedruckteEmail);
		
		// Radiobutton "Papier" zu Beginn auf ausgewählt setzen
		rbPapier.setSelected(true);
		
		JButton addButton = new JButton("anlegen");
		
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addNachreichung();
			}
		});
		
		// Panel mit RadioButtons zum Hinzufügen neuer Nachreichungen
		SForm addPanel = new SForm();
		
		addPanel.add(lbTextNeu,            0, 0, 1, 1, 0.0, 0.0, 0, 18, new Insets(2, 2, 10, 2));
		addPanel.add(rbPapier,             0, 1, 1, 1, 0.0, 0.0, 0, 18, new Insets(2, 2, 2, 2));
		addPanel.add(rbEmail,              0, 2, 1, 1, 0.0, 0.0, 0, 18, new Insets(2, 2, 2, 2));
		addPanel.add(rbAusgedruckteEmail,  0, 3, 1, 1, 1.0, 1.0, 0, 18, new Insets(2, 2, 10, 2));
		addPanel.add(addButton,            0, 4, 1, 1, 0.0, 0.0, 0, 16, new Insets(2, 2, 2, 2));
		
		
		// ***** Panel zum Auflisten und prüfen vorhandener Nachreichungen
		
		JLabel lbTextList = new JLabel("Vorhandene Nachreichungen");

		nachreichungView = new SForm();
		nachreichungView.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		nachreichungView.setBackground(new Color(235, 235, 235));
		
		checkedButton = new JButton("geprüft");
		
		checkedButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				control.saveNachreichungen();
			}
		});
		
		// Panel mit der Auflistung der vorhandenen Nachreichungen
		SForm listPanel = new SForm();
		
		listPanel.add(lbTextList,            0, 0, 1, 1, 0.0, 0.0, 0, 18, new Insets(2, 2, 10, 2));
		listPanel.add(nachreichungView,      0, 1, 1, 1, 1.0, 1.0, 2, 18, new Insets(2, 2, 2, 2));
		listPanel.add(checkedButton,         0, 2, 1, 1, 0.0, 0.0, 0, 16, new Insets(2, 2, 2, 2));
		
		
		
		
		
		SForm mainPanel = new SForm();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		//mainPanel.add(titelPanel,         0, 0, 3, 1, 0.0, 0.0, 2, 18, new Insets(2, 2, 10, 2));
		mainPanel.add(headPanel,         0, 0, 3, 1, 0.0, 0.0, 2, 18, new Insets(2, 2, 10, 2));
		mainPanel.add(addPanel,           0, 1, 1, 1, 0.0, 0.0, 0, 18, new Insets(2, 2, 10, 2));
		mainPanel.add(new JSeparator(JSeparator.VERTICAL),         1, 1, 1, 1, 0.0, 0.0, 1, 18, new Insets(2, 15, 10, 15));
		mainPanel.add(listPanel,          2, 1, 1, 1, 1.0, 1.0, 1, 18, new Insets(2, 2, 10, 2));
		
		
		add(mainPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

	}
	
	
	
	// 
	/**
	 * Fügt neue Nachreichung hinzu
	 */
	public void addNachreichung() {

		NachreichungArt nachreichungArt = null;

		// NachreichungArt, je nach selektiertem radioButton
		if(rbPapier.isSelected())
			nachreichungArt = NachreichungArt.PAPER;
		
		if(rbEmail.isSelected())
			nachreichungArt = NachreichungArt.EMAIL;
		
		if(rbAusgedruckteEmail.isSelected())
			nachreichungArt = NachreichungArt.PRINTED_EMAIL;

		control.addNachreichung(nachreichungArt);
			
	}

	
	
	/**
	 * View mit Daten aus Model füllen
	 */
	public void updateView() {
		
		lbSemester.setText(control.getSemester());
		lbName.setText(control.getName());
		lbMatrikelnr.setText(control.getMatrikelnr());


		final java.util.List<Nachreichung> nachreichungListe = control.getModel().getList();
		
		if(nachreichungListe.size() > 0) {
			
			int row = 0;
			
			for(final Nachreichung n : nachreichungListe) {
				
				JLabel lbDatumEingang = new JLabel(df.format(n.getTimestampEingang().getTime()), JLabel.CENTER);
				JLabel lbNachreichungArt = new JLabel(n.getNachreichungArt().getNachreichungArtText());
				JCheckBox cbChecked = new JCheckBox();
				cbChecked.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						control.updateNachreichung(nachreichungListe.indexOf(n), ((JCheckBox) e.getSource()).isSelected());
					}
				});
				
				nachreichungView.add(cbChecked,             0, row, 1, 1, 0.0, 0.0, 0, 17, new Insets(3, 3, 3, 10));
				nachreichungView.add(lbDatumEingang,        1, row, 1, 1, 0.0, 0.0, 0, 17, new Insets(3, 3, 3, 10));
				nachreichungView.add(lbNachreichungArt,     2, row, 1, 1, 1.0, 1.0, 0, 17, new Insets(3, 3, 3, 3));
				
				
				row++;
				
			}
		}
			else {
				nachreichungView.add(new JLabel("- keine Nachreichungen vorhanden -"), 0, 0, 1, 1, 0.0, 0.0, 0, 11, new Insets(3, 3, 3, 3));
				enableCheckButton(false);
			}

	}
	

	
	
	/**
	 * Button "geprüft" deaktivieren, wenn keine Nachreichungen vorhanden
	 * @param state Anzeige Button ja/nein
	 */
	public void enableCheckButton(boolean state) {
		checkedButton.setEnabled(state);
	}

}
