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

package org.semtix.db.dao;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Klasse mit Daten zu einem Textbaustein. Die Textbausteine haben eine ID, unter welcher sie 
 * in der Datenbank abgespeichert werden, und auch eine Parent-ID (übergeordnete Bezeichnung), um eine 
 * Baum-Struktur der Textbausteine darzustellen.
 */
@Entity
public class Textbaustein implements Serializable, Comparable {

    @Id
    @SequenceGenerator(name = "textbausteinID_seq",
            sequenceName = "textbausteinID_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "textbausteinID_seq")
    private int textbausteinID;

    private int parentID;
	private String bezeichnung;

    @Column(length=10000)
	private String text_de;

    @Column(length=10000)
    private String text_en;
	
	
	/**
	 * Leerer Standard-Konstruktor
	 */
	public Textbaustein() {
		
	}
	
	
	/**
	 * Erstellt ein Objekt Textbaustein mit Feldern der Klasse
	 * @param textbausteinID ID des Textbausteins
	 * @param parentID Übergeordnete ID des Textbausteins
	 * @param bezeichnung Name/Bezeichnung des Textbausteins
	 * @param text_de Text (deutsch) 
	 * @param text_en Text (englisch)
	 */
	public Textbaustein(int textbausteinID, int parentID, String bezeichnung, String text_de, String text_en) {
		
		this.textbausteinID = textbausteinID;
		this.parentID = parentID;
		this.bezeichnung = bezeichnung;
		this.text_de = text_de;
		this.text_en = text_en;
		
	}

    public Textbaustein(int parentID, String bezeichnung, String text_de, String text_en) {
        this.parentID = parentID;
        this.bezeichnung = bezeichnung;
        this.text_de = text_de;
        this.text_en = text_en;
    }


	
	/**
	 * Liefert die ID des Textbausteins.
	 * @return ID des Textbausteins
	 */
	public int getTextbausteinID() {
		return textbausteinID;
	}


	/**
	 * Setzt die ID des Textbausteins.
	 * @param textbausteinID ID des Textbausteins
	 */
	public void setTextbausteinID(int textbausteinID) {
		this.textbausteinID = textbausteinID;
	}
	
	
	/**
	 * Liefert die übergeordnete ID des Textbausteins.
	 * @return übergeordnete ID des Textbausteins
	 */
	public int getParentID() {
		return parentID;
	}


	/**
	 * Setzt die übergeordnete ID des Textbausteins.
	 * @param parentID übergeordnete ID des Textbausteins
	 */
	public void setParentID(int parentID) {
		this.parentID = parentID;
	}


	/**
	 * Liefert die Bezeichnung des Textbausteins.
	 * @return Bezeichnung des Textbausteins
	 */
	public String getBezeichnung() {
		return bezeichnung;
	}


	/**
	 * Setzt die Bezeichnung des Textbausteins.
	 * @param bezeichnung Bezeichnung des Textbausteins
	 */
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}


	/**
	 * Liefert den deutschsprachigen Text des Textbausteins.
	 * @return Textbaustein (deutsch)
	 */
	public String getText_de() {
		return text_de;
	}


	/**
	 * Setzt den deutschsprachigen Text des Textbausteins.
	 * @param text_de Textbaustein (deutsch)
	 */
	public void setText_de(String text_de) {
		this.text_de = text_de;
	}
	
	
	/**
	 * Liefert den englischsprachigen Text des Textbausteins.
	 * @return Textbaustein (englisch)
	 */
	public String getText_en() {
		return text_en;
	}


	/**
	 * Setzt den englischsprachigen Text des Textbausteins.
	 * @param text_en Textbaustein (englisch)
	 */
	public void setText_en(String text_en) {
		this.text_en = text_en;
	}

	
	/**
	 * Liefert die Bezeichnung des Textbausteins (Überschreibung der Methode totring).
	 * @return Bezeichnung des Textbausteins
	 */
	public String toString() {
		return bezeichnung;
	}

    public Textbaustein clone() {
        return new Textbaustein(textbausteinID, parentID, bezeichnung, text_de, text_en);
    }

	@Override
	public int compareTo(Object o) {
		return this.bezeichnung.compareToIgnoreCase(((Textbaustein) o).bezeichnung);
	}
}
