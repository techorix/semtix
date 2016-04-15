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

package org.semtix.shared.elements.control;

import javax.swing.*;

/**
 * InputVerifier zur Überprüfung des Textfeldes für die Emailadresse. Der InputVerifier überprüft 
 * die Gültigkeit der eingegebenen Zeichen nach Verlassen des Textfeldes (Fokusverlust).
 * 
 */
public class InputEmailVerifier
extends InputVerifier {

	
	
	
	
	
	@Override
	public boolean verify(JComponent input) {
		
		// Übergebene Komponente ist Textfeld
		JTextField tf = (JTextField) input;
		
		// Datumsstring aus Textfeld holen
		String email = tf.getText().trim();
		
		// Pattern mit Mailadresse vergleichen
		boolean foundMatch = email.matches("(?i)^[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");
		
		// Falls Mailadresse mit Pattern übereinstimmt oder leer ist...
		if(foundMatch || email.length()==0)
			return true;
		// ansonsten Fehlermeldung ausgeben.
		else{
			String message = "E-Mail-Adresse nicht korrekt!";
            JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
            return false;
		}
		

	}

}
