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


package org.semtix.db;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.semtix.config.UniConf;
import org.semtix.config.UserConf;
import org.semtix.db.dao.*;
import org.semtix.db.hibernate.HibernateCRUD;
import org.semtix.db.hibernate.HibernateUtil;
import org.semtix.gui.filter.Filter;
import org.semtix.gui.tabs.antrag.AntragIndex;
import org.semtix.shared.daten.StringHelper;
import org.semtix.shared.daten.enums.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * DBHandler-Klasse zur Bearbeitung der Datenbank-Tabelle antrag.
 * Mithilfe dieser DBHandler-Klasse können in der Datenbank neue Anträge angelegt, bestehende Anträge
 * ausgelesen, geändert oder gelöscht werden.
 *
 * @see org.semtix.db.dao.Antrag
 */
public class DBHandlerAntrag {

	private HibernateCRUD<Antrag> dbhandler;


	/**
	 * Default Konstruktor
	 */
	public DBHandlerAntrag() {

		dbhandler = new HibernateCRUD<Antrag>(Antrag.class);

	}

	/**
	 * Importiert Daten aus Datei
	 *
	 * @param datenpfad Pfad zu Eingangsdaten
	 * @param separator Feldseparator der Eingangsdaten
	 * @throws IOException    bei Dateizugrifffehler
	 * @throws ParseException bei Fehler im Format der Datei
	 */
	public static void importFromFile(String datenpfad, String separator) throws IOException, ParseException {
		DBHandlerAntrag dbHandler = new DBHandlerAntrag();
		DBHandlerPerson dbHandlerPerson = new DBHandlerPerson();


		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(datenpfad), "windows-1252"));
		String line;
		while ((line = br.readLine()) != null) {
			String[] fields = line.split(separator);
			String matrikelnr = fields[0];

			int semesterId = Integer.parseInt(fields[1]);
			if (semesterId == 43) {
				semesterId = 1;
			} else if (semesterId == 40) {
				semesterId = 7;
			} else if (semesterId == 38) {
				semesterId = 8;
			}


			Person p = dbHandlerPerson.getPersonByMatrikelnummer(matrikelnr);
			boolean alreadyExists = false;
			for (Antrag antrag : dbHandler.getAntragListe(p.getPersonID())) {
				if (antrag.getSemesterID() == semesterId)
					alreadyExists = true;
			}

			if (!alreadyExists) {
				int userId = UserConf.CURRENT_USER.getUserID();

				Antrag a = new Antrag();

				a.setAntragStatus(AntragStatus.NICHTENTSCHIEDEN);
				a.setSemesterID(semesterId);
				a.setPersonID(p.getPersonID());
				a.setUserAngelegt(userId);
				a.setUserGeaendert(userId);
				if (!fields[6].isEmpty())
					a.setPunkteHaerte(Integer.parseInt(fields[6]));
				if (!fields[8].isEmpty())
					a.setPunkteEinkommen(Integer.parseInt(fields[8]));

				dbHandler.createAntrag(a);

			}
		}
	}

	public static void cleanUpAntraege() {
		List<Integer> personIds = new ArrayList<Integer>();
		for (Person p : new DBHandlerPerson().getPersonenListe()) {
			personIds.add(p.getPersonID());
		}
		DBHandlerAntrag dbHandlerAntrag = new DBHandlerAntrag();
		for (Antrag a : dbHandlerAntrag.getAntragListe()) {
			if (a.getPersonID() <= 0 || (!(personIds.contains(a.getPersonID())))) {
				dbHandlerAntrag.delete(a);
			}
		}
	}

	/**
	 * Erstellt eine Liste die Anträge und Antragsteller-Anfangsbuchstaben zum späteren durchblättern verknüpft
	 * {see org.semtix.gui.filter.Filter}
	 * <p>
	 * (Sollte gegebenenfalls in eine höhere Schicht z.B. Shared.Daten oder gui.Filter.Model transferiert werden, da
	 * auch für Personen. Ggf. sollten die einzelnen Filter in UnterMethoden unterteilt werden)
	 *
	 * @param filter Filter-Objekt mit Filter-Details
	 * @return Liste mit den entsprechenden Daten
	 */
	public List<AntragIndex> getAntragIndexListe(Filter filter) {

		String buchstaben = filter.getBuchstaben();

		List<AntragIndex> indexListe = new ArrayList<>();

		Session session = HibernateUtil.getSessionFactory().openSession();

		StringBuilder queryString = new StringBuilder("SELECT * FROM person where uni=" + (UniConf.aktuelleUni.getID() - 1));

		if (!filter.getArchiviert().equals(Status.EGAL)) {
			queryString.append(" and archiviert=" + filter.getArchiviert().equals(Status.JA));
		}

		if (!buchstaben.equals("alle")) {
			queryString.append(" and (upper(unaccent(nachname)) like '" + buchstaben.charAt(0) + "%'");
			for (int i = 1; i < buchstaben.length(); i++) {
				queryString.append(" or upper(unaccent(nachname)) like '" + buchstaben.charAt(i) + "%'");
			}
			queryString.append(")");
		}

		SQLQuery query = session.createSQLQuery(queryString.toString()).addEntity(Person.class);
		List<Object> persons = query.list();
		HashMap<Integer, String> idnachnamemap = new HashMap<>();
		for (Object o : persons) {
			Person p = (Person) o;
			idnachnamemap.put(p.getPersonID(), p.getNachname());
		}


		Criteria crit = session.createCriteria(Antrag.class);

		crit.add(Restrictions.in("personID", idnachnamemap.keySet()));

		if (filter.getSemesterID() > 0) {
			crit.add(Restrictions.eq("semesterID", filter.getSemesterID()));
		}

		if (!filter.getErstsemester().equals(Status.EGAL)) {
			crit.add(Restrictions.eq("erstsemester", filter.getErstsemester().equals(Status.JA)));
		}

		if (!filter.getKulanz().equals(Status.EGAL)) {
			crit.add(Restrictions.eq("kulanz", filter.getKulanz().equals(Status.JA)));
		}

		if (!filter.getNothilfe().equals(Status.EGAL)) {
			crit.add(Restrictions.eq("nothilfe", filter.getNothilfe().equals(Status.JA)));
		}

		if (!filter.getRatenzahlung().equals(Status.EGAL)) {
			crit.add(Restrictions.eq("raten", filter.getRatenzahlung().equals(Status.JA)));
		}

		if (!filter.getBarauszahler().equals(Status.EGAL)) {
			crit.add(Restrictions.eq("manAuszahlen", filter.getBarauszahler().equals(Status.JA)));
		}

		if (!filter.getTeilzuschuss().equals(Status.EGAL)) {
			crit.add(Restrictions.eq("teilzuschuss", filter.getTeilzuschuss().equals(Status.JA)));
		}

		FilterArt filterArt = filter.getFilterArt();
		if (null != filterArt) {
			switch (filterArt) {
				case ABGELEHNT:
					crit.add(Restrictions.eq("antragStatus", AntragStatus.ABGELEHNT));
					break;
				case ANGENOMMEN:
					crit.add(Restrictions.eq("antragStatus", AntragStatus.GENEHMIGT));
					break;
				case UNENTSCHIEDEN:
					crit.add(Restrictions.eq("antragStatus", AntragStatus.NICHTENTSCHIEDEN));
					break;
				default:
					//beides True
					break;
			}
		}

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Antrag> antraege = crit.list();


		session.close();


		GregorianCalendar today = new GregorianCalendar();

		for (Antrag a : antraege) {
			boolean adden = true;
			boolean adden2 = true;
			boolean adden3 = true;


			//das ist wichtig, damit die Methode auch ohne Filter funktioniert
			if (null != filterArt) {
				List<Vorgang> vorgaenge = null;
				List<Unterlagen> unterlagen = null;
				DBHandlerVorgaenge dbHandlerVorgaenge = new DBHandlerVorgaenge();
				DBHandlerUnterlagen dbHandlerUnterlagen = new DBHandlerUnterlagen();
				switch (filterArt) {

					//Ist Erstzurechnen oder noch abzuklären:
					// nicht entschieden AND NOT Unterlagen nachgefragt, fehlt gemahnt
					/* Bedeutet: Wenn der Antrag noch nicht entschieden ist UND gleichzeitig keine Unterlagen ausstehen, dann könnte er erstgerechnet werden */

					case ERSTRECHNEN:
						if (a.getAntragStatus().equals(AntragStatus.NICHTENTSCHIEDEN)) {

								vorgaenge = dbHandlerVorgaenge.getVorgaengeListe(a.getAntragID());
								unterlagen = dbHandlerUnterlagen.getUnterlagenListe(a.getAntragID());

							//schaut, ob schon erstgerechnet wurde:
							for (Vorgang v : vorgaenge) {
								if (v.getVorgangsart().equals(Vorgangsart.ERSTRECHNUNG)) {
									adden = false;
									break;
								}
							}

							//überprüft, ob es angeforderte oder gemahnte Unterlagen gibt:
							for (Unterlagen u : unterlagen) {
								if (!u.getUnterlagenStatus().equals(UnterlagenStatus.VORHANDEN)) {
									adden = false;
									break;
								}

							}
							//Entschiedene Anträge sind nicht von belang:
						} else {
							adden = false;
						}
						break;

					// "Erneut nachfragen oder mahnen"
					// nicht entschieden AND Unterlagen nachgefragt, fehlt, Nachreichungsfrist abgelaufen
					case MAHNEN:
						if (a.getAntragStatus().equals(AntragStatus.NICHTENTSCHIEDEN)) {
							adden = false;
							//überprüft, ob es angeforderte oder gemahnte Unterlagen gibt:


								unterlagen = dbHandlerUnterlagen.getUnterlagenListe(a.getAntragID());


							for (Unterlagen u : unterlagen) {
								if (u.getUnterlagenStatus().equals(UnterlagenStatus.NACHGEFORDERT)) {
									if (today.compareTo(u.getFristNachfrage()) > 0)
										adden = true;
								} else if (u.getUnterlagenStatus().equals(UnterlagenStatus.GEMAHNT)) {
									if (today.compareTo(u.getFristMahnung()) > 0)
										adden = true;
								}

							}
							//Entschiedene Anträge sind nicht von belang:
						} else {
							adden = false;
						}
						break;

					// "Zweitrechnen"
					// entschieden (genehmigt ODER abgelehnt) AND erstgerechnet AND NOT zweitgerechnet
					case ZWEITRECHNEN:
						if (a.getAntragStatus().equals(AntragStatus.GENEHMIGT) || a.getAntragStatus().equals(AntragStatus.ABGELEHNT)) {
							adden = false;

								vorgaenge = dbHandlerVorgaenge.getVorgaengeListe(a.getAntragID());


							for (Vorgang v : vorgaenge) {
								if (v.getVorgangsart().equals(Vorgangsart.ERSTRECHNUNG)) {
									adden = true;
									break;
								}
							}

							for (Vorgang v : vorgaenge) {
								if (v.getVorgangsart().equals(Vorgangsart.ZWEITRECHNUNG)) {
									adden = false;
									break;
								}
							}
						} else {
							adden = false;
						}
						break;

					// "Kulanz rechnen"
					// nicht entschieden AND nachgefragt, Unterlagen fehlen, Nachreichungsfrist abgelaufen,
					// gemahnt, Mahnfrist abgelaufen, Kulanz "nein" in den letzten 3 Semestern

//                                case KULANZRECHNEN:
//                                    if (a.getAntragStatus().equals(AntragStatus.NICHTENTSCHIEDEN) && !a.isKulanz()) {
//                                        adden = isHoechsteEisenBahn(unterlagen);
//                                    } else {
//                                        adden = false;
//                                    }
//                                    break;
					// "unvollständig rechnen"
					// nicht entschieden AND nachgefragt, Unterlagen fehlen, Nachreichungsfrist abgelaufen,
					case UNVOLLSTAENDIG:
						if (a.getAntragStatus().equals(AntragStatus.NICHTENTSCHIEDEN) && a.isKulanz()) {
							unterlagen = dbHandlerUnterlagen.getUnterlagenListe(a.getAntragID());

							adden = isHoechsteEisenBahn(unterlagen);
						} else {
							adden = false;
						}
						break;

					case ABGELEHNT:
						break;
					case ANGENOMMEN:
						break;
					case UNENTSCHIEDEN:
						break;
					case NUR_SONSTIGE:
						//beides True
						break;
					default:
						//in case its null?
						break;

				}
			}

			//wenn der Antrag den Hauptfilter erfüllt, dann sonstige Filter:
			if (adden && adden2) {


				DBHandlerNachreichung dbHandlerNachreichung = new DBHandlerNachreichung();

				if (filter.getNachreichung().equals(Status.JA)) {
					adden3 = adden3 && (dbHandlerNachreichung.getUncheckedNachreichungenCountForAntrag(a.getAntragID()) > 0);
				} else if (filter.getNachreichung().equals(Status.NEIN)) {
					adden3 = adden3 && (dbHandlerNachreichung.getUncheckedNachreichungenCountForAntrag(a.getAntragID()) == 0);
				}

				//Spätis, hier erstmal, sind alle genehmigten Anträge im eingestellten Semester, die noch nicht ausgezahlt wurden. Siehe auch DialogSpaetis.java
				//Die anderen Kriterien für Spätis werden ja durch die anderen Filter schon abgedeckt. Ratenzahler, Erstsemester, Nothilfe usw.
				if (filter.getSpaetis().equals(Status.JA))
					adden3 = adden3 && !a.isAuszahlung() && a.getAntragStatus().equals(AntragStatus.GENEHMIGT);
				else if (filter.getSpaetis().equals(Status.NEIN))
					adden3 = adden3 && a.isAuszahlung() && a.getAntragStatus().equals(AntragStatus.GENEHMIGT);

				if (adden3) {
					AntragIndex index = new AntragIndex(a.getAntragID(), getAnfangsBuchstabe(idnachnamemap.get(a.getPersonID())), a.getPersonID());
					indexListe.add(index);
				}
			}
		}


		return indexListe;
	}

	private char getAnfangsBuchstabe(String nachname) {
		return StringHelper.removeDiacriticalMarks(nachname.trim().toUpperCase()).charAt(0);
	}

	/**
	 * Kleine Hilfsmethode, die die Unterlagen durchgeht und schaut ob Unterlagen nachgefragt oder gemahnt wurden und
	 * ob
	 * dabei auch schon die Fristen abgelaufen sind
	 *
	 * @param unterlagen Liste der Unterlagen
	 * @return true wenn UnterlagenStatus.NACHGEFORDERT oder GEMAHNT und Nachfragefrist oder Mahnfrist abgelaufen
	 */
	private boolean isHoechsteEisenBahn(List<Unterlagen> unterlagen) {
		for (Unterlagen u : unterlagen) {
			if (u.getUnterlagenStatus().equals(UnterlagenStatus.NACHGEFORDERT)) {
				if (u.getFristNachfrage().compareTo(new GregorianCalendar()) <= 0)
					return true;
			}

			if (u.getUnterlagenStatus().equals(UnterlagenStatus.GEMAHNT)) {
				if (u.getFristMahnung().compareTo(new GregorianCalendar()) <= 0)
					return true;
			}
		}

		return false;
	}

	/**
	 * Neuen Antrag in Datenbank anlegen (CREATE).
	 *
	 * @param antrag Objekt mit Antragsdaten
	 */
	public void createAntrag(Antrag antrag) {
		if (0 == antrag.getUserAngelegt())
			antrag.setUserAngelegt(UserConf.CURRENT_USER.getUserID());
		antrag.setDatumAngelegt(new GregorianCalendar());
		if (0 == antrag.getUserGeaendert())
			antrag.setUserGeaendert(UserConf.CURRENT_USER.getUserID());
		antrag.setDatumGeaendert(new GregorianCalendar());

		dbhandler.create(antrag);
	}

	/**
	 * Antrag aus Datenbank lesen (SELECT)
	 *
	 * @param antragID ID des gewünschten antrags
	 * @return Objekt mit Antragsdaten
	 */
	public Antrag readAntrag(int antragID) {
		return dbhandler.getByID(antragID);
	}


	/**
	 * Antrag in Datenbank aktualisieren (UPDATE)
	 * <p>
	 * Sollte gute getestet werden, da eben auch die anhängigen AntragHaerten gespeichert werden sollten
	 *
	 * @param antrag Objekt mit Antragsdaten, welche aktualisiert werden sollen
	 */
	public void updateAntrag(Antrag antrag) {
		antrag.setUserGeaendert(UserConf.CURRENT_USER.getUserID());
		antrag.setDatumGeaendert(new GregorianCalendar());

		//TODO Datumsänderung kann man auch die DB automatisch machen lassen

		dbhandler.update(antrag);

	}

	/**
	 * Antrag aus Datenbank löschen (DELETE)
	 * Auch alle Antraghärten werden mitgelöscht
	 *
	 * @param antragID ID des Antrags, der gelöscht werden soll
	 */
	public void delete(int antragID) {

		delete(readAntrag(antragID));

	}

	public void deletePersonAntraege(int personID) {
		for (Antrag a : readAntragListe("personID", "" + personID)) {
			delete(a);
		}
	}


	/**
	 * Antrag aus Datenbank löschen (DELETE)
	 * <p>
	 * Dabei werden auch der dazugehörige BRZ, die Vörgänge und die Unterlagen gelöscht.
	 *
	 * @param a Antrag
	 */
	public void delete(Antrag a) {

		//TODO alle anderen Deletes hier durch bessere Hibernate-Modelierung ersetzen

		new DBHandlerBerechnungszettel().deleteByAntragId(a.getAntragID());

		DBHandlerUnterlagen dbHandlerUnterlagen = new DBHandlerUnterlagen();

		for (Unterlagen u : dbHandlerUnterlagen.getUnterlagenListe(a.getAntragID())) {
			dbHandlerUnterlagen.deleteUnterlagen(u);
		}

		DBHandlerVorgaenge dbHandlerVorgaenge = new DBHandlerVorgaenge();
		for (Vorgang v : dbHandlerVorgaenge.getVorgaengeListe(a.getAntragID())) {
			dbHandlerVorgaenge.delete(v);
		}

		DBHandlerNachreichung dbHandlerNachreichung = new DBHandlerNachreichung();
		for (Nachreichung n : dbHandlerNachreichung.getNachreichungenForAntrag(a.getAntragID())) {
			dbHandlerNachreichung.delete(n);
		}

		dbhandler.delete(a);

	}

	/**
	 * Liefert Liste mit allen Anträgen zurück
	 *
	 * @return Liste mit Anträgen
	 */
	public List<Antrag> getAntragListe() {

		return dbhandler.getListOfAllElements();

	}

	/**
	 * Macht eine Datenbankanfrage mit clause 'where key=value'
	 *
	 * @param key   Schlüssel nach dem gesucht wird (Datenbankspalte)
	 * @param value Wert in der Spalte des Schlüssels
	 * @return Liste die Key,Value matcht
	 */
	public List<Antrag> readAntragListe(String key, String value) {

		return dbhandler.readList(key, value);

	}

	/**
	 * Liefert Liste mit allen Anträgen für ein Semester zurück
	 *
	 * @param semesterID ID Semester
	 * @return Liste mit Anträgen
	 */
	public List<Antrag> getAntragListeSemester(int semesterID) {

		return dbhandler.readList("semesterid", "" + semesterID);

	}

	/**
	 * Liefert Liste mit allen Anträgen für eine bestimmte Uni
	 * zurück. Anträge haben selber kein Feld für die Universität,
	 * so dass etwas aufwendig über das Semester gesucht werden muss.
	 *
	 * @param uni Uni
	 * @return Liste mit Anträgen
	 */
	public List<Antrag> getAntragListeUni(Uni uni) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(Antrag.class);

		Property property = Property.forName("semesterID");
		List<Integer> ids = new ArrayList<>();
		for (Semester s : new DBHandlerSemester().getSemesterListe(uni)) {
			ids.add(s.getSemesterID());
		}
		crit.add(property.in(ids));

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<Antrag> result = crit.list();

		session.close();

		return result;

	}

	/**
	 * Holt von der Datenbank alle Datensätze aus Tabelle 'antrag' (Anträge)
	 * die zu einer bestimmten Person gehören in Form von AntragUebersicht-Objekten
	 *
	 * @param personID ID der Person
	 * @return Rückgabe einer ArrayList mit Antragsobjekten
	 */
	public List<Antrag> getAntragListe(int personID) {

		return dbhandler.readList("personid", "" + personID);

	}


	/**
	 * Schaut in allen Anträge einer bestimmten Person ob Ratenzahlung gegeben ist (gleichzeitig nicht entschieden)
	 *
	 * @param personID ID der Person
	 * @return boolean Ratenzahlung vorhanden (true) oder nicht (false)
	 */
	public boolean getAntragRatenzahlung(int personID) {

		for (Antrag a : readAntragListe("personid", "" + personID)) {
			if (a.isRaten())
				return true;
		}


		return false;


	}

	/**
	 * zählt Anträge für ein bestimmtes Semester und mit bestimmtem Antragstatus
	 *
	 * @param semester_id ID des gewünschten Semesters
	 * @return List of Object[] where Object[0] has type AntragStatus and Object[1] has type Long
	 */
	public List<Object[]> countAntraegeByStatus(int semester_id) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		String query = "select a.antragStatus, count(a.antragStatus) " +
				"from Antrag a where a.semesterID=" + semester_id + " group by a.antragStatus";

		List<Object[]> result = session.createQuery(query).list();

		session.close();

		return result;



	}

	/**
	 * Genehmigte Anträge die noch nicht ausgezahlt wurden
	 * @param semester Das betreffende Semester
	 * @return List der Anträge
	 */
	public List<Antrag> getListNochNichtAusgezahlt(Semester semester) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Criteria crit = session.createCriteria(Antrag.class);

		crit.add(Restrictions.eq("semesterID", semester.getSemesterID()));

		crit.add(Restrictions.eq("antragStatus", AntragStatus.GENEHMIGT));

		crit.add(Restrictions.eq("auszahlung", false));

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<Antrag> result = crit.list();

		session.close();

		return result;

	}

	public List<Antrag> getAntragListeEntschiedenSemesterFiltered(int semesterID) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Criteria crit = session.createCriteria(Antrag.class);

		crit.add(Restrictions.eq("semesterID", semesterID));

		crit.add(Restrictions.disjunction()
				.add(Restrictions.eq("antragStatus", AntragStatus.GENEHMIGT))
				.add(Restrictions.eq("antragStatus", AntragStatus.ABGELEHNT)));

		crit.add(Restrictions.eq("erstsemester", false));
		crit.add(Restrictions.eq("nothilfe", false));
		crit.add(Restrictions.eq("raten", false));

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<Antrag> result = crit.list();

		session.close();

		return result;
	}

	public List<Antrag> getAntragListeSemesterGenehmigt(int semesterID) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Criteria crit = session.createCriteria(Antrag.class);

		crit.add(Restrictions.eq("semesterID", semesterID));

		crit.add(Restrictions.eq("antragStatus", AntragStatus.GENEHMIGT));

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<Antrag> result = crit.list();

		session.close();

		return result;

	}
}
