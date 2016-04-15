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

package org.semtix.gui.tabs.antrag.nachreichungen.tabelle;

import org.semtix.db.dao.Unterlagen;
import org.semtix.shared.daten.enums.UnterlagenStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Panel mit Text und farbigem Icon zur Darstellung der Unterlagen (DialogUnterlagen)
 */
@SuppressWarnings("serial")
public class UnterlagenStatusPanel
extends JPanel {
	
	private Unterlagen unterlagen;
	
	private JTextArea textfeld;
	private JLabel iconLabel;
	
	private int iconIndex = 0;
	
	// Icons, die den Unerlagenstatus darstellen
	private Icon bild1 = new ImageIcon(getClass().getResource("/images/unterlagenstatus_erhalten.gif"));
	private Icon bild2 = new ImageIcon(getClass().getResource("/images/unterlagenstatus_nachgefragt.gif"));
	private Icon bild3 = new ImageIcon(getClass().getResource("/images/unterlagenstatus_gemahnt.gif"));
	private Icon bild4 = new ImageIcon(getClass().getResource("/images/unterlagenstatus_nichtnachgefragt.gif"));
	
	
	private Icon[] iconArray = {bild1, bild2, bild3, bild4};

	/**
	 * Erstellt ein neues Panel
	 * @param unterlagen Unterlagen-Objekt
	 */
	public UnterlagenStatusPanel(Unterlagen unterlagen) {
		
		this.unterlagen = unterlagen;

		
		if (unterlagen.getUnterlagenStatus().getID()-1 > iconArray.length-1)
			iconIndex = 0;
		else
			iconIndex = unterlagen.getUnterlagenStatus().getID()-1;
		
//		setPreferredSize(new Dimension(400, 170));
		
		textfeld = new JTextArea();
		textfeld.setLineWrap(true);
		textfeld.setWrapStyleWord(true);
		textfeld.setText(unterlagen.getText());
		textfeld.setCaretPosition(0);
		
		JScrollPane scrollText = new JScrollPane();
		scrollText.setPreferredSize(new Dimension(350, 150));
		scrollText.setViewportView(textfeld);
		
		iconLabel = new JLabel(iconArray[iconIndex]);
		
		// 
		iconLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				
				// linke Maustaste
				if (me.getButton() == MouseEvent.BUTTON1){
					iconIndex++;
				    
				}
				
				// rechte Maustaste
				if (me.getButton() == MouseEvent.BUTTON3){
					iconIndex--;
				    
				}
				
                //iconIndex++;
                if (iconIndex == iconArray.length)
                	iconIndex = 0;
                if (iconIndex == -1)
                	iconIndex = iconArray.length-1;
                
                changeIcon(iconIndex);
                
            }
		});
		
		add(scrollText);
		add(iconLabel);
		

	}


    /**
	 * angezeigtes Icon ändern
	 * @param index Index der Iconliste
	 */
	public void changeIcon(int index) {
		iconLabel.setIcon(iconArray[index]);
	}
	
	
	/**
	 * Setzt den Text für den Unterlagenstatus
	 * @param text Text
	 */
	public void setText(String text) {
		textfeld.setText(text);
	}
	
	
	/**
	 * Liefert Unterlagen
	 * @return Unterlagen
	 */
	public Unterlagen getUnterlagen() {
		
		unterlagen.setUnterlagenStatus(UnterlagenStatus.getUnterlagenStatusByID(iconIndex + 1));
		unterlagen.setText(textfeld.getText());
		
		return unterlagen;
	}

	

}
