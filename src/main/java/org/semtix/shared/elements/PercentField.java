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
import javax.swing.border.Border;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Texteingabefeld für Prozentwerte. Bei Fokuserhalt erhält das Eingabefeld einen farbigen Hintergrund.
 * Der Eingabe- und Ausgabewert soll als BigDecimal erfolgen.
 */
@SuppressWarnings("serial")
public class PercentField
extends JFormattedTextField
implements ActionListener {
	

	// Schriftfarbe bei positivem bzw. negativem Wert
	private Color colorPlus = Color.BLACK;
	private Color colorMinus = Color.RED;
	
	// Hintergrundfarbe des Textfeldes bei Fokuserhalt
	private Color backgroundColor = new Color(255, 255, 190);


	
	/**
	 * Konstruktor nur mit Anzahl der Stellen des Textfeldes
	 * @param columns Stellenanzahl
	 */
	public PercentField(int columns) {
		this(new BigDecimal("0.0"), columns);
	}

	
	// Konstruktor mit Vorgabewert, Rahmen und Textfeldgrösse
	public PercentField(BigDecimal bigd, int columns) {
		
		
		// Formatter für Standard und Display
		//NumberFormatter displayFormatter = new NumberFormatter(NumberFormat.getPercentInstance(Locale.GERMANY));
		NumberFormatter displayFormatter = new NumberFormatter(new DecimalFormat("0.00"));
		
		// Formatter für Eingabe von Beträgen (edit)
		NumberFormatter editFormatter = new NumberFormatter(new DecimalFormat("0.00"));
		
		// Für editFormatter keine ungültigen Zeichen zulassen
		editFormatter.setAllowsInvalid(false);
		
		// FormatterFactory setzen (standard, display, edit)
		setFormatterFactory(new DefaultFormatterFactory(displayFormatter, displayFormatter, editFormatter));
		
		// BigDecimal-Wert setzen
		setValue(bigd);
		
		// Grösse des Textfeldes setzen (Spaltenanzahl)
		setColumns(columns);
		
		//setPreferredSize(new Dimension(60, 24));
		
		// Rahmen mit Linie aussen um das Textfeld
		Border borderLine = BorderFactory.createLineBorder(Color.BLACK, 1);
		
		// Innenabstand im Textfeld
		Border borderEmpty = BorderFactory.createEmptyBorder(2, 2, 2, 5);
		
		// Zusammengesetzter Rahmen: Linie aussen und Innenabstand
		setBorder(BorderFactory.createCompoundBorder(borderLine, borderEmpty));

		// Ausrichtung im Textfeld rechtsbündig
		setHorizontalAlignment(JLabel.RIGHT);
		
		// ActionListener ans Textfeld hängen (Reaktion auf ENTER-Taste)
		addActionListener(this);
		
		// FocusListener ans Textfeld hängen (Reaktionen auf Fokuserhalt/Fokusverlust)
		//addFocusListener(this);
				
	}
	
	

	/**
	 * Liefert den momentan im Textfeld eingetragenen Wert zurück.
	 * @return Wert im Textfeld als BigDecimal
	 */
	public BigDecimal getBValue() {

		//String stringValue = "0.0";
		BigDecimal bd = new BigDecimal("0.0");

		if(getValue() instanceof Double)
			bd = new BigDecimal(Double.toString((Double) getValue()));
			//stringValue = Double.toString((Double) getValue());
		
		if(getValue() instanceof Long)
			bd = new BigDecimal(Long.toString((Long) getValue()));
			//stringValue = Long.toString((Long) getValue());
		
		if(getValue() instanceof BigDecimal)
			bd = (BigDecimal) getValue();

		return bd;
		//return new BigDecimal(stringValue);
	}
	
	
	

	/**
	 * Setzt einen Wert in das Textfeld
	 * @param value Wert als BigDecimal
	 */
	public void setBValue(BigDecimal value) {
		setValue(value);
	}





	/**
	 * Bei Drücken der ENTER-Taste springt der Fokus weiter
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		transferFocus();
	}
	
	
	
	
	/**
	 * Setzt Hintergrundfarbe und Selektion bei Fouserhalt bzw. -verlust beim Textfeld
	 * @param state Zustand Focus
	 */
	public void setFocus(boolean state) {
		
		if(state) {
			
			// Bei Fokuserhalt Textfeldinhalt selektieren
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	selectAll();
	            }
			});
			
			// Hintergrundfarbe bei Fokuserhalt setzen
			setBackground(backgroundColor);
			
		}
		else {
			
			// Bei Fokusverlust Textfarbe bei negativen Werten rot
			SwingUtilities.invokeLater(new Runnable() {

	            public void run() {
					if (getBValue().compareTo(BigDecimal.ZERO) < 0)
						setForeground(colorMinus);
					else
	        			setForeground(colorPlus);
	            }
			});
			
			// Hintergrundfarbe bei Fokusverlust wieder auf weiss setzen
			setBackground(Color.WHITE);
			
		}
		
	}



	
	/*
	@Override
	public void focusGained(FocusEvent arg0) {

		// Bei Fokuserhalt Textfeldinhalt selektieren
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	selectAll();
            }
		});
		
		// Hintergrundfarbe bei Fokuserhalt setzen
		setBackground(backgroundColor);

	}

	
	
	@Override
	public void focusLost(FocusEvent arg0) {
		
		// Bei Fokusverlust Textfarbe bei negativen Werten rot
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	if(((BigDecimal) getBValue()).compareTo(BigDecimal.ZERO)  < 0)
            		setForeground(colorMinus);
        		else
        			setForeground(colorPlus);
            }
		});
		
		// Hintergrundfarbe bei Fokusverlust wieder auf weiss setzen
		setBackground(Color.WHITE);
	}*/	

}
