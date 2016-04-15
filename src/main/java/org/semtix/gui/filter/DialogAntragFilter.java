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


package org.semtix.gui.filter;

import com.lowagie.text.Font;
import org.semtix.config.UniConf;
import org.semtix.shared.actions.ActionCloseDialog;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


/**
 * Dialog zur Filterung der Anträge
 */
public class DialogAntragFilter {
	
	private JDialog dialog;
	
	private FilterControl filterControl;
	
	private FilterPanel filterPanel;
	
	private SonstigeFilterPanel sonstigeFilterPanel;
	
	private BuchstabenPanel buchstabenPanel;

	
	/**
	 * Erstellt einne Dialog zur Antrgafilterung
	 * @param control FilterControl
	 */
	public DialogAntragFilter(FilterControl control) {

		this.filterControl = control;
		
		dialog = new JDialog();
		
		dialog.setTitle("Datensätze nach Antragsart filtern");
		
		dialog.setSize(500, 300);
		
		dialog.setResizable(false);
		
		dialog.setLayout(new BorderLayout());
		
		dialog.setModal(true);
		
		
		
		
		
		// ***** Panel erstellen *****
		
		// TitelPanel
		
		JLabel lbTitel = new JLabel("Filter");
		JLabel lbUni = new JLabel("Anträge für " + UniConf.aktuelleUni);
		
		lbTitel.setFont(lbTitel.getFont().deriveFont(Font.BOLD, 14f));
		lbUni.setFont(lbUni.getFont().deriveFont(Font.BOLD));
		
		SForm titelPanel = new SForm();
		titelPanel.setBackground(new Color(200, 200, 200));
		titelPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
		
		
		titelPanel.add(lbTitel,           0,  0, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 5, 5, 5));
		titelPanel.add(lbUni,             0,  1, 1, 1, 1.0, 1.0, 0, 18, new Insets(5, 5, 5, 5));


		// FilterPanel für direkete Filter (über Buttons)
		filterPanel = new FilterPanel(filterControl);

		// BuchstabenPanel mit den CheckBoxen und Anfangsbuchstaben für die Filterauswahl
		buchstabenPanel = new BuchstabenPanel(filterControl);
		
		// SonstigeFilterPanel mit auswahlisten
		sonstigeFilterPanel = new SonstigeFilterPanel(filterControl);
		
		
		
		// Panel für Button zum Abbrechen (Dialog schließen)
		JPanel buttonPanel = new JPanel();
		
		// Button zum Schließen des Dialogs hinzufügen
		buttonPanel.add(new JButton(new ActionCloseDialog(dialog, "Beenden")));


        buttonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "actionEscapeDialog");
        buttonPanel.getActionMap().put("actionEscapeDialog", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                dialog.dispose();
            }

        });

        SForm mainPanel = new SForm();
		
		// leeren Rahmen erzeugen (Innenabstand des Panels)
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		mainPanel.add(titelPanel,             0, 0, 2, 1, 0.0, 0.0, 2, 18, new Insets(5, 5, 5, 5));

		mainPanel.add(filterPanel,            0, 1, 1, 1, 0.5, 0.5, 2, 18, new Insets(5, 5, 5, 15));
		
		mainPanel.add(sonstigeFilterPanel,    1, 1, 1, 1, 0.5, 0.5, 2, 18, new Insets(5, 15, 5, 5));
		
		mainPanel.add(buchstabenPanel,        0, 2, 2, 1, 1.0, 1.0, 2, 18, new Insets(5, 5, 5, 5));
		
		
		dialog.add(mainPanel, BorderLayout.CENTER);
		dialog.add(buttonPanel, BorderLayout.SOUTH);


		dialog.pack();

		// Frame auf dem Bildschirm zentrieren
		dialog.setLocationRelativeTo(null);

		dialog.setVisible(true);
	}
}
