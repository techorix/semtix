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

package org.semtix.gui.filter;

import org.semtix.config.SemesterConf;
import org.semtix.config.UniConf;
import org.semtix.db.DBHandlerSemester;
import org.semtix.shared.daten.enums.AntragStatus;
import org.semtix.shared.daten.enums.FilterArt;
import org.semtix.shared.daten.enums.Status;
import org.semtix.shared.daten.enums.Uni;

import javax.swing.*;


/**
 * Klasse mit Werten für einen Filter
 */
public class Filter {

	private int semesterID;
	
	private Uni uni;
	
	private AntragStatus antragStatus;
	
	private FilterArt filterArt;
	
	private Status erstsemester;

	private Status nothilfe;

	private Status kulanz;

	private Status ratenzahlung;

	private Status nachreichung;

    private Status spaetis;

    private Status archiviert;

	private Status teilzuschuss;

	private Status barauszahler;

	private String buchstaben;

	
	/**
     * Erstellt einen FiltergetAntragIndexListe
     */
	public Filter() {
		
		// Filter auf Standard setzen
		reset();
	
	}
	
	/**
     * Liefert die im Filter gesetzte Universität
     * @return Universität
     */
    public Uni getUni() {

        return uni;
    }
	
	/**
     * Setzt im Filter die Universität
     * @param uni Universität
     */
    public void setUni(Uni uni) {

        this.uni = uni;

	}
	
	/**
     * Liefert die im Filter gesetzte Semester-ID
     * @return Semester-ID
     */
    public int getSemesterID() {

        return semesterID;

	}
	
	/**
     * Setzt im Filter die Semester-ID
     * @param semesterID Semester-ID
     */
    public void setSemesterID(int semesterID) {

        this.semesterID = semesterID;

	}

	/**
     * Liefert den im Filter gesetzten AntragStatus
     * @return AntragStatus
     */
    public AntragStatus getAntragStatus() {

        return antragStatus;

	}
	
	/**
     * Setzt im Filter den AntragStatus
     * @param antragStatus AntragStatus
     */
    public void setAntragStatus(AntragStatus antragStatus) {

        this.antragStatus = antragStatus;

	}
	
	/**
     * Liefert die im Filter gesetze FilterArt (siehe Enum filter.enums.FilterArt)
     * @return FilterArt
     */
    public FilterArt getFilterArt() {

        return filterArt;

	}
	
	/**
     * Setzt im Filter die FilterArt (siehe Enum filter.enums.FilterArt)
     * @param filterArt FilterArt
     */
    public void setFilterArt(FilterArt filterArt) {

        this.filterArt = filterArt;

	}
	
	/**
     * Liefert die im Filter gesetzten Buchstaben (Buchstabenverteilung)
     * @return Buchstaben
     */
    public String getBuchstaben() {

        return buchstaben;

	}
	
	/**
     * Setzt im Filter die Buchstaben (Buchstabenverteilung)
     * @param buchstaben Buchstaben
     */
    public void setBuchstaben(String buchstaben) {

        this.buchstaben = buchstaben;

	}
	
	/**
	 * Filter auf Standard zurücksetzen
	 */
	public void reset() {

		uni = UniConf.aktuelleUni;
		semesterID = SemesterConf.getSemester().getSemesterID();

		setErstsemester(Status.EGAL);
		setKulanz(Status.EGAL);
		setBarauszahler(Status.EGAL);
		setNachreichung(Status.EGAL);
		setNothilfe(Status.EGAL);
		setRatenzahlung(Status.EGAL);
		setSpaetis(Status.EGAL);
        setArchiviert(Status.EGAL);
		setTeilzuschuss(Status.EGAL);
		filterArt = null;
		setBuchstaben("alle");
		
	}
	
	
	/**
	 * Liefert den Text zum Filter
	 * @return Text was gefiltert wurde
	 */
	public String getText() {
		
		String filterText = "";

        String antragstatusText;

        String semester = "";

        if (semesterID == -1) {
            semester = "alle";
        } else {
            DBHandlerSemester dbHandlerSemester = new DBHandlerSemester();
            try {
                semester = (dbHandlerSemester.readSemester(semesterID)).getSemesterKurzform();
            } catch (NullPointerException npe) {
                JOptionPane.showMessageDialog(null, "Fehler: Bitte übprüfen ob ein (globales) Semester gesetzt wurde.");
            }
        }

		
		if(antragStatus == null)
			antragstatusText = "alle";
		else
			antragstatusText = antragStatus.toString();
		
		if(filterArt != null)
			filterText += "Filter: " + filterArt + " | ";
		
		filterText += "Semester: " + semester + " | ";
		
		filterText += "Antragstatus: " + antragstatusText + " | ";
		
		filterText += "Erstsemester: " + getErstsemester().toString() + " | ";
		
		if(getBuchstaben().equals("alle"))
			filterText += "Buchstaben: alle | ";
		else
			filterText += "Buchstaben: " + getBuchstaben() + " | ";
		
		
		return filterText;
		
	}


    public Status getErstsemester() {
        return erstsemester;
    }

	public void setErstsemester(Status erstsemester) {
		this.erstsemester = erstsemester;
	}

	public Status getNothilfe() {
		return nothilfe;
	}

	public void setNothilfe(Status nothilfe) {
		this.nothilfe = nothilfe;
	}

	public Status getKulanz() {
		return kulanz;
	}

	public void setKulanz(Status kulanz) {
		this.kulanz = kulanz;
	}

	public Status getRatenzahlung() {
		return ratenzahlung;
	}

	public void setRatenzahlung(Status ratenzahlung) {
		this.ratenzahlung = ratenzahlung;
	}

	public Status getNachreichung() {
		return nachreichung;
	}

	public void setNachreichung(Status nachreichung) {
		this.nachreichung = nachreichung;
	}

	public Status getSpaetis() {
		return spaetis;
	}

	public void setSpaetis(Status spaetis) {
		this.spaetis = spaetis;
	}

	public Status getBarauszahler() {
		return barauszahler;
	}

	public void setBarauszahler(Status barauszahler) {
		this.barauszahler = barauszahler;
	}

    public Status getArchiviert() {
        return archiviert;
    }

    public void setArchiviert(Status archiviert) {
        this.archiviert = archiviert;
    }

	public Status getTeilzuschuss() {
		return teilzuschuss;
	}

	public void setTeilzuschuss(Status teilzuschuss) {
		this.teilzuschuss = teilzuschuss;
	}
}
