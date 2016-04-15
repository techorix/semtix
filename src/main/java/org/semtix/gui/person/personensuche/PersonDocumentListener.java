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

package org.semtix.gui.person.personensuche;


import org.semtix.shared.tablemodels.MyRowFilter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.List;

/**
 * DocumentListener zur Erkennung von Änderungen bei den Texteingabefeldern. 
 * Filtert die Personeneinträge in der Liste.
 *
 * Die FilterSpalten sind hartkodiert. Auch deshalb ist diese Klasse unmittelbar mit DialogPersonSuche verknüpft
 */
public class PersonDocumentListener
implements DocumentListener {
    
    private DialogPersonSuche dialog;

    /**
     * Konstruktor: der Dialog wird mit übergeben, damit
	 * Zugriff auf die Textfelder möglich ist
     * @param dialog Dialog
     */
    public PersonDocumentListener(DialogPersonSuche dialog) {
    	
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
    	
    	// Suchtexte werden aus den Eingabefeldern geholt
    	String tf1 = dialog.getTextNachname();
    	String tf2 = dialog.getTextVorname();
    	String tf3 = dialog.getTextMatrikelnummer();
    	
    	// eine Liste mit den 3 Filtern wird erstellt
        List<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>(3);

        if (tf3.length() > 0) {
            filters.add(MyRowFilter.regexFilter("^(?i)" + tf3, 4)); //Textfeld3
        } else {
            filters.add(new MyRowFilter(tf1, 2));
            filters.add(new MyRowFilter(tf2, 3));
        }
        // RowFilter.andFilter wird erstellt
    	RowFilter<Object, Object> filter = RowFilter.andFilter(filters);

    	// Filter wird gesetzt
    	dialog.getSorter().setRowFilter(filter);

    	// Anzahl der angezeigten Einträge aktualisieren
    	dialog.updateAnzahl();
    	
    	// Nach Filtern immer die 1. Zeile der Tabelle selektieren
    	dialog.setSelectedRow();

    }

}

