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

package org.semtix.gui.tabs;

import org.semtix.db.DBHandlerAntrag;
import org.semtix.gui.MainControl;
import org.semtix.gui.filter.Filter;
import org.semtix.gui.tabs.antrag.AntragIndex;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Action öffnet einen Tab in der Anwendung und zeigt die Anträge an, durch 
 * die geblättert werden kann.
 */
@SuppressWarnings("serial")
public class ActionNewTab
extends AbstractAction {
	
	private MainControl mainControl;
	
	public ActionNewTab(MainControl mainControl) {
		
		this.mainControl = mainControl;
		
		putValue(Action.NAME, "<html>Anträge<br>anzeigen</html>");
		putValue(Action.SHORT_DESCRIPTION, "Anträge anzeigen");
	
	}
	
	
	/**
	 * Action ausführen und neuen Tab öffnen
	 */
	public void actionPerformed(ActionEvent event) {
		List<AntragIndex> indexListe = new DBHandlerAntrag().getAntragIndexListe(new Filter());

        if (indexListe.size() > 0)
            mainControl.addTab(indexListe);
        else
            JOptionPane.showMessageDialog(null, "Keine Daten gefunden.", "Fehler", JOptionPane.WARNING_MESSAGE);

	}

}
