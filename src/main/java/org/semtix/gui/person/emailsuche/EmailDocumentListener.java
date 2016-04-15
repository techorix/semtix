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

package org.semtix.gui.person.emailsuche;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/**
 * DocumentListener zur Erkennung von Änderungen bei den Texteingabefeldern. 
 * Filtert die Einträge in der E-Mail-Liste.
 */
public class EmailDocumentListener
implements DocumentListener {
	
	// Suchtext, der aus dem Eingabefeld geholt wird
    private String tf;
    
    private DialogEmailSuche dialog;
    
    
    /**
     * Konstruktor: der Dialog wird mit übergeben, damit
	 * Zugriff auf die Textfelder möglich ist
     * @param dialog Dialog
     */
    public EmailDocumentListener(DialogEmailSuche dialog) {
    	
    	this.dialog = dialog;
    	
    }


 
    /**
     * Text wird zu einem Textfeld hinzugefügt.
     */
    public void insertUpdate(DocumentEvent e) {
        filter();
    }
    
    /**
     * Text wird von einem Textfeld entfernt.
     */
    public void removeUpdate(DocumentEvent e) {
    	filter();
    }
    
    /**
     * Methode wird nicht gebraucht. Ist nur für style changes...
     */
    public void changedUpdate(DocumentEvent e) {
        //Plain text components do not fire these events
    }
    
    /**
     * RowFilter wird zusammengestellt und gesetzt
     */
    public void filter() {
    	
    	// die eingegebenen Suchtexte werden geholt
    	tf = dialog.getTextEmail();
    	
    	// Filter wird gesetzt
    	dialog.getSorter().setRowFilter(RowFilter.regexFilter("^(?i)" + tf, 0));
    	
    	// Anzahl der angezeigten Einträge aktualisieren
    	dialog.updateAnzahl();
    	
    	// Nach Filtern immer die 1. Zeile der Tabelle selektieren
    	dialog.setSelectedRow();
    	
    }



}
