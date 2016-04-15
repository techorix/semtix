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


package org.semtix.gui.info;

import org.semtix.config.Settings;
import org.semtix.shared.elements.SForm;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * 
 * Dialog zur Anzeige von Infos über das Programm (Version)
 *
 */
@SuppressWarnings("serial")
public class DialogAbout
extends JDialog {

	/**
	 * Erzeugt einen neuen Dialog, in dem während der Entwicklungsphase die laufende Versionsnummer 
	 * sowie das Datum des letzten Builds angezeigt werden.
	 */
	public DialogAbout() {
		
		setTitle("Über das Programm");

		buildDialog();
		
	}
	
	
	/**
	 * Komponenten zum Dialog hinzufügen.
	 */
	public void buildDialog() {
		
		setSize(200, 150);
		
		// Frame auf dem Bildschirm zentrieren
		setLocationRelativeTo(getParent());
				
		setModal(true);
		setResizable(false);
		
		// Panel mit Infos erstellen
		SForm infoPanel = new SForm();
		
		// leeren Rahmen erzeugen (Innenabstand des Panels)
		infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		infoPanel.setPreferredSize(new Dimension(180, 130));
		infoPanel.setOpaque(false);

        infoPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "actionEscapeDialog");
        infoPanel.getActionMap().put("actionEscapeDialog", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }

        });
        // MouseListener zum Schließen des Dialogs per Mausklick
		infoPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                closeDialog();
            }
		});
		
		JLabel lbName = new JLabel(Settings.APP_NAME);
		JLabel lbVersion = new JLabel(Settings.APP_VERSION);
		JLabel lbDatum = new JLabel(Settings.APP_BUILD_DATE);
		
		lbName.setForeground(Color.WHITE);
		lbVersion.setForeground(Color.WHITE);
		lbDatum.setForeground(Color.WHITE);
		
		infoPanel.add(lbName,        0, 1, 1, 1, 1.0, 1.0, 0, 15, new Insets(5, 7, 1, 2));
		infoPanel.add(lbVersion,     0, 2, 1, 1, 0.0, 0.0, 0, 15, new Insets(5, 7, 1, 2));
		infoPanel.add(lbDatum,       0, 3, 1, 1, 0.0, 0.0, 0, 15, new Insets(5, 7, 1, 2));
		
		// Hintergrundbild für Dialog anlegen
		Background background = new Background(); 
        getContentPane().add(background); 
        
        // Infos zum Hintergrundbild hinzufügen
        background.add(infoPanel);
		
		pack();
		setVisible(true);
		
	}
	
	
	// Schließen des Dialogs
	private void closeDialog() {
		this.dispose();
	}

	
	/**
	 * Panel mit Hintergrundbild
	 */
	 class Background
	 extends JPanel{
		 
		 private BufferedImage image; 
		 
		 public Background(){ 
 
			 try{ 
				 image = ImageIO.read(getClass().getResource("/images/about.jpg"));
			 }catch(Exception ex){ 
				 ex.printStackTrace(); 
			 } 
		 } 
		 
		 public void paintComponent(final Graphics g) { 
			 super.paintComponent(g); 
			 g.drawImage(image, 0, 0, null); 
		 } 
	        
	 } 
	 

}
