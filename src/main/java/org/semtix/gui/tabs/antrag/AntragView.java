/*
 * Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 * Semesterticketbüro der Humboldt-Universität Berlin
 *
 * Copyright (c) 2015. Michael Mertins (MichaelMertins@gmail.com)
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

package org.semtix.gui.tabs.antrag;

import javax.swing.*;
import java.awt.*;

/**
 * Panel enthält AntragsFormular (CENTER-Bereich des BorderLayouts) und PagingPanel zum Durchblättern 
 *(SOUTH-Bereich des BorderLayouts)
 */
public class AntragView {
	
	private JPanel mainPanel;
	
	
	public AntragView(AntragPanel antragPanel) {
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		mainPanel.add(antragPanel.getScrollPane());
		
	}

	
	/**
	 * Fügt der AntragView das PagingPanel (Durchblättern der Anträge) hinzu
	 * @param pagingPanel PagingPanel
	 */
	public void setPagingPanel(PagingPanel pagingPanel) {
		mainPanel.add(pagingPanel, BorderLayout.SOUTH);
	}
	

	/**
	 * Liefert das MainPanel (enthält das AntragsFormular)
	 * @return MainPanel
	 */
	public JPanel getMainPanel() {
		return mainPanel;
	}


}
