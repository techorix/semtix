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

package org.semtix.shared.elements;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Zeichnet Label mit vertikal gedrehter Schrift
 */
@SuppressWarnings("serial")
public class LabelVertical extends JLabel {

    private String text;

    public LabelVertical(String s) {

        text = s;
        setPreferredSize(new Dimension(24, 120));
        setMinimumSize(this.getPreferredSize());

    }

    public void paintComponent(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform aTransform = new AffineTransform();
        aTransform.setToTranslation(12.0, 60.0); // Mittelpunkt des Labels, um den gedreht wird
        g2d.transform(aTransform);
        aTransform.setToRotation(-Math.PI / 2.0);  //entsprechen -90° ; PI is für Kreis und Pi/2 is halber ;)
        g2d.transform(aTransform);               // das muss nochmal hin, sonst gehts nicht
        g2d.setFont(new Font(Font.DIALOG, Font.PLAIN, 11));
        g2d.drawString(text, -55.0f, 4.0f);         // Text mit Position

    }

}
