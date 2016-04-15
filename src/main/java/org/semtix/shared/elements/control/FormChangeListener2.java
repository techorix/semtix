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


package org.semtix.shared.elements.control;

import org.semtix.gui.tabs.TabInterface;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;



/**
 * ItemListener soll Änderungen bei Checkboxen, Comboboxen und
 * ToggleButtons im Formular erkennen und melden, 
 * damit der User merkt, dass er den geänderten
 * Datensatz noch abspeichern muss.
 */
public class FormChangeListener2
implements ItemListener {

	private TabInterface formularControl;
	
	/**
	 * Erstellt einen FormChangeListener
	 * @param formularControl Interface FormularController
	 */
	public FormChangeListener2(TabInterface formularControl) {
		
		this.formularControl = formularControl;
		
	}
	
	
	/**
	 * Änderungen am SaveStatus, wenn sich der Zustand der Komponentne geändert hat.
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		formularControl.setSaveStatus(true);
	}

}
