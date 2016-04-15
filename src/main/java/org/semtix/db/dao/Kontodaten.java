/*
 *
 *  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 *         Semesterticketbüro der Humboldt-Universität Berlin
 *
 *  Copyright (c) 2015-2016 Michael Mertins (MichaelMertins@gmail.com)
 *  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)
 *
 *    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.semtix.db.dao;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * Created by Michael Mertins on 2/7/16.
 */
public class Kontodaten {


	@Id
	@SequenceGenerator(name = "kontoID_seq",
			sequenceName = "kontoID_seq",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "kontoID_seq")
	private int kontoID;                                // ID  (Primärschlüssel in Datenbank)
	private String strasse;                    // Strasse Kontoinhaber_in (falls von Antragsteller_in abweichend)
	private String wohnort;                    // Wohnort Kontoinhaber_in (falls von Antragsteller_in abweichend)
	private String iban;                            // Bankverbindung IBAN
	private String bic;                                // Bankverbindung BIC
	private String name;                    // Name Kontoinhaber_in (falls von Antragsteller_in abweichend)

	public int getKontoID() {
		return kontoID;
	}

	public void setKontoID(int kontoID) {
		this.kontoID = kontoID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getWohnort() {
		return wohnort;
	}

	public void setWohnort(String wohnort) {
		this.wohnort = wohnort;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}


}
