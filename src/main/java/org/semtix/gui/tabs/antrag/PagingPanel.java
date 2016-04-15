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

package org.semtix.gui.tabs.antrag;

import org.semtix.gui.tabs.TabControl;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controlpanel zum Navigieren durch Antragsdaten (Filtern und Blättern)
 *
 */
@SuppressWarnings("serial")
public class PagingPanel
extends JPanel {
	
	private final Dimension dimButton = new Dimension(30, 30);
	private final Insets insets = new Insets(-9, -9, -9, -9);
	private final Color filterButtonBackground = new Color(252, 238, 14);
	private final Color activeButtonBackground = new Color(120, 255, 25);
    private TabControl tabControl;
	private JPanel letterPanel;

	private JButton backwardButton, forwardButton, firstIndexButton, lastIndexButton;
	
	private ArrayList<JButton> buttonListe;

	private JLabel indexAnzeige;
	
	
	/**
	 * Erstellt ein neues PaginPanel
	 * @param tabControl TabControl
	 */
	public PagingPanel(TabControl tabControl) {
		
		this.tabControl = tabControl;
		
		setLayout(new BorderLayout());
		
		
		
		letterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		SForm controlPanel = new SForm();

		controlPanel.setPreferredSize(new Dimension(220, 40));

		controlPanel.setBackground(new Color(200, 200, 200));
		
		
		// Button zum rückwärts blättern (Index wird runtergezählt)
		backwardButton = new JButton("<");
		backwardButton.setToolTipText("rückwärts");
		
		// Button zum vorwärts blättern (Index wird hochgezählt)
		forwardButton = new JButton(">");
		forwardButton.setToolTipText("vorwärts");
		
		// Button zum rückwärts blättern (Index wird runtergezählt)
		firstIndexButton = new JButton("|<");
		firstIndexButton.setToolTipText("erster Eintrag");
		
		// Button zum vorwärts blättern (Index wird hochgezählt)
		lastIndexButton = new JButton(">|");
		lastIndexButton.setToolTipText("letzter Eintrag");
		
		backwardButton.setPreferredSize(dimButton);
		forwardButton.setPreferredSize(dimButton);
		firstIndexButton.setPreferredSize(dimButton);
		lastIndexButton.setPreferredSize(dimButton);

		backwardButton.setMargin(insets);
		forwardButton.setMargin(insets);
		firstIndexButton.setMargin(insets);
		lastIndexButton.setMargin(insets);


		buttonListe = new ArrayList<JButton>();
		
		
		indexAnzeige = new JLabel("Indexanzeige");


		controlPanel.add(indexAnzeige, 0, 0, 1, 1, 0.0, 0.0, 0, 18, new Insets(9, 10, 0, 1));
		controlPanel.add(firstIndexButton, 1, 0, 1, 1, 0.0, 0.0, 0, 18, new Insets(1, 0, 0, 1));
		controlPanel.add(backwardButton, 2, 0, 1, 1, 0.0, 0.0, 0, 18, new Insets(1, 0, 0, 1));
		controlPanel.add(forwardButton, 3, 0, 1, 1, 0.0, 0.0, 0, 18, new Insets(1, 0, 0, 1));
		controlPanel.add(lastIndexButton, 4, 0, 1, 1, 1.0, 1.0, 0, 18, new Insets(1, 10, 0, 1));
		
		add(letterPanel, BorderLayout.CENTER);
		add(controlPanel, BorderLayout.LINE_END);

	}
	


	
	/**
	 * ActionListener mit Referenz auf PagingControl auf Buttons setzen
	 * @param antragControl AntragControl
	 */
	public void setListener(final AntragControl antragControl) {

		backwardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				antragControl.minus();
			}
		});
		
		
		forwardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				antragControl.plus();
			}
		});
		
		
		firstIndexButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				antragControl.firstIndex();
			}
		});
		
		
		lastIndexButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				antragControl.lastIndex();
			}
		});

		
	}
	
	
	
	/**
	 * Erstellt Buttons mit Anfangsbuchstaben 
	 * @param indexListe Liste der Anträge
	 * @param antragControl AntragControl
	 */
    public void buildButtons(final List<AntragIndex> indexListe, final AntragControl antragControl) {

		buttonListe = new ArrayList<JButton>();
		
		// Panel leeren (alle Komponenten entfernen)
		letterPanel.removeAll();

		//Sortieren wichtig!
		Collections.sort(indexListe);

		char lastCapital = 0;
		
		for(AntragIndex api : indexListe){

			final AntragIndex i = api;

			
			if(i.getCapital() != lastCapital){

				JButton button = new JButton(new AbstractAction(String.valueOf(i.getCapital())) {
					public void actionPerformed(ActionEvent e) {
						antragControl.setIndex(indexListe.indexOf(i));
						//antragControl.updateAntrag();
						//antragModel.setIndex(indexListe.indexOf(i));
					}
				});
				
				button.setPreferredSize(dimButton);
				button.setMargin(insets);
				
				// Buttons der Liste hinzufügen
				buttonListe.add(button);
				
				// Button dem Panel hinzufügen
				letterPanel.add(button);
				
				lastCapital = i.getCapital();
				
			}
			
			
		}

		letterPanel.repaint();
		letterPanel.revalidate();
		
	}



	/**
	 * Markiert den Button farbig, dessen Anfangsbuchstabe der aktuell 
	 * angezeigten Person entspricht
	 * @param letter Anfangsbuchstabe
	 */
	public void setButtonColor(String letter) {
		
		for(JButton b: buttonListe) {
			if(b.getText().equals(letter))
				b.setBackground(activeButtonBackground);
			else
				b.setBackground(null);
		}
		
	}
	
	
	/**
	 * Vorwärtsbutton aktivieren/deaktivieren
	 * @param state anklickbar? ja/nein
	 */
	public void enableForwardButton(boolean state){
		forwardButton.setEnabled(state);
	}
	
	
	/**
	 * Rückwärtsbutton aktivieren/deaktivieren
	 * @param state anklickbar? ja/nein
	 */
	public void enableBackwardButton(boolean state){
		backwardButton.setEnabled(state);
	}
	
	
	/**
	 * Zustand des FirstIndexButton ändern
	 * @param state anklickbar? ja/nein
	 */
	public void enableFirstIndexButton(boolean state) {
		firstIndexButton.setEnabled(state);
	}
	
	
	/**
	 * Zustand des LastIndexButton ändern
	 * @param state anklickbar? ja/nein
	 */
	public void enableLastIndexButton(boolean state) {
		lastIndexButton.setEnabled(state);
	}
	
	
	
	
	
	/**
	 * Setzt den Text der Indexaneige (Datensatz Nummer / Anzahl Gesamt)
	 * @param text Text der Indexanzeige
	 */
	public void setIndexAnzeige(String text){
		indexAnzeige.setText(text);
	}

}