/*

  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
         Semesterticketbüro der Humboldt-Universität Berlin

  Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package org.semtix.shared.elements;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Formatiertes Textfeld zur eingabe von Geldbeträgen.
 *
 */
@SuppressWarnings("serial")
public class NewCurrencyField
		extends JFormattedTextField implements ActionListener {

	
	// Schriftfarbe bei positivem bzw. negativem Wert
	private Color colorPlus = Color.BLACK;
	private Color colorMinus = Color.RED;
	
	// Hintergrundfarbe des Textfeldes
	private Color backgroundColorActive = new Color(255, 255, 190);
	private Color backgroundColorInActive = Color.WHITE;


	/**
	 * Konstruktor mit Spaltenanzahl des Textfeldes
	 * @param columns Anzahl Zeichen
	 */
	public NewCurrencyField(int columns) {
		this(BigDecimal.ZERO, columns);
	}
	
	
	/**
	 * Konstruktor mit Vorgabewert und Textfeldgrösse
	 * @param bigd Vorgabewert (Geldbetrag)
	 * @param columns Anzahl Zeichen
	 */
	public NewCurrencyField(BigDecimal bigd, int columns) {
		
		// Formatter für Standard und Display
		NumberFormatter displayFormatter = new NumberFormatter(NumberFormat.getCurrencyInstance(Locale.GERMANY));

		// Formatter bei Eingabe
		DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
		dfs.setDecimalSeparator(',');
		DecimalFormat dm = new DecimalFormat("0.00", dfs);
		dm.setCurrency(Currency.getInstance(Locale.GERMANY));
		NumberFormatter editFormatter = new NumberFormatter(dm);
		editFormatter.setOverwriteMode(true);
		editFormatter.setCommitsOnValidEdit(true);

		// FormatterFactory setzen (standard, display, edit)
		setFormatterFactory(new DefaultFormatterFactory(displayFormatter, displayFormatter, editFormatter));

		// BigDecimal-Wert setzen
		setValue(bigd);
		
		// Grösse des Textfeldes setzen (Spaltenanzahl)
		setColumns(columns);

        setPreferredSize(new Dimension(70, 24));

        setMinimumSize(getPreferredSize());

        setMaximumSize(getPreferredSize());

		// Rahmen mit Linie aussen um das Textfeld
		Border borderLine = BorderFactory.createLineBorder(Color.BLACK, 1);
		
		// Innenabstand im Textfeld
		Border borderEmpty = BorderFactory.createEmptyBorder(0, 0, 0, 5);
		
		// Zusammengesetzter Rahmen: Linie aussen und Innenabstand
		setBorder(BorderFactory.createCompoundBorder(borderLine, borderEmpty));

		// Ausrichtung im Textfeld rechtsbündig
		setHorizontalAlignment(JLabel.RIGHT);

		//UP+DOWN ARROW FOR FOCUS TRAVERSAL
		Set<AWTKeyStroke> forwardKeys = new HashSet<>(getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
		forwardKeys.add(AWTKeyStroke.getAWTKeyStroke(10, 0));
		forwardKeys.add(AWTKeyStroke.getAWTKeyStroke(40, 0));
		Set<AWTKeyStroke> backwardKeys = new HashSet<>(getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
		backwardKeys.add(AWTKeyStroke.getAWTKeyStroke(38, 0));

		setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);
		setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys);

		this.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				// Hintergrundfarbe bei Fokuserhalt setzen
				setBackground(backgroundColorActive);
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						selectAll();
					}
				});
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (getText().length() == 0)
					setBValue(BigDecimal.ZERO);


				if (getBValue().compareTo(BigDecimal.ZERO) < 0)
					setForeground(colorMinus);
				else
					setForeground(colorPlus);

				// Hintergrundfarbe bei Fokusverlust wieder auf weiss setzen
				setBackground(backgroundColorInActive);
			}
		});
	}


	/**
	 * Liefert Wert im Textfeld zurück
	 * @return Wert im Textfeld
	 */
	public BigDecimal getBValue() {

		if (getValue() instanceof String)
			return new BigDecimal((String) getValue());

		if(getValue() instanceof Double)
			return new BigDecimal(Double.toString((Double) getValue()));
		
		if(getValue() instanceof Long)
			return new BigDecimal(Long.toString((Long) getValue()));
		
		if(getValue() instanceof BigDecimal)
			return (BigDecimal) getValue();

		return null;
	}
	
	
	
	
	
	/**
	 * Setzt einen Wert ins Textfeld
	 * @param value Geldbetrag der ins Textfeld gestzt wird
	 */
	public void setBValue(BigDecimal value) {
		if (value == null) {
			setValue(BigDecimal.ZERO);
		} else {
			setValue(value);
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		transferFocus();
	}
}
