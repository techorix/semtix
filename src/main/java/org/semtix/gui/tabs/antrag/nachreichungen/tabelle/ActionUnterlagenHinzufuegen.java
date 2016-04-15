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

package org.semtix.gui.tabs.antrag.nachreichungen.tabelle;

import org.semtix.gui.tabs.antrag.AntragControl;
import org.semtix.shared.textbausteine.DialogNachfragebriefe;

import javax.swing.*;
import java.awt.event.ActionEvent;
//import persontab.antrag.AntragControl;

/**
 * Action öffnet Dialog zum Hinzufügen von fehlenden Unterlagen
 */
@SuppressWarnings("serial")
public class ActionUnterlagenHinzufuegen
extends AbstractAction {

	private AntragControl antragControl;

	
	/**
	 * Erstellt neue Action
	 * @param antragControl AntragControl
	 */
	public ActionUnterlagenHinzufuegen(AntragControl antragControl){

		this.antragControl = antragControl;

		putValue(Action.NAME, "Texte hinzufügen");
		
	}
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {

		new DialogNachfragebriefe(antragControl);
		
	}

}
