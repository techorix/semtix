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


package org.semtix.gui.filter;


import org.semtix.shared.daten.enums.FilterArt;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Das FilterPanel ist Teil des Dialogs zum Filtern nach Anträgen (DialogAntragFilter)
 */
@SuppressWarnings("serial")
public class FilterPanel
extends JPanel {

    private final FilterControl filterController;

    /**
	 * Erstellt ein FilterPanel
     * @param filterControl Objekt
     */
    FilterPanel(FilterControl filterControl) {

        this.filterController = filterControl;

		setLayout(new BorderLayout());
		
		JLabel lbTitel = new JLabel("Ich möchte filtern nach...");
		lbTitel.setFont(lbTitel.getFont().deriveFont(Font.BOLD));

        SForm mainPanel = new SForm();

        mainPanel.add(lbTitel, 0, 0, 1, 1, 0.0, 0.0, 2, 18, new Insets(5, 5, 2, 5));
        mainPanel.add(new JSeparator(JSeparator.HORIZONTAL), 0, 1, 1, 1, 0.0, 0.0, 2, 18, new Insets(2, 5, 5, 5));

        int i = 2;
        for (final FilterArt art : FilterArt.values()) {
            JButton filterButton = new JButton(art.toString());
            filterButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    filterController.setFilter(art);
                }
            });

            mainPanel.add(filterButton, 0, i, 1, 1, 0.0, 0.0, 2, 18, new Insets(5, 5, 5, 5));
            i++;
        }

        this.add(mainPanel);

    }


}
