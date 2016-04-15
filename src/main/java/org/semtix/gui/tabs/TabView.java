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

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * TabView ist das Panel, welches in einem Tab des JTabbedPane angezeigt wird.
 */
@SuppressWarnings("serial")
public class TabView
extends JPanel {

	private CardLayout cardLayout;

    private String currentCardName;

    /**
	 * Erstellt ein neues TabView
	 */
    public TabView() {
		cardLayout = new CardLayout();
		setLayout(cardLayout);

        // adding <enter> key for focus transfer:
        Set forwardKeys = getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
        Set newForwardKeys = new HashSet(forwardKeys);
        newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
                newForwardKeys);

	}

    /**
     * Fügt dem CardLayout ein Panel hinzu
	 * @param view TabView mit CardLayout
	 * @param name Name des Panels im CardLayout
	 */
	public void addView(JComponent view, String name) {
		add(view, name);
	}
	
	/**
	 * Zeigt die gewünschte Seite (Panel) im Frame an
	 * @param cardname Name des Panels im CardLayout
	 */
	public void setCards(String cardname) {
        this.currentCardName = cardname;
        cardLayout.show(this, cardname);
    }

    public String getCurrentCardsName() {
        return this.currentCardName;
    }
}
