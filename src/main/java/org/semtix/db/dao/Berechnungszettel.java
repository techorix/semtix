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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entwurf für Datenbankspeicherung des Berechnungszettels
 * <br>
 * Alle einzelnen Werte sollen gespeichert werden.
 * <br>
 * Die Härten werden bereits im Antrag selber gespeichert.
 * <br>
 *
 * @author Michael Mertins
 */

@Entity
public class Berechnungszettel implements Serializable {


	@Id
	@SequenceGenerator(name = "berechnungszettelID_seq",
			sequenceName = "berechnungszettelID_seq",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "berechnungszettelID_seq")
	private int brzId;

	//Jeder Antrag hat 0 oder 1 Berechnungszettel
//    @OneToOne(fetch = FetchType.EAGER, mappedBy = "antragId", orphanRemoval = true)
//    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
	private int antragId;

	private int kinder;
	private int weiterePersonen;

	private boolean heizkostenPauschaleCheck;
	private boolean auslandskostenCheck;
	private boolean außerhalbABCCheck;

	@Column(name = "keinemietkappung", nullable = true, columnDefinition = "boolean default false")
	private boolean keineMietkappung;

	private String bemerkung;


	private boolean schuldenkappungCheck;


	@OneToMany(fetch = FetchType.EAGER, targetEntity = LabeledDecimalList.class, mappedBy = "brzId", orphanRemoval = true)
	@Fetch(FetchMode.JOIN)
	@OrderBy
	@Cascade(CascadeType.ALL)
	private List<LabeledDecimalList> berechnungsWerteListe;


	public Berechnungszettel() {

	}

	/**
	 * Konstruktor initialisiert Berechnungszettel mit Defaultwerten
	 *
	 * Die Antrag ID wird unbedingt benötigt
	 *
	 * Contraint/Invariant: Ein Berechnungszettel muss immer zu genau einem Antrag gehören
	 *
	 * @param antragID ID des zugehörigen Antrags
	 */
	public Berechnungszettel(int antragID) {
		this.setAntragId(antragID);

		this.brzId = -1;
		this.setKinder(0);
		this.setWeiterePersonen(0);

		schuldenkappungCheck = true;


		//6* Kosten + 4* Einkommen
		this.berechnungsWerteListe = new ArrayList<LabeledDecimalList>();


		//alle BigDecimal-Felder initialisieren ??

	}


	public int getBrzId() {
		return brzId;
	}

	public void setBrzId(int brzId) {
		this.brzId = brzId;
	}

	public int getAntragId() {
		return antragId;
	}

	public void setAntragId(int antragId) {
		this.antragId = antragId;
	}

	public int getKinder() {
		return kinder;
	}

	public void setKinder(int kinder) {
		this.kinder = kinder;
	}

	public int getWeiterePersonen() {
		return weiterePersonen;
	}

	public void setWeiterePersonen(int weiterePersonen) {
		this.weiterePersonen = weiterePersonen;
	}

	public boolean isHeizkostenPauschaleCheck() {
		return heizkostenPauschaleCheck;
	}

	public void setHeizkostenPauschaleCheck(boolean heizkostenPauschaleCheck) {
		this.heizkostenPauschaleCheck = heizkostenPauschaleCheck;
	}

	public boolean isAuslandskostenCheck() {
		return auslandskostenCheck;
	}

	public void setAuslandskostenCheck(boolean auslandskostenCheck) {
		this.auslandskostenCheck = auslandskostenCheck;
	}

	public boolean isAußerhalbABCCheck() {
		return außerhalbABCCheck;
	}

	public void setAußerhalbABCCheck(boolean außerhalbABCCheck) {
		this.außerhalbABCCheck = außerhalbABCCheck;
	}

	public String getBemerkung() {
		return bemerkung;
	}

	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}


	public List<LabeledDecimalList> getBerechnungsWerteListe() {
		return berechnungsWerteListe;
	}

	public void setBerechnungsWerteListe(List<LabeledDecimalList> berechnungsWerteListe) {
		this.berechnungsWerteListe = berechnungsWerteListe;
	}

	public boolean isSchuldenkappungCheck() {
		return schuldenkappungCheck;
	}

	public void setSchuldenkappungCheck(boolean schuldenkappungCheck) {
		this.schuldenkappungCheck = schuldenkappungCheck;
	}



	public boolean isKeineMietkappung() {
		return keineMietkappung;
	}

	public void setKeineMietkappung(boolean keineMietkappung) {
		this.keineMietkappung = keineMietkappung;
	}
}
