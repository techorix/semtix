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


import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;


/**
 * Filter für Textfelder für eine bestimmte Zeichenlänge und ein Muster, welche Zeichen 
 * gültig für die Eingabe sind.
 * 
 * <p>Beispiel eines Textfeldes mit DocumentSizeFilter:</p>
 * <pre>
 * {@code
 *  // Textfeld Nachname, max. 50 Zeichen, Muster TEXT_PATTERN (Konstante)
 *  JTextField tfNachname = new JTextField(20);
 *  ((AbstractDocument) tfNachname.getDocument()).setDocumentFilter(new DocumentSizeFilter(50, DocumentSizeFilter.TEXT_PATTERN));
 * }
 * </pre>
 *
 */
public class DocumentSizeFilter
extends DocumentFilter {
	
	/**
	 * Pattern für Buchstaben, Ziffern, Sonderzeichen, Leerzeichen und Bindestrich
	 */
	public static final String ANYTEXT_PATTERN = ".+";
	
	/**
	 * Pattern für Buchstaben, Ziffern, Sonderzeichen, Leerzeichen und Bindestrich
	 */
	public static final String FULLTEXT_PATTERN = "[ A-Za-z0-9.,:;*-ş\\xC0-\\xFF]+";
	
	/**
	 * Pattern für Emailadressen
	 */
	public static final String EMAIL_PATTERN = "[A-Za-z0-9.@!#$%&'*+-/=?^_`{|}~]+";
	
	/**
	 * Pattern für Buchstaben, Sonderzeichen, Leerzeichen und Bindestrich
	 */
	public static final String TEXT_PATTERN = "[ A-Za-z-'\\xC0-\\xFF]+";
	
	/**
	 * Pattern nur für Buchstaben
	 */
	public static final String ONLY_TEXT_PATTERN = "[A-Za-z]+";

	/**
	 * Pattern nur für Buchstaben
	 */
    public static final String BIC_PATTERN = "[A-Z0-9]+";

    /**
	 * Pattern nur für Ziffern
	 */
	public static final String NUMBER_PATTERN = "[0-9-]+";
	
	/**
	 * Pattern nur für Ziffern über null (keine negative Zahlen)
	 *
	 */
	public static final String POSITIVE_NUMBER_PATTERN = "[0-9]+";

	public static final String DIGIT_PATTERN = "\\d*";
	/**
	 * Pattern für Dezimalzahlen mit Komma oder Punkt
	 */
	public static final String DECIMAL_PATTERN = "[0-9.,]+";
	
	/**
	 * Pattern für Datum (Zahlen und Punkt oder sogar Kommata)
	 */
    public static final String DATE_PATTERN = "[0-9.,]+";

    /**
	 * Pattern für Postleitzahlen (Buchstaben, Zahlen, Leerzeichen und Bindestrich)
	 */
	public static final String PLZ_PATTERN = "[ A-Za-z0-9-]+";


	protected int maxCharacters;
	protected String pattern;

	public DocumentSizeFilter() {

	}

	/**
	 * Erstellt einen neuen DocumentSizeFilter
     *
	 * @param maxChars maximal zulässige Zeichenlänge
	 * @param pattern Muster für Texteingabe
	 */
	public DocumentSizeFilter(final int maxChars, final String pattern) {
		maxCharacters = maxChars;
		this.pattern = pattern;
	}

	
	@Override
    public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a)
            throws BadLocationException {

		if (str.matches(pattern) && (fb.getDocument().getLength() + str.length() - length) <= maxCharacters || str.isEmpty()) {
            super.replace(fb, offs, length, str, a);
        }

	}
}

