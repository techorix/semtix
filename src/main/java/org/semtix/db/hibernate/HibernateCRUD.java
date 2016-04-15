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

package org.semtix.db.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Klasse mit grundlegenden Datenbankbefehlen auf Hibernate Sessions (Create, Read, Update, Delete usw.)
 *
 * Created by MM on 02.02.15.
 */
public class HibernateCRUD<T> {

    private Session session;

    private Class<T> typeParameterClass;

    public HibernateCRUD(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }

    public void create (T t) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(t);
        session.getTransaction().commit();
        session.close();
    }

    public T getByID(int id) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        T object = (T) session.get(typeParameterClass,id);
        session.getTransaction().commit();
        session.close();
        return object;
    }

    public Transaction getTransaction() {
        session = HibernateUtil.getSessionFactory().openSession();
        return session.beginTransaction();
    }

    public T read (String parameter, String value) {
        session = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        T t = null;
        try {
            tx = session.beginTransaction();

            t = (T) session.createQuery("from " + typeParameterClass.getName() + " where " + parameter +"='"+value+"'")
                    .uniqueResult();

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                try {
                    // Second try catch as the rollback could fail as well
                    tx.rollback();
                } catch (HibernateException e1) {
                    e.printStackTrace();
                }
                // throw again the first exception
                throw e;
            } else {
                session.close();
                return null;
            }


        }

        session.close();
        return t;
    }

    public List<T> readList (String parameter, String value) {
        session = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        List<T> t = null;
        Object result = null;
        try {
            tx = session.beginTransaction();

            t = session.createQuery("from " + typeParameterClass.getName() + " where " + parameter +"='"+value+"'").list();

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                try {
                    // Second try catch as the rollback could fail as well
                    tx.rollback();
                } catch (HibernateException e1) {
                    e.printStackTrace();
                }
                // throw again the first exception
                throw e;
            }


        }
        session.close();
        return t;
    }

    public List<T> readList(String parameter, String value, String parameter2, String value2, String orderedBy, String orderedBy2) {
        session = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        List<T> t = null;
        Object result = null;
        try {
            tx = session.beginTransaction();

            t = session.createQuery("from " + typeParameterClass.getName() + " where " + parameter + "='" + value + "' AND " + parameter2 + "='" + value2 + "' order by " + orderedBy + " asc, " + orderedBy2 + " asc").list();

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                try {
                    // Second try catch as the rollback could fail as well
                    tx.rollback();
                } catch (HibernateException e1) {
                    e.printStackTrace();
                }
                // throw again the first exception
                throw e;
            }


        }
        session.close();
        return t;
    }

    public void update (T t) {
        session = HibernateUtil.getSessionFactory().openSession();


        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(t);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                try {
                    // Second try catch as the rollback could fail as well
                    tx.rollback();
                } catch (HibernateException e1) {
//		          logger.debug(“Error rolling back transaction”);
                }
                // throw again the first exception
                throw e;
            }
        }
        session.close();

    }

    //zur Illustration, dass update equals saveOrUpdate
    public void saveOrUpdate(T t) {
        update(t);
    }

    public void delete(T t) {
        session = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(t);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                try {
                    // Second try catch as the rollback could fail as well
                    tx.rollback();
                } catch (HibernateException e1) {
//		          logger.debug(“Error rolling back transaction”);
                }
                // throw again the first exception
                throw e;
            }
        }
        session.close();

    }

    public List<T> getListOfAllElements() {
        session = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        List bausteine = null;
        try {
            tx = session.beginTransaction();


            //vorher select h from Text... as h
            bausteine = session.createQuery("select h from " + typeParameterClass.getName() + " as h ")
                    .list();

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                try {
                    // Second try catch as the rollback could fail as well
                    tx.rollback();
                } catch (HibernateException e1) {
                    e.printStackTrace();
                }
                // throw again the first exception
                throw e;
            }


        }
        session.close();

        return bausteine;
    }



}
