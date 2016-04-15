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
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

/**
 * Filter für Textfelder zur Eingabe von Geldbeträgen. Der Filter reagiert während der Eingabe in das 
 * Textfeld und lässt nur das festgelegte Pattern und die angegebene Anzahl an Zeichen zu. Für den 
 * Geldbetrag kann eine maximale Anzahl an Ziffern vor dem Komma festgelegt werden. Es gibt immer 
 * 2 Nachkommastellen.
 * 
 * <p>Beispiel eines Textfeldes mit CurrencyFilter:</p>
 * <pre>
 * {@code
 *  // Textfeld Ticketpreis, max. 4 Ziffern vor und 2 nach dem Komma
 *	tfTicketpreis = new JTextField(10);
 *	((AbstractDocument) tfTicketpreis.getDocument()).setDocumentFilter(new CurrencyFilter(4));
 * }
 * </pre>
 */
public class CurrencyFilter
extends DocumentFilter {
	
	private String currencyPattern;

	    

	/**
	 * Erstellt einen neuen CurrencyFilter
	 * @param maxChars maximale Anzahl zulässige Ziffern vor dem Komma (immer 2 Nachkommastellen)
	 */
	public CurrencyFilter(int maxChars) {
		
		currencyPattern = "(?:[1-9][0-9]{0," + (maxChars-1) + "}|0)(?:,[0-9]{0,2})?";
		
	}

	
	@Override
    public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a)
            throws BadLocationException {
		
		
		Document doc = fb.getDocument();
	    String oldString = doc.getText(0,doc.getLength());
	    String newString = oldString.substring(0, offs) + str + 
	       (doc.getLength() > offs + length ? oldString.substring(offs+length) : "");       
	    if ( newString.matches(currencyPattern) || newString.isEmpty() ) {
	        super.replace(fb, offs, length, str, a);
	    }
        
    }

	
}
