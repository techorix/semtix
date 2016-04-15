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



package org.semtix.gui.tabs;

import org.semtix.config.UniConf;
import org.semtix.db.dao.Person;
import org.semtix.gui.tabs.antrag.AntragIndex;
import org.semtix.shared.daten.enums.Uni;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;


/**
 * TabComponent für JTabbedPane mit Panel bestehend aus Label (Name) und
 * Button zum Schließen des Tabs.
 */ 
@SuppressWarnings("serial")
public class ButtonTabComponent
extends JPanel {
	
    private final static String SAVE_TRUE = "*";
    private final static String SAVE_FALSE = " ";
    private TabControl tabControl;
    private JLabel lbName, lbSaveStatus;
    
    
    public ButtonTabComponent(TabControl tc) {
    	
    	// FlowLayout-Abstände zurücksetzen (da Abstände später separat gesetzt werden)
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));

        this.setTabControl(tc);
        
        // Panel ist durchsichtig
        setOpaque(false);
        
        // Label mit Anzeige des Save-Status (Sternchen)
        lbSaveStatus = new JLabel(SAVE_FALSE);
        
        // Label mit Namen erstellen
        lbName = new JLabel();
        
        // Schrift für Tab einstellen
        lbName.setFont(getFont().deriveFont(Font.BOLD, 14f));
        lbSaveStatus.setFont(getFont().deriveFont(Font.BOLD, 14f));
        
        // Abstand zwischen Label und Button
        lbName.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 8));
        
        
        
        // Schließen-Button zum Tab hinzufügen
        JButton button = new JButton();
        
        // Grafik zum Schließen als Icon
    	button.setIcon(new ImageIcon(getClass().getResource("/images/close-tab.png")));
    	
        // Tooltip für Schließen-Button
    	button.setToolTipText("Tab schließen");
        
        // Make the button looks the same for all Laf's
    	button.setUI(new BasicButtonUI());
        
        // Make it transparent
    	button.setContentAreaFilled(false);
        
        // No need to be focusable
    	button.setFocusable(false);
        
        // setBorder(BorderFactory.createEtchedBorder());
    	button.setBorder(BorderFactory.createLineBorder(new Color(155, 155, 155)));
    	button.setBorderPainted(false);

        // MouseListener für Rollover-Effekt
    	button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                Component component = e.getComponent();
                if (component instanceof AbstractButton) {
                    AbstractButton button = (AbstractButton) component;
                    button.setBorderPainted(true);
                }
            }

            public void mouseExited(MouseEvent e) {
                Component component = e.getComponent();
                if (component instanceof AbstractButton) {
                    AbstractButton button = (AbstractButton) component;
                    button.setBorderPainted(false);
                }
            }
        });
    	
    	// Rollover-Effekte des Button setzen
    	button.setRolloverEnabled(true);
        
        // Close the proper tab by clicking the button
    	button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                getTabControl().closeTab();
            }
        });
        
        
        
        // Labels (Name und Save-Status) und Button zum Tab hinzufügen
    	add(lbSaveStatus);
        add(lbName);
        add(button);
        
        // Abstand innerhalb des Tabreiters (Panel) setzen
        setBorder(BorderFactory.createEmptyBorder(4, 2, 1, 2));
    	
    }

    
    
    
    // TabComponent aktualisieren
    public void update(Person person, List<AntragIndex> filter, boolean saveStatus) {

        // Save-Status aktualisieren (Sternchen vor Name, wenn noch gespeichert werden muss)
        lbSaveStatus.setText(saveStatus ? SAVE_TRUE : SAVE_FALSE);

        String text = "Anträge";
        Uni uni = UniConf.aktuelleUni;

        // Beschriftung des Tab aktualsieren
        if(person != null) {
    		text = person.toString();
    	}
    	
    	lbName.setText(text);
    	
    	// Schriftfarbe setzen (HU -> schwarz, KW -> rot)
    	lbName.setForeground(uni==Uni.KW ? Color.RED : Color.BLACK);
    	
    }


    public TabControl getTabControl() {
        return tabControl;
    }

    public void setTabControl(TabControl tabControl) {
        this.tabControl = tabControl;
    }
}

