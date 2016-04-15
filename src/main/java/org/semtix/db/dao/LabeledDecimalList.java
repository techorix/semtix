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

package org.semtix.db.dao;

import org.hibernate.annotations.Cascade;
import org.semtix.shared.daten.enums.BerechnungsZettelCFTypen;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasse speichert ein BigDecimal, eine Liste von BigDecimal und einen Bezeichner für beides.
 */

@Entity
public class LabeledDecimalList {

	@Id
	@SequenceGenerator(name = "decimalListID_seq",
			sequenceName = "decimalListID_seq",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "decimalListID_seq")
	private long id;

	private String label;
	private int brzId;
	@Enumerated(EnumType.ORDINAL)
	private BerechnungsZettelCFTypen typ;
	private int divisor;
	private BigDecimal value;

	@ElementCollection(fetch = FetchType.EAGER)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private List<BigDecimal> valueList;


    public LabeledDecimalList() {

        this.label = "";

        value = new BigDecimal("0.0");

		valueList = new ArrayList<BigDecimal>();

		divisor = 6;

	}

	public int getBrzId() {
		return brzId;
	}

	public void setBrzId(int brzId) {
		this.brzId = brzId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLabel() {
			return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}


	/**
	 *
	 * @return Oftmals der Summenwert der sich aus einer Berechnung über die valueList ergibt
	 */
	public BigDecimal getValue() {
		if (null == value)
			return BigDecimal.ZERO;
		else
			return value;
	}


	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public List<BigDecimal> getValueList() {
		return valueList;
	}

	public void setValueList(List<BigDecimal> valueList) {
		this.valueList = valueList;
	}


	/**
	 *
	 * @return the sum of the list values may be divided afterwards by this divisor (mind checking for 0 value!)
	 */
	public int getDivisor() {
		return divisor;
	}

	public void setDivisor(int divisor) {
		this.divisor = divisor;
	}

	public BerechnungsZettelCFTypen getTyp() {
		return typ;
	}

	public void setTyp(BerechnungsZettelCFTypen typ) {
		this.typ = typ;
	}
}
