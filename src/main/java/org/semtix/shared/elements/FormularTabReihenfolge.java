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

/**
 *
 * Tabreihenfolge in Formularen festlegen
 *
 */

package org.semtix.shared.elements;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Legt die Tab-Reihenfolge der Komponenten innerhalb eines Formulares fest. 
 */
public class FormularTabReihenfolge
extends FocusTraversalPolicy {
	
	private JComponent[] order;
	private List<JComponent> list;
	
	
	/**
	 * Erstellt neue TabReihenfolge.
	 * @param order Geordnete Liste der Komponenten
	 */
	public FormularTabReihenfolge( JComponent[] order ) {
		
		this.order = order;
		
		list = java.util.Arrays.asList( order );

	}


	 public Component getFirstComponent( Container focusCycleRoot ) {
		 return order[0];
	 }

	 public Component getLastComponent( Container focusCycleRoot ) {
		 return order[order.length - 1];
	 }

	 public Component getComponentAfter( Container focusCycleRoot, Component aComponent ) {
		 int index = list.indexOf( aComponent );
		 return order[(index + 1) % order.length];
	 }

	 public Component getComponentBefore( Container focusCycleRoot, Component aComponent ) {
		 int index = list.indexOf( aComponent );
		 return order[(index - 1 + order.length) % order.length];
	 }

	 public Component getDefaultComponent( Container focusCycleRoot ) {
		 return order[0];
	 }

	 public Component getInitialComponent( Window window ) {
		 return order[0];
	 }
	

}
