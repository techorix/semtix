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

package org.semtix.shared.print;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.util.List;

/**
 * Klasse zum Drucken von Komponenten (Panels).
 * Hiermit werden z.B. die Berechnungszettel ausgedruckt.
 */
public class PrintableBerechnungszettel implements Printable {
    
    private List<Component> componentsToPrint;

    private Logger logger = Logger.getLogger(PrintableBerechnungszettel.class);

    public PrintableBerechnungszettel(List<Component> componentsToPrint) {
        this.componentsToPrint = componentsToPrint;
	}

    public static void printComponents(List<Component> componentsToPrint) {
        new PrintableBerechnungszettel(componentsToPrint).print();
    }

    public static void disableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(false);
    }

    public static void enableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(true);
    }

    /**
     * PrinterJob erstellen und drucken
     */
    public void print() {

        PrinterJob printJob = PrinterJob.getPrinterJob();


        PageFormat pageFormat = printJob.defaultPage();  //new PageFormat();
      //pageFormat.setOrientation( PageFormat.LANDSCAPE );   //Längs- oder Querformat (Standard: längs)

        Paper a4PortraitPaper = new Paper();
        final double cm2inch = 0.3937;  // 1in = 2.54cm
        double paperHeight = 29.7 * cm2inch;
        double paperWidth = 21.0 * cm2inch;
        double margin = 1.5 * cm2inch;

        a4PortraitPaper.setSize(paperWidth * 72.0, paperHeight * 72.0);
        a4PortraitPaper.setImageableArea(margin * 72.0, margin * 72.0,
                          ( paperWidth - 2 * margin ) * 72.0,
                          ( paperHeight - 2 * margin ) * 72.0 );

        pageFormat.setPaper(a4PortraitPaper);

        printJob.setPrintable(this, pageFormat);

        try {
          printJob.print();
        } catch( PrinterException pe ) {
            logger.error("Fehler beim Drucken", pe);
        }

    }

    public int print( Graphics g, PageFormat pageFormat, int pageIndex ) {

        double gBreite, gHoehe;
        int b, h;
        double skalierung = 0.0;

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        gBreite = pageFormat.getImageableWidth();
        gHoehe = pageFormat.getImageableHeight();


        if (pageIndex < componentsToPrint.size()) {

            Component c = componentsToPrint.get(pageIndex);

            // ***** Skalierung *****

            b = c.getWidth();
            h = c.getHeight();

            skalierung = gBreite / b;

            g2d.scale(skalierung, skalierung);

            // ***** Ende Skalierung *****

    		disableDoubleBuffering( componentsToPrint.get( pageIndex ) );
    		componentsToPrint.get( pageIndex ).paint( g2d );
    		enableDoubleBuffering( componentsToPrint.get( pageIndex ) );
    		return PAGE_EXISTS;

        } else
            return NO_SUCH_PAGE;


    }
  }