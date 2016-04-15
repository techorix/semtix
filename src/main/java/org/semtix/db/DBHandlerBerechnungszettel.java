/*

  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
         Semesterticketbüro der Humboldt-Universität Berlin

  Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package org.semtix.db;

import org.semtix.db.dao.Berechnungszettel;
import org.semtix.db.hibernate.HibernateCRUD;

/**
 * DBHandler für DAO Berechnungszettel
 *
 * {see org.semtix.db.dao.Berechnungszettel}
 *
 * Created by MM on 27.04.15.
 */
public class DBHandlerBerechnungszettel {


    private HibernateCRUD<Berechnungszettel> dbhandler;

    public DBHandlerBerechnungszettel() {

        this.dbhandler = new HibernateCRUD<Berechnungszettel>(Berechnungszettel.class);

    }


    public void saveOrUpdateBRZ(Berechnungszettel brz) {

        dbhandler.saveOrUpdate(brz);

    }


    public Berechnungszettel getBRZ(int id) {

        return dbhandler.getByID(id);

    }

    public Berechnungszettel getBRZByAntragID(int antragID) {

        return dbhandler.read("antragID", "" + antragID);
    }

    public void deleteByAntragId(int antragID) {

        Berechnungszettel brz = getBRZByAntragID(antragID);

		if (null != brz) {
			delete(brz);
		}
	}

    public void create(Berechnungszettel berechnungszettel) {
        dbhandler.create(berechnungszettel);
    }

    public void delete(Berechnungszettel berechnungszettel) {
        dbhandler.delete(berechnungszettel);
	}
}
