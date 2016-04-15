/*

  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
         Semesterticketbüro der Humboldt-Universität Berlin

  Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package org.semtix.config;

import org.semtix.db.DBHandlerConf;
import org.semtix.db.DBHandlerSemester;
import org.semtix.db.dao.Semester;
import org.semtix.shared.daten.enums.Uni;
import org.semtix.shared.daten.enums.UserSemesterStatus;

/**
 * Klasse enthält globale Einstellungswerte, die für die gesamte Anwendung benötigt werden.
 * 
 */
public class SemesterConf {

	
	// Status des aktuell gesetzten Semesters für HU (lokal oder global)
	public static UserSemesterStatus semesterStatusHU;
	
	
	// Status des aktuell gesetzten Semesters für KW (lokal oder global)
	public static UserSemesterStatus semesterStatusKW;
	
	
	// aktuell gesetztes Semester für HU (lokal)
	public static Semester semesterLokalHU;
	
	
	// aktuell gesetztes Semester für KW (lokal)
	public static Semester semesterLokalKW;
	
	
	// aktuell gesetztes Semester für HU (global)
	public static Semester semesterGlobalHU;
	
	
	// aktuell gesetztes Semester für KW (global)
	public static Semester semesterGlobalKW;


    /**
     * Initialisiert die eingestellten Semester des Programms derart, dass
     * immer mit dem aktuellen GLOBALEN Semester gestartet wird und der Benutzer
     * manuell ein lokales Semester einstellen muss
     *
     * (damit man kollektiv ein globales Semester einstellen kann und nur für Rückfragen usw. auch ein anderes Semester einstellbar ist)
     *
     */
	public static void init() {

				
		// Semesterstatus für HU und KW auf global setzen
		semesterStatusHU = UserSemesterStatus.GLOBAL;
		semesterStatusKW = UserSemesterStatus.GLOBAL;
				
		DBHandlerConf dbHandlerConf = new DBHandlerConf();
		DBHandlerSemester dbHandlerSemester = new DBHandlerSemester();
				
		// globale Semester aus der DB in die static-Variablen schreiben
		if(null != dbHandlerConf.read("semester_global_hu"))
			semesterGlobalHU = dbHandlerSemester.readSemester(Integer.parseInt(dbHandlerConf.read("semester_global_hu")));
		if(null != dbHandlerConf.read("semester_global_kw"))
			semesterGlobalKW = dbHandlerSemester.readSemester(Integer.parseInt(dbHandlerConf.read("semester_global_kw")));
		
		// lokale Semester aus der DB in die static-Variablen schreiben
		if(null != UserConf.CURRENT_USER) {
			semesterLokalHU = dbHandlerSemester.readSemester(UserConf.CURRENT_USER.getSemLokalHU());
			semesterLokalKW = dbHandlerSemester.readSemester(UserConf.CURRENT_USER.getSemLokalKW());			
		}

        if (null == semesterLokalHU) {
            semesterLokalHU = new Semester();
            semesterLokalHU.setUni(Uni.HU);
        }

        if (null == semesterLokalKW) {
            semesterLokalKW = new Semester();
            semesterLokalKW.setUni(Uni.KW);
        }


        if (null == semesterGlobalHU) {
            semesterGlobalHU = new Semester();
            semesterGlobalHU.setUni(Uni.HU);
        }

        if (null == semesterGlobalKW) {
            semesterGlobalKW = new Semester();
            semesterGlobalKW.setUni(Uni.KW);
        }
    }


    public static UserSemesterStatus getSemesterStatus() {

        UserSemesterStatus status = semesterStatusHU;

		
		if (UniConf.aktuelleUni.name().equals("KW"))
			status = semesterStatusKW;		
		
		return status;
		
	}
	
	
	
	public static Semester getSemester() {

		Semester semester;

		if (UniConf.aktuelleUni.name().equals("KW")) {
			if (semesterStatusKW == UserSemesterStatus.LOKAL)
				semester = semesterLokalKW;
			else
				semester = semesterGlobalKW;			
		}
		else {
			if (semesterStatusHU == UserSemesterStatus.LOKAL)
				semester = semesterLokalHU;
			else
				semester = semesterGlobalHU;
		}

		return semester;
	}

	
	
	// Aktuelles Semester (global) für HU oder KW in DB schreiben
	public static void setGlobalSemester(Semester semester) {
		
		
		if(semester.getUni() == Uni.KW) {
			semesterGlobalKW = semester;
		}
	
		else {
			semesterGlobalHU = semester;
		}


		String key = (semester.getUni() == Uni.KW ? "semester_global_kw" : "semester_global_hu");
		
		String value = Integer.toString(semester.getSemesterID());
		
		DBHandlerConf dbHandlerConf = new DBHandlerConf();
		dbHandlerConf.update(key, value);

	}
	

	
	
}
