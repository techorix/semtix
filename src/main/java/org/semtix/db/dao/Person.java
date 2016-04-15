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

import org.semtix.config.UniConf;
import org.semtix.shared.daten.StringHelper;
import org.semtix.shared.daten.enums.Uni;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Klasse für Objekte von Personen (entspricht der Datenbanktabelle personensuche).
 * 
 * <p>In diesen Objekten können alle Werte gespeichert werden, die zu einer bestimmten Person gehören.</p>
 *
 * <p><b>Geburtsdatum:</b><br>
 * Für die Eingabe des Geburtsdatum im PersonenFormular gibt es verschiedene Eingabemöglichkeiten, die 
 * in {@link org.semtix.shared.elements.control.InputDateVerifier} festgelegt sind.</p>
 *  
 */
@Entity
public class Person
        implements Cloneable, Serializable, Comparable {


    @Id
    @SequenceGenerator(name = "personID_seq",
            sequenceName = "personID_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "personID_seq")
    private int personID;							// ID für die PersonAntrag (Primärschlüssel in Datenbank)

    @Enumerated
    private Uni uni;								// Universität, in welcher die Person eingeschrieben ist

	//TODO optional: die Anmerkungen zur Person hier verknüpfen

	//Matrikelnummer sollte unique sein. Hibernate kann das nicht checken, sollte man auf Datenbankebene machen, bzw. kann beim Auto-Anlegen über Hibernate gehen
	@Column(unique = true)
    private String matrikelnr;						// Matrikelnummer
	private String nachname;						// Nachname
	private String vorname;							// Vorname
	private String co;								// c/o (wohnhaft bei)
	private String strasse;							// Strasse
	private String wohneinheit;						// Wohneinheit (sonstige Zusätze zur Wohnungsbestimmung)
	private String plz;								// Postleitzahl
	private String wohnort;							// Wohnort
	private String land;							// Land
	private String email;							// Emailadresse

	//TODO: statt werte hier, besser verknüpfung zu Kontodaten model

	private String kontoInhaber_N;					// Name Kontoinhaber_in (falls von Antragsteller_in abweichend)
	private String kontoInhaber_S;					// Strasse Kontoinhaber_in (falls von Antragsteller_in abweichend)
	private String kontoInhaber_W;					// Wohnort Kontoinhaber_in (falls von Antragsteller_in abweichend)
	private String iban;							// Bankverbindung IBAN
	private String bic;								// Bankverbindung BIC

	private GregorianCalendar gebdatum;				// Geburtsdatum
	private GregorianCalendar datumAngelegt;		// Zeitstempel, wann Person angelegt wurde
	private GregorianCalendar datumGeaendert;		// Zeitstempel, wann Person zuletzt geändert wurde
	
	private int userAngelegt;						// User-ID, wer die Person angelegt hat
	private int userGeaendert;						// User-ID, wer die Person zuletzt geändert hat

	private boolean englischsprachig;				// englischsprachig
    private boolean hatTelefon;                        // Telefon vorhanden?
    private boolean barauszahler;                    // Barauszahlung erwünscht?
    private boolean archiviert;                     // ist die Person archiviert?


	/**
	 * Standardkonstruktor mit Startwerten
	 */
	public Person() {
		this("", "", "");
	}

	
	/**
	 * Konstruktor für neuanzulegende Personen (nur Vorname, Nachname, Matrikelnumer)
	 *
	 * @param matrikelnr Matrikelnummer
	 * @param nachname Nachname
	 * @param vorname Vorname
	 */
	public Person(String matrikelnr, String nachname, String vorname) {

		this.matrikelnr = matrikelnr;
		this.nachname = nachname;
		this.vorname = vorname;

		// Standardwerte bei neuangelegten Personen
		this.uni = UniConf.aktuelleUni;
		this.personID = -1;
		this.co = "";
		this.strasse = "";
		this.wohneinheit = "";
		this.plz = "";
		this.wohnort = "";
		this.land = "";
		this.email = "";
		this.kontoInhaber_N = "";
		this.kontoInhaber_S = "";
		this.kontoInhaber_W = "";
        this.setIban("");
        this.setBic("");


	}

	/**
	 * Konstruktor für neuanzulegende Personen (nur Vorname, Nachname, Matrikelnumer)
	 *
     * @param matrikelnr Matrikelnummer
     * @param nachname Nachname
     * @param vorname Vorname
     * @param gebdatum Geburtsdatum
	 */
	public Person(String matrikelnr, String nachname, String vorname, GregorianCalendar gebdatum) {

		this(matrikelnr, nachname, vorname);

		this.gebdatum = gebdatum;

	}

	/**
	 * Konstruktor mit Feldwerten als Parameter
	 * @param personID ID für die PersonAntrag (Primärschlüssel in Datenbank)
	 * @param matrikelnr Matrikelnummer
	 * @param uni Universität, in welcher die Person eingeschrieben ist
	 * @param nachname Nachname
	 * @param vorname Vorname
	 * @param gebdatum Geburtsdatum
	 * @param englischsprachig englischsprachig
	 * @param co c/o (wohnhaft bei)
	 * @param strasse Strasse
	 * @param wohneinheit Wohneinheit (sonstige Zusätze zur Wohnungsbestimmung)
	 * @param plz Postleitzahl
	 * @param wohnort Wohnort
	 * @param land Land
     * @param hatTelefon Telefon vorhanden?
     * @param email Emailadresse
     * @param barauszahler Barauszahlung erwünscht?
     * @param userAngelegt User-ID, wer die Person angelegt hat (Fremdschlüssel)
	 * @param datumAngelegt Zeitstempel, wann Person angelegt wurde
	 * @param userGeaendert User-ID, wer die Person zuletzt geändert hat (Fremdschlüssel)
	 * @param datumGeaendert Zeitstempel, wann Person zuletzt geändert wurde
	 * @param iban Bankverbindung IBAN
	 * @param bic Bankverbindung BIC
	 * @param kontoInhaber_N Name Kontoinhaber_in (falls von Antragsteller_in abweichend)
	 * @param kontoInhaber_S Strasse Kontoinhaber_in (falls von Antragsteller_in abweichend)
	 * @param kontoInhaber_W Wohnort Kontoinhaber_in (falls von Antragsteller_in abweichend)
	 */
	public Person(int personID, String matrikelnr, Uni uni,
			String nachname, String vorname, GregorianCalendar gebdatum,
			boolean englischsprachig, String co, String strasse, String wohneinheit,
            String plz, String wohnort, String land, boolean hatTelefon,
            String email, boolean barauszahler,
            int userAngelegt, GregorianCalendar datumAngelegt,
			int userGeaendert, GregorianCalendar datumGeaendert,
			String iban, String bic,
            String kontoInhaber_N, String kontoInhaber_S, String kontoInhaber_W) {

        this.personID = personID;
		this.matrikelnr = matrikelnr;
		this.uni = uni;
		this.nachname = nachname;
		this.vorname = vorname;
		this.gebdatum = gebdatum;
		this.englischsprachig = englischsprachig;
		this.co = co;
		this.strasse = strasse;
		this.wohneinheit = wohneinheit;
		this.plz = plz;
		this.wohnort = wohnort;
		this.land = land;
        this.hatTelefon = hatTelefon;
        this.email = email;
        this.barauszahler = barauszahler;
        this.userAngelegt = userAngelegt;
		this.datumAngelegt = datumAngelegt;
		this.userGeaendert = userGeaendert;
		this.datumGeaendert = datumGeaendert;
        this.setIban(iban);
        this.setBic(bic);
        this.kontoInhaber_N = kontoInhaber_N;
		this.kontoInhaber_S = kontoInhaber_S;
		this.kontoInhaber_W = kontoInhaber_W;
	}

	
	

    public Person(int personID, String nachname, String vorname, String email, String matrikelnr) {
        this(matrikelnr, nachname,vorname);
        this.email = email;
        this.personID = personID;

    }

    public Person(int personID, String nachname, String vorname, String matrikelnr) {
        this(matrikelnr, nachname,vorname);
        this.personID = personID;

    }

	public Person(int personID, String nachname, String vorname, String matrikelnr, GregorianCalendar gebdatum) {
		this(matrikelnr, nachname,vorname,gebdatum);
		this.personID = personID;
	}

	public Person(int personID, String nachname, String vorname, String email, String matrikelnr, GregorianCalendar gebdatum) {
		this(personID, nachname,vorname,matrikelnr,gebdatum);
		this.email = email;

	}

    // Kopie von Objekt Person erstellen
    public Person clone() throws CloneNotSupportedException {
        return (Person) super.clone();
    }

	
	

	/**
	 * Liefert die Universität, in welcher die Person eingeschrieben ist
	 * @return Universität
	 */
	public Uni getUni() {
		return uni;
	}


	/**
	 * Setzt die Universität, in welcher die Person eingeschrieben ist
	 * @param uni Universität
	 */
	public void setUni(Uni uni) {
		this.uni = uni;
	}


	/**
	 * Liefert die ID der Person (Primärschlüssel in Datenbank)
	 * @return ID der Person
	 */
	public int getPersonID() {
		return personID;
	}


	/**
	 * Setzt die ID der Person (Primärschlüssel in Datenbank)
	 * @param personID ID der Person
	 */
	public void setPersonID(int personID) {
		this.personID = personID;
	}


	/**
	 * Liefert User-ID, wer die Person angelegt hat (Fremdschlüssel)
	 * @return User-ID
	 */
	public int getUserAngelegt() {
		return userAngelegt;
	}


	/**
	 * Setzt User-ID, wer die Person angelegt hat (Fremdschlüssel)
	 * @param userAngelegt User-ID
	 */
	public void setUserAngelegt(int userAngelegt) {
		this.userAngelegt = userAngelegt;
	}


	/**
	 * Liefert User-ID, wer die Person zuletzt geändert hat (Fremdschlüssel)
	 * @return User-ID
	 */
	public int getUserGeaendert() {
		return userGeaendert;
	}


	/**
	 * Setzt User-ID, wer die Person zuletzt geändert hat (Fremdschlüssel)
	 * @param userGeaendert User-ID
	 */
	public void setUserGeaendert(int userGeaendert) {
		this.userGeaendert = userGeaendert;
	}

	/**
	 * Liefert die Matrikelnummer der Person
	 * @return Matrikelnummer
	 */
	public String getMatrikelnr() {
		return matrikelnr;
	}


	/**
	 * Setzt die Matrikelnummer der Person
	 * @param matrikelnr Matrikelnummer
	 */
	public void setMatrikelnr(String matrikelnr) {
		this.matrikelnr = matrikelnr;
	}


	/**
	 * Liefert Nachname der Person
	 * @return Nachname
	 */
	public String getNachname() {
		return nachname;
	}


	/**
	 * Setzt Nachname der Person
	 * @param nachname Nachname
	 */
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}


	/**
	 * Liefert Vorhname der Person
	 * @return Vorname
	 */
	public String getVorname() {
		return vorname;
	}


	/**
	 * Setzt Vorhname der Person
	 * @param vorname Vorname
	 */
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}


	/**
	 * Liefert c/o (wohnhaft bei)
	 * @return c/o
	 */
	public String getCo() {
		return co;
	}


	/**
	 * Setzt c/o (wohnhaft bei)
	 * @param co c/o
	 */
	public void setCo(String co) {
		this.co = co;
	}


	/**
	 * Liefert Strasse der Wohnanschrift
	 * @return Strasse
	 */
	public String getStrasse() {
		return strasse;
	}


	/**
	 * Setzt Strasse der Wohnanschrift
	 * @param strasse Strasse
	 */
	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}


	/**
	 * Liefert Wohneinheit (sonstige Zusätze zur Wohnungsbestimmung)
	 * @return Wohneinheit
	 */
	public String getWohneinheit() {
		return wohneinheit;
	}


	/**
	 * Setzt Wohneinheit (sonstige Zusätze zur Wohnungsbestimmung)
	 * @param wohneinheit Wohneinheit
	 */
	public void setWohneinheit(String wohneinheit) {
		this.wohneinheit = wohneinheit;
	}


	/**
	 * Liefert Postleitzahl
	 * @return Postleitzahl
	 */
	public String getPlz() {
		return plz;
	}


	/**
	 * Setzt Postleitzahl
	 * @param plz Postleitzahl
	 */
	public void setPlz(String plz) {
		this.plz = plz;
	}


	/**
	 * Liefert Wohnort der Person
	 * @return Wohnort
	 */
	public String getWohnort() {
		return wohnort;
	}


	/**
	 * Setzt Wohnort der Person
	 * @param wohnort Wohnort
	 */
	public void setWohnort(String wohnort) {
		this.wohnort = wohnort;
	}


	/**
	 * Liefert Land
	 * @return Land
	 */
	public String getLand() {
		return land;
	}


	/**
	 * Setzt Land
	 * @param land Land
	 */
	public void setLand(String land) {
		this.land = land;
	}


	/**
	 * Liefert Emailadresse der Person
	 * @return Emailadresse
	 */
	public String getEmail() {
		return email;
	}


	/**
	 * Setzt Emailadresse der Person
	 * @param email Emailadresse
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	/**
	 * Liefert IBAN der Bankverbindung der Person (entspricht Kontonummer nach SEPA)
	 * @return IBAN
	 */
	public String getIBAN() {
        return getIban();
    }


	/**
	 * Setzt IBAN der Bankverbindung der Person (entspricht Kontonummer nach SEPA)
	 * @param iban IBAN
	 */
	public void setIBAN(String iban) {
        this.setIban(iban);
    }
	
	
	/**
	 * Liefert BIC der Bankverbindung der Person (entspricht Bankleitzahl nach SEPA)
	 * @return BIC
	 */
	public String getBIC() {
        return getBic();
    }


	/**
	 * Setzt BIC der Bankverbindung der Person (entspricht Bankleitzahl nach SEPA)
	 * @param bic BIC
	 */
	public void setBIC(String bic) {
        this.setBic(bic);
    }
	
	/**
	 * Liefert Namen von Antragsteller_in abweichende Kontoinhaber_in
	 * @return Namen von Antragsteller_in abweichende Kontoinhaber_in
	 */
	public String getKontoInhaber_N() {
		return kontoInhaber_N;
	}


	/**
	 * Setzt Namen von Antragsteller_in abweichende Kontoinhaber_in
	 * @param kontoInhaberN Namen von Antragsteller_in abweichende Kontoinhaber_in
	 */
	public void setKontoInhaber_N(String kontoInhaberN) {
		kontoInhaber_N = kontoInhaberN;
	}


	/**
	 * Liefert Strasse von Antragsteller_in abweichende Kontoinhaber_in
	 * @return Strasse von Antragsteller_in abweichende Kontoinhaber_in
	 */
	public String getKontoInhaber_S() {
		return kontoInhaber_S;
	}


	/**
	 * Setzt Strasse von Antragsteller_in abweichende Kontoinhaber_in
	 * @param kontoInhaberS Strasse von Antragsteller_in abweichende Kontoinhaber_in
	 */
	public void setKontoInhaber_S(String kontoInhaberS) {
		kontoInhaber_S = kontoInhaberS;
	}


	/**
	 * Liefert Wohnort von Antragsteller_in abweichende Kontoinhaber_in
	 * @return Wohnort von Antragsteller_in abweichende Kontoinhaber_in
	 */
	public String getKontoInhaber_W() {
		return kontoInhaber_W;
	}


	/**
	 * Setzt Wohnort von Antragsteller_in abweichende Kontoinhaber_in
	 * @param kontoInhaberW Wohnort von Antragsteller_in abweichende Kontoinhaber_in
	 */
	public void setKontoInhaber_W(String kontoInhaberW) {
		kontoInhaber_W = kontoInhaberW;
    }

	public String getKontoInhaber_Name() {
		return kontoInhaber_N;
	}


	public void setKontoInhaber_Name(String kontoInhaberName) {
		kontoInhaber_N = kontoInhaberName;
	}


	public String getKontoInhaber_Strasse() {
		return kontoInhaber_S;
	}


	public void setKontoInhaber_Strasse(String kontoInhaberS) {
		kontoInhaber_S = kontoInhaberS;
	}


	public String getKontoInhaber_Wohnort() {
		return kontoInhaber_W;
	}


	public void setKontoInhaber_Wohnort(String kontoInhaberW) {
		kontoInhaber_W = kontoInhaberW;
	}


	/**
	 * Liefert Geburtsdatum der Person
	 * @return Geburtsdatum
	 */
	public GregorianCalendar getGebdatum() {
		return gebdatum;
	}
	
	/**
     * Setzt Geburtsdatum der Person
     * @param gebdatum Geburtsdatum
     */
    public void setGebdatum(GregorianCalendar gebdatum) {
        this.gebdatum = gebdatum;
    }

    /**
     * Liefert formatiertes Geburtsdatum (Tag.Monat.Jahr)
	 * @return Geburtsdatum als String
	 */
	public String getFormattedGebdatum() {
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		Date myDate = gebdatum.getTime();
		return df.format(myDate);
	}

	/**
	 * Liefert Zeitstempel, wann Person angelegt wurde
	 * @return Zeitstempel
	 */
	public GregorianCalendar getDatumAngelegt() {
		return datumAngelegt;
	}


	/**
	 * Setzt Zeitstempel, wann Person angelegt wurde
	 * @param datumAngelegt Zeitstempel
	 */
	public void setDatumAngelegt(GregorianCalendar datumAngelegt) {
		this.datumAngelegt = datumAngelegt;
	}


	/**
	 * Liefert Zeitstempel, wann Person zuletzt geändert wurde
	 * @return Zeitstempel
	 */
	public GregorianCalendar getDatumGeaendert() {
		return datumGeaendert;
	}


	/**
	 * Setzt Zeitstempel, wann Person zuletzt geändert  wurde
	 * @param datumGeaendert Zeitstempel
	 */
	public void setDatumGeaendert(GregorianCalendar datumGeaendert) {
		this.datumGeaendert = datumGeaendert;
	}


	/**
	 * Liefert ob Person englischsprachig ist
	 * @return englischsprachig ja/nein
	 */
	public boolean isEnglischsprachig() {
		return englischsprachig;
	}


	/**
	 * Setzt ob Person englischsprachig ist
	 * @param englischsprachig englischsprachig ja/nein
	 */
	public void setEnglischsprachig(boolean englischsprachig) {
		this.englischsprachig = englischsprachig;
	}


	/**
	 * Liefert ob Telefon vorhanden ist
	 * @return Telefon vorhanden ja/nein
	 */
    public boolean isHatTelefon() {
        return hatTelefon;
    }


	/**
	 * Setzt ob Telefon vorhanden ist
     * @param hatTelefon Telefon vorhanden ja/nein
     */
    public void setHatTelefon(boolean hatTelefon) {
        this.hatTelefon = hatTelefon;
    }


	/**
	 * Liefert ob Barauszahlung erwünscht ist
	 * @return Barauszahlung erwünscht ja/nein
	 */
    public boolean isBarauszahler() {
        return barauszahler;
    }


	/**
	 * Setzt ob Barauszahlung erwünscht ist
     * @param barauszahler Barauszahlung erwünscht ja/nein
     */
    public void setBarauszahler(boolean barauszahler) {
        this.barauszahler = barauszahler;
    }


	/**
	 * Liefert String mit zusammengesetztem Namen (Nachname, Vorname)
	 */
	public String toString() {
		return nachname + ", " + vorname;
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

    /**
     * Archivierte Personen sollten in der normalen Suche nicht mehr auftauchen
     *
     * @return true or false
     */
    public boolean isArchiviert() {
        return archiviert;
    }

    /**
     * Archivierte Personen sollten in der normalen Suche nicht mehr auftauchen
     *
     * @param archiviert ist archiviert oder nicht
     */
    public void setArchiviert(boolean archiviert) {
        this.archiviert = archiviert;
    }

    @Override
    public boolean equals(Object o) {
        return this.getPersonID() == ((Person) o).getPersonID();
    }

    @Override
    public int compareTo(Object o) {
        if (equals(o)) {
            return 0;
        } else {
            return StringHelper.removeDiacriticalMarks(this.getNachname().toLowerCase().trim()).
                    compareTo(StringHelper.removeDiacriticalMarks(((Person) o).getNachname().toLowerCase().trim()));
        }
    }
}
