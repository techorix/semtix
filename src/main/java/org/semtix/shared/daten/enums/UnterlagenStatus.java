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

package org.semtix.shared.daten.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Konstante für den Status der fehlenden Unterlagen (vorhanden, nachgefordert, gemahnt, fehlt)
 */
public enum UnterlagenStatus {
	
	VORHANDEN (1, "vorhanden"),
    NACHGEFORDERT (2, "nachgefordert"),
	GEMAHNT (3, "gemahnt"),
    FEHLT (4, "fehlt");


	private static Map<Integer, UnterlagenStatus> idToUnterlagenStatusMapping;
	private int statusID;
	private String statusText;


	UnterlagenStatus(int statusID, String statusText) {
		
		this.statusID = statusID;
		this.statusText = statusText;

	}
	
	
	
	/**
	 * holt UnterlagenStatus anhand der statusID
	 * @param i ID des UnterlagenStatus
	 * @return UnterlagenStatus
	 */
	public static UnterlagenStatus getUnterlagenStatusByID(int i) {
		if(idToUnterlagenStatusMapping == null) {
			initMapping();
		}
		return idToUnterlagenStatusMapping.get(i);
	}
	
	
	// schreibt statusID und UnterlagenStatus in eine Map, damit auch über
	// die ID auf den UnterlagenStatus zugegriffen werden kann
	private static void initMapping() {
		idToUnterlagenStatusMapping = new HashMap<Integer, UnterlagenStatus>();
		for (UnterlagenStatus u : values()) {
			idToUnterlagenStatusMapping.put(u.statusID, u);
		}
	}
	
	
	public int getID() {
		return statusID;
	}
	
	
	public String getText() {
		return statusText;
	}

}
