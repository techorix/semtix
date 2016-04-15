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

/**
 * @(#)VerticalTableHeaderCellRenderer.java	1.0 02/26/09
 */
package org.semtix.shared.elements;

import javax.swing.*;
import javax.swing.RowSorter.SortKey;
import java.awt.*;

/**
 * A renderer for a JTableHeader with text rotated 90° counterclockwise.
 * <P>
 * Extends {@link DefaultTableHeaderCellRenderer}.
 *
 * @see VerticalLabelUI
 */
public class VerticalTableHeaderCellRenderer
        extends DefaultTableHeaderCellRenderer {

    /**
     * Constructs a <code>VerticalTableHeaderCellRenderer</code>.
     * <P>
     * The horizontal and vertical alignments and text positions are set as
     * appropriate to a vertical table header cell.
     *
     * @param b background white (true) , grey/default (false)
     */
    public VerticalTableHeaderCellRenderer(boolean b) {
        setHorizontalAlignment(LEFT);
        setHorizontalTextPosition(CENTER);
        setVerticalAlignment(CENTER);
        setVerticalTextPosition(TOP);
        setUI(new VerticalLabelUI());

        if (b)
            setBackground(Color.WHITE);
//      setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));


    }

    /**
     * Overridden to return a rotated version of the sort icon.
     *
     * @param table  the <code>JTable</code>.
     * @param column the colummn index.
     * @return the sort icon, or null if the column is unsorted.
     */
    @Override
    protected Icon getIcon(JTable table, int column) {
        SortKey sortKey = getSortKey(table, column);
        if (sortKey != null && table.convertColumnIndexToView(sortKey.getColumn()) == column) {
            SortOrder sortOrder = sortKey.getSortOrder();
            switch (sortOrder) {
                case ASCENDING:
                    return VerticalSortIcon.ASCENDING;
                case DESCENDING:
                    return VerticalSortIcon.DESCENDING;
            }
        }
        return null;
    }

    /**
     * An icon implementation to paint the contained icon rotated 90° clockwise.
     * <P>
     * This implementation assumes that the L&F provides ascending and
     * descending sort icons of identical size.
     */
    private enum VerticalSortIcon implements Icon {

        ASCENDING(UIManager.getIcon("Table.ascendingSortIcon")),
        DESCENDING(UIManager.getIcon("Table.descendingSortIcon"));
        private final Icon icon;// = ;

        VerticalSortIcon(Icon icon) {
            this.icon = icon;
        }

        /**
         * Paints an icon suitable for the header of a sorted table column,
         * rotated by 90° clockwise.  This rotation is applied to compensate
         * the rotation already applied to the passed in Graphics reference
         * by the VerticalLabelUI.
         * <P>
         * The icon is retrieved from the UIManager to obtain an icon
         * appropriate to the L&F.
         *
         * @param c the component to which the icon is to be rendered
         * @param g the graphics context
         * @param x the X coordinate of the icon's top-left corner
         * @param y the Y coordinate of the icon's top-left corner
         */
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            int maxSide = Math.max(getIconWidth(), getIconHeight());
            Graphics2D g2 = (Graphics2D) g.create(x, y, maxSide, maxSide);
            g2.rotate((Math.PI / 2));
            g2.translate(0, -maxSide);
            icon.paintIcon(c, g2, 0, 0);
            g2.dispose();
        }

        /**
         * Returns the width of the rotated icon.
         *
         * @return the <B>height</B> of the contained icon
         */
        @Override
        public int getIconWidth() {
            return icon.getIconHeight();
        }

        /**
         * Returns the height of the rotated icon.
         *
         * @return the <B>width</B> of the contained icon
         */
        @Override
        public int getIconHeight() {
            return icon.getIconWidth();
        }
    }
}
