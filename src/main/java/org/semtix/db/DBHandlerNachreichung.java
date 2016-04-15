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
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.semtix.config.SemesterConf;
import org.semtix.config.UserConf;
import org.semtix.db.dao.Nachreichung;
import org.semtix.db.hibernate.HibernateCRUD;
import org.semtix.db.hibernate.HibernateUtil;
import org.semtix.gui.tabs.antrag.nachreichungen.pruefen.NachreichungArt;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * Nachreichung
 *
 * Created by MM on 26.02.15.
 */
public class DBHandlerNachreichung {

	private HibernateCRUD<Nachreichung> dbhandler;

	public DBHandlerNachreichung() {

		dbhandler = new HibernateCRUD<Nachreichung>(Nachreichung.class);

	}

	/**
	 * Legte eine neue Nachreichung mit den nötigen Werten in der DB an
	 *
	 * @param nachreichungArt enum NachreichungArt
	 * @param antragID        AntragID
	 */
	public void create(NachreichungArt nachreichungArt, int antragID) {
		Nachreichung nachreichung = new Nachreichung();

		nachreichung.setAntragID(antragID);
		nachreichung.setNachreichungArt(nachreichungArt);
		nachreichung.setUserEingang(UserConf.CURRENT_USER.getUserID());
		nachreichung.setStatusChecked(false);
		nachreichung.setTimestampEingang(new GregorianCalendar());

		dbhandler.create(nachreichung);

	}

	public void update(Nachreichung n) {
		dbhandler.update(n);
	}


	public Integer getUncheckedNachreichungenCountForAntrag(int antragID) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Criteria crit = session.createCriteria(Nachreichung.class);
		crit.add(Restrictions.eq("antragID", antragID));
		crit.add(Restrictions.eq("statusChecked", false));
		crit.setProjection(Projections.rowCount());

		Long result = (Long) crit.uniqueResult();

		session.close();

		return result.intValue();
	}

	public List<Nachreichung> getUncheckedNachreichungenForAntrag(int antragID) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Criteria crit = session.createCriteria(Nachreichung.class);
		crit.add(Restrictions.eq("antragID", antragID));
		crit.add(Restrictions.eq("statusChecked", false));

		List<Nachreichung> result = crit.list();

		session.close();

		return result;
	}

	public List<Nachreichung> getNachreichungenForAntrag(int antragID) {
		return dbhandler.readList("antragid", "" + antragID);
	}

	public Integer getNachreichungenPruefen(int personID) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Long count = (Long) session.createQuery("select count(*) from Nachreichung n, Antrag a where " +
				"n.antragID = a.antragID and " +
				"n.statusChecked = false and " +
				"a.personID = " + personID + " and " +
				"a.semesterID = " + SemesterConf.getSemester().getSemesterID())
				.uniqueResult();

		session.close();
		return count.intValue();
	}


	public void delete(Nachreichung n) {
		dbhandler.delete(n);
	}
}
