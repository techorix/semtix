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

package org.semtix.shared.actions;

import org.semtix.config.UniConf;
import org.semtix.gui.MainControl;
import org.semtix.shared.daten.enums.Uni;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Action wechselt den Uni-Modus von HU zu KW und von KW zu HU
 */
@SuppressWarnings("serial")
public class ActionChangeUni
extends AbstractAction {
		
	private MainControl mainControl;
		
	public ActionChangeUni(MainControl mainControl) {
			
		this.mainControl = mainControl;
			
		// Button mit Icon (in toolBar) zum Wechseln der Uni
		Icon icon = new ImageIcon(getClass().getResource("/images/hu_kw.png"));

					
		putValue(Action.SHORT_DESCRIPTION, "Universität wechseln");
		putValue(Action.LARGE_ICON_KEY, icon);

	}

	public void actionPerformed(ActionEvent event) {
		
		// wenn aktuelle Uni = HU, dann nach KW wechseln
		if(UniConf.aktuelleUni == Uni.HU)
			mainControl.setUni(Uni.KW);
		// wenn aktuelle Uni = KW, dann nach HU wechseln
		else if(UniConf.aktuelleUni == Uni.KW)
			mainControl.setUni(Uni.HU);	
   	
	}

}
