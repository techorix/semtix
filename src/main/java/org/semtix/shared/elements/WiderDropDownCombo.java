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

public class WiderDropDownCombo extends JComboBox {

    private boolean layingOut = false;
    private int widestLengh = 0;
    private boolean wide = false;

    public WiderDropDownCombo( Object[] objs ) {
        super( objs );
    }

    public boolean isWide() {
        return wide;
    }

    /**
     * Is it wide or not?
     *
     * @param wide Is it wide or not?
     */
    public void setWide(boolean wide) {
        this.wide = wide;
        widestLengh = getWidestItemWidth();

    }
    public Dimension getSize(){
        Dimension dim = super.getSize();
        if(!layingOut && isWide())
            dim.width = Math.max( widestLengh, dim.width );
        return dim;
    }

    public int getWidestItemWidth()
    {

        int numOfItems = this.getItemCount();
        Font font = this.getFont();
        FontMetrics metrics = this.getFontMetrics( font );
        int widest = 0;
        for ( int i = 0; i < numOfItems; i++ )
        {
            Object item = this.getItemAt( i );
            int lineWidth = metrics.stringWidth( item.toString() );
            widest = Math.max( widest, lineWidth );
        }

        return widest + 15;
    }
    public void doLayout(){
        try{
            layingOut = true;
            super.doLayout();
        }finally{
            layingOut = false;
        }
    }

}