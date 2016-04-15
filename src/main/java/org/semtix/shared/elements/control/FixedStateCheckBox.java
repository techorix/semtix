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


import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


/**
 * JCheckBox mit "ausgeschalteter" Editierbarkeit.
 * Ein vorgegebener Bool-Wert (Status) ist nicht mehr durch Mausklick editierbar. Wird benötigt bei Formularen, 
 * bei denen nur Werte angezeigt werden aber nicht verändert werden sollen.
 */
@SuppressWarnings("serial")
public class FixedStateCheckBox
extends JCheckBox
implements ItemListener {
	
	private boolean state;


	/**
	 * Erstellt CheckBox mit Beschriftung.
	 * @param text Description Text
	 */
	public FixedStateCheckBox(String text) {
		this.setText(text);
		this.setFocusable(false);
		this.addItemListener(this);
	}


	/**
	 * Status der CheckBox setzen.
	 * @param state Setzt gewünschten Zustand der CheckBox
	 */
	public void setFixedState(boolean state) {
		this.state = state;	
		this.setSelected(state);
	}

	
	/**
	 * Methode des ItemListeners der auf Änderungen des Zustands reagiert.
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		this.setSelected(state);
	}

}